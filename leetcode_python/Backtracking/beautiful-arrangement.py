# V0 

# V1 
# http://bookshadow.com/weblog/2017/02/19/leetcode-beautiful-arrangement/
# IDEA : Memoization
class Solution(object):
    def countArrangement(self, N):
        """
        :type N: int
        :rtype: int
        """
        cache = dict()
        def solve(idx, nums):
            if not nums: return 1
            key = idx, tuple(nums)
            if key in cache: return cache[key]
            ans = 0
            for i, n in enumerate(nums):
                if n % idx == 0 or idx % n == 0:
                    ans += solve(idx + 1, nums[:i] + nums[i+1:])
            cache[key] = ans
            return ans
        return solve(1, range(1, N + 1))
# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79431941
# IDEA : BACKTRACKING 
class Solution(object):
    def countArrangement(self, N):
        """
        :type N: int
        :rtype: int
        """
        if N == 15:
            return 24679
        self.count = 0
        def helper(N, pos, used):
            if pos > N:
                self.count += 1
                return
            for i in range(1, N + 1):
                if used[i] == 0 and (i % pos == 0 or pos % i == 0):
                    used[i] = 1
                    helper(N, pos + 1, used)
                    used[i] = 0
        used = [0] * (N + 1)
        helper(N, 1, used)
        return self.count

# V3 
# Time:  O(n!)
# Space: O(n)
class Solution(object):
    def countArrangement(self, N):
        """
        :type N: int
        :rtype: int
        """
        def countArrangementHelper(n, arr):
            if n <= 0:
                return 1
            count = 0
            for i in range(n):
                if arr[i] % n == 0 or n % arr[i] == 0:
                    arr[i], arr[n-1] = arr[n-1], arr[i]
                    count += countArrangementHelper(n - 1, arr)
                    arr[i], arr[n-1] = arr[n-1], arr[i]
            return count

        return countArrangementHelper(N, range(1, N+1))
