"""

3423. Maximum Difference Between Adjacent Elements in a Circular Array
Easy

Given a circular array nums, find the maximum absolute difference between
adjacent elements.

Note: In a circular array, the first and last elements are adjacent.

Example 1:

Input: nums = [1,2,4]

Output: 3

Explanation:

Because nums is circular, nums[0] and nums[2] are adjacent. They have the
maximum absolute difference of |4 - 1| = 3.

Example 2:

Input: nums = [-5,-10,-5]

Output: 5

Explanation:

The adjacent elements nums[0] and nums[1] have the maximum absolute difference
of |-5 - (-10)| = 5.

Constraints:

2 <= nums.length <= 100
-100 <= nums[i] <= 100

"""

# V0
# IDEA : SCAN THE PAIRS, WRAPPING THE INDEX WITH MODULO
#
#   a circular array has exactly n adjacent pairs — the n - 1 ordinary ones plus
#   the (last, first) pair — so indexing with (i + 1) % n visits every pair once
#   and no pair twice.  no structure to exploit beyond that.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxAdjacentDistance(self, nums):
        n = len(nums)
        return max(abs(nums[i] - nums[(i + 1) % n]) for i in range(n))


# V0-1
# IDEA : DIVIDE AND CONQUER (RECURSION) OVER THE n CIRCULAR PAIRS
#
#   the n adjacent pairs are indexed 0 .. n-1, pair i being
#   (nums[i], nums[(i+1) % n]).  "max over a range of pairs" splits cleanly :
#
#       best(lo, hi) = max( best(lo, mid), best(mid+1, hi) )
#
#   with a single pair as the base case.  same n comparisons of work as the
#   flat scan, but the control flow is a recursion tree instead of a loop -
#   the shape you need when the same reduction has to be parallelised or run
#   over a segment tree.
#
# time = O(n)
# space = O(log n) recursion stack
class Solution(object):
    def maxAdjacentDistance(self, nums):
        n = len(nums)

        def solve(lo, hi):
            if lo == hi:
                return abs(nums[hi] - nums[(hi + 1) % n])
            mid = (lo + hi) // 2
            return max(solve(lo, mid), solve(mid + 1, hi))

        return solve(0, n - 1)


# V0-2
# IDEA : DROP abs() - TWO SIGNED PASSES OVER A ROTATED COPY
#
#   abs() can be decomposed :  max |x - y| = max( max (y - x), max (x - y) ),
#   so the answer is the larger of two plain signed maxima.
#
#   pairing is handled by rotating the array by one (nxt[i] is the circular
#   successor of nums[i]) and zipping, so neither modulo arithmetic nor an
#   index variable appears - the two passes are pure streaming reductions.
#
# time = O(n) (two passes)
# space = O(n) for the rotated copy
class Solution(object):
    def maxAdjacentDistance(self, nums):
        nxt = nums[1:] + nums[:1]
        up = max(b - a for a, b in zip(nums, nxt))
        down = max(a - b for a, b in zip(nums, nxt))
        return max(up, down)
