package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/make-the-xor-of-all-segments-equal-to-zero/description/

import java.util.*;

/**
 * 1787. Make the XOR of All Segments Equal to Zero
 * Hard
 *
 * You are given an array nums and an integer k. The XOR of a segment [left, right] where
 * left <= right is the XOR of all the elements with indices between left and right,
 * inclusive: nums[left] XOR nums[left+1] XOR ... XOR nums[right].
 *
 * Return the minimum number of elements to change in the array such that the XOR of all
 * segments of size k is equal to zero.
 *
 * Example 1:
 *
 * Input: nums = [1,2,0,3,0], k = 1
 * Output: 3
 * Explanation: Modify the array from [1,2,0,3,0] to from [0,0,0,0,0].
 *
 * Example 2:
 *
 * Input: nums = [3,4,5,2,1,7,3,4,7], k = 3
 * Output: 3
 * Explanation: Modify the array from [3,4,5,2,1,7,3,4,7] to [3,4,7,3,4,7,3,4,7].
 *
 * Example 3:
 *
 * Input: nums = [1,2,4,1,2,5,1,2,6], k = 3
 * Output: 3
 * Explanation: Modify the array from [1,2,4,1,2,5,1,2,6] to [1,2,3,1,2,3,1,2,3].
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 2000
 * 0 <= nums[i] < 2^10
 *
 */
public class MakeTheXorOfAllSegmentsEqualToZero {

    // V0
    // IDEA: GROUP BY INDEX MOD K + DP OVER (group, running XOR)
    /**
     *  Every window of size k XORs to 0  <=>  nums[i] == nums[i + k] for all i,
     *  and the first k values XOR to 0. So each residue group (i % k) must end up
     *  holding one single value, and the k chosen values must XOR to 0.
     *
     *  DP def:
     *     - dp[x] = min changes over groups 0..g-1 so that their chosen values XOR to x
     *
     *  DP eq (for group g with size sz and value counts cnt):
     *     - pick a value v that already appears in the group:
     *           next[x] = dp[x ^ v] + sz - cnt[v]
     *     - pick a value NOT in the group (every element changes):
     *           next[x] = min(dp) + sz
     *
     *  Answer: dp[0] after all k groups.
     *
     *  time = O(K * 2^10 * (N / K)) = O(2^10 * N)
     *  space = O(N + 2^10)
     */
    private static final int MAX_XOR = 1 << 10; // all values are below 2^10
    private static final int INF = Integer.MAX_VALUE / 2;

    public int minChanges(int[] nums, int k) {
        // value counts and size of each residue group
        List<Map<Integer, Integer>> groupCounts = new ArrayList<>();
        int[] groupSize = new int[k];
        for (int g = 0; g < k; g++) {
            groupCounts.add(new HashMap<>());
        }
        for (int i = 0; i < nums.length; i++) {
            Map<Integer, Integer> counts = groupCounts.get(i % k);
            counts.put(nums[i], counts.getOrDefault(nums[i], 0) + 1);
            groupSize[i % k]++;
        }

        int[] dp = new int[MAX_XOR];
        Arrays.fill(dp, INF);
        dp[0] = 0; // no groups chosen yet, XOR is 0 at no cost

        for (int g = 0; g < k; g++) {
            int size = groupSize[g];

            // option "brand new value": reach any XOR from the cheapest previous state
            int cheapestPrev = INF;
            for (int x = 0; x < MAX_XOR; x++) {
                cheapestPrev = Math.min(cheapestPrev, dp[x]);
            }
            int[] next = new int[MAX_XOR];
            Arrays.fill(next, cheapestPrev + size);

            // option "keep a value already in the group"
            for (int x = 0; x < MAX_XOR; x++) {
                for (Map.Entry<Integer, Integer> entry : groupCounts.get(g).entrySet()) {
                    int value = entry.getKey();
                    int kept = entry.getValue();
                    // previous XOR must be x ^ value so that adding value gives x
                    int candidate = dp[x ^ value] + size - kept;
                    if (candidate < next[x]) {
                        next[x] = candidate;
                    }
                }
            }
            dp = next;
        }
        return dp[0];
    }
}
