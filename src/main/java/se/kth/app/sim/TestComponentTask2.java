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

import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.CORB.CBroadcast;
import se.kth.app.Utility.*;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class TestComponentTask2 extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(TestComponent.class);
    private String logPrefix = " ";
    private int test;

    //*******************************CONNECTIONS********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<Network> networkPort = requires(Network.class);


    //**************************************************************************
    private KAddress selfAdr, target;


    public TestComponentTask2(Init init) {
        this.selfAdr = init.selAdr;
        this.target = init.target;
        this.test = init.test;
    }

    Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start start) {
            //sendSimpleEvent();
            //sendRemoveEvent();


            /**Test for TWOPSET**/
            //testTwoPSet();

            /**Test for GSET**/
            sendGSet();


            /**Test for ORSET**/
            //testORSet();

            /**Test for 2P2PGraph**/
            //testGraph();

        }

    };

    private void testGraph() {

        switch (test) {
            case 1:
                sendAddVertex();
                break;
            case 2:
                sendEdge();
                break;
            case 3:
                removeVertex();
                break;
            case 4:
                //removeEdge();
                sendEdge2();
                break;

                default:
                    System.out.println("Nothing found");
        }
    }

    private void sendEdge2() {

        EdgeEvent edgeEvent = new EdgeEvent(true, 1, 2);

        AddEvent addEvent = new AddEvent(edgeEvent);

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, addEvent);
        trigger(contentMsg, networkPort);

    }

    private void removeEdge() {
        EdgeEvent edgeEvent = new EdgeEvent(true, 0, 1);

        RemoveEvent removeEvent = new RemoveEvent(edgeEvent);

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, removeEvent);
        trigger(contentMsg, networkPort);
    }

    private void removeVertex() {

        VertexEvent vertexEvent = new VertexEvent(true, 1);

        RemoveEvent removeEvent = new RemoveEvent(vertexEvent);

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, removeEvent);
        trigger(contentMsg, networkPort);

    }

    private void sendEdge() {

        EdgeEvent edgeEvent = new EdgeEvent(true, 0, 1);

        AddEvent addEvent = new AddEvent(edgeEvent);

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, addEvent);
        trigger(contentMsg, networkPort);

    }

    private void sendAddVertex() {


        for (int i = 0; i < 3; i++) {

            VertexEvent vertexEvent = new VertexEvent(true, i);

            AddEvent addEvent = new AddEvent(vertexEvent);

            KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
            KContentMsg contentMsg = new BasicContentMsg(header, addEvent);
            trigger(contentMsg, networkPort);

        }
    }

    private void testORSet() {
        switch (test) {
            case 1:
                sendORSet();
                break;
            case 2:
                sendORRemoveSet();
                break;
            case 3:
                sendORAdd();
                break;
            case 4:
                sendORRemove();
                break;
                default:
                    System.out.println("no matching");
        }
    }

    private void sendORRemove() {
        OREvent orEvent = new OREvent("Apa0", null);

        RemoveEvent removeEvent = new RemoveEvent(orEvent);

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, removeEvent);

        trigger(contentMsg, networkPort);

    }


    private void sendORAdd() {
        OREvent orEvent = new OREvent("Apa0", null);

        AddEvent addEvent = new AddEvent(orEvent);

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, addEvent);

        trigger(contentMsg, networkPort);
    }


    private void testTwoPSet(){
        switch (test) {
            case 1:
                sendGSet();
                break;
            case 2:
                sendTwoPSet();
                break;
        }
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

    private void sendORSet(){

        OREvent orEvent = new OREvent("Apa0", null);

        AddEvent addEvent = new AddEvent(orEvent);

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, addEvent);

        trigger(contentMsg, networkPort);

        for (int i = 0; i < 3; i++) {

        orEvent = new OREvent("Apa" + i, null);

        addEvent = new AddEvent(orEvent);

        header = new BasicHeader(selfAdr, target, Transport.UDP);
        contentMsg = new BasicContentMsg(header, addEvent);

            trigger(contentMsg, networkPort);

        }
    }

    private void sendORRemoveSet() {

        OREvent orEvent = new OREvent("Apa0", null);

        RemoveEvent removeEvent = new RemoveEvent(orEvent);

        KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
        KContentMsg contentMsg = new BasicContentMsg(header, removeEvent);

        trigger(contentMsg, networkPort);

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
