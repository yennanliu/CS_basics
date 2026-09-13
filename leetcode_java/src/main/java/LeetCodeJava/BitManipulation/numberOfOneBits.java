package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-1-bits/submissions/1264092870/

/**
 * 191. Number of 1 Bits
 * Easy
 *
 * Given a positive integer n, write a function that returns the number of set bits in its binary representation (also known as the Hamming weight).
 *
 * Example 1:
 *
 * Input: n = 11
 * Output: 3
 * Explanation:
 *
 * The input binary string 1011 has a total of three set bits.
 *
 * Example 2:
 *
 * Input: n = 128
 * Output: 1
 * Explanation:
 *
 * The input binary string 10000000 has a total of one set bit.
 *
 * Example 3:
 *
 * Input: n = 2147483645
 * Output: 30
 * Explanation:
 *
 * The input binary string 1111111111111111111111111111101 has a total of thirty set bits.
 *
 * Constraints:
 *
 * 1 <= n <= 2^31 - 1
 *
 * Follow up: If this function is called many times, how would you optimize it?
 *
 */
public class numberOfOneBits {

    // V0
    // IDEA : bit op
    public int hammingWeight(int n) {
        String bin = Integer.toBinaryString(n);
        int res = count_one(bin);
        return res;
    }

    public int count_one(String input){
        int res = 0;
        for (String x : input.split("")){
            //System.out.println("x = " + x);
            if (x.equals("1")) {
                res += 1;
            }
        }
        return res;
    }

}
