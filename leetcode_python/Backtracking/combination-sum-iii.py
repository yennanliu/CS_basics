# V0 

# V1 
# http://bookshadow.com/weblog/2015/05/24/leetcode-combination-sum-iii/
# IDEA : BACKTRACKING 
# DEMO :
# if k = 3, n = 3 -> output = [[1,1,1]] # only solution 
# similarly, if  k = 4, n = 4 -> output = [[1,1,1,1]] # only solution 


class Solution:
    # @param {integer} k
    # @param {integer} n
    # @return {integer[][]}
    def combinationSum3(self, k, n):
        ans = []
        def search(start, cnt, sums, nums):
            if cnt > k or sums > n:  # to confine the cnt and sums lie in the interval (i.e. < k and < n)
                return
            if cnt == k and sums == n:
                ans.append(nums)
                return
            for x in range(start + 1, 10):
                search(x, cnt + 1, sums + x, nums + [x])
        search(0, 0, 0, [])
        return ans

# V2 
# Time:  O(k * C(n, k))
# Space: O(k)
class Solution(object):
    # @param {integer} k
    # @param {integer} n
    # @return {integer[][]}
    def combinationSum3(self, k, n):
        result = []
        self.combinationSumRecu(result, [], 1, k, n)
        return result

    def combinationSumRecu(self, result, intermediate, start, k, target):
        if k == 0 and target == 0:
            result.append(list(intermediate))
        elif k < 0:
            return
        while start < 10 and start * k + k * (k - 1) / 2 <= target:
            intermediate.append(start)
            self.combinationSumRecu(result, intermediate, start + 1, k - 1, target - start)
            intermediate.pop()
            start += 1