"""

2267. Check if There Is a Valid Parentheses String Path
Hard

A parentheses string is a non-empty string consisting only of '(' and ')'. It is valid if any of the following conditions is true:

It is ().
It can be written as AB (A concatenated with B), where A and B are valid parentheses strings.
It can be written as (A), where A is a valid parentheses string.

You are given an m x n matrix of parentheses grid. A valid parentheses string path in the grid is a path satisfying all of the following conditions:

The path starts from the upper left cell (0, 0).
The path ends at the bottom-right cell (m - 1, n - 1).
The path only ever moves down or right.
The resulting parentheses string formed by the path is valid.

Return true if there exists a valid parentheses string path in the grid. Otherwise, return false.


Example 1:

Input: grid = [["(","(","("],[")","(",")"],["(","(",")"],["(","(",")"]]
Output: true
Explanation: The above diagram shows two possible paths that form valid parentheses strings.
The first path shown results in the valid parentheses string "()(())".
The second path shown results in the valid parentheses string "((()))".
Note that there may be other valid parentheses string paths.

Example 2:

Input: grid = [[")",")"],["(","("]]
Output: false
Explanation: The two possible paths form the parentheses strings "))(" and ")((". Since neither of them are valid parentheses strings, we return false.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 100
grid[i][j] is either '(' or ')'.

"""

# V0
# IDEA : DP OVER REACHABLE BALANCES, kept as a BITSET (one python int per cell)
#
#   state = (cell, running balance = #'(' - #')'). a path is valid iff the
#   balance never goes negative and ends at 0.
#
#   dp[i][j] is an integer whose bit b is set when balance b is reachable at
#   (i, j). moving into a cell:
#     '(' -> shift the incoming mask LEFT by 1  (every balance +1)
#     ')' -> shift the incoming mask RIGHT by 1 (every balance -1, bit 0 falls
#            off, which is exactly the "balance went negative" pruning)
#
#   answer = bit 0 of dp[m-1][n-1].
#
#   NOTE : path length is m+n-1, so an odd m+n-1 can never balance -> early
#          exit; likewise grid[0][0] must be '(' and the last cell ')'.
#
# time = O(m * n * (m + n) / 64) big-int word ops, space = O(n * (m + n) / 64)
class Solution(object):
    def hasValidPath(self, grid):
        m, n = len(grid), len(grid[0])
        if (m + n - 1) % 2 == 1:
            return False
        if grid[0][0] == ')' or grid[m - 1][n - 1] == '(':
            return False

        # dp for the current row; dp[j] = bitset of reachable balances
        dp = [0] * n
        for i in range(m):
            new = [0] * n
            for j in range(n):
                if i == 0 and j == 0:
                    incoming = 1          # balance 0 before entering (0,0)
                else:
                    incoming = 0
                    if i > 0:
                        incoming |= dp[j]
                    if j > 0:
                        incoming |= new[j - 1]
                if incoming == 0:
                    continue
                if grid[i][j] == '(':
                    new[j] = incoming << 1
                else:
                    new[j] = incoming >> 1
            dp = new

        return (dp[n - 1] & 1) == 1
