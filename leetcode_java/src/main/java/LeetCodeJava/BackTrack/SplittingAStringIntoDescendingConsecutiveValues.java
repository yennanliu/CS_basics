package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/splitting-a-string-into-descending-consecutive-values/

/**
 *  1849. Splitting a String Into Descending Consecutive Values
 *  Medium
 *
 *  You are given a string s that consists of only digits.
 *
 *  Check if we can split s into two or more non-empty substrings such that the
 *  numerical values of the substrings are in descending order and the difference
 *  between numerical values of every two adjacent substrings is equal to 1.
 *
 *  For example, the string s = "0090089" can be split into ["0090", "089"] with
 *  numerical values [90,89]. The values are in descending order and adjacent values
 *  differ by 1, so this way is valid.
 *
 *  Return true if it is possible to split s as described above, or false otherwise.
 *
 *  Example 1:
 *    Input: s = "1234"
 *    Output: false
 *
 *  Example 2:
 *    Input: s = "050043"
 *    Output: true
 *    Explanation: s can be split into ["05", "004", "3"] -> [5,4,3].
 *
 *  Example 3:
 *    Input: s = "9080701"
 *    Output: false
 *
 *  Constraints:
 *    1 <= s.length <= 20
 *    s only consists of digits.
 */
public class SplittingAStringIntoDescendingConsecutiveValues {

    private String s;
    private int n;

    // V0
    // IDEA: BACKTRACKING -- only the FIRST piece really branches
    //       dfs(i, prev) : can s[i:] be split so the next piece is prev - 1 and the
    //       chain keeps descending by 1?
    //         - i == len(s) -> the split closed cleanly -> true
    //         - otherwise grow a candidate y digit by digit from position i and
    //           recurse whenever y == prev - 1.
    //
    //       for the very first piece (prev = -1) every prefix is allowed, EXCEPT the
    //       whole string -- the split must have at least two parts (the `n - 1` cap).
    //
    //       NOTE : leading zeros are fine ("004" == 4), so we just accumulate
    //              y = y * 10 + digit without any special casing.
    //       NOTE : once prev is fixed there is at most ONE valid next value, so the
    //              search collapses to a linear walk after the first choice.
    //       NOTE : 20 digits overflow int -> use long, and stop growing y once it
    //              already exceeds prev (it can only get bigger).
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public boolean splitString(String s) {
        this.s = s;
        this.n = s.length();
        return dfs(0, -1L);
    }

    private boolean dfs(int i, long prev) {
        if (i == n) {
            return true;
        }
        long y = 0;
        int end = (prev < 0) ? n - 1 : n;
        for (int j = i; j < end; j++) {
            // guard : a piece that big can never be followed by (piece - 1)
            // inside the remaining <= 20 characters, and it would overflow long
            if (y > 100000000000000000L) {
                break;
            }
            y = y * 10 + (s.charAt(j) - '0');
            if (prev >= 0 && y >= prev) {
                break; // y only grows from here -> prev - y == 1 impossible
            }
            if (prev < 0 || prev - y == 1) {
                if (dfs(j + 1, y)) {
                    return true;
                }
            }
        }
        return false;
    }
}
