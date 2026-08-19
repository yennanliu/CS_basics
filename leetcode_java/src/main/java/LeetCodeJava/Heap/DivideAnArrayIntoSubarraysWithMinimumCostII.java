package LeetCodeJava.Heap;

// https://leetcode.com/problems/divide-an-array-into-subarrays-with-minimum-cost-ii/

import java.util.Comparator;
import java.util.TreeSet;

/**
 *  3013. Divide an Array Into Subarrays With Minimum Cost II
 *  Hard
 *
 *  You are given a 0-indexed array of integers nums of length n, and two positive
 *  integers k and dist.
 *
 *  The cost of an array is the value of its first element. For example, the cost of
 *  [1,2,3] is 1 while the cost of [3,4,1] is 3.
 *
 *  You need to divide nums into k disjoint contiguous subarrays, such that the
 *  difference between the starting index of the second subarray and the starting
 *  index of the kth subarray is less than or equal to dist. In other words, if you
 *  split nums into nums[0..(i1-1)], nums[i1..(i2-1)], ..., nums[ik-1..(n-1)], then
 *  ik-1 - i1 <= dist.
 *
 *  Return the minimum possible sum of the cost of these subarrays.
 *
 *  Example 1:
 *    Input: nums = [1,3,2,6,4,2], k = 3, dist = 3
 *    Output: 5
 *    Explanation: [1,3], [2], [6,4,2] -> 1 + 2 + 2 = 5.
 *
 *  Example 2:
 *    Input: nums = [10,1,2,2,2,1], k = 4, dist = 3
 *    Output: 15
 *    Explanation: [10], [1], [2], [2,2,1] -> 10 + 1 + 2 + 2 = 15.
 *
 *  Constraints:
 *    3 <= n <= 10^5
 *    1 <= nums[i] <= 10^9
 *    3 <= k <= n
 *    k - 2 <= dist <= n - 2
 */
public class DivideAnArrayIntoSubarraysWithMinimumCostII {

    // V0
    // IDEA: SLIDING WINDOW OF WIDTH dist+1, KEEPING THE k-1 SMALLEST STARTS
    //
    //   nums[0] is always paid. The other k-1 subarray starts are distinct indices
    //   in 1..n-1 whose spread is at most dist, i.e. they all fit inside some
    //   window of dist+1 consecutive indices, so
    //
    //       answer = nums[0] + min over windows of (sum of the k-1 smallest)
    //
    //   Any window works: if the chosen starts sit inside [l, l+dist], the window
    //   anchored at their own minimum contains them too.
    //
    //   Maintaining "sum of the k-1 smallest" under insert AND delete is the real
    //   work. Two ordered sets straddle the cut, keyed by (value, index) so that
    //   duplicates stay distinct and an exact element can be deleted in O(log n):
    //       small : the k-1 smallest, with running sum
    //       large : everything else
    //   After every insert/delete, refill/trim `small` to k-1 elements and then
    //   swap while its max exceeds `large`'s min.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public long minimumCost(int[] nums, int k, int dist) {
        int n = nums.length;
        final int need = k - 1;

        Comparator<long[]> byValueThenIndex = new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                if (a[0] != b[0]) {
                    return Long.compare(a[0], b[0]);
                }
                return Long.compare(a[1], b[1]);
            }
        };
        TreeSet<long[]> small = new TreeSet<>(byValueThenIndex);
        TreeSet<long[]> large = new TreeSet<>(byValueThenIndex);
        long sumSmall = 0L;

        long res = Long.MAX_VALUE;
        for (int r = 1; r < n; r++) {
            // ---- insert index r ----
            large.add(new long[]{nums[r], r});
            sumSmall = rebalance(small, large, sumSmall, need);

            // ---- drop the index that just fell out of [r - dist, r] ----
            int l = Math.max(1, r - dist);
            int gone = l - 1;
            if (gone >= 1) {
                long[] e = new long[]{nums[gone], gone};
                if (small.remove(e)) {
                    sumSmall -= nums[gone];
                } else {
                    large.remove(e);
                }
                sumSmall = rebalance(small, large, sumSmall, need);
            }

            if (small.size() == need) {
                res = Math.min(res, sumSmall);
            }
        }
        return nums[0] + res;
    }

    /** keeps `small` at exactly `need` elements and every member <= every member of `large`. */
    private long rebalance(TreeSet<long[]> small, TreeSet<long[]> large, long sumSmall, int need) {
        while (small.size() < need && !large.isEmpty()) {
            long[] e = large.pollFirst();
            small.add(e);
            sumSmall += e[0];
        }
        while (small.size() > need) {
            long[] e = small.pollLast();
            sumSmall -= e[0];
            large.add(e);
        }
        while (!small.isEmpty() && !large.isEmpty()
                && small.last()[0] > large.first()[0]) {
            long[] a = small.pollLast();
            long[] b = large.pollFirst();
            sumSmall += b[0] - a[0];
            large.add(a);
            small.add(b);
        }
        return sumSmall;
    }
}
