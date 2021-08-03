"""

Follow up for H-Index:  (274)

What if the citations array is sorted 
in ascending order? Could you optimize your algorithm?

Hint:

Expected runtime complexity is in O(log n) and the input is sorted.


ref :
https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Sort/h-index.py

"""

# V0
# IDEA : BINARY SEARCH
class Solution:
    def hIndex(self, c):
        # NOTE : right = len(c) -1 
        left, right = 0, len(c)-1
        res = 0
        while left <= right:
            mid = left + (right-left)//2
            rem = len(c) - mid # NOTE this
            if c[mid] >= rem:
                res = rem 
                right = mid - 1 # NOTE this, we want mid become smaller
            else:
                left = mid + 1 # NOTE this, we want mid become bigger
        return res

# V0'
# IDEA : SAME AS #274 H-Index
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        # reverse ordeing the citations first, so the first validated case is the max h-index 
        # i : how many digit in the list that are >= c 
        # c : the value of "possible" h-index
        for i, c in enumerate(sorted(citations, reverse = True)):
            if i >= c:
                return i
        # BE AWARE OF IT
        # for the [] or [1] ... cases
        return len(citations)

# V1
# https://leetcode.com/problems/h-index-ii/discuss/709447/Python-O(logN)-Binary-Search
# IDEA : BINARY SEARCH
class Solution:
    def hIndex(self, c: List[int]) -> int:
        left, right = 0, len(c)-1
        res = 0
        while left <= right:
            mid = left + (right-left)//2
            rem = len(c) - mid
            if c[mid] >= rem:
                res = rem
                right = mid - 1
            else:
                left = mid + 1
        return res

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82949743
# IDEA : when a scientist has index  = h, then there are at least h citations of all his 
# papers. 
# h=0 -> at least 0 citations paper 
# h=1 -> at least 1 citations paper
# ....
# h=6 -> at least 6 citations papers
# ... 
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        N = len(citations)
        l, r = 0, N - 1
        H = 0
        while l <= r:
            mid = l + (r - l) / 2
            H = max(H, min(citations[mid], N - mid))
            if citations[mid] < N - mid:
                l = mid + 1
            else:
                r = mid - 1
        return H

# V1'
# http://bookshadow.com/weblog/2015/09/04/leetcode-h-index-ii/
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        N = len(citations)
        low, high = 0, N - 1
        while low <= high:
            mid = (low + high) / 2
            if N - mid > citations[mid]:
                low = mid + 1
            else:
                high = mid - 1
        return N - low

# V1''
# https://www.hrwhisper.me/leetcode-h-index-h-index-ii/
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        if not citations: return 0
        return max([min(i + 1, c) for i, c in enumerate(sorted(citations, reverse=True))])

# V1'''
# https://www.hrwhisper.me/leetcode-h-index-h-index-ii/
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        return sum([i < c for i, c in enumerate(sorted(citations, reverse=True))])

# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def hIndex(self, citations):
        """
        :type citations: List[int]
        :rtype: int
        """
        n = len(citations)
        left, right = 0, n - 1
        while left <= right:
            mid = (left + right) / 2
            if citations[mid] >= n - mid:
                right = mid - 1
            else:
                left = mid + 1
        return n - left