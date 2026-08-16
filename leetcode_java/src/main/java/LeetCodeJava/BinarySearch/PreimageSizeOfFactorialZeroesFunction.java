package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/preimage-size-of-factorial-zeroes-function/description/
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

}
