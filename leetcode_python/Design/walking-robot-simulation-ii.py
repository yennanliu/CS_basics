"""

2069. Walking Robot Simulation II
Medium

A width x height grid is on an XY-plane with the bottom-left cell at (0, 0) and the top-right cell at (width - 1, height - 1). The grid is aligned with the four cardinal directions ("North", "East", "South", and "West"). A robot is initially at cell (0, 0) facing direction "East".

The robot can be instructed to move for a specific number of steps. For each step, it does the following.

Attempts to move forward one cell in the direction it is facing.
If the cell the robot is moving to is out of bounds, the robot instead turns 90 degrees counterclockwise and retries the step.

After the robot finishes moving the number of steps required, it stops and awaits the next instruction.

Implement the Robot class:

Robot(int width, int height) Initializes the width x height grid with the robot at (0, 0) facing "East".
void step(int num) Instructs the robot to move forward num steps.
int[] getPos() Returns the current cell the robot is at, as an array of length 2, [x, y].
String getDir() Returns the current direction of the robot, "North", "East", "South", or "West".


Example 1:

Input
["Robot", "step", "step", "getPos", "getDir", "step", "step", "step", "getPos", "getDir"]
[[6, 3], [2], [2], [], [], [2], [1], [4], [], []]
Output
[null, null, null, [4, 0], "East", null, null, null, [1, 2], "West"]

Explanation
Robot robot = new Robot(6, 3); // Initialize the grid and the robot at (0, 0) facing East.
robot.step(2);  // It moves two steps East to (2, 0), and faces East.
robot.step(2);  // It moves two steps East to (4, 0), and faces East.
robot.getPos(); // return [4, 0]
robot.getDir(); // return "East"
robot.step(2);  // It moves one step East to (5, 0), and faces East.
                // Moving the next step East would be out of bounds, so it turns and faces North.
                // Then, it moves one step North to (5, 1), and faces North.
robot.step(1);  // It moves one step North to (5, 2), and faces North (not West).
robot.step(4);  // Moving the next step North would be out of bounds, so it turns and faces West.
                // Then, it moves four steps West to (1, 2), and faces West.
robot.getPos(); // return [1, 2]
robot.getDir(); // return "West"


Constraints:

2 <= width, height <= 100
1 <= num <= 10^5
At most 10^4 calls in total will be made to step and getPos and getDir.

"""

# V0
# IDEA : THE ROBOT IS PERMANENTLY ON THE PERIMETER — WALK A CYCLIC RING
#
#   after the very first move the robot can never leave the border, and the
#   border is a cycle of length  perimeter = 2 * (width + height - 2).
#   precompute that ring of (x, y, direction) once, then `step(num)` is just
#       idx = (idx + num) % perimeter
#   which makes huge `num` values free.
#
#   the ONE special case is the start : the robot sits at (0, 0) facing East
#   before it has moved at all. after a full lap it lands back on (0, 0) but
#   facing SOUTH, so the ring's entry for (0, 0) stores "South" and a
#   separate `moved` flag reports "East" until the first step happens.
#
# time = O(width + height) to build, O(1) per call, space = O(width + height)
class Robot(object):

    def __init__(self, width, height):
        self.ring = []
        # bottom edge, left -> right (facing East)
        for x in range(width):
            self.ring.append((x, 0, "East"))
        # right edge, bottom -> top (facing North)
        for y in range(1, height):
            self.ring.append((width - 1, y, "North"))
        # top edge, right -> left (facing West)
        for x in range(width - 2, -1, -1):
            self.ring.append((x, height - 1, "West"))
        # left edge, top -> bottom (facing South)
        for y in range(height - 2, 0, -1):
            self.ring.append((0, y, "South"))
        # coming back to the origin means the robot is heading South
        self.ring[0] = (0, 0, "South")

        self.idx = 0
        self.moved = False

    def step(self, num):
        self.moved = True
        self.idx = (self.idx + num) % len(self.ring)

    def getPos(self):
        x, y, _ = self.ring[self.idx]
        return [x, y]

    def getDir(self):
        if not self.moved:
            return "East"
        return self.ring[self.idx][2]


# Your Robot object will be instantiated and called as such:
# obj = Robot(width, height)
# obj.step(num)
# param_2 = obj.getPos()
# param_3 = obj.getDir()
