"""

4. Median of Two Sorted Arrays
Hard

15208

1907

Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.

The overall run time complexity should be O(log (m+n)).

 

Example 1:

Input: nums1 = [1,3], nums2 = [2]
Output: 2.00000
Explanation: merged array = [1,2,3] and median is 2.
Example 2:

Input: nums1 = [1,2], nums2 = [3,4]
Output: 2.50000
Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
 

Constraints:

nums1.length == m
nums2.length == n
0 <= m <= 1000
0 <= n <= 1000
1 <= m + n <= 2000
-106 <= nums1[i], nums2[i] <= 106

"""

# V0
# IDEA : medium definiton
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        nums = nums1 + nums2
        nums.sort()
        
        L = len(nums)
        if L==1:
            return nums[0]
        if L % 2 != 0:
            return nums[L//2]
        else:
            return (nums[L//2 - 1] + nums[L//2])/2.0 # NOTE this !!!

# V0'
# IDEA : heapq
import heapq
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        final = heapq.merge(nums1,nums2)
        final = list(final)
        if len(final)%2 != 0:
            return final[len(final)//2]
        else:
            left = int(len(final)/2) - 1
            right = int(len(final)/2)
            return (final[left]+final[right])/2.0

# V1
# IDEA : medium definiton
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/520489/Simple-Python-solution
# IDEA 
# This very simple Python solution beats 94% (speed) and 100% (memory).
# Merge both arrays and sort the new array.
# If the length is odd, return the number in the middle of the array.
# If the length is even, return the mean between the two numbers in the middle.
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        nums = nums1 + nums2
        nums.sort()
        
        L = len(nums)
        if L==1:
            return nums[0]
        if L % 2 != 0:
            return nums[L//2]
        else:
            return (nums[L//2 - 1] + nums[L//2])/2.0 # NOTE this !!!


# V1
# IDEA : heapq
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/1373385/Python-3-Easy-With-explanation-Heaps
# IDEA :
#  -> We use the property of heaps to our advantage for this problem. We can easily get a merged sorted list using heaps, once we have this it is simple arithmetic.
#  -> If the length of the merged list is even, then the median is the avg of the two middle numbers. If the length of the merged list is odd, then the median will be the middle number.
#  -> building a heap is O(Nlog(n)) (n is number of elements in the array).
import heapq
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        final = heapq.merge(nums1,nums2)
        final = list(final)
        if len(final)%2 != 0:
            return final[len(final)//2]
        else:
            left = int(len(final)/2) - 1
            right = int(len(final)/2)
            return (final[left]+final[right])/2.0

# V1
# IDEA : bisect
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2755/9-lines-O(log(min(mn)))-Python
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        a, b = sorted((nums1, nums2), key=len)
        m, n = len(a), len(b)
        after = (m + n - 1) / 2
        class Range:
            def __getitem__(self, i):
                return after-i-1 < 0 or a[i] >= b[after-i-1]
        i = bisect.bisect_left(Range(), True, 0, m)
        nextfew = sorted(a[i:i+2] + b[after-i:after-i+2])
        return (nextfew[0] + nextfew[1 - (m+n)%2]) / 2.0

# V1
# IDEA : binary search
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2755/9-lines-O(log(min(mn)))-Python
class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        a, b = sorted((nums1, nums2), key=len)
        m, n = len(a), len(b)
        after = (m + n - 1) / 2
        lo, hi = 0, m
        while lo < hi:
            i = (lo + hi) / 2
            if after-i-1 < 0 or a[i] >= b[after-i-1]:
                hi = i
            else:
                lo = i + 1
        i = lo
        nextfew = sorted(a[i:i+2] + b[after-i:after-i+2])
        return (nextfew[0] + nextfew[1 - (m+n)%2]) / 2.0

# V1
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/468832/Simple-python-solution
class Solution:
    def findMedianSortedArrays(self, nums1: List[int], nums2: List[int]) -> float:      
        concat = sorted(nums1+nums2) 
        if len(concat)%2 == 1:
            med = concat[int(len(concat)/2)]
        else:
            tot_len = int(len(concat)/2)
            med = (concat[tot_len-1]+concat[tot_len]) / 2
                         
        return med

# V1
# https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/952647/Simple-python
import statistics
class Solution:
    def findMedianSortedArrays(self, nums1: List[int], nums2: List[int]) -> float:
        nums3 = nums1 + nums2
        nums3 = sorted(nums3)
        return statistics.median(nums3)

# V2