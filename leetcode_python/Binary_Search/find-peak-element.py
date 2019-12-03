# V0 

# V1 
# https://blog.csdn.net/aliceyangxi1987/article/details/50484982
class Solution(object):
    def findPeakElement(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return self.helpsearch(nums,0,len(nums)-1)
    
    def helpsearch(self,nums,start,end):
        
        if start==end:
            return start
        if end-start==1:
            return [end,start][nums[start]>nums[end]]       
        mid=(start+end)/2
        if nums[mid]<nums[mid-1]:
            return self.helpsearch(nums,start,mid-1)
        if nums[mid]<nums[mid+1]:
            return self.helpsearch(nums,mid+1,end)
        return mid

# V1'
# https://www.jiuzhang.com/solution/find-peak-element/#tag-highlight-lang-python
class Solution:
    #@param A: An integers list.
    #@return: return any of peek positions.
    def findPeak(self, A):
        # write your code here
        start, end = 1, len(A) - 2
        while start + 1 <  end:
            mid = (start + end) // 2
            if A[mid] < A[mid - 1]:
                end = mid
            elif A[mid] < A[mid + 1]:
                start = mid
            else:
                end = mid
        if A[start] < A[end]:
            return end
        else:
            return start

# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def findPeakElement(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        left, right = 0, len(nums) - 1

        while left < right:
            mid = left + (right - left) / 2
            if nums[mid] > nums[mid + 1]:
                right = mid
            else:
                left = mid + 1
        return left
