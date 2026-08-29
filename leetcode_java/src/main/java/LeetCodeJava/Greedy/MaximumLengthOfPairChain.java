package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-length-of-pair-chain/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  646. Maximum Length of Pair Chain
 *  Medium
 *
 *  You are given an array of n pairs where pairs[i] = [lefti, righti] and lefti < righti.
 *
 *  A pair p2 = [c, d] follows a pair p1 = [a, b] if b < c.
 *  A chain of pairs can be formed in this fashion.
 *
 *  Return the length of the longest chain which can be formed.
 *  You do not need to use up all the given intervals. You can select pairs in any order.
 *
 *  Example 1:
 *    Input: pairs = [[1,2],[2,3],[3,4]]
 *    Output: 2    ([1,2] -> [3,4])
 *
 *  Example 2:
 *    Input: pairs = [[1,2],[7,8],[4,5]]
 *    Output: 3
 *
 *  Constraints:
 *    n == pairs.length
 *    1 <= n <= 1000
 *    -1000 <= lefti < righti <= 1000
 */
public class MaximumLengthOfPairChain {

    // V0
    // IDEA: classic activity selection - sort by end value, greedily take a pair
    //       whenever its start is strictly greater than the last chosen end.
    /**
     * time = O(n log n)
     * space = O(1) extra (sort aside)
     */
    public int findLongestChain(int[][] pairs) {
        Arrays.sort(pairs, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[1], b[1]);
            }
        });

        int count = 0;
        int curEnd = Integer.MIN_VALUE;
        for (int[] p : pairs) {
            if (p[0] > curEnd) {
                count++;
                curEnd = p[1];
            }
        }
        return count;
    }

    // V1
    // IDEA: LIS-style DP - sort by start, dp[i] = longest chain ending at pair i.
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int findLongestChain_1(int[][] pairs) {
        Arrays.sort(pairs, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });

        int n = pairs.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

        int res = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (pairs[j][1] < pairs[i][0]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            res = Math.max(res, dp[i]);
        }
        return res;
    }

    // V2
    // IDEA: PATIENCE / BINARY SEARCH DP (the O(n log n) LIS trick applied to V1's DP).
    //       Sort by start, keep tails[k] = the smallest achievable END of a chain of
    //       length k + 1 (tails is increasing), and binary search where each pair lands.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int findLongestChain_2(int[][] pairs) {
        Arrays.sort(pairs, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });

        int[] tails = new int[pairs.length];
        int size = 0;

        for (int[] p : pairs) {
            // first chain length whose tail can NOT be followed by p (tails[idx] >= p[0])
            int lo = 0;
            int hi = size;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (tails[mid] < p[0]) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            if (lo == size) {
                tails[size++] = p[1];          // extends the longest chain so far
            } else if (p[1] < tails[lo]) {
                tails[lo] = p[1];              // same length, but a smaller (better) end
            }
        }
        return size;
    }
}
