package LeetCodeJava.Greedy;

// https://leetcode.com/problems/earliest-possible-day-of-full-bloom/description/

import java.util.Arrays;

/**
 * 2136. Earliest Possible Day of Full Bloom
 * Hard
 *
 * You have n flower seeds. Every seed must be planted first before it can begin to grow, then bloom.
 * Planting a seed takes time and so does the growth of a seed. You are given two 0-indexed integer arrays
 * plantTime and growTime, of length n each:
 *
 * plantTime[i] is the number of full days it takes you to plant the ith seed. Every day, you can work on
 * planting exactly one seed. You do not have to work on planting the same seed on consecutive days, but the
 * planting of a seed is not complete until you have worked plantTime[i] days on planting it in total.
 * growTime[i] is the number of full days it takes the ith seed to grow after being completely planted.
 * After the last day of its growth, the flower blooms and stays bloomed forever.
 *
 * From the beginning of day 0, you can plant the seeds in any order.
 *
 * Return the earliest possible day where all seeds are blooming.
 *
 *
 * Example 1:
 *
 * Input: plantTime = [1,4,3], growTime = [2,3,1]
 * Output: 9
 *
 * Example 2:
 *
 * Input: plantTime = [1,2,3,2], growTime = [2,1,2,1]
 * Output: 9
 *
 * Example 3:
 *
 * Input: plantTime = [1], growTime = [1]
 * Output: 2
 *
 *
 * Constraints:
 *
 * n == plantTime.length == growTime.length
 * 1 <= n <= 10^5
 * 1 <= plantTime[i], growTime[i] <= 10^4
 *
 */
public class EarliestPossibleDayOfFullBloom {

    // V0
    // IDEA: GREEDY - PLANT THE SLOWEST-GROWING SEEDS FIRST (EXCHANGE ARGUMENT)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int earliestFullBloom(int[] plantTime, int[] growTime) {
        int n = plantTime.length;

        // sort seed indices by growTime, largest first: a long growth then overlaps later planting
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(growTime[b], growTime[a]));

        // total planting days are fixed; each seed blooms at (planting finished so far) + its growTime
        int plantedDays = 0;
        int lastBloom = 0;
        for (int seed : order) {
            plantedDays += plantTime[seed];
            lastBloom = Math.max(lastBloom, plantedDays + growTime[seed]);
        }
        return lastBloom;
    }
}
