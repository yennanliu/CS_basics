package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/smallest-substring-with-identical-characters-ii/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3399. Smallest Substring With Identical Characters II
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
 * 1 <= n == s.length <= 10^5
 * s consists only of '0' and '1'.
 * 0 <= numOps <= n
 *
 */
public class SmallestSubstringWithIdenticalCharacters2 {

    // V0
    // IDEA: BINARY SEARCH THE ANSWER m + GREEDY PER-RUN COST
    // a plan keeping every run <= m also keeps it <= m + 1, so feasibility is monotone in m.
    // m == 1 is special: the string must alternate, and the two alternating strings' mismatch
    //   counts sum to n -> cost min(mismatch, n - mismatch).
    // m >= 2: a maximal run of length L needs floor(L / (m + 1)) flips placed strictly inside
    //   it, so runs stay independent and the costs add. n <= 1e5 -> binary search m in [2, n].
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minLength(String s, int numOps) {
        int n = s.length();

        // m == 1 case: mismatches against "0101...", or against "1010..." (n - mismatch)
        int mismatch = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) - '0' != i % 2) {
                mismatch++;
            }
        }
        if (Math.min(mismatch, n - mismatch) <= numOps) {
            return 1;
        }

        // runs do not depend on m, so measure them once
        List<Integer> runs = new ArrayList<>();
        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && s.charAt(j) == s.charAt(i)) {
                j++;
            }
            runs.add(j - i);
            i = j;
        }

        int left = 2;
        int right = n;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canLimitRuns(runs, numOps, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Whether at most numOps flips can cut every run down to length <= m (m >= 2). */
    private boolean canLimitRuns(List<Integer> runs, int numOps, int m) {
        int flips = 0;
        for (int runLength : runs) {
            flips += runLength / (m + 1);
            if (flips > numOps) {
                return false;
            }
        }
        return true;
    }
}
