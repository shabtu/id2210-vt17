package se.kth.app.Utility;

/**
 * Created by tobiaj on 2017-05-23.
 */
public class VertexEvent {

    private Vertex vertex;
    private boolean source;
    private int id;

    public VertexEvent(boolean source, int id){
        this.source = source;
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public boolean isSource() {
        return source;
    }

    public void setSource(boolean source) {
        this.source = source;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }
}
