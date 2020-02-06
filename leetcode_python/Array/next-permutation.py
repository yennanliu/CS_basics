# V0 
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
        num[k + 1:] = num[:k:-1] ### dounle check here ###

# V1 
# https://zxi.mytechroad.com/blog/algorithms/array/leetcode-31-next-permutation/
# VIDEO DEMO 
# https://www.youtube.com/watch?v=1ja5s9TmwZM
class Solution:
  def nextPermutation(self, nums):
    n = len(nums)
    i = n - 2
    while i >= 0 and nums[i] >= nums[i + 1]: 
        i -= 1
    if i >= 0: # if can find such "i" fit what problem need 
      j = n - 1
      while j >= 0 and nums[j] <= nums[i]: 
        j -= 1
      nums[i], nums[j] = nums[j], nums[i]
    # reverse
    nums[i+1:] = nums[i+1:][::-1]

# V1' 
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

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82113409
# DMEO
# STEP 1) given 12431
# STEP 2) 12 431 (431 is the decreasing substring) (start from the right hand side of orignal list)
# STEP 3) 12 134 (reverse 431 -> 134)
# STEP 4) 13 124 (find the 1st elment in 134 that bigger than 2)
# STEP 5) answer = 13124
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
        demo :
            nums = [1,2,3,4,5,6]
            i, j = 1,3 
            r = reverse(nums, i, j)
            -> r = [1, 4, 3, 2, 5, 6]
        """
        for k in range(i, (i + j) / 2 + 1):
            self.swap(nums, k, i + j - k)

        
    def swap(self, nums, i, j):
        """
        contains i and j.
        """
        nums[i], nums[j] = nums[j], nums[i]

# V1'''
# https://www.jiuzhang.com/solution/next-permutation/#tag-highlight-lang-python
class Solution:
    # @param num :  a list of integer
    # @return : a list of integer
    def nextPermutation(self, num):
        # write your code here
        for i in range(len(num)-2, -1, -1):
            if num[i] < num[i+1]:
                break
        else:
            num.reverse()
            return num
        for j in range(len(num)-1, i, -1):
            if num[j] > num[i]:
                num[i], num[j] = num[j], num[i]
                break
        for j in range(0, (len(num) - i)//2):
            num[i+j+1], num[len(num)-j-1] = num[len(num)-j-1], num[i+j+1]
        return num

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
