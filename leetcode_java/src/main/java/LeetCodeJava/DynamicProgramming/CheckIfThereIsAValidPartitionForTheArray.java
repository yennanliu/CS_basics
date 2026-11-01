package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/check-if-there-is-a-valid-partition-for-the-array/description/

/**
 * 2369. Check if There is a Valid Partition For The Array
 * Medium
 *
 * You are given a 0-indexed integer array nums. You have to partition the array into one or more contiguous subarrays.
 *
 * We call a partition of the array valid if each of the obtained subarrays satisfies one of the following conditions:
 *
 * The subarray consists of exactly 2 equal elements. For example, the subarray [2,2] is good.
 * The subarray consists of exactly 3 equal elements. For example, the subarray [4,4,4] is good.
 * The subarray consists of exactly 3 consecutive increasing elements, that is, the difference between adjacent elements is 1. For example, the subarray [3,4,5] is good, but the subarray [1,3,5] is not.
 *
 * Return true if the array has at least one valid partition. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,4,4,5,6]
 * Output: true
 * Explanation: The array can be partitioned into the subarrays [4,4] and [4,5,6].
 * This partition is valid, so we return true.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,2]
 * Output: false
 * Explanation: There is no valid partition for this array.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^6
 *
 */
public class CheckIfThereIsAValidPartitionForTheArray {

    // V0
    // IDEA: PREFIX DP — dp[i] = nums[0..i-1] CAN BE SPLIT; THE LAST BLOCK HAS LENGTH 2 OR 3
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean validPartition(int[] nums) {
        int n = nums.length;

        // dp[i] = the first i elements can be partitioned validly; empty prefix is valid
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;

        for (int i = 2; i <= n; i++) {
            int last = nums[i - 1];
            int secondLast = nums[i - 2];

            // last block = two equal elements
            if (dp[i - 2] && secondLast == last) {
                dp[i] = true;
                continue;
            }

            if (i >= 3 && dp[i - 3]) {
                int thirdLast = nums[i - 3];
                // last block = three equal elements
                boolean threeEqual = thirdLast == secondLast && secondLast == last;
                // last block = three consecutive increasing elements
                boolean threeConsecutive = secondLast == thirdLast + 1 && last == secondLast + 1;
                if (threeEqual || threeConsecutive) {
                    dp[i] = true;
                }
            }
        }
        return dp[n];
    }
}
