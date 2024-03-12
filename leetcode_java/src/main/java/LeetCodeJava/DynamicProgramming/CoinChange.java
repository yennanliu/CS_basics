package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/coin-change/description/

import java.util.*;

public class CoinChange {

    // V0
    // IDEA : BFS
    // TODO : implement it
//    public int coinChange(int[] coins, int amount) {
//        return 0;
//    }

    // V0
    // IDEA : BFS (modified by GPT)
    public int coinChange_0(int[] coins, int amount) {
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
