package LeetCodeJava.BitManipulation;

import java.util.ArrayList;
import java.util.List;

// https://leetcode.com/problems/number-of-wonderful-substrings/description/
/**
 * 1915. Number of Wonderful Substrings
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * A wonderful string is a string where at most one letter appears an odd number of times.
 *
 * For example, "ccjjc" and "abab" are wonderful, but "ab" is not.
 * Given a string word that consists of the first ten lowercase English letters ('a' through 'j'), return the number of wonderful non-empty substrings in word. If the same substring appears multiple times in word, then count each occurrence separately.
 *
 * A substring is a contiguous sequence of characters in a string.
 *
 *
 *
 * Example 1:
 *
 * Input: word = "aba"
 * Output: 4
 * Explanation: The four wonderful substrings are underlined below:
 * - "aba" -> "a"
 * - "aba" -> "b"
 * - "aba" -> "a"
 * - "aba" -> "aba"
 * Example 2:
 *
 * Input: word = "aabb"
 * Output: 9
 * Explanation: The nine wonderful substrings are underlined below:
 * - "aabb" -> "a"
 * - "aabb" -> "aa"
 * - "aabb" -> "aab"
 * - "aabb" -> "aabb"
 * - "aabb" -> "a"
 * - "aabb" -> "abb"
 * - "aabb" -> "b"
 * - "aabb" -> "bb"
 * - "aabb" -> "b"
 * Example 3:
 *
 * Input: word = "he"
 * Output: 2
 * Explanation: The two wonderful substrings are underlined below:
 * - "he" -> "h"
 * - "he" -> "e"
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 105
 * word consists of lowercase English letters from 'a' to 'j'.
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 */
public class BitwiseOROfEvenNumbersInAnArray {

    // V0
    // IDEA: BIT OP
    // https://leetcode.com/contest/weekly-contest-468/problems/bitwise-or-of-even-numbers-in-an-array/description/
    public int evenNumberBitwiseORs(int[] nums) {
        // edge
        if (nums.length == 0) {
            return 0;
        }
        List<Integer> list = new ArrayList<>();
        for (int x : nums) {
            if (x % 2 == 0) {
                list.add(x);
            }
        }
        if (list.isEmpty()) {
            return 0;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        int tmp = 0;
        for (int i = 0; i < list.size(); i++) {
            // tmp = tmp | (list.get(i) | list.get(i+1));
            tmp = (tmp | list.get(i));
        }

        return tmp;
    }

    // V1
    // https://leetcode.com/problems/bitwise-or-of-even-numbers-in-an-array/solutions/7209597/clean-easy-5-lines-beats-100-easy-explan-w9qu/
    public int evenNumberBitwiseORs_1(int[] nums) {
        int count = 0;
        for (int num : nums) {
            if (num % 2 == 0) {
                count |= num;
            }
        }
        return count;
    }


}
