"""

213. House Robber II
Medium

You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed. All houses at this place are arranged in a circle. That means the first house is the neighbor of the last one. Meanwhile, adjacent houses have a security system connected, and it will automatically contact the police if two adjacent houses were broken into on the same night.

Given an integer array nums representing the amount of money of each house, return the maximum amount of money you can rob tonight without alerting the police.

 

Example 1:

Input: nums = [2,3,2]
Output: 3
Explanation: You cannot rob house 1 (money = 2) and then rob house 3 (money = 2), because they are adjacent houses.
Example 2:

Input: nums = [1,2,3,1]
Output: 4
Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
Total amount you can rob = 1 + 3 = 4.
Example 3:

Input: nums = [1,2,3]
Output: 3
 

Constraints:

1 <= nums.length <= 100
0 <= nums[i] <= 1000

"""

# V0

# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/house-robber-ii/discuss/230657/Python-solution
# Since nums[0] and nums[-1] cannot be robbed simultaneously, The robber has to rob houses in nums[:-1] or in nums[1:], whichever is larger. Therefore, the problem reduces to two LC 198. House Robber I problems, which have already been solved.
class Solution:
    def rob(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        prev_max = 0
        curr_max = 0
        for i in range(len(nums)-1):
            tmp = curr_max
            curr_max = max(curr_max, prev_max+nums[i])
            prev_max = tmp
        rec = curr_max
        
        prev_max = 0
        curr_max = 0
        for i in range(len(nums)-1, 0, -1):
            tmp = curr_max
            curr_max = max(curr_max, prev_max+nums[i])
            prev_max = tmp
        return max(rec, curr_max)

# V1'
# https://leetcode.com/problems/house-robber-ii/discuss/60047/Python-easy-to-understand-solution
class Solution(object):
    def rob(self, nums):
        if len(nums) == 1:
            return nums[0]
        return max(self.help(nums[:-1]), self.help(nums[1:]))
        
    def help(self, nums):
        if len(nums) == 1:
            return nums[0]
        a, b = nums[0], max(nums[:2])
        for i in range(2, len(nums)):
            a, b = b, max(b, a+nums[i])
        return b

# V1''
# https://leetcode.com/problems/house-robber-ii/discuss/132149/Easy-understanding-Python-Solution
class Solution(object):
    def rob(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums) <= 1:
            return sum(nums)
        rob, rob2 = 0, 0
        cool, cool2 = 0, 0
        # rob -> do -> cool
        # rob -> skip -> rob
        # cool -> skip -> rob
        # skip the first house
        for num in nums[1:]:
            rob, cool = max(rob, cool + num), rob
        # skip the last house
        for num in nums[:-1]:
            rob2, cool2 = max(rob2, cool2 + num), rob2
        return max(rob, rob2)

# V1'''
# IDEA : DP
# https://leetcode.com/problems/house-robber-ii/solution/
class Solution:
    def rob(self, nums: List[int]) -> int:
        if len(nums) == 0 or nums is None:
            return 0

        if len(nums) == 1:
            return nums[0]

        return max(self.rob_simple(nums[:-1]), self.rob_simple(nums[1:]))

    def rob_simple(self, nums: List[int]) -> int:
        t1 = 0
        t2 = 0
        for current in nums:
            temp = t1
            t1 = max(current + t2, t1)
            t2 = temp

        return t1

# V1'''''
# IDEA : DP
class Solution:
    def rob(self, nums):
        def help(nums):
            t1 = 0
            t2 = 0
            for current in nums:
                temp = t1
                t1 = max(current + t2, t1)
                t2 = temp
            return t1
        
        # edge case
        if len(nums) == 0 or nums is None:
            return 0

        if len(nums) == 1:
            return nums[0]

        return max(help(nums[:-1]), help(nums[1:]))

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def rob(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def robHelper(root):
            if not root:
                return (0, 0)
            left, right = robHelper(root.left), robHelper(root.right)
            return (root.val + left[1] + right[1], max(left) + max(right))
        return max(robHelper(root))