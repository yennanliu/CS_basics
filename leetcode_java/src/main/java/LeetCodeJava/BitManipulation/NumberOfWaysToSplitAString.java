package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-ways-to-split-a-string/

/**
 *  1573. Number of Ways to Split a String
 *  Medium
 *
 *  Given a binary string s, you can split s into 3 non-empty strings s1, s2 and
 *  s3 where s1 + s2 + s3 = s.
 *
 *  Return the number of ways s can be split such that the number of ones is the
 *  same in s1, s2, and s3. Since the answer may be too large, return it modulo
 *  10^9 + 7.
 *
 *  Example 1:
 *  Input: s = "10101"
 *  Output: 4
 *  Explanation: "1|010|1", "1|01|01", "10|10|1", "10|1|01"
 *
 *  Example 2:
 *  Input: s = "1001"
 *  Output: 0
 *
 *  Example 3:
 *  Input: s = "0000"
 *  Output: 3
 *  Explanation: "0|0|00", "0|00|0", "00|0|0"
 *
 *  Constraints:
 *  3 <= s.length <= 10^5
 *  s[i] is either '0' or '1'.
 */
public class NumberOfWaysToSplitAString {

    // V0
    // IDEA: math / counting.
    //   - if total ones == 0 -> choose 2 cut points among n-1 -> C(n-1, 2)
    //   - if total ones % 3 != 0 -> 0
    //   - otherwise each part must hold k = total/3 ones; the first cut can float
    //     over the zero-gap between the k-th and (k+1)-th one, the second cut
    //     over the gap between the 2k-th and (2k+1)-th one -> product of gaps
    /**
     * time = O(n)
     * space = O(n)  (positions of the ones)
     */
    public int numWays(String s) {
        final long MOD = 1_000_000_007L;
        int n = s.length();

        int total = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '1') {
                total++;
            }
        }

        if (total == 0) {
            // C(n-1, 2)
            long ways = ((long) (n - 1) * (n - 2) / 2) % MOD;
            return (int) ways;
        }
        if (total % 3 != 0) {
            return 0;
        }

        int k = total / 3;
        int[] ones = new int[total];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '1') {
                ones[idx++] = i;
            }
        }

        long gap1 = ones[k] - ones[k - 1];
        long gap2 = ones[2 * k] - ones[2 * k - 1];
        return (int) ((gap1 % MOD) * (gap2 % MOD) % MOD);
    }
}
