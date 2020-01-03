# V0 
# IDEA : GREEDY 
class Solution(object):
    def findLengthOfLCIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        ans = cnt = 0
        last = None
        for n in nums:
            if n > last:
                cnt += 1
            else:
                ans = max(ans, cnt)
                cnt = 1
            last = n
        return max(ans, cnt)

# V0'
# IDEA : GREEDY 
class Solution(object):
    def findLengthOfLCIS(self, nums):
        if not nums: return 0
        count = 1
        for i in range(len(nums)-1):
            tmp_count = 1 
            j = i
            while j < len(nums) - 1:
                #print ("i :", i, "j :", j, "nums[j] :", nums[j], "nums[j+1] :", nums[j+1], "count :", count, "tmp_count :", tmp_count)
                if nums[j+1] > nums[j]:
                    j += 1 
                    tmp_count += 1 
                else:
                    break
            count = max(count, tmp_count)
        return count 

# V1
# http://bookshadow.com/weblog/2017/09/10/leetcode-longest-continuous-increasing-subsequence/
# https://www.polarxiong.com/archives/LeetCode-674-longest-continuous-increasing-subsequence.html
# IDEA : GREEDY 
class Solution(object):
    def findLengthOfLCIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        ans = cnt = 0
        last = None
        for n in nums:
            if n > last:
                cnt += 1
            else:
                ans = max(ans, cnt)
                cnt = 1
            last = n
        return max(ans, cnt)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79220527
# IDEA : DP 
class Solution(object):
    def findLengthOfLCIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums: return 0
        N = len(nums)
        dp = [1] * N
        for i in range(1, N):
            if nums[i] > nums[i - 1]:
                dp[i] = dp[i - 1] + 1
        return max(dp)

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79220527
# IDEA : DP WITH SPACE OPTIMIZATION 
class Solution(object):
    def findLengthOfLCIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        longest = 0
        cur = 0
        for i in range(len(nums)):
            if i == 0 or nums[i] > nums[i - 1]:
                cur += 1
                longest = max(longest, cur)
            else:
                cur = 1
        return longest

# V1'''  
# https://blog.csdn.net/fuxuemingzhu/article/details/79220527
class Solution(object):
    def findLengthOfLCIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums: return 0
        N = len(nums)
        dp = 1
        res = 1
        for i in range(1, N):
            if nums[i] > nums[i - 1]:
                dp += 1
                res = max(res, dp)
            else:
                dp = 1
        return res

# V2
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findLengthOfLCIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result, count = 0, 0
        for i in range(len(nums)):
            if i == 0 or nums[i-1] < nums[i]:
                count += 1
                result = max(result, count)
            else:
                count = 1
        return result