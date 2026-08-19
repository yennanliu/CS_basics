package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/split-a-string-into-the-max-number-of-unique-substrings/

import java.util.HashSet;
import java.util.Set;

/**
 *  1593. Split a String Into the Max Number of Unique Substrings
 *  Medium
 *
 *  Given a string s, return the maximum number of unique substrings that the given
 *  string can be split into.
 *
 *  You can split string s into any list of non-empty substrings, where the
 *  concatenation of the substrings forms the original string. However, you must
 *  split the substrings such that all of them are unique.
 *
 *  A substring is a contiguous sequence of characters within a string.
 *
 *  Example 1:
 *    Input: s = "ababccc"
 *    Output: 5
 *    Explanation: One way to split maximally is ['a', 'b', 'ab', 'c', 'cc'].
 *
 *  Example 2:
 *    Input: s = "aba"
 *    Output: 2
 *    Explanation: One way to split maximally is ['a', 'ba'].
 *
 *  Constraints:
 *    1 <= s.length <= 16
 *    s contains only lower case English letters.
 */
public class SplitAStringIntoTheMaxNumberOfUniqueSubstrings {

    private String s;
    private int n;
    private Set<String> used;
    private int best;

    // V0
    // IDEA: BACKTRACKING (n <= 16, try every cut with a "used pieces" set)
    //       at position i try every next cut j, and if s[i, j) has not been used yet
    //       take it and recurse.
    //       NOTE : prune with "current count + remaining characters <= best", since
    //              every remaining char can add at most one more piece.
    /**
     * time = O(2^n * n)
     * space = O(n^2)
     */
    public int maxUniqueSplit(String s) {
        this.s = s;
        this.n = s.length();
        this.used = new HashSet<>();
        this.best = 0;
        dfs(0, 0);
        return best;
    }

    private void dfs(int i, int cnt) {
        if (cnt + (n - i) <= best) {
            return;
        }
        if (i == n) {
            best = cnt;
            return;
        }
        for (int j = i + 1; j <= n; j++) {
            String piece = s.substring(i, j);
            if (used.contains(piece)) {
                continue;
            }
            used.add(piece);
            dfs(j, cnt + 1);
            used.remove(piece);
        }
    }
}
