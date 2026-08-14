"""

1345. Jump Game IV
Hard

Given an array of integers arr, you are initially positioned at the first
index of the array.

In one step you can jump from index i to index:

- i + 1 where: i + 1 < arr.length.
- i - 1 where: i - 1 >= 0.
- j where: arr[i] == arr[j] and i != j.

Return the minimum number of steps to reach the last index of the array.

Notice that you can not jump outside of the array at any time.


Example 1:

Input: arr = [100,-23,-23,404,100,23,23,23,3,404]
Output: 3
Explanation: You need three jumps from index 0 --> 4 --> 3 --> 9.
Note that index 9 is the last index of the array.

Example 2:

Input: arr = [7]
Output: 0
Explanation: Start index is the last index. You do not need to jump.

Example 3:

Input: arr = [7,6,9,6,9,6,9,7]
Output: 1
Explanation: You can jump directly from index 0 to index 7 which is
last index of the array.


Constraints:

1 <= arr.length <= 5 * 10^4
-10^8 <= arr[i] <= 10^8

"""

# V0
# IDEA: BFS (level by level = shortest number of steps)
#
#  NOTE !!!
#   the KEY optimization:
#     after we expand a value group `g[arr[i]]` once, we DELETE it.
#     otherwise a value repeated m times costs O(m^2) and TLEs
#     (e.g. arr = [7,7,7,...,7]).
#     it is safe: every index in that group is enqueued at the SAME
#     (minimal) level, so we never need the group again.
#
# time = O(n)
# space = O(n)
from collections import defaultdict, deque
class Solution(object):
    def minJumps(self, arr):
        n = len(arr)

        # edge
        if n <= 1:
            return 0

        # value -> list of indices holding that value
        g = defaultdict(list)
        for i, x in enumerate(arr):
            g[x].append(i)

        q = deque([0])
        visited = set([0])
        step = 0

        while q:
            for _ in range(len(q)):
                i = q.popleft()
                if i == n - 1:
                    return step

                # same-value jumps (consume the group, then drop it)
                for j in g.pop(arr[i], []):
                    if j not in visited:
                        visited.add(j)
                        q.append(j)

                # neighbour jumps
                for j in (i - 1, i + 1):
                    if 0 <= j < n and j not in visited:
                        visited.add(j)
                        q.append(j)

            step += 1

        return -1
