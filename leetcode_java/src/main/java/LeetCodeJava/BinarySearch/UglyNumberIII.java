package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/ugly-number-iii/description/

/**
 * 1201. Ugly Number III
 * Medium
 *
 * An ugly number is a positive integer that is divisible by a, b, or c.
 *
 * Given four integers n, a, b, and c, return the nth ugly number.
 *
 *
 * Example 1:
 *
 * Input: n = 3, a = 2, b = 3, c = 5
 * Output: 4
 * Explanation: The ugly numbers are 2, 3, 4, 5, 6, 8, 9, 10... The 3rd is 4.
 *
 * Example 2:
 *
 * Input: n = 4, a = 2, b = 3, c = 4
 * Output: 6
 * Explanation: The ugly numbers are 2, 3, 4, 6, 8, 9, 10, 12... The 4th is 6.
 *
 * Example 3:
 *
 * Input: n = 5, a = 2, b = 11, c = 13
 * Output: 10
 * Explanation: The ugly numbers are 2, 4, 6, 8, 10, 11, 12, 13... The 5th is 10.
 *
 *
 * Constraints:
 *
 * 1 <= n, a, b, c <= 10^9
 * 1 <= a * b * c <= 10^18
 * It is guaranteed that the result will be in range [1, 2 * 10^9].
 *
 */
public class UglyNumberIII {

    private static final long MAX_ANSWER = 2_000_000_000L;

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER + INCLUSION-EXCLUSION
    /**
     *  count(x) = how many ugly numbers are <= x
     *           = x/a + x/b + x/c - x/lcm(a,b) - x/lcm(a,c) - x/lcm(b,c) + x/lcm(a,b,c)
     *
     *  count(x) is non-decreasing, so binary search the smallest x with count(x) >= n.
     *  That x is itself ugly: if it were not, count(x - 1) == count(x) and x - 1 would
     *  already satisfy the predicate.
     */
    /**
     * time = O(log(2 * 10^9))
     * space = O(1)
     */
    public int nthUglyNumber(int n, int a, int b, int c) {
        // pairwise and triple lcm, in long since a * b can reach 10^18
        long ab = lcm(a, b);
        long ac = lcm(a, c);
        long bc = lcm(b, c);
        long abc = lcm(ab, c);

        long left = 1;
        long right = MAX_ANSWER;
        while (left < right) {
            long mid = left + (right - left) / 2;
            long count = mid / a + mid / b + mid / c
                    - mid / ab - mid / ac - mid / bc
                    + mid / abc;
            if (count >= n) {
                // mid is large enough; the first such value is at or before it
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return (int) left;
    }

    /** Least common multiple; divides before multiplying to delay overflow. */
    private long lcm(long x, long y) {
        return x / gcd(x, y) * y;
    }

    /** Greatest common divisor by Euclid's algorithm. */
    private long gcd(long x, long y) {
        while (y != 0) {
            long remainder = x % y;
            x = y;
            y = remainder;
        }
        return x;
    }
}
