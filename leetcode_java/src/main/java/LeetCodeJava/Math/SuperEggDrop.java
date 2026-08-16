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

}
