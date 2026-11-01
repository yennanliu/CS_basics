package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-great-partitions/description/

/**
 * 2518. Number of Great Partitions
 * Hard
 *
 * You are given an array nums consisting of positive integers and an integer k.
 *
 * Partition the array into two ordered groups such that each element is in exactly one group. A
 * partition is called great if the sum of elements of each group is greater than or equal to k.
 *
 * Return the number of distinct great partitions. Since the answer may be too large, return it
 * modulo 10^9 + 7.
 *
 * Two partitions are considered distinct if some element nums[i] is in different groups in the
 * two partitions.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4], k = 4
 * Output: 6
 * Explanation: The great partitions are: ([1,2,3], [4]), ([1,3], [2,4]), ([1,4], [2,3]), ([2,3], [1,4]), ([2,4], [1,3]) and ([4], [1,2,3]).
 *
 * Example 2:
 *
 * Input: nums = [3,3,3], k = 4
 * Output: 0
 * Explanation: There are no great partitions for this array.
 *
 * Example 3:
 *
 * Input: nums = [6,6], k = 2
 * Output: 2
 * Explanation: We can either put nums[0] in the first partition or in the second partition.
 * The great partitions will be ([6], [6]) and ([6], [6]).
 *
 *
 * Constraints:
 *
 * 1 <= nums.length, k <= 1000
 * 1 <= nums[i] <= 10^9
 *
 */
public class NumberOfGreatPartitions {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COMPLEMENTARY COUNTING + 0/1 KNAPSACK - ANSWER = 2^n - 2 * (#SUBSETS WITH SUM < k)
    /**
     * time = O(N * K)
     * space = O(K)
     */
    public int countPartitions(int[] nums, int k) {
        // if the total is under 2k, both groups can never reach k
        long totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        if (totalSum < 2L * k) {
            return 0;
        }

        // with total >= 2k, "group A < k" and "group B < k" cannot both hold, so the bad
        // partitions are exactly 2 * (#subsets whose sum is < k)

        // ways[s] = number of subsets whose sum is exactly s, for s < k
        long[] ways = new long[k];
        ways[0] = 1;
        for (int num : nums) {
            // an element >= k can never sit in a sub-k subset
            if (num >= k) {
                continue;
            }
            // iterate sums downwards so each element is used at most once (0/1 knapsack)
            for (int s = k - 1; s >= num; s--) {
                ways[s] = (ways[s] + ways[s - num]) % MOD;
            }
        }

        // 2^n ordered partitions in total
        long allPartitions = 1;
        for (int i = 0; i < nums.length; i++) {
            allPartitions = allPartitions * 2 % MOD;
        }

        long underK = 0;
        for (int s = 0; s < k; s++) {
            underK = (underK + ways[s]) % MOD;
        }

        // add MOD twice before the final mod so the result is never negative
        long result = (allPartitions - 2 * underK % MOD + 2L * MOD) % MOD;
        return (int) result;
    }
}
