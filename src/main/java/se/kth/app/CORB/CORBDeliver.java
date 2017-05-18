package se.kth.app.CORB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.KompicsEvent;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class CORBDeliver implements KompicsEvent{


    private DeliverEvent event;
    private Set<DeliverEvent> list;

    public CORBDeliver(DeliverEvent event){
        this.event = event;
    }

    public DeliverEvent getEvent() {
        return this.event;
    }

    public void setList(Set<DeliverEvent> list) {
        this.list = list;
    }
}
