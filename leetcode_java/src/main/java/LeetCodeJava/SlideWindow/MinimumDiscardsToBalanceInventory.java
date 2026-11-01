package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/minimum-discards-to-balance-inventory/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3679. Minimum Discards to Balance Inventory
 * Medium
 *
 * You are given two integers w and m, and an integer array arrivals, where arrivals[i] is the
 * type of item arriving on day i (days are 1-indexed).
 *
 * Items are managed according to the following rules:
 *
 * Each arrival may be kept or discarded; an item may only be discarded on its arrival day.
 * For each day i, consider the window of days [max(1, i - w + 1), i] (the w most recent days up
 * to day i):
 *   For any such window, each item type may appear at most m times among kept arrivals whose
 *   arrival day lies in that window.
 *   If keeping the arrival on day i would cause its type to appear more than m times in the
 *   window, that arrival must be discarded.
 *
 * Return the minimum number of arrivals to be discarded so that every w-day window contains at
 * most m occurrences of each type.
 *
 *
 * Example 1:
 *
 * Input: arrivals = [1,2,1,3,1], w = 4, m = 2
 * Output: 0
 *
 * Example 2:
 *
 * Input: arrivals = [1,2,3,3,3,4], w = 3, m = 2
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= arrivals.length <= 10^5
 * 1 <= arrivals[i] <= 10^5
 * 1 <= w <= arrivals.length
 * 1 <= m <= w
 *
 */
public class MinimumDiscardsToBalanceInventory {

    // V0
    // IDEA: SIMULATE A SLIDING WINDOW OF THE LAST w DAYS, COUNTING ONLY KEPT ITEMS
    //  the rule forces every decision, so the minimum is just how often the forced discard fires.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minArrivalsToDiscard(int[] arrivals, int w, int m) {
        int n = arrivals.length;
        // type -> kept arrivals of that type inside the window
        Map<Integer, Integer> keptCount = new HashMap<>();
        boolean[] kept = new boolean[n];
        int oldest = 0; // oldest day that may still be inside the window
        int discarded = 0;

        for (int day = 0; day < n; day++) {
            // expire days older than day - w + 1 BEFORE judging today
            while (oldest <= day - w) {
                if (kept[oldest]) {
                    int type = arrivals[oldest];
                    keptCount.put(type, keptCount.get(type) - 1);
                }
                oldest++;
            }

            int type = arrivals[day];
            int current = keptCount.getOrDefault(type, 0);
            if (current < m) {
                keptCount.put(type, current + 1);
                kept[day] = true;
            } else {
                discarded++;
            }
        }
        return discarded;
    }
}
