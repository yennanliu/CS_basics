package LeetCodeJava.Math;

// https://leetcode.com/problems/furthest-point-from-origin/description/

/**
 * 2833. Furthest Point From Origin
 * Easy
 *
 * You are given a string moves of length n consisting only of characters 'L', 'R', and '_'. The string represents your movement on a number line starting from the origin 0.
 *
 * In the i-th move, you can choose one of the following directions:
 *
 * move to the left if moves[i] = 'L' or moves[i] = '_'
 * move to the right if moves[i] = 'R' or moves[i] = '_'
 *
 * Return the distance from the origin of the furthest point you can get to after n moves.
 *
 *
 * Example 1:
 *
 * Input: moves = "L_RL__R"
 * Output: 3
 *
 * Example 2:
 *
 * Input: moves = "_R__LL_"
 * Output: 5
 *
 * Example 3:
 *
 * Input: moves = "_______"
 * Output: 7
 *
 *
 * Constraints:
 *
 * 1 <= moves.length == n <= 50
 * moves consists only of characters 'L', 'R' and '_'.
 *
 */
public class FurthestPointFromOrigin {

    // V0
    // IDEA: COUNTING -- forced moves give |R - L|; every '_' then points the same way as that displacement
    /**
     * time = O(N)
     * space = O(1)
     */
    public int furthestDistanceFromOrigin(String moves) {
        int leftCount = 0;
        int rightCount = 0;
        int freeCount = 0;
        for (char move : moves.toCharArray()) {
            if (move == 'L') {
                leftCount++;
            } else if (move == 'R') {
                rightCount++;
            } else {
                freeCount++;
            }
        }
        return Math.abs(rightCount - leftCount) + freeCount;
    }
}
