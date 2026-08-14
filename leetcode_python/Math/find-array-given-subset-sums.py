"""

1982. Find Array Given Subset Sums
Hard

You are given an integer n representing the length of an unknown array that you are trying to recover. You are also given an array sums containing the values of all 2^n subset sums of the unknown array (in no particular order).

Return the array ans of length n representing the unknown array. If multiple answers exist, return any of them.

An array sub is a subset of an array arr if sub can be obtained from arr by deleting some (possibly zero or all) elements of arr. The sum of the elements in sub is one possible subset sum of arr. The sum of an empty array is considered to be 0.

Note: Test cases are generated such that there will always be at least one correct answer.


Example 1:

Input: n = 3, sums = [-3,-2,-1,0,0,1,2,3]
Output: [1,2,-3]
Explanation: [1,2,-3] is able to achieve the given subset sums:
- []: sum is 0
- [1]: sum is 1
- [2]: sum is 2
- [1,2]: sum is 3
- [-3]: sum is -3
- [1,-3]: sum is -2
- [2,-3]: sum is -1
- [1,2,-3]: sum is 0
Note that any permutation of [1,2,-3] and also any permutation of [-1,-2,3] will also be accepted.

Example 2:

Input: n = 2, sums = [0,0,0,0]
Output: [0,0]
Explanation: The only correct answer is [0,0].

Example 3:

Input: n = 4, sums = [0,0,5,5,4,-1,4,9,9,-1,4,3,4,8,3,8]
Output: [0,-1,4,5]
Explanation: [0,-1,4,5] is able to achieve the given subset sums.


Constraints:

1 <= n <= 15
sums.length == 2^n
-10^4 <= sums[i] <= 10^4

"""

# V0
# IDEA : DIVIDE AND CONQUER ON THE SORTED MULTISET (peel one element at a time)
#
#   the 2^k subset sums of k elements split into two equal halves :
#     A = sums of subsets WITHOUT the peeled element x
#     B = sums of subsets WITH it        (B = A + x elementwise)
#   so the multiset is a perfect pairing (s, s + d) with d = |x|.
#
#   d is recovered as (largest - second largest) : those two differ exactly by
#   the peeled element (the top sum with, and without, it).
#
#   which half survives? the one that still contains 0 - the empty subset must
#   remain a subset sum of the leftover elements.
#     - shifted half (s + d) holds 0  -> the element was -d, keep that half
#     - otherwise                     -> the element was +d, keep the low half
#
#   NOTE : pairing is done greedily on the SORTED list with a counter, so each
#          smallest unused s is matched to s + d.
#
# time = O(2^n * n), space = O(2^n)
from collections import Counter
class Solution(object):
    def recoverArray(self, n, sums):
        sums = sorted(sums)
        res = []
        for i in range(n, 0, -1):
            k = 1 << i
            cur = sums[:k]
            d = cur[-1] - cur[-2]          # |peeled element|

            cnt = Counter(cur)
            low, high = [], []
            sign = 1
            for s in cur:
                if cnt[s] <= 0:
                    continue               # already used as some (t + d)
                cnt[s] -= 1
                cnt[s + d] -= 1
                low.append(s)
                high.append(s + d)
                if s + d == 0:
                    sign = -1              # the shifted half keeps the 0
            res.append(sign * d)
            sums = high if sign == -1 else low
        return res
