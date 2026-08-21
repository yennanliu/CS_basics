package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimize-max-distance-to-gas-station/

import java.util.PriorityQueue;

/**
 *  774. Minimize Max Distance to Gas Station
 *  Hard
 *
 *  You are given an integer array stations that represents the positions of the
 *  gas stations on the x-axis. You are also given an integer k.
 *
 *  You should add k new gas stations. You can add the stations anywhere on the
 *  x-axis, and not necessarily on an integer position.
 *
 *  Let penalty() be the maximum distance between adjacent gas stations after
 *  adding the k new stations.
 *
 *  Return the smallest possible value of penalty().
 *  Answers within 10^-6 of the actual answer will be accepted.
 *
 *  Example 1:
 *
 *  Input: stations = [1,2,3,4,5,6,7,8,9,10], k = 9
 *  Output: 0.50000
 *
 *  Example 2:
 *
 *  Input: stations = [23,24,36,39,46,56,57,65,84,98], k = 1
 *  Output: 14.00000
 *
 *  Constraints:
 *
 *  10 <= stations.length <= 2000
 *  0 <= stations[i] <= 10^8
 *  stations is sorted in a strictly increasing order.
 *  1 <= k <= 10^6
 */
public class MinimizeMaxDistanceToGasStation {

    // V0
    // IDEA: binary search on the (real valued) answer `d`; for a candidate d the
    //       gap g needs ceil(g / d) - 1 extra stations -> feasible if sum <= k
    /**
     * time = O(n * log(W / eps))   // W = stations[n-1] - stations[0]
     * space = O(1)
     */
    public double minmaxGasDist(int[] stations, int k) {
        double l = 0.0;
        double r = 0.0;
        for (int i = 1; i < stations.length; i++) {
            r = Math.max(r, stations[i] - stations[i - 1]);
        }
        // NOTE !!! float binary search -> loop till the window is under precision
        while (r - l > 1e-7) {
            double mid = l + (r - l) / 2.0;
            if (canAchieve(stations, k, mid)) {
                r = mid; // we minimize, so keep going left
            } else {
                l = mid;
            }
        }
        return r;
    }

    // can we make every adjacent distance <= d by adding at most k stations ?
    private boolean canAchieve(int[] stations, int k, double d) {
        long cnt = 0;
        for (int i = 1; i < stations.length; i++) {
            double gap = stations[i] - stations[i - 1];
            cnt += (long) (gap / d); // == ceil(gap/d) - 1 for non exact division
            if (cnt > k) {
                return false;
            }
        }
        return cnt <= k;
    }

    // V1
    // IDEA: greedy with a max-heap - repeatedly give the next station to the gap
    //       whose current sub-distance (gap / parts) is the largest
    /**
     * time = O(n + k log n)
     * space = O(n)
     */
    public double minmaxGasDist_1(int[] stations, int k) {
        int n = stations.length;
        // entry = {gap length, how many equal parts it is split into}
        PriorityQueue<double[]> pq = new PriorityQueue<>(
                (a, b) -> Double.compare(b[0] / b[1], a[0] / a[1]));
        for (int i = 1; i < n; i++) {
            pq.add(new double[] { stations[i] - stations[i - 1], 1.0 });
        }
        if (pq.isEmpty()) {
            return 0.0;
        }
        for (int i = 0; i < k; i++) {
            double[] top = pq.poll();
            top[1] += 1.0; // one more station inside this gap -> one more part
            pq.add(top);
        }
        double[] top = pq.peek();
        return top[0] / top[1];
    }

    // V2
    // IDEA: brute force - same greedy as V1 but WITHOUT a heap, the widest
    //       current sub-distance is found by a linear scan every round.
    //       Kept as a readable correctness reference (too slow for large k).
    /**
     * time = O(n * k)
     * space = O(n)
     */
    public double minmaxGasDist_2(int[] stations, int k) {
        int n = stations.length;
        if (n < 2) {
            return 0.0;
        }
        double[] gaps = new double[n - 1];
        int[] parts = new int[n - 1];
        for (int i = 1; i < n; i++) {
            gaps[i - 1] = stations[i] - stations[i - 1];
            parts[i - 1] = 1;
        }
        for (int step = 0; step < k; step++) {
            int bestIdx = 0;
            double bestVal = -1.0;
            for (int i = 0; i < gaps.length; i++) {
                double cur = gaps[i] / parts[i];
                if (cur > bestVal) {
                    bestVal = cur;
                    bestIdx = i;
                }
            }
            parts[bestIdx]++;
        }
        double res = 0.0;
        for (int i = 0; i < gaps.length; i++) {
            res = Math.max(res, gaps[i] / parts[i]);
        }
        return res;
    }
}
