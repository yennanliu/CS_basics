package LeetCodeJava.String;

// https://leetcode.com/problems/valid-parenthesis-string/description/

import java.util.Stack;

/**
 * 678. Valid Parenthesis String
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a string s containing only three types of characters: '(', ')' and '*', return true if s is valid.
 *
 * The following rules define a valid string:
 *
 * Any left parenthesis '(' must have a corresponding right parenthesis ')'.
 * Any right parenthesis ')' must have a corresponding left parenthesis '('.
 * Left parenthesis '(' must go before the corresponding right parenthesis ')'.
 * '*' could be treated as a single right parenthesis ')' or a single left parenthesis '(' or an empty string "".
 *
 *
 * Example 1:
 *
 * Input: s = "()"
 * Output: true
 * Example 2:
 *
 * Input: s = "(*)"
 * Output: true
 * Example 3:
 *
 * Input: s = "(*))"
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s[i] is '(', ')' or '*'.
 *
 */
public class ValidParenthesisString {

    // V0
//    public boolean checkValidString(String s) {
//
//    }

    // V1-1
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: RECURSION
    public boolean checkValidString_1_1(String s) {

        return dfs(0, 0, s);
    }

    private boolean dfs(int i, int open, String s) {
        if (open < 0) return false;
        if (i == s.length()) return open == 0;

        if (s.charAt(i) == '(') {
            return dfs(i + 1, open + 1, s);
        } else if (s.charAt(i) == ')') {
            return dfs(i + 1, open - 1, s);
        } else {
            return dfs(i + 1, open, s) ||
                    dfs(i + 1, open + 1, s) ||
                    dfs(i + 1, open - 1, s);
        }
    }

    // V1-2
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: DP (TOP DOWN)
    public boolean checkValidString_1_2(String s) {
        int n = s.length();
        Boolean[][] memo = new Boolean[n + 1][n + 1];
        return dfs(0, 0, s, memo);
    }

    private boolean dfs(int i, int open, String s, Boolean[][] memo) {
        if (open < 0) return false;
        if (i == s.length()) return open == 0;

        if (memo[i][open] != null) return memo[i][open];

        boolean result;
        if (s.charAt(i) == '(') {
            result = dfs(i + 1, open + 1, s, memo);
        } else if (s.charAt(i) == ')') {
            result = dfs(i + 1, open - 1, s, memo);
        } else {
            result = (dfs(i + 1, open, s, memo) ||
                    dfs(i + 1, open + 1, s, memo) ||
                    dfs(i + 1, open - 1, s, memo));
        }

        memo[i][open] = result;
        return result;
    }


    // V1-3
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: DP (BOTTOM UP)
    public boolean checkValidString_1_3(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n + 1][n + 1];
        dp[n][0] = true;

        for (int i = n - 1; i >= 0; i--) {
            for (int open = 0; open < n; open++) {
                boolean res = false;
                if (s.charAt(i) == '*') {
                    res |= dp[i + 1][open + 1];
                    if (open > 0) res |= dp[i + 1][open - 1];
                    res |= dp[i + 1][open];
                } else {
                    if (s.charAt(i) == '(') {
                        res |= dp[i + 1][open + 1];
                    } else if (open > 0) {
                        res |= dp[i + 1][open - 1];
                    }
                }
                dp[i][open] = res;
            }
        }
        return dp[0][0];
    }


    // V1-4
    // https:/neetcode.io/problems/valid-parenthesis-string
    // IDEA: DP (SPACE OPTIMIZED)
    public boolean checkValidString_1_4(String s) {
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;

        for (int i = n - 1; i >= 0; i--) {
            boolean[] newDp = new boolean[n + 1];
            for (int open = 0; open < n; open++) {
                if (s.charAt(i) == '*') {
                    newDp[open] = dp[open + 1] ||
                            (open > 0 && dp[open - 1]) || dp[open];
                } else if (s.charAt(i) == '(') {
                    newDp[open] = dp[open + 1];
                } else if (open > 0) {
                    newDp[open] = dp[open - 1];
                }
            }
            dp = newDp;
        }
        return dp[0];
    }


    // V1-5
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: STACK
    public boolean checkValidString_1_5(String s) {
        Stack<Integer> left = new Stack<>();
        Stack<Integer> star = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                left.push(i);
            } else if (ch == '*') {
                star.push(i);
            } else {
                if (left.isEmpty() && star.isEmpty()) return false;
                if (!left.isEmpty()) {
                    left.pop();
                } else{
                    star.pop();
                }
            }
        }
        while (!left.isEmpty() && !star.isEmpty()) {
            if (left.pop() > star.pop())
                return false;
        }
        return left.isEmpty();
    }

    // V1-6
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: GREEDY
    public boolean checkValidString_1_6(String s) {
        int leftMin = 0, leftMax = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                leftMin++;
                leftMax++;
            } else if (c == ')') {
                leftMin--;
                leftMax--;
            } else {
                leftMin--;
                leftMax++;
            }
            if (leftMax < 0) {
                return false;
            }
            if (leftMin < 0) {
                leftMin = 0;
            }
        }
        return leftMin == 0;
    }


    // V2

}
