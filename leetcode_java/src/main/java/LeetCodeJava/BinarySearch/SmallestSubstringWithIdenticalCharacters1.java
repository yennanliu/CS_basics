package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/smallest-substring-with-identical-characters-i/description/

/**
 * 3398. Smallest Substring With Identical Characters I
 * Hard
 *
 * You are given a binary string s of length n and an integer numOps.
 *
 * You are allowed to perform the following operation on s at most numOps times:
 *
 * Select any index i (where 0 <= i < n) and flip s[i]. If s[i] == '1', change s[i]
 * to '0' and vice versa.
 *
 * You need to minimize the length of the longest substring of s such that all the
 * characters in the substring are identical.
 *
 * Return the minimum length after the operations.
 *
 * Example 1:
 *
 * Input: s = "000001", numOps = 1
 *
 * Output: 2
 *
 * Explanation:
 *
 * By changing s[2] to '1', s becomes "001001". The longest substrings with
 * identical characters are s[0..1] and s[3..4].
 *
 * Example 2:
 *
 * Input: s = "0000", numOps = 2
 *
 * Output: 1
 *
 * Explanation:
 *
 * By changing s[0] and s[2] to '1', s becomes "1010".
 *
 * Example 3:
 *
 * Input: s = "0101", numOps = 0
 *
 * Output: 1
 *
 * Constraints:
 *
 * 1 <= n == s.length <= 1000
 * s consists only of '0' and '1'.
 * 0 <= numOps <= n
 *
 */
public class SmallestSubstringWithIdenticalCharacters1 {

    // V0
    // IDEA: TRY EACH CANDIDATE LENGTH m IN INCREASING ORDER (n <= 1000)
    // m == 1: the whole string must alternate; the two alternating strings' mismatch counts
    //   add up to n, so the cost is min(mismatch, n - mismatch).
    // m >= 2: runs are independent; a maximal run of L equal chars needs floor(L / (m + 1))
    //   flips (placed strictly inside the run, so they never merge with a neighbour run).
    // the first m whose total cost <= numOps is the answer.
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int minLength(String s, int numOps) {
        int n = s.length();
        if (alternatingCost(s) <= numOps) {
            return 1;
        }
        for (int m = 2; m <= n; m++) {
            if (flipsForMaxRun(s, m) <= numOps) {
                return m;
            }
        }
        return n;
    }

    /** Minimum flips to make s fully alternating. */
    private int alternatingCost(String s) {
        int n = s.length();
        // mismatches against "0101..."
        int mismatch = 0;
        for (int i = 0; i < n; i++) {
            int expected = i % 2;
            if (s.charAt(i) - '0' != expected) {
                mismatch++;
            }
        }
        return Math.min(mismatch, n - mismatch);
    }

    /** Flips needed so that no run is longer than m (valid for m >= 2). */
    private int flipsForMaxRun(String s, int m) {
        int n = s.length();
        int flips = 0;
        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && s.charAt(j) == s.charAt(i)) {
                j++;
            }
            flips += (j - i) / (m + 1);
            i = j;
        }
        return flips;
    }
}
