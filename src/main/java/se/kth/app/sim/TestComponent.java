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
import se.kth.app.AppComp;
import se.kth.app.CORB.CBroadcast;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.network.test.Message;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.identifiable.Identifier;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.util.LinkedList;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class TestComponent extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(TestComponent.class);
    private String logPrefix = " ";

    //*******************************CONNECTIONS********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<Network> networkPort = requires(Network.class);


    //**************************************************************************
    private KAddress selfAdr, target;


    public TestComponent(Init init) {
        this.selfAdr = init.selAdr;
        this.target = init.target;
    }

    Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start start) {

            LinkedList linkedList = new LinkedList();
            CBroadcast cBroadcast = new CBroadcast(new SimpleEvent("The important event"));

            KHeader header = new BasicHeader(selfAdr, target, Transport.UDP);
            KContentMsg contentMsg = new BasicContentMsg(header, cBroadcast);

            trigger(contentMsg, networkPort);
        }
    };

    public static class Init extends se.sics.kompics.Init<TestComponent> {

        public final KAddress target;
        public final KAddress selAdr;

        public Init(KAddress selfAdr, KAddress target) {
            this.selAdr = selfAdr;
            this.target = target;

        }
    }
    {
        subscribe(startHandler, control);
    }
}
