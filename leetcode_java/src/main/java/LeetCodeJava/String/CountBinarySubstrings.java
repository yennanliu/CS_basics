package LeetCodeJava.String;

// https://leetcode.com/problems/count-binary-substrings/

/**
 *  696. Count Binary Substrings
 *  Easy
 *
 *  Given a binary string s, return the number of non-empty substrings that have
 *  the same number of 0's and 1's, and all the 0's and all the 1's in these
 *  substrings are grouped consecutively.
 *  Substrings that occur multiple times are counted the number of times they occur.
 *
 *  Example 1:
 *    Input:  s = "00110011"
 *    Output: 6
 *    ("0011", "01", "1100", "10", "0011", "01")
 *
 *  Example 2:
 *    Input:  s = "10101"
 *    Output: 4
 *
 *  Constraints:
 *    1 <= s.length <= 10^5
 *    s[i] is either '0' or '1'.
 */
public class CountBinarySubstrings {

    // V0
    // IDEA: group consecutive equal chars; adjacent groups (a, b) contribute min(a, b)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int countBinarySubstrings(String s) {
        if (s == null || s.length() <= 1) {
            return 0;
        }
        int prev = 0;
        int cur = 1;
        int res = 0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                cur++;
            } else {
                res += Math.min(prev, cur);
                prev = cur;
                cur = 1;
            }
        }
        return res + Math.min(prev, cur);
    }
}
