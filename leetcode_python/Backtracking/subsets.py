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
                """
                dfs(layer+1, i+1, tmp + [nums[i]])
        nums.sort()
        res = []
        dfs(0, 0, [])
        return res

# V0'
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

# V0''
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

# V0''''
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

# V0''''' 
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