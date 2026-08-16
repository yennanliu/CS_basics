"""

3594. Minimum Time to Transport All Individuals
Hard

You are given n individuals at a base camp who need to cross a river to reach a
destination using a single boat. The boat can carry at most k people at a time.
The trip is affected by environmental conditions that vary cyclically over m
stages.

Each stage j has a speed multiplier mul[j]:

If mul[j] > 1, the trip slows down.
If mul[j] < 1, the trip speeds up.

Each individual i has a rowing strength represented by time[i], the time (in
minutes) it takes them to cross alone in neutral conditions.

Rules:

A group g departing at stage j takes time equal to the maximum time[i] among
its members, multiplied by mul[j] minutes to reach the destination.
After the group crosses the river in time d, the stage advances by
floor(d) % m steps.
If individuals are left behind, one person must return with the boat. Let r be
the index of the returning person, the return takes time[r] x mul[current_stage],
defined as return_time, and the stage advances by floor(return_time) % m steps.

Return the minimum total time required to transport all individuals. If it is
not possible to transport all individuals to the destination, return -1.


Example 1:

Input: n = 1, k = 1, m = 2, time = [5], mul = [1.0,1.3]
Output: 5.00000
Explanation:
Individual 0 departs from stage 0, so crossing time = 5 x 1.00 = 5.00 minutes.
All team members are now at the destination. Thus, the total time taken is
5.00 minutes.

Example 2:

Input: n = 3, k = 2, m = 3, time = [2,5,8], mul = [1.0,1.5,0.75]
Output: 14.50000
Explanation:
The optimal strategy is:
Send individuals 0 and 2 from the base camp to the destination from stage 0.
The crossing time is max(2, 8) x mul[0] = 8 x 1.00 = 8.00 minutes. The stage
advances by floor(8.00) % 3 = 2, so the next stage is (0 + 2) % 3 = 2.
Individual 0 returns alone from the destination to the base camp from stage 2.
The return time is 2 x mul[2] = 2 x 0.75 = 1.50 minutes. The stage advances by
floor(1.50) % 3 = 1, so the next stage is (2 + 1) % 3 = 0.
Send individuals 0 and 1 from the base camp to the destination from stage 0.
The crossing time is max(2, 5) x mul[0] = 5 x 1.00 = 5.00 minutes. The stage
advances by floor(5.00) % 3 = 2, so the final stage is (0 + 2) % 3 = 2.
All team members are now at the destination. The total time taken is
8.00 + 1.50 + 5.00 = 14.50 minutes.

Example 3:

Input: n = 2, k = 1, m = 2, time = [10,10], mul = [2.0,2.0]
Output: -1.00000
Explanation:
Since the boat can only carry one person at a time, it is impossible to
transport both individuals as one must always return. Thus, the answer is
-1.00.


Constraints:

1 <= n == time.length <= 12
1 <= k <= 5
1 <= m <= 5
1 <= time[i] <= 100
m == mul.length
0.5 <= mul[i] <= 2.0

"""

# V0
# IDEA : DIJKSTRA OVER (CAMP BITMASK, STAGE, BOAT SIDE)
#
#   everything the future depends on is captured by three things: who is still
#   at the base camp, which stage the cycle is in, and which bank the boat is
#   on. names of the people already across never matter beyond "they are
#   available to row back", and that is exactly the complement of the camp
#   mask. so the state graph has only 2^n * m * 2 vertices — at most 40960.
#
#   modelling the crossing and the return as two separate edges rather than one
#   combined move is what keeps the graph small. a combined move would have to
#   enumerate (group, returner) pairs together, multiplying the two choices;
#   split apart, the crossing edges cost sum(2^|S|) over the reachable masks and
#   the return edges cost only n per state.
#
#   every edge has strictly positive weight, since time[i] >= 1 and mul >= 0.5,
#   so dijkstra is valid and the first time the goal is popped the distance is
#   final. the goal is an empty camp with the boat on the far side, which is
#   precisely the moment no return is owed. if the goal is never popped the
#   configuration is unreachable and the answer is -1 — this is what happens for
#   k == 1 and n >= 2, where every crossing is undone by the forced return.
#
#   only the maximum time in a group is charged, so a group is summarised by
#   that maximum; it is precomputed for all 2^n masks by peeling off the lowest
#   set bit.
#
# time = O(3^n * m * log(2^n * m)), space = O(3^n)
import heapq


class Solution(object):
    def minTime(self, n, k, m, time, mul):
        size = 1 << n
        full = size - 1

        popcount = [0] * size
        gmax = [0] * size
        for mask in range(1, size):
            low = mask & -mask
            rest = mask ^ low
            i = low.bit_length() - 1
            popcount[mask] = popcount[rest] + 1
            gmax[mask] = time[i] if time[i] > gmax[rest] else gmax[rest]

        # boarding parties of a camp mask, cached because each mask is popped
        # once per stage
        parties = [None] * size

        def groups(mask):
            gs = parties[mask]
            if gs is None:
                gs = []
                sub = mask
                while sub:
                    if popcount[sub] <= k:
                        gs.append(sub)
                    sub = (sub - 1) & mask
                parties[mask] = gs
            return gs

        # state id = (mask * m + stage) * 2 + side, side 0 = boat at the camp
        INF = float('inf')
        best = [INF] * (size * m * 2)
        start = (full * m) * 2
        best[start] = 0.0
        pq = [(0.0, start)]

        while pq:
            d, s = heapq.heappop(pq)
            if d > best[s]:
                continue
            side = s & 1
            stage = (s >> 1) % m
            mask = (s >> 1) // m

            # an empty camp with the boat across is the goal, and dijkstra has
            # already settled its distance
            if side == 1 and mask == 0:
                return d

            if side == 0:
                for grp in groups(mask):
                    cost = gmax[grp] * mul[stage]
                    nxt_stage = (stage + int(cost)) % m
                    nxt_mask = mask ^ grp
                    t = (((nxt_mask * m) + nxt_stage) << 1) | 1
                    if d + cost < best[t]:
                        best[t] = d + cost
                        heapq.heappush(pq, (d + cost, t))
            else:
                across = full ^ mask
                while across:
                    low = across & -across
                    across ^= low
                    r = low.bit_length() - 1
                    cost = time[r] * mul[stage]
                    nxt_stage = (stage + int(cost)) % m
                    t = ((mask | low) * m + nxt_stage) << 1
                    if d + cost < best[t]:
                        best[t] = d + cost
                        heapq.heappush(pq, (d + cost, t))
        return -1.0
