package se.kth.app.CORB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class CBroadcast implements KompicsEvent, Serializable{

    private DeliverEvent event;

    private Set<KompicsEvent> list;

    public CBroadcast(DeliverEvent event) {
        this.event = event;

    }

    public DeliverEvent getEvent() {
        return event;
    }


    public void setEvent(DeliverEvent event) {
        this.event = event;
    }

    public Set<KompicsEvent> getList() {
        return list;
    }

    public void setList(Set<KompicsEvent> list) {
        this.list = list;
    }
}