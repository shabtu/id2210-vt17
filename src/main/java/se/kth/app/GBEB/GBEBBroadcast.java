package se.kth.app.GBEB;

import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class GBEBBroadcast implements KompicsEvent, Serializable {

    private KompicsEvent event;

    public GBEBBroadcast(KompicsEvent event) {
        this.event = event;
    }

    public KompicsEvent getEvent() {
        return event;
    }


    public void setEvent(KompicsEvent event) {
        this.event = event;
    }

}
