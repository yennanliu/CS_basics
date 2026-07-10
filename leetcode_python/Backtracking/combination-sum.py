"""

39. Combination Sum
Medium

Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of candidates where the chosen numbers sum to target. You may return the combinations in any order.

The same number may be chosen from candidates an unlimited number of times. Two combinations are unique if the frequency of at least one of the chosen numbers is different.

It is guaranteed that the number of unique combinations that sum up to target is less than 150 combinations for the given input.

 

Example 1:

Input: candidates = [2,3,6,7], target = 7
Output: [[2,2,3],[7]]
Explanation:
2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
7 is a candidate, and 7 = 7.
These are the only two combinations.
Example 2:

Input: candidates = [2,3,5], target = 8
Output: [[2,2,2,2],[2,3,3],[3,5]]
Example 3:

Input: candidates = [2], target = 1
Output: []
Example 4:

Input: candidates = [1], target = 1
Output: [[1]]
Example 5:

Input: candidates = [1], target = 2
Output: [[1,1]]
 

Constraints:

1 <= candidates.length <= 30
1 <= candidates[i] <= 200
All elements of candidates are distinct.
1 <= target <= 500

"""

# V0
# IDEA : DFS + BACKTRACK
# time = O(n^(t/m)), n = len(candidates), t = target, m = min(candidates)
# space = O(t/m)
class Solution(object):
    def combinationSum(self, candidates, target):
        if not candidates:
            return []

        # NOTE !!! why `sort`
        # Sorting lets us optimize
        # and break early out of loops
        candidates.sort()
        self.res = []

        # Pass 0 as the starting index to begin our search
        self.helper(candidates, target, [], 0)
        return self.res

    # NOTE !!! why `start`
    # Added 'start' to track our 
    # position and avoid looking backward
    def helper(self, candidates, target, cur, start):
        # Base Case: Instead of calculating sum(cur) every time, 
        # we subtract from target. If target hits 0, we found a match!
        if target == 0:
            self.res.append(cur[:])
            return
        
        # Start our loop from the 'start' pointer, not from the beginning
        for i in range(start, len(candidates)):
            val = candidates[i]
            
            # OPTIMIZATION: Since candidates are sorted, if the current value 
            # is greater than our target, all values after it are also too big.
            if val > target:
                break
                
            cur.append(val) 
            
            # Recurse: Pass 'i' as the next start index so the same 
            # number can be reused, but numbers before 'i' are ignored.
            """
            NOTE !!!

             -> we pass `i` to helper func (but NOT `i+1`)
             -> reason:
                -> we want to reuse `same` char in some of
                    the next recursion calls
            """
            self.helper(candidates, target - val, cur, i)
            
            # Backtrack
            cur.pop()


# V0
# IDEA : DFS + BACKTRACK
# time = O(n^(t/m)), n = len(candidates), t = target, m = min(candidates)
# space = O(t/m)
class Solution(object):
    def combinationSum(self, candidates, target):

        def dfs(tmp):
            if sum(tmp) == target:
                tmp.sort()
                if tmp not in res:
                    res.append(tmp)
                return
            if sum(tmp) > target:
                return
            for c in candidates:
                ### NOTE : we can add tmp with [c] in func arg directly (as below)
                dfs(tmp + [c])

        res = []
        tmp = []
        dfs(tmp)
        return res


# V0-1
# time = O(n^(t/m)), n = len(candidates), t = target, m = min(candidates)
# space = O(t/m)
class Solution(object):
    def combinationSum(self, candidates, target):
        self.res = []
        candidates.sort()

        self.helper(candidates, target, 0, [])
        return self.res

    def helper(self, candidates, target, start, cur):
        if target == 0:
            self.res.append(cur[:])
            return

        if target < 0:
            return

        for i in range(start, len(candidates)):
            cur.append(candidates[i])

            # i (not i+1) because we can reuse the same number
            self.helper(
                candidates,
                target - candidates[i],
                i,
                cur
            )

            cur.pop()   # backtrack



# V0'
# IDEA : DFS + BACKTRACK
# time = O(n^(t/m)), n = len(candidates), t = target, m = min(candidates)
# space = O(t/m)
class Solution(object):
    def combinationSum(self, candidates, target):
        res = []
        candidates.sort() 
        self.dfs(candidates, target, 0, res, [])
        return res
    
    def dfs(self, nums, target, index, res, path):
        if target < 0:
            return
        elif target == 0:
            res.append(path)
            return
        for i in range(index, len(nums)):
            if nums[index] > target:
                return
            self.dfs(nums, target - nums[i], i, res, path + [nums[i]])

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79322462
# IDEA : DFS 
# time = O(n^(t/m)), n = len(candidates), t = target, m = min(candidates)
# space = O(t/m)
class Solution(object):
    def combinationSum(self, candidates, target):
        """
        :type candidates: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        res = [] 
        candidates.sort()   # sort the candidates first, for the following "if nums[index] > target: return" logic n "
        self.dfs(candidates, target, 0, res, [])
        return res
    
    def dfs(self, nums, target, index, res, path):
        if target < 0:
            return
        elif target == 0:
            res.append(path)
            return
        for i in range(index, len(nums)):
            if nums[index] > target:    # if the current value in candidates already > target, that means there is no such collection can make its sum as same as target
                return
            self.dfs(nums, target - nums[i], i, res, path + [nums[i]])

# V1
# https://github.com/neetcode-gh/leetcode/blob/main/python/0039-combination-sum.py
# https://www.youtube.com/watch?v=GBKI9VSKdGg
# time = O(n^(t/m)), n = len(candidates), t = target, m = min(candidates)
# space = O(t/m)
class Solution:
    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:
        res = []

        def dfs(i, cur, total):
            if total == target:
                res.append(cur.copy())
                return
            if i >= len(candidates) or total > target:
                return

            cur.append(candidates[i])
            dfs(i, cur, total + candidates[i])
            cur.pop()
            dfs(i + 1, cur, total)

        dfs(0, [], 0)
        return res

### Test case : dev

# V1'
# https://leetcode.com/problems/combination-sum/discuss/16554/Share-My-Python-Solution-beating-98.17
# IDEA : BACKTRACKING
# time = O(n^(t/m)), n = len(candidates), t = target, m = min(candidates)
# space = O(t/m)
class Solution(object):
    def combinationSum(self, candidates, target):
        result = []
        candidates = sorted(candidates)
        def dfs(remain, stack):
            if remain == 0:
                result.append(stack)
                return 

            for item in candidates:
                if item > remain: break
                if stack and item < stack[-1]: continue
                else:
                    dfs(remain - item, stack + [item])

        dfs(target, [])
        return result

# V1''
# https://www.jiuzhang.com/solution/combination-sum/#tag-highlight-lang-python
# time = O(n^(t/m)), n = len(candidates), t = target, m = min(candidates)
# space = O(t/m)
class Solution:
    # @param candidates, a list of integers
    # @param target, integer
    # @return a list of lists of integers
    def combinationSum(self, candidates, target):
        candidates = sorted(list(set(candidates)))
        results = []
        self.dfs(candidates, target, 0, [], results)
        return results

    def dfs(self, candidates, target, start, combination, results):

        if target < 0:
            return
        
        if target == 0:
            # deepcooy
            return results.append(list(combination))
            
        for i in range(start, len(candidates)):
            # [2] => [2,2]
            combination.append(candidates[i])
            self.dfs(candidates, target - candidates[i], i, combination, results)
            # [2,2] => [2]
            combination.pop()  # backtracking
            
# V2 
# time = O(k * n^k)
# space = O(k)
class Solution(object):
    # @param candidates, a list of integers
    # @param target, integer
    # @return a list of lists of integers
    def combinationSum(self, candidates, target):
        result = []
        self.combinationSumRecu(sorted(candidates), result, 0, [], target)
        return result

    def combinationSumRecu(self, candidates, result, start, intermediate, target):
        if target == 0:
            result.append(list(intermediate))
        while start < len(candidates) and candidates[start] <= target:
            intermediate.append(candidates[start])
            self.combinationSumRecu(candidates, result, start, intermediate, target - candidates[start])
            intermediate.pop()
            start += 1