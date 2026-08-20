package LeetCodeJava.Sort;

// https://leetcode.com/problems/minimum-difference-between-largest-and-smallest-value-in-three-moves/

import java.util.Arrays;

/**
 *  1509. Minimum Difference Between Largest and Smallest Value in Three Moves
 *  Medium
 *
 *  You are given an integer array nums.
 *
 *  In one move, you can choose one element of nums and change it to any value.
 *
 *  Return the minimum difference between the largest and smallest value of nums
 *  after performing at most three moves.
 *
 *  Example 1:
 *    Input: nums = [5,3,2,4]
 *    Output: 0
 *    Explanation: 3 moves can flatten every value -> difference 0.
 *
 *  Example 2:
 *    Input: nums = [1,5,0,10,14]
 *    Output: 1
 *    Explanation: change 5 -> 0, 10 -> 0, 14 -> 1, giving [1,0,0,0,1].
 *
 *  Example 3:
 *    Input: nums = [3,100,20]
 *    Output: 0
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    -10^9 <= nums[i] <= 10^9
 */
public class MinimumDifferenceBetweenLargestAndSmallestValueInThreeMoves {

    // V0
    // IDEA: SORT + ENUMERATE THE 4 SPLITS
    //       changing a value to "any value" is the same as DELETING it (we can
    //       always park it inside the surviving range), and it is never useful to
    //       delete anything but the extremes.
    //
    //       after sorting, the 3 removals split as (l from the left, 3-l from the
    //       right) for l in 0..3, leaving the window [l, n-1-(3-l)]:
    //           answer = min over l in {0,1,2,3} of nums[n-1-(3-l)] - nums[l]
    //
    //       NOTE: if n <= 4 everything can be flattened -> answer is 0.
    /**
     * time = O(N log N)
     * space = O(1)   // ignoring the sort
     */
    public int minDifference(int[] nums) {
        int n = nums.length;
        if (n <= 4) {
            return 0;
        }
        Arrays.sort(nums);
        int res = Integer.MAX_VALUE;
        for (int l = 0; l <= 3; l++) {
            int r = 3 - l;
            res = Math.min(res, nums[n - 1 - r] - nums[l]);
        }
        return res;
    }
}
