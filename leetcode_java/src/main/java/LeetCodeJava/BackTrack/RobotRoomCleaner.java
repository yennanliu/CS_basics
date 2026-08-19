package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/robot-room-cleaner/

import java.util.*;

/**
 *  489. Robot Room Cleaner
 *  Hard
 *
 *  You are controlling a robot that is located somewhere in a room. The room is
 *  modeled as an m x n binary grid where 0 represents a wall and 1 represents an
 *  empty slot.
 *
 *  The robot starts at an unknown location in the room that is guaranteed to be
 *  empty, and you do not have access to the grid, but you can move the robot
 *  using the given API Robot. You are tasked to use the robot to clean the
 *  entire room.
 *
 *  When the robot tries to move into a wall cell, its bumper sensor detects the
 *  obstacle, and it stays on the current cell.
 *
 *  API:
 *   boolean move();   // true if next cell is open and robot moved into it
 *   void turnLeft();  // 90 degrees, robot stays on the same cell
 *   void turnRight(); // 90 degrees, robot stays on the same cell
 *   void clean();     // clean the current cell
 *
 *  The initial direction of the robot is facing up. All four edges of the grid
 *  are surrounded by a wall.
 *
 *  Example 1:
 *   Input: room = [[1,1,1,1,1,0,1,1],[1,1,1,1,1,0,1,1],[1,0,1,1,1,1,1,1],
 *                  [0,0,0,1,0,0,0,0],[1,1,1,1,1,1,1,1]], row = 1, col = 3
 *   Output: Robot cleaned all rooms.
 *
 *  Constraints:
 *   m == room.length, n == room[i].length
 *   1 <= m <= 100, 1 <= n <= 200
 *   room[i][j] is either 0 or 1.
 *   All the empty cells can be visited from the starting position.
 */
public class RobotRoomCleaner {

    /**
     * The robot's control interface (provided by LeetCode).
     * Declared here so this file compiles standalone - do NOT implement it.
     */
    public interface Robot {
        // returns true if next cell is open and robot moves into the cell.
        // returns false if next cell is obstacle and robot stays on the current cell.
        public boolean move();

        // Robot will stay on the same cell after calling turnLeft/turnRight.
        // Each turn will be 90 degrees.
        public void turnLeft();

        public void turnRight();

        // Clean the current cell.
        public void clean();
    }

    // V0
    // IDEA: backtracking (spiral DFS) on a virtual coordinate system.
    //       The robot starts at (0,0) facing "up"; after exploring a direction we
    //       physically "go back" so the recursion state matches the robot state.
    /**
     * time = O(N), N = number of accessible cells (each visited a constant number of times)
     * space = O(N)
     */
    // directions in order: up, right, down, left (clockwise, matches turnRight)
    private static final int[][] DIRS = new int[][]{ {-1, 0}, {0, 1}, {1, 0}, {0, -1} };

    public void cleanRoom(Robot robot) {
        Set<String> visited = new HashSet<>();
        backtrack(robot, 0, 0, 0, visited);
    }

    private void backtrack(Robot robot, int row, int col, int dir, Set<String> visited) {
        visited.add(row + "," + col);
        robot.clean();

        // try all 4 directions, rotating clockwise
        for (int i = 0; i < 4; i++) {
            int newDir = (dir + i) % 4;
            int newRow = row + DIRS[newDir][0];
            int newCol = col + DIRS[newDir][1];

            if (!visited.contains(newRow + "," + newCol) && robot.move()) {
                backtrack(robot, newRow, newCol, newDir, visited);
                goBack(robot);
            }
            // face the next direction
            robot.turnRight();
        }
    }

    // turn 180 degrees, move one step, then turn back 180 degrees
    private void goBack(Robot robot) {
        robot.turnRight();
        robot.turnRight();
        robot.move();
        robot.turnRight();
        robot.turnRight();
    }
}
