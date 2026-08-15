"""

3312. Sorted GCD Pair Queries
Hard

You are given an integer array nums of length n and an integer array queries.

Let gcdPairs denote an array obtained by calculating GCD(nums[i], nums[j]) for all 0 <= i < j < n, and then sorting these values in ascending order.

For each query queries[i], you need to find the element at index queries[i] in gcdPairs.

Return an integer array answer, where answer[i] is the value at gcdPairs[queries[i]] for each query.

The term gcd(a, b) denotes the greatest common divisor of a and b.


Example 1:

Input: nums = [2,3,4], queries = [0,2,2]
Output: [1,2,2]
Explanation:
gcdPairs = [gcd(nums[0], nums[1]), gcd(nums[0], nums[2]), gcd(nums[1], nums[2])] = [1, 2, 1].
After sorting in ascending order, gcdPairs = [1, 1, 2].
So, the answer is [gcdPairs[0], gcdPairs[2], gcdPairs[2]] = [1, 2, 2].

Example 2:

Input: nums = [4,4,2,1], queries = [5,3,1,0]
Output: [4,2,1,1]
Explanation:
gcdPairs sorted in ascending order is [1, 1, 1, 2, 2, 4].

Example 3:

Input: nums = [2,2], queries = [0,0]
Output: [2,2]
Explanation:
gcdPairs = [2].


Constraints:

2 <= n == nums.length <= 10^5
1 <= nums[i] <= 5 * 10^4
1 <= queries.length <= 10^5
0 <= queries[i] < n * (n - 1) / 2

"""

# V0
# IDEA : COUNT PAIRS BY GCD WITH A DIVISOR SIEVE, THEN BINARY SEARCH THE PREFIX
#
#   there are up to 5 * 10^9 pairs, so gcdPairs can never be built. but the
#   VALUES are bounded by 5 * 10^4, so counting how many pairs share each gcd
#   is enough.
#
#   for a divisor d let m[d] be how many elements are multiples of d — a
#   harmonic sieve over the value histogram. then C(m[d], 2) counts the pairs
#   whose gcd is a MULTIPLE of d, and peeling those off from large d down
#   leaves the exact count :
#
#       exact[d] = C(m[d], 2) - sum over multiples e > d of exact[e]
#
#   a prefix sum of exact turns each query into a binary search for the first
#   value whose running total exceeds the query index.
#
# time = O(max log max + q log max), space = O(max)
import bisect


class Solution(object):
    def gcdValues(self, nums, queries):
        top = max(nums)
        freq = [0] * (top + 1)
        for v in nums:
            freq[v] += 1

        # multiples[d] = how many elements are divisible by d
        multiples = [0] * (top + 1)
        for d in range(1, top + 1):
            s = 0
            for m in range(d, top + 1, d):
                s += freq[m]
            multiples[d] = s

        exact = [0] * (top + 1)
        for d in range(top, 0, -1):
            cnt = multiples[d] * (multiples[d] - 1) // 2
            for m in range(2 * d, top + 1, d):
                cnt -= exact[m]
            exact[d] = cnt

        prefix = [0] * (top + 1)
        run = 0
        for d in range(1, top + 1):
            run += exact[d]
            prefix[d] = run

        # prefix[d] = how many pairs have gcd <= d
        return [bisect.bisect_right(prefix, q) for q in queries]
