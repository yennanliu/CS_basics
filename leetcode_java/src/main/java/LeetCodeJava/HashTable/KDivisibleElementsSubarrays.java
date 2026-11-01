package LeetCodeJava.HashTable;

// https://leetcode.com/problems/k-divisible-elements-subarrays/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 2261. K Divisible Elements Subarrays
 * Medium
 *
 * Given an integer array nums and two integers k and p, return the number of distinct subarrays, which have at most k elements that are divisible by p.
 *
 * Two arrays nums1 and nums2 are said to be distinct if:
 *
 * They are of different lengths, or
 * There exists at least one index i where nums1[i] != nums2[i].
 *
 * A subarray is defined as a non-empty contiguous sequence of elements in an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,3,2,2], k = 2, p = 2
 * Output: 11
 * Explanation:
 * The elements at indices 0, 3, and 4 are divisible by p = 2.
 * The 11 distinct subarrays which have at most k = 2 elements divisible by 2 are:
 * [2], [2,3], [2,3,3], [2,3,3,2], [3], [3,3], [3,3,2], [3,3,2,2], [3,2], [3,2,2], and [2,2].
 * Note that the subarrays [2] and [3] occur more than once in nums, but they should each be counted only once.
 * The subarray [2,3,3,2,2] should not be counted because it has 3 elements that are divisible by 2.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4], k = 4, p = 1
 * Output: 10
 * Explanation:
 * All element of nums are divisible by p = 1.
 * Also, every subarray of nums will have at most 4 elements that are divisible by 1.
 * Since all subarrays are distinct, the total number of subarrays satisfying all the constraints is 10.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 200
 * 1 <= nums[i], p <= 200
 * 1 <= k <= nums.length
 *
 * Follow up:
 *
 * Can you solve this problem in O(n^2) time complexity?
 *
 */
public class KDivisibleElementsSubarrays {

    // V0
    // IDEA: ENUMERATE SUBARRAYS + HASH SET OF THEIR CONTENTS (n <= 200)
    //   fix the left end i and extend j, counting elements divisible by p; once that count
    //   exceeds k no longer window from i can qualify, so stop. "distinct" means distinct
    //   CONTENT, so each window is stored as a string key built incrementally.
    /**
     * time = O(N^3)
     * space = O(N^3)
     */
    public int countDistinct(int[] nums, int k, int p) {
        int n = nums.length;
        Set<String> seen = new HashSet<>();

        for (int i = 0; i < n; i++) {
            int divisibleCount = 0;
            StringBuilder window = new StringBuilder();
            for (int j = i; j < n; j++) {
                if (nums[j] % p == 0) {
                    divisibleCount++;
                }
                if (divisibleCount > k) {
                    break;
                }
                // a separator keeps [1, 23] and [12, 3] from producing the same key
                window.append(nums[j]).append(',');
                seen.add(window.toString());
            }
        }
        return seen.size();
    }
}
