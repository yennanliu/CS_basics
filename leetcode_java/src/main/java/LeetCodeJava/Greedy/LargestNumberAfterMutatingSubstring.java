package LeetCodeJava.Greedy;

// https://leetcode.com/problems/largest-number-after-mutating-substring/description/

/**
 * 1946. Largest Number After Mutating Substring
 * Medium
 *
 * You are given a string num, which represents a large integer. You are also given a 0-indexed integer array
 * change of length 10 that maps each digit 0-9 to another digit. More formally, digit d maps to digit change[d].
 *
 * You may choose to mutate a single substring of num. To mutate a substring, replace each digit num[i] with
 * the digit it maps to in change (i.e. replace num[i] with change[num[i]]).
 *
 * Return a string representing the largest possible integer after mutating (or choosing not to) a single
 * substring of num.
 *
 * A substring is a contiguous sequence of characters within the string.
 *
 *
 * Example 1:
 *
 * Input: num = "132", change = [9,8,5,0,3,6,4,2,6,8]
 * Output: "832"
 * Explanation: Replace the substring "1":
 * - 1 maps to change[1] = 8.
 * Thus, "132" becomes "832".
 *
 * Example 2:
 *
 * Input: num = "021", change = [9,4,3,5,7,2,1,9,0,6]
 * Output: "934"
 *
 * Example 3:
 *
 * Input: num = "5", change = [1,4,7,5,3,2,5,6,9,4]
 * Output: "5"
 *
 *
 * Constraints:
 *
 * 1 <= num.length <= 10^5
 * num consists of only digits 0-9.
 * change.length == 10
 * 0 <= change[d] <= 9
 *
 */
public class LargestNumberAfterMutatingSubstring {

    // V0
    // IDEA: GREEDY - START AT THE FIRST IMPROVABLE DIGIT, STOP AT THE FIRST LOSS
    /**
     * time = O(N)
     * space = O(N)
     */
    public String maximumNumber(String num, int[] change) {
        char[] digits = num.toCharArray();
        boolean started = false;

        for (int i = 0; i < digits.length; i++) {
            int current = digits[i] - '0';
            int mapped = change[current];

            // once the mutation has started, a smaller mapped digit ends it:
            // the substring must be contiguous, and shrinking this digit would lower the number
            if (started && mapped < current) {
                break;
            }

            // the length never changes, so a bigger digit at the earliest position wins;
            // an equal mapping is free and lets the substring reach further improvements
            if (mapped > current) {
                started = true;
                digits[i] = (char) ('0' + mapped);
            }
        }
        return new String(digits);
    }
}
