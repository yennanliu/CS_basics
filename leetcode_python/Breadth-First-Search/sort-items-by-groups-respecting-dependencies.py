"""

1203. Sort Items by Groups Respecting Dependencies
Hard

There are n items each belonging to zero or one of m groups where group[i] is
the group that the i-th item belongs to and it's equal to -1 if the i-th item
belongs to no group. The items and the groups are zero indexed. A group can have
no item belonging to it.

Return a sorted list of the items such that:

The items that belong to the same group are next to each other in the sorted
list.
There are some relations between these items where beforeItems[i] is a list
containing all the items that should come before the i-th item in the sorted
array (to the left of the i-th item).

Return any solution if there is more than one solution and return an empty list
if there is no solution.


Example 1:

Input: n = 8, m = 2, group = [-1,-1,1,0,0,1,0,-1],
       beforeItems = [[],[6],[5],[6],[3,6],[],[],[]]
Output: [6,3,4,1,5,2,0,7]

Example 2:

Input: n = 8, m = 2, group = [-1,-1,1,0,0,1,0,-1],
       beforeItems = [[],[6],[5],[6],[3],[],[4],[]]
Output: []
Explanation: This is the same as example 1 except that 4 needs to be before 6
in the sorted list.


Constraints:

1 <= m <= n <= 3 * 10^4
group.length == beforeItems.length == n
-1 <= group[i] <= m - 1
0 <= beforeItems[i].length <= n - 1
0 <= beforeItems[i][j] <= n - 1
i != beforeItems[i][j]
beforeItems[i] does not contain duplicates elements.

"""

# V0
# IDEA: TWO-LEVEL TOPOLOGICAL SORT (BFS / Kahn)
"""

 STEP 1) give every "group == -1" item its OWN brand new group
         -> now every item belongs to exactly one group,
            and the "same group items are adjacent" rule is free for them

 STEP 2) build TWO graphs from beforeItems:
         - item graph  : edge j -> i   when group[i] == group[j]  (INSIDE a group)
         - group graph : edge g[j] -> g[i] when group[i] != group[j] (BETWEEN groups)

 STEP 3) topo sort the GROUP graph  -> order of the blocks
         topo sort each group's ITEMS -> order inside a block

 -> a cycle in either graph means no valid ordering => return []
"""
# time = O(n + m + E), E = total number of beforeItems edges
# space = O(n + m + E)
from collections import defaultdict, deque
class Solution(object):
    def sortItems(self, n, m, group, beforeItems):
        # STEP 1) every item gets a real group id
        group = list(group)
        for i in range(n):
            if group[i] == -1:
                group[i] = m
                m += 1

        # STEP 2) build both graphs
        item_graph = defaultdict(list)
        item_indeg = [0] * n
        group_graph = defaultdict(list)
        group_indeg = [0] * m

        for i in range(n):
            gi = group[i]
            for j in beforeItems[i]:
                if group[j] == gi:
                    item_graph[j].append(i)
                    item_indeg[i] += 1
                else:
                    group_graph[group[j]].append(gi)
                    group_indeg[gi] += 1

        def topo(nodes, graph, indeg):
            """Kahn BFS. Returns [] if a cycle blocks a full ordering."""
            q = deque(x for x in nodes if indeg[x] == 0)
            order = []
            while q:
                cur = q.popleft()
                order.append(cur)
                for nxt in graph[cur]:
                    indeg[nxt] -= 1
                    if indeg[nxt] == 0:
                        q.append(nxt)
            return order if len(order) == len(nodes) else []

        # STEP 3a) order the groups
        group_order = topo(list(range(m)), group_graph, group_indeg)
        if not group_order:
            return []

        group_items = defaultdict(list)
        for i in range(n):
            group_items[group[i]].append(i)

        # STEP 3b) order the items inside each group
        res = []
        for g in group_order:
            items = group_items[g]
            if not items:
                continue
            item_order = topo(items, item_graph, item_indeg)
            if not item_order:
                return []
            res.extend(item_order)

        return res
