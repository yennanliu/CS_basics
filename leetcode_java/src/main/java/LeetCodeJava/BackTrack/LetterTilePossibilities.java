package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/letter-tile-possibilities/description/

/**
 * 1079. Letter Tile Possibilities
 * Medium
 *
 * You have n tiles, where each tile has one letter tiles[i] printed on it.
 *
 * Return the number of possible non-empty sequences of letters you can make using the letters
 * printed on those tiles.
 *
 *
 * Example 1:
 *
 * Input: tiles = "AAB"
 * Output: 8
 * Explanation: The possible sequences are "A", "B", "AA", "AB", "BA", "AAB", "ABA", "BAA".
 *
 * Example 2:
 *
 * Input: tiles = "AAABBC"
 * Output: 188
 *
 * Example 3:
 *
 * Input: tiles = "V"
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= tiles.length <= 7
 * tiles consists of uppercase English letters.
 *
 */
public class LetterTilePossibilities {

    // V0
    // IDEA: BACKTRACK ON A LETTER COUNTER (dedupe by letter, not by tile position)
    //   each step picks a letter that still has stock: that choice is itself one new sequence,
    //   then recurse to extend it. looping over distinct letters instead of tile positions means
    //   identical tiles are never counted twice.
    /**
     * time = O(N!) worst case (N <= 7)
     * space = O(N) recursion depth
     */
    public int numTilePossibilities(String tiles) {
        int[] count = new int[26];
        for (int i = 0; i < tiles.length(); i++) {
            count[tiles.charAt(i) - 'A']++;
        }
        return countSequences(count);
    }

    /** Number of non-empty sequences that can be appended using the remaining letters. */
    private int countSequences(int[] count) {
        int total = 0;
        for (int letter = 0; letter < 26; letter++) {
            if (count[letter] == 0) {
                continue;
            }
            // the sequence that ends with this letter
            total += 1;
            // choose, explore every extension, un-choose
            count[letter]--;
            total += countSequences(count);
            count[letter]++;
        }
        return total;
    }
}
