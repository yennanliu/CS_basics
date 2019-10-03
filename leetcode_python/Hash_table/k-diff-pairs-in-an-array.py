# Time:  O(n)
# Space: O(n)
# Total Accepted: 5671
# Total Submissions: 20941
# Difficulty: Easy
# Contributors: murali.kf370
# Given an array of integers and an integer k,
# you need to find the number of unique k-diff pairs in the array.
# Here a k-diff pair is defined as an integer pair (i, j),
# where i and j are both numbers in the array and their absolute difference is k.
#
# Example 1:
# Input: [3, 1, 4, 1, 5], k = 2
# Output: 2
# Explanation: There are two 2-diff pairs in the array, (1, 3) and (3, 5).
# Although we have two 1s in the input, we should only return the number of unique pairs.
# Example 2:
# Input:[1, 2, 3, 4, 5], k = 1
# Output: 4
# Explanation: There are four 1-diff pairs in the array, (1, 2), (2, 3), (3, 4) and (4, 5).
# Example 3:
# Input: [1, 3, 1, 5, 4], k = 0
# Output: 1
# Explanation: There is one 0-diff pair in the array, (1, 1).
# Note:
# The pairs (i, j) and (j, i) count as the same pair.
# The length of the array won't exceed 10,000.
# All the integers in the given input belong to the range: [-1e7, 1e7].

# V0 
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
