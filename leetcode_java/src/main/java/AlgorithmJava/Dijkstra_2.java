package AlgorithmJava;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *  DIJKSTRA (V2) -- LC 743 Network Delay Time
 *
 *  Scope: the DIRECTED, edge-array shape LeetCode hands you, and the
 *         "how long until EVERY node is reached?" question. See
 *         Dijkstra_1 for the reusable undirected graph class and
 *         algorithm/java/DijkstraSP.java for path reconstruction.
 *
 *  LC 743: a signal starts at node k and travels along directed edges
 *  {source, target, time}. How long until all n nodes receive it?
 *
 *  THE REFRAMING that makes it a one-liner over Dijkstra: a node hears
 *  the signal at exactly its shortest-path distance from k, and the
 *  broadcast finishes when the LAST node hears it. So the answer is
 *
 *      max over all nodes of distance(k, node)
 *
 *  and because Dijkstra settles nodes in non-decreasing distance order,
 *  that maximum is simply the distance of the node settled LAST -- no
 *  separate scan is needed. If some node is never settled it is
 *  unreachable, and the answer is -1.
 *
 *      times = [[2,1,1], [2,3,1], [3,4,1]], n = 4, k = 2
 *
 *          2 --1--> 1
 *          |
 *          1
 *          v
 *          3 --1--> 4        node 4 hears it at t = 2  ->  answer 2
 *
 *  NOTE the DIRECTED edges: unlike Dijkstra_1 each edge is recorded
 *  once, so a signal can reach a node without that node being able to
 *  reply.
 *
 *  Weights are travel times and therefore non-negative, which is what
 *  makes Dijkstra applicable at all.
 *
 *  Time  : O(E log E)
 *  Space : O(V + E)
 */
public class Dijkstra_2 {

    private final int[][] times;   // each row is {source, target, travelTime}
    private final int n;           // nodes are labelled 1..n

    public Dijkstra_2(int[][] times, int n) {
        this.times = times;
        this.n = n;
    }

    /**
     *  Time for a signal starting at k to reach every node, or -1 if
     *  some node is unreachable.
     */
    public int getShortestPath(int k) {
        // Step 1: build the DIRECTED adjacency map, source -> [{target, time}]
        Map<Integer, List<int[]>> edges = new HashMap<>();
        for (int[] time : times) {
            edges.computeIfAbsent(time[0], key -> new ArrayList<>())
                 .add(new int[] {time[1], time[2]});
        }

        // Step 2: min-heap of {timeSoFar, node}, smallest time first
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minHeap.offer(new int[] {0, k});

        Set<Integer> visited = new HashSet<>();
        int elapsed = 0;

        // Step 3: settle nodes in non-decreasing distance order
        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int timeSoFar = current[0];
            int node = current[1];

            // lazy deletion: a cheaper entry for this node was settled
            // earlier, so this one is stale
            if (visited.contains(node)) {
                continue;
            }
            visited.add(node);

            // nodes come off the heap in increasing distance, so the
            // last one settled carries the maximum
            elapsed = timeSoFar;

            for (int[] next : edges.getOrDefault(node, List.of())) {
                int neighbor = next[0];
                int travelTime = next[1];
                if (!visited.contains(neighbor)) {
                    minHeap.offer(new int[] {timeSoFar + travelTime, neighbor});
                }
            }
        }

        // Step 4: every node must have been reached
        return visited.size() == n ? elapsed : -1;
    }

    public static void main(String[] args) {
        // LC 743 example: 2 -> 1, 2 -> 3 -> 4
        int[][] times = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        assertThat(new Dijkstra_2(times, 4).getShortestPath(2) == 2,
                "the last node hears it at t = 2");

        // a single node, no edges: it already has the signal
        assertThat(new Dijkstra_2(new int[0][], 1).getShortestPath(1) == 0, "one node, t = 0");

        // node 2 cannot be reached from node 1
        assertThat(new Dijkstra_2(new int[0][], 2).getShortestPath(1) == -1, "unreachable node");

        // edges are DIRECTED: 1 -> 2 does not let 2 reach 1
        int[][] oneWay = {{1, 2, 1}};
        assertThat(new Dijkstra_2(oneWay, 2).getShortestPath(1) == 1, "downstream works");
        assertThat(new Dijkstra_2(oneWay, 2).getShortestPath(2) == -1, "upstream does not");

        // the answer is the MAXIMUM distance, not the sum or the last edge
        int[][] uneven = {{1, 2, 1}, {1, 3, 100}};
        assertThat(new Dijkstra_2(uneven, 3).getShortestPath(1) == 100, "the slowest node decides");

        // a shorter multi-hop route must beat a long direct edge
        int[][] detour = {{1, 2, 1}, {2, 3, 1}, {1, 3, 100}};
        assertThat(new Dijkstra_2(detour, 3).getShortestPath(1) == 2, "1 -> 2 -> 3 (2) beats 1 -> 3 (100)");

        System.out.println("network delay time: " + new Dijkstra_2(times, 4).getShortestPath(2));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
