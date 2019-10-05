# V0 
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

# V1 
# http://www.voidcn.com/article/p-pbvzylrp-qp.html
# https://www.cnblogs.com/lightwindy/p/9760070.html
# IDEA : DICT 
# STEPS :
# -> HAVE A dic RECORD THE SUMS AND THEIR INDEX 
# -> GO THROUGH ALL ELEMENTS IN nums AND KEEP DOING ACCUMULATED SUM : acc
# -> IF acc not in dict : dic[acc] = i (add sum and it index to dict)
# -> IF acc - k in dic : result = max(result, i - dic[acc-k])
# acc + sth = k ---> acc - k = sth, so if acc - k already in dict ---> acc + sth = k MUST EXIST
# SO IF ABOVE CASE EXIST, THEN WE DO max(result, i - dic[acc-k]) : TO RETURN THE MAX LENGTH OF SUB ARRAY
# i - dic[acc-k] : GET THE LENGTH BETWEEN i and sth (sth = dic[acc-k] )
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

# V1'
# https://www.jiuzhang.com/solution/maximum-size-subarray-sum-equals-k/#tag-highlight-lang-python
class Solution:
    """
    @param nums: an array
    @param k: a target value
    @return: the maximum length of a subarray that sums to k
    """
    def maxSubArrayLen(self, nums, k):
        # Write your code here
        m = {}
        ans = 0
        m[k] = 0
        n = len(nums)
        sum = [0 for i in range(n + 1)]
        for i in range(1, n + 1):
            sum[i] = sum[i - 1] + nums[i - 1]  # use this trick to "record" accumulated sum, avoid using double loop 
            if sum[i] in m:
                ans = max(ans, i - m[sum[i]])
            if sum[i] + k not in m:
                m[sum[i] + k] = i
        return ans

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