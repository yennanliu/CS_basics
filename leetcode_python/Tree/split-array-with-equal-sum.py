# V0

# V1 
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