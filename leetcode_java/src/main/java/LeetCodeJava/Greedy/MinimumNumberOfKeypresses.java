package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-keypresses/description/
// https://leetcode.ca/2022-02-14-2268-Minimum-Number-of-Keypresses/

import java.util.Arrays;

/**
 * 2268. Minimum Number of Keypresses
 * Medium
 *
 * You have a keypad with 9 buttons, numbered from 1 to 9, each mapped to lowercase English letters. You can
 * choose which characters each button is matched to as long as:
 *
 * All 26 lowercase English letters are mapped to.
 * Each character is mapped to by exactly 1 button.
 * Each button maps to at most 3 characters.
 *
 * To type the first character matched to a button, you press the button once. To type the second character,
 * you press the button twice, and so on.
 *
 * Given a string s, return the minimum number of keypresses needed to type s using your keypad.
 *
 * Note that the characters mapped to by each button, and the order they are mapped in cannot be changed.
 *
 *
 * Example 1:
 *
 * Input: s = "apple"
 * Output: 5
 *
 * Example 2:
 *
 * Input: s = "abcdefghijkl"
 * Output: 15
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of lowercase English letters.
 *
 */
public class MinimumNumberOfKeypresses {

    private static final int BUTTONS = 9;

    // V0
    // IDEA: GREEDY (REARRANGEMENT INEQUALITY) - THE MOST FREQUENT LETTERS GET THE 1-PRESS SLOTS
    /**
     * time = O(N + 26 log 26)
     * space = O(26)
     */
    public int minimumKeypresses(String s) {
        int[] frequency = new int[26];
        for (int i = 0; i < s.length(); i++) {
            frequency[s.charAt(i) - 'a']++;
        }

        // ascending sort; walk it from the end to visit frequencies in descending order
        Arrays.sort(frequency);

        // 9 slots cost 1 press, the next 9 cost 2, the last 8 cost 3
        int presses = 0;
        int rank = 0;
        for (int i = 25; i >= 0; i--) {
            int costPerPress = rank / BUTTONS + 1;
            presses += costPerPress * frequency[i];
            rank++;
        }
        return presses;
    }
}
