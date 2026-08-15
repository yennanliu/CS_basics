"""

3357. Minimize the Maximum Adjacent Element Difference
Hard

You are given an array of integers nums. Some values in nums are missing and are denoted by -1.

You can choose a pair of positive integers (x, y) exactly once and replace each missing element with either x or y.

You need to minimize the maximum absolute difference between adjacent elements of nums after replacements.

Return the minimum possible difference.


Example 1:

Input: nums = [1,2,-1,10,8]
Output: 4
Explanation:
By choosing the pair as (6, 7), nums can be changed to [1, 2, 6, 10, 8].
The absolute differences between adjacent elements are:
|1 - 2| == 1
|2 - 6| == 4
|6 - 10| == 4
|10 - 8| == 2

Example 2:

Input: nums = [-1,-1,-1]
Output: 0
Explanation:
By choosing the pair as (4, 4), nums can be changed to [4, 4, 4].

Example 3:

Input: nums = [-1,10,-1,8]
Output: 1
Explanation:
By choosing the pair as (11, 9), nums can be changed to [11, 10, 9, 8].


Constraints:

2 <= nums.length <= 10^5
nums[i] is either -1 or in the range [1, 10^9].

"""

# V0
# IDEA : BINARY SEARCH THE ANSWER, GREEDY FEASIBILITY WITH TWO CANDIDATE VALUES
#
#   a larger allowed difference is always easier to satisfy, so binary search
#   over the answer d and ask "can some pair (x, y) keep every adjacency
#   within d ?".
#
#   the fixed-fixed adjacencies are out of our hands — their difference is a
#   lower bound. what is left are the KNOWN NEIGHBOURS of missing runs; each
#   such neighbour v demands that whichever of x or y sits next to it lands
#   in [v - d, v + d].
#
#   for the check, split those neighbours around the midpoint of their range:
#   x serves the small ones and y the large ones. x is pushed as high as the
#   small group allows and y as low as the large group allows, and then
#       - every neighbour must be within d of x or of y
#       - a run of missing cells with known values on BOTH ends must be
#         bridgeable, which needs either both ends served by the same value,
#         or (for runs of length >= 2) one end by x and the other by y with
#         |x - y| <= d
#
#   the two extremes of the neighbour multiset drive all of it, so the check
#   is a linear scan.
#
# time = O(n log(max value)), space = O(n)
class Solution(object):
    def minDifference(self, nums):
        n = len(nums)

        base = 0                                # fixed-fixed adjacencies
        for i in range(n - 1):
            if nums[i] != -1 and nums[i + 1] != -1:
                base = max(base, abs(nums[i] - nums[i + 1]))

        # runs of -1 with their known neighbours
        runs = []                               # (left value or None, right value or None, length)
        i = 0
        while i < n:
            if nums[i] != -1:
                i += 1
                continue
            j = i
            while j < n and nums[j] == -1:
                j += 1
            left = nums[i - 1] if i > 0 else None
            right = nums[j] if j < n else None
            runs.append((left, right, j - i))
            i = j

        if not runs:
            return base

        neighbours = []
        for left, right, _ in runs:
            if left is not None:
                neighbours.append(left)
            if right is not None:
                neighbours.append(right)
        if not neighbours:
            return base

        lo_v, hi_v = min(neighbours), max(neighbours)

        def feasible(d):
            # x covers the low neighbours, y the high ones
            x = lo_v + d
            y = hi_v - d
            for left, right, length in runs:
                ends = [v for v in (left, right) if v is not None]
                if not ends:
                    continue
                ok = True
                for v in ends:
                    if abs(v - x) > d and abs(v - y) > d:
                        ok = False
                        break
                if not ok:
                    return False
                if len(ends) == 2 and length == 1:
                    # one value must serve both ends at once
                    a, b = ends
                    if not ((abs(a - x) <= d and abs(b - x) <= d)
                            or (abs(a - y) <= d and abs(b - y) <= d)):
                        return False
                elif len(ends) == 2:
                    a, b = ends
                    same = ((abs(a - x) <= d and abs(b - x) <= d)
                            or (abs(a - y) <= d and abs(b - y) <= d))
                    mixed = (((abs(a - x) <= d and abs(b - y) <= d)
                              or (abs(a - y) <= d and abs(b - x) <= d))
                             and abs(x - y) <= d)
                    if not (same or mixed):
                        return False
            return True

        lo, hi = base, max(base, hi_v - lo_v)
        while lo < hi:
            mid = (lo + hi) // 2
            if feasible(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo
