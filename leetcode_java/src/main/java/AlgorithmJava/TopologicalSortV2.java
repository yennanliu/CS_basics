package AlgorithmJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *  TOPOLOGICAL SORT (V2) -- Kahn's algorithm over an edge list
 *
 *  Scope: the same algorithm as TopologicalSort, taking edges as
 *         List&lt;List&lt;Integer&gt;&gt; {from, to} and returning a List rather
 *         than an int[]. Two differences worth knowing about:
 *
 *    1) EDGE DIRECTION. Here an edge reads {from, to} -- from comes
 *       first. TopologicalSort takes {course, prerequisite}, which is
 *       the REVERSE. Mixing the two up silently produces a backwards
 *       ordering, and is the single most common bug in these problems.
 *
 *    2) FAILURE MODE. A cycle throws IllegalArgumentException here,
 *       instead of returning an empty result. Loud failure suits a
 *       library; a sentinel suits LeetCode's return type.
 *
 *      numNodes = 4, edges = [[0,1], [0,2], [1,3], [2,3]]
 *
 *              0
 *             / \          order: 0, 1, 2, 3
 *            1   2
 *             \ /
 *              3
 *
 *  THE ALGORITHM
 *    1) queue every node with in-degree 0 -- nothing points at them
 *    2) pop one, append it to the order
 *    3) for each node it points at, decrement in-degree; if that hits 0,
 *       queue it
 *    4) a short result means some nodes never reached in-degree 0, i.e.
 *       the graph has a cycle
 *
 *  Time  : O(V + E)
 *  Space : O(V + E)
 */
public class TopologicalSortV2 {

    /**
     *  Order the nodes so that every edge points forwards.
     *
     *  @param edges pairs {from, to}: `from` must come before `to`
     *  @throws IllegalArgumentException if the graph contains a cycle
     */
    public static List<Integer> topologicalSort(int numNodes, List<List<Integer>> edges) {

        // Step 1: build the adjacency map and count in-degrees
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i < numNodes; i++) {
            graph.put(i, new ArrayList<>());
        }

        int[] inDegree = new int[numNodes];
        for (List<Integer> edge : edges) {
            int from = edge.get(0);
            int to = edge.get(1);
            graph.get(from).add(to);
            inDegree[to]++;             // `to` has one more unmet dependency
        }

        // Step 2: seed the queue with EVERY in-degree-0 node, not just one
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numNodes; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        // Step 3: drain the queue
        List<Integer> topologicalOrder = new ArrayList<>(numNodes);
        while (!queue.isEmpty()) {
            // only in-degree-0 nodes ever enter the queue, so anything
            // popped is safe to append to the order
            int current = queue.poll();
            topologicalOrder.add(current);

            for (int neighbor : graph.get(current)) {
                inDegree[neighbor]--;         // one dependency satisfied
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);    // now reachable
                }
            }
        }

        // Step 4: in a cycle every node waits on another, so none of them
        // ever reaches in-degree 0 and the order comes up short
        if (topologicalOrder.size() != numNodes) {
            throw new IllegalArgumentException(
                    "the graph has a cycle, so topological sort is not possible");
        }
        return topologicalOrder;
    }

    public static void main(String[] args) {
        //     0
        //    / \
        //   1   2
        //    \ /
        //     3
        List<List<Integer>> diamond = edges(new int[][] {{0, 1}, {0, 2}, {1, 3}, {2, 3}});
        List<Integer> order = topologicalSort(4, diamond);

        assertThat(order.size() == 4, "every node appears exactly once");
        assertThat(order.get(0) == 0, "0 has no incoming edges, so it comes first");
        assertThat(order.get(3) == 3, "3 depends on everything, so it comes last");
        // the ordering is not unique, so check the PROPERTY, not one answer
        assertThat(respectsEdges(order, diamond), "every edge points forwards");

        // a single chain has exactly one valid order
        List<Integer> chain = topologicalSort(4, edges(new int[][] {{0, 1}, {1, 2}, {2, 3}}));
        assertThat(chain.toString().equals("[0, 1, 2, 3]"), "a chain forces one order");

        // no edges at all: any order is valid
        assertThat(topologicalSort(3, new ArrayList<>()).size() == 3, "no edges");

        // a cycle throws
        try {
            topologicalSort(2, edges(new int[][] {{0, 1}, {1, 0}}));
            assertThat(false, "expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }

        // a cycle among only SOME nodes still blocks the whole sort
        try {
            topologicalSort(3, edges(new int[][] {{0, 1}, {1, 2}, {2, 1}}));
            assertThat(false, "expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }

        System.out.println("Topological Order: " + order);
        System.out.println("Success.");
    }

    private static List<List<Integer>> edges(int[][] pairs) {
        List<List<Integer>> edges = new ArrayList<>();
        for (int[] pair : pairs) {
            edges.add(Arrays.asList(pair[0], pair[1]));
        }
        return edges;
    }

    /** Every edge {from, to} must have `from` before `to`. */
    private static boolean respectsEdges(List<Integer> order, List<List<Integer>> edges) {
        for (List<Integer> edge : edges) {
            if (order.indexOf(edge.get(0)) > order.indexOf(edge.get(1))) {
                return false;
            }
        }
        return true;
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
