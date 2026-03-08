package LeetCodeJava.Array;

// https://leetcode.com/problems/robot-bounded-in-circle/description/
/**
 *  1041. Robot Bounded In Circle
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * On an infinite plane, a robot initially stands at (0, 0) and faces north. Note that:
 *
 * The north direction is the positive direction of the y-axis.
 * The south direction is the negative direction of the y-axis.
 * The east direction is the positive direction of the x-axis.
 * The west direction is the negative direction of the x-axis.
 * The robot can receive one of three instructions:
 *
 * "G": go straight 1 unit.
 * "L": turn 90 degrees to the left (i.e., anti-clockwise direction).
 * "R": turn 90 degrees to the right (i.e., clockwise direction).
 * The robot performs the instructions given in order, and repeats them forever.
 *
 * Return true if and only if there exists a circle in the plane such that the robot never leaves the circle.
 *
 *
 *
 * Example 1:
 *
 * Input: instructions = "GGLLGG"
 * Output: true
 * Explanation: The robot is initially at (0, 0) facing the north direction.
 * "G": move one step. Position: (0, 1). Direction: North.
 * "G": move one step. Position: (0, 2). Direction: North.
 * "L": turn 90 degrees anti-clockwise. Position: (0, 2). Direction: West.
 * "L": turn 90 degrees anti-clockwise. Position: (0, 2). Direction: South.
 * "G": move one step. Position: (0, 1). Direction: South.
 * "G": move one step. Position: (0, 0). Direction: South.
 * Repeating the instructions, the robot goes into the cycle: (0, 0) --> (0, 1) --> (0, 2) --> (0, 1) --> (0, 0).
 * Based on that, we return true.
 * Example 2:
 *
 * Input: instructions = "GG"
 * Output: false
 * Explanation: The robot is initially at (0, 0) facing the north direction.
 * "G": move one step. Position: (0, 1). Direction: North.
 * "G": move one step. Position: (0, 2). Direction: North.
 * Repeating the instructions, keeps advancing in the north direction and does not go into cycles.
 * Based on that, we return false.
 * Example 3:
 *
 * Input: instructions = "GL"
 * Output: true
 * Explanation: The robot is initially at (0, 0) facing the north direction.
 * "G": move one step. Position: (0, 1). Direction: North.
 * "L": turn 90 degrees anti-clockwise. Position: (0, 1). Direction: West.
 * "G": move one step. Position: (-1, 1). Direction: West.
 * "L": turn 90 degrees anti-clockwise. Position: (-1, 1). Direction: South.
 * "G": move one step. Position: (-1, 0). Direction: South.
 * "L": turn 90 degrees anti-clockwise. Position: (-1, 0). Direction: East.
 * "G": move one step. Position: (0, 0). Direction: East.
 * "L": turn 90 degrees anti-clockwise. Position: (0, 0). Direction: North.
 * Repeating the instructions, the robot goes into the cycle: (0, 0) --> (0, 1) --> (-1, 1) --> (-1, 0) --> (0, 0).
 * Based on that, we return true.
 *
 *
 * Constraints:
 *
 * 1 <= instructions.length <= 100
 * instructions[i] is 'G', 'L' or, 'R'.
 *
 *
 *
 */
public class RobotBoundedInCircle {

    // V0
//    public boolean isRobotBounded(String instructions) {
//
//    }

    // V1
    // https://leetcode.com/problems/robot-bounded-in-circle/solutions/1676710/well-detailed-explaination-java-c-easy-f-u8r4/
    public boolean isRobotBounded_1(String instructions) {
        int dir[][] = {{0,1}, {-1, 0}, {0, -1}, {1,0}};
        int i = 0;
        int x = 0;
        int y = 0;

        for(int s = 0; s < instructions.length(); s++){
            if(instructions.charAt(s) == 'L'){
                i = (i + 1) % 4;
            }
            else if(instructions.charAt(s) == 'R'){
                i = (i + 3) % 4;
            }
            else{
                x = x + dir[i][0];
                y = y + dir[i][1];
            }
        }
        return x == 0 && y == 0 || i != 0;
    }


    // V2
    // https://leetcode.com/problems/robot-bounded-in-circle/solutions/1677124/cpythonjava-simple-one-pass-simulation-1-u8ae/
    public boolean isRobotBounded_2(String I) {
        int[][] d = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
        int di = 0, x = 0, y = 0;

        for (int i = 0; i < I.length(); i++) {
            switch (I.charAt(i)) {
                case 'L':
                    di = (di + 1) % 4;
                    break;
                case 'R':
                    di = (di + 3) % 4;
                    break;
                default:
                    x = x + d[di][0];
                    y = y + d[di][1];
            }
        }
        if (x == 0 && y == 0 || di > 0)
            return true;
        return false;
    }

    // V3
    // https://leetcode.com/problems/robot-bounded-in-circle/solutions/850969/my-java-solution-with-the-thought-proces-01zo/
    /**
    My thought process:
    So in question its given we are initially at 0, 0 at North directions. So we need to keep track of the points as well as the directions in which the robot travels. So we can have x, y = 0 and directions = North

    Now our problem is to find whether the robot is moving outside the circle after following some instructions. So the robot leaves the circle if it keep moving in the North direction.

    So lets loop through each and every character from the instruction string, then:
    1. We check whether its G, if G then we have to move one point from the current position.
        SO if we are at North direction, then if we consider the coordinate, we are at +y directions, so we will move only up, just understand like that, SO we increment our y by 1, by following this pattern we can automatically deduce that if we are at South, then decrement y by 1. Same way for East, increment x by 1 and for West decrement x by 1.
    2. Next we check wheter its L, then we have to move 90 degree left wards.
                    North
            West                East    , So do a counter clockwise assumption. If we are at a directions North, then its
                                          counter clockwis, yes West update direction by west, Same way if our directions is                          South                West, them its counter clockwise south, same way for direction south, update                                                     direction by east. So just rememebr if chaarcter is L, then counter clockwise.
    3. Next whetehr the character if R, then we already got it rememebr about clockwise direction and update direction according to it

    Finally we check whetehr the robot get back to the position, if yes, return true as the robot donot go out of the circle.
    We check whether the direction is still North, then it will sure go out of the circle, so return false.
    If none of the above condition satisfies, then also the robot will be some where inside the circle, so return true.
    */
    public boolean isRobotBounded_3(String instructions) {
        if (instructions.length() == 0)
            return false;
        int x = 0;
        int y = 0; // initial points of the robot
        String directions = "North"; // initial direction of robot
        /*
                    North
            West                East
                    South

        */
        for (char ch : instructions.toCharArray()) {
            if (ch == 'G') {
                if (directions.equals("North"))
                    y += 1;
                else if (directions.equals("South"))
                    y -= 1;
                else if (directions.equals("East"))
                    x += 1;
                else
                    x -= 1;
            } else if (ch == 'L') {
                if (directions.equals("North"))
                    directions = "West";
                else if (directions.equals("West"))
                    directions = "South";
                else if (directions.equals("South"))
                    directions = "East";
                else
                    directions = "North";
            } else if (ch == 'R') {
                if (directions.equals("North"))
                    directions = "East";
                else if (directions.equals("East"))
                    directions = "South";
                else if (directions.equals("South"))
                    directions = "West";
                else
                    directions = "North";
            }
        }
        if (x == 0 && y == 0)
            return true;
        if (directions.equals("North"))
            return false;
        return true;
    }



}
