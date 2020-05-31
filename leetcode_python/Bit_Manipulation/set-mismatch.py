# 645. Set Mismatch
# Easy
#
#
# Add to List
#
# The set S originally contains numbers from 1 to n. But unfortunately, due to the data error, one of the numbers in the set got duplicated to another number in the set, which results in repetition of one number and loss of another number.
#
# Given an array nums representing the data status of this set after the error. Your task is to firstly find the number occurs twice and then find the number that is missing. Return them in the form of an array.
#
# Example 1:
# Input: nums = [1,2,2,4]
# Output: [2,3]
# Note:
# The given array size will in the range [2, 10000].
# The given array's numbers won't have any order.


# V0 
class Solution(object):
    def findErrorNums(self, nums):
        N = len(nums)
        nset = set(nums)
        missing = N * (N + 1) // 2 - sum(nset)
        duplicated = sum(nums) - sum(nset)
        return [duplicated, missing]

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79247916
# http://bookshadow.com/weblog/2017/07/24/leetcode-set-mismatch/
# IDEA : SUM
class Solution(object):
    def findErrorNums(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        N = len(nums)
        nset = set(nums)
        missing = N * (N + 1) // 2 - sum(nset)
        duplicated = sum(nums) - sum(nset)
        return [duplicated, missing]

### Test case :
s=Solution()
assert s.findErrorNums([1,2,2,4]) == [2,3]       
assert s.findErrorNums([1,1,3,4]) == [1,2]       
assert s.findErrorNums([]) == [0,0]       
assert s.findErrorNums([1,2,3]) == [0,0]       

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79247916
# http://bookshadow.com/weblog/2017/07/24/leetcode-set-mismatch/
# IDEA : HASH TABLE 
class Solution(object):
    def findErrorNums(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        hashs = [0] * len(nums)
        missing = -1
        for i in range(len(nums)):
            hashs[nums[i] - 1] += 1
        return [hashs.index(2) + 1, hashs.index(0) + 1]

# V1''
# https://leetcode.com/problems/set-mismatch/discuss/105558/Oneliner-Python
class Solution(object):
    def findErrorNums(self, nums):
        return [sum(nums) - sum(set(nums)), sum(range(1, len(nums)+1)) - sum(set(nums))]

# V1'''
# https://leetcode.com/problems/set-mismatch/discuss/345631/Multiple-Python-Solution
class Solution:
    def findErrorNums(self, nums):
        x = sum(nums) - sum(set(nums))
        y = sum(range(len(nums)+1))-(sum(nums)-x)
        return [x,y]

# V1''''
# https://leetcode.com/problems/set-mismatch/discuss/345631/Multiple-Python-Solution
class Solution:
    def findErrorNums(self, nums):
       count = [0] * (len(nums)+1)
        for x in nums:
            count[x] += 1
            
        twice = z = 0
        for x in range(1, len(nums)+1):
            if count[x] == 2:
                twice = x
            if count[x] == 0:
                z = x
        return [twice, z]

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findErrorNums(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        x_xor_y = 0
        for i in range(len(nums)):
            x_xor_y ^= nums[i] ^ (i+1)
        bit =  x_xor_y & ~(x_xor_y-1)
        result = [0] * 2
        for i, num in enumerate(nums):
            result[bool(num & bit)] ^= num
            result[bool((i+1) & bit)] ^= i+1
        if result[0] not in nums:
            result[0], result[1] = result[1], result[0]
        return result


# Time:  O(n)
# Space: O(1)
class Solution2(object):
    def findErrorNums(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        result = [0] * 2
        for i in nums:
            if nums[abs(i)-1] < 0:
                result[0] = abs(i)
            else:
                nums[abs(i)-1] *= -1
        for i in range(len(nums)):
            if nums[i] > 0:
                result[1] = i+1
            else:
                nums[i] *= -1
        return result

# Time:  O(n)
# Space: O(1)
class Solution3(object):
    def findErrorNums(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        N = len(nums)
        x_minus_y = sum(nums) - N*(N+1)//2
        x_plus_y = (sum(x*x for x in nums) - N*(N+1)*(2*N+1)/6) // x_minus_y
        return (x_plus_y+x_minus_y) // 2, (x_plus_y-x_minus_y) // 2
