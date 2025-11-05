package LeetCodeJava.BFS;

// https://leetcode.com/problems/possible-bipartition/description/

import java.util.*;

/**
 * 886. Possible Bipartition
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * We want to split a group of n people (labeled from 1 to n) into two groups of any size. Each person may dislike some other people, and they should not go into the same group.
 *
 * Given the integer n and the array dislikes where dislikes[i] = [ai, bi] indicates that the person labeled ai does not like the person labeled bi, return true if it is possible to split everyone into two groups in this way.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 4, dislikes = [[1,2],[1,3],[2,4]]
 * Output: true
 * Explanation: The first group has [1,4], and the second group has [2,3].
 * Example 2:
 *
 * Input: n = 3, dislikes = [[1,2],[1,3],[2,3]]
 * Output: false
 * Explanation: We need at least 3 groups to divide them. We cannot put them in two groups.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 2000
 * 0 <= dislikes.length <= 104
 * dislikes[i].length == 2
 * 1 <= ai < bi <= n
 * All the pairs of dislikes are unique.
 *
 *
 */
public class PossibleBipartition {

    // V0
    // IDEA: LC 785 + DFS + NEIGHBOR CHECK (fixed by gpt)
    public boolean possibleBipartition(int n, int[][] dislikes) {
        // build adjacency list
        Map<Integer, List<Integer>> graph = new HashMap<>();

        /**  NOTE !!! we use below structure, which is more simple, and fast-implement
         *
         *   -> Instead of below:
         *
         *          Map<Integer, List<Integer>> dislikeMap = new HashMap<>();
         *         for(int[] d: dislikes){
         *             int a = d[0];
         *             int b = d[1];
         *             List<Integer> list1 = new ArrayList<>();
         *             List<Integer> list2 = new ArrayList<>();
         *
         *             // ???
         *             people.add(a);
         *             people.add(b);
         *
         *             if(dislikeMap.containsKey(a)){
         *                 list1 = dislikeMap.get(a);
         *             }
         *             list1.add(b);
         *             dislikeMap.put(a, list1);
         *
         *             if(dislikeMap.containsKey(b)){
         *                 list2 = dislikeMap.get(b);
         *             }
         *             list2.add(a);
         *             dislikeMap.put(b, list2);
         *         }
         */
        for (int i = 1; i <= n; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] d : dislikes) {
            /** NOTE !!!! below append to map syntax */


            /** V1 */
            graph.get(d[0]).add(d[1]);
            graph.get(d[1]).add(d[0]);

            /** V2 */
//            List<Integer> list1 = dislikeMap.get(a);
//            List<Integer> list2 = dislikeMap.get(b);
//
//            list1.add(b);
//            list2.add(a);
//
//            dislikeMap.put(a, list1);
//            dislikeMap.put(b, list2);
        }


        /** NOTE !!!
         *
         *  we define color status (as below), use array.
         *  instead of using hashMap
         *
         *  NOTE !!!
         *
         *   0 = unvisited
         *   1 = group A
         *   -1 = group B
         */
        // color array: 0 = unvisited, 1 = group A, -1 = group B
        /** NOTE !!! since n is 1 based, so we use `new int[n + 1]` */
        int[] color = new int[n + 1];

        // DFS each component
        for (int i = 1; i <= n; i++) {
            /** NOTE !!!
             *
             *  ONLY if the person if NOT grouped yet (color),
             *  then we call dfs
             *
             *
             *  NOTE !!!
             *
             *   assign to `team 1` by default
             */
            if (color[i] == 0) {
                if (!dfs(i, 1, color, graph)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean dfs(int node, int c, int[] color, Map<Integer, List<Integer>> graph) {
        /** NOTE !!!
         *
         *  set the people group (`color`)
         */
        color[node] = c;

        /** NOTE !!!
         *
         *  we loop over dislike neighbors (but NOT loop over 1... n)
         *
         *  ---
         *
         *  reason:
         *
         *  // NOTE !!! since the neighbor are the person does NOT like current node
         *  // so we need to assign them to different team
         *  // do above via dfs call, (and validate if it's possible)
         */
        for (int nei : graph.get(node)) {
            /** NOTE !!!
             *
             *  ONLY if the person if NOT grouped yet (color),
             *  then we call dfs
             */
            if (color[nei] == c) {
                // same color conflict
                return false;
            }
            /** NOTE !!!
             *
             *  we check if color (group) conflicted when go over nodes
             *  and check the color status (with the node).
             *
             *
             *  Instead of checking at beginning (before for loop)
             */
            if (color[nei] == 0) {
                // NOTE !!! assign to `team -1 * c`
                // since in the prev call. we assign prev node to `team c`
                if (!dfs(nei, -c, color, graph)) {
                    return false;
                }
            }
        }
        return true;
    }

    // V0-0-1
    // IDEA: DFS + LC 785 (fixed by gpt)
    public boolean possibleBipartition_0_0_1(int n, int[][] dislikes) {
        // Build adjacency list
        Map<Integer, List<Integer>> dislikeMap = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            dislikeMap.put(i, new ArrayList<>());
        }

        for (int[] d : dislikes) {
            int a = d[0], b = d[1];
            dislikeMap.get(a).add(b);
            dislikeMap.get(b).add(a);
        }

        // 0 = unassigned, 1 = group A, -1 = group B
        int[] groupStatus = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            if (groupStatus[i] == 0) {
                if (!dfs_0_0_1(i, 1, groupStatus, dislikeMap)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean dfs_0_0_1(int node, int team, int[] groupStatus, Map<Integer, List<Integer>> dislikeMap) {
        if (groupStatus[node] != 0) {
            // already assigned, check consistency
            return groupStatus[node] == team;
        }

        // assign this node
        groupStatus[node] = team;

        // assign opposite team to all neighbors
        for (int nei : dislikeMap.get(node)) {
            if (!dfs_0_0_1(nei, -team, groupStatus, dislikeMap)) {
                return false;
            }
        }

        return true;
    }

    // V0-1
    // IDEA: LC 785 + HASH SAP + DFS (fixed by gemini)
    /**
     * Main function: Checks if the graph can be split into two groups.
     */
    public boolean possibleBipartition_0_1(int n, int[][] dislikes) {
        // --- FIX: Edge Cases ---
        // If there are no dislikes, it's always possible.
        if (dislikes == null || dislikes.length == 0) {
            return true;
        }
        // If there's only one person, it's also always possible.
        if (n <= 1) {
            return true;
        }

        // --- 1. Build the Adjacency List (Graph) ---
        // Your graph-building logic was correct.
        Map<Integer, List<Integer>> dislikeMap = new HashMap<>();
        for (int[] d : dislikes) {
            int a = d[0];
            int b = d[1];

            // Add edges for both directions
            dislikeMap.putIfAbsent(a, new ArrayList<>());
            dislikeMap.get(a).add(b);

            dislikeMap.putIfAbsent(b, new ArrayList<>());
            dislikeMap.get(b).add(a);
        }

        // --- 2. Create a "colors" array to track groups ---
        // People are 1-indexed, so we use n + 1 size.
        // 0 = Unvisited
        // 1 = Group 1
        //-1 = Group 2
        int[] colors = new int[n + 1];

        // --- 3. Iterate through all people ---
        // We must loop from 1 to n to handle disconnected components.
        for (int i = 1; i <= n; i++) {
            // If this person is uncolored, start a new DFS coloring.
            if (colors[i] == 0) {
                // Try to color them with '1'. If it fails, return false.
                if (!canColor(i, 1, dislikeMap, colors)) {
                    return false;
                }
            }
        }

        // If all components were colored successfully
        return true;
    }

    /**
     * --- FIX: Correct DFS Graph Coloring Helper ---
     * * @param person The current person (node) to color.
     * @param color The color (1 or -1) to assign to 'person'.
     * @param dislikeMap The adjacency list (graph).
     * @param colors The array tracking the colors of all people.
     * @return true if coloring is possible, false if a conflict is found.
     */
    private boolean canColor(int person, int color, Map<Integer, List<Integer>> dislikeMap, int[] colors) {

        // 1. Assign the color to the current person.
        colors[person] = color;

        // 2. Check all neighbors (people they dislike).
        // If a person dislikes no one, they won't be in the map's keys.
        if (dislikeMap.containsKey(person)) {
            for (int neighbor : dislikeMap.get(person)) {

                // Case A: The neighbor is uncolored (0)
                if (colors[neighbor] == 0) {
                    // Try to color the neighbor with the *opposite* color (-color).
                    // If that recursive call fails, propagate the failure.
                    if (!canColor(neighbor, -color, dislikeMap, colors)) {
                        return false;
                    }
                }
                // Case B: The neighbor is already colored
                else if (colors[neighbor] == color) {
                    // CONFLICT! The neighbor has the *same* color.
                    // This means bipartition is impossible.
                    return false;
                }
                // (If colors[neighbor] == -color, it's good, so we continue.)
            }
        }

        // If we get through all neighbors without a conflict, this path is valid.
        return true;
    }


    // V1
    // https://leetcode.ca/2018-05-04-886-Possible-Bipartition/
    private int[] p;
    public boolean possibleBipartition_1(int n, int[][] dislikes) {
        p = new int[n];
        List<Integer>[] g = new List[n];
        Arrays.setAll(g, k -> new ArrayList<>());
        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }
        for (int[] e : dislikes) {
            int a = e[0] - 1, b = e[1] - 1;
            g[a].add(b);
            g[b].add(a);
        }
        for (int i = 0; i < n; ++i) {
            for (int j : g[i]) {
                if (find(i) == find(j)) {
                    return false;
                }
                p[find(j)] = find(g[i].get(0));
            }
        }
        return true;
    }

    private int find(int x) {
        if (p[x] != x) {
            p[x] = find(p[x]);
        }
        return p[x];
    }

    // V2
    // IDEA: BFS
    // https://leetcode.com/problems/possible-bipartition/solutions/7317770/possible-bipartition-solutionbfs-by-ajen-ncyp/
    public boolean possibleBipartition_2(int n, int[][] dislikes) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int d[] : dislikes) {
            graph.get(d[0]).add(d[1]);
            graph.get(d[1]).add(d[0]);
        }
        int col[] = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            col[i] = -1;
        }
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i <= n; i++) {
            if (col[i] == -1) {
                q.add(i);
                col[i] = 0;
                while (!q.isEmpty()) {
                    int curr = q.remove();
                    for (int j = 0; j < graph.get(curr).size(); j++) {
                        int e = graph.get(curr).get(j);
                        if (col[e] == -1) {
                            col[e] = 1 - col[curr];
                            q.add(e);
                        } else if (col[e] == col[curr]) {
                            return false;
                        }
                    }

                }

            }
        }

        return true;
    }


    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/possible-bipartition/solutions/654955/pythoncjava-on-by-dfs-and-coloring-w-gra-vjcq/
    public boolean possibleBipartition_3(int N, int[][] dislikes) {
        List<Integer>[] graph = new List[N + 1];

        for (int i = 1; i <= N; ++i)
            graph[i] = new ArrayList<>();

        for (int[] dislike : dislikes) {
            graph[dislike[0]].add(dislike[1]);
            graph[dislike[1]].add(dislike[0]);
        }

        Integer[] colors = new Integer[N + 1];

        for (int i = 1; i <= N; ++i) {
            // If the connected component that node i belongs to hasn't been colored yet then try coloring it.
            if (colors[i] == null && !dfs(graph, colors, i, 1))
                return false;
        }
        return true;
    }

    private boolean dfs(List<Integer>[] graph, Integer[] colors, int currNode, int currColor) {
        colors[currNode] = currColor;

        // Color all uncolored adjacent nodes.
        for (Integer adjacentNode : graph[currNode]) {

            if (colors[adjacentNode] == null) {
                if (!dfs(graph, colors, adjacentNode, currColor * -1))
                    return false;

            } else if (colors[adjacentNode] == currColor) {
                return false;
            }
        }
        return true;
    }


}
