"""

1691. Maximum Height by Stacking Cuboids
Hard

Given n cuboids where the dimensions of the ith cuboid is cuboids[i] = [widthi, lengthi, heighti]
(0-indexed). Choose a subset of cuboids and place them on each other.

You can place cuboid i on cuboid j if widthi <= widthj and lengthi <= lengthj and heighti <= heightj.
You can rearrange any cuboid's dimensions by rotating it to put it on another cuboid.

Return the maximum height of the stacked cuboids.


Example 1:

Input: cuboids = [[50,45,20],[95,37,53],[45,23,12]]
Output: 190
Explanation:
Cuboid 1 is placed on the bottom with the 53x37 side facing down with height 95.
Cuboid 0 is placed next with the 45x20 side facing down with height 50.
Cuboid 2 is placed next with the 23x12 side facing down with height 45.
The total height is 95 + 50 + 45 = 190.

Example 2:

Input: cuboids = [[38,25,45],[76,35,3]]
Output: 76
Explanation:
You can't place any of the cuboids on the other.
We choose cuboid 1 and rotate it so that the 35x3 side is facing down and its height is 76.

Example 3:

Input: cuboids = [[7,11,17],[7,17,11],[11,7,17],[11,17,7],[17,7,11],[17,11,7]]
Output: 102
Explanation:
After rearranging the cuboids, you can see that all cuboids have the same dimension.
You can place the 11x7 side down on all cuboids so their heights are 17.
The maximum height of stacked cuboids is 6 * 17 = 102.


Constraints:

n == cuboids.length
1 <= n <= 100
1 <= widthi, lengthi, heighti <= 100

"""

# V0
# IDEA : SORT EACH CUBOID + LIS-STYLE DP (rotation collapses to "sort the dims")
#
#   KEY CLAIM : in an optimal stack we may assume every cuboid is oriented with
#   its dimensions sorted ascending (a <= b <= c) and c used as the height.
#   reason : if cuboid X sits on cuboid Y at all, then X's sorted triple is
#   componentwise <= Y's sorted triple; so re-orienting everything to "sorted"
#   keeps the stack legal and can only raise the total height (c is the largest).
#
#   after that the problem is a 3-D chain / longest increasing subsequence:
#     sort all cuboids lexicographically, then
#     dp[i] = c_i + max{ dp[j] : j < i and b_j <= b_i and c_j <= c_i }
#   (a_j <= a_i already holds from the lexicographic sort)
#
# time = O(n^2), space = O(n)
class Solution(object):
    def maxHeight(self, cuboids):
        cubes = sorted(sorted(c) for c in cuboids)
        n = len(cubes)
        dp = [0] * n
        best = 0
        for i in range(n):
            ai, bi, ci = cubes[i]
            cur = 0
            for j in range(i):
                if cubes[j][1] <= bi and cubes[j][2] <= ci and dp[j] > cur:
                    cur = dp[j]
            dp[i] = cur + ci
            if dp[i] > best:
                best = dp[i]
        return best
