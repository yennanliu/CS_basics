package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/3sum-smaller/

import java.util.Arrays;

/**
 *  259. 3Sum Smaller
 *  Medium
 *
 *  Given an array of n integers nums and an integer target, find the number of
 *  index triplets i, j, k with 0 <= i < j < k < n that satisfy the condition
 *  nums[i] + nums[j] + nums[k] < target.
 *
 *  Example 1:
 *    Input: nums = [-2,0,1,3], target = 2
 *    Output: 2
 *    Explanation: Because there are two triplets which sums are less than 2:
 *                 [-2,0,1] and [-2,0,3]
 *
 *  Example 2:
 *    Input: nums = [], target = 0
 *    Output: 0
 *
 *  Example 3:
 *    Input: nums = [0], target = 0
 *    Output: 0
 *
 *  Constraints:
 *    n == nums.length
 *    0 <= n <= 3500
 *    -100 <= nums[i] <= 100
 *    -100 <= target <= 100
 *
 *  Follow up: Could you solve it in O(n^2) runtime?
 */
public class ThreeSumSmaller {

    // V0
    // IDEA: SORT + 2 POINTERS
    //       fix i, then shrink [l, r]; if nums[i]+nums[l]+nums[r] < target then
    //       EVERY r' in (l, r] also works -> add (r - l) at once.
    /**
     * time = O(N^2)
     * space = O(1)   // ignoring the sort
     */
    public int threeSumSmaller(int[] nums, int target) {
        if (nums == null || nums.length < 3) {
            return 0;
        }
        Arrays.sort(nums);

        int res = 0;
        for (int i = 0; i < nums.length - 2; i++) {
            int l = i + 1;
            int r = nums.length - 1;
            while (l < r) {
                if (nums[i] + nums[l] + nums[r] < target) {
                    // all pairs (l, l+1), (l, l+2) ... (l, r) are valid
                    res += (r - l);
                    l++;
                } else {
                    r--;
                }
            }
        }
        return res;
    }
}
