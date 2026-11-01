package LeetCodeJava.Array;

// https://leetcode.com/problems/determine-the-winner-of-a-bowling-game/description/

/**
 * 2660. Determine the Winner of a Bowling Game
 * Easy
 *
 * You are given two 0-indexed integer arrays player1 and player2, representing the number of pins that player 1 and player 2 hit in a bowling game, respectively.
 *
 * The bowling game consists of n turns, and the number of pins in each turn is exactly 10.
 *
 * Assume a player hits xi pins in the ith turn. The value of the ith turn for the player is:
 *
 * 2xi if the player hits 10 pins in either (i - 1)th or (i - 2)th turn.
 * Otherwise, it is xi.
 *
 * The score of the player is the sum of the values of their n turns.
 *
 * Return
 *
 * 1 if the score of player 1 is more than the score of player 2,
 * 2 if the score of player 2 is more than the score of player 1, and
 * 0 in case of a draw.
 *
 *
 * Example 1:
 *
 * Input: player1 = [5,10,3,2], player2 = [6,5,7,3]
 * Output: 1
 * Explanation:
 * The score of player 1 is 5 + 10 + 2*3 + 2*2 = 25.
 * The score of player 2 is 6 + 5 + 7 + 3 = 21.
 *
 * Example 2:
 *
 * Input: player1 = [3,5,7,6], player2 = [8,10,10,2]
 * Output: 2
 * Explanation:
 * The score of player 1 is 3 + 5 + 7 + 6 = 21.
 * The score of player 2 is 8 + 10 + 2*10 + 2*2 = 42.
 *
 * Example 3:
 *
 * Input: player1 = [2,3], player2 = [4,1]
 * Output: 0
 * Explanation:
 * The score of player1 is 2 + 3 = 5.
 * The score of player2 is 4 + 1 = 5.
 *
 * Example 4:
 *
 * Input: player1 = [1,1,1,10,10,10,10], player2 = [10,10,10,10,1,1,1]
 * Output: 2
 * Explanation:
 * The score of player1 is 1 + 1 + 1 + 10 + 2*10 + 2*10 + 2*10 = 73.
 * The score of player2 is 10 + 2*10 + 2*10 + 2*10 + 2*1 + 2*1 + 1 = 75.
 *
 *
 * Constraints:
 *
 * n == player1.length == player2.length
 * 1 <= n <= 1000
 * 0 <= player1[i], player2[i] <= 10
 *
 */
public class DetermineTheWinnerOfABowlingGame {

    private static final int STRIKE = 10;

    // V0
    // IDEA: SIMULATION (score each player, then compare)
    //       turn i is doubled when either of the two previous turns hit 10 pins; the check
    //       looks at the ORIGINAL pin counts and the bonus is never stacked.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int isWinner(int[] player1, int[] player2) {
        int score1 = score(player1);
        int score2 = score(player2);
        if (score1 > score2) {
            return 1;
        }
        if (score2 > score1) {
            return 2;
        }
        return 0;
    }

    /** Total score of one player under the "double after a strike" rule. */
    private int score(int[] pins) {
        int total = 0;
        for (int i = 0; i < pins.length; i++) {
            // guard the indices: turn 0 has no previous turn, turn 1 only one
            boolean strikeOneBack = i >= 1 && pins[i - 1] == STRIKE;
            boolean strikeTwoBack = i >= 2 && pins[i - 2] == STRIKE;
            if (strikeOneBack || strikeTwoBack) {
                total += 2 * pins[i];
            } else {
                total += pins[i];
            }
        }
        return total;
    }
}
