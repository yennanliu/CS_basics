# V0 
# IDEA : MAINTAIN var a, b 
#        AND GO THROUGH nums to check if there exists x (on the right hand side of a, b )
#        such that x > a > b 
class Solution(object):
    def increasingTriplet(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        a = b = None
        for n in nums:
            if a is None or a >= n:    # min element (1st element) : a 
                a = n
            elif b is None or b >= n:  # 2nd min element (2nd element) : b
                b = n
            else:                      # 3rd min element (3nd element) : return Ture if this case exists
                return True
        return False

# V1 
# http://bookshadow.com/weblog/2016/02/16/leetcode-increasing-triplet-subsequence/
# IDEA :  MAINTAIN ON 2 VAR a, b (min and 2nd min)
# a IS THE CURRENT MIN ELEMENT ; 
# b IS THE ONE CLOSEST TO (NEXT TO) a and BIGGER TO a 
# PROCESS:
# STEP 1) INIT : a = b = None
# STEP 2) GO THROUGH THE ARRAY, RECORD CURRENT ELEMENT n
# STEP 3) if a is None or a >= n  ----> a = n
# STEP 3) elif b is None or b >= n ----> b = n
# STEP 3) or, return True
# STEP 4) return False 
class Solution(object):
    def increasingTriplet(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        a = b = None
        for n in nums:
            if a is None or a >= n:    # min element (1st element) : a 
                a = n
            elif b is None or b >= n:  # 2nd min element (2nd element) : b
                b = n
            else:                      # 3rd min element (3nd element) : return Ture if this case exists
                return True
        return False

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79826703
class Solution(object):
    def increasingTriplet(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        first, second = float('inf'), float('inf')
        for num in nums:
            if num <= first:
                first = num
            elif num <= second:
                second = num
            else:
                return True
        return False

# V2 
# Time:  O(n)
# Space: O(1)
import bisect
class Solution(object):
    def increasingTriplet(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        min_num, a, b = float("inf"), float("inf"), float("inf")
        for c in nums:
            if min_num >= c:
                min_num = c
            elif b >= c:
                a, b = min_num, c
            else:  # a < b < c
                return True
        return False

# Time:  O(n * logk)
# Space: O(k)
# Generalization of k-uplet.
class Solution_Generalization(object):
    def increasingTriplet(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        def increasingKUplet(nums, k):
            inc = [float('inf')] * (k - 1)
            for num in nums:
                i = bisect.bisect_left(inc, num)
                if i >= k - 1:
                    return True
                inc[i] = num
            return k == 0
        return increasingKUplet(nums, 3)