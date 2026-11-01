package LeetCodeJava.Greedy;

// https://leetcode.com/problems/lexicographically-smallest-string-after-operations-with-constraint/description/

/**
 * 3106. Lexicographically Smallest String After Operations With Constraint
 * Medium
 *
 * You are given a string s and an integer k.
 *
 * Define a function distance(s1, s2) between two strings s1 and s2 of the same length n as:
 *
 * The sum of the minimum distance between s1[i] and s2[i] when the characters from 'a' to 'z' are placed in a cyclic order, for all i in the range [0, n - 1].
 *
 * For example, distance("ab", "cd") == 4, and distance("a", "z") == 1.
 *
 * You can change any letter of s to any other lowercase English letter, any number of times.
 *
 * Return a string denoting the lexicographically smallest string t you can get after some changes, such that distance(s, t) <= k.
 *
 *
 * Example 1:
 *
 * Input: s = "zbbz", k = 3
 * Output: "aaaz"
 * Explanation:
 * Change s to "aaaz". The distance between "zbbz" and "aaaz" is equal to k = 3.
 *
 * Example 2:
 *
 * Input: s = "xaxcd", k = 4
 * Output: "aawcd"
 * Explanation:
 * The distance between "xaxcd" and "aawcd" is equal to k = 4.
 *
 * Example 3:
 *
 * Input: s = "lol", k = 0
 * Output: "lol"
 * Explanation:
 * It's impossible to change any character as k = 0.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * 0 <= k <= 2000
 * s consists only of lowercase English letters.
 *
 */
public class LexicographicallySmallestStringAfterOperationsWithConstraint {

    // V0
    // IDEA: GREEDY LEFT TO RIGHT - SPEND THE BUDGET ON THE EARLIEST POSITIONS
    /**
     *  Earlier characters dominate lexicographic order, so for each position in
     *  turn pick the smallest letter whose cyclic distance from the current
     *  letter fits in the remaining budget k, and pay for it.
     *
     *  time = O(26 * N)
     *  space = O(N)
     */
    public String getSmallestString(String s, int k) {
        StringBuilder result = new StringBuilder();
        int budget = k;

        for (int i = 0; i < s.length(); i++) {
            int current = s.charAt(i) - 'a';
            // try targets from 'a' upward; target == current costs 0, so one always fits
            for (int target = 0; target < 26; target++) {
                int cost = cyclicDistance(current, target);
                if (cost <= budget) {
                    result.append((char) ('a' + target));
                    budget -= cost;
                    break;
                }
            }
        }
        return result.toString();
    }

    /** Shortest distance between two letters on the 26-letter cycle. */
    private int cyclicDistance(int a, int b) {
        int diff = Math.abs(a - b);
        return Math.min(diff, 26 - diff);
    }
}
