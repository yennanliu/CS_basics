# V0 
# IDEA : DFS
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