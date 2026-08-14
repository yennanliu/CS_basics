"""

1488. Avoid Flood in The City
Medium

Your country has 10^9 lakes. Initially, all the lakes are empty, but when it rains over the nth lake, the nth lake becomes full of water. If it rains over a lake that is full of water, there will be a flood. Your goal is to avoid floods in any lake.

Given an integer array rains where:

rains[i] > 0 means there will be rains over the rains[i] lake.
rains[i] == 0 means there are no rains this day and you must choose one lake this day and dry it.

Return an array ans where:

ans.length == rains.length
ans[i] == -1 if rains[i] > 0.
ans[i] is the lake you choose to dry in the ith day if rains[i] == 0.

If there are multiple valid answers return any of them. If it is impossible to avoid flood return an empty array.

Notice that if you chose to dry a full lake, it becomes empty, but if you chose to dry an empty lake, nothing changes.


Example 1:

Input: rains = [1,2,3,4]
Output: [-1,-1,-1,-1]
Explanation: After the first day full lakes are [1]
After the second day full lakes are [1,2]
After the third day full lakes are [1,2,3]
After the fourth day full lakes are [1,2,3,4]
There's no day to dry any lake and there is no flood in any lake.

Example 2:

Input: rains = [1,2,0,0,2,1]
Output: [-1,-1,2,1,-1,-1]
Explanation: After the first day full lakes are [1]
After the second day full lakes are [1,2]
After the third day, we dry lake 2. Full lakes are [1]
After the fourth day, we dry lake 1. There is no full lakes.
After the fifth day, full lakes are [2].
After the sixth day, full lakes are [1,2].
It is easy that this scenario is flood-free. [-1,-1,1,2,-1,-1] is another acceptable scenario.

Example 3:

Input: rains = [1,2,0,1,2]
Output: []
Explanation: After the second day, full lakes are [1,2]. We have to dry one lake in the third day.
After that, it will rain over lakes [1,2]. It's easy to prove that no matter which lake you choose to dry in the 3rd day, the other one will flood.


Constraints:

1 <= rains.length <= 10^5
0 <= rains[i] <= 10^9

"""

# V0
# IDEA : GREEDY + MIN-HEAP KEYED ON THE NEXT RAIN (dry the most urgent lake)
#
#   first pass: nxt[i] = the next day the same lake rains again (or None).
#   second pass: keep a heap of full lakes ordered by that next rain day.
#     - dry day -> pop the lake whose deadline is nearest and empty it;
#       an exchange argument shows postponing the most urgent lake can
#       never help.
#     - rain day -> if the lake is already full, no dry day was available
#       in time, so a flood is unavoidable -> return [].
#   NOTE : a dry day with nothing full still must name a lake - any lake
#          works, so output 1.
#
# time = O(n log n), space = O(n)
import heapq
class Solution(object):
    def avoidFlood(self, rains):
        n = len(rains)

        # next occurrence of the same lake
        nxt = [-1] * n
        last = {}
        for i in range(n - 1, -1, -1):
            v = rains[i]
            if v > 0:
                if v in last:
                    nxt[i] = last[v]
                last[v] = i

        res = [-1] * n
        full = set()
        heap = []            # (next rain day, lake)

        for i in range(n):
            v = rains[i]
            if v > 0:
                if v in full:
                    return []            # already full -> flood
                full.add(v)
                if nxt[i] != -1:
                    heapq.heappush(heap, (nxt[i], v))
            else:
                if heap:
                    _, lake = heapq.heappop(heap)
                    full.discard(lake)
                    res[i] = lake
                else:
                    res[i] = 1           # nothing urgent, dry anything

        return res
