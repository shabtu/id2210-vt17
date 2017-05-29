package se.kth.app.CRDT;

import se.kth.app.Utility.Edge;
import se.kth.app.Utility.Vertex;

import java.util.HashSet;
import java.util.Set;

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

        vertecies.add(vertex);
    }

    public Vertex addVertexByID(int vertexId){

        Vertex vertex = new Vertex(vertexId);

        vertecies.add(vertex);

        return vertex;
    }

    public Vertex removeVertexByID(int id){

        Vertex vertex = getVertexbyID(id);

        if (vertex != null) {
            if (lookupVertex(vertex)) {

                removeVertex(vertex);
                return vertex;
            }
        }

        return null;

    }

    private void removeEdgesFromVertex(Vertex vertex) {
        Set<Object> objects = edges.getSet();
        Set<Object> remove = new HashSet<>();

        System.out.println("edges " + objects   + " size " + objects.size() );
        for (Object object : objects){

            Edge edge = (Edge) object;

            if (lookupEdge(edge)){

                if (vertex.equals(edge.getVertexOne()) || vertex.equals(edge.getVertexTwo())) {
                    remove.add(edge);
                    //removeEdge(edge);

                }

            }
        }
        for (Object object : remove){
            Edge edge = (Edge) object;

            removeEdge(edge);
        }

    }

    public void removeVertex(Vertex vertex){

        if (lookupVertex(vertex)){
            removeEdgesFromVertex(vertex);
            vertecies.remove(vertex);
        }
    }

    public void addEdge(Edge edge){

        if (lookupVertex(edge.getVertexOne()) && lookupVertex(edge.getVertexTwo())) {
            edges.add(edge);
        }

    }

    public Edge addEdgeByVerticesID(int ID_A, int ID_B){

        Vertex vertexA = getVertexbyID(ID_A);
        Vertex vertexB = getVertexbyID(ID_B);

        if (vertexA != null && vertexB != null){

            if (lookupVertex(vertexA) && lookupVertex(vertexB)){

                String stringID = "" + vertexA.getId() +""+ vertexB.getId();

                Edge edge = new Edge(stringID, vertexA, vertexB);

                edges.add(edge);

                return edge;

            }
        }

        return null;
    }

    public Edge removeEdgeByID(int ID_A, int ID_B){

        Vertex vertexA = getVertexbyID(ID_A);
        Vertex vertexB = getVertexbyID(ID_B);

        if (vertexA != null && vertexB != null){

            Set<Object> objects = edges.getSet();

            for (Object object : objects) {

                Edge edge = (Edge) object;

                if ((vertexA.equals(edge.getVertexOne()) || vertexA.equals(edge.getVertexTwo())) && (vertexB.equals(edge.getVertexOne()) || vertexB.equals(edge.getVertexTwo()))) {

                    removeEdge(edge);
                    return edge;
                }
            }
        }

        return null;
    }

    public void removeEdge(Edge edge){

        try {

            if (lookupEdge(edge)) {
                edges.remove(edge);
            }

        }
        catch (NullPointerException e){
            System.out.println("Already removed");
        }
    }

    public boolean lookupEdge(Edge edge){

        try{

            if (lookupVertex(edge.getVertexOne()) && lookupVertex(edge.getVertexTwo()) && edges.lookUp(edge)){
                return true;
            }

        }
        catch (NullPointerException e){
            System.out.println("Edge does not exist");
        }

        return false;
    }

    public boolean lookupVertex(Vertex vertex){


        try {
            if (vertecies.lookUp(vertex)) {
                return true;
            }
        }

        catch (NullPointerException e){
            System.out.println("Vertex does not exist");
        }

        return false;
    }

    public String print2P2PVerteciesStore(){

        Set<Object> objects = vertecies.getSet();
        StringBuilder stringBuilder = new StringBuilder();

        for (Object object : objects){
            Vertex vertex = (Vertex) object;
            if (vertex !=null) {
                stringBuilder.append("Vertex: " + vertex.getId() + " " + vertex + " \n");
            }
        }
        return stringBuilder.toString();
    }

    public String print2P2PEdgesStore(){

        Set<Object> objects = edges.getSet();
        StringBuilder stringBuilder = new StringBuilder();

        for (Object object : objects){
            Edge edge = (Edge) object;
            if (edge != null) {
                stringBuilder.append("Edge: " + edge.getId() + " connected between vertex: " + edge.getVertexOne().getId() + " and vertex: " + edge.getVertexTwo().getId() + "\n ");
            }
        }
        return stringBuilder.toString();

    }

    public Vertex getVertexbyID(int id){

        Set<Object> setOfVertecies = vertecies.getSet();

        for (Object object : setOfVertecies){

            Vertex vertex = (Vertex) object;

            if (vertex.getId() == id){
                return vertex;
            }

        }
        return null;

    }
}
