package LeetCodeJava.String;

// https://leetcode.com/problems/check-if-two-string-arrays-are-equivalent/description/

/**
 * 1662. Check If Two String Arrays are Equivalent
 * Easy
 *
 * Given two string arrays word1 and word2, return true if the two arrays represent the same string,
 * and false otherwise.
 *
 * A string is represented by an array if the array elements concatenated in order forms the string.
 *
 *
 * Example 1:
 *
 * Input: word1 = ["ab", "c"], word2 = ["a", "bc"]
 * Output: true
 * Explanation:
 * word1 represents string "ab" + "c" -> "abc"
 * word2 represents string "a" + "bc" -> "abc"
 * The strings are the same, so return true.
 *
 * Example 2:
 *
 * Input: word1 = ["a", "cb"], word2 = ["ab", "c"]
 * Output: false
 *
 * Example 3:
 *
 * Input: word1  = ["abc", "d", "defg"], word2 = ["abcddefg"]
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= word1.length, word2.length <= 10^3
 * 1 <= word1[i].length, word2[i].length <= 10^3
 * 1 <= sum(word1[i].length), sum(word2[i].length) <= 10^3
 * word1[i] and word2[i] consist of lowercase letters.
 *
 */
public class CheckIfTwoStringArraysAreEquivalent {

    // V0
    // IDEA: CONCATENATE BOTH SIDES AND COMPARE
    //       the arrays are just chunked forms of one string each; chunk
    //       boundaries carry no information.
    /**
     * time = O(N + M)   (total characters)
     * space = O(N + M)
     */
    public boolean arrayStringsAreEqual(String[] word1, String[] word2) {
        StringBuilder first = new StringBuilder();
        for (String piece : word1) {
            first.append(piece);
        }
        StringBuilder second = new StringBuilder();
        for (String piece : word2) {
            second.append(piece);
        }
        return first.toString().equals(second.toString());
    }

    // V1
    // IDEA: FOUR POINTERS (WHICH WORD + WHICH CHAR, ON EACH SIDE), NO CONCATENATION
    //       walk both arrays character by character; the answer is false on the
    //       first mismatch, true if both run out at the same time.
    /**
     * time = O(N + M)
     * space = O(1)
     */
    public boolean arrayStringsAreEqual_1(String[] word1, String[] word2) {
        int word1Index = 0;
        int char1Index = 0;
        int word2Index = 0;
        int char2Index = 0;

        while (word1Index < word1.length && word2Index < word2.length) {
            if (word1[word1Index].charAt(char1Index) != word2[word2Index].charAt(char2Index)) {
                return false;
            }
            // advance inside the current word, or step to the next word
            char1Index++;
            if (char1Index == word1[word1Index].length()) {
                word1Index++;
                char1Index = 0;
            }
            char2Index++;
            if (char2Index == word2[word2Index].length()) {
                word2Index++;
                char2Index = 0;
            }
        }
        // equal only if both sides were consumed completely
        return word1Index == word1.length && word2Index == word2.length;
    }
}
