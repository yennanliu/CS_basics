"""

45. Jump Game II
Medium

Given an array of non-negative integers nums, you are initially positioned at the first index of the array.

Each element in the array represents your maximum jump length at that position.

Your goal is to reach the last index in the minimum number of jumps.

You can assume that you can always reach the last index.

 

Example 1:

Input: nums = [2,3,1,1,4]
Output: 2
Explanation: The minimum number of jumps to reach the last index is 2. Jump 1 step from index 0 to 1, then 3 steps to the last index.
Example 2:

Input: nums = [2,3,0,1,4]
Output: 2
 

Constraints:

1 <= nums.length <= 104
0 <= nums[i] <= 1000

"""

# V0
# IDEA : GREEDY
"""
Steps:
    step 1) Initialize three integer variables: jumps to count the number of jumps, currentJumpEnd to mark the end of the range that we can jump to, and farthest to mark the farthest place that we can reach. Set each variable to zero
    step 2) terate over nums. Note that we exclude the last element from our iteration because as soon as we reach the last element, we do not need to jump anymore.
            - Update farthest to i + nums[i] if the latter is larger.
            - If we reach currentJumpEnd, it means we finished the current jump, and can begin checking the next jump by setting currentJumpEnd = farthest.
    step 3) return jumps
"""
class Solution:
    def jump(self, nums: List[int]) -> int:
            jumps = 0
            current_jump_end = 0
            farthest = 0
            for i in range(len(nums) - 1):
                # we continuously find the how far we can reach in the current jump
                farthest = max(farthest, i + nums[i])
                # if we have come to the end of the current jump,
                # we need to make another jump
                if i == current_jump_end:
                    jumps += 1
                    current_jump_end = farthest
            return jumps

# V1
# IDEA : GREEDY
# https://leetcode.com/problems/jump-game-ii/discuss/1672485/Python-Greedy
class Solution:
    def jump(self, nums: List[int]) -> int:
        l = r = res = farthest = 0
        while r < len(nums) - 1:
            for idx in range(l, r+1):
                farthest = max(farthest, idx + nums[idx])
            l = r+1
            r = farthest 
            res += 1
        return res

# V1'
# IDEA : GREEDY
# https://blog.csdn.net/fuxuemingzhu/article/details/84578893
# https://blog.51cto.com/u_15302258/3078964
class Solution:
    def jump(self, nums):
        reach = 0
        cur = 0
        N = len(nums)
        count = 0
        pos = 0
        while cur < N - 1:
            count += 1
            pre = cur
            while pos <= pre:
                cur = max(cur, pos + nums[pos])
                pos += 1
        return count

# V1''
# IDEA : TWO POINTERS
# https://leetcode.com/problems/jump-game-ii/discuss/431343/python-two-pointer
# IDEA :
# core idea come from bfs from each idx and create nxt level range node and maintain seen hashset.
# since new level node are always continous range number, we can just keep a range with two pointer.
# each idx will create a range can jump(for x<=right).just keep this range and move forward.
class Solution:
    def jump(self, nums: List[int]) -> int:
        l=r=steps=0
        while r<len(nums)-1:
            steps+=1
            l,r=r+1,max(idx+nums[idx] for idx in range(l,r+1))
        return steps

# V1'''
# IDEA : GREEDY
# https://leetcode.com/problems/jump-game-ii/solution/
"""
Steps:
    step 1) Initialize three integer variables: jumps to count the number of jumps, currentJumpEnd to mark the end of the range that we can jump to, and farthest to mark the farthest place that we can reach. Set each variable to zero
    step 2) terate over nums. Note that we exclude the last element from our iteration because as soon as we reach the last element, we do not need to jump anymore.
            - Update farthest to i + nums[i] if the latter is larger.
            - If we reach currentJumpEnd, it means we finished the current jump, and can begin checking the next jump by setting currentJumpEnd = farthest.
    step 3) return jumps
"""
class Solution:
    def jump(self, nums: List[int]) -> int:
            jumps = 0
            current_jump_end = 0
            farthest = 0
            for i in range(len(nums) - 1):
                # we continuously find the how far we can reach in the current jump
                farthest = max(farthest, i + nums[i])
                # if we have come to the end of the current jump,
                # we need to make another jump
                if i == current_jump_end:
                    jumps += 1
                    current_jump_end = farthest
            return jumps

# V1''''
# IDEA : memorization
# https://leetcode.com/problems/jump-game-ii/discuss/433949/python-memorization-
class Solution:
    def jump(self, nums):
            num_jump = 0
            far, max_dis = 0, 0
            for i, num in enumerate(nums):
                max_dis = max(max_dis, num+i)
                if far<=i and i<len(nums)-1:
                    num_jump+=1
                    far = max_dis

            return num_jump

# V1''''''
# IDEA : DP
# https://leetcode.com/problems/jump-game-ii/discuss/1455589/Python-Dynamic-Programming
import math
class Solution:
    # Dynamic Programming
    def jump(self, nums: List[int]) -> int:
        jmps = [math.inf for _ in range(len(nums))]
        jmps[0] = 0
        for i in range(len(nums) - 1):
            farthest = min(i + nums[i], len(nums) - 1)
            for j in range(i + 1, farthest + 1):
                jmps[j] = min(jmps[j], jmps[i] + 1)
        return jmps[-1]

# V2