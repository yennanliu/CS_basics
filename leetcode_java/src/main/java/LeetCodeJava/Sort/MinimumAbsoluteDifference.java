package LeetCodeJava.Sort;

// https://leetcode.com/problems/minimum-absolute-difference/description/

import java.util.*;

/**
 * 1200. Minimum Absolute Difference
 * Easy
 *
 * Given an array of distinct integers arr, find all pairs of elements with the
 * minimum absolute difference of any two elements.
 *
 * Return a list of pairs in ascending order (with respect to pairs), each pair
 * [a, b] follows
 *
 * a, b are from arr
 * a < b
 * b - a equals to the minimum absolute difference of any two elements in arr
 *
 * Example 1:
 *
 * Input: arr = [4,2,1,3]
 * Output: [[1,2],[2,3],[3,4]]
 * Explanation: The minimum absolute difference is 1. List all pairs with
 * difference equal to 1 in ascending order.
 *
 * Example 2:
 *
 * Input: arr = [1,3,6,10,15]
 * Output: [[1,3]]
 *
 * Example 3:
 *
 * Input: arr = [3,8,-10,23,19,-4,-14,27]
 * Output: [[-14,-10],[19,23],[23,27]]
 *
 * Constraints:
 *
 * 2 <= arr.length <= 10^5
 * -10^6 <= arr[i] <= 10^6
 *
 */
public class MinimumAbsoluteDifference {

    // V0
    // IDEA: SORT + ADJACENT PAIRS
    //   after sorting, the minimum difference can only come from two ADJACENT
    //   elements, so: one pass to find the min gap, one pass to collect pairs.
    //   the collected pairs are already ascending because arr is sorted.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public List<List<Integer>> minimumAbsDifference(int[] arr) {
        List<List<Integer>> result = new ArrayList<>();
        if (arr == null || arr.length < 2) {
            return result;
        }

        int[] sorted = arr.clone();
        Arrays.sort(sorted);

        // pass 1: the smallest gap between neighbours
        int minGap = Integer.MAX_VALUE;
        for (int i = 0; i + 1 < sorted.length; i++) {
            minGap = Math.min(minGap, sorted[i + 1] - sorted[i]);
        }

        // pass 2: every neighbour pair with exactly that gap
        for (int i = 0; i + 1 < sorted.length; i++) {
            if (sorted[i + 1] - sorted[i] == minGap) {
                result.add(Arrays.asList(sorted[i], sorted[i + 1]));
            }
        }
        return result;
    }
}
