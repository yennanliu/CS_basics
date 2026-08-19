package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-x-sum-of-all-k-long-subarrays-ii/

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 *  3321. Find X-Sum of All K-Long Subarrays II
 *  Hard
 *
 *  You are given an array nums of n integers and two integers k and x.
 *
 *  The x-sum of an array is calculated by the following procedure:
 *    - Count the occurrences of all elements in the array.
 *    - Keep only the occurrences of the top x most frequent elements. If two elements
 *      have the same number of occurrences, the element with the bigger value is
 *      considered more frequent.
 *    - Calculate the sum of the resulting array.
 *  If an array has fewer than x distinct elements, its x-sum is the sum of the array.
 *
 *  Return an integer array answer of length n - k + 1 where answer[i] is the x-sum of
 *  the subarray nums[i..i + k - 1].
 *
 *  Example 1:
 *    Input: nums = [1,1,2,2,3,4,2,3], k = 6, x = 2
 *    Output: [6,10,12]
 *    Explanation: for [1,1,2,2,3,4] only 1 and 2 are kept -> 1+1+2+2 = 6, etc.
 *
 *  Example 2:
 *    Input: nums = [3,8,7,8,7,5], k = 2, x = 2
 *    Output: [11,15,15,15,12]
 *    Explanation: k == x, so every answer is just the window sum.
 *
 *  Constraints:
 *    nums.length == n
 *    1 <= n <= 10^5
 *    1 <= nums[i] <= 10^9
 *    1 <= x <= k <= nums.length
 */
public class FindXSumOfAllKLongSubarraysII {

    // V0
    // IDEA: TWO ORDERED SETS STRADDLING THE "TOP x" CUT
    //
    //   sliding the window changes exactly two counts, so the x-sum is MAINTAINED
    //   rather than recomputed. The ranking key is (count, value) - more
    //   occurrences first, bigger value breaking ties - so keep
    //       top  : the x best (count, value) pairs, with their weighted sum
    //       rest : everybody else
    //   as two TreeSets on that key. `top.first()` is the weakest member (the one
    //   that can be evicted) and `rest.last()` is the strongest challenger.
    //
    //   A TreeSet keyed by (count, value) allows the EXACT old entry of a value to
    //   be deleted in O(log n) when its count changes - which is why no lazy
    //   deletion / staleness bookkeeping is needed here. After each count change the
    //   sets are rebalanced so `top` holds exactly min(x, distinct) values and no
    //   member of `rest` outranks a member of `top`.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public long[] findXSum(int[] nums, int k, int x) {
        int n = nums.length;
        Map<Integer, Integer> cnt = new HashMap<>();

        Comparator<long[]> byCountThenValue = new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                if (a[0] != b[0]) {
                    return Long.compare(a[0], b[0]);
                }
                return Long.compare(a[1], b[1]);
            }
        };
        TreeSet<long[]> top = new TreeSet<>(byCountThenValue);   // the x best
        TreeSet<long[]> rest = new TreeSet<>(byCountThenValue);  // everybody else

        long[] res = new long[n - k + 1];
        long[] sumTop = new long[]{0L};

        for (int i = 0; i < n; i++) {
            change(nums[i], 1, cnt, top, rest, sumTop, x);
            if (i >= k) {
                change(nums[i - k], -1, cnt, top, rest, sumTop, x);
            }
            if (i >= k - 1) {
                res[i - k + 1] = sumTop[0];
            }
        }
        return res;
    }

    /** applies cnt[v] += delta, keeping `top` / `rest` / sumTop consistent. */
    private void change(int v, int delta, Map<Integer, Integer> cnt,
                        TreeSet<long[]> top, TreeSet<long[]> rest,
                        long[] sumTop, int x) {
        int old = cnt.containsKey(v) ? cnt.get(v) : 0;
        if (old > 0) {
            long[] e = new long[]{old, v};
            if (top.remove(e)) {
                sumTop[0] -= (long) v * old;
            } else {
                rest.remove(e);
            }
        }
        int now = old + delta;
        if (now > 0) {
            cnt.put(v, now);
            rest.add(new long[]{now, v});
        } else {
            cnt.remove(v);
        }
        rebalance(top, rest, sumTop, x);
    }

    private void rebalance(TreeSet<long[]> top, TreeSet<long[]> rest,
                           long[] sumTop, int x) {
        while (true) {
            if (top.size() < x && !rest.isEmpty()) {
                long[] e = rest.pollLast();               // strongest challenger
                top.add(e);
                sumTop[0] += e[0] * e[1];
                continue;
            }
            if (top.size() > x) {
                long[] e = top.pollFirst();               // weakest member
                sumTop[0] -= e[0] * e[1];
                rest.add(e);
                continue;
            }
            if (!top.isEmpty() && !rest.isEmpty()
                    && compareKey(rest.last(), top.first()) > 0) {
                long[] out = top.pollFirst();
                long[] in = rest.pollLast();
                sumTop[0] += in[0] * in[1] - out[0] * out[1];
                rest.add(out);
                top.add(in);
                continue;
            }
            break;
        }
    }

    private int compareKey(long[] a, long[] b) {
        if (a[0] != b[0]) {
            return Long.compare(a[0], b[0]);
        }
        return Long.compare(a[1], b[1]);
    }
}
