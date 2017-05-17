package se.kth.app.EagerRB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class ReliableDeliver extends DeliverEvent {


    private KAddress kAddress;
    private KompicsEvent event;

    private LinkedList list;

    public ReliableDeliver(KAddress kAddress, KompicsEvent event, LinkedList list){
        super(kAddress, event);
        this.list = list;
    }

    public LinkedList getList() {
        return list;
    }
    public KompicsEvent getEvent() {
        return event;
    }
    public KAddress getkAddress() {
        return kAddress;
    }


}
