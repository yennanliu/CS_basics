package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/number-of-squareful-arrays/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * 996. Number of Squareful Arrays
 * Hard
 *
 * An array is squareful if the sum of every pair of adjacent elements is a perfect square.
 *
 * Given an integer array nums, return the number of permutations of nums that are squareful.
 *
 * Two permutations perm1 and perm2 are different if there is some index i such that
 * perm1[i] != perm2[i].
 *
 * Example 1:
 *
 * Input: nums = [1,17,8]
 * Output: 2
 * Explanation: [1,8,17] and [17,8,1] are the valid permutations.
 *
 * Example 2:
 *
 * Input: nums = [2,2,2]
 * Output: 1
 *
 * Constraints:
 *
 * 1 <= nums.length <= 12
 * 0 <= nums[i] <= 10^9
 *
 */
public class NumberOfSquarefulArrays {

    // V0
    // IDEA: BACKTRACKING (permutations with duplicates) + PRUNING
    /**
     *  - n <= 12, so we enumerate permutations, but prune HARD: a candidate is
     *    only appended when (last_picked + candidate) is a PERFECT SQUARE.
     *
     *  - Duplicate handling (the classic `permutations II` trick):
     *      SORT first, then skip nums[i] if nums[i] == nums[i-1] and nums[i-1]
     *      is NOT currently used. That forces equal values to be consumed in
     *      left-to-right order, so each distinct permutation is counted ONCE.
     *
     *  - NOTE !!! nums[i] can be up to 10^9, so an adjacent SUM can reach 2 * 10^9,
     *    which OVERFLOWS int -> the sum must be computed as `long`.
     *
     *  time  = O(n!) worst case, far less in practice thanks to the square pruning
     *  space = O(n)
     */

    private int res;
    private boolean[] used;
    private int[] nums;
    private int lastPicked;
    private int pathLen;

    public int numSquarefulPerms(int[] nums) {
        Arrays.sort(nums);

        this.nums = nums;
        this.used = new boolean[nums.length];
        this.res = 0;
        this.pathLen = 0;
        this.lastPicked = 0;

        backtrack();
        return res;
    }

    private void backtrack() {
        int n = nums.length;

        if (pathLen == n) {
            res += 1;
            return;
        }

        for (int i = 0; i < n; i++) {
            if (used[i]) {
                continue;
            }

            /** NOTE !!!
             *
             *  skip duplicates at the SAME recursion depth.
             *  `!used[i - 1]` is what forces equal values into left-to-right order.
             */
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }

            // adjacent sum must be a perfect square (the real pruning power)
            if (pathLen > 0 && !isSquare((long) lastPicked + (long) nums[i])) {
                continue;
            }

            int prevPicked = lastPicked;

            used[i] = true;
            lastPicked = nums[i];
            pathLen += 1;

            backtrack();

            // backtrack
            pathLen -= 1;
            lastPicked = prevPicked;
            used[i] = false;
        }
    }

    /** EXACT integer square root test (no float rounding issues up to 2 * 10^9) */
    private boolean isSquare(long v) {
        if (v < 0) {
            return false;
        }
        long r = (long) Math.sqrt((double) v);
        // fix up any float drift in both directions
        while (r > 0 && r * r > v) {
            r -= 1;
        }
        while ((r + 1) * (r + 1) <= v) {
            r += 1;
        }
        return r * r == v;
    }


    // V1
    // IDEA: COUNT MAP BACKTRACKING (dedup by construction)
    /**
     *  Instead of sorting and using the `!used[i-1]` guard, recurse over the set of
     *  DISTINCT values with their remaining counts. Each distinct value is tried
     *  once per position, so duplicate permutations can never be generated.
     *
     *  Clearer than the sorted-array trick, and it does not need the input sorted.
     *
     *  time  = O(n!) worst case
     *  space = O(n)
     */
    public int numSquarefulPerms_1(int[] nums) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int v : nums) {
            cnt.merge(v, 1, Integer::sum);
        }
        int[] res = new int[1];
        for (Integer v : new ArrayList<>(cnt.keySet())) {
            cnt.merge(v, -1, Integer::sum);
            dfsCount(cnt, v, nums.length - 1, res);
            cnt.merge(v, 1, Integer::sum);
        }
        return res[0];
    }

    private void dfsCount(Map<Integer, Integer> cnt, int last, int remain, int[] res) {
        if (remain == 0) {
            res[0] += 1;
            return;
        }
        for (Map.Entry<Integer, Integer> e : cnt.entrySet()) {
            int v = e.getKey();
            if (e.getValue() == 0 || !isPerfectSquare((long) last + v)) {
                continue;
            }
            e.setValue(e.getValue() - 1);
            dfsCount(cnt, v, remain - 1, res);
            e.setValue(e.getValue() + 1);
        }
    }

    /** EXACT integer square test (no float drift up to 2 * 10^9) */
    private boolean isPerfectSquare(long v) {
        if (v < 0) {
            return false;
        }
        long r = (long) Math.sqrt((double) v);
        while (r > 0 && r * r > v) {
            r -= 1;
        }
        while ((r + 1) * (r + 1) <= v) {
            r += 1;
        }
        return r * r == v;
    }

    // V2
    // IDEA: BITMASK DP -- count HAMILTONIAN PATHS, then divide out the duplicates
    /**
     *  Build a graph where i - j is an edge iff nums[i] + nums[j] is a perfect
     *  square, and count Hamiltonian paths with
     *      dp[mask][last] = paths using exactly `mask`, ending at `last`
     *
     *  That counts INDEX permutations, so equal values are over-counted by
     *  prod(count[v]!) -- divide at the end.
     *
     *  NOTE !!! this is polynomial in 2^n rather than factorial, so it is the only
     *           version whose runtime does not depend on how much the square
     *           condition happens to prune.
     *
     *  time  = O(n^2 * 2^n)
     *  space = O(n * 2^n)
     */
    public int numSquarefulPerms_2(int[] nums) {
        int n = nums.length;

        boolean[][] edge = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    edge[i][j] = isPerfectSquare((long) nums[i] + nums[j]);
                }
            }
        }

        long[][] dp = new long[1 << n][n];
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = 1;
        }

        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if (dp[mask][last] == 0 || ((mask >> last) & 1) == 0) {
                    continue;
                }
                for (int nxt = 0; nxt < n; nxt++) {
                    if (((mask >> nxt) & 1) == 1 || !edge[last][nxt]) {
                        continue;
                    }
                    dp[mask | (1 << nxt)][nxt] += dp[mask][last];
                }
            }
        }

        long total = 0;
        for (int last = 0; last < n; last++) {
            total += dp[(1 << n) - 1][last];
        }

        // divide out the permutations of equal values
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int v : nums) {
            cnt.merge(v, 1, Integer::sum);
        }
        for (int c : cnt.values()) {
            for (int f = 2; f <= c; f++) {
                total /= f;
            }
        }

        return (int) total;
    }

    // V3
    // IDEA: PRECOMPUTED ADJACENCY LISTS + sorted-dedup backtracking
    /**
     *  Same search as V0, but the `is the adjacent sum square?` test is hoisted
     *  into an n x n adjacency list built once up front.
     *
     *  The square test then never runs inside the recursion, which is where V0
     *  spends most of its time on larger inputs.
     *
     *  time  = O(n^2 + n!) worst case, with a much smaller constant
     *  space = O(n^2)
     */
    public int numSquarefulPerms_3(int[] nums) {
        int n = nums.length;
        int[] arr = nums.clone();
        Arrays.sort(arr);

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                if (i != j && isPerfectSquare((long) arr[i] + arr[j])) {
                    adj.get(i).add(j);
                }
            }
        }

        boolean[] used = new boolean[n];
        int[] res = new int[1];
        for (int i = 0; i < n; i++) {
            if (i > 0 && arr[i] == arr[i - 1]) {
                continue;
            }
            used[i] = true;
            dfsAdj(arr, adj, used, i, 1, res);
            used[i] = false;
        }
        return res[0];
    }

    private void dfsAdj(int[] arr, List<List<Integer>> adj, boolean[] used,
                        int last, int depth, int[] res) {
        if (depth == arr.length) {
            res[0] += 1;
            return;
        }
        int prev = -1;
        for (int nxt : adj.get(last)) {
            if (used[nxt] || (prev != -1 && arr[nxt] == prev)) {
                continue;
            }
            prev = arr[nxt]; // skip equal values at the same depth
            used[nxt] = true;
            dfsAdj(arr, adj, used, nxt, depth + 1, res);
            used[nxt] = false;
        }
    }

}
