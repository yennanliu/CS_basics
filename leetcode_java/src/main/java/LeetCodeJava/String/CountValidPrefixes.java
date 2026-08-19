package LeetCodeJava.String;

// https://leetcode.com/problems/count-valid-prefixes/

/**
 *  4006. Count Valid Prefixes
 *  Easy
 *
 *  You are given a binary string s.
 *
 *  A prefix of s is considered valid if its characters can be rearranged
 *  to form an alternating string.
 *
 *  Return the number of valid prefixes of s.
 *
 *  A string is considered alternating if no two adjacent characters are equal.
 *
 *  Example 1:
 *
 *  Input: s = "00101"
 *  Output: 3
 *  Explanation: the valid prefixes are "0", "001" and "00101".
 *
 *  Example 2:
 *
 *  Input: s = "101"
 *  Output: 3
 *
 *  Constraints:
 *
 *  1 <= s.length <= 100
 *  s consists only of '0' and '1'.
 */
public class CountValidPrefixes {

    // V0
    // IDEA: a multiset of 0s and 1s can be rearranged into an alternating
    //       string iff |cnt0 - cnt1| <= 1, so keep running counts per prefix
    /**
     * time = O(n)
     * space = O(1)
     */
    public int countValidPrefixes(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        int zeroCnt = 0;
        int oneCnt = 0;
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                zeroCnt++;
            } else {
                oneCnt++;
            }
            if (Math.abs(zeroCnt - oneCnt) <= 1) {
                res++;
            }
        }
        return res;
    }
}
