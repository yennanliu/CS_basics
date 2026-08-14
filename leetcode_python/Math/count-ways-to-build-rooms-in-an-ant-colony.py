"""

1916. Count Ways to Build Rooms in an Ant Colony
Hard

You are an ant tasked with adding n new rooms numbered 0 to n-1 to your colony. You are given the expansion plan as a 0-indexed integer array of length n, prevRoom, where prevRoom[i] indicates that you must build room prevRoom[i] before building room i, and these two rooms must be connected directly. Room 0 is already built, so prevRoom[0] = -1. The expansion plan is given such that once all the rooms are built, every room will be reachable from room 0.

You can only build one room at a time, and you can travel freely between rooms you have already built only if they are connected. You can choose to build any room as long as its previous room is already built.

Return the number of different orders you can build all the rooms in. Since the answer may be large, return it modulo 10^9 + 7.


Example 1:

Input: prevRoom = [-1,0,1]
Output: 1
Explanation: There is only one way to build the additional rooms: 0 -> 1 -> 2

Example 2:

Input: prevRoom = [-1,0,0,1,2]
Output: 6
Explanation: The 6 ways are:
0 -> 1 -> 3 -> 2 -> 4
0 -> 2 -> 4 -> 1 -> 3
0 -> 1 -> 2 -> 3 -> 4
0 -> 1 -> 2 -> 4 -> 3
0 -> 2 -> 1 -> 3 -> 4
0 -> 2 -> 1 -> 4 -> 3


Constraints:

n == prevRoom.length
2 <= n <= 10^5
prevRoom[0] == -1
0 <= prevRoom[i] < n for all 1 <= i < n
Every room is reachable from room 0.

"""

# V0
# IDEA : HOOK LENGTH FORMULA FOR TREES (count linear extensions of a rooted tree)
#
#   the build order is exactly a topological order of the tree rooted at 0.
#   the number of such orders is
#
#         n!  /  PRODUCT over every node v of size(subtree(v))
#
#   (each node must come before all of its subtree; the probability a random
#    permutation puts v first inside its own subtree is 1/size(v), and those
#    events are independent over the tree).
#
#   NOTE : mod is prime -> divide with pow(x, MOD-2, MOD).
#   NOTE : n can be 10^5, so build the subtree sizes ITERATIVELY (BFS order
#          reversed) instead of recursing.
#
# time = O(n log MOD), space = O(n)
class Solution(object):
    def waysToBuildRooms(self, prevRoom):
        MOD = 10 ** 9 + 7
        n = len(prevRoom)

        children = [[] for _ in range(n)]
        for i in range(1, n):
            children[prevRoom[i]].append(i)

        # BFS from the root -> parents always appear before their children
        order = [0]
        head = 0
        while head < len(order):
            u = order[head]
            head += 1
            for v in children[u]:
                order.append(v)

        size = [1] * n
        for i in range(len(order) - 1, 0, -1):
            u = order[i]
            size[prevRoom[u]] += size[u]

        res = 1
        for i in range(2, n + 1):   # n!
            res = res * i % MOD
        denom = 1
        for s in size:
            denom = denom * s % MOD
        return res * pow(denom, MOD - 2, MOD) % MOD
