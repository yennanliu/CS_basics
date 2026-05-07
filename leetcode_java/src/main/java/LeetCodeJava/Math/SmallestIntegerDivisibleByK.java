package LeetCodeJava.Math;

// https://leetcode.com/problems/smallest-integer-divisible-by-k/description/
/**
 *  1015. Smallest Integer Divisible by K
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a positive integer k, you need to find the length of the smallest positive integer n such that n is divisible by k, and n only contains the digit 1.
 *
 * Return the length of n. If there is no such n, return -1.
 *
 * Note: n may not fit in a 64-bit signed integer.
 *
 *
 *
 * Example 1:
 *
 * Input: k = 1
 * Output: 1
 * Explanation: The smallest answer is n = 1, which has length 1.
 * Example 2:
 *
 * Input: k = 2
 * Output: -1
 * Explanation: There is no such positive integer n divisible by 2.
 * Example 3:
 *
 * Input: k = 3
 * Output: 3
 * Explanation: The smallest answer is n = 111, which has length 3.
 *
 *
 * Constraints:
 *
 * 1 <= k <= 105
 *
 *
 */
public class SmallestIntegerDivisibleByK {

    // V0
//    public int smallestRepunitDivByK(int k) {
//
//    }

    // TODO: fix below
//    public int smallestRepunitDivByK(int k) {
//        int val = 0;
//
//        // ???
//        for(int i = 0; i < k; i++){
//            val = val * 10 + 1;
//
//            System.out.println(">>> val = " + val
//                    + ", k = " + k);
//            if(val % k == 0){
//                return i+1; // ???
//            }
//        }
//
//
//        return val % k == 0 ? k : -1;
//    }



    // V0-1
    // IDEA: MATH (gemini)
    public int smallestRepunitDivByK_0_1(int k) {
        // 1. Quick check: No repunit (1, 11, 111...) ends in 0, 2, 4, 5, 6, 8.
        // Thus, it cannot be divisible by 2 or 5.
        if (k % 2 == 0 || k % 5 == 0) {
            return -1;
        }

        int remainder = 0;

        // 2. We only need to check up to k lengths (Pigeonhole Principle)
        for (int length = 1; length <= k; length++) {
            /**  NOTE !!!
             *
             *  to avoid `overflow`, we need to
             *  keep the `REMAINDER`, instead of the original number
             *
             *  ->
             *
             *  cannot fit inside int (or even long) for large k.
             *
             * You must keep only the remainder instead of the full number.
             *
             *
             * ->
             *
             *  e.g.
             *
             *  remainder = (remainder * 10 + 1) % k;
             *
             *
             */
            // Update remainder instead of storing the massive number
            // (remainder * 10 + 1) % k
            remainder = (remainder * 10 + 1) % k;

            // 3. If remainder is 0, we found the length
            if (remainder == 0) {
                return length;
            }
        }

        return -1;
    }


    // V0-2
    // IDEA: MATH (GPT)
    public int smallestRepunitDivByK_0_2(int k) {

        // Numbers made only of 1's
        // can never be divisible by 2 or 5
        if (k % 2 == 0 || k % 5 == 0) {
            return -1;
        }

        int val = 0;

        for (int i = 0; i < k; i++) {

            // Keep remainder only
            /**  NOTE !!!
             *
             *  to avoid `overflow`, we need to
             *  keep the `REMAINDER`, instead of the original number
             *
             *  ->
             *
             *  cannot fit inside int (or even long) for large k.
             *
             * You must keep only the remainder instead of the full number.
             *
             *
             * ->
             *
             *  e.g.
             *
             *  remainder = (remainder * 10 + 1) % k;
             *
             *
             */
            val = (val * 10 + 1) % k;

            //System.out.println(">>> remainder = " + val);

            if (val == 0) {
                return i + 1;
            }
        }

        return -1;
    }



    // V1
    // IDEA: Checking Loop
    // https://leetcode.com/problems/smallest-integer-divisible-by-k/editorial/
    public int smallestRepunitDivByK_1(int K) {
        int remainder = 0;
        for (int length_N = 1; length_N <= K; length_N++) {
            remainder = (remainder * 10 + 1) % K;
            if (remainder == 0) {
                return length_N;
            }
        }
        return -1;
    }



    // V2
    // https://leetcode.com/problems/smallest-integer-divisible-by-k/solutions/7372933/super-duper-easy-solution-just-remainder-e6aq/
    public int smallestRepunitDivByK_2(int k) {
        if (k == 1)
            return 1;

        // Numbers made of only 1s can NEVER be divisible by 2 or 5
        if (k % 2 == 0 || k % 5 == 0)
            return -1;

        int rem = 0;
        for (int i = 1; i <= k; i++) {
            rem = (rem * 10 + 1) % k;
            if (rem == 0)
                return i;
        }
        return -1;
    }


    // V3
    // https://leetcode.com/problems/smallest-integer-divisible-by-k/solutions/7372514/r10r1k-for-k-coprime-to-2-5beats-100-by-bdrto/
    public int smallestRepunitDivByK_3(int k) {
        if ((k & 1) == 0 || k % 5 == 0)
            return -1;
        if (k == 1)
            return 1;
        int r = 1;
        for (int len0 = 2;; len0++) {
            r = (10 * r + 1) % k;
            if (r == 0)
                return len0;
        }
    }






}
