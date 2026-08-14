"""

1654. Minimum Jumps to Reach Home
Medium

A certain bug's home is on the x-axis at position x. Help them get there from position 0.

The bug jumps according to the following rules:

- It can jump exactly a positions forward (to the right).
- It can jump exactly b positions backward (to the left).
- It cannot jump backward twice in a row.
- It cannot jump to any forbidden positions.

The bug may jump forward beyond its home, but it cannot jump to positions numbered with negative integers.

Given an array of integers forbidden, where forbidden[i] means that the bug cannot jump to the position forbidden[i], and integers a, b, and x, return the minimum number of jumps needed for the bug to reach its home. If there is no possible sequence of jumps that lands the bug on position x, return -1.


Example 1:

Input: forbidden = [14,4,18,1,15], a = 3, b = 15, x = 9
Output: 3
Explanation: 3 jumps forward (0 -> 3 -> 6 -> 9) will get the bug home.

Example 2:

Input: forbidden = [8,3,16,6,12,20], a = 15, b = 13, x = 11
Output: -1

Example 3:

Input: forbidden = [1,6,2,14,5,17,4], a = 16, b = 9, x = 7
Output: 2
Explanation: One jump forward (0 -> 16) then one jump backward (16 -> 7) will get the bug home.


Constraints:

1 <= forbidden.length <= 1000
1 <= a, b, forbidden[i] <= 2000
0 <= x <= 2000
All the elements in forbidden are distinct.
Position x is not forbidden.

"""

# V0
# IDEA : BFS over state (position, came-here-by-backward-jump?)
#
#   shortest #jumps -> BFS. But position alone is NOT a state: whether we
#   may jump back depends on the previous move, so the state is
#     (pos, back) with back = 1 if the last jump was backward.
#   from (pos, 0) we may go forward or backward; from (pos, 1) only forward.
#
#   NOTE : the search space must be bounded. Any useful detour stays below
#          max(x, max(forbidden)) + a + b -- past that point going further
#          right can never pay off, since a single b-step back is all we
#          get. 6000 covers the constraints (all values <= 2000).
#
# time = O(LIMIT), space = O(LIMIT)
from collections import deque
class Solution(object):
    def minimumJumps(self, forbidden, a, b, x):
        blocked = set(forbidden)
        limit = max([x] + forbidden) + a + b

        q = deque([(0, 0, 0)])          # (pos, came_by_back, steps)
        seen = set([(0, 0)])
        while q:
            pos, back, steps = q.popleft()
            if pos == x:
                return steps

            nxt = [(pos + a, 0)]
            if not back:
                nxt.append((pos - b, 1))

            for np, nb in nxt:
                if np < 0 or np > limit:
                    continue
                if np in blocked or (np, nb) in seen:
                    continue
                seen.add((np, nb))
                q.append((np, nb, steps + 1))

        return -1
