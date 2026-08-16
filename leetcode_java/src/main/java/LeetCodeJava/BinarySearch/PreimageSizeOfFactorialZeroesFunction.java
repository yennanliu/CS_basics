package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/preimage-size-of-factorial-zeroes-function/description/

import java.util.ArrayList;
import java.util.List;
/**
 * 793. Preimage Size of Factorial Zeroes Function
 * Hard
 *
 * Let f(x) be the number of zeroes at the end of x!.
 * Recall that x! = 1 * 2 * 3 * ... * x and by convention, 0! = 1.
 *
 * For example, f(3) = 0 because 3! = 6 has no zeroes at the end,
 * while f(11) = 2 because 11! = 39916800 has two zeroes at the end.
 *
 * Given an integer k, return the number of non-negative integers x
 * have the property that f(x) = k.
 *
 *
 * Example 1:
 *
 * Input: k = 0
 * Output: 5
 * Explanation: 0!, 1!, 2!, 3!, and 4! end with k = 0 zeroes.
 *
 * Example 2:
 *
 * Input: k = 5
 * Output: 0
 * Explanation: There is no x such that x! ends in k = 5 zeroes.
 *
 * Example 3:
 *
 * Input: k = 3
 * Output: 5
 *
 *
 * Constraints:
 *
 * 0 <= k <= 10^9
 *
 */
public class PreimageSizeOfFactorialZeroesFunction {

    // V0
    // IDEA: BINARY SEARCH on a monotonic function
    /**
     *   f(x) = x/5 + x/25 + x/125 + ...  (number of factor-5s in x!)
     *   f is NON-DECREASING, so
     *
     *       answer = (smallest x with f(x) >= k+1) - (smallest x with f(x) >= k)
     *
     *   i.e. the WIDTH of the `plateau` where f equals exactly k.
     *   The answer is always 0 or 5 (f jumps by >= 2 whenever x crosses 25, 50, ...).
     *
     *   Search bound: f(5*m) >= m, so 5*(k+1) is always a SAFE upper bound.
     *
     *   NOTE !!! k can reach 10^9, so 5 * (k + 1) OVERFLOWS int -> use `long`.
     *
     *   time  = O(log(k) * log(k))   // binary search x log5 steps inside f
     *   space = O(1)
     */
    public int preimageSizeFZF(int k) {
        return (int) (lowest((long) k + 1) - lowest(k));
    }

    /** trailing zeros of x! */
    private long f(long x) {
        long cnt = 0;
        while (x > 0) {
            x /= 5;
            cnt += x;
        }
        return cnt;
    }

    /** smallest x such that f(x) >= target */
    private long lowest(long target) {
        long lo = 0;
        long hi = 5 * target + 5;
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (f(mid) >= target) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }


    // V1
    // IDEA: THE ANSWER IS ONLY EVER 0 OR 5
    /**
     *  f jumps by AT LEAST 1 at every multiple of 5 and never revisits a value, so
     *  each attainable k is hit by exactly the five numbers 5m, 5m+1, ..., 5m+4.
     *
     *  -> one binary search for `is k attainable at all?` is enough; the size is
     *     then a constant 5 (or 0).
     *
     *  Half the work of V0, which runs the search twice.
     *
     *  time  = O(log(k) * log(k))
     *  space = O(1)
     */
    public int preimageSizeFZF_1(int k) {
        long lo = 0;
        long hi = 5L * k + 5;
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (zeros(mid) >= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return zeros(lo) == k ? 5 : 0;
    }

    private long zeros(long x) {
        long cnt = 0;
        while (x > 0) {
            x /= 5;
            cnt += x;
        }
        return cnt;
    }

    // V2
    // IDEA: GREEDY DECOMPOSITION over the series (5^i - 1) / 4
    /**
     *  Writing x in base 5 as sum(d_i * 5^i) gives
     *      f(x) = sum(d_i * (5^i - 1) / 4)
     *  so k is attainable exactly when it decomposes into that series with every
     *  digit d_i in [0, 4].
     *
     *  Peeling the largest term off greedily answers the question with NO search
     *  and no f() evaluation at all -- pure number theory.
     *
     *  time  = O(log k)
     *  space = O(log k)
     */
    public int preimageSizeFZF_2(int k) {
        // the series 1, 6, 31, 156, ... = (5^i - 1) / 4
        List<Long> series = new ArrayList<>();
        long term = 1;
        while (term <= k) {
            series.add(term);
            term = term * 5 + 1;
        }

        long remain = k;
        for (int i = series.size() - 1; i >= 0; i--) {
            long t = series.get(i);
            long digit = remain / t;
            if (digit > 4) {
                return 0; // no base-5 digit can exceed 4
            }
            remain -= digit * t;
        }

        return remain == 0 ? 5 : 0;
    }

    // V3
    // IDEA: ONE BINARY SEARCH + A LINEAR WALK over the plateau
    /**
     *  Find the first x with f(x) >= k, then simply COUNT FORWARD while f still
     *  equals k.
     *
     *  The plateau is at most 5 wide, so the walk is O(1) -- and unlike V1 it does
     *  not rely on knowing the answer is 0 or 5 in advance, which makes it the
     *  version to reach for if the base (5) ever changes.
     *
     *  time  = O(log(k) * log(k))
     *  space = O(1)
     */
    public int preimageSizeFZF_3(int k) {
        long lo = 0;
        long hi = 5L * k + 5;
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (zeros(mid) >= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        int size = 0;
        while (zeros(lo + size) == k) {
            size += 1;
        }
        return size;
    }

}
