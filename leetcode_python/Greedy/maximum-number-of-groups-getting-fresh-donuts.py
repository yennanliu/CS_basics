"""

1815. Maximum Number of Groups Getting Fresh Donuts
Hard

There is a donuts shop that bakes donuts in batches of batchSize. They have a rule where they must serve all of the donuts of a batch before serving any donuts of the next batch. You are given an integer batchSize and an integer array groups, where groups[i] denotes that there is a group of groups[i] customers that will visit the shop. Each customer will get exactly one donut.

When a group visits the shop, all customers of the group must be served before serving any of the following groups. A group will be happy if they all get fresh donuts. That is, the first customer of the group does not receive a donut that was left over from the previous group.

You can freely rearrange the ordering of the groups. Return the maximum possible number of happy groups after rearranging the groups.


Example 1:

Input: batchSize = 3, groups = [1,2,3,4,5,6]
Output: 4
Explanation: You can arrange the groups as [6,2,4,5,1,3]. Then the 1st, 2nd, 4th, and 6th groups will be happy.

Example 2:

Input: batchSize = 4, groups = [1,3,2,5,2,2,1,6]
Output: 4


Constraints:

1 <= batchSize <= 9
1 <= groups.length <= 30
1 <= groups[i] <= 10^9

"""

# V0
# IDEA : STATE-COMPRESSION DP OVER REMAINDER COUNTS (order matters only via
#        the running leftover)
#
#   only groups[i] % batchSize matters. two facts:
#     - a group with remainder 0 never changes the leftover, and it is happy
#       exactly when it is served while leftover == 0 -> put them all first,
#       they are ALWAYS happy -> add them to the answer and drop them.
#     - for the rest, the state is just "how many groups of each remainder
#       1..batchSize-1 are still unserved" plus the current leftover `mod`.
#
#   dfs(state, mod) = max happy groups obtainable from `state`.
#     serving a group of remainder i : it is happy iff mod == 0, and the new
#     leftover becomes (mod + i) % batchSize.
#     dfs(state, mod) = max over available i of dfs(state - one_i, (mod+i)%b)
#                       + (1 if mod == 0 else 0)
#
#   the counts (<= 30) are packed into 5-bit slots, one slot per remainder, so
#   the whole multiset is a single integer key.
#
#   NOTE : greedy pairing (i + j == batchSize) is NOT optimal -- the search is
#          needed, but the count-based state keeps it small.
#
# time = O(b * prod(cnt_i + 1) * b), b = batchSize
# space = O(b * prod(cnt_i + 1))
class Solution(object):
    def maxHappyGroups(self, batchSize, groups):
        memo = {}

        def dfs(state, mod):
            if state == 0:
                return 0
            key = (state, mod)
            if key in memo:
                return memo[key]
            gain = 1 if mod == 0 else 0
            best = 0
            for i in range(1, batchSize):
                if (state >> (i * 5)) & 31:
                    got = dfs(state - (1 << (i * 5)), (mod + i) % batchSize)
                    if got + gain > best:
                        best = got + gain
            memo[key] = best
            return best

        state = 0
        res = 0
        for v in groups:
            i = v % batchSize
            if i == 0:
                res += 1
            else:
                state += 1 << (i * 5)
        return res + dfs(state, 0)
