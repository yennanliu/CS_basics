package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/palindrome-partitioning-iv/description/
/**
 * 1745. Palindrome Partitioning IV
 * Hard
 *
 * Given a string s, return true if it is possible to split the string s into three
 * non-empty palindromic substrings. Otherwise, return false.
 *
 * A string is said to be palindrome if it the same string when reversed.
 *
 *
 * Example 1:
 *
 * Input: s = "abcbdd"
 * Output: true
 * Explanation: "abcbdd" = "a" + "bcb" + "dd", and all three substrings are palindromes.
 *
 * Example 2:
 *
 * Input: s = "bcbddxy"
 * Output: false
 * Explanation: s cannot be split into 3 palindromes.
 *
 *
 * Constraints:
 *
 * 3 <= s.length <= 2000
 * s consists only of lowercase English letters.
 *
 */
public class PalindromePartitioningIV {

    // V0
    // IDEA: INTERVAL DP PALINDROME TABLE + ENUMERATE THE TWO CUTS
    /**
     *  DP def:
     *     - isPal[i][j] = true if s[i..j] is a palindrome
     *
     *  DP eq:
     *     - isPal[i][j] = (s[i] == s[j]) && (j - i < 2 || isPal[i + 1][j - 1])
     *
     *  isPal[i][j] reads the SHORTER interval isPal[i + 1][j - 1], so i sweeps
     *  from n - 1 down to 0 and j upward.
     *
     *  Then a 3-way split is fixed by two cuts first < second:
     *     s[0..first], s[first+1..second], s[second+1..n-1]
     *
     *  time = O(N^2)
     *  space = O(N^2)
     */
    public boolean checkPartitioning(String s) {
        int n = s.length();

        // build the palindrome table
        boolean[][] isPal = new boolean[n][n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (s.charAt(i) != s.charAt(j)) {
                    continue;
                }
                // length 1 or 2 with equal ends, or equal ends around a palindrome
                if (j - i < 2 || isPal[i + 1][j - 1]) {
                    isPal[i][j] = true;
                }
            }
        }

        // enumerate the end of the first part, then the end of the second part
        for (int first = 0; first <= n - 3; first++) {
            // prune: the first part must already be a palindrome
            if (!isPal[0][first]) {
                continue;
            }
            for (int second = first + 1; second <= n - 2; second++) {
                if (isPal[first + 1][second] && isPal[second + 1][n - 1]) {
                    return true;
                }
            }
        }
        return false;
    }
}
