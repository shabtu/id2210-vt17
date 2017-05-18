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

    private Set<DeliverEvent> delivered;
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
            LOG.info("EWGWEHEWHEWHEWH" + cBroadcast + " event is " + cBroadcast.getEvent());

            ReliableBroadcast reliableBroadcast = new ReliableBroadcast(new DeliverEvent(new SimpleEvent("Test"), selfAdr));
            reliableBroadcast.setList(past);


            trigger(reliableBroadcast, eagerRBPort);

            past.add(cBroadcast.getEvent());

        }
    };

    protected Handler<ReliableDeliver> reliableDeliverHandler = new Handler<ReliableDeliver>() {
        @Override
        public void handle(ReliableDeliver reliableDeliver) {
            if (!delivered.contains(reliableDeliver.getEvent())) {
                //LinkedList<DeliverEvent> list = reliableDeliver.getList();
                for (DeliverEvent event : past) {
                    //KompicsEvent event = deliverEvent.getEvent();
                    if (!delivered.contains(event)) {
                        CORBDeliver corbDeliver = new CORBDeliver(event);

                        trigger(corbDeliver, corbPort);
                        delivered.add(event);
                        if (!past.contains(event)) {
                            past.add(event);
                        }
                    }
                }

                CORBDeliver corbDeliver = new CORBDeliver(reliableDeliver.getEvent());

                trigger(corbDeliver, corbPort);
                delivered.add(reliableDeliver.getEvent());

                if (!past.contains(reliableDeliver)) {
                    past.add(reliableDeliver.getEvent());
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
