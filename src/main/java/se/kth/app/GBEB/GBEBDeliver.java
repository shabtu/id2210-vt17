package se.kth.app.GBEB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * Created by tobiaj on 2017-05-16.
 */
public class GBEBDeliver extends DeliverEvent {


    private KAddress kAddress;
    private KompicsEvent event;


    public GBEBDeliver(KAddress kAddress, KompicsEvent event) {
        super(kAddress, event);
    }

    @Override
    public KAddress getkAddress() {
        return kAddress;
    }

    @Override
    public KompicsEvent getEvent() {
        return event;
    }
}
