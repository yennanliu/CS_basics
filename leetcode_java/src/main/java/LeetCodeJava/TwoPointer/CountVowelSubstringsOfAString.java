package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/count-vowel-substrings-of-a-string/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 2062. Count Vowel Substrings of a String
 * Easy
 *
 * A substring is a contiguous (non-empty) sequence of characters within a string.
 *
 * A vowel substring is a substring that only consists of vowels ('a', 'e', 'i', 'o', and 'u') and has all five vowels present in it.
 *
 * Given a string word, return the number of vowel substrings in word.
 *
 *
 * Example 1:
 *
 * Input: word = "aeiouu"
 * Output: 2
 * Explanation: The vowel substrings of word are as follows (underlined):
 * - "aeiouu"
 * - "aeiouu"
 *
 * Example 2:
 *
 * Input: word = "unicornarihan"
 * Output: 0
 * Explanation: Not all 5 vowels are present, so there are no vowel substrings.
 *
 * Example 3:
 *
 * Input: word = "cuaieuouac"
 * Output: 7
 * Explanation: The vowel substrings of word are as follows (underlined):
 * - "cuaieuouac"
 * - "cuaieuouac"
 * - "cuaieuouac"
 * - "cuaieuouac"
 * - "cuaieuouac"
 * - "cuaieuouac"
 * - "cuaieuouac"
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 100
 * word consists of lowercase English letters only.
 *
 */
public class CountVowelSubstringsOfAString {

    private static final String VOWELS = "aeiou";

    // V0
    // IDEA: FIX THE LEFT END, GROW THE RIGHT END, STOP AT THE FIRST CONSONANT
    //  keep the set of distinct vowels seen in word[i..j]; once a consonant appears no longer
    //  substring starting at i can be all vowels. each time the set holds all 5 vowels,
    //  word[i..j] is a vowel substring. n <= 100, so O(n^2) is the intended bound.
    /**
     * time = O(N^2)
     * space = O(1)   (the set holds at most 5 letters)
     */
    public int countVowelSubstrings(String word) {
        int n = word.length();
        int count = 0;
        for (int i = 0; i < n; i++) {
            Set<Character> seen = new HashSet<>();
            for (int j = i; j < n; j++) {
                char c = word.charAt(j);
                if (VOWELS.indexOf(c) < 0) {
                    break;
                }
                seen.add(c);
                if (seen.size() == 5) {
                    count++;
                }
            }
        }
        return count;
    }
}
