"""

909. Snakes and Ladders
Medium

You are given an n x n integer matrix board where the cells are labeled from 1 to n2 in a Boustrophedon style starting from the bottom left of the board (i.e. board[n - 1][0]) and alternating direction each row.

You start on square 1 of the board. In each move, starting from square curr, do the following:

Choose a destination square next with a label in the range [curr + 1, min(curr + 6, n2)].
This choice simulates the result of a standard 6-sided die roll: i.e., there are always at most 6 destinations, regardless of the size of the board.
If next has a snake or ladder, you must move to the destination of that snake or ladder. Otherwise, you move to next.
The game ends when you reach the square n2.
A board square on row r and column c has a snake or ladder if board[r][c] != -1. The destination of that snake or ladder is board[r][c]. Squares 1 and n2 do not have a snake or ladder.

Note that you only take a snake or ladder at most once per move. If the destination to a snake or ladder is the start of another snake or ladder, you do not follow the subsequent snake or ladder.

For example, suppose the board is [[-1,4],[-1,3]], and on the first move, your destination square is 2. You follow the ladder to square 3, but do not follow the subsequent ladder to 4.
Return the least number of moves required to reach the square n2. If it is not possible to reach the square, return -1.

 

Example 1:


Input: board = [[-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1],[-1,35,-1,-1,13,-1],[-1,-1,-1,-1,-1,-1],[-1,15,-1,-1,-1,-1]]
Output: 4
Explanation: 
In the beginning, you start at square 1 (at row 5, column 0).
You decide to move to square 2 and must take the ladder to square 15.
You then decide to move to square 17 and must take the snake to square 13.
You then decide to move to square 14 and must take the ladder to square 35.
You then decide to move to square 36, ending the game.
This is the lowest possible number of moves to reach the last square, so return 4.
Example 2:

Input: board = [[-1,-1],[-1,3]]
Output: 1
 

Constraints:

n == board.length == board[i].length
2 <= n <= 20
grid[i][j] is either -1 or in the range [1, n2].
The squares labeled 1 and n2 do not have any ladders or snakes.

"""

# V0
# IDEA : BFS + Matrix op
# https://www.youtube.com/watch?v=6lH4nO3JfLk
class Solution(object):
    def snakesAndLadders(self, board):
        length = len(board)
        # reverse the board, so our code is more straight forward
        board.reverse()
        # help func
        def intToPos(square):
            # minus 1, since idx usually start from 0
            r = (square - 1) // length
            c = (square - 1) % length
            # special handling for odd row
            if r % 2 == 1:
                c = length - 1 - c
            return [r, c]
        q = deque()
        q.append([1,0]) # [square, moves]
        visited = set()
        while q:
            square, moves = q.popleft()
            # simulate dice throw
            for i in range(1, 7):
                nextSquare = square + i
                r, c = intToPos(nextSquare)
                # if no short cut (ladder)
                if board[r][c] != -1:
                    nextSquare = board[r][c]
                # if meet the end point
                if nextSquare == length * length:
                    return moves + 1
                # if meet short cut (ladder) and not visited yet
                if  nextSquare not in visited:
                    visited.add(nextSquare)
                    q.append([nextSquare, moves+1])
        return -1

# V0
# IDEA : BFS
class Solution(object):
    def snakesAndLadders(self, board):
        """
        NOTE !!! below help func
        """
        def get(s):
            # Given a square num s, return board coordinates (r, c)
            quot, rem = divmod(s-1, N)
            row = N - 1 - quot
            if row%2 != N%2:
                col = rem
            else:
                col = N - 1 - rem
            return row, col

        N = len(board)
        d = {1: 0}
        q = [1]
        while q:
            s = q.pop(0)
            if s == N*N:
                return d[s]
            # NOTE this !!
            for s2 in range(s+1, min(s+6, N*N) + 1):
                r, c = get(s2)
                if board[r][c] != -1:
                    s2 = board[r][c]
                if s2 not in d:
                    d[s2] = d[s] + 1
                    q.append(s2)
        return -1

# V1
# IDEA : BFS + Matrix op
# https://www.youtube.com/watch?v=6lH4nO3JfLk
class Solution(object):
    def snakesAndLadders(self, board):
        length = len(board)
        # reverse the board, so our code is more straight forward
        board.reverse()
        # help func
        def intToPos(square):
            # minus 1, since idx usually start from 0
            r = (square - 1) // length
            c = (square - 1) % length
            # special handling for odd row
            if r % 2 == 1:
                c = length - 1 - c
            return [r, c]
        q = deque()
        q.append([1,0]) # [square, moves]
        visited = set()
        while q:
            square, moves = q.popleft()
            # simulate dice throw
            for i in range(1, 7):
                nextSquare = square + i
                r, c = intToPos(nextSquare)
                # if no short cut (ladder)
                if board[r][c] != -1:
                    nextSquare = board[r][c]
                # if meet the end point
                if nextSquare == length * length:
                    return moves + 1
                # if meet short cut (ladder) and not visited yet
                if  nextSquare not in visited:
                    visited.add(nextSquare)
                    q.append([nextSquare, moves+1])
        return -1

# V1'
# IDEA : BFS
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

# V1''
# https://zxi.mytechroad.com/blog/searching/leetcode-909-snakes-and-ladders/
# C++
# class Solution {
# public:
#   int snakesAndLadders(vector<vector<int>>& board) {
#     const int N = board.size();
#     int steps = 0;
#     vector<int> seen(N * N + 1, 0);
#     queue<int> q;
#     q.push(1);
#     seen[1] = 1;
#     while (!q.empty()) {
#       int size = q.size();
#       ++steps;
#       while (size--) {
#         int s = q.front(); q.pop();        
#         for (int x = s + 1; x <= min(s + 6, N * N); ++x) {          
#           int r, c;
#           getRC(x, N, &r, &c);
#           int nx = board[r][c] == -1 ? x : board[r][c];          
#           if (seen[nx]) continue;
#           if (nx == N * N) return steps;
#           q.push(nx);
#           seen[nx] = 1;
#         } 
#       }      
#     }
#     return -1;
#   }
# private:
#   void getRC(int s, int N, int* r, int* c) {
#     int y = (s - 1) / N;
#     int x = (s - 1) % N;
#     *r = N - 1 - y;
#     *c = (y % 2) ?  N - 1 - x : x;
#   }
# };

# V1''
# https://blog.csdn.net/zml66666/article/details/118258076

# V1'''
# https://blog.csdn.net/qq_46105170/article/details/113068598

# V1''''
# https://www.cnblogs.com/grandyang/p/11342652.html

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