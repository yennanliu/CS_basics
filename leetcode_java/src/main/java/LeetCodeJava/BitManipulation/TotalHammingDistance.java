package LeetCodeJava.BitManipulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://leetcode.com/problems/total-hamming-distance/

/**
 *  477. Total Hamming Distance
 *  Medium
 *
 *  The Hamming distance between two integers is the number of positions at
 *  which the corresponding bits are different.
 *
 *  Given an integer array nums, return the sum of Hamming distances between all
 *  the pairs of the integers in nums.
 *
 *  Example 1:
 *  Input: nums = [4,14,2]
 *  Output: 6
 *  Explanation: In binary representation, 4 is 0100, 14 is 1110, and 2 is 0010.
 *  HammingDistance(4, 14) + HammingDistance(4, 2) + HammingDistance(14, 2)
 *   = 2 + 2 + 2 = 6.
 *
 *  Example 2:
 *  Input: nums = [4,14,4]
 *  Output: 4
 *
 *  Constraints:
 *  1 <= nums.length <= 10^4
 *  0 <= nums[i] <= 10^9
 *  The answer for the given input will fit in a 32-bit integer.
 */
public class TotalHammingDistance {

    // V0
    // IDEA: count per bit column - if k numbers have that bit set and (n - k)
    //       don't, that column contributes k * (n - k) to the total
    /**
     * time = O(32 * n)
     * space = O(1)
     */
    public int totalHammingDistance(int[] nums) {
        int n = nums.length;
        int res = 0;
        for (int bit = 0; bit < 32; bit++) {
            int ones = 0;
            for (int num : nums) {
                ones += (num >> bit) & 1;
            }
            res += ones * (n - ones);
        }
        return res;
    }

    // V1
    // IDEA: brute force O(n^2) - kept as a readable correctness reference: sum
    //       popcount(a ^ b) over every unordered pair
    /**
     * time = O(n^2)
     * space = O(1)
     */
    public int totalHammingDistance_1(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                res += Integer.bitCount(nums[i] ^ nums[j]);
            }
        }
        return res;
    }

    // V2
    // IDEA: collapse duplicates with a HashMap<value, count>. Equal values contribute 0,
    //       so only pairs of DISTINCT values matter, each weighted by cnt[a] * cnt[b].
    //       Much cheaper than V1 when the array has few distinct values.
    /**
     * time = O(n + d^2) with d = number of distinct values
     * space = O(d)
     */
    public int totalHammingDistance_2(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        List<Integer> vals = new ArrayList<>(freq.keySet());
        long res = 0;
        for (int i = 0; i < vals.size(); i++) {
            for (int j = i + 1; j < vals.size(); j++) {
                int a = vals.get(i);
                int b = vals.get(j);
                res += (long) Integer.bitCount(a ^ b) * freq.get(a) * freq.get(b);
            }
        }
        return (int) res;
    }
}
