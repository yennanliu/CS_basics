package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-string-balanced/

/**
 *  1963. Minimum Number of Swaps to Make the String Balanced
 *  Medium
 *
 *  You are given a 0-indexed string s of even length n. The string consists of exactly
 *  n / 2 opening brackets '[' and n / 2 closing brackets ']'.
 *
 *  A string is called balanced if and only if:
 *   - It is the empty string, or
 *   - It can be written as AB, where both A and B are balanced strings, or
 *   - It can be written as [C], where C is a balanced string.
 *
 *  You may swap the brackets at any two indices any number of times.
 *  Return the minimum number of swaps to make s balanced.
 *
 *  Example 1:
 *  Input: s = "][]["
 *  Output: 1
 *  Explanation: You can make the string balanced by swapping index 0 with index 3.
 *
 *  Example 2:
 *  Input: s = "]]][[["
 *  Output: 2
 *
 *  Example 3:
 *  Input: s = "[]"
 *  Output: 0
 *
 *  Constraints:
 *   n == s.length
 *   2 <= n <= 10^6
 *   n is even.
 *   s[i] is either '[' or ']'.
 *   The number of opening brackets '[' equals n / 2.
 */
public class MinimumNumberOfSwapsToMakeTheStringBalanced {

    // V0
    // IDEA: greedy - count unmatched ']', answer = ceil(unmatched / 2)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int minSwaps(String s) {
        int unmatched = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '[') {
                if (unmatched > 0) {
                    unmatched--;
                }
            } else {
                unmatched++;
            }
        }
        return (unmatched + 1) / 2;
    }

    // V1
    // IDEA: same greedy, expressed via running balance; each swap resets balance to 1
    /**
     * time = O(n)
     * space = O(1)
     */
    public int minSwaps_1(String s) {
        int balance = 0;
        int swaps = 0;
        for (int i = 0; i < s.length(); i++) {
            balance += (s.charAt(i) == '[') ? 1 : -1;
            if (balance < 0) {
                swaps++;
                balance = 1;
            }
        }
        return swaps;
    }
}
