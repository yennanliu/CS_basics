package LeetCodeJava.HashTable;

// https://leetcode.com/problems/determine-if-two-strings-are-close/description/

import java.util.*;

/**
 * 1657. Determine if Two Strings Are Close
 * Medium
 *
 * Two strings are considered close if you can attain one from the other using the following operations:
 *
 * Operation 1: Swap any two existing characters.
 *   For example, abcde -> aecdb
 * Operation 2: Transform every occurrence of one existing character into another existing character,
 *   and do the same with the other character.
 *   For example, aacabb -> bbcbaa (all a's turn into b's, and all b's turn into a's)
 *
 * You can use the operations on either string as many times as necessary.
 *
 * Given two strings, word1 and word2, return true if word1 and word2 are close, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: word1 = "abc", word2 = "bca"
 * Output: true
 *
 * Example 2:
 *
 * Input: word1 = "a", word2 = "aa"
 * Output: false
 *
 * Example 3:
 *
 * Input: word1 = "cabbba", word2 = "abbccc"
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= word1.length, word2.length <= 10^5
 * word1 and word2 contain only lowercase English letters.
 *
 */
public class DetermineIfTwoStringsAreClose {

    // V0
    // IDEA: COUNTING (two invariants under the operations)
    //       op 1 makes order irrelevant; op 2 permutes counts among letters already present.
    //       so close <=> same SET of letters AND same sorted multiset of counts.
    /**
     * time = O(N + M)
     * space = O(1)
     */
    public boolean closeStrings(String word1, String word2) {
        if (word1.length() != word2.length()) {
            return false;
        }

        int[] count1 = new int[26];
        int[] count2 = new int[26];
        for (char c : word1.toCharArray()) {
            count1[c - 'a']++;
        }
        for (char c : word2.toCharArray()) {
            count2[c - 'a']++;
        }

        // op 2 can never create or remove a letter: both words must use the same letters
        for (int i = 0; i < 26; i++) {
            boolean inWord1 = count1[i] > 0;
            boolean inWord2 = count2[i] > 0;
            if (inWord1 != inWord2) {
                return false;
            }
        }

        // the frequencies can be reassigned freely, so compare them as sorted lists
        Arrays.sort(count1);
        Arrays.sort(count2);
        return Arrays.equals(count1, count2);
    }
}
