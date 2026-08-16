package LeetCodeJava.BFS;

// https://leetcode.com/problems/reachable-nodes-in-subdivided-graph/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 882. Reachable Nodes In Subdivided Graph
 * Hard
 *
 * You are given an undirected graph (the "original graph") with n nodes labeled from
 * 0 to n - 1. You decide to subdivide each edge in the graph into a chain of nodes,
 * with the number of new nodes varying between each edge.
 *
 * The graph is given as a 2D array of edges where edges[i] = [ui, vi, cnti] indicates
 * that there is an edge between nodes ui and vi in the original graph, and cnti is the
 * total number of new nodes that you will subdivide the edge into.
 * Note that cnti == 0 means you will not subdivide the edge.
 *
 * To subdivide the edge [ui, vi], replace it with (cnti + 1) new edges and cnti new nodes.
 * The new nodes are x1, x2, ..., xcnti, and the new edges are [ui, x1], [x1, x2],
 * [x2, x3], ..., [xcnti-1, xcnti], [xcnti, vi].
 *
 * In this new graph, you want to know how many nodes are reachable from the node 0,
 * where a node is reachable if the distance is maxMoves or less.
 *
 * Given the original graph and maxMoves, return the number of nodes that are reachable
 * from node 0 in the new graph.
 *
 *
 * Example 1:
 *
 * Input: edges = [[0,1,10],[0,2,1],[1,2,2]], maxMoves = 6, n = 3
 * Output: 13
 * Explanation: The edge subdivisions are shown in the image above.
 * The nodes that are reachable are highlighted in yellow.
 *
 * Example 2:
 *
 * Input: edges = [[0,1,4],[1,2,6],[0,2,8],[1,3,1]], maxMoves = 10, n = 4
 * Output: 23
 *
 * Example 3:
 *
 * Input: edges = [[1,2,4],[1,4,5],[1,3,1],[2,3,4],[3,4,5]], maxMoves = 17, n = 5
 * Output: 1
 * Explanation: Node 0 is disconnected from the rest of the graph,
 * so only node 0 is reachable.
 *
 *
 * Constraints:
 *
 * 0 <= edges.length <= min(n * (n - 1) / 2, 10^4)
 * edges[i].length == 3
 * 0 <= ui < vi < n
 * There are no multiple edges in the graph.
 * 0 <= cnti <= 10^4
 * 0 <= maxMoves <= 10^9
 * 1 <= n <= 3000
 *
 */
public class ReachableNodesInSubdividedGraph {

    // V0
    // IDEA: DIJKSTRA (weighted BFS) + COUNT SUBDIVIDED NODES PER EDGE
    /**
     *   1) Do NOT expand the subdivided nodes (there can be 10^8 of them).
     *      Instead treat edge (u, v, cnt) as a SINGLE weighted edge of cost cnt + 1
     *      and run Dijkstra from node 0 to get dist[] on the ORIGINAL nodes.
     *
     *   2) An original node is reachable iff dist[node] <= maxMoves.
     *
     *   3) For each edge (u, v, cnt), the subdivided nodes reachable from the u side
     *      are min(cnt, maxMoves - dist[u]), and from the v side
     *      min(cnt, maxMoves - dist[v]).
     *
     *      NOTE !!! their sum is CAPPED at cnt, so we never DOUBLE COUNT a node
     *               that is reachable from both ends.
     *
     *   time  = O(E * log(E))
     *   space = O(N + E)
     */
    public int reachableNodes(int[][] edges, int maxMoves, int n) {
        // adjacency: node -> list of {neighbour, weight}
        List<List<int[]>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            // cnt new nodes on the edge -> cnt + 1 moves to CROSS it
            g.get(e[0]).add(new int[] { e[1], e[2] + 1 });
            g.get(e[1]).add(new int[] { e[0], e[2] + 1 });
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0;

        // {distance, node}
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.add(new long[] { 0, 0 });

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];

            /** NOTE !!!
             *
             *  a `stale` entry -- we already popped this node with a better distance
             */
            if (d > dist[u]) {
                continue;
            }

            for (int[] nb : g.get(u)) {
                int v = nb[0];
                long nd = d + nb[1];
                if (nd < dist[v]) {
                    dist[v] = nd;
                    pq.add(new long[] { nd, v });
                }
            }
        }

        // 1) original nodes we can stand on
        int res = 0;
        for (long d : dist) {
            if (d <= maxMoves) {
                res += 1;
            }
        }

        // 2) subdivided nodes, counted EDGE BY EDGE
        for (int[] e : edges) {
            long fromU = dist[e[0]] == Long.MAX_VALUE ? 0
                    : Math.min(e[2], Math.max(0, maxMoves - dist[e[0]]));
            long fromV = dist[e[1]] == Long.MAX_VALUE ? 0
                    : Math.min(e[2], Math.max(0, maxMoves - dist[e[1]]));

            // cap at cnt -> avoids double counting the middle nodes
            res += (int) Math.min(e[2], fromU + fromV);
        }

        return res;
    }


    // V1
    // IDEA: SPFA / BELLMAN-FORD QUEUE RELAXATION instead of Dijkstra
    /**
     *  Relax edges from a FIFO queue, re-enqueuing a node whenever its distance
     *  improves. No priority queue and no `stale entry` check.
     *
     *  Worse in theory (O(V*E) worst case) but perfectly fine here, and unlike
     *  Dijkstra it would still be correct if some edge weight were negative.
     *
     *  time  = O(V * E) worst case, near O(E) in practice
     *  space = O(V + E)
     */
    public int reachableNodes_1(int[][] edges, int maxMoves, int n) {
        List<List<int[]>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(new int[] { e[1], e[2] + 1 });
            g.get(e[1]).add(new int[] { e[0], e[2] + 1 });
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0;

        boolean[] inQueue = new boolean[n];
        Deque<Integer> q = new ArrayDeque<>();
        q.offer(0);
        inQueue[0] = true;

        while (!q.isEmpty()) {
            int u = q.poll();
            inQueue[u] = false;
            for (int[] nb : g.get(u)) {
                long nd = dist[u] + nb[1];
                if (nd < dist[nb[0]]) {
                    dist[nb[0]] = nd;
                    if (!inQueue[nb[0]]) {
                        q.offer(nb[0]);
                        inQueue[nb[0]] = true;
                    }
                }
            }
        }

        return tally(edges, maxMoves, dist);
    }

    /** original nodes within reach + subdivided nodes counted per edge */
    private int tally(int[][] edges, int maxMoves, long[] dist) {
        int res = 0;
        for (long d : dist) {
            if (d <= maxMoves) {
                res += 1;
            }
        }
        for (int[] e : edges) {
            long fromU = dist[e[0]] == Long.MAX_VALUE ? 0
                    : Math.min(e[2], Math.max(0, maxMoves - dist[e[0]]));
            long fromV = dist[e[1]] == Long.MAX_VALUE ? 0
                    : Math.min(e[2], Math.max(0, maxMoves - dist[e[1]]));
            res += (int) Math.min(e[2], fromU + fromV);
        }
        return res;
    }

    // V2
    // IDEA: DENSE DIJKSTRA -- O(V^2) linear scan, no heap
    /**
     *  With n <= 3000 and up to ~10^4 edges the graph is fairly dense, and the
     *  classic array-scan Dijkstra (pick the unvisited minimum by a linear sweep)
     *  costs O(V^2) = 9 * 10^6 -- competitive with the heap version and with zero
     *  allocation per relaxation.
     *
     *  time  = O(V^2 + E)
     *  space = O(V + E)
     */
    public int reachableNodes_2(int[][] edges, int maxMoves, int n) {
        long[][] w = new long[n][n];
        for (long[] row : w) {
            Arrays.fill(row, Long.MAX_VALUE);
        }
        for (int[] e : edges) {
            long cost = e[2] + 1;
            w[e[0]][e[1]] = Math.min(w[e[0]][e[1]], cost);
            w[e[1]][e[0]] = Math.min(w[e[1]][e[0]], cost);
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0;
        boolean[] done = new boolean[n];

        for (int iter = 0; iter < n; iter++) {
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!done[i] && dist[i] != Long.MAX_VALUE
                        && (u == -1 || dist[i] < dist[u])) {
                    u = i;
                }
            }
            if (u == -1) {
                break; // the rest is unreachable
            }
            done[u] = true;
            for (int v = 0; v < n; v++) {
                if (w[u][v] != Long.MAX_VALUE && dist[u] + w[u][v] < dist[v]) {
                    dist[v] = dist[u] + w[u][v];
                }
            }
        }

        return tally(edges, maxMoves, dist);
    }

    // V3
    // IDEA: DIJKSTRA WITH A TreeSet AS AN INDEXED PRIORITY QUEUE (real decrease-key)
    /**
     *  A PriorityQueue cannot update a key, which is why V0 tolerates stale
     *  entries. A TreeSet ordered by (distance, node) CAN: remove the old pair,
     *  reinsert the improved one.
     *
     *  -> the queue never holds more than V elements, so the memory is bounded by
     *     the vertex count instead of the edge count.
     *
     *  time  = O(E log V)
     *  space = O(V + E)
     */
    public int reachableNodes_3(int[][] edges, int maxMoves, int n) {
        List<List<int[]>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(new int[] { e[1], e[2] + 1 });
            g.get(e[1]).add(new int[] { e[0], e[2] + 1 });
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0;

        TreeSet<long[]> pq = new TreeSet<>((x, y) ->
                x[0] != y[0] ? Long.compare(x[0], y[0]) : Long.compare(x[1], y[1]));
        pq.add(new long[] { 0, 0 });

        while (!pq.isEmpty()) {
            long[] cur = pq.pollFirst();
            int u = (int) cur[1];
            for (int[] nb : g.get(u)) {
                int v = nb[0];
                long nd = dist[u] + nb[1];
                if (nd < dist[v]) {
                    // DECREASE-KEY: drop the stale pair before inserting the new one
                    if (dist[v] != Long.MAX_VALUE) {
                        pq.remove(new long[] { dist[v], v });
                    }
                    dist[v] = nd;
                    pq.add(new long[] { nd, v });
                }
            }
        }

        return tally(edges, maxMoves, dist);
    }

}
