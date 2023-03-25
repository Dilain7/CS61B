import java.util.*;

/**
 *  A weighted graph.
 *  @author
 */
public class Graph {

    /** Adjacency lists by vertex number. */
    private LinkedList<Edge>[] adjLists;
    /** Number of vertices in me. */
    private int vertexCount;

    /** A graph with NUMVERTICES vertices and no edges. */
    @SuppressWarnings("unchecked")
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /** Add to the graph a directed edge from vertex V1 to vertex V2,
     *  with weight EDGEWEIGHT. If the edge already exists, replaces
     *  the weight of the current edge EDGEWEIGHT. */
    public void addEdge(int v1, int v2, int edgeWeight) {
        if (!isAdjacent(v1, v2)) {
            LinkedList<Edge> v1Neighbors = adjLists[v1];
            v1Neighbors.add(new Edge(v1, v2, edgeWeight));
        } else {
            LinkedList<Edge> v1Neighbors = adjLists[v1];
            for (Edge e : v1Neighbors) {
                if (e.to() == v2) {
                    e.edgeWeight = edgeWeight;
                }
            }
        }
    }

    /** Add to the graph an undirected edge from vertex V1 to vertex V2,
     *  with weight EDGEWEIGHT. If the edge already exists, replaces
     *  the weight of the current edge EDGEWEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int edgeWeight) {
        addEdge(v1, v2, edgeWeight);
        addEdge(v2, v1, edgeWeight);
    }

    /** Returns true iff there is an edge from vertex FROM to vertex TO. */
    public boolean isAdjacent(int from, int to) {
        for (Edge e : adjLists[from]) {
            if (e.to() == to) {
                return true;
            }
        }
        return false;
    }

    /** Returns a list of all the neighboring vertices u
     *  such that the edge (VERTEX, u) exists in this graph. */
    public List<Integer> neighbors(int vertex) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (Edge e : adjLists[vertex]) {
            neighbors.add(e.to());
        }
        return neighbors;
    }

    /** Runs Dijkstra's algorithm starting from vertex STARTVERTEX and returns
     *  an integer array consisting of the shortest distances
     *  from STARTVERTEX to all other vertices. */
    public int[] dijkstras(int startVertex) {
        distance = new int[vertexCount];
        prev = new int[vertexCount];

        fringe = new PriorityQueue<>(vertexCount, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (distance[o2] >= distance[o1]) {
                    return o1;
                }
                return o2;
            }
        });


        for (int i = 0; i < distance.length; i += 1) {
            distance[i] = Integer.MAX_VALUE;
            fringe.add(i);
        }
        distance[startVertex] = 0;

        while (!fringe.isEmpty()) {
            D = vertexCount- fringe.size();
            int C = fringe.poll();
            for (int v : neighbors(C)) {
                int disT = distance[C];
                if (getEdge(C, v).edgeWeight + distance[C] < disT) {
                    distance[v] = getEdge(C, v).edgeWeight + disT;
                    prev[v] = C;
                    fringe.add(v);
                }
            }
        }
        return prev;
    }

    /** Returns the edge (V1, V2). (ou may find this helpful to implement!) */
    private Edge getEdge(int v1, int v2) {
        return null;
    }

    /** Represents an edge in this graph. */
    private class Edge {

        /** End points of this edge. */
        private int from, to;
        /** Weight label of this edge. */
        private int edgeWeight;

        /** The edge (V0, V1) with weight WEIGHT. */
        Edge(int v0, int v1, int weight) {
            this.from = v0;
            this.to = v1;
            this.edgeWeight = weight;
        }

        /** Return neighbor vertex along this edge. */
        public int to() {
            return to;
        }

        /** Return weight of this edge. */
        public int info() {
            return edgeWeight;
        }

        @Override
        public String toString() {
            return "(" + from + "," + to + ",dist=" + edgeWeight + ")";
        }

    }

    /** Tests of Graph. */
    public static void main(String[] unused) {
        // Put some tests here!

        Graph g1 = new Graph(5);
        g1.addEdge(0, 1, 1);
        g1.addEdge(0, 2, 1);
        g1.addEdge(0, 4, 1);
        g1.addEdge(1, 2, 1);
        g1.addEdge(2, 0, 1);
        g1.addEdge(2, 3, 1);
        g1.addEdge(4, 3, 1);

        int[] dist101 = g1.dijkstras(1);
        System.out.println(Arrays.toString(dist101));

        Graph g2 = new Graph(5);
        g2.addEdge(0, 1, 1);
        g2.addEdge(0, 2, 1);
        g2.addEdge(0, 4, 1);
        g2.addEdge(1, 2, 1);
        g2.addEdge(2, 3, 1);
        g2.addEdge(4, 3, 1);

        
        int[] dist102 = g2.dijkstras(0);
        System.out.println(Arrays.toString(dist102));

    }
    private int[] distance;

    private int[] prev;

    PriorityQueue<Integer> fringe;

    private int D;


}
