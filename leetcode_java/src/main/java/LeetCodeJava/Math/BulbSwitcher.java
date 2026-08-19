package LeetCodeJava.Math;

// https://leetcode.com/problems/bulb-switcher/

/**
 *  319. Bulb Switcher
 *  Medium
 *
 *  There are n bulbs that are initially off. You first turn on all the bulbs, then you
 *  turn off every second bulb. On the third round, you toggle every third bulb
 *  (turning on if it's off or turning off if it's on). For the ith round, you toggle
 *  every i bulb. For the nth round, you only toggle the last bulb.
 *
 *  Return the number of bulbs that are on after n rounds.
 *
 *  Example 1:
 *
 *  Input: n = 3
 *  Output: 1
 *  Explanation: after round 3 the state is [on, off, off] -> 1 bulb is on.
 *
 *  Example 2:
 *
 *  Input: n = 0
 *  Output: 0
 *
 *  Constraints:
 *
 *  0 <= n <= 10^9
 */
public class BulbSwitcher {

    // V0
    // IDEA: bulb i is toggled once per divisor; it ends ON iff i has an odd number of
    //       divisors, i.e. i is a perfect square -> answer = floor(sqrt(n))
    /**
     * time = O(1)
     * space = O(1)
     */
    public int bulbSwitch(int n) {
        int r = (int) Math.sqrt((double) n);
        // guard against floating point rounding
        while ((long) (r + 1) * (r + 1) <= n) {
            r++;
        }
        while ((long) r * r > n) {
            r--;
        }
        return r;
    }
}
