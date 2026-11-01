package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-score-from-removing-substrings/description/

/**
 * 1717. Maximum Score From Removing Substrings
 * Medium
 *
 * You are given a string s and two integers x and y. You can perform two types of
 * operations any number of times.
 *
 * - Remove substring "ab" and gain x points.
 *   For example, when removing "ab" from "cabxbae" it becomes "cxbae".
 * - Remove substring "ba" and gain y points.
 *   For example, when removing "ba" from "cabxbae" it becomes "cabxe".
 *
 * Return the maximum points you can gain after applying the above operations on s.
 *
 *
 * Example 1:
 *
 * Input: s = "cdbcbbaaabab", x = 4, y = 5
 * Output: 19
 *
 * Example 2:
 *
 * Input: s = "aabbaaxybbaabb", x = 5, y = 4
 * Output: 20
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * 1 <= x, y <= 10^4
 * s consists of lowercase English letters.
 *
 */
public class MaximumScoreFromRemovingSubstrings {

    // V0
    // IDEA: GREEDY + STACK COUNTERS (REMOVE THE HIGHER-PAYING PAIR FIRST)
    //       both operations delete one 'a' and one 'b', so inside a block of only
    //       a/b chars the number of removals is fixed; only the mix is ours. Take as
    //       many of the expensive pair as possible, then the cheap pair from what is
    //       left. A stack of only two letters collapses to two counters.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maximumGain(String s, int x, int y) {
        // make (first, second) the pair worth `highScore`
        char first = 'a';
        char second = 'b';
        int highScore = x;
        int lowScore = y;
        if (x < y) {
            first = 'b';
            second = 'a';
            highScore = y;
            lowScore = x;
        }

        int total = 0;
        int firstCount = 0;  // unmatched `first` chars (stack contents)
        int secondCount = 0; // `second` chars with no `first` before them
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == first) {
                firstCount++;
            } else if (ch == second) {
                if (firstCount > 0) {
                    // close the expensive pair right away
                    firstCount--;
                    total += highScore;
                } else {
                    secondCount++;
                }
            } else {
                // block ends: leftovers are second...second first...first -> cheap pairs
                total += Math.min(firstCount, secondCount) * lowScore;
                firstCount = 0;
                secondCount = 0;
            }
        }
        // flush the trailing block
        total += Math.min(firstCount, secondCount) * lowScore;
        return total;
    }
}
