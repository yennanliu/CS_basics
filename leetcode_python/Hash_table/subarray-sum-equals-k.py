# V0 
class Solution(object):
    def subarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        n = len(nums)
        d = collections.defaultdict(int)
        d[0] = 1
        sum = 0
        res = 0
        for i in range(n):
            sum += nums[i]
            if sum - k in d:
                res += d[sum - k]
            d[sum] += 1
        return res

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82767119
# IDEA : Cum-SUM + collections.defaultdict
# DEMO :
# nums =  [1,1,1]
# k =  2 
# s = Solution()
# r = s.subarraySum(nums, k)
# print (r)
# sum : 0
# res : 0
# d : defaultdict(<class 'int'>, {0: 1})
# sum : 1
# res : 0
# d : defaultdict(<class 'int'>, {0: 1, 1: 1})
# sum : 2
# res : 1
# d : defaultdict(<class 'int'>, {0: 1, 1: 1, 2: 1})
# 2
import collections
class Solution(object):
    def subarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        n = len(nums)
        d = collections.defaultdict(int)
        d[0] = 1
        sum = 0
        res = 0
        for i in range(n):
            sum += nums[i]
            if sum - k in d:
                res += d[sum - k]
            d[sum] += 1
        return res

# V1' 
# http://bookshadow.com/weblog/2017/04/30/leetcode-subarray-sum-equals-k/
import collections
class Solution(object):
    def subarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        ans = sums = 0
        cnt = collections.Counter()
        for num in nums:
            cnt[sums] += 1
            sums += num
            ans += cnt[sums - k]
        return ans

# V1''
# https://www.jiuzhang.com/solution/subarray-sum-equals-k/#tag-highlight-lang-python
class Solution:
    """
    @param nums: a list of integer
    @param k: an integer
    @return: return an integer, denote the number of continuous subarrays whose sum equals to k
    """
    def subarraySumEqualsK(self, nums, k):
        # write your code here
        for i in range(1, len(nums)):
            nums[i] += nums[i - 1]
        d, ans = {0 : 1}, 0
        for i in range(len(nums)):
            if(d.get(nums[i] - k) != None):
                ans += d[nums[i] - k]
            if(d.get(nums[i]) == None):
                d[nums[i]] = 1
            else:
                d[nums[i]] += 1
        return ans

# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def subarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        result = 0
        accumulated_sum = 0
        lookup = collections.defaultdict(int)
        lookup[0] += 1
        for num in nums:
            accumulated_sum += num
            result += lookup[accumulated_sum - k]
            lookup[accumulated_sum] += 1
        return result