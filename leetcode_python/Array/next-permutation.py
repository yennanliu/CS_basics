"""

31. Next Permutation
Medium

Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers.

If such an arrangement is not possible, it must rearrange it as the lowest possible order (i.e., sorted in ascending order).

The replacement must be in place and use only constant extra memory.

 
Example 1:

Input: nums = [1,2,3]
Output: [1,3,2]

Example 2:

Input: nums = [3,2,1]
Output: [1,2,3]

Example 3:

Input: nums = [1,1,5]
Output: [1,5,1]

Example 4:

Input: nums = [1]
Output: [1]
 

Constraints:

1 <= nums.length <= 100
0 <= nums[i] <= 100


"""

# V0
class Solution:
    def nextPermutation(self, nums):
        i = j = len(nums)-1
        while i > 0 and nums[i-1] >= nums[i]:
            i -= 1
        if i == 0:   # nums are in descending order
            nums.reverse()
            return 
        k = i - 1    # find the last "ascending" position
        while nums[j] <= nums[k]:
            j -= 1
        nums[k], nums[j] = nums[j], nums[k]  
        l, r = k+1, len(nums)-1  # reverse the second part
        while l < r:
            nums[l], nums[r] = nums[r], nums[l]
            l +=1
            r -= 1

# V0'
class Solution(object):
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
# IDEA : 2 POINTERS
# https://leetcode.com/problems/next-permutation/discuss/14054/Python-solution-with-comments.
# https://leetcode.com/problems/next-permutation/discuss/545263/Python-3-Easy-to-understand
class Solution:
    def nextPermutation(self, nums):
        i = j = len(nums)-1
        while i > 0 and nums[i-1] >= nums[i]:
            i -= 1
        if i == 0:   # nums are in descending order
            nums.reverse()
            return 
        k = i - 1    # find the last "ascending" position
        while nums[j] <= nums[k]:
            j -= 1
        nums[k], nums[j] = nums[j], nums[k]  
        l, r = k+1, len(nums)-1  # reverse the second part
        while l < r:
            nums[l], nums[r] = nums[r], nums[l]
            l +=1
            r -= 1

# V1
# https://leetcode.com/problems/next-permutation/discuss/229211/Python-solution
# IDEA :
# First observe that if a list of numbers is in descending order, then there is no lexicographically next greater permutation. Hence for i in range(n-1,0,-1), we search for the first occurrence of i such that nums[i] < nums[i+1]. If no such i exists, the list is in descending order, and we use nums.reverse() to reverse the list in-place. Otherwise, if such i exists, then nums[i-1] will be updated to get the lexicographically next greater permutation. Next, we need to search for the smallest number in nums[i:] that's larger than nums[i-1], and swap it with nums[i-1]. Note that nums[i:] is sorted in descending order. Hence we start with j = i, and while j < n and nums[j] > nums[i-1], we do idx = j, j += 1. When we are out of the while loop, nums[idx] will be the smallest number in nums[i:] that's larger than nums[i]. We then swap nums[idx] and nums[i-1]. After the swap, we just need to sort nums[i:] in ascending order to get the lexicographically next greater permutation. This can be achieved fairly easily in-place, because nums[i:] is already in descending order, and we just need to invert nums[i:] in-place to sort nums[i:] in ascending order.
# To illustrate the algorithm with an example, consider nums = [2,3,1,5,4,2]. It is easy to see that i = 2 is the first i (from the right) such that nums[i] < nums[i+1]. Then we swap nums[2] = 1 with the smallest number in nums[3:] that is larger than 1, which is nums[5] = 2, after which we get nums = [2,3,2,5,4,1]. To get the lexicographically next greater permutation of nums, we just need to sort nums[3:] = [5,4,1] in-place. Finally, we reach nums = [2,3,2,1,4,5].
# Time complexity: O(n), space complexity: O(1).
class Solution(object):
    def nextPermutation(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        n = len(nums)
        for i in range(n-1, 0, -1):
            if nums[i] > nums[i-1]:
                j = i
                while j < n and nums[j] > nums[i-1]:
                    idx = j
                    j += 1
                nums[idx], nums[i-1] = nums[i-1], nums[idx]
                for k in range((n-i)//2):
                    nums[i+k], nums[n-1-k] = nums[n-1-k], nums[i+k]
                break
        else:
            nums.reverse()

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

# V1
# https://leetcode.com/problems/next-permutation/discuss/162049/Explanations-(Java-Python)
class Solution:
    def nextPermutation(self, nums: List[int]) -> None:
        """
        Do not return anything, modify nums in-place instead.
        """      
        first_inc = len(nums) - 2
        
        while first_inc >= 0 and nums[first_inc] >= nums[first_inc + 1]:
            first_inc -= 1
    
        if first_inc != -1:  # nums is not non-increasing as a whole
            for i in range(len(nums) - 1, first_inc, -1):
                if nums[i] > nums[first_inc]:
                    nums[first_inc], nums[i] = nums[i], nums[first_inc]
                    break
    
        nums[first_inc + 1: len(nums)] = nums[first_inc + 1: len(nums)][::-1]

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

# V1
# IDEA : Single Pass Approach
# https://leetcode.com/problems/next-permutation/solution/
# JAVA
# public class Solution {
#     public void nextPermutation(int[] nums) {
#         int i = nums.length - 2;
#         while (i >= 0 && nums[i + 1] <= nums[i]) {
#             i--;
#         }
#         if (i >= 0) {
#             int j = nums.length - 1;
#             while (nums[j] <= nums[i]) {
#                 j--;
#             }
#             swap(nums, i, j);
#         }
#         reverse(nums, i + 1);
#     }
#
#     private void reverse(int[] nums, int start) {
#         int i = start, j = nums.length - 1;
#         while (i < j) {
#             swap(nums, i, j);
#             i++;
#             j--;
#         }
#     }
#
#     private void swap(int[] nums, int i, int j) {
#         int temp = nums[i];
#         nums[i] = nums[j];
#         nums[j] = temp;
#     }
# }

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