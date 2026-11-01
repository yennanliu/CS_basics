package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-special-triplets/description/

import java.util.*;

/**
 * 3583. Count Special Triplets
 * Medium
 *
 * You are given an integer array nums.
 *
 * A special triplet is defined as a triplet of indices (i, j, k) satisfying:
 *
 * 0 <= i < j < k < n, where n = nums.length
 * nums[i] == nums[j] * 2
 * nums[k] == nums[j] * 2
 *
 * Return the total number of special triplets in the array.
 *
 * Since the answer may be large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [6,3,6]
 * Output: 1
 * Explanation:
 * The only special triplet is (i, j, k) = (0, 1, 2), where:
 * nums[0] = 6, nums[1] = 3, nums[2] = 6
 * nums[0] = nums[1] * 2 = 6
 * nums[2] = nums[1] * 2 = 6
 *
 * Example 2:
 *
 * Input: nums = [0,1,0,0]
 * Output: 1
 * Explanation:
 * The only special triplet is (i, j, k) = (0, 2, 3), where:
 * nums[0] = 0, nums[2] = 0, nums[3] = 0
 * nums[0] = nums[2] * 2 = 0
 * nums[3] = nums[2] * 2 = 0
 *
 * Example 3:
 *
 * Input: nums = [8,4,2,8,4]
 * Output: 2
 * Explanation:
 * There are exactly 2 special triplets:
 * (i, j, k) = (0, 1, 3)
 * nums[0] = 8, nums[1] = 4, nums[3] = 8
 * nums[0] = nums[1] * 2 = 8
 * nums[3] = nums[1] * 2 = 8
 * (i, j, k) = (1, 2, 4)
 * nums[1] = 4, nums[2] = 2, nums[4] = 4
 * nums[1] = nums[2] * 2 = 4
 * nums[4] = nums[2] * 2 = 4
 *
 *
 * Constraints:
 *
 * 3 <= n == nums.length <= 10^5
 * 0 <= nums[i] <= 10^5
 *
 */
public class CountSpecialTriplets {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: FIX THE MIDDLE INDEX AND MULTIPLY THE TWO SIDE COUNTS
    //   both outer elements must equal 2 * nums[j], and once j is fixed the two
    //   choices are independent. sweep j while keeping counters of everything
    //   strictly left and strictly right of j; each j adds left[t] * right[t].
    /**
     * time = O(N)
     * space = O(N)
     */
    public int specialTriplets(int[] nums) {
        Map<Integer, Integer> left = new HashMap<>();
        Map<Integer, Integer> right = new HashMap<>();
        for (int num : nums) {
            right.put(num, right.getOrDefault(num, 0) + 1);
        }

        long triplets = 0;
        for (int num : nums) {
            // nums[j] itself is neither left nor right of j
            right.put(num, right.get(num) - 1);

            int target = num * 2;
            long leftCount = left.getOrDefault(target, 0);
            long rightCount = right.getOrDefault(target, 0);
            // each factor is < 10^5, so the product fits in a long before the mod
            triplets = (triplets + leftCount * rightCount) % MOD;

            left.put(num, left.getOrDefault(num, 0) + 1);
        }
        return (int) triplets;
    }
}
