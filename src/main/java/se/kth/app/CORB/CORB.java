package se.kth.app.CORB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.EagerRB.EagerRB;
import se.kth.app.EagerRB.EagerRBPort;
import se.kth.app.EagerRB.ReliableBroadcast;
import se.kth.app.EagerRB.ReliableDeliver;
import se.kth.app.GBEB.DeliverEvent;
import se.kth.app.GBEB.GBEB;
import se.kth.app.GBEB.GBEBPort;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class CORB extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(CORB.class);
    private String logPrefix = " ";

    //*******************************CONNECTIONS********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<Network> networkPort = requires(Network.class);

    Negative<CORBPort> corbPort = provides(CORBPort.class);

    Positive<EagerRBPort> eagerRBPort = requires(EagerRBPort.class);

    //**************************************************************************
    private KAddress selfAdr;

    private Set delivered;
    private LinkedList past;


    public CORB(Init init){
        selfAdr = init.selfAdr;


        subscribe(handler, control);
        subscribe(cBroadcastHandler, corbPort);
        subscribe(reliableDeliverHandler, eagerRBPort);

    }

    Handler<Start> handler = new Handler<Start>() {
        @Override
        public void handle(Start start) {
            delivered = new HashSet();
            past = new LinkedList();

        }
    };


    protected Handler<CBroadcast> cBroadcastHandler = new Handler<CBroadcast>() {
        @Override
        public void handle(CBroadcast cBroadcast) {
            ReliableBroadcast reliableBroadcast = new ReliableBroadcast(past, cBroadcast);
            CORBDeliver corbDeliver = new CORBDeliver(selfAdr, cBroadcast);
            trigger(reliableBroadcast, eagerRBPort);
            past.add(corbDeliver);

        }
    };

    protected Handler<ReliableDeliver> reliableDeliverHandler = new Handler<ReliableDeliver>() {
        @Override
        public void handle(ReliableDeliver reliableDeliver) {
            if (!delivered.contains(reliableDeliver.getEvent())){
                LinkedList<DeliverEvent> list = reliableDeliver.getList();
                for (DeliverEvent deliverEvent : list){
                    KompicsEvent event = deliverEvent.getEvent();
                    if (!delivered.contains(event)){
                        CORBDeliver corbDeliver = new CORBDeliver(deliverEvent.getkAddress(), deliverEvent.getEvent());
                        trigger(corbDeliver, corbPort);
                        delivered.add(event);
                        if (!past.contains(deliverEvent)){
                            past.add(deliverEvent);
                        }
                    }
                }
                CORBDeliver corbDeliver = new CORBDeliver(reliableDeliver.getkAddress(), reliableDeliver.getEvent());

                trigger(corbDeliver, corbPort);
                delivered.add(reliableDeliver.getEvent());

                if (!past.contains(reliableDeliver)){
                    past.add(reliableDeliver);
                }

            }

        }
    };

    public static class Init extends se.sics.kompics.Init<CORB> {

        public final KAddress selfAdr;

        public Init(KAddress selfAdr) {
            this.selfAdr = selfAdr;
        }
    }

}
