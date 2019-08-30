# V0 

# V1 
# https://www.hrwhisper.me/leetcode-wiggle-sort-ii/
class Solution(object):
    def wiggleSort(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        temp = sorted(nums)
        s, t = (len(nums) + 1) >> 1, len(nums)
        for i in range(len(nums)):
            if i & 1 == 0:
                s -= 1
                nums[i] = temp[s]
            else:
                t -= 1
                nums[i] = temp[t]

# V1'
# http://bookshadow.com/weblog/2015/12/31/leetcode-wiggle-sort-ii/
class Solution(object):
    def wiggleSort(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        size = len(nums)
        snums = sorted(nums)
        for x in range(1, size, 2) + range(0, size, 2):
            nums[x] = snums.pop()

# V1''
# https://www.jiuzhang.com/solution/wiggle-sort-ii/#tag-highlight-lang-python
class Solution:
    """
    @param: nums: A list of integers
    @return: nothing
    """
    def wiggleSort(self, nums):
        if not nums:
            return
        
        # partition nums into smaller half and bigger half
        # all nums in smaller half <= any num in bigger half
        median = self.find_median(nums)
        
        n = len(nums)

        # reorder the nums from
        # 0 => n-1(odd), (n-2)(even)
        # 1 => n-3
        # 2 => n-5
        # ...
        # (n - 1) / 2 => 0
        # (n - 1) / 2 + 1 => n - 2(odd), n - 1(even)
        # (n - 1) / 2 + 2 => n - 4(odd), n - 3(even)
        # ... 
        def get_index(i):
            if i <= (n - 1) // 2:
                return n - i * 2 - 1 - (n + 1) % 2
            i -= (n - 1) // 2 + 1
            return n - i * 2 - 1 - n % 2
            
        # 3-way partition
        left, i, right = 0, 0, n - 1
        while i <= right:
            if nums[get_index(i)] < median:
                nums[get_index(left)], nums[get_index(i)] = nums[get_index(i)], nums[get_index(left)]
                i += 1
                left += 1
            elif nums[get_index(i)] == median:
                i += 1
            else:
                nums[get_index(right)], nums[get_index(i)] = nums[get_index(i)], nums[get_index(right)]
                right -= 1
        
    def find_median(self, nums):
        return self.find_kth(nums, 0, len(nums) - 1, (len(nums) - 1) // 2)
    
    def find_kth(self, nums, start, end, kth):
        # k is zero based
        left, right = start, end
        mid = nums[(left + right) // 2]
        
        while left <= right:
            while left <= right and nums[left] < mid:
                left += 1
            while left <= right and nums[right] > mid:
                right -= 1
            if left <= right:
                nums[left], nums[right] = nums[right], nums[left]
                left, right = left + 1, right - 1
                
        if kth <= right:
            return self.find_kth(nums, start, right, kth)
        elif kth >= left:
            return self.find_kth(nums, left, end, kth)
        else:
            return nums[kth]

# V2 
# Time:  O(n) ~ O(n^2)
# Space: O(1)
# Tri Partition (aka Dutch National Flag Problem) with virtual index solution. (TLE)
from random import randint
class Solution2(object):
    def wiggleSort(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        def findKthLargest(nums, k):
            left, right = 0, len(nums) - 1
            while left <= right:
                pivot_idx = randint(left, right)
                new_pivot_idx = partitionAroundPivot(left, right, pivot_idx, nums)
                if new_pivot_idx == k - 1:
                    return nums[new_pivot_idx]
                elif new_pivot_idx > k - 1:
                    right = new_pivot_idx - 1
                else:  # new_pivot_idx < k - 1.
                    left = new_pivot_idx + 1

        def partitionAroundPivot(left, right, pivot_idx, nums):
            pivot_value = nums[pivot_idx]
            new_pivot_idx = left
            nums[pivot_idx], nums[right] = nums[right], nums[pivot_idx]
            for i in range(left, right):
                if nums[i] > pivot_value:
                    nums[i], nums[new_pivot_idx] = nums[new_pivot_idx], nums[i]
                    new_pivot_idx += 1
            nums[right], nums[new_pivot_idx] = nums[new_pivot_idx], nums[right]
            return new_pivot_idx

        def reversedTriPartitionWithVI(nums, val):
            def idx(i, N):
                return (1 + 2 * (i)) % N

            N = len(nums) / 2 * 2 + 1
            i, j, n = 0, 0, len(nums) - 1
            while j <= n:
                if nums[idx(j, N)] > val:
                    nums[idx(i, N)], nums[idx(j, N)] = nums[idx(j, N)], nums[idx(i, N)]
                    i += 1
                    j += 1
                elif nums[idx(j, N)] < val:
                    nums[idx(j, N)], nums[idx(n, N)] = nums[idx(n, N)], nums[idx(j, N)]
                    n -= 1
                else:
                    j += 1

        mid = (len(nums) - 1) / 2
        findKthLargest(nums, mid + 1)
        reversedTriPartitionWithVI(nums, nums[mid])