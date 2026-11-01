package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1239. Maximum Length of a Concatenated String with Unique Characters
 * Medium
 *
 * You are given an array of strings arr. A string s is formed by the concatenation of a
 * subsequence of arr that has unique characters.
 *
 * Return the maximum possible length of s.
 *
 * A subsequence is an array that can be derived from another array by deleting some or no
 * elements without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: arr = ["un","iq","ue"]
 * Output: 4
 * Explanation: All the valid concatenations are:
 * - ""
 * - "un"
 * - "iq"
 * - "ue"
 * - "uniq" ("un" + "iq")
 * - "ique" ("iq" + "ue")
 * Maximum length is 4.
 *
 * Example 2:
 *
 * Input: arr = ["cha","r","act","ers"]
 * Output: 6
 * Explanation: Possible longest valid concatenations are "chaers" ("cha" + "ers") and
 * "acters" ("act" + "ers").
 *
 * Example 3:
 *
 * Input: arr = ["abcdefghijklmnopqrstuvwxyz"]
 * Output: 26
 * Explanation: The only string in arr has all 26 characters.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 16
 * 1 <= arr[i].length <= 26
 * arr[i] contains only lowercase English letters.
 *
 */
public class MaximumLengthOfAConcatenatedStringWithUniqueCharacters {

    /** Marks a word that repeats a letter and can never be used. */
    private static final int INVALID = -1;

    // V0
    // IDEA: BITMASK DP (GROW THE LIST OF REACHABLE "LETTERS USED" STATES)
    /**
     * a state is a 26-bit mask: bit k set = letter 'a' + k is already used
     * reachable starts as [0] (nothing chosen)
     *
     * for each word with mask w (skipped if it repeats a letter itself):
     *   for every state s reachable BEFORE this word:
     *     if w and s share no letter (w & s == 0), then w | s is reachable too
     *
     * answer = max number of set bits over all reachable states
     */
    /**
     * time = O(2^N + L), N = arr.length, L = total characters
     * space = O(2^N)
     */
    public int maxLength(List<String> arr) {
        List<Integer> reachable = new ArrayList<>();
        reachable.add(0);
        int best = 0;

        for (String word : arr) {
            int wordMask = toMask(word);
            if (wordMask == INVALID) {
                continue;
            }
            // only the states that existed before this word: each word is used at most once
            int existing = reachable.size();
            for (int i = 0; i < existing; i++) {
                int state = reachable.get(i);
                // no letter in common -> the two can be concatenated
                if ((state & wordMask) == 0) {
                    int combined = state | wordMask;
                    reachable.add(combined);
                    // number of set bits = number of distinct letters = length
                    best = Math.max(best, Integer.bitCount(combined));
                }
            }
        }
        return best;
    }

    // V1
    // IDEA: BACKTRACKING (PICK / SKIP EACH WORD), BITMASK OF USED LETTERS
    /**
     * time = O(2^N)
     * space = O(N)
     */
    public int maxLength_1(List<String> arr) {
        int[] masks = new int[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            masks[i] = toMask(arr.get(i));
        }
        return backtrack(masks, 0, 0);
    }

    /** Best length reachable from word index onward, given the letters already used. */
    private int backtrack(int[] masks, int index, int used) {
        if (index == masks.length) {
            return Integer.bitCount(used);
        }
        // skip this word
        int best = backtrack(masks, index + 1, used);
        // take this word, if it is valid and shares no letter with what is used
        int wordMask = masks[index];
        if (wordMask != INVALID && (wordMask & used) == 0) {
            best = Math.max(best, backtrack(masks, index + 1, used | wordMask));
        }
        return best;
    }

    /** 26-bit mask of the word's letters, or INVALID if a letter repeats. */
    private int toMask(String word) {
        int mask = 0;
        for (char ch : word.toCharArray()) {
            int bit = 1 << (ch - 'a');
            // the letter is already in this word
            if ((mask & bit) != 0) {
                return INVALID;
            }
            mask |= bit;
        }
        return mask;
    }
}
