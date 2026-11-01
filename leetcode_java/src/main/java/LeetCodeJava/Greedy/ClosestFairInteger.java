package LeetCodeJava.Greedy;

// https://leetcode.com/problems/closest-fair-integer/description/

import java.util.*;

/**
 * 2417. Closest Fair Integer
 * Medium
 *
 *
 * You are given a positive integer n.
 *
 * We call an integer k fair if the number of even digits in k is equal to the number of odd digits in it.
 *
 * Return the smallest fair integer that is greater than or equal to n.
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 10
 * Explanation: The smallest fair integer that is greater than or equal to 2 is 10.
 * 10 is fair because it has an equal number of even and odd digits (one even digit and one odd digit).
 *
 * Example 2:
 *
 * Input: n = 403
 * Output: 1001
 * Explanation: The smallest fair integer that is greater than or equal to 403 is 1001.
 * 1001 is fair because it has an equal number of even and odd digits (two even digits and two odd digits).
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 *
 */
public class ClosestFairInteger {

    // V0
    // IDEA: SKIP ODD DIGIT COUNTS, THEN INCREMENT
    //       a fair number has an even digit count. With an odd count of 2k - 1 digits,
    //       jump to the smallest fair number with 2k digits: '1' + k zeros + (k - 1) ones.
    //       Within an even length a fair number is only a few steps away, so increment.
    /**
     * time = O(gap * D), D = number of digits
     * space = O(D)
     */
    public int closestFair(int n) {
        long current = n;
        while (true) {
            String digits = Long.toString(current);
            int length = digits.length();

            if (length % 2 == 1) {
                // smallest fair number one digit longer
                int k = (length + 1) / 2;
                StringBuilder smallest = new StringBuilder("1");
                for (int i = 0; i < k; i++) {
                    smallest.append('0');
                }
                for (int i = 0; i < k - 1; i++) {
                    smallest.append('1');
                }
                current = Long.parseLong(smallest.toString());
                continue;
            }

            int evenCount = 0;
            for (char c : digits.toCharArray()) {
                if ((c - '0') % 2 == 0) {
                    evenCount++;
                }
            }
            if (evenCount * 2 == length) {
                return (int) current;
            }
            current++;
        }
    }
}
