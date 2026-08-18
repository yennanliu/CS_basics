import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

/**
 *  DIJKSTRA -- single-source shortest paths on a weighted digraph
 *
 *  BFS finds the path with the fewest EDGES. Dijkstra finds the path
 *  with the smallest total WEIGHT, by replacing BFS's plain queue with
 *  a PRIORITY queue keyed on distance-so-far.
 *
 *      0 --4--> 1 --11--> 2
 *      |        |         ^
 *      8        2         |
 *      v        v         3
 *      3 --7--> 4 --------+
 *
 *      from 0:  distTo[1] = 4,  distTo[3] = 8,  distTo[4] = 6,  distTo[2] = 9
 *
 *  THE ALGORITHM
 *    1) distTo[s] = 0, every other distance = infinity
 *    2) repeatedly take the UNSETTLED vertex with the smallest distTo
 *    3) RELAX each of its outgoing edges: if going through v reaches w
 *       more cheaply than the best route found so far, record that
 *    4) once a vertex comes off the queue its distance is final
 *
 *  WHY STEP 4 HOLDS -- and its precondition: the closest unsettled
 *  vertex cannot be improved later, because any other route to it goes
 *  through some vertex that is already at least as far away, and adding
 *  more edges can only add more distance. That last clause needs
 *  NON-NEGATIVE weights. With a negative edge the argument collapses
 *  and Dijkstra returns wrong answers -- use Bellman-Ford instead (see
 *  algorithm/python/bellman_ford.py).
 *
 *  LAZY vs EAGER: java.util.PriorityQueue has no decrease-key, so this
 *  is the LAZY variant -- push a new entry on every improvement and
 *  discard stale ones on the way out. The heap can hold up to E entries
 *  instead of V, which costs memory but keeps the code short.
 *
 *  Time  : O(E log E)  lazy   (O(E log V) with an indexed PQ)
 *  Space : O(V + E)
 *
 *  Reference: https://www.coursera.org/learn/algorithms-part2/lecture/2e9Ic/dijkstras-algorithm
 */
public class DijkstraSP {

    private final double[] distTo;          // distTo[v] = weight of the best known path s -> v
    private final DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on that path
    private final boolean[] settled;        // settled[v] = is distTo[v] final?

    public DijkstraSP(EdgeWeightedDigraph g, int s) {
        for (DirectedEdge e : g.edges()) {
            if (e.weight() < 0) {
                throw new IllegalArgumentException("edge " + e + " has negative weight");
            }
        }

        distTo = new double[g.V()];
        edgeTo = new DirectedEdge[g.V()];
        settled = new boolean[g.V()];

        for (int v = 0; v < g.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;

        // entries are {vertex, distance-at-push-time}, ordered by distance
        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(entry -> entry[1]));
        pq.add(new double[] {s, 0.0});

        while (!pq.isEmpty()) {
            int v = (int) pq.poll()[0];

            // lazy deletion: an improved entry was pushed later, so this
            // one is stale -- skip it
            if (settled[v]) {
                continue;
            }
            settled[v] = true;

            for (DirectedEdge e : g.adj(v)) {
                if (relax(e)) {
                    pq.add(new double[] {e.to(), distTo[e.to()]});
                }
            }
        }
    }

    /** If e gives a cheaper route to e.to(), record it. Returns true if it did. */
    private boolean relax(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            return true;
        }
        return false;
    }

    /** Weight of the shortest path to v; infinity when v is unreachable. */
    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /** The edges of the shortest path to v, in order; null when unreachable. */
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        // walk edgeTo[] backwards from v, then reverse via a stack
        Deque<DirectedEdge> path = new ArrayDeque<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    /** A weighted, directed edge. */
    public static class DirectedEdge {
        private final int v;
        private final int w;
        private final double weight;

        public DirectedEdge(int v, int w, double weight) {
            this.v = v;
            this.w = w;
            this.weight = weight;
        }

        public int from() {
            return v;
        }

        public int to() {
            return w;
        }

        /** NOTE: a double, not an int -- rounding here would silently change the answer. */
        public double weight() {
            return weight;
        }

        @Override
        public String toString() {
            return String.format("%d->%d %.2f", v, w, weight);
        }
    }

    /** Edge-weighted digraph, adjacency-list representation. */
    public static class EdgeWeightedDigraph {
        private final int V;
        private final List<List<DirectedEdge>> adj;

        public EdgeWeightedDigraph(int V) {
            this.V = V;
            this.adj = new ArrayList<>(V);
            for (int v = 0; v < V; v++) {
                adj.add(new ArrayList<>());
            }
        }

        public int V() {
            return V;
        }

        public void addEdge(DirectedEdge e) {
            adj.get(e.from()).add(e);
        }

        public void addEdge(int v, int w, double weight) {
            addEdge(new DirectedEdge(v, w, weight));
        }

        public Iterable<DirectedEdge> adj(int v) {
            return adj.get(v);
        }

        public Iterable<DirectedEdge> edges() {
            List<DirectedEdge> all = new ArrayList<>();
            for (List<DirectedEdge> list : adj) {
                all.addAll(list);
            }
            return all;
        }
    }

    public static void main(String[] args) {
        //  0 --4--> 1 --11--> 2
        //  |        |         ^
        //  8        2         3
        //  v        v         |
        //  3 --7--> 4 --------+
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(6);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 3, 8);
        g.addEdge(1, 2, 11);
        g.addEdge(1, 4, 2);
        g.addEdge(3, 4, 7);
        g.addEdge(4, 2, 3);

        DijkstraSP sp = new DijkstraSP(g, 0);

        assertThat(sp.distTo(0) == 0.0, "distance to the source is 0");
        assertThat(sp.distTo(1) == 4.0, "0 -> 1 directly");
        assertThat(sp.distTo(3) == 8.0, "0 -> 3 directly");
        assertThat(sp.distTo(4) == 6.0, "0 -> 1 -> 4 (6) beats 0 -> 3 -> 4 (15)");

        // the point of the algorithm: 0 -> 1 -> 4 -> 2 costs 9, while
        // the single direct-looking route 0 -> 1 -> 2 costs 15
        assertThat(sp.distTo(2) == 9.0, "a longer path in EDGES can be shorter in WEIGHT");

        StringBuilder path = new StringBuilder();
        for (DirectedEdge e : sp.pathTo(2)) {
            path.append(e).append("  ");
        }
        assertThat(path.toString().trim().equals("0->1 4.00  1->4 2.00  4->2 3.00"), "path to 2");

        // vertex 5 has no incoming edges
        assertThat(!sp.hasPathTo(5), "unreachable vertex");
        assertThat(sp.distTo(5) == Double.POSITIVE_INFINITY, "unreachable distance is infinity");
        assertThat(sp.pathTo(5) == null, "no path object for an unreachable vertex");

        // a cheaper route discovered LATE must still win -- this is what
        // the lazy stale-entry skip has to get right
        EdgeWeightedDigraph late = new EdgeWeightedDigraph(4);
        late.addEdge(0, 1, 1);
        late.addEdge(0, 2, 10);
        late.addEdge(1, 2, 2);
        late.addEdge(2, 3, 1);
        DijkstraSP lateSp = new DijkstraSP(late, 0);
        assertThat(lateSp.distTo(2) == 3.0, "0 -> 1 -> 2 (3) replaces the direct edge (10)");
        assertThat(lateSp.distTo(3) == 4.0, "the improvement propagates onward");

        // negative weights break the algorithm's core assumption, so reject them
        EdgeWeightedDigraph negative = new EdgeWeightedDigraph(2);
        negative.addEdge(0, 1, -1);
        try {
            new DijkstraSP(negative, 0);
            assertThat(false, "expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }

        System.out.println("distTo(2)  : " + sp.distTo(2));
        System.out.println("pathTo(2)  : " + path.toString().trim());
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
