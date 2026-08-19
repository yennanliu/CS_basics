package LeetCodeJava.Math;

// https://leetcode.com/problems/nim-game/

/**
 *  292. Nim Game
 *  Easy
 *
 *  You are playing the following Nim Game with your friend:
 *
 *   - Initially, there is a heap of stones on the table.
 *   - You and your friend will alternate taking turns, and you go first.
 *   - On each turn, the person whose turn it is will remove 1 to 3 stones from the heap.
 *   - The one who removes the last stone is the winner.
 *
 *  Given n, the number of stones in the heap, return true if you can win the game
 *  assuming both you and your friend play optimally, otherwise return false.
 *
 *  Example 1:
 *
 *  Input: n = 4
 *  Output: false
 *  Explanation: whatever you take (1~3), your friend takes the rest and wins.
 *
 *  Example 2:
 *
 *  Input: n = 1
 *  Output: true
 *
 *  Constraints:
 *
 *  1 <= n <= 2^31 - 1
 */
public class NimGame {

    // V0
    // IDEA: you lose exactly when n is a multiple of 4 (opponent can always restore a multiple of 4)
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean canWinNim(int n) {
        return n % 4 != 0;
    }
}
