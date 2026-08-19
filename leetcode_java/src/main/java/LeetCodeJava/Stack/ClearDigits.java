package LeetCodeJava.Stack;

// https://leetcode.com/problems/clear-digits/

/**
 *  3174. Clear Digits
 *  Easy
 *
 *  You are given a string s.
 *
 *  Your task is to remove all digits by doing this operation repeatedly:
 *  delete the first digit and the closest non-digit character to its left.
 *
 *  Return the resulting string after removing all digits.
 *
 *  Example 1:
 *    Input: s = "abc"
 *    Output: "abc"
 *    Explanation: There is no digit in the string.
 *
 *  Example 2:
 *    Input: s = "cb34"
 *    Output: ""
 *    Explanation: apply on s[2] -> "c4", then on s[1] -> "".
 *
 *  Constraints:
 *    1 <= s.length <= 100
 *    s consists only of lowercase English letters and digits.
 *    The input is generated such that it is possible to delete all digits.
 */
public class ClearDigits {

    // V0
    // IDEA: STACK - A DIGIT ALWAYS CANCELS WHATEVER IS ON TOP
    //       "the closest non-digit to its left" is exactly the most recently
    //       kept character, because every earlier digit already consumed its
    //       own partner. So push letters and pop on each digit.
    //       Input is guaranteed solvable -> the stack is never empty at a pop.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String clearDigits(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isDigit(ch)) {
                sb.deleteCharAt(sb.length() - 1);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
