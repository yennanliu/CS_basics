package LeetCodeJava.Array;

// https://leetcode.com/problems/get-maximum-in-generated-array/description/

/**
 * 1646. Get Maximum in Generated Array
 * Easy
 *
 * You are given an integer n. A 0-indexed integer array nums of length n + 1 is generated
 * in the following way:
 *
 * nums[0] = 0
 * nums[1] = 1
 * nums[2 * i] = nums[i] when 2 <= 2 * i <= n
 * nums[2 * i + 1] = nums[i] + nums[i + 1] when 2 <= 2 * i + 1 <= n
 *
 * Return the maximum integer in the array nums.
 *
 *
 * Example 1:
 *
 * Input: n = 7
 * Output: 3
 * Explanation: nums = [0,1,1,2,1,3,2,3], and the maximum is 3.
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 1
 * Explanation: nums = [0,1,1]. The maximum is 1.
 *
 * Example 3:
 *
 * Input: n = 3
 * Output: 2
 * Explanation: nums = [0,1,1,2]. The maximum is 2.
 *
 *
 * Constraints:
 *
 * 0 <= n <= 100
 *
 */
public class GetMaximumInGeneratedArray {

    // V0
    // IDEA: SIMULATION — rewrite both rules in terms of i:
    //       i even -> nums[i] = nums[i / 2];  i odd -> nums[i] = nums[i / 2] + nums[i / 2 + 1]
    //       both referenced indices are < i (for i >= 2), so one forward pass suffices
    /**
     * time = O(N)
     * space = O(N)
     */
    public int getMaximumGenerated(int n) {
        // the array is too short for the loop: nums = [0] or [0, 1]
        if (n < 2) {
            return n;
        }

        int[] nums = new int[n + 1];
        nums[1] = 1;
        int maximum = 1;

        for (int i = 2; i <= n; i++) {
            int half = i / 2;
            if (i % 2 == 0) {
                nums[i] = nums[half];
            } else {
                nums[i] = nums[half] + nums[half + 1];
            }
            maximum = Math.max(maximum, nums[i]);
        }
        return maximum;
    }
}
