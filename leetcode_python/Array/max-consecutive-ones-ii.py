"""

487. Max Consecutive Ones II
Medium

Given a binary array nums, return the maximum number of consecutive 1's in the array if you can flip at most one 0.

Example 1:

Input: nums = [1,0,1,1,0]
Output: 4
Explanation:
- If we flip the first zero, nums becomes [1,1,1,1,0] and we have 4 consecutive ones.
- If we flip the second zero, nums becomes [1,0,1,1,1] and we have 3 consecutive ones.
The max number of consecutive ones is 4.

Example 2:

Input: nums = [1,0,1,1,0,1]
Output: 4
Explanation:
- If we flip the first zero, nums becomes [1,1,1,1,0,1] and we have 4 consecutive ones.
- If we flip the second zero, nums becomes [1,0,1,1,1,1] and we have 4 consecutive ones.
The max number of consecutive ones is 4.


Constraints:

1 <= nums.length <= 10^5
nums[i] is either 0 or 1.


Follow up: What if the input numbers come in one by one as an infinite stream?
In other words, you can't store all numbers coming from the stream as it's too large
to hold in memory. Could you solve it efficiently?

"""

# V0
# IDEA : SLIDING WINDOW (at most one 0 inside the window)
# time = O(n)
# space = O(1)
class Solution(object):
    def findMaxConsecutiveOnes(self, nums):
        left = 0
        zero_cnt = 0
        res = 0

        for right in range(len(nums)):
            if nums[right] == 0:
                zero_cnt += 1

            # shrink from left until the window holds at most one 0
            while zero_cnt > 1:
                if nums[left] == 0:
                    zero_cnt -= 1
                left += 1

            res = max(res, right - left + 1)

        return res

# V1
# IDEA : STREAMING (O(1) space, single pass, no random access on nums)
#        -> keeps only "# of 1s before the last 0" and "# of 1s after the last 0"
#        -> this answers the follow up (infinite stream)
# time = O(n)
# space = O(1)
class Solution(object):
    def findMaxConsecutiveOnes(self, nums):
        prev = 0   # count of consecutive 1s BEFORE the most recent 0
        cur = 0    # count of consecutive 1s AFTER the most recent 0
        seen_zero = False
        res = 0

        for x in nums:
            if x == 1:
                cur += 1
            else:
                # the current 0 becomes the (only) flipped one
                prev = cur
                cur = 0
                seen_zero = True
            # +1 only if there is actually a 0 we can flip
            res = max(res, prev + cur + (1 if seen_zero else 0))

        return res
