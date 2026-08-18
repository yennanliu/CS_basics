package AlgorithmJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *  TOPOLOGICAL SORT (V1) -- Kahn's algorithm, BFS over in-degrees
 *
 *  Scope: the array-based (int[][] prerequisites) formulation used by
 *         the course-schedule problems. See TopologicalSortV2 for the
 *         same algorithm over an adjacency list of edges, and
 *         algorithm/java/DepthFirstOrder.java for the DFS
 *         reverse-post-order formulation.
 *
 *  A topological order lists the vertices of a DIRECTED ACYCLIC graph
 *  so that every edge points FORWARDS -- if u must come before v, u
 *  appears first. "Which order can I take these courses in?" is the
 *  canonical phrasing.
 *
 *      prerequisites = {{1,0}, {2,0}, {3,1}, {3,2}}
 *      (each pair is {course, itsPrerequisite})
 *
 *              0
 *             / \          0 unlocks 1 and 2
 *            1   2         1 and 2 unlock 3
 *             \ /
 *              3           order: 0, 1, 2, 3
 *
 *  THE TWO STRUCTURES -- and why each exists:
 *
 *    graph      src -> [dests]   who becomes available when src is done
 *    inDegree   per vertex       how many prerequisites are still unmet;
 *                                inDegree == 0 means "takeable NOW"
 *
 *  THE ALGORITHM
 *    1) queue every vertex with inDegree 0 -- they need nothing
 *    2) pop one, append it to the order (it was takeable, so this is safe)
 *    3) for each vertex it unlocks, decrement inDegree; if that hits 0,
 *       queue it
 *    4) repeat until the queue is empty
 *
 *  CYCLE DETECTION FALLS OUT FOR FREE: in a cycle every vertex waits on
 *  another, so none ever reaches inDegree 0. If the output is shorter
 *  than the vertex count, the graph is not a DAG and no ordering exists.
 *
 *  NOTE the ordering is not unique -- any vertex with inDegree 0 may be
 *  taken next. Both 0,1,2,3 and 0,2,1,3 are valid above.
 *
 *  Used by: LC 207 Course Schedule, LC 210 Course Schedule II,
 *           LC 269 Alien Dictionary, LC 444 Sequence Reconstruction.
 *
 *  Time  : O(V + E)  -- each vertex queued once, each edge relaxed once
 *  Space : O(V + E)
 */
public class TopologicalSort {

    /**
     *  Return a valid course order, or an EMPTY array if the graph has
     *  a cycle.
     *
     *  @param prerequisites pairs {course, itsPrerequisite}
     */
    public int[] topologicalSort(int numCourses, int[][] prerequisites) {

        // Step 1: build the adjacency list and count in-degrees
        List<List<Integer>> graph = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }

        int[] inDegree = new int[numCourses];
        for (int[] prereq : prerequisites) {
            // the pair reads {dest, src}: src must be finished before dest
            int dest = prereq[0];
            int src = prereq[1];
            graph.get(src).add(dest);   // finishing src unlocks dest
            inDegree[dest]++;           // dest has one more unmet prerequisite
        }

        // Step 2: seed the queue with everything that needs nothing
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        // Step 3: drain the queue
        int[] topoOrder = new int[numCourses];
        int index = 0;

        while (!queue.isEmpty()) {
            // only in-degree-0 vertices ever enter the queue, so anything
            // popped is safe to append to the order
            int node = queue.poll();
            topoOrder[index++] = node;

            for (int neighbor : graph.get(node)) {
                inDegree[neighbor]--;         // one prerequisite satisfied
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);      // now takeable
                }
            }
        }

        // Step 4: a short order means some vertices never reached
        // in-degree 0 -- they are stuck in a cycle
        if (index != numCourses) {
            return new int[0];
        }
        return topoOrder;
    }

    /** True if every course can be completed (LC 207). */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        return topologicalSort(numCourses, prerequisites).length == numCourses;
    }

    public static void main(String[] args) {
        TopologicalSort topoSort = new TopologicalSort();

        //     0
        //    / \
        //   1   2
        //    \ /
        //     3
        int[][] diamond = {{1, 0}, {3, 1}, {2, 0}, {3, 2}};
        int[] order = topoSort.topologicalSort(4, diamond);

        assertThat(order.length == 4, "every course appears exactly once");
        assertThat(order[0] == 0, "0 has no prerequisites, so it comes first");
        assertThat(order[3] == 3, "3 depends on everything, so it comes last");
        // the ordering is not unique, so check the PROPERTY, not one answer
        assertThat(respectsPrerequisites(order, diamond), "every prerequisite comes first");
        assertThat(topoSort.canFinish(4, diamond), "the diamond is completable");

        // a single chain has exactly one valid order
        int[][] chain = {{1, 0}, {2, 1}, {3, 2}};
        assertThat(Arrays.toString(topoSort.topologicalSort(4, chain)).equals("[0, 1, 2, 3]"),
                "a chain forces one order");

        // no prerequisites at all: everything is takeable immediately
        assertThat(topoSort.topologicalSort(3, new int[0][]).length == 3, "no edges");

        // a cycle has no valid ordering
        int[][] cycle = {{1, 0}, {0, 1}};
        assertThat(topoSort.topologicalSort(2, cycle).length == 0, "0 and 1 need each other");
        assertThat(!topoSort.canFinish(2, cycle), "a cycle cannot be completed");

        // a self-loop is a cycle too
        assertThat(topoSort.topologicalSort(1, new int[][] {{0, 0}}).length == 0, "self-loop");

        // a cycle among only SOME vertices still blocks the whole sort
        int[][] partialCycle = {{1, 0}, {2, 1}, {1, 2}};
        assertThat(topoSort.topologicalSort(3, partialCycle).length == 0, "1 and 2 deadlock");

        System.out.println("Topological Order: " + Arrays.toString(order));
        System.out.println("Success.");
    }

    /** Every prerequisite must appear before the course that needs it. */
    private static boolean respectsPrerequisites(int[] order, int[][] prerequisites) {
        int[] position = new int[order.length];
        for (int i = 0; i < order.length; i++) {
            position[order[i]] = i;
        }
        for (int[] prereq : prerequisites) {
            if (position[prereq[1]] > position[prereq[0]]) {
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
