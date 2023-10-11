"""

90. Subsets II
Medium

Given an integer array nums that may contain duplicates, return all possible 
subsets
 (the power set).

The solution set must not contain duplicate subsets. Return the solution in any order.

 

Example 1:

Input: nums = [1,2,2]
Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]
Example 2:

Input: nums = [0]
Output: [[],[0]]
 

Constraints:

1 <= nums.length <= 10
-10 <= nums[i] <= 10

"""

# V0
# IDEA : BACKTRACK + LC 078 Subsets + dict
from collections import Counter
class Solution:
    def subsetsWithDup(self, nums):
        result = []
        path = []
        # sort nums, so same element are in neighbor
        nums.sort()
        # NOTE : we use _cnt for record how count of element and used element
        _cnt = Counter(nums)
        self.backtracking(nums, 0, path, result, _cnt)
        return result

    def backtracking(self, nums, startIndex, path, result, _cnt):
        if path not in result: # this line is optional
            result.append(path[:])  # NOTE here
        if startIndex >= len(nums):  # optional
            return
        for i in range(startIndex, len(nums)): # NOTE here : we start from startIndex every time
            if _cnt[nums[i]] > 0:
                _cnt[nums[i]] -= 1
                path.append(nums[i])
                self.backtracking(nums, i + 1, path, result, _cnt) # NOTE here
                path.pop(-1) # NOTE here
                _cnt[nums[i]] += 1

# V0'
# IDEA : BACKTRACKING + LC 078 Subsets + dict
from collections import Counter
class Solution(object):
    def subsetsWithDup(self, nums):
        def help(start, tmp, _cnt):
            tmp.sort()
            if tmp not in res:
                res.append(tmp)
            if start >= len(nums):
                return
            for i in range(start, len(nums)):
                if _cnt[nums[i]]  > 0:
                    _cnt[nums[i]] -= 1
                    help(start+1, tmp + [nums[i]], _cnt)
                    """
                    NOTE : here we "undo" the "_cnt[nums[i]] -= 1" op,
                          -> so next recursive can still have the "capacity" of such element
                    """
                    _cnt[nums[i]] += 1

        # edge case
        if not nums:
            return []

        # edge case
        if len(nums) == 1:
            res = [[]]
            res.append([nums[0]])
            return res

        res = [[]]
        _cnt = Counter(nums)
        help(0, [], _cnt)
        print ("res = " + str(res))
        return res

# V0'''' 
# IDEA : backtrack + seen
# https://github.com/youngyangyang04/leetcode-master/blob/master/problems/0090.%E5%AD%90%E9%9B%86II.md
class Solution:
    def subsetsWithDup(self, nums):
        result = []
        path = []
        used = [False] * len(nums)
        nums.sort()  # 去重需要排序
        self.backtracking(nums, 0, used, path, result)
        return result

    def backtracking(self, nums, startIndex, used, path, result):
        result.append(path[:])  # 收集子集
        for i in range(startIndex, len(nums)):
            # used[i - 1] == True，说明同一树枝 nums[i - 1] 使用过
            # used[i - 1] == False，说明同一树层 nums[i - 1] 使用过
            # 而我们要对同一树层使用过的元素进行跳过
            if i > 0 and nums[i] == nums[i - 1] and not used[i - 1]:
                continue
            path.append(nums[i])
            used[i] = True
            self.backtracking(nums, i + 1, used, path, result)
            used[i] = False
            path.pop()

# V0'
# IDEA : BRUTE FORCE
class Solution:
    def subsetsWithDup(self, nums):
        # small trick (init with a null array)
        ans=[[]]
        for i in nums:
            for l in list(ans):
                # sorted here, since we want to the "non-duplicated" power set
                temp=sorted(l+[i])
                # avoid duplicated
                if temp not in ans:
                    ans.append(temp) 
        return ans

# V0''
# IDEA : DFS
### NOTE :
# in py, we can pass the var into the "sub func" directly, but no need to put it in the func variable
# demo : 
# def test():
#     def func(x):
#         print ("x = " + str(x) + " y = " + str(y))
#         for i in range(3):
#             z.append(i)
#
#     x = 0
#     y = 100
#     z = []
#     func(x)
# test()
# print (z)
class Solution(object):
    def subsetsWithDup(self, nums):
        ### NOTE the param
        def dfs(depth, start, valueList):
            # if valueList not in res, then add
            if valueList not in res:
                res.append(valueList)
            ### NOTE : this condition
            if depth == len(nums):
                return
            for i in range(start, len(nums)):
                ### NOTE how it update param
                dfs(depth + 1, i + 1, valueList+[nums[i]])
        
        # need sort first, so make sure what we get is power set
        nums.sort()
        res = []
        dfs(0, 0, [])
        return res

# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/subsets-ii/discuss/221114/Python-iterative-solution
class Solution:
    def subsetsWithDup(self, nums):
        ans=[[]]
        for i in nums:
            for l in list(ans):
                # sorted here, since we want to the "non-duplicated" power set
                temp=sorted(l+[i])
                if temp not in ans:
                    ans.append(temp) 
        return ans

# V1
# IDEA : DFS 
# https://www.cnblogs.com/loadofleaf/p/5395066.html
#  SAME AS # 78 subset
class Solution(object):
    def subsetsWithDup(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        def dfs(depth, start, valueList):
            if valueList not in res:
                res.append(valueList)
            if depth == len(nums):
                return
            for i in range(start, len(nums)):
                dfs(depth + 1, i + 1, valueList+[nums[i]])
        
        nums.sort()
        res = []
        dfs(0, 0, [])
        return res

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79785548
# IDEA : BACKTRACKING  + DFS 
# DEMO :
# In [12]: nums = [1,2,3]
# In [13]: Solution().subsets(nums)
# i =  0
# path :  [] , [nums[i]] [1]
# path + [nums[i]] : [1]

# i =  1
# path :  [1] , [nums[i]] [2]
# path + [nums[i]] : [1, 2]

# i =  2
# path :  [1, 2] , [nums[i]] [3]
# path + [nums[i]] : [1, 2, 3]

# i =  2
# path :  [1] , [nums[i]] [3]
# path + [nums[i]] : [1, 3]

# i =  1
# path :  [] , [nums[i]] [2]
# path + [nums[i]] : [2]

# i =  2
# path :  [2] , [nums[i]] [3]
# path + [nums[i]] : [2, 3]

# i =  2
# path :  [] , [nums[i]] [3]
# path + [nums[i]] : [3]

# Out[13]: [[], [1], [1, 2], [1, 2, 3], [1, 3], [2], [2, 3], [3]]
class Solution(object):
    def subsets(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        res = []
        self.dfs(nums, 0, res, [])
        return res
    
    def dfs(self, nums, index, res, path):
        res.append(path)
        for i in range(index, len(nums)):
            self.dfs(nums, i + 1, res, path + [nums[i]]) # [] + [1] = [1], [] + [1] + [2] = [1,2]

# V1''
# IDEA : BACKTRACKING
# https://leetcode.com/problems/subsets-ii/discuss/690114/Python-Backtracking-Solution
class Solution:
    def subsetsWithDup(self, nums):
        nums.sort()
        self.res = []
        self.backtrack(nums, [], 0)
        return self.res
    
    def backtrack(self, nums, current, start):
        self.res.append(current)
        if start > len(nums):
            return
        for i in range(start, len(nums)):
            if i > start and nums[i-1] == nums[i]:
                continue
            self.backtrack(nums, current + [nums[i]], i + 1)

# V1''
# https://www.jianshu.com/p/b6c831622498
from itertools import combinations
class Solution(object):
    def subsetsWithDup(self, nums):
        res = []
        nums.sort()
        for i in range(len(nums) + 1):
            temp = combinations(nums, i)
            guo = [list(i) for i in set(temp)]
            res.extend(guo)
        return res

# V1'''
# https://www.jianshu.com/p/b6c831622498
class Solution(object):
    def subsetsWithDup(self, nums):
        res = [[]]
        nums.sort()
        temp = 0
        for i in range(len(nums) + 1):
            start = temp if i >= 1 and nums[i] == nums[i -1] else 0
            temp = len(res)
            for j in range(start, temp):
                res.append(res[j] + [nums[i]])
        return res

# V2 
# Time:  O(n * 2^n)
# Space: O(1)
class Solution(object):
    def subsetsWithDup(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        nums.sort()
        result = [[]]
        previous_size = 0
        for i in xrange(len(nums)):
            size = len(result)
            for j in xrange(size):
                # Only union non-duplicate element or new union set.
                if i == 0 or nums[i] != nums[i - 1] or j >= previous_size:
                    result.append(list(result[j]))
                    result[-1].append(nums[i])
            previous_size = size
        return result

# Time:  O(n * 2^n) ~ O((n * 2^n)^2)
# Space: O(1)
class Solution2(object):
    def subsetsWithDup(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        result = []
        i, count = 0, 1 << len(nums)
        nums.sort()

        while i < count:
            cur = []
            for j in xrange(len(nums)):
                if i & 1 << j:
                    cur.append(nums[j])
            if cur not in result:
                result.append(cur)
            i += 1
        return result

# Time:  O(n * 2^n) ~ O((n * 2^n)^2)
# Space: O(1)
class Solution3(object):
    def subsetsWithDup(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        result = []
        self.subsetsWithDupRecu(result, [], sorted(nums))
        return result

    def subsetsWithDupRecu(self, result, cur, nums):
        if not nums:
            if cur not in result:
                result.append(cur)
        else:
            self.subsetsWithDupRecu(result, cur, nums[1:])
            self.subsetsWithDupRecu(result, cur + [nums[0]], nums[1:])