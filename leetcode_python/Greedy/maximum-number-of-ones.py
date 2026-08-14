"""

1183. Maximum Number of Ones
Hard

Consider a matrix M with dimensions width * height, such that every cell has value 0 or 1,
and any square sub-matrix of M of size sideLength * sideLength has at most maxOnes ones.

Return the maximum possible number of ones that the matrix M can have.


Example 1:

Input: width = 3, height = 3, sideLength = 2, maxOnes = 1
Output: 4
Explanation:
In a 3*3 matrix, no 2*2 sub-matrix can have more than 1 one.
The best solution that has 4 ones is:
[1,0,1]
[0,0,0]
[1,0,1]

Example 2:

Input: width = 3, height = 3, sideLength = 2, maxOnes = 2
Output: 6
Explanation:
[1,0,1]
[1,0,1]
[1,0,1]


Constraints:

1 <= width, height <= 100
1 <= sideLength <= width, height
0 <= maxOnes <= sideLength * sideLength

"""

# V0
# IDEA : GREEDY over residue classes (the pattern must be periodic mod sideLength)
#
#   key observation : cells (i, j) and (i + k, j) are indistinguishable to the
#   sliding sideLength x sideLength window, so the whole matrix is forced to be
#   periodic with period `sideLength` in BOTH directions.
#   -> every cell belongs to a residue class (i % L, j % L), and the class is
#      either all-ones or all-zeros.
#
#   each window contains exactly one cell of each of the L*L classes, so we may
#   switch on at most `maxOnes` classes.
#   greedy : count how many real cells each class owns and turn on the
#   `maxOnes` most populated classes.
#   NOTE : that count is just how many times the residue repeats inside
#          width x height -- computed here by a direct O(width*height) sweep.
#
# time = O(width * height + L^2 log L), space = O(L^2)   L = sideLength
class Solution(object):
    def maximumNumberOfOnes(self, width, height, sideLength, maxOnes):
        L = sideLength
        cnt = [0] * (L * L)
        for i in range(width):
            for j in range(height):
                cnt[(i % L) * L + (j % L)] += 1
        cnt.sort(reverse=True)
        return sum(cnt[:maxOnes])
