package LeetCodeJava.String;

// https://leetcode.com/problems/check-if-the-sentence-is-pangram/description/

/**
 * 1832. Check if the Sentence Is Pangram
 * Easy
 *
 * A pangram is a sentence where every letter of the English alphabet appears at least once.
 *
 * Given a string sentence containing only lowercase English letters, return true if sentence is a pangram, or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: sentence = "thequickbrownfoxjumpsoverthelazydog"
 * Output: true
 * Explanation: sentence contains at least one of every letter of the English alphabet.
 *
 * Example 2:
 *
 * Input: sentence = "leetcode"
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= sentence.length <= 1000
 * sentence consists of lowercase English letters.
 *
 */
public class CheckIfTheSentenceIsPangram {

    private static final int ALL_LETTERS_MASK = (1 << 26) - 1;

    // V0
    // IDEA: BITMASK OF SEEN LETTERS (BIT c - 'a' SET FOR EVERY CHARACTER)
    //       a pangram has all 26 bits set.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean checkIfPangram(String sentence) {
        int seenMask = 0;
        for (char c : sentence.toCharArray()) {
            seenMask |= 1 << (c - 'a');
        }
        return seenMask == ALL_LETTERS_MASK;
    }
}
