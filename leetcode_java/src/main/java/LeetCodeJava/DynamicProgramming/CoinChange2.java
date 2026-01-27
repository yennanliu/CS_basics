package LeetCodeJava.DynamicProgramming;

// https://leetcode.ca/all/518.html
// https://leetcode.com/problems/coin-change-2/

import java.util.Arrays;

/**
 * 518. Coin Change II
 * Medium
 * Topics
 * Companies
 * You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
 *
 * Return the number of combinations that make up that amount. If that amount of money cannot be made up by any combination of the coins, return 0.
 *
 * You may assume that you have an infinite number of each kind of coin.
 *
 * The answer is guaranteed to fit into a signed 32-bit integer.
 *
 *
 *
 * Example 1:
 *
 * Input: amount = 5, coins = [1,2,5]
 * Output: 4
 * Explanation: there are four ways to make up the amount:
 * 5=5
 * 5=2+2+1
 * 5=2+1+1+1
 * 5=1+1+1+1+1
 * Example 2:
 *
 * Input: amount = 3, coins = [2]
 * Output: 0
 * Explanation: the amount of 3 cannot be made up just with coins of 2.
 * Example 3:
 *
 * Input: amount = 10, coins = [10]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= coins.length <= 300
 * 1 <= coins[i] <= 5000
 * All the values of coins are unique.
 * 0 <= amount <= 5000
 *
 *
 *
 */
public class CoinChange2 {

    // V0
    // IDEA: BOTTOM UP DP (fixed by gemini)
    public int change(int amount, int[] coins) {

        /** NOTE !!!
         *
         *   dp[i] = total number of `combinations` that make up amount i
         *
         *
         *   compare with LC 322
         */
        // 1. dp[i] = total number of combinations that make up amount i
        int[] dp = new int[amount + 1];

        /** NOTE !!!
         *
         *   Base case: There is exactly 1 way to make 0 amount (empty set)
         *
         *  -> need to set dp[0] = 1, instead of 0
         */
        // 2. Base case: There is exactly 1 way to make 0 amount (empty set)
        dp[0] = 1;

        /** NOTE !!!
         *
         *    1. loop `coin -> amount` can
         *       - AVOID duplicate combinations like [1,2] and [2,1].
         *
         *
         *   Explanation:
         *
         *
         *   Exactly. If you put the **amount loop** on the outside,
         *    -> you aren't just getting duplicates;
         *      you are actually calculating **Permutations**
         *      instead of **Combinations**.
         *
         * In LeetCode 518, the problem asks for the
         * number of ways to make up the amount, where
         * the **order does not matter** (e.g.,  is the same as ).
         *
         * ---
         *
         * ### Why the "Amount Outer" loop causes duplicates
         *
         * When the **Amount** is the outer loop, you
         * are essentially asking: *"To get to sum ,
         * what was the very last coin I added?"*
         *
         * Imagine `coins = {1, 2}` and `amount = 3`:
         *
         * 1. **To make sum 1:** Only one way .
         * 2. **To make sum 2:**
         * * Add a `1` to `dp[1]`:
         * * Add a `2` to `dp[0]`:
         * * *Total ways: 2*
         *
         *
         * 3. **To make sum 3:**
         * * Add a `1` to `dp[2]`: This includes adding `1` to  and adding `1` to . Result: **** and ****.
         * * Add a `2` to `dp[1]`: This includes adding `2` to . Result: ****.
         * * *Total ways: 3*
         *
         *
         *
         * Notice that **** and **** are both counted. This is why your result for amount 3 would be `3`, but the correct answer for combinations is `2`.
         *
         * ---
         *
         *
         *  NOTE !!!!!!!!!!
         *
         *
         * ### The Fix: Coin Outer Loop
         *
         * When the **Coin** is the outer loop, you force an order. You say:
         *
         * *"First, I will find all ways using only 1s. Then,
         * I will find all ways using 1s and 2s."* By the time you start using
         * the `2` coin, you have finished all calculations for the `1` coin.
         * This makes it impossible to ever place a `1` *after* a `2`,
         * effectively forcing all combinations to be in non-decreasing order (like ),
         * which naturally prevents duplicates.
         *
         *
         * 
         * ---
         *
         * ### Summary Comparison
         *
         * | Loop Order | Logical Meaning | Result Type | LeetCode Example |
         * | --- | --- | --- | --- |
         * | **Outer: Coin**<br>
         *
         * <br>Inner: Amount | Each coin is considered one by one. | **Combinations**<br>
         *
         * <br>(Order doesn't matter) | **LC 518** (Coin Change II) |
         * | **Outer: Amount**<br>
         *
         * <br>Inner: Coin | Every coin is tried at every step. | **Permutations**<br>
         *
         * <br>(Order matters) | **LC 377** (Combination Sum IV) |
         *
         * ---
         *
         * ### Corrected Code for LC 518
         *
         * Here is how your logic should look to avoid those duplicates:
         *
         * ```java
         * public int change(int amount, int[] coins) {
         *     int[] dp = new int[amount + 1];
         *     dp[0] = 1; // Base case: 1 way to make 0
         *
         *     // COIN OUTER LOOP = COMBINATIONS
         *     for (int c : coins) {
         *         for (int i = c; i <= amount; i++) {
         *             dp[i] += dp[i - c];
         *         }
         *     }
         *
         *     return dp[amount];
         * }
         *
         * ```
         *
         *
         */
        // 3. OUTER LOOP: Iterate through each coin
        // This ensures we process all uses of one coin before moving to the next,
        // which prevents duplicate combinations like [1,2] and [2,1].
        for (int coin : coins) {
            // 4. INNER LOOP: Update dp table for all amounts reachable by this coin
            for (int i = coin; i <= amount; i++) {
                // Number of ways to make amount 'i' is:
                // (Current ways) + (Ways to make 'i - coin')
                dp[i] += dp[i - coin];
            }
        }

        return dp[amount];
    }

    // V0-0-1
    // IDEA: 1D DP (gemini)
    public int change_0_0_1(int amount, int[] coins) {
        // edge

        int[] dp = new int[amount + 1]; // ???
        // init
        dp[0] = 1;

        /** NOTE !!!
         *
         *   before looping `order` is WRONG
         *   -> we need to loop coin, then amount
         *   e.g.
         *    for(int c: coins){
         *        // ...
         *        for(int i = 1; i < amount + 1; i++){
         *            // ....
         *        }
         *    }
         *
         */
        //        for(int i = 1; i < amount + 1; i++){
        //            for(int c: coins){
        //                // i - c: remaining amount
        //                if(i - c > 0){
        //                    dp[i] += dp[i - c];
        //                }
        //            }
        //        }


        // 3. OUTER LOOP: Iterate through each coin
        // This ensures we process coins one by one to avoid duplicate combinations
        for (int c : coins) {
            // 4. INNER LOOP: Update dp table for all amounts reachable by this coin
            for (int i = c; i <= amount; i++) {
                // New ways to make i = existing ways + ways to make (i - current coin)
                dp[i] += dp[i - c];
            }
        }

        //System.out.println(">>> dp = " + Arrays.toString(dp));

        return dp[amount];
    }

    

    // V0-0-2
    // IDEA: DP (gpt)
    public int change_0_0_2(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;

        for (int coin : coins) {
            for (int a = coin; a <= amount; a++) {
                dp[a] += dp[a - coin];
            }
        }

        return dp[amount];
    }


    // V0-1
    int combinationCnt_0_1 = 0;

    public int change_0_1(int amount, int[] coins) {
        if (coins == null || coins.length == 0)
            return 0;
        if (amount == 0)
            return 1;

        backtrack_0_1(amount, coins, 0, 0);
        return combinationCnt_0_1;
    }

    private void backtrack_0_1(int amount, int[] coins, int startIdx, int currentSum) {
        if (currentSum == amount) {
            combinationCnt_0_1++;
            return;
        }

        if (currentSum > amount)
            return;

        // Single recursive call, loop handles all choices
        for (int i = startIdx; i < coins.length; i++) {
            /**
             *  NOTE !!!!
             *
             *
             *  -> // still i (not i + 1) because coins are infinite
             *
             *  e.g. if we are solving the  `use each coin at most once, not infinite supply` problem
             *      -> then we need ` i + 1` as start_idx in next recursion call
             */
            /**
             *  Question: Why do we not have `duplicate logic` in below code ?
             *
             *
             * ## 🧠 The `Coin Change II` problem **does not require tracking permutations**
             *
             * Your code with:
             *
             * ```java
             * for (int i = startIdx; i < coins.length; i++) {
             *     backtrack(amount, coins, i, currentSum + coins[i]);
             * }
             * ```
             *
             * **does not generate duplicates**, as long as:
             *
             * * You always **start the loop at `startIdx`**
             * * You do **not** allow backtracking to earlier indices
             *
             * ---
             *
             * ### ✅ Why This Works (No Need for Visited Set)
             *
             * This loop structure inherently avoids duplicate combinations:
             *
             * ```java
             * coins = [1, 2, 5]
             * amount = 5
             * ```
             *
             * Valid recursive paths from this will include:
             *
             * * \[1,1,1,1,1]
             * * \[1,1,1,2]
             * * \[1,2,2]
             * * \[5]
             *
             * But **not**:
             *
             * * \[2,1,1,1] — because 2 would appear *after* 1 in the loop, and we never go "backwards"
             *
             * So, each combination is **built in non-decreasing order**, and this:
             *
             * * Prevents permutation duplicates
             * * Does **not** need a `visited` set
             * * Matches exactly the required behavior for **unique combinations**
             *
             * ---
             *
             * ## 🧪 Test This in Practice
             *
             * Try printing combinations to see for yourself:
             *
             * ```java
             * private void backtrack(int amount, int[] coins, int startIdx, int currentSum, List<Integer> path) {
             *     if (currentSum == amount) {
             *         System.out.println(path);
             *         combinationCnt++;
             *         return;
             *     }
             *
             *     if (currentSum > amount) return;
             *
             *     for (int i = startIdx; i < coins.length; i++) {
             *         path.add(coins[i]);
             *         backtrack(amount, coins, i, currentSum + coins[i], path);
             *         path.remove(path.size() - 1);
             *     }
             * }
             * ```
             *
             * ---
             *
             * ### 🔁 What About 0/1 Knapsack (Use Each Coin Once)?
             *
             * If your goal is to solve the **0/1 version** (use each coin **at most once**, not infinite supply), then yes:
             *
             * * You must ensure you don’t reuse the same coin multiple times.
             * * You do this by **advancing `i + 1` in recursion**:
             *
             * ```java
             * backtrack(amount, coins, i + 1, currentSum + coins[i]); // move forward
             * ```
             *
             * * In this case, **duplicate filtering may be needed** if combinations like `[1,2]` and `[2,1]` are treated the same and can both occur.
             *
             * ---
             *
             * ## ✅ Summary
             *
             * | Version                             | Coin Usage         | Requires Deduplication?                          |
             * | ----------------------------------- | ------------------ | ------------------------------------------------ |
             * | Unbounded Knapsack (Coin Change II) | Infinite coins     | ❌ No — loop from `startIdx` handles it           |
             * | 0/1 Knapsack                        | Use each coin once | ✅ Yes — or use `i + 1` recursion to enforce once |
             *
             *
             */
            backtrack_0_1(amount, coins, i, currentSum + coins[i]); // still i (not i + 1) because coins are infinite
        }
    }

    // V0-2
    // IDEA: BACKTRACK (gpt) (TLE)
    public int change_0_2(int amount, int[] coins) {
        return backtrack(coins, 0, amount);
    }

    private int backtrack(int[] coins, int index, int remaining) {
        if (remaining == 0)
            return 1; // valid combination
        if (remaining < 0 || index == coins.length)
            return 0; // invalid path

        // Choose coin at `index` or skip to next coin
        int withCoin = backtrack(coins, index, remaining - coins[index]);
        int withoutCoin = backtrack(coins, index + 1, remaining);

        return withCoin + withoutCoin;
    }

    // V0-3
    // IDEA: Backtracking + Memoization (Top-Down DP)  (gpt)
    public int change_0_3(int amount, int[] coins) {
        Integer[][] memo = new Integer[coins.length][amount + 1];
        return dfs(coins, 0, amount, memo);
    }

    private int dfs(int[] coins, int i, int rem, Integer[][] memo) {
        if (rem == 0)
            return 1;
        if (rem < 0 || i == coins.length)
            return 0;
        if (memo[i][rem] != null)
            return memo[i][rem];

        int use = dfs(coins, i, rem - coins[i], memo); // take coin[i]
        int skip = dfs(coins, i + 1, rem, memo); // skip coin[i]

        memo[i][rem] = use + skip;
        return memo[i][rem];
    }

    // V0-4
    // IDEA: BACKTRACK (gpt) (TLE)
    int combinationCnt = 0;

    public int change_0_4(int amount, int[] coins) {
        if (coins == null || coins.length == 0)
            return 0;
        if (amount == 0)
            return 1;

        backtrack(amount, coins, 0, 0);
        return combinationCnt;
    }

    private void backtrack(int amount, int[] coins, int idx, int currentSum) {
        if (currentSum == amount) {
            combinationCnt++;
            return;
        }

        if (currentSum > amount || idx >= coins.length) {
            return;
        }

        // Choose current coin (can reuse it)
        backtrack(amount, coins, idx, currentSum + coins[idx]);

        // Skip to next coin
        backtrack(amount, coins, idx + 1, currentSum);
    }

    // V1-1
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Recursion
    public int change_1_1(int amount, int[] coins) {
        Arrays.sort(coins);

        return dfs(coins, 0, amount);
    }

    private int dfs(int[] coins, int i, int a) {
        if (a == 0) {
            return 1;
        }
        if (i >= coins.length) {
            return 0;
        }

        int res = 0;
        if (a >= coins[i]) {
            res = dfs(coins, i + 1, a);
            res += dfs(coins, i, a - coins[i]);
        }
        return res;
    }

    // V1-2
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Dynamic Programming (Top-Down)
    public int change_1_2(int amount, int[] coins) {
        Arrays.sort(coins);
        int[][] memo = new int[coins.length + 1][amount + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        return dfs(0, amount, coins, memo);
    }

    private int dfs(int i, int a, int[] coins, int[][] memo) {
        if (a == 0) return 1;
        if (i >= coins.length) return 0;
        if (memo[i][a] != -1) return memo[i][a];

        int res = 0;
        if (a >= coins[i]) {
            res = dfs(i + 1, a, coins, memo);
            res += dfs(i, a - coins[i], coins, memo);
        }
        memo[i][a] = res;
        return res;
    }


    // V1-3
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Dynamic Programming (Bottom-Up)
    public int change_1_3(int amount, int[] coins) {
        int n = coins.length;
        Arrays.sort(coins);
        int[][] dp = new int[n + 1][amount + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int a = 0; a <= amount; a++) {
                if (a >= coins[i]) {
                    dp[i][a] = dp[i + 1][a];
                    dp[i][a] += dp[i][a - coins[i]];
                }
            }
        }

        return dp[0][amount];
    }


    // V1-4
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Dynamic Programming (Space Optimized)
    public int change_1_4(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int i = coins.length - 1; i >= 0; i--) {
            int[] nextDP = new int[amount + 1];
            nextDP[0] = 1;

            for (int a = 1; a <= amount; a++) {
                nextDP[a] = dp[a];
                if (a - coins[i] >= 0) {
                    nextDP[a] += nextDP[a - coins[i]];
                }
            }
            dp = nextDP;
        }
        return dp[amount];
    }


    // V1-5
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Dynamic Programming (Optimal)
    public int change_1_5(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int i = coins.length - 1; i >= 0; i--)
            for (int a = 1; a <= amount; a++)
                dp[a] = dp[a] + (coins[i] <= a ? dp[a - coins[i]] : 0);
        return dp[amount];
    }

    // V2




}
