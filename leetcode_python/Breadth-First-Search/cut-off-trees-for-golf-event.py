"""

675. Cut Off Trees for Golf Event
Hard

You are asked to cut off all the trees in a forest for a golf event. The forest is represented as an m x n matrix. In this matrix:

0 means the cell cannot be walked through.
1 represents an empty cell that can be walked through.
A number greater than 1 represents a tree in a cell that can be walked through, and this number is the tree's height.

In one step, you can walk in any of the four directions: north, east, south, and west. If you are standing in a cell with a tree, you can choose whether to cut it off.

You must cut off the trees in order from shortest to tallest. When you cut off a tree, the value at its cell becomes 1 (an empty cell).

Starting from the point (0, 0), return the minimum steps you need to walk to cut off all the trees. If you cannot cut off all the trees, return -1.

Note: The input is generated such that no two trees have the same height, and there is at least one tree needs to be cut off.

Example 1:

https://assets.leetcode.com/uploads/2020/11/26/trees1.jpg

Input: forest = [[1,2,3],[0,0,4],[7,6,5]]
Output: 6
Explanation: Following the path above allows you to cut off the trees from shortest to tallest in 6 steps.

Example 2:

https://assets.leetcode.com/uploads/2020/11/26/trees2.jpg

Input: forest = [[1,2,3],[0,0,0],[7,6,5]]
Output: -1
Explanation: The trees in the bottom row cannot be accessed as the middle row is blocked.

Example 3:

Input: forest = [[2,3,4],[0,0,5],[8,7,6]]
Output: 6
Explanation: You can follow the same path as Example 1 to cut off all the trees.
Note that you can cut off the first tree at (0, 0) before making any steps.

Constraints:

m == forest.length
n == forest[i].length
1 <= m, n <= 50
0 <= forest[i][j] <= 10^9
Heights of all trees are distinct.

"""

# V0

# V1
# https://leetcode.com/problems/cut-off-trees-for-golf-event/discuss/107396/Python-solution-based-on-wufangjie's-(Hadlock's-algorithm)
# IDEA : wufangjie's (Hadlock's algorithm?)
# time = O(T * m * n), T = number of trees, m*n = grid size
# space = O(m * n)
class Solution:
    def cutOffTree(self, forest):

        # Add sentinels (a border of zeros) so we don't need index-checks later on.
        forest.append([0] * len(forest[0]))
        for row in forest:
            row.append(0)

        # Find the trees.
        trees = [(height, i, j)
                 for i, row in enumerate(forest)
                 for j, height in enumerate(row)
                 if height > 1]

        # Can we reach every tree? If not, return -1 right away.
        queue = [(0, 0)]
        reached = set()
        for i, j in queue:
            if (i, j) not in reached and forest[i][j]:
                reached.add((i, j))
                queue += (i+1, j), (i-1, j), (i, j+1), (i, j-1)
        if not all((i, j) in reached for (_, i, j) in trees):
            return -1

        # Distance from (i, j) to (I, J).
        def distance(i, j, I, J):
            now, soon = [(i, j)], []
            expanded = set()
            manhattan = abs(i - I) + abs(j - J)
            detours = 0
            while True:
                if not now:
                    now, soon = soon, []
                    detours += 1
                i, j = now.pop()
                if (i, j) == (I, J):
                    return manhattan + 2 * detours
                if (i, j) not in expanded:
                    expanded.add((i, j))
                    for i, j, closer in (i+1, j, i < I), (i-1, j, i > I), (i, j+1, j < J), (i, j-1, j > J):
                        if forest[i][j]:
                            (now if closer else soon).append((i, j))

        # Sum the distances from one tree to the next (sorted by height).
        trees.sort()
        return sum(distance(i, j, I, J) for (_, i, j), (_, I, J) in zip([(0, 0, 0)] + trees, trees))

# V1'
# https://leetcode.com/problems/cut-off-trees-for-golf-event/discuss/107415/my-python-solution-inspired-by-A*-algorithm
# time = O(T * m * n), T = number of trees, m*n = grid size
# space = O(m * n)
class Solution(object):
    def cutOffTree(self, forest):
        """
        :type forest: List[List[int]]
        :rtype: int
        """
        nrow, ncol = len(forest), len(forest[0])
        forest.append([0] * ncol)
        for row in forest:
            row.append(0)

        trees = {(i, j) for i in range(nrow) for j in range(ncol)
                 if forest[i][j] > 1}

        canReach = {(0, 0)}
        stack = [(0, 0)]
        while stack:
            i, j = stack.pop()
            for i2, j2 in ((i - 1, j), (i + 1, j), (i, j - 1), (i, j + 1)):
                if forest[i2][j2] != 0 and (i2, j2) not in canReach:
                    canReach.add((i2, j2))
                    stack.append((i2, j2))

        if trees.difference(canReach):
            return -1

        def get_sp(p1, p2):
            theMin = abs(p1[0] - p2[0]) + abs(p1[1] - p2[1])
            stack1, stack2 = [p1], []
            used, visited = {p1}, {p1}

            while 1:
                if not stack1:
                    stack1, stack2 = stack2, stack1
                    used.update(stack1)
                    theMin += 2

                p = stack1.pop()
                if p == p2:
                    break

                i, j = p
                add1, add2 = [], []

                if i == p2[0]:
                    add2.append((i - 1, j))
                    add2.append((i + 1, j))
                elif i < p2[0]:
                    add2.append((i - 1, j))
                    add1.append((i + 1, j))
                else:
                    add1.append((i - 1, j))
                    add2.append((i + 1, j))

                if j == p2[1]:
                    add2.append((i, j - 1))
                    add2.append((i, j + 1))
                elif j < p2[1]:
                    add2.append((i, j - 1))
                    add1.append((i, j + 1))
                else:
                    add1.append((i, j - 1))
                    add2.append((i, j + 1))

                for v in add1:
                    if forest[v[0]][v[1]] != 0 and v not in used:
                        visited.add(v)
                        used.add(v)
                        stack1.append(v)
                for v in add2:
                    if forest[v[0]][v[1]] != 0 and v not in visited:
                        visited.add(v)
                        stack2.append(v)

            return theMin

        seq = sorted(trees, key=lambda x: forest[x[0]][x[1]])
        if seq[0] != (0, 0):
            seq.insert(0, (0, 0))
        return sum(get_sp(seq[i], seq[i + 1]) for i in range(len(seq) - 1))
        
# V1''
# http://bookshadow.com/weblog/2017/09/10/leetcode-cut-off-trees-for-golf-event/
# JAVA

# V1'''
# https://leetcode.com/problems/cut-off-trees-for-golf-event/discuss/107416/Very-simple-Python-BFS-But-Why-TLE
# TO SOLVE : TIME OUT ERROR
import collections
# time = O(T * m * n), T = number of trees, m*n = grid size
# space = O(m * n)
class Solution(object):
    def cutOffTree(self, G):
        """
        :type forest: List[List[int]]
        :rtype: int
        """
        if not G or not G[0]: return -1
        m, n = len(G), len(G[0])
        trees = []
        for i in range(m):
            for j in range(n):
                if G[i][j] > 1:
                    trees.append((G[i][j], i, j))
        trees = sorted(trees)
        count = 0
        cx, cy = 0, 0
        for h, x, y in trees:
            step = self.BFS(G, cx, cy, x, y)
            if step == -1:
                return -1
            else:
                count += step
                G[x][y] = 1
                cx, cy = x, y
        return count

    def BFS(self, G, cx, cy, tx, ty):
        m, n = len(G), len(G[0])
        visited = [[False for j in range(n)] for i in range(m)]
        Q = collections.deque()
        step = -1
        Q.append((cx, cy))
        while len(Q) > 0:
            size = len(Q)
            step += 1
            for i in range(size):
                x, y = Q.popleft()
                visited[x][y] = True
                if x == tx and y == ty:
                    return step
                for nx, ny in [(x + 1, y), (x - 1, y), (x, y-1), (x, y + 1)]:
                    if nx < 0 or nx >= m or ny < 0 or ny >= n or G[nx][ny] == 0 or visited[nx][ny]:
                        continue
                    Q.append((nx, ny))
        return -1

# V2