package LeetCodeJava.Greedy;

// https://leetcode.com/problems/construct-smallest-number-from-di-string/description/

import java.util.*;

/**
 * 2375. Construct Smallest Number From DI String
 * Medium
 *
 * You are given a 0-indexed string pattern of length n consisting of the characters 'I' meaning increasing and 'D' meaning decreasing.
 *
 * A 0-indexed string num of length n + 1 is created using the following conditions:
 *
 * num consists of the digits '1' to '9', where each digit is used at most once.
 * If pattern[i] == 'I', then num[i] < num[i + 1].
 * If pattern[i] == 'D', then num[i] > num[i + 1].
 *
 * Return the lexicographically smallest possible string num that meets the conditions.
 *
 *
 * Example 1:
 *
 * Input: pattern = "IIIDIDDD"
 * Output: "123549876"
 * Explanation:
 * At indices 0, 1, 2, and 4 we must have that num[i] < num[i+1].
 * At indices 3, 5, 6, and 7 we must have that num[i] > num[i+1].
 * Some possible values of num are "245639871", "135749862", and "123849765".
 * It can be proven that "123549876" is the smallest possible num that meets the conditions.
 * Note that "123414321" is not possible because the digit '1' is used more than once.
 *
 * Example 2:
 *
 * Input: pattern = "DDD"
 * Output: "4321"
 * Explanation:
 * Some possible values of num are "9876", "7321", and "8742".
 * It can be proven that "4321" is the smallest possible num that meets the conditions.
 *
 *
 * Constraints:
 *
 * 1 <= pattern.length <= 8
 * pattern consists of only the characters 'I' and 'D'.
 *
 */
public class ConstructSmallestNumberFromDIString {

    // V0
    // IDEA: STACK (PUSH 1, 2, 3, ... AND FLUSH AT EVERY 'I' AND AT THE END)
    //       handing out digits in increasing order keeps the number smallest;
    //       a run of 'D's needs its digits descending, which is exactly what popping gives.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String smallestNumber(String pattern) {
        StringBuilder result = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i <= pattern.length(); i++) {
            stack.push((char) ('1' + i));
            // an 'I' (or the end) releases everything pending, in reverse order
            if (i == pattern.length() || pattern.charAt(i) == 'I') {
                while (!stack.isEmpty()) {
                    result.append(stack.pop());
                }
            }
        }
        return result.toString();
    }
}
