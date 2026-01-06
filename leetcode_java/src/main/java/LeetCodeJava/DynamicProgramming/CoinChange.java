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


        /** NOTE !!!
         *
         * "backward-looking" approach:
         *
         *  "To reach sum i, which coin did I just use?"
         */
        // 4. Iterate through every amount from 1 to 'amount'
        for (int i = 1; i <= amount; i++) {
            // 5. For each amount, try every coin
            for (int coin : coins) {
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
