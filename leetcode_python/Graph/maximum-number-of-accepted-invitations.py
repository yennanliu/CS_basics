"""

1820. Maximum Number of Accepted Invitations
Medium

There are m boys and n girls in a class attending an upcoming party.

You are given an m x n integer matrix grid, where grid[i][j] equals 0 or 1. If grid[i][j] == 1, then that means the ith boy can invite the jth girl to the party. A boy can invite at most one girl, and a girl can accept at most one invitation from a boy.

Return the maximum possible number of accepted invitations.


Example 1:

Input: grid = [[1,1,1],
               [1,0,1],
               [0,0,1]]
Output: 3
Explanation: The invitations are sent as follows:
- The 1st boy invites the 2nd girl.
- The 2nd boy invites the 1st girl.
- The 3rd boy invites the 3rd girl.

Example 2:

Input: grid = [[1,0,1,0],
               [1,0,0,0],
               [0,0,1,0],
               [1,1,1,0]]
Output: 3
Explanation: The invitations are sent as follows:
- The 1st boy invites the 3rd girl.
- The 2nd boy invites the 1st girl.
- The 3rd boy invites no one.
- The 4th boy invites the 2nd girl.


Constraints:

grid.length == m
grid[i].length == n
1 <= m, n <= 200
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : MAXIMUM BIPARTITE MATCHING (Hungarian / Kuhn's augmenting path)
#
#   boys on the left, girls on the right, an edge where grid[i][j] == 1.
#   "each boy at most one girl, each girl at most one boy" is exactly a
#   matching, and we want the maximum one.
#
#   Kuhn's algorithm : process the boys one by one and try to find an
#   AUGMENTING PATH for boy i:
#     - try each girl j he can invite that is not yet visited in THIS attempt
#     - if j is free, or the boy currently holding j can be re-seated
#       elsewhere (recursive try), then take j.
#   every successful attempt grows the matching by exactly 1.
#
#   NOTE : `vis` must be reset for every boy -- it guards one search, not the
#          whole run.
#
# time = O(m * n * m) worst case (V * E), space = O(m + n)
class Solution(object):
    def maximumInvitations(self, grid):
        m, n = len(grid), len(grid[0])
        match = [-1] * n   # match[j] = boy currently paired with girl j

        def try_seat(i, vis):
            for j in range(n):
                if grid[i][j] and j not in vis:
                    vis.add(j)
                    if match[j] == -1 or try_seat(match[j], vis):
                        match[j] = i
                        return True
            return False

        res = 0
        for i in range(m):
            if try_seat(i, set()):
                res += 1
        return res
