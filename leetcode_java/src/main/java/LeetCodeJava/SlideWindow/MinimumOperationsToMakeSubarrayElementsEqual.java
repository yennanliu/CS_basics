package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/minimum-operations-to-make-subarray-elements-equal/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 3422. Minimum Operations to Make Subarray Elements Equal
 * Medium
 *
 * You are given an integer array nums and an integer k. You can perform the following operation
 * any number of times:
 *
 * Increase or decrease any element of nums by 1.
 *
 * Return the minimum number of operations required to ensure that at least one subarray of size
 * k in nums has all elements equal.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,-3,2,1,-4,6], k = 3
 * Output: 5
 * Explanation:
 * Use 4 operations to add 4 to nums[1]. The resulting array is [4, 1, 2, 1, -4, 6].
 * Use 1 operation to subtract 1 from nums[2]. The resulting array is [4, 1, 1, 1, -4, 6].
 * The array now contains a subarray [1, 1, 1] of size k = 3 with all elements equal. Hence, the
 * answer is 5.
 *
 * Example 2:
 *
 * Input: nums = [-2,-2,3,1,4], k = 2
 * Output: 0
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * -10^6 <= nums[i] <= 10^6
 * 2 <= k <= nums.length
 *
 */
public class MinimumOperationsToMakeSubarrayElementsEqual {

    // V0
    // IDEA: SLIDING WINDOW MEDIAN WITH TWO FENWICK TREES (COUNT AND SUM OVER COMPRESSED VALUES)
    //  for one window, sum |a_i - t| is minimised at a median t, and the cost is
    //      (median * j - sumLow) + (sumHigh - median * (k - j)),  j = (k + 1) / 2
    //  the count tree finds the j-th smallest by binary lifting; the sum tree gives sumLow.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long minOperations(int[] nums, int k) {
        int n = nums.length;

        // coordinate-compress the values to Fenwick indices 1..m
        int[] sortedValues = nums.clone();
        Arrays.sort(sortedValues);
        int m = 0;
        int[] values = new int[n];
        for (int v : sortedValues) {
            if (m == 0 || values[m - 1] != v) {
                values[m] = v;
                m++;
            }
        }
        Map<Integer, Integer> indexOf = new HashMap<>();
        for (int i = 0; i < m; i++) {
            indexOf.put(values[i], i + 1);
        }

        long[] countTree = new long[m + 1];
        long[] sumTree = new long[m + 1];
        // largest power of two <= m: the first step of the binary-lifting descent
        int topStep = Integer.highestOneBit(m);

        int medianRank = (k + 1) / 2;
        long windowSum = 0;
        long best = Long.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            update(countTree, sumTree, indexOf.get(nums[i]), 1, nums[i]);
            windowSum += nums[i];
            if (i >= k) {
                int leaving = nums[i - k];
                update(countTree, sumTree, indexOf.get(leaving), -1, -leaving);
                windowSum -= leaving;
            }
            if (i < k - 1) {
                continue;
            }

            // descend: largest position whose prefix count is still < medianRank
            int pos = 0;
            long countBefore = 0;
            long sumBefore = 0;
            for (int step = topStep; step > 0; step /= 2) {
                int next = pos + step;
                if (next <= m && countBefore + countTree[next] < medianRank) {
                    pos = next;
                    countBefore += countTree[next];
                    sumBefore += sumTree[next];
                }
            }
            // pos + 1 is the bucket holding the median value
            long median = values[pos];
            // the median bucket may hold several copies: take just enough to reach the rank
            long sumLow = sumBefore + (medianRank - countBefore) * median;
            long sumHigh = windowSum - sumLow;
            long cost = (median * medianRank - sumLow) + (sumHigh - median * (k - medianRank));
            best = Math.min(best, cost);
        }
        return best;
    }

    /** Fenwick point update: add deltaCount / deltaSum at index i. */
    private void update(long[] countTree, long[] sumTree, int i, int deltaCount, long deltaSum) {
        while (i < countTree.length) {
            countTree[i] += deltaCount;
            sumTree[i] += deltaSum;
            // i & (-i) is the lowest set bit of i: jump to the next node covering i
            i += i & (-i);
        }
    }
}
