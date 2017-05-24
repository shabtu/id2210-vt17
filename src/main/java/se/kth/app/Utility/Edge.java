package se.kth.app.Utility;

/**
 * Created by tobiaj on 2017-05-23.
 */
public class Edge {

    private String id;
    private Vertex vertexOne;
    private Vertex vertexTwo;


    public Edge(String id, Vertex vertexOne, Vertex vertexTwo){
        this.id = id;
        this.vertexOne = vertexOne;
        this.vertexTwo = vertexTwo;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vertex getVertexOne() {
        return vertexOne;
    }

    public void setVertexOne(Vertex vertexOne) {
        this.vertexOne = vertexOne;
    }

    public Vertex getVertexTwo() {
        return vertexTwo;
    }

    public void setVertexTwo(Vertex vertexTwo) {
        this.vertexTwo = vertexTwo;
    }
}
