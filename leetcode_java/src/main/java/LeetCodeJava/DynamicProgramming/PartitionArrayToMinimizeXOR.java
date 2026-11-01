package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/partition-array-to-minimize-xor/description/

import java.util.*;

/**
 * 3599. Partition Array to Minimize XOR
 * Medium
 *
 * You are given an integer array nums and an integer k.
 *
 * Your task is to partition nums into k non-empty subarrays. For each subarray, compute the bitwise XOR of all its elements.
 *
 * Return the minimum possible value of the maximum XOR among these k subarrays.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3], k = 2
 * Output: 1
 * Explanation:
 * The optimal partition is [1] and [2, 3].
 * XOR of the first subarray is 1.
 * XOR of the second subarray is 2 XOR 3 = 1.
 * The maximum XOR among the subarrays is 1, which is the minimum possible.
 *
 * Example 2:
 *
 * Input: nums = [2,3,3,2], k = 3
 * Output: 2
 * Explanation:
 * The optimal partition is [2], [3, 3], and [2].
 * XOR of the first subarray is 2.
 * XOR of the second subarray is 3 XOR 3 = 0.
 * XOR of the third subarray is 2.
 * The maximum XOR among the subarrays is 2, which is the minimum possible.
 *
 * Example 3:
 *
 * Input: nums = [1,1,2,3,1], k = 2
 * Output: 0
 * Explanation:
 * The optimal partition is [1, 1] and [2, 3, 1].
 * XOR of the first subarray is 1 XOR 1 = 0.
 * XOR of the second subarray is 2 XOR 3 XOR 1 = 0.
 * The maximum XOR among the subarrays is 0, which is the minimum possible.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 250
 * 1 <= nums[i] <= 10^9
 * 1 <= k <= n
 *
 */
public class PartitionArrayToMinimizeXOR {

    // V0
    // IDEA: DP OVER (NUMBER OF PARTS, PREFIX LENGTH), MINIMISING THE LARGEST SEGMENT XOR
    //   prefixXor[i] = nums[0] ^ ... ^ nums[i-1], so xor(nums[t..i-1]) = prefixXor[i] ^ prefixXor[t]
    //   dp[j][i] = smallest possible "max segment xor" when the first i elements form j parts
    //   dp[j][i] = min over t in [j-1, i) of max(dp[j-1][t], prefixXor[i] ^ prefixXor[t])
    //   only the previous row is needed, so two 1-D arrays are rolled.
    /**
     * time = O(K * N^2)
     * space = O(N)
     */
    public int minXor(int[] nums, int k) {
        int n = nums.length;
        final int INF = Integer.MAX_VALUE;
        int[] prefixXor = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixXor[i + 1] = prefixXor[i] ^ nums[i];
        }

        int[] previous = new int[n + 1];
        Arrays.fill(previous, INF);
        // zero parts cover zero elements
        previous[0] = 0;
        for (int parts = 1; parts <= k; parts++) {
            int[] current = new int[n + 1];
            Arrays.fill(current, INF);
            // leave at least one element for each of the remaining k - parts parts
            for (int i = parts; i <= n - (k - parts); i++) {
                int best = INF;
                // the last part is nums[t..i-1]
                for (int t = parts - 1; t < i; t++) {
                    if (previous[t] == INF) {
                        continue;
                    }
                    int segmentXor = prefixXor[i] ^ prefixXor[t];
                    best = Math.min(best, Math.max(previous[t], segmentXor));
                }
                current[i] = best;
            }
            previous = current;
        }
        return previous[n];
    }
}
