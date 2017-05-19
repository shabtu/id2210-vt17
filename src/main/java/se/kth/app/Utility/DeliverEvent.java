package se.kth.app.Utility;

import se.kth.app.sim.SimpleEvent;
import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class DeliverEvent implements KompicsEvent, Serializable {

    private KAddress selfAdr;

    private KompicsEvent event;

    private Set<DeliverEvent> list;

    public void setList(Set<DeliverEvent> list) {
        this.list = list;
    }

    public Set<DeliverEvent> getList() {
        return this.list;
    }


    public KompicsEvent getEvent() {
        return event;
    }

    public DeliverEvent(KompicsEvent event, KAddress address) {
        this.event = event;
        this.selfAdr = address;
    }

    public KAddress getSelfAdr() {
        return selfAdr;
    }

    public void setSelfAdr(KAddress selfAdr) {
        this.selfAdr = selfAdr;
    }
}
