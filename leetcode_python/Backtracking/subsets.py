"""

78. Subsets
Medium

6854

121

Add to List

Share
Given an integer array nums of unique elements, return all possible subsets (the power set).

The solution set must not contain duplicate subsets. Return the solution in any order.

 

Example 1:

Input: nums = [1,2,3]
Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]

Example 2:

Input: nums = [0]
Output: [[],[0]]
 

Constraints:

1 <= nums.length <= 10
-10 <= nums[i] <= 10
All the numbers of nums are unique.

"""

# V0
# IDEA : BACKTRACK
class Solution:
    def subsets(self, nums):
        result = []
        path = []
        self.backtracking(nums, 0, path, result)
        return result

    def backtracking(self, nums, startIndex, path, result):
        result.append(path[:])  # NOTE here
        if startIndex >= len(nums):  # optional
            return
        for i in range(startIndex, len(nums)): # NOTE here : we start from startIndex every time
            path.append(nums[i])
            self.backtracking(nums, i + 1, path, result) # NOTE here
            path.pop(-1) # NOTE here

# V0
# IDEA : DFS
# plz also refer backtrack cheatsheet
# https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/backtrack.md
class Solution(object):
    def subsets(self, nums):
        def dfs(layer, start, tmp):
            if tmp not in res:
                res.append(tmp)
            if layer == len(nums):
                return
            ### NOTE : we have if condition first, then for loop
            for i in range(start, len(nums)):
                """
                ### NOTE below can make loop start `start idx` updated each time
                NOTE : since we NOT contain duplicate subsets
                ->  1. every loop start from i  (instead of 0 idx)
                ->  (i start from `start`, and start = i+1 for each loop)


                -> NOTE !!! below line actually has backtrack logic
                -> tmp + [nums[i]] is NOT adding new element to tmp, but only add new element to current recursive call, for other call tmp is unchaged
                """
                dfs(layer+1, i+1, tmp + [nums[i]])
        nums.sort()
        res = []
        dfs(0, 0, [])
        return res

# V0'
# brack tracking
class Solution(object):
    def subsets(self, nums):
        def help(start, tmp, res):
            tmp.sort()
            if tmp not in res:
                res.append(tmp)
            for i in range(start, len(nums)):
                if nums[i] not in tmp:
                    help(start+1, tmp + [nums[i]], res)
        res = []
        start = 0
        tmp = []
        if len(nums) == 1:
            res = [[]]
            res.append(nums)
            return res
        help(start, tmp, res)
        return res

# V0''
# IDEA : BRUTE FROCE
class Solution(object):
    def subsets(self, nums):
        nums.sort()
        result = [[]]
        for i in range(len(nums)):
            size = len(result)
            for j in range(size):
                result.append(list(result[j]))
                result[-1].append(nums[i])
        return result

# V0'''
# IDEA : Backtracking
class Solution:
    def subsets(self, nums):
        def backtrack(first = 0, curr = []):
            # if the combination is done
            if len(curr) == k:  
                output.append(curr[:])
                return
            for i in range(first, n):
                # add nums[i] into the current combination
                curr.append(nums[i])
                # use next integers to complete the combination
                backtrack(i + 1, curr)
                # backtrack
                curr.pop()
        
        output = []
        n = len(nums)
        for k in range(n + 1):
            backtrack()
        return output

# V0''''''
# IDEA : DFS 
# SAME AS # 90 subset-ii 
class Solution(object):
    def subsets(self, nums):
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
        
        # this sort is optional
        nums.sort()
        res = []
        dfs(0, 0, [])
        return res

# V0''''''''
# IDEA : DFS
class Solution:
    """
    @param nums: A set of numbers
    @return: A list of lists
    """
    def subsets(self, nums):
        nums = sorted(nums)
        combinations = []
        self.dfs(nums, 0, [], combinations)
        return combinations
        
    def dfs(self, nums, index, combination, combinations):
        combinations.append(list(combination))
        
        for i in range(index, len(nums)):
            combination.append(nums[i])
            self.dfs(nums, i + 1, combination, combinations)
            combination.pop()


# V1
# IDEA : BACKTRACK
# https://github.com/youngyangyang04/leetcode-master/blob/master/problems/0078.%E5%AD%90%E9%9B%86.md
class Solution:
    def subsets(self, nums):
        result = []
        path = []
        self.backtracking(nums, 0, path, result)
        return result

    def backtracking(self, nums, startIndex, path, result):
        result.append(path[:])  # 收集子集，要放在终止添加的上面，否则会漏掉自己
        # if startIndex >= len(nums):  # 终止条件可以不加
        #     return
        for i in range(startIndex, len(nums)):
            path.append(nums[i])
            self.backtracking(nums, i + 1, path, result)
            path.pop()

# V1
# https://www.jiuzhang.com/solution/subsets/#tag-highlight-lang-python
class Solution:
    """
    @param nums: A set of numbers
    @return: A list of lists
    """
    def subsets(self, nums):
        nums = sorted(nums)
        combinations = []
        self.dfs(nums, 0, [], combinations)
        return combinations
        
    def dfs(self, nums, index, combination, combinations):
        combinations.append(list(combination))
        
        for i in range(index, len(nums)):
            combination.append(nums[i])
            self.dfs(nums, i + 1, combination, combinations)
            combination.pop()

# V1
# IDEA : BACKTRACK
# https://www.youtube.com/watch?v=REOH22Xwdkk&t=4s
# https://github.com/neetcode-gh/leetcode/blob/main/python/0078-subsets.py
class Solution:
    def subsets(self, nums: List[int]) -> List[List[int]]:
        res = []

        subset = []

        def dfs(i):
            if i >= len(nums):
                res.append(subset.copy())
                return
            # decision to include nums[i]
            subset.append(nums[i])
            dfs(i + 1)
            # decision NOT to include nums[i]
            subset.pop()
            dfs(i + 1)

        dfs(0)
        return res

# V1'
# https://leetcode.com/problems/subsets/solution/
# IDEA : Cascading
class Solution:
    def subsets(self, nums):
        n = len(nums)
        output = [[]]
        
        for num in nums:
            output += [curr + [num] for curr in output]
        
        return output

# V1''
# https://leetcode.com/problems/subsets/solution/
# IDEA : Backtracking
class Solution:
    def subsets(self, nums):
        def backtrack(first = 0, curr = []):
            # if the combination is done
            if len(curr) == k:  
                output.append(curr[:])
                return
            for i in range(first, n):
                # add nums[i] into the current combination
                curr.append(nums[i])
                # use next integers to complete the combination
                backtrack(i + 1, curr)
                # backtrack
                curr.pop()
        
        output = []
        n = len(nums)
        for k in range(n + 1):
            backtrack()
        return output

# V1'''
# https://leetcode.com/problems/subsets/solution/
# IDRA :  Lexicographic (Binary Sorted) Subsets
class Solution:
    def subsets(self, nums):
        n = len(nums)
        output = []
        
        for i in range(2**n, 2**(n + 1)):
            # generate bitmask, from 0..00 to 1..11
            bitmask = bin(i)[3:]
            
            # append subset corresponding to that bitmask
            output.append([nums[j] for j in range(n) if bitmask[j] == '1'])
        
        return output

# V1''
# https://www.jiuzhang.com/solution/subsets/#tag-highlight-lang-python
class Solution:
    
    def search(self, nums, S, index):
        if index == len(nums):
            self.results.append(list(S))
            return
        
        S.append(nums[index])
        self.search(nums, S, index + 1)
        S.pop()
        self.search(nums, S, index + 1)
        
    def subsets(self, nums):
        self.results = []
        self.search(sorted(nums), [], 0)
        return self.results

# V1'''
# https://www.jiuzhang.com/solution/subsets/#tag-highlight-lang-python
class Solution:
    
    def search(self, nums, S, index):
        if index == len(nums):
            self.results.append(S)
            return
        
        self.search(nums, S + [nums[index]], index + 1)
        self.search(nums, S, index + 1)
        
    def subsets(self, nums):
        self.results = []
        self.search(sorted(nums), [], 0)
        return self.results

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
class Solution(object):
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
class Solution(object):
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