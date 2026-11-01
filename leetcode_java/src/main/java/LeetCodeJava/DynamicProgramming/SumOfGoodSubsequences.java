package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/sum-of-good-subsequences/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3351. Sum of Good Subsequences
 * Hard
 *
 * You are given an integer array nums. A good subsequence is defined as a subsequence of nums
 * where the absolute difference between any two consecutive elements in the subsequence is exactly 1.
 *
 * Return the sum of all possible good subsequences of nums.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Note that a subsequence of size 1 is considered good by definition.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,1]
 * Output: 14
 * Explanation:
 * Good subsequences are: [1], [2], [1], [1,2], [2,1], [1,2,1].
 * The sum of elements in these subsequences is 14.
 *
 * Example 2:
 *
 * Input: nums = [3,4,5]
 * Output: 40
 * Explanation:
 * Good subsequences are: [3], [4], [5], [3,4], [4,5], [3,4,5].
 * The sum of elements in these subsequences is 40.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^5
 *
 */
public class SumOfGoodSubsequences {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP KEYED BY LAST VALUE — COUNT AND SUM OF GOOD SUBSEQUENCES ENDING IN v
    /**
     *  DP def:
     *     - count[v] = number of good subsequences ending in value v
     *     - total[v] = sum of all elements over those subsequences
     *  DP eq, when x arrives (it can extend anything ending in x-1 or x+1, or start alone):
     *     - newCount = count[x-1] + count[x+1] + 1
     *     - newTotal = total[x-1] + total[x+1] + x * newCount   (x is added once to each)
     *     - count[x] += newCount, total[x] += newTotal
     *  Answer: sum of total[v] — each subsequence is counted once, by its last element.
     *
     *  time = O(N)
     *  space = O(N)
     */
    public int sumOfGoodSubsequences(int[] nums) {
        Map<Integer, Long> count = new HashMap<>();
        Map<Integer, Long> total = new HashMap<>();

        for (int x : nums) {
            long newCount = (count.getOrDefault(x - 1, 0L) + count.getOrDefault(x + 1, 0L) + 1) % MOD;
            long newTotal = (total.getOrDefault(x - 1, 0L) + total.getOrDefault(x + 1, 0L)
                    + (long) x * newCount) % MOD;
            count.put(x, (count.getOrDefault(x, 0L) + newCount) % MOD);
            total.put(x, (total.getOrDefault(x, 0L) + newTotal) % MOD);
        }

        long answer = 0;
        for (long value : total.values()) {
            answer = (answer + value) % MOD;
        }
        return (int) answer;
    }
}
