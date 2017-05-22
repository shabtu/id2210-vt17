package se.kth.app.CRDT;

import com.google.common.collect.Sets;
import se.kth.app.Utility.OREvent;
import se.kth.app.Utility.RemoveEvent;

import java.util.*;

/**
 * Created by tobiaj on 2017-05-22.
 */
public class ORSet extends SuperSet {

    public HashMap<Object, Set<UUID>> dataSet;

    public ORSet(){

        this.dataSet = new HashMap<>();
    }


    public void updateAdd(Object element){

        OREvent orEvent = (OREvent) element;
        Object tempElement = orEvent.getElement();
        UUID tempUUID = orEvent.getID();
        Set<UUID> tempList;

        Set<UUID> temp = dataSet.get(tempElement);

        if (temp != null){

            tempList = dataSet.get(tempElement);

            tempList.add(tempUUID);
        }
        else {
            tempList = new HashSet<>();
            tempList.add(tempUUID);

        }

        dataSet.put(tempElement, tempList);


    }

    public Object updateRemove(Object element){

        OREvent orEvent = (OREvent) element;
        Object tempElement = orEvent.getElement();
        Set<UUID> tempList;

        if (queryLookup(element)) {

            tempList = dataSet.get(tempElement);
            orEvent.setSet(tempList);

        }

        return element;
    }

    public boolean queryLookup(Object element){

        OREvent orEvent = (OREvent) element;

        if (dataSet.containsKey(orEvent.getElement()))
            return true;
        else
            return false;


    }

    public UUID createID(){

        return UUID.randomUUID();
    }

    public String printORSet(){
        StringBuilder stringBuilder = new StringBuilder();


        for (Object object : dataSet.entrySet()) {
            stringBuilder.append("\n Key: " + object);

        }

        return stringBuilder.toString();


    }

    public void updateRemoveDownstream(OREvent orEvent) {

        Set<UUID> newList = Sets.symmetricDifference(orEvent.getSet(), dataSet.get(orEvent.getElement()));

        if (newList.isEmpty()){
            dataSet.remove(orEvent.getElement());
        } else {
            dataSet.put(orEvent.getElement(), newList);
        }
    }
}
