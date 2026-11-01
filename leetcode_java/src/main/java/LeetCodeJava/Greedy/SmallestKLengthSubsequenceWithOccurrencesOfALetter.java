package LeetCodeJava.Greedy;

// https://leetcode.com/problems/smallest-k-length-subsequence-with-occurrences-of-a-letter/description/

/**
 * 2030. Smallest K-Length Subsequence With Occurrences of a Letter
 * Hard
 *
 * You are given a string s, an integer k, a letter letter, and an integer repetition.
 *
 * Return the lexicographically smallest subsequence of s of length k that has the letter letter appear at
 * least repetition times. The test cases are generated so that the letter appears in s at least repetition
 * times.
 *
 * A subsequence is a string that can be derived from another string by deleting some or no characters
 * without changing the order of the remaining characters.
 *
 * A string a is lexicographically smaller than a string b if in the first position where a and b differ,
 * string a has a letter that appears earlier in the alphabet than the corresponding letter in b.
 *
 *
 * Example 1:
 *
 * Input: s = "leet", k = 3, letter = "e", repetition = 1
 * Output: "eet"
 *
 * Example 2:
 *
 * Input: s = "leetcode", k = 4, letter = "e", repetition = 2
 * Output: "ecde"
 *
 * Example 3:
 *
 * Input: s = "bb", k = 2, letter = "b", repetition = 2
 * Output: "bb"
 *
 *
 * Constraints:
 *
 * 1 <= repetition <= k <= s.length <= 5 * 10^4
 * s consists of lowercase English letters.
 * letter is a lowercase English letter, and appears in s at least repetition times.
 *
 */
public class SmallestKLengthSubsequenceWithOccurrencesOfALetter {

    // V0
    // IDEA: MONOTONIC STACK ("REMOVE K DIGITS" GREEDY) + A QUOTA FOR `letter`
    /**
     * time = O(N)
     * space = O(K)
     */
    public String smallestSubsequence(String s, int k, char letter, int repetition) {
        int n = s.length();

        // occurrences of `letter` from index i onwards (not yet scanned)
        int lettersLeft = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == letter) {
                lettersLeft++;
            }
        }

        // the stack is the answer being built; a StringBuilder used as a char stack
        StringBuilder stack = new StringBuilder();
        // occurrences of `letter` currently kept in the stack
        int keptLetters = 0;

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            // characters available from i (inclusive)
            int remain = n - i;

            // pop a bigger character on top while it is still safe to do so
            while (stack.length() > 0) {
                char top = stack.charAt(stack.length() - 1);
                if (top <= c) {
                    break;
                }
                // guard 1: after popping, can we still reach length k?
                if (stack.length() - 1 + remain < k) {
                    break;
                }
                // guard 2: popping a `letter` must leave enough letters to meet the quota
                if (top == letter && keptLetters - 1 + lettersLeft < repetition) {
                    break;
                }
                stack.deleteCharAt(stack.length() - 1);
                if (top == letter) {
                    keptLetters--;
                }
            }

            if (stack.length() < k) {
                if (c == letter) {
                    stack.append(c);
                    keptLetters++;
                } else if (k - stack.length() > repetition - keptLetters) {
                    // a non-letter char only fits if a slot is left after reserving the outstanding quota
                    stack.append(c);
                }
            }

            if (c == letter) {
                lettersLeft--;
            }
        }
        return stack.toString();
    }
}
