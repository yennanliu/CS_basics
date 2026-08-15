"""

3202. Find the Maximum Length of Valid Subsequence II
Medium

You are given an integer array nums and a positive integer k.

A subsequence sub of nums with length x is called valid if it satisfies:

(sub[0] + sub[1]) % k == (sub[1] + sub[2]) % k == ... == (sub[x - 2] + sub[x - 1]) % k.

Return the length of the longest valid subsequence of nums.


Example 1:

Input: nums = [1,2,3,4,5], k = 2
Output: 5
Explanation:
The longest valid subsequence is [1, 2, 3, 4, 5].

Example 2:

Input: nums = [1,4,2,3,1,4], k = 3
Output: 4
Explanation:
The longest valid subsequence is [1, 4, 1, 4].


Constraints:

2 <= nums.length <= 10^3
1 <= nums[i] <= 10^7
1 <= k <= 10^3

"""

# V0
# IDEA : FIX THE COMMON SUM RESIDUE, THEN THE SEQUENCE OF RESIDUES IS FORCED
#
#   if every adjacent pair sums to r modulo k, then knowing one element's
#   residue a pins the next one to (r - a) % k, and the one after that back
#   to a. so for a fixed r the subsequence just alternates between two
#   residue classes.
#
#   that makes a tiny DP per r :
#       best[a] = longest valid subsequence (for this r) ending in residue a
#       best[a] = best[(r - a) % k] + 1     as each element is scanned
#
#   trying all k values of r gives O(n * k) overall.
#
# time = O(n * k), space = O(k)
class Solution(object):
    def maximumLength(self, nums, k):
        res = 0
        for r in range(k):
            best = [0] * k
            for x in nums:
                a = x % k
                partner = (r - a) % k
                if best[partner] + 1 > best[a]:
                    best[a] = best[partner] + 1
            res = max(res, max(best))
        return res
