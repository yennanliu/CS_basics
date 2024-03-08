package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/reverse-bits/description/

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
