package se.kth.app.CORB;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class CORBDeliver implements KompicsEvent, Serializable{

    public KAddress getkAddress() {
        return kAddress;
    }

    private KAddress kAddress;

    public KompicsEvent getEvent() {
        return event;
    }

    private KompicsEvent event;

    public CORBDeliver(KAddress kAddress, KompicsEvent event){
        this.kAddress = kAddress;
        this.event = event;

    }
}
