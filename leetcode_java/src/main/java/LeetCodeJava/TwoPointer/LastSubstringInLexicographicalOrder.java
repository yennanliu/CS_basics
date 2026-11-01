package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/last-substring-in-lexicographical-order/description/

/**
 * 1163. Last Substring in Lexicographical Order
 * Hard
 *
 * Given a string s, return the last substring of s in lexicographical order.
 *
 *
 * Example 1:
 *
 * Input: s = "abab"
 * Output: "bab"
 * Explanation: The substrings are ["a", "ab", "aba", "abab", "b", "ba", "bab"].
 * The lexicographically maximum substring is "bab".
 *
 * Example 2:
 *
 * Input: s = "leetcode"
 * Output: "tcode"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 4 * 10^5
 * s contains only lowercase English letters.
 *
 */
public class LastSubstringInLexicographicalOrder {

    // V0
    // IDEA: TWO POINTERS over candidate suffixes (the answer is always a suffix)
    //       brute force max over all suffixes is O(N^2) and times out on 4 * 10^5
    /**
     * time = O(N)
     * space = O(1)
     */
    public String lastSubstring(String s) {
        int n = s.length();

        // best = start of the best suffix so far, candidate = start of the challenger,
        // matched = how many characters the two already agree on
        int best = 0;
        int candidate = 1;
        int matched = 0;

        while (candidate + matched < n) {
            char bestChar = s.charAt(best + matched);
            char candidateChar = s.charAt(candidate + matched);

            if (bestChar == candidateChar) {
                // still tied: compare the next character
                matched++;
            } else if (bestChar < candidateChar) {
                // the challenger wins, and so would any start in [best, best + matched]
                // (each is beaten by the matching start inside the challenger),
                // so the next best start is past them -- and never before the challenger
                best = Math.max(best + matched + 1, candidate);
                candidate = best + 1;
                matched = 0;
            } else {
                // the current best wins: skip every start in [candidate, candidate + matched]
                candidate = candidate + matched + 1;
                matched = 0;
            }
        }
        return s.substring(best);
    }
}
