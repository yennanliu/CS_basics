package LeetCodeJava.Stack;

// https://leetcode.com/problems/minimum-insertions-to-balance-a-parentheses-string/

/**
 *  1541. Minimum Insertions to Balance a Parentheses String
 *  Medium
 *
 *  Given a parentheses string s containing only the characters '(' and ')'.
 *  A parentheses string is balanced if:
 *    - any left parenthesis '(' must have a corresponding two consecutive
 *      right parenthesis '))'
 *    - left parenthesis '(' must go before the corresponding '))'
 *
 *  In other words, we treat '(' as an opening parenthesis and '))' as a
 *  closing parenthesis. For example "())", "())(())))" and "(())())))" are
 *  balanced, while ")()", "()))" and "(()))" are not.
 *
 *  You can insert the characters '(' and ')' at any position of the string to
 *  balance it if needed. Return the minimum number of insertions needed.
 *
 *  Example 1:
 *    Input: s = "(()))"
 *    Output: 1
 *    Explanation: add one ')' at the end -> "(())))" which is balanced.
 *
 *  Example 2:
 *    Input: s = "))())("
 *    Output: 3
 *    Explanation: add '(' to match the first '))', add '))' for the last '('.
 *
 *  Constraints:
 *    1 <= s.length <= 10^5
 *    s consists of '(' and ')' only.
 */
public class MinimumInsertionsToBalanceAParenthesesString {

    // V0
    // IDEA: GREEDY COUNTER (a "close" is the 2-char token '))')
    //       Scan left to right keeping `need` = number of '(' still waiting
    //       for their '))'. No real stack is needed, only its size.
    //         on '(' : need++
    //         on ')' : we are starting a closing token
    //                  - if the NEXT char is also ')' consume both, else the
    //                    token is half-missing -> insert 1 ')'
    //                  - now the token is complete: if need > 0 it pays off
    //                    one '(', else insert a '(' for this orphan '))'
    //       At the end every leftover '(' still needs a full '))' -> 2 each.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minInsertions(String s) {
        int n = s.length();
        int res = 0;
        int need = 0;
        int i = 0;
        while (i < n) {
            if (s.charAt(i) == '(') {
                need++;
                i++;
            } else {
                if (i + 1 < n && s.charAt(i + 1) == ')') {
                    i += 2;
                } else {
                    res++; // insert the missing second ')'
                    i++;
                }
                if (need > 0) {
                    need--;
                } else {
                    res++; // insert a '(' for this orphan '))'
                }
            }
        }
        return res + need * 2;
    }
}
