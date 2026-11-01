package LeetCodeJava.Math;

// https://leetcode.com/problems/stone-removal-game/description/

/**
 * 3360. Stone Removal Game
 * Easy
 *
 * Alice and Bob are playing a game where they take turns removing stones from a pile, with Alice going
 * first.
 *
 * Alice starts by removing exactly 10 stones on her first turn.
 * For each subsequent turn, each player removes exactly 1 fewer stone than the previous opponent.
 *
 * The player who cannot make a move loses the game.
 *
 * Given a positive integer n, return true if Alice wins the game and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: n = 12
 * Output: true
 * Explanation:
 * Alice removes 10 stones on her first turn, leaving 2 stones for Bob.
 * Bob cannot remove 9 stones, so Alice wins.
 *
 * Example 2:
 *
 * Input: n = 1
 * Output: false
 * Explanation:
 * Alice cannot remove 10 stones, so Alice loses.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 50
 *
 */
public class StoneRemovalGame {

    // V0
    // IDEA: SIMULATION — THE MOVES ARE FORCED (10, 9, 8, ...), SO PLAY UNTIL SOMEONE CANNOT PAY
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean canAliceWin(int n) {
        int stonesLeft = n;
        int take = 10;
        boolean aliceToMove = true;
        while (stonesLeft >= take) {
            stonesLeft -= take;
            take--;
            aliceToMove = !aliceToMove;
        }
        // the player to move cannot pay and loses, so Alice wins iff it is Bob's turn
        return !aliceToMove;
    }
}
