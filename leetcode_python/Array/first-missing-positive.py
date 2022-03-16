"""

41. First Missing Positive
Hard

Given an unsorted integer array nums, return the smallest missing positive integer.

You must implement an algorithm that runs in O(n) time and uses constant extra space.

 

Example 1:

Input: nums = [1,2,0]
Output: 3
Example 2:

Input: nums = [3,4,-1,1]
Output: 2
Example 3:

Input: nums = [7,8,9,11,12]
Output: 1
 

Constraints:

1 <= nums.length <= 5 * 105
-231 <= nums[i] <= 231 - 1

"""

# V0

# V1
# https://leetcode.com/problems/first-missing-positive/discuss/231337/Python-solution
# IDEA :
# Idea: change nums[n-1] to float('inf') for all n in nums if 1 <= n <= len(nums), meaning that we have seen n in nums. Then we traverse nums once more, and find the first idx such that nums[idx] != float('inf'), then idx+1 will be the first missing positive in nums. If no such idx exists, it means that we have seen 1, 2, ..., len(nums) in nums, hence the first missing positive is len(nums)+1.
# Time complexity: O(n), space complexity: O(1).
class Solution:
    def firstMissingPositive(self, nums):
        for i, n in enumerate(nums):
            if n < 0:
                continue
            else:
                while n <= len(nums) and n > 0:
                    tmp = nums[n-1]
                    nums[n-1] = float('inf')
                    n = tmp
        for i in range(len(nums)):
            if nums[i] != float('inf'):
                return i+1
            
        return len(nums)+1

# V1'
# IDEA :  Index as a hash key.
# https://leetcode.com/problems/first-missing-positive/solution/
# /doc/pic/first-missing-positive.png
class Solution:
    def firstMissingPositive(self, nums: List[int]) -> int:
        n = len(nums)
        
        # Base case.
        if 1 not in nums:
            return 1
        
        # Replace negative numbers, zeros,
        # and numbers larger than n by 1s.
        # After this convertion nums will contain 
        # only positive numbers.
        for i in range(n):
            if nums[i] <= 0 or nums[i] > n:
                nums[i] = 1
        
        # Use index as a hash key and number sign as a presence detector.
        # For example, if nums[1] is negative that means that number `1`
        # is present in the array. 
        # If nums[2] is positive - number 2 is missing.
        for i in range(n): 
            a = abs(nums[i])
            # If you meet number a in the array - change the sign of a-th element.
            # Be careful with duplicates : do it only once.
            if a == n:
                nums[0] = - abs(nums[0])
            else:
                nums[a] = - abs(nums[a])
            
        # Now the index of the first positive number 
        # is equal to first missing positive.
        for i in range(1, n):
            if nums[i] > 0:
                return i
        
        if nums[0] > 0:
            return n
            
        return n + 1

# V1''
# https://leetcode.com/problems/first-missing-positive/discuss/697416/clean-python-code
class Solution:
    def firstMissingPositive(self, nums: List[int]) -> int:
        L = len(nums)
        for i in range(L):
            if nums[i] <= 0 or nums[i]>L:
                nums[i] = -1

        for i in range(L):
            tmp = nums[i]
            if tmp > 0:
                nums[i] = -1
                while tmp > 0:
                    nums[tmp-1], tmp = 0, nums[tmp-1]

        for i in range(L):
            if nums[i] == -1:
                return i+1

        return L+1

# V1'''
# https://leetcode.com/problems/first-missing-positive/discuss/17164/Python-solution-with-detailed-explanation
# IDEA :
# Solution using extra space: O(N) time and space
# If the length of the nums array is N, then the first missing positive will be between 1 to N+1. Think Why N+1? We can have in the array 1 to N.
# Take an temp array of size N and for any number x in nums such that 1<=x<=N, mark temp[x-1]. Then simply walk the temp array and report the first unmarked index.
class Solution(object):
    def firstMissingPositive(self, nums):
        temp, N = [None]*len(nums), len(nums)
        for x in nums:
            if 1<=x<=N:
                temp[x-1] = x
        for i in range(N):
            if temp[i] == None:
                return i+1
        return N+1

# V1'''''
# https://leetcode.com/problems/first-missing-positive/discuss/17164/Python-solution-with-detailed-explanation
# IDEA :
# Optimized solution with O(1) Space
# Simply traverse the nums array and put any number within [1, N] in their right place. For example if 2 is in that input, then put 2 at index 1.
# Now traverse this "shuffled" array again. You expect 1 at 0th index. Otherwise it is missing. Then you expect 2 at 1st index and so on.
# Above idea can be a little tricky. What about cases like [1] and [1,1] - i.e. 1 is in its place or there are duplicates - we need to advance pointer regardless.
class Solution(object):
    def firstMissingPositive(self, nums):
        N, i = len(nums), 0
        while i < N:
            while 1<=nums[i]<=N:
                idx_expected = nums[i]-1
                if nums[i] == nums[idx_expected]:
                    break
                nums[i], nums[idx_expected] = nums[idx_expected], nums[i]
            i = i + 1
        for i in range(N):
            if nums[i] != i+1:
                return i+1
        return N+1

# V2