# Time:  O(n)
# Space: O(1)
#
# You are a professional robber planning to rob houses along a street.
# Each house has a certain amount of money stashed, the only constraint stopping you
# from robbing each of them is that adjacent houses have security system connected
# and it will automatically contact the police if two adjacent houses were broken into on the same night.
#
# Given a list of non-negative integers representing the amount of money of each house,
# determine the maximum amount of money you can rob tonight without alerting the police.
#

# V0
class Solution:
    # @param num, a list of integer
    # @return an integer
    def rob(self, num):
        size = len(num)
        dp = [0] * (size + 1)
        if size:
            dp[1] = num[0]
        for i in range(2, size + 1):
            dp[i] = max(dp[i - 1], dp[i - 2] + num[i - 1])
        return dp[size]

# V1
# http://bookshadow.com/weblog/2015/04/01/leetcode-house-robber/
# IDEA : DP
class Solution:
    # @param num, a list of integer
    # @return an integer
    def rob(self, num):
        size = len(num)
        dp = [0] * (size + 1)
        if size:
            dp[1] = num[0]
        for i in range(2, size + 1):
            dp[i] = max(dp[i - 1], dp[i - 2] + num[i - 1])
        return dp[size]

### Test case
s=Solution()
assert s.rob([1,2,3]) == 4
assert s.rob([1,2,3,4]) == 6
assert s.rob([]) == 0
assert s.rob([0]) == 0
assert s.rob([0,0,0,0]) == 0
assert s.rob([0,1]) == 1
assert s.rob([0,1,2]) == 2
assert s.rob([0,1,1,1]) == 2
assert s.rob([2,7,9,3,1]) == 12
assert s.rob([1,2,3,1]) == 4
assert s.rob([ _ for _ in range(99)]) == 2450
assert s.rob([99,100,3,7,4]) == 107

# V1'
# http://bookshadow.com/weblog/2015/04/01/leetcode-house-robber/
# IDEA : DP
class Solution:
    # @param num, a list of integer
    # @return an integer
    def rob(self, num):
        size = len(num)
        odd, even = 0, 0
        for i in range(size):
            if i % 2:
                odd = max(odd + num[i], even)
            else:
                even = max(even + num[i], odd)
        return max(odd, even)

# V1''
# https://leetcode.com/problems/house-robber/discuss/299056/Python-O(n)-time-O(1)-space-4-lines
# IDEA : DP
# time complexity = O(n)
# space complexity = O(1)
#
# Option 1: If rob, then rob = not_rob + nums[i]
# (max money if rob the current house = max money if not rob the last house + amount of the current house)
# Option 2: If not rob, then not_rob = max(rob, not_rob)
# (max money if not rob the current house = max money at the last house, either rob or not rob)
#
# Varibles: rob = max money if rob the current house
# not_rob = max money if not rob the current house.
# Both variables are initially set to 0
class Solution(object):
    def rob(self, nums):
        rob, not_rob = 0, 0
        for num in nums:
            rob, not_rob = not_rob + num, max(rob, not_rob)
        return max(rob, not_rob)

# V1'''
# https://leetcode.com/problems/house-robber/discuss/341554/Python-Dynamic-Progranming
# IDEA : DP
class Solution(object):
    def rob(self, nums: List[int]):
            if not nums:
                return 0
            if len(nums) < 2:
                return nums[0]
            nums[1] = max(nums[0], nums[1])
            for i in range(2, len(nums)):
                # nums[i-2]+nums[i] : rob nums[i]
                #  nums[i-1] : not rob nums[i]
                nums[i] = max((nums[i-2]+nums[i]), nums[i-1])
            return nums[-1]

# V1''''
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param num, a list of integer
    # @return an integer
    def rob(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        last, now = 0, 0
        for i in nums:
            last, now = now, max(last + i, now)
        return now

# V2 
class Solution:
    # @param num, a list of integer
    # @return an integer
    def rob(self, num):
        if len(num) == 0:
            return 0

        if len(num) == 1:
            return num[0]

        num_i, num_i_1 = max(num[1], num[0]), num[0]
        for i in range(2, len(num)):
            num_i_1, num_i_2 = num_i, num_i_1
            num_i = max(num[i] + num_i_2, num_i_1);

        return num_i

    def rob2(self, nums):
        """
        rob[i] = nums[i] + pass[i-1]
        pass[i] = max(rob[i-1], pass[i-1])
        """

        """
        :type nums: List[int]
        :rtype: int
        """
        last, now = 0, 0
        for i in nums:
            last, now = now, max(last + i, now)
        return now
