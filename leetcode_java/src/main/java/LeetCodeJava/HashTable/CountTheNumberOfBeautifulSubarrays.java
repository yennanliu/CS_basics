package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-the-number-of-beautiful-subarrays/description/

import java.util.*;

/**
 * 2588. Count the Number of Beautiful Subarrays
 * Medium
 *
 * You are given a 0-indexed integer array nums. In one operation, you can:
 *
 * Choose two different indices i and j such that 0 <= i, j < nums.length.
 * Choose a non-negative integer k such that the kth bit (0-indexed) in the binary representation of nums[i] and nums[j] is 1.
 * Subtract 2^k from nums[i] and nums[j].
 *
 * A subarray is beautiful if it is possible to make all of its elements equal to 0 after applying the above operation any number of times (including zero).
 *
 * Return the number of beautiful subarrays in the array nums.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 * Note: Subarrays where all elements are initially 0 are considered beautiful, as no operation is needed.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,3,1,2,4]
 * Output: 2
 * Explanation: There are 2 beautiful subarrays in nums: [4,3,1,2,4] and [4,3,1,2,4].
 * - We can make all elements in the subarray [3,1,2] equal to 0 in the following way:
 *   - Choose [3, 1, 2] and k = 1. Subtract 2^1 from both numbers. The subarray becomes [1, 1, 0].
 *   - Choose [1, 1, 0] and k = 0. Subtract 2^0 from both numbers. The subarray becomes [0, 0, 0].
 * - We can make all elements in the subarray [4,3,1,2,4] equal to 0 in the following way:
 *   - Choose [4, 3, 1, 2, 4] and k = 2. Subtract 2^2 from both numbers. The subarray becomes [0, 3, 1, 2, 0].
 *   - Choose [0, 3, 1, 2, 0] and k = 0. Subtract 2^0 from both numbers. The subarray becomes [0, 2, 0, 2, 0].
 *   - Choose [0, 2, 0, 2, 0] and k = 1. Subtract 2^1 from both numbers. The subarray becomes [0, 0, 0, 0, 0].
 *
 * Example 2:
 *
 * Input: nums = [1,10,4]
 * Output: 0
 * Explanation: There are no beautiful subarrays in nums.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^6
 *
 */
public class CountTheNumberOfBeautifulSubarrays {

    // V0
    // IDEA: PREFIX XOR + HASH MAP (beautiful <=> every bit column has an even count <=> subarray XOR == 0)
    /**
     * time = O(N)
     * space = O(N)
     */
    public long beautifulSubarrays(int[] nums) {
        // prefix XOR value -> how many prefixes produced it
        Map<Integer, Integer> prefixCount = new HashMap<>();
        // the empty prefix, so subarrays starting at index 0 are counted
        prefixCount.put(0, 1);

        int prefixXor = 0;
        long result = 0;
        for (int num : nums) {
            prefixXor ^= num;
            // xor(nums[l..r]) == 0  <=>  prefix[l] == prefix[r + 1]
            int earlier = prefixCount.getOrDefault(prefixXor, 0);
            result += earlier;
            prefixCount.put(prefixXor, earlier + 1);
        }
        return result;
    }
}
