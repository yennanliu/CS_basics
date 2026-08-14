"""

1306. Jump Game III
Medium

Given an array of non-negative integers arr, you are initially positioned at
start index of the array. When you are at index i, you can jump to i + arr[i]
or i - arr[i], check if you can reach any index with value 0.

Notice that you can not jump outside of the array at any time.


Example 1:

Input: arr = [4,2,3,0,3,1,2], start = 5
Output: true
Explanation:
All possible ways to reach at index 3 with value 0 are:
index 5 -> index 4 -> index 1 -> index 3
index 5 -> index 6 -> index 4 -> index 1 -> index 3

Example 2:

Input: arr = [4,2,3,0,3,1,2], start = 0
Output: true
Explanation:
One possible way to reach at index 3 with value 0 is:
index 0 -> index 4 -> index 1 -> index 3

Example 3:

Input: arr = [3,0,2,1,2], start = 2
Output: false
Explanation: There is no way to reach at index 1 with value 0.


Constraints:

1 <= arr.length <= 5 * 10^4
0 <= arr[i] < arr.length
0 <= start < arr.length

"""

# V0
# IDEA : BFS + visited set
#        every index is a node, index i has edges to (i + arr[i]) and (i - arr[i]),
#        so this is just a reachability check on an undirected-ish graph.
# time = O(n)
# space = O(n)
from collections import deque
class Solution(object):
    def canReach(self, arr, start):
        n = len(arr)
        q = deque([start])
        visited = set([start])
        while q:
            i = q.popleft()
            if arr[i] == 0:
                return True
            for j in (i + arr[i], i - arr[i]):
                if 0 <= j < n and j not in visited:
                    visited.add(j)
                    q.append(j)
        return False

# V1
# IDEA : DFS (recursion), mark visited by flipping value to negative
# time = O(n)
# space = O(n)
class Solution(object):
    def canReach(self, arr, start):
        n = len(arr)

        def dfs(i):
            if not (0 <= i < n) or arr[i] < 0:
                return False
            if arr[i] == 0:
                return True
            step = arr[i]
            arr[i] = -1  # mark visited
            return dfs(i + step) or dfs(i - step)

        return dfs(start)
