package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/description/
/**
 *
 * 921. Minimum Add to Make Parentheses Valid
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * A parentheses string is valid if and only if:
 *
 * It is the empty string,
 * It can be written as AB (A concatenated with B), where A and B are valid strings, or
 * It can be written as (A), where A is a valid string.
 * You are given a parentheses string s. In one move, you can insert a parenthesis at any position of the string.
 *
 * For example, if s = "()))", you can insert an opening parenthesis to be "(()))" or a closing parenthesis to be "())))".
 * Return the minimum number of moves required to make s valid.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "())"
 * Output: 1
 * Example 2:
 *
 * Input: s = "((("
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s[i] is either '(' or ')'.
 *
 *
 */
public class MinimumAddToMakeParenthesesValid {

    // V0
//    public int minAddToMakeValid(String s) {
//
//    }

    // V0-1
    // IDEA: GREEDY + BRACKET OP (fixed by gpt)
    // time: O(N), space: O(1)
    public int minAddToMakeValid_0_1(String s) {
        int leftCnt = 0; // "(" waiting to match
        int rightCnt = 0; // ")" waiting to match

        for (String x : s.split("")) {
            if (x.equals("(")) {
                leftCnt++;
            } else { // ")"
                if (leftCnt > 0) {
                    leftCnt--; // match one "("
                } else {
                    rightCnt++; // need one "(" to fix
                }
            }
        }
        return leftCnt + rightCnt;
    }

    // V0-2
    // IDEA: GREEDY + BRACKET OP (fixed by gpt)
    // time: O(N), space: O(1)
    public int minAddToMakeValid_0_2(String s) {
        // edge
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int ans = 0; // number of insertions needed
        int leftCnt = 0; // unmatched "("

        for (String x : s.split("")) {
            if (x.equals("(")) {
                leftCnt++; // one more "(" waiting to match
            } else { // ")"
                if (leftCnt > 0) {
                    leftCnt--; // match with a previous "("
                } else {
                    ans++; // no "(" to match, need to add one
                }
            }
        }

        // unmatched "(" left over
        return ans + leftCnt;
    }

    // V1
    // IDEA: Open Bracket Counter
    // https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/editorial/
    // time: O(N), space: O(1)
    public int minAddToMakeValid_1(String s) {
        int openBrackets = 0;
        int minAddsRequired = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                openBrackets++;
            } else {
                // If an open bracket exists, match it with the closing one
                // If not, we need to add an open bracket.
                if (openBrackets > 0) {
                    openBrackets--;
                } else {
                    minAddsRequired++;
                }
            }
        }

        // Add the remaining open brackets as closing brackets would be required.
        return minAddsRequired + openBrackets;
    }


    // V2
    // https://leetcode.ca/2018-06-08-921-Minimum-Add-to-Make-Parentheses-Valid/
    // time: O(N), space: O(1)
    public int minAddToMakeValid_2(String s) {
        int ans = 0, cnt = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                ++cnt;
            } else if (cnt > 0) {
                --cnt;
            } else {
                ++ans;
            }
        }
        ans += cnt;
        return ans;
    }

    // V3

    // V4

}
