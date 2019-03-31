# LeetCode 573. Squirrel Simulation

# There's a tree, a squirrel, and several nuts. Positions are represented by the cells in a 2D grid. Your goal is to find the minimal distance for the squirrel to collect all the nuts and put them under the tree one by one. The squirrel can only take at most one nut at one time and can move in four directions - up, down, left and right, to the adjacent cell. The distance is represented by the number of moves.

# Example 1:

# Input: 
# Height : 5
# Width : 7
# Tree position : [2,2]
# Squirrel : [4,4]
# Nuts : [[3,0], [2,5]]
# Output: 12
# Explanation:

# Note:

# All given positions won't overlap.
# The squirrel can take at most one nut at one time.
# The given positions of nuts have no order.
# Height and width are positive integers. 3 <= height * width <= 10,000.
# The given positions contain at least one nut, only one tree and one squirrel.



# V1 : dev 


# V2 
# http://bookshadow.com/weblog/2017/05/07/leetcode-squirrel-simulation/
class Solution(object):
    def minDistance(self, height, width, tree, squirrel, nuts):
        """
        :type height: int
        :type width: int
        :type tree: List[int]
        :type squirrel: List[int]
        :type nuts: List[List[int]]
        :rtype: int
        """
        manhattan = lambda p, q: abs(p[0] - q[0]) + abs(p[1] - q[1])
        total = 2 * sum(manhattan(tree, nut) for nut in nuts)
        return total + min(manhattan(squirrel, nut) - manhattan(tree, nut) for nut in nuts)


# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def minDistance(self, height, width, tree, squirrel, nuts):
        """
        :type height: int
        :type width: int
        :type tree: List[int]
        :type squirrel: List[int]
        :type nuts: List[List[int]]
        :rtype: int
        """
        def distance(a, b):
            return abs(a[0] - b[0]) + abs(a[1] - b[1])

        result = 0
        d = float("inf")
        for nut in nuts:
            result += (distance(nut, tree) * 2)
            d = min(d, distance(nut, squirrel) - distance(nut, tree))
        return result + d