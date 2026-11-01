package LeetCodeJava.HashTable;

// https://leetcode.com/problems/check-if-number-has-equal-digit-count-and-digit-value/description/

/**
 * 2283. Check if Number Has Equal Digit Count and Digit Value
 * Easy
 *
 * You are given a 0-indexed string num of length n consisting of digits.
 *
 * Return true if for every index i in the range 0 <= i < n, the digit i occurs num[i] times in num, otherwise return false.
 *
 *
 * Example 1:
 *
 * Input: num = "1210"
 * Output: true
 * Explanation:
 * num[0] = '1'. The digit 0 occurs once in num.
 * num[1] = '2'. The digit 1 occurs twice in num.
 * num[2] = '1'. The digit 2 occurs once in num.
 * num[3] = '0'. The digit 3 occurs zero times in num.
 * The condition holds true for every index in "1210", so return true.
 *
 * Example 2:
 *
 * Input: num = "030"
 * Output: false
 * Explanation:
 * num[0] = '0'. The digit 0 should occur zero times, but actually occurs twice in num.
 * num[1] = '3'. The digit 1 should occur three times, but actually occurs zero times in num.
 * num[2] = '0'. The digit 2 occurs zero times in num.
 * The indices 0 and 1 both violate the condition, so return false.
 *
 *
 * Constraints:
 *
 * n == num.length
 * 1 <= n <= 10
 * num consists of digits.
 *
 */
public class CheckIfNumberHasEqualDigitCountAndDigitValue {

    // V0
    // IDEA: COUNT THE DIGITS ONCE, THEN CHECK EVERY INDEX
    //   index i must hold "how many times digit i appears in num".
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean digitCount(String num) {
        // digitCount[d] = occurrences of digit d in num
        int[] digitCount = new int[10];
        for (int i = 0; i < num.length(); i++) {
            digitCount[num.charAt(i) - '0']++;
        }

        // n <= 10, so every index i is itself a valid digit
        for (int i = 0; i < num.length(); i++) {
            int expected = num.charAt(i) - '0';
            if (digitCount[i] != expected) {
                return false;
            }
        }
        return true;
    }
}
