package se.kth.app.CRDT;

import java.util.Set;

/**
 * Created by tobiaj on 2017-05-18.
 */
public class GSet extends SuperSet{

    private Set<Object> dataSet;

    public GSet(){
        this.dataSet = super.dataSet;
    }



}
