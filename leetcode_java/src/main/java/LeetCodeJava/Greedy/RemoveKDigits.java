package LeetCodeJava.Greedy;

// https://leetcode.com/problems/remove-k-digits/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * 402. Remove K Digits
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given string num representing a non-negative integer num, and an integer k, return the smallest possible integer after removing k digits from num.
 *
 *
 *
 * Example 1:
 *
 * Input: num = "1432219", k = 3
 * Output: "1219"
 * Explanation: Remove the three digits 4, 3, and 2 to form the new number 1219 which is the smallest.
 * Example 2:
 *
 * Input: num = "10200", k = 1
 * Output: "200"
 * Explanation: Remove the leading 1 and the number is 200. Note that the output must not contain leading zeroes.
 * Example 3:
 *
 * Input: num = "10", k = 2
 * Output: "0"
 * Explanation: Remove all the digits from the number and it is left with nothing which is 0.
 *
 *
 * Constraints:
 *
 * 1 <= k <= num.length <= 105
 * num consists of only digits.
 * num does not have any leading zeros except for the zero itself.
 *
 */
public class RemoveKDigits {

    // V0
//    public String removeKdigits(String num, int k) {
//
//    }


    // V0-1
    // IDEA: MONO STACK (gemini)
    /**  IDEA:
     *
     * To solve **LC 402 (Remove K Digits)**,
     * the goal is to make the number as small as possible.
     * To achieve this, we want the digits at the beginning
     * (the most significant positions) to be as small as possible.
     *
     * ### 💡 The Greedy Strategy
     *
     * We use a **Monotonic Stack** approach:
     *
     * 1. Iterate through the digits. If the current digit is **smaller** than the previous digit (the top of the stack), we should remove the previous digit because replacing a large digit with a smaller one at a higher power of ten always results in a smaller number.
     * 2. We repeat this "pop" process as long as .
     * 3. **Edge Case 1:** If we finish the loop and still need to remove digits (), we remove them from the end (since the stack is now in non-decreasing order).
     * 4. **Edge Case 2:** Remove leading zeros from the final result.
     * 5. **Edge Case 3:** If the result is empty, return `"0"`.
     *
     *
     * ---
     *
     * ### 🔍 Dry Run Example
     *
     * **Input:** `num = "1432219"`, `k = 3`
     *
     * 1. Push `1`: `[1]`
     * 2. Push `4`: `[1, 4]`
     * 3. `3` < `4`: Pop `4`, Push `3`, `k=2`. Stack: `[1, 3]`
     * 4. `2` < `3`: Pop `3`, Push `2`, `k=1`. Stack: `[1, 2]`
     * 5. Push `2`: `[1, 2, 2]`
     * 6. `1` < `2`: Pop `2`, Push `1`, `k=0`. Stack: `[1, 2, 1]`
     * 7. `k` is now 0. Push `9`: `[1, 2, 1, 9]`
     * 8. **Result:** `"1219"`
     *
     * ---
     *
     * ### 📊 Complexity
     *
     * * **Time Complexity:** . Each digit is pushed and popped from the stack at most once.
     * * **Space Complexity:**  to store the stack/StringBuilder.
     *
     * ### 💡 Why `ArrayDeque`?
     *
     * In Java, `Stack<Character>` is synchronized and slow. `ArrayDeque` is the modern, faster alternative for stack operations. Using `removeFirst()` also allows us to build the string in the correct order without needing to `reverse()` it.
     *
     */
    public String removeKdigits_0_1(String num, int k) {
        int n = num.length();
        if (k == n)
            return "0";

        // We use a Deque as a stack for efficient iteration and removal
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            char digit = num.charAt(i);

            // While we still need to remove digits and the current digit
            // is smaller than the top of the stack, pop the stack.
            while (k > 0 && !stack.isEmpty() && stack.peekLast() > digit) {
                stack.removeLast();
                k--;
            }
            stack.addLast(digit);
        }

        // If k is still > 0, remove digits from the tail
        while (k > 0) {
            stack.removeLast();
            k--;
        }

        // Build the string and remove leading zeros
        StringBuilder sb = new StringBuilder();
        boolean leadingZero = true;
        while (!stack.isEmpty()) {
            char c = stack.removeFirst();
            if (leadingZero && c == '0')
                continue;
            leadingZero = false;
            sb.append(c);
        }

        return sb.length() == 0 ? "0" : sb.toString();
    }


    // V0-2
    // IDEA: MONO STACK (GPT)
    public String removeKdigits_0_2(String num, int k) {
        if (k == num.length())
            return "0"; // remove all digits

        Deque<Character> stack = new ArrayDeque<>();

        for (char c : num.toCharArray()) {
            // While we still can remove digits (k > 0) and the last digit in stack
            // is greater than the current digit, pop it (to make number smaller).
            while (k > 0 && !stack.isEmpty() && stack.peekLast() > c) {
                stack.pollLast();
                k--;
            }
            stack.addLast(c);
        }

        // If k > 0, remove from the end (largest remaining)
        while (k > 0 && !stack.isEmpty()) {
            stack.pollLast();
            k--;
        }

        // Build result while removing leading zeros
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            char ch = stack.pollFirst();
            // skip leading zeros
            if (sb.length() == 0 && ch == '0')
                continue;
            sb.append(ch);
        }

        // If empty, result is "0"
        return sb.length() == 0 ? "0" : sb.toString();
    }



    // V1
    // IDEA: STACK
    // https://leetcode.com/problems/remove-k-digits/solutions/88708/straightforward-java-solution-using-stac-2ny3/
    public String removeKdigits_1(String num, int k) {
        int len = num.length();
        //corner case
        if (k == len)
            return "0";

        Stack<Character> stack = new Stack<>();
        int i = 0;
        while (i < num.length()) {
            //whenever meet a digit which is less than the previous digit, discard the previous one
            while (k > 0 && !stack.isEmpty() && stack.peek() > num.charAt(i)) {
                stack.pop();
                k--;
            }
            stack.push(num.charAt(i));
            i++;
        }

        // corner case like "1111"
        while (k > 0) {
            stack.pop();
            k--;
        }

        //construct the number from the stack
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty())
            sb.append(stack.pop());
        sb.reverse();

        //remove all the 0 at the head
        while (sb.length() > 1 && sb.charAt(0) == '0')
            sb.deleteCharAt(0);
        return sb.toString();
    }



    // V2
    // IDEA: STACK
    // https://leetcode.com/problems/remove-k-digits/solutions/5005706/faster-lesserdetailed-explainationstackg-ve6y/
    public String removeKdigits_2(String num, int k) {
        Stack<Character> stack = new Stack<>();

        for (char digit : num.toCharArray()) {
            while (!stack.isEmpty() && k > 0 && stack.peek() > digit) {
                stack.pop();
                k--;
            }
            stack.push(digit);
        }

        // Remove remaining k digits from the end of the stack
        while (k > 0 && !stack.isEmpty()) {
            stack.pop();
            k--;
        }

        // Construct the resulting string from the stack
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        sb.reverse(); // Reverse to get the correct order

        // Remove leading zeros
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }

        // Handle edge case where result might be empty
        return sb.length() > 0 ? sb.toString() : "0";
    }




}
