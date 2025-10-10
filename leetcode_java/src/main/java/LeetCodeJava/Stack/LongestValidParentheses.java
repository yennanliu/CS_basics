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

    // V0-1
    // IDEA: STACK (fixed by gpt)
    public int longestValidParentheses_0_1(String s) {
        // Edge cases
        // Strings shorter than 2 can’t form valid pairs.
        if (s == null || s.length() < 2) {
            return 0;
        }

        /**  NOTE !!!
         *
         *  Stack : { idx_of_char }
         *
         * - stack — will store indexes of characters in s (not the chars themselves).
         *
         *  -> This helps compute substring lengths.
         *
         * - maxLen — will track the maximum valid length found so far.
         */
        Stack<Integer> stack = new Stack<>();
        int maxLen = 0;

        // Base: push -1 to handle the first valid substring
        /**
         * Push a base index -1 before iteration.
         * Why?
         *
         * It acts as a starting boundary for the first valid substring.
         *
         * When we find a valid substring starting from index 0,
         * subtracting from -1 gives the correct length.
         *
         * 📦 Stack now: [ -1 ]
         */
        stack.push(-1);

        // Iterate through each character in the string, keeping track of its index i.
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            /**
             *  CASE 1) "("
             *
             *  ➡️ Push its index onto the stack.
             *
             * Reason:
             *
             * A '(' could be matched later by a ')'.
             *
             * Keeping its index lets us know where a valid substring could start.
             *
             */
            if (c == '(') {
                stack.push(i);
            }
            /**  CASE 2)  ")"
             *
             *  Pop the last '(' index —
             *  we’re trying to match it with this ')'.
             *
             */
            else { // c == ')'
                stack.pop();

                /**  SUB CASE 2-1) Stack becomes empty after popping
                 *
                 *  ➡️ If the stack is empty, it means there’s
                 *  no unmatched '(' left to pair with this ')'.
                 *  So this ')' is a kind of “reset point”.
                 *
                 * We push its index to mark a new base.
                 *
                 * -> This means: valid substrings can only start after index 0.
                 *
                 */
                if (stack.isEmpty()) {
                    // no matching '(' — reset base index
                    stack.push(i);
                }
                /**  SUB CASE 2-2) Stack is not empty
                 *
                 * ➡️ If the stack still has something,
                 *  the top element (stack.peek()) represents
                 *  the index before the start of the current valid substring.
                 *
                 * Substring length = current index (i) − top of stack.
                 *
                 */
                else {
                    // valid substring length = current index - last unmatched '(' index
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }

        return maxLen;
    }

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
