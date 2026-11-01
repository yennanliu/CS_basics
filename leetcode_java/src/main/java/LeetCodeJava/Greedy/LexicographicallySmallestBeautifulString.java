package LeetCodeJava.Greedy;

// https://leetcode.com/problems/lexicographically-smallest-beautiful-string/description/

/**
 * 2663. Lexicographically Smallest Beautiful String
 * Hard
 *
 * A string is beautiful if:
 *
 * It consists of the first k letters of the English lowercase alphabet.
 * It does not contain any substring of length 2 or more which is a palindrome.
 *
 * You are given a beautiful string s of length n and a positive integer k.
 *
 * Return the lexicographically smallest string of length n, which is larger than s and is beautiful. If there is no such string, return an empty string.
 *
 * A string a is lexicographically larger than a string b (of the same length) if in the first position where a and b differ, a has a character strictly larger than the corresponding character in b.
 *
 * For example, "abcd" is lexicographically larger than "abcc" because the first position they differ is at the fourth character, and d is greater than c.
 *
 *
 * Example 1:
 *
 * Input: s = "abcz", k = 26
 * Output: "abda"
 * Explanation: The string "abda" is beautiful and lexicographically larger than the string "abcz".
 * It can be proven that there is no string that is lexicographically larger than the string "abcz", beautiful, and lexicographically smaller than the string "abda".
 *
 * Example 2:
 *
 * Input: s = "dc", k = 4
 * Output: ""
 * Explanation: It can be proven that there is no string that is lexicographically larger than the string "dc" and is beautiful.
 *
 *
 * Constraints:
 *
 * 1 <= n == s.length <= 10^5
 * 4 <= k <= 26
 * s is a beautiful string.
 *
 */
public class LexicographicallySmallestBeautifulString {

    // V0
    // IDEA: GREEDY — "NEXT PERMUTATION" STYLE BUMP, THEN MINIMAL REFILL
    //       a string has no palindrome of length >= 2 iff every char differs from the previous TWO.
    //       1) from the right, find the rightmost index that can be raised to a larger letter
    //          (< 'a' + k) that differs from its two left neighbours.
    //       2) refill everything after it with the smallest letter that differs from its two left
    //          neighbours (k >= 4 guarantees one exists).
    //       3) no index can be raised -> "".
    /**
     * time = O(N * K)
     * space = O(N)
     */
    public String smallestBeautifulString(String s, int k) {
        int n = s.length();
        char[] word = s.toCharArray();
        char lastLetter = (char) ('a' + k - 1);

        for (int i = n - 1; i >= 0; i--) {
            // the prefix word[0..i-1] is still the original (beautiful) string
            char raised = 0;
            for (char c = (char) (word[i] + 1); c <= lastLetter; c++) {
                if (fitsAfter(word, i, c)) {
                    raised = c;
                    break;
                }
            }
            if (raised == 0) {
                continue;
            }

            word[i] = raised;
            // everything to the right is free: fill each with the smallest legal letter
            for (int j = i + 1; j < n; j++) {
                for (char c = 'a'; c <= lastLetter; c++) {
                    if (fitsAfter(word, j, c)) {
                        word[j] = c;
                        break;
                    }
                }
            }
            return new String(word);
        }
        return "";
    }

    /** True if letter c at index pos differs from the two letters just before it. */
    private boolean fitsAfter(char[] word, int pos, char c) {
        if (pos >= 1 && word[pos - 1] == c) {
            return false;
        }
        if (pos >= 2 && word[pos - 2] == c) {
            return false;
        }
        return true;
    }
}
