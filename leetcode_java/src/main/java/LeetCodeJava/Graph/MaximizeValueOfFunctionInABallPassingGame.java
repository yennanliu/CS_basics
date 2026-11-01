package LeetCodeJava.Graph;

// https://leetcode.com/problems/maximize-value-of-function-in-a-ball-passing-game/description/

import java.util.*;

/**
 * 2836. Maximize Value of Function in a Ball Passing Game
 * Hard
 *
 * You are given an integer array receiver of length n and an integer k. n players are playing a ball-passing game.
 *
 * You choose the starting player, i. The game proceeds as follows: player i passes the ball to player receiver[i], who then passes it to receiver[receiver[i]], and so on, for k passes in total. The game's score is the sum of the indices of the players who touched the ball, including repetitions, i.e. i + receiver[i] + receiver[receiver[i]] + ... + receiver^(k)[i].
 *
 * Return the maximum possible score.
 *
 * Notes:
 *
 * - receiver may contain duplicates.
 * - receiver[i] may be equal to i.
 *
 *
 * Example 1:
 *
 * Input: receiver = [2,0,1], k = 4
 * Output: 6
 * Explanation: Starting with player i = 2 the initial score is 2:
 * Pass 1: player 2 passes to player 1, score 3
 * Pass 2: player 1 passes to player 0, score 3
 * Pass 3: player 0 passes to player 2, score 5
 * Pass 4: player 2 passes to player 1, score 6
 *
 * Example 2:
 *
 * Input: receiver = [1,1,1,2,3], k = 3
 * Output: 10
 * Explanation: Starting with player i = 4 the initial score is 4:
 * Pass 1: player 4 passes to player 3, score 7
 * Pass 2: player 3 passes to player 2, score 9
 * Pass 3: player 2 passes to player 1, score 10
 *
 *
 * Constraints:
 *
 * 1 <= receiver.length == n <= 10^5
 * 0 <= receiver[i] <= n - 1
 * 1 <= k <= 10^10
 *
 */
public class MaximizeValueOfFunctionInABallPassingGame {

    // V0
    // IDEA: BINARY LIFTING ON THE FUNCTIONAL GRAPH
    //  - k can be 10^10, so precompute power-of-two jumps:
    //    jump[j][i]   = player reached after 2^j passes from i
    //    jumpSum[j][i] = sum of players touched on those passes, counting i but NOT the landing
    //  - jump[j][i] = jump[j-1][jump[j-1][i]], jumpSum[j][i] = jumpSum[j-1][i] + jumpSum[j-1][mid]
    //  - decompose k into set bits per start, then add the final landing player once
    /**
     * time = O(n log k)
     * space = O(n log k)
     */
    public long getMaxFunctionValue(List<Integer> receiver, long k) {
        int n = receiver.size();
        // number of bits needed to write k (at least 1)
        int levels = Math.max(1, 64 - Long.numberOfLeadingZeros(k));

        int[][] jump = new int[levels][n];
        long[][] jumpSum = new long[levels][n];
        for (int i = 0; i < n; i++) {
            jump[0][i] = receiver.get(i);
            jumpSum[0][i] = i;
        }
        for (int j = 1; j < levels; j++) {
            for (int i = 0; i < n; i++) {
                int mid = jump[j - 1][i];
                jump[j][i] = jump[j - 1][mid];
                jumpSum[j][i] = jumpSum[j - 1][i] + jumpSum[j - 1][mid];
            }
        }

        long best = 0;
        for (int start = 0; start < n; start++) {
            int player = start;
            long total = 0;
            for (int j = 0; j < levels; j++) {
                // bit j of k set -> take a 2^j jump
                if (((k >> j) & 1L) == 1L) {
                    total += jumpSum[j][player];
                    player = jump[j][player];
                }
            }
            // the landing player has not been counted yet
            best = Math.max(best, total + player);
        }
        return best;
    }
}
