package se.kth.app.GBEB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.PortType;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class GBEBPort extends PortType {

    {
        request(GBEBBroadcast.class);
        indication(GBEBDeliver.class);
    }
}
