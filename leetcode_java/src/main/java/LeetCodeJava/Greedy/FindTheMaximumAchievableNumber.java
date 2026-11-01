package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-maximum-achievable-number/description/

/**
 * 2769. Find the Maximum Achievable Number
 * Easy
 *
 * Given two integers, num and t. A number x is achievable if it can become equal to num after applying the following operation at most t times:
 *
 * Increase or decrease x by 1, and simultaneously increase or decrease num by 1.
 *
 * Return the maximum possible value of x.
 *
 *
 * Example 1:
 *
 * Input: num = 4, t = 1
 *
 * Output: 6
 *
 * Explanation:
 *
 * Apply the following operation once to make the maximum achievable number equal to num:
 *
 * Decrease the maximum achievable number by 1, and increase num by 1.
 *
 * Example 2:
 *
 * Input: num = 3, t = 2
 *
 * Output: 7
 *
 * Explanation:
 *
 * Apply the following operation twice to make the maximum achievable number equal to num:
 *
 * Decrease the maximum achievable number by 1, and increase num by 1.
 *
 *
 * Constraints:
 *
 * 1 <= num, t <= 50
 *
 */
public class FindTheMaximumAchievableNumber {

    // V0
    // IDEA: GREEDY / MATH (each move closes a gap of 2)
    //       one op moves x down by 1 and num up by 1, closing |x - num| by 2.
    //       with t ops the largest reachable gap is 2 * t, so x = num + 2 * t.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int theMaximumAchievableX(int num, int t) {
        return num + 2 * t;
    }
}
