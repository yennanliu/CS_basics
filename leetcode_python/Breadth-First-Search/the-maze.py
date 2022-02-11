"""

[LeetCode] 490. The Maze

# https://www.cnblogs.com/grandyang/p/6381458.html
 

There is a ball in a maze with empty spaces and walls. The ball can go through empty spaces by rolling up, down, left or right, but it won't stop rolling until hitting a wall. When the ball stops, it could choose the next direction.

Given the ball's start position, the destination and the maze, determine whether the ball could stop at the destination.

The maze is represented by a binary 2D array. 1 means the wall and 0 means the empty space. You may assume that the borders of the maze are all walls. The start and destination coordinates are represented by row and column indexes.

Example 1

Input 1: a maze represented by a 2D array

0 0 1 0 0
0 0 0 0 0
0 0 0 1 0
1 1 0 1 1
0 0 0 0 0

Input 2: start coordinate (rowStart, colStart) = (0, 4)
Input 3: destination coordinate (rowDest, colDest) = (4, 4)

Output: true
Explanation: One possible way is : left -> down -> left -> down -> right -> down -> right.

 

Example 2

Input 1: a maze represented by a 2D array

0 0 1 0 0
0 0 0 0 0
0 0 0 1 0
1 1 0 1 1
0 0 0 0 0

Input 2: start coordinate (rowStart, colStart) = (0, 4)
Input 3: destination coordinate (rowDest, colDest) = (3, 2)

Output: false
Explanation: There is no way for the ball to stop at the destination.

 

Note:

There is only one ball and one destination in the maze.
Both the ball and the destination exist on an empty space, and they will not be at the same position initially.
The given maze does not contain border (like the red rectangle in the example pictures), but you could assume the border of the maze are all walls.
The maze contains at least 2 empty spaces, and both the width and height of the maze won't exceed 100.

"""

# V0
# IDEA : DFS 
class Solution(object):
    def hasPath(self, maze, start, destination):

        def dfs(x, y):
            # return if the ball can stop at destination if starting at (x, y)
            if [x,y] == destination:
                return True
            ### beware of this 
            if (x, y) in visited:
                return False
            visited.add((x, y))
            ### beware of this  : for -> while
            for dx, dy in [(-1,0),(1,0),(0,-1),(0,1)]:
                new_x, new_y = x, y
                # the ball rolls until hitting a wall
                while 0 <= new_x+dx < row and 0 <= new_y+dy < col and maze[new_x+dx][new_y+dy] == 0:  ### beware of this 
                    new_x += dx
                    new_y += dy
                if dfs(new_x, new_y):
                    ### beware of this 
                    return True
            return False
        
        row, col = len(maze), len(maze[0])
        visited = set()
        return dfs(start[0], start[1])

# V0'
# IDEA : BFS
class Solution:
    def hasPath(self, maze, start, destination):
        # write your code here
        Q = [start]
        n = len(maze)
        m = len(maze[0])
        dirs = ((0, 1), (0, -1), (1, 0), (-1, 0))
        while Q:
            i, j = Q.pop(0)
            maze[i][j] = 2
            if i == destination[0] and j == destination[1]:
                return True
            for x, y in dirs:
                row = i + x
                col = j + y
                ### NOTICE : THE OPTIMIZATION HERE
                while 0 <= row < n and 0 <= col < m and maze[row][col] != 1:
                    row += x
                    col += y
                row -= x
                col -= y
                if maze[row][col] == 0:
                    Q.append([row, col])
                    
        return False

# V1 
# https://blog.csdn.net/danspace1/article/details/88773383
# IDEA : DFS 
class Solution(object):
    def hasPath(self, maze, start, destination):
        """
        :type maze: List[List[int]]
        :type start: List[int]
        :type destination: List[int]
        :rtype: bool
        """
        def dfs(x, y):
            # return if the ball can stop at destination if starting at (x, y)
            if [x,y] == destination:
                return True
            if (x, y) in visited:
                return False
            visited.add((x, y))
            for dx, dy in [(-1,0),(1,0),(0,-1),(0,1)]:
                new_x, new_y = x, y
                # the ball rolls until hitting a wall
                while 0 <= new_x+dx < row and 0 <= new_y+dy < col and maze[new_x+dx][new_y+dy] == 0:
                    new_x += dx
                    new_y += dy
                if dfs(new_x, new_y):
                    return True
            return False
        
        row, col = len(maze), len(maze[0])
        visited = set()
        return dfs(start[0], start[1])

### TEST CASE : dev

# V1'
# https://www.jiuzhang.com/solution/the-maze/#tag-highlight-lang-python
# IDEA : BFS 
class Solution:
    """
    @param maze: the maze
    @param start: the start
    @param destination: the destination
    @return: whether the ball could stop at the destination
    """
    def hasPath(self, maze, start, destination):
        # write your code here
        Q = [start]
        n = len(maze)
        m = len(maze[0])
        dirs = ((0, 1), (0, -1), (1, 0), (-1, 0))
        while Q:
            i, j = Q.pop(0)
            maze[i][j] = 2
            if i == destination[0] and j == destination[1]:
                return True
            for x, y in dirs:
                row = i + x
                col = j + y
                ### NOTICE : THE OPTIMIZATION HERE
                while 0 <= row < n and 0 <= col < m and maze[row][col] != 1:
                    row += x
                    col += y
                row -= x
                col -= y
                if maze[row][col] == 0:
                    Q.append([row, col])

        return False
        
# V2 
# Time:  O(max(r, c) * w)
# Space: O(w)
import collections
class Solution(object):
    def hasPath(self, maze, start, destination):
        """
        :type maze: List[List[int]]
        :type start: List[int]
        :type destination: List[int]
        :rtype: bool
        """
        def neighbors(maze, node):
            for i, j in [(-1, 0), (0, 1), (0, -1), (1, 0)]:
                x, y = node
                while 0 <= x + i < len(maze) and \
                      0 <= y + j < len(maze[0]) and \
                      not maze[x+i][y+j]:
                    x += i
                    y += j
                yield x, y

        start, destination = tuple(start), tuple(destination)
        queue = collections.deque([start])
        visited = set()
        while queue:
            node = queue.popleft()
            if node in visited: continue
            if node == destination:
                return True
            visited.add(node)
            for neighbor in neighbors(maze, node):
                queue.append(neighbor)

        return False