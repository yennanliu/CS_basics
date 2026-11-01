package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-wear-different-hats-to-each-other/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1434. Number of Ways to Wear Different Hats to Each Other
 * Hard
 *
 * There are n people and 40 types of hats labeled from 1 to 40.
 *
 * Given a 2D integer array hats, where hats[i] is a list of all hats preferred by
 * the ith person.
 *
 * Return the number of ways that n people can wear different hats from each other.
 *
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: hats = [[3,4],[4,5],[5]]
 * Output: 1
 * Explanation: There is only one way to choose hats given the conditions.
 * First person chooses hat 3, Second person chooses hat 4 and last one hat 5.
 *
 * Example 2:
 *
 * Input: hats = [[3,5,1],[3,5]]
 * Output: 4
 * Explanation: There are 4 ways to choose hats:
 * (3,5), (5,3), (1,3) and (1,5)
 *
 * Example 3:
 *
 * Input: hats = [[1,2,3,4],[1,2,3,4],[1,2,3,4],[1,2,3,4]]
 * Output: 24
 * Explanation: Each person can choose hats labeled from 1 to 4.
 * Number of Permutations of (1,2,3,4) = 24.
 *
 *
 * Constraints:
 *
 * n == hats.length
 * 1 <= n <= 10
 * 1 <= hats[i].length <= 40
 * 1 <= hats[i][j] <= 40
 * hats[i] contains a list of unique integers.
 *
 */
public class NumberOfWaysToWearDifferentHatsToEachOther {

    private static final int MOD = 1_000_000_007;
    private static final int MAX_HAT = 40;

    // V0
    // IDEA: BITMASK DP OVER PEOPLE, ITERATING HATS (not people)
    //       a person may like up to 40 hats (2^40 states, too many), but there are at most
    //       10 people -> 2^10 masks of "who already has a hat".
    //       dp[mask] = # of ways to serve exactly the people in mask with the hats seen so far.
    //       processing hat h: new[mask] = dp[mask]                       (hat h unused)
    //                                   + dp[mask without p], p likes h  (hat h -> person p)
    /**
     * time = O(40 * 2^N * N)
     * space = O(2^N)
     */
    public int numberWays(List<List<Integer>> hats) {
        int n = hats.size();
        int fullMask = (1 << n) - 1;

        // peopleWhoLike.get(h) = every person who likes hat h
        List<List<Integer>> peopleWhoLike = new ArrayList<>();
        for (int h = 0; h <= MAX_HAT; h++) {
            peopleWhoLike.add(new ArrayList<>());
        }
        for (int person = 0; person < n; person++) {
            for (int hat : hats.get(person)) {
                peopleWhoLike.get(hat).add(person);
            }
        }

        // base case: nobody served, using no hats -> one way
        long[] dp = new long[1 << n];
        dp[0] = 1;

        for (int hat = 1; hat <= MAX_HAT; hat++) {
            if (peopleWhoLike.get(hat).isEmpty()) {
                continue;
            }
            // copy first: "do not use this hat" is always an option, and the copy keeps
            // this hat from being handed out twice in the same round
            long[] next = dp.clone();
            for (int mask = 0; mask <= fullMask; mask++) {
                if (dp[mask] == 0) {
                    continue;
                }
                for (int person : peopleWhoLike.get(hat)) {
                    // bit `person` of mask is set when that person already wears a hat
                    boolean alreadyServed = ((mask >> person) & 1) == 1;
                    if (!alreadyServed) {
                        int withPerson = mask | (1 << person);
                        next[withPerson] = (next[withPerson] + dp[mask]) % MOD;
                    }
                }
            }
            dp = next;
        }

        return (int) dp[fullMask];
    }
}
