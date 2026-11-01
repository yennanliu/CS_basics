package LeetCodeJava.Math;

// https://leetcode.com/problems/count-binary-palindromic-numbers/description/

/**
 * 3677. Count Binary Palindromic Numbers
 * Hard
 *
 * You are given a non-negative integer n.
 *
 * A non-negative integer is called binary-palindromic if its binary
 * representation (written without leading zeros) reads the same forward and
 * backward.
 *
 * Return the number of integers k such that 0 <= k <= n and the binary
 * representation of k is a palindrome.
 *
 * Note: The number 0 is considered binary-palindromic, and its representation
 * is "0".
 *
 *
 * Example 1:
 *
 * Input: n = 9
 * Output: 6
 * Explanation:
 * The integers k in the range [0, 9] whose binary representations are
 * palindromes are:
 * 0 -> "0"
 * 1 -> "1"
 * 3 -> "11"
 * 5 -> "101"
 * 7 -> "111"
 * 9 -> "1001"
 * All other values in [0, 9] have non-palindromic binary forms. Therefore,
 * the count is 6.
 *
 * Example 2:
 *
 * Input: n = 0
 * Output: 1
 * Explanation:
 * Since "0" is a palindrome, the count is 1.
 *
 *
 * Constraints:
 *
 * 0 <= n <= 10^15
 *
 */
public class CountBinaryPalindromicNumbers {

    // V0
    // IDEA: A PALINDROME IS DETERMINED BY ITS FIRST HALF -- COUNT HALVES
    //       a length-L binary palindrome is fixed by its top h = ceil(L / 2) bits, and every
    //       h-bit string starting with 1 mirrors into exactly one -> 2^(h-1) palindromes of
    //       length L. the map "half -> palindrome" is increasing, so for n's own length the
    //       halves 2^(h-1) .. f-1 (f = n's top h bits) all give palindromes below n, and the one
    //       built from f itself is compared directly. 0 is counted separately.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int countBinaryPalindromes(long n) {
        if (n == 0) {
            return 1;
        }

        int bitLength = 64 - Long.numberOfLeadingZeros(n);
        long count = 1; // k = 0

        // every length shorter than n's: free choice of the h - 1 bits below the leading 1
        for (int length = 1; length < bitLength; length++) {
            int halfLength = (length + 1) / 2;
            count += 1L << (halfLength - 1);
        }

        // n's own length: halves strictly below n's top half give palindromes strictly below n
        int halfLength = (bitLength + 1) / 2;
        long topHalf = n >> (bitLength - halfLength);
        count += topHalf - (1L << (halfLength - 1));

        // the palindrome built from n's own top half shares n's prefix -- compare it directly
        if (mirror(topHalf, bitLength) <= n) {
            count++;
        }
        return (int) count;
    }

    /** Builds the bitLength-bit palindrome whose top half is half. */
    private long mirror(long half, int bitLength) {
        long result = half;
        // when the length is odd, the middle bit belongs to the half and is not repeated
        long rest = (bitLength % 2 == 1) ? half >> 1 : half;
        int restLength = bitLength / 2;
        for (int i = 0; i < restLength; i++) {
            // append the bits of rest from lowest to highest = the half, reversed
            result = (result << 1) | (rest & 1L);
            rest >>= 1;
        }
        return result;
    }
}
