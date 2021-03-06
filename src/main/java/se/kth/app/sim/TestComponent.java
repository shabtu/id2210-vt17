package se.kth.app.sim;/*
 * 2016 Royal Institute of Technology (KTH)
 *
 * LSelector is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.CORB.CBroadcast;
import se.kth.app.Utility.AddEvent;
import se.kth.app.Utility.DeliverEvent;
import se.kth.app.Utility.RemoveEvent;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class TestComponent extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(TestComponent.class);
    private String logPrefix = " ";
    private int test;

    //*******************************CONNECTIONS********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<Network> networkPort = requires(Network.class);


    //**************************************************************************
    private KAddress selfAdr, target;


    public TestComponent(Init init) {
        this.selfAdr = init.selAdr;
        this.target = init.target;
        this.test = init.test;
    }

    Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start start) {

            sendSimpleEvent();
            
            //sendGSet();
            //sendTwoPSet();

            //sendRemoveEvent();


        }
        
    };

    private void sendRemoveEvent() {

        RemoveEvent removeEvent = new RemoveEvent("remove");

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, removeEvent);
        trigger(contentMsg, networkPort);
    }

    private void sendGSet() {

        for (int i = 0; i < 3; i++) {
            AddEvent addEvent = new AddEvent("Object "+ i);


            KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
            KContentMsg contentMsg = new BasicContentMsg(header, addEvent);
            trigger(contentMsg, networkPort);
        }

    }

    private void sendTwoPSet(){


        for (int i = 2; i < 4; i++) {
            RemoveEvent removeEvent = new RemoveEvent("Object "+ i);


            KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
            KContentMsg contentMsg = new BasicContentMsg(header, removeEvent);
            trigger(contentMsg, networkPort);

        }

        AddEvent addEvent = new AddEvent("Object 2");

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, addEvent);
        trigger(contentMsg, networkPort);
    }
    private void sendSimpleEvent() {

        for (int i = 0; i < 3; i++) {

            CBroadcast cBroadcast = new CBroadcast(new DeliverEvent(new SimpleEvent("The important event" + i), selfAdr));

            KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
            KContentMsg contentMsg = new BasicContentMsg(header, cBroadcast);
            trigger(contentMsg, networkPort);
        }
    }

    public static class Init extends se.sics.kompics.Init<TestComponent> {

        public final KAddress target;
        public final KAddress selAdr;
        public final int test;

        public Init(KAddress selfAdr, KAddress target, Integer integer) {
            this.selAdr = selfAdr;
            this.target = target;
            this.test = integer;

        }
    }

    {
        subscribe(startHandler, control);
    }
}
