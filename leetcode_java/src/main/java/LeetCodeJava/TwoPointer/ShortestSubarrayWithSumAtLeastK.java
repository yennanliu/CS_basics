package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/description/

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 862. Shortest Subarray with Sum at Least K
 * Hard
 *
 * Given an integer array nums and an integer k, return the length of the shortest
 * non-empty subarray of nums with a sum of at least k. If there is no such subarray,
 * return -1.
 *
 * A subarray is a contiguous part of an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1], k = 1
 * Output: 1
 *
 * Example 2:
 *
 * Input: nums = [1,2], k = 4
 * Output: -1
 *
 * Example 3:
 *
 * Input: nums = [2,-1,2], k = 3
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^5 <= nums[i] <= 10^5
 * 1 <= k <= 10^9
 *
 */
public class ShortestSubarrayWithSumAtLeastK {

    // V0
    // IDEA: PREFIX SUM + MONOTONIC DEQUE
    /**
     *   With NEGATIVE numbers the classic sliding window BREAKS (shrinking the
     *   window can INCREASE the sum), so we work on PREFIX SUMS instead:
     *
     *       sum(nums[j..i-1]) = prefix[i] - prefix[j]
     *
     *   We want the smallest (i - j) with prefix[i] - prefix[j] >= k.
     *
     *   Keep a deque of candidate indices j with STRICTLY INCREASING prefix values:
     *
     *     - pop from the BACK while prefix[back] >= prefix[i]:
     *       a LATER index with a smaller-or-equal prefix is always at least as good
     *       (shorter subarray AND bigger sum), so the older one is USELESS.
     *
     *     - pop from the FRONT while prefix[i] - prefix[front] >= k:
     *       record the length; that front can NEVER give a shorter answer for a
     *       later i, so it is safe to discard.
     *
     *   Every index enters and leaves the deque at most once -> LINEAR time.
     *
     *   NOTE !!! prefix sums reach 10^5 * 10^5 = 10^10, which OVERFLOWS int
     *            -> the prefix array must be `long`.
     *
     *   time  = O(n)
     *   space = O(n)
     */
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length;

        // prefix[i] = sum of the first i elements
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        int ans = n + 1;
        Deque<Integer> q = new ArrayDeque<>(); // indices into prefix, values increasing

        for (int i = 0; i <= n; i++) {
            long cur = prefix[i];

            // keep the deque INCREASING
            while (!q.isEmpty() && prefix[q.peekLast()] >= cur) {
                q.pollLast();
            }

            // the front already satisfies the target -> shortest for this i
            while (!q.isEmpty() && cur - prefix[q.peekFirst()] >= k) {
                ans = Math.min(ans, i - q.pollFirst());
            }

            q.offerLast(i);
        }

        return ans <= n ? ans : -1;
    }


    // V1
    // IDEA: BRUTE FORCE over every subarray
    /**
     *  Prefix sums, then try every (i, j) pair and keep the shortest whose sum
     *  reaches k.
     *
     *  O(n^2), hopeless at n = 10^5, but the deque invariant in V0 is subtle enough
     *  that having an unarguable reference is worth the file space.
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public int shortestSubarray_1(int[] nums, int k) {
        int n = nums.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        int ans = n + 1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (prefix[j] - prefix[i] >= k) {
                    ans = Math.min(ans, j - i);
                    break; // extending j only makes this start longer
                }
            }
        }
        return ans <= n ? ans : -1;
    }

    // V2
    // IDEA: BINARY SEARCH ON THE ANSWER LENGTH + a fixed-width window check
    /**
     *  Ask `is there a window of width >= L whose sum reaches k?`. That predicate
     *  is MONOTONE in L (a valid width L stays valid for every smaller L, because
     *  the same window is still allowed), so L can be binary searched.
     *
     *  Each check is one O(n) pass keeping the minimum prefix that is L behind.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public int shortestSubarray_2(int[] nums, int k) {
        int n = nums.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        int lo = 1;
        int hi = n;
        int ans = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (existsWindow(prefix, mid, k)) {
                ans = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return ans;
    }

    /** is there a window of width exactly `len` whose sum reaches k? */
    private boolean existsWindow(long[] prefix, int len, int k) {
        int n = prefix.length - 1;
        for (int j = len; j <= n; j++) {
            if (prefix[j] - prefix[j - len] >= k) {
                return true;
            }
        }
        return false;
    }

    // V3
    // IDEA: TreeMap OF PREFIX SUMS (ordered map instead of a monotonic deque)
    /**
     *  For each j we want the LARGEST index i < j with prefix[i] <= prefix[j] - k.
     *  A TreeMap<prefixValue, index> answers that with floorEntry.
     *
     *  NOTE !!! smaller prefix values can sit at LATER indices, so the floor entry
     *           alone is not enough -- we walk the head of the map downward and
     *           keep the best index, dropping entries that can never win again.
     *
     *  Slower than the deque by a log factor, but it is a plain ordered lookup
     *  rather than a monotonic-pop argument.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public int shortestSubarray_3(int[] nums, int k) {
        int n = nums.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        // prefix value -> the LARGEST index achieving it (a smaller index is
        // always worse for a fixed j, so only the newest matters)
        TreeMap<Long, Integer> seen = new TreeMap<>();
        int ans = n + 1;

        for (int j = 0; j <= n; j++) {
            // every prefix <= prefix[j] - k is a candidate start
            Map.Entry<Long, Integer> e = seen.floorEntry(prefix[j] - k);
            while (e != null) {
                ans = Math.min(ans, j - e.getValue());
                // that entry can never beat this j again -> retire it
                seen.remove(e.getKey());
                e = seen.floorEntry(prefix[j] - k);
            }
            seen.merge(prefix[j], j, Math::max);
        }

        return ans <= n ? ans : -1;
    }

}
