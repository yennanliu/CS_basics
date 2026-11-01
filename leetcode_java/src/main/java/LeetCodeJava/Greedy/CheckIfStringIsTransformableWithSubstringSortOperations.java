package LeetCodeJava.Greedy;

// https://leetcode.com/problems/check-if-string-is-transformable-with-substring-sort-operations/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 1585. Check If String Is Transformable With Substring Sort Operations
 * Hard
 *
 * Given two strings s and t, transform string s into string t using the following
 * operation any number of times:
 *
 * Choose a non-empty substring in s and sort it in place so the characters are in
 * ascending order.
 * For example, applying the operation on the underlined substring in "14234"
 * results in "12344".
 *
 * Return true if it is possible to transform s into t. Otherwise, return false.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "84532", t = "34852"
 * Output: true
 * Explanation: "84532" (from index 2 to 3) -> "84352"
 *              "84352" (from index 0 to 2) -> "34852"
 *
 * Example 2:
 *
 * Input: s = "34521", t = "23415"
 * Output: true
 * Explanation: "34521" -> "23451"
 *              "23451" -> "23415"
 *
 * Example 3:
 *
 * Input: s = "12345", t = "12435"
 * Output: false
 *
 *
 * Constraints:
 *
 * s.length == t.length
 * 1 <= s.length <= 10^5
 * s and t consist of only digits.
 *
 */
public class CheckIfStringIsTransformableWithSubstringSortOperations {

    // V0
    // IDEA: GREEDY + QUEUE PER DIGIT (A SORT = BUBBLING A DIGIT LEFT PAST BIGGER ONES)
    //       sorting substrings == repeatedly swapping an adjacent pair whose left
    //       char is bigger, so a digit can move LEFT only across strictly bigger
    //       digits. Build t from the left: for t[i] = d take the earliest unused d
    //       in s at index p; it can reach the front iff no unused smaller digit
    //       sits before p.
    /**
     * time = O(10 * N)
     * space = O(N)
     */
    public boolean isTransformable(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        // positions[d] = indices of digit d in s, in increasing order (unused only)
        List<Deque<Integer>> positions = new ArrayList<>();
        for (int d = 0; d < 10; d++) {
            positions.add(new ArrayDeque<>());
        }
        for (int i = 0; i < s.length(); i++) {
            positions.get(s.charAt(i) - '0').addLast(i);
        }

        for (int i = 0; i < t.length(); i++) {
            int digit = t.charAt(i) - '0';

            // s has no unused copy of this digit left
            if (positions.get(digit).isEmpty()) {
                return false;
            }
            int index = positions.get(digit).peekFirst();

            // a smaller unused digit in front of it would block it forever
            for (int smaller = 0; smaller < digit; smaller++) {
                if (!positions.get(smaller).isEmpty() && positions.get(smaller).peekFirst() < index) {
                    return false;
                }
            }
            positions.get(digit).pollFirst();
        }
        return true;
    }
}
