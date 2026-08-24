"""

3351. Sum of Good Subsequences
Hard

You are given an integer array nums. A good subsequence is defined as a subsequence of nums where the absolute difference between any two consecutive elements in the subsequence is exactly 1.

Return the sum of all possible good subsequences of nums.

Since the answer may be very large, return it modulo 10^9 + 7.

Note that a subsequence of size 1 is considered good by definition.


Example 1:

Input: nums = [1,2,1]
Output: 14
Explanation:
Good subsequences are: [1], [2], [1], [1,2], [2,1], [1,2,1].
The sum of elements in these subsequences is 14.

Example 2:

Input: nums = [3,4,5]
Output: 40
Explanation:
Good subsequences are: [3], [4], [5], [3,4], [4,5], [3,4,5].
The sum of elements in these subsequences is 40.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^5

"""

# V0
# IDEA : PER-VALUE TALLIES OF "HOW MANY SUBSEQUENCES END HERE" AND "WHAT THEY SUM TO"
#
#   a good subsequence ending with value v can only be extended by v-1 or
#   v+1, so keep two dictionaries keyed by the LAST value :
#
#       cnt[v]   = number of good subsequences ending in v
#       total[v] = the sum of all their elements
#
#   when the element x arrives, the subsequences it can close are the ones
#   ending in x-1 or x+1, plus the singleton :
#
#       new_cnt   = cnt[x-1] + cnt[x+1] + 1
#       new_total = total[x-1] + total[x+1] + x * new_cnt
#
#   (the x * new_cnt term adds x once to every one of them). folding those
#   into cnt[x] and total[x] keeps the tallies live for later elements, and
#   summing total at the end covers every subsequence exactly once — by its
#   final element.
#
"""

DP def
    a good subsequence ending in value v can only be extended by v-1 or v+1,
    so key the tallies by the LAST value:

    cnt[v]  : number of good subsequences ENDING in v

    total[v]: the sum of all their elements

DP eq

     when the element x arrives, the subsequences it can close are those
     ending in x-1 or x+1, plus the singleton:

        new_cnt   = cnt[x-1] + cnt[x+1] + 1

        new_total = total[x-1] + total[x+1] + x * new_cnt

     then fold into cnt[x] += new_cnt, total[x] += new_total


    -> e.g. the `x * new_cnt` term adds x ONCE to every one of those
              subsequences

     folding into the tallies keeps them live for later elements, and each
     subsequence is counted exactly once - by its FINAL element

     ans = sum(total.values()) % (10^9 + 7)

"""
# time = O(n), space = O(n)
class Solution(object):
    def sumOfGoodSubsequences(self, nums):
        MOD = 10 ** 9 + 7
        cnt = {}
        total = {}

        for x in nums:
            new_cnt = (cnt.get(x - 1, 0) + cnt.get(x + 1, 0) + 1) % MOD
            new_total = (total.get(x - 1, 0) + total.get(x + 1, 0)
                         + x * new_cnt) % MOD
            cnt[x] = (cnt.get(x, 0) + new_cnt) % MOD
            total[x] = (total.get(x, 0) + new_total) % MOD

        return sum(total.values()) % MOD
