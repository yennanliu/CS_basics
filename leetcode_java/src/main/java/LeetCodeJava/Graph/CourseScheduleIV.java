package LeetCodeJava.Graph;

// https://leetcode.com/problems/course-schedule-iv/description/

import java.util.*;

/**
 *
 * 1462. Course Schedule IV
 * Medium
 * Topics
 * Companies
 * Hint
 * There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course ai first if you want to take course bi.
 *
 * For example, the pair [0, 1] indicates that you have to take course 0 before you can take course 1.
 * Prerequisites can also be indirect. If course a is a prerequisite of course b, and course b is a prerequisite of course c, then course a is a prerequisite of course c.
 *
 * You are also given an array queries where queries[j] = [uj, vj]. For the jth query, you should answer whether course uj is a prerequisite of course vj or not.
 *
 * Return a boolean array answer, where answer[j] is the answer to the jth query.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: numCourses = 2, prerequisites = [[1,0]], queries = [[0,1],[1,0]]
 * Output: [false,true]
 * Explanation: The pair [1, 0] indicates that you have to take course 1 before you can take course 0.
 * Course 0 is not a prerequisite of course 1, but the opposite is true.
 * Example 2:
 *
 * Input: numCourses = 2, prerequisites = [], queries = [[1,0],[0,1]]
 * Output: [false,false]
 * Explanation: There are no prerequisites, and each course is independent.
 * Example 3:
 *
 *
 * Input: numCourses = 3, prerequisites = [[1,2],[1,0],[2,0]], queries = [[1,0],[1,2]]
 * Output: [true,true]
 *
 *
 * Constraints:
 *
 * 2 <= numCourses <= 100
 * 0 <= prerequisites.length <= (numCourses * (numCourses - 1) / 2)
 * prerequisites[i].length == 2
 * 0 <= ai, bi <= numCourses - 1
 * ai != bi
 * All the pairs [ai, bi] are unique.
 * The prerequisites graph has no cycles.
 * 1 <= queries.length <= 104
 * 0 <= ui, vi <= numCourses - 1
 * ui != vi
 *
 *
 */
public class CourseScheduleIV {

    // V0
//    public List<Boolean> checkIfPrerequisite(int numCourses, int[][] prerequisites, int[][] queries) {
//
//    }

    // V0-1-1
    // TODO: fix below:
//    public List<Boolean> checkIfPrerequisite(int numCourses, int[][] prerequisites, int[][] queries) {
//
//        List<Boolean> res = new ArrayList<>();
//        // edge
//        if(numCourses <= 1){
//            return res;
//        }
////        for(int i = 0; i < queries.length; i++){
////            res.add(false);
////        }
//        if(prerequisites == null || prerequisites.length == 0){
//            for(int i = 0; i < queries.length; i++){
//                res.add(false);
//            }
//            return res;
//        }
//
//        // get `topological order
//        List<Integer> topoOrder = myTopoSort2(numCourses, prerequisites);
//        if(topoOrder == null){
//            for(int i = 0; i < queries.length; i++){
//                res.add(false);
//            }
//            return res;
//        }
//
//        // order -> hashmap {val : idx}
//        Map<Integer, Integer> orderMap = new HashMap<>();
//        for(int i = 0; i < topoOrder.size(); i++){
//            orderMap.put(topoOrder.get(i), i);
//        }
//
//        // update result
//        for(int i = 0; i < queries.length; i++){
//            int[] query = queries[i];
//            int pre_idx = orderMap.getOrDefault(query[0], -1);
//            int cur_idx = orderMap.getOrDefault(query[1], -1);
//            //return pre_idx < cur_idx;
//            res.add(pre_idx < cur_idx);
//        }
//
//        return res;
//    }
//
//    public List<Integer> myTopoSort2(int numCourses, int[][] prerequisites){
//
//        // init degrees
//        int[] degrees = new int[numCourses]; // init val as 0 ????
//        // init preMap
//        Map<Integer, List<Integer>> preMap = new HashMap<>();
//
//        // update degrees, map
//        for(int[] p: prerequisites){
//            int cur = p[0];
//            int prev = p[1];
//
//            List<Integer> preList = preMap.getOrDefault(cur, new ArrayList<>());
//            preList.add(prev);
//            preMap.put(cur, preList);
//
//            // NOTE !!! for `preMap`, we update `prev`
//            degrees[prev] += 1;
//        }
//
//        Queue<Integer> q = new LinkedList<>();
//        // add all `0 degree` to queue
//        for(int i = 0; i < degrees.length; i++){
//            if(degrees[i] == 0){
//                q.add(i);
//            }
//        }
//
//        List<Integer> cache = new ArrayList<>();
//
//        while(!q.isEmpty()){
//            int cur = q.poll();
//            // add to cache
//            cache.add(cur);
//
//            if(preMap.containsKey(cur)){
//                for(int x: preMap.get(cur)){
//                    degrees[x] -= 1;
//                    if(degrees[x] == 0){
//                        // add to queue
//                        q.add(x);
//                    }
//                }
//            }
//        }
//
//        // final `validation` !!!
//        if(cache.size() != numCourses){
//            return null;
//        }
//
//        // reverse
//        Collections.reverse(cache);
//
//        return cache;
//    }

    // V0-1
    // IDEA: TOPOLOGICAL SORT (fixed by gpt)
    public List<Boolean> checkIfPrerequisite_0_1(int numCourses, int[][] prerequisites, int[][] queries) {
        List<Boolean> res = new ArrayList<>();

        // Step 1: Build graph and indegree array
        List<Integer>[] graph = new List[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }
        int[] indegree = new int[numCourses];
        for (int[] pre : prerequisites) {
            graph[pre[0]].add(pre[1]);
            indegree[pre[1]]++;
        }

        // Step 2: Topological sort (Kahn's algorithm)
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0)
                queue.offer(i);
        }

        // Step 3: Prerequisite sets
        Set<Integer>[] prereqSets = new HashSet[numCourses];
        for (int i = 0; i < numCourses; i++) {
            prereqSets[i] = new HashSet<>();
        }

        while (!queue.isEmpty()) {
            int course = queue.poll();
            for (int next : graph[course]) {
                // Add current course and all its prerequisites to the next course
                prereqSets[next].add(course);
                prereqSets[next].addAll(prereqSets[course]);

                // Decrease indegree and add to queue if it hits zero
                if (--indegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        // Step 4: Answer queries
        for (int[] q : queries) {
            res.add(prereqSets[q[1]].contains(q[0]));
        }

        return res;
    }

    // V0-2
    // IDEA: DFS (fixed by gpt)
    public List<Boolean> checkIfPrerequisite_0_2(int numCourses, int[][] prerequisites, int[][] queries) {
        List<Boolean> res = new ArrayList<>();

        // Adjacency list
        List<Integer>[] graph = new List[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] pre : prerequisites) {
            graph[pre[0]].add(pre[1]);
        }

        // Reachability set for each node
        Set<Integer>[] reachable = new HashSet[numCourses];
        for (int i = 0; i < numCourses; i++) {
            reachable[i] = new HashSet<>();
        }

        // DFS to fill reachability
        for (int i = 0; i < numCourses; i++) {
            dfs(i, graph, reachable, new boolean[numCourses]);
        }

        // Answer queries
        for (int[] q : queries) {
            res.add(reachable[q[0]].contains(q[1]));
        }

        return res;
    }

    private void dfs(int course, List<Integer>[] graph, Set<Integer>[] reachable, boolean[] visited) {
        if (!reachable[course].isEmpty())
            return;

        visited[course] = true;

        for (int neighbor : graph[course]) {
            if (!visited[neighbor]) {
                dfs(neighbor, graph, reachable, visited);
            }
            reachable[course].add(neighbor);
            reachable[course].addAll(reachable[neighbor]);
        }

        visited[course] = false;
    }


    // V1
    // https://www.youtube.com/watch?v=cEW05ofxhn0
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F1462-course-schedule-iv.java
    public List<Boolean> checkIfPrerequisite_1(int numCourses, int[][] prerequisites, int[][] queries) {
        HashMap<Integer, List<Integer>> hm = new HashMap<>();
        List<Boolean> res = new ArrayList<>();
        boolean[] visited = new boolean[numCourses];
        for (int i = 0; i < prerequisites.length; i++) {
            hm.putIfAbsent(prerequisites[i][1], new ArrayList<>());
            hm.get(prerequisites[i][1]).add(prerequisites[i][0]);
        }
        for (int i = 0; i < queries.length; i++) {
            visited = new boolean[numCourses];
            res.add(dfs(hm, queries[i][1], queries[i][0], visited));
        }
        return res;
    }

    boolean dfs(HashMap<Integer, List<Integer>> hm, int s, int target, boolean[] visited) {
        if (!hm.containsKey(s)) return false;
        if (hm.get(s).contains(target)) return true;

        for (int i: hm.get(s)) {
            if (visited[i]) continue;
            visited[i] = true;
            if (dfs(hm, i, target, visited)) return true;
        }
        return false;
    }


    // V2-1
    // https://leetcode.com/problems/course-schedule-iv/editorial/
    // IDEA: Tree Traversal - On Demand
    // Performs DFS and returns true if there's a path between src and target
    // and false otherwise.
    private boolean isPrerequisite(
            Map<Integer, List<Integer>> adjList,
            boolean[] visited,
            int src,
            int target
    ) {
        visited[src] = true;

        if (src == target) {
            return true;
        }

        boolean answer = false;
        List<Integer> neighbors = adjList.getOrDefault(src, new ArrayList<>());
        for (int adj : neighbors) {
            if (!visited[adj]) {
                answer =
                        answer || isPrerequisite(adjList, visited, adj, target);
            }
        }
        return answer;
    }

    public List<Boolean> checkIfPrerequisite_2_1(
            int numCourses,
            int[][] prerequisites,
            int[][] queries
    ) {
        Map<Integer, List<Integer>> adjList = new HashMap<>();

        for (int[] edge : prerequisites) {
            adjList
                    .computeIfAbsent(edge[0], k -> new ArrayList<>())
                    .add(edge[1]);
        }

        List<Boolean> result = new ArrayList<>();
        for (int i = 0; i < queries.length; i++) {
            // Reset the visited array for each query
            boolean[] visited = new boolean[numCourses];
            result.add(
                    isPrerequisite(adjList, visited, queries[i][0], queries[i][1])
            );
        }

        return result;
    }


    // V2-2
    // https://leetcode.com/problems/course-schedule-iv/editorial/
    // IDEA: Tree Traversal - Preprocessed
    // Iterate over each node and perform BFS starting from it.
    // Mark the starting node as a prerequisite to all the nodes in the BFS
    // traversal.
    private void preprocess(
            int numCourses,
            int[][] prerequisites,
            Map<Integer, List<Integer>> adjList,
            boolean[][] isPrerequisite
    ) {
        for (int i = 0; i < numCourses; i++) {
            Queue<Integer> q = new LinkedList<>();
            q.offer(i);

            while (!q.isEmpty()) {
                int node = q.poll();

                for (int adj : adjList.getOrDefault(node, new ArrayList<>())) {
                    // If we have marked i as a prerequisite of adj it implies we
                    // have visited it. This is equivalent to using a visited
                    // array.
                    if (!isPrerequisite[i][adj]) {
                        isPrerequisite[i][adj] = true;
                        q.offer(adj);
                    }
                }
            }
        }
    }

    public List<Boolean> checkIfPrerequisite_2_2(
            int numCourses,
            int[][] prerequisites,
            int[][] queries
    ) {
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        for (int[] edge : prerequisites) {
            adjList
                    .computeIfAbsent(edge[0], k -> new ArrayList<>())
                    .add(edge[1]);
        }

        boolean[][] isPrerequisite = new boolean[numCourses][numCourses];
        preprocess(numCourses, prerequisites, adjList, isPrerequisite);

        List<Boolean> answer = new ArrayList<>();
        for (int[] query : queries) {
            answer.add(isPrerequisite[query[0]][query[1]]);
        }

        return answer;
    }


    // V2-3
    // https://leetcode.com/problems/course-schedule-iv/editorial/
    // IDEA: Topological Sort - Kahn's Algorithm
    public List<Boolean> checkIfPrerequisite_2_3(
            int numCourses,
            int[][] prerequisites,
            int[][] queries
    ) {
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        int[] indegree = new int[numCourses];

        for (int[] edge : prerequisites) {
            adjList
                    .computeIfAbsent(edge[0], k -> new ArrayList<>())
                    .add(edge[1]);
            indegree[edge[1]]++;
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                q.offer(i);
            }
        }

        // Map from the node as key to the set of prerequisite nodes.
        Map<Integer, Set<Integer>> nodePrerequisites = new HashMap<>();

        while (!q.isEmpty()) {
            int node = q.poll();

            for (int adj : adjList.getOrDefault(node, new ArrayList<>())) {
                // Add node and prerequisites of the node to the prerequisites of adj
                nodePrerequisites
                        .computeIfAbsent(adj, k -> new HashSet<>())
                        .add(node);
                for (int prereq : nodePrerequisites.getOrDefault(
                        node,
                        new HashSet<>()
                )) {
                    nodePrerequisites.get(adj).add(prereq);
                }

                indegree[adj]--;
                if (indegree[adj] == 0) {
                    q.offer(adj);
                }
            }
        }

        List<Boolean> answer = new ArrayList<>();
        for (int[] query : queries) {
            answer.add(
                    nodePrerequisites
                            .getOrDefault(query[1], new HashSet<>())
                            .contains(query[0])
            );
        }

        return answer;
    }


    // V2-4
    // https://leetcode.com/problems/course-schedule-iv/editorial/
    // IDEA: Floyd Warshall Algorithm
    public List<Boolean> checkIfPrerequisite_2_4(
            int numCourses,
            int[][] prerequisites,
            int[][] queries
    ) {
        boolean[][] isPrerequisite = new boolean[numCourses][numCourses];

        for (int[] edge : prerequisites) {
            isPrerequisite[edge[0]][edge[1]] = true;
        }

        for (int intermediate = 0; intermediate < numCourses; intermediate++) {
            for (int src = 0; src < numCourses; src++) {
                for (int target = 0; target < numCourses; target++) {
                    // If there is a path i -> intermediate and intermediate -> j, then i -> j exists as well.
                    isPrerequisite[src][target] =
                            isPrerequisite[src][target] ||
                                    (isPrerequisite[src][intermediate] &&
                                            isPrerequisite[intermediate][target]);
                }
            }
        }

        List<Boolean> answer = new ArrayList<>();
        for (int[] query : queries) {
            answer.add(isPrerequisite[query[0]][query[1]]);
        }

        return answer;
    }


    // V3

}
