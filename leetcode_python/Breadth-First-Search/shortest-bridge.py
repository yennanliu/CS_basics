# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83716820
# IDEA : BFS + DFS 
class Solution:
    def shortestBridge(self, A):
        """
        :type A: List[List[int]]
        :rtype: int
        """
        M, N = len(A), len(A[0])
        dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)]
        visited = [[0] * N for _ in range(M)]
        hasfind = False
        que = collections.deque()
        for i in range(M):
            if hasfind: break
            for j in range(N):
                if A[i][j] == 1:
                    self.dfs(A, i, j, visited, que)
                    hasfind = True
                    break
        step = 0
        while que:
            size = len(que)
            for _ in range(size):
                i, j = que.popleft()
                for d in dirs:
                    x, y = i + d[0], j + d[1]
                    if 0 <= x < M and 0 <= y < N:
                        visited[x][y] = 1
                        if A[x][y] == 1:
                            return step
                        elif A[x][y] == 0:
                            A[x][y] = 2
                            que.append((x, y))
                        else:
                            continue
            step += 1
        return -1

    def dfs(self, A, i, j, visited, que):
        if visited[i][j]: return
        visited[i][j] = 1
        M, N = len(A), len(A[0])
        dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)]
        if A[i][j] == 1:
            que.append((i, j))
            A[i][j] = 2
            for d in dirs:
                x, y = i + d[0], j + d[1]
                if 0 <= x < M and 0 <= y < N:
                    self.dfs(A, x, y, visited, que)

# V2 
# Time:  O(n^2)
# Space: O(n^2)
import collections
class Solution(object):
    def shortestBridge(self, A):
        """
        :type A: List[List[int]]
        :rtype: int
        """
        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

        def get_islands(A):
            islands = []
            done = set()
            for r, row in enumerate(A):
                for c, val in enumerate(row):
                    if val == 0 or (r, c) in done:
                        continue
                    s = [(r, c)]
                    lookup = set(s)
                    while s:
                        node = s.pop()
                        for d in directions:
                            nei = node[0]+d[0], node[1]+d[1]
                            if not (0 <= nei[0] < len(A) and 0 <= nei[1] < len(A[0])) or \
                               nei in lookup or A[nei[0]][nei[1]] == 0:
                                continue
                            s.append(nei)
                            lookup.add(nei)
                    done |= lookup
                    islands.append(lookup)
                    if len(islands) == 2:
                        break
            return islands

        lookup, target = get_islands(A)
        q = collections.deque([(node, 0) for node in lookup])
        while q:
            node, dis = q.popleft()
            if node in target:
                return dis-1
            for d in directions:
                nei = node[0]+d[0], node[1]+d[1]
                if not (0 <= nei[0] < len(A) and 0 <= nei[1] < len(A[0])) or \
                   nei in lookup:
                    continue
                q.append((nei, dis+1))
                lookup.add(nei)