package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-all-possible-routes/description/

/**
 * 1575. Count All Possible Routes
 * Hard
 *
 * You are given an array of distinct positive integers locations where locations[i] represents the position of city i. You are also given integers start, finish and fuel representing the starting city, ending city, and the initial amount of fuel you have, respectively.
 *
 * At each step, if you are at city i, you can pick any city j such that j != i and 0 <= j < locations.length and move to city j. Moving from city i to city j reduces the amount of fuel you have by |locations[i] - locations[j]|. Please notice that |x| denotes the absolute value of x.
 *
 * Notice that fuel cannot become negative at any point in time, and that you are allowed to visit any city more than once (including start and finish).
 *
 * Return the count of all possible routes from start to finish. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: locations = [2,3,6,8,4], start = 1, finish = 3, fuel = 5
 * Output: 4
 * Explanation: The following are all possible routes, each uses 5 units of fuel:
 * 1 -> 3
 * 1 -> 2 -> 3
 * 1 -> 4 -> 3
 * 1 -> 4 -> 2 -> 3
 *
 * Example 2:
 *
 * Input: locations = [4,3,1], start = 1, finish = 0, fuel = 6
 * Output: 5
 * Explanation: The following are all possible routes:
 * 1 -> 0, used fuel = 1
 * 1 -> 2 -> 0, used fuel = 5
 * 1 -> 2 -> 1 -> 0, used fuel = 5
 * 1 -> 0 -> 1 -> 0, used fuel = 3
 * 1 -> 0 -> 1 -> 0 -> 1 -> 0, used fuel = 5
 *
 * Example 3:
 *
 * Input: locations = [5,2,1], start = 0, finish = 2, fuel = 3
 * Output: 0
 * Explanation: It is impossible to get from 0 to 2 using only 3 units of fuel since the shortest route needs 4 units of fuel.
 *
 * Constraints:
 *
 * 2 <= locations.length <= 100
 * 1 <= locations[i] <= 10^9
 * All integers in locations are distinct.
 * 0 <= start, finish < locations.length
 * 1 <= fuel <= 200
 *
 */
public class CountAllPossibleRoutes {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: BOTTOM-UP DP ON (FUEL LEFT, CITY)
    //       routes[f][i] = # of routes from city i to `finish` with f fuel left.
    //       being AT finish already counts as one route (and the route may keep going):
    //         routes[f][i] = (i == finish ? 1 : 0)
    //                      + sum over j != i with dist(i, j) <= f of routes[f - dist][j]
    //       dist > 0 always (locations are distinct), so row f only reads smaller rows.
    /**
     * time = O(F * N^2)
     * space = O(F * N)
     */
    public int countRoutes(int[] locations, int start, int finish, int fuel) {
        int n = locations.length;
        long[][] routes = new long[fuel + 1][n];

        for (int f = 0; f <= fuel; f++) {
            for (int i = 0; i < n; i++) {
                long count = (i == finish) ? 1 : 0;
                for (int j = 0; j < n; j++) {
                    if (j == i) {
                        continue;
                    }
                    int distance = Math.abs(locations[i] - locations[j]);
                    if (distance <= f) {
                        count += routes[f - distance][j];
                    }
                }
                routes[f][i] = count % MOD;
            }
        }
        return (int) routes[fuel][start];
    }
}
