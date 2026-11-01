package LeetCodeJava.Greedy;

// https://leetcode.com/problems/max-difference-you-can-get-from-changing-an-integer/description/

/**
 * 1432. Max Difference You Can Get From Changing an Integer
 * Medium
 *
 * You are given an integer num. You will apply the following steps to num two
 * separate times:
 *
 * - Pick a digit x (0 <= x <= 9).
 * - Pick another digit y (0 <= y <= 9). Note y can be equal to x.
 * - Replace all the occurrences of x in the decimal representation of num by y.
 *
 * Let a and b be the two results from applying the operation to num
 * independently.
 *
 * Return the max difference between a and b.
 *
 * Note that neither a nor b may have any leading zeros, and must not be 0.
 *
 *
 * Example 1:
 *
 * Input: num = 555
 * Output: 888
 * Explanation: The first time pick x = 5 and y = 9 and store the new integer in a.
 * The second time pick x = 5 and y = 1 and store the new integer in b.
 * We have now a = 999 and b = 111 and max difference = 888
 *
 * Example 2:
 *
 * Input: num = 9
 * Output: 8
 * Explanation: The first time pick x = 9 and y = 9 and store the new integer in a.
 * The second time pick x = 9 and y = 1 and store the new integer in b.
 * We have now a = 9 and b = 1 and max difference = 8
 *
 *
 * Constraints:
 *
 * 1 <= num <= 10^8
 *
 */
public class MaxDifferenceYouCanGetFromChangingAnInteger {

    // V0
    // IDEA: GREEDY (max: first non-9 digit -> 9; min: leading digit -> 1, else first other non-0 digit -> 0)
    /**
     * time = O(D)   D = number of digits
     * space = O(D)
     */
    public int maxDiff(int num) {
        String digits = String.valueOf(num);

        // largest: the leftmost digit that is not 9 gains the most when it becomes 9
        String largest = digits;
        for (int i = 0; i < digits.length(); i++) {
            char c = digits.charAt(i);
            if (c != '9') {
                largest = digits.replace(c, '9');
                break;
            }
        }

        // smallest: the leading digit cannot become 0 (leading zero), so make it 1;
        // if it already is 1, zero out the first later digit that is neither 0 nor 1
        String smallest = digits;
        char lead = digits.charAt(0);
        if (lead != '1') {
            smallest = digits.replace(lead, '1');
        } else {
            for (int i = 1; i < digits.length(); i++) {
                char c = digits.charAt(i);
                // '0' would be a no-op; the lead digit would create a leading zero
                if (c != '0' && c != lead) {
                    smallest = digits.replace(c, '0');
                    break;
                }
            }
        }

        return Integer.parseInt(largest) - Integer.parseInt(smallest);
    }
}
