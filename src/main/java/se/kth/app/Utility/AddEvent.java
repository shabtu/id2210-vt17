package se.kth.app.Utility;

import se.sics.kompics.KompicsEvent;

/**
 * Created by tobiaj on 2017-05-18.
 */
public class AddEvent implements KompicsEvent {

    private Object object;

    public AddEvent(Object object){
        this.object = object;

    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
