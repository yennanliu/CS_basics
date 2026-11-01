package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/maximum-coins-heroes-can-collect/description/

import java.util.Arrays;

/**
 * 2838. Maximum Coins Heroes Can Collect
 * Medium
 *
 * There is a battle and n heroes are trying to defeat m monsters. You are given two 1-indexed
 * arrays of positive integers heroes and monsters of length n and m, respectively. heroes[i] is
 * the power of ith hero, and monsters[i] is the power of ith monster.
 *
 * The ith hero can defeat the jth monster if monsters[j] <= heroes[i].
 *
 * You are also given a 1-indexed array coins of length m consisting of positive integers.
 * coins[i] is the number of coins that each hero earns after defeating the ith monster.
 *
 * Return an array ans of length n where ans[i] is the maximum number of coins that the ith hero
 * can collect from this battle.
 *
 * Notes
 *
 * - The health of a hero doesn't get reduced after defeating a monster.
 * - Multiple heroes can defeat a monster, but each monster can be defeated by a given hero only
 *   once.
 *
 *
 * Example 1:
 *
 * Input: heroes = [1,4,2], monsters = [1,1,5,2,3], coins = [2,3,4,5,6]
 * Output: [5,16,10]
 *
 * Example 2:
 *
 * Input: heroes = [5], monsters = [2,3,1,2], coins = [10,6,5,2]
 * Output: [23]
 *
 * Example 3:
 *
 * Input: heroes = [4,4], monsters = [5,7,8], coins = [1,1,1]
 * Output: [0,0]
 *
 *
 * Constraints:
 *
 * 1 <= n == heroes.length <= 10^5
 * 1 <= m == monsters.length <= 10^5
 * coins.length == m
 * 1 <= heroes[i], monsters[i], coins[i] <= 10^9
 *
 */
public class MaximumCoinsHeroesCanCollect {

    // V0
    // IDEA: SORT MONSTERS + PREFIX SUM OF COINS + TWO POINTERS OVER HEROES SORTED BY POWER
    //  a hero of power h collects every monster with power <= h, so the answer is the coin
    //  prefix sum up to the number of such monsters.
    /**
     * time = O(N log N + M log M)
     * space = O(N + M)
     */
    public long[] maximumCoins(int[] heroes, int[] monsters, int[] coins) {
        int m = monsters.length;
        int n = heroes.length;

        // monsters as {power, coins}, sorted by power
        int[][] monsterPairs = new int[m][2];
        for (int i = 0; i < m; i++) {
            monsterPairs[i][0] = monsters[i];
            monsterPairs[i][1] = coins[i];
        }
        Arrays.sort(monsterPairs, (a, b) -> Integer.compare(a[0], b[0]));

        // prefix[j] = coins from the j weakest monsters (up to 10^14 -> long)
        long[] prefix = new long[m + 1];
        for (int i = 0; i < m; i++) {
            prefix[i + 1] = prefix[i] + monsterPairs[i][1];
        }

        // visit heroes weakest first, so the monster pointer only moves forward
        Integer[] heroOrder = new Integer[n];
        for (int i = 0; i < n; i++) {
            heroOrder[i] = i;
        }
        Arrays.sort(heroOrder, (a, b) -> Integer.compare(heroes[a], heroes[b]));

        long[] result = new long[n];
        int beaten = 0;
        for (int heroIndex : heroOrder) {
            int power = heroes[heroIndex];
            while (beaten < m && monsterPairs[beaten][0] <= power) {
                beaten++;
            }
            // write back in the original hero order
            result[heroIndex] = prefix[beaten];
        }
        return result;
    }
}
