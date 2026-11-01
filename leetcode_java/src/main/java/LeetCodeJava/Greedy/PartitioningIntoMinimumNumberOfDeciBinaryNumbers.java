package LeetCodeJava.Greedy;

// https://leetcode.com/problems/partitioning-into-minimum-number-of-deci-binary-numbers/description/

/**
 * 1689. Partitioning Into Minimum Number Of Deci-Binary Numbers
 * Medium
 *
 * A decimal number is called deci-binary if each of its digits is either 0 or 1
 * without any leading zeros. For example, 101 and 1100 are deci-binary, while 112
 * and 3001 are not.
 *
 * Given a string n that represents a positive decimal integer, return the minimum
 * number of positive deci-binary numbers needed so that they sum up to n.
 *
 *
 * Example 1:
 *
 * Input: n = "32"
 * Output: 3
 * Explanation: 10 + 11 + 11 = 32
 *
 * Example 2:
 *
 * Input: n = "82734"
 * Output: 8
 *
 * Example 3:
 *
 * Input: n = "27346209830709182346"
 * Output: 9
 *
 *
 * Constraints:
 *
 * 1 <= n.length <= 10^5
 * n consists of only digits.
 * n does not contain any leading zeros and represents a positive integer.
 *
 */
public class PartitioningIntoMinimumNumberOfDeciBinaryNumbers {

    // V0
    // IDEA: GREEDY / MAX DIGIT
    //       each deci-binary summand adds at most 1 to a column and nothing ever
    //       carries, so a column with digit d needs at least d summands; with
    //       max(digit) summands every column can be filled exactly.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minPartitions(String n) {
        int maxDigit = 0;
        for (int i = 0; i < n.length(); i++) {
            int digit = n.charAt(i) - '0';
            maxDigit = Math.max(maxDigit, digit);
            // 9 is the largest possible answer, no need to read further
            if (maxDigit == 9) {
                break;
            }
        }
        return maxDigit;
    }
}
