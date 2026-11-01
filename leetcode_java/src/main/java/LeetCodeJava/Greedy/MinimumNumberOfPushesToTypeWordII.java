package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-pushes-to-type-word-ii/description/

import java.util.*;

/**
 * 3016. Minimum Number of Pushes to Type Word II
 * Medium
 *
 * You are given a string word containing lowercase English letters.
 *
 * Telephone keypads have keys mapped with distinct collections of lowercase English letters, which can be used to form words by pushing them. For example, the key 2 is mapped with ["a","b","c"], we need to push the key one time to type "a", two times to type "b", and three times to type "c".
 *
 * It is allowed to remap the keys numbered 2 to 9 to distinct collections of letters. The keys can be remapped to any amount of letters, but each letter must be mapped to exactly one key. You need to find the minimum number of times the keys will be pushed to type the string word.
 *
 * Return the minimum number of pushes needed to type word after remapping the keys.
 *
 * An example mapping of letters to keys on a telephone keypad is given below. Note that 1, *, #, and 0 do not map to any letters.
 *
 *
 * Example 1:
 *
 * Input: word = "abcde"
 * Output: 5
 * Explanation: The remapped keypad given in the image provides the minimum cost.
 * "a" -> one push on key 2
 * "b" -> one push on key 3
 * "c" -> one push on key 4
 * "d" -> one push on key 5
 * "e" -> one push on key 6
 * Total cost is 1 + 1 + 1 + 1 + 1 = 5.
 * It can be shown that no other mapping can provide a lower cost.
 *
 * Example 2:
 *
 * Input: word = "xyzxyzxyzxyz"
 * Output: 12
 * Explanation: The remapped keypad given in the image provides the minimum cost.
 * "x" -> one push on key 2
 * "y" -> one push on key 3
 * "z" -> one push on key 4
 * Total cost is 1 * 4 + 1 * 4 + 1 * 4 = 12
 * It can be shown that no other mapping can provide a lower cost.
 * Note that the key 9 is not mapped to any letter: it is not necessary to map letters to every key, but to map all the letters.
 *
 * Example 3:
 *
 * Input: word = "aabbccddeeffgghhiiiiii"
 * Output: 24
 * Explanation: The remapped keypad given in the image provides the minimum cost.
 * "a" -> one push on key 2
 * "b" -> one push on key 3
 * "c" -> one push on key 4
 * "d" -> one push on key 5
 * "e" -> one push on key 6
 * "f" -> one push on key 7
 * "g" -> one push on key 8
 * "h" -> two pushes on key 9
 * "i" -> one push on key 9
 * Total cost is 1 * 2 + 1 * 2 + 1 * 2 + 1 * 2 + 1 * 2 + 1 * 2 + 1 * 2 + 2 * 2 + 1 * 6 = 24.
 * It can be shown that no other mapping can provide a lower cost.
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 10^5
 * word consists of lowercase English letters.
 *
 */
public class MinimumNumberOfPushesToTypeWordII {

    private static final int KEYS = 8; // keys 2..9

    // V0
    // IDEA: EXCHANGE ARGUMENT - THE MOST FREQUENT LETTERS GET THE CHEAPEST SLOTS
    /**
     *  Sort letter frequencies descending; the i-th letter goes into slot
     *  i / 8 + 1 and costs freq * (i / 8 + 1). Swapping a more frequent letter
     *  into a cheaper slot never hurts, so this order is optimal.
     *
     *  time = O(N + 26 log 26)
     *  space = O(26)
     */
    public int minimumPushes(String word) {
        Integer[] frequency = new Integer[26];
        Arrays.fill(frequency, 0);
        for (int i = 0; i < word.length(); i++) {
            frequency[word.charAt(i) - 'a']++;
        }
        Arrays.sort(frequency, Collections.reverseOrder());

        int pushes = 0;
        for (int i = 0; i < 26; i++) {
            if (frequency[i] == 0) {
                break; // remaining letters do not appear
            }
            pushes += frequency[i] * (i / KEYS + 1);
        }
        return pushes;
    }
}
