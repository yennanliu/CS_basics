import java.util.ArrayList;
import java.util.List;

/**
 *  GRAPH -- undirected, adjacency-list representation
 *
 *  A graph is V vertices (numbered 0 .. V-1) plus E edges. Unlike a
 *  tree it may contain cycles and has no root.
 *
 *      0 --- 1 --- 3
 *      |    /|     |
 *      2 --/ |     4 --- 5 --- 6
 *
 *  TWO WAYS TO STORE IT
 *
 *    adjacency LIST     one list of neighbours per vertex
 *      space O(V + E); iterating a vertex's neighbours is O(deg(v))
 *      -> the default, because real graphs are SPARSE
 *
 *    adjacency MATRIX   a V-by-V boolean grid
 *      space O(V^2); edge lookup is O(1), neighbour iteration is O(V)
 *      -> only worth it for DENSE graphs or O(1) edge tests
 *
 *  An UNDIRECTED edge v-w is stored TWICE, once in each vertex's list.
 *  That is why {@link #E()} halves the total.
 *
 *  Time  : addEdge O(1), adj(v) O(1) to obtain, O(deg(v)) to walk
 *  Space : O(V + E)
 *
 *  See GraphClient.java for the read-only queries built on this API.
 *  Reference: https://www.coursera.org/learn/algorithms-part2/lecture/4ZE6G/graph-api
 */
public class Graph {

    private final int V;                    // number of vertices, fixed at construction
    private int E;                          // number of edges
    private final List<List<Integer>> adj;  // adj.get(v) = the neighbours of v

    /** Create a graph with V vertices and no edges. */
    public Graph(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("vertex count must be non-negative");
        }
        this.V = V;
        this.E = 0;
        this.adj = new ArrayList<>(V);
        for (int v = 0; v < V; v++) {
            adj.add(new ArrayList<>());
        }
    }

    /** Number of vertices. */
    public int V() {
        return V;
    }

    /** Number of edges. */
    public int E() {
        return E;
    }

    /** Add the undirected edge v-w. Self-loops and parallel edges are allowed. */
    public void addEdge(int v, int w) {
        validate(v);
        validate(w);
        adj.get(v).add(w);
        adj.get(w).add(v);   // undirected -> record BOTH directions
        E++;
    }

    /** The vertices adjacent to v. */
    public Iterable<Integer> adj(int v) {
        validate(v);
        return adj.get(v);
    }

    /** Number of edges touching v. A self-loop counts twice. */
    public int degree(int v) {
        validate(v);
        return adj.get(v).size();
    }

    private void validate(int v) {
        if (v < 0 || v >= V) {
            throw new IndexOutOfBoundsException("vertex " + v + " is not in 0.." + (V - 1));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V).append(" vertices, ").append(E).append(" edges\n");
        for (int v = 0; v < V; v++) {
            sb.append(v).append(": ");
            for (int w : adj.get(v)) {
                sb.append(w).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        //    0 --- 1 --- 3
        //    |    /|     |
        //    2 --/ |     4 --- 5 --- 6
        Graph g = new Graph(7);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 6);

        assertThat(g.V() == 7, "7 vertices");
        assertThat(g.E() == 7, "7 edges (each stored twice, counted once)");
        assertThat(g.degree(1) == 3, "vertex 1 touches 0, 2, 3");
        assertThat(g.degree(6) == 1, "vertex 6 touches only 5");

        // an undirected edge shows up in both adjacency lists
        assertThat(contains(g.adj(0), 1) && contains(g.adj(1), 0), "edge is bidirectional");

        // a self-loop adds 2 to the degree but only 1 to the edge count
        Graph loop = new Graph(1);
        loop.addEdge(0, 0);
        assertThat(loop.E() == 1 && loop.degree(0) == 2, "self-loop counts twice in degree");

        try {
            g.addEdge(0, 99);
            assertThat(false, "expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }

        System.out.print(g);
        System.out.println("Success.");
    }

    private static boolean contains(Iterable<Integer> values, int target) {
        for (int value : values) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
