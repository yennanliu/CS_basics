package LeetCodeJava.HashTable;

// https://leetcode.com/problems/make-sum-divisible-by-p/description/

import java.util.*;

/**
 * 1590. Make Sum Divisible by P
 * Medium
 *
 * Given an array of positive integers nums, remove the smallest subarray (possibly empty) such that
 * the sum of the remaining elements is divisible by p. It is not allowed to remove the whole array.
 *
 * Return the length of the smallest subarray that you need to remove, or -1 if it's impossible.
 *
 * A subarray is defined as a contiguous block of elements in the array.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,1,4,2], p = 6
 * Output: 1
 * Explanation: The sum of the elements in nums is 10, which is not divisible by 6. We can remove
 * the subarray [4], and the sum of the remaining elements is 6, which is divisible by 6.
 *
 * Example 2:
 *
 * Input: nums = [6,3,5,2], p = 9
 * Output: 2
 * Explanation: We cannot remove a single element to get a sum divisible by 9. The best way is to
 * remove the subarray [5,2], leaving us with [6,3] with sum 9.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3], p = 3
 * Output: 0
 * Explanation: Here the sum is 6. which is already divisible by 3. Thus we do not need to remove anything.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 1 <= p <= 10^9
 *
 */
public class MakeSumDivisibleByP {

    // V0
    // IDEA: PREFIX SUM MOD + HASH TABLE (last index of each prefix remainder)
    //       the removed subarray must have sum % p == need = total % p, i.e. two prefixes with
    //       prefix[j] - prefix[i] == need (mod p)  ->  prefix[i] == (prefix[j] - need) mod p
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minSubarray(int[] nums, int p) {
        int n = nums.length;

        // the total can reach 10^14: reduce it mod p in a long
        long total = 0;
        for (int num : nums) {
            total = (total + num) % p;
        }
        int need = (int) total;
        if (need == 0) {
            return 0;
        }

        // remainder of a prefix -> the last index where that prefix ended (empty prefix ends at -1)
        Map<Integer, Integer> lastIndex = new HashMap<>();
        lastIndex.put(0, -1);

        int shortest = n;
        long prefix = 0;
        for (int i = 0; i < n; i++) {
            prefix = (prefix + nums[i]) % p;
            // + p keeps the remainder non-negative
            int want = (int) ((prefix - need + p) % p);
            if (lastIndex.containsKey(want)) {
                shortest = Math.min(shortest, i - lastIndex.get(want));
            }
            lastIndex.put((int) prefix, i);
        }
        // removing the whole array is not allowed
        return shortest < n ? shortest : -1;
    }
}
