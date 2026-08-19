package LeetCodeJava.String;

// https://leetcode.com/problems/longest-uncommon-subsequence-ii/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  522. Longest Uncommon Subsequence II
 *  Medium
 *
 *  Given an array of strings strs, return the length of the longest uncommon
 *  subsequence between them. If the longest uncommon subsequence does not
 *  exist, return -1.
 *
 *  An uncommon subsequence between an array of strings is a string that is a
 *  subsequence of one string but not the others.
 *
 *  Example 1:
 *    Input: strs = ["aba","cdc","eae"]   Output: 3
 *  Example 2:
 *    Input: strs = ["aaa","aaa","aa"]    Output: -1
 *
 *  Constraints:
 *    2 <= strs.length <= 50
 *    1 <= strs[i].length <= 10
 *    strs[i] consists of lowercase English letters.
 */
public class LongestUncommonSubsequenceII {

    // V0
    // IDEA: sort by length desc, then take the first string that is not a
    //       subsequence of any other string (a duplicate always fails this test)
    /**
     * time = O(n^2 * l), n = strs.length, l = max string length
     * space = O(n)
     */
    public int findLUSlength(String[] strs) {
        if (strs == null || strs.length == 0) {
            return -1;
        }

        String[] arr = strs.clone();
        Arrays.sort(arr, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.length() - a.length();
            }
        });

        for (int i = 0; i < arr.length; i++) {
            boolean uncommon = true;
            for (int j = 0; j < arr.length; j++) {
                if (i == j) {
                    continue;
                }
                // sorted desc, so once arr[j] is shorter no later one can contain arr[i]
                if (arr[j].length() < arr[i].length()) {
                    break;
                }
                if (isSubsequence(arr[i], arr[j])) {
                    uncommon = false;
                    break;
                }
            }
            if (uncommon) {
                return arr[i].length();
            }
        }
        return -1;
    }

    // is a a subsequence of b ?
    private boolean isSubsequence(String a, String b) {
        int i = 0;
        for (int j = 0; j < b.length() && i < a.length(); j++) {
            if (a.charAt(i) == b.charAt(j)) {
                i++;
            }
        }
        return i == a.length();
    }
}
