"""

594. Longest Harmonious Subsequence
Easy

We define a harmonious array as an array where the difference between its maximum value and its minimum value is exactly 1.

Given an integer array nums, return the length of its longest harmonious subsequence among all its possible subsequences.

Example 1:

Input: nums = [1,3,2,2,5,2,3,7]
Output: 5
Explanation:

The longest harmonious subsequence is [3,2,2,2,3].

Example 2:

Input: nums = [1,2,3,4]
Output: 2
Explanation:

The longest harmonious subsequences are [1,2], [2,3], and [3,4], all of which have a length of 2.

Example 3:

Input: nums = [1,1,1,1]
Output: 0
Explanation:

No harmonic subsequence exists.

Constraints:

1 <= nums.length <= 2 * 10^4
-10^9 <= nums[i] <= 10^9

"""

# time = O(n)
# space = O(n)
# We define a harmonious array is an array where the difference
# between its maximum value and its minimum value is exactly 1.
#
# Now, given an integer array, you need to find the length of its
# longest harmonious subsequence among all its possible subsequences.
#
# Example 1:
# Input: [1,3,2,2,5,2,3,7]
# Output: 5
# Explanation: The longest harmonious subsequence is [3,2,2,2,3].
# Note: The length of the input array will not exceed 20,000.

# V0
import collections
# time = O(n), n = len(nums)
# space = O(n)
class Solution(object):
    def findLHS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        count = collections.Counter(nums)
        res = 0
        for num in count.keys():
            if num + 1 in count:
                res = max(res, count[num] + count[num + 1])
        return res

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79233752
import collections
# time = O(n), n = len(nums)
# space = O(n)
class Solution(object):
    def findLHS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        counter = collections.Counter(nums)
        nums_set = set(nums)
        longest = 0
        for num in nums_set:
            if num + 1 in counter:
                longest = max(longest, counter[num] + counter[num + 1])
        return longest

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79233752
import collections
# time = O(n), n = len(nums)
# space = O(n)
class Solution(object):
    def findLHS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        count = collections.Counter(nums)
        res = 0
        for num in count.keys():
            if num + 1 in count:
                res = max(res, count[num] + count[num + 1])
        return res

# V1''
# http://bookshadow.com/weblog/2017/05/21/leetcode-longest-harmonious-subsequence/
import collections
# time = O(n log n), n = len(nums) (sorted over unique keys)
# space = O(n)
class Solution(object):
    def findLHS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        cnt = collections.Counter(nums)
        ans = 0
        lastKey = lastVal = None
        for key, val in sorted(cnt.items()):
            if lastKey is not None and lastKey + 1 == key:
                ans = max(ans, val + lastVal)
            lastKey, lastVal = key, val
        return ans

# V1'''
# https://www.jiuzhang.com/solution/longest-harmonious-subsequence/#tag-highlight-lang-python
# time = O(n), n = len(nums)
# space = O(n)
class Solution:
    """
    @param nums: a list of integers
    @return: return a integer
    """

    def findLHS(self, nums):
        # write your code here
        vis = {}
        ans = 0
        for num in nums:
            if num in vis:
                vis[num] += 1
            else:
                vis[num] = 1
        for num in nums:
            if num in vis and num - 1 in vis:
                ans = max(ans, vis[num] + vis[num - 1])
        return ans

# V2
# time = O(n), n = len(nums)
# space = O(n)
import collections
class Solution(object):
    def findLHS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        lookup = collections.defaultdict(int)
        result = 0
        for num in nums:
            lookup[num] += 1
            for diff in [-1, 1]:
                if (num + diff) in lookup:
                    result = max(result, lookup[num] + lookup[num + diff])
        return result