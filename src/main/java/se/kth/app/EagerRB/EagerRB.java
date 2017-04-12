package se.kth.app.EagerRB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.GBEB.BroadcastEvent;
import se.kth.app.GBEB.DeliverEvent;
import se.kth.app.GBEB.GBEB;
import se.kth.app.GBEB.GBEBPort;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class EagerRB extends ComponentDefinition {


    private static final Logger LOG = LoggerFactory.getLogger(EagerRB.class);
    private String logPrefix = " ";

    //*******************************CONNECTIONS********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<Network> networkPort = requires(Network.class);

    Negative<EagerRBPort> eagerPort = provides(EagerRBPort.class);

    Positive<GBEBPort> gbebPort = requires(GBEBPort.class);


    //**************************************************************************
    private KAddress selfAdr;

    private Set delivered;


    public EagerRB(EagerRB.Init init){
        selfAdr = init.selfAdr;

        subscribe(handler, control);
        subscribe(reliableBroadcastHandler, eagerPort);
        subscribe(bebDeliver, gbebPort);


    }

    Handler<Start> handler = new Handler<Start>() {
        @Override
        public void handle(Start start) {
            delivered = new HashSet();
        }
    };

    protected Handler<ReliableBroadcast> reliableBroadcastHandler = new Handler<ReliableBroadcast>() {
        @Override
        public void handle(ReliableBroadcast reliableBroadcast) {

            trigger(new BroadcastEvent(reliableBroadcast), gbebPort);
        }
    };

    protected Handler<DeliverEvent> bebDeliver = new Handler<DeliverEvent>() {
        @Override
        public void handle(DeliverEvent deliverEvent) {
            KompicsEvent event = deliverEvent.getEvent();
            if (!delivered.contains(event)){
                delivered.add(event);
                ReliableBroadcast reliableBroadcast = (ReliableBroadcast) deliverEvent.getEvent();
                ReliableDeliver reliableDeliver = new ReliableDeliver(deliverEvent.getkAddress(), deliverEvent.getEvent(), reliableBroadcast.getList());
                BroadcastEvent broadcastEvent = new BroadcastEvent(deliverEvent);

                trigger(reliableDeliver, eagerPort);
                trigger(broadcastEvent, gbebPort);

            }
        }
    };




    public static class Init extends se.sics.kompics.Init<EagerRB> {

        public final KAddress selfAdr;

        public Init(KAddress selfAdr) {
            this.selfAdr = selfAdr;
        }
    }

}
