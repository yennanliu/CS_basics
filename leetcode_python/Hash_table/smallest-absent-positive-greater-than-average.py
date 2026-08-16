"""

3678. Smallest Absent Positive Greater Than Average
Easy

You are given an integer array nums.

Return the smallest absent positive integer in nums such that it is
strictly greater than the average of all elements in nums.

The average of an array is defined as the sum of all its elements divided
by the number of elements.


Example 1:

Input: nums = [3,5]
Output: 6
Explanation:
The average of nums is (3 + 5) / 2 = 8 / 2 = 4.
The smallest absent positive integer greater than 4 is 6.

Example 2:

Input: nums = [-1,1,2]
Output: 3
Explanation:
The average of nums is (-1 + 1 + 2) / 3 = 2 / 3 = 0.667.
The smallest absent positive integer greater than 0.667 is 3.

Example 3:

Input: nums = [4,-1]
Output: 2
Explanation:
The average of nums is (4 + (-1)) / 2 = 3 / 2 = 1.50.
The smallest absent positive integer greater than 1.50 is 2.


Constraints:

1 <= nums.length <= 100
-100 <= nums[i] <= 100

"""

# V0
# IDEA : FLOOR OF THE AVERAGE + 1, THEN WALK PAST PRESENT VALUES
#
#   two conditions must hold: the answer is a positive integer strictly
#   above the average, and it is absent from nums. the first condition has a
#   closed form -- the smallest integer strictly greater than avg is
#   floor(avg) + 1, and that identity holds whether or not avg is itself an
#   integer (if avg == a then floor(a) + 1 == a + 1 > a). clamping with
#   max(1, ...) enforces positivity when the average is small or negative.
#
#   using integer floor division sum // n rather than float division avoids
#   any rounding worry, and python's // already floors toward -inf, which is
#   the direction we want for negative sums.
#
#   from that starting point we just step up while the candidate is present.
#   at most n distinct values can block us, so the walk is short.
#
# time = O(n), space = O(n)
class Solution(object):
    def smallestAbsent(self, nums):
        seen = set(nums)
        ans = max(1, sum(nums) // len(nums) + 1)
        while ans in seen:
            ans += 1
        return ans
