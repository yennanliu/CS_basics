"""

489. Robot Room Cleaner
Hard

You are controlling a robot that is located somewhere in a room. The room is modeled as an m x n binary grid where 0 represents a wall and 1 represents an empty slot.

The robot starts at an unknown location in the room that is guaranteed to be empty, and you do not have access to the grid, but you can move the robot using the given API Robot.

You are tasked to use the robot to clean the entire room (i.e., clean every empty cell in the room). The robot with the four given APIs can move forward, turn left, or turn right. Each turn is 90 degrees.

When the robot tries to move into a wall cell, its bumper sensor detects the obstacle, and it stays on the current cell.

Design an algorithm to clean the entire room using the following APIs:

interface Robot {
  // returns true if next cell is open and robot moves into the cell.
  // returns false if next cell is obstacle and robot stays on the current cell.
  boolean move();

  // Robot will stay on the same cell after calling turnLeft/turnRight.
  // Each turn will be 90 degrees.
  void turnLeft();
  void turnRight();

  // Clean the current cell.
  void clean();
}
Note that the initial direction of the robot will be facing up. You can assume all four edges of the grid are all surrounded by a wall.

 

Custom testing:

The input is only given to initialize the room and the robot's position internally. You must solve this problem "blindfolded". In other words, you must control the robot using only the four mentioned APIs without knowing the room layout and the initial robot's position.

 

Example 1:


Input: room = [[1,1,1,1,1,0,1,1],[1,1,1,1,1,0,1,1],[1,0,1,1,1,1,1,1],[0,0,0,1,0,0,0,0],[1,1,1,1,1,1,1,1]], row = 1, col = 3
Output: Robot cleaned all rooms.
Explanation: All grids in the room are marked by either 0 or 1.
0 means the cell is blocked, while 1 means the cell is accessible.
The robot initially starts at the position of row=1, col=3.
From the top left corner, its position is one row below and three columns right.
Example 2:

Input: room = [[1]], row = 0, col = 0
Output: Robot cleaned all rooms.
 

Constraints:

m == room.length
n == room[i].length
1 <= m <= 100
1 <= n <= 200
room[i][j] is either 0 or 1.
0 <= row < m
0 <= col < n
room[row][col] == 1
All the empty cells can be visited from the starting position.

"""

# V0

# V1
# IDEA : BACKTRACKING
# https://leetcode.com/problems/robot-room-cleaner/solutions/265763/robot-room-cleaner/
class Solution:       
    def cleanRoom(self, robot):
        """
        :type robot: Robot
        :rtype: None
        """
        def go_back():
            robot.turnRight()
            robot.turnRight()
            robot.move()
            robot.turnRight()
            robot.turnRight()
            
        def backtrack(cell = (0, 0), d = 0):
            visited.add(cell)
            robot.clean()
            # going clockwise : 0: 'up', 1: 'right', 2: 'down', 3: 'left'
            for i in range(4):
                new_d = (d + i) % 4
                new_cell = (cell[0] + directions[new_d][0], \
                            cell[1] + directions[new_d][1])
                
                if not new_cell in visited and robot.move():
                    backtrack(new_cell, new_d)
                    go_back()
                # turn the robot following chosen direction : clockwise
                robot.turnRight()
    
        # going clockwise : 0: 'up', 1: 'right', 2: 'down', 3: 'left'
        directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
        visited = set()
        backtrack()

# V1'
# IDEA : DFS
# https://leetcode.com/problems/robot-room-cleaner/solutions/462887/python-dfs-solution/
class Solution:
    def cleanRoom(self, robot):
        """
        :type robot: Robot
        :rtype: None
        """
        
        visited = set()
        
        #Clockwise
        directions = ((-1,0), #up
                      (0,1),  #right
                      (1,0),  #down
                      (0,-1)) #left
        
        def explore(cell = (0, 0), _dir = 0):
            """explore "cell", starting at "_dir" direction and then turning clockwise"""
            
            visited.add(cell)
            robot.clean()
            
            for d in range(4):
                new_dir = (_dir + d) % 4
                new_cell = cell[0] + directions[new_dir][0], \
                           cell[1] + directions[new_dir][1]
                
                if new_cell not in visited and robot.move():
                    explore(new_cell, new_dir)
                    go_back() #retreat to "cell" and face same direction when you started "explor"ing
                    
                robot.turnRight()
                
        def go_back():
            """Rotate 180 degree, move to previous cell and then Rotate 180 degree to face old direction"""
            
            robot.turnRight()
            robot.turnRight()
            robot.move()
            robot.turnRight()
            robot.turnRight()
        
        explore()

# V1''
# IDEA : BACKTRACKING
# https://leetcode.com/problems/robot-room-cleaner/solutions/217835/python-backtracking/
class Solution(object):
    def cleanRoom(self, robot):
        """
        :type robot: Robot
        :rtype: None
        """
        def bt(x, y, ori):
            # ori: {0: up, 90: right, 180: down, 270: left}
            pos = "{}#{}".format(x, y)
            if pos in visited:
                return
            robot.clean()
            visited.add(pos)
            for _ in range(4):
                if robot.move():
                    i, j = x, y
                    if ori == 0:
                        i -= 1
                    elif ori == 90:
                        j += 1
                    elif ori == 180:
                        i += 1
                    elif ori == 270:
                        j -= 1
                    bt(i, j, ori)
                    robot.turnLeft()
                    robot.turnLeft()
                    robot.move()
                    robot.turnRight()
                    robot.turnRight()

                robot.turnRight()
                ori = (ori + 90) % 360
        
        visited = set()
        bt(0, 0, 0)

# V1'''
# IDEA : BACKTRCKING
# https://leetcode.com/problems/robot-room-cleaner/solutions/213456/python-solution/
class Solution(object):
    def cleanRoom(self, robot):
        """
        :type robot: Robot
        :rtype: None
        """
        def dfs(y, x, dy, dx):
            robot.clean()
            dirs = {(1,0), (0,1), (-1,0), (0,-1)}
            for df in dirs:
                yf, xf = y+df[0], x+df[1]
                if (yf, xf) not in seen:
                    seen.add((yf, xf))
                    # move in the same direction as dy, dx
                    if df[0] == dy and df[1] == dx:
                        if robot.move():
                            dfs(yf, xf, df[0], df[1])
                            robot.turnLeft()
                            robot.turnLeft()
                            robot.move()
                            robot.turnLeft()
                            robot.turnLeft()
                    # move in the direction opposite to dy, dx
                    elif df[0] == -dy and df[1] == -dx:
                        robot.turnLeft()
                        robot.turnLeft()
                        if robot.move():
                            dfs(yf, xf, df[0], df[1])
                            robot.turnLeft()
                            robot.turnLeft()
                            robot.move()
                        else:
                            robot.turnLeft()
                            robot.turnLeft()
                    # rotate dy, dx to the right by 90 degrees and move
                    elif df[0]*dx - df[1]*dy == -1:
                        robot.turnRight()
                        if robot.move():
                            dfs(yf, xf, df[0], df[1])
                            robot.turnLeft()
                            robot.turnLeft()
                            robot.move()
                            robot.turnRight()
                        else:
                            robot.turnLeft()
                    # rotate dy, dx to the left by 90 degrees and move
                    else:
                        robot.turnLeft()
                        if robot.move():
                            dfs(yf, xf, df[0], df[1])  
                            robot.turnLeft()
                            robot.turnLeft()
                            robot.move()
                            robot.turnLeft()
                        else:
                            robot.turnRight()
                            
        seen = set([(0,0)])               
        dfs(0, 0, 1, 0)

# V2