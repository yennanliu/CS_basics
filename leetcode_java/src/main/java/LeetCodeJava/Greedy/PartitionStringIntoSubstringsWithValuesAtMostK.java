package LeetCodeJava.Greedy;

// https://leetcode.com/problems/partition-string-into-substrings-with-values-at-most-k/description/

import java.util.*;

/**
 * 2522. Partition String Into Substrings With Values at Most K
 * Medium
 *
 * You are given a string s consisting of digits from 1 to 9 and an integer k.
 *
 * A partition of a string s is called good if:
 *
 * Each digit of s is part of exactly one substring.
 * The value of each substring is less than or equal to k.
 *
 * Return the minimum number of substrings in a good partition of s. If no good
 * partition of s exists, return -1.
 *
 * Note that:
 *
 * The value of a string is its result when interpreted as an integer. For example,
 * the value of "123" is 123 and the value of "1" is 1.
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "165462", k = 60
 * Output: 4
 * Explanation: We can partition the string into substrings "16", "54", "6", and "2".
 * Each substring has a value less than or equal to k = 60.
 * It can be shown that we cannot partition the string into less than 4 substrings.
 *
 * Example 2:
 *
 * Input: s = "238182", k = 5
 * Output: -1
 * Explanation: There is no good partition for this string.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s[i] is a digit from '1' to '9'.
 * 1 <= k <= 10^9
 *
 */
public class PartitionStringIntoSubstringsWithValuesAtMostK {

    // V0
    // IDEA: GREEDY (EXTEND THE CURRENT PIECE AS FAR AS IT CAN LEGALLY GO)
    //       a longer piece never forces more pieces later (moving a cut right only shrinks
    //       the next piece). A single digit > k makes the answer -1.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumPartition(String s, int k) {
        int pieces = 1;
        // value of the piece being built; long because current * 10 can pass int range
        long current = 0;
        for (char c : s.toCharArray()) {
            int digit = c - '0';
            if (digit > k) {
                // this digit cannot sit in any piece
                return -1;
            }
            if (current * 10 + digit <= k) {
                current = current * 10 + digit;
            } else {
                // close the current piece and open a new one at this digit
                pieces++;
                current = digit;
            }
        }
        return pieces;
    }
}
