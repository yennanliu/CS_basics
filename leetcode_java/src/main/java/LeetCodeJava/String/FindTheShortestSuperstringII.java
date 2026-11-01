package LeetCodeJava.String;

// https://leetcode.com/problems/find-the-shortest-superstring-ii/description/

/**
 * 3571. Find the Shortest Superstring II
 * Easy
 *
 * You are given two strings, s1 and s2. Return the shortest possible string
 * that contains both s1 and s2 as substrings. If there are multiple valid
 * answers, return any one of them.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s1 = "aba", s2 = "bab"
 * Output: "abab"
 * Explanation:
 * "abab" is the shortest string that contains both "aba" and "bab" as
 * substrings.
 *
 * Example 2:
 *
 * Input: s1 = "aa", s2 = "aaa"
 * Output: "aaa"
 * Explanation:
 * "aa" is already contained within "aaa", so the shortest superstring is
 * "aaa".
 *
 *
 * Constraints:
 *
 * 1 <= s1.length <= 100
 * 1 <= s2.length <= 100
 * s1 and s2 consist of lowercase English letters only.
 *
 */
public class FindTheShortestSuperstringII {

    // V0
    // IDEA: MAXIMUM PREFIX / SUFFIX OVERLAP
    //   the answer is the longer string (if the shorter one is inside it), or one string
    //   followed by the other with the largest possible overlap. With s1 the shorter one,
    //   an overlap of m - i characters gives length n + i, so try i = 0, 1, ... and return
    //   the first orientation that fits.
    /**
     * time = O(M * N)
     * space = O(M + N)
     */
    public String shortestSuperstring(String s1, String s2) {
        // keep s1 as the shorter string; the answer is symmetric in the two arguments
        if (s1.length() > s2.length()) {
            String swap = s1;
            s1 = s2;
            s2 = swap;
        }
        // nesting case: s2 already contains s1
        if (s2.contains(s1)) {
            return s2;
        }
        int m = s1.length();
        for (int i = 0; i < m; i++) {
            // s1's suffix of length m - i is a prefix of s2 -> s1[0, i) + s2
            if (s2.startsWith(s1.substring(i))) {
                return s1.substring(0, i) + s2;
            }
            // s1's prefix of length m - i is a suffix of s2 -> s2 + s1[m - i, m)
            if (s2.endsWith(s1.substring(0, m - i))) {
                return s2 + s1.substring(m - i);
            }
        }
        // no overlap at all
        return s1 + s2;
    }
}
