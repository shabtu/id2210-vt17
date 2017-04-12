package se.kth.app.CORB;

import se.sics.kompics.PortType;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class CORBPort extends PortType {

    {
        indication(CBroadcast.class);
    }
}
