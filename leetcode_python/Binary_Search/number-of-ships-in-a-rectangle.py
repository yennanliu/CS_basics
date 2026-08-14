"""

1274. Number of Ships in a Rectangle
Hard

(This problem is an interactive problem.)

Each ship is located at an integer point on the sea represented by a cartesian
plane, and each integer point may contain at most 1 ship.

You have a function Sea.hasShips(topRight, bottomLeft) which takes two points
as arguments and returns true If there is at least one ship in the rectangle
represented by the two points, including on the boundary.

Given two points: the top right and bottom left corners of a rectangle,
return the number of ships present in that rectangle.
It is guaranteed that there are at most 10 ships in that rectangle.

Submissions making more than 400 calls to hasShips will be judged Wrong Answer.
Also, any solutions that attempt to circumvent the judge will result in
disqualification.


Example 1:

Input:
ships = [[1,1],[2,2],[3,3],[5,5]], topRight = [4,4], bottomLeft = [0,0]
Output: 3
Explanation: From [0,0] to [4,4] we can count 3 ships within the range.

Example 2:

Input: ans = [[1,1],[2,2],[3,3]], topRight = [1000,1000], bottomLeft = [0,0]
Output: 3


Constraints:

On the input ships is only given to initialize the map internally.
You must solve this problem "blindfolded". In other words, you must find the
answer using the given hasShips API, without knowing the ships position.
0 <= bottomLeft[0] <= topRight[0] <= 1000
0 <= bottomLeft[1] <= topRight[1] <= 1000
topRight != bottomLeft

"""

# """
# This is Sea's API interface.
# You should not implement it, or speculate about its implementation
# """
# class Sea(object):
#    def hasShips(self, topRight: 'Point', bottomLeft: 'Point') -> bool:
#
# class Point(object):
#    def __init__(self, x: int, y: int):
#        self.x = x
#        self.y = y

# V0
# IDEA : DIVIDE AND CONQUER (quad tree style)
#        if the rectangle holds no ship -> prune the whole branch (this is
#        what keeps the number of hasShips calls small);
#        if it is a single cell and holds a ship -> count 1;
#        otherwise cut it into 4 sub rectangles and recurse
# time = O(s * log(max(m, n))), s = number of ships (<= 10)
# space = O(log(max(m, n))), recursion depth
class Solution(object):
    def countShips(self, sea, topRight, bottomLeft):
        # empty rectangle
        if topRight.x < bottomLeft.x or topRight.y < bottomLeft.y:
            return 0
        # PRUNE : no ship here at all
        if not sea.hasShips(topRight, bottomLeft):
            return 0
        # single cell + hasShips is True -> exactly one ship
        if topRight.x == bottomLeft.x and topRight.y == bottomLeft.y:
            return 1

        mx = (topRight.x + bottomLeft.x) // 2
        my = (topRight.y + bottomLeft.y) // 2

        # bottom left / bottom right / top left / top right quadrants
        return (self.countShips(sea, Point(mx, my), bottomLeft)
                + self.countShips(sea, Point(topRight.x, my), Point(mx + 1, bottomLeft.y))
                + self.countShips(sea, Point(mx, topRight.y), Point(bottomLeft.x, my + 1))
                + self.countShips(sea, topRight, Point(mx + 1, my + 1)))
