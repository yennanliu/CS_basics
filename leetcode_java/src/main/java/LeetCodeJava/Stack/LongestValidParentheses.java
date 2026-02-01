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
    /**
     * time = O(1)
     * space = O(1)
     */
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

    // V0-5
    // IDEA: 1D DP (gemini)
    /**  IDEA:
     *
     *  To solve **LeetCode 32: Longest Valid Parentheses**,
     *  we can use a 1D DP array
     *  where
     *    `dp[i]` represents the length of the longest valid parentheses
     *      substring **ending at index `i**`.
     *
     * ### 💡 The Logic
     *
     * A valid substring must end with a closing parenthesis `')'`.
     * Therefore, for any `dp[i]` where `s.charAt(i) == '('`,
     * the value will always be `0`. We only update `dp[i]`
     * when we encounter a `')'`.
     *
     *
     *
     * There are two main cases when we find a `')'` at index `i`:
     *
     * 1. **Case 1: `s[i-1] == '('**` (Pattern: `...()`)
     * * We found a simple pair. The length is 2 plus whatever valid
     * length existed before this pair.
     * *
     *
     *
     * 2. **Case 2: `s[i-1] == ')'**` (Pattern: `...))`)
     * * If the character at `i-1` was also a closing bracket, it must have been part of its own valid substring (length `dp[i-1]`).
     * * We look for a matching opening bracket for our current `')'` at index: ****.
     * * If `s[j] == '('`, then we've found a match!
     * *
     *

     *
     * ---
     *
     * ### 🔍 Dry Run Example
     *
     * **Input:** `s = "()(())"`
     *
     * 1. `i=1` (`)`): Matches `s[0]`. `dp[1] = dp[-1] + 2 = 2`.
     * 2. `i=2` (`(`): `dp[2] = 0`.
     * 3. `i=3` (`(`): `dp[3] = 0`.
     * 4. `i=4` (`)`): Matches `s[3]`. `dp[4] = dp[2] + 2 = 2`.
     * 5. `i=5` (`)`): `s[4]` is `)`. Look for match for `s[5]`.
     * * `dp[4]` is 2. The matching index is .
     * * `s[2]` is `(`. Found a match!
     * * `dp[5] = dp[4] + 2 + dp[1]` (inner + new pair + previous)
     * * `dp[5] = 2 + 2 + 2 = 6`.
     *
     *
     *
     * ### 📊 Complexity Analysis
     *
     * * **Time Complexity:**  since we iterate through the string once.
     * * **Space Complexity:**  to store the `dp` array.
     *
     */
    /**
     * time = O(1)
     * space = O(1)
     */
    public int longestValidParentheses_0_5(String s) {
        int n = s.length();
        if (n < 2)
            return 0;

        /**  NOTE !!
         *
         *  DP def:
         *
         *   `dp[i]` : the length of the longest
         *      valid parentheses substring **ending at index `i**
         *
         */
        int[] dp = new int[n];
        int maxLen = 0;

        // Start from index 1 because index 0 cannot form a pair
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                // Case 1: s[i-1] is '(', simple pair "()"
                if (s.charAt(i - 1) == '(') {
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                }
                // Case 2: s[i-1] is ')', check if there's a matching '(' before the sub-sequence
                else {
                    int matchingOpenIndex = i - dp[i - 1] - 1;
                    if (matchingOpenIndex >= 0 && s.charAt(matchingOpenIndex) == '(') {
                        // Current match (2) + inner match (dp[i-1]) + match before the opening bracket
                        int beforeMatch = (matchingOpenIndex >= 1) ? dp[matchingOpenIndex - 1] : 0;
                        dp[i] = dp[i - 1] + 2 + beforeMatch;
                    }
                }
                maxLen = Math.max(maxLen, dp[i]);
            }
        }

        return maxLen;
    }

    // V0-6
    // IDEA: 1D DP (GPT)
    /**  IDEA:
     *
     * Here’s a clean **Java DP solution** for **LeetCode 32. Longest Valid Parentheses**.
     *
     * ---
     *
     * ## 🧠 Problem Recap
     *
     * Given a string of `'('` and `')'`, find the length of the **longest valid (well-formed)** parentheses substring.
     *
     * ---
     *
     * ## 💡 DP Strategy
     *
     * Let:
     *
     * * `dp[i]` = **length of longest valid parentheses ending at index `i`**
     * * We only update when `s.charAt(i) == ')'` because a valid substring must end with `)`
     *
     * Transitions:
     *
     * ```
     * if s[i] == ')':
     *     if s[i-1] == '(':
     *         dp[i] = (i >= 2 ? dp[i-2] : 0) + 2
     *
     *     else if s[i-1] == ')' and
     *             s[i - dp[i-1] - 1] == '(':
     *         dp[i] = dp[i-1] + 2 +
     *                 (i - dp[i-1] - 2 >= 0 ? dp[i - dp[i-1] - 2] : 0)
     * ```
     *
     * Explanation:
     *
     * * Case `...()`: pair directly with previous `'('`
     * * Case `...))`: match current `)` with a `'('` before a valid block
     *
     * Track **max length** over all `dp[i]`.

     * ---
     *
     * ## 📊 Example
     *
     * ```
     * Input:  "(()())"
     * dp:     0 0 2 0 4 6
     * Output: 6
     * ```
     *
     * ```
     * Input:  ")()())"
     * dp:     0 0 2 0 4 0
     * Output: 4
     * ```
     *
     * ---
     *
     * ## ⏱ Complexity
     *
     * | Metric | Value |
     * | ------ | ----- |
     * | Time   | O(n)  |
     * | Space  | O(n)  |
     *
     * ---
     *
     * ## 🚀 Why It Works
     *
     * The recursive definition:
     *
     * * Builds on previous valid substrings
     * * Efficiently jumps over inner valid regions
     * * Avoids re-scanning characters → linear time
     *
     */
    /**
     * time = O(1)
     * space = O(1)
     */
    public int longestValidParentheses_0_6(String s) {
        int n = s.length();
        if (n < 2)
            return 0;

        int[] dp = new int[n];
        int maxLen = 0;

        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                // Case 1: immediate "()"
                if (s.charAt(i - 1) == '(') {
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                }
                // Case 2: ... "))"
                else if (i - dp[i - 1] - 1 >= 0
                        && s.charAt(i - dp[i - 1] - 1) == '(') {
                    dp[i] = dp[i - 1] + 2
                            + ((i - dp[i - 1] - 2 >= 0)
                            ? dp[i - dp[i - 1] - 2]
                            : 0);
                }
                maxLen = Math.max(maxLen, dp[i]);
            }
        }

        return maxLen;
    }



    // V1-1
    // https://leetcode.ca/2016-01-01-32-Longest-Valid-Parentheses/
    /**
     * time = O(1)
     * space = O(1)
     */
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
    /**
     * time = O(1)
     * space = O(1)
     */
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
    /**
     * time = O(1)
     * space = O(1)
     */
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
    /**
     * time = O(1)
     * space = O(1)
     */
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
    /**
     * time = O(1)
     * space = O(1)
     */
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
    /**
     * time = O(1)
     * space = O(1)
     */
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
