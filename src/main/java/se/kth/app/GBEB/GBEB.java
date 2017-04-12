package se.kth.app.GBEB;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.test.Ping;
import se.kth.croupier.util.CroupierHelper;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.network.test.Message;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.croupier.CroupierPort;
import se.sics.ktoolbox.croupier.event.CroupierSample;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class GBEB extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(GBEB.class);
    private String logPrefix = " ";

    //*******************************CONNECTIONS********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<Network> networkPort = requires(Network.class);

    Positive<GBEBPort> GBEBPort = requires(GBEBPort.class);
    Positive<CroupierPort> croupierPort = requires(CroupierPort.class);


    //**************************************************************************
    private KAddress selfAdr;
    private KAddress bootstrapServer;

    private Set<DeliverEvent> pasts;


    public GBEB(Init init){
        selfAdr = init.selfAdr;
        bootstrapServer = init.bootstrapServer;

        subscribe(handler, control);
        subscribe(initGBEBHandler, GBEBPort);
        subscribe(broadcastEventHandler, GBEBPort);
        subscribe(croupierSampleHandler, networkPort);
        subscribe(historyRequest, networkPort);
        subscribe(historyResponse, networkPort);


    }

    Handler<Start> handler = new Handler<Start>() {
        @Override
        public void handle(Start start) {
            pasts = new HashSet<>();
        }
    };


    protected Handler<InitGBEB> initGBEBHandler = new Handler<InitGBEB>() {
        @Override
        public void handle(InitGBEB initGBEB) {
            LOG.info("SHIIIIIIIIIIET!");
        }
    };

    protected Handler<BroadcastEvent> broadcastEventHandler = new Handler<BroadcastEvent>() {
        @Override
        public void handle(BroadcastEvent broadcastEvent) {
            DeliverEvent deliverEvent = new DeliverEvent(selfAdr, broadcastEvent.getEvent());
            pasts.add(deliverEvent);
        }
    };

    protected Handler<CroupierSample> croupierSampleHandler = new Handler<CroupierSample>() {

        @Override
        public void handle(CroupierSample croupierSample) {

            if (croupierSample.publicSample.isEmpty()) {
                return;
            }

            List<KAddress> list = CroupierHelper.getSample(croupierSample);
            for (KAddress address : list){
                KHeader header = new BasicHeader(selfAdr, address, Transport.UDP);
                KContentMsg msg = new BasicContentMsg(header, new HistoryRequest());
                trigger(msg, networkPort);
            }
        }
    };

    ClassMatchedHandler historyRequest = new ClassMatchedHandler<HistoryRequest, KContentMsg>() {

        @Override
        public void handle(HistoryRequest historyRequest, KContentMsg kContentMsg) {

            kContentMsg.answer(new HistoryResponse(pasts));

        }
    };

    ClassMatchedHandler historyResponse = new ClassMatchedHandler<HistoryResponse, KContentMsg>() {

        @Override
        public void handle(HistoryResponse historyResponse, KContentMsg kContentMsg) {
            Set<DeliverEvent> response = historyResponse.getPasts();

            Set<DeliverEvent> unseen = Sets.symmetricDifference(pasts, response);

            for (DeliverEvent past : unseen) {
                trigger(past, GBEBPort);
            }

            pasts.addAll(unseen);



        }
    };

    public static class Init extends se.sics.kompics.Init<GBEB> {

        public final KAddress selfAdr;
        public final KAddress bootstrapServer;

        public Init(KAddress selfAdr, KAddress bootstrapServer) {
            this.selfAdr = selfAdr;
            this.bootstrapServer = bootstrapServer;
        }
    }


}
