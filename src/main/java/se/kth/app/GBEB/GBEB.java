package se.kth.app.GBEB;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.CORB.CBroadcast;
import se.kth.app.EagerRB.ReliableBroadcast;
import se.kth.app.Utility.DeliverEvent;
import se.kth.app.sim.SimpleEvent;
import se.kth.croupier.util.CroupierHelper;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.croupier.CroupierPort;
import se.sics.ktoolbox.croupier.event.CroupierSample;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.util.Arrays;
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

    Negative<GBEBPort> GBEBPort = provides(GBEBPort.class);
    Positive<CroupierPort> croupierPort = requires(CroupierPort.class);


    //**************************************************************************
    private KAddress selfAdr;

    private Set<DeliverEvent> pasts;


    public GBEB(Init init){
        selfAdr = init.selfAdr;
        logPrefix = "<nid:" + selfAdr + ">";

        subscribe(handler, control);
        subscribe(broadcastEventHandler, GBEBPort);
        subscribe(croupierSampleHandler, croupierPort);
        subscribe(historyRequest, networkPort);
        subscribe(historyResponse, networkPort);


    }

    Handler<Start> handler = new Handler<Start>() {
        @Override
        public void handle(Start start) {
            pasts = new HashSet<>();
        }
    };


    protected Handler<GBEBBroadcast> broadcastEventHandler = new Handler<GBEBBroadcast>() {
        @Override
        public void handle(GBEBBroadcast gbebBroadcast) {

            GBEBDeliver gbebDeliver = new GBEBDeliver(selfAdr, gbebBroadcast.getEvent());

            pasts.add(gbebDeliver);

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

    ClassMatchedHandler historyRequest = new ClassMatchedHandler<HistoryRequest, KContentMsg<?, ?, HistoryRequest>>() {

        @Override
        public void handle(HistoryRequest historyRequest, KContentMsg kContentMsg) {

            LOG.info("I am " + selfAdr + " and will send a history response to " + kContentMsg.getHeader().getSource() + " with pasts " + pasts.size());
            trigger(kContentMsg.answer(new HistoryResponse(pasts)), networkPort);

        }
    };

    ClassMatchedHandler historyResponse = new ClassMatchedHandler<HistoryResponse, KContentMsg<?, ?, HistoryResponse>>() {

        @Override
        public void handle(HistoryResponse historyResponse, KContentMsg kContentMsg) {
            //LOG.info("I am " + selfAdr + " and got a history response from " + kContentMsg.getHeader().getSource() + " and response is " + historyResponse.getPasts().size());

            Set<DeliverEvent> response = historyResponse.getPasts();

            Set<DeliverEvent> unseen = Sets.difference(response, pasts);




            for (DeliverEvent deliverEvent : unseen) {
                LOG.info(" I AM " + selfAdr +" sending " + deliverEvent.getEvent());
                trigger(deliverEvent, GBEBPort);
            }

            pasts.addAll(unseen);



        }
    };

    public static class Init extends se.sics.kompics.Init<GBEB> {

        public final KAddress selfAdr;

        public Init(KAddress selfAdr) {
            this.selfAdr = selfAdr;
        }
    }


}
