# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79789165
class Solution:
    def canPartitionKSubsets(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        if not nums or len(nums) < k: return False
        _sum = sum(nums)
        div, mod = divmod(_sum, k)
        if _sum % k or max(nums) > _sum / k: return False
        nums.sort(reverse = True)
        target = [div] * k
        return self.dfs(nums, k, 0, target)
        
    def dfs(self, nums, k, index, target):
        if index == len(nums): return True
        num = nums[index]
        for i in range(k):
            if target[i] >= num:
                target[i] -= num
                if self.dfs(nums, k, index + 1, target): return True
                target[i] += num
        return False

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79789165
class Solution(object):
    def canPartitionKSubsets(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        if k == 1: return True
        self.n = len(nums)
        if self.n < k: return False
        total = sum(nums)
        if total % k: return False
        self.target = total / k
        visited = [0] * self.n
        nums.sort(reverse = True)
        def dfs(k, ind, sum, cnt):
            if k == 1: return True
            if sum == self.target and cnt > 0:
                return dfs(k - 1, 0, 0, 0)
            for i in range(ind, self.n):
                if not visited[i] and sum + nums[i] <= self.target:
                    visited[i] = 1
                    if dfs(k, i + 1, sum + nums[i], cnt + 1):
                        return True
                    visited[i] = 0
            return False
        return dfs(k, 0, 0, 0)
        
# V2 
# Time:  O(n*2^n)
# Space: O(2^n)
class Solution(object):
    def canPartitionKSubsets(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        def dfs(nums, target, used, todo, lookup):
            if lookup[used] is None:
                targ = (todo-1)%target + 1
                lookup[used] = any(dfs(nums, target, used | (1<<i), todo-num, lookup) \
                                   for i, num in enumerate(nums) \
                                   if ((used>>i) & 1) == 0 and num <= targ)
            return lookup[used]

        total = sum(nums)
        if total%k or max(nums) > total//k:
            return False
        lookup = [None] * (1 << len(nums))
        lookup[-1] = True
        return dfs(nums, total//k, 0, total, lookup)


# Time:  O(k^(n-k) * k!)
# Space: O(n)
# DFS solution with pruning.
class Solution2(object):
    def canPartitionKSubsets(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        def dfs(nums, target, i, subset_sums):
            if i == len(nums):
                return True
            for k in range(len(subset_sums)):
                if subset_sums[k]+nums[i] > target:
                    continue
                subset_sums[k] += nums[i]
                if dfs(nums, target, i+1, subset_sums):
                    return True
                subset_sums[k] -= nums[i]
                if not subset_sums[k]: break
            return False

        total = sum(nums)
        if total%k != 0 or max(nums) > total//k:
            return False
        nums.sort(reverse=True)
        subset_sums = [0] * k
        return dfs(nums, total//k, 0, subset_sums)