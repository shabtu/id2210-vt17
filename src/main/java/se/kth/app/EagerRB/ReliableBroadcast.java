package se.kth.app.EagerRB;

import se.sics.kompics.KompicsEvent;

import java.io.Serializable;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class ReliableBroadcast implements KompicsEvent, Serializable {


    private KompicsEvent event;
    public ReliableBroadcast(KompicsEvent event) {
        this.event = event;

    }

    public KompicsEvent getEvent() {
        return event;
    }
}
