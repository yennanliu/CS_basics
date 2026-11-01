package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/stone-game-iv/description/

/**
 * 1510. Stone Game IV
 * Hard
 *
 * Alice and Bob take turns playing a game, with Alice starting first.
 *
 * Initially, there are n stones in a pile. On each player's turn, that player makes a move consisting of removing any non-zero square number of stones in the pile.
 *
 * Also, if a player cannot make a move, he/she loses the game.
 *
 * Given a positive integer n, return true if and only if Alice wins the game otherwise return false, assuming both players play optimally.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: true
 * Explanation: Alice can remove 1 stone winning the game because Bob doesn't have any moves.
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: false
 * Explanation: Alice can only remove 1 stone, after that Bob removes the last one winning the game (2 -> 1 -> 0).
 *
 * Example 3:
 *
 * Input: n = 4
 * Output: true
 * Explanation: n is already a perfect square, Alice can win with one move, removing 4 stones (4 -> 0).
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 *
 */
public class StoneGameIV {

    // V0
    // IDEA: GAME DP (WIN / LOSE STATES)
    //       canWin[i] = true if the player to move with i stones left can force a win.
    //       a position is winning iff some square move j*j leads to a LOSING position
    //       for the opponent: canWin[i] = OR over j of !canWin[i - j*j].
    //       canWin[0] = false (no move left -> the player to move loses).
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public boolean winnerSquareGame(int n) {
        boolean[] canWin = new boolean[n + 1];
        for (int stones = 1; stones <= n; stones++) {
            for (int j = 1; j * j <= stones; j++) {
                // removing j*j stones leaves the opponent in a losing spot
                if (!canWin[stones - j * j]) {
                    canWin[stones] = true;
                    break;
                }
            }
        }
        return canWin[n];
    }
}
