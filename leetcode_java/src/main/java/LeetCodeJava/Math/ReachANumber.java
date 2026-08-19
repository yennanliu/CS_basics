package LeetCodeJava.Math;

// https://leetcode.com/problems/reach-a-number/

/**
 *  754. Reach a Number
 *  Medium
 *
 *  You are standing at position 0 on an infinite number line. There is a
 *  destination at position target.
 *
 *  You can make some number of moves numMoves so that:
 *    - On each move, you can either go left or right.
 *    - During the ith move (starting from i == 1 to i == numMoves), you take
 *      i steps in the chosen direction.
 *
 *  Given the integer target, return the minimum number of moves required
 *  (i.e. the minimum numMoves) to reach the destination.
 *
 *  Example 1:
 *    Input: target = 2
 *    Output: 3
 *    Explanation: 0 -> 1 -> -1 -> 2
 *
 *  Example 2:
 *    Input: target = 3
 *    Output: 2
 *    Explanation: 0 -> 1 -> 3
 *
 *  Constraints:
 *   - -10^9 <= target <= 10^9
 *   - target != 0
 */
public class ReachANumber {

    // V0
    // IDEA: MATH / GREEDY.
    //       By symmetry only |target| matters. Keep adding 1,2,3,... until the
    //       running sum >= target AND (sum - target) is even -- flipping the
    //       sign of a move of size k changes the sum by 2k (always even), so
    //       an even surplus can always be cancelled out exactly.
    /**
     * time = O(sqrt(target))
     * space = O(1)
     */
    public int reachNumber(int target) {

        long t = Math.abs((long) target);

        long sum = 0;
        int moves = 0;

        while (sum < t || (sum - t) % 2 != 0) {
            moves++;
            sum += moves;
        }

        return moves;
    }
}
