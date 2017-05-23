package se.kth.app.Utility;

/**
 * Created by tobiaj on 2017-05-23.
 */
public class Edge {

    private int id;
    private Vertex vertexOne;
    private Vertex vertexTwo;


    public Edge(int id, Vertex vertexOne, Vertex vertexTwo){
        this.id = id;
        this.vertexOne = vertexOne;
        this.vertexTwo = vertexTwo;

    }
}
