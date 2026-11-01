package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-difference-by-remapping-a-digit/description/

/**
 * 2566. Maximum Difference by Remapping a Digit
 * Easy
 *
 * You are given an integer num. You know that Bob will sneakily remap one of the 10 possible digits (0 to 9) to another digit.
 *
 * Return the difference between the maximum and minimum values Bob can make by remapping exactly one digit in num.
 *
 * Notes:
 *
 * When Bob remaps a digit d1 to another digit d2, Bob replaces all occurrences of d1 in num with d2.
 * Bob can remap a digit to itself, in which case num does not change.
 * Bob can remap different digits for obtaining minimum and maximum values respectively.
 * The resulting number after remapping can contain leading zeroes.
 *
 *
 * Example 1:
 *
 * Input: num = 11891
 * Output: 99009
 * Explanation:
 * To achieve the maximum value, Bob can remap the digit 1 to the digit 9 to yield 99899.
 * To achieve the minimum value, Bob can remap the digit 1 to the digit 0, yielding 890.
 * The difference between these two numbers is 99009.
 *
 * Example 2:
 *
 * Input: num = 90
 * Output: 99
 * Explanation:
 * The maximum value that can be returned by the function is 99 (if 0 is replaced by 9) and the minimum value that can be returned by the function is 0 (if 9 is replaced by 0).
 * Thus, we return 99.
 *
 *
 * Constraints:
 *
 * 1 <= num <= 10^8
 *
 */
public class MaximumDifferenceByRemappingADigit {

    // V0
    // IDEA: GREEDY ON THE MOST SIGNIFICANT DIGIT
    //       min: remap the leading digit to '0'.
    //       max: remap the first digit that is not '9' to '9' (all 9s -> no change).
    /**
     * time = O(log(num))
     * space = O(log(num))
     */
    public int minMaxDifference(int num) {
        String digits = String.valueOf(num);

        // minimum: every copy of the leading digit becomes 0 (leading zeroes are allowed)
        char leading = digits.charAt(0);
        int minValue = Integer.parseInt(digits.replace(leading, '0'));

        // maximum: the leftmost digit that can still grow is turned into 9 everywhere
        int maxValue = num;
        for (int i = 0; i < digits.length(); i++) {
            char c = digits.charAt(i);
            if (c != '9') {
                maxValue = Integer.parseInt(digits.replace(c, '9'));
                break;
            }
        }
        return maxValue - minValue;
    }
}
