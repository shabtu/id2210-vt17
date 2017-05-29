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
            dataSet.remove(tempElement);

        }

        return element;
    }

    public void updateRemoveDownstream(OREvent orEvent) {

        Set<UUID> tempOrEvent = orEvent.getSet();
        Set<UUID> tempORSet = dataSet.get(orEvent.getElement());
        Set<UUID> newSet = new HashSet<>();
        if (tempOrEvent != null  && tempORSet != null){

            /**Should only remove those entries that does match, does that has been delivered, causal
             * tells that you can not remove something that has not been delivered**/
            for (UUID id : tempOrEvent){
                if (tempORSet.contains(id)){
                    newSet.add(id);
                }
            }

            tempORSet.removeAll(newSet);

            if (tempORSet.isEmpty()){
                dataSet.remove(orEvent.getElement());
            } else {
                dataSet.put(orEvent.getElement(), tempORSet);
            }
        }
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


}
