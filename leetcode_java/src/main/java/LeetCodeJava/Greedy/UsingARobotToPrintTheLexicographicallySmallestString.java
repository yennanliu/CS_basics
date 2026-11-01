package LeetCodeJava.Greedy;

// https://leetcode.com/problems/using-a-robot-to-print-the-lexicographically-smallest-string/description/

import java.util.*;

/**
 * 2434. Using a Robot to Print the Lexicographically Smallest String
 * Medium
 *
 * You are given a string s and a robot that currently holds an empty string t. Apply one of the following operations until s and t are both empty:
 *
 * Remove the first character of a string s and give it to the robot. The robot will append this character to string t.
 * Remove the last character of a string t and give it to the robot. The robot will write this character on paper.
 *
 * Return the lexicographically smallest string that can be written on the paper.
 *
 *
 * Example 1:
 *
 * Input: s = "zza"
 * Output: "azz"
 * Explanation: Let p denote the written string.
 * Initially p="", s="zza", t="".
 * Perform first operation three times p="", s="", t="zza".
 * Perform second operation three times p="azz", s="", t="".
 *
 * Example 2:
 *
 * Input: s = "bac"
 * Output: "abc"
 * Explanation: Let p denote the written string.
 * Perform first operation twice p="", s="c", t="ba".
 * Perform second operation twice p="ab", s="c", t="".
 * Perform first operation p="ab", s="", t="c".
 * Perform second operation p="abc", s="", t="".
 *
 * Example 3:
 *
 * Input: s = "bdda"
 * Output: "addb"
 * Explanation: Let p denote the written string.
 * Initially p="", s="bdda", t="".
 * Perform first operation four times p="", s="", t="bdda".
 * Perform second operation four times p="addb", s="", t="".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of only English lowercase letters.
 *
 */
public class UsingARobotToPrintTheLexicographicallySmallestString {

    // V0
    // IDEA: STACK + SUFFIX MINIMUM
    //       t behaves as a stack. After pushing s[i], write out the top while nothing
    //       still left in s is strictly smaller than it (top <= suffixMin[i + 1]).
    /**
     * time = O(N)
     * space = O(N)
     */
    public String robotWithString(String s) {
        int n = s.length();

        // suffixMin[i] = smallest character in s[i..n-1]; a sentinel above 'z' at n
        char[] suffixMin = new char[n + 1];
        suffixMin[n] = (char) ('z' + 1);
        for (int i = n - 1; i >= 0; i--) {
            suffixMin[i] = (char) Math.min(s.charAt(i), suffixMin[i + 1]);
        }

        StringBuilder written = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            stack.push(s.charAt(i));
            // waiting only helps if a strictly smaller character is still to come
            while (!stack.isEmpty() && stack.peek() <= suffixMin[i + 1]) {
                written.append(stack.pop());
            }
        }
        // s is exhausted: flush whatever is left
        while (!stack.isEmpty()) {
            written.append(stack.pop());
        }
        return written.toString();
    }
}
