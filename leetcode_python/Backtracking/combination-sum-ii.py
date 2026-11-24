"""

40. Combination Sum II
Medium

Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in candidates where the candidate numbers sum to target.

Each number in candidates may only be used once in the combination.

Note: The solution set must not contain duplicate combinations.

 

Example 1:

Input: candidates = [10,1,2,7,6,1,5], target = 8
Output: 
[
[1,1,6],
[1,2,5],
[1,7],
[2,6]
]
Example 2:

Input: candidates = [2,5,2,1,2], target = 5
Output: 
[
[1,2,2],
[5]
]
 

Constraints:

1 <= candidates.length <= 100
1 <= candidates[i] <= 50
1 <= target <= 30

"""

# V0
# IDEA: BACKTRACK + SAME LAYER NEGLECT (gpt)
class Solution(object):
    def combinationSum2(self, candidates, target):
        """
        :type candidates: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        self.res = []

        # Important: sort to handle duplicates and enable pruning
        candidates.sort()

        self.helper(candidates, target, [], 0)

        return self.res

    def helper(self, candidates, target, cur, start_idx):

        # Found a valid combination
        if target == 0:
            self.res.append(cur[:])
            return

        n = len(candidates)

        for i in range(start_idx, n):
            val = candidates[i]

            # Skip duplicates at the same recursion level
            if i > start_idx and candidates[i] == candidates[i - 1]:
                continue

            # Since candidates are sorted, no need to explore larger values
            if val > target:
                break

            # Choose
            cur.append(val)

            # Explore: i + 1 means each element can only be used once
            self.helper(candidates, target - val, cur, i + 1)

            # Backtrack
            cur.pop()


# V0-1
# IDEA: BACKTRACK + SAME LAYER NEGLECT (GEMINI)
class Solution(object):
    def combinationSum2(self, candidates, target):
        """
        :type candidates: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        self.res = []
        if not candidates:
            return self.res

        # 1. 排序讓相同元素相鄰，方便同層去重與提前中斷
        candidates.sort()
        self.helper(candidates, target, 0, [])
        return self.res

    def helper(self, candidates, remain, start_idx, cur):
        # 恰好湊齊目標總和，記錄結果後即可返回
        if remain == 0:
            self.res.append(cur[:])
            return

        for i in range(start_idx, len(candidates)):
            # 2. 同層去重：同一層決策中，相同數字只挑第一個，避免產生重複組合
            if i > start_idx and candidates[i] == candidates[i - 1]:
                continue

            val = candidates[i]

            # 3. 排序後剪枝：若當前數字已大於剩餘目標，後續數字更大皆不可能湊出，直接 break
            if val > remain:
                break

            cur.append(val)
            # 每個元素只能使用一次，下一層從 i + 1 開始
            self.helper(candidates, remain - val, i + 1, cur)
            cur.pop()


# V0-2
# time = O(2^n * n), n = len(candidates)
# space = O(n)
class Solution(object):

    def combinationSum2(self, candidates, target):

        """
        NOTE : we have index here for tracking
               -> avoid reuse same element from candidates
        """
        def dfs(nums, target, index, res, path):
            if target < 0:
                return
            elif target == 0:
                res.append(path)
                return
            for i in range(index, len(nums)):
                ### NOTE : below condition
                if i > index and nums[i] == nums[i-1]:
                    continue
                dfs(nums, target - nums[i], i + 1, res, path + [nums[i]])

        ### NOTE : once we sort the input array, the occurrance of each unique number would be adjacent to each other.
        candidates.sort()
        print(candidates)
        res = []
        dfs(candidates, target, 0, res, [])
        return res

# V0-1
# IDEA : backtrack
# TODO : fix TLE
# from collections import Counter
# class Solution(object):
#     def combinationSum2(self, candidates, target):
#         def help(idx, cur, visited):
#             if sum(cur) == target:
#                 tmp = cur[:]
#                 tmp.sort()
#                 if tmp not in res:
#                     res.append(tmp)
#                     cur = []
#                     visited =[]
#                     return
#             if sum(cur) > target or len(cur) > _len:
#                 cur = []
#                 visited = []
#                 return
#             for i in range(idx, _len):
#                 if i not in visited:
#                     visited.append(i)
#                     cur.append(candidates[i])
#                     help(idx+1, cur, visited)
#                     cur.pop(-1)
#                     visited.pop(-1)
#         # edge case
#         if not candidates and target:
#             return []
#         if sum(candidates) < target:
#             return []
#         res = []
#         cur = []
#         visited = []
#         idx = 0
#         candidates.sort()
#         _len = len(candidates)
#         cnt = Counter(candidates)
#         help(idx, cur, visited)
#         print ("res = " + str(res))
#         return res

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79343638
# IDEA : DFS 
# time = O(2^n * n), n = len(candidates)
# space = O(n)
class Solution(object):
    def combinationSum2(self, candidates, target):
        candidates.sort()
        print(candidates)
        res = []
        self.dfs(candidates, target, 0, res, [])
        return res

    def dfs(self, nums, target, index, res, path):
        if target < 0:
            return
        elif target == 0:
            res.append(path)
            return
        for i in range(index, len(nums)):
            if i > index and nums[i] == nums[i-1]:
                continue
            self.dfs(nums, target - nums[i], i + 1, res, path + [nums[i]])

# V1'
# IDEA : BACKTRACKING 
# https://leetcode.com/problems/combination-sum-ii/solution/
# time = O(2^n * n), n = len(candidates)
# space = O(n)
class Solution:
    def combinationSum2(self, candidates, target):

        def backtrack(comb, remain, curr, counter, results):
            if remain == 0:
                # make a deep copy of the current combination
                #   rather than keeping the reference.
                results.append(list(comb))
                return
            elif remain < 0:
                return

            for next_curr in range(curr, len(counter)):
                candidate, freq = counter[next_curr]

                if freq <= 0:
                    continue

                # add a new element to the current combination
                comb.append(candidate)
                counter[next_curr] = (candidate, freq-1)

                # continue the exploration with the updated combination
                backtrack(comb, remain - candidate, next_curr, counter, results)

                # backtrack the changes, so that we can try another candidate
                counter[next_curr] = (candidate, freq)
                comb.pop()

        results = []  # container to hold the final combinations
        counter = Counter(candidates)
        # convert the counter table to a list of (num, count) tuples
        counter = [(c, counter[c]) for c in counter]

        backtrack(comb = [], remain = target, curr = 0,
                  counter = counter, results = results)

        return results

# V1''
# IDEA : BACKTRACKING with index
# https://leetcode.com/problems/combination-sum-ii/solution/
# time = O(2^n * n), n = len(candidates)
# space = O(n)
class Solution:
    def combinationSum2(self, candidates, target: int):

        def backtrack(comb, remain, curr, results):

            if remain == 0:
                # make a deep copy of the resulted combination
                results.append(list(comb))
                return

            for next_curr in range(curr, len(candidates)):

                if next_curr > curr \
                  and candidates[next_curr] == candidates[next_curr-1]:
                    continue

                pick = candidates[next_curr]
                # optimization: skip the rest of elements starting from 'curr' index
                if remain - pick < 0:
                    break

                comb.append(pick)
                backtrack(comb, remain - pick, next_curr + 1, results)
                comb.pop()

        candidates.sort()

        comb, results = [], []
        backtrack(comb, target, 0, results)

        return results

# V2
# time = O(k * C(n, k))
# space = O(k)
class Solution(object):
    # @param candidates, a list of integers
    # @param target, integer
    # @return a list of lists of integers
    def combinationSum2(self, candidates, target):
        result = []
        self.combinationSumRecu(sorted(candidates), result, 0, [], target)
        return result

    def combinationSumRecu(self, candidates, result, start, intermediate, target):
        if target == 0:
            result.append(list(intermediate))
        prev = 0
        while start < len(candidates) and candidates[start] <= target:
            if prev != candidates[start]:
                intermediate.append(candidates[start])
                self.combinationSumRecu(candidates, result, start + 1, intermediate, target - candidates[start])
                intermediate.pop()
                prev = candidates[start]
            start += 1
