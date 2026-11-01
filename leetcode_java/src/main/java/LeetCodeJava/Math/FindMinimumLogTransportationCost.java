package LeetCodeJava.Math;

// https://leetcode.com/problems/find-minimum-log-transportation-cost/description/

/**
 * 3560. Find Minimum Log Transportation Cost
 * Easy
 *
 * You are given integers n, m, and k.
 *
 * There are two logs of lengths n and m units, which need to be transported in
 * three trucks where each truck can carry one log with length at most k units.
 *
 * You may cut the logs into smaller pieces, where the cost of cutting a log of
 * length x into logs of length len1 and len2 is cost = len1 * len2 such that len1
 * + len2 = x.
 *
 * Return the minimum total cost to distribute the logs onto the trucks. If the
 * logs don't need to be cut, the total cost is 0.
 *
 * Example 1:
 *
 * Input: n = 6, m = 5, k = 5
 *
 * Output: 5
 *
 * Explanation:
 *
 * Cut the log with length 6 into logs with length 1 and 5, at a cost equal to 1 *
 * 5 == 5. Now the three logs of length 1, 5, and 5 can fit in one truck each.
 *
 * Example 2:
 *
 * Input: n = 4, m = 4, k = 6
 *
 * Output: 0
 *
 * Explanation:
 *
 * The two logs can fit in the trucks already, hence we don't need to cut the logs.
 *
 * Constraints:
 *
 * 2 <= k <= 10^5
 *
 * 1 <= n, m <= 2 * k
 *
 * The input is generated such that it is always possible to transport the logs.
 *
 */
public class FindMinimumLogTransportationCost {

    // V0
    // IDEA: AT MOST ONE CUT, AND IT IS FORCED
    //       n, m <= 2k and there are three trucks, so at most one log is too long and one cut
    //       (two pieces) is enough. for the long log of length x both pieces must be <= k, so a
    //       piece p lies in [x - k, k]. cost p * (x - p) is a downward parabola peaking at x/2,
    //       so on that interval it is smallest at an endpoint: k * (x - k).
    /**
     * time = O(1)
     * space = O(1)
     */
    public long minCuttingCost(int n, int m, int k) {
        int longest = Math.max(n, m);
        if (longest <= k) {
            // both logs already fit, one per truck
            return 0;
        }
        return (long) k * (longest - k);
    }
}
