package se.kth.app.CRDT;

import se.kth.app.Utility.Edge;
import se.kth.app.Utility.Vertex;

import java.util.Set;

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

    public boolean lookUp(Object object){

        if (object instanceof Edge){

            Edge edge = (Edge) object;

            System.out.println("hit i twopset "+ edge);

            if(store.contains(edge) && !tombstone.contains(edge)){
                return true;
            }
            else {


                return false;
            }
        }
        else {
            if (store.contains(object) && !tombstone.contains(object)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Set<Object> getSet(){

        return store.getDataSet();
    }


}
