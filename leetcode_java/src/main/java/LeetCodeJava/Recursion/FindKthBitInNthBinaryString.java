package LeetCodeJava.Recursion;

// https://leetcode.com/problems/find-kth-bit-in-nth-binary-string/description/

/**
 * 1545. Find Kth Bit in Nth Binary String
 * Medium
 *
 * Given two positive integers n and k, the binary string S_n is formed as follows:
 *
 * S_1 = "0"
 * S_i = S_(i-1) + "1" + reverse(invert(S_(i-1))) for i > 1
 *
 * Where + denotes the concatenation operation, reverse(x) returns the reversed string x, and invert(x) inverts all the bits in x (0 changes to 1 and 1 changes to 0).
 *
 * For example, the first four strings in the above sequence are:
 *
 * S_1 = "0"
 * S_2 = "011"
 * S_3 = "0111001"
 * S_4 = "011100110110001"
 *
 * Return the kth bit in S_n. It is guaranteed that k is valid for the given n.
 *
 *
 * Example 1:
 *
 * Input: n = 3, k = 1
 * Output: "0"
 * Explanation: S_3 is "0111001".
 * The 1st bit is "0".
 *
 * Example 2:
 *
 * Input: n = 4, k = 11
 * Output: "1"
 * Explanation: S_4 is "0111001101 1 0001".
 * The 11th bit is "1".
 *
 *
 * Constraints:
 *
 * 1 <= n <= 20
 * 1 <= k <= 2^n - 1
 *
 */
public class FindKthBitInNthBinaryString {

    // V0
    // IDEA: FOLD k BACK INTO THE PREVIOUS STRING (S_n IS NEVER BUILT)
    //       |S_n| = 2^n - 1 and its middle position mid = 2^(n-1) holds '1'.
    //         k <  mid : same position in S_(n-1)
    //         k == mid : the injected '1'
    //         k >  mid : the tail is reverse(invert(S_(n-1))), so position k maps to
    //                    position 2^n - k of S_(n-1), with the bit FLIPPED
    //       the flips are carried in one boolean instead of nested inversions.
    /**
     * time = O(N)
     * space = O(1)
     */
    public char findKthBit(int n, int k) {
        boolean flipped = false;
        int level = n;
        int position = k;

        while (level > 1) {
            int mid = 1 << (level - 1);   // 2^(level-1)
            if (position == mid) {
                return flipped ? '0' : '1';
            }
            if (position > mid) {
                // mirror into the first half, and remember the inversion
                position = (1 << level) - position;
                flipped = !flipped;
            }
            level--;
        }
        // S_1 = "0"
        return flipped ? '1' : '0';
    }

    // V1
    // IDEA: RECURSION ON THE SAME THREE CASES
    /**
     * time = O(N)
     * space = O(N)   (recursion stack)
     */
    public char findKthBit_1(int n, int k) {
        if (n == 1) {
            return '0';
        }
        int mid = 1 << (n - 1);
        if (k == mid) {
            return '1';
        }
        if (k < mid) {
            return findKthBit_1(n - 1, k);
        }
        // mirrored position in S_(n-1), bit inverted
        char mirrored = findKthBit_1(n - 1, (1 << n) - k);
        return mirrored == '0' ? '1' : '0';
    }
}
