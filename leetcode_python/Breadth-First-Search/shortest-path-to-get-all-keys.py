"""

864. Shortest Path to Get All Keys
Hard

You are given an m x n grid grid where:

'.' is an empty cell.
'#' is a wall.
'@' is the starting point.
Lowercase letters represent keys.
Uppercase letters represent locks.
You start at the starting point and one move consists of walking one space in one of the four cardinal directions. You cannot walk outside the grid, or walk into a wall.

If you walk over a key, you can pick it up and you cannot walk over a lock unless you have its corresponding key.

For some 1 <= k <= 6, there is exactly one lowercase and one uppercase letter of the first k letters of the English alphabet in the grid. This means that there is exactly one key for each lock, and one lock for each key; and also that the letters used to represent the keys and locks were chosen in the same order as the English alphabet.

Return the lowest number of moves to acquire all keys. If it is impossible, return -1.

 

Example 1:


Input: grid = ["@.a.#","###.#","b.A.B"]
Output: 8
Explanation: Note that the goal is to obtain all the keys not to open all the locks.
Example 2:


Input: grid = ["@..aA","..B#.","....b"]
Output: 6
Example 3:


Input: grid = ["@Aa"]
Output: -1
 

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 30
grid[i][j] is either an English letter, '.', '#', or '@'.
The number of keys in the grid is in the range [1, 6].
Each key in the grid is unique.
Each key in the grid has a matching lock.

"""

# V0

# V1
# IDEA : BFS
# https://leetcode.com/problems/shortest-path-to-get-all-keys/discuss/458167/Python-BFS
class Solution:
    def shortestPathAllKeys(self, grid):
        R, C = len(grid), len(grid[0])
        ii, jj =0 ,0 
        target = 0
        for i in range(R):
            for j in range(C):
                if grid[i][j]=='@':
                    ii, jj = i, j
                if grid[i][j].islower():
                    target+=1
        def bfs(i, j):
            from collections import deque
            Q = deque([(i,j, 0 ,set())])
            seen = {(i,j,tuple(sorted(set())))}
            while Q:
                i,j, d, s = Q.popleft()
                for di, dj in [(0,1),(1,0),(0,-1),(-1,0)]:
                    r, c = i+di, j+dj
                    if 0<=r<R and 0<=c<C:
                        if grid[r][c].islower():
                            #s1 = s
                            s1 =  s.copy()
                            s1.add(grid[r][c])
                            if len(s1)==target:return d+1
                            tuple_s = tuple(sorted(set(s1)))
                            if (r,c,tuple_s) not in seen:
                                seen.add((r,c,tuple_s))
                                Q.append((r,c, d+1,s1))
                        elif grid[r][c].isupper():
                            if grid[r][c].lower() in s:
                                tuple_s = tuple(sorted(set(s)))
                                if (r,c,tuple_s) not in seen:
                                    seen.add((r,c,tuple_s))
                                    Q.append((r,c,  d+1, s))
                        elif grid[r][c]!='#':
                            tuple_s = tuple(sorted(set(s)))
                            if (r,c,tuple_s) not in seen:
                                seen.add((r,c,tuple_s))
                                Q.append((r,c, d+1, s))
            return -1
        return bfs(ii,jj)

# V1'
# IDEA : BFS
# https://leetcode.com/problems/shortest-path-to-get-all-keys/discuss/324603/python-bfs
from collections import deque
# bfs can visit back only if the key is updated
class Solution:
    def shortestPathAllKeys(self, grid):
        h, w = len(grid), len(grid[0])
        n_keys = 0
        begin_i, begin_j = 0, 0

        for i in range(h):
            for j in range(w):
                c = grid[i][j]
                if c in 'abcdef':
                    n_keys += 1
                if c == '@':
                    begin_i, begin_j = i, j

        q = deque()
        q += [[begin_i, begin_j, '@.abcdef', 0, 0]]
        visited = set()

        while q:
            i, j, key, steps, vis_keys = q.pop()

            c = grid[i][j]
            if c in 'abcdef' and c.upper() not in key:
                key += c.upper()
                vis_keys += 1
            if vis_keys == n_keys:
                return steps

            for di, dj in zip([-1, 1, 0, 0], [0, 0, -1, 1]):
                ni, nj = i + di, j + dj
                if 0 <= ni < h and 0 <= nj < w and grid[ni][nj] in key:
                    if (ni, nj, key) not in visited:
                        visited.add((ni, nj, key))
                        q.appendleft([ni, nj, key, steps + 1, vis_keys])

        return -1

# V1'
# IDEA : BFS + memorization
# https://leetcode.com/problems/shortest-path-to-get-all-keys/discuss/146990/Python-Heapq-solution-w-memoization
class Solution:
    def shortestPathAllKeys(self, grid):
        final, m, n, si, sj = 0, len(grid), len(grid[0]), 0, 0
        for i in range(m):
            for j in range(n):
                if grid[i][j] in "abcdef":
                    final |= 1 << ord(grid[i][j]) - ord("a")
                elif grid[i][j] == "@":
                    si, sj = i, j
        q, memo = [(0, si, sj, 0)], set()
        while q:
            moves, i, j, state = heapq.heappop(q)
            if state == final: return moves
            for x, y in ((i - 1, j), (i + 1, j), (i, j - 1), (i, j + 1)):
                if 0 <= x < m and 0 <= y < n and grid[x][y] != "#":
                    if grid[x][y].isupper() and not state & 1 << (ord(grid[x][y].lower()) - ord("a")): continue
                    newState = ord(grid[x][y]) >= ord("a") and state | 1 << (ord(grid[x][y]) - ord("a")) or state
                    if (newState, x, y) not in memo:
                        memo.add((newState, x, y))
                        heapq.heappush(q, (moves + 1, x, y, newState))
        return -1

# V2