package se.kth.app.sim;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * Created by habtu on 2017-04-19.
 */
public class SimpleEvent implements KompicsEvent {

    private String textMessage;

    public SimpleEvent(String textMessage){
        this.textMessage = textMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }

}
