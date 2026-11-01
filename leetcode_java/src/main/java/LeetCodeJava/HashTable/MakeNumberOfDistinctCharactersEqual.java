package LeetCodeJava.HashTable;

// https://leetcode.com/problems/make-number-of-distinct-characters-equal/description/

/**
 * 2531. Make Number of Distinct Characters Equal
 * Medium
 *
 * You are given two 0-indexed strings word1 and word2.
 *
 * A move consists of choosing two indices i and j such that 0 <= i < word1.length and 0 <= j < word2.length and swapping word1[i] with word2[j].
 *
 * Return true if it is possible to get the number of distinct characters in word1 and word2 to be equal with exactly one move. Return false otherwise.
 *
 *
 * Example 1:
 *
 * Input: word1 = "ac", word2 = "b"
 * Output: false
 * Explanation: Any pair of swaps would yield two distinct characters in the first string, and one in the second string.
 *
 * Example 2:
 *
 * Input: word1 = "abcc", word2 = "aab"
 * Output: true
 * Explanation: We swap index 2 of the first string with index 0 of the second string. The resulting strings are word1 = "abac" and word2 = "cab", which both have 3 distinct characters.
 *
 * Example 3:
 *
 * Input: word1 = "abcde", word2 = "fghij"
 * Output: true
 * Explanation: Both resulting strings will have 5 distinct characters, regardless of which indices we swap.
 *
 *
 * Constraints:
 *
 * 1 <= word1.length, word2.length <= 10^5
 * word1 and word2 consist of only lowercase English letters.
 *
 */
public class MakeNumberOfDistinctCharactersEqual {

    // V0
    // IDEA: COUNTING + ENUMERATE THE 26 x 26 CHARACTER SWAPS
    /**
     * time = O(M + N + 26 * 26)
     * space = O(26)
     */
    public boolean isItPossible(String word1, String word2) {
        int[] count1 = new int[26];
        int[] count2 = new int[26];
        for (char ch : word1.toCharArray()) {
            count1[ch - 'a']++;
        }
        for (char ch : word2.toCharArray()) {
            count2[ch - 'a']++;
        }
        int distinct1 = countDistinct(count1);
        int distinct2 = countDistinct(count2);

        // a move only depends on WHICH characters are exchanged, not where they sit
        for (int out = 0; out < 26; out++) {
            if (count1[out] == 0) {
                continue;
            }
            for (int in = 0; in < 26; in++) {
                if (count2[in] == 0) {
                    continue;
                }
                if (out == in) {
                    // swapping equal letters changes nothing
                    if (distinct1 == distinct2) {
                        return true;
                    }
                    continue;
                }
                // word1 gives away `out` and receives `in`; word2 the other way round
                int after1 = distinct1;
                if (count1[out] == 1) {
                    after1--;
                }
                if (count1[in] == 0) {
                    after1++;
                }
                int after2 = distinct2;
                if (count2[in] == 1) {
                    after2--;
                }
                if (count2[out] == 0) {
                    after2++;
                }
                if (after1 == after2) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Number of letters with a non-zero count. */
    private int countDistinct(int[] count) {
        int distinct = 0;
        for (int c : count) {
            if (c > 0) {
                distinct++;
            }
        }
        return distinct;
    }
}
