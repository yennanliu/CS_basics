package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-average-subarray-ii/description/
/**
 * 644. Maximum Average Subarray II
 * Hard
 * Lock: Prime
 *
 * You are given an integer array nums consisting of n elements, and an integer k.
 *
 * Find a contiguous subarray whose length is greater than or equal to k that has the
 * maximum average value and return this value.
 * Any answer with a calculation error less than 10^-5 will be accepted.
 *
 * Example 1:
 *
 * Input: nums = [1,12,-5,-6,50,3], k = 4
 * Output: 12.75000
 * Explanation:
 * - When the length is 4, averages are [0.5, 12.75, 10.5] and the maximum average is 12.75
 * - When the length is 5, averages are [10.4, 10.8] and the maximum average is 10.8
 * - When the length is 6, averages are [9.16667] and the maximum average is 9.16667
 * The maximum average is when we choose a subarray of length 4 (i.e., the sub array
 * [12, -5, -6, 50]) which has the max average 12.75, so we return 12.75
 * Note that we do not consider the subarrays of length < 4.
 *
 * Example 2:
 *
 * Input: nums = [5], k = 1
 * Output: 5.00000
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= k <= n <= 10^4
 * -10^4 <= nums[i] <= 10^4
 *
 */
public class MaximumAverageSubarray2 {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER (a real value) + PREFIX SUM
    /**
     *   Guess an average `v` and ask: is there a subarray of length >= k whose
     *   average is >= v ?
     *
     *     (a_1 + ... + a_j) / j >= v
     *     <=> (a_1 - v) + ... + (a_j - v) >= 0
     *
     *   So subtract v from every element and look for a subarray of length >= k
     *   with a NON-NEGATIVE sum. With prefix sums S of the shifted array, that is:
     *
     *     exists i >= k  with  S[i] - min(S[0..i-k]) >= 0
     *
     *   which ONE LINEAR PASS computes, keeping the running minimum of the prefix
     *   sums that are at least k positions behind.
     *
     *   The predicate is MONOTONE in v (true for small v, false for large v), so we
     *   binary search on the real interval [min(nums), max(nums)].
     *
     *   time  = O(n * log((max - min) / eps))
     *   space = O(1)
     */
    public double findMaxAverage(int[] nums, int k) {
        double lo = nums[0];
        double hi = nums[0];
        for (int x : nums) {
            lo = Math.min(lo, x);
            hi = Math.max(hi, x);
        }

        final double EPS = 1e-6;

        /** NOTE !!!
         *
         *  binary search on a REAL value (the answer),
         *  NOT on an index -> the loop condition is `hi - lo > EPS`
         */
        while (hi - lo > EPS) {
            double mid = (lo + hi) / 2.0;
            if (canReach(nums, k, mid)) {
                lo = mid;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    /** is there a subarray of length >= k with average >= v ? */
    private boolean canReach(int[] nums, int k, double v) {
        int n = nums.length;

        // sum of the first k shifted elements
        double window = 0.0;
        for (int i = 0; i < k; i++) {
            window += nums[i] - v;
        }
        if (window >= 0) {
            return true;
        }

        /** NOTE !!!
         *
         *  `lag` is the prefix sum lagging k behind the window,
         *  `minLag` its running minimum
         *
         *  -> subtracting minLag from window lets the window GROW beyond k
         */
        double lag = 0.0;
        double minLag = 0.0;
        for (int i = k; i < n; i++) {
            window += nums[i] - v;
            lag += nums[i - k] - v;
            minLag = Math.min(minLag, lag);
            if (window - minLag >= 0) {
                return true;
            }
        }

        return false;
    }


    // V1
    // IDEA: BINARY SEARCH ON THE ANSWER + PLAIN PREFIX-MIN (no rolling window)
    /**
     *  Same predicate as V0, expressed with an explicit prefix array instead of a
     *  rolling window pair. `minPrefix` tracks min(prefix[0..i-k]) as i advances.
     *
     *  Longer in memory (O(n)) but the invariant is much easier to see:
     *      exists i >= k with prefix[i] - min(prefix[0..i-k]) >= 0
     *
     *  time  = O(n * log((max - min) / eps))
     *  space = O(n)
     */
    public double findMaxAverage_1(int[] nums, int k) {
        int n = nums.length;
        double lo = nums[0];
        double hi = nums[0];
        for (int x : nums) {
            lo = Math.min(lo, x);
            hi = Math.max(hi, x);
        }

        final double EPS = 1e-6;
        double[] prefix = new double[n + 1];

        while (hi - lo > EPS) {
            double mid = (lo + hi) / 2.0;

            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + nums[i] - mid;
            }

            boolean ok = false;
            double minPrefix = 0.0;
            for (int i = k; i <= n; i++) {
                minPrefix = Math.min(minPrefix, prefix[i - k]);
                if (prefix[i] - minPrefix >= 0) {
                    ok = true;
                    break;
                }
            }

            if (ok) {
                lo = mid;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    // V2
    // IDEA: FIXED ITERATION COUNT instead of an epsilon loop
    /**
     *  `while (hi - lo > EPS)` couples the loop count to the VALUE RANGE, which is
     *  awkward to reason about. Running a fixed ~100 halvings instead shrinks the
     *  interval by 2^-100, far below the 10^-5 the problem allows, and makes the
     *  runtime completely input independent.
     *
     *  A standard trick for float binary search in contests.
     *
     *  time  = O(100 * n)
     *  space = O(1)
     */
    public double findMaxAverage_2(int[] nums, int k) {
        double lo = nums[0];
        double hi = nums[0];
        for (int x : nums) {
            lo = Math.min(lo, x);
            hi = Math.max(hi, x);
        }

        for (int iter = 0; iter < 100; iter++) {
            double mid = (lo + hi) / 2.0;
            if (canReachAvg(nums, k, mid)) {
                lo = mid;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    private boolean canReachAvg(int[] nums, int k, double v) {
        int n = nums.length;
        double window = 0.0;
        for (int i = 0; i < k; i++) {
            window += nums[i] - v;
        }
        if (window >= 0) {
            return true;
        }
        double lag = 0.0;
        double minLag = 0.0;
        for (int i = k; i < n; i++) {
            window += nums[i] - v;
            lag += nums[i - k] - v;
            minLag = Math.min(minLag, lag);
            if (window - minLag >= 0) {
                return true;
            }
        }
        return false;
    }

    // V3
    // IDEA: BRUTE FORCE over every subarray of length >= k
    /**
     *  Prefix sums, then try every (i, j) with j - i >= k and keep the best mean.
     *
     *  O(n^2) so it TLEs at n = 10^4, but it involves no binary search and no
     *  epsilon at all -- the exact answer, useful as the oracle for the three
     *  approximate versions above.
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public double findMaxAverage_3(int[] nums, int k) {
        int n = nums.length;
        double[] prefix = new double[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        double best = -Double.MAX_VALUE;
        for (int i = 0; i + k <= n; i++) {
            for (int j = i + k; j <= n; j++) {
                best = Math.max(best, (prefix[j] - prefix[i]) / (j - i));
            }
        }
        return best;
    }

}
