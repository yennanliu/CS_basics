"""

497. Random Point in Non-overlapping Rectangles
Medium

You are given an array of non-overlapping axis-aligned rectangles rects where rects[i] = [a_i, b_i, x_i, y_i] indicates that (a_i, b_i) is the bottom-left corner point of the i^th rectangle and (x_i, y_i) is the top-right corner point of the i^th rectangle. Design an algorithm to pick a random integer point inside the space covered by one of the given rectangles. A point on the perimeter of a rectangle is included in the space covered by the rectangle.

Any integer point inside the space covered by one of the given rectangles should be equally likely to be returned.

Note that an integer point is a point that has integer coordinates.

Implement the Solution class:

Solution(int[][] rects) Initializes the object with the given rectangles rects.
int[] pick() Returns a random integer point [u, v] inside the space covered by one of the given rectangles.

Example 1:

https://assets.leetcode.com/uploads/2021/07/24/lc-pickrandomrec.jpg

Input
["Solution", "pick", "pick", "pick", "pick", "pick"]
[[[[-2, -2, 1, 1], [2, 2, 4, 6]]], [], [], [], [], []]
Output
[null, [1, -2], [1, -1], [-1, -2], [-2, -2], [0, 0]]

Explanation
Solution solution = new Solution([[-2, -2, 1, 1], [2, 2, 4, 6]]);
solution.pick(); // return [1, -2]
solution.pick(); // return [1, -1]
solution.pick(); // return [-1, -2]
solution.pick(); // return [-2, -2]
solution.pick(); // return [0, 0]

Constraints:

1 <= rects.length <= 100
rects[i].length == 4
-10^9 <= a_i < x_i <= 10^9
-10^9 <= b_i < y_i <= 10^9
x_i - a_i <= 2000
y_i - b_i <= 2000
All the rectangles do not overlap.
At most 10^4 calls will be made to pick.

"""

# V0

# V1 : DEV 


# V2
# https://blog.csdn.net/fuxuemingzhu/article/details/83098201
# time = O(n) ctor, O(log n) pick  # n = len(rects)
# space = O(n)
class Solution(object):

    def __init__(self, rects):
        """
        :type rects: List[List[int]]
        """
        self.rects = rects
        self.N = len(rects)
        areas = [(x2 - x1 + 1) * (y2 - y1 + 1) for x1, y1, x2, y2 in rects]
        self.preSum = [0] * self.N
        self.preSum[0] = areas[0]
        for i in range(1, self.N):
            self.preSum[i] = self.preSum[i - 1] + areas[i]
        self.total = self.preSum[-1]

    def pickRect(self):
        rand = random.randint(0, self.total - 1)
        return bisect.bisect_right(self.preSum, rand)

    def pickPoint(self, rect):
        x1, y1, x2, y2 = rect
        x = random.randint(x1, x2)
        y = random.randint(y1, y2)
        return x, y
        
    def pick(self):
        """
        :rtype: List[int]
        """
        rectIndex = self.pickRect()
        rect = self.rects[rectIndex]
        return self.pickPoint(rect)
        


# Your Solution object will be instantiated and called as such:
# obj = Solution(rects)
# param_1 = obj.pick()


# V3
# time = O(n) ctor, O(log n) pick  # n = len(rects)
# space = O(n)

import random
import bisect


class Solution(object):

    def __init__(self, rects):
        """
        :type rects: List[List[int]]
        """
        self.__rects = list(rects)
        self.__prefix_sum = [(x[2]-x[0]+1)*(x[3]-x[1]+1) for x in rects]
        for i in range(1, len(self.__prefix_sum)):
            self.__prefix_sum[i] += self.__prefix_sum[i-1]

    def pick(self):
        """
        :rtype: List[int]
        """
        target = random.randint(0, self.__prefix_sum[-1]-1)
        left = bisect.bisect_right(self.__prefix_sum, target)
        rect = self.__rects[left]
        width, height = rect[2]-rect[0]+1, rect[3]-rect[1]+1
        base = self.__prefix_sum[left]-width*height
        return [rect[0]+(target-base)%width, rect[1]+(target-base)//width]


