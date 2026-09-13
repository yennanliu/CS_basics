"""

491. Non-decreasing Subsequences
Medium

Given an integer array nums, return all the different possible non-decreasing subsequences of the given array with at least two elements. You may return the answer in any order.

Example 1:

Input: nums = [4,6,7,7]
Output: [[4,6],[4,6,7],[4,6,7,7],[4,7],[4,7,7],[6,7],[6,7,7],[7,7]]

Example 2:

Input: nums = [4,4,3,2,1]
Output: [[4,4]]

Constraints:

1 <= nums.length <= 15
-100 <= nums[i] <= 100

"""

# V0
# IDEA : DFS
# time = O(2^n * n), n = len(nums)
# space = O(2^n * n)
class Solution(object):
    def findSubsequences(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        res = set()
        self.dfs(nums, 0, res, [])
        return map(list, res)

    def dfs(self, nums, index, res, path):
        if len(path) >= 2:
            res.add(tuple(path))
        for i in range(index, len(nums)):
            ### be aware of it
            if not path or nums[i] >= path[-1]:
                ### be aware of it
                self.dfs(nums, i + 1, res, path + [nums[i]])

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79827505
# IDEA : DFS
# time = O(2^n * n), n = len(nums)
# space = O(2^n * n)
class Solution(object):
    def findSubsequences(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        res = set()
        self.dfs(nums, 0, res, [])
        return map(list, res)

    def dfs(self, nums, index, res, path):
        if len(path) >= 2:
            res.add(tuple(path))
        for i in range(index, len(nums)):
            if not path or nums[i] >= path[-1]:
                self.dfs(nums, i + 1, res, path + [nums[i]])

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79827505
# IDEA : DP
# time = O(2^n * n), n = len(nums)
# space = O(2^n * n)
class Solution(object):
    def findSubsequences(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        dp = set()
        for n in nums:
            for y in list(dp):
                if n >= y[-1]:
                    dp.add(y + (n,))
            dp.add((n,))
        return list(e for e in dp if len(e) > 1)

# V1''
# https://www.jiuzhang.com/solution/increasing-subsequences/#tag-highlight-lang-python
# time = O(2^n * n), n = len(nums)
# space = O(2^n * n)
class Solution(object):
    def findSubsequences(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        res = []
        self.subsets(nums, 0, [], res)
        return res
        
    def subsets(self, nums, index, temp, res):
        if len(nums) >= index and len(temp) >= 2:
            res.append(temp[:])
        used = {}
        for i in range(index, len(nums)):
            if len(temp) > 0 and temp[-1] > nums[i]: continue
            if nums[i] in used: continue
            used[nums[i]] = True
            temp.append(nums[i])
            self.subsets(nums, i+1, temp, res)
            temp.pop()
            
# V2
