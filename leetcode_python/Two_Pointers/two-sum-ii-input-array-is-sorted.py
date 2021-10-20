"""

167. Two Sum II - Input array is sorted
Easy

Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order, find two numbers such that they add up to a specific target number. Let these two numbers be numbers[index1] and numbers[index2] where 1 <= first < second <= numbers.length.

Return the indices of the two numbers, index1 and index2, as an integer array [index1, index2] of length 2.

The tests are generated such that there is exactly one solution. You may not use the same element twice.

 
Example 1:

Input: numbers = [2,7,11,15], target = 9
Output: [1,2]
Explanation: The sum of 2 and 7 is 9. Therefore index1 = 1, index2 = 2.
Example 2:

Input: numbers = [2,3,4], target = 6
Output: [1,3]
Explanation: The sum of 2 and 4 is 6. Therefore index1 = 1, index2 = 3.
Example 3:

Input: numbers = [-1,0], target = -1
Output: [1,2]
Explanation: The sum of -1 and 0 is -1. Therefore index1 = 1, index2 = 2.
 

Constraints:

2 <= numbers.length <= 3 * 104
-1000 <= numbers[i] <= 1000
numbers is sorted in non-decreasing order.
-1000 <= target <= 1000
The tests are generated such that there is exactly one solution.

"""

# V0
# IDEA : TWO POINTERS
#       -> l = 0, r = len(numbers) - 1
class Solution(object):
    def twoSum(self, numbers, target):
        l = 0
        ### NOTE this
        r = len(numbers) - 1
        res = []
        while r > l:
            #print ("l, r = {}, {}".format(l, r))
            tmp = numbers[l] + numbers[r]
            if tmp == target:
                return [l+1, r+1]
            ### NOTE this
            elif tmp > target:
                r -= 1
            ### NOTE this
            else:
                l += 1
        return [-1, -1]

# V0'
# IDEA : TWO POINTERS 
class Solution(object):
    def twoSum(self, numbers, target):
        left, right = 0, len(numbers) - 1
        while left < right:
            if numbers[left] + numbers[right] == target:
                return [left + 1, right + 1]
            elif numbers[left] + numbers[right] > target:
                right -= 1
            else:
                left += 1

# V0'
# IDEA : DICT
class Solution(object):
    def twoSum(self, numbers, target):
        num_dict = {}
        for i, num in enumerate(numbers):
            if (target - num) in num_dict:
                return [num_dict[target - num], i + 1]
            num_dict[num] = i + 1

# V0''
# IDEA : BINARY SEARCH 
class Solution(object):
    def twoSum(self, numbers, target):
        for i in range(len(numbers)):
            l, r = i+1, len(numbers)-1
            tmp = target - numbers[i]
            while l <= r:
                mid = l + (r-l)//2
                if numbers[mid] == tmp:
                    return [i+1, mid+1]
                elif numbers[mid] < tmp:
                    l = mid+1
                else:
                    r = mid-1
                    
# V1 
# https://blog.csdn.net/coder_orz/article/details/52388066
# IDEA : TWO POINTER
class Solution(object):
    def twoSum(self, numbers, target):
        """
        :type numbers: List[int]
        :type target: int
        :rtype: List[int]
        """
        left, right = 0, len(numbers) - 1
        while left < right:
            if numbers[left] + numbers[right] == target:
                return [left + 1, right + 1]
            elif numbers[left] + numbers[right] > target:
                right -= 1
            else:
                left += 1

### Test case
s=Solution()
assert s.twoSum([1,2,3,4],5) == [1,4]
assert s.twoSum([1,2,3,4],6) == [2,4]
assert s.twoSum([1,2,3,4],7) == [3,4]
assert s.twoSum([1,2],3) == [1,2]
assert s.twoSum([1,1,1,1],2) == [1,4]
assert s.twoSum([-1,1,2,3],0) == [1,2]
assert s.twoSum([-1,1,2,3],2) == [1,4]
assert s.twoSum([],2) == None
assert s.twoSum([],0) == None
       
# V1' 
# https://blog.csdn.net/coder_orz/article/details/52388066
# IDEA : DICT
class Solution(object):
    def twoSum(self, numbers, target):
        """
        :type numbers: List[int]
        :type target: int
        :rtype: List[int]
        """
        num_dict = {}
        for i, num in enumerate(numbers):
            if (target - num) in num_dict:
                return [num_dict[target - num], i + 1]
            num_dict[num] = i + 1

# V1''
# https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/discuss/51249/Python-different-solutions-(two-pointer-dictionary-binary-search).
# IDEA : BINARY SEARCH 
class Solution(object):
    def twoSum(self, numbers, target):
        for i in range(len(numbers)):
            l, r = i+1, len(numbers)-1
            tmp = target - numbers[i]
            while l <= r:
                mid = l + (r-l)//2
                if numbers[mid] == tmp:
                    return [i+1, mid+1]
                elif numbers[mid] < tmp:
                    l = mid+1
                else:
                    r = mid-1

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def twoSum(self, nums, target):
        start, end = 0, len(nums) - 1

        while start != end:
            sum = nums[start] + nums[end]
            if sum > target:
                end -= 1
            elif sum < target:
                start += 1
            else:
                return [start + 1, end + 1]