"""

2061. Number of Spaces Cleaning Robot Cleaned
Medium

A room is represented by a 0-indexed 2D binary matrix room where a 0 represents an empty space and a 1 represents a space with an object. The top left corner of the room will be empty in all test cases.

A cleaning robot starts at the top left corner of the room and is facing right. The robot will continue heading straight until it reaches the edge of the room or it hits an object, after which it will turn 90 degrees clockwise and repeat this process. The starting space and all spaces that the robot visits are cleaned by it.

Return the number of clean spaces in the room if the robot runs indefinitely.


Example 1:

Input: room = [[0,0,0],[1,1,0],[0,0,0]]
Output: 7
Explanation:
1. The robot cleans the spaces at (0, 0), (0, 1), and (0, 2).
2. The robot is at the edge of the room, so it turns 90 degrees clockwise and now faces down.
3. The robot cleans the spaces at (1, 2), and (2, 2).
4. The robot is at the edge of the room, so it turns 90 degrees clockwise and now faces left.
5. The robot cleans the spaces at (2, 1), and (2, 0).
6. The robot has cleaned all 7 empty spaces, so return 7.

Example 2:

Input: room = [[0,1,0],[1,0,0],[0,0,0]]
Output: 1
Explanation:
1. The robot cleans the space at (0, 0).
2. The robot hits an object, so it turns 90 degrees clockwise and now faces down.
3. The robot hits an object, so it turns 90 degrees clockwise and now faces left.
4. The robot is at the edge of the room, so it turns 90 degrees clockwise and now faces up.
5. The robot is at the edge of the room, so it turns 90 degrees clockwise and now faces right.
6. The robot is back at its starting position.
7. The robot has cleaned 1 space, so return 1.

Example 3:

Input: room = [[0,0,0],[0,0,0],[0,0,0]]
Output: 8


Constraints:

m == room.length
n == room[r].length
1 <= m, n <= 300
room[r][c] is either 0 or 1.
room[0][0] == 0

"""

# V0
# IDEA : SIMULATION UNTIL A (cell, direction) STATE REPEATS
#
#   the robot is fully determined by (row, col, facing); there are only
#   m * n * 4 such states, so once a state repeats the walk is periodic and
#   nothing new will ever be cleaned -> stop.
#
#   dirs is the flattened clockwise cycle right / down / left / up, so
#   turning is just k = (k + 1) % 4.
#
#   NOTE : mark a cleaned cell with -1 (not 1) so it is still walkable but
#          is not counted twice.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def numberOfCleanRooms(self, room):
        m, n = len(room), len(room[0])
        dirs = (0, 1, 0, -1, 0)
        i = j = k = 0
        res = 0
        seen = set()
        while (i, j, k) not in seen:
            seen.add((i, j, k))
            if room[i][j] == 0:
                res += 1
                room[i][j] = -1
            x, y = i + dirs[k], j + dirs[k + 1]
            if 0 <= x < m and 0 <= y < n and room[x][y] != 1:
                i, j = x, y
            else:
                k = (k + 1) % 4
        return res
