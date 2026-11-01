package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-time-to-kill-all-monsters/description/

import java.util.*;

/**
 * 2403. Minimum Time to Kill All Monsters
 * Hard
 *
 * (premium / locked problem)
 *
 * You are given an integer array power where power[i] is the minimum required energy to defeat the ith monster.
 *
 * You start with 0 energy and you have an unlimited number of days to defeat all the monsters. Each day, you gain some energy and you may defeat a monster.
 *
 * Each day, you gain mana points equal to your current gain. Initially, your gain is 1.
 * If your current mana points is greater than or equal to power[i], you may defeat the ith monster. Your mana points reset to 0 and your gain increases by 1.
 *
 * Return the minimum number of days needed to defeat all the monsters.
 *
 *
 * Example 1:
 *
 * Input: power = [3,1,4]
 * Output: 4
 * Explanation:
 * The optimal way to defeat all the monsters is as follows:
 * - Day 1: Gain 1 mana point to get a total of 1 mana point. Defeat the second monster.
 * - Day 2: Gain 2 mana points to get a total of 2 mana points.
 * - Day 3: Gain 2 mana points to get a total of 4 mana points. Defeat the third monster.
 * - Day 4: Gain 3 mana points to get a total of 3 mana points. Defeat the first monster.
 * It can be proven that 4 is the minimum number of days needed.
 *
 * Example 2:
 *
 * Input: power = [1,1,4]
 * Output: 4
 * Explanation:
 * The optimal way to defeat all the monsters is as follows:
 * - Day 1: Gain 1 mana point to get a total of 1 mana point. Defeat the first monster.
 * - Day 2: Gain 2 mana points to get a total of 2 mana points. Defeat the second monster.
 * - Day 3: Gain 3 mana points to get a total of 3 mana points.
 * - Day 4: Gain 3 mana points to get a total of 6 mana points. Defeat the third monster.
 * It can be proven that 4 is the minimum number of days needed.
 *
 * Example 3:
 *
 * Input: power = [1,2,4,9]
 * Output: 6
 * Explanation:
 * The optimal way to defeat all the monsters is as follows:
 * - Day 1: Gain 1 mana point to get a total of 1 mana point. Defeat the first monster.
 * - Day 2: Gain 2 mana points to get a total of 2 mana points. Defeat the second monster.
 * - Day 3: Gain 3 mana points to get a total of 3 mana points.
 * - Day 4: Gain 3 mana points to get a total of 6 mana points.
 * - Day 5: Gain 3 mana points to get a total of 9 mana points. Defeat the fourth monster.
 * - Day 6: Gain 4 mana points to get a total of 4 mana points. Defeat the third monster.
 * It can be proven that 6 is the minimum number of days needed.
 *
 *
 * Constraints:
 *
 * 1 <= power.length <= 17
 * 1 <= power[i] <= 10^9
 *
 */
public class MinimumTimeToKillAllMonsters {

    // V0
    // IDEA: BITMASK DP — dp[mask] = MIN DAYS TO KILL THE MONSTERS IN mask; GAIN = KILLED + 1
    /**
     * time = O(2^N * N)
     * space = O(2^N)
     */
    public long minimumTime(int[] power) {
        int n = power.length;
        int size = 1 << n;

        // dp[mask] = fewest days to kill exactly the monsters in mask
        long[] dp = new long[size];
        Arrays.fill(dp, Long.MAX_VALUE);
        dp[0] = 0;

        for (int mask = 0; mask < size; mask++) {
            if (dp[mask] == Long.MAX_VALUE) {
                continue;
            }
            // after each kill mana resets and the daily gain grows by one
            int gain = Integer.bitCount(mask) + 1;
            for (int i = 0; i < n; i++) {
                boolean alreadyKilled = ((mask >> i) & 1) == 1;
                if (alreadyKilled) {
                    continue;
                }
                // days to reach power[i] mana at `gain` per day = ceil(power[i] / gain)
                long days = (power[i] + gain - 1) / gain;
                int nextMask = mask | (1 << i);
                dp[nextMask] = Math.min(dp[nextMask], dp[mask] + days);
            }
        }
        return dp[size - 1];
    }
}
