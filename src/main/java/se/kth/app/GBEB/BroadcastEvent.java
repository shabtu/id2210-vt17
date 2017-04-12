package se.kth.app.GBEB;

import se.sics.kompics.KompicsEvent;

import java.io.Serializable;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class BroadcastEvent implements KompicsEvent, Serializable {

    private KompicsEvent event;
    public BroadcastEvent(KompicsEvent event) {
        this.event = event;

    }

    public KompicsEvent getEvent() {
        return event;
    }
}
