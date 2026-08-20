package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/reverse-bits/description/

public class ReverseBits {

    // V0
    // IDEA: BIT OP (shift out the LSB of n, shift it into the LSB of res, 32 times)
    // LC 190
    /**
     * time = O(1)  (fixed 32 iterations)
     * space = O(1)
     */
    public int reverseBits(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            // make room for the next bit
            res <<= 1;
            // copy `n` current lowest bit into res
            res |= (n & 1);
            /** NOTE !!!
             *
             *  we MUST use `unsigned right shift` (>>>),
             *  since n is treated as an UNSIGNED value,
             *  and `>>` would keep re-inserting the sign bit
             *  and loop forever on negative input
             */
            n >>>= 1;
        }
        return res;
    }

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
