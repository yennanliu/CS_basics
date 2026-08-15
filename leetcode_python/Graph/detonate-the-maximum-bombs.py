"""

2101. Detonate the Maximum Bombs
Medium

You are given a list of bombs. The range of a bomb is defined as the area where its effect can be felt. This area is in the shape of a circle with the center as the location of the bomb.

The bombs are represented by a 0-indexed 2D integer array bombs where bombs[i] = [xi, yi, ri]. xi and yi denote the X-coordinate and Y-coordinate of the location of the ith bomb, whereas ri denotes the radius of its range.

You may choose to detonate a single bomb. When a bomb is detonated, it will detonate all bombs that lie in its range. These bombs will further detonate the bombs that lie in their ranges.

Given the list of bombs, return the maximum number of bombs that can be detonated if you are allowed to detonate only one bomb.


Example 1:

Input: bombs = [[2,1,3],[6,1,4]]
Output: 2
Explanation:
The above figure shows the positions and ranges of the 2 bombs.
If we detonate the left bomb, the right bomb will not be affected.
But if we detonate the right bomb, both bombs will be detonated.
So the maximum bombs that can be detonated is max(1, 2) = 2.

Example 2:

Input: bombs = [[1,1,5],[10,10,5]]
Output: 1
Explanation:
Detonating either bomb will not detonate the other bomb, so the maximum number of bombs that can be detonated is 1.

Example 3:

Input: bombs = [[1,2,3],[2,3,1],[3,4,2],[4,5,3],[5,6,4]]
Output: 5
Explanation:
The best bomb to detonate is bomb 0 because:
- Bomb 0 detonates bombs 1 and 2. The red circle denotes the range of bomb 0.
- Bomb 2 detonates bomb 3. The blue circle denotes the range of bomb 2.
- Bomb 3 detonates bomb 4. The green circle denotes the range of bomb 3.
Thus all 5 bombs are detonated.


Constraints:

1 <= bombs.length <= 100
bombs[i].length == 3
1 <= xi, yi, ri <= 10^5

"""

# V0
# IDEA : DIRECTED REACHABILITY GRAPH + DFS FROM EVERY BOMB
#
#   bomb i triggers bomb j when j's CENTRE falls inside i's circle :
#       (xi-xj)^2 + (yi-yj)^2 <= ri^2
#   note this is one-way — a big bomb reaching a small one says nothing
#   about the reverse — so the graph is DIRECTED, not a union-find.
#
#   compare SQUARED distances so the check stays in integers (no sqrt, no
#   float rounding at the boundary).
#
#   with n <= 100 we can afford to build all n^2 edges and run a DFS from
#   each start, taking the largest reachable set.
#
# time = O(n^2) to build + O(n * (n + E)) = O(n^3) worst case, space = O(n^2)
class Solution(object):
    def maximumDetonation(self, bombs):
        n = len(bombs)
        adj = [[] for _ in range(n)]
        for i in range(n):
            xi, yi, ri = bombs[i]
            for j in range(n):
                if i == j:
                    continue
                xj, yj, _ = bombs[j]
                if (xi - xj) ** 2 + (yi - yj) ** 2 <= ri * ri:
                    adj[i].append(j)

        res = 0
        for start in range(n):
            seen = set([start])
            stack = [start]
            while stack:
                cur = stack.pop()
                for nxt in adj[cur]:
                    if nxt not in seen:
                        seen.add(nxt)
                        stack.append(nxt)
            res = max(res, len(seen))
        return res
