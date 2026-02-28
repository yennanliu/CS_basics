package LeetCodeJava.Stack;

// https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 *  1209. Remove All Adjacent Duplicates in String II
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a string s and an integer k, a k duplicate removal consists of choosing k adjacent and equal letters from s and removing them, causing the left and the right side of the deleted substring to concatenate together.
 *
 * We repeatedly make k duplicate removals on s until we no longer can.
 *
 * Return the final string after all such duplicate removals have been made. It is guaranteed that the answer is unique.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "abcd", k = 2
 * Output: "abcd"
 * Explanation: There's nothing to delete.
 * Example 2:
 *
 * Input: s = "deeedbbcccbdaa", k = 3
 * Output: "aa"
 * Explanation:
 * First delete "eee" and "ccc", get "ddbbbdaa"
 * Then delete "bbb", get "dddaa"
 * Finally delete "ddd", get "aa"
 * Example 3:
 *
 * Input: s = "pbbcggttciiippooaais", k = 2
 * Output: "ps"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 105
 * 2 <= k <= 104
 * s only contains lowercase English letters.
 *
 *
 */
public class RemoveAllAdjacentDuplicatesInString2 {

    // V0
//    public String removeDuplicates(String s, int k) {
//
//    }

    // V0-1
    // IDEA: STACK (gemini)
    public String removeDuplicates_0_1(String s, int k) {
        // We store an int array: {character_as_int, count}
        Stack<int[]> st = new Stack<>();

        for (char ch : s.toCharArray()) {
            if (!st.isEmpty() && st.peek()[0] == ch) {
                // If it matches the top, increment the count
                st.peek()[1]++;

                // If the count reaches K, remove the whole block
                if (st.peek()[1] == k) {
                    st.pop();
                }
            } else {
                // Otherwise, it's a new character, start count at 1
                st.push(new int[] { ch, 1 });
            }
        }

        // Build the final string
        StringBuilder sb = new StringBuilder();
        for (int[] pair : st) {
            char c = (char) pair[0];
            int count = pair[1];
            for (int i = 0; i < count; i++) {
                sb.append(c);
            }
        }

        return sb.toString();
    }


    // V0-2
    // IDEA: Deque (GPT)
    public String removeDuplicates_0_2(String s, int k) {

        Deque<int[]> stack = new LinkedList<>();
        // int[0] = char
        // int[1] = frequency

        for (char c : s.toCharArray()) {

            if (!stack.isEmpty() && stack.peek()[0] == c) {
                stack.peek()[1]++;

                if (stack.peek()[1] == k) {
                    stack.pop(); // remove when count hits k
                }
            } else {
                stack.push(new int[] { c, 1 });
            }
        }

        // rebuild string
        StringBuilder sb = new StringBuilder();
        for (int[] pair : stack) {
            for (int i = 0; i < pair[1]; i++) {
                sb.append((char) pair[0]);
            }
        }

        return sb.reverse().toString();
    }



    // V1
    // https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/solutions/1161097/java-stack-easy-by-himanshuchhikara-f923/
    public String removeDuplicates_1(String s, int k) {

        Stack<Character> charSt = new Stack<>();
        Stack<Integer> countSt = new Stack<>();

        for (char ch : s.toCharArray()) {
            if (charSt.size() > 0 && charSt.peek() == ch)
                countSt.push(countSt.peek() + 1);
            else
                countSt.push(1);

            charSt.push(ch);
            if (countSt.peek() == k) {
                for (int i = 0; i < k; i++) {
                    charSt.pop();
                    countSt.pop();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        while (charSt.size() > 0)
            sb.append(charSt.pop());
        return sb.reverse().toString();
    }


    // V2

    // V3
    // https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/solutions/392939/pythoncjava-stack-based-solution-clean-c-3xk5/
    public String removeDuplicates_3(String s, int k) {
        // ArrayDeque has better performance than Stack and LinkedList
        ArrayDeque<Adjacent> st = new ArrayDeque<>(s.length());
        for (char c : s.toCharArray()) {
            if (!st.isEmpty() && st.peekLast().ch == c) {
                st.peekLast().freq++; // Increase the frequency
            } else {
                st.addLast(new Adjacent(c, 1));
            }
            if (st.peekLast().freq == k) // If reach enough k duplicate letters -> then remove
                st.removeLast();
        }
        StringBuilder sb = new StringBuilder();
        // TODO: fix below for java 8
//        for (Adjacent a : st) {
//            sb.append(String.valueOf(a.ch).repeat(a.freq));
//        }
        return sb.toString();
    }
    class Adjacent {
        char ch;
        int freq;
        public Adjacent(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }
    }



}
