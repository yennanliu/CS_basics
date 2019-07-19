# V0 

# V1
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

# V2 
# Time:  O(n * 2^n)
# Space: O(1)
class Solution(object):
    def subsets(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        nums.sort()
        result = [[]]
        for i in range(len(nums)):
            size = len(result)
            for j in range(size):
                result.append(list(result[j]))
                result[-1].append(nums[i])
        return result


# Time:  O(n * 2^n)
# Space: O(1)
class Solution2(object):
    def subsets(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        result = []
        i, count = 0, 1 << len(nums)
        nums.sort()

        while i < count:
            cur = []
            for j in range(len(nums)):
                if i & 1 << j:
                    cur.append(nums[j])
            result.append(cur)
            i += 1

        return result


# Time:  O(n * 2^n)
# Space: O(1)
class Solution3(object):
    def subsets(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        return self.subsetsRecu([], sorted(nums))

    def subsetsRecu(self, cur, nums):
        if not nums:
            return [cur]

        return self.subsetsRecu(cur, nums[1:]) + self.subsetsRecu(cur + [nums[0]], nums[1:])
