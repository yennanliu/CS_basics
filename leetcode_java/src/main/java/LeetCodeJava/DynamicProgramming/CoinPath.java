package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/coin-path/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 656. Coin Path
 * Hard
 * Lock: Prime
 *
 * You are given an integer array coins (1-indexed) of length n and an integer maxJump.
 * You can jump to any index i of the array coins if coins[i] != -1 and you have to pay
 * coins[i] when you visit index i. In addition to that, if you are currently at index i,
 * you can only jump to any index i + k where i + k <= n and k is a value in the range
 * [1, maxJump].
 *
 * You are initially positioned at index 1 (coins[1]). You want to find the path that
 * reaches index n with the minimum cost.
 *
 * Return an integer array of the indices that you will visit in order so that you can
 * reach index n with the minimum cost. If there are multiple paths with the same cost,
 * return the lexicographically smallest such path. If it is not possible to reach index
 * n, return an empty array.
 *
 * A path p1 = [Pa1, Pa2, ..., Pax] of length x is lexicographically smaller than
 * p2 = [Pb1, Pb2, ..., Pbx] of length y, if and only if at the first j where Paj and Pbj
 * differ, Paj < Pbj; when no such j exists, then x < y.
 *
 * Example 1:
 *
 * Input: coins = [1,2,4,-1,2], maxJump = 2
 * Output: [1,3,5]
 *
 * Example 2:
 *
 * Input: coins = [1,2,4,-1,2], maxJump = 1
 * Output: []
 *
 * Constraints:
 *
 * 1 <= coins.length <= 1000
 * -1 <= coins[i] <= 100
 * coins[1] != -1
 * 1 <= maxJump <= 100
 *
 */
public class CoinPath {

    // V0
    // IDEA: BACKWARD DP + FORWARD GREEDY RECONSTRUCTION
    /**
     *  DP def (0-indexed internally):
     *    - f[i] = minimum cost to travel from index i to the LAST index (coins[i] included)
     *    - f[i] = INF if index i is blocked (coins[i] == -1) or the end is unreachable
     *
     *  DP eq:
     *    - f[n-1] = coins[n-1]
     *    - f[i]   = coins[i] + min(f[i+1] .. f[i+maxJump])
     *
     *  Filling f from RIGHT TO LEFT is required because f[i] depends on LARGER indices.
     *
     *  Reconstruction (LEXICOGRAPHICALLY SMALLEST):
     *    Walk i = 0, 1, 2, ... keeping `remain` = the cost still to be paid from the
     *    current position onward. Whenever f[i] == remain, index i lies on an OPTIMAL
     *    path -- and because we sweep LEFT TO RIGHT, it is the SMALLEST such index,
     *    which is exactly what `lexicographically smallest` asks for.
     *
     *    NOTE !!! such an index ALWAYS exists within maxJump of the previous one
     *             (that is what the min in the DP equation guarantees), so the
     *             reconstructed jumps are legal.
     *
     *  time  = O(n * maxJump)
     *  space = O(n)
     */
    public List<Integer> cheapestJump(int[] coins, int maxJump) {
        int n = coins.length;
        final long INF = Long.MAX_VALUE / 4;

        List<Integer> res = new ArrayList<>();

        // the DESTINATION itself must be steppable
        if (coins[n - 1] == -1) {
            return res;
        }

        long[] f = new long[n];
        Arrays.fill(f, INF);
        f[n - 1] = coins[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            if (coins[i] == -1) {
                continue;
            }
            for (int j = i + 1; j < Math.min(n, i + maxJump + 1); j++) {
                if (f[j] + coins[i] < f[i]) {
                    f[i] = f[j] + coins[i];
                }
            }
        }

        if (f[0] >= INF) {
            return res;
        }

        // greedily pick the EARLIEST index that is still on an optimal path
        long remain = f[0];
        for (int i = 0; i < n; i++) {
            if (f[i] == remain) {
                res.add(i + 1); // the answer is 1-INDEXED
                remain -= coins[i];
            }
        }
        return res;
    }

}
