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




}
