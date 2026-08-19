package LeetCodeJava.String;

// https://leetcode.com/problems/rotate-string/

/**
 *  796. Rotate String
 *  Easy
 *
 *  Given two strings s and goal, return true if and only if s can become goal
 *  after some number of shifts on s.
 *  A shift on s consists of moving the leftmost character of s to the rightmost
 *  position. For example, if s = "abcde", then it will be "bcdea" after one shift.
 *
 *  Example 1:
 *    Input:  s = "abcde", goal = "cdeab"
 *    Output: true
 *
 *  Example 2:
 *    Input:  s = "abcde", goal = "abced"
 *    Output: false
 *
 *  Constraints:
 *    1 <= s.length, goal.length <= 100
 *    s and goal consist of lowercase English letters.
 */
public class RotateString {

    // V0
    // IDEA: every rotation of s is a substring of s + s (given equal lengths)
    /**
     * time = O(n^2)  (naive indexOf; O(n) with KMP)
     * space = O(n)
     */
    public boolean rotateString(String s, String goal) {
        if (s == null || goal == null || s.length() != goal.length()) {
            return false;
        }
        return (s + s).contains(goal);
    }

    // V1
    // IDEA: brute force — build each of the n rotations and compare
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public boolean rotateString_1(String s, String goal) {
        if (s == null || goal == null || s.length() != goal.length()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if ((s.substring(i) + s.substring(0, i)).equals(goal)) {
                return true;
            }
        }
        return s.isEmpty();
    }
}
