# Time:  O(logn)
# Space: O(1)
#
# You are a product manager and currently leading a team to
# develop a new product. Unfortunately, the latest version of
# your product fails the quality check. Since each version is
# developed based on the previous version, all the versions
# after a bad version are also bad.
#
# Suppose you have n versions [1, 2, ..., n] and you want to
# find out the first bad one, which causes all the following
# ones to be bad.
#
# You are given an API bool isBadVersion(version) which will
# return whether version is bad. Implement a function to find
# the first bad version. You should minimize the number of
# calls to the API.
#
# The isBadVersion API is already defined for you.
# @param version, an integer
# @return a bool
# def isBadVersion(version):
# e.g. 
# isBadVersion(n1) == True # if n1 version is the bad version 
# isBadVersion(n2) == False # if n2 version is the OK version 

# V0 
class Solution(object):
    def firstBadVersion(self, n):
        left = 1 
        right = n
        while right > left + 1:
            mid = (left + right)//2
            if SVNRepo.isBadVersion(mid):
                end = mid 
            else:
                left = mid 
        if SVNRepo.isBadVersion(left):
            return left
        return right 

# V1 
# https://blog.csdn.net/coder_orz/article/details/52048093
class Solution(object):
    def firstBadVersion(self, n):
        """
        :type n: int
        :rtype: int
        """
        left, right = 1, n
        while True:
            mid = (left + right) / 2
            if Solution.isBadVersion(mid):
                if mid == 1 or not Solution.isBadVersion(mid - 1):
                    return mid
                right = mid - 1
            else:
                left = mid + 1

# V1'
# https://blog.csdn.net/coder_orz/article/details/52048093
class Solution(object):
    def firstBadVersion(self, n):
        """
        :type n: int
        :rtype: int
        """
        left, right = 1, n
        while left < right:
            mid = (left + right) / 2
            if isBadVersion(mid):
                right = mid
            else:
                left = mid + 1
        return left

# V1''
# https://www.jiuzhang.com/solution/first-bad-version/#tag-highlight-lang-python
class Solution:
    """
    @param n: An integers.
    @return: An integer which is the first bad version.
    """
    def findFirstBadVersion(self, n):
        start, end = 1, n
        while start + 1 < end:
            mid = (start + end) // 2
            if SVNRepo.isBadVersion(mid):
                end = mid
            else:
                start = mid
        if SVNRepo.isBadVersion(start):
            return start
        return end

# V2  
class Solution(object):
    def firstBadVersion(self, n):
        """
        :type n: int
        :rtype: int
        """
        left, right = 1, n
        while left < right:
            mid = (left + right) / 2
            if Solution.isBadVersion(mid):
                right = mid
            else:
                left = mid + 1
        return left

# V3  
class Solution(object):
    def firstBadVersion(self, n):
        """
        :type n: int
        :rtype: int
        """
        left, right = 1, n
        while left <= right:
            mid = left + (right - left) / 2
            if isBadVersion(mid): # noqa
                right = mid - 1
            else:
                left = mid + 1
        return left
