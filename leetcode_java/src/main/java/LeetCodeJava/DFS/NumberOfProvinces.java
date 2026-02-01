package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-provinces/description/

import java.util.*;

/**
 * 547. Number of Provinces
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * There are n cities. Some of them are connected, while some are not. If city a is connected directly with city b, and city b is connected directly with city c, then city a is connected indirectly with city c.
 *
 * A province is a group of directly or indirectly connected cities and no other cities outside of the group.
 *
 * You are given an n x n matrix isConnected where isConnected[i][j] = 1 if the ith city and the jth city are directly connected, and isConnected[i][j] = 0 otherwise.
 *
 * Return the total number of provinces.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: isConnected = [[1,1,0],[1,1,0],[0,0,1]]
 * Output: 2
 * Example 2:
 *
 *
 * Input: isConnected = [[1,0,0],[0,1,0],[0,0,1]]
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= n <= 200
 * n == isConnected.length
 * n == isConnected[i].length
 * isConnected[i][j] is 1 or 0.
 * isConnected[i][i] == 1
 * isConnected[i][j] == isConnected[j][i]
 *
 */
public class NumberOfProvinces {

    // V0
//    public int findCircleNum(int[][] isConnected) {
//
//    }

    // V0-1
    // IDEA: DFS + HASHMAP
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int findCircleNum_0_1(int[][] isConnected) {
        // edge
        if (isConnected == null || isConnected.length == 0 || isConnected[0].length == 0) {
            return 0;
        }
        if (isConnected.length == 1 && isConnected[0].length == 1) {
            return isConnected[0][0];
        }

        // build graph
        // { val : Set(neighbor_1, neighbor_2, ...) }
        Map<Integer, Set<Integer>> map = new HashMap<>();


        int l = isConnected.length;
        int w = isConnected[0].length;
        /** NOTE !!!
         *
         * // since the `node is from 1 to N`
         * // for any row in int[][] isConnected
         *
         */
        for (int y = 0; y < l; y++) {
            map.put(y + 1, new HashSet<>());
        }

        // ???
        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                // NOTE !!
                /** NOTE !!!
                 *
                 *   no need to add itself (e.g. (0,0), (1,1),...)
                 */
                if (x != y) {
                    if (isConnected[y][x] == 1) {
                        // /??
                        map.get(y + 1).add(x + 1);
                        map.get(x + 1).add(y + 1);
                    }
                }
            }
        }

        //System.out.println(">>> map = " + map);

        boolean[] visited = new boolean[l + 1]; // ???

        int landCnt = 0;

        // run the dfs
        for (int k : map.keySet()) {
            if (!visited[k]) {
                dfsIslandHelper(k, map, visited);
                landCnt += 1;
            }

        }

        return landCnt;
    }

    private boolean dfsIslandHelper(int node, Map<Integer, Set<Integer>> map, boolean[] visited) {

        /** NOTE !!!
         *
         *   mark as `visited`
         */
        visited[node] = true;

        /** NOTE !!!
         *
         *   visit neighbors
         */
        for (Integer x : map.get(node)) {
            if (!visited[x]) {
                dfsIslandHelper(x, map, visited);
            }
        }

        return false;
    }

    // V0-2
    // IDEA: DFS (gemini)
    /**
     * Finds the number of provinces (connected components) using DFS directly on the adjacency matrix.
     * @param isConnected The adjacency matrix where isConnected[i][j] = 1 means provinces i and j are connected.
     * @return The total number of provinces.
     * time = O(N^2)
     * space = O(N)
     */
    public int findCircleNum_0_2(int[][] isConnected) {
        // Edge case checks
        if (isConnected == null || isConnected.length == 0) {
            return 0;
        }

        int N = isConnected.length; // Number of cities/provinces

        // visited array tracks which cities have already been included in a province.
        // We use 0-based indexing (0 to N-1).
        boolean[] visited = new boolean[N];

        int provinceCount = 0;

        // Iterate through every city to find the starting point of a new province.
        for (int i = 0; i < N; i++) {
            // If the city 'i' hasn't been visited, it means we found a new province.
            if (!visited[i]) {
                // Start DFS to explore all connected cities in this new province.
                dfs(isConnected, visited, i, N);

                // Increment the count of provinces found.
                provinceCount++;
            }
        }

        return provinceCount;
    }

    /**
     * Helper function to perform DFS and mark all cities in the current province as visited.
     * @param isConnected The adjacency matrix.
     * @param visited The boolean array tracking visited cities.
     * @param currentCity The current city index (0 to N-1).
     * @param N Total number of cities.
     */
    private void dfs(int[][] isConnected, boolean[] visited, int currentCity, int N) {

        // Mark the current city as visited.
        visited[currentCity] = true;

        // Look at all possible connections (j) from the current city (currentCity).
        for (int j = 0; j < N; j++) {
            // Check if there is a connection AND the neighbor city 'j' hasn't been visited.
            if (isConnected[currentCity][j] == 1 && !visited[j]) {
                // Recursively visit the connected, unvisited city 'j'.
                dfs(isConnected, visited, j, N);
            }
        }
        // Note: The DFS simply marks the visited status and doesn't need to return a value.
    }


    // V0-3
    // IDEA: DFS (gpt)
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int findCircleNum_0_3(int[][] isConnected) {

        // edge
        if (isConnected == null || isConnected.length == 0)
            return 0;
        if (isConnected.length == 1)
            return 1;

        int n = isConnected.length;

        // build graph as adjacency list (0-based)
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(i, new ArrayList<>());
        }

        // fill adjacency list
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && isConnected[i][j] == 1) {
                    map.get(i).add(j);
                    map.get(j).add(i);
                }
            }
        }

        boolean[] visited = new boolean[n];
        int provinces = 0;

        // DFS count connected components
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i, map, visited);
                provinces++;
            }
        }

        return provinces;
    }

    private void dfs(int node, Map<Integer, List<Integer>> map, boolean[] visited) {
        visited[node] = true;
        for (int nei : map.get(node)) {
            if (!visited[nei]) {
                dfs(nei, map, visited);
            }
        }
    }


    // V1-1
    // IDEA: DFS
    // https://leetcode.com/problems/number-of-provinces/editorial/
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public void dfs(int node, int[][] isConnected, boolean[] visit) {
        visit[node] = true;
        for (int i = 0; i < isConnected.length; i++) {
            if (isConnected[node][i] == 1 && !visit[i]) {
                dfs(i, isConnected, visit);
            }
        }
    }

    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int findCircleNum_1_1(int[][] isConnected) {
        int n = isConnected.length;
        int numberOfComponents = 0;
        boolean[] visit = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!visit[i]) {
                numberOfComponents++;
                dfs(i, isConnected, visit);
            }
        }

        return numberOfComponents;
    }


    // V1-2
    // IDEA: BFS
    // https://leetcode.com/problems/number-of-provinces/editorial/
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public void bfs(int node, int[][] isConnected, boolean[] visit) {
        Queue<Integer> q = new LinkedList<>();
        q.offer(node);
        visit[node] = true;

        while (!q.isEmpty()) {
            node = q.poll();

            for (int i = 0; i < isConnected.length; i++) {
                if (isConnected[node][i] == 1 && !visit[i]) {
                    q.offer(i);
                    visit[i] = true;
                }
            }
        }
    }

    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int findCircleNum_1_2(int[][] isConnected) {
        int n = isConnected.length;
        int numberOfComponents = 0;
        boolean[] visit = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!visit[i]) {
                numberOfComponents++;
                bfs(i, isConnected, visit);
            }
        }

        return numberOfComponents;
    }


    // V1-3
    // IDEA: Union-find
    // https://leetcode.com/problems/number-of-provinces/editorial/
    /**
     * time = O(N^2 * α(N))
     * space = O(N)
     */
    class UnionFind {

        int[] parent;
        int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++)
                parent[i] = i;
            rank = new int[size];
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union_set(int x, int y) {
            int xset = find(x), yset = find(y);
            if (xset == yset) {
                return;
            } else if (rank[xset] < rank[yset]) {
                parent[xset] = yset;
            } else if (rank[xset] > rank[yset]) {
                parent[yset] = xset;
            } else {
                parent[yset] = xset;
                rank[xset]++;
            }
        }
    }

    /**
     * time = O(N^2 * α(N))
     * space = O(N)
     */
    public int findCircleNum_1_3(int[][] isConnected) {
        int n = isConnected.length;
        UnionFind dsu = new UnionFind(n);
        int numberOfComponents = n;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1 && dsu.find(i) != dsu.find(j)) {
                    numberOfComponents--;
                    dsu.union_set(i, j);
                }
            }
        }

        return numberOfComponents;
    }


    // V2


    // V3




}
