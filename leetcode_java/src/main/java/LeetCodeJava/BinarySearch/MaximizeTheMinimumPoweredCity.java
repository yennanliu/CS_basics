package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximize-the-minimum-powered-city/description/

/**
 * 2528. Maximize the Minimum Powered City
 * Hard
 *
 * You are given a 0-indexed integer array stations of length n, where stations[i]
 * represents the number of power stations in the ith city.
 *
 * Each power station can provide power to every city in a fixed range. In other
 * words, if the range is denoted by r, then a power station at city i can provide
 * power to all cities j such that |i - j| <= r and 0 <= i, j <= n - 1.
 *
 * Note that |x| denotes absolute value. For example, |7 - 5| = 2 and |3 - 10| = 7.
 *
 * The power of a city is the total number of power stations it is being provided
 * power from.
 *
 * The government has sanctioned building k more power stations, each of which can
 * be built in any city, and have the same range as the pre-existing ones.
 *
 * Given the two integers r and k, return the maximum possible minimum power of a
 * city, if the additional power stations are built optimally.
 *
 * Note that you can build the k power stations in multiple cities.
 *
 *
 * Example 1:
 *
 * Input: stations = [1,2,4,5,0], r = 1, k = 2
 * Output: 5
 * Explanation:
 * One of the optimal ways is to install both the power stations at city 1.
 * So stations will become [1,4,4,5,0].
 * - City 0 is provided by 1 + 4 = 5 power stations.
 * - City 1 is provided by 1 + 4 + 4 = 9 power stations.
 * - City 2 is provided by 4 + 4 + 5 = 13 power stations.
 * - City 3 is provided by 5 + 4 = 9 power stations.
 * - City 4 is provided by 5 + 0 = 5 power stations.
 * So the minimum power of a city is 5.
 * Since it is not possible to obtain a larger power, we return 5.
 *
 * Example 2:
 *
 * Input: stations = [4,4,4,4], r = 0, k = 3
 * Output: 4
 * Explanation:
 * It can be proved that we cannot make the minimum power of a city greater than 4.
 *
 *
 * Constraints:
 *
 * n == stations.length
 * 1 <= n <= 10^5
 * 0 <= stations[i] <= 10^5
 * 0 <= r <= n - 1
 * 0 <= k <= 10^9
 *
 */
public class MaximizeTheMinimumPoweredCity {

    // V0
    // IDEA: BINARY SEARCH ON ANSWER + DIFFERENCE ARRAY + GREEDY LEFT-TO-RIGHT SWEEP
    // if a floor x is achievable, every smaller floor is too -> binary search the largest x.
    // check(x): sweep cities left to right; when city i is short by `need`, it can only be
    // helped by builds in [i-r, i+r], and everything left of i is settled, so build `need`
    // stations at min(i + r, n - 1) - the rightmost city that still covers i - which pushes
    // the new coverage window as far right as possible.
    /**
     * time = O(N * log(sum(stations) + k))
     * space = O(N)
     */
    public long maxPower(int[] stations, int r, int k) {
        int n = stations.length;

        // basePower[i] = power of city i before any new build (difference array + prefix sum)
        long[] diff = new long[n + 1];
        long totalStations = 0;
        for (int i = 0; i < n; i++) {
            totalStations += stations[i];
            int from = Math.max(0, i - r);
            int to = Math.min(i + r, n - 1);
            diff[from] += stations[i];
            diff[to + 1] -= stations[i];
        }
        long[] basePower = new long[n];
        long running = 0;
        for (int i = 0; i < n; i++) {
            running += diff[i];
            basePower[i] = running;
        }

        // answers exceed 32 bits: up to sum(stations) + k
        long left = 0;
        long right = totalStations + k;
        while (left < right) {
            long mid = left + (right - left + 1) / 2;
            if (canReach(basePower, r, k, mid)) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    /** Whether every city can reach power >= floor using at most k new stations. */
    private boolean canReach(long[] basePower, int r, long k, long floor) {
        int n = basePower.length;
        // added[] is a difference array of the power the new stations contribute
        long[] added = new long[n + 1];
        long budget = k;
        long addedHere = 0;
        for (int i = 0; i < n; i++) {
            addedHere += added[i];
            long need = floor - (basePower[i] + addedHere);
            if (need > 0) {
                if (need > budget) {
                    return false;
                }
                budget -= need;
                // build at the rightmost city that still covers city i
                int buildAt = Math.min(i + r, n - 1);
                int from = Math.max(0, buildAt - r);
                int to = Math.min(buildAt + r, n - 1);
                added[from] += need;
                added[to + 1] -= need;
                // the new window already covers city i itself (from <= i)
                addedHere += need;
            }
        }
        return true;
    }
}
