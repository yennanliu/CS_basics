"""

644. Maximum Average Subarray II
Hard

You are given an integer array nums consisting of n elements, and an integer k.

Find a contiguous subarray whose length is greater than or equal to k that has the
maximum average value and return this value.
Any answer with a calculation error less than 10^-5 will be accepted.

Example 1:

Input: nums = [1,12,-5,-6,50,3], k = 4
Output: 12.75000
Explanation:
- When the length is 4, averages are [0.5, 12.75, 10.5] and the maximum average is 12.75
- When the length is 5, averages are [10.4, 10.8] and the maximum average is 10.8
- When the length is 6, averages are [9.16667] and the maximum average is 9.16667
The maximum average is when we choose a subarray of length 4 (i.e., the sub array
[12, -5, -6, 50]) which has the max average 12.75, so we return 12.75
Note that we do not consider the subarrays of length < 4.

Example 2:

Input: nums = [5], k = 1
Output: 5.00000

Constraints:

n == nums.length
1 <= k <= n <= 10^4
-10^4 <= nums[i] <= 10^4

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER (a real value) + PREFIX SUM
#
#   Guess an average `v` and ask: is there a subarray of length >= k whose
#   average is >= v ?
#
#     (a_1 + ... + a_j) / j >= v
#     <=> (a_1 - v) + ... + (a_j - v) >= 0
#
#   So subtract v from every element and look for a subarray of length >= k
#   with a non-negative sum. With prefix sums S of the shifted array, that is:
#
#     exists i >= k  with  S[i] - min(S[0..i-k]) >= 0
#
#   which one linear pass computes, keeping the running minimum of the prefix
#   sums that are at least k positions behind.
#
#   The predicate is monotone in v (true for small v, false for large v), so
#   binary search on the real interval [min(nums), max(nums)].
#
# time = O(n * log((max - min) / eps))
# space = O(1)
class Solution(object):
    def findMaxAverage(self, nums, k):
        n = len(nums)
        EPS = 1e-6

        def can_reach(v):
            """is there a subarray of length >= k with average >= v ?"""
            # sum of the first k shifted elements
            window = 0.0
            for i in range(k):
                window += nums[i] - v
            if window >= 0:
                return True

            # prefix sum lagging k behind, and its running minimum
            lag = 0.0
            min_lag = 0.0
            for i in range(k, n):
                window += nums[i] - v
                lag += nums[i - k] - v
                min_lag = min(min_lag, lag)
                if window - min_lag >= 0:
                    return True
            return False

        lo = float(min(nums))
        hi = float(max(nums))
        while hi - lo > EPS:
            mid = (lo + hi) / 2.0
            if can_reach(mid):
                lo = mid
            else:
                hi = mid

        return lo


# V0-1
# IDEA : BRUTE FORCE OVER EVERY SUBARRAY (running sum, no prefix array)
#
#   fix the left endpoint i, extend the right endpoint j and keep the running
#   sum. once the window is long enough (j - i + 1 >= k) its average is a
#   candidate. no monotonicity is exploited, so every one of the O(n^2)
#   subarrays is priced explicitly.
#
#   too slow for n = 10^4, but it is the definition of the problem and is what
#   the smarter versions get validated against.
#
# time = O(n^2)
# space = O(1)
class Solution(object):
    def findMaxAverage(self, nums, k):
        n = len(nums)
        res = float("-inf")
        for i in range(n):
            total = 0
            for j in range(i, n):
                total += nums[j]
                length = j - i + 1
                if length >= k:
                    res = max(res, total / float(length))
        return res


# V0-2
# IDEA : CONVEX HULL TRICK (MAX SLOPE BETWEEN PREFIX-SUM POINTS) - EXACT, O(n)
#
#   with prefix sums P, the average of nums[i..j-1] is
#
#       (P[j] - P[i]) / (j - i)
#
#   which is exactly the SLOPE of the line through the plane points
#   A_i = (i, P[i]) and A_j = (j, P[j]). so the task becomes: maximise the
#   slope over all pairs with j - i >= k.
#
#   for a fixed right point A_j, the left point maximising the slope always
#   lies on the LOWER convex hull of {A_0 ... A_{j-k}} - any point strictly
#   above the hull is dominated. so:
#
#     * as j advances, the newly legal left index j-k is pushed onto a
#       monotonic stack that keeps the hull convex (pop while the middle point
#       sits on/above the segment through its neighbours),
#     * the best hull vertex for j is found by a pointer that only ever moves
#       FORWARD, because that optimum is non-decreasing in j.
#
#   every index is pushed once, popped at most once, and the pointer advances
#   at most n times -> linear, and unlike V0 this is exact (no epsilon).
#
# time = O(n)
# space = O(n)
class Solution(object):
    def findMaxAverage(self, nums, k):
        n = len(nums)
        pre = [0] * (n + 1)
        for i, v in enumerate(nums):
            pre[i + 1] = pre[i] + v

        def slope(a, b):
            return (pre[b] - pre[a]) / float(b - a)

        def useless(a, b, c):
            # b is on/above segment (a, c) -> it can never be a hull optimum
            return (pre[b] - pre[a]) * (c - a) >= (pre[c] - pre[a]) * (b - a)

        hull = []
        ptr = 0
        res = float("-inf")
        for j in range(k, n + 1):
            # index j - k just became a legal left endpoint
            new = j - k
            while len(hull) >= 2 and useless(hull[-2], hull[-1], new):
                hull.pop()
            hull.append(new)

            if ptr >= len(hull):
                ptr = len(hull) - 1
            while (ptr + 1 < len(hull)
                   and slope(hull[ptr], j) <= slope(hull[ptr + 1], j)):
                ptr += 1

            res = max(res, slope(hull[ptr], j))

        return res
