package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-operations-to-make-elements-within-k-subarrays-equal/description/

import java.util.*;

/**
 * 3505. Minimum Operations to Make Elements Within K Subarrays Equal
 * Hard
 *
 * You are given an integer array nums and two integers, x and k. You can perform the
 * following operation any number of times (including zero):
 *
 * Increase or decrease any element of nums by 1.
 *
 * Return the minimum number of operations needed to have at least k non-overlapping
 * subarrays of size exactly x in nums, where all elements within each subarray are equal.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,-2,1,3,7,3,6,4,-1], x = 3, k = 2
 * Output: 8
 * Explanation: Make [1, 1, 1] at indices 1..3 (5 operations) and [4, 4, 4] at indices
 * 5..7 (3 operations).
 *
 * Example 2:
 *
 * Input: nums = [9,-2,-2,-2,1,5], x = 2, k = 2
 * Output: 3
 * Explanation: Subtract 3 from nums[4]; [-2, -2] at 1..2 and [-2, -2] at 3..4.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * -10^6 <= nums[i] <= 10^6
 * 2 <= x <= nums.length
 * 1 <= k <= 15
 * 2 <= k * x <= nums.length
 *
 */
public class MinimumOperationsToMakeElementsWithinKSubarraysEqual {

    private static final long INF = Long.MAX_VALUE / 2;

    // V0
    // IDEA: SLIDING-WINDOW MEDIAN COST (TWO ORDERED SETS) + DP OVER NUMBER OF PICKED WINDOWS
    /**
     *  Step 1: making one window equal costs sum |a_i - t|, minimised at the MEDIAN.
     *          cost[i] = price of window nums[i .. i+x-1], via a sliding median kept in
     *          two ordered sets (lower half / upper half) plus their sums:
     *              cost = (median * lowCount - lowSum) + (highSum - median * highCount)
     *
     *  Step 2: DP def:
     *     - dp[j][i] = min total price choosing j disjoint windows inside nums[0 .. i-1]
     *          DP eq:
     *     - dp[j][i] = min( dp[j][i-1],                    // no window ends at i-1
     *                       dp[j-1][i-x] + cost[i-x] )     // window nums[i-x .. i-1]
     *     - init: dp[0][i] = 0.  Answer: dp[k][n]
     *
     *  time  = O(N log X + N * K)
     *  space = O(N)
     */
    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;
        long[] cost = windowCosts(nums, x);

        // previous layer: j - 1 windows chosen; j = 0 costs nothing
        long[] prev = new long[n + 1];
        for (int j = 1; j <= k; j++) {
            long[] cur = new long[n + 1];
            Arrays.fill(cur, INF);
            // j windows need at least j * x elements
            for (int i = j * x; i <= n; i++) {
                long skip = cur[i - 1];
                long take = prev[i - x] + cost[i - x];
                cur[i] = Math.min(skip, take);
            }
            prev = cur;
        }
        return prev[n];
    }

    /** cost[i] = min operations to make nums[i .. i+x-1] all equal (pull everything to the median). */
    private long[] windowCosts(int[] nums, int x) {
        int n = nums.length;
        long[] cost = new long[n - x + 1];

        // order indices by (value, index) so duplicates are distinct entries
        Comparator<Integer> byValue = (a, b) -> {
            if (nums[a] != nums[b]) {
                return Integer.compare(nums[a], nums[b]);
            }
            return Integer.compare(a, b);
        };
        TreeSet<Integer> low = new TreeSet<>(byValue);   // lower half; its max is the median
        TreeSet<Integer> high = new TreeSet<>(byValue);  // upper half
        long lowSum = 0;
        long highSum = 0;
        int lowTarget = (x + 1) / 2;                     // lower half owns the median

        for (int i = 0; i < n; i++) {
            // insert the new element on the correct side
            if (!low.isEmpty() && byValue.compare(i, low.last()) < 0) {
                low.add(i);
                lowSum += nums[i];
            } else {
                high.add(i);
                highSum += nums[i];
            }

            // remove the element that left the window
            if (i >= x) {
                int leaving = i - x;
                if (low.remove(leaving)) {
                    lowSum -= nums[leaving];
                } else {
                    high.remove(leaving);
                    highSum -= nums[leaving];
                }
            }

            // rebalance so low has exactly lowTarget elements (or all of them, early on)
            while (low.size() > lowTarget) {
                int moved = low.pollLast();
                lowSum -= nums[moved];
                high.add(moved);
                highSum += nums[moved];
            }
            while (low.size() < lowTarget && !high.isEmpty()) {
                int moved = high.pollFirst();
                highSum -= nums[moved];
                low.add(moved);
                lowSum += nums[moved];
            }

            // a full window ends at i
            if (i >= x - 1) {
                long median = nums[low.last()];
                long raiseLow = median * low.size() - lowSum;
                long lowerHigh = highSum - median * high.size();
                cost[i - x + 1] = raiseLow + lowerHigh;
            }
        }
        return cost;
    }
}
