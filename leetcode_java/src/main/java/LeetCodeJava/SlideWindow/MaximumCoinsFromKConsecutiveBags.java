package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-coins-from-k-consecutive-bags/description/

import java.util.Arrays;

/**
 * 3413. Maximum Coins From K Consecutive Bags
 * Medium
 *
 * There are an infinite amount of bags on a number line, one bag for each coordinate. Some of
 * these bags contain coins.
 *
 * You are given a 2D array coins, where coins[i] = [l_i, r_i, c_i] denotes that every bag from
 * l_i to r_i contains c_i coins.
 *
 * The segments that coins contain are non-overlapping.
 *
 * You are also given an integer k.
 *
 * Return the maximum amount of coins you can obtain by collecting k consecutive bags.
 *
 *
 * Example 1:
 *
 * Input: coins = [[8,10,1],[1,3,2],[5,6,4]], k = 4
 * Output: 10
 * Explanation:
 * Selecting bags at positions [3, 4, 5, 6] gives the maximum number of coins: 2 + 0 + 4 + 4 = 10.
 *
 * Example 2:
 *
 * Input: coins = [[1,10,3]], k = 2
 * Output: 6
 *
 *
 * Constraints:
 *
 * 1 <= coins.length <= 10^5
 * 1 <= k <= 10^9
 * coins[i] == [l_i, r_i, c_i]
 * 1 <= l_i <= r_i <= 10^9
 * 1 <= c_i <= 1000
 * The given segments are non-overlapping.
 *
 */
public class MaximumCoinsFromKConsecutiveBags {

    // V0
    // IDEA: SOME BEST WINDOW STARTS AT A SEGMENT'S LEFT END OR ENDS AT A SEGMENT'S RIGHT END
    //  sweep 1 anchors the window's left edge at every l_i (two pointers over whole segments plus
    //  one partial segment on the right); sweep 2 is the mirror image, done by reflecting every
    //  segment [l, r] -> [-r, -l] and running sweep 1 again.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long maximumCoins(int[][] coins, int k) {
        int n = coins.length;
        long[][] forward = new long[n][3];
        long[][] backward = new long[n][3];
        for (int i = 0; i < n; i++) {
            forward[i][0] = coins[i][0];
            forward[i][1] = coins[i][1];
            forward[i][2] = coins[i][2];
            // reflect through the origin: a window ending at r becomes one starting at -r
            backward[i][0] = -coins[i][1];
            backward[i][1] = -coins[i][0];
            backward[i][2] = coins[i][2];
        }
        return Math.max(sweepFromLeftEdges(forward, k), sweepFromLeftEdges(backward, k));
    }

    /** Best window of width k whose left edge sits on some segment's left end. */
    private long sweepFromLeftEdges(long[][] segments, int k) {
        Arrays.sort(segments, (a, b) -> Long.compare(a[0], b[0]));
        int m = segments.length;
        long best = 0;
        // segments [i, j) lie entirely inside the window; fullCoins is their total
        int j = 0;
        long fullCoins = 0;

        for (int i = 0; i < m; i++) {
            if (j < i) {
                j = i;
                fullCoins = 0;
            }
            long windowEnd = segments[i][0] + k - 1;
            // absorb every segment that ends inside the window
            while (j < m && segments[j][1] <= windowEnd) {
                fullCoins += (segments[j][1] - segments[j][0] + 1) * segments[j][2];
                j++;
            }
            // segment j may hang over the right edge: take only its part inside the window
            long partialCoins = 0;
            if (j < m && segments[j][0] <= windowEnd) {
                partialCoins = (windowEnd - segments[j][0] + 1) * segments[j][2];
            }
            best = Math.max(best, fullCoins + partialCoins);

            // segment i leaves before the next window (anchored at l_{i+1}) is evaluated
            if (i < j) {
                fullCoins -= (segments[i][1] - segments[i][0] + 1) * segments[i][2];
            }
        }
        return best;
    }
}
