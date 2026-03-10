package LeetCodeJava.Stack;

// https://leetcode.com/problems/make-the-string-great/description/

import java.util.Stack;

/**
 *  1544. Make The String Great
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a string s of lower and upper case English letters.
 *
 * A good string is a string which doesn't have two adjacent characters s[i] and s[i + 1] where:
 *
 * 0 <= i <= s.length - 2
 * s[i] is a lower-case letter and s[i + 1] is the same letter but in upper-case or vice-versa.
 * To make the string good, you can choose two adjacent characters that make the string bad and remove them. You can keep doing this until the string becomes good.
 *
 * Return the string after making it good. The answer is guaranteed to be unique under the given constraints.
 *
 * Notice that an empty string is also good.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "leEeetcode"
 * Output: "leetcode"
 * Explanation: In the first step, either you choose i = 1 or i = 2, both will result "leEeetcode" to be reduced to "leetcode".
 * Example 2:
 *
 * Input: s = "abBAcC"
 * Output: ""
 * Explanation: We have many possible scenarios, and all lead to the same answer. For example:
 * "abBAcC" --> "aAcC" --> "cC" --> ""
 * "abBAcC" --> "abBA" --> "aA" --> ""
 * Example 3:
 *
 * Input: s = "s"
 * Output: "s"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s contains only lower and upper case English letters.
 *
 *
 *
 */
public class MakeTheStringGreat {

    // V0
//    public String makeGood(String s) {
//
//    }

    // V0-1
    // IDEA: STACK (gemini)
    /** NOTE !!
     *
     *  core idea:
     *
     *   The "Great" Condition: A pair is bad if |char1 - char2| == 32.
     */
    public String makeGood_0_1(String s) {
        // 1. Edge cases
        if (s == null || s.length() < 2) {
            return s;
        }

        // 2. Use a Stack (or a Deque for better performance in Java)
        Stack<Character> stack = new Stack<>();

        for (char curr : s.toCharArray()) {
            if (!stack.isEmpty()) {
                char prev = stack.peek();

                /** NOTE !!
                 *
                 *  core idea:
                 *
                 *   The "Great" Condition: A pair is bad if |char1 - char2| == 32.
                 */
                // The "Bad Pair" check:
                // Math.abs('a' - 'A') is 32.
                // This checks if they are the same letter but different case.
                if (Math.abs(curr - prev) == 32) {
                    stack.pop(); // They react! Remove them.
                    continue; // Move to next character
                }
            }
            stack.push(curr);
        }

        // 3. Build result from the remaining characters in the stack
        StringBuilder sb = new StringBuilder();
        for (char c : stack) {
            sb.append(c);
        }

        return sb.toString();
    }


    // V0-2
    // IDEA: STRING  + |char1 - char2| == 32. check (gemini)
    public String makeGood_0_2(String s) {

        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            int len = sb.length();
            if (len > 0 && Math.abs(sb.charAt(len - 1) - c) == 32) {
                sb.deleteCharAt(len - 1);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();

    }

    // V0-3
    // IDEA: STACK (GPT)
    public String makeGood_0_3(String s) {

        if (s == null || s.length() <= 1) {
            return s;
        }

        Stack<Character> st = new Stack<>();

        for (char ch : s.toCharArray()) {

            /** NOTE !!
             *
             *  core idea:
             *
             *   The "Great" Condition: A pair is bad if |char1 - char2| == 32.
             */
            if (!st.isEmpty() && Math.abs(st.peek() - ch) == 32) {
                st.pop();
            } else {
                st.push(ch);
            }
        }

        StringBuilder sb = new StringBuilder();

        for (char c : st) {
            sb.append(c);
        }

        return sb.toString();
    }


    // V1
    // IDEA: STACK
    // https://leetcode.com/problems/make-the-string-great/solutions/4975146/9844easy-soluitonwith-explanation-by-mra-mqgb/
    public String makeGood_1(String s) {
        Stack<Character> stack = new Stack<>();

        for (char c : s.toCharArray()) {
            if (!stack.isEmpty() && Math.abs(c - stack.peek()) == 32) {
                stack.pop();
            } else {
                stack.push(c);
            }
        }

        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.insert(0, stack.pop());
        }

        return result.toString();
    }

    // V2-1
    // https://leetcode.com/problems/make-the-string-great/solutions/4975247/beats-98ascii-and-stack-2-approaches-wit-5izu/
    public String makeGood_2_1(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        int flag = 0;
        while (flag == 0 && sb.length() > 0) {
            flag = 1;
            for (int i = 0; i < sb.length() - 1; i++) {
                if (isGreat(sb, i)) {
                    sb.delete(i, i + 2);
                    flag = 0;
                }
            }
        }
        return sb.toString();
    }

    public static boolean isGreat(StringBuilder sb, int i) {
        if (Integer.valueOf(sb.charAt(i)) == Integer.valueOf(sb.charAt(i + 1)) + 32
                || Integer.valueOf(sb.charAt(i)) == Integer.valueOf(sb.charAt(i + 1)) - 32) {

            return true;
        }
        return false;
    }


    // V2-2
    // https://leetcode.com/problems/make-the-string-great/solutions/4975247/beats-98ascii-and-stack-2-approaches-wit-5izu/
    public String makeGood_2_2(String s) {
        int n = s.length();
        Stack<Character> st = new Stack<>();
        String ans = "";
        st.push(s.charAt(0));
        char[] arr = s.toCharArray();
        for (int i = 1; i < n; i++) {
            if (!st.isEmpty() && (st.peek() - arr[i] == 32 || st.peek() - arr[i] == -32)) {
                st.pop();
            } else {
                st.push(arr[i]);
            }

        }
        while (!st.empty()) {
            ans = st.pop() + ans;
        }
        return ans;
    }



    // V3




}
