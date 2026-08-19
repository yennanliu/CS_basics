package LeetCodeJava.HashTable;

// https://leetcode.com/problems/longest-common-subpath/

import java.util.HashSet;
import java.util.Set;

/**
 *  1923. Longest Common Subpath
 *  Hard
 *
 *  There is a country of n cities numbered from 0 to n - 1. In this country, there
 *  is a road connecting every pair of cities.
 *
 *  There are m friends numbered from 0 to m - 1 who are traveling through the
 *  country. Each one of them will take a path consisting of some cities. Each path
 *  is represented by an integer array that contains the visited cities in order.
 *  The path may contain a city more than once, but the same city will not be listed
 *  consecutively.
 *
 *  Given an integer n and a 2D integer array paths where paths[i] is an integer
 *  array representing the path of the ith friend, return the length of the longest
 *  common subpath that is shared by every friend's path, or 0 if there is no common
 *  subpath at all.
 *
 *  A subpath of a path is a contiguous sequence of cities within that path.
 *
 *  Example 1:
 *  Input: n = 5, paths = [[0,1,2,3,4],[2,3,4],[4,0,1,2,3]]
 *  Output: 2   (the longest common subpath is [2,3])
 *
 *  Example 2:
 *  Input: n = 3, paths = [[0],[1],[2]]
 *  Output: 0
 *
 *  Example 3:
 *  Input: n = 5, paths = [[0,1,2,3,4],[4,3,2,1,0]]
 *  Output: 1
 *
 *  Constraints:
 *  1 <= n <= 10^5
 *  m == paths.length
 *  2 <= m <= 10^5
 *  sum(paths[i].length) <= 10^5
 *  0 <= paths[i][j] < n
 */
public class LongestCommonSubpath {

    // double hashing to make collisions negligible
    private static final long MOD_1 = 1000000007L;
    private static final long MOD_2 = 998244353L;
    private static final long BASE_1 = 100003L;
    private static final long BASE_2 = 131071L;

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER LENGTH + ROLLING HASH (Rabin-Karp).
    //       "there is a common subpath of length L" is monotonic in L, so binary
    //       search L, and for a fixed L intersect the hash sets of all windows.
    /**
     * time = O(N * log(minLen)), N = sum of all path lengths
     * space = O(N)
     */
    public int longestCommonSubpath(int n, int[][] paths) {

        // edge
        if (paths == null || paths.length == 0) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;
        for (int[] p : paths) {
            if (p == null || p.length == 0) {
                return 0;
            }
            minLen = Math.min(minLen, p.length);
        }

        int lo = 0;
        int hi = minLen;
        while (lo < hi) {
            int mid = lo + (hi - lo + 1) / 2;
            if (hasCommon(paths, mid)) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }

        return lo;
    }

    /** true if every path shares at least one window of length len */
    private boolean hasCommon(int[][] paths, int len) {

        long pow1 = power(BASE_1, len, MOD_1);
        long pow2 = power(BASE_2, len, MOD_2);

        Set<Long> common = null;

        for (int[] path : paths) {
            Set<Long> cur = new HashSet<>();
            long h1 = 0;
            long h2 = 0;
            for (int j = 0; j < path.length; j++) {
                long v = path[j] + 1L; // avoid the value 0 killing the hash
                h1 = (h1 * BASE_1 + v) % MOD_1;
                h2 = (h2 * BASE_2 + v) % MOD_2;
                if (j >= len) {
                    long out = path[j - len] + 1L;
                    h1 = ((h1 - out * pow1) % MOD_1 + MOD_1) % MOD_1;
                    h2 = ((h2 - out * pow2) % MOD_2 + MOD_2) % MOD_2;
                }
                if (j >= len - 1) {
                    cur.add(h1 * MOD_2 + h2);
                }
            }

            if (common == null) {
                common = cur;
            } else {
                common.retainAll(cur);
            }
            if (common.isEmpty()) {
                return false;
            }
        }

        return common != null && !common.isEmpty();
    }

    private long power(long base, long exp, long mod) {
        long res = 1;
        long b = base % mod;
        while (exp > 0) {
            if ((exp & 1L) == 1L) {
                res = res * b % mod;
            }
            b = b * b % mod;
            exp >>= 1;
        }
        return res;
    }
}
