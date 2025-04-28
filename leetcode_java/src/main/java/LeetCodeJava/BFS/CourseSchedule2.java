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
    // IDEA : TOPOLOGICAL SORT (cur - pre map) (fixed by gpt)
    /** NOTE !!! we CAN'T use `quick union` for this problem */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // edge
        if (numCourses == 0) {
            return null;
        }
        if (numCourses == 1) {
            return new int[] { 0 };
        }

        // topo sort
        int[] res = courseHelper(numCourses, prerequisites);
        System.out.println(Arrays.toString(res));

        // if res is null, we should ` empty array`; otherwise, return res we got
        return res == null ? new int[] {} : res;
    }

    public int[] courseHelper(int numCourses, int[][] prerequisites) {
        /**
         *
         * NOTE !!!
         *
         *  we define preMap as below:
         *
         *  map : {course_1 : [pre_course_1, pre_course_2, ...] }
         *
         *  so,
         *    - key is cur_course, and
         *    - value is the list of `pre-course`,
         *      which need to completed BEFORE taking cur course
         *
         */
        Map<Integer, List<Integer>> preMap = new HashMap<>();
        int[] degrees = new int[numCourses]; // init val as 0 ???

        for (int[] p : prerequisites) {
            int cur = p[0];
            int prev = p[1];

            // update preMap
            List<Integer> preList = preMap.getOrDefault(cur, new ArrayList<>());
            preList.add(prev);
            preMap.put(cur, preList);

            // update orders
            /**
             *  NOTE !!!
             *
             *   if use `preMap`, we need to update `PREV` 's degree !!!!!
             *
             *
             *   (if use `followingMap), we update cur's degree instead, (check below other approaches)
             *
             */
            //orders[cur] += 1; // this one is WRONG !!!! (for `preMap`)
            degrees[prev] += 1; // NOTE !!!! this
        }

        Queue<Integer> q = new LinkedList<>();
        List<Integer> collected = new ArrayList<>();

        // add all `0 order` to queue
        for (int i = 0; i < degrees.length; i++) {
            if (degrees[i] == 0) {
                q.add(i);
            }
        }

        while (!q.isEmpty()) {

            int cur = q.poll();
            /**
             * NOTE !!! we add cur to tmp result right after pop from queue
             */
            collected.add(cur);

            if (preMap.containsKey(cur)) {
                /**
                 * NOTE !!! we loop over `prev` courses
                 */
                for (int prev : preMap.get(cur)) {
                    /**
                     * NOTE !!! we update `prev` order by `-= 1`
                     */
                    degrees[prev] -= 1;
                    /**
                     * NOTE !!! if `prev` course ordering == 0,
                     *          we add it to queue
                     */
                    if (degrees[prev] == 0) {
                        q.add(prev);
                    }
                }
            }
        }

        // final validation !!! (see if input is validate)
        if (collected.size() != numCourses) {
            return null;
        }

        // reverse
        /**
         * NOTE !!!  we `reverse` collected, so the order is correct
         *           for our final result
         */
        Collections.reverse(collected);

        int[] res = new int[collected.size()];
        for (int i = 0; i < collected.size(); i++) {
            res[i] = collected.get(i);
        }

        return res;
    }

    // V0-1
    // IDEA : TOPOLOGICAL SORT (fixed by gpt)
    /** NOTE !!! we CAN'T use `quick union` for this problem */
    public int[] findOrder_0_1_1(int numCourses, int[][] prerequisites) {
        // Edge case: no prerequisites
        if (prerequisites == null || prerequisites.length == 0) {
            int[] result = new int[numCourses];
            for (int i = 0; i < numCourses; i++) {
                result[i] = i;
            }
            return result;
        }

        // Initialize in-degrees and graph
        int[] inDegree = new int[numCourses];
        /**
         * NOTE !!!
         *
         *  graph : {prev: [next_1, next_2,,,,]
         *
         *  -> so we use prev as key,
         *     array as value that collect all `prev`'s next courses
         *
         */
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (int[] prereq : prerequisites) {
            int next = prereq[0];
            int prev = prereq[1];

            // Build the graph
            graph.putIfAbsent(prev, new ArrayList<>());
            graph.get(prev).add(next);

            // Update in-degree for the next course
            /**
             * NOTE !!!
             *
             *  update `NEXT` course's degree,
             *  since every time when meet a prev-next
             *  means before take that next course, we need to take prev first
             *  so its (next course) degree increase by 1
             */
            inDegree[next] += 1;
        }

        // Initialize queue with courses having in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        // Perform BFS (Kahn's algorithm for topological sorting)
        int[] result = new int[numCourses];
        int idx = 0;

        while (!queue.isEmpty()) {
            int course = queue.poll();
            result[idx++] = course;

            // Process all neighbors of the current course
            if (graph.containsKey(course)) {
                for (int next : graph.get(course)) {
                    /**
                     * NOTE !!!
                     *
                     *  update `NEXT` degree (decrease by 1)
                     *  since its `prev` course just took,
                     */
                    inDegree[next] -= 1; // Decrease in-degree
                    if (inDegree[next] == 0) { // Add to queue if in-degree becomes 0
                        queue.add(next);
                    }
                }
            }
        }

        // If all courses are processed, return the result
        if (idx == numCourses) {
            return result;
        }

        // Otherwise, return an empty array (cycle detected)
        return new int[0];
    }


    // V0-1
    // IDEA : TOPOLOGICAL SORT (fixed by gpt)
    // ref : https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/TopologicalSortV2.java
    public int[] findOrder_0_1(int numCourses, int[][] prerequisites) {
        if (numCourses == 1) {
            return new int[]{0};
        }

        // topologic ordering
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


    // V1-1
    // https://neetcode.io/problems/course-schedule-ii
    // IDEA:  Cycle Detection (DFS)
    public int[] findOrder_1_1(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> prereq = new HashMap<>();
        for (int[] pair : prerequisites) {
            prereq.computeIfAbsent(pair[0],
                    k -> new ArrayList<>()).add(pair[1]);
        }

        List<Integer> output = new ArrayList<>();
        Set<Integer> visit = new HashSet<>();
        Set<Integer> cycle = new HashSet<>();

        for (int course = 0; course < numCourses; course++) {
            if (!dfs(course, prereq, visit, cycle, output)) {
                return new int[0];
            }
        }

        int[] result = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            result[i] = output.get(i);
        }
        return result;
    }

    private boolean dfs(int course, Map<Integer, List<Integer>> prereq,
                        Set<Integer> visit, Set<Integer> cycle,
                        List<Integer> output) {

        if (cycle.contains(course)) {
            return false;
        }
        if (visit.contains(course)) {
            return true;
        }

        cycle.add(course);
        for (int pre : prereq.getOrDefault(course, Collections.emptyList())) {
            if (!dfs(pre, prereq, visit, cycle, output)) {
                return false;
            }
        }
        cycle.remove(course);
        visit.add(course);
        output.add(course);
        return true;
    }

    // V1-2
    // https://neetcode.io/problems/course-schedule-ii
    // IDEA:  Topological Sort (Kahn's Algorithm)
    public int[] findOrder_1_2(int numCourses, int[][] prerequisites) {
        int[] indegree = new int[numCourses];
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] pre : prerequisites) {
            indegree[pre[1]]++;
            adj.get(pre[0]).add(pre[1]);
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }

        int finish = 0;
        int[] output = new int[numCourses];
        while (!q.isEmpty()) {
            int node = q.poll();
            output[numCourses - finish - 1] = node;
            finish++;
            for (int nei : adj.get(node)) {
                indegree[nei]--;
                if (indegree[nei] == 0) {
                    q.add(nei);
                }
            }
        }

        if (finish != numCourses) {
            return new int[0];
        }
        return output;
    }


    // V1-3
    // https://neetcode.io/problems/course-schedule-ii
    // IDEA: Topological Sort (DFS)
    private List<Integer> output = new ArrayList<>();
    private int[] indegree;
    private List<List<Integer>> adj;

    private void dfs(int node) {
        output.add(node);
        indegree[node]--;
        for (int nei : adj.get(node)) {
            indegree[nei]--;
            if (indegree[nei] == 0) {
                dfs(nei);
            }
        }
    }

    public int[] findOrder_1_3(int numCourses, int[][] prerequisites) {
        adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }
        indegree = new int[numCourses];
        for (int[] pre : prerequisites) {
            indegree[pre[0]]++;
            adj.get(pre[1]).add(pre[0]);
        }

        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                dfs(i);
            }
        }

        if (output.size() != numCourses) return new int[0];
        int[] res = new int[output.size()];
        for (int i = 0; i < output.size(); i++) {
            res[i] = output.get(i);
        }
        return res;
    }


    // V2

}
