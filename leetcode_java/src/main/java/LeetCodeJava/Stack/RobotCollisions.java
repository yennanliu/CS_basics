package LeetCodeJava.Stack;

// https://leetcode.com/problems/robot-collisions/description/

import java.util.*;

/**
 * 2751. Robot Collisions
 * Hard
 *
 * There are n 1-indexed robots, each having a position on a line, health, and movement direction.
 *
 * You are given 0-indexed integer arrays positions, healths, and a string directions (directions[i] is either 'L' for left or 'R' for right). All integers in positions are unique.
 *
 * All robots start moving on the line simultaneously at the same speed in their given directions. If two robots ever share the same position while moving, they will collide.
 *
 * If two robots collide, the robot with lower health is removed from the line, and the health of the other robot decreases by one. The surviving robot continues in the same direction it was going. If both robots have the same health, they are both removed from the line.
 *
 * Your task is to determine the health of the robots that survive the collisions, in the same order that the robots were given, i.e. final health of robot 1 (if survived), final health of robot 2 (if survived), and so on. If there are no survivors, return an empty array.
 *
 * Return an array containing the health of the remaining robots (in the order they were given in the input), after no further collisions can occur.
 *
 * Note: The positions may be unsorted.
 *
 *
 * Example 1:
 *
 * Input: positions = [5,4,3,2,1], healths = [2,17,9,15,10], directions = "RRRRR"
 * Output: [2,17,9,15,10]
 * Explanation: No collision occurs in this example, since all robots are moving in the same direction. So, the health of the robots in order from the first robot is returned, [2, 17, 9, 15, 10].
 *
 * Example 2:
 *
 * Input: positions = [3,5,2,6], healths = [10,10,15,12], directions = "RLRL"
 * Output: [14]
 * Explanation: There are 2 collisions in this example. Firstly, robot 1 and robot 2 will collide, and since both have the same health, they will be removed from the line. Next, robot 3 and robot 4 will collide and since robot 4's health is smaller, it gets removed, and robot 3's health becomes 15 - 1 = 14. Only robot 3 remains, so we return [14].
 *
 * Example 3:
 *
 * Input: positions = [1,2,5,6], healths = [10,10,11,11], directions = "RLRL"
 * Output: []
 * Explanation: Robot 1 and robot 2 will collide and since both have the same health, they are both removed. Robot 3 and 4 will collide and since both have the same health, they are both removed. So, we return an empty array, [].
 *
 *
 * Constraints:
 *
 * 1 <= positions.length == healths.length == directions.length == n <= 10^5
 * 1 <= positions[i], healths[i] <= 10^9
 * directions[i] == 'L' or directions[i] == 'R'
 * All values in positions are distinct
 *
 */
public class RobotCollisions {

    // V0
    // IDEA: SORT BY POSITION + STACK SIMULATION ("asteroid collision")
    //       only an 'R' robot and an 'L' robot to its right can collide. walk the robots by
    //       increasing position; the stack holds the still-alive right-movers, so an 'L' robot
    //       always fights the stack top first, and keeps fighting until it dies or the stack is
    //       empty. dead robots get health 0; the survivors are read back in input order.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public List<Integer> survivedRobotsHealths(int[] positions, int[] healths, String directions) {
        int n = positions.length;
        // copy, so the caller's array is not modified
        int[] health = new int[n];
        for (int i = 0; i < n; i++) {
            health[i] = healths[i];
        }

        // sort the indices (not the arrays), so the input order stays recoverable
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(positions[a], positions[b]));

        // indices of alive right-moving robots, nearest one on top
        Deque<Integer> rightMovers = new ArrayDeque<>();
        for (int current : order) {
            if (directions.charAt(current) == 'R') {
                rightMovers.push(current);
                continue;
            }
            // a left-mover fights until it dies or nobody is left to fight
            while (!rightMovers.isEmpty() && health[current] > 0) {
                int top = rightMovers.peek();
                if (health[top] > health[current]) {
                    // the left-mover dies, the right-mover loses 1
                    health[top]--;
                    health[current] = 0;
                } else if (health[top] < health[current]) {
                    // the right-mover dies, the left-mover loses 1 and keeps going
                    health[current]--;
                    health[top] = 0;
                    rightMovers.pop();
                } else {
                    // equal health: both are removed
                    health[current] = 0;
                    health[top] = 0;
                    rightMovers.pop();
                }
            }
        }

        List<Integer> survivors = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (health[i] > 0) {
                survivors.add(health[i]);
            }
        }
        return survivors;
    }
}
