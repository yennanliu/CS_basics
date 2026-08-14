"""

1886. Determine Whether Matrix Can Be Obtained By Rotation
Easy

Given two n x n binary matrices mat and target, return true if it is possible to make mat equal to target by rotating mat in 90-degree increments, or false otherwise.


Example 1:

Input: mat = [[0,1],[1,0]], target = [[1,0],[0,1]]
Output: true
Explanation: We can rotate mat 90 degrees clockwise to make mat equal target.

Example 2:

Input: mat = [[0,1],[1,1]], target = [[1,0],[0,1]]
Output: false
Explanation: It is impossible to make mat equal to target by rotating mat.

Example 3:

Input: mat = [[0,0,0],[0,1,0],[1,1,1]], target = [[1,1,1],[0,1,0],[0,0,0]]
Output: true
Explanation: We can rotate mat 90 degrees clockwise two times to make mat equal target.


Constraints:

n == mat.length == target.length
n == mat[i].length == target[i].length
1 <= n <= 10
mat[i][j] and target[i][j] are either 0 or 1.

"""

# V0
# IDEA : BRUTE FORCE OVER THE 4 ROTATIONS (a 4th rotation is the identity)
#
#   rotating 90 degrees clockwise maps (i, j) -> (j, n-1-i), i.e.
#     new[i][j] = old[n-1-j][i]
#   applying it four times returns the original matrix, so only 0/90/180/270
#   are distinct candidates.
#
#   rotate, compare, repeat - n <= 10 so 4 * n^2 = 400 cell reads at most.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def findRotation(self, mat, target):
        n = len(mat)
        cur = mat

        for _ in range(4):
            if cur == target:
                return True
            # rotate 90 degrees clockwise
            cur = [[cur[n - 1 - j][i] for j in range(n)] for i in range(n)]

        return False
