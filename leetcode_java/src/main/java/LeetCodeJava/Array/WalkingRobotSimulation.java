package LeetCodeJava.Array;

// https://leetcode.com/problems/walking-robot-simulation/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

/**
 * 874. Walking Robot Simulation
 * Medium
 *
 * A robot on an infinite XY-plane starts at point (0, 0) facing north. The robot
 * receives an array of integers commands, which represents a sequence of moves that
 * it needs to execute. There are only three possible types of instructions the robot
 * can receive:
 *
 * -2: Turn left 90 degrees.
 * -1: Turn right 90 degrees.
 * 1 <= k <= 9: Move forward k units, one unit at a time.
 *
 * Some of the grid squares are obstacles. The ith obstacle is at grid point
 * obstacles[i] = (xi, yi). If the robot runs into an obstacle, it will stay in its
 * current location (on the block adjacent to the obstacle) and move onto the next
 * command.
 *
 * Return the maximum squared Euclidean distance that the robot reaches at any point
 * in its path (i.e. if the distance is 5, return 25).
 *
 * Note:
 * There can be an obstacle at (0, 0). If this happens, the robot will ignore the
 * obstacle until it has moved off the origin. However, it will be unable to return
 * to (0, 0) due to the obstacle.
 * North means +Y direction.
 * East means +X direction.
 * South means -Y direction.
 * West means -X direction.
 *
 *
 * Example 1:
 *
 * Input: commands = [4,-1,3], obstacles = []
 * Output: 25
 * Explanation:
 * The robot starts at (0, 0):
 * 1. Move north 4 units to (0, 4).
 * 2. Turn right.
 * 3. Move east 3 units to (3, 4).
 * The furthest point the robot ever gets from the origin is (3, 4),
 * which squared is 3^2 + 4^2 = 25 units away.
 *
 * Example 2:
 *
 * Input: commands = [4,-1,4,-2,4], obstacles = [[2,4]]
 * Output: 65
 * Explanation:
 * The robot starts at (0, 0):
 * 1. Move north 4 units to (0, 4).
 * 2. Turn right.
 * 3. Move east 1 unit and get blocked by the obstacle at (2, 4), robot is at (1, 4).
 * 4. Turn left.
 * 5. Move north 4 units to (1, 8).
 * The furthest point the robot ever gets from the origin is (1, 8),
 * which squared is 1^2 + 8^2 = 65 units away.
 *
 * Example 3:
 *
 * Input: commands = [6,-1,-1,6], obstacles = [[0,0]]
 * Output: 36
 * Explanation:
 * The robot starts at (0, 0):
 * 1. Move north 6 units to (0, 6).
 * 2. Turn right.
 * 3. Turn right.
 * 4. Move south 5 units and get blocked by the obstacle at (0,0), robot is at (0, 1).
 * The furthest point the robot ever gets from the origin is (0, 6),
 * which squared is 6^2 = 36 units away.
 *
 *
 * Constraints:
 *
 * 1 <= commands.length <= 10^4
 * commands[i] is either -2, -1, or an integer in the range [1, 9].
 * 0 <= obstacles.length <= 10^4
 * -3 * 10^4 <= xi, yi <= 3 * 10^4
 * The answer is guaranteed to be less than 2^31.
 *
 */
public class WalkingRobotSimulation {

    // V0
    // IDEA: SIMULATION + HASH SET for O(1) obstacle lookup
    /**
     *   Keep the 4 directions in CLOCKWISE order (north, east, south, west) so
     *   that a RIGHT turn is +1 and a LEFT turn is -1 (mod 4).
     *
     *   Move ONE UNIT AT A TIME and check the NEXT cell against the obstacle set;
     *   stop the current command as soon as it is blocked.
     *
     *   NOTE !!! because we only test the cell we are ABOUT TO ENTER, an obstacle
     *            sitting on the start (0, 0) is naturally ignored until the robot
     *            tries to come back -- exactly what the problem asks for.
     *
     *   NOTE !!! java's `%` can go negative, so a left turn uses `(d - 1 + 4) % 4`.
     *
     *   Track the max squared distance after EVERY single unit step (the furthest
     *   point may NOT be at the end of a command).
     *
     *   time  = O(sum(commands) + obstacles.length)   // each command moves <= 9 units
     *   space = O(obstacles.length)
     */
    public int robotSim(int[] commands, int[][] obstacles) {
        // clockwise: north, east, south, west
        int[][] dirs = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        /** NOTE !!!
         *
         *  coordinates span [-3*10^4, 3*10^4], so we pack (x, y) into one long
         *  instead of allocating a 2D grid
         */
        Set<Long> blocked = new HashSet<>();
        for (int[] o : obstacles) {
            blocked.add(encode(o[0], o[1]));
        }

        int x = 0;
        int y = 0;
        int d = 0; // facing north
        int ans = 0;

        for (int c : commands) {
            if (c == -2) {
                d = (d - 1 + 4) % 4; // turn LEFT
            } else if (c == -1) {
                d = (d + 1) % 4;     // turn RIGHT
            } else {
                int dx = dirs[d][0];
                int dy = dirs[d][1];
                for (int step = 0; step < c; step++) {
                    if (blocked.contains(encode(x + dx, y + dy))) {
                        break; // stop RIGHT BEFORE the obstacle
                    }
                    x += dx;
                    y += dy;
                    ans = Math.max(ans, x * x + y * y);
                }
            }
        }

        return ans;
    }

    private long encode(int x, int y) {
        return (long) x * 1000000L + y;
    }


    // V1
    // IDEA: GROUP OBSTACLES BY ROW / COLUMN + BINARY SEARCH THE BLOCKER
    /**
     *  Instead of stepping one unit at a time and probing a hash set, precompute
     *  for every row the sorted list of obstacle x's (and per column the sorted
     *  y's). A move then JUMPS straight to the blocking obstacle via binary search.
     *
     *  -> a single command costs O(log(obstacles)) instead of O(k) probes,
     *     which matters when k is large.
     *
     *  time  = O(len(obstacles) * log + len(commands) * log)
     *  space = O(len(obstacles))
     */
    public int robotSim_1(int[] commands, int[][] obstacles) {
        Map<Integer, List<Integer>> byRow = new HashMap<>(); // y -> sorted xs
        Map<Integer, List<Integer>> byCol = new HashMap<>(); // x -> sorted ys
        for (int[] o : obstacles) {
            byRow.computeIfAbsent(o[1], k -> new ArrayList<>()).add(o[0]);
            byCol.computeIfAbsent(o[0], k -> new ArrayList<>()).add(o[1]);
        }
        for (List<Integer> v : byRow.values()) {
            Collections.sort(v);
        }
        for (List<Integer> v : byCol.values()) {
            Collections.sort(v);
        }

        int[][] dirs = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
        int x = 0;
        int y = 0;
        int d = 0;
        int ans = 0;

        for (int c : commands) {
            if (c == -2) {
                d = (d - 1 + 4) % 4;
                continue;
            }
            if (c == -1) {
                d = (d + 1) % 4;
                continue;
            }

            if (dirs[d][0] == 0) {
                // vertical move along column x
                List<Integer> ys = byCol.getOrDefault(x, Collections.emptyList());
                int target = y + dirs[d][1] * c;
                if (dirs[d][1] > 0) {
                    Integer blk = firstGreater(ys, y);
                    if (blk != null) {
                        target = Math.min(target, blk - 1);
                    }
                } else {
                    Integer blk = lastLess(ys, y);
                    if (blk != null) {
                        target = Math.max(target, blk + 1);
                    }
                }
                y = target;
            } else {
                // horizontal move along row y
                List<Integer> xs = byRow.getOrDefault(y, Collections.emptyList());
                int target = x + dirs[d][0] * c;
                if (dirs[d][0] > 0) {
                    Integer blk = firstGreater(xs, x);
                    if (blk != null) {
                        target = Math.min(target, blk - 1);
                    }
                } else {
                    Integer blk = lastLess(xs, x);
                    if (blk != null) {
                        target = Math.max(target, blk + 1);
                    }
                }
                x = target;
            }

            ans = Math.max(ans, x * x + y * y);
        }

        return ans;
    }

    private Integer firstGreater(List<Integer> sorted, int v) {
        int lo = 0;
        int hi = sorted.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (sorted.get(mid) > v) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo < sorted.size() ? sorted.get(lo) : null;
    }

    private Integer lastLess(List<Integer> sorted, int v) {
        int lo = 0;
        int hi = sorted.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (sorted.get(mid) < v) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo > 0 ? sorted.get(lo - 1) : null;
    }

    // V2
    // IDEA: STRING-KEYED OBSTACLE SET
    /**
     *  Same unit-step simulation as V0, but the obstacle key is the string
     *  "x,y" instead of a packed long.
     *
     *  Slower and allocation-heavy, yet it is the version that CANNOT silently
     *  collide -- worth keeping as the reference when the packing constant of V0
     *  is under suspicion.
     *
     *  time  = O(sum(commands) + len(obstacles))
     *  space = O(len(obstacles))
     */
    public int robotSim_2(int[] commands, int[][] obstacles) {
        Set<String> blocked = new HashSet<>();
        for (int[] o : obstacles) {
            blocked.add(o[0] + "," + o[1]);
        }

        int[] dx = { 0, 1, 0, -1 };
        int[] dy = { 1, 0, -1, 0 };
        int x = 0;
        int y = 0;
        int d = 0;
        int ans = 0;

        for (int c : commands) {
            if (c == -2) {
                d = (d + 3) % 4;
            } else if (c == -1) {
                d = (d + 1) % 4;
            } else {
                for (int s = 0; s < c; s++) {
                    if (blocked.contains((x + dx[d]) + "," + (y + dy[d]))) {
                        break;
                    }
                    x += dx[d];
                    y += dy[d];
                    ans = Math.max(ans, x * x + y * y);
                }
            }
        }
        return ans;
    }

    // V3
    // IDEA: COMPLEX-NUMBER STYLE ROTATION (no direction table)
    /**
     *  Represent the heading as a vector (dx, dy) and rotate it arithmetically:
     *      turn right : (dx, dy) -> ( dy, -dx)
     *      turn left  : (dx, dy) -> (-dy,  dx)
     *
     *  This drops the dirs[] table and the modulo bookkeeping entirely -- the
     *  same trick used for spiral-matrix walks.
     *
     *  time  = O(sum(commands) + len(obstacles))
     *  space = O(len(obstacles))
     */
    public int robotSim_3(int[] commands, int[][] obstacles) {
        Set<Long> blocked = new HashSet<>();
        for (int[] o : obstacles) {
            blocked.add((long) o[0] * 1000000L + o[1]);
        }

        int x = 0;
        int y = 0;
        int dx = 0; // facing north
        int dy = 1;
        int ans = 0;

        for (int c : commands) {
            if (c == -1) {           // right
                int t = dx;
                dx = dy;
                dy = -t;
            } else if (c == -2) {    // left
                int t = dx;
                dx = -dy;
                dy = t;
            } else {
                for (int s = 0; s < c; s++) {
                    if (blocked.contains((long) (x + dx) * 1000000L + (y + dy))) {
                        break;
                    }
                    x += dx;
                    y += dy;
                    ans = Math.max(ans, x * x + y * y);
                }
            }
        }
        return ans;
    }

}
