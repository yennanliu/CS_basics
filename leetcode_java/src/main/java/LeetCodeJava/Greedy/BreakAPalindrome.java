package LeetCodeJava.Greedy;

// https://leetcode.com/problems/break-a-palindrome/description/

/**
 * 1328. Break a Palindrome
 * Medium
 *
 * Given a palindromic string of lowercase English letters palindrome, replace exactly one character
 * with any lowercase English letter so that the resulting string is not a palindrome and that it is
 * the lexicographically smallest one possible.
 *
 * Return the resulting string. If there is no way to replace a character to make it not a palindrome,
 * return an empty string.
 *
 * A string a is lexicographically smaller than a string b (of the same length) if in the first
 * position where a and b differ, a has a character strictly smaller than the corresponding character
 * in b. For example, "abcc" is lexicographically smaller than "abcd" because the first position
 * they differ is at the fourth character, and 'c' is smaller than 'd'.
 *
 *
 * Example 1:
 *
 * Input: palindrome = "abccba"
 * Output: "aaccba"
 * Explanation: There are many ways to make "abccba" not a palindrome, such as "zbccba", "aaccba",
 * and "abacba".
 * Of all the ways, "aaccba" is the lexicographically smallest.
 *
 * Example 2:
 *
 * Input: palindrome = "a"
 * Output: ""
 * Explanation: There is no way to replace a single character to make "a" not a palindrome,
 * so return an empty string.
 *
 *
 * Constraints:
 *
 * 1 <= palindrome.length <= 1000
 * palindrome consists of only lowercase English letters.
 *
 */
public class BreakAPalindrome {

    // V0
    // IDEA: GREEDY - FIRST NON-'a' IN THE LEFT HALF BECOMES 'a', ELSE THE LAST CHAR BECOMES 'b'
    //       lowering the earliest possible char to 'a' gives the smallest string, and any
    //       left-half change breaks the mirror. the middle of an odd-length palindrome is
    //       skipped on purpose: changing it keeps the palindrome.
    //       if the left half is all 'a', raise the LAST char to 'b' (smallest possible bump).
    //       length 1 -> every replacement is still a palindrome -> "".
    /**
     * time = O(N)
     * space = O(N)
     */
    public String breakPalindrome(String palindrome) {
        int n = palindrome.length();
        if (n < 2) {
            return "";
        }

        char[] chars = palindrome.toCharArray();
        for (int i = 0; i < n / 2; i++) {
            if (chars[i] != 'a') {
                chars[i] = 'a';
                return new String(chars);
            }
        }

        chars[n - 1] = 'b';
        return new String(chars);
    }
}
