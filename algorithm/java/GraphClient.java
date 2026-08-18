/**
 *  GRAPH CLIENT -- read-only queries over the {@link Graph} API
 *
 *  A "client" here means code that uses the graph WITHOUT knowing how
 *  it is stored. Every method below goes through V(), E() and adj(v),
 *  so swapping the adjacency list for a matrix would not change a line
 *  of this file. That separation is the point of the exercise.
 *
 *  Time  : degree            O(deg(v))
 *          maxDegree         O(V + E)
 *          averageDegree     O(1)     -- straight from V and E
 *          numberOfSelfLoops O(V + E)
 *  Space : O(1) extra
 *
 *  Reference: https://www.coursera.org/learn/algorithms-part2/lecture/4ZE6G/graph-api
 */
public class GraphClient {

    /** Number of edges touching v. */
    public static int degree(Graph g, int v) {
        int degree = 0;
        for (int ignored : g.adj(v)) {
            degree++;
        }
        return degree;
    }

    /** The largest degree in the graph. */
    public static int maxDegree(Graph g) {
        int max = 0;
        for (int v = 0; v < g.V(); v++) {
            max = Math.max(max, degree(g, v));
        }
        return max;
    }

    /**
     *  Average degree.
     *
     *  No traversal needed: every undirected edge contributes 1 to the
     *  degree of each endpoint, so the degrees sum to 2E and the mean
     *  is 2E/V. (This is the handshake lemma.)
     */
    public static double averageDegree(Graph g) {
        if (g.V() == 0) {
            return 0.0;
        }
        return 2.0 * g.E() / g.V();
    }

    /**
     *  Number of self-loops (edges v-v).
     *
     *  A self-loop appears TWICE in v's own adjacency list, so the raw
     *  count has to be halved.
     */
    public static int numberOfSelfLoops(Graph g) {
        int count = 0;
        for (int v = 0; v < g.V(); v++) {
            for (int w : g.adj(v)) {
                if (v == w) {
                    count++;
                }
            }
        }
        return count / 2;
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

        assertThat(degree(g, 1) == 3, "vertex 1 touches 0, 2, 3");
        assertThat(degree(g, 6) == 1, "vertex 6 touches only 5");
        assertThat(maxDegree(g) == 3, "the busiest vertex has degree 3");

        // 7 edges over 7 vertices -> 2 * 7 / 7 = 2.0
        assertThat(Math.abs(averageDegree(g) - 2.0) < 1e-9, "average degree is 2E/V");
        assertThat(numberOfSelfLoops(g) == 0, "no self-loops yet");

        g.addEdge(0, 0);
        assertThat(numberOfSelfLoops(g) == 1, "one self-loop, counted once");
        assertThat(degree(g, 0) == 4, "a self-loop adds 2 to the degree");

        Graph empty = new Graph(0);
        assertThat(maxDegree(empty) == 0 && averageDegree(empty) == 0.0, "empty graph");

        System.out.println("max degree     : " + maxDegree(g));
        System.out.println("average degree : " + averageDegree(g));
        System.out.println("self-loops     : " + numberOfSelfLoops(g));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
