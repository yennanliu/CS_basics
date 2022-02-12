"""

836. Rectangle Overlap
Easy

An axis-aligned rectangle is represented as a list [x1, y1, x2, y2], where (x1, y1) is the coordinate of its bottom-left corner, and (x2, y2) is the coordinate of its top-right corner. Its top and bottom edges are parallel to the X-axis, and its left and right edges are parallel to the Y-axis.

Two rectangles overlap if the area of their intersection is positive. To be clear, two rectangles that only touch at the corner or edges do not overlap.

Given two axis-aligned rectangles rec1 and rec2, return true if they overlap, otherwise return false.

 

Example 1:

Input: rec1 = [0,0,2,2], rec2 = [1,1,3,3]
Output: true
Example 2:

Input: rec1 = [0,0,1,1], rec2 = [1,0,2,1]
Output: false
Example 3:

Input: rec1 = [0,0,1,1], rec2 = [2,2,3,3]
Output: false
 

Constraints:

rect1.length == 4
rect2.length == 4
-109 <= rec1[i], rec2[i] <= 109
rec1 and rec2 represent a valid rectangle with a non-zero area.

"""

# V0
class Solution(object):
    def isRectangleOverlap(self, rec1, rec2):
        [A, B, C, D], [E, F, G, H] = rec1, rec2
        x, y = (min(C, G) - max(A, E)), (min(D, H) - max(B, F))
        return x > 0 and y > 0

# V0'
class Solution:
    def isRectangleOverlap(self, rec1, rec2):
        rec1_x1, rec1_y1, rec1_x2, rec1_y2 = rec1
        rec2_x1, rec2_y1, rec2_x2, rec2_y2 = rec2
        return not (rec1_x1 >= rec2_x2 or rec1_x2 <= rec2_x1 or rec1_y1 >= rec2_y2 or rec1_y2 <= rec2_y1)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/80472764
# A rectangle is represented as a list [x1, y1, x2, y2], 
# where (x1, y1) are the coordinates of its bottom-left corner, 
# and (x2, y2) are the coordinates of its top-right corner.
# --> so x2 is always > x1  and y2 is always > y1 
# find out  --> if the overlap x and y are all > 0 ---> these 2 rectangles are overlap 
class Solution(object):
    def isRectangleOverlap(self, rec1, rec2):
        """
        :type rec1: List[int]
        :type rec2: List[int]
        :rtype: bool
        """
        # (x1, y1, x2, y2)
        [A, B, C, D], [E, F, G, H] = rec1, rec2
        x, y = (min(C, G) - max(A, E)), (min(D, H) - max(B, F))
        return x > 0 and y > 0

### Test case
s=Solution()
assert s.isRectangleOverlap([0,0,2,2],[1,1,3,3])==True
assert s.isRectangleOverlap([0,0,1,1],[1,0,2,1])==False
assert s.isRectangleOverlap([0,0,0,0],[0,0,0,0])==False
assert s.isRectangleOverlap([1,1,1,1],[1,1,1,1])==False

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80472764
class Solution:
    def isRectangleOverlap(self, rec1, rec2):
        """
        :type rec1: List[int]
        :type rec2: List[int]
        :rtype: bool
        """
        rec1_x1, rec1_y1, rec1_x2, rec1_y2 = rec1
        rec2_x1, rec2_y1, rec2_x2, rec2_y2 = rec2
        return not (rec1_x1 >= rec2_x2 or rec1_x2 <= rec2_x1 or rec1_y1 >= rec2_y2 or rec1_y2 <= rec2_y1)

# V2
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def isRectangleOverlap(self, rec1, rec2):
        """
        :type rec1: List[int]
        :type rec2: List[int]
        :rtype: bool
        """
        def intersect(p_left, p_right, q_left, q_right):
            return max(p_left, q_left) < min(p_right, q_right)

        return (intersect(rec1[0], rec1[2], rec2[0], rec2[2]) and
                intersect(rec1[1], rec1[3], rec2[1], rec2[3]))