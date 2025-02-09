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
    // IDEA : BFS
    // TODO : implement it
//    public int coinChange(int[] coins, int amount) {
//        return 0;
//    }

    // V0-1
    // IDEA : BFS (modified by GPT)
    public int coinChange_0_1(int[] coins, int amount) {
        /**
         * NOTE !!!
         *
         *  we use `steps` (hash map) to avoid duplicated computation
         */
        Map<Integer, Integer> steps = new HashMap<>();
        steps.put(0, 0);
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);

        while (!queue.isEmpty()) {
            int tmp = queue.poll();
            int level = steps.get(tmp);
            if (tmp == amount) {
                return level;
            }
            for (int c : coins) {
                if (tmp + c > amount) {
                    continue;
                }
                if (!steps.containsKey(tmp + c)) {
                    queue.offer(tmp + c);
                    steps.put(tmp + c, level + 1);
                }
            }
        }

        return -1;
    }

    // V0-2
    // IDEA: BFS (fix by GPT)
    public static class CoinStatus {
        int curSum;
        int numCoins;

        public CoinStatus(int curSum, int numCoins) {
            this.curSum = curSum;
            this.numCoins = numCoins;
        }
    }

    public int coinChange_0_2(int[] coins, int amount) {
        // Edge case
        if (amount == 0) {
            return 0;
        }

        // Convert to Integer array for sorting
        Integer[] coins_ = Arrays.stream(coins).boxed().toArray(Integer[]::new);
        /** NOTE !!! sort is not necessary*/
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

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/coin-change/solutions/4564988/pretty-intuitive-approach-with-recursion-memoization/
    public int coinChange_1(int[] coins, int amount) {
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

    // V2
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

    public int coinChange_2(int[] coins, int amount) {
        int n = coins.length;

        Integer[][] dp = new Integer[n][amount+1];

        if( amount==0) return 0;
        int ans = find( 0,amount,coins,dp );

        if( ans == (int)1e9) return -1;
        return ans ;

    }

    // V3
    // IDEA : DP
    // https://leetcode.com/problems/coin-change/solutions/4649450/easy-to-understand-java-code-aditya-verma-approach/
    public int coinChange_3(int[] coins, int amount) {
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
