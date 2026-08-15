"""

3180. Maximum Total Reward Using Operations I
Medium

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

1 <= rewardValues.length <= 2000
1 <= rewardValues[i] <= 2000

"""

# V0
# IDEA : REACHABLE-TOTALS DP, WITH THE VALUES TAKEN IN INCREASING ORDER
#
#   a reward v may only be taken while the current total is strictly below v,
#   so the totals grow and the values must be consumed in increasing order —
#   sorting loses nothing, and duplicates are useless (the second copy can
#   never beat the total the first one produced).
#
#   then track the SET of reachable totals as a boolean array : from a total
#   t < v the value v opens t + v. sweeping t downwards keeps the additions
#   from cascading within one value.
#
#   totals stay under 2 * max(v), which bounds the array.
#
# time = O(n * max value), space = O(max value)
class Solution(object):
    def maxTotalReward(self, rewardValues):
        vals = sorted(set(rewardValues))
        limit = 2 * vals[-1]
        reach = [False] * limit
        reach[0] = True

        for v in vals:
            for t in range(v - 1, -1, -1):      # only totals below v may take it
                if reach[t] and t + v < limit:
                    reach[t + v] = True

        return max(t for t in range(limit) if reach[t])
