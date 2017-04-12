package se.kth.app.EagerRB;

import com.sun.org.apache.regexp.internal.RE;
import se.kth.app.CORB.CBroadcast;
import se.sics.kompics.PortType;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class EagerRBPort extends PortType {

    {
        request(ReliableBroadcast.class);
        indication(ReliableDeliver.class);
    }

}
