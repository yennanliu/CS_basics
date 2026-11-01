package LeetCodeJava.Greedy;

// https://leetcode.com/problems/smallest-string-with-a-given-numeric-value/description/

/**
 * 1663. Smallest String With A Given Numeric Value
 * Medium
 *
 * The numeric value of a lowercase character is defined as its position
 * (1-indexed) in the alphabet, so the numeric value of a is 1, the numeric value of
 * b is 2, the numeric value of c is 3, and so on.
 *
 * The numeric value of a string consisting of lowercase characters is defined as
 * the sum of its characters' numeric values. For example, the numeric value of the
 * string "abe" is equal to 1 + 2 + 5 = 8.
 *
 * You are given two integers n and k. Return the lexicographically smallest string
 * with length equal to n and numeric value equal to k.
 *
 * Note that a string x is lexicographically smaller than string y if x comes before
 * y in dictionary order, that is, either x is a prefix of y, or if i is the first
 * position such that x[i] != y[i], then x[i] comes before y[i] in alphabetic order.
 *
 *
 * Example 1:
 *
 * Input: n = 3, k = 27
 * Output: "aay"
 *
 * Example 2:
 *
 * Input: n = 5, k = 73
 * Output: "aaszz"
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * n <= k <= 26 * n
 *
 */
public class SmallestStringWithAGivenNumericValue {

    // V0
    // IDEA: GREEDY FROM THE BACK (KEEP THE PREFIX AS SMALL AS POSSIBLE)
    //       start from "aa...a" (value n); the surplus k - n is pushed into the
    //       LAST positions, each taking up to 25 extra ('z'), from right to left.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String getSmallestString(int n, int k) {
        char[] result = new char[n];
        for (int i = 0; i < n; i++) {
            result[i] = 'a';
        }

        int surplus = k - n;
        int index = n - 1;
        // a full 'z' absorbs 25 extra
        while (surplus > 25) {
            result[index] = 'z';
            surplus -= 25;
            index--;
        }
        // the remainder fits into a single slot
        if (surplus > 0) {
            result[index] = (char) ('a' + surplus);
        }
        return new String(result);
    }
}
