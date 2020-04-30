# V0 
# IDEA : BINARY SEARCH
class Solution(object):
    def findMin(self, nums):
        l = 0
        r = len(nums) - 1
        while r > l:
            mid = int(l + (r - l) / 2)
            if nums[mid] > nums[r]:
                l = mid + 1
            else:
                r = mid
        # nums[r] is OK as well, since 2 pointers (l, r) overlap in the final step
        return nums[l] 

# V0'
# IDEA : LINEAR SEARCH 
class Solution:
    def findMin(self, num):
        return min(num)

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79533470
class Solution(object):
    def findMin(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums: return 
        if len(nums) == 1: return nums[0]
        left, right = 0, len(nums) - 1
        mid = left
        while nums[left] >= nums[right]:
            if left + 1 == right:
                mid = right
                break
            mid = int((left + right) / 2)
            if nums[mid] >= nums[left]:
                left = mid
            elif nums[mid] <= nums[right]:
                right = mid
        return nums[mid]

### Test case
s=Solution()
assert s.findMin([1,2,3,4,5]) == 1
assert s.findMin([3,4,5,1,2]) == 1
assert s.findMin([4,5,1,2,3]) == 1
assert s.findMin([]) == None
assert s.findMin([0]) == 0
assert s.findMin([1]) == 1
assert s.findMin([3,3,3,3]) == 3
assert s.findMin([3,3,3,1,1]) == 1
assert s.findMin([3,5,4,4,1,2]) == 1
assert s.findMin([-1,0,1,2]) == -1
assert s.findMin([-3,5,4,4,1,2]) == -3

# V1'
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/discuss/48619/9-line-python-clean-code
class Solution(object):
    def findMin(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        i = 0
        j = len(nums) - 1
        while i < j:
            m = int(i + (j - i) / 2)
            if nums[m] > nums[j]:
                i = m + 1
            else:
                j = m
        return nums[i]

# V1''
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/solution/
class Solution(object):
    def findMin(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # If the list has just one element then return that element.
        if len(nums) == 1:
            return nums[0]

        # left pointer
        left = 0
        # right pointer
        right = len(nums) - 1

        # if the last element is greater than the first element then there is no rotation.
        # e.g. 1 < 2 < 3 < 4 < 5 < 7. Already sorted array.
        # Hence the smallest element is first element. A[0]
        if nums[right] > nums[0]:
            return nums[0]

        # Binary search way
        while right >= left:
            # Find the mid element
            mid = left + (right - left) / 2
            # if the mid element is greater than its next element then mid+1 element is the smallest
            # This point would be the point of change. From higher to lower value.
            if nums[mid] > nums[mid + 1]:
                return nums[mid + 1]
            # if the mid element is lesser than its previous element then mid element is the smallest
            if nums[mid - 1] > nums[mid]:
                return nums[mid]

            # if the mid elements value is greater than the 0th element this means
            # the least value is still somewhere to the right as we are still dealing with elements greater than nums[0]
            if nums[mid] > nums[0]:
                left = mid + 1
            # if nums[0] is greater than the mid value then this means the smallest value is somewhere to the left
            else:
                right = mid - 1

# V1''''
# IDEA : LINEAR SEARCH 
# http://bookshadow.com/weblog/2014/10/16/leetcode-find-minimum-rotated-sorted-array/
class Solution:
    # @param num, a list of integer
    # @return an integer
    def findMin(self, num):
        return min(num)

# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def findMin(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        left, right = 0, len(nums)
        target = nums[-1]

        while left < right:
            mid = left + (right - left) / 2

            if nums[mid] <= target:
                right = mid
            else:
                left = mid + 1

        return nums[left]

class Solution2(object):
    def findMin(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        left, right = 0, len(nums) - 1
        while left < right and nums[left] >= nums[right]:
            mid = left + (right - left) / 2

            if nums[mid] < nums[left]:
                right = mid
            else:
                left = mid + 1

        return nums[left]
