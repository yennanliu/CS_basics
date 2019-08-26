# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80643250
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
# Time:  O(n)
# Space: O(1)
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