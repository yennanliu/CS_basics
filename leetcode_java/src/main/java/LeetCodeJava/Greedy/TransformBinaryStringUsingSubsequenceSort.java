package LeetCodeJava.Greedy;

// https://leetcode.com/problems/transform-binary-string-using-subsequence-sort/

import java.util.*;

/**
 *  3998. Transform Binary String Using Subsequence Sort
 *  Medium
 *
 *  You are given a binary string s, and an array of strings strs, where each
 *  strs[i] has the same length as s and consists of '0', '1' and '?'.
 *  Each '?' can be replaced by either '0' or '1'.
 *
 *  You may perform the following operation any number of times:
 *   - Choose any subsequence sub of s, sort sub in non-decreasing order and
 *     put it back in place (other characters unchanged).
 *
 *  Return a boolean array ans, where ans[i] is true if it's possible to replace
 *  all '?' in strs[i] and transform s into the resulting string.
 *
 *  Example 1:
 *  Input: s = "101", strs = ["1?1","0?1","0?0"]
 *  Output: [true,true,false]
 *
 *  Example 2:
 *  Input: s = "1100", strs = ["0011","11?1","1?1?"]
 *  Output: [true,false,true]
 *
 *  Example 3:
 *  Input: s = "1010", strs = ["0011"]
 *  Output: [true]
 *
 *  Constraints:
 *   - 1 <= n == s.length <= 2000
 *   - s[i] is either '0' or '1'
 *   - 1 <= strs.length <= 2000
 *   - strs[i].length == n, strs[i] is '0', '1' or '?'
 */
public class TransformBinaryStringUsingSubsequenceSort {

    // V0
    // IDEA: PREFIX SUM + GREEDY
    //       sorting a subsequence only moves '1' to the RIGHT and keeps the total
    //       count of '1'. So target t is reachable from s
    //         <=> ones(t) == ones(s)  AND  prefixOnes(t, i) <= prefixOnes(s, i) for every i.
    //       For the '?' we must place exactly (ones(s) - ones(t_fixed)) ones;
    //       greedily put them as LATE as possible (fill leading '?' with '0'),
    //       which minimizes every prefix count.
    /**
     * time = O(n * m)   # m = strs.length, n = s.length
     * space = O(n)
     */
    public boolean[] transformStr(String s, String[] strs) {

        int n = s.length();

        // prefix count of '1' in s
        int[] prefS = new int[n];
        int cur = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '1') {
                cur++;
            }
            prefS[i] = cur;
        }
        int onesS = cur;

        boolean[] res = new boolean[strs.length];

        for (int k = 0; k < strs.length; k++) {

            String x = strs[k];
            int onesX = 0;
            int qX = 0;
            for (int i = 0; i < n; i++) {
                char c = x.charAt(i);
                if (c == '1') {
                    onesX++;
                } else if (c == '?') {
                    qX++;
                }
            }

            // can NOT match the total number of '1'
            if (onesS < onesX || onesS > onesX + qX) {
                res[k] = false;
                continue;
            }

            int onesNeeded = onesS - onesX;   // '?' that must become '1'
            int zerosForQ = qX - onesNeeded;  // leading '?' that become '0'

            int curOnesX = 0;
            int qSeen = 0;
            boolean ok = true;

            for (int i = 0; i < n; i++) {
                char c = x.charAt(i);
                if (c == '1') {
                    curOnesX++;
                } else if (c == '?') {
                    qSeen++;
                    if (qSeen > zerosForQ) {
                        curOnesX++;
                    }
                }

                // target has more '1' in this prefix than s -> impossible
                if (prefS[i] < curOnesX) {
                    ok = false;
                    break;
                }
            }

            res[k] = ok;
        }

        return res;
    }
}
