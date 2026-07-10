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
# IDEA 1) 1D DP (2 DP)
# time = O(n)  # n = len(nums)
# space = O(n)  # dp1, dp2 arrays
class Solution(object):
    def rob(self, nums):
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        if len(nums) == 2:
            return max(nums[0], nums[1])

        n = len(nums)

        # exclude last
        dp1 = [0] * n
        dp1[0] = nums[0]
        """
        NOTE !!!
            -> dp1[1] init as the max from  nums[0], nums[1]

            -> # CRITICAL FIX: House 1 is a valid option in this scenario!

            ---


            This version is not correct.

            The issue is:

            dp1[0] = nums[0]
            dp1[1] = 0

            This does not represent "exclude the last house". It represents a strange state where house 0 is taken and house 1 is forbidden.

            For example:

            nums = [2, 7, 9, 3, 1]

            Your dp1 becomes:

            dp1[0] = 2
            dp1[1] = 0
            dp1[2] = max(2+9, 0) = 11

            which is already wrong because the best answer for houses [0,1,2] is:

            max(2+9, 7) = 11

            but for other inputs you'll lose solutions that start from house 1.

        """
        dp1[1] = max(nums[0], nums[1])

        # NOTE !!!
        # dp1 loop from `n` to `n-1`
        for i in range(2, n - 1):
            dp1[i] = max(dp1[i - 2] + nums[i], dp1[i - 1])

        # exclude first
        dp2 = [0] * n
        dp2[0] = 0
        dp2[1] = nums[1]

        for i in range(2, n):
            dp2[i] = max(dp2[i - 2] + nums[i], dp2[i - 1])

        # dp1 max is at `n-2` idx
        return max(dp1[n - 2], dp2[n - 1])


# V0-1
# IDEA: 1D DP
# time = O(n)  # n = len(nums)
# space = O(n)  # dp1, dp2 arrays
class Solution(object):
    def rob(self, nums):
        # Edge cases
        if not nums or len(nums) == 0:
            return 0
        if len(nums) == 1:
            return nums[0]
        if len(nums) == 2:
            return max(nums[0], nums[1])      
            
        n = len(nums)
        
        # ---------------------------------------------------------------------
        # Scenario 1: Rob from house 0 up to house n-2 (Excludes the last house)
        # ---------------------------------------------------------------------
        dp1 = [0] * n 
        dp1[0] = nums[0] 
        # CRITICAL FIX: House 1 is a valid option in this scenario!
        dp1[1] = max(nums[0], nums[1]) 
        
        # Even though the loop runs up to n, we will manually ignore the last 
        # calculation by only reading up to index n-2 at the end.
        for i in range(2, n):
            dp1[i] = max(dp1[i-2] + nums[i], dp1[i-1])
            
        # ---------------------------------------------------------------------
        # Scenario 2: Rob from house 1 up to house n-1 (Excludes the first house)
        # ---------------------------------------------------------------------
        dp2 = [0] * n 
        dp2[0] = 0        # House 0 is skipped entirely
        dp2[1] = nums[1]  # Our baseline starting point is now house 1
        
        for i in range(2, n):
            dp2[i] = max(dp2[i-2] + nums[i], dp2[i-1])
            
        # CORRECT AND CLEAN POINTER EXTRACTION:
        # As you correctly noted, the max for dp1 is safely stored at dp1[n-2]!
        return max(dp2[n-1], dp1[n-2])



# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/house-robber-ii/discuss/230657/Python-solution
# IDEA : 
# Since nums[0] and nums[-1] cannot be robbed simultaneously, 
# The robber has to rob houses in nums[:-1] or in nums[1:], 
# whichever is larger. Therefore, the problem reduces to two LC 198. 
# House Robber I problems, which have already been solved.
# -> so same as LC 198 (House robber)
# -> just need to implement method on nums[:-1] or nums[1:], and return max
# time = O(n)  # n = len(nums)
# space = O(1)
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
# time = O(n)  # n = len(nums)
# space = O(n)  # nums[:-1] / nums[1:] slice copies
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
# time = O(n)  # n = len(nums)
# space = O(n)  # nums[1:] / nums[:-1] slice copies
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
# time = O(n)  # n = len(nums)
# space = O(n)  # nums[:-1] / nums[1:] slice copies
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
# time = O(n)  # n = len(nums)
# space = O(n)  # nums[:-1] / nums[1:] slice copies
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
# time = O(n)  # n = number of tree nodes
# space = O(h)  # h = tree height (recursion stack)
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