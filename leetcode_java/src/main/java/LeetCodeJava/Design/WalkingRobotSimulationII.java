package LeetCodeJava.Design;

// https://leetcode.com/problems/walking-robot-simulation-ii/

/**
 *  2069. Walking Robot Simulation II
 *  Medium
 *
 *  A width x height grid is on an XY-plane with the bottom-left cell at (0, 0) and the
 *  top-right cell at (width - 1, height - 1). The grid is aligned with the four cardinal
 *  directions ("North", "East", "South", and "West"). A robot is initially at cell (0, 0)
 *  facing direction "East".
 *
 *  The robot can be instructed to move for a specific number of steps. For each step, it
 *  does the following:
 *
 *   - Attempts to move forward one cell in the direction it is facing.
 *   - If the cell the robot is moving to is out of bounds, the robot instead turns 90 degrees
 *     counterclockwise and retries the step.
 *
 *  Implement the Robot class:
 *
 *   - Robot(int width, int height) Initializes the width x height grid with the robot at
 *     (0, 0) facing "East".
 *   - void step(int num) Instructs the robot to move forward num steps.
 *   - int[] getPos() Returns the current cell the robot is at, as an array [x, y].
 *   - String getDir() Returns the current direction of the robot, "North", "East",
 *     "South", or "West".
 *
 *  Example 1:
 *
 *  Input
 *  ["Robot", "step", "step", "getPos", "getDir", "step", "step", "step", "getPos", "getDir"]
 *  [[6, 3], [2], [2], [], [], [2], [1], [4], [], []]
 *  Output
 *  [null, null, null, [4, 0], "East", null, null, null, [1, 2], "West"]
 *
 *  Explanation
 *  Robot robot = new Robot(6, 3); // robot at (0, 0) facing East
 *  robot.step(2);  // two steps East -> (2, 0), facing East
 *  robot.step(2);  // two steps East -> (4, 0), facing East
 *  robot.getPos(); // return [4, 0]
 *  robot.getDir(); // return "East"
 *  robot.step(2);  // one step East -> (5, 0); next step would be out of bounds, so it
 *                  // turns to face North, then moves one step -> (5, 1), facing North
 *  robot.step(1);  // one step North -> (5, 2), facing North (not West)
 *  robot.step(4);  // out of bounds ahead -> turn to West, then four steps -> (1, 2), West
 *  robot.getPos(); // return [1, 2]
 *  robot.getDir(); // return "West"
 *
 *  Constraints:
 *
 *   2 <= width, height <= 100
 *   1 <= num <= 10^5
 *   At most 10^4 calls in total will be made to step, getPos and getDir.
 */
public class WalkingRobotSimulationII {

    // V0
    // IDEA: THE ROBOT IS PERMANENTLY ON THE PERIMETER -> WALK A CYCLIC RING
    //       after the very first move the robot can never leave the border, and the border
    //       is a cycle of length  perimeter = 2 * (width + height - 2).
    //       precompute that ring of (x, y, direction) once, then step(num) is just
    //           idx = (idx + num) % perimeter
    //       which makes huge `num` values free (no per-step simulation).
    //
    //       the ONE special case is the start: the robot sits at (0, 0) facing East before
    //       it has moved at all. after a full lap it lands back on (0, 0) but facing SOUTH,
    //       so the ring's entry for (0, 0) stores "South", and a separate `moved` flag makes
    //       getDir() report "East" until the first step happens.
    /**
     * time = O(width + height) to build, O(1) per call
     * space = O(width + height)
     */
    private final int[][] ringPos;   // ringPos[i] = {x, y}
    private final String[] ringDir;  // ringDir[i] = direction faced when sitting on i
    private final int perimeter;
    private int idx;
    private boolean moved;

    public WalkingRobotSimulationII(int width, int height) {
        this.perimeter = 2 * (width + height - 2);
        this.ringPos = new int[perimeter][2];
        this.ringDir = new String[perimeter];

        int i = 0;
        // bottom edge, left -> right (facing East)
        for (int x = 0; x < width; x++) {
            ringPos[i] = new int[]{x, 0};
            ringDir[i++] = "East";
        }
        // right edge, bottom -> top (facing North)
        for (int y = 1; y < height; y++) {
            ringPos[i] = new int[]{width - 1, y};
            ringDir[i++] = "North";
        }
        // top edge, right -> left (facing West)
        for (int x = width - 2; x >= 0; x--) {
            ringPos[i] = new int[]{x, height - 1};
            ringDir[i++] = "West";
        }
        // left edge, top -> bottom (facing South)
        for (int y = height - 2; y >= 1; y--) {
            ringPos[i] = new int[]{0, y};
            ringDir[i++] = "South";
        }
        // coming back to the origin means the robot is heading South
        ringDir[0] = "South";

        this.idx = 0;
        this.moved = false;
    }

    public void step(int num) {
        this.moved = true;
        this.idx = (this.idx + num) % this.perimeter;
    }

    public int[] getPos() {
        return new int[]{ringPos[idx][0], ringPos[idx][1]};
    }

    public String getDir() {
        if (!this.moved) {
            return "East"; // never moved -> still the initial facing
        }
        return ringDir[idx];
    }
}
