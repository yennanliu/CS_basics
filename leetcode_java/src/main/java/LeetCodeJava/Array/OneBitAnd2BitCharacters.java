package LeetCodeJava.Array;

// https://leetcode.com/problems/1-bit-and-2-bit-characters/

/**
 *  717. 1-bit and 2-bit Characters
 *  Easy
 *
 *  We have two special characters:
 *   - The first character can be represented by one bit 0.
 *   - The second character can be represented by two bits (10 or 11).
 *
 *  Given a binary array bits that ends with 0, return true if the last
 *  character must be a one-bit character.
 *
 *  Example 1:
 *    Input: bits = [1,0,0]
 *    Output: true
 *    Explanation: The only way to decode it is two-bit character and one-bit
 *    character. So the last character is one-bit character.
 *
 *  Example 2:
 *    Input: bits = [1,1,1,0]
 *    Output: false
 *    Explanation: The only way to decode it is two-bit character and two-bit
 *    character. So the last character is not one-bit character.
 *
 *  Constraints:
 *    1 <= bits.length <= 1000
 *    bits[i] is either 0 or 1.
 */
public class OneBitAnd2BitCharacters {

    // V0
    // IDEA: GREEDY SCAN. Decoding is deterministic: a leading 1 always consumes
    //       2 bits, a leading 0 consumes 1. Walk to the end and see where we land.
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean isOneBitCharacter(int[] bits) {
        int pos = 0;
        int n = bits.length;
        while (pos < n - 1) {
            pos += (bits[pos] == 1) ? 2 : 1;
        }
        return pos == n - 1;
    }

    // V1
    // IDEA: count the trailing run of 1s before the last 0; if it is even the
    //       last 0 stands alone as a one-bit character.
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean isOneBitCharacter_1(int[] bits) {
        int ones = 0;
        for (int i = bits.length - 2; i >= 0 && bits[i] == 1; i--) {
            ones++;
        }
        return ones % 2 == 0;
    }
}
