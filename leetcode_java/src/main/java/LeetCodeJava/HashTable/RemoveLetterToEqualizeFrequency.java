package LeetCodeJava.HashTable;

// https://leetcode.com/problems/remove-letter-to-equalize-frequency/description/

/**
 * 2423. Remove Letter To Equalize Frequency
 * Easy
 *
 * You are given a 0-indexed string word, consisting of lowercase English letters. You need to select one index and remove the letter at that index from word so that the frequency of every letter present in word is equal.
 *
 * Return true if it is possible to remove one letter so that the frequency of all letters in word are equal, and false otherwise.
 *
 * Note:
 *
 * The frequency of a letter x is the number of times it occurs in the string.
 * You must remove exactly one letter and cannot choose to do nothing.
 *
 *
 * Example 1:
 *
 * Input: word = "abcc"
 * Output: true
 * Explanation: Select index 3 and delete it: word becomes "abc" and each character has a frequency of 1.
 *
 * Example 2:
 *
 * Input: word = "aazz"
 * Output: false
 * Explanation: We must delete a character, so either the frequency of "a" is 1 and the frequency of "z" is 2, or vice versa. It is impossible to make all present letters have equal frequency.
 *
 *
 * Constraints:
 *
 * 2 <= word.length <= 100
 * word consists of lowercase English letters only.
 *
 */
public class RemoveLetterToEqualizeFrequency {

    // V0
    // IDEA: COUNTING + TRY REMOVING ONE COPY OF EACH DISTINCT LETTER, RE-CHECK
    /**
     * time = O(N + 26 * 26)
     * space = O(26)
     */
    public boolean equalFrequency(String word) {
        int[] count = new int[26];
        for (char ch : word.toCharArray()) {
            count[ch - 'a']++;
        }

        // only 26 candidate removals exist; try each present letter
        for (int letter = 0; letter < 26; letter++) {
            if (count[letter] == 0) {
                continue;
            }
            count[letter]--;
            boolean ok = allSameFrequency(count);
            // undo the trial removal before trying the next letter
            count[letter]++;
            if (ok) {
                return true;
            }
        }
        return false;
    }

    /** True if every letter that is still present has the same frequency. */
    private boolean allSameFrequency(int[] count) {
        int shared = 0;
        for (int freq : count) {
            // a letter whose count dropped to 0 is gone, so it does not take part
            if (freq == 0) {
                continue;
            }
            if (shared == 0) {
                shared = freq;
            } else if (freq != shared) {
                return false;
            }
        }
        return true;
    }
}
