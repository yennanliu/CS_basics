package LeetCodeJava.Greedy;

// https://leetcode.com/problems/lexicographically-smallest-string-after-a-swap/description/

/**
 * 3216. Lexicographically Smallest String After a Swap
 * Easy
 *
 * Given a string s containing only digits, return the lexicographically smallest string that can be obtained after swapping adjacent digits in s with the same parity at most once.
 *
 * Digits have the same parity if both are odd or both are even. For example, 5 and 9, as well as 2 and 4, have the same parity, while 6 and 9 do not.
 *
 *
 * Example 1:
 *
 * Input: s = "45320"
 * Output: "43520"
 * Explanation:
 * s[1] == '5' and s[2] == '3' both have the same parity, and swapping them results in the lexicographically smallest string.
 *
 * Example 2:
 *
 * Input: s = "001"
 * Output: "001"
 * Explanation:
 * There is no need to perform a swap because s is already the lexicographically smallest.
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 100
 * s consists only of digits.
 *
 */
public class LexicographicallySmallestStringAfterASwap {

    // V0
    // IDEA: ONE SWAP, SO TAKE THE EARLIEST ONE THAT ACTUALLY HELPS
    /**
     *  Only adjacent digits of the same parity may be swapped, at most once.
     *  The swap that helps most is the earliest one where the left digit is
     *  larger than the right one; if none exists, keep s.
     *
     *  time = O(N)
     *  space = O(N)
     */
    public String getSmallestString(String s) {
        char[] digits = s.toCharArray();
        for (int i = 0; i + 1 < digits.length; i++) {
            int left = digits[i] - '0';
            int right = digits[i + 1] - '0';
            boolean sameParity = left % 2 == right % 2;
            if (sameParity && left > right) {
                digits[i] = (char) ('0' + right);
                digits[i + 1] = (char) ('0' + left);
                break;
            }
        }
        return new String(digits);
    }
}
