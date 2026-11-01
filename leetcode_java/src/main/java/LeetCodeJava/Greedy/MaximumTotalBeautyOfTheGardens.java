package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-total-beauty-of-the-gardens/description/

import java.util.Arrays;

/**
 * 2234. Maximum Total Beauty of the Gardens
 * Hard
 *
 * Alice is a caretaker of n gardens and she wants to plant flowers to maximize the total beauty of all her gardens.
 *
 * You are given a 0-indexed integer array flowers of size n, where flowers[i] is the number of flowers already planted in the ith garden. Flowers that are already planted cannot be removed. You are then given another integer newFlowers, which is the maximum number of flowers that Alice can additionally plant. You are also given the integers target, full, and partial.
 *
 * A garden is considered complete if it has at least target flowers. The total beauty of the gardens is then determined as the sum of the following:
 *
 * The number of complete gardens multiplied by full.
 * The minimum number of flowers in any of the incomplete gardens multiplied by partial. If there are no incomplete gardens, then this value will be 0.
 *
 * Return the maximum total beauty that Alice can obtain after planting at most newFlowers flowers.
 *
 *
 * Example 1:
 *
 * Input: flowers = [1,3,1,1], newFlowers = 7, target = 6, full = 12, partial = 1
 * Output: 14
 * Explanation: Alice can plant
 * - 2 flowers in the 0th garden
 * - 3 flowers in the 1st garden
 * - 1 flower in the 2nd garden
 * - 1 flower in the 3rd garden
 * The gardens will then be [3,6,2,2]. She planted a total of 2 + 3 + 1 + 1 = 7 flowers.
 * There is 1 garden that is complete.
 * The minimum number of flowers in the incomplete gardens is 2.
 * Thus, the total beauty is 1 * 12 + 2 * 1 = 12 + 2 = 14.
 * No other way of planting flowers can obtain a total beauty higher than 14.
 *
 * Example 2:
 *
 * Input: flowers = [2,4,5,3], newFlowers = 10, target = 5, full = 2, partial = 6
 * Output: 30
 * Explanation: Alice can plant
 * - 3 flowers in the 0th garden
 * - 0 flowers in the 1st garden
 * - 0 flowers in the 2nd garden
 * - 2 flowers in the 3rd garden
 * The gardens will then be [5,4,5,5]. She planted a total of 3 + 0 + 0 + 2 = 5 flowers.
 * There are 3 gardens that are complete.
 * The minimum number of flowers in the incomplete gardens is 4.
 * Thus, the total beauty is 3 * 2 + 4 * 6 = 6 + 24 = 30.
 * No other way of planting flowers can obtain a total beauty higher than 30.
 * Note that Alice could make all the gardens complete but in this case, she would obtain a lower total beauty.
 *
 *
 * Constraints:
 *
 * 1 <= flowers.length <= 10^5
 * 1 <= flowers[i], target <= 10^5
 * 1 <= newFlowers <= 10^10
 * 1 <= full, partial <= 10^5
 *
 */
public class MaximumTotalBeautyOfTheGardens {

    // V0
    // IDEA: SORT + TRY EVERY NUMBER OF COMPLETE GARDENS + BINARY SEARCH THE WATER LEVEL
    //  sort ascending. if `complete` gardens end up complete, the cheapest choice is the
    //  `complete` LARGEST ones (a suffix). the rest, flowers[0 .. rest - 1], stay incomplete,
    //  and the leftover budget raises their MINIMUM as high as possible, capped at target - 1.
    //  raising the minimum is "water filling": levelling flowers[0 .. idx] up to flowers[idx]
    //  costs flowers[idx] * (idx + 1) - prefix[idx + 1], which grows with idx, so binary search
    //  the largest affordable idx, then spread the remainder evenly over those idx + 1 gardens.
    //  the completion cost is accumulated as `complete` grows; stop once the budget goes negative.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long maximumBeauty(int[] flowers, long newFlowers, int target, int full, int partial) {
        int n = flowers.length;
        int[] sorted = flowers.clone();
        Arrays.sort(sorted);

        // prefix[i] = sorted[0] + ... + sorted[i - 1]
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + sorted[i];
        }

        // gardens that are already complete cost nothing
        int alreadyComplete = 0;
        for (int i = 0; i < n; i++) {
            if (sorted[i] >= target) {
                alreadyComplete++;
            }
        }

        long best = 0;
        long budget = newFlowers;
        for (int complete = alreadyComplete; complete <= n; complete++) {
            // make the complete-th largest garden complete (no-op for one already complete)
            if (complete > 0) {
                long need = (long) target - sorted[n - complete];
                if (need > 0) {
                    budget -= need;
                }
            }
            if (budget < 0) {
                break;
            }

            // incomplete gardens are sorted[0 .. rest - 1], all of them < target
            int rest = n - complete;
            long minIncomplete = 0;
            if (rest > 0) {
                // largest idx such that levelling sorted[0 .. idx] up to sorted[idx] is affordable
                int low = 0;
                int high = rest - 1;
                while (low < high) {
                    int mid = low + (high - low + 1) / 2;
                    if (levelCost(sorted, prefix, mid) <= budget) {
                        low = mid;
                    } else {
                        high = mid - 1;
                    }
                }
                long remaining = budget - levelCost(sorted, prefix, low);
                minIncomplete = sorted[low] + remaining / (low + 1);
                // reaching target would make it complete, which this case does not count
                minIncomplete = Math.min(minIncomplete, (long) target - 1);
            }

            long beauty = (long) complete * full + minIncomplete * partial;
            best = Math.max(best, beauty);
        }
        return best;
    }

    /** Flowers needed to raise sorted[0 .. idx] all up to sorted[idx]. */
    private long levelCost(int[] sorted, long[] prefix, int idx) {
        return (long) sorted[idx] * (idx + 1) - prefix[idx + 1];
    }
}
