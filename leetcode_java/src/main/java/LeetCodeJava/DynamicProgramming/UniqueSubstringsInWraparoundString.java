package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/unique-substrings-in-wraparound-string/

/**
 *  467. Unique Substrings in Wraparound String
 *  Medium
 *
 *  We define the string base to be the infinite wraparound string of
 *  "abcdefghijklmnopqrstuvwxyz", so base looks like:
 *  "..zabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcd..".
 *
 *  Given a string s, return the number of unique non-empty substrings of s
 *  that are present in base.
 *
 *  Example 1:
 *
 *  Input: s = "a"
 *  Output: 1
 *
 *  Example 2:
 *
 *  Input: s = "cac"
 *  Output: 2
 *  Explanation: There are two substrings ("a", "c") of s in base.
 *
 *  Example 3:
 *
 *  Input: s = "zab"
 *  Output: 6
 *  Explanation: There are six substrings ("z", "a", "b", "za", "ab", "zab")
 *  of s in base.
 *
 *  Constraints:
 *
 *  1 <= s.length <= 10^5
 *  s consists of lowercase English letters.
 */
public class UniqueSubstringsInWraparoundString {

    // V0
    // IDEA: DP - for each ending letter c, keep the longest contiguous
    //       wraparound run ending with c. Every distinct valid substring is
    //       uniquely identified by (ending letter, length), so the answer is
    //       the sum of those maxima.
    /**
     * time = O(n)
     * space = O(1)  // fixed 26-size array
     */
    public int findSubstringInWraproundString(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int[] maxLen = new int[26]; // maxLen[c] = longest run ending with letter c
        int len = 0;
        for (int i = 0; i < s.length(); i++) {
            int cur = s.charAt(i) - 'a';
            if (i > 0) {
                int prev = s.charAt(i - 1) - 'a';
                if ((prev + 1) % 26 == cur) {
                    len++;
                } else {
                    len = 1;
                }
            } else {
                len = 1;
            }
            maxLen[cur] = Math.max(maxLen[cur], len);
        }

        int res = 0;
        for (int v : maxLen) {
            res += v;
        }
        return res;
    }
}
