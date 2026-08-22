"""

3584. Maximum Product of First and Last Elements of a Subsequence
Medium

You are given an integer array nums and an integer m.

Return the maximum product of the first and last elements of any subsequence of nums of size m.


Example 1:

Input: nums = [-1,-9,2,3,-2,-3,1], m = 1
Output: 81
Explanation:
The subsequence [-9] has the largest product of the first and last elements: -9 * -9 = 81. Therefore, the answer is 81.

Example 2:

Input: nums = [1,3,-5,5,6,-4], m = 3
Output: 20
Explanation:
The subsequence [-5, 6, -4] has the largest product of the first and last elements.

Example 3:

Input: nums = [2,-1,2,-6,5,2,-5,7], m = 2
Output: 35
Explanation:
The subsequence [5, 7] has the largest product of the first and last elements.


Constraints:

1 <= nums.length <= 10^5
-10^5 <= nums[i] <= 10^5
1 <= m <= nums.length

"""

# V0
# IDEA : ONLY THE TWO ENDPOINTS MATTER, SO SLIDE A GAP OF m-1
#
#   the middle m-2 picks are irrelevant to the score; a subsequence of size
#   m exists with endpoints i and j exactly when j - i >= m - 1. so the task
#   collapses to "maximise nums[i] * nums[j] over all pairs at least m-1
#   apart".
#
#   because of negative values the best partner for nums[j] is either the
#   running maximum or the running minimum of the allowed prefix, and both
#   are maintained in O(1) as j advances.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumProduct(self, nums, m):
        n = len(nums)
        hi = float('-inf')
        lo = float('inf')
        ans = float('-inf')
        for j in range(m - 1, n):
            i = j - m + 1
            v = nums[i]
            if v > hi:
                hi = v
            if v < lo:
                lo = v
            b = nums[j]
            c1 = hi * b
            c2 = lo * b
            if c1 > ans:
                ans = c1
            if c2 > ans:
                ans = c2
        return ans


# V0-1
# IDEA : BRUTE FORCE OVER EVERY VALID (first, last) PAIR
#
#   a size-m subsequence with endpoints i and j exists exactly when
#   j - i >= m - 1 (i == j is allowed when m == 1), so score every such pair
#   directly and keep the best.
#   quadratic is far too slow at n = 10^5, but it is the literal definition of
#   the problem and therefore the reference oracle for the linear versions.
#
# time = O(n^2)
# space = O(1)
class Solution(object):
    def maximumProduct(self, nums, m):
        n = len(nums)
        ans = float('-inf')
        for i in range(n):
            for j in range(i + m - 1, n):
                p = nums[i] * nums[j]
                if p > ans:
                    ans = p
        return ans


# V0-2
# IDEA : TWO PASSES WITH EXPLICIT PREFIX MAX / MIN TABLES
#
#   pass 1 tabulates pmax[i] / pmin[i] = max / min of nums[0..i].
#   pass 2 then answers each right endpoint j in O(1) by reading index
#   j - m + 1: the best partner is either the largest or the smallest value in
#   the allowed prefix, since nums[j] may be negative.
#   same maths as V0, but the prefix statistics are stored instead of streamed
#   -- the shape to reach for when the table is needed again (other m values,
#   or reconstructing which indices won).
#
# time = O(n)
# space = O(n)
class Solution(object):
    def maximumProduct(self, nums, m):
        n = len(nums)
        pmax = [0] * n
        pmin = [0] * n
        pmax[0] = pmin[0] = nums[0]
        for i in range(1, n):
            pmax[i] = pmax[i - 1] if pmax[i - 1] > nums[i] else nums[i]
            pmin[i] = pmin[i - 1] if pmin[i - 1] < nums[i] else nums[i]
        ans = float('-inf')
        for j in range(m - 1, n):
            i = j - m + 1
            b = nums[j]
            c = pmax[i] * b
            if c > ans:
                ans = c
            c = pmin[i] * b
            if c > ans:
                ans = c
        return ans
