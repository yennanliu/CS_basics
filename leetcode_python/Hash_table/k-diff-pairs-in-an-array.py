"""

532. K-diff Pairs in an Array
Medium

Given an array of integers nums and an integer k, return the number of unique k-diff pairs in the array.

A k-diff pair is an integer pair (nums[i], nums[j]), where the following are true:

0 <= i < j < nums.length
|nums[i] - nums[j]| == k
Notice that |val| denotes the absolute value of val.

 

Example 1:

Input: nums = [3,1,4,1,5], k = 2
Output: 2
Explanation: There are two 2-diff pairs in the array, (1, 3) and (3, 5).
Although we have two 1s in the input, we should only return the number of unique pairs.
Example 2:

Input: nums = [1,2,3,4,5], k = 1
Output: 4
Explanation: There are four 1-diff pairs in the array, (1, 2), (2, 3), (3, 4) and (4, 5).
Example 3:

Input: nums = [1,3,1,5,4], k = 0
Output: 1
Explanation: There is one 0-diff pair in the array, (1, 1).
Example 4:

Input: nums = [1,2,4,4,3,3,0,9,2,3], k = 3
Output: 2
Example 5:

Input: nums = [-1,-2,-3], k = 1
Output: 2
 

Constraints:

1 <= nums.length <= 104
-107 <= nums[i] <= 107
0 <= k <= 107

"""


# V0
# IDEA : SORT + BRUTE FORCE + BREAK
class Solution(object):
    def findPairs(self, nums, k):
        d = {}
        res = []
        # NOTE : we sort here
        nums.sort()
        for i in range(len(nums)-1):
            for j in range(i+1, len(nums)):
                if abs(nums[i] - nums[j]) == k:
                    tmp = [nums[i],nums[j]]
                    tmp.sort()
                    if tmp not in res:
                        res.append(tmp)
                if abs(nums[j] - nums[i]) > k:
                    break
        return res

# V0 
import collections
class Solution(object):
    def findPairs(self, nums, k):
        answer = 0
        counter = collections.Counter(nums)
        for num in set(nums):
            if k > 0 and num + k in counter: # | a - b | = k -> a - b = +k or -k, but here don't have to deal with "a - b = -k" case, since this sutuation will be covered when go through whole nums  
                answer += 1
            if k == 0 and counter[num] > 1:  # for cases k = 0 ->  pair like (1,1) will work. (i.e. 1 + (-1))
                answer += 1
        return answer
        
# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79255633
# IDEA : collections.Counter
# IDEA : FIND # OF PAIR THAT SUM-PAIR = K  (i.e. for pair(a,b), -> a + b = k)
# -> a+ b = k 
# -> a = k - b 
import collections
class Solution(object):
    def findPairs(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        answer = 0
        counter = collections.Counter(nums)
        for num in set(nums):
            if k > 0 and num + k in counter: # | a - b | = k -> a - b = +k or -k, but here don't have to deal with "a - b = -k" case, since this sutuation will be covered when go through whole nums  
                answer += 1
            if k == 0 and counter[num] > 1:  # for cases k = 0 ->  pair like (1,1) will work. (i.e. 1 + (-1))
                answer += 1
        return answer

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79255633
# IDEA : collections.Counter 
class Solution(object):
    def findPairs(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        res = 0
        if k < 0: return 0
        elif k == 0:
            count = collections.Counter(nums)
            for n, v in count.items():
                if v >= 2:
                    res += 1
            return res
        else:
            nums = set(nums)
            for num in nums:
                if num + k in nums:
                    res += 1
            return res

# V1''
# https://www.jiuzhang.com/solution/k-diff-pairs-in-an-array/#tag-highlight-lang-python
class Solution:
    """
    @param nums: an array of integers
    @param k: an integer
    @return: the number of unique k-diff pairs
    """

    def findPairs(self, nums, k):
        # Write your code here
        nums.sort()
        n, j, ans = len(nums), 0, 0
        for i in range(n):
            if i == j:
                j += 1
            while i + 1 < n and nums[i] == nums[i + 1]:
                i += 1
            while j + 1 < n and nums[j] == nums[j + 1]:
                j += 1
            while j < n and abs(nums[i] - nums[j]) < k:
                j += 1
            if j >= n:
                break
            if abs(nums[i] - nums[j]) == k:
                ans, j = ans + 1, j + 1
        return ans

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def findPairs(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        if k < 0: return 0
        result, lookup = set(), set()
        for num in nums:
            if num-k in lookup:
                result.add(num-k)
            if num+k in lookup:
                result.add(num)
            lookup.add(num)
        return len(result)