package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/minimum-moves-to-equal-array-elements-ii/

import java.util.Arrays;

/**
 *  462. Minimum Moves to Equal Array Elements II
 *  Medium
 *
 *  Given an integer array nums of size n, return the minimum number of moves
 *  required to make all array elements equal.
 *
 *  In one move, you can increment or decrement an element of the array by 1.
 *
 *  Test cases are designed so that the answer will fit in a 32-bit integer.
 *
 *  Example 1:
 *  Input: nums = [1,2,3]
 *  Output: 2
 *  Explanation: Only two moves are needed: [1,2,3] => [2,2,3] => [2,2,2]
 *
 *  Example 2:
 *  Input: nums = [1,10,2,9]
 *  Output: 16
 *
 *  Constraints:
 *  n == nums.length
 *  1 <= nums.length <= 10^5
 *  -10^9 <= nums[i] <= 10^9
 */
public class MinimumMovesToEqualArrayElementsII {

    // V0
    // IDEA: the optimal meeting point is the median; sum |nums[i] - median|
    /**
     * time = O(n log n)
     * space = O(1) (in-place sort)
     */
    public int minMoves2(int[] nums) {
        Arrays.sort(nums);
        int median = nums[nums.length / 2];
        long res = 0;
        for (int num : nums) {
            res += Math.abs((long) num - median);
        }
        return (int) res;
    }

    // V1
    // IDEA: two pointers - pair smallest with largest, each pair costs (hi - lo),
    //       which avoids computing the median explicitly
    /**
     * time = O(n log n)
     * space = O(1)
     */
    public int minMoves2_1(int[] nums) {
        Arrays.sort(nums);
        int i = 0;
        int j = nums.length - 1;
        long res = 0;
        while (i < j) {
            res += (long) nums[j] - nums[i];
            i++;
            j--;
        }
        return (int) res;
    }
}
