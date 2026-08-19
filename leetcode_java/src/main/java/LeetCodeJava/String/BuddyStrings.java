package LeetCodeJava.String;

// https://leetcode.com/problems/buddy-strings/

/**
 *  859. Buddy Strings
 *  Easy
 *
 *  Given two strings s and goal, return true if you can swap two letters in s
 *  so the result is equal to goal, otherwise, return false.
 *
 *  Swapping letters is defined as taking two indices i and j (0-indexed) such
 *  that i != j and swapping the characters at s[i] and s[j].
 *
 *  Example 1:
 *  Input: s = "ab", goal = "ba"
 *  Output: true
 *
 *  Example 2:
 *  Input: s = "ab", goal = "ab"
 *  Output: false (the only swap gives "ba" != goal)
 *
 *  Example 3:
 *  Input: s = "aa", goal = "aa"
 *  Output: true
 *
 *  Constraints:
 *   - 1 <= s.length, goal.length <= 2 * 10^4
 *   - s and goal consist of lowercase letters.
 */
public class BuddyStrings {

    // V0
    // IDEA: collect mismatching indices; need exactly 2 that cross-match,
    //       or 0 mismatches plus a duplicated letter in s.
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean buddyStrings(String s, String goal) {
        if (s == null || goal == null || s.length() != goal.length()) {
            return false;
        }

        if (s.equals(goal)) {
            // need at least one duplicate letter so a swap is a no-op
            boolean[] seen = new boolean[26];
            for (int i = 0; i < s.length(); i++) {
                int c = s.charAt(i) - 'a';
                if (seen[c]) {
                    return true;
                }
                seen[c] = true;
            }
            return false;
        }

        int first = -1;
        int second = -1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != goal.charAt(i)) {
                if (first == -1) {
                    first = i;
                } else if (second == -1) {
                    second = i;
                } else {
                    return false; // more than 2 diffs
                }
            }
        }

        return second != -1
                && s.charAt(first) == goal.charAt(second)
                && s.charAt(second) == goal.charAt(first);
    }
}
