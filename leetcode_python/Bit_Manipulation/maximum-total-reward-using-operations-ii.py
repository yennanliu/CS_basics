"""

3181. Maximum Total Reward Using Operations II
Hard

You are given an integer array rewardValues of length n, representing the values of rewards.

Initially, your total reward x is 0, and all indices are unmarked. You are allowed to perform the following operation any number of times:

Choose an unmarked index i from the range [0, n - 1].
If rewardValues[i] is greater than your current total reward x, then add rewardValues[i] to x (i.e., x = x + rewardValues[i]), and mark the index i.

Return an integer denoting the maximum total reward you can collect by performing the operations optimally.


Example 1:

Input: rewardValues = [1,1,3,3]
Output: 4
Explanation:
During the operations, we can choose to mark indices 0 and 2 in order, and the total reward will be 4, which is the maximum.

Example 2:

Input: rewardValues = [1,6,4,3,2]
Output: 11
Explanation:
Mark the indices 0, 2, 3, and 5 in order. The total reward will then be 11, which is the maximum.


Constraints:

1 <= rewardValues.length <= 5 * 10^4
1 <= rewardValues[i] <= 5 * 10^4

"""

# V0
# IDEA : SAME REACHABILITY DP, BUT THE SET LIVES IN THE BITS OF ONE BIG INT
#
#   LC 3180's boolean array costs O(n * max) cell writes; here that is
#   5*10^4 * 10^5 = 5 billion, far too many in python.
#
#   the fix is to keep the reachable totals as a BITSET inside a single
#   integer — bit t set means total t is reachable — so one value's whole
#   transition is three word-parallel operations :
#
#       low  = reach & ((1 << v) - 1)     # totals strictly below v
#       reach |= low << v                 # each of them can take v
#
#   python's arbitrary-precision ints run those in C over machine words, so
#   each distinct value costs O(max / 64) instead of O(max).
#
#   the answer is the highest set bit, i.e. bit_length() - 1.
#
# time = O(n * max / word size), space = O(max / word size)
class Solution(object):
    def maxTotalReward(self, rewardValues):
        reach = 1                                # only total 0 is reachable
        for v in sorted(set(rewardValues)):
            low = reach & ((1 << v) - 1)         # totals below v may take it
            reach |= low << v
        return reach.bit_length() - 1
