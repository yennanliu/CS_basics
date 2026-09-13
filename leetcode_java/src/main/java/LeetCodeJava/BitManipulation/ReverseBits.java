package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/reverse-bits/description/

/**
 * 190. Reverse Bits
 * Easy
 *
 * Reverse bits of a given 32 bits signed integer.
 *
 * Example 1:
 *
 * Input: n = 43261596
 * Output: 964176192
 * Explanation:
 *
 * Integer
 * Binary
 *
 * 43261596 | 00000010100101000001111010011100
 *
 * 964176192 | 00111001011110000010100101000000
 *
 * Example 2:
 *
 * Input: n = 2147483644
 * Output: 1073741822
 * Explanation:
 *
 * Integer
 * Binary
 *
 * 2147483644 | 01111111111111111111111111111100
 *
 * 1073741822 | 00111111111111111111111111111110
 *
 * Constraints:
 *
 * 0 <= n <= 2^31 - 2
 * n is even.
 *
 * Follow up: If this function is called many times, how would you optimize it?
 *
 */
public class ReverseBits {

    // V0
    // TODO : implement
//    public int reverseBits(int n) {
//        return 0;
//    }

    // V1
    // https://leetcode.com/problems/reverse-bits/solutions/4658159/java-solution/
    // you need treat n as an unsigned value
    public int reverseBits_1(int n) {
        int result=0,i;
        for(i=0;i<32;i++)
        {
            result<<=1;
            result=result|(n&1);
            n>>=1;
        }
        return result;
    }

    // V2
    // https://leetcode.com/problems/reverse-bits/solutions/4167696/beats-100-easy-to-understand/
    // you need treat n as an unsigned value
    public int reverseBits_2(int n) {
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            ans <<= 1;
            ans |= (n & 1);
            n >>= 1;
        }
        return ans;
    }
    
}
