package LeetCodeJava.Greedy;

// https://leetcode.com/problems/replace-question-marks-in-string-to-minimize-its-value/description/

import java.util.*;

/**
 * 3081. Replace Question Marks in String to Minimize Its Value
 * Medium
 *
 * You are given a string s. s[i] is either a lowercase English letter or '?'.
 *
 * For a string t having length m containing only lowercase English letters, we define the function cost(i) for an index i as the number of characters equal to t[i] that appeared before it, i.e. in the range [0, i - 1].
 *
 * The value of t is the sum of cost(i) for all indices i.
 *
 * For example, for the string t = "aab":
 *
 * cost(0) = 0
 * cost(1) = 1
 * cost(2) = 0
 * Hence, the value of "aab" is 0 + 1 + 0 = 1.
 *
 * Your task is to replace all occurrences of '?' in s with any lowercase English letter so that the value of s is minimized.
 *
 * Return a string denoting the modified string with replaced occurrences of '?'. If there are multiple strings resulting in the minimum value, return the lexicographically smallest one.
 *
 *
 * Example 1:
 *
 * Input: s = "???"
 * Output: "abc"
 * Explanation: In this example, we can replace the occurrences of '?' to make s equal to "abc".
 * For "abc", cost(0) = 0, cost(1) = 0, and cost(2) = 0.
 * The value of "abc" is 0.
 * Some other modifications of s that have a value of 0 are "cba", "abz", and, "hey".
 * Among all of them, we choose the lexicographically smallest.
 *
 * Example 2:
 *
 * Input: s = "a?a?"
 * Output: "abac"
 * Explanation: In this example, the occurrences of '?' can be replaced to make s equal to "abac".
 * For "abac", cost(0) = 0, cost(1) = 0, cost(2) = 1, and cost(3) = 0.
 * The value of "abac" is 1.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s[i] is either a lowercase English letter or '?'.
 *
 */
public class ReplaceQuestionMarksInStringToMinimizeItsValue {

    // V0
    // IDEA: MIN-HEAP ON LETTER COUNTS, THEN SORT THE CHOSEN LETTERS
    /**
     *  A letter appearing f times adds 0 + 1 + ... + (f - 1) to the value, so
     *  the value only depends on the counts. Each '?' should take the letter
     *  with the lowest current count (ties -> the smaller letter), which a
     *  min-heap of (count, letter) gives. The chosen multiset is then placed
     *  in sorted order into the '?' slots for the lexicographically smallest
     *  string.
     *
     *  time = O(N log 26 + Q log Q)
     *  space = O(N)
     */
    public String minimizeStringValue(String s) {
        int[] count = new int[26];
        int questionMarks = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '?') {
                questionMarks++;
            } else {
                count[ch - 'a']++;
            }
        }

        // heap entry = {count, letter}; lowest count first, then smallest letter
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        });
        for (int letter = 0; letter < 26; letter++) {
            heap.offer(new int[] {count[letter], letter});
        }

        char[] chosen = new char[questionMarks];
        for (int q = 0; q < questionMarks; q++) {
            int[] top = heap.poll();
            chosen[q] = (char) ('a' + top[1]);
            heap.offer(new int[] {top[0] + 1, top[1]});
        }
        // the multiset is fixed; sorting it gives the smallest arrangement
        Arrays.sort(chosen);

        StringBuilder result = new StringBuilder();
        int next = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '?') {
                result.append(chosen[next]);
                next++;
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
