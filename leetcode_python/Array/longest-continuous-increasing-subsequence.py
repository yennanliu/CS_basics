# V0 
# IDEA : GREEDY
# DEMO :  CHECK BELOW CASES
# nums = [1,2,3,4,2,5,6,7]
# elment = 1 
#    -> if we start from 1 and go from 1, 2... 4, then meet 2 and stop, since "1, 2, 3, 4, 2" not a sub-increase string
#    -> but WE DON'T HAVE TO START FROM 2 (the 2nd element in the nums), SINCE THE sub-string from 2 is not going to "LONGER" THAN ABOVE ANYWAY
# so we just have to start from 2 instead  
# elment =  2  (the second "2" in the nums)
# ..
# .. 
class Solution:
    def findLengthOfLCIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums) == 0:
            return 0
        count = 1
        result = 1
        for i in range(1, len(nums)):
            if nums[i] > nums[i - 1]:
                count += 1
            else:
                result = max(result, count)
                count = 1
        result = max(result, count)
        return result

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

# V0''
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