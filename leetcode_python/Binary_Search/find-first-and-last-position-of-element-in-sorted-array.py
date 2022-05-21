"""

34. Find First and Last Position of Element in Sorted Array
Medium

Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.

If target is not found in the array, return [-1, -1].

You must write an algorithm with O(log n) runtime complexity.

 

Example 1:

Input: nums = [5,7,7,8,8,10], target = 8
Output: [3,4]
Example 2:

Input: nums = [5,7,7,8,8,10], target = 6
Output: [-1,-1]
Example 3:

Input: nums = [], target = 0
Output: [-1,-1]
 

Constraints:

0 <= nums.length <= 105
-109 <= nums[i] <= 109
nums is a non-decreasing array.
-109 <= target <= 109

"""

# V0
# IDEA : BINARY SEARCH
class Solution:
    def searchRange(self, nums, target):
        l = self.findLeft(nums, target)
        r = self.findRight(nums, target)
        return [l, r] if l <= r else [-1, -1]
     
    def findLeft(self, nums, target):
        l = 0
        r = len(nums)-1
        while l <= r:
            mid = l + (r-l)//2
            """
            NOTE HERE !!!
            """
            if nums[mid] < target:
                l = mid + 1
            else:
                r = mid - 1
        return l    
    
    def findRight(self, nums, target):
        l = 0
        r = len(nums)-1
        while l <= r:
            mid = l + (r-l)//2
            """
            NOTE HERE !!!
            """
            if nums[mid] <= target:
                l = mid + 1
            else:
                r = mid - 1
        return r

# V0'
# IDEA : BINARY SEARCH
class Solution(object):
    def searchRange(self, nums, target):
        # help func
        def getMin(l, r):
            l = l
            r = r
            # note this
            while r >= l:
                mid = l + (r-l) // 2
                """
                NOTE THIS !!!

                    -> if nums[mid] == target
                    -> we want to SMALLER index
                """
                if nums[mid] == target:
                    r = mid - 1
                elif nums[mid] < target:
                    l = mid + 1
                elif nums[mid] > target:
                    r = mid - 1
            # NOTE !!! we return l
            return l

        def getMax(l, r):
            l = l
            r = r
            # note this
            while r >= l:
                mid = l + (r-l) // 2
                """
                NOTE THIS !!!

                    -> if nums[mid] == target
                    -> we want to BIGGER index
                """
                if nums[mid] == target:
                    l = mid + 1
                elif nums[mid] < target:
                    l = mid + 1
                elif nums[mid] > target:
                    r = mid - 1
            # NOTE !!! we return r
            return r
        # edge case
        if not nums or target not in nums:
            return [-1, -1]
        res = [-1, -1]
        l = 0
        r = len(nums) - 1
        _min = getMin(l, r)
        _max = getMax(l, r)
        return [_min, _max]

# V0'
# IDEA : BINARY SEARCH
class Solution:
    def searchRange(self, nums: List[int], target: int) -> List[int]:
        
        def search(x):
            lo, hi = 0, len(nums)           
            while lo < hi:
                mid = (lo + hi) // 2
                if nums[mid] < x:
                    lo = mid+1
                else:
                    hi = mid                    
            return lo
        
        lo = search(target)
        hi = search(target+1)-1
        
        if lo <= hi:
            return [lo, hi]
                
        return [-1, -1]

# V0'
# IDEA : BRUTE FORCE + PY default
# binary search : time O(n log n)
class Solution(object):
    def searchRange(self, nums, target):
        # edge case
        if not nums:
            return [-1, -1]
        if target not in nums:
            return [-1, -1]
        f_idx = nums.index(target)
        idx = f_idx
        _len = len(nums)
        while idx < _len and nums[idx] == target:
            idx += 1
        return [f_idx, idx-1]

# V1
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/discuss/1054742/Python-O(logn)
class Solution:
    def searchRange(self, nums: List[int], target: int) -> List[int]:
        
        def search(x):
            lo, hi = 0, len(nums)           
            while lo < hi:
                mid = (lo + hi) // 2
                if nums[mid] < x:
                    lo = mid+1
                else:
                    hi = mid                    
            return lo
        
        lo = search(target)
        hi = search(target+1)-1
        
        if lo <= hi:
            return [lo, hi]
                
        return [-1, -1]

# V1'
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/discuss/144320/Python-tm
class Solution:
    def searchRange(self, nums, target):
        l = self.findLeft(nums, target)
        r = self.findRight(nums, target)
        return [l, r] if l <= r else [-1, -1]
     
    def findLeft(self, nums, target):
        l, mid, r = 0, 0, len(nums)-1
        while l <= r:
            mid = l + (r-l)//2
            if nums[mid] < target:
                l = mid + 1
            else:
                r = mid - 1
        return l    
    
    def findRight(self, nums, target):
        l, mid, r = 0, 0, len(nums)-1
        while l <= r:
            mid = l + (r-l)//2
            if nums[mid] <= target:
                l = mid + 1
            else:
                r = mid - 1
        return r

# V1''
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/discuss/236386/Python-solution
class Solution(object):
    def searchRange(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """            
        def search_for(nums, target, left = True):
            i = 0
            j = len(nums) 
            while i < j:
                mid = (i+j) / 2
                if nums[mid] == target:
                    if left:
                        j = mid 
                    else:
                        i = mid + 1
                elif nums[mid] < target:
                    i = mid + 1
                else:
                    j = mid 
            return i
        left = search_for(nums, target, True)
        right = search_for(nums, target, False)
        if not nums:
            return[-1,-1]
        elif 0 <= left < len(nums) and nums[left] == target:
            return [left, right-1]
        else:
            return [-1,-1]

# V1'''
# IDEA : bisect_left
# https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/discuss/236386/Python-solution
class Solution:
    def searchRange(self, nums: 'List[int]', target: 'int') -> 'List[int]':
        if not nums:
            return [-1, -1]
        left = bisect.bisect_left(nums, target)
        if left >= len(nums) or nums[left] != target:
            return [-1, -1]
        right = bisect.bisect_right(nums, target)
        return [left, right-1]

# V1''''
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/solution/
class Solution:
    def searchRange(self, nums: List[int], target: int) -> List[int]:
        
        lower_bound = self.findBound(nums, target, True)
        if (lower_bound == -1):
            return [-1, -1]
        
        upper_bound = self.findBound(nums, target, False)
        
        return [lower_bound, upper_bound]
        
    def findBound(self, nums: List[int], target: int, isFirst: bool) -> int:
        
        N = len(nums)
        begin, end = 0, N - 1
        while begin <= end:
            mid = int((begin + end) / 2)    
            
            if nums[mid] == target:
                
                if isFirst:
                    # This means we found our lower bound.
                    if mid == begin or nums[mid - 1] < target:
                        return mid

                    # Search on the left side for the bound.
                    end = mid - 1
                else:
                    
                    # This means we found our upper bound.
                    if mid == end or nums[mid + 1] > target:
                        return mid
                    
                    # Search on the right side for the bound.
                    begin = mid + 1
            
            elif nums[mid] > target:
                end = mid - 1
            else:
                begin = mid + 1
        
        return -1

# V2