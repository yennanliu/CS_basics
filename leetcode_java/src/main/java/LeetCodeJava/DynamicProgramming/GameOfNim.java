package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/game-of-nim/description/

import java.util.*;

/**
 * 1908. Game of Nim
 * Medium
 *
 * Alice and Bob take turns playing a game with Alice starting first.
 *
 * In this game, there are n piles of stones. On each player's turn, the player should remove any
 * positive number of stones from a non-empty pile of his or her choice. The first player who cannot
 * make a move loses, and the other player wins.
 *
 * Given an integer array piles, where piles[i] is the number of stones in the ith pile, return true
 * if Alice wins, or false if Bob wins.
 *
 * Both Alice and Bob play optimally.
 *
 *
 * Example 1:
 *
 * Input: piles = [1]
 * Output: true
 *
 * Example 2:
 *
 * Input: piles = [1,1]
 * Output: false
 *
 * Example 3:
 *
 * Input: piles = [1,2,3]
 * Output: false
 *
 *
 * Constraints:
 *
 * n == piles.length
 * 1 <= n <= 7
 * 1 <= piles[i] <= 7
 *
 *
 * Follow-up: Could you find a linear time solution? Although the linear time solution may be beyond
 * the scope of an interview, it could be interesting to know.
 *
 */
public class GameOfNim {

    // V0
    // IDEA: GAME THEORY / SPRAGUE-GRUNDY -> XOR "NIM-SUM"
    //
    //   the Grundy value of one pile of size x is x; the whole game's is the XOR of the piles.
    //   the player to move LOSES  <=>  XOR == 0.
    //   why: if XOR != 0, some pile has XOR's highest set bit; shrink it to make XOR 0 and hand
    //   a losing position over. if XOR == 0, every move makes it non-zero.
    //   Alice moves first -> Alice wins <=> XOR(piles) != 0.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean nimGame(int[] piles) {
        int nimSum = 0;
        for (int pile : piles) {
            nimSum ^= pile;
        }
        return nimSum != 0;
    }

    // V1
    // IDEA: MEMOIZED GAME DP OVER PILE STATES (the in-interview approach for n, piles <= 7)
    //
    //   win(state) = true if SOME move leads to a state where the opponent cannot win.
    //   the order of piles does not matter, so the state is the SORTED piles -> fewer states.
    //   no move possible (all piles empty) -> the player to move loses.
    /**
     * time = O(S * N * maxPile), S = number of distinct sorted states (at most C(14, 7) = 3432)
     * space = O(S)
     */
    public boolean nimGame_1(int[] piles) {
        Map<String, Boolean> memo = new HashMap<>();
        int[] start = piles.clone();
        Arrays.sort(start);
        return canWin(start, memo);
    }

    /** True if the player to move wins from these (sorted) piles. */
    private boolean canWin(int[] piles, Map<String, Boolean> memo) {
        String key = Arrays.toString(piles);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        boolean win = false;
        for (int i = 0; i < piles.length && !win; i++) {
            // remove `take` stones from pile i
            for (int take = 1; take <= piles[i]; take++) {
                int[] next = piles.clone();
                next[i] -= take;
                Arrays.sort(next);
                if (!canWin(next, memo)) {
                    win = true;
                    break;
                }
            }
        }
        memo.put(key, win);
        return win;
    }
}
