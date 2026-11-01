package LeetCodeJava.HashTable;

// https://leetcode.com/problems/number-of-unequal-triplets-in-array/description/

import java.util.*;

/**
 * 2475. Number of Unequal Triplets in Array
 * Easy
 *
 * You are given a 0-indexed array of positive integers nums. Find the number of triplets
 * (i, j, k) that meet the following conditions:
 *
 * 0 <= i < j < k < nums.length
 * nums[i], nums[j], and nums[k] are pairwise distinct.
 * In other words, nums[i] != nums[j], nums[i] != nums[k], and nums[j] != nums[k].
 *
 * Return the number of triplets that meet the conditions.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,4,2,4,3]
 * Output: 3
 * Explanation: The following triplets meet the conditions:
 * - (0, 2, 4) because 4 != 2 != 3
 * - (1, 2, 4) because 4 != 2 != 3
 * - (2, 3, 4) because 2 != 4 != 3
 * Since there are 3 triplets, we return 3.
 * Note that (2, 0, 4) is not a valid triplet because 2 > 0.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,1,1]
 * Output: 0
 * Explanation: No triplets meet the conditions so we return 0.
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 100
 * 1 <= nums[i] <= 1000
 *
 */
public class NumberOfUnequalTripletsInArray {

    // V0
    // IDEA: GROUP COUNTING - PICK 3 DISTINCT VALUE GROUPS; WALK GROUPS, ADD left * middle * right
    /**
     * time = O(N)
     * space = O(N)
     */
    public int unequalTriplets(int[] nums) {
        // index order does not matter: any 3 positions with distinct values sort into one triplet
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        int n = nums.length;
        int result = 0;
        int seenBefore = 0;   // elements in groups already walked (the "left" pool)

        for (int groupSize : count.values()) {
            // elements in groups not yet walked (the "right" pool)
            int seenAfter = n - seenBefore - groupSize;
            // this group is the middle one, so each unordered choice of 3 groups is counted once
            result += seenBefore * groupSize * seenAfter;
            seenBefore += groupSize;
        }
        return result;
    }

    // V1
    // IDEA: BRUTE FORCE OVER ALL i < j < k (n <= 100, SO ~1.6 * 10^5 TRIPLES)
    /**
     * time = O(N^3)
     * space = O(1)
     */
    public int unequalTriplets_1(int[] nums) {
        int n = nums.length;
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] == nums[j]) {
                    continue;
                }
                for (int k = j + 1; k < n; k++) {
                    if (nums[k] != nums[i] && nums[k] != nums[j]) {
                        result++;
                    }
                }
            }
        }
        return result;
    }
}
