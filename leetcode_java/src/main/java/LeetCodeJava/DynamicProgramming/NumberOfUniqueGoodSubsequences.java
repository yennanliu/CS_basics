package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-unique-good-subsequences/description/
/**
 * 1987. Number of Unique Good Subsequences
 * Hard
 *
 * You are given a binary string binary. A subsequence of binary is considered good if it is
 * not empty and has no leading zeros (with the exception of "0").
 *
 * Find the number of unique good subsequences of binary.
 *
 * For example, if binary = "001", then all the good subsequences are ["0", "0", "1"], so the
 * unique good subsequences are "0" and "1". Note that subsequences "00", "01", and "001" are
 * not good because they have leading zeros.
 *
 * Return the number of unique good subsequences of binary. Since the answer may be very
 * large, return it modulo 10^9 + 7.
 *
 * A subsequence is a sequence that can be derived from another sequence by deleting some or
 * no elements without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: binary = "001"
 * Output: 2
 * Explanation: The unique good subsequences are "0" and "1".
 *
 * Example 2:
 *
 * Input: binary = "11"
 * Output: 2
 * Explanation: The unique good subsequences are "1" and "11".
 *
 * Example 3:
 *
 * Input: binary = "101"
 * Output: 5
 * Explanation: The unique good subsequences are "0", "1", "10", "11", and "101".
 *
 *
 * Constraints:
 *
 * 1 <= binary.length <= 10^5
 * binary consists of only '0's and '1's.
 *
 */
public class NumberOfUniqueGoodSubsequences {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP ON THE LAST CHARACTER (COUNT DISTINCT STRINGS, NOT POSITIONS)
    /**
     *  Every good subsequence except the lone "0" starts with '1'.
     *
     *  DP def:
     *     - endsWithOne  = distinct good subsequences (starting with '1') ending in '1'
     *     - endsWithZero = distinct good subsequences (starting with '1') ending in '0'
     *     - hasZero      = 1 once the standalone "0" is available
     *
     *  DP eq:
     *     - c == '0': endsWithZero = endsWithOne + endsWithZero   (append '0' to every string)
     *     - c == '1': endsWithOne  = endsWithOne + endsWithZero + 1  (append '1', plus "1")
     *
     *  NOTE !!! ASSIGNING (not adding to the old value) is what removes duplicates:
     *           appending c to every distinct string yields every distinct string ending in c.
     *
     *  Answer: endsWithOne + endsWithZero + hasZero
     *
     *  time = O(N)
     *  space = O(1)
     */
    public int numberOfUniqueGoodSubsequences(String binary) {
        long endsWithOne = 0;
        long endsWithZero = 0;
        int hasZero = 0;

        for (int i = 0; i < binary.length(); i++) {
            char c = binary.charAt(i);
            if (c == '0') {
                // every distinct string, extended by '0', is a distinct string ending in '0'
                endsWithZero = (endsWithOne + endsWithZero) % MOD;
                hasZero = 1;
            } else {
                // same for '1', plus the brand-new single "1"
                endsWithOne = (endsWithOne + endsWithZero + 1) % MOD;
            }
        }
        return (int) ((endsWithOne + endsWithZero + hasZero) % MOD);
    }
}
