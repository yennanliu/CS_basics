package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-distance-in-arrays/

import java.util.List;

/**
 *  624. Maximum Distance in Arrays
 *  Medium
 *
 *  You are given m arrays, where each array is sorted in ascending order.
 *
 *  You can pick up two integers from two different arrays (each array picks one)
 *  and calculate the distance. We define the distance between two integers a and b
 *  to be their absolute difference |a - b|.
 *
 *  Return the maximum distance.
 *
 *  Example 1:
 *  Input: arrays = [[1,2,3],[4,5],[1,2,3]]
 *  Output: 4
 *  Explanation: One way to reach the maximum distance 4 is to pick 1 in the first
 *  or third array and pick 5 in the second array.
 *
 *  Example 2:
 *  Input: arrays = [[1],[1]]
 *  Output: 0
 *
 *  Constraints:
 *  m == arrays.length
 *  2 <= m <= 10^5
 *  1 <= arrays[i].length <= 500
 *  -10^4 <= arrays[i][j] <= 10^4
 *  arrays[i] is sorted in ascending order.
 */
public class MaximumDistanceInArrays {

    // V0
    // IDEA: single scan - keep the min-first / max-last seen among PREVIOUS arrays,
    //       so the pair is always taken from two different arrays
    /**
     * time = O(m)
     * space = O(1)
     */
    public int maxDistance(List<List<Integer>> arrays) {
        if (arrays == null || arrays.size() < 2) {
            return 0;
        }
        List<Integer> first = arrays.get(0);
        int minVal = first.get(0);
        int maxVal = first.get(first.size() - 1);
        int res = 0;

        for (int i = 1; i < arrays.size(); i++) {
            List<Integer> cur = arrays.get(i);
            int curMin = cur.get(0);
            int curMax = cur.get(cur.size() - 1);

            res = Math.max(res, Math.max(curMax - minVal, maxVal - curMin));

            minVal = Math.min(minVal, curMin);
            maxVal = Math.max(maxVal, curMax);
        }
        return res;
    }
}
