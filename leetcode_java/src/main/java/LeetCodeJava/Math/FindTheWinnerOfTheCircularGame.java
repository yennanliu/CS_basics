package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-winner-of-the-circular-game/description/

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 1823. Find the Winner of the Circular Game
 * Medium
 *
 * There are n friends that are playing a game. The friends are sitting in a circle and are numbered from 1 to n in clockwise order. More formally, moving clockwise from the ith friend brings you to the (i+1)th friend for 1 <= i < n, and moving clockwise from the nth friend brings you to the 1st friend.
 *
 * The rules of the game are as follows:
 *
 * Start at the 1st friend.
 * Count the next k friends in the clockwise direction including the friend you started at. The counting wraps around the circle and may count some friends more than once.
 * The last friend you counted leaves the circle and loses the game.
 * If there is still more than one friend in the circle, go back to step 2 starting from the friend immediately clockwise of the friend who just lost and repeat.
 * Else, the last friend in the circle wins the game.
 *
 * Given the number of friends, n, and an integer k, return the winner of the game.
 *
 *
 * Example 1:
 *
 * Input: n = 5, k = 2
 * Output: 3
 * Explanation: Here are the steps of the game:
 * 1) Start at friend 1.
 * 2) Count 2 friends clockwise, which are friends 1 and 2.
 * 3) Friend 2 leaves the circle. Next start is friend 3.
 * 4) Count 2 friends clockwise, which are friends 3 and 4.
 * 5) Friend 4 leaves the circle. Next start is friend 5.
 * 6) Count 2 friends clockwise, which are friends 5 and 1.
 * 7) Friend 1 leaves the circle. Next start is friend 3.
 * 8) Count 2 friends clockwise, which are friends 3 and 5.
 * 9) Friend 5 leaves the circle. Only friend 3 is left, so they are the winner.
 *
 * Example 2:
 *
 * Input: n = 6, k = 5
 * Output: 1
 * Explanation: The friends leave in this order: 5, 4, 6, 2, 3. The winner is friend 1.
 *
 *
 * Constraints:
 *
 * 1 <= k <= n <= 500
 *
 *
 * Follow up:
 *
 * Could you solve this problem in linear time with constant space?
 *
 */
public class FindTheWinnerOfTheCircularGame {

    // V0
    // IDEA: JOSEPHUS RECURRENCE, BOTTOM-UP
    //   f(m) = 0-indexed winner among m people:  f(1) = 0,  f(m) = (f(m - 1) + k) % m,
    //   because after the first elimination the m - 1 survivors play the same game,
    //   rotated so that their seat 0 is the old seat k. Convert to 1-indexed at the end.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int findTheWinner(int n, int k) {
        int winner = 0;
        for (int m = 2; m <= n; m++) {
            winner = (winner + k) % m;
        }
        return winner + 1;
    }

    // V1
    // IDEA: SIMULATION WITH A QUEUE (rotate k - 1 friends to the back, remove the k-th)
    /**
     * time = O(N * K)
     * space = O(N)
     */
    public int findTheWinner_1(int n, int k) {
        Queue<Integer> circle = new ArrayDeque<>();
        for (int friend = 1; friend <= n; friend++) {
            circle.offer(friend);
        }
        while (circle.size() > 1) {
            // the first k - 1 friends counted survive this round: move them to the back
            for (int i = 0; i < k - 1; i++) {
                circle.offer(circle.poll());
            }
            // the k-th friend counted leaves the circle
            circle.poll();
        }
        return circle.peek();
    }
}
