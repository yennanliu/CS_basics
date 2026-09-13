"""

840. Magic Squares In Grid
Medium

A 3 x 3 magic square is a 3 x 3 grid filled with distinct numbers from 1 to 9 such that each row, column, and both diagonals all have the same sum.

Given a row x col grid of integers, how many 3 x 3 magic square subgrids are there?

Note: while a magic square can only contain numbers from 1 to 9, grid may contain numbers up to 15.

Example 1:

https://assets.leetcode.com/uploads/2020/09/11/magic_main.jpg

Input: grid = [[4,3,8,4],[9,5,1,9],[2,7,6,2]]
Output: 1
Explanation:
The following subgrid is a 3 x 3 magic square:
https://assets.leetcode.com/uploads/2020/09/11/magic_valid.jpg
while this one is not:
https://assets.leetcode.com/uploads/2020/09/11/magic_invalid.jpg
In total, there is only one magic square inside the given grid.

Example 2:

Input: grid = [[8]]
Output: 0

Constraints:

row == grid.length
col == grid[i].length
1 <= row, col <= 10
0 <= grid[i][j] <= 15

"""

# V0 

# V1 
# https://blog.csdn.net/Chip_Wan/article/details/81346936
# time = O(m * n)
# space = O(1)
class Solution:
    def numMagicSquaresInside(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        res,n=0,len(grid)
        isMag=['16729438','18349276','34927618','38167294','72943816','76183492','92761834','94381672']
        for i in range(1,n-1):
            for j in range(1,n-1):
                if grid[i][j]==5:
                    tmp=''.join(map(str,[grid[i-1][j],grid[i-1][j+1],grid[i][j+1],grid[i+1][j+1],grid[i+1][j],\
                                         grid[i+1][j-1],grid[i][j-1],grid[i-1][j-1]]))
                    res+=1 if tmp in isMag else 0
        return res
        
# V2
# time = O(m * n)
# space = O(1)
class Solution(object):
    def numMagicSquaresInside(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        def magic(grid, r, c):
            expect = k * (k**2+1) // 2
            nums = set()
            min_num = float("inf")
            sum_diag, sum_anti = 0, 0
            for i in range(k):
                sum_diag += grid[r+i][c+i]
                sum_anti += grid[r+i][c+k-1-i]
                sum_r, sum_c = 0, 0
                for j in range(k):
                    min_num = min(min_num, grid[r+i][c+j])
                    nums.add(grid[r+i][c+j])
                    sum_r += grid[r+i][c+j]
                    sum_c += grid[r+j][c+i]
                if not (sum_r == sum_c == expect):
                    return False
            return sum_diag == sum_anti == expect and \
                len(nums) == k**2 and \
                min_num == 1

        k = 3
        result = 0
        for r in range(len(grid)-k+1):
            for c in range(len(grid[r])-k+1):
                if magic(grid, r, c):
                    result += 1
        return result