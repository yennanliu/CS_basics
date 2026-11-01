package LeetCodeJava.String;

// https://leetcode.com/problems/find-beautiful-indices-in-the-given-array-ii/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3008. Find Beautiful Indices in the Given Array II
 * Hard
 *
 * You are given a 0-indexed string s, a string a, a string b, and an integer k.
 *
 * An index i is beautiful if:
 *
 * 0 <= i <= s.length - a.length
 * s[i..(i + a.length - 1)] == a
 * There exists an index j such that:
 *     0 <= j <= s.length - b.length
 *     s[j..(j + b.length - 1)] == b
 *     |j - i| <= k
 *
 * Return the array that contains beautiful indices in sorted order from smallest to largest.
 *
 *
 * Example 1:
 *
 * Input: s = "isawsquirrelnearmysquirrelhouseohmy", a = "my", b = "squirrel", k = 15
 * Output: [16,33]
 * Explanation: There are 2 beautiful indices: [16,33].
 * - The index 16 is beautiful as s[16..17] == "my" and there exists an index 4 with s[4..11] == "squirrel" and |16 - 4| <= 15.
 * - The index 33 is beautiful as s[33..34] == "my" and there exists an index 18 with s[18..25] == "squirrel" and |33 - 18| <= 15.
 * Thus we return [16,33] as the result.
 *
 * Example 2:
 *
 * Input: s = "abcd", a = "a", b = "a", k = 4
 * Output: [0]
 * Explanation: There is 1 beautiful index: [0].
 * - The index 0 is beautiful as s[0..0] == "a" and there exists an index 0 with s[0..0] == "a" and |0 - 0| <= 4.
 * Thus we return [0] as the result.
 *
 *
 * Constraints:
 *
 * 1 <= k <= s.length <= 5 * 10^5
 * 1 <= a.length, b.length <= 5 * 10^5
 * s, a, and b contain only lowercase English letters.
 *
 */
public class FindBeautifulIndicesInTheGivenArrayII {

    // V0
    // IDEA: KMP FOR BOTH PATTERNS, THEN A SINGLE MERGE PASS
    //       same shape as LC 3006, but patterns reach 5e5 chars, so matching
    //       must be linear (KMP). Both match lists are ascending, so one
    //       pointer into posB that never rewinds replaces the binary search:
    //       skip b-matches < i - k, then i is beautiful iff the next one <= i + k.
    /**
     * time = O(|s| + |a| + |b|)
     * space = O(|s| + |a| + |b|)
     */
    public List<Integer> beautifulIndices(String s, String a, String b, int k) {
        List<Integer> posA = kmpSearch(s, a);
        List<Integer> posB = kmpSearch(s, b);

        List<Integer> result = new ArrayList<>();
        int t = 0;
        for (int i : posA) {
            // b-matches too far to the left can never help a later (larger) i either
            while (t < posB.size() && posB.get(t) < i - k) {
                t++;
            }
            if (t < posB.size() && posB.get(t) <= i + k) {
                result.add(i);
            }
        }
        return result;
    }

    /** Every start index of pattern in text, found with KMP in O(|text| + |pattern|). */
    private List<Integer> kmpSearch(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int m = pattern.length();
        if (m > text.length()) {
            return positions;
        }

        // fail[i] = length of the longest proper prefix of pattern[0..i] that is also its suffix
        int[] fail = new int[m];
        int matched = 0;
        for (int i = 1; i < m; i++) {
            while (matched > 0 && pattern.charAt(i) != pattern.charAt(matched)) {
                matched = fail[matched - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(matched)) {
                matched++;
            }
            fail[i] = matched;
        }

        // scan the text, falling back through fail[] on a mismatch
        matched = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            while (matched > 0 && c != pattern.charAt(matched)) {
                matched = fail[matched - 1];
            }
            if (c == pattern.charAt(matched)) {
                matched++;
            }
            if (matched == m) {
                positions.add(i - m + 1);
                // keep going so overlapping matches are found too
                matched = fail[matched - 1];
            }
        }
        return positions;
    }
}
