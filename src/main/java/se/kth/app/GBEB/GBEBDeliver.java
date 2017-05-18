package se.kth.app.GBEB;

import se.kth.app.Utility.DeliverEvent;
import se.kth.app.sim.SimpleEvent;
import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.Set;

/**
 * Created by tobiaj on 2017-05-16.
 */
public class GBEBDeliver implements KompicsEvent {


    private DeliverEvent event;
    private Set<DeliverEvent> list;

    public GBEBDeliver(DeliverEvent event) {
        this.event = event;
    }

    public DeliverEvent getEvent() {
        return this.event;
    }
}
