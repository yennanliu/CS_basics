package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-taps-to-open-to-water-a-garden/description/

import java.util.*;

/**
 * 1326. Minimum Number of Taps to Open to Water a Garden
 * Hard
 *
 * There is a one-dimensional garden on the x-axis. The garden starts at the point 0
 * and ends at the point n. (i.e., the length of the garden is n).
 *
 * There are n + 1 taps located at points [0, 1, ..., n] in the garden.
 *
 * Given an integer n and an integer array ranges of length n + 1 where ranges[i] (0-indexed)
 * means the i-th tap can water the area [i - ranges[i], i + ranges[i]] if it was open.
 *
 * Return the minimum number of taps that should be open to water the whole garden,
 * If the garden cannot be watered return -1.
 *
 *
 * Example 1:
 *
 * Input: n = 5, ranges = [3,4,1,1,0,0]
 * Output: 1
 * Explanation: The tap at point 0 can cover the interval [-3,3]
 * The tap at point 1 can cover the interval [-3,5]
 * The tap at point 2 can cover the interval [1,3]
 * The tap at point 3 can cover the interval [2,4]
 * The tap at point 4 can cover the interval [4,4]
 * The tap at point 5 can cover the interval [5,5]
 * Opening Only the second tap will water the whole garden [0,5]
 *
 * Example 2:
 *
 * Input: n = 3, ranges = [0,0,0,0]
 * Output: -1
 * Explanation: Even if you activate all the four taps you cannot water the whole garden.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^4
 * ranges.length == n + 1
 * 0 <= ranges[i] <= 100
 *
 */
public class MinimumNumberOfTapsToOpenToWaterAGarden {

    // V0
    // IDEA: GREEDY / JUMP GAME II (reach[left] = farthest right end of any tap starting at left)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minTaps(int n, int[] ranges) {
        // collapse the taps: reach[left] = farthest point watered by a tap whose
        // interval starts at `left` (anything left of 0 is clamped to 0)
        int[] reach = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            int left = Math.max(0, i - ranges[i]);
            int right = i + ranges[i];
            reach[left] = Math.max(reach[left], right);
        }

        int taps = 0;
        // farthest point reachable by any tap starting at or before i
        int farthest = 0;
        // right end covered by the taps opened so far
        int coveredUntil = 0;

        // only positions 0..n-1 need a step: covering [n-1, n] finishes the garden
        for (int i = 0; i < n; i++) {
            farthest = Math.max(farthest, reach[i]);
            if (farthest <= i) {
                // nothing waters the segment [i, i + 1]
                return -1;
            }
            if (i == coveredUntil) {
                // the open taps stop here: open the one that reaches farthest
                taps++;
                coveredUntil = farthest;
            }
        }
        return taps;
    }

    // V1
    // IDEA: DP (dp[x] = min taps to water [0, x]; taps processed in order of their left end)
    /**
     * time = O(N * R)   R = max range
     * space = O(N)
     */
    public int minTaps_1(int n, int[] ranges) {
        // reach[left] = farthest right end of a tap starting at `left` (-1 = no tap)
        int[] reach = new int[n + 1];
        Arrays.fill(reach, -1);
        for (int i = 0; i <= n; i++) {
            int left = Math.max(0, i - ranges[i]);
            int right = Math.min(n, i + ranges[i]);
            reach[left] = Math.max(reach[left], right);
        }

        final int INF = n + 2;
        int[] dp = new int[n + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        // walking `left` upward makes dp[left] final before it is used:
        // only taps starting before `left` can water up to `left`
        for (int left = 0; left <= n; left++) {
            if (reach[left] < 0 || dp[left] >= INF) {
                continue;
            }
            // opening this tap waters (left, reach[left]] for one more tap
            for (int x = left + 1; x <= reach[left]; x++) {
                dp[x] = Math.min(dp[x], dp[left] + 1);
            }
        }

        if (dp[n] >= INF) {
            return -1;
        }
        return dp[n];
    }
}
