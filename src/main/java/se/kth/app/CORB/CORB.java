package se.kth.app.CORB;

import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.EagerRB.EagerRBPort;
import se.kth.app.EagerRB.ReliableBroadcast;
import se.kth.app.EagerRB.ReliableDeliver;
import se.kth.app.Utility.DeliverEvent;
import se.kth.app.sim.SimpleEvent;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;
import java.util.LinkedList;
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

    private Set<KompicsEvent> delivered;
    private Set<DeliverEvent> past;


    public CORB(Init init) {
        selfAdr = init.selfAdr;
        logPrefix = "<nid:" + selfAdr + ">";


        subscribe(handler, control);
        subscribe(cBroadcastHandler, corbPort);
        subscribe(reliableDeliverHandler, eagerRBPort);

    }

    Handler<Start> handler = new Handler<Start>() {
        @Override
        public void handle(Start start) {
            delivered = new HashSet();
            past = new HashSet<>();

        }
    };


    protected Handler<CBroadcast> cBroadcastHandler = new Handler<CBroadcast>() {
        @Override
        public void handle(CBroadcast cBroadcast) {
            //LOG.info("EWGWEHEWHEWHEWH" + cBroadcast + " event is " + cBroadcast.getEvent());

            ReliableBroadcast reliableBroadcast = new ReliableBroadcast(new DeliverEvent(cBroadcast.getEvent().getEvent(), selfAdr));
            reliableBroadcast.setList(past);

            //System.out.println("IN CORB the list is " + reliableBroadcast.getList());


            trigger(reliableBroadcast, eagerRBPort);

            past.add(cBroadcast.getEvent());

        }
    };

    protected Handler<ReliableDeliver> reliableDeliverHandler = new Handler<ReliableDeliver>() {
        @Override
        public void handle(ReliableDeliver reliableDeliver) {

            //System.out.println(logPrefix + " IN CORB UP " + reliableDeliver.getList());
            //System.out.println(logPrefix + " CORP DELIVERED " + delivered);
            if (!delivered.contains(reliableDeliver.getEvent().getEvent())) {
                Set<DeliverEvent> list = reliableDeliver.getEvent().getList();
                for (DeliverEvent deliverEvent : list) {
                    KompicsEvent event = deliverEvent.getEvent();
                    //System.out.println("THE EVENT IS " + event + "the delvier event is " + deliverEvent + " reliable event is " + reliableDeliver.getEvent().getEvent());

                    if (!delivered.contains(event)) {
                        CORBDeliver corbDeliver = new CORBDeliver(deliverEvent);

                        trigger(corbDeliver, corbPort);
                        delivered.add(event);
                        if (!past.contains(deliverEvent)) {
                            past.add(deliverEvent);
                        }
                    }
                }

                //Is this part okey, it will remove the double
                if (!delivered.contains(reliableDeliver.getEvent().getEvent())) {

                    CORBDeliver corbDeliver = new CORBDeliver(reliableDeliver.getEvent());
                    corbDeliver.setList(reliableDeliver.getList());

                    trigger(corbDeliver, corbPort);
                    delivered.add(reliableDeliver.getEvent().getEvent());

                    if (!past.contains(reliableDeliver)) {
                        past.add(reliableDeliver.getEvent());
                    }

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
