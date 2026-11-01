package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-number-of-valid-strings-to-form-target-ii/description/

/**
 * 3292. Minimum Number of Valid Strings to Form Target II
 * Hard
 *
 * You are given an array of strings words and a string target.
 *
 * A string x is called valid if x is a prefix of any string in words.
 *
 * Return the minimum number of valid strings that can be concatenated to form target. If it is not possible to form target, return -1.
 *
 *
 * Example 1:
 *
 * Input: words = ["abc","aaaaa","bcdef"], target = "aabcdabc"
 * Output: 3
 * Explanation:
 * The target string can be formed by concatenating:
 * Prefix of length 2 of words[1], i.e. "aa".
 * Prefix of length 3 of words[2], i.e. "bcd".
 * Prefix of length 3 of words[0], i.e. "abc".
 *
 * Example 2:
 *
 * Input: words = ["abababab","ab"], target = "ababaababa"
 * Output: 2
 * Explanation:
 * The target string can be formed by concatenating:
 * Prefix of length 5 of words[0], i.e. "ababa".
 * Prefix of length 5 of words[0], i.e. "ababa".
 *
 * Example 3:
 *
 * Input: words = ["abcdef"], target = "xyz"
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 5 * 10^4
 * The input is generated such that sum(words[i].length) <= 10^5.
 * words[i] consists only of lowercase English letters.
 * 1 <= target.length <= 5 * 10^4
 * target consists only of lowercase English letters.
 *
 */
public class MinimumNumberOfValidStringsToFormTargetII {

    // V0
    // IDEA: Z-FUNCTION PER WORD -> reach[i] (LONGEST WORD PREFIX MATCHING AT i) -> JUMP GAME II
    //   Z over (word + '#' + target): the z-value at target position i is how far word's
    //   prefix matches target starting at i. Take the max over all words. Then the problem is
    //   Jump Game II: from position i we can jump up to reach[i] characters forward.
    //   (A trie walk per position is the LC 3291 approach; it is too slow at 5 * 10^4.)
    /**
     * time = O(sum of word lengths + |words| * |target|)
     * space = O(|target| + max word length)
     */
    public int minValidStrings(String[] words, String target) {
        int n = target.length();
        // longestMatch[i] = longest prefix of any word that matches target starting at i
        int[] longestMatch = new int[n];

        for (String word : words) {
            String combined = word + "#" + target;
            int[] z = zArray(combined);
            int offset = word.length() + 1;
            for (int i = 0; i < n; i++) {
                longestMatch[i] = Math.max(longestMatch[i], z[offset + i]);
            }
        }

        // Jump Game II: currentEnd = covered length with `pieces` valid strings
        int pieces = 0;
        int currentEnd = 0;
        int farthest = 0;
        int i = 0;
        while (currentEnd < n) {
            // every start inside the covered prefix may begin the next piece
            while (i < n && i <= currentEnd) {
                farthest = Math.max(farthest, i + longestMatch[i]);
                i++;
            }
            // no piece can extend the coverage -> target cannot be formed
            if (farthest <= currentEnd) {
                return -1;
            }
            currentEnd = farthest;
            pieces++;
        }
        return pieces;
    }

    /** Standard Z-algorithm: z[i] = length of the longest common prefix of s and s[i..]. */
    private int[] zArray(String s) {
        int m = s.length();
        int[] z = new int[m];
        // [left, right) is the rightmost window known to match a prefix of s
        int left = 0;
        int right = 0;
        for (int i = 1; i < m; i++) {
            if (i < right) {
                // reuse the mirrored value inside the known window
                z[i] = Math.min(right - i, z[i - left]);
            }
            while (i + z[i] < m && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] > right) {
                left = i;
                right = i + z[i];
            }
        }
        return z;
    }
}
