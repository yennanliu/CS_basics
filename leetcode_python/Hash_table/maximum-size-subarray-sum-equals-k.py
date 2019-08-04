# V0 

# V1 
# http://www.voidcn.com/article/p-pbvzylrp-qp.html
# https://www.cnblogs.com/lightwindy/p/9760070.html
class Solution(object):
    def maxSubArrayLen(self, nums, k):

        result, acc = 0, 0
        dic = {0: -1}

        for i in range(len(nums)):
            acc += nums[i]
            if acc not in dic:
                dic[acc] = i
            if acc - k in dic:
                result = max(result, i - dic[acc-k])
        return result

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def maxSubArrayLen(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        sums = {}
        cur_sum, max_len = 0, 0
        for i in range(len(nums)):
            cur_sum += nums[i]
            if cur_sum == k:
                max_len = i + 1
            elif cur_sum - k in sums:
                max_len = max(max_len, i - sums[cur_sum - k])
            if cur_sum not in sums:
                sums[cur_sum] = i  # Only keep the smallest index.
        return max_len