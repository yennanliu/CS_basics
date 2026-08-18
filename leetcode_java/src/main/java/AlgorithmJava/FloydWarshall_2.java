package AlgorithmJava;

/**
 *  FLOYD-WARSHALL (V2) -- the minimal form
 *
 *  Scope: the shortest readable version -- three loops and a print.
 *         See FloydWarshall_1 for the version that returns a matrix,
 *         guards the INF arithmetic explicitly, and detects negative
 *         cycles.
 *
 *  All-pairs shortest paths in three nested loops:
 *
 *      dist[i][j] = min( dist[i][j], dist[i][k] + dist[k][j] )
 *
 *  k -- the INTERMEDIATE vertex -- MUST be the outer loop. After
 *  iteration k, dist[i][j] is the shortest i -> j path using only
 *  vertices 0..k in between; each new k merely asks "does routing
 *  through k help?". Putting k inside considers intermediates that are
 *  not final yet and silently produces wrong answers.
 *
 *      0 --5--> 1 --3--> 2 --1--> 3
 *      |                          ^
 *      +-----------10-------------+
 *
 *      dist[0][3]:  10  ->  9  (via 1 and 2)
 *
 *  WHY INF IS 99999 AND NOT Integer.MAX_VALUE: the relaxation adds two
 *  cells unconditionally. With MAX_VALUE that sum OVERFLOWS to a
 *  negative number, which then looks like a wonderfully short path and
 *  corrupts the matrix. A large-but-addable sentinel keeps
 *  INF + INF safely positive, so no comparison ever prefers it.
 *
 *  Time  : O(V^3)
 *  Space : O(V^2)
 */
public class FloydWarshall_2 {

    /** "No edge". Small enough that INF + INF does not overflow an int. */
    static final int INF = 99999;

    /** Compute all-pairs shortest paths and print the result matrix. */
    void floydWarshall(int[][] graph) {
        int[][] dist = shortestPaths(graph);
        printSolution(dist);
    }

    /** All-pairs shortest paths. Returns a NEW matrix; `graph` is untouched. */
    int[][] shortestPaths(int[][] graph) {
        int v = graph.length;
        int[][] dist = new int[v][v];

        // start from the direct edges
        for (int i = 0; i < v; i++) {
            System.arraycopy(graph[i], 0, dist[i], 0, v);
        }

        // k is the INTERMEDIATE vertex, and must be the outer loop
        for (int k = 0; k < v; k++) {
            for (int i = 0; i < v; i++) {
                for (int j = 0; j < v; j++) {
                    // relaxation: is going through k cheaper than what we have?
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        return dist;
    }

    void printSolution(int[][] dist) {
        System.out.println("Shortest distances between every pair of vertices:");
        for (int[] row : dist) {
            StringBuilder line = new StringBuilder();
            for (int value : row) {
                line.append(value >= INF ? "INF" : String.valueOf(value)).append(" ");
            }
            System.out.println(line.toString().trim());
        }
    }

    public static void main(String[] args) {
        //  0 --5--> 1 --3--> 2 --1--> 3
        //  |                          ^
        //  +-----------10-------------+
        int[][] graph = {
                {0,   5,   INF, 10},
                {INF, 0,   3,   INF},
                {INF, INF, 0,   1},
                {INF, INF, INF, 0}
        };

        FloydWarshall_2 fw = new FloydWarshall_2();
        int[][] dist = fw.shortestPaths(graph);

        assertThat(dist[0][0] == 0 && dist[3][3] == 0, "a vertex is 0 from itself");
        assertThat(dist[0][1] == 5, "0 -> 1 directly");
        assertThat(dist[0][2] == 8, "0 -> 1 -> 2 costs 8");

        // the headline result: the 3-hop route beats the direct edge
        assertThat(dist[0][3] == 9, "0 -> 1 -> 2 -> 3 (9) beats the direct edge (10)");

        // the graph is directed, so unreachable pairs stay at (or above) INF
        assertThat(dist[3][0] >= INF, "nothing points back to 0");

        // the input matrix is not modified
        assertThat(graph[0][3] == 10, "input is left untouched");

        fw.floydWarshall(graph);
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
