package LeetCodeJava.Sort;

// https://leetcode.com/problems/coordinate-with-maximum-network-quality/

/**
 *  1620. Coordinate With Maximum Network Quality
 *  Medium
 *
 *  You are given an array of network towers towers, where towers[i] = [xi, yi, qi]
 *  denotes the ith network tower with location (xi, yi) and quality factor qi. All
 *  the coordinates are integral coordinates on the X-Y plane, and the distance
 *  between two coordinates is the Euclidean distance.
 *
 *  You are also given an integer radius where a tower is reachable if the distance
 *  is less than or equal to radius. Outside that distance the tower is not reachable.
 *
 *  The signal quality of the ith tower at a coordinate (x, y) is
 *  floor(qi / (1 + d)), where d is the distance between the tower and the
 *  coordinate. The network quality at a coordinate is the sum of the signal
 *  qualities from all the reachable towers.
 *
 *  Return the array [cx, cy] representing the integral coordinate where the network
 *  quality is maximum. If there are multiple such coordinates, return the
 *  lexicographically minimum non-negative coordinate.
 *
 *  Example 1:
 *    Input: towers = [[1,2,5],[2,1,7],[3,1,9]], radius = 2
 *    Output: [2,1]
 *    Explanation: at (2, 1) the total quality is 7 + 2 + 4 = 13, the maximum.
 *
 *  Example 2:
 *    Input: towers = [[23,11,21]], radius = 9
 *    Output: [23,11]
 *
 *  Constraints:
 *    1 <= towers.length <= 50
 *    towers[i].length == 3
 *    0 <= xi, yi, qi <= 50
 *    1 <= radius <= 50
 */
public class CoordinateWithMaximumNetworkQuality {

    // V0
    // IDEA: BRUTE FORCE OVER THE BOUNDED 51 x 51 INTEGRAL GRID
    //       every tower sits in [0,50]^2 and quality only decreases with
    //       distance, so an optimal coordinate never needs to leave [0,50]^2 -
    //       moving away from the tower cloud strictly lowers every term.
    //       scan x = 0..50 then y = 0..50 and keep the FIRST strictly better
    //       score: iterating in increasing (x, y) makes the lexicographically
    //       smallest tie-break automatic.
    //       compare distance with radius by squaring (d2 <= r*r) to avoid float
    //       error at the boundary.
    /**
     * time = O(51 * 51 * m), m = towers.length
     * space = O(1)
     */
    public int[] bestCoordinate(int[][] towers, int radius) {
        int best = 0;
        int[] res = new int[]{0, 0};
        int rr = radius * radius;

        for (int x = 0; x <= 50; x++) {
            for (int y = 0; y <= 50; y++) {
                int total = 0;
                for (int[] t : towers) {
                    int dx = t[0] - x;
                    int dy = t[1] - y;
                    int d2 = dx * dx + dy * dy;
                    if (d2 <= rr) {
                        total += (int) (t[2] / (1 + Math.sqrt(d2)));
                    }
                }
                if (total > best) {
                    best = total;
                    res = new int[]{x, y};
                }
            }
        }
        return res;
    }
}
