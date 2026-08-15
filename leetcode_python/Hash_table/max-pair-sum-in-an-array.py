"""

2815. Max Pair Sum in an Array
Easy

You are given an integer array nums. You have to find the maximum sum of a pair of numbers from nums such that the largest digit in both numbers is equal.

For example, 2373 is made up of three distinct digits: 2, 3, and 7, where 7 is the largest among them.

Return the maximum sum or -1 if no such pair exists.


Example 1:

Input: nums = [112,131,411]

Output: -1

Explanation:

Each numbers largest digit in order is [2,3,4].

Example 2:

Input: nums = [2536,1613,3366,162]

Output: 5902

Explanation:

All the numbers have 6 as their largest digit, so the answer is 2536 + 3366 = 5902.

Example 3:

Input: nums = [51,71,17,24,42]

Output: 88

Explanation:

Each number's largest digit in order is [5,7,7,4,4].

So we have only two possible pairs, 71 + 17 = 88 and 24 + 42 = 66.


Constraints:

2 <= nums.length <= 100
1 <= nums[i] <= 10^4

"""

# V0
# IDEA : BUCKET BY LARGEST DIGIT + KEEP THE RUNNING BEST PER BUCKET
#
#   Two numbers may be paired iff they share the same "largest digit", which
#   is a key in 0..9 - only 10 possible buckets. Within one bucket we always
#   want the two biggest values, so there is no need to store the bucket at
#   all: keep `best[d]` = the largest number seen so far whose max digit is d,
#   and while scanning pair each new number with that stored partner.
#
#   NOTE : one single pass suffices - a pair (i, j) with i < j is evaluated
#          exactly when j is scanned, and `best[d]` at that moment is the
#          maximum over all earlier candidates, which is the best partner j
#          could have.
#
#   NOTE : return -1 when no bucket ever holds two numbers; seeding the answer
#          with -1 and only overwriting on a real pair covers this, since all
#          values are positive so any real pair sum is > -1.
#
# time = O(n * log10(max(nums))), space = O(1)   # 10 buckets only
class Solution(object):
    def maxSum(self, nums):
        best = [0] * 10          # best[d] = 0 means "nothing seen for digit d"
        ans = -1
        for v in nums:
            d, t = 0, v
            while t:
                r = t % 10
                if r > d:
                    d = r
                t //= 10
            if best[d]:
                s = best[d] + v
                if s > ans:
                    ans = s
            if v > best[d]:
                best[d] = v
        return ans
