package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/flip-game-ii/

import java.util.*;

/**
 *  294. Flip Game II
 *  Medium
 *
 *  You are playing a Flip Game with your friend.
 *
 *  You are given a string currentState that contains only '+' and '-'. You and
 *  your friend take turns to flip two consecutive "++" into "--". The game ends
 *  when a person can no longer make a move, and therefore the other person will
 *  be the winner.
 *
 *  Return true if the starting player can guarantee a win, and false otherwise.
 *
 *  Example 1:
 *   Input: currentState = "++++"
 *   Output: true
 *   Explanation: The starting player can guarantee a win by flipping the middle "++"
 *                to become "+--+".
 *
 *  Example 2:
 *   Input: currentState = "+"
 *   Output: false
 *
 *  Constraints:
 *   1 <= currentState.length <= 60
 *   currentState[i] is either '+' or '-'.
 */
public class FlipGameII {

    // V0
    // IDEA: minimax / backtracking with memoization on the board state -
    //       current player wins if ANY move leaves the opponent in a losing state
    /**
     * time = O(n!!) worst case, heavily reduced by the memo
     * space = O(number of reachable states * n)
     */
    public boolean canWin(String currentState) {
        if (currentState == null || currentState.length() < 2) {
            return false;
        }
        return helper(currentState, new HashMap<String, Boolean>());
    }

    private boolean helper(String state, Map<String, Boolean> memo) {
        if (memo.containsKey(state)) {
            return memo.get(state);
        }
        boolean win = false;
        for (int i = 0; i + 1 < state.length(); i++) {
            if (state.charAt(i) == '+' && state.charAt(i + 1) == '+') {
                String next = state.substring(0, i) + "--" + state.substring(i + 2);
                // opponent loses on `next` -> current player wins
                if (!helper(next, memo)) {
                    win = true;
                    break;
                }
            }
        }
        memo.put(state, win);
        return win;
    }
}
