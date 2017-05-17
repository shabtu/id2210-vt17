package se.kth.app.CORB;

import se.sics.kompics.KompicsEvent;
import sun.jvm.hotspot.runtime.linux_arm.LinuxARMJavaThreadPDAccess;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class CBroadcast implements KompicsEvent, Serializable{

    private KompicsEvent event;

    public CBroadcast(KompicsEvent event) {
        this.event = event;

    }

    public KompicsEvent getEvent() {
        return event;
    }


    public void setEvent(KompicsEvent event) {
        this.event = event;
    }

}