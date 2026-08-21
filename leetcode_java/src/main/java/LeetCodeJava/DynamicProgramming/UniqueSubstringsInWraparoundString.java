package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/unique-substrings-in-wraparound-string/

import java.util.HashSet;
import java.util.Set;

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

    // V1
    // IDEA: brute force - enumerate every substring that is a wraparound run and let a
    //       HashSet do the de-duplication. Kept as a readable correctness reference
    //       (far too slow / memory hungry for n = 10^5).
    /**
     * time = O(n^2) substrings, O(n^3) counting the string building/hashing
     * space = O(n^2)
     */
    public int findSubstringInWraproundString_1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        Set<String> seen = new HashSet<>();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(s.charAt(i));
            seen.add(sb.toString());
            for (int j = i + 1; j < n; j++) {
                int prev = s.charAt(j - 1) - 'a';
                int cur = s.charAt(j) - 'a';
                if ((prev + 1) % 26 != cur) {
                    break;   // the run stops here, so does every longer substring from i
                }
                sb.append(s.charAt(j));
                seen.add(sb.toString());
            }
        }
        return seen.size();
    }

    // V2
    // IDEA: SEGMENT DECOMPOSITION - cut s into maximal wraparound segments, then identify
    //       each valid substring by (STARTING letter, length), the mirror bijection of V0's
    //       (ending letter, length). Inside a segment of length L the substring starting at
    //       offset k can be at most L - k long, so one pass per segment fills the 26 maxima.
    /**
     * time = O(n)
     * space = O(1)  // fixed 26-size array
     */
    public int findSubstringInWraproundString_2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int n = s.length();
        int[] maxStart = new int[26];   // maxStart[c] = longest valid substring starting with c

        int i = 0;
        while (i < n) {
            int j = i + 1;
            while (j < n && (s.charAt(j - 1) - 'a' + 1) % 26 == s.charAt(j) - 'a') {
                j++;
            }
            int len = j - i;            // maximal wraparound segment s[i, j)
            for (int k = i; k < j; k++) {
                int c = s.charAt(k) - 'a';
                maxStart[c] = Math.max(maxStart[c], len - (k - i));
            }
            i = j;
        }

        int res = 0;
        for (int v : maxStart) {
            res += v;
        }
        return res;
    }
}
