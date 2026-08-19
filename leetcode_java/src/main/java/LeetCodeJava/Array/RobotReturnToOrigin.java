package LeetCodeJava.Array;

// https://leetcode.com/problems/robot-return-to-origin/

/**
 *  657. Robot Return to Origin
 *  Easy
 *
 *  There is a robot starting at the position (0, 0), the origin, on a 2D plane.
 *  Given a sequence of its moves, judge if this robot ends up at (0, 0) after
 *  it completes its moves.
 *
 *  You are given a string moves that represents the move sequence of the robot
 *  where moves[i] represents its i-th move. Valid moves are 'R' (right),
 *  'L' (left), 'U' (up), and 'D' (down).
 *
 *  Return true if the robot returns to the origin after it finishes all of its
 *  moves, or false otherwise.
 *
 *  Example 1:
 *  Input: moves = "UD"
 *  Output: true
 *
 *  Example 2:
 *  Input: moves = "LL"
 *  Output: false
 *
 *  Constraints:
 *  1 <= moves.length <= 2 * 10^4
 *  moves only contains the characters 'U', 'D', 'L' and 'R'.
 */
public class RobotReturnToOrigin {

    // V0
    // IDEA: track x / y offsets; back at origin iff both are 0
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean judgeCircle(String moves) {
        if (moves == null || moves.isEmpty()) {
            return true;
        }
        int x = 0;
        int y = 0;
        for (int i = 0; i < moves.length(); i++) {
            char c = moves.charAt(i);
            if (c == 'U') {
                y++;
            } else if (c == 'D') {
                y--;
            } else if (c == 'L') {
                x--;
            } else if (c == 'R') {
                x++;
            }
        }
        return x == 0 && y == 0;
    }
}
