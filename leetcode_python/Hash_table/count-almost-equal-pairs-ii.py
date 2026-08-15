"""

3267. Count Almost Equal Pairs II
Hard

Attention: In this version, the number of operations that can be performed, has been increased to twice.

You are given an array nums consisting of positive integers.

We call two integers x and y almost equal if both integers can become equal after performing the following operation at most twice:

Choose either x or y and swap any two digits within the chosen number.

Return the number of indices i and j in nums where i < j such that nums[i] and nums[j] are almost equal.

Note that it is allowed for an integer to have leading zeros after performing an operation.


Example 1:

Input: nums = [1023,2310,2130,213]
Output: 4
Explanation:
The almost equal pairs of elements are:
1023 and 2310. By swapping the digits 1 and 2, and then the digits 0 and 3, you get 2310.
1023 and 213. By swapping the digits 1 and 0, and then the digits 1 and 2, you get 0213 which is 213.
2310 and 213. By swapping the digits 2 and 0, and then the digits 3 and 2, you get 0213 which is 213.
2310 and 2130. By swapping the digits 3 and 1, you get 2130.

Example 2:

Input: nums = [1,10,100]
Output: 3
Explanation:
The almost equal pairs of elements are:
1 and 10. By swapping the digits 1 and 0, you get 01 which is 1.
1 and 100. By swapping the second 0 with the digit 1, you get 001 which is 1.
10 and 100. By swapping the first 0 with the digit 1, you get 010 which is 10.


Constraints:

2 <= nums.length <= 5000
1 <= nums[i] < 10^7

"""

# V0
# IDEA : ENUMERATE EVERY STRING REACHABLE FROM x IN AT MOST TWO SWAPS
#
#   a swap applied to y can always be replayed as its inverse on x, so
#   "a swaps on x plus b swaps on y with a + b <= 2" is the same as "at most
#   two swaps on x alone". that turns the pairing test into a lookup.
#
#   with values below 10^7 the padded width is at most 7 digits, so one
#   number has at most 1 + 21 + 21*21 = 463 variants before deduplication —
#   cheap to generate.
#
#   sweeping left to right and counting how many ALREADY SEEN numbers appear
#   among the current one's variants gives every pair exactly once, with the
#   i < j ordering for free.
#
# time = O(n * d^4), space = O(n)
from collections import Counter


class Solution(object):
    def countPairs(self, nums):
        strs = [str(v) for v in nums]
        width = max(len(t) for t in strs)
        strs = [t.rjust(width, '0') for t in strs]

        def variants(t):
            out = set([t])
            base = list(t)
            for i in range(width):
                for j in range(i + 1, width):
                    one = base[:]
                    one[i], one[j] = one[j], one[i]
                    out.add(''.join(one))
                    for a in range(width):
                        for b in range(a + 1, width):
                            two = one[:]
                            two[a], two[b] = two[b], two[a]
                            out.add(''.join(two))
            return out

        seen = Counter()
        res = 0
        for t in strs:
            for v in variants(t):
                res += seen[v]
            seen[t] += 1
        return res
