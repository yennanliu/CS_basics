package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-increment-to-make-array-unique/

import java.util.Arrays;

/**
 *  945. Minimum Increment to Make Array Unique
 *  Medium
 *
 *  You are given an integer array nums. In one move, you can pick an index i where
 *  0 <= i < nums.length and increment nums[i] by 1.
 *
 *  Return the minimum number of moves to make every value in nums unique.
 *
 *  The test cases are generated so that the answer fits in a 32-bit integer.
 *
 *
 *  Example 1:
 *
 *  Input: nums = [1,2,2]
 *  Output: 1
 *  Explanation: After 1 move, the array could be [1,2,3].
 *
 *  Example 2:
 *
 *  Input: nums = [3,2,1,2,1,7]
 *  Output: 6
 *  Explanation: After 6 moves, the array could be [3,4,1,2,5,7].
 *  It can be shown that it is impossible for the array to have all unique values
 *  with 5 or less moves.
 *
 *
 *  Constraints:
 *
 *  1 <= nums.length <= 10^5
 *  0 <= nums[i] <= 10^5
 */
public class MinimumIncrementToMakeArrayUnique {

    // V0
    // IDEA: SORT + GREEDY (force nums[i] to be at least prev + 1)
    /**
     * time = O(n log n)
     * space = O(1) (ignoring the sort's internal space)
     */
    public int minIncrementForUnique(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        Arrays.sort(nums);
        int res = 0;
        int prev = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] <= prev) {
                prev = prev + 1;
                res += prev - nums[i];
            } else {
                prev = nums[i];
            }
        }
        return res;
    }

    // V1
    // IDEA: COUNTING SORT — push the surplus of each value forward to the next slot
    /**
     * time = O(n + m), m = max value range
     * space = O(n + m)
     */
    public int minIncrementForUnique_1(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        int max = 0;
        for (int x : nums) {
            max = Math.max(max, x);
        }
        // extra room: n duplicates can all be pushed past max
        int[] cnt = new int[max + nums.length + 1];
        for (int x : nums) {
            cnt[x]++;
        }
        int res = 0;
        for (int v = 0; v < cnt.length - 1; v++) {
            if (cnt[v] > 1) {
                int surplus = cnt[v] - 1;
                cnt[v + 1] += surplus;
                cnt[v] = 1;
                res += surplus;
            }
        }
        return res;
    }
}
