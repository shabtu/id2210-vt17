package se.kth.app.Utility;

import se.sics.kompics.KompicsEvent;

import java.util.*;

/**
 * Created by tobiaj on 2017-05-22.
 */
public class OREvent implements KompicsEvent {

    private Object element;
    private UUID ID;
    private Set<UUID> set;

    public OREvent(Object element, UUID uuid){
        this.element = element;
        this.ID = uuid;
        this.set = new HashSet<>();

    }

    public Object getElement() {
        return element;
    }

    public void setElement(Object element) {
        this.element = element;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID IDs) {
        this.ID = IDs;
    }

    public Set<UUID> getSet() {
        return set;
    }

    public void setSet(Set<UUID> set) {
        this.set = set;
    }
}
