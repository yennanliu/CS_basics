package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-total-damage-with-spell-casting/description/

import java.util.*;

/**
 * 3186. Maximum Total Damage With Spell Casting
 * Medium
 *
 * A magician has various spells.
 *
 * You are given an array power, where each element represents the damage of a spell. Multiple spells can have the same damage value.
 *
 * It is a known fact that if a magician decides to cast a spell with a damage of power[i], they cannot cast any spell with a damage of power[i] - 2, power[i] - 1, power[i] + 1, or power[i] + 2.
 *
 * Each spell can be cast only once.
 *
 * Return the maximum possible total damage that a magician can cast.
 *
 *
 * Example 1:
 *
 * Input: power = [1,1,3,4]
 * Output: 6
 * Explanation:
 * The maximum possible damage of 6 is produced by casting spells 0, 1, 3 with damage 1, 1, 4.
 *
 * Example 2:
 *
 * Input: power = [7,1,6,6]
 * Output: 13
 * Explanation:
 * The maximum possible damage of 13 is produced by casting spells 1, 2, 3 with damage 1, 6, 6.
 *
 *
 * Constraints:
 *
 * 1 <= power.length <= 10^5
 * 1 <= power[i] <= 10^9
 *
 */
public class MaximumTotalDamageWithSpellCasting {

    // V0
    // IDEA: GROUP EQUAL DAMAGES + HOUSE ROBBER DP OVER SORTED DISTINCT VALUES
    //   Spells with the same damage never block each other, so value v is taken all at once
    //   for v * count(v). Taking v forbids [v-2, v+2].
    //   dp[i + 1] = best total using the first i+1 distinct values
    //             = max(dp[i], v * count + dp[j]),  j = first index with values[j] >= v - 2
    //   values are sorted, so j only moves forward (one moving pointer, no binary search).
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long maximumTotalDamage(int[] power) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int p : power) {
            count.put(p, count.getOrDefault(p, 0) + 1);
        }
        List<Integer> values = new ArrayList<>(count.keySet());
        Collections.sort(values);
        int m = values.size();

        long[] dp = new long[m + 1];
        // firstAllowed = first index whose value is >= v - 2 (indices before it are compatible)
        int firstAllowed = 0;
        for (int i = 0; i < m; i++) {
            int value = values.get(i);
            while (values.get(firstAllowed) < value - 2) {
                firstAllowed++;
            }
            long take = (long) value * count.get(value) + dp[firstAllowed];
            long skip = dp[i];
            dp[i + 1] = Math.max(skip, take);
        }
        return dp[m];
    }
}
