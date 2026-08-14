"""

2557. Maximum Number of Integers to Choose From a Range II
Medium

You are given an integer array banned and two integers n and maxSum. You are choosing some number of integers following the below rules:

- The chosen integers have to be in the range [1, n].
- Each integer can be chosen at most once.
- The chosen integers should not be in the array banned.
- The sum of the chosen integers should not exceed maxSum.

Return the maximum number of integers you can choose following the mentioned rules.


Example 1:

Input: banned = [1,4,6], n = 6, maxSum = 4
Output: 1
Explanation: You can choose the integer 3.
3 is in the range [1, 6], and do not appear in banned. The sum of the chosen integers is 3, which does not exceed maxSum.

Example 2:

Input: banned = [4,3,5,6], n = 7, maxSum = 18
Output: 3
Explanation: You can choose the integers 1, 2, and 7.
All these integers are in the range [1, 7], all do not appear in banned, and their sum is 10, which does not exceed maxSum.


Constraints:

1 <= banned.length <= 10^5
1 <= banned[i] <= n <= 10^9
1 <= maxSum <= 10^15

"""

# V0
# IDEA : GREEDY (SMALLEST FIRST) + BINARY SEARCH ON EACH FREE GAP
#
#   to maximize the COUNT we always want the smallest available integers,
#   so we walk the allowed values left -> right and take as many as the
#   remaining budget allows.
#
#   n can be 10^9, so we cannot enumerate [1, n]. instead we sort the banned
#   values (plus sentinels 0 and n+1) and treat the free values as the gaps
#   between consecutive banned values: gap (i, j) holds i+1 .. j-1.
#
#   inside a gap, taking the first t values costs
#       (i+1) + (i+2) + ... + (i+t) = t * (2*i + 1 + t) / 2
#   which grows monotonically in t -> binary search the largest feasible t.
#
#   NOTE : duplicates in banned must be removed, otherwise a gap of "width 0"
#          would still be scanned (harmless) and, worse, the sentinels could
#          collide with a real banned value.
#   NOTE : once the budget is exhausted we can stop — every later gap only
#          contains larger integers.
#
# time = O(m log m + m log n), space = O(m)   (m = len(banned))
class Solution(object):
    def maxCount(self, banned, n, maxSum):
        ban = sorted(set(banned) | {0, n + 1})
        ans = 0
        for idx in range(1, len(ban)):
            i, j = ban[idx - 1], ban[idx]
            # values i+1 .. j-1 are free  ->  at most j - i - 1 of them
            lo, hi = 0, j - i - 1
            while lo < hi:
                mid = (lo + hi + 1) // 2
                if mid * (2 * i + 1 + mid) // 2 <= maxSum:
                    lo = mid
                else:
                    hi = mid - 1
            ans += lo
            maxSum -= lo * (2 * i + 1 + lo) // 2
            if maxSum <= 0:
                break
        return ans
