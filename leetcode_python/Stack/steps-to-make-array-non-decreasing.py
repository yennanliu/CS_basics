"""

2289. Steps to Make Array Non-decreasing
Medium

You are given a 0-indexed integer array nums. In one step, remove all elements
nums[i] where nums[i - 1] > nums[i] for all 0 < i < nums.length.

Return the number of steps performed until nums becomes a non-decreasing array.


Example 1:

Input: nums = [5,3,4,4,7,3,6,11,8,5,11]
Output: 3
Explanation: The following are the steps performed:
- Step 1: [5,3,4,4,7,3,6,11,8,5,11] becomes [5,4,4,7,6,11,11]
- Step 2: [5,4,4,7,6,11,11] becomes [5,4,7,11,11]
- Step 3: [5,4,7,11,11] becomes [5,7,11,11]
[5,7,11,11] is a non-decreasing array. Therefore, we return 3.

Example 2:

Input: nums = [4,5,7,7,13]
Output: 0
Explanation: nums is already a non-decreasing array. Therefore, we return 0.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
class Solution(object):
    def totalSteps(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass


# V0
# IDEA : MONOTONIC STACK + DP ("WHEN DOES nums[i] GET EATEN?")
#
#   Simulating the rounds is O(n^2). Instead ask, per element, the only
#   question that matters: dp[i] = the round in which nums[i] is removed
#   (0 = never removed). The answer is max(dp).
#
#   nums[i] is eaten by the nearest element on its LEFT that is strictly
#   greater than it — but not before everything between them is gone. So:
#
#     * while nums[i] >= nums[stack[-1]], that top can never eat nums[i];
#       pop it, and inherit its deadline (nums[i] must outlive it).
#     * if the stack still holds something, its value is > nums[i]: that is
#       the killer, and it reaches nums[i] one round after the last of the
#       popped elements died  ->  dp[i] = max(popped dp) + 1
#     * if the stack is now empty, nothing on the left is bigger, so nums[i]
#       survives forever  ->  dp[i] = 0
#
#   e.g. nums = [10,1,2,7] -> dp = [0,1,2,3]: 10 eats 1 in round 1, then 2
#   is exposed and eaten in round 2, then 7 in round 3 -> answer 3
#
# NOTE !!! the pop test is `>=`, not `>`: an equal element is not removed
#          (the rule removes only nums[i-1] > nums[i]), so it is not a
#          killer either and must be popped like the smaller ones.
#
# time = O(n), space = O(n)
class Solution(object):
    def totalSteps(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge
        if not nums:
            return 0

        n = len(nums)
        dp = [0] * n   # dp[i] = round in which nums[i] is removed (0 = never)
        stack = []     # indices, values monotonically DECREASING
        res = 0

        for i in range(n):
            cur = 0
            while stack and nums[i] >= nums[stack[-1]]:
                cur = max(cur, dp[stack.pop()])
            if stack:
                dp[i] = cur + 1
                res = max(res, dp[i])
            stack.append(i)

        return res


# V0-1
# IDEA : Monotonic Stack + DP -> LEFT (gemini)
class Solution(object):

    def totalSteps(self, nums):
        """
        :type nums: List[int]

        :rtype: int
        """
        # 棧中儲存二元組：(數值 num, 該數字被消除所需的步數 step)
        stack = []
        max_steps = 0

        for num in nums:
            cur_steps = 0

            # 單調遞減棧：當前 num >= 棧頂數字時，說明 num 會存活並「吞併」這些較小數字
            # num 必須等待這些較小數字被消除完後，才有機會被更左邊的更大數字消除
            while stack and stack[-1][0] <= num:
                cur_steps = max(cur_steps, stack.pop()[1])

            # 如果棧不為空，說明左邊有一個嚴格大於 num 的數字可以消除它
            if stack:
                cur_steps += 1
            else:
                # 棧為空，說明 num 是當前最大值，永遠不會被消除
                cur_steps = 0

            # 更新全局最大步數
            max_steps = max(max_steps, cur_steps)
            stack.append((num, cur_steps))

        return max_steps


# V0-2
# IDEA : SAME DP, SCANNED RIGHT -> LEFT
#
#   Justified as a variant (not a respelling): scanning from the right lets
#   each element compute its own answer from the elements it EATS, so the
#   "is there a bigger element on my left?" branch of V0 disappears.
#
#   here steps = how many rounds nums[i] spends eating. Popping a smaller
#   right neighbour j costs one more round than what we have eaten so far,
#   but j may itself be busy dying until round dp[j], hence
#
#       steps = max(steps + 1, dp[j])
#
#   and the total number of rounds is max(dp), the longest such chain.
#
# time = O(n), space = O(n)
class Solution2(object):
    def totalSteps(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge
        if not nums:
            return 0

        n = len(nums)
        dp = [0] * n
        stack = []
        res = 0

        for i in range(n - 1, -1, -1):
            steps = 0
            while stack and nums[i] > nums[stack[-1]]:
                steps = max(steps + 1, dp[stack.pop()])
            dp[i] = steps
            res = max(res, steps)
            stack.append(i)

        return res
