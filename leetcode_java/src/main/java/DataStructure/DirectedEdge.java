package DataStructure;

/**
 *  DIRECTED EDGE -- one weighted, one-way edge of a graph
 *
 *  The value object behind every weighted-graph algorithm: Dijkstra,
 *  Bellman-Ford, Prim, Kruskal. An edge knows where it starts, where it
 *  ends, and what it costs.
 *
 *      v ---- weight ----> w
 *
 *  WHY A CLASS AND NOT AN int[3]: `e.from()`, `e.to()` and `e.weight()`
 *  say what they mean, where `e[0]`, `e[1]`, `e[2]` do not -- and the
 *  weight can be a double without forcing the endpoints to be doubles
 *  too.
 *
 *  DIRECTED means the edge is stored ONCE, in v's adjacency list only.
 *  An undirected graph stores each edge twice, once per endpoint. Mixing
 *  the two up is a common source of wrong shortest paths.
 *
 *  All three fields are FINAL: an edge never changes once built, so it
 *  is safe to share between adjacency lists, priority queues and result
 *  paths without defensive copying.
 *
 *  Time  : all accessors O(1)
 *  Space : O(1)
 *
 *  See algorithm/java/DijkstraSP.java for the algorithm that consumes
 *  these, and DataStructure/MinHeap.java for the priority queue it uses.
 *  Reference: https://www.coursera.org/learn/algorithms-part2/lecture/e3UfD/shortest-paths-apis
 */
public class DirectedEdge implements Comparable<DirectedEdge> {

    private final int v;            // tail: where the edge starts
    private final int w;            // head: where the edge points
    private final double weight;

    public DirectedEdge(int v, int w, double weight) {
        if (v < 0 || w < 0) {
            throw new IllegalArgumentException("vertex names must be non-negative");
        }
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException("weight must not be NaN");
        }
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    /** The vertex this edge points AWAY from. */
    public int from() {
        return v;
    }

    /** The vertex this edge points TO. */
    public int to() {
        return w;
    }

    /**
     *  The edge weight.
     *
     *  NOTE this returns a double, NOT an int. Rounding here would
     *  silently change which path is shortest -- a 2.4 + 2.4 route
     *  (4.8) would be reported as tying a 4.0 one.
     */
    public double weight() {
        return weight;
    }

    /** Order by weight, so edges can go straight into a priority queue. */
    @Override
    public int compareTo(DirectedEdge other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("%d->%d %.2f", v, w, weight);
    }

    public static void main(String[] args) {
        DirectedEdge edge = new DirectedEdge(3, 7, 2.5);

        assertThat(edge.from() == 3, "starts at 3");
        assertThat(edge.to() == 7, "points at 7");
        assertThat(edge.weight() == 2.5, "the weight is a double, not rounded to 2");
        assertThat(edge.toString().equals("3->7 2.50"), "readable form");

        // directed: 3 -> 7 is a different edge from 7 -> 3
        DirectedEdge reverse = new DirectedEdge(7, 3, 2.5);
        assertThat(reverse.from() == 7 && reverse.to() == 3, "the reverse edge is its own object");

        // ordering by weight, which is what a priority queue needs
        DirectedEdge cheap = new DirectedEdge(0, 1, 1.0);
        DirectedEdge dear = new DirectedEdge(0, 2, 9.0);
        assertThat(cheap.compareTo(dear) < 0, "cheaper edges sort first");
        assertThat(cheap.compareTo(cheap) == 0, "equal weights tie");

        // negative weights are LEGAL here -- Bellman-Ford accepts them.
        // It is Dijkstra that must reject them, and it does so itself.
        assertThat(new DirectedEdge(0, 1, -3.0).weight() == -3.0, "negative weights are allowed");

        try {
            new DirectedEdge(-1, 0, 1.0);
            assertThat(false, "expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }

        try {
            new DirectedEdge(0, 1, Double.NaN);
            assertThat(false, "expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }

        System.out.println(edge);
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
