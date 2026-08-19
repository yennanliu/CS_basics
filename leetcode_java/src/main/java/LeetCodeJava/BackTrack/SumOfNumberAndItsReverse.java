package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/sum-of-number-and-its-reverse/

/**
 *  2443. Sum of Number and Its Reverse
 *  Medium
 *
 *  Given a non-negative integer num, return true if num can be expressed as the sum
 *  of any non-negative integer and its reverse, or false otherwise.
 *
 *  Example 1:
 *    Input: num = 443
 *    Output: true
 *    Explanation: 172 + 271 = 443 so we return true.
 *
 *  Example 2:
 *    Input: num = 63
 *    Output: false
 *
 *  Example 3:
 *    Input: num = 181
 *    Output: true
 *    Explanation: 140 + 041 = 181. Note that when a number is reversed, there may
 *                 be leading zeros.
 *
 *  Constraints:
 *    0 <= num <= 10^5
 */
public class SumOfNumberAndItsReverse {

    // V0
    // IDEA: ENUMERATION (the candidate k is bounded, so just scan it)
    //       if num = k + reverse(k) then k <= num, and by symmetry one of
    //       k / reverse(k) is >= num / 2, so scanning k in [num / 2, num] is enough.
    //       NOTE : num <= 10^5, so this is ~5 * 10^4 cheap digit reversals.
    //              reverse() drops leading zeros: 140 + reverse(140) = 140 + 41 = 181.
    /**
     * time = O(num * d), d = number of digits
     * space = O(1)
     */
    public boolean sumOfNumberAndReverse(int num) {
        for (int k = num / 2; k <= num; k++) {
            if (k + reverse(k) == num) {
                return true;
            }
        }
        return false;
    }

    private int reverse(int x) {
        int r = 0;
        while (x > 0) {
            r = r * 10 + x % 10;
            x /= 10;
        }
        return r;
    }
}
