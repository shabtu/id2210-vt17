package se.kth.app.EagerRB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class ReliableBroadcast implements KompicsEvent, Serializable {


    private Set<DeliverEvent> list;
    private DeliverEvent event;

    public ReliableBroadcast(DeliverEvent event) {
        this.event = event;

    }

    public DeliverEvent getEvent() {
        return this.event;
    }

    public Set<DeliverEvent> getList() {
        return list;
    }

    public void setList(Set<DeliverEvent> list) {
        this.list = list;
    }
}
