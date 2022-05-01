"""

1041. Robot Bounded In Circle
Medium

On an infinite plane, a robot initially stands at (0, 0) and faces north. The robot can receive one of three instructions:

"G": go straight 1 unit;
"L": turn 90 degrees to the left;
"R": turn 90 degrees to the right.
The robot performs the instructions given in order, and repeats them forever.

Return true if and only if there exists a circle in the plane such that the robot never leaves the circle.

 

Example 1:

Input: instructions = "GGLLGG"
Output: true
Explanation: The robot moves from (0,0) to (0,2), turns 180 degrees, and then returns to (0,0).
When repeating these instructions, the robot remains in the circle of radius 2 centered at the origin.
Example 2:

Input: instructions = "GG"
Output: false
Explanation: The robot moves north indefinitely.
Example 3:

Input: instructions = "GL"
Output: true
Explanation: The robot moves from (0, 0) -> (0, 1) -> (-1, 1) -> (-1, 0) -> (0, 0) -> ...
 

Constraints:

1 <= instructions.length <= 100
instructions[i] is 'G', 'L' or, 'R'.

"""

# V0
# IDEA : math + array
class Solution:
    def isRobotBounded(self, instructions):
        """
        NOTE !!! we make direction as below

         c == 'L':  move LEFT : [0,-1]
         c == 'R':  move RIGHT : [0,1]
        """
        dirs = [[0,1], [1,0], [0,-1], [-1,0]]
        x = 0;
        y = 0;
        idx = 0;
        for c in instructions:
            print ("c = " + str(c) + " idx = " + str(idx))
            """
            NOTE !!! since we need to verify if robot back to start point
                -> we use (idx + k)  % 4 for detecting cyclic cases
            """
            if c == 'L':
                idx = (idx + 3) % 4
            elif c == 'R':
                idx = (idx + 1) % 4
            elif c == 'G':
                x = x + dirs[idx][0]
                y = y + dirs[idx][1]
        return (x == 0 and y ==0) or idx !=0

# V0'
# TODO : fix below (cyclic case)
# class Solution:
#     def isRobotBounded(self, instructions):
#         def get_dirc(degree):
#             if degree < 0:
#                 degree += 360
#             degree %= 360
#             if degree == 0:
#                 return 0, 1
#             elif degree == 90:
#                 return 1, 0
#             elif degree == 180:
#                 return 0, -1
#             else:
#                 return -1, 0
#         # edge case
#         if not instructions:
#             return True
#         cur = [0,0]
#         degree = 0
#         for i in instructions:
#             print ("i = " + str(i) + " degree = " + str(degree) + " cur = " + str(cur))
#             #y_dirc, x_dirc = get_dirc(degree)
#             if i == "G":
#                 cur[1] += x_dirc * 1
#                 cur[0] += y_dirc * 1
#             elif i == "L":
#                 degree -= 90
#                 y_dirc, x_dirc = get_dirc(degree)
#             elif i == "R":
#                 degree += 90
#                 y_dirc, x_dirc = get_dirc(degree)
#         return True if cur == [0,0] else False

# V1
# https://leetcode.com/problems/robot-bounded-in-circle/discuss/1425424/Java-%2B-Python-solution
class Solution:
    def isRobotBounded(self, instructions):
        dirs = [[0,1], [1,0], [0,-1], [-1,0]]
        x = 0;
        y = 0;
        idx = 0;
        for c in instructions:
            if c == 'L':
                idx = (idx + 3) % 4
            elif c == 'R':
                idx = (idx + 1) % 4
            elif c == 'G':
                x = x + dirs[idx][0]
                y = y + dirs[idx][1]
        return (x == 0 and y ==0) or idx !=0

# V1
# IDEA : one pass
# https://leetcode.com/problems/robot-bounded-in-circle/solution/
class Solution:
    def isRobotBounded(self, instructions: str) -> bool:
        # north = 0, east = 1, south = 2, west = 3
        directions = [[0, 1], [1, 0], [0, -1], [-1, 0]]
        # Initial position is in the center
        x = y = 0
        # facing north
        idx = 0
        
        for i in instructions:
            if i == "L":
                idx = (idx + 3) % 4
            elif i == "R":
                idx = (idx + 1) % 4
            else:
                x += directions[idx][0]
                y += directions[idx][1]
        
        # after one cycle:
        # robot returns into initial position
        # or robot doesn't face north
        return (x == 0 and y == 0) or idx != 0

# V1
# https://leetcode.com/problems/robot-bounded-in-circle/discuss/910396/Python-easy
class Solution:
    def isRobotBounded(self, instructions):
        print(instructions)
        x,y = 0,0
        # 0 90 180 270
        directions = [(1,0),(0,1),(-1,0),(0,-1)]
        dir = 0
        for j in range(4):
            for i in instructions:
                if i=="L":
                    dir = (dir+1)%4
                elif i=="R":
                    dir = (dir+3)%4
                else:
                    x+=directions[dir][0]
                    y+=directions[dir][1]
            if x==0 and y==0:
                return True
        return False

# V1'
# https://leetcode.com/problems/robot-bounded-in-circle/discuss/290870/Python
class Solution:
    def isRobotBounded(self, instructions):
        marked = set()
        directions = [[0,1], [1,0], [0,-1], [-1,0]]
        curr_direction = 0
        pos = (0,0)
        for i in range(5):
            if pos in marked:
                return True
            marked.add(pos)
            for i in instructions:
                if i == 'G':
                    pos = (pos[0] + directions[curr_direction][0], pos[1] + directions[curr_direction][1])
                elif i == 'L':
                    curr_direction = curr_direction - 1 if curr_direction else 3 
                else:
                    curr_direction = (curr_direction + 1)%4
        return False

# V2