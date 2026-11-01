package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-valid-words-for-each-puzzle/description/

import java.util.*;

/**
 * 1178. Number of Valid Words for Each Puzzle
 * Hard
 *
 * With respect to a given puzzle string, a word is valid if both the following conditions are satisfied:
 *
 * word contains the first letter of puzzle.
 * For each letter in word, that letter is in puzzle.
 *     For example, if the puzzle is "abcdefg", then valid words are "faced", "cabbage", and "baggage", while
 *     invalid words are "beefed" (does not include 'a') and "based" (includes 's' which is not in the puzzle).
 *
 * Return an array answer, where answer[i] is the number of words in the given word list words that is valid
 * with respect to the puzzle puzzles[i].
 *
 * Example 1:
 *
 * Input: words = ["aaaa","asas","able","ability","actt","actor","access"],
 *        puzzles = ["aboveyz","abrodyz","abslute","absoryz","actresz","gaswxyz"]
 * Output: [1,1,3,2,4,0]
 * Explanation:
 * 1 valid word for "aboveyz" : "aaaa"
 * 1 valid word for "abrodyz" : "aaaa"
 * 3 valid words for "abslute" : "aaaa", "asas", "able"
 * 2 valid words for "absoryz" : "aaaa", "asas"
 * 4 valid words for "actresz" : "aaaa", "asas", "actt", "access"
 * There are no valid words for "gaswxyz" cause none of the words in the list contains letter 'g'.
 *
 * Example 2:
 *
 * Input: words = ["apple","pleas","please"], puzzles = ["aelwxyz","aelpxyz","aelpsxy","saelpxy","xaelpsy"]
 * Output: [0,1,3,2,0]
 *
 * Constraints:
 *
 * 1 <= words.length <= 10^5
 * 4 <= words[i].length <= 50
 * 1 <= puzzles.length <= 10^4
 * puzzles[i].length == 7
 * words[i] and puzzles[i] consist of lowercase English letters.
 * Each puzzles[i] does not contain repeated characters.
 *
 */
public class NumberOfValidWordsForEachPuzzle {

    // V0
    // IDEA: BITMASK + HASH MAP + SUBSET ENUMERATION
    //       a word only matters by its SET of letters -> compress it to a 26-bit mask and
    //       count how many words share each mask.
    //       a puzzle has exactly 7 letters -> only 2^7 = 128 subsets of its letter mask.
    //       for every subset containing the puzzle's first letter, add count[subset]
    //       (exactly the words whose letter set equals that subset).
    /**
     * time = O(L + P * 2^7), L = total length of words, P = puzzles.length
     * space = O(W), W = words.length
     */
    public List<Integer> findNumOfValidWords(String[] words, String[] puzzles) {
        // letter-set mask -> number of words with exactly that set
        Map<Integer, Integer> maskCount = new HashMap<>();
        for (String word : words) {
            int mask = toMask(word);
            maskCount.put(mask, maskCount.getOrDefault(mask, 0) + 1);
        }

        List<Integer> result = new ArrayList<>();
        for (String puzzle : puzzles) {
            int firstBit = 1 << (puzzle.charAt(0) - 'a');
            int puzzleMask = toMask(puzzle);

            int total = 0;
            // walk every subset of puzzleMask, from puzzleMask itself down to 0:
            // (subset - 1) & puzzleMask is the next smaller subset of puzzleMask
            int subset = puzzleMask;
            while (true) {
                if ((subset & firstBit) != 0) {
                    total += maskCount.getOrDefault(subset, 0);
                }
                if (subset == 0) {
                    break;
                }
                subset = (subset - 1) & puzzleMask;
            }
            result.add(total);
        }
        return result;
    }

    /** Bit c is set when letter 'a' + c occurs in the string. */
    private int toMask(String str) {
        int mask = 0;
        for (char c : str.toCharArray()) {
            mask |= 1 << (c - 'a');
        }
        return mask;
    }
}
