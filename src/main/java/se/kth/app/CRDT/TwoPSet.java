package se.kth.app.CRDT;

/**
 * Created by tobiaj on 2017-05-18.
 */
public class TwoPSet extends SuperSet{

    private GSet store;
    private GSet tombstone;

    public TwoPSet(){
        this.store = new GSet();
        this.tombstone = new GSet();

    }

    public void add(Object object){
        if (!tombstone.contains(object))
            store.addObject(object);
    }


    public void remove(Object object){
        if (store.contains(object)) {
            tombstone.addObject(object);
            store.dataSet.remove(object);
        }

    }


    public String printStore(){
        return store.printStore();

    }

    public String printTombStone(){
        return tombstone.printStore();

    }



}
