package se.kth.app.CORB;

import se.kth.app.Utility.DeliverEvent;
import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class CORBDeliver extends DeliverEvent{


    public CORBDeliver(KAddress kAddress, KompicsEvent event){
        super(kAddress, event);

    }


    public KAddress getkAddress() {
        return super.getkAddress();
    }


    public KompicsEvent getEvent() {
        return super.getEvent();
    }

}
