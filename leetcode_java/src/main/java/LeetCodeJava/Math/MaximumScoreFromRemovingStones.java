package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-score-from-removing-stones/description/

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 1753. Maximum Score From Removing Stones
 * Medium
 *
 * You are playing a solitaire game with three piles of stones of sizes a, b, and c respectively. Each turn you choose two different non-empty piles, take one stone from each, and add 1 point to your score. The game stops when there are fewer than two non-empty piles (meaning there are no more available moves).
 *
 * Given three integers a, b, and c, return the maximum score you can get.
 *
 * Example 1:
 *
 * Input: a = 2, b = 4, c = 6
 * Output: 6
 * Explanation: The starting state is (2, 4, 6). One optimal set of moves is:
 * - Take from 1st and 3rd piles, state is now (1, 4, 5)
 * - Take from 1st and 3rd piles, state is now (0, 4, 4)
 * - Take from 2nd and 3rd piles, state is now (0, 3, 3)
 * - Take from 2nd and 3rd piles, state is now (0, 2, 2)
 * - Take from 2nd and 3rd piles, state is now (0, 1, 1)
 * - Take from 2nd and 3rd piles, state is now (0, 0, 0)
 * There are fewer than two non-empty piles, so the game ends. Total: 6 points.
 *
 * Example 2:
 *
 * Input: a = 4, b = 4, c = 6
 * Output: 7
 * Explanation: The starting state is (4, 4, 6). One optimal set of moves is:
 * - Take from 1st and 2nd piles, state is now (3, 3, 6)
 * - Take from 1st and 3rd piles, state is now (2, 3, 5)
 * - Take from 1st and 3rd piles, state is now (1, 3, 4)
 * - Take from 1st and 3rd piles, state is now (0, 3, 3)
 * - Take from 2nd and 3rd piles, state is now (0, 2, 2)
 * - Take from 2nd and 3rd piles, state is now (0, 1, 1)
 * - Take from 2nd and 3rd piles, state is now (0, 0, 0)
 * There are fewer than two non-empty piles, so the game ends. Total: 7 points.
 *
 * Example 3:
 *
 * Input: a = 1, b = 8, c = 8
 * Output: 8
 * Explanation: One optimal set of moves is to take from the 2nd and 3rd piles for 8 turns until they are empty.
 * After that, there are fewer than two non-empty piles, so the game ends.
 *
 * Constraints:
 *
 * 1 <= a, b, c <= 10^5
 *
 */
public class MaximumScoreFromRemovingStones {

    // V0
    // IDEA: MATH / GREEDY (sort so that x <= y <= z)
    //   x + y <= z: the biggest pile never runs out, every move pairs z with x or y -> x + y.
    //   otherwise the piles are balanced enough to pair until at most one stone is left -> sum / 2.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int maximumScore(int a, int b, int c) {
        int[] piles = {a, b, c};
        Arrays.sort(piles);
        int smallest = piles[0];
        int middle = piles[1];
        int largest = piles[2];

        if (smallest + middle <= largest) {
            return smallest + middle;
        }
        return (smallest + middle + largest) / 2;
    }

    // V1
    // IDEA: SIMULATE WITH A MAX HEAP (always take from the two largest piles)
    /**
     * time = O(S log 3) = O(S)  S = a + b + c
     * space = O(1)
     */
    public int maximumScore_1(int a, int b, int c) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((x, y) -> Integer.compare(y, x));
        maxHeap.offer(a);
        maxHeap.offer(b);
        maxHeap.offer(c);

        int score = 0;
        while (true) {
            int first = maxHeap.poll();
            int second = maxHeap.poll();
            // fewer than two non-empty piles: no more moves
            if (second == 0) {
                break;
            }
            score++;
            maxHeap.offer(first - 1);
            maxHeap.offer(second - 1);
        }
        return score;
    }
}
