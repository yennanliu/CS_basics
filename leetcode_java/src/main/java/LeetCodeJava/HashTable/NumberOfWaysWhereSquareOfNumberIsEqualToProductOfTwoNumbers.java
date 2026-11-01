package LeetCodeJava.HashTable;

// https://leetcode.com/problems/number-of-ways-where-square-of-number-is-equal-to-product-of-two-numbers/description/

import java.util.*;

/**
 * 1577. Number of Ways Where Square of Number Is Equal to Product of Two Numbers
 * Medium
 *
 * Given two arrays of integers nums1 and nums2, return the number of triplets formed (type 1 and
 * type 2) under the following rules:
 *
 * Type 1: Triplet (i, j, k) if nums1[i]^2 == nums2[j] * nums2[k] where 0 <= i < nums1.length and 0 <= j < k < nums2.length.
 * Type 2: Triplet (i, j, k) if nums2[i]^2 == nums1[j] * nums1[k] where 0 <= i < nums2.length and 0 <= j < k < nums1.length.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [7,4], nums2 = [5,2,8,9]
 * Output: 1
 * Explanation: Type 1: (1, 1, 2), nums1[1]^2 = nums2[1] * nums2[2]. (4^2 = 2 * 8).
 *
 * Example 2:
 *
 * Input: nums1 = [1,1], nums2 = [1,1,1]
 * Output: 9
 * Explanation: All Triplets are valid, because 1^2 = 1 * 1.
 *
 * Example 3:
 *
 * Input: nums1 = [7,7,8,3], nums2 = [1,2,9,7]
 * Output: 2
 * Explanation: There are 2 valid triplets.
 * Type 1: (3,0,2).  nums1[3]^2 = nums2[0] * nums2[2].
 * Type 2: (3,0,1).  nums2[3]^2 = nums1[0] * nums1[1].
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 1000
 * 1 <= nums1[i], nums2[i] <= 10^5
 *
 */
public class NumberOfWaysWhereSquareOfNumberIsEqualToProductOfTwoNumbers {

    // V0
    // IDEA: HASH TABLE (count values of the other array, then count pairs whose product is x*x)
    /**
     * time = O((N1 + N2) * D), D = number of distinct values
     * space = O(D)
     */
    public int numTriplets(int[] nums1, int[] nums2) {
        Map<Long, Integer> count1 = countValues(nums1);
        Map<Long, Integer> count2 = countValues(nums2);
        long total = countPairs(nums1, count2) + countPairs(nums2, count1);
        return (int) total;
    }

    /** For each x in squares, counts index pairs j < k of the other array with product x * x. */
    private long countPairs(int[] squares, Map<Long, Integer> otherCount) {
        long pairs = 0;
        for (int x : squares) {
            // x * x reaches 10^10, so it needs a long
            long target = (long) x * x;
            for (Map.Entry<Long, Integer> entry : otherCount.entrySet()) {
                long value = entry.getKey();
                long copies = entry.getValue();
                if (target % value != 0) {
                    continue;
                }
                long partner = target / value;
                if (partner == value) {
                    // both picks come from the same value: choose 2 of its copies
                    pairs += copies * (copies - 1) / 2;
                } else if (partner > value && otherCount.containsKey(partner)) {
                    // partner > value counts each unordered pair of values once
                    pairs += copies * otherCount.get(partner);
                }
            }
        }
        return pairs;
    }

    /** Value -> number of occurrences. */
    private Map<Long, Integer> countValues(int[] nums) {
        Map<Long, Integer> count = new HashMap<>();
        for (int num : nums) {
            long key = num;
            count.put(key, count.getOrDefault(key, 0) + 1);
        }
        return count;
    }
}
