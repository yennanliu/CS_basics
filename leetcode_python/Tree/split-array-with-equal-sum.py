# V0

# V1
# https://www.jiuzhang.com/solution/split-array-with-equal-sum/#tag-highlight-lang-python
# IDEA : GREEDY 
# PROCESS 
# 1) INIT A NULL ARRAY 
# 2) SET INDEX j to split array into left and right sub array 
# 3) CHECK IF THERE EXIST INDEX i IN LEFT ARRAY THAT CAN SPLIT 2 SUB ARRAY WITH SAME SUM
# 4) CHECK IF THERE EXIST INDEX k IN RIGHT ARRAY THAT CAN SPLIT 2 SUB ARRAY WITH SAME SUM
# 5) RETURN TRUE IF ANY CASE HAPPEN ABOVE
# 6) RETURN FALSE IF NO ABOVE CASE 
class Solution:
    """
    @param nums: a list of integer
    @return: return a boolean
    """
    def splitArray(self, nums):
        n = len(nums)
        sum = [0] * n
        sum[0] = nums[0]
        for i in range(1, n):
            sum[i] = sum[i - 1] + nums[i]                # cache the current sum, use sum(n) to caculate sum(n+1)
        for j in range(3, n - 3):                        # j start from 3 
            S = set()
            for i in range(1, j - 1):
                if(sum[i - 1] == sum[j - 1] - sum[i]):   # use sum cache array to check if sum[i - 1] == sum[j - 1] - sum[i]
                    S.add(sum[i - 1])
            for k in range(j + 2, n - 1):                # k start from j+2
                x, y = sum[k - 1] - sum[j], sum[n - 1] - sum[k]
                if(x == y and x in S):
                    return True
        return False

# V1' 
# http://bookshadow.com/weblog/2017/04/03/leetcode-split-array-with-equal-sum/
class Solution(object):
    def splitArray(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        size = len(nums)
        sums = [0] * (size + 1)
        for x in range(size):
            sums[x + 1] += sums[x] + nums[x]

        idxs = collections.defaultdict(list)
        for x in range(size):
            idxs[sums[x + 1]].append(x)
        
        jlist = collections.defaultdict(list)
        for i in range(1, size):
            for j in idxs[2 * sums[i] + nums[i]]:
                if i < j < size:
                    jlist[sums[i]].append(j + 1)
        
        for k in range(size - 2, 0, -1):
            for j in jlist[sums[size] - sums[k + 1]]:
                if j + 1 > k: continue
                if sums[k] - sums[j + 1] == sums[size] - sums[k + 1]:
                    return True
        return False
        
# V2 
# Time:  O(n^2)
# Space: O(n)
class Solution(object):
    def splitArray(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        if len(nums) < 7:
            return False

        accumulated_sum = [0] * len(nums)
        accumulated_sum[0] = nums[0]
        for i in range(1, len(nums)):
            accumulated_sum[i] = accumulated_sum[i-1] + nums[i]
        for j in range(3, len(nums)-3):
            lookup = set()
            for i in range(1, j-1):
                if accumulated_sum[i-1] == accumulated_sum[j-1] - accumulated_sum[i]:
                    lookup.add(accumulated_sum[i-1])
            for k in range(j+2, len(nums)-1):
                if accumulated_sum[-1] - accumulated_sum[k] == accumulated_sum[k-1] - accumulated_sum[j] and \
                   accumulated_sum[k - 1] - accumulated_sum[j] in lookup:
                    return True
        return False