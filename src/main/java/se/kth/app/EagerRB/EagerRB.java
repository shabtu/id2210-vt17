package se.kth.app.EagerRB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.CORB.CBroadcast;
import se.kth.app.GBEB.GBEBBroadcast;
import se.kth.app.GBEB.GBEBDeliver;
import se.kth.app.GBEB.GBEBPort;
import se.kth.app.Utility.DeliverEvent;
import se.kth.app.sim.SimpleEvent;
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
        logPrefix = "<nid:" + selfAdr + ">";


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

            GBEBBroadcast gbebBroadcast = new GBEBBroadcast(reliableBroadcast.getEvent());
            gbebBroadcast.setList(reliableBroadcast.getList());

            //System.out.println("IN eager the list is " + reliableBroadcast.getList());
            trigger(gbebBroadcast, gbebPort);
        }
    };

    protected Handler<GBEBDeliver> bebDeliver = new Handler<GBEBDeliver>() {
        @Override
        public void handle(GBEBDeliver gbebDeliver) {
            KompicsEvent event = gbebDeliver.getEvent();
            //System.out.println("IN EAGER UP " + gbebDeliver.getList());
            if (!delivered.contains(event)){

                delivered.add(event);

                ReliableDeliver reliableDeliver = new ReliableDeliver(gbebDeliver.getEvent());
                reliableDeliver.setList(gbebDeliver.getList());


                GBEBBroadcast gbebBroadcast = new GBEBBroadcast(gbebDeliver.getEvent());
                gbebBroadcast.setList(gbebDeliver.getList());

                trigger(reliableDeliver, eagerPort);
                trigger(gbebBroadcast, gbebPort);

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
