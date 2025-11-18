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
    /**
     *  Example:
     *
     *  # Execution Visualization for `decodeString("3[a2[c]]")`
     *
     * ---
     *
     * ### Initial state
     * ```
     * stack = []
     * ```
     *
     * ---
     *
     * ### Step 1 → char = "3"
     * - Not `]` → push `"3"`
     * ```
     * stack = ["3"]
     * ```
     *
     * ---
     *
     * ### Step 2 → char = "["
     * - Not `]` → push `"["`
     * ```
     * stack = ["3", "["]
     * ```
     *
     * ---
     *
     * ### Step 3 → char = "a"
     * - Not `]` → push `"a"`
     * ```
     * stack = ["3", "[", "a"]
     * ```
     *
     * ---
     *
     * ### Step 4 → char = "2"
     * - Not `]` → push `"2"`
     * ```
     * stack = ["3", "[", "a", "2"]
     * ```
     *
     * ---
     *
     * ### Step 5 → char = "["
     * - Not `]` → push `"["`
     * ```
     * stack = ["3", "[", "a", "2", "["]
     * ```
     *
     * ---
     *
     * ### Step 6 → char = "c"
     * - Not `]` → push `"c"`
     * ```
     * stack = ["3", "[", "a", "2", "[", "c"]
     * ```
     *
     * ---
     *
     * ### Step 7 → char = "]"
     * - Start popping until `"["`:
     *   - Pop `"c"` → segment = `"c"`
     *   - Pop `"["`
     * - Now stack = `["3", "[", "a", "2"]`
     * - Pop digits:
     *   - Pop `"2"` → repeat = 2
     * - Repeat `"c"` → `"cc"`
     * - Push `"cc"`
     * ```
     * stack = ["3", "[", "a", "cc"]
     * ```
     *
     * ---
     *
     * ### Step 8 → char = "]"
     * - Start popping until `"["`:
     *   - Pop `"cc"` → segment = `"cc"`
     *   - Pop `"a"` → segment = `"a" + "cc"` = `"acc"`
     *   - Pop `"["`
     * - Now stack = ["3"]
     * - Pop digits:
     *   - Pop `"3"` → repeat = 3
     * - Repeat `"acc"` → `"accaccacc"`
     * - Push `"accaccacc"`
     * ```
     * stack = ["accaccacc"]
     * ```
     *
     * ---
     *
     * ### End of input
     * Join everything in stack:
     * ```
     * result = "accaccacc"
     * ```
     *
     * ✅ Final Output: `"accaccacc"`
     *
     *
     */
    public String decodeString(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        Stack<String> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();

        for (String x : s.split("")) {
            if (!x.equals("]")) {
                // push everything except we ignore "]"
                stack.push(x);
            } else {
                // pop until "["
                /**
                 *  NOTE !!!
                 *
                 *   we NEED to add "[" to stack
                 *   since "[" can work as a `separator`
                 *   so we know when to STOP when pop elment from stack
                 */
                StringBuilder segment = new StringBuilder();
                while (!stack.isEmpty() && !stack.peek().equals("[")) {
                    segment.insert(0, stack.pop());
                }
                stack.pop(); // remove "["

                // now get the number (could be multi-digit, but we’ll read digits one by one)
                StringBuilder numSb = new StringBuilder();
                 String nums = "0123456789";

                // below 2 approaches are OK
                //  while(!stack.isEmpty() && stack.peek().matches("\\d")){} ..
                // 	.matches("\\d") → checks if that string is a single digit (0–9).
                while (!stack.isEmpty() && nums.contains(stack.peek())) {
                    numSb.insert(0, stack.pop()); // prepend digit
                }
                int repeat = Integer.parseInt(numSb.toString());

                // build repeated substring
                String repeated = getMultiplyStr(segment.toString(), repeat);
                stack.push(repeated);
            }
        }

        // build result from stack
        while (!stack.isEmpty()) {
            sb.insert(0, stack.pop());
        }

        return sb.toString();
    }

    private String getMultiplyStr(String cur, int multiply) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < multiply; x++) {
            sb.append(cur);
        }
        return sb.toString();
    }

    // V0-1
    // IDEA: STACK (fixed by gemini)
    /**
     * Decodes a string with nested encoded sub-strings (LC 394).
     * Time Complexity: O(MaxK * N), where MaxK is the largest repetition count and N is the length of the string.
     * Space Complexity: O(N)
     *
     * @param s The encoded string.
     * @return The decoded string.
     */
    public String decodeString_0_1(String s) {
        // Stack to store counts (k)
        Stack<Integer> countStack = new Stack<>();
        // Stack to store intermediate decoded strings (the parts *before* the current '[')
        Stack<String> resultStack = new Stack<>();

        StringBuilder currentResult = new StringBuilder();
        int k = 0; // Current count being built

        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch)) {
                /** NOTE !!!
                 * 
                 *  for `digit` parsing, need to consider `muti-digit` cases.
                 *  e.g. 
                 *    10
                 *    100
                 *    333
                 *    
                 *  -> so below logic for handling this
                 */
                // 1. If it's a digit, build the multi-digit repetition count 'k'
                k = k * 10 + (ch - '0');

            } else if (ch == '[') {
                // 2. If '[', save the current state before descending into the nested structure

                // Push the current count 'k' onto the count stack
                countStack.push(k);

                // Push the string built so far onto the result stack (e.g., "a" in "3[a2[c]]")
                resultStack.push(currentResult.toString());

                // Reset the count and the current result to start fresh for the inner structure
                k = 0;
                currentResult = new StringBuilder();

            } else if (ch == ']') {
                // 3. If ']', decode the inner structure

                // Get the repetition count 'k'
                int repeatTimes = countStack.pop();

                // Get the string segment built so far (e.g., "c" or "cc")
                String decodedSegment = currentResult.toString();

                // Repeat the segment using a temporary StringBuilder
                StringBuilder temp = new StringBuilder();
                for (int i = 0; i < repeatTimes; i++) {
                    temp.append(decodedSegment);
                }

                // Combine: Pop the previous string segment (from before the '['), and append the new repeated segment
                String previousResult = resultStack.pop();
                currentResult = new StringBuilder(previousResult).append(temp.toString());

            } else {
                // 4. If it's a letter, just append it to the current result string
                currentResult.append(ch);
            }
        }

        // The final decoded string is in currentResult
        return currentResult.toString();
    }

    // V0-2
    // IDEA: STACK (fixed by gpt)
    public String decodeString_0_2(String s) {
        if (s == null || s.isEmpty())
            return s;

        Stack<Integer> countStack = new Stack<>();
        Stack<StringBuilder> stringStack = new Stack<>();
        StringBuilder currentStr = new StringBuilder();
        int k = 0;

        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch)) {
                k = k * 10 + ch - '0'; // build multi-digit number
            } else if (ch == '[') {
                countStack.push(k);
                stringStack.push(currentStr);
                currentStr = new StringBuilder();
                k = 0;
            } else if (ch == ']') {
                StringBuilder decoded = stringStack.pop();
                int times = countStack.pop();
                for (int i = 0; i < times; i++) {
                    decoded.append(currentStr);
                }
                currentStr = decoded;
            } else {
                currentStr.append(ch);
            }
        }

        return currentStr.toString();
    }


    // V1
    // https://youtu.be/qB0zZpBJlh8?si=ZZBP6BrcHvIIfup9
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
