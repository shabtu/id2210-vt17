package se.kth.app.EagerRB;

import se.kth.app.CORB.CBroadcast;
import se.sics.kompics.KompicsEvent;
import sun.awt.image.ImageWatched;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class ReliableBroadcast implements KompicsEvent, Serializable {


    private KompicsEvent event;
    private LinkedList list;

    public ReliableBroadcast(KompicsEvent event, LinkedList list) {
        this.event = event;
        this.list = list;

    }

    public KompicsEvent getEvent() {
        return event;
    }

    public LinkedList getList() {
        return list;
    }
}
