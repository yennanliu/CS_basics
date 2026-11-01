package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-sum-of-squared-difference/description/

/**
 * 2333. Minimum Sum of Squared Difference
 * Medium
 *
 * You are given two positive 0-indexed integer arrays nums1 and nums2, both of length n.
 *
 * The sum of squared difference of arrays nums1 and nums2 is defined as the sum of
 * (nums1[i] - nums2[i])^2 for each 0 <= i < n.
 *
 * You are also given two positive integers k1 and k2. You can modify any of the elements of nums1 by +1
 * or -1 at most k1 times. Similarly, you can modify any of the elements of nums2 by +1 or -1 at most k2
 * times.
 *
 * Return the minimum sum of squared difference after modifying array nums1 at most k1 times and
 * modifying array nums2 at most k2 times.
 *
 * Note: You are allowed to modify the array elements to become negative integers.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,2,3,4], nums2 = [2,10,20,19], k1 = 0, k2 = 0
 * Output: 579
 * Explanation: The elements in nums1 and nums2 cannot be modified because k1 = 0 and k2 = 0.
 * The sum of square difference will be: (1 - 2)^2 + (2 - 10)^2 + (3 - 20)^2 + (4 - 19)^2 = 579.
 *
 * Example 2:
 *
 * Input: nums1 = [1,4,10,12], nums2 = [5,8,6,9], k1 = 1, k2 = 1
 * Output: 43
 * Explanation: One way to obtain the minimum sum of square difference is:
 * - Increase nums1[0] once.
 * - Increase nums2[2] once.
 * The minimum of the sum of square difference will be:
 * (2 - 5)^2 + (4 - 8)^2 + (10 - 7)^2 + (12 - 9)^2 = 43.
 * Note that, there are other ways to obtain the minimum of the sum of square difference, but there is
 * no way to obtain a sum smaller than 43.
 *
 *
 * Constraints:
 *
 * n == nums1.length == nums2.length
 * 1 <= n <= 10^5
 * 0 <= nums1[i], nums2[i] <= 10^5
 * 0 <= k1, k2 <= 10^9
 *
 */
public class MinimumSumOfSquaredDifference {

    // V0
    // IDEA: ONLY |nums1[i] - nums2[i]| MATTERS -> BINARY SEARCH THE CAP
    /**
     *  A change on either array moves one difference by 1, so the budgets merge into
     *  k = k1 + k2: spend k unit decrements across the diffs to minimise the sum of squares.
     *
     *  Squares are convex, so the optimum levels the largest diffs down to a cap t:
     *    cost(t) = sum(max(0, d - t)) must be <= k, and cost is monotone in t
     *  -> binary search the smallest feasible t. Leftover budget then pushes that many
     *  entries sitting at t down to t - 1, one unit each.
     *  If k covers the whole sum of diffs, everything becomes 0.
     */
    /**
     * time = O(N log(max diff))
     * space = O(N)
     */
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        int n = nums1.length;
        long budget = (long) k1 + k2;

        int[] diffs = new int[n];
        long totalDiff = 0;
        int maxDiff = 0;
        for (int i = 0; i < n; i++) {
            diffs[i] = Math.abs(nums1[i] - nums2[i]);
            totalDiff += diffs[i];
            maxDiff = Math.max(maxDiff, diffs[i]);
        }
        // enough budget to zero every difference
        if (totalDiff <= budget) {
            return 0;
        }

        // smallest cap whose levelling cost fits the budget
        int left = 0;
        int right = maxDiff;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (levelCost(diffs, mid) <= budget) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        int cap = left;

        long remaining = budget - levelCost(diffs, cap);
        long result = 0;
        long atCap = 0;
        for (int diff : diffs) {
            if (diff >= cap) {
                // this entry is levelled down to the cap
                atCap++;
            } else {
                result += (long) diff * diff;
            }
        }
        // remaining < atCap (or the cap could have been lower), so this split is valid
        result += (atCap - remaining) * cap * cap;
        result += remaining * (cap - 1L) * (cap - 1L);
        return result;
    }

    /** Units needed to bring every difference down to at most `cap`. */
    private long levelCost(int[] diffs, int cap) {
        long cost = 0;
        for (int diff : diffs) {
            if (diff > cap) {
                cost += diff - cap;
            }
        }
        return cost;
    }
}
