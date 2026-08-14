"""

1711. Count Good Meals
Medium

A good meal is a meal that contains exactly two different food items with a sum of deliciousness equal to a power of two.

You can pick any two different foods to make a good meal.

Given an array of integers deliciousness where deliciousness[i] is the deliciousness of the ith item of food, return the number of different good meals you can make from this list modulo 10^9 + 7.

Note that items with different indices are considered different even if they have the same deliciousness value.


Example 1:

Input: deliciousness = [1,3,5,7,9]
Output: 4
Explanation: The good meals are (1,3), (1,7), (3,5) and, (7,9).
Their respective sums are 4, 8, 8, and 16, all of which are powers of 2.

Example 2:

Input: deliciousness = [1,1,1,3,3,3,7]
Output: 15
Explanation: The good meals are (1,1) with 3 ways, (1,3) with 9 ways, and (1,7) with 3 ways.


Constraints:

1 <= deliciousness.length <= 10^5
0 <= deliciousness[i] <= 2^20

"""

# V0
# IDEA : HASH TABLE + ENUMERATE THE 22 POSSIBLE POWERS OF TWO ("two sum" style)
#
#   O(n^2) over all pairs is too slow. instead fix the TARGET sum:
#   since 0 <= d <= 2^20, any pair sum lies in [0, 2^21], so the only
#   candidate sums are s = 2^0, 2^1, ..., 2^21  -> just 22 of them.
#
#   sweep left to right keeping cnt = multiset of values seen SO FAR.
#   for the current d, every earlier value equal to (s - d) pairs with it.
#   counting only backwards means each unordered pair is counted once,
#   which also handles equal values (1,1) correctly with no /2 fixup.
#
#   NOTE : s starts at 1, not 0 - 2^0 = 1 is the smallest power of two.
#          (d = 0 pairs are still reachable via s = 1 .. 2^21.)
#
# time = O(n * 22), space = O(n)
from collections import defaultdict
class Solution(object):
    def countPairs(self, deliciousness):
        MOD = 10 ** 9 + 7

        cnt = defaultdict(int)
        res = 0
        for d in deliciousness:
            s = 1
            for _ in range(22):
                res += cnt[s - d]
                s <<= 1
            cnt[d] += 1

        return res % MOD
