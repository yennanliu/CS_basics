# Given a 2D grid, each cell is either a wall 'Y', an enemy 'X' or empty '0' (the number zero), return the maximum enemies you can kill using one bomb.
# The bomb kills all the enemies in the same row and column from the planted point until it hits the wall since the wall is too strong to be destroyed.
# Note that you can only put the bomb at an empty cell.
#
# Example:
#
# For the given grid
#
# 0 X 0 0
# X 0 Y X
# 0 X 0 0
#
# return 3. (Placing a bomb at (1,1) kills 3 enemies)

# V0 
### NOTE : IT'S LIKE "BOMBERMAN", SO THE BOMB CAN ONLY EXPLODE "STRAIGHT" LINE,
# -> BFS IN THIS PROBLEM MAY NOT WORKS
class Solution(object):
    def maxKilledEnemies(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        def count(x, y):
            left, right, up, down = 0, 0, 0, 0
            # left (<-)
            for i in range(x-1, -1, -1):
                if grid[i][y] == 'W':
                    break
                if grid[i][y] == 'E':
                    left += 1
            # right (->)    
            for i in range(x+1, row):
                if grid[i][y] == 'W':
                    break
                if grid[i][y] == 'E':
                    right += 1
            # up (↑)    
            for j in range(y+1, col):
                if grid[x][j] == 'W':
                    break
                if grid[x][j] == 'E':
                    up += 1
            # down (↓)
            for j in range(y-1, -1, -1):
                if grid[x][j] == 'W':
                    break
                if grid[x][j] == 'E':
                    down += 1
            return left+right+up+down
        # base case
        if not grid: return 0
        row, col = len(grid), len(grid[0])
        ans = 0
        for i in range(row):
            for j in range(col):
                if grid[i][j] == '0':
                    # count the num of enemies 
                    ans = max(ans, count(i, j))                 
        return ans

# V0'
# TODO : OPTIMIZATION : NOT TO SEARCH "visited" grid REPEATEDLY

# V1 
# https://www.twblogs.net/a/5c9baf03bd9eee73ef4b0e0d
class Solution(object):
    def maxKilledEnemies(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        def count(x, y):
            left, right, up, down = 0, 0, 0, 0
            for i in range(x-1, -1, -1):
                if grid[i][y] == 'W':
                    break
                if grid[i][y] == 'E':
                    left += 1
                    
            for i in range(x+1, row):
                if grid[i][y] == 'W':
                    break
                if grid[i][y] == 'E':
                    right += 1
                    
            for j in range(y+1, col):
                if grid[x][j] == 'W':
                    break
                if grid[x][j] == 'E':
                    up += 1
            
            for j in range(y-1, -1, -1):
                if grid[x][j] == 'W':
                    break
                if grid[x][j] == 'E':
                    down += 1
            return left+right+up+down
        # base case
        if not grid: return 0
        row, col = len(grid), len(grid[0])
        ans = 0
        for i in range(row):
            for j in range(col):
                if grid[i][j] == '0':
                    # count the num of enemies 
                    ans = max(ans, count(i, j))
                    
        return ans

### Test case : dev

# V1'
# https://www.twblogs.net/a/5c9baf03bd9eee73ef4b0e0d
class Solution(object):
    def maxKilledEnemies(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        # base case 
        if not grid: return 0
        row, col = len(grid), len(grid[0])
        ans = 0
        arr = [[0]*col for i in range(row)]
        # from left to right
        for i in range(row):
            count = 0
            for j in range(col):
                if grid[i][j] == 'E':
                    count += 1
                elif grid[i][j] == 'W':
                    count = 0
                else:
                    arr[i][j] += count
                    
        # from right to left
        for i in range(row):
            count = 0
            for j in range(col-1, -1, -1):
                if grid[i][j] == 'E':
                    count += 1
                elif grid[i][j] == 'W':
                    count = 0
                else:
                    arr[i][j] += count
        # from up to down
        for j in range(col):
            count = 0
            for i in range(row):
                if grid[i][j] == 'E':
                    count += 1
                elif grid[i][j] == 'W':
                    count = 0
                else:
                    arr[i][j] += count
                    
        # from down to up
        for j in range(col):
            count = 0
            for i in range(row-1, -1, -1):
                if grid[i][j] == 'E':
                    count += 1
                elif grid[i][j] == 'W':
                    count = 0
                else:
                    arr[i][j] += count
                    ans = max(ans, arr[i][j])
                    
        return ans

# V2 
# Time:  O(m * n)
# Space: O(m * n)
class Solution(object):
    def maxKilledEnemies(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        result = 0
        if not grid or not grid[0]:
            return result

        down = [[0 for _ in range(len(grid[0]))] for _ in range(len(grid))]
        right = [[0 for _ in range(len(grid[0]))] for _ in range(len(grid))]
        for i in reversed(range(len(grid))):
            for j in reversed(range(len(grid[0]))):
                if grid[i][j] != 'W':
                    if i + 1 < len(grid):
                        down[i][j] = down[i + 1][j]
                    if j + 1 < len(grid[0]):
                        right[i][j] = right[i][j + 1]
                    if grid[i][j] == 'E':
                        down[i][j] += 1
                        right[i][j] += 1

        up = [0 for _ in range(len(grid[0]))]
        for i in range(len(grid)):
            left = 0
            for j in range(len(grid[0])):
                if grid[i][j] == 'W':
                    up[j], left = 0, 0
                elif grid[i][j] == 'E':
                    up[j] += 1
                    left += 1
                else:
                    result = max(result,
                                 left + up[j] + right[i][j] + down[i][j])

        return result
