package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimize-the-maximum-of-two-arrays/description/

/**
 * 2513. Minimize the Maximum of Two Arrays
 * Medium
 *
 * We have two arrays arr1 and arr2 which are initially empty. You need to add positive integers to them such that they satisfy all the following conditions:
 *
 * arr1 contains uniqueCnt1 distinct positive integers, each of which is not divisible by divisor1.
 * arr2 contains uniqueCnt2 distinct positive integers, each of which is not divisible by divisor2.
 * No integer is present in both arr1 and arr2.
 *
 * Given divisor1, divisor2, uniqueCnt1, and uniqueCnt2, return the minimum possible maximum integer that can be present in either array.
 *
 *
 * Example 1:
 *
 * Input: divisor1 = 2, divisor2 = 7, uniqueCnt1 = 1, uniqueCnt2 = 3
 * Output: 4
 * Explanation:
 * We can distribute the first 4 natural numbers into arr1 and arr2.
 * arr1 = [1] and arr2 = [2,3,4].
 * We can see that both arrays satisfy all the conditions.
 * Since the maximum value is 4, we return it.
 *
 * Example 2:
 *
 * Input: divisor1 = 3, divisor2 = 5, uniqueCnt1 = 2, uniqueCnt2 = 1
 * Output: 3
 * Explanation:
 * Here arr1 = [1,2], and arr2 = [3] satisfy all conditions.
 * Since the maximum value is 3, we return it.
 *
 * Example 3:
 *
 * Input: divisor1 = 2, divisor2 = 4, uniqueCnt1 = 8, uniqueCnt2 = 2
 * Output: 15
 * Explanation:
 * Here, the final possible arrays can be arr1 = [1,3,5,7,9,11,13,15], and arr2 = [2,6].
 * It can be shown that it is not possible to obtain a lower maximum satisfying all conditions.
 *
 *
 * Constraints:
 *
 * 2 <= divisor1, divisor2 <= 10^5
 * 1 <= uniqueCnt1, uniqueCnt2 < 10^9
 * 2 <= uniqueCnt1 + uniqueCnt2 <= 10^9
 *
 */
public class MinimizeTheMaximumOfTwoArrays {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER + INCLUSION-EXCLUSION VIA LCM
    // feasibility is monotone: if both arrays fit inside [1..x], they fit inside [1..x+1].
    // x works iff:
    //   x - x/divisor1 >= uniqueCnt1         (numbers arr1 may use)
    //   x - x/divisor2 >= uniqueCnt2         (numbers arr2 may use)
    //   x - x/lcm      >= uniqueCnt1 + uniqueCnt2
    // the third test matters: numbers divisible by BOTH divisors are usable by neither array.
    /**
     * time = O(log(MAX) + log(min(d1, d2)))
     * space = O(1)
     */
    public int minimizeSet(int divisor1, int divisor2, int uniqueCnt1, int uniqueCnt2) {
        // lcm can reach 10^10, so keep it in a long
        long lcm = (long) divisor1 / gcd(divisor1, divisor2) * divisor2;

        long left = 1;
        long right = 10_000_000_000L;
        while (left < right) {
            long mid = left + (right - left) / 2;
            if (canFit(mid, divisor1, divisor2, lcm, uniqueCnt1, uniqueCnt2)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return (int) left;
    }

    /** Whether both arrays can be filled using only the numbers 1..x. */
    private boolean canFit(long x, long divisor1, long divisor2, long lcm, long uniqueCnt1, long uniqueCnt2) {
        if (x - x / divisor1 < uniqueCnt1) {
            return false;
        }
        if (x - x / divisor2 < uniqueCnt2) {
            return false;
        }
        // the shared pool (not divisible by both) must cover the combined demand
        return x - x / lcm >= uniqueCnt1 + uniqueCnt2;
    }

    /** Euclid's gcd. */
    private long gcd(long a, long b) {
        while (b != 0) {
            long remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
