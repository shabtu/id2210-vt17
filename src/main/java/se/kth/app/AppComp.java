/*
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
package se.kth.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.CORB.CBroadcast;
import se.kth.app.CORB.CORBDeliver;
import se.kth.app.CORB.CORBPort;
import se.kth.app.CRDT.*;
import se.kth.app.Utility.*;
import se.kth.app.sim.SimpleEvent;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;

import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.croupier.CroupierPort;

import se.sics.ktoolbox.util.identifiable.Identifier;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;

import java.util.UUID;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class AppComp extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(AppComp.class);
    private String logPrefix = " ";

    //*******************************CONNECTIONS********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<Network> networkPort = requires(Network.class);
    Positive<CroupierPort> croupierPort = requires(CroupierPort.class);
    Positive<CORBPort> corbPortPositive = requires(CORBPort.class);

    //**************************************************************************
    private KAddress selfAdr;
    private SuperSet dataSet;

    public AppComp(Init init) {
        selfAdr = init.selfAdr;
        logPrefix = "<nid:" + selfAdr.getId() + ">";
        LOG.info("{}initiating...", logPrefix);

        Object typeOfSuperClass = null;
        try {
            typeOfSuperClass = Class.forName(init.superSet).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        dataSet =  (SuperSet) typeOfSuperClass;

        System.out.println("Dataset is" + dataSet.getClass() + "   " + typeOfSuperClass.getClass());


        subscribe(handleStart, control);
        subscribe(handleDeliver, corbPortPositive);
        subscribe(simpleEventHandler, networkPort);
        subscribe(addEventHandler, networkPort);
        subscribe(removeEventHandler, networkPort);
    }

    Handler handleStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            LOG.info("{}starting...", logPrefix);

        }
    };

    Handler handleDeliver = new Handler<CORBDeliver> (){

        @Override
        public void handle(CORBDeliver corbDeliver) {

            //System.out.println("Getting event " + corbDeliver.getEvent().getEvent());

            if (corbDeliver.getEvent().getEvent() instanceof SimpleEvent){

                SimpleEvent simpleEvent = (SimpleEvent) corbDeliver.getEvent().getEvent();

                LOG.info("{} received {} from {} ", logPrefix, simpleEvent.getTextMessage(), corbDeliver.getEvent().getSelfAdr());

            }
            else if (corbDeliver.getEvent().getEvent() instanceof AddEvent){

                AddEvent addEvent = (AddEvent) corbDeliver.getEvent().getEvent();
                handleAddEvent(addEvent);

                //LOG.info("{} received {} from {} ", logPrefix, addEvent.getObject(), corbDeliver.getEvent().getSelfAdr());

            }

            else if (corbDeliver.getEvent().getEvent() instanceof RemoveEvent){

                RemoveEvent removeEvent = (RemoveEvent) corbDeliver.getEvent().getEvent();
                handleRemoveEvent(removeEvent);
                //LOG.info("{} received {} from {} ", logPrefix, removeEvent.getObject(), corbDeliver.getEvent().getSelfAdr());

            }

            else{
                LOG.info("{} received something not matching ", logPrefix);

            }


        }
    };

    ClassMatchedHandler<CBroadcast, KContentMsg<?, ?, CBroadcast>> simpleEventHandler = new ClassMatchedHandler<CBroadcast, KContentMsg<?, ?, CBroadcast>>() {
        @Override
        public void handle(CBroadcast cBroadcast, KContentMsg kContentMsg) {
            //LOG.info("SENDING " + cBroadcast.getEvent().getEvent());

            trigger(cBroadcast, corbPortPositive);
        }
    };

    ClassMatchedHandler<AddEvent, KContentMsg<?, ?, AddEvent>> addEventHandler = new ClassMatchedHandler<AddEvent, KContentMsg<?, ?, AddEvent>>() {
        @Override
        public void handle(AddEvent addEvent, KContentMsg kContentMsg) {

            addEvent = handleAddEvent(addEvent);

            DeliverEvent deliverEvent = new DeliverEvent(addEvent, selfAdr);

            CBroadcast cBroadcast = new CBroadcast(deliverEvent);


            trigger(cBroadcast, corbPortPositive);
        }
    };

    ClassMatchedHandler<RemoveEvent, KContentMsg<?, ?, RemoveEvent>> removeEventHandler = new ClassMatchedHandler<RemoveEvent, KContentMsg<?, ?, RemoveEvent>>() {
        @Override
        public void handle(RemoveEvent removeEvent, KContentMsg kContentMsg) {

            LOG.info("REMOVE EVENT " + removeEvent.getObject());
            removeEvent = handleRemoveEvent(removeEvent);

            DeliverEvent deliverEvent = new DeliverEvent(removeEvent, selfAdr);

            CBroadcast cBroadcast = new CBroadcast(deliverEvent);


            trigger(cBroadcast, corbPortPositive);
        }
    };


    private RemoveEvent handleRemoveEvent(RemoveEvent removeEvent) {
        String results = "";

        LOG.info(logPrefix + " got remove event " + removeEvent.getObject().toString());
        if (dataSet instanceof TwoPSet){
            ((TwoPSet)dataSet).remove(removeEvent.getObject());

            results = ((TwoPSet) dataSet).printStore();
            printTomb();

        }
        else if(dataSet instanceof ORSet){

            OREvent orEvent = (OREvent) removeEvent.getObject();

            dataSet = ((ORSet) dataSet);

            LOG.info(logPrefix +  " got element " + orEvent.getElement() + " uuid " + orEvent.getID() + " list " + orEvent.getSet() + " size " + orEvent.getSet().size() + " empty " + orEvent.getSet().isEmpty());
            LOG.info(logPrefix + " my dataset contains before remove " + ((ORSet) dataSet).printORSet());

            if (orEvent.getSet().isEmpty()) {

                if (((ORSet) dataSet).queryLookup((orEvent))){
                    orEvent = (OREvent) ((ORSet) dataSet).updateRemove(orEvent);

                }

            }
            else{

                ((ORSet) dataSet).updateRemoveDownstream(orEvent);

            }

            results = ((ORSet) dataSet).printORSet();


        }

        else if(dataSet instanceof TwoPTwoPGraph){

            System.out.println("remove event i 2p2pgraph " + removeEvent.getObject());

            if (removeEvent.getObject() instanceof VertexEvent){

                removeEvent = handelVertexRemoveEvent(removeEvent);
                results = ((TwoPTwoPGraph) dataSet).print2P2PVerteciesStore();


            }else if (removeEvent.getObject() instanceof EdgeEvent){

                System.out.println("hit då`??");
                removeEvent = handleEdgeRemoveEvent(removeEvent);
                results = ((TwoPTwoPGraph) dataSet).print2P2PEdgesStore();


            }
            else{
                LOG.info(logPrefix + " got add event with no matching");
            }

        }
        else {

        }

        LOG.info(logPrefix + " my dataset contains after remove \n" + results);

        return removeEvent;

    }

    private void printTomb() {

        LOG.info(logPrefix + " my tombstone contains \n" + ((TwoPSet)dataSet).printTombStone());
    }


    private AddEvent handleAddEvent(AddEvent addEvent) {
        LOG.info(logPrefix + " got add event " + addEvent.getObject().toString());

        String results = "";

        if (dataSet instanceof GSet) {
            ((GSet)dataSet).addObject(addEvent.getObject());
            results = dataSet.printStore();


        }
        else if (dataSet instanceof TwoPSet){
            ((TwoPSet) dataSet).add(addEvent.getObject());
            results = ((TwoPSet) dataSet).printStore();
            printTomb();
        }
        else if (dataSet instanceof ORSet){

            OREvent orEvent = (OREvent) addEvent.getObject();

            LOG.info(logPrefix +  " got element " + orEvent.getElement() + " uuid " + orEvent.getID());
            if (orEvent.getID() == null) {
                UUID uuid = ((ORSet) dataSet).createID();
                orEvent.setID(uuid);

                ((ORSet) dataSet).updateAdd(orEvent);
            }
            else{
                ((ORSet) dataSet).updateAdd(orEvent);

            }

            results = ((ORSet) dataSet).printORSet();

        }
        else if(dataSet instanceof TwoPTwoPGraph){

            if (addEvent.getObject() instanceof VertexEvent){

                addEvent = handelVertexAddEvent(addEvent);
                results = ((TwoPTwoPGraph) dataSet).print2P2PVerteciesStore();


            }else if (addEvent.getObject() instanceof EdgeEvent){

                addEvent = handleEdgeAddEvent(addEvent);
                results = ((TwoPTwoPGraph) dataSet).print2P2PEdgesStore();


            }
            else{
                LOG.info(logPrefix + " got add event with no matching");
            }

        }
        else{

            LOG.info("No matching class");
        }


        LOG.info(logPrefix + " my dataset contains after add \n" + results);

        return addEvent;
    }

    private AddEvent handleEdgeAddEvent(AddEvent addEvent) {

        EdgeEvent edgeEvent = (EdgeEvent) addEvent.getObject();

        dataSet = (TwoPTwoPGraph) dataSet;

        if (edgeEvent.isSource()){
            edgeEvent.setSource(false);

            Edge edge = ((TwoPTwoPGraph) dataSet).addEdgeByVerteciesID(edgeEvent.getVertexA(), edgeEvent.getVertexB());

            edgeEvent.setEdge(edge);

        }
        else{

            Edge edge = edgeEvent.getEdge();

            ((TwoPTwoPGraph) dataSet).addEdge(edge);
        }


        return addEvent;
    }

    private AddEvent handelVertexAddEvent(AddEvent addEvent) {

        VertexEvent vertexEvent = (VertexEvent) addEvent.getObject();

        dataSet = (TwoPTwoPGraph) dataSet;

        if (vertexEvent.isSource()){
            vertexEvent.setSource(false);

            int id = vertexEvent.getId();

            Vertex vertex = ((TwoPTwoPGraph) dataSet).addVertexByID(id);

            vertexEvent.setVertex(vertex);

        }
        else{

            ((TwoPTwoPGraph) dataSet).addVertex(vertexEvent.getVertex());

        }

        return addEvent;

    }


    private RemoveEvent handelVertexRemoveEvent(RemoveEvent removeEvent) {

        VertexEvent vertexEvent = (VertexEvent) removeEvent.getObject();
        dataSet = (TwoPTwoPGraph) dataSet;

        if (vertexEvent.isSource()){
            vertexEvent.setSource(false);

            Vertex vertex = ((TwoPTwoPGraph) dataSet).removeVertexByID(vertexEvent.getId());
            vertexEvent.setVertex(vertex);
        }
        else{

            ((TwoPTwoPGraph) dataSet).removeVertex(vertexEvent.getVertex());
        }
        return removeEvent;
    }

    private RemoveEvent handleEdgeRemoveEvent(RemoveEvent removeEvent) {

        EdgeEvent edgeEvent = (EdgeEvent) removeEvent.getObject();
        dataSet = (TwoPTwoPGraph) dataSet;
        System.out.println("komer jag hit");

        if (edgeEvent.isSource()){
            edgeEvent.setSource(false);

            System.out.println("kommer jag hit");

            Edge edge = ((TwoPTwoPGraph) dataSet).removeEdgeByID(edgeEvent.getVertexA(), edgeEvent.getVertexB());

            edgeEvent.setEdge(edge);
        }

        else{

            Edge edge = edgeEvent.getEdge();

            ((TwoPTwoPGraph) dataSet).removeEdge(edge);
        }

        return removeEvent;
    }


    public static class Init extends se.sics.kompics.Init<AppComp> {

        public final KAddress selfAdr;
        public final Identifier gradientOId;
        public final String superSet;

        public Init(KAddress selfAdr, Identifier gradientOId, String superSet) {
            this.selfAdr = selfAdr;
            this.gradientOId = gradientOId;
            this.superSet = superSet;
        }
    }
}
