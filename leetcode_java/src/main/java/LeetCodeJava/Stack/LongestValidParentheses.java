package LeetCodeJava.Stack;

// https://leetcode.com/problems/longest-valid-parentheses/

import java.util.Stack;

/**
 *  32. Longest Valid Parentheses
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 *
 * Given a string containing just the characters '(' and ')', return the length of the longest valid (well-formed) parentheses substring.
 *  Example 1:
 *
 * Input: s = "(()"
 * Output: 2
 * Explanation: The longest valid parentheses substring is "()".
 * Example 2:
 *
 * Input: s = ")()())"
 * Output: 4
 * Explanation: The longest valid parentheses substring is "()()".
 * Example 3:
 *
 * Input: s = ""
 * Output: 0
 *
 * Constraints:
 *
 * 0 <= s.length <= 3 * 104
 * s[i] is '(', or ')'.
 *
 *
 */
public class LongestValidParentheses {

    // V0
//    public int longestValidParentheses(String s) {
//
//    }

    // V1-1
    // https://leetcode.ca/2016-01-01-32-Longest-Valid-Parentheses/
    public int longestValidParentheses_1_1(String s) {
        int n = s.length();
        int[] f = new int[n + 1];
        int ans = 0;
        for (int i = 2; i <= n; ++i) {
            if (s.charAt(i - 1) == ')') {
                if (s.charAt(i - 2) == '(') {
                    f[i] = f[i - 2] + 2;
                } else {
                    int j = i - f[i - 1] - 1;
                    if (j > 0 && s.charAt(j - 1) == '(') {
                        f[i] = f[i - 1] + 2 + f[j - 1];
                    }
                }
                ans = Math.max(ans, f[i]);
            }
        }
        return ans;
    }

    // V1-2
    // https://leetcode.ca/2016-01-01-32-Longest-Valid-Parentheses/
    public int longestValidParentheses_1_2(String s) {
        int res = 0, left = 0, right = 0, n = s.length();

        // from left to right, '(()' => will never hit left==right
        for (int i = 0; i < n; ++i) {
            if (s.charAt(i) == '(') ++left;
            else ++right;

            if (left == right) res = Math.max(res, 2 * right);
            else if (right > left) left = right = 0;
        }

        // from right to left, '())' => will never hit left==right
        left = right = 0;
        for (int i = n - 1; i >= 0; --i) {
            if (s.charAt(i) == '(') ++left;
            else ++right;

            if (left == right) res = Math.max(res, 2 * left);
            else if (left > right) left = right = 0;
        }
        return res;

    }

    // V1-3
    // https://leetcode.ca/2016-01-01-32-Longest-Valid-Parentheses/
    public int longestValidParentheses_1_3(String s) {
        Stack<Integer> sk = new Stack<>();
        int start = 0;
        int result = 0;
        for (int i = 0;i < s.length(); i++) {
            if(s.charAt(i) == '(') {
                sk.push(i);
            } else {
                if (sk.empty()) {
                    start = i + 1;
                } else {
                    sk.pop();
                    result = Math.max(result, sk.isEmpty() ? i - start + 1 : i - sk.peek());
                }
            }
        }
        return result;

    }

    // V2
    // https://leetcode.com/problems/longest-valid-parentheses/solutions/5373015/stack-solution-video-explanation-by-niit-x3ct/
    public int longestValidParentheses_2(String s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        int max_len = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i);
                } else {
                    max_len = Math.max(max_len, i - stack.peek());
                }
            }
        }

        return max_len;
    }

    // V3
    public int longestValidParentheses_3(String s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1); // Base index
        int maxLength = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i);
                } else {
                    maxLength = Math.max(maxLength, i - stack.peek());
                }
            }
        }
        return maxLength;
    }


    // V4
    // https://leetcode.com/problems/longest-valid-parentheses/solutions/3401956/100-detailed-explaination-with-pictures-xc4yq/
    public int longestValidParentheses_4(String s) {
        int leftCount = 0;
        int rightCount = 0;
        int maxLength = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                leftCount++;
            } else {
                rightCount++;
            }

            if (leftCount == rightCount) {
                maxLength = Math.max(maxLength, 2 * rightCount);
            } else if (rightCount > leftCount) {
                leftCount = rightCount = 0;
            }
        }

        leftCount = rightCount = 0;

        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') {
                leftCount++;
            } else {
                rightCount++;
            }

            if (leftCount == rightCount) {
                maxLength = Math.max(maxLength, 2 * leftCount);
            } else if (leftCount > rightCount) {
                leftCount = rightCount = 0;
            }
        }

        return maxLength;
    }



}
