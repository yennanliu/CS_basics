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


# V0-1
# IDEA : INDEX-MAPPED COMPARISON (no rotated matrix is ever built)
#
#   instead of materialising each rotation, compare cell-by-cell through the
#   index map that a k-times-90-degrees-clockwise rotation induces :
#
#     k = 0 : target[i][j] == mat[i][j]
#     k = 1 : target[i][j] == mat[n-1-j][i]
#     k = 2 : target[i][j] == mat[n-1-i][n-1-j]
#     k = 3 : target[i][j] == mat[j][n-1-i]
#
#   all() short-circuits on the first mismatch, and nothing is allocated, so
#   this trades the O(n^2) scratch matrix of V0 for O(1) extra space.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def findRotation(self, mat, target):
        n = len(mat)
        maps = [
            lambda i, j: (i, j),
            lambda i, j: (n - 1 - j, i),
            lambda i, j: (n - 1 - i, n - 1 - j),
            lambda i, j: (j, n - 1 - i),
        ]

        for f in maps:
            if all(
                target[i][j] == mat[f(i, j)[0]][f(i, j)[1]]
                for i in range(n)
                for j in range(n)
            ):
                return True
        return False


# V0-2
# IDEA : CANONICAL FORM OF THE ROTATION ORBIT
#
#   "mat is reachable from target by rotation" is an equivalence relation :
#   both matrices sit in the same 4-element rotation orbit. So pick ONE
#   representative of an orbit - the lexicographically smallest of its 4
#   rotations, flattened to a tuple - and just compare representatives.
#
#   this needs no pairwise loop over the two inputs at all; it is the standard
#   canonicalisation trick that also lets you bucket many matrices by orbit
#   (e.g. group / dedup a whole list) in one pass each.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def findRotation(self, mat, target):
        def canonical(m):
            n = len(m)
            cur = m
            best = None
            for _ in range(4):
                flat = tuple(v for row in cur for v in row)
                if best is None or flat < best:
                    best = flat
                cur = [[cur[n - 1 - j][i] for j in range(n)] for i in range(n)]
            return best

        return canonical(mat) == canonical(target)
