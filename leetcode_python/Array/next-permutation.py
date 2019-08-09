# V0 

# V1 
# http://bookshadow.com/weblog/2016/09/09/leetcode-next-permutation/
class Solution(object):
    def nextPermutation(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        size = len(nums)
        for x in range(size - 1, -1, -1):
            if nums[x - 1] < nums[x]:
                break
        if x > 0:
            for y in range(size - 1, -1, -1):
                if nums[y] > nums[x - 1]:
                    nums[x - 1], nums[y] = nums[y], nums[x - 1]
                    break
        for z in range((size - x) / 2):
            nums[x + z], nums[size - z - 1] = nums[size - z - 1], nums[x + z]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82113409
class Solution(object):
    def nextPermutation(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        n = len(nums)
        i = n - 1
        while i > 0 and nums[i] <= nums[i - 1]:
            i -= 1
        self.reverse(nums, i, n - 1)
        if i > 0:
            for j in range(i, n):
                if nums[j] > nums[i-1]:
                    self.swap(nums, i-1, j)
                    break
        
    def reverse(self, nums, i, j):
        """
        contains i and j.
        """
        for k in range(i, (i + j) / 2 + 1):
            self.swap(nums, k, i + j - k)

        
    def swap(self, nums, i, j):
        """
        contains i and j.
        """
        nums[i], nums[j] = nums[j], nums[i]
        
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param {integer[]} nums
    # @return {void} Do not return anything, modify nums in-place instead.
    def nextPermutation(self, num):
        k, l = -1, 0
        for i in range(len(num) - 1):
            if num[i] < num[i + 1]:
                k = i

        if k == -1:
            num.reverse()
            return

        for i in range(k + 1, len(num)):
            if num[i] > num[k]:
                l = i
        num[k], num[l] = num[l], num[k]
        num[k + 1:] = num[:k:-1]