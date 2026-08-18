package AlgorithmJava;

/**
 *  FLOYD-WARSHALL (V1) -- all-pairs shortest paths
 *
 *  Scope: the version that takes an INF-padded adjacency matrix and
 *         RETURNS a distance matrix, plus negative-cycle detection.
 *         See FloydWarshall_2 for the minimal in-place-and-print form.
 *
 *  Dijkstra answers "shortest path from ONE source". Floyd-Warshall
 *  answers "shortest path between EVERY pair", in three nested loops.
 *
 *  THE RECURRENCE -- and the reason k is the OUTER loop:
 *
 *      dist[i][j] = min( dist[i][j], dist[i][k] + dist[k][j] )
 *
 *  After iteration k, dist[i][j] holds the shortest i -> j path that
 *  uses only vertices 0..k as intermediates. Each new k merely asks
 *  "does routing through k help?". Putting k inside would consider
 *  intermediates that have not been finalised yet, and quietly produce
 *  wrong answers -- the classic Floyd-Warshall bug.
 *
 *      0 --5--> 1 --3--> 2 --1--> 3
 *      |                          ^
 *      +-----------10-------------+
 *
 *      dist[0][3] starts at 10, then k=1,2 find 5+3+1 = 9
 *
 *  THE OVERFLOW GUARD: `dist[i][k] + dist[k][j]` is computed even when
 *  one side is "no path". With Integer.MAX_VALUE as the sentinel that
 *  addition WRAPS NEGATIVE and looks like a wonderfully short path, so
 *  INF is a large-but-addable constant and pairs involving it are
 *  skipped outright.
 *
 *  WHEN TO PREFER IT: V^3 beats running Dijkstra V times (V * E log V)
 *  on DENSE graphs, and it handles NEGATIVE edge weights, which
 *  Dijkstra cannot. Negative CYCLES still make shortest paths
 *  meaningless -- hence hasNegativeCycle().
 *
 *  Used by: LC 1334 Find the City With the Smallest Number of Neighbors,
 *           LC 2642 Design Graph With Shortest Path Calculator.
 *
 *  Time  : O(V^3)
 *  Space : O(V^2)
 */
public class FloydWarshall_1 {

    /**
     *  "No edge". Large enough to dominate any real path, small enough
     *  that INF + INF does not overflow an int.
     */
    public static final int INF = 100_000_000;

    /**
     *  All-pairs shortest paths.
     *
     *  @param graph adjacency matrix; graph[i][j] is the weight of the
     *               direct edge i -> j, or {@link #INF} when there is none
     *  @return a NEW matrix where result[i][j] is the shortest distance
     */
    public int[][] floydWarshall(int[][] graph) {
        int v = graph.length;
        int[][] dist = new int[v][v];

        // 1) start from the direct edges; a vertex is always 0 from itself
        for (int i = 0; i < v; i++) {
            System.arraycopy(graph[i], 0, dist[i], 0, v);
            dist[i][i] = 0;
        }

        // 2) k is the INTERMEDIATE vertex, and MUST be the outer loop
        for (int k = 0; k < v; k++) {
            for (int i = 0; i < v; i++) {
                for (int j = 0; j < v; j++) {
                    // skip pairs with no path -- adding INF would produce
                    // a bogus "shortest" route through a non-existent edge
                    if (dist[i][k] == INF || dist[k][j] == INF) {
                        continue;
                    }
                    // relaxation
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        return dist;
    }

    /**
     *  True if the graph contains a negative cycle.
     *
     *  A vertex that can reach itself at negative cost sits on a cycle
     *  you can loop forever to drive the distance to minus infinity, so
     *  "shortest path" stops being well defined.
     */
    public boolean hasNegativeCycle(int[][] graph) {
        int[][] dist = floydWarshall(graph);
        for (int i = 0; i < dist.length; i++) {
            if (dist[i][i] < 0) {
                return true;
            }
        }
        return false;
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

        FloydWarshall_1 fw = new FloydWarshall_1();
        int[][] dist = fw.floydWarshall(graph);

        assertThat(dist[0][0] == 0 && dist[3][3] == 0, "a vertex is 0 from itself");
        assertThat(dist[0][1] == 5, "0 -> 1 directly");
        assertThat(dist[0][2] == 8, "0 -> 1 -> 2 costs 8");

        // the headline result: the 3-hop route beats the direct edge
        assertThat(dist[0][3] == 9, "0 -> 1 -> 2 -> 3 (9) beats the direct edge (10)");

        // the graph is directed, so unreachable pairs stay at INF
        assertThat(dist[3][0] == INF, "nothing points back to 0");
        assertThat(dist[2][1] == INF, "no route from 2 to 1");

        // the input matrix is not modified
        assertThat(graph[0][3] == 10, "input is left untouched");

        // negative EDGES are fine -- this is what Dijkstra cannot do
        int[][] negativeEdge = {
                {0,   4,   INF},
                {INF, 0,   -2},
                {INF, INF, 0}
        };
        assertThat(fw.floydWarshall(negativeEdge)[0][2] == 2, "0 -> 1 -> 2 costs 4 + (-2) = 2");
        assertThat(!fw.hasNegativeCycle(negativeEdge), "a negative edge is not a negative cycle");

        // a negative CYCLE is not: 0 -> 1 -> 0 costs -1 per lap
        int[][] negativeCycle = {
                {0,  1,   INF},
                {-2, 0,   INF},
                {INF, INF, 0}
        };
        assertThat(fw.hasNegativeCycle(negativeCycle), "0 -> 1 -> 0 costs -1 per lap");

        printSolution(dist);
        System.out.println("Success.");
    }

    private static void printSolution(int[][] dist) {
        System.out.println("Shortest distances between every pair of vertices:");
        for (int[] row : dist) {
            StringBuilder line = new StringBuilder();
            for (int value : row) {
                line.append(value == INF ? "INF" : value).append("\t");
            }
            System.out.println(line.toString().trim());
        }
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
