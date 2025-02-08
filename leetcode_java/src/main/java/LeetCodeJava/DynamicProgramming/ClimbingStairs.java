package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/climbing-stairs/
/**
 * 70. Climbing Stairs
 * Solved
 * Easy
 * Topics
 * Companies
 * Hint
 * You are climbing a staircase. It takes n steps to reach the top.
 *
 * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
 *
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 2
 * Explanation: There are two ways to climb to the top.
 * 1. 1 step + 1 step
 * 2. 2 steps
 * Example 2:
 *
 * Input: n = 3
 * Output: 3
 * Explanation: There are three ways to climb to the top.
 * 1. 1 step + 1 step + 1 step
 * 2. 1 step + 2 steps
 * 3. 2 steps + 1 step
 *
 *
 * Constraints:
 *
 * 1 <= n <= 45
 */
import java.util.*;

public class ClimbingStairs {

    // V0
    // IDEA : DP
    public int climbStairs(int n) {

        if (n <= 2){
            if (n == 1){
                return 1;
            }
            return 2;
        }

        /**
         *
         *  0, 1, 2, 3..... k-2, k-1, k
         *  dp[k] = dp[k-2] + dp[k-1]
         *
         */

        // init dp
        int[] dp = new int[n+1];
        dp[0] = 1;
        dp[1] = 2;

        for (int i = 2; i < n; i++){
            dp[i] = dp[i-2] + dp[i-1];
            //System.out.println("dp[i] = " + dp[i]);
        }

        return dp[n-1];
    }

    // V0
    // IDEA : DP
    public int climbStairs_0(int n) {

        // null check
        if (n <= 0){
            return 0;
        }

        if (n == 1 || n == 2){
            return n;
        }

        // NOTE !!! init dp as ArrayList
        List<Integer> dp = new ArrayList<>();
        dp.add(1); // i = 0
        dp.add(1); // i = 1
        //System.out.println("dp = " + dp);

        // NOTE !!! start from i = 2
        for (int i = 2; i <= n; i++){
            int tmp =  dp.get(i-1) + dp.get(i-2);
            dp.add(tmp);
        }

        return dp.get(n);
    }

    // V1
    // IDEA : Memoization
    // https://leetcode.com/problems/climbing-stairs/solutions/3708750/4-method-s-beat-s-100-c-java-python-beginner-friendly/
    public int climbStairs_2(int n) {
        Map<Integer, Integer> memo = new HashMap<>();
        return climbStairs(n, memo);
    }

    private int climbStairs(int n, Map<Integer, Integer> memo) {
        if (n == 0 || n == 1) {
            return 1;
        }
        if (!memo.containsKey(n)) {
            memo.put(n, climbStairs(n-1, memo) + climbStairs(n-2, memo));
        }
        return memo.get(n);
    }

    // V2
    // IDEA : Tabulation
    // https://leetcode.com/problems/climbing-stairs/solutions/3708750/4-method-s-beat-s-100-c-java-python-beginner-friendly/
    public int climbStairs_4(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }

        int[] dp = new int[n+1];
        dp[0] = dp[1] = 1;

        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }

    // V3
    // IDEA : Space Optimization
    // https://leetcode.com/problems/climbing-stairs/solutions/3708750/4-method-s-beat-s-100-c-java-python-beginner-friendly/
    public int climbStairs_5(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        int prev = 1, curr = 1;
        for (int i = 2; i <= n; i++) {
            int temp = curr;
            curr = prev + curr;
            prev = temp;
        }
        return curr;
    }

}
