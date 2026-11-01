package LeetCodeJava.Graph;

// https://leetcode.com/problems/valid-arrangement-of-pairs/description/

import java.util.*;

/**
 * 2097. Valid Arrangement of Pairs
 * Hard
 *
 * You are given a 0-indexed 2D integer array pairs where pairs[i] = [start_i, end_i]. An arrangement of pairs
 * is valid if for every index i where 1 <= i < pairs.length, we have end_(i-1) == start_i.
 *
 * Return any valid arrangement of pairs.
 *
 * Note: The inputs will be generated such that there exists a valid arrangement of pairs.
 *
 *
 * Example 1:
 *
 * Input: pairs = [[5,1],[4,5],[11,9],[9,4]]
 * Output: [[11,9],[9,4],[4,5],[5,1]]
 *
 * Example 2:
 *
 * Input: pairs = [[1,3],[3,2],[2,1]]
 * Output: [[1,3],[3,2],[2,1]]
 * Explanation: The arrangements [[2,1],[1,3],[3,2]] and [[3,2],[2,1],[1,3]] are also valid.
 *
 * Example 3:
 *
 * Input: pairs = [[1,2],[1,3],[2,1]]
 * Output: [[1,2],[2,1],[1,3]]
 *
 *
 * Constraints:
 *
 * 1 <= pairs.length <= 10^5
 * pairs[i].length == 2
 * 0 <= start_i, end_i <= 10^9
 * start_i != end_i
 * No two pairs are exactly the same.
 * There exists a valid arrangement of pairs.
 *
 */
public class ValidArrangementOfPairs {

    // V0
    // IDEA: EULERIAN PATH (HIERHOLZER) -- each pair is a DIRECTED EDGE start -> end
    // chaining pairs so each end meets the next start is walking every edge exactly once.
    // Start at the node with outDegree - inDegree == 1 if there is one, else anywhere (circuit).
    // Iterative Hierholzer: from the stack top, follow an unused edge; when a node has none
    // left, pop it onto the route. The reversed route is the node order of the path.
    /**
     * time = O(V + E)
     * space = O(V + E)
     */
    public int[][] validArrangement(int[][] pairs) {
        Map<Integer, Deque<Integer>> graph = new HashMap<>();
        // balance[x] = outDegree(x) - inDegree(x)
        Map<Integer, Integer> balance = new HashMap<>();
        for (int[] pair : pairs) {
            graph.computeIfAbsent(pair[0], x -> new ArrayDeque<>()).push(pair[1]);
            balance.put(pair[0], balance.getOrDefault(pair[0], 0) + 1);
            balance.put(pair[1], balance.getOrDefault(pair[1], 0) - 1);
        }

        int start = pairs[0][0];
        for (Map.Entry<Integer, Integer> entry : balance.entrySet()) {
            if (entry.getValue() == 1) {
                start = entry.getKey();
                break;
            }
        }

        // route is built backwards: a node is added once all its edges are used
        List<Integer> route = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            int node = stack.peek();
            Deque<Integer> outgoing = graph.get(node);
            if (outgoing != null && !outgoing.isEmpty()) {
                // consume one unused edge
                stack.push(outgoing.pop());
            } else {
                route.add(stack.pop());
            }
        }
        Collections.reverse(route);

        int[][] result = new int[pairs.length][2];
        for (int i = 0; i < pairs.length; i++) {
            result[i][0] = route.get(i);
            result[i][1] = route.get(i + 1);
        }
        return result;
    }
}
