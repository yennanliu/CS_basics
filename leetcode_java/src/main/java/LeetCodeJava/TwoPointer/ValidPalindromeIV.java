package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/valid-palindrome-iv/description/

/**
 * 2330. Valid Palindrome IV
 * Medium
 *
 * You are given a 0-indexed string s consisting of only lowercase English letters. In one operation, you can change any character of s to any other character.
 *
 * Return true if you can make s a palindrome after performing exactly one or two operations, or return false otherwise.
 *
 *
 * Example 1:
 *
 * Input: s = "abcdba"
 * Output: true
 * Explanation: One way to make s a palindrome using 1 operation is:
 * - Change s[2] to 'd'. Now, s = "abddba".
 * One operation could be performed to make s a palindrome so return true.
 *
 * Example 2:
 *
 * Input: s = "aa"
 * Output: true
 * Explanation: One way to make s a palindrome using 2 operations is:
 * - Change s[0] to 'b'. Now, s = "ba".
 * - Change s[1] to 'b'. Now, s = "bb".
 * Two operations could be performed to make s a palindrome so return true.
 *
 * Example 3:
 *
 * Input: s = "abcdef"
 * Output: false
 * Explanation: It is not possible to make s a palindrome using one or two operations so return false.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists only of lowercase English letters.
 *
 */
public class ValidPalindromeIV {

    // V0
    // IDEA: COUNT THE MISMATCHED MIRROR PAIRS WITH TWO POINTERS — THE ANSWER IS "AT MOST 2"
    //  each mismatched pair s[left] != s[right] costs exactly one operation, so at most 2 of
    //  them may exist. an already-palindromic string still works: rewrite one mirror pair (or
    //  the middle character twice) and the string stays a palindrome.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean makePalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        int mismatches = 0;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                mismatches++;
                if (mismatches > 2) {
                    return false;
                }
            }
            left++;
            right--;
        }
        return true;
    }
}
