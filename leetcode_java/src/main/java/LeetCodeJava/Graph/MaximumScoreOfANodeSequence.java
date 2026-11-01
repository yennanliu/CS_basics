package LeetCodeJava.Graph;

// https://leetcode.com/problems/maximum-score-of-a-node-sequence/description/

import java.util.*;

/**
 * 2242. Maximum Score of a Node Sequence
 * Hard
 *
 * There is an undirected graph with n nodes, numbered from 0 to n - 1.
 *
 * You are given a 0-indexed integer array scores of length n where scores[i] denotes the score of node i. You are also given a 2D integer array edges where edges[i] = [ai, bi] denotes that there exists an undirected edge connecting nodes ai and bi.
 *
 * A node sequence is valid if it meets the following conditions:
 *
 * There is an edge connecting every pair of adjacent nodes in the sequence.
 * No node appears more than once in the sequence.
 *
 * The score of a node sequence is defined as the sum of the scores of the nodes in the sequence.
 *
 * Return the maximum score of a valid node sequence with a length of 4. If no such sequence exists, return -1.
 *
 *
 * Example 1:
 *
 * Input: scores = [5,2,9,8,4], edges = [[0,1],[1,2],[2,3],[0,2],[1,3],[2,4]]
 * Output: 24
 * Explanation: The figure above shows the graph and the chosen node sequence [0,1,2,3].
 * The score of the node sequence is 5 + 2 + 9 + 8 = 24.
 * It can be shown that no other node sequence has a score of more than 24.
 * Note that the sequences [3,1,2,0] and [1,0,2,3] are also valid and have a score of 24.
 * The sequence [0,3,2,4] is not valid since no edge connects nodes 0 and 3.
 *
 * Example 2:
 *
 * Input: scores = [9,20,6,4,11,12], edges = [[0,3],[5,3],[2,4],[1,3]]
 * Output: -1
 * Explanation: The figure above shows the graph.
 * There are no valid node sequences of length 4, so we return -1.
 *
 *
 * Constraints:
 *
 * n == scores.length
 * 4 <= n <= 5 * 10^4
 * 1 <= scores[i] <= 10^8
 * 0 <= edges.length <= 5 * 10^4
 * edges[i].length == 2
 * 0 <= ai, bi <= n - 1
 * ai != bi
 * There are no duplicate edges.
 *
 */
public class MaximumScoreOfANodeSequence {

    // V0
    // IDEA: ANCHOR ON THE MIDDLE EDGE, KEEP ONLY EACH NODE'S TOP-3 NEIGHBOURS
    //  - a length-4 path is a - b - c - d; try every edge as the middle pair (b, c)
    //  - at most two of b's neighbours are blocked (c and d), so the best a is always
    //    among b's 3 highest-scoring neighbours; the same holds for d around c
    /**
     * time = O(V + E)
     * space = O(V)
     */
    public int maximumScore(int[] scores, int[][] edges) {
        int n = scores.length;

        // top[node] = up to 3 neighbours with the highest scores (min-heap by score)
        List<PriorityQueue<Integer>> top = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            top.add(new PriorityQueue<>((x, y) -> Integer.compare(scores[x], scores[y])));
        }
        for (int[] edge : edges) {
            addNeighbour(top.get(edge[0]), edge[1]);
            addNeighbour(top.get(edge[1]), edge[0]);
        }

        int best = -1;
        for (int[] edge : edges) {
            int b = edge[0];
            int c = edge[1];
            for (int a : top.get(b)) {
                if (a == c) {
                    continue;
                }
                for (int d : top.get(c)) {
                    // d must differ from both b and a (it already differs from c)
                    if (d == b || d == a) {
                        continue;
                    }
                    int score = scores[a] + scores[b] + scores[c] + scores[d];
                    best = Math.max(best, score);
                }
            }
        }
        return best;
    }

    /** Adds a neighbour to a size-3 min-heap, evicting the lowest score when it overflows. */
    private void addNeighbour(PriorityQueue<Integer> heap, int neighbour) {
        heap.offer(neighbour);
        if (heap.size() > 3) {
            heap.poll();
        }
    }
}
