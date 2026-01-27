package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/coin-change/description/
/**
 * 322. Coin Change
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
 *
 * Return the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return -1.
 *
 * You may assume that you have an infinite number of each kind of coin.
 *
 *
 *
 * Example 1:
 *
 * Input: coins = [1,2,5], amount = 11
 * Output: 3
 * Explanation: 11 = 5 + 5 + 1
 * Example 2:
 *
 * Input: coins = [2], amount = 3
 * Output: -1
 * Example 3:
 *
 * Input: coins = [1], amount = 0
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= coins.length <= 12
 * 1 <= coins[i] <= 231 - 1
 * 0 <= amount <= 104
 *
 */
import java.util.*;

public class CoinChange {

    /**  NOTE !!!
     *
     *  1) in Coin change (LC 322),
     *  we do below looping:
     *
     *    ```
     *     for (int i = 0; i < size; i++) {
     *             // ...
     *             for (int coin : coins) {
     *                 // ...
     *     }
     *   ```
     *
     *
     *  2) while in this LC (Coin change 2 (LC 518),
     *     we do below:
     *
     *    ````
     *    for(int c: coins){
     *        // ...
     *          for(int i = 1; i < amount + 1; i++){
     *           // ....
     *           }
     *        }
     *   ```
     *
     */
    
    // V0
    // IDEA : BFS (fixed by gemini)
    public int coinChange(int[] coins, int amount) {
        if (amount == 0)
            return 0;

        // Using a boolean array for 'visited' is much faster than a HashSet for fixed ranges
        //boolean[] visited = new boolean[amount + 1];
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> q = new LinkedList<>();

        q.add(0);
        //visited[0] = true;
        visited.add(0);
        int steps = 0; // This tracks the number of coins used

        while (!q.isEmpty()) {
            int size = q.size();
            steps++; // Moving to the next level of the tree (adding 1 more coin)

            for (int i = 0; i < size; i++) {
                int curSum = q.poll();

                for (int coin : coins) {
                    // Use long to prevent overflow if amount is near Integer.MAX_VALUE
                    long nextSum = (long) curSum + coin;

                    if (nextSum == amount) {
                        return steps;
                    }

                    if (nextSum < amount && !visited.contains((int) nextSum)) {
                        //visited[(int) nextSum] = true;
                        visited.add((int) nextSum);
                        q.add((int) nextSum);
                    }
                }
            }
        }
        return -1;
    }


    // V0-1
    // IDEA : BFS (modified by GPT)
    public int coinChange_0_1(int[] coins, int amount) {
        /**
         * NOTE !!!
         *
         *   1) we use `steps` (hash map) to avoid duplicated computation
         *   2) we use `step` to record `count of steps` at the moment
         *        -> so once cur_sum == amount, we can return above as result directly
         */
        Map<Integer, Integer> steps = new HashMap<>();
        steps.put(0, 0);
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);

        while (!queue.isEmpty()) {
            int tmp = queue.poll();
            int level = steps.get(tmp);
            /**
             *  NOTE !!!
             *
             *   since we want to fine the `minimum` ,
             *   so via BFS, when the first solution is found,
             *   we just return it, as it's the `minimum` solution
             *
             *
             *   Return the fewest number of coins that you need to make up that amount.
             *
             */
            if (tmp == amount) {
                return level;
            }
            for (int c : coins) {
                if (tmp + c > amount) {
                    continue;
                }
                /**
                 *  NOTE !!!
                 *
                 *   below, ONLY when current `tmp` (cur_sum) is NOT visited yet,
                 *   we go further on this option
                 *
                 */
                if (!steps.containsKey(tmp + c)) {
                    queue.offer(tmp + c);
                    steps.put(tmp + c, level + 1);
                }
            }
        }

        return -1;
    }

    // V0-2
    // IDEA: bottom up DP ("backward-looking" approach) (fixed by gemini)
    /** NOTE !!! CORE IDEA
     *
     *  backward-looking
     *
     *  -> It's more standard and efficient to use a "backward-looking"
     *     approach: "To reach sum i, which coin did I just use?"
     *
     *     ```
     *            for (int i = 1; i <= amount; i++) {
     *             // 5. For each amount, try every coin
     *             for (int coin : coins) {
     *                 // If the coin is smaller than or equal to the current amount i
     *                 if (i >= coin) {
     *
     *                     // NOTE !!!!! CORE BELOW:
     *
     *
     *                     // Transition: Min of (current value) OR (1 coin + coins needed for remainder)
     *                     dp[i] = Math.min(dp[i], dp[i - coin] + 1);
     *                 }
     *             }
     *         }
     *   ```
     *
     *
     *
     *  ---------
     *
     *
     *   Nested Loop Logic:
     *   Your current nested loop (j = i + 1) is essentially checking
     *   "from every sum $i$, what other sum j can I reach?"
     *   This is a "forward-looking" approach.
     *
     *    NOTE !!! below code is `forward-looking`, which is NOT efficient for this LC problem
     *
     *    ```
     *        for(int i = 1; i < dp.length; i++){
     *            // ???
     *            for(int j = i+1; j < dp.length; j++){
     *                int diff = j - i;
     *                if(set.contains(diff)){
     *                    dp[j] = Math.min(dp[j], dp[j - diff] + 1);
     *                }
     *            }
     *         }
     *    ```
     *
     */
    /**  NOTE !!!
     *
     * Question:   why `if (i - c >= 0) ?`
     *                 but NOT `if (i - c == 0)` ?
     *
     *             -> if `i-c > 0`, the coin sum is NOT equals to amount
     *                can that still count as the valid coin amount?
     *
     *
     * Answer (gemini):
     *
     * This is a great point of confusion,
     * and it’s the key to understanding how **Dynamic Programming**
     * builds on top of previously solved "sub-problems."
     *
     *
     * -> (NOTE !!!) The short answer:
     *     `i - c == 0` only checks if a **single coin** matches the amount.
     *     `i - c > 0` checks if a coin can be **combined** with a previous sum
     *     to reach the amount.
     *
     * ---
     *
     * ### 1. The Logic of `i - c > 0`
     *
     * When we calculate `dp[i]`, we aren't just looking for one coin that equals `i`. We are looking for a coin `c` that, when subtracted from `i`, leaves a remainder that we **already know how to solve.**
     *
     * * **`i`**: The total amount we are trying to reach right now.
     * * **`c`**: The value of the coin we just picked up.
     * * **`i - c`**: The "remainder" or the amount left over.
     *
     * If `i - c > 0`, it means we still need more coins to reach `i`. But since we are filling the table from `0` to `amount`, we have **already calculated** the best way to make the remainder `i - c`.
     *
     * looks back at dp[i-c]]
     *
     * ---
     *
     * ### 2. A Concrete Example
     *
     * Imagine `coins = [2]` and we want to find `dp[4]` (how to make 4 cents).
     *
     * 1. We try the coin `c = 2`.
     * 2. `i - c` is `4 - 2 = 2`.
     * 3. Since `2 > 0`, we don't stop. We look at `dp[2]`.
     * 4. We already calculated `dp[2] = 1` (it took one 2-cent coin to make 2 cents).
     * 5. So, `dp[4] = dp[2] + 1 = 2`.
     *
     * If we only used `if (i - c == 0)`, we would only ever find that `dp[2] = 1`. When we got to `dp[4]`, the condition `4 - 2 == 0` would be **false**, and we would incorrectly conclude that we can't make 4 cents!
     *
     * ---
     *
     * ### 3. The Three Scenarios
     *
     * When checking `i - c`:
     *
     * | Result of `i - c` | Meaning | Action |
     * | --- | --- | --- |
     * | **Negative** (`< 0`) | The coin is too big for this amount. | Skip this coin. |
     * | **Zero** (`== 0`) | This single coin matches the amount perfectly. | `dp[i] = 1` |
     * | **Positive** (`> 0`) | This coin fits, and we need to check the "remainder." | `dp[i] = dp[remainder] + 1` |
     *
     * ### 💡 Summary
     *
     * The condition `if (i - c >= 0)` covers both the case where a coin matches exactly **and** the case where a coin is just one piece of a larger puzzle.
     *
     *
     *---------------
     *
     *  # More examples:
     *
     *    ```
     *    Input: coins = [1,2,5], amount = 11Output: 3
     *    Explanation: 11 = 5 + 5 + 1
     *    ```
     *
     * ### 1. The Setup
     *
     * * **DP Array:** `int[12]` (Indices 0 to 11).
     * * **Initialization:** `dp[0] = 0`, all others = `12` (our "Infinity").
     *
     * ---
     *
     * ### 2. The Step-by-Step Trace
     *
     * #### **Amounts 1 through 4**
     *
     * * **At **: Only coin `1` fits (). .
     * * **At **:
     * * Coin `1`: .
     * * Coin `2`:  (Winner: Min is 1).
     *
     *
     * * **At **:
     * * Coin `1`: .
     * * Coin `2`: .
     * *  (e.g., ).
     *
     *
     * * **At **:
     * * Coin `1`: .
     * * Coin `2`: .
     * *  (e.g., ).
     *
     *
     *
     * #### **Amount 5 (The first big jump)**
     *
     * * Coin `1`: .
     * * Coin `2`: .
     * * **Coin `5**`: .
     * * **Result:** . (Matches perfectly).
     *
     * #### **Amounts 6 through 10**
     *
     * The table continues to fill. Let's skip to **Amount 10**:
     *
     * * Coin `1`: .
     * * Coin `2`: .
     * * **Coin `5**`: .
     * * **Result:**  (this represents ).
     *
     * ---
     *
     * ### 3. The Final Goal: Amount 11
     *
     * Now the computer calculates the final answer. It tries every coin in the `coins` array:
     *
     * 1. **Try Coin `1**`:
     * * Remainder: .
     * * Look up `dp[10]`: It is `2`.
     * * Calculation: .
     *
     *
     * 2. **Try Coin `2**`:
     * * Remainder: .
     * * Look up `dp[9]`: It is `3` (e.g., ).
     * * Calculation: .
     *
     *
     * 3. **Try Coin `5**`:
     * * Remainder: .
     * * Look up `dp[6]`: It is `2` (e.g., ).
     * * Calculation: .
     *
     *
     *
     * **Final Comparison:** .
     *
     * ---
     *
     * ### 📊 Why the remainder `i - c > 0` worked:
     *
     * When calculating for **11**, the computer didn't have to "re-solve" how to make 10 or 6. It just looked at the table:
     *
     * * "Oh, I know the best way to make **10** is **2** coins ()."
     * * "If I add my **1** coin to that, I get **11** using **3** coins ()."
     *
     * ### Summary Table (Simplified)
     *
     * | i | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | **11** |
     * | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
     * | **dp[i]** | 0 | 1 | 1 | 2 | 2 | 1 | 2 | 2 | 3 | 3 | 2 | **3** |
     *
     *
     */
    public int coinChange_0_2(int[] coins, int amount) {
        if (amount == 0) return 0;

        /** NOTE !!!
         *
         * dp[i] = min `coins` to make amount i.
         *
         *
         * compare with LC 518
         *
         */
        // 1. Create DP array. dp[i] = min `coins` to make amount i.
        int[] dp = new int[amount + 1];

        // 2. Initialize with "Infinity".
        // amount + 1 is safe because even with all 1-cent coins,
        // you can't use more than 'amount' coins.
        /** NOTE !!!
         *
         * init with `amount + 1`, a safer to avoid stackoverflow
         */
        Arrays.fill(dp, amount + 1);

        // 3. Base case: 0 coins needed for 0 amount
        dp[0] = 0;


        /** NOTE !!! core of DP
         *
         * "backward-looking" approach:
         *
         *  "To reach sum i, which coin did I just use?"
         */
        // 4. Iterate through every amount from 1 to 'amount'
        for (int i = 1; i <= amount; i++) {
            // 5. For each amount, try every coin
            for (int coin : coins) {
                /** NOTE !!! CRITICAL !!!
                 *
                 *  the condition should be `i - coin >= 0` (e.g. i >= coin)
                 *  -> so once we know the `remainder > = 0` (i - coin),
                 *     we can let `prev dp and cur dp` decide the min coin cnt
                 *     - prev dp:
                 *          - dp[i - coin]
                 *     - cur dp:
                 *          - dp[i]
                 *
                 *
                 * --------------------
                 *
                 *  NOTE !!! below logic in WRONG !!!
                 *
                 *   we should NOT only rely on `set.contains(i - c)`
                 *   to for the sum up check
                 *
                 *  ```
                 *  // ....
                 *                 if( set.contains(i - c) ){
                 *                     // dp[i] = min( dp[i],  dp[num - i] + 1 )
                 *                     dp[i] = Math.min( dp[i],  dp[i - c] + 1 );
                 *                 }
                 *  // ...
                 * ```
                 *
                 */
                // If the coin is smaller than or equal to the current amount i
                if (i >= coin) {
                    /** NOTE !!!
                     *
                     * DP equation:
                     *    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                     */
                    // Transition: Min of (current value) OR (1 coin + coins needed for remainder)
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }

        // 6. If the value is still the "Infinity" marker, we couldn't reach it.
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // V0-2-1
    // IDEA: bottom up DP ("backward-looking" approach) (fixed by gemini)
    public int coinChange_0_2_1(int[] coins, int amount) {
        // 1. Base case
        if (amount == 0)
            return 0;

        // 2. dp[i] = min coins needed to make amount i
        int[] dp = new int[amount + 1];

        // 3. Initialize with a value larger than any possible answer (amount + 1)
        // We avoid Integer.MAX_VALUE to prevent overflow when adding 1
        int max = amount + 1;
        Arrays.fill(dp, max);

        // It takes 0 coins to make 0 amount
        dp[0] = 0;

        // 4. Iterate through each coin
        for (int coin : coins) {
            // For each coin, update all reachable amounts from 'coin' up to 'amount'
            for (int i = coin; i < amount + 1; i++) {
                /**  NOTE !!!  below logic seems optional (compare V0-2 and V0-2-1)
                 *
                 *  ``
                 *  if (i >= coin) {
                 *      // ...
                 *  }
                 *
                 *
                 */
                // Update dp[i] if using this coin gives us a smaller count
                // dp[i - coin] is the min coins for the remainder
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }

        // 5. If dp[amount] is still 'max', it means the amount is unreachable
        return dp[amount] > amount ? -1 : dp[amount];
    }




    // V0-3
    // IDEA: "forward-looking" approach: DP (fixed by gpt) (TLE)
    public int coinChange_0_3(int[] coins, int amount) {
        int INF = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        Set<Integer> set = new HashSet<>();
        for (int c : coins) set.add(c);

        /** NOTE !!
         *
         *  below is NOT efficient, and may cause TLE
         */
        // forward relaxation
        for (int i = 0; i <= amount; i++) {
            if (dp[i] == INF) continue; // unreachable state

            for (int j = i + 1; j <= amount; j++) {
                int diff = j - i;
                if (set.contains(diff)) {
                    dp[j] = Math.min(dp[j], dp[i] + 1);
                }
            }
        }

        return dp[amount] == INF ? -1 : dp[amount];
    }



    // V0-4
    // IDEA: BFS (fix by GPT)
    public static class CoinStatus {
        int curSum;
        int numCoins;

        public CoinStatus(int curSum, int numCoins) {
            this.curSum = curSum;
            this.numCoins = numCoins;
        }
    }

    public int coinChange_0_4(int[] coins, int amount) {
        // Edge case
        if (amount == 0) {
            return 0;
        }

        // Convert to Integer array for sorting
        Integer[] coins_ = Arrays.stream(coins).boxed().toArray(Integer[]::new);
        /** NOTE !!! sort is not necessary */
        // Arrays.sort(coins_, Collections.reverseOrder()); // Sort coins in descending order

        // BFS Initialization
        Queue<CoinStatus> queue = new LinkedList<>();
        /**
         * NOTE !!!
         *
         *  we use visited to avoid duplicated computation,
         *  can also use `Hash Map` approach (see solution V-0)
         */
        Set<Integer> visited = new HashSet<>();

        /**
         *  NOTE !!!
         *
         *   at BFS init, we ONLY add `0` coin as its init state,
         *   (but NOT add all coin in coins to it)
         */
        queue.add(new CoinStatus(0, 0)); // Start from sum = 0, numCoins = 0
        /**
         * NOTE !!!
         *
         *  we use visited to avoid duplicated computation
         */
        visited.add(0);

        while (!queue.isEmpty()) {
            CoinStatus curr = queue.poll();

            for (int coin : coins_) {
                int newSum = curr.curSum + coin;

                if (newSum == amount) {
                    return curr.numCoins + 1; // Found the answer
                }

                if (newSum < amount && !visited.contains(newSum)) {
                    queue.add(new CoinStatus(newSum, curr.numCoins + 1));
                    visited.add(newSum); // Avoid duplicate states
                }
            }
        }

        return -1; // No solution found
    }

    // V0-5
    // IDEA: BFS (fixed by gpt)
    public int coinChange_0_5(int[] coins, int amount) {
        // edge case: if amount is 0, no coins are needed
        if (amount == 0) {
            return 0;
        }

        // edge case: if no coins are provided
        if (coins == null || coins.length == 0) {
            return -1;
        }

        // BFS initialization
        Queue<Integer> q = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        q.offer(0); // start with sum 0
        visited.add(0);

        int res = 0;

        while (!q.isEmpty()) {
            int size = q.size();
            res++;

            for (int i = 0; i < size; i++) {
                int curSum = q.poll();

                // Try each coin and check if we can reach the amount
                for (int coin : coins) {
                    int newSum = curSum + coin;

                    // If new sum equals the amount, we have found the solution
                    if (newSum == amount) {
                        return res;
                    }

                    // If new sum is less than the amount and not visited yet, add to the queue
                    if (newSum < amount && !visited.contains(newSum)) {
                        visited.add(newSum);
                        q.offer(newSum);
                    }
                }
            }
        }

        return -1; // If no solution is found
    }

    // V0-6
    // IDEA: BFS (TLE)
    public int coinChange_0_6(int[] coins, int amount) {
        // edge
        if (coins == null || coins.length == 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }

        // if all `coins` bigger than amount
        boolean possible = false;
        for (int c : coins) {
            if (c <= amount) {
                possible = true;
                break;
            }
        }
        if (!possible) {
            return -1;
        }

        // sorting (big -> small)
        List<Integer> coins_ = new ArrayList<>();
        for (int c : coins) {
            coins_.add(c);
        }
        Collections.sort(coins_, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        // q : [cur_val, cnt]
        Queue<int[]> q = new LinkedList<>();
        // add init val
        q.add(new int[] { 0, 0 });

        int res = 0;

        // bfs
        while (!q.isEmpty()) {

            int[] cur = q.poll();
            int cur_val = cur[0];
            int cnt = cur[1];

            if (cur_val == amount) {
                return cnt;
            }

            if (cur_val > amount) {
                continue;
            }

            // loop over coins
            for (int c : coins) {
                q.add(new int[] { cur_val + c, cnt + 1 });
            }
        }

        return -1;
    }

    // V0-4-1 (fixed of V0-4)
    // IDEA: BFS
    public int coinChange_0_4_1(int[] coins, int amount) {
        if (amount < 0) return -1;
        if (amount == 0) return 0;

        // Use a queue for BFS: [current sum, number of coins used]
        Queue<int[]> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(new int[]{0, 0});
        visited.add(0);

        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int currentSum = state[0];
            int coinCount = state[1];

            for (int coin : coins) {
                int nextSum = currentSum + coin;

                if (nextSum == amount) return coinCount + 1;
                if (nextSum > amount || visited.contains(nextSum)) continue;

                queue.add(new int[]{nextSum, coinCount + 1});
                visited.add(nextSum);
            }
        }

        return -1; // No solution found
    }


    // V1-1
    // https://neetcode.io/problems/coin-change
    // IDEA: RECURSION
    public int dfs(int[] coins, int amount) {
        if (amount == 0) return 0;

        int res = (int) 1e9;
        for (int coin : coins) {
            if (amount - coin >= 0) {
                res = Math.min(res,
                        1 + dfs(coins, amount - coin));
            }
        }
        return res;
    }

    public int coinChange_1_1(int[] coins, int amount) {
        int minCoins = dfs(coins, amount);
        return (minCoins >= 1e9) ? -1 : minCoins;
    }

    // V1-2
    // https://neetcode.io/problems/coin-change
    // IDEA: Dynamic Programming (Top-Down)
    HashMap<Integer, Integer> memo = new HashMap<>();

    public int dfs(int amount, int[] coins) {
        if (amount == 0) return 0;
        if (memo.containsKey(amount))
            return memo.get(amount);

        int res = Integer.MAX_VALUE;
        for (int coin : coins) {
            if (amount - coin >= 0) {
                int result = dfs(amount - coin, coins);
                if (result != Integer.MAX_VALUE) {
                    res = Math.min(res, 1 + result);
                }
            }
        }

        memo.put(amount, res);
        return res;
    }

    public int coinChange_1_2(int[] coins, int amount) {
        int minCoins = dfs(amount, coins);
        return minCoins == Integer.MAX_VALUE ? -1 : minCoins;
    }


    // V1-3
    // https://neetcode.io/problems/coin-change
    // IDEA: Dynamic Programming (Bottom-Up)
    public int coinChange_1_3(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (coins[j] <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }


    // V1-4
    // https://neetcode.io/problems/coin-change
    // IDEA: BFS
    public int coinChange_1_4(int[] coins, int amount) {
        if (amount == 0) return 0;

        Queue<Integer> q = new LinkedList<>();
        q.add(0);
        boolean[] seen = new boolean[amount + 1];
        seen[0] = true;
        int res = 0;

        while (!q.isEmpty()) {
            res++;
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int cur = q.poll();
                for (int coin : coins) {
                    int nxt = cur + coin;
                    if (nxt == amount) return res;
                    if (nxt > amount || seen[nxt]) continue;
                    seen[nxt] = true;
                    q.add(nxt);
                }
            }
        }

        return -1;
    }



    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/coin-change/solutions/4564988/pretty-intuitive-approach-with-recursion-memoization/
    public int coinChange_2(int[] coins, int amount) {
        if(amount == 0){
            return 0;
        }
        Map<Integer, Integer> cache = new HashMap<>();
        int ans = dfs(coins, amount, cache);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    public int dfs(int[] coins, int amount, Map<Integer, Integer> cache){
        if(cache.containsKey(amount)){
            return cache.get(amount);
        }
        if(amount < 0){
            return Integer.MAX_VALUE;
        }
        if(amount == 0){
            return 0;
        }
        List<Integer> results = new ArrayList<>();
        for(int coin : coins){
            int res = dfs(coins, amount - coin, cache);
            if(res != Integer.MAX_VALUE) results.add(res + 1);
        }

        int ans = Integer.MAX_VALUE;
        for(int res: results) ans = Math.min(ans, res);
        cache.put(amount, ans);
        return ans;
    }

    // V3
    // IDEA : DP
    // https://leetcode.com/problems/coin-change/solutions/4638602/java/
    public int find( int i,int tar,int[] arr, Integer[][] dp ) {

        if( tar == 0 ) return 0;
        if( i >= arr.length  ) return (int)1e9;
        if( dp[i][tar] != null ) return dp[i][tar];


        int take=(int)1e9;
        int ntake =(int)1e9;

        if(arr[i]  <= tar) take= 1 + find( i,tar-arr[i],arr,dp );
        ntake= find( i+1,tar,arr,dp );


        return dp[i][tar] = Math.min(take,ntake);
    }

    public int coinChange_3(int[] coins, int amount) {
        int n = coins.length;

        Integer[][] dp = new Integer[n][amount+1];

        if( amount==0) return 0;
        int ans = find( 0,amount,coins,dp );

        if( ans == (int)1e9) return -1;
        return ans ;

    }

    // V4
    // IDEA : DP
    // https://leetcode.com/problems/coin-change/solutions/4649450/easy-to-understand-java-code-aditya-verma-approach/
    public int coinChange_4(int[] coins, int amount) {
        int n = coins.length;
        int[][] t = new int[n + 1][amount + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= amount; j++) {
                if (j == 0) {
                    t[i][j] = 0;
                } else {
                    t[i][j] = Integer.MAX_VALUE - 1;
                }
            }
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= amount; j++) {
                if (coins[i - 1] <= j) {
                    t[i][j] = Math.min(t[i][j - coins[i - 1]] + 1, t[i - 1][j]);
                } else {
                    t[i][j] = t[i - 1][j];
                }
            }
        }

        return t[n][amount] == Integer.MAX_VALUE - 1 ? -1 : t[n][amount];
    }




}
