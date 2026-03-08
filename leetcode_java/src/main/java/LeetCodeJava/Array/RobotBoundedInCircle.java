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

    // V0-1
    // IDEA: ARRAY OP (gemini)
    public boolean isRobotBounded_0_1(String instructions) {
        // 1. Directions: North, East, South, West (clockwise)
        // index: 0=N, 1=E, 2=S, 3=W
        /**  NOTE !!
         *
         *  we can also represent `index VS move` relation as below:
         *
         *   { 0: { 0, 1 },
         *    1: { 1, 0 },
         *    2: { 0, -1 },
         *    3 : { -1, 0 }
         *    }
         *
         */
        int[][] dirs = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        int x = 0, y = 0;
        int idx = 0; // Starting direction is North (index 0)

        /** NOTE !!!
         *
         *   // index: 0=N, 1=E, 2=S, 3=W.
         *
         *   so
         *      when `L` dir, index += 3
         *      when `R` dir, index += 1
         */
        for (char c : instructions.toCharArray()) {
            if (c == 'L') {
                // Turning Left is moving counter-clockwise in our array
                idx = (idx + 3) % 4;
            } else if (c == 'R') {
                // Turning Right is moving clockwise
                idx = (idx + 1) % 4;
            } else {
                // Move in the current direction
                x += dirs[idx][0];
                y += dirs[idx][1];
            }
        }

        /** NOTE !!!
         *
         *   1. since bot ALWAYS starts from origin (0,0) cell,
         *      so when it visits (0,0) again, we know it `move` as
         *      circle possibly.
         *
         *  2. `(idx != 0)` condition (explaination as below)
         *
         */
        /**
         *
         *This is the "aha!" moment of the problem. It comes down to **vector rotation**.
         *
         * Think of one full set of instructions as a single **net displacement** (a jump from point $A$ to point $B$) and a **net change in direction**.
         *
         * ### 1. If the Robot faces North ($idx = 0$)
         *
         * If the robot ends at $(x, y)$ and is still facing North, it means every time it repeats the instructions, it will move **the exact same distance in the exact same direction**.
         *
         * * Cycle 1: Ends at $(2, 2)$
         * * Cycle 2: Ends at $(4, 4)$
         * * Cycle 3: Ends at $(6, 6)$...
         * It's a straight line to infinity. **Not bounded.**
         *
         * ---
         *
         * ### 2. If the Robot faces any OTHER direction ($idx \neq 0$)
         *
         * If the direction changes, the "jump" rotates every time the instructions repeat. Let's see what happens if the robot ends facing **East** ($90^\circ$ right):
         *
         * * **Cycle 1:** Moves "North-ish" to $(2, 2)$. (Now facing East).
         * * **Cycle 2:** Since it's facing East, the "North-ish" instructions now actually move it "East-ish." It ends at $(4, 0)$. (Now facing South).
         * * **Cycle 3:** Now facing South, the instructions move it "South-ish." It ends at $(2, -2)$. (Now facing West).
         * * **Cycle 4:** Now facing West, it moves "West-ish" and ends back at **$(0, 0)$**! (Back to facing North).
         *
         * **Mathematically:**
         *
         * * If the net rotation is $90^\circ$ or $270^\circ$, it takes **4 cycles** to return to the origin.
         * * If the net rotation is $180^\circ$ (facing South), it takes **2 cycles** to return to the origin (it goes out, then comes right back).
         *
         * ---
         *
         * ### 📊 The "Bounded" Rule Summary
         *
         * A robot is trapped in a circle if and only if:
         *
         * 1. **Distance is 0:** It's already back where it started.
         * 2. **Direction $\neq$ North:** It is "turning." Even if it's currently far away, that turn guarantees it will eventually point back toward $(0,0)$ and close the loop.
         *
         * ### 💡 Visualizing the "Turn"
         *
         * Imagine a car. If you drive 10 feet and turn the wheel $90^\circ$ and repeat that 4 times, you've driven in a **square**. You are bounded. The only way to escape is to drive 10 feet and **not turn at all** (staying North).
         *
         * 
         */
        // A robot is bounded if:
        // 1. It returns to the origin (0,0)
        // 2. OR it is NOT facing North (idx != 0)
        return (x == 0 && y == 0) || (idx != 0);
    }


    // V0-2
    // IDEA: ARRAY OP (gpt)
    public boolean isRobotBounded_0_2(String instructions) {

        int x = 0, y = 0;
        int dir = 0; // 0=N, 1=E, 2=S, 3=W

        int[][] moves = {
                { 0, 1 }, // N
                { 1, 0 }, // E
                { 0, -1 }, // S
                { -1, 0 } // W
        };

        for (char c : instructions.toCharArray()) {
            if (c == 'G') {
                x += moves[dir][0];
                y += moves[dir][1];
            } else if (c == 'L') {
                dir = (dir + 3) % 4; // turn left
            } else if (c == 'R') {
                dir = (dir + 1) % 4; // turn right
            }
        }

        return (x == 0 && y == 0) || dir != 0;
    }


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
