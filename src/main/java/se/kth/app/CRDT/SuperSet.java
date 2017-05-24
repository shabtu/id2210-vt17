package se.kth.app.CRDT;


import java.util.AbstractCollection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tobiaj on 2017-05-18.
 */
public abstract class SuperSet {

    Set <Object> dataSet;

    public SuperSet(){
        this.dataSet = new HashSet<>();
    }

    public void addObject(Object object){
        dataSet.add(object);
    }


    public String printStore(){
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (Object object : dataSet){
            stringBuilder.append("\n" + counter + ": " + object.toString());
            counter++;
        }
        return stringBuilder.toString();
    }

    public boolean contains(Object object){

        return dataSet.contains(object);
    }


    public Set<Object> getDataSet(){
        return dataSet;

    }

}
