package se.kth.app.Utility;

/**
 * Created by tobiaj on 2017-05-23.
 */
public class EdgeEvent {


    private Edge edge;
    private boolean source;
    private int vertexA;
    private int vertexB;

    public EdgeEvent(boolean source, int vertexA, int vertexB){
        this.source = source;
        this.vertexA = vertexA;
        this.vertexB = vertexB;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public boolean isSource() {
        return source;
    }

    public void setSource(boolean source) {
        this.source = source;
    }

    public int getVertexA() {
        return vertexA;
    }

    public void setVertexA(int vertexA) {
        this.vertexA = vertexA;
    }

    public int getVertexB() {
        return vertexB;
    }

    public void setVertexB(int vertexB) {
        this.vertexB = vertexB;
    }
}
