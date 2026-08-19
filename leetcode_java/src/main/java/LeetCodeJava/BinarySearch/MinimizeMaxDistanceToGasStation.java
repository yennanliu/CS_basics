package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimize-max-distance-to-gas-station/

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
}
