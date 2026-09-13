"""

456. 132 Pattern
Medium

Given an array of n integers nums, a 132 pattern is a subsequence of three integers nums[i], nums[j] and nums[k] such that i < j < k and nums[i] < nums[k] < nums[j].

Return true if there is a 132 pattern in nums, otherwise, return false.

Example 1:

Input: nums = [1,2,3,4]
Output: false
Explanation: There is no 132 pattern in the sequence.

Example 2:

Input: nums = [3,1,4,2]
Output: true
Explanation: There is a 132 pattern in the sequence: [1, 4, 2].

Example 3:

Input: nums = [-1,3,2,0]
Output: true
Explanation: There are three 132 patterns in the sequence: [-1, 3, 2], [-1, 3, 0] and [-1, 2, 0].

Constraints:

n == nums.length
1 <= n <= 2 * 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0

# V1 
# https://blog.csdn.net/u011693064/article/details/54650260
# https://blog.csdn.net/fuxuemingzhu/article/details/81869398
# time = O(n)
# space = O(n)
class Solution:
    def find132pattern(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        if len(nums) <=2:
            return False
        third = float('-inf')
        stack = []
        for i in range(len(nums)-1, -1, -1):
            if nums[i] < third:
                return True
            else:
                while stack and stack[-1] < nums[i]:
                    third = stack.pop()
            stack.append(nums[i])
        return False
        
# V2 
# time = O(n)
# space = O(n)
class Solution(object):
    def find132pattern(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        ak = float("-inf")
        st = []
        for i in reversed(xrange(len(nums))):
            if nums[i] < ak:
                return True
            else:
                while st and nums[i] > st[-1]:
                    ak = st.pop()
            st.append(nums[i])
        return False