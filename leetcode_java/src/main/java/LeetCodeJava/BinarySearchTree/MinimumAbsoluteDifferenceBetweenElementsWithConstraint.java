package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/minimum-absolute-difference-between-elements-with-constraint/

import java.util.List;
import java.util.TreeSet;

/**
 *  2817. Minimum Absolute Difference Between Elements With Constraint
 *  Medium
 *
 *  You are given a 0-indexed integer array nums and an integer x.
 *
 *  Find the minimum absolute difference between two elements in the array that are
 *  at least x indices apart.
 *
 *  In other words, find two indices i and j such that abs(i - j) >= x and
 *  abs(nums[i] - nums[j]) is minimized.
 *
 *  Return an integer denoting the minimum absolute difference between two elements
 *  that are at least x indices apart.
 *
 *  Example 1:
 *    Input: nums = [4,3,2,4], x = 2
 *    Output: 0
 *    Explanation: We can select nums[0] = 4 and nums[3] = 4. They are at least 2
 *                 indices apart and their absolute difference is 0.
 *
 *  Example 2:
 *    Input: nums = [5,3,2,10,15], x = 1
 *    Output: 1
 *    Explanation: We can select nums[1] = 3 and nums[2] = 2.
 *
 *  Example 3:
 *    Input: nums = [1,2,3,4], x = 3
 *    Output: 3
 *    Explanation: We can select nums[0] = 1 and nums[3] = 4.
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^9
 *    0 <= x < nums.length
 */
public class MinimumAbsoluteDifferenceBetweenElementsWithConstraint {

    // V0
    // IDEA: SLIDING "ORDERED SET" OF ELIGIBLE PARTNERS (BST PREDECESSOR /
    //       SUCCESSOR QUERIES VIA TreeSet)
    //       sweep j = x, x+1, ... and just BEFORE handling j insert nums[j - x]
    //       into an ordered set S. by construction S then holds exactly the values
    //       whose index is <= j - x, i.e. every partner that is >= x indices away
    //       from j (pairs with i > j are covered symmetrically when the roles swap,
    //       so one forward sweep is enough).
    //       the best partner for nums[j] inside S is its PREDECESSOR (largest value
    //       <= nums[j], TreeSet.floor) or its SUCCESSOR (smallest value >= nums[j],
    //       TreeSet.ceiling) - any value further away is strictly worse. two
    //       ordered-set queries per step, O(log N) each.
    //       NOTE: x == 0 is legal and means "any two indices, including i == j", so
    //             the sweep must insert nums[j] itself before querying j -> the
    //             answer is then 0. starting at j = x with insert-then-query order
    //             gets this right automatically.
    //       NOTE: a TreeSet drops duplicates, which is harmless here - a repeated
    //             value only ever yields difference 0, which the first copy in the
    //             set already reports.
    /**
     * time = O(N * log N)
     * space = O(N)
     */
    public int minAbsoluteDifference(List<Integer> nums, int x) {
        int n = nums.size();
        TreeSet<Integer> seen = new TreeSet<>();
        int res = Integer.MAX_VALUE;

        for (int j = x; j < n; j++) {
            seen.add(nums.get(j - x));

            int cur = nums.get(j);
            Integer pred = seen.floor(cur);
            if (pred != null && cur - pred < res) {
                res = cur - pred;
            }
            Integer succ = seen.ceiling(cur);
            if (succ != null && succ - cur < res) {
                res = succ - cur;
            }
            if (res == 0) {
                return 0;
            }
        }
        return res;
    }
}
