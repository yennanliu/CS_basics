"""

120. Triangle
Medium

Given a triangle array, return the minimum path sum from top to bottom.

For each step, you may move to an adjacent number of the row below. More formally, if you are on index i on the current row, you may move to either index i or index i + 1 on the next row.

 

Example 1:

Input: triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
Output: 11
Explanation: The triangle looks like:
   2
  3 4
 6 5 7
4 1 8 3
The minimum path sum from top to bottom is 2 + 3 + 5 + 1 = 11 (underlined above).
Example 2:

Input: triangle = [[-10]]
Output: -10
 

Constraints:

1 <= triangle.length <= 200
triangle[0].length == 1
triangle[i].length == triangle[i - 1].length + 1
-104 <= triangle[i][j] <= 104
 

Follow up: Could you do this using only O(n) extra space, where n is the total number of rows in the triangle?

"""

# V0

# V1
# https://leetcode.com/problems/triangle/solution/
# IDEA :  Dynamic Programming (Bottom-up: In-place)
class Solution:
    def minimumTotal(self, triangle: List[List[int]]) -> int:
        for row in range(1, len(triangle)):
            for col in range(row + 1):
                smallest_above = math.inf
                if col > 0:
                    smallest_above = triangle[row - 1][col - 1]
                if col < row:
                    smallest_above = min(smallest_above, triangle[row - 1][col])
                triangle[row][col] += smallest_above
        return min(triangle[-1])

# V1'
# https://leetcode.com/problems/triangle/solution/
# IDEA : Dynamic Programming (Bottom-up: Auxiliary Space)
class Solution:
    def minimumTotal(self, triangle: List[List[int]]) -> int:
        prev_row = triangle[0]
        for row in range(1, len(triangle)):
            curr_row = []
            for col in range(row + 1):
                smallest_above = math.inf
                if col > 0:
                    smallest_above = prev_row[col - 1]
                if col < row:
                    smallest_above = min(smallest_above, prev_row[col])
                curr_row.append(triangle[row][col] + smallest_above)
            prev_row = curr_row
        return min(prev_row)

# V1''
# https://leetcode.com/problems/triangle/solution/
# IDEA : Dynamic Programming (Bottom-up: Flip Triangle Upside Down)
class Solution:
    def minimumTotal(self, triangle: List[List[int]]) -> int:
        below_row = triangle[-1] 
        n = len(triangle)
        for row in reversed(range(n - 1)):     
            curr_row = []
            for col in range(row + 1):
                smallest_below = min(below_row[col], below_row[col + 1])
                curr_row.append(triangle[row][col] + smallest_below)
            below_row = curr_row
        return below_row[0]

# V1'''
# https://leetcode.com/problems/triangle/solution/
# IDEA : Dynamic Programming (Bottom-up: Flip Triangle Upside Down)
class Solution:
    def minimumTotal(self, triangle: List[List[int]]) -> int:
        below_row = triangle[-1] 
        n = len(triangle)
        for row in reversed(range(n - 1)):       
            curr_row = []
            for col in range(row + 1):
                smallest_below = min(below_row[col], below_row[col + 1])
                curr_row.append(triangle[row][col] + smallest_below)
            below_row = curr_row
        return below_row[0]

# V1''''
# https://leetcode.com/problems/triangle/solution/
# IDEA : Memoization (Top-Down)
class Solution:
    def minimumTotal(self, triangle: List[List[int]]) -> int:
        @lru_cache(maxsize=None)
        def min_path(row, col):
            path = triangle[row][col]
            if row < len(triangle) - 1:
                path += min(min_path(row + 1, col), min_path(row + 1, col + 1))
            return path
        return min_path(0, 0)

# V1'''''
# https://blog.csdn.net/qqxx6661/article/details/78474268
# IDEA : DP 
# dp[j] = triangle[i][j] + min(dp[j], dp[j + 1])
class Solution(object):
    def minimumTotal(self, triangle):
        """
        :type triangle: List[List[int]]
        :rtype: int
        """
        n = len(triangle)
        dp = triangle[-1]
        for i in range(n - 2, -1, -1):
            for j in range(i + 1):  # start from the second last digit 
                dp[j] = triangle[i][j] + min(dp[j], dp[j + 1])
        print(dp)
        return dp[0]

# V1''''''
# https://blog.csdn.net/fuxuemingzhu/article/details/82883187
# IDEA : DP
# minpath[k][i] = min( minpath[k+1][i], minpath[k+1][i+1]) + triangle[k][i]
# For the kth level:
# minpath[i] = min( minpath[i], minpath[i+1]) + triangle[k][i];
class Solution(object):
    def minimumTotal(self, triangle):
        """
        :type triangle: List[List[int]]
        :rtype: int
        """
        n = len(triangle)
        dp = triangle[-1]
        for layer in range(n - 2, -1, -1):
            for i in range(layer + 1):
                dp[i] = min(dp[i], dp[i + 1]) + triangle[layer][i]
        return dp[0]

# V2
# Time:  O(m * n)
# Space: O(n)
from functools import reduce
class Solution(object):
    # @param triangle, a list of lists of integers
    # @return an integer
    def minimumTotal(self, triangle):
        if not triangle:
            return 0
        cur = triangle[0] + [float("inf")]
        for i in range(1, len(triangle)):
            next = []
            next.append(triangle[i][0] + cur[0])
            for j in range(1, i + 1):
                next.append(triangle[i][j] + min(cur[j - 1], cur[j]))
            cur = next + [float("inf")]
        return reduce(min, cur)