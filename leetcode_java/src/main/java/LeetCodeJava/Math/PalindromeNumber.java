package LeetCodeJava.Math;

// https://leetcode.com/problems/palindrome-number/

/**
 *  9. Palindrome Number
 *  Easy
 *
 *  Given an integer x, return true if x is a palindrome, and false otherwise.
 *  An integer is a palindrome when it reads the same forward and backward.
 *  For example, 121 is a palindrome while 123 is not.
 *
 *  Example 1:
 *    Input: x = 121
 *    Output: true
 *
 *  Example 2:
 *    Input: x = -121
 *    Output: false   (reads 121- from right to left)
 *
 *  Example 3:
 *    Input: x = 10
 *    Output: false
 *
 *  Constraints:
 *    -2^31 <= x <= 2^31 - 1
 *
 *  Follow up: Could you solve it without converting the integer to a string?
 */
public class PalindromeNumber {

    // V0
    // IDEA: reverse only the second half of the digits, compare with the first half
    /**
     * time = O(log x)
     * space = O(1)
     */
    public boolean isPalindrome(int x) {

        // negatives are never palindromes;
        // a number ending with 0 (except 0 itself) can't be one either
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int reverted = 0;
        while (x > reverted) {
            reverted = reverted * 10 + x % 10;
            x /= 10;
        }

        // even digit count: x == reverted
        // odd  digit count: drop the middle digit via reverted / 10
        return x == reverted || x == reverted / 10;
    }
}
