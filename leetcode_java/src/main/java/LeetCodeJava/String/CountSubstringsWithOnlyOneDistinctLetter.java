package LeetCodeJava.String;

// https://leetcode.com/problems/count-substrings-with-only-one-distinct-letter/

/**
 *  1180. Count Substrings with Only One Distinct Letter
 *  Easy
 *
 *  Given a string s, return the number of substrings that have only one distinct letter.
 *
 *  Example 1:
 *    Input: s = "aaaba"
 *    Output: 8
 *    Explanation: The substrings with one distinct letter are "aaa", "aa", "a", "b".
 *                 "aaa" occurs 1 time.
 *                 "aa" occurs 2 times.
 *                 "a" occurs 4 times.
 *                 "b" occurs 1 time.
 *                 So the answer is 1 + 2 + 4 + 1 = 8.
 *
 *  Example 2:
 *    Input: s = "aaaaaaaaaa"
 *    Output: 55
 *
 *  Constraints:
 *    1 <= s.length <= 1000
 *    s[i] consists of only lowercase English letters.
 */
public class CountSubstringsWithOnlyOneDistinctLetter {

    // V0
    // IDEA: TWO POINTERS (group the string into equal-letter RUNS)
    //       every "single distinct letter" substring lives entirely inside one run,
    //       and a run of length L contributes L * (L + 1) / 2 substrings
    //       (1 of length L, 2 of length L-1, ... L of length 1).
    //       so : find each maximal run, add its triangular number, jump past it.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countLetters(String s) {
        int n = s.length();
        int res = 0;
        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && s.charAt(j) == s.charAt(i)) {
                j++;
            }
            int len = j - i;
            res += len * (len + 1) / 2;
            i = j;   // NOTE !!! jump to the start of the next run
        }
        return res;
    }
}
