package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/maximum-number-of-achievable-transfer-requests/description/

import java.util.*;

/**
 * 1601. Maximum Number of Achievable Transfer Requests
 * Hard
 *
 * We have n buildings numbered from 0 to n - 1. Each building has a number of employees. It's transfer
 * season, and some employees want to change the building they reside in.
 *
 * You are given an array requests where requests[i] = [fromi, toi] represents an employee's request to
 * transfer from building fromi to building toi.
 *
 * All buildings are full, so a list of requests is achievable only if for each building, the net change
 * in employee transfers is zero. This means the number of employees leaving is equal to the number of
 * employees moving in.
 *
 * Return the maximum number of achievable requests.
 *
 *
 * Example 1:
 *
 * Input: n = 5, requests = [[0,1],[1,0],[0,1],[1,2],[2,0],[3,4]]
 * Output: 5
 *
 * Example 2:
 *
 * Input: n = 3, requests = [[0,0],[1,2],[2,1]]
 * Output: 3
 *
 * Example 3:
 *
 * Input: n = 4, requests = [[0,3],[3,1],[1,2],[2,0]]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= n <= 20
 * 1 <= requests.length <= 16
 * requests[i].length == 2
 * 0 <= fromi, toi < n
 *
 */
public class MaximumNumberOfAchievableTransferRequests {

    // V0
    // IDEA: BITMASK ENUMERATION (requests.length <= 16 -> at most 2^16 subsets)
    // a subset of requests is achievable iff every building's net change is 0.
    // Enumerate every subset, skipping one whose size cannot beat the best so far.
    /**
     * time = O(2^M * M), M = requests.length
     * space = O(N)
     */
    public int maximumRequests(int n, int[][] requests) {
        int m = requests.length;
        int best = 0;
        for (int mask = 0; mask < (1 << m); mask++) {
            int chosen = Integer.bitCount(mask);
            // cannot improve the answer
            if (chosen <= best) {
                continue;
            }
            int[] net = new int[n];
            for (int i = 0; i < m; i++) {
                // request i is in this subset
                if ((mask & (1 << i)) != 0) {
                    net[requests[i][0]]--;
                    net[requests[i][1]]++;
                }
            }
            if (allZero(net)) {
                best = chosen;
            }
        }
        return best;
    }

    /** True when every building's net change is zero. */
    private boolean allZero(int[] net) {
        for (int value : net) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }

    // largest balanced subset size found so far by V1's search
    private int bestTaken;

    // V1
    // IDEA: BACKTRACKING (take / skip each request, check balance at the end)
    // same search space as V0, but the net array is updated incrementally on the way
    // down and undone on the way back, instead of being rebuilt per subset.
    /**
     * time = O(2^M * N)
     * space = O(N + M)
     */
    public int maximumRequests_1(int n, int[][] requests) {
        bestTaken = 0;
        backtrack(requests, 0, new int[n], 0);
        return bestTaken;
    }

    /** Decides request `index` onward, with `taken` requests already accepted. */
    private void backtrack(int[][] requests, int index, int[] net, int taken) {
        if (index == requests.length) {
            if (allZero(net)) {
                bestTaken = Math.max(bestTaken, taken);
            }
            return;
        }
        // prune: even taking every remaining request cannot beat the best
        if (taken + (requests.length - index) <= bestTaken) {
            return;
        }

        // take request `index`
        net[requests[index][0]]--;
        net[requests[index][1]]++;
        backtrack(requests, index + 1, net, taken + 1);
        net[requests[index][0]]++;
        net[requests[index][1]]--;

        // skip request `index`
        backtrack(requests, index + 1, net, taken);
    }
}
