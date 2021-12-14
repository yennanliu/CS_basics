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

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79343638
# IDEA : DFS 
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

# V1'
# IDEA : BACKTRACKING with index
# https://leetcode.com/problems/combination-sum-ii/solution/
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
# Time:  O(k * C(n, k))
# Space: O(k)
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