"""

3649. Number of Perfect Pairs
Medium

You are given an integer array nums.

A pair of indices (i, j) is called perfect if the following conditions are satisfied:

i < j
Let a = nums[i], b = nums[j], then:
  min(|a - b|, |a + b|) <= min(|a|, |b|)
  max(|a - b|, |a + b|) >= max(|a|, |b|)

Return the number of distinct perfect pairs.


Example 1:

Input: nums = [0,1,2,-3]
Output: 2
Explanation:
The perfect pairs are (1, 2) with values (1, 2) and (2, 3) with values (2, -3).

Example 2:

Input: nums = [-3,2,-1,4]
Output: 4
Explanation:
The perfect pairs are the ones formed by the values (-3, 2), (-3, 4), (2, -1) and (2, 4).


Constraints:

2 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : SIGNS CANCEL — THE CONDITION IS JUST max|.| <= 2 * min|.|
#
#   write x = min(|a|,|b|) and y = max(|a|,|b|). whatever the signs are, the
#   unordered pair {|a-b|, |a+b|} always equals {y - x, y + x}: if a and b
#   share a sign the difference is y-x, otherwise the sum is.
#
#   so min(|a-b|,|a+b|) = y - x and max(...) = y + x, and the two conditions
#   collapse to
#       y - x <= x   ->  y <= 2x
#       y + x >= y   ->  always true (x >= 0)
#
#   after taking absolute values and sorting, "y <= 2x" is a window condition
#   whose left end only moves right, so two pointers count all pairs.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def perfectPairs(self, nums):
        a = sorted(abs(v) for v in nums)
        res = 0
        l = 0
        for r in range(len(a)):
            while a[r] > 2 * a[l]:
                l += 1
            res += r - l
        return res
