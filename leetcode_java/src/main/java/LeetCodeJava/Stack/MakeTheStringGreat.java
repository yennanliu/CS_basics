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
