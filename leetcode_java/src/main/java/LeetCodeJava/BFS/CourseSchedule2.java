package LeetCodeJava.BFS;

// https://leetcode.com/problems/course-schedule-ii/description/

import java.util.*;

/**
 * 210. Course Schedule II
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.
 *
 * For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
 * Return the ordering of courses you should take to finish all courses. If there are many valid answers, return any of them. If it is impossible to finish all courses, return an empty array.
 *
 *
 *
 * Example 1:
 *
 * Input: numCourses = 2, prerequisites = [[1,0]]
 * Output: [0,1]
 * Explanation: There are a total of 2 courses to take. To take course 1 you should have finished course 0. So the correct course order is [0,1].
 * Example 2:
 *
 * Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 * Output: [0,2,1,3]
 * Explanation: There are a total of 4 courses to take. To take course 3 you should have finished both courses 1 and 2. Both courses 1 and 2 should be taken after you finished course 0.
 * So one correct course order is [0,1,2,3]. Another correct ordering is [0,2,1,3].
 * Example 3:
 *
 * Input: numCourses = 1, prerequisites = []
 * Output: [0]
 *
 *
 * Constraints:
 *
 * 1 <= numCourses <= 2000
 * 0 <= prerequisites.length <= numCourses * (numCourses - 1)
 * prerequisites[i].length == 2
 * 0 <= ai, bi < numCourses
 * ai != bi
 * All the pairs [ai, bi] are distinct.
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 1.2M
 * Submissions
 *
 */
public class CourseSchedule2 {

    // V0
    // IDEA : TOPOLOGICAL SORT (fixed by gpt)
    // ref : https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/TopologicalSortV2.java
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        if (numCourses == 1) {
            return new int[]{0};
        }

        // topologic ordering

        //return topoOrdering(numCourses, prerequisites);
        List<Integer> ordering = topologicalSort(numCourses, prerequisites);
        //System.out.println(">>> ordering = " + ordering);
        if (ordering == null){
            return new int[]{};
        }
        int[] res = new int[numCourses];
        for (int x = 0; x < ordering.size(); x++) {
            int val = ordering.get(x);
            //System.out.println(val);
            res[x] = val;
        }

        return res;
    }

    public List<Integer> topologicalSort(int numNodes, int[][] edges) {
        // Step 1: Build the graph and calculate in-degrees
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] inDegree = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            graph.get(from).add(to);
            inDegree[to]++;
        }

        // Step 2: Initialize a queue with nodes that have in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numNodes; i++) {
            /**
             * NOTE !!!
             *
             *  we add ALL nodes with degree = 0 to queue at init step
             */
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();

        // Step 3: Process the nodes in topological order
        while (!queue.isEmpty()) {
            /**
             * NOTE !!!
             *
             *  ONLY "degree = 0"  nodes CAN be added to queue
             *
             *  -> so we can add whatever node from queue to final result (topologicalOrder)
             */
            int current = queue.poll();
            topologicalOrder.add(current);

            for (int neighbor : graph.get(current)) {
                inDegree[neighbor] -= 1;
                /**
                 * NOTE !!!
                 *
                 *  if a node "degree = 0"  means this node can be ACCESSED now,
                 *
                 *  -> so we need to add it to the queue (for adding to topologicalOrder in the following while loop iteration)
                 */
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // If topologicalOrder does not contain all nodes, there was a cycle in the graph
        if (topologicalOrder.size() != numNodes) {
            //throw new IllegalArgumentException("The graph has a cycle, so topological sort is not possible.");
            return null;
        }

        /** NOTE !!! reverse ordering */
        Collections.reverse(topologicalOrder);
        return topologicalOrder;
    }

    // V1

    // V2
}
