package LeetCodeJava.Stack;

// https://leetcode.com/problems/decode-string/description/
// https://leetcode.ca/all/394.html

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * 394. Decode String
 * Given an encoded string, return its decoded string.
 * <p>
 * The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated exactly k times. Note that k is guaranteed to be a positive integer.
 * <p>
 * You may assume that the input string is always valid; No extra white spaces, square brackets are well-formed, etc.
 * <p>
 * Furthermore, you may assume that the original data does not contain any digits and that digits are only for those repeat numbers, k. For example, there won't be input like 3a or 2[4].
 * <p>
 * Examples:
 * <p>
 * s = "3[a]2[bc]", return "aaabcbc".
 * s = "3[a2[c]]", return "accaccacc".
 * s = "2[abc]3[cd]ef", return "abcabccdcdcdef".
 * <p>
 * <p>
 * Difficulty:
 * Medium
 * Lock:
 * Normal
 * Company:
 * Amazon AppDynamics Apple Atlassian Bloomberg Cisco Coupang Cruise Automation Facebook Google Huawei Microsoft Oracle Salesforce Snapchat Tencent VMware Yahoo Yelp
 */
public class DecodeString {

    // V0
    // IDEA : STACK
    // TODO : optimize code
    public String decodeString(String s) {
        if (s.isEmpty()) {
            return null;
        }
        // init
        Stack<String> stack = new Stack<>(); // ??
        StringBuilder sb = new StringBuilder();
        String A_TO_Z = "abcdefghijklmnopqrstuvwxyz";
        for (String x : s.split("")) {
            //System.out.println(">>> x = " + x);
            String tmp = "";
            StringBuilder tmpSb = new StringBuilder();
            if (!x.equals("]")) {
                if (!x.equals("[")) {
                    stack.add(x);
                }
            } else {
                // pop all elements from stack, multiply, and add to res
                while (!stack.isEmpty()) {
                    String cur = stack.pop(); // ??
                    if (A_TO_Z.contains(cur)) {
                        tmp = cur + tmp;
                    } else {
                        tmp = getMultiplyStr(tmp, Integer.parseInt(cur));
                    }
                }
            }
            sb.append(tmp);
        }

        StringBuilder tmpSb = new StringBuilder();

        // add remaining stack element to result
        while (!stack.isEmpty()) {
            tmpSb.append(stack.pop());
        }

        sb.append(tmpSb.reverse());
        return sb.toString();
    }

    private String getMultiplyStr(String cur, Integer multiply) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < multiply; x++) {
            sb.append(cur);
        }
        return sb.toString();
    }


    // V1
    // https://leetcode.ca/2016-12-28-394-Decode-String/
    public String decodeString_1(String s) {
        Deque<Integer> s1 = new ArrayDeque<>();
        Deque<String> s2 = new ArrayDeque<>();
        int num = 0;
        String res = "";
        for (char c : s.toCharArray()) {
            if ('0' <= c && c <= '9') {
                num = num * 10 + c - '0';
            } else if (c == '[') {
                s1.push(num);
                s2.push(res);
                num = 0;
                res = "";
            } else if (c == ']') {
                StringBuilder t = new StringBuilder();
                for (int i = 0, n = s1.pop(); i < n; ++i) {
                    t.append(res);
                }
                res = s2.pop() + t.toString();
            } else {
                res += String.valueOf(c);
            }
        }
        return res;
    }

    // V2
    // IDEA : STACK (gpt)
    public static String decodeString_2(String s) {
        if (s.isEmpty()) {
            return "";
        }

        // Stacks for numbers and strings
        Stack<Integer> countStack = new Stack<>();
        Stack<StringBuilder> stringStack = new Stack<>();
        StringBuilder currentString = new StringBuilder();
        int k = 0; // Temporary variable for number parsing

        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch)) {
                // Build the number (support for multi-digit numbers)
                k = k * 10 + (ch - '0');
            } else if (ch == '[') {
                // Push current number and string onto their respective stacks
                countStack.push(k);
                stringStack.push(currentString);
                currentString = new StringBuilder(); // Start a new substring
                k = 0; // Reset the number
            } else if (ch == ']') {
                // Decode substring
                int count = countStack.pop();
                StringBuilder decodedString = stringStack.pop();
                for (int i = 0; i < count; i++) {
                    decodedString.append(currentString);
                }
                currentString = decodedString; // Update current string
            } else {
                // Append characters to current string
                currentString.append(ch);
            }
        }

        return currentString.toString();
    }

}
