"""

849. Maximize Distance to Closest Person
Medium

You are given an array representing a row of seats where seats[i] = 1 represents a person sitting in the i^th seat, and seats[i] = 0 represents that the i^th seat is empty (0-indexed).

There is at least one empty seat, and at least one person sitting.

Alex wants to sit in the seat such that the distance between him and the closest person to him is maximized.

Return that maximum distance to the closest person.

Example 1:

https://assets.leetcode.com/uploads/2020/09/10/distance.jpg

Input: seats = [1,0,0,0,1,0,1]
Output: 2
Explanation:
If Alex sits in the second open seat (i.e. seats[2]), then the closest person has distance 2.
If Alex sits in any other open seat, the closest person has distance 1.
Thus, the maximum distance to the closest person is 2.

Example 2:

Input: seats = [1,0,0,0]
Output: 3
Explanation:
If Alex sits in the last seat (i.e. seats[3]), the closest person is 3 seats away.
This is the maximum distance possible, so the answer is 3.

Example 3:

Input: seats = [0,1]
Output: 1

Constraints:

2 <= seats.length <= 2 * 10^4
seats[i] is 0 or 1.
At least one seat is empty.
At least one seat is occupied.

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80643250
# time = O(n)
# space = O(n)
class Solution(object):
    def maxDistToClosest(self, seats):
        """
        :type seats: List[int]
        :rtype: int
        """
        index = -200000
        _len = len(seats)
        ans = [0] * _len
        for i in range(_len):
            if seats[i] == 1:
                index = i
            else:
                ans[i] = abs(i - index)
        index = -200000
        for i in range(_len - 1, -1, -1):
            if seats[i] == 1:
                index = i
            else:
                ans[i] = min(abs(i - index), ans[i])
        return max(ans)

# V1'
# https://www.jiuzhang.com/solution/maximize-distance-to-closest-person/#tag-highlight-lang-python
# time = O(n)
# space = O(1)
class Solution:
    """
    @param seats: an array
    @return: the maximum distance
    """
    def maxDistToClosest(self, seats):
        # Write your code here.
        res = i = 0
        for j in range(len(seats)):
            if seats[j]:
                res = max(res, j - i + 1 >> 1) if i else j
                i = j + 1
        return max(res, len(seats) - i)

# V2 
# time = O(n)
# space = O(1)
class Solution(object):
    def maxDistToClosest(self, seats):
        """
        :type seats: List[int]
        :rtype: int
        """
        prev, result = -1, 1
        for i in range(len(seats)):
            if seats[i]:
                if prev < 0:
                    result = i
                else:
                    result = max(result, (i-prev)//2)
                prev = i
        return max(result, len(seats)-1-prev)