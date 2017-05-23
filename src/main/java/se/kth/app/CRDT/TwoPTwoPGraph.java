package se.kth.app.CRDT;

import se.kth.app.Utility.Edge;
import se.kth.app.Utility.Vertex;

/**
 * Created by tobiaj on 2017-05-23.
 */
public class TwoPTwoPGraph extends SuperSet{

    private TwoPSet vertecies;
    private TwoPSet edges;

    public TwoPTwoPGraph(){
        this.vertecies = new TwoPSet();
        this.edges = new TwoPSet();

    }

    public void addVertex(Vertex vertex){

    }

    public void removeVertex(Vertex vertex){

    }

    public void addEdge(Vertex v, Vertex u){

    }

    public void removeEdge(Edge edge){

    }

    public boolean lookupEdge(Edge edge){

        return true;
    }

    public boolean lookupVertex(Vertex vertex){

        return true;
    }
}
