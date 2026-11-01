package LeetCodeJava.Greedy;

// https://leetcode.com/problems/stone-game-vi/description/

import java.util.Arrays;

/**
 * 1686. Stone Game VI
 * Medium
 *
 * Alice and Bob take turns playing a game, with Alice starting first.
 *
 * There are n stones in a pile. On each player's turn, they can remove a stone from
 * the pile and receive points based on the stone's value. Alice and Bob may value
 * the stones differently.
 *
 * You are given two integer arrays of length n, aliceValues and bobValues. Each
 * aliceValues[i] and bobValues[i] represents how Alice and Bob, respectively, value
 * the ith stone.
 *
 * The winner is the person with the most points after all the stones are chosen.
 * If both players have the same amount of points, the game results in a draw. Both
 * players will play optimally. Both players know the other's values.
 *
 * Determine the result of the game, and:
 *
 * - If Alice wins, return 1.
 * - If Bob wins, return -1.
 * - If the game results in a draw, return 0.
 *
 *
 * Example 1:
 *
 * Input: aliceValues = [1,3], bobValues = [2,1]
 * Output: 1
 *
 * Example 2:
 *
 * Input: aliceValues = [1,2], bobValues = [3,1]
 * Output: 0
 *
 * Example 3:
 *
 * Input: aliceValues = [2,4,3], bobValues = [1,6,7]
 * Output: -1
 *
 *
 * Constraints:
 *
 * n == aliceValues.length == bobValues.length
 * 1 <= n <= 10^5
 * 1 <= aliceValues[i], bobValues[i] <= 100
 *
 */
public class StoneGameVI {

    // V0
    // IDEA: GREEDY, SORT BY aliceValues[i] + bobValues[i] DESCENDING
    //       taking stone i gains a[i] for the mover AND denies b[i] to the other,
    //       so its real worth is a[i] + b[i]. Both players take the largest
    //       remaining sum: Alice gets positions 0, 2, 4, ..., Bob 1, 3, 5, ...
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int stoneGameVI(int[] aliceValues, int[] bobValues) {
        int n = aliceValues.length;

        // stones[i] = {combined worth, index}
        int[][] stones = new int[n][2];
        for (int i = 0; i < n; i++) {
            stones[i][0] = aliceValues[i] + bobValues[i];
            stones[i][1] = i;
        }
        Arrays.sort(stones, (x, y) -> Integer.compare(y[0], x[0]));

        int aliceScore = 0;
        int bobScore = 0;
        for (int turn = 0; turn < n; turn++) {
            int stone = stones[turn][1];
            if (turn % 2 == 0) {
                aliceScore += aliceValues[stone];
            } else {
                bobScore += bobValues[stone];
            }
        }
        return Integer.compare(aliceScore, bobScore);
    }
}
