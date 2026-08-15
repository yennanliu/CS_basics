"""

3314. Construct the Minimum Bitwise Array I
Easy

You are given an array nums consisting of n prime integers.

You need to construct an array ans of length n, such that, for each index i, the bitwise OR of ans[i] and ans[i] + 1 is equal to nums[i], i.e. ans[i] OR (ans[i] + 1) == nums[i].

Additionally, you must minimize each value of ans[i] in the resulting array.

If it is not possible to find such a value for ans[i] that satisfies the condition, then set ans[i] = -1.


Example 1:

Input: nums = [2,3,5,7]
Output: [-1,1,4,3]
Explanation:
For i = 0, as there is no value for ans[0] that satisfies ans[0] OR (ans[0] + 1) = 2, so ans[0] = -1.
For i = 1, the smallest ans[1] that satisfies ans[1] OR (ans[1] + 1) = 3 is 1, because 1 OR (1 + 1) = 3.
For i = 2, the smallest ans[2] that satisfies ans[2] OR (ans[2] + 1) = 5 is 4, because 4 OR (4 + 1) = 5.
For i = 3, the smallest ans[3] that satisfies ans[3] OR (ans[3] + 1) = 7 is 3, because 3 OR (3 + 1) = 7.

Example 2:

Input: nums = [11,13,31]
Output: [9,12,15]
Explanation:
For i = 0, the smallest ans[0] that satisfies ans[0] OR (ans[0] + 1) = 11 is 9, because 9 OR (9 + 1) = 11.
For i = 1, the smallest ans[1] that satisfies ans[1] OR (ans[1] + 1) = 13 is 12, because 12 OR (12 + 1) = 13.
For i = 2, the smallest ans[2] that satisfies ans[2] OR (ans[2] + 1) = 31 is 15, because 15 OR (15 + 1) = 15.


Constraints:

1 <= nums.length <= 100
2 <= nums[i] <= 1000
nums[i] is a prime number.

"""

# V0
# IDEA : x OR (x+1) ONLY EVER *SETS* A RUN OF LOW BITS — SO CLEAR ONE OF THEM
#
#   adding 1 to x flips its trailing run of 1s to 0 and sets the next 0 bit,
#   so x OR (x+1) equals x with that next 0 bit switched on. the result is
#   therefore always ODD unless x is even with... concretely, n = 2 is the
#   only even prime and it has no solution at all.
#
#   for an odd n, find its lowest ZERO bit at position b (it exists unless n
#   is all ones up to its width, in which case b is just past the top). the
#   answer clears the bit BELOW it :
#
#       ans = n - 2^(b-1)
#
#   e.g. 5 = 101 -> lowest zero at bit 1 -> 5 - 1 = 4, and 4 OR 5 = 5.
#
# time = O(n log max), space = O(n)
class Solution(object):
    def minBitwiseArray(self, nums):
        res = []
        for v in nums:
            if v % 2 == 0:                    # only the prime 2 lands here
                res.append(-1)
                continue
            b = 0
            while (v >> b) & 1:               # first zero bit from the bottom
                b += 1
            res.append(v - (1 << (b - 1)))
        return res
