package LeetCodeJava.Math;

// https://leetcode.com/problems/count-the-number-of-houses-at-a-certain-distance-ii/description/

/**
 * 3017. Count the Number of Houses at a Certain Distance II
 * Hard
 *
 * You are given three positive integers n, x, and y.
 *
 * In a city, there exist houses numbered 1 to n connected by n streets. There is a street connecting the house numbered i with the house numbered i + 1 for all 1 <= i <= n - 1 . An additional street connects the house numbered x with the house numbered y.
 *
 * For each k, such that 1 <= k <= n, you need to find the number of pairs of houses (house1, house2) such that the minimum number of streets that need to be traveled to reach house2 from house1 is k.
 *
 * Return a 1-indexed array result of length n where result[k] represents the total number of pairs of houses such that the minimum streets required to reach one house from the other is k.
 *
 * Note that x and y can be equal.
 *
 *
 * Example 1:
 *
 * Input: n = 3, x = 1, y = 3
 * Output: [6,0,0]
 *
 * Example 2:
 *
 * Input: n = 5, x = 2, y = 4
 * Output: [10,8,2,0,0]
 *
 * Example 3:
 *
 * Input: n = 4, x = 1, y = 1
 * Output: [6,4,2,0]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * 1 <= x, y <= n
 *
 */
public class CountTheNumberOfHousesAtACertainDistanceII {

    // V0
    // IDEA: THE GRAPH IS A CYCLE (x..y) WITH TWO TAILS (1..x-1 and y+1..n);
    //       every source's distance profile is a few ARITHMETIC RUNS, each added to a DIFFERENCE ARRAY in O(1)
    //       (the per-source BFS of LC 3015 is O(n^2) and too slow for n = 10^5)
    /**
     * time = O(N)
     * space = O(N)
     */
    public long[] countOfPairs(int n, int x, int y) {
        // make x the smaller endpoint
        if (x > y) {
            int swap = x;
            x = y;
            y = swap;
        }

        long[] result = new long[n];

        // the extra street duplicates (or loops on) an existing one: the city is a plain path,
        // where 2 * (n - k) ordered pairs sit at distance k
        if (y - x <= 1) {
            for (int k = 1; k <= n; k++) {
                result[k - 1] = 2L * (n - k);
            }
            return result;
        }

        // diff[k] - diff[k - 1] = change in count at distance k
        long[] diff = new long[n + 3];

        int cycleLength = y - x + 1;          // houses on the cycle
        int half = (cycleLength - 1) / 2;     // distances reached twice around the cycle
        boolean even = cycleLength % 2 == 0;  // one extra house exactly opposite
        int leftTail = x - 1;                 // houses 1 .. x-1
        int rightTail = n - y;                // houses y+1 .. n

        // ---- sources in the left tail ----
        for (int u = 1; u < x; u++) {
            int toAnchor = x - u;                                  // steps to house x
            addRange(diff, n, 1, u - 1, 1);                         // tail houses further left
            addRange(diff, n, 1, x - 1 - u, 1);                     // tail houses further right
            addRange(diff, n, toAnchor, toAnchor, 1);               // the anchor x itself
            addRange(diff, n, toAnchor + 1, toAnchor + half, 2);    // the rest of the cycle, both ways
            if (even) {
                addRange(diff, n, toAnchor + cycleLength / 2, toAnchor + cycleLength / 2, 1);
            }
            // through x, over the extra street to y (1 step), then into the right tail
            addRange(diff, n, toAnchor + 2, toAnchor + 1 + rightTail, 1);
        }

        // ---- sources in the right tail (mirror image) ----
        for (int u = y + 1; u <= n; u++) {
            int toAnchor = u - y;
            addRange(diff, n, 1, n - u, 1);
            addRange(diff, n, 1, u - y - 1, 1);
            addRange(diff, n, toAnchor, toAnchor, 1);
            addRange(diff, n, toAnchor + 1, toAnchor + half, 2);
            if (even) {
                addRange(diff, n, toAnchor + cycleLength / 2, toAnchor + cycleLength / 2, 1);
            }
            addRange(diff, n, toAnchor + 2, toAnchor + 1 + leftTail, 1);
        }

        // ---- sources on the cycle ----
        for (int u = x; u <= y; u++) {
            // other cycle houses
            addRange(diff, n, 1, half, 2);
            if (even) {
                addRange(diff, n, cycleLength / 2, cycleLength / 2, 1);
            }
            // shortest way around the cycle to each anchor, then out along its tail
            int toLeftAnchor = Math.min(u - x, cycleLength - (u - x));
            int toRightAnchor = Math.min(y - u, cycleLength - (y - u));
            addRange(diff, n, toLeftAnchor + 1, toLeftAnchor + leftTail, 1);
            addRange(diff, n, toRightAnchor + 1, toRightAnchor + rightTail, 1);
        }

        // prefix-sum the difference array into counts
        long running = 0;
        for (int k = 1; k <= n; k++) {
            running += diff[k];
            result[k - 1] = running;
        }
        return result;
    }

    /** Adds `count` to every distance in [low, high], clipped to [1, n]. */
    private void addRange(long[] diff, int n, int low, int high, int count) {
        low = Math.max(low, 1);
        high = Math.min(high, n);
        if (low > high) {
            return;
        }
        diff[low] += count;
        diff[high + 1] -= count;
    }
}
