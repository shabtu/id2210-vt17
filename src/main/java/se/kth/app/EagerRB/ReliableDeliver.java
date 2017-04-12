package se.kth.app.EagerRB;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class ReliableDeliver implements KompicsEvent, Serializable {

    public KAddress getkAddress() {
        return kAddress;
    }

    private KAddress kAddress;

    public KompicsEvent getEvent() {
        return event;
    }

    private KompicsEvent event;

    private LinkedList list;

    public ReliableDeliver(KAddress kAddress, KompicsEvent event, LinkedList list){
        this.kAddress = kAddress;
        this.event = event;
        this.list = list;
    }

    public LinkedList getList() {
        return list;
    }
}
