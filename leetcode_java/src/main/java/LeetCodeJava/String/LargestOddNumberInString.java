package LeetCodeJava.String;

// https://leetcode.com/problems/largest-odd-number-in-string/description/

/**
 * 1903. Largest Odd Number in String
 * Easy
 *
 * You are given a string num, representing a large integer. Return the largest-valued odd integer (as a string) that is a non-empty substring of num, or an empty string "" if no odd integer exists.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: num = "52"
 * Output: "5"
 * Explanation: The only non-empty substrings are "5", "2", and "52". "5" is the only odd number.
 *
 * Example 2:
 *
 * Input: num = "4206"
 * Output: ""
 * Explanation: There are no odd numbers in "4206".
 *
 * Example 3:
 *
 * Input: num = "35427"
 * Output: "35427"
 * Explanation: "35427" is already an odd number.
 *
 *
 * Constraints:
 *
 * 1 <= num.length <= 10^5
 * num only consists of digits and does not contain any leading zeros.
 *
 */
public class LargestOddNumberInString {

    // V0
    // IDEA: GREEDY (KEEP THE WHOLE PREFIX UP TO THE LAST ODD DIGIT)
    //       a number is odd iff its last digit is odd, and with no leading zeros a
    //       longer prefix is always bigger -> the prefix ending at the rightmost
    //       odd digit wins.
    /**
     * time = O(N)
     * space = O(1)   (besides the returned string)
     */
    public String largestOddNumber(String num) {
        for (int i = num.length() - 1; i >= 0; i--) {
            int digit = num.charAt(i) - '0';
            if (digit % 2 == 1) {
                return num.substring(0, i + 1);
            }
        }
        return "";
    }
}
