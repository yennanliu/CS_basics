package LeetCodeJava.String;

// https://leetcode.com/problems/longest-uncommon-subsequence-i/

/**
 *  521. Longest Uncommon Subsequence I
 *  Easy
 *
 *  Given two strings a and b, return the length of the longest uncommon
 *  subsequence between a and b. If no such uncommon subsequence exists,
 *  return -1.
 *
 *  An uncommon subsequence between two strings is a string that is a
 *  subsequence of exactly one of them.
 *
 *  Example 1:
 *    Input: a = "aba", b = "cdc"    Output: 3
 *  Example 2:
 *    Input: a = "aaa", b = "bbb"    Output: 3
 *  Example 3:
 *    Input: a = "aaa", b = "aaa"    Output: -1
 *
 *  Constraints:
 *    1 <= a.length, b.length <= 100
 *    a and b consist of lower-case English letters.
 */
public class LongestUncommonSubsequenceI {

    // V0
    // IDEA: if the strings differ, the longer one itself is uncommon; if equal, nothing is
    /**
     * time = O(min(a, b))
     * space = O(1)
     */
    public int findLUSlength(String a, String b) {
        if (a.equals(b)) {
            return -1;
        }
        return Math.max(a.length(), b.length());
    }
}
