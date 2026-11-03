"""

752. Open the Lock
Medium

You have a lock in front of you with 4 circular wheels. Each wheel has 10 slots: '0', '1',
'2', '3', '4', '5', '6', '7', '8', '9'. The wheels can rotate freely and wrap around: for
example we can turn '9' to be '0', or '0' to be '9'. Each move consists of turning one wheel
one slot.

The lock initially starts at '0000', a string representing the state of the 4 wheels.

You are given a list of deadends dead ends, meaning if the lock displays any of these codes,
the wheels of the lock will stop turning and you will be unable to open it.

Given a target representing the value of the wheels that will unlock the lock, return the
minimum total number of turns required to open the lock, or -1 if it is impossible.

Example 1:

Input: deadends = ["0201","0101","0102","1212","2002"], target = "0202"
Output: 6
Explanation:
A sequence of valid moves would be
"0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202".
Note that a sequence like "0000" -> "0001" -> "0002" -> "0102" -> "0202" would be invalid,
because the wheels of the lock become stuck after the display becomes the dead end "0102".

Example 2:

Input: deadends = ["8888"], target = "0009"
Output: 1
Explanation: We can turn the last wheel in reverse to move from "0000" -> "0009".

Example 3:

Input: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
Output: -1
Explanation: We cannot reach the target without getting stuck.

Constraints:

1 <= deadends.length <= 500
deadends[i].length == 4
target.length == 4
target will not be in the list deadends.
target and deadends[i] consist of digits only.

"""

# V0
# IDEA : BFS ON THE STATE GRAPH (level by level = number of turns)
#
#   node = one lock reading, "0000" .. "9999" (10^4 nodes)
#   edge = one turn: pick one of 4 wheels, roll it +1 or -1 (wrapping 9 <-> 0)
#          -> every node has exactly 8 neighbours
#   every edge costs 1 turn, so plain BFS from "0000" reaches each reading
#   for the first time at its minimum number of turns. deadends are simply
#   nodes we never enter.
#
#   e.g. target = "0009": "0000" -> roll wheel 3 by -1 -> "0009" -> 1 turn
#
# time = O(10^4 * 8 + d), space = O(10^4 + d)    (d = len(deadends))
from collections import deque

START = "0000"


class Solution(object):
    def openLock(self, deadends, target):
        """
        :type deadends: List[str]
        :type target: str
        :rtype: int
        """
        dead = set(deadends)

        # NOTE !!! the lock can already be stuck at the very start
        if START in dead:
            return -1
        if target == START:
            return 0

        queue = deque([START])
        visited = set([START])
        turns = 0

        while queue:
            # everything in the queue now is exactly `turns` turns from START
            turns += 1
            for _ in range(len(queue)):
                code = queue.popleft()
                for neighbour in self._neighbours(code):
                    if neighbour in dead or neighbour in visited:
                        continue
                    if neighbour == target:
                        return turns
                    visited.add(neighbour)
                    queue.append(neighbour)

        # every reachable reading was explored without meeting target
        return -1

    def _neighbours(self, code):
        """Every reading one turn away from code (8 of them)."""
        result = []
        for i in range(len(code)):
            digit = int(code[i])
            for step in (1, -1):
                # % 10 makes the wheel wrap: 9 + 1 -> 0, 0 - 1 -> 9
                new_digit = (digit + step) % 10
                result.append(code[:i] + str(new_digit) + code[i + 1:])
        return result


# V1
# IDEA : BIDIRECTIONAL BFS (grow from START and from target, meet in the middle)
#
#   keep two frontiers: readings reached from START and readings reached from
#   target. each round, expand the SMALLER frontier by one turn; the moment a
#   new reading is already in the other frontier, the two paths join and the
#   answer is the turns taken so far.
#
#   turns are reversible (+1 undoes -1), so searching backward from target is
#   the same graph. each side only goes about half as deep, which visits far
#   fewer readings in practice -- same worst case as V0.
#   Say V0 first in an interview; offer this as the optimisation.
#
# time = O(10^4 * 8 + d), space = O(10^4 + d)
class Solution2(object):
    def openLock(self, deadends, target):
        """
        :type deadends: List[str]
        :type target: str
        :rtype: int
        """
        dead = set(deadends)
        if START in dead:
            return -1
        if target == START:
            return 0

        front = set([START])
        back = set([target])
        visited = set([START, target])
        turns = 0

        while front and back:
            # always expand the smaller side: fewer neighbours to generate
            if len(front) > len(back):
                front, back = back, front

            turns += 1
            next_front = set()
            for code in front:
                for neighbour in self._neighbours(code):
                    # NOTE !!! the two searches meet -> path found
                    if neighbour in back:
                        return turns
                    if neighbour in dead or neighbour in visited:
                        continue
                    visited.add(neighbour)
                    next_front.add(neighbour)
            front = next_front

        # one side ran out of readings: START and target are disconnected
        return -1

    def _neighbours(self, code):
        """Every reading one turn away from code (8 of them)."""
        result = []
        for i in range(len(code)):
            digit = int(code[i])
            for step in (1, -1):
                new_digit = (digit + step) % 10
                result.append(code[:i] + str(new_digit) + code[i + 1:])
        return result
