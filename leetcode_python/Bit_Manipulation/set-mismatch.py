"""

645. Set Mismatch
Easy

You have a set of integers s, which originally contains all the numbers from 1 to n. Unfortunately, due to some error, one of the numbers in s got duplicated to another number in the set, which results in repetition of one number and loss of another number.

You are given an integer array nums representing the data status of this set after the error.

Find the number that occurs twice and the number that is missing and return them in the form of an array.

 

Example 1:

Input: nums = [1,2,2,4]
Output: [2,3]
Example 2:

Input: nums = [1,1]
Output: [1,2]
 

Constraints:

2 <= nums.length <= 104
1 <= nums[i] <= 104

"""

# V0 
class Solution(object):
    def findErrorNums(self, nums):
        N = len(nums)
        nset = set(nums)
        missing = N * (N + 1) // 2 - sum(nset)
        duplicated = sum(nums) - sum(nset)
        return [duplicated, missing]

# V0'
from collections import Counter
class Solution(object):
    def findErrorNums(self, nums):
        _nums = Counter(nums)
        duplicate = int([i for i in _nums if _nums[i] > 1][0])
        _diff = sum([i for i in range(len(nums)+1)]) - sum(nums) 
        #print ("duplicate = " + str(duplicate))
        return [duplicate, duplicate+_diff]

# V0''
from collections import Counter
class Solution(object):
    def findErrorNums(self, nums):
        cnt = Counter(nums)
        _len = len(nums)
        _nums = [x+1 for x in range(_len)]
        n_sum = sum([x+1 for x in range(_len)])
        repeat = [x for x in list(cnt.keys()) if cnt[x] == 2][0]
        miss = n_sum - (sum(nums) - repeat)
        # print ("repeat = " + str(repeat))
        # print ("miss = " + str(miss))
        return [repeat, miss]

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
