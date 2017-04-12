package se.kth.app.GBEB;

import se.sics.kompics.PortType;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class GBEBPort extends PortType {

    {
        request(BroadcastEvent.class);
        indication(DeliverEvent.class);
    }
}
