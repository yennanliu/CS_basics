"""

2455. Average Value of Even Numbers That Are Divisible by Three
Easy

Given an integer array nums of positive integers, return the average value of all even integers that are divisible by 3.

Note that the average of n elements is the sum of the n elements divided by n and rounded down to the nearest integer.


Example 1:

Input: nums = [1,3,6,10,12,15]
Output: 9
Explanation: 6 and 12 are even numbers that are divisible by 3. (6 + 12) / 2 = 9.

Example 2:

Input: nums = [1,2,4,7,10]
Output: 0
Explanation: There is no single number that satisfies the requirement, so return 0.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 1000

"""

# V0
# IDEA : "EVEN AND DIVISIBLE BY 3" IS EXACTLY "DIVISIBLE BY 6"
#
#   2 and 3 are coprime, so a single x % 6 == 0 test replaces both checks.
#
#   the average is floored (integer division), and an empty selection returns
#   0 rather than dividing by zero.
#
# time = O(n), space = O(n)
class Solution(object):
    def averageValue(self, nums):
        picked = [x for x in nums if x % 6 == 0]
        if not picked:
            return 0
        return sum(picked) // len(picked)
