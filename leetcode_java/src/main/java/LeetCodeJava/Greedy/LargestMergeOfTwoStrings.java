package LeetCodeJava.Greedy;

// https://leetcode.com/problems/largest-merge-of-two-strings/description/

/**
 * 1754. Largest Merge Of Two Strings
 * Medium
 *
 * You are given two strings word1 and word2. You want to construct a string merge in
 * the following way: while either word1 or word2 are non-empty, choose one of the
 * following options:
 *
 * - If word1 is non-empty, append the first character in word1 to merge and delete
 *   it from word1. For example, if word1 = "abc" and merge = "dv", then after
 *   choosing this operation, word1 = "bc" and merge = "dva".
 * - If word2 is non-empty, append the first character in word2 to merge and delete
 *   it from word2. For example, if word2 = "abc" and merge = "", then after choosing
 *   this operation, word2 = "bc" and merge = "a".
 *
 * Return the lexicographically largest merge you can construct.
 *
 * A string a is lexicographically larger than a string b (of the same length) if in
 * the first position where a and b differ, a has a character strictly larger than
 * the corresponding character in b. For example, "abcd" is lexicographically larger
 * than "abcc" because the first position they differ is at the fourth character,
 * and d is greater than c.
 *
 *
 * Example 1:
 *
 * Input: word1 = "cabaa", word2 = "bcaaa"
 * Output: "cbcabaaaaa"
 *
 * Example 2:
 *
 * Input: word1 = "abcabc", word2 = "abdcaba"
 * Output: "abdcabcabcaba"
 *
 *
 * Constraints:
 *
 * 1 <= word1.length, word2.length <= 3000
 * word1 and word2 consist only of lowercase English letters.
 *
 */
public class LargestMergeOfTwoStrings {

    // V0
    // IDEA: GREEDY WITH SUFFIX COMPARISON (TAKE FROM THE LEXICOGRAPHICALLY BIGGER TAIL)
    //       comparing only the front chars fails on a tie ("ca" vs "cb"); what
    //       decides is the whole remaining suffix, so compare word1[i:] with
    //       word2[j:] and take one char from the larger.
    /**
     * time = O((M + N)^2)
     * space = O(M + N)
     */
    public String largestMerge(String word1, String word2) {
        StringBuilder merge = new StringBuilder();
        int i = 0;
        int j = 0;
        while (i < word1.length() && j < word2.length()) {
            if (suffixIsLarger(word1, i, word2, j)) {
                merge.append(word1.charAt(i));
                i++;
            } else {
                merge.append(word2.charAt(j));
                j++;
            }
        }
        // one word is used up: the other's remainder goes on as is
        merge.append(word1.substring(i));
        merge.append(word2.substring(j));
        return merge.toString();
    }

    /** True if a[i..] is lexicographically larger than b[j..] (a longer string wins a shared prefix). */
    private boolean suffixIsLarger(String a, int i, String b, int j) {
        while (i < a.length() && j < b.length()) {
            if (a.charAt(i) != b.charAt(j)) {
                return a.charAt(i) > b.charAt(j);
            }
            i++;
            j++;
        }
        // equal up to the shorter one: the one with characters left is larger
        return i < a.length();
    }
}
