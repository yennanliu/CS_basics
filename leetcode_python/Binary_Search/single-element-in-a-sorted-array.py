# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79275636
class Solution(object):
    def singleNonDuplicate(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return reduce(lambda x, y: x^y, nums)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79275636
class Solution(object):
    def singleNonDuplicate(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        for i in range(0, len(nums) - 1, 2):
            if nums[i] != nums[i + 1]:
                return nums[i]
        return nums[-1]

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79275636
class Solution(object):
    def singleNonDuplicate(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        N = len(nums)
        left, right = 0, N - 1 #[left, right]
        while (left < right):
            mid = left + (right - left) / 2
            if mid % 2 == 1: mid -= 1
            if nums[mid] != nums[mid + 1]:
                right = mid
            else:
                left = mid + 2
        return nums[left]
        
# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def singleNonDuplicate(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        left, right = 0, len(nums)-1
        while left <= right:
            mid = left + (right - left) / 2
            if not (mid%2 == 0 and mid+1 < len(nums) and \
                    nums[mid] == nums[mid+1]) and \
               not (mid%2 == 1 and nums[mid] == nums[mid-1]):
                right = mid-1
            else:
                left = mid+1
        return nums[left]
