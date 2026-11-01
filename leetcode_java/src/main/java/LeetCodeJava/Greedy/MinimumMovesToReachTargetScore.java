package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-moves-to-reach-target-score/description/

/**
 * 2139. Minimum Moves to Reach Target Score
 * Medium
 *
 * You are playing a game with integers. You start with the integer 1 and you want to reach the integer
 * target.
 *
 * In one move, you can either:
 *
 * Increment the current integer by one (i.e., x = x + 1).
 * Double the current integer (i.e., x = 2 * x).
 *
 * You can use the increment operation any number of times, however, you can only use the double operation
 * at most maxDoubles times.
 *
 * Given the two integers target and maxDoubles, return the minimum number of moves needed to reach target.
 *
 *
 * Example 1:
 *
 * Input: target = 5, maxDoubles = 0
 * Output: 4
 *
 * Example 2:
 *
 * Input: target = 19, maxDoubles = 2
 * Output: 7
 *
 * Example 3:
 *
 * Input: target = 10, maxDoubles = 4
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= target <= 10^9
 * 0 <= maxDoubles <= 100
 *
 */
public class MinimumMovesToReachTargetScore {

    // V0
    // IDEA: WORK BACKWARDS FROM target - HALVE WHENEVER YOU CAN (DOUBLING IS WORTH MOST AT THE TOP)
    /**
     * time = O(log target)
     * space = O(1)
     */
    public int minMoves(int target, int maxDoubles) {
        int moves = 0;
        int current = target;
        int doublesLeft = maxDoubles;

        // reversed moves: "double" becomes "halve", "increment" becomes "decrement"
        while (current > 1 && doublesLeft > 0) {
            if (current % 2 == 1) {
                // odd: one decrement to make it halvable
                current -= 1;
            } else {
                current /= 2;
                doublesLeft--;
            }
            moves++;
        }

        // no doubles left (or already at 1): the rest is pure decrements down to 1
        return moves + (current - 1);
    }
}
