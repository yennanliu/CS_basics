package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-moves-to-convert-string/description/

/**
 * 2027. Minimum Moves to Convert String
 * Easy
 *
 * You are given a string s consisting of n characters which are either 'X' or 'O'.
 *
 * A move is defined as selecting three consecutive characters of s and converting them to 'O'. Note that if
 * a move is applied to the character 'O', it will stay the same.
 *
 * Return the minimum number of moves required so that all the characters of s are converted to 'O'.
 *
 *
 * Example 1:
 *
 * Input: s = "XXX"
 * Output: 1
 *
 * Example 2:
 *
 * Input: s = "XXOX"
 * Output: 2
 *
 * Example 3:
 *
 * Input: s = "OOOO"
 * Output: 0
 *
 *
 * Constraints:
 *
 * 3 <= s.length <= 1000
 * s[i] is either 'X' or 'O'.
 *
 */
public class MinimumMovesToConvertString {

    // V0
    // IDEA: GREEDY LEFT-TO-RIGHT SCAN - START A MOVE AT THE LEFTMOST REMAINING 'X'
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumMoves(String s) {
        int moves = 0;
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == 'X') {
                // the leftmost 'X' must be covered; a move starting here covers the most ground
                moves++;
                // the move clears i, i + 1, i + 2 (a window running off the end still clears the tail)
                i += 3;
            } else {
                i++;
            }
        }
        return moves;
    }
}
