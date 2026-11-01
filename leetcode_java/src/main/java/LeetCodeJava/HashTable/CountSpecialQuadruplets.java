package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-special-quadruplets/description/

import java.util.*;

/**
 * 1995. Count Special Quadruplets
 * Easy
 *
 * Given a 0-indexed integer array nums, return the number of distinct quadruplets (a, b, c, d) such that:
 *
 * nums[a] + nums[b] + nums[c] == nums[d], and
 * a < b < c < d
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,6]
 * Output: 1
 * Explanation: The only quadruplet that satisfies the requirement is (0, 1, 2, 3) because 1 + 2 + 3 == 6.
 *
 * Example 2:
 *
 * Input: nums = [3,3,6,4,5]
 * Output: 0
 * Explanation: There are no such quadruplets in [3,3,6,4,5].
 *
 * Example 3:
 *
 * Input: nums = [1,1,1,3,5]
 * Output: 4
 * Explanation: The 4 quadruplets that satisfy the requirement are:
 * - (0, 1, 2, 3): 1 + 1 + 1 == 3
 * - (0, 1, 3, 4): 1 + 1 + 3 == 5
 * - (0, 2, 3, 4): 1 + 1 + 3 == 5
 * - (1, 2, 3, 4): 1 + 1 + 3 == 5
 *
 *
 * Constraints:
 *
 * 4 <= nums.length <= 50
 * 1 <= nums[i] <= 100
 *
 */
public class CountSpecialQuadruplets {

    // V0
    // IDEA: HASH TABLE ON nums[d] - nums[c]  (O(N^2) instead of O(N^4))
    //       nums[a] + nums[b] == nums[d] - nums[c]: the left side depends on (a, b), the right on
    //       (c, d), and b < c separates them. Sweep b right to left; before handling b, add every
    //       pair with c == b + 1 to the counter, so it always holds all (c, d) with c > b.
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int countQuadruplets(int[] nums) {
        int n = nums.length;
        // nums[d] - nums[c] -> number of (c, d) pairs with c > current b
        Map<Integer, Integer> diffCount = new HashMap<>();
        int quadruplets = 0;

        for (int b = n - 3; b >= 1; b--) {
            // fold in the (c, d) pairs that just became valid: c = b + 1
            int c = b + 1;
            for (int d = c + 1; d < n; d++) {
                int diff = nums[d] - nums[c];
                diffCount.put(diff, diffCount.getOrDefault(diff, 0) + 1);
            }
            // every a < b pairs with all (c, d) whose difference equals nums[a] + nums[b]
            for (int a = 0; a < b; a++) {
                quadruplets += diffCount.getOrDefault(nums[a] + nums[b], 0);
            }
        }
        return quadruplets;
    }
}
