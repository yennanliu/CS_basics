"""

2602. Minimum Operations to Make All Array Elements Equal
Medium

You are given an array nums consisting of positive integers.

You are also given an integer array queries of size m. For the ith query, you want to make all of the elements of nums equal to queries[i]. You can perform the following operation on the array any number of times:

- Increase or decrease an element of the array by 1.

Return an array answer of size m where answer[i] is the minimum number of operations to make all elements of nums equal to queries[i].

Note that after each query the array is reset to its original state.


Example 1:

Input: nums = [3,1,6,8], queries = [1,5]
Output: [14,10]
Explanation: For the first query we can do the following operations:
- Decrease nums[0] 2 times, so that nums = [1,1,6,8].
- Decrease nums[2] 5 times, so that nums = [1,1,1,8].
- Decrease nums[3] 7 times, so that nums = [1,1,1,1].
So the total number of operations for the first query is 2 + 5 + 7 = 14.
For the second query we can do the following operations:
- Increase nums[0] 2 times, so that nums = [5,1,6,8].
- Increase nums[1] 4 times, so that nums = [5,5,6,8].
- Decrease nums[2] 1 time, so that nums = [5,5,5,8].
- Decrease nums[3] 3 times, so that nums = [5,5,5,5].
So the total number of operations for the second query is 2 + 4 + 1 + 3 = 10.

Example 2:

Input: nums = [2,9,6,3], queries = [10]
Output: [20]
Explanation: We can increase each value in the array to 10. The total number of operations will be 8 + 1 + 4 + 7 = 20.


Constraints:

n == nums.length
m == queries.length
1 <= n, m <= 10^5
1 <= nums[i], queries[i] <= 10^9

"""

import bisect

# V0
# IDEA : SORT + PREFIX SUM + BINARY SEARCH
#
#   the answer for a query x is sum(|nums[i] - x|), which splits into
#   the elements below x and the elements above x:
#
#     sort nums, build prefix sums s (s[i] = sum of first i elements)
#     i = # of elements < x                  -> cost up   = x * i - s[i]
#     j = # of elements <= x                 -> cost down = (s[n] - s[j]) - (n - j) * x
#
#   NOTE : elements EQUAL to x must land in neither bucket — they cost 0.
#          using bisect_left for the "below" split and bisect_right for the
#          "above" split drops them out of both sums automatically.
#   NOTE : n, m up to 1e5 so a per-query O(n) scan is too slow; each query
#          must be answered in O(log n).
#
# time = O((n + m) * log n), space = O(n)
class Solution(object):
    def minOperations(self, nums, queries):
        nums.sort()
        n = len(nums)
        s = [0] * (n + 1)
        for i, v in enumerate(nums):
            s[i + 1] = s[i] + v

        res = []
        for x in queries:
            i = bisect.bisect_left(nums, x)   # nums[:i]  are  < x
            j = bisect.bisect_right(nums, x)  # nums[j:]  are  > x
            up = x * i - s[i]
            down = (s[n] - s[j]) - (n - j) * x
            res.append(up + down)
        return res
