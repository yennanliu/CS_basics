# V0

# V1 
# http://us.jiuzhang.com/solution/snakes-and-ladders/#tag-highlight-lang-python
class Solution(object):
    def snakesAndLadders(self, board):
        N = len(board)

        def get(s):
            # Given a square num s, return board coordinates (r, c)
            quot, rem = divmod(s-1, N)
            row = N - 1 - quot
            col = rem if row%2 != N%2 else N - 1 - rem
            return row, col

        dist = {1: 0}
        queue = collections.deque([1])
        while queue:
            s = queue.popleft()
            if s == N*N: return dist[s]
            for s2 in range(s+1, min(s+6, N*N) + 1):
                r, c = get(s2)
                if board[r][c] != -1:
                    s2 = board[r][c]
                if s2 not in dist:
                    dist[s2] = dist[s] + 1
                    queue.append(s2)
        return -1

# V2 
# Time:  O(n^2)
# Space: O(n^2)
import collections
class Solution(object):
    def snakesAndLadders(self, board):
        """
        :type board: List[List[int]]
        :rtype: int
        """
        def coordinate(n, s):
            a, b = divmod(s-1, n)
            r = n-1-a
            c = b if r%2 != n%2 else n-1-b
            return r, c

        n = len(board)
        lookup = {1: 0}
        q = collections.deque([1])
        while q:
            s = q.popleft()
            if s == n*n:
                return lookup[s]
            for s2 in range(s+1, min(s+6, n*n)+1):
                r, c = coordinate(n, s2)
                if board[r][c] != -1:
                    s2 = board[r][c]
                if s2 not in lookup:
                    lookup[s2] = lookup[s]+1
                    q.append(s2)
        return -1