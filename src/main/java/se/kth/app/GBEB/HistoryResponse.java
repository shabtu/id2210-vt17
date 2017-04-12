package se.kth.app.GBEB;

import se.sics.kompics.KompicsEvent;

import java.util.Set;

/**
 * Created by tobiaj on 2017-04-11.
 */
public class HistoryResponse implements KompicsEvent {

    private Set<DeliverEvent> pasts;

    public HistoryResponse(Set<DeliverEvent> pasts) {
        this.pasts = pasts;
    }

    public Set<DeliverEvent> getPasts() {
        return pasts;
    }
}
