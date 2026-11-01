package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/fair-distribution-of-cookies/description/

import java.util.*;

/**
 * 2305. Fair Distribution of Cookies
 * Medium
 *
 * You are given an integer array cookies, where cookies[i] denotes the number of cookies in the ith bag. You are also given an integer k that denotes the number of children to distribute all the bags of cookies to. All the cookies in the same bag must go to the same child and cannot be split up.
 *
 * The unfairness of a distribution is defined as the maximum total cookies obtained by a single child in the distribution.
 *
 * Return the minimum unfairness of all distributions.
 *
 *
 * Example 1:
 *
 * Input: cookies = [8,15,10,20,8], k = 2
 * Output: 31
 * Explanation: One optimal distribution is [8,15,8] and [10,20]
 * - The 1st child receives [8,15,8] which has a total of 8 + 15 + 8 = 31 cookies.
 * - The 2nd child receives [10,20] which has a total of 10 + 20 = 30 cookies.
 * The unfairness of the distribution is max(31,30) = 31.
 * It can be shown that there is no distribution with an unfairness less than 31.
 *
 * Example 2:
 *
 * Input: cookies = [6,1,3,2,2,4,1,2], k = 3
 * Output: 7
 * Explanation: One optimal distribution is [6,1], [3,2,2], and [4,1,2]
 * - The 1st child receives [6,1] which has a total of 6 + 1 = 7 cookies.
 * - The 2nd child receives [3,2,2] which has a total of 3 + 2 + 2 = 7 cookies.
 * - The 3rd child receives [4,1,2] which has a total of 4 + 1 + 2 = 7 cookies.
 * The unfairness of the distribution is max(7,7,7) = 7.
 * It can be shown that there is no distribution with an unfairness less than 7.
 *
 *
 * Constraints:
 *
 * 2 <= cookies.length <= 8
 * 1 <= cookies[i] <= 10^5
 * 2 <= k <= cookies.length
 *
 */
public class FairDistributionOfCookies {

    // V0
    // IDEA: BITMASK DP — dp[mask] = MIN UNFAIRNESS GIVING THE COOKIE BAGS IN mask TO THE CHILDREN SO FAR
    /**
     * time = O(K * 3^N)
     * space = O(2^N)
     */
    public int distributeCookies(int[] cookies, int k) {
        int n = cookies.length;
        int size = 1 << n;
        int fullMask = size - 1;

        // bagTotal[mask] = total cookies in the bags of mask
        int[] bagTotal = new int[size];
        for (int mask = 1; mask < size; mask++) {
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    bagTotal[mask] += cookies[i];
                }
            }
        }

        // one child: they take everything in mask
        int[] dp = bagTotal.clone();

        // add the remaining children one at a time
        for (int child = 2; child <= k; child++) {
            int[] next = new int[size];
            Arrays.fill(next, Integer.MAX_VALUE);
            for (int mask = 0; mask < size; mask++) {
                // the new child takes subset `sub` of mask; the others share the rest
                // (sub - 1) & mask steps through every subset of mask in decreasing order
                int sub = mask;
                while (true) {
                    int unfairness = Math.max(bagTotal[sub], dp[mask ^ sub]);
                    next[mask] = Math.min(next[mask], unfairness);
                    if (sub == 0) {
                        break;
                    }
                    sub = (sub - 1) & mask;
                }
            }
            dp = next;
        }
        return dp[fullMask];
    }

    // V1
    // IDEA: BACKTRACKING — GIVE EACH BAG TO EACH CHILD, PRUNE WHEN A CHILD ALREADY EXCEEDS THE BEST
    /**
     * time = O(K^N)
     * space = O(K + N)
     */
    public int distributeCookies_1(int[] cookies, int k) {
        int[] childTotals = new int[k];
        int[] best = {Integer.MAX_VALUE};
        assignBag(cookies, 0, childTotals, best);
        return best[0];
    }

    /** Tries every child for bag index; records the smallest max total seen. */
    private void assignBag(int[] cookies, int index, int[] childTotals, int[] best) {
        if (index == cookies.length) {
            int unfairness = 0;
            for (int total : childTotals) {
                unfairness = Math.max(unfairness, total);
            }
            best[0] = Math.min(best[0], unfairness);
            return;
        }
        for (int child = 0; child < childTotals.length; child++) {
            // prune: this child would already be no better than the best answer
            if (childTotals[child] + cookies[index] >= best[0]) {
                continue;
            }
            childTotals[child] += cookies[index];
            assignBag(cookies, index + 1, childTotals, best);
            childTotals[child] -= cookies[index];
            // children with 0 cookies are interchangeable: trying one is enough
            if (childTotals[child] == 0) {
                break;
            }
        }
    }
}
