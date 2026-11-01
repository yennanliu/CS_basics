package LeetCodeJava.Graph;

// https://leetcode.com/problems/restore-the-array-from-adjacent-pairs/description/

import java.util.*;

/**
 * 1743. Restore the Array From Adjacent Pairs
 * Medium
 *
 * There is an integer array nums that consists of n unique elements, but you have forgotten it. However, you
 * do remember every pair of adjacent elements in nums.
 *
 * You are given a 2D integer array adjacentPairs of size n - 1 where each adjacentPairs[i] = [ui, vi]
 * indicates that the elements ui and vi are adjacent in nums.
 *
 * It is guaranteed that every adjacent pair of elements nums[i] and nums[i+1] will exist in adjacentPairs,
 * either as [nums[i], nums[i+1]] or [nums[i+1], nums[i]]. The pairs can appear in any order.
 *
 * Return the original array nums. If there are multiple solutions, return any of them.
 *
 *
 * Example 1:
 *
 * Input: adjacentPairs = [[2,1],[3,4],[3,2]]
 * Output: [1,2,3,4]
 *
 * Example 2:
 *
 * Input: adjacentPairs = [[4,-2],[1,4],[-3,1]]
 * Output: [-2,4,1,-3]
 * Explanation: There can be negative numbers. Another solution is [-3,1,4,-2], which would also be accepted.
 *
 * Example 3:
 *
 * Input: adjacentPairs = [[100000,-100000]]
 * Output: [100000,-100000]
 *
 *
 * Constraints:
 *
 * nums.length == n
 * adjacentPairs.length == n - 1
 * adjacentPairs[i].length == 2
 * 2 <= n <= 10^5
 * -10^5 <= nums[i], ui, vi <= 10^5
 * There exists some nums that has adjacentPairs as its pairs.
 *
 */
public class RestoreTheArrayFromAdjacentPairs {

    // V0
    // IDEA: THE GRAPH IS A PATH -- START AT A DEGREE-1 ENDPOINT AND WALK IT
    // values are nodes, pairs are undirected edges. With unique values the graph is a simple
    // path: the two ends have degree 1, every interior value degree 2. Start at an end and
    // repeatedly step to the neighbour that is not where we came from.
    // NOTE: walk iteratively -- n can be 10^5, too deep for recursion.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] restoreArray(int[][] adjacentPairs) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] pair : adjacentPairs) {
            graph.computeIfAbsent(pair[0], x -> new ArrayList<>()).add(pair[1]);
            graph.computeIfAbsent(pair[1], x -> new ArrayList<>()).add(pair[0]);
        }

        int n = adjacentPairs.length + 1;

        // any degree-1 node is an end of the array
        int start = 0;
        for (Map.Entry<Integer, List<Integer>> entry : graph.entrySet()) {
            if (entry.getValue().size() == 1) {
                start = entry.getKey();
                break;
            }
        }

        int[] result = new int[n];
        result[0] = start;
        // result[1] is start's only neighbour
        result[1] = graph.get(start).get(0);
        for (int i = 2; i < n; i++) {
            List<Integer> neighbours = graph.get(result[i - 1]);
            // the interior node has two neighbours: skip the one we came from
            if (neighbours.get(0) == result[i - 2]) {
                result[i] = neighbours.get(1);
            } else {
                result[i] = neighbours.get(0);
            }
        }
        return result;
    }
}
