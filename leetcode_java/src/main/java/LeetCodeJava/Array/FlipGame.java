package LeetCodeJava.Array;

// https://leetcode.com/problems/flip-game/

import java.util.ArrayList;
import java.util.List;

/**
 *  293. Flip Game
 *  Easy
 *
 *  You are playing a Flip Game with your friend.
 *
 *  You are given a string currentState that contains only '+' and '-'.
 *  You and your friend take turns to flip two consecutive "++" into "--".
 *  The game ends when a person can no longer make a move, and therefore
 *  the other person will be the winner.
 *
 *  Return all possible states of the string currentState after one valid
 *  move. You may return the answer in any order. If there is no valid move,
 *  return an empty list [].
 *
 *  Example 1:
 *   Input: currentState = "++++"
 *   Output: ["--++","+--+","++--"]
 *
 *  Example 2:
 *   Input: currentState = "+"
 *   Output: []
 *
 *  Constraints:
 *   1 <= currentState.length <= 500
 *   currentState[i] is either '+' or '-'
 */
public class FlipGame {

    // V0
    // IDEA: SCAN FOR "++", REPLACE THAT PAIR WITH "--"
    /**
     * time = O(n^2)   (n candidate states, each of length n)
     * space = O(n)    (excluding output)
     */
    public List<String> generatePossibleNextMoves(String currentState) {
        List<String> res = new ArrayList<>();
        if (currentState == null || currentState.length() < 2) {
            return res;
        }

        char[] arr = currentState.toCharArray();
        for (int i = 0; i + 1 < arr.length; i++) {
            if (arr[i] == '+' && arr[i + 1] == '+') {
                arr[i] = '-';
                arr[i + 1] = '-';
                res.add(new String(arr));
                // backtrack
                arr[i] = '+';
                arr[i + 1] = '+';
            }
        }

        return res;
    }
}
