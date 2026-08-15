"""

3027. Find the Number of Ways to Place People II
Hard

You are given a 2D array points of size n x 2 representing integer coordinates of some points on a 2D-plane, where points[i] = [xi, yi].

We define the right direction as positive x-axis (increasing x-coordinate) and the left direction as negative x-axis (decreasing x-coordinate). Similarly, we define the up direction as positive y-axis (increasing y-coordinate) and the down direction as negative y-axis (decreasing y-coordinate).

You have to place n people, including Alice and Bob, at these points such that there is exactly one person at every point. Alice wants to be alone with Bob, so Alice will build a rectangular fence with Alice's position as the upper left corner and Bob's position as the lower right corner of the fence (Note that the fence might not enclose any area, i.e. it can be a line). If any person other than Alice and Bob is either inside the fence or on the fence, Alice will be sad.

Return the number of pairs of points where you can place Alice and Bob, such that Alice does not become sad on building the fence.

Note that Alice can only build a fence with Alice's position as the upper left corner, and Bob's position as the lower right corner. For example, Alice at (1, 1) and Bob at (2, 0) forms a valid fence. Alice at (1, 1) and Bob at (1, 0) also forms a valid fence but Alice at (1, 1) and Bob at (0, 0) does not form a valid fence because Alice's position is not the upper left corner.


Example 1:

Input: points = [[1,1],[2,2],[3,3]]
Output: 0
Explanation: There is no way to place Alice and Bob such that Alice can build a fence with Alice's position as the upper left corner and Bob's position as the lower right corner. Hence we return 0.

Example 2:

Input: points = [[6,2],[4,4],[2,6]]
Output: 2
Explanation: There are two ways to place Alice and Bob such that Alice will not be sad:
- Place Alice at (4, 4) and Bob at (6, 2).
- Place Alice at (2, 6) and Bob at (4, 4).
You cannot place Alice at (2, 6) and Bob at (6, 2) because the person at (4, 4) will be inside the fence.

Example 3:

Input: points = [[3,1],[1,3],[1,1]]
Output: 2
Explanation: There are two ways to place Alice and Bob such that Alice will not be sad:
- Place Alice at (1, 1) and Bob at (3, 1).
- Place Alice at (1, 3) and Bob at (1, 1).
You cannot place Alice at (1, 3) and Bob at (3, 1) because the person at (1, 1) will be on the fence.
Note that it does not matter if the fence encloses any area, the first and second fences in the image are valid.


Constraints:

2 <= n <= 1000
points[i].length == 2
-10^9 <= points[i][0], points[i][1] <= 10^9
All points[i] are distinct.

"""

# V0
# IDEA : SAME SWEEP AS LC 3025 — IT WAS ALREADY O(n^2)
#
#   sort by (x ascending, y descending) so every candidate Bob follows its
#   Alice in the order. then for each Alice i, scan forward tracking the
#   highest y accepted so far :
#
#       point j is a valid Bob  <=>  y[j] <= y[i]  and  y[j] > best
#
#   because anything with a y strictly between `best` and y[i] would land
#   inside the fence.
#
#   the only thing this sequel changes is the scale — n grows to 1000 and the
#   coordinates to +-10^9 — so the O(n^3) "check every third point" version
#   is out, while this sweep still runs in 10^6 steps.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def numberOfPairs(self, points):
        pts = sorted(points, key=lambda p: (p[0], -p[1]))
        n = len(pts)
        res = 0
        for i in range(n):
            best = float('-inf')
            for j in range(i + 1, n):
                if pts[j][1] <= pts[i][1] and pts[j][1] > best:
                    res += 1
                    best = pts[j][1]
        return res
