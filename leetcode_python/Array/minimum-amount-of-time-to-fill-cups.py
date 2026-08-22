"""

2335. Minimum Amount of Time to Fill Cups
Easy

You have a water dispenser that can dispense cold, warm, and hot water. Every second, you can either fill up 2 cups with different types of water, or 1 cup of any type of water.

You are given a 0-indexed integer array amount of length 3 where amount[0], amount[1], and amount[2] denote the number of cold, warm, and hot water cups you need to fill respectively. Return the minimum number of seconds needed to fill up all the cups.


Example 1:

Input: amount = [1,4,2]
Output: 4
Explanation: One way to fill up the cups is:
Second 1: Fill up a cold cup and a warm cup.
Second 2: Fill up a warm cup and a hot cup.
Second 3: Fill up a warm cup and a hot cup.
Second 4: Fill up a warm cup.
It can be proven that 4 is the minimum number of seconds needed.

Example 2:

Input: amount = [5,4,4]
Output: 7
Explanation: One way to fill up the cups is:
Second 1: Fill up a cold cup, and a hot cup.
Second 2: Fill up a cold cup, and a warm cup.
Second 3: Fill up a cold cup, and a warm cup.
Second 4: Fill up a warm cup, and a hot cup.
Second 5: Fill up a cold cup, and a hot cup.
Second 6: Fill up a cold cup, and a hot cup.
Second 7: Fill up a warm cup.

Example 3:

Input: amount = [5,0,0]
Output: 5
Explanation: Every second, we fill up a cold cup.


Constraints:

amount.length == 3
0 <= amount[i] <= 100

"""

# V0
# IDEA : TWO LOWER BOUNDS, AND THE LARGER ONE IS ALWAYS ACHIEVABLE
#
#   bound 1 : the biggest pile must be poured one cup per second at best, so
#             at least max(amount) seconds are needed.
#   bound 2 : each second fills at most 2 cups, so at least
#             ceil(total / 2) seconds.
#
#   the answer is exactly the larger of the two. when one pile dominates the
#   other two combined, bound 1 binds; otherwise the piles can be paired off
#   evenly and bound 2 binds.
#
# time = O(1), space = O(1)
class Solution(object):
    def fillCups(self, amount):
        total = sum(amount)
        return max(max(amount), (total + 1) // 2)


# V0-1
# IDEA : GREEDY SIMULATION WITH A MAX HEAP
#
#   at every second, pour from the two currently LARGEST piles (they are the
#   ones at risk of being left alone at the end); if only one pile is left,
#   pour a single cup. python's heapq is a min heap, so store negated counts.
#
#   this is the constructive proof of the O(1) formula : it actually builds a
#   schedule, second by second, instead of reasoning about lower bounds.
#
# time = O(S log 3) = O(S), S = sum(amount)
# space = O(1)
import heapq
class Solution(object):
    def fillCups(self, amount):
        h = [-a for a in amount if a > 0]
        heapq.heapify(h)
        sec = 0
        while h:
            a = heapq.heappop(h) + 1     # negated -> += 1 means "one cup out"
            if h:
                b = heapq.heappop(h) + 1
                if b:
                    heapq.heappush(h, b)
            if a:
                heapq.heappush(h, a)
            sec += 1
        return sec


# V0-2
# IDEA : TOP-DOWN DP (MEMOIZED SEARCH OVER THE STATE SPACE)
#
#   state = the three remaining counts. from a state we may fill any single
#   pile, or any PAIR of distinct piles, each costing 1 second :
#
#       dp(a, b, c) = 1 + min(dp over all legal moves)
#
#   sorting the triple before memoizing collapses permutations of the same
#   state, so the table stays small. this makes no greedy assumption at all —
#   it searches every schedule — which is why it is the right cross-check for
#   the O(1) formula in V0.
#
# time = O(k^3) states, k = max cup count (<= 100)
# space = O(k^3)
from functools import lru_cache
class Solution(object):
    def fillCups(self, amount):

        @lru_cache(None)
        def dp(a, b, c):
            if a == 0 and b == 0 and c == 0:
                return 0
            best = float('inf')
            cur = (a, b, c)
            # fill one cup
            for i in range(3):
                if cur[i]:
                    nxt = list(cur)
                    nxt[i] -= 1
                    best = min(best, 1 + dp(*sorted(nxt)))
            # fill two cups of different types
            for i in range(3):
                for j in range(i + 1, 3):
                    if cur[i] and cur[j]:
                        nxt = list(cur)
                        nxt[i] -= 1
                        nxt[j] -= 1
                        best = min(best, 1 + dp(*sorted(nxt)))
            return best

        return dp(*sorted(amount))
