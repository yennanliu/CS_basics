package AlgorithmJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *  DIJKSTRA (V1) -- single-source shortest paths, min-heap + BFS
 *
 *  Scope: the reusable graph class (build with addEdge, then run).
 *         See Dijkstra_2 for the LC 743 "Network Delay Time" shape that
 *         takes an edge array directly, and
 *         algorithm/java/DijkstraSP.java for the weighted-digraph API
 *         version with path reconstruction.
 *
 *  BFS finds the path with the fewest EDGES. Dijkstra finds the path
 *  with the smallest total WEIGHT, by replacing BFS's plain queue with
 *  a PRIORITY queue keyed on distance-so-far.
 *
 *      0 --4-- 1 --8-- 2
 *      |       |
 *      8       11
 *      |       |
 *      7 --1-- 6
 *
 *  THE ALGORITHM
 *    1) distances[source] = 0, everything else = infinity
 *    2) pop the UNSETTLED vertex with the smallest distance
 *    3) RELAX its edges: if going through u reaches v more cheaply than
 *       the best route so far, record that and push v
 *    4) once a vertex is popped its distance is FINAL
 *
 *  WHY STEP 4 HOLDS -- and its precondition: the closest unsettled
 *  vertex cannot be improved later, because any other route to it runs
 *  through a vertex at least as far away, and more edges only add more
 *  distance. That last clause needs NON-NEGATIVE weights. With a
 *  negative edge the argument collapses and Dijkstra returns wrong
 *  answers -- use Bellman-Ford (algorithm/python/bellman_ford.py).
 *
 *  LAZY DELETION: java.util.PriorityQueue has no decrease-key, so an
 *  improved distance is pushed as a NEW entry and the stale one is
 *  skipped when it surfaces. Without that `settled` check the same
 *  vertex would be expanded several times.
 *
 *  Time  : O(E log E)
 *  Space : O(V + E)
 */
public class Dijkstra_1 {

    private final int vertices;
    private final List<List<Node>> adjList;

    public Dijkstra_1(int vertices) {
        this.vertices = vertices;
        adjList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    /** Add an UNDIRECTED weighted edge (recorded in both directions). */
    public void addEdge(int source, int destination, int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException(
                    "Dijkstra requires non-negative weights, got " + weight);
        }
        adjList.get(source).add(new Node(destination, weight));
        adjList.get(destination).add(new Node(source, weight));
    }

    /**
     *  Shortest distance from `start` to every vertex; Integer.MAX_VALUE
     *  if unreachable.
     *
     *  LIMIT: because Integer.MAX_VALUE doubles as the "unreachable"
     *  sentinel, a real path whose total weight reaches it cannot be
     *  distinguished from no path at all. Relaxation is done in long so
     *  such a path never wraps negative, but graphs with weights that
     *  large need a long[] distance array and a separate sentinel.
     */
    public int[] shortestDistances(int start) {
        int[] distances = new int[vertices];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[start] = 0;

        boolean[] settled = new boolean[vertices];

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));
        pq.add(new Node(start, 0));

        while (!pq.isEmpty()) {
            int u = pq.poll().vertex;

            // lazy deletion: a better entry for u was pushed later, so
            // this one is stale
            if (settled[u]) {
                continue;
            }
            settled[u] = true;

            for (Node neighbor : adjList.get(u)) {
                int v = neighbor.vertex;
                int weight = neighbor.distance;

                // relax: is going through u cheaper than what we have?
                //
                // NOTE the widening to long. distances[u] + weight can
                // exceed Integer.MAX_VALUE on a legitimately long path,
                // and int arithmetic would WRAP NEGATIVE -- which then
                // looks like a wonderfully short route and silently
                // corrupts every distance downstream of it.
                long candidate = (long) distances[u] + weight;
                if (!settled[v] && candidate < distances[v]) {
                    distances[v] = (int) candidate;
                    pq.add(new Node(v, distances[v]));
                }
            }
        }
        return distances;
    }

    /** Run the search and print the result table. */
    public void dijkstra(int start) {
        printSolution(shortestDistances(start));
    }

    private void printSolution(int[] distances) {
        System.out.println("Vertex\tDistance from Source");
        for (int i = 0; i < vertices; i++) {
            System.out.println(i + "\t" + (distances[i] == Integer.MAX_VALUE ? "INF" : distances[i]));
        }
    }

    /** Doubles as an adjacency entry (neighbour + edge weight) and a heap entry. */
    static class Node {
        final int vertex;
        final int distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public static void main(String[] args) {
        Dijkstra_1 graph = new Dijkstra_1(9);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 7, 8);
        graph.addEdge(1, 2, 8);
        graph.addEdge(1, 7, 11);
        graph.addEdge(2, 3, 7);
        graph.addEdge(2, 5, 4);
        graph.addEdge(2, 8, 2);
        graph.addEdge(3, 4, 9);
        graph.addEdge(3, 5, 14);
        graph.addEdge(4, 5, 10);
        graph.addEdge(5, 6, 2);
        graph.addEdge(6, 7, 1);
        graph.addEdge(6, 8, 6);
        graph.addEdge(7, 8, 7);

        int[] distances = graph.shortestDistances(0);

        assertThat(distances[0] == 0, "distance to the source is 0");
        assertThat(distances[1] == 4, "0 -> 1 directly");
        assertThat(distances[7] == 8, "0 -> 7 directly");

        // the point of the algorithm: more edges can still mean less weight.
        // 0 -> 7 -> 6 (9) beats every shorter-in-edges alternative.
        assertThat(distances[6] == 9, "0 -> 7 -> 6 costs 9");
        assertThat(distances[5] == 11, "0 -> 7 -> 6 -> 5 costs 11");
        assertThat(distances[2] == 12, "0 -> 1 -> 2 costs 12");
        assertThat(distances[3] == 19, "0 -> 1 -> 2 -> 3 costs 19");
        assertThat(distances[4] == 21, "0 -> 7 -> 6 -> 5 -> 4 costs 21");
        assertThat(distances[8] == 14, "0 -> 1 -> 2 -> 8 costs 14");

        // a long path must not wrap negative through int overflow
        Dijkstra_1 huge = new Dijkstra_1(3);
        huge.addEdge(0, 1, Integer.MAX_VALUE - 1);
        huge.addEdge(1, 2, 2);
        int[] hugeDist = huge.shortestDistances(0);
        assertThat(hugeDist[1] == Integer.MAX_VALUE - 1, "the first hop is exact");
        assertThat(hugeDist[2] > 0, "the second hop did not overflow to a negative distance");

        // an unreachable vertex keeps its infinite distance
        Dijkstra_1 disconnected = new Dijkstra_1(3);
        disconnected.addEdge(0, 1, 5);
        int[] partial = disconnected.shortestDistances(0);
        assertThat(partial[1] == 5 && partial[2] == Integer.MAX_VALUE, "vertex 2 is unreachable");

        // a cheaper route discovered LATE must still win -- this is what
        // lazy deletion has to get right
        Dijkstra_1 late = new Dijkstra_1(4);
        late.addEdge(0, 1, 1);
        late.addEdge(0, 2, 10);
        late.addEdge(1, 2, 2);
        late.addEdge(2, 3, 1);
        int[] lateDist = late.shortestDistances(0);
        assertThat(lateDist[2] == 3, "0 -> 1 -> 2 (3) replaces the direct edge (10)");
        assertThat(lateDist[3] == 4, "the improvement propagates onward");

        // negative weights break the algorithm's core assumption
        try {
            new Dijkstra_1(2).addEdge(0, 1, -1);
            assertThat(false, "expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }

        graph.dijkstra(0);
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
