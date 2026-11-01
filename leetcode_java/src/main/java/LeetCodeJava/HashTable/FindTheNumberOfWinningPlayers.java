package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-the-number-of-winning-players/description/

/**
 * 3238. Find the Number of Winning Players
 * Easy
 *
 * You are given an integer n representing the number of players in a game and a 2D array pick where pick[i] = [x_i, y_i] represents that the player x_i picked a ball of color y_i.
 *
 * Player i wins the game if they pick strictly more than i balls of the same color. In other words,
 *
 * Player 0 wins if they pick any ball.
 * Player 1 wins if they pick at least two balls of the same color.
 * ...
 * Player i wins if they pick at least i + 1 balls of the same color.
 *
 * Return the number of players who win the game.
 *
 * Note that multiple players can win the game.
 *
 *
 * Example 1:
 *
 * Input: n = 4, pick = [[0,0],[1,0],[1,0],[2,1],[2,1],[2,0]]
 * Output: 2
 * Explanation:
 * Player 0 and player 1 win the game, while players 2 and 3 do not win.
 *
 * Example 2:
 *
 * Input: n = 5, pick = [[1,1],[1,2],[1,3],[1,4]]
 * Output: 0
 * Explanation:
 * Player 1 and others do not win the game.
 *
 * Example 3:
 *
 * Input: n = 5, pick = [[1,1],[2,4],[2,4],[2,4]]
 * Output: 1
 * Explanation:
 * Player 2 wins the game by picking 3 balls with color 4.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10
 * 1 <= pick.length <= 100
 * pick[i].length == 2
 * 0 <= x_i <= n - 1
 * 0 <= y_i <= 10
 *
 */
public class FindTheNumberOfWinningPlayers {

    // V0
    // IDEA: TALLY (PLAYER, COLOR) PAIRS, THEN COMPARE EACH PLAYER'S BEST COLOR
    //   player i needs i + 1 balls of ONE color, so only the largest per-color
    //   count matters. with n <= 10 and colors <= 10 a small table is enough.
    /**
     * time = O(P + N * C)
     * space = O(N * C)
     */
    public int winningPlayerCount(int n, int[][] pick) {
        // 0 <= color <= 10
        int[][] count = new int[n][11];
        for (int[] p : pick) {
            count[p[0]][p[1]]++;
        }

        int winners = 0;
        for (int player = 0; player < n; player++) {
            for (int color = 0; color <= 10; color++) {
                // player i wins with strictly more than i balls of one color
                if (count[player][color] > player) {
                    winners++;
                    break;
                }
            }
        }
        return winners;
    }
}
