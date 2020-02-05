# V0 
# IDEA : 
# STEP 0) NEGLECT THE "DUPLICATES" : "while l < r and nums[l] == nums[r]: l += 1"
# STEP 1) CHECK WHETHER "LEFT SUB-STRING" OR "RIGHT SUB-STRING" IS ORDERING 
#         -> IF THE SUB-STRING IS ORDERING => IT'S IS ASCENDING ORDERING  => SO WE CAN USE THE ORIGINAL "BINRARY SEARCH" FOR TARGET SEARCH
# STEP 2) CHECK THE RELATIONS : nums[l] <= target < nums[mid], nums[mid] < target <= nums[r]
#         -> THEN MODIFY l, r, with mid by cases
# STEP 3) LOOP STEP 0) -> STEP 3), if nums[m] == target, return it, if not then return False at the end.
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: bool
        """
        N = len(nums)
        l, r = 0, N - 1
        while l <= r:
            while l < r and nums[l] == nums[r]:
                l += 1
            mid = l + (r - l) // 2
            if nums[mid] == target:
                return True
            if nums[mid] >= nums[l]:
                if nums[l] <= target < nums[mid]:
                    r = mid - 1
                else:
                    l = mid + 1
            elif nums[mid] <= nums[r]:
                if nums[mid] < target <= nums[r]:
                    l = mid + 1
                else:
                    r = mid - 1
        return False

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83214278
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: bool
        """
        N = len(nums)
        l, r = 0, N - 1
        while l <= r:
            while l < r and nums[l] == nums[r]:
                l += 1
            mid = l + (r - l) // 2
            if nums[mid] == target:
                return True
            if nums[mid] >= nums[l]:
                if nums[l] <= target < nums[mid]:
                    r = mid - 1
                else:
                    l = mid + 1
            elif nums[mid] <= nums[r]:
                if nums[mid] < target <= nums[r]:
                    l = mid + 1
                else:
                    r = mid - 1
        return False

# V1'
# https://blog.csdn.net/happyaaaaaaaaaaa/article/details/51602234
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: bool
        """
        left, right = 0, len(nums) - 1
        while left <= right :
            mid = (left+right) // 2
            if nums[mid] == target : return True
            if nums[mid] == nums[left] : left += 1
            elif nums[mid] > nums[left] :
                if nums[mid] > target and nums[left] <= target :
                    right = mid - 1
                else : left = mid + 1
            else :
                if nums[mid] < target and nums[right] >= target :
                    left = mid + 1
                else : right = mid - 1
        return False

# V1''
# https://www.jiuzhang.com/solution/search-in-rotated-sorted-array-ii/#tag-highlight-lang-python
class Solution(object):
    def search(self, A, target):
        # write your code here
        for num in A:
            if num == target:
                return True
        return False

# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        left, right = 0, len(nums) - 1

        while left <= right:
            mid = left + (right - left) // 2

            if nums[mid] == target:
                return True
            elif nums[mid] == nums[left]:
                left += 1
            elif (nums[mid] > nums[left] and nums[left] <= target < nums[mid]) or \
                 (nums[mid] < nums[left] and not (nums[mid] < target <= nums[right])):
                right = mid - 1
            else:
                left = mid + 1
        return False