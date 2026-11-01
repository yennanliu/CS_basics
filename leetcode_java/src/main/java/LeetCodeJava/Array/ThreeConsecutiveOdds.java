package LeetCodeJava.Array;

// https://leetcode.com/problems/three-consecutive-odds/description/

/**
 * 1550. Three Consecutive Odds
 * Easy
 *
 * Given an integer array arr, return true if there are three consecutive odd numbers in
 * the array. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: arr = [2,6,4,1]
 * Output: false
 * Explanation: There are no three consecutive odds.
 *
 * Example 2:
 *
 * Input: arr = [1,2,34,3,4,5,7,23,12]
 * Output: true
 * Explanation: [5,7,23] are three consecutive odds.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 1000
 * 1 <= arr[i] <= 1000
 *
 */
public class ThreeConsecutiveOdds {

    // V0
    // IDEA: RUNNING STREAK COUNTER — count odd numbers seen back-to-back, reset on an even
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean threeConsecutiveOdds(int[] arr) {
        int oddStreak = 0;
        for (int value : arr) {
            if (value % 2 == 1) {
                oddStreak++;
                if (oddStreak == 3) {
                    return true;
                }
            } else {
                oddStreak = 0;
            }
        }
        return false;
    }
}
