"""

1840. Maximum Building Height
Hard

You want to build n new buildings in a city. The new buildings will be built in a line and are labeled from 1 to n.

However, there are city restrictions on the heights of the new buildings:

The height of each building must be a non-negative integer.
The height of the first building must be 0.
The height difference between any two adjacent buildings cannot exceed 1.

Additionally, there are city restrictions on the maximum height of specific buildings. These restrictions are given as a 2D integer array restrictions where restrictions[i] = [idi, maxHeighti] indicates that building idi must have a height less than or equal to maxHeighti.

It is guaranteed that each building will appear at most once in restrictions, and building 1 will not be in restrictions.

Return the maximum possible height of the tallest building.


Example 1:

Input: n = 5, restrictions = [[2,1],[4,1]]
Output: 2
Explanation: The green area in the image indicates the maximum allowed height for each building.
We can build the buildings with heights [0,1,2,1,2], and the tallest building has a height of 2.

Example 2:

Input: n = 6, restrictions = []
Output: 5
Explanation: The green area in the image indicates the maximum allowed height for each building.
We can build the buildings with heights [0,1,2,3,4,5], and the tallest building has a height of 5.

Example 3:

Input: n = 10, restrictions = [[5,3],[2,5],[7,4],[10,3]]
Output: 5
Explanation: The green area in the image indicates the maximum allowed height for each building.
We can build the buildings with heights [0,1,2,3,3,4,4,5,4,3], and the tallest building has a height of 5.


Constraints:

2 <= n <= 10^9
0 <= restrictions.length <= min(n - 1, 10^5)
2 <= idi <= n
idi is unique.
0 <= maxHeighti <= 10^9

"""

# V0
# IDEA : TIGHTEN THE RESTRICTIONS BOTH WAYS, THEN PEAK BETWEEN NEIGHBOURS
#
#   n is up to 10^9 so we can only reason about the O(10^5) restricted points.
#   add the implicit restriction [1, 0] (building 1 has height 0) and sort.
#
#   1) left -> right sweep : h[i] = min(h[i], h[i-1] + (id[i] - id[i-1]))
#      2) right -> left sweep : h[i] = min(h[i], h[i+1] + (id[i+1] - id[i]))
#   after both passes every recorded height is simultaneously reachable -- the
#   |diff| <= 1 rule can no longer be violated between consecutive markers.
#
#   3) between two adjacent markers (i, hi) and (j, hj) the profile can climb
#      from both ends and meet in the middle, so the peak there is
#        (hi + hj + (j - i)) // 2
#      take the max over all gaps.
#
#   NOTE : if building n is not restricted, append [n, n - 1] -- an unrestricted
#          tail can still rise by 1 per step, and that bound is never binding.
#
# time = O(m log m), m = len(restrictions)
# space = O(m)
class Solution(object):
    def maxBuilding(self, n, restrictions):
        r = [[a, b] for a, b in restrictions]
        r.append([1, 0])
        r.sort()
        if r[-1][0] != n:
            r.append([n, n - 1])

        m = len(r)
        for i in range(1, m):
            r[i][1] = min(r[i][1], r[i - 1][1] + r[i][0] - r[i - 1][0])
        for i in range(m - 2, -1, -1):
            r[i][1] = min(r[i][1], r[i + 1][1] + r[i + 1][0] - r[i][0])

        res = 0
        for i in range(m - 1):
            peak = (r[i][1] + r[i + 1][1] + r[i + 1][0] - r[i][0]) // 2
            if peak > res:
                res = peak
        return res
