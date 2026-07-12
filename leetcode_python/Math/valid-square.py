"""

593. Valid Square
Solved
Medium
Topics
premium lock icon
Companies
Given the coordinates of four points in 2D space p1, p2, p3 and p4, return true if the four points construct a square.

The coordinate of a point pi is represented as [xi, yi]. The input is not given in any order.

A valid square has four equal sides with positive length and four equal angles (90-degree angles).

 

Example 1:

Input: p1 = [0,0], p2 = [1,1], p3 = [1,0], p4 = [0,1]
Output: true
Example 2:

Input: p1 = [0,0], p2 = [1,1], p3 = [1,0], p4 = [0,12]
Output: false
Example 3:

Input: p1 = [1,0], p2 = [-1,0], p3 = [0,1], p4 = [0,-1]
Output: true
 

Constraints:

p1.length == p2.length == p3.length == p4.length == 2
-104 <= xi, yi <= 104
 


"""

# V0
class Solution(object):
    def validSquare(self, p1, p2, p3, p4):
        """
        :type p1: List[int]
        :type p2: List[int]
        :type p3: List[int]
        :type p4: List[int]
        :rtype: bool
        """
        pass


# V1-1
# IDEA: MATH (gpt)
"""
NOTE !!!

how to check a `valid` squre?


->

A valid square has:

4 equal sides
2 equal diagonals
diagonal > side
side > 0

There are exactly 6 distances among 4 points.


"""
class Solution(object):
    def validSquare(self, p1, p2, p3, p4):
        points = [p1, p2, p3, p4]

        dists = []

        for i in range(4):
            for j in range(i + 1, 4):
                dists.append(self.get_len(
                    points[i][0], points[i][1],
                    points[j][0], points[j][1]
                ))

        dists.sort()

        return (
            dists[0] > 0 and                 # no overlapping points
            dists[0] == dists[1] ==
            dists[2] == dists[3] and         # four equal sides
            dists[4] == dists[5] and         # two equal diagonals
            dists[4] > dists[0]              # diagonal longer than side
        )

    def get_len(self, x1, y1, x2, y2):
        return (x1 - x2) ** 2 + (y1 - y2) ** 2



# V1-2
# IDEA: MATH (GEMINI)
class Solution(object):
    def validSquare(self, p1, p2, p3, p4):
        """
        :type p1: List[int]
        :type p2: List[int]
        :type p3: List[int]
        :type p4: List[int]
        :rtype: bool
        """
        # Helper function to get the squared distance between two points
        def get_dist(a, b):
            return (a[0] - b[0])**2 + (a[1] - b[1])**2

        points = [p1, p2, p3, p4]
        distances = set()

        # Calculate distances between all 6 possible pairs
        for i in range(4):
            for j in range(i + 1, 4):
                d = get_dist(points[i], points[j])
                
                # If distance is 0, points overlap, so it cannot be a square
                if d == 0:
                    return False
                    
                distances.add(d)

        # A valid square must have exactly 2 distinct distances (side and diagonal)
        return len(distances) == 2



# V2



# V3
# time = O(1)
# space = O(1)
class Solution(object):
    def validSquare(self, p1, p2, p3, p4):
        """
        :type p1: List[int]
        :type p2: List[int]
        :type p3: List[int]
        :type p4: List[int]
        :rtype: bool
        """
        def dist(p1, p2):
            return (p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2

        lookup = set([dist(p1, p2), dist(p1, p3),\
                      dist(p1, p4), dist(p2, p3),\
                      dist(p2, p4), dist(p3, p4)])
        return 0 not in lookup and len(lookup) == 2
