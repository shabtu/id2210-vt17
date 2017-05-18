package se.kth.app.EagerRB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class ReliableDeliver implements KompicsEvent {

    private DeliverEvent event;
    private Set<DeliverEvent> list;

    public ReliableDeliver(DeliverEvent event){
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
