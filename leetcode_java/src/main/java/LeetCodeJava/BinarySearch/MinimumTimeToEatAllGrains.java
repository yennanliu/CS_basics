package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-time-to-eat-all-grains/description/

import java.util.*;

/**
 * 2604. Minimum Time to Eat All Grains
 * Hard
 *
 * There are n hens and m grains on a line. You are given the initial positions of the hens and the grains in two integer arrays hens and grains of size n and m respectively.
 *
 * Any hen can eat a grain if they are on the same position. The time taken for this is negligible. One hen can also eat multiple grains.
 *
 * In 1 second, a hen can move right or left by 1 unit. The hens can move simultaneously and independently of each other.
 *
 * Return the minimum time to eat all grains if the hens act optimally.
 *
 *
 * Example 1:
 *
 * Input: hens = [3,6,7], grains = [2,4,7,9]
 * Output: 2
 * Explanation:
 * One of the ways hens eat all grains in 2 seconds is described below:
 * - The first hen eats the grain at position 2 in 1 second.
 * - The second hen eats the grain at position 4 in 2 seconds.
 * - The third hen eats the grains at positions 7 and 9 in 2 seconds.
 * So, the maximum time needed is 2.
 * It can be proven that the hens cannot eat all grains before 2 seconds.
 *
 * Example 2:
 *
 * Input: hens = [4,6,109,111,213,215], grains = [5,110,214]
 * Output: 1
 * Explanation:
 * One of the ways hens eat all grains in 1 second is described below:
 * - The first hen eats the grain at position 5 in 1 second.
 * - The fourth hen eats the grain at position 110 in 1 second.
 * - The sixth hen eats the grain at position 214 in 1 second.
 * - The other hens do not move.
 * So, the maximum time needed is 1.
 *
 *
 * Constraints:
 *
 * 1 <= hens.length, grains.length <= 2*10^4
 * 0 <= hens[i], grains[j] <= 10^9
 *
 */
public class MinimumTimeToEatAllGrains {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER + GREEDY FEASIBILITY SWEEP
    //       "can every grain be eaten within t seconds?" is monotone in t -> binary search t.
    //       check(t): sort both; hens never need to cross, so each hen (left to right) takes a
    //       contiguous block of the leftmost uneaten grains.
    //         - first grain y left of hen x: walk left d = x - y (d > t -> impossible), then a grain
    //           z right of x costs min(d, z - x) + (z - y)  (left-then-right vs right-then-left).
    //         - first grain right of x: the hen only walks right, grain z costs z - x.
    /**
     * time = O((N + M) * log U + N log N + M log M)   (U = the coordinate range)
     * space = O(N + M)
     */
    public int minimumTime(int[] hens, int[] grains) {
        int[] sortedHens = Arrays.copyOf(hens, hens.length);
        int[] sortedGrains = Arrays.copyOf(grains, grains.length);
        Arrays.sort(sortedHens);
        Arrays.sort(sortedGrains);

        // one hen can always do everything: reach the first grain, then sweep to the last
        long low = 0;
        long high = Math.abs((long) sortedHens[0] - sortedGrains[0])
                + ((long) sortedGrains[sortedGrains.length - 1] - sortedGrains[0]);

        // find the smallest t for which every grain can be eaten
        while (low < high) {
            long mid = low + (high - low) / 2;
            if (canEatAll(sortedHens, sortedGrains, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return (int) low;
    }

    /** True if the sorted hens can eat every sorted grain within `time` seconds. */
    private boolean canEatAll(int[] hens, int[] grains, long time) {
        int m = grains.length;
        int next = 0; // leftmost grain no hen has taken yet

        for (int hen : hens) {
            if (next == m) {
                return true;
            }
            long firstGrain = grains[next];

            if (firstGrain <= hen) {
                // this hen must walk left to the first grain; later hens are even further right
                long leftDistance = hen - firstGrain;
                if (leftDistance > time) {
                    return false;
                }
                // grains between firstGrain and the hen are eaten on the way
                while (next < m && grains[next] <= hen) {
                    next++;
                }
                // grains to the right: go whichever way round is cheaper
                while (next < m) {
                    long rightDistance = grains[next] - (long) hen;
                    long cost = Math.min(leftDistance, rightDistance) + (grains[next] - firstGrain);
                    if (cost > time) {
                        break;
                    }
                    next++;
                }
            } else {
                // every remaining grain is to the right: walk right as far as time allows
                while (next < m && grains[next] - (long) hen <= time) {
                    next++;
                }
            }
        }
        return next == m;
    }
}
