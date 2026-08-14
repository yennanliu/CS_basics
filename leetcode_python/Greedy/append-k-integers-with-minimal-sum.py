"""

2195. Append K Integers With Minimal Sum
Medium

You are given an integer array nums and an integer k. Append k unique positive integers that do not appear in nums to nums such that the resulting total sum is minimum.

Return the sum of the k integers appended to nums.


Example 1:

Input: nums = [1,4,25,10,25], k = 2
Output: 5
Explanation: The two unique positive integers that do not appear in nums which we append are 2 and 3.
The resulting sum of nums is 1 + 4 + 25 + 10 + 25 + 2 + 3 = 70, which is the minimum.
The sum of the two integers appended is 2 + 3 = 5, so we return 5.

Example 2:

Input: nums = [5,6], k = 6
Output: 25
Explanation: The six unique positive integers that do not appear in nums which we append are 1, 2, 3, 4, 7, and 8.
The resulting sum of nums is 5 + 6 + 1 + 2 + 3 + 4 + 7 + 8 = 36, which is the minimum.
The sum of the six integers appended is 1 + 2 + 3 + 4 + 7 + 8 = 25, so we return 25.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
1 <= k <= 10^8

"""

# V0
# IDEA : WALK THE GAPS BETWEEN THE SORTED DISTINCT VALUES, SUMMING WITH GAUSS
#
#   the k cheapest missing numbers are simply the smallest positive integers
#   absent from nums. k can be 10^8, so they must be summed in BULK, not one
#   at a time.
#
#   sort the distinct values; between the previous taken value `prev` and the
#   next present value v lies the open gap [prev+1, v-1]. take as many of
#   those as still needed and add them with the arithmetic-series formula
#       sum(a..b) = (a + b) * (b - a + 1) / 2
#
#   if k is still positive after the last element, take the run starting just
#   above the maximum.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minimalKSum(self, nums, k):
        def series(a, b):
            return (a + b) * (b - a + 1) // 2

        res = 0
        prev = 0
        for v in sorted(set(nums)):
            if k == 0:
                break
            if v > prev + 1:
                lo = prev + 1
                hi = min(v - 1, lo + k - 1)
                res += series(lo, hi)
                k -= hi - lo + 1
            prev = v

        if k > 0:
            res += series(prev + 1, prev + k)
        return res
