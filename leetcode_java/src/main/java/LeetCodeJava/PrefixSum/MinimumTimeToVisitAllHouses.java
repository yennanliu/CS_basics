package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/minimum-time-to-visit-all-houses/description/

/**
 * 3540. Minimum Time to Visit All Houses
 * Medium
 *
 * You are given two integer arrays forward and backward, both of size n. You are
 * also given another integer array queries.
 *
 * There are n houses arranged in a circle. The houses are connected via roads in a
 * special arrangement:
 *
 * For all 0 <= i <= n - 2, house i is connected to house i + 1 via a road with
 * length forward[i] meters. Additionally, house n - 1 is connected back to house 0
 * via a road with length forward[n - 1] meters, completing the circle.
 *
 * For all 1 <= i <= n - 1, house i is connected to house i - 1 via a road with
 * length backward[i] meters. Additionally, house 0 is connected back to house n - 1
 * via a road with length backward[0] meters, completing the circle.
 *
 * You can walk at a pace of one meter per second. Starting from house 0, find the
 * minimum time taken to visit each house in the order specified by queries.
 *
 * Return the minimum total time taken to visit the houses.
 *
 *
 * Example 1:
 *
 * Input: forward = [1,4,4], backward = [4,1,2], queries = [1,2,0,2]
 * Output: 12
 *
 * Example 2:
 *
 * Input: forward = [1,1,1,1], backward = [2,2,2,2], queries = [1,2,3,0]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * n == forward.length == backward.length
 * 1 <= forward[i], backward[i] <= 10^5
 * 1 <= queries.length <= 10^5
 * 0 <= queries[i] < n
 * queries[i] != queries[i + 1]
 * queries[0] is not 0.
 *
 */
public class MinimumTimeToVisitAllHouses {

    // V0
    // IDEA: EACH HOP IS INDEPENDENT -- TWO PREFIX SUMS AROUND THE CIRCLE
    //   the order is fixed, so the walk is a chain of independent hops; each hop
    //   is either all-forward or all-backward, and both are contiguous runs of
    //   road lengths (with a wrap-around) that prefix sums answer in O(1).
    /**
     * time = O(N + Q)
     * space = O(N)
     */
    public long minTotalTime(int[] forward, int[] backward, int[] queries) {
        int n = forward.length;

        // forwardPrefix[i] = forward[0] + ... + forward[i-1]: walking 0 -> i forward
        long[] forwardPrefix = new long[n + 1];
        // backwardPrefix[i] = backward[0] + ... + backward[i-1]
        long[] backwardPrefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            forwardPrefix[i + 1] = forwardPrefix[i] + forward[i];
            backwardPrefix[i + 1] = backwardPrefix[i] + backward[i];
        }

        long total = 0;
        int current = 0;
        for (int next : queries) {
            if (next == current) {
                continue;
            }
            long forwardCost;
            long backwardCost;
            if (next > current) {
                // forward: roads current .. next-1, no wrap
                forwardCost = forwardPrefix[next] - forwardPrefix[current];
                // backward from current to next wraps: roads backward[current..1], backward[0],
                // then backward[n-1 .. next+1]
                backwardCost = backwardPrefix[current + 1]
                        + (backwardPrefix[n] - backwardPrefix[next + 1]);
            } else {
                // forward wraps: roads current .. n-1, then 0 .. next-1
                forwardCost = forwardPrefix[n] - forwardPrefix[current] + forwardPrefix[next];
                // backward without wrap: road i is used to step from house i to i-1,
                // for i = current down to next+1
                backwardCost = backwardPrefix[current + 1] - backwardPrefix[next + 1];
            }
            total += Math.min(forwardCost, backwardCost);
            current = next;
        }
        return total;
    }
}
