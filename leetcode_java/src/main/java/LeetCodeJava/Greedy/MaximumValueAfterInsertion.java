package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-value-after-insertion/description/

/**
 * 1881. Maximum Value after Insertion
 * Medium
 *
 * You are given a very large integer n, represented as a string, and an integer
 * digit x. The digits in n and the digit x are in the inclusive range [1, 9],
 * and n may represent a negative number.
 *
 * You want to maximize n's numerical value by inserting x anywhere in the
 * decimal representation of n. You cannot insert x to the left of the negative sign.
 *
 * For example, if n = 73 and x = 6, it would be best to insert it between 7 and
 * 3, making n = 763.
 * If n = -55 and x = 2, it would be best to insert it before the first 5, making
 * n = -255.
 *
 * Return a string representing the maximum value of n after the insertion.
 *
 *
 * Example 1:
 *
 * Input: n = "99", x = 9
 * Output: "999"
 * Explanation: The result is the same regardless of where you insert 9.
 *
 * Example 2:
 *
 * Input: n = "-13", x = 2
 * Output: "-123"
 * Explanation: You can make n one of {-213, -123, -132}, and the largest of those three is -123.
 *
 *
 * Constraints:
 *
 * 1 <= n.length <= 10^5
 * 1 <= x <= 9
 * The digits in n are in the range [1, 9].
 * n is a valid representation of an integer.
 * In the case of a negative n, it will begin with '-'.
 *
 */
public class MaximumValueAfterInsertion {

    // V0
    // IDEA: GREEDY (insert at the FIRST position where x improves the digit there)
    /**
     * time = O(N)
     * space = O(N) for the result string
     */
    public String maxValue(String n, int x) {
        boolean negative = n.charAt(0) == '-';
        // never insert to the left of the sign
        int position = negative ? 1 : 0;

        while (position < n.length()) {
            int digit = n.charAt(position) - '0';
            if (negative) {
                // negative: smallest magnitude wins -> go before the first digit bigger than x
                if (digit > x) {
                    break;
                }
            } else {
                // positive: largest magnitude wins -> go before the first digit smaller than x
                if (digit < x) {
                    break;
                }
            }
            position++;
        }

        // no such digit -> position == n.length() and x is appended
        StringBuilder result = new StringBuilder(n.length() + 1);
        result.append(n, 0, position);
        result.append(x);
        result.append(n, position, n.length());
        return result.toString();
    }
}
