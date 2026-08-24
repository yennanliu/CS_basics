"""

2475. Number of Unequal Triplets in Array
Easy

You are given a 0-indexed array of positive integers nums. Find the number of triplets (i, j, k) that meet the following conditions:

0 <= i < j < k < nums.length
nums[i], nums[j], and nums[k] are pairwise distinct.
In other words, nums[i] != nums[j], nums[i] != nums[k], and nums[j] != nums[k].

Return the number of triplets that meet the conditions.


Example 1:

Input: nums = [4,4,2,4,3]
Output: 3
Explanation: The following triplets meet the conditions:
- (0, 2, 4) because 4 != 2 != 3
- (1, 2, 4) because 4 != 2 != 3
- (2, 3, 4) because 2 != 4 != 3
Since there are 3 triplets, we return 3.
Note that (2, 0, 4) is not a valid triplet because 2 > 0.

Example 2:

Input: nums = [1,1,1,1,1]
Output: 0
Explanation: No triplets meet the conditions so we return 0.


Constraints:

3 <= nums.length <= 100
1 <= nums[i] <= 1000

"""

# V0
# IDEA : GROUP COUNTING (pick 3 DIFFERENT value-groups, order does not matter)
#
#   the index order i < j < k is irrelevant : any set of 3 positions holding
#   3 pairwise-distinct values can be sorted into exactly one valid triplet.
#   so we only need to count how many ways to pick one element from each of
#   3 distinct value groups.
#   walk the groups once, keeping a = #elements already seen (the "left" pool)
#   and c = n - a - b = #elements not yet seen (the "right" pool):
#       ans += a * b * c
#   NOTE : this counts every unordered choice of 3 groups exactly once,
#          because b is the middle group in the enumeration order.
#
"""

DP def
    the index order i < j < k is IRRELEVANT: any set of 3 positions holding
    3 pairwise-distinct values sorts into exactly one valid triplet. so just
    count ways to pick one element from each of 3 DISTINCT value groups.

    walking the groups once, keep

        a = # elements already seen          (the "left" pool)
        b = size of the current group        (the "middle")
        c = n - a - b                        (the "right" pool)

DP eq

     res += a * b * c

     a   += b


    -> e.g. every unordered choice of 3 groups is counted EXACTLY ONCE,
              because b is pinned as the MIDDLE group in the enumeration
              order

     ans = res

"""
# time = O(n), space = O(n)
from collections import Counter
class Solution(object):
    def unequalTriplets(self, nums):
        cnt = Counter(nums)
        n = len(nums)
        res = 0
        a = 0
        for b in cnt.values():
            c = n - a - b
            res += a * b * c
            a += b
        return res
