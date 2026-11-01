package LeetCodeJava.Greedy;

// https://leetcode.com/problems/next-palindrome-using-same-digits/description/

/**
 * 1842. Next Palindrome Using Same Digits
 * Hard
 *
 * You are given a numeric string num, representing a very large palindrome.
 *
 * Return the smallest palindrome larger than num that can be created by
 * rearranging its digits. If no such palindrome exists, return an empty string "".
 *
 * A palindrome is a number that reads the same backward as forward.
 *
 *
 * Example 1:
 *
 * Input: num = "1221"
 * Output: "2112"
 * Explanation: The next palindrome larger than "1221" is "2112".
 *
 * Example 2:
 *
 * Input: num = "32123"
 * Output: ""
 * Explanation: No palindromes larger than "32123" can be made by rearranging its digits.
 *
 * Example 3:
 *
 * Input: num = "45544554"
 * Output: "54455445"
 * Explanation: The next palindrome larger than "45544554" is "54455445".
 *
 *
 * Constraints:
 *
 * 1 <= num.length <= 10^5
 * num is a palindrome.
 *
 */
public class NextPalindromeUsingSameDigits {

    // V0
    // IDEA: NEXT PERMUTATION OF THE FIRST HALF ONLY, THEN MIRROR IT
    /**
     * time = O(N)
     * space = O(N)
     */
    public String nextPalindrome(String num) {
        char[] digits = num.toCharArray();
        int n = digits.length;
        // a palindrome is fixed by its first half; the middle digit (odd n) never moves
        int half = n / 2;

        // 1) rightmost ascent inside the first half
        int pivot = half - 2;
        while (pivot >= 0 && digits[pivot] >= digits[pivot + 1]) {
            pivot--;
        }
        // the half is already its largest arrangement -> no bigger palindrome exists
        if (pivot < 0) {
            return "";
        }

        // 2) swap the pivot with the rightmost digit in the half that is bigger than it
        int successor = half - 1;
        while (digits[successor] <= digits[pivot]) {
            successor--;
        }
        swap(digits, pivot, successor);

        // 3) reverse the tail of the half so it is the smallest possible
        reverse(digits, pivot + 1, half - 1);

        // 4) mirror the first half onto the second half
        for (int i = 0; i < half; i++) {
            digits[n - 1 - i] = digits[i];
        }
        return new String(digits);
    }

    /** Swaps a[i] and a[j]. */
    private void swap(char[] a, int i, int j) {
        char temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /** Reverses a[from..to] in place (both ends inclusive). */
    private void reverse(char[] a, int from, int to) {
        while (from < to) {
            swap(a, from, to);
            from++;
            to--;
        }
    }
}
