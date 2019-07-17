# V0 

# V1 
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