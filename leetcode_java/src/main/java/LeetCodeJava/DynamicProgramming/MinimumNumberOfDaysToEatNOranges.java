package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-number-of-days-to-eat-n-oranges/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 1553. Minimum Number of Days to Eat N Oranges
 * Hard
 *
 * There are n oranges in the kitchen and you decided to eat some of these oranges every day as follows:
 *
 * Eat one orange.
 * If the number of remaining oranges n is divisible by 2 then you can eat n / 2 oranges.
 * If the number of remaining oranges n is divisible by 3 then you can eat 2 * (n / 3) oranges.
 *
 * You can only choose one of the actions per day.
 *
 * Given the integer n, return the minimum number of days to eat n oranges.
 *
 * Example 1:
 *
 * Input: n = 10
 * Output: 4
 * Explanation: You have 10 oranges.
 * Day 1: Eat 1 orange,  10 - 1 = 9.
 * Day 2: Eat 6 oranges, 9 - 2*(9/3) = 9 - 6 = 3. (Since 9 is divisible by 3)
 * Day 3: Eat 2 oranges, 3 - 2*(3/3) = 3 - 2 = 1.
 * Day 4: Eat the last orange  1 - 1  = 0.
 * You need at least 4 days to eat the 10 oranges.
 *
 * Example 2:
 *
 * Input: n = 6
 * Output: 3
 * Explanation: You have 6 oranges.
 * Day 1: Eat 3 oranges, 6 - 6/2 = 6 - 3 = 3. (Since 6 is divisible by 2).
 * Day 2: Eat 2 oranges, 3 - 2*(3/3) = 3 - 2 = 1. (Since 3 is divisible by 3)
 * Day 3: Eat the last orange  1 - 1  = 0.
 * You need at least 3 days to eat the 6 oranges.
 *
 * Constraints:
 *
 * 1 <= n <= 2 * 10^9
 *
 */
public class MinimumNumberOfDaysToEatNOranges {

    // V0
    // IDEA: MEMOIZED DFS — ONLY EVER DIVIDE
    //       eating one orange a day is only worth it to reach a multiple of 2 or 3, so from n:
    //         go to n / 2 : (n % 2) single days + 1 halving day
    //         go to n / 3 : (n % 3) single days + 1 third day
    //       days(n) = 1 + min(n % 2 + days(n / 2), n % 3 + days(n / 3))
    //       only O(log^2 n) distinct values of n are ever reached, so a HashMap memo is enough.
    //       (a plain dp array of size n times out / runs out of memory for n = 2 * 10^9)
    /**
     * time = O(log^2 N)
     * space = O(log^2 N)
     */
    public int minDays(int n) {
        Map<Integer, Integer> memo = new HashMap<>();
        // base cases: 0 oranges takes 0 days, 1 orange takes 1 day
        memo.put(0, 0);
        memo.put(1, 1);
        return days(n, memo);
    }

    /** Minimum days to eat `n` oranges. */
    private int days(int n, Map<Integer, Integer> memo) {
        Integer cached = memo.get(n);
        if (cached != null) {
            return cached;
        }
        int viaHalf = n % 2 + days(n / 2, memo);
        int viaThird = n % 3 + days(n / 3, memo);
        int result = 1 + Math.min(viaHalf, viaThird);
        memo.put(n, result);
        return result;
    }
}
