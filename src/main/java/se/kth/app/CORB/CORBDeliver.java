package se.kth.app.CORB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class CORBDeliver extends DeliverEvent{

    public KAddress getkAddress() {
        return kAddress;
    }

    private KAddress kAddress;

    public KompicsEvent getEvent() {
        return event;
    }

    private KompicsEvent event;

    public CORBDeliver(KAddress kAddress, KompicsEvent event){
        super(kAddress, event);

    }
}
