package LeetCodeJava.DFS;

// https://leetcode.com/problems/find-eventual-safe-states/description/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 802. Find Eventual Safe States
 * Medium
 * Topics
 * Companies
 * There is a directed graph of n nodes with each node labeled from 0 to n - 1. The graph is represented by a 0-indexed 2D integer array graph where graph[i] is an integer array of nodes adjacent to node i, meaning there is an edge from node i to each node in graph[i].
 *
 * A node is a terminal node if there are no outgoing edges. A node is a safe node if every possible path starting from that node leads to a terminal node (or another safe node).
 *
 * Return an array containing all the safe nodes of the graph. The answer should be sorted in ascending order.
 *
 *
 *
 * Example 1:
 *
 * Illustration of graph
 * Input: graph = [[1,2],[2,3],[5],[0],[5],[],[]]
 * Output: [2,4,5,6]
 * Explanation: The given graph is shown above.
 * Nodes 5 and 6 are terminal nodes as there are no outgoing edges from either of them.
 * Every path starting at nodes 2, 4, 5, and 6 all lead to either node 5 or 6.
 * Example 2:
 *
 * Input: graph = [[1,2,3,4],[1,2],[3,4],[0,4],[]]
 * Output: [4]
 * Explanation:
 * Only node 4 is a terminal node, and every path starting at node 4 leads to node 4.
 *
 *
 * Constraints:
 *
 * n == graph.length
 * 1 <= n <= 104
 * 0 <= graph[i].length <= n
 * 0 <= graph[i][j] <= n - 1
 * graph[i] is sorted in a strictly increasing order.
 * The graph may contain self-loops.
 * The number of edges in the graph will be in the range [1, 4 * 104].
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 292.9K
 * Submissions
 * 454.2K
 * Acceptance Rate
 * 64.5%
 *
 */
public class FindEventualSafeStates {

    // V0
    // TODO : implement
//    public List<Integer> eventualSafeNodes(int[][] graph) {
//
//    }

    // V1-0
    // IDEA : DFS
    // KEY : check if there is a "cycle" on a node
    // https://www.youtube.com/watch?v=v5Ni_3bHjzk
    // https://zxi.mytechroad.com/blog/graph/leetcode-802-find-eventual-safe-states/
    // https://github.com/yennanliu/CS_basics/tree/master/doc/pic/lc_802.png
    public List<Integer> eventualSafeNodes(int[][] graph) {
        // init
        int n = graph.length;
        /**
         *  NOTE !!!
         *
         *   we init status array (states)
         *   and set all init value as "UNKNOWN"
         */
        State[] states = new State[n];
        for (int i = 0; i < n; i++) {
            states[i] = State.UNKNOWN;
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            // if node is with SAFE state, add to result
            /**
             *  NOTE !!!
             *
             *   the dfs parameter: we use index, state array as well
             */
            if (dfs(graph, i, states) == State.SAFE) {
                result.add(i);
            }
        }
        return result;
    }

    private enum State {
        UNKNOWN, VISITING, SAFE, UNSAFE
    }

    private State dfs(int[][] graph, int node, State[] states) {
        /**
         * NOTE !!!
         *  if a node with "VISITING" state,
         *  but is visited again (within the other iteration)
         *  -> there must be a cycle
         *  -> this node is UNSAFE
         *
         *
         *  NOTE !!!
         *
         *   we DON'T compare current status,
         *   but compare state (states[node]) via index, and enum
         */
        if (states[node] == State.VISITING) {
            return states[node] = State.UNSAFE;
        }
        /**
         * NOTE !!!
         *  if a node is not with "UNKNOWN" state,
         *  -> update its state
         *
         *   NOTE !!!
         *   we DON'T compare current status,
         *   but compare state (states[node]) via index, and enum
         */
        if (states[node] != State.UNKNOWN) {
            return states[node];
        }

        /**
         * NOTE !!!
         *  update node state as VISITING
         */
        states[node] = State.VISITING;
        for (int next : graph[node]) {
            /**
             * NOTE !!!
             *   for every sub node, if any of them
             *   has UNSAFE state,
             *   -> set and return node state as UNSAFE directly
             */
            if (dfs(graph, next, states) == State.UNSAFE) {
                return states[node] = State.UNSAFE;
            }
        }

        /**
         * NOTE !!!
         *   if can pass all above checks
         *   -> this is node has SAFE state
         */
        return states[node] = State.SAFE;
    }

    // V1-1
    // https://leetcode.com/problems/find-eventual-safe-states/editorial/
    // IDEA : DFS
    public boolean dfs(int node, int[][] adj, boolean[] visit, boolean[] inStack) {
        // If the node is already in the stack, we have a cycle.
        if (inStack[node]) {
            return true;
        }
        if (visit[node]) {
            return false;
        }
        // Mark the current node as visited and part of current recursion stack.
        visit[node] = true;
        inStack[node] = true;
        for (int neighbor : adj[node]) {
            if (dfs(neighbor, adj, visit, inStack)) {
                return true;
            }
        }
        // Remove the node from the stack.
        inStack[node] = false;
        return false;
    }

    public List<Integer> eventualSafeNodes_1_1(int[][] graph) {
        int n = graph.length;
        boolean[] visit = new boolean[n];
        boolean[] inStack = new boolean[n];

        for (int i = 0; i < n; i++) {
            dfs(i, graph, visit, inStack);
        }

        List<Integer> safeNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!inStack[i]) {
                safeNodes.add(i);
            }
        }

        return safeNodes;
    }

    // V1-2
    // https://leetcode.com/problems/find-eventual-safe-states/editorial/
    // IDEA : Topological Sort Using Kahn's Algorithm
    public List<Integer> eventualSafeNodes_1_2(int[][] graph) {
        int n = graph.length;
        int[] indegree = new int[n];
        List<List<Integer>> adj = new ArrayList<>();

        for(int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int i = 0; i < n; i++) {
            for (int node : graph[i]) {
                adj.get(node).add(i);
                indegree[i]++;
            }
        }

        Queue<Integer> q = new LinkedList<>();
        // Push all the nodes with indegree zero in the queue.
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }

        boolean[] safe = new boolean[n];
        while (!q.isEmpty()) {
            int node = q.poll();
            safe[node] = true;

            for (int neighbor : adj.get(node)) {
                // Delete the edge "node -> neighbor".
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    q.add(neighbor);
                }
            }
        }

        List<Integer> safeNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (safe[i]) {
                safeNodes.add(i);
            }
        }
        return safeNodes;
    }

    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/find-eventual-safe-states/solutions/3752567/cycle-detection-same-code-begineers-friendly-c-java-python/
    public boolean dfs2(List<List<Integer>> adj, int src, boolean[] vis, boolean[] recst) {
        vis[src] = true;
        recst[src] = true;
        for (int x : adj.get(src)) {
            if (!vis[x] && dfs2(adj, x, vis, recst)) {
                return true;
            } else if (recst[x]) {
                return true;
            }
        }
        recst[src] = false;
        return false;
    }

    public List<Integer> eventualSafeNodes_2(int[][] graph) {
        int n = graph.length;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < graph[i].length; j++) {
                list.add(graph[i][j]);
            }
            adj.add(list);
        }
        boolean[] vis = new boolean[n];
        boolean[] recst = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                dfs2(adj, i, vis, recst);
            }
        }
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < recst.length; i++) {
            if (!recst[i]) {
                ans.add(i);
            }
        }
        return ans;
    }


    // V3
    // IDEA : BFS
    // https://leetcode.com/problems/find-eventual-safe-states/solutions/3753320/c-java-python-bfs-dfs-topological-sort-easy-to-understand/
    public List<Integer> eventualSafeNodes_3(int[][] graph) {
        // Using BFS
        int n = graph.length;
        int[] indegree = new int[n];
        List<List<Integer>> g = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int itt : graph[i]) {
                g.get(itt).add(i); // reverse the direction of node
                indegree[i]++;
            }
        }
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }
        List<Integer> ans = new ArrayList<>();
        while (!q.isEmpty()) {
            int node = q.poll();
            ans.add(node);
            for (int it : g.get(node)) {
                indegree[it]--;
                if (indegree[it] == 0)
                    q.add(it);
            }
        }
        ans.sort(null);
        return ans;
    }

    // V4
}
