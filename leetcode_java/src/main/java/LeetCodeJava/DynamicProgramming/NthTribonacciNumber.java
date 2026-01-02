package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/n-th-tribonacci-number/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 1137. N-th Tribonacci Number
 * Easy
 * Topics
 * Companies
 * Hint
 * The Tribonacci sequence Tn is defined as follows:
 *
 * T0 = 0, T1 = 1, T2 = 1, and Tn+3 = Tn + Tn+1 + Tn+2 for n >= 0.
 *
 * Given n, return the value of Tn.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 4
 * Output: 4
 * Explanation:
 * T_3 = 0 + 1 + 1 = 2
 * T_4 = 1 + 1 + 2 = 4
 * Example 2:
 *
 * Input: n = 25
 * Output: 1389537
 *
 *
 * Constraints:
 *
 * 0 <= n <= 37
 * The answer is guaranteed to fit within a 32-bit integer, ie. answer <= 2^31 - 1.
 *
 *
 */
public class NthTribonacciNumber {

    // V0
    // IDEA: DP
    public int tribonacci(int n) {
        // edge
        if (n == 0) {
            return 0;
        }
        if (n <= 2) {
            return 1;
        }

        /**  NOTE !!! should be
         *
         *  1. int[n+1]
         *  2. for (int i = 3; i < n + 1; i++)
         *
         * ---
         *
         *  The Problems
         *
         *  1. Array Size: If you want to find the $n$-th number, your array needs to be size n + 1. If $n=3$ and you initialize new int[3], the indices are $0, 1, 2$. Accessing dp[3] will cause an ArrayIndexOutOfBoundsException.
         *
         *  2.  Loop Boundary: The loop should run up to and including $n$ (i <= n).
         *
         *  3. Return Value: You should return dp[n], not dp[n-1].
         *
         */
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 1;

        for (int i = 3; i < n + 1; i++) {
            /**
             * Tn+3 = Tn + Tn+1 + Tn+2
             *
             * -> Tn = Tn-3 + Tn-2 + Tn-1
             *
             */
            dp[i] = (dp[i - 3] + dp[i - 1] + dp[i - 2]);
        }

        return dp[n]; // ??
    }



    // V0-1
    // IDEA: DP (fixed by gpt)
    public int tribonacci_0_1(int n) {
        if (n == 0)
            return 0;
        if (n == 1 || n == 2)
            return 1;

        // NOTE !!! below, array size is `n + 1`
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 1;

        // NOTE !!! below, we loop from i = 3 to `i <= n`
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2] + dp[i - 3];
        }

        return dp[n];
    }

    // V0-2
    // IDEA: RECURSION (TLE)
    public int tribonacci_0_2(int n) {
        // edge
        if(n == 0){
            return 0;
        }
        if(n == 1){
            return 1;
        }
        if(n == 2){
            return 1;
        }
        if(n == 3){
            return 2;
        }

        // Tn+3 = Tn + Tn+1 + Tn+2 for n >= 0.
        return tribonacci_0_1(n-3) + tribonacci_0_1(n-2) + tribonacci_0_1(n-1);
    }

    // V0-3
    // IDEA: DP (via hashmap)
    public int tribonacci_0_3(int n) {
        if (n == 0)
            return 0;
        if (n == 1 || n == 2)
            return 1;

        Map<Integer, Integer> cache = new HashMap<>();
        cache.put(0, 0);
        cache.put(1, 1);
        cache.put(2, 1);

        // NOTE !!! below, we loop from i = 3 to `i <= n`
        for (int i = 3; i <= n; i++) {
            int val = cache.get(i - 1) + cache.get(i - 2) + cache.get(i - 3);
            cache.put(i, val);
        }

        return cache.get(n);
    }

    // V1
    // https://www.youtube.com/watch?v=3lpNp5Ojvrw
    // python
    // https://github.com/neetcode-gh/leetcode/blob/main/python%2F1137-n-th-tribonacci-number.py


    // V2
    // https://leetcode.com/problems/n-th-tribonacci-number/solutions/6707211/beginner-friendly-step-by-step-code-walk-ftvo/
    public int tribonacci_2(int n) {
        int[] dp = new int[n+1];
        Arrays.fill(dp,-1);
        return findTribNum(n,dp);
    }
    public int findTribNum(int n,int[] dp)
    {
        if(n == 0){
            return 0;
        }
        if(n==1 || n==2){
            return 1;
        }

        if(dp[n] != -1){
            return dp[n];
        }

        return dp[n] = findTribNum(n-3,dp)+findTribNum(n-2,dp)+findTribNum(n-1,dp);
    }

    // V3


}
