# V0
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        if not nums: return -1
        left, right = 0, len(nums) - 1
        while left <= right:
            mid = (left + right) // 2
            if nums[mid] == target:
                return mid
            # case 1 :  descending  mid  ascending
            if nums[mid] < nums[right]:
                # mind NOT use (" nums[mid] < target <= nums[right]")
                # mind the "<="
                if target > nums[mid] and target <= nums[right]: # check the relationship with target, which is different from the default binary search
                    left = mid + 1
                else:
                    right = mid - 1
            # case 2 :  ascending  mid  descending  
            else:
                # # mind NOT use (" nums[left] <= target < nums[mid]")
                # mind the "<="
                if target < nums[mid] and target >= nums[left]:  # check the relationship with target, which is different from the default binary search
                    right = mid - 1
                else:
                    left = mid + 1
        return -1     

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79534213
# CHECK ALSO : BINARY SEARCH 
# https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/binary_search.py
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        if not nums: return -1
        left, right = 0, len(nums) - 1
        while left <= right:
            mid = (left + right) // 2
            if nums[mid] == target:
                return mid
            if nums[mid] < nums[right]:
                if target > nums[mid] and target <= nums[right]: # check the relationship with target, which is different from the default binary search
                    left = mid + 1
                else:
                    right = mid - 1
            else:
                if target < nums[mid] and target >= nums[left]:  # check the relationship with target, which is different from the default binary search
                    right = mid - 1
                else:
                    left = mid + 1
        return -1     

# V1'
# https://www.jiuzhang.com/solution/search-in-rotated-sorted-array/#tag-highlight-lang-python
class Solution:
    """
    @param A: an integer rotated sorted array
    @param target: an integer to be searched
    @return: an integer
    """
    def search(self, A, target):
        if not A:
            return -1
            
        start, end = 0, len(A) - 1
        while start + 1 < end:
            mid = (start + end) // 2
            if A[mid] >= A[start]:
                if A[start] <= target <= A[mid]:
                    end = mid
                else:
                    start = mid
            else:
                if A[mid] <= target <= A[end]:
                    start = mid
                else:
                    end = mid
                    
        if A[start] == target:
            return start
        if A[end] == target:
            return end
        return -1

# V1''
# https://blog.csdn.net/aliceyangxi1987/article/details/50557496
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        
        start=0
        end=len(nums)-1
        
        while start<=end:
        
            mid=(start+end)/2
        
            if nums[mid]==target:
                return mid
                
            if nums[mid]>=nums[start]:                              # when nums[mid] belongs left ascending array 
        
                if target>=nums[start] and target<nums[mid]:
                    end=mid-1
                else:
                    start=mid+1
            
            if nums[mid]<nums[end]:                                 # when nums[mid] belongs left ascending array 
        
                if target>nums[mid] and target<=nums[end]:
                    start=mid+1
                else:
                    end=mid-1        
        return -1
