package se.kth.app.GBEB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class GBEBBroadcast implements KompicsEvent, Serializable {

    private DeliverEvent event;

    private Set<DeliverEvent> list;


    public GBEBBroadcast(DeliverEvent event) {
        this.event = event;
    }

    public DeliverEvent getEvent() {
        return event;
    }

    public void setEvent(DeliverEvent event) {
        this.event = event;
    }

    public Set<DeliverEvent> getList() {
        return list;
    }

    public void setList(Set<DeliverEvent> list) {
        this.list = list;
    }
}
