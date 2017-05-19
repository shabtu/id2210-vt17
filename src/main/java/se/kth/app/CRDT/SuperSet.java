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

    public boolean addObject(Object object){
        if (!dataSet.contains(object)){
            dataSet.add(object);
            return true;
        }
        else return false;
    }


    public String printStore(){
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (Object object : dataSet){
            stringBuilder.append(counter + ": " + object.toString() + " \n");
            counter++;
        }
        return stringBuilder.toString();
    }

}
