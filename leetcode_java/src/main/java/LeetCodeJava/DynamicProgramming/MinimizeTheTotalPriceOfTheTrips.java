package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimize-the-total-price-of-the-trips/description/

import java.util.*;

/**
 * 2646. Minimize the Total Price of the Trips
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There exists an undirected and unrooted tree with n nodes indexed from 0 to n - 1. You are given the integer n and a 2D integer array edges of length n - 1, where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree.
 *
 * Each node has an associated price. You are given an integer array price, where price[i] is the price of the ith node.
 *
 * The price sum of a given path is the sum of the prices of all nodes lying on that path.
 *
 * Additionally, you are given a 2D integer array trips, where trips[i] = [starti, endi] indicates that you start the ith trip from the node starti and travel to the node endi by any path you like.
 *
 * Before performing your first trip, you can choose some non-adjacent nodes and halve the prices.
 *
 * Return the minimum total price sum to perform all the given trips.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 4, edges = [[0,1],[1,2],[1,3]], price = [2,2,10,6], trips = [[0,3],[2,1],[2,3]]
 * Output: 23
 * Explanation: The diagram above denotes the tree after rooting it at node 2. The first part shows the initial tree and the second part shows the tree after choosing nodes 0, 2, and 3, and making their price half.
 * For the 1st trip, we choose path [0,1,3]. The price sum of that path is 1 + 2 + 3 = 6.
 * For the 2nd trip, we choose path [2,1]. The price sum of that path is 2 + 5 = 7.
 * For the 3rd trip, we choose path [2,1,3]. The price sum of that path is 5 + 2 + 3 = 10.
 * The total price sum of all trips is 6 + 7 + 10 = 23.
 * It can be proven, that 23 is the minimum answer that we can achieve.
 * Example 2:
 *
 *
 * Input: n = 2, edges = [[0,1]], price = [2,2], trips = [[0,0]]
 * Output: 1
 * Explanation: The diagram above denotes the tree after rooting it at node 0. The first part shows the initial tree and the second part shows the tree after choosing node 0, and making its price half.
 * For the 1st trip, we choose path [0]. The price sum of that path is 1.
 * The total price sum of all trips is 1. It can be proven, that 1 is the minimum answer that we can achieve.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 50
 * edges.length == n - 1
 * 0 <= ai, bi <= n - 1
 * edges represents a valid tree.
 * price.length == n
 * price[i] is an even integer.
 * 1 <= price[i] <= 1000
 * 1 <= trips.length <= 100
 * 0 <= starti, endi <= n - 1
 *
 */
public class MinimizeTheTotalPriceOfTheTrips {

    // V0
//    public int minimumTotalPrice(int n, int[][] edges, int[] price, int[][] trips) {
//
//    }

    // V1
    // IDEA: DFS (?)
    // https://leetcode.ca/2023-02-27-2646-Minimize-the-Total-Price-of-the-Trips/
    private List<Integer>[] g;
    private int[] price;
    private int[] cnt;

    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumTotalPrice_1(int n, int[][] edges, int[] price, int[][] trips) {
        this.price = price;
        cnt = new int[n];
        g = new List[n];
        Arrays.setAll(g, k -> new ArrayList<>());
        for (int[] e : edges) {
            int a = e[0], b = e[1];
            g[a].add(b);
            g[b].add(a);
        }
        for (int[] t : trips) {
            int start = t[0], end = t[1];
            dfs(start, -1, end);
        }
        int[] ans = dfs2(0, -1);
        return Math.min(ans[0], ans[1]);
    }

    private boolean dfs(int i, int fa, int k) {
        ++cnt[i];
        if (i == k) {
            return true;
        }
        boolean ok = false;
        for (int j : g[i]) {
            if (j != fa) {
                ok = dfs(j, i, k);
                if (ok) {
                    break;
                }
            }
        }
        if (!ok) {
            --cnt[i];
        }
        return ok;
    }

    private int[] dfs2(int i, int fa) {
        int a = cnt[i] * price[i];
        int b = a >> 1;
        for (int j : g[i]) {
            if (j != fa) {
                int[] t = dfs2(j, i);
                a += Math.min(t[0], t[1]);
                b += t[0];
            }
        }
        return new int[] {a, b};
    }


    // V2
    // IDEA: Variation of House Robber III, BFS
    // https://leetcode.com/problems/minimize-the-total-price-of-the-trips/solutions/3422043/variation-of-house-robber-iii-easy-to-un-m1od/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumTotalPrice_2(int n, int[][] edges, int[] price, int[][] trips) {
        List<List<Integer>> tree = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tree.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            tree.get(edge[0]).add(edge[1]);
            tree.get(edge[1]).add(edge[0]);
        }
        Map<Integer, Integer> counts = new HashMap<>();
        for (int[] trip : trips) {
            bfs(tree, trip[0], trip[1], counts);
        }
        int[] currPrice = new int[n];
        for (int i = 0; i < n; i++) {
            currPrice[i] = counts.getOrDefault(i, 0) * price[i];
        }
        int[] result = helper(tree, 0, -1, currPrice);
        return Math.min(result[0], result[1]);
    }

    private int[] helper(List<List<Integer>> tree, int curr, int parent, int[] currPrice) {
        List<Integer> neighbors = tree.get(curr);
        int whole = 0;
        int halved = 0;
        for (int nei : neighbors) {
            if (nei == parent) {
                continue;
            }
            int[] neiResult = helper(tree, nei, curr, currPrice);
            whole += neiResult[0];
            halved += Math.min(neiResult[0], neiResult[1]);
        }
        return new int[] { currPrice[curr] + halved, currPrice[curr] / 2 + whole };
    }

    private void bfs(List<List<Integer>> tree, int src, int dst, Map<Integer, Integer> counts) {
        int n = tree.size();
        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        queue.offer(src);
        visited[src] = true;
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            if (curr == dst) {
                break;
            }
            for (int nei : tree.get(curr)) {
                if (!visited[nei]) {
                    visited[nei] = true;
                    parent[nei] = curr;
                    queue.offer(nei);
                }
            }
        }
        int curr = dst;
        while (curr != -1) {
            counts.put(curr, counts.getOrDefault(curr, 0) + 1);
            curr = parent[curr];
        }
    }


    // V3
    // IDEA: DFS + DP
    // https://leetcode.com/problems/minimize-the-total-price-of-the-trips/solutions/3421754/java-solution-using-dfs-memoisation-for-3h0xh/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumTotalPrice_3(int n, int[][] edges, int[] price, int[][] trips) {
        List<Integer>[] tree = buildTree(n, edges);

        int[] visitCount = new int[n];
        for (int[] trip : trips) {
            travel(trip[0], -1, trip[1], tree, visitCount);
        }

        int[] cost = new int[n];
        int[][] calcualted = new int[n][2];
        for (int i = 0; i < n; i++) {
            Arrays.fill(calcualted[i], -1);
            cost[i] = price[i] * visitCount[i];
        }

        return minPrice(trips[0][0], -1, tree, 1, cost, calcualted);
    }

    private int minPrice(int node, int parent, List<Integer>[] tree, int canHalf, int[] cost, int[][] calculated) {
        if (calculated[node][canHalf] != -1) {
            return calculated[node][canHalf];
        }

        int minPrice;

        int noHalfCurrentNodePrice = cost[node];
        for (int next : tree[node]) {
            if (next != parent) {
                noHalfCurrentNodePrice += minPrice(next, node, tree, 1, cost, calculated);
            }
        }

        int halfCurrentNodePrice = Integer.MAX_VALUE;
        if (canHalf == 1) {
            halfCurrentNodePrice = cost[node] / 2;

            for (int next : tree[node]) {
                if (next != parent) {
                    halfCurrentNodePrice += minPrice(next, node, tree, 0, cost, calculated);
                }
            }
        }

        calculated[node][canHalf] = Math.min(noHalfCurrentNodePrice, halfCurrentNodePrice);

        return calculated[node][canHalf];
    }

    private boolean travel(int node, int parent, int target, List<Integer>[] tree, int[] visitCount) {
        if (node == target) {
            visitCount[node]++;
            return true;
        }

        boolean found = false;
        for (int next : tree[node]) {
            if (parent != next) {
                found |= travel(next, node, target, tree, visitCount);
            }

            if (found) {
                break;
            }
        }

        if (found) {
            visitCount[node]++;
        }

        return found;
    }

    private List<Integer>[] buildTree(int n, int[][] edges) {
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            tree[i] = new ArrayList<Integer>();
        }

        for (int[] edge : edges) {
            tree[edge[0]].add(edge[1]);
            tree[edge[1]].add(edge[0]);
        }

        return tree;
    }


    // V4
    // IDEA: DFS + DP
    // https://leetcode.com/problems/minimize-the-total-price-of-the-trips/solutions/3444706/java-dfs-dp-with-comments-by-okarun-uhb0/
    HashMap<Integer, ArrayList<Integer>> graph;
    int[] freq;
    int[] price_4;
    int[][] dp;

    // dfs to find the frequency of all nodes in trips
    boolean dfs_4(int src, int dest, int parent) {
        if (src == dest) {
            freq[src]++;
            return true;
        }
        boolean found = false;
        if (graph.containsKey(src)) {
            for (int nei : graph.get(src)) {
                if (nei != parent) {
                    found |= dfs_4(nei, dest, src);
                }
                if (found)
                    break;
            }
        }
        if (found)
            freq[src]++;
        return found;
    }

    // to find th minimum value of price using recursion and memoization
    int recursion(int node, int parent, int half) {
        if (dp[node][half] != -1)
            return dp[node][half];
        int res1 = (price_4[node] * freq[node]) / 2, res2 = price_4[node] * freq[node];
        if (graph.containsKey(node)) {
            for (int i : graph.get(node)) {
                if (i != parent) {
                    res2 += recursion(i, node, 0);
                }
            }

            if (half == 1) {
                dp[node][half] = res2;
                return res2;
            }

            for (int i : graph.get(node)) {
                if (i != parent) {
                    res1 += recursion(i, node, 1);
                }
            }
        }
        return dp[node][half] = Math.min(res1, res2);
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumTotalPrice_4(int n, int[][] edges, int[] price, int[][] trips) {
        // creating graph
        graph = new HashMap<>();
        for (int[] edge : edges) {
            if (!graph.containsKey(edge[0]))
                graph.put(edge[0], new ArrayList<>());
            if (!graph.containsKey(edge[1]))
                graph.put(edge[1], new ArrayList<>());
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // creating frequency array
        freq = new int[n];
        for (int[] trip : trips) {
            dfs_4(trip[0], trip[1], -1);
        }

        // finding minimum price
        dp = new int[n][2];
        for (int i = 0; i < n; i++)
            Arrays.fill(dp[i], -1);
        this.price_4 = price;
        return recursion(0, -1, 0);
    }



}
