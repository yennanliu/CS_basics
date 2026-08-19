package LeetCodeJava.Math;

// https://leetcode.com/problems/bulb-switcher-ii/

/**
 *  672. Bulb Switcher II
 *  Medium
 *
 *  There is a room with n bulbs labeled from 1 to n that all are turned on
 *  initially, and four buttons on the wall. Each of the four buttons has a
 *  different functionality where:
 *    Button 1: Flips the status of all the bulbs.
 *    Button 2: Flips the status of all the bulbs with even labels (2, 4, ...).
 *    Button 3: Flips the status of all the bulbs with odd labels (1, 3, ...).
 *    Button 4: Flips the status of all the bulbs with a label j = 3k + 1
 *              where k = 0, 1, 2, ... (1, 4, 7, 10, ...).
 *
 *  You must make exactly presses button presses in total. For each press, you
 *  may pick any of the four buttons.
 *
 *  Given the two integers n and presses, return the number of different
 *  possible statuses after performing all presses button presses.
 *
 *  Example 1:
 *    Input: n = 1, presses = 1
 *    Output: 2
 *
 *  Example 2:
 *    Input: n = 2, presses = 1
 *    Output: 3
 *
 *  Example 3:
 *    Input: n = 3, presses = 1
 *    Output: 4
 *
 *  Constraints:
 *   - 1 <= n <= 1000
 *   - 0 <= presses <= 1000
 */
public class BulbSwitcherII {

    // V0
    // IDEA: MATH / CASE ANALYSIS.
    //       Only the first 3 bulbs (and only the parity of each button count)
    //       matter, so n can be capped at 3 and presses at 3. That leaves a
    //       tiny fixed table of answers.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int flipLights(int n, int presses) {

        if (presses == 0) {
            return 1;
        }
        if (n == 1) {
            // bulb 1 is either on or off
            return 2;
        }
        if (n == 2) {
            return presses == 1 ? 3 : 4;
        }
        // n >= 3
        if (presses == 1) {
            return 4;
        }
        if (presses == 2) {
            return 7;
        }
        return 8;
    }
}
