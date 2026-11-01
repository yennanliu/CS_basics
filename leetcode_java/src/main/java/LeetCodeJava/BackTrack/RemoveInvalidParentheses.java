package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/remove-invalid-parentheses/description/

import java.util.*;

/**
 * 301. Remove Invalid Parentheses
 * Hard
 *
 * Given a string s that contains parentheses and letters, remove the minimum number
 * of invalid parentheses to make the input string valid.
 *
 * Return a list of unique strings that are valid with the minimum number of removals.
 * You may return the answer in any order.
 *
 *
 * Example 1:
 *
 * Input: s = "()())()"
 * Output: ["(())()","()()()"]
 *
 * Example 2:
 *
 * Input: s = "(a)())()"
 * Output: ["(a())()","(a)()()"]
 *
 * Example 3:
 *
 * Input: s = ")("
 * Output: [""]
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 25
 * s consists of lowercase English letters and parentheses '(' and ')'.
 * There will be at most 20 parentheses in s.
 *
 */
public class RemoveInvalidParentheses {

    // V0
    // IDEA: BFS LEVEL BY LEVEL (THE FIRST LEVEL THAT HAS A VALID STRING IS THE ANSWER)
    //
    //   "minimum number of removals" is a shortest-path question: level k holds every
    //   string reachable by deleting exactly k characters. The first level that holds
    //   a valid string is returned whole -- anything deeper deletes more than needed.
    //
    //     ")("  ->  level 0: ")("       invalid
    //              level 1: ")", "("    invalid
    //              level 2: ""          valid -> [""]
    //
    //   NOTE: each level is a set -- deleting either '(' of "((" gives the same string,
    //         and without dedupe the frontier blows up and the answer repeats.
    /**
     * time = O(2^N * N)
     * space = O(2^N * N)
     */
    public List<String> removeInvalidParentheses(String s) {
        Set<String> level = new HashSet<>();
        level.add(s);

        while (true) {
            // collect every valid string on this level
            List<String> found = new ArrayList<>();
            for (String candidate : level) {
                if (isValid(candidate)) {
                    found.add(candidate);
                }
            }
            if (!found.isEmpty()) {
                return found;
            }

            // build the next level: delete one more parenthesis, in every position
            Set<String> nextLevel = new HashSet<>();
            for (String candidate : level) {
                for (int i = 0; i < candidate.length(); i++) {
                    char ch = candidate.charAt(i);
                    // only removing a parenthesis can change validity
                    if (ch == '(' || ch == ')') {
                        nextLevel.add(candidate.substring(0, i) + candidate.substring(i + 1));
                    }
                }
            }
            level = nextLevel;
        }
    }

    /** True if every ')' closes an earlier '(' and nothing is left open. */
    private boolean isValid(String s) {
        int open = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                open++;
            } else if (ch == ')') {
                open--;
                // a ')' with nothing open can never be repaired later
                if (open < 0) {
                    return false;
                }
            }
        }
        return open == 0;
    }

    // V1
    // IDEA: COUNT THE REMOVALS FIRST, THEN BACKTRACKING (DFS)
    //
    //   One left-to-right scan tells exactly how many '(' and ')' must go:
    //   an unmatched ')' must be removed, and every '(' still open at the end must be.
    //   The DFS then decides, character by character, to keep or drop it, never
    //   dropping more than those counts and never letting the prefix go negative.
    //   A set dedupes results that drop different-but-equal characters.
    //
    //   Faster than V0 in practice (it never explores a non-minimal removal),
    //   but V0 is easier to explain in an interview.
    /**
     * time = O(2^N * N)
     * space = O(N) recursion, plus the output
     */
    public List<String> removeInvalidParentheses_1(String s) {
        // count the '(' and ')' that must be removed
        int leftToRemove = 0;
        int rightToRemove = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                leftToRemove++;
            } else if (ch == ')') {
                if (leftToRemove > 0) {
                    // this ')' matches an earlier '('
                    leftToRemove--;
                } else {
                    rightToRemove++;
                }
            }
        }

        Set<String> result = new HashSet<>();
        dfs(s, 0, leftToRemove, rightToRemove, 0, new StringBuilder(), result);
        return new ArrayList<>(result);
    }

    /**
     * Decides keep / drop for s[index]; open = '(' kept so far minus ')' kept so far.
     */
    private void dfs(String s, int index, int leftToRemove, int rightToRemove, int open,
                     StringBuilder path, Set<String> result) {
        if (index == s.length()) {
            // all required removals made and the kept string is balanced
            if (leftToRemove == 0 && rightToRemove == 0 && open == 0) {
                result.add(path.toString());
            }
            return;
        }

        char ch = s.charAt(index);
        int lengthBefore = path.length();

        if (ch == '(') {
            // option 1: drop this '('
            if (leftToRemove > 0) {
                dfs(s, index + 1, leftToRemove - 1, rightToRemove, open, path, result);
            }
            // option 2: keep it
            path.append(ch);
            dfs(s, index + 1, leftToRemove, rightToRemove, open + 1, path, result);
        } else if (ch == ')') {
            // option 1: drop this ')'
            if (rightToRemove > 0) {
                dfs(s, index + 1, leftToRemove, rightToRemove - 1, open, path, result);
            }
            // option 2: keep it, only if it closes something
            if (open > 0) {
                path.append(ch);
                dfs(s, index + 1, leftToRemove, rightToRemove, open - 1, path, result);
            }
        } else {
            // a letter is always kept
            path.append(ch);
            dfs(s, index + 1, leftToRemove, rightToRemove, open, path, result);
        }

        // undo whatever this call appended
        path.setLength(lengthBefore);
    }
}
