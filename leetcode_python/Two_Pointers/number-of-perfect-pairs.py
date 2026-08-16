"""

3649. Number of Perfect Pairs
Medium

You are given an integer array nums.

A pair of indices (i, j) is called perfect if the following conditions are
satisfied:

i < j

Let a = nums[i], b = nums[j]. Then:

min(|a - b|, |a + b|) <= min(|a|, |b|)

max(|a - b|, |a + b|) >= max(|a|, |b|)

Return the number of distinct perfect pairs.

Note: The absolute value |x| refers to the non-negative value of x.

Example 1:

Input: nums = [0,1,2,3]
Output: 2
Explanation:
There are 2 perfect pairs:
(i, j) | (a, b) | min(|a − b|, |a + b|) | min(|a|, |b|) | max(|a − b|, |a + b|) | max(|a|, |b|)
(1, 2) | (1, 2) | min(|1 − 2|, |1 + 2|) = 1 | 1 | max(|1 − 2|, |1 + 2|) = 3 | 2
(2, 3) | (2, 3) | min(|2 − 3|, |2 + 3|) = 1 | 2 | max(|2 − 3|, |2 + 3|) = 5 | 3

Example 2:

Input: nums = [-3,2,-1,4]
Output: 4
Explanation:
There are 4 perfect pairs:
(i, j) | (a, b) | min(|a − b|, |a + b|) | min(|a|, |b|) | max(|a − b|, |a + b|) | max(|a|, |b|)
(0, 1) | (-3, 2) | min(|-3 - 2|, |-3 + 2|) = 1 | 2 | max(|-3 - 2|, |-3 + 2|) = 5 | 3
(0, 3) | (-3, 4) | min(|-3 - 4|, |-3 + 4|) = 1 | 3 | max(|-3 - 4|, |-3 + 4|) = 7 | 4
(1, 2) | (2, -1) | min(|2 - (-1)|, |2 + (-1)|) = 1 | 1 | max(|2 - (-1)|, |2 + (-1)|) = 3 | 2
(1, 3) | (2, 4) | min(|2 - 4|, |2 + 4|) = 2 | 2 | max(|2 - 4|, |2 + 4|) = 6 | 4

Example 3:

Input: nums = [1,10,100,1000]
Output: 0
Explanation:
There are no perfect pairs. Thus, the answer is 0.

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
