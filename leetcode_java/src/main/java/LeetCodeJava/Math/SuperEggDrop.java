package LeetCodeJava.Math;

// https://leetcode.com/problems/super-egg-drop/description/
/**
 * 887. Super Egg Drop
 * Hard
 *
 * You are given k identical eggs and you have access to a building with n floors
 * labeled from 1 to n.
 *
 * You know that there exists a floor f where 0 <= f <= n such that any egg dropped at
 * a floor higher than f will break, and any egg dropped at or below floor f will not
 * break.
 *
 * Each move, you may take an unbroken egg and drop it from any floor x (where
 * 1 <= x <= n). If the egg breaks, you can no longer use it. However, if the egg does
 * not break, you may reuse it in future moves.
 *
 * Return the minimum number of moves that you need to determine with certainty
 * what the value of f is.
 *
 *
 * Example 1:
 *
 * Input: k = 1, n = 2
 * Output: 2
 * Explanation:
 * Drop the egg from floor 1. If it breaks, we know that f = 0.
 * Otherwise, drop the egg from floor 2. If it breaks, we know that f = 1.
 * If it does not break, then we know f = 2.
 * Hence, we need at minimum 2 moves to determine with certainty what the value of f is.
 *
 * Example 2:
 *
 * Input: k = 2, n = 6
 * Output: 3
 *
 * Example 3:
 *
 * Input: k = 3, n = 14
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= k <= 100
 * 1 <= n <= 10^4
 *
 */
public class SuperEggDrop {

    // V0
    // IDEA: DP ON `MOVES` (flip the state around)
    /**
     *  Instead of asking `how many moves for n floors`, ask the INVERSE:
     *
     *  DP def:
     *     - f[t][j] = the MAXIMUM number of floors we can fully decide
     *                 using t moves and j eggs
     *
     *  DP eq:
     *     - f[t][j] = f[t-1][j-1]   (egg BREAKS   -> search below, one fewer egg)
     *               + f[t-1][j]     (egg SURVIVES -> search above, same eggs)
     *               + 1             (the floor we just dropped from)
     *
     *  Answer: the SMALLEST t with f[t][k] >= n
     *
     *  time  = O(k * moves), moves <= n
     *  space = O(k)
     */
    public int superEggDrop(int k, int n) {
        // f[j] = max floors solvable with the current number of moves and j eggs
        int[] f = new int[k + 1];
        int moves = 0;

        while (f[k] < n) {
            moves += 1;
            /** NOTE !!!
             *
             *  iterate eggs DOWNWARDS so f[j - 1] is still the value
             *  from the PREVIOUS move count (this is the 1D rolling-array trick)
             */
            for (int j = k; j > 0; j--) {
                f[j] = f[j] + f[j - 1] + 1;
            }
        }

        return moves;
    }

    // V0-1
    // IDEA: BOTTOM UP DP + BINARY SEARCH ON THE DROP FLOOR
    /**
     *   f[i][j] = min moves for i floors with j eggs.
     *   For a fixed i, f[mid-1][j-1] GROWS with mid while f[i-mid][j] SHRINKS,
     *   so the optimal drop floor can be found by BINARY SEARCH instead of a
     *   linear scan.
     *
     *   time  = O(n * k * log(n))
     *   space = O(n * k)
     */
    public int superEggDrop_0_1(int k, int n) {
        int[][] f = new int[n + 1][k + 1];

        // 1 egg -> we must scan floor by floor
        for (int i = 1; i <= n; i++) {
            f[i][1] = i;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 2; j <= k; j++) {
                int lo = 1;
                int hi = i;
                while (lo < hi) {
                    int mid = (lo + hi + 1) / 2;
                    int broke = f[mid - 1][j - 1];  // increasing in mid
                    int survived = f[i - mid][j];   // decreasing in mid
                    if (broke <= survived) {
                        lo = mid;
                    } else {
                        hi = mid - 1;
                    }
                }
                f[i][j] = Math.max(f[lo - 1][j - 1], f[i - lo][j]) + 1;
            }
        }

        return f[n][k];
    }


    // V1
    // IDEA: BINOMIAL COUNTING -- the answer is the smallest m with sum C(m, i) >= n
    /**
     *  With m moves and k eggs the number of distinguishable outcomes is
     *      C(m,1) + C(m,2) + ... + C(m,k)
     *  because a strategy is a decision tree of depth m with at most k `break`
     *  branches on any root-to-leaf path.
     *
     *  So increment m until that sum reaches n. The running sum updates in O(k) per
     *  step via C(m, i) = C(m-1, i) + C(m-1, i-1) -- the SAME recurrence as V0, but
     *  read as combinatorics rather than as a DP table.
     *
     *  time  = O(k * moves)
     *  space = O(k)
     */
    public int superEggDrop_1(int k, int n) {
        long[] binom = new long[k + 1]; // binom[i] = C(m, i) for the current m
        int m = 0;
        long total = 0;

        while (total < n) {
            m += 1;
            // update in place, descending, so binom[i-1] is still C(m-1, i-1)
            for (int i = k; i >= 1; i--) {
                binom[i] = binom[i] + binom[i - 1] + (i == 1 ? 1 : 0);
            }
            total = 0;
            for (int i = 1; i <= k; i++) {
                total += binom[i];
                if (total >= n) {
                    break;
                }
            }
        }
        return m;
    }

    // V2
    // IDEA: FULL 2D `MOVES x EGGS` TABLE (no rolling array)
    /**
     *  f[t][j] = the maximum floors solvable with t moves and j eggs, materialised
     *  in full rather than rolled into one row.
     *
     *  O(k * moves) memory instead of O(k), but the whole table is inspectable --
     *  which is how you would actually CHECK the recurrence
     *  f[t][j] = f[t-1][j-1] + f[t-1][j] + 1.
     *
     *  time  = O(k * moves)
     *  space = O(k * moves)
     */
    public int superEggDrop_2(int k, int n) {
        int maxMoves = n;   // never more than n moves (one floor at a time)
        int[][] f = new int[maxMoves + 1][k + 1];

        for (int t = 1; t <= maxMoves; t++) {
            for (int j = 1; j <= k; j++) {
                f[t][j] = f[t - 1][j - 1] + f[t - 1][j] + 1;
            }
            if (f[t][k] >= n) {
                return t;
            }
        }
        return maxMoves;
    }

    // V3
    // IDEA: TOP-DOWN MEMOISED RECURSION + binary search on the drop floor
    /**
     *  dfs(floors, eggs) = 1 + min over x of max(dfs(x-1, eggs-1), dfs(floors-x, eggs))
     *
     *  The inner max is the crossing point of an increasing and a decreasing
     *  function of x, so the optimum is found by binary search rather than by
     *  scanning every floor.
     *
     *  The recursive counterpart of V0-1 -- same recurrence, memo instead of a
     *  bottom-up sweep, so only the reachable states are ever computed.
     *
     *  time  = O(n * k * log n)
     *  space = O(n * k)
     */
    private Integer[][] memoEgg;

    public int superEggDrop_3(int k, int n) {
        memoEgg = new Integer[n + 1][k + 1];
        return dfsEgg(n, k);
    }

    private int dfsEgg(int floors, int eggs) {
        if (floors == 0) {
            return 0;
        }
        if (eggs == 1) {
            return floors;   // one egg -> linear scan
        }
        if (memoEgg[floors][eggs] != null) {
            return memoEgg[floors][eggs];
        }

        int lo = 1;
        int hi = floors;
        while (lo < hi) {
            int mid = lo + (hi - lo + 1) / 2;
            int broke = dfsEgg(mid - 1, eggs - 1);   // increasing in mid
            int survived = dfsEgg(floors - mid, eggs); // decreasing in mid
            if (broke <= survived) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }

        int res = Math.max(dfsEgg(lo - 1, eggs - 1), dfsEgg(floors - lo, eggs)) + 1;
        memoEgg[floors][eggs] = res;
        return res;
    }

}
