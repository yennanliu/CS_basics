"""

47. Permutations II
Medium
8.1K
137
Companies
Given a collection of numbers, nums, that might contain duplicates, return all possible unique permutations in any order.

 

Example 1:

Input: nums = [1,1,2]
Output:
[[1,1,2],
 [1,2,1],
 [2,1,1]]
Example 2:

Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 

Constraints:

1 <= nums.length <= 8
-10 <= nums[i] <= 10

"""

# V0
# IDEA : BACKTRACK + LC 46 + COUNTER
# https://leetcode.com/problems/permutations-ii/editorial/
class Solution(object):
    def permuteUnique(self, nums):
        def help(res, cur, cnt):
            if len(cur) == len(nums):
                if cur not in res:
                    res.append(cur[:])
                    return
            if len(cur) > len(nums):
                return
            for x in _cnt:
                #print ("i = " + str(i) + " cur = " + str(cur))
                #if i not in cur:
                if _cnt[x] > 0:
                    cur.append(x)
                    _cnt[x] -= 1
                    help(res, cur, _cnt)
                    """
                    NOTE !!! : we UNDO the last op we just made (pop last element we put into array)
                    """
                    cur.pop(-1)
                    _cnt[x] += 1
        # edge case
        if not nums:
            return [[]]
        _cnt = Counter(nums)
        #print ("_cnt = " + str(_cnt))
        res = []
        cur = []
        help(res, cur, _cnt)
        return res

# V0'
# IDEA : BACKTRACK + LC 46 -> TLE
class Solution(object):
    def permuteUnique(self, nums):
        def help(res, cur, cnt):
            if len(cur) == len(nums):
                if cur not in res:
                    res.append(cur[:])
                    return
            if len(cur) > len(nums):
                return
            for i in range(len(nums)):
                #print ("i = " + str(i) + " cur = " + str(cur))
                #if i not in cur:
                if _cnt[nums[i]] > 0:
                    cur.append(nums[i])
                    _cnt[nums[i]] -= 1
                    help(res, cur, _cnt)
                    """
                    NOTE !!! : we UNDO the last op we just made (pop last element we put into array)
                    """
                    cur.pop(-1)
                    _cnt[nums[i]] += 1
        # edge case
        if not nums:
            return [[]]
        _cnt = Counter(nums)
        print ("_cnt = " + str(_cnt))
        res = []
        cur = []
        help(res, cur, _cnt)
        #print ("res = " + str(res))
        return res

# V1
# IDEA : BACKTRACK + COUNTER
# https://leetcode.com/problems/permutations-ii/editorial/
class Solution:
    def permuteUnique(self, nums: List[int]) -> List[List[int]]:
        results = []
        def backtrack(comb, counter):
            if len(comb) == len(nums):
                # make a deep copy of the resulting permutation,
                # since the permutation would be backtracked later.
                results.append(list(comb))
                return

            for num in counter:
                if counter[num] > 0:
                    # add this number into the current combination
                    comb.append(num)
                    counter[num] -= 1
                    # continue the exploration
                    backtrack(comb, counter)
                    # revert the choice for the next exploration
                    comb.pop()
                    counter[num] += 1

        backtrack([], Counter(nums))

        return results

# V2
# https://blog.csdn.net/weixin_38111819/article/details/79131409
# https://blog.csdn.net/XX_123_1_RJ/article/details/81021815
# IDEA : DFS 
class Solution(object):
    def permuteUnique(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        res = []
        nums.sort()
        self.dfs(nums, [], res)
        return res
    
    def dfs(self, nums, path, res):
        if not nums:
            res.append(path)
        for i in range(len(nums)):
            if i>0 and nums[i] == nums[i-1]:
                continue
            self.dfs(nums[:i]+nums[i+1:], path+[nums[i]], res)

# V2 
# Time:  O(n * n!)
# Space: O(n)
class Solution(object):
    def permuteUnique(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        nums.sort()
        result = []
        used = [False] * len(nums)
        self.permuteUniqueRecu(result, used, [], nums)
        return result

    def permuteUniqueRecu(self, result, used, cur, nums):
        if len(cur) == len(nums):
            result.append(cur + [])
            return
        for i in range(len(nums)):
            if used[i] or (i > 0 and nums[i-1] == nums[i] and not used[i-1]):
                continue
            used[i] = True
            cur.append(nums[i])
            self.permuteUniqueRecu(result, used, cur, nums)
            cur.pop()
            used[i] = False

class Solution2(object):
    # @param num, a list of integer
    # @return a list of lists of integers
    def permuteUnique(self, nums):
        solutions = [[]]

        for num in nums:
            next = []
            for solution in solutions:
                for i in range(len(solution) + 1):
                    candidate = solution[:i] + [num] + solution[i:]
                    if candidate not in next:
                        next.append(candidate)

            solutions = next

        return solutions