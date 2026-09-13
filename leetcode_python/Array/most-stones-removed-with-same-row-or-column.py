"""

947. Most Stones Removed with Same Row or Column
Medium

On a 2D plane, we place n stones at some integer coordinate points. Each coordinate point may have at most one stone.

A stone can be removed if it shares either the same row or the same column as another stone that has not been removed.

Given an array stones of length n where stones[i] = [x_i, y_i] represents the location of the i^th stone, return the largest possible number of stones that can be removed.

Example 1:

Input: stones = [[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]
Output: 5
Explanation: One way to remove 5 stones is as follows:
1. Remove stone [2,2] because it shares the same row as [2,1].
2. Remove stone [2,1] because it shares the same column as [0,1].
3. Remove stone [1,2] because it shares the same row as [1,0].
4. Remove stone [1,0] because it shares the same column as [0,0].
5. Remove stone [0,1] because it shares the same row as [0,0].
Stone [0,0] cannot be removed since it does not share a row/column with another stone still on the plane.

Example 2:

Input: stones = [[0,0],[0,2],[1,1],[2,0],[2,2]]
Output: 3
Explanation: One way to make 3 moves is as follows:
1. Remove stone [2,2] because it shares the same row as [2,0].
2. Remove stone [2,0] because it shares the same column as [0,0].
3. Remove stone [0,2] because it shares the same row as [0,0].
Stones [0,0] and [1,1] cannot be removed since they do not share a row/column with another stone still on the plane.

Example 3:

Input: stones = [[0,0]]
Output: 0
Explanation: [0,0] is the only stone on the plane, so you cannot remove it.

Constraints:

1 <= stones.length <= 1000
0 <= x_i, y_i <= 10^4
No two stones are at the same coordinate point.

"""

# V0 

# V1 
# https://www.jianshu.com/p/30d2058db7f7
# IDEA : DFS 
# time = O(n)
# space = O(n)
def removeStones(self, points):
        rows = collections.defaultdict(set)
        cols = collections.defaultdict(set)
        for i, j in points:
            rows[i].add(j)
            cols[j].add(i)

        def dfsRow(i):
            seenR.add(i)
            for j in rows[i]:
                if j not in seenC:
                    dfsCol(j)

        def dfsCol(j):
            seenC.add(j)
            for i in cols[j]:
                if i not in seenR:
                    dfsRow(i)

        seenR, seenC = set(), set()
        islands = 0
        for i, j in points:
            if i not in seenR:
                islands += 1
                dfsRow(i)
                dfsCol(j)
        return len(points) - islands

# V1'
# https://www.jianshu.com/p/30d2058db7f7
# IDEA : DFS optimized
# time = O(n)
# space = O(n)
def removeStones(self, points):
        index = collections.defaultdict(set)
        for i, j in points:
            index[i].add(j + 10000)
            index[j + 10000].add(i)

        def dfs(i):
            seen.add(i)
            for j in index[i]:
                if j not in seen:
                    dfs(j)

        seen = set()
        islands = 0
        for i, j in points:
            if i not in seen:
                islands += 1
                dfs(i)
                dfs(j + 10000)
        return len(points) - islands

# V1''
# https://www.jianshu.com/p/30d2058db7f7
# time = O(n)
# space = O(n)
def removeStones(self, points):
        UF = {}
        def find(x):
            if x != UF[x]:
                UF[x] = find(UF[x])
            return UF[x]
        def union(x, y):
            UF.setdefault(x, x)
            UF.setdefault(y, y)
            UF[find(x)] = find(y)

        for i, j in points:
            union(i, ~j)
        return len(points) - len({find(x) for x in UF})

# V2
# time = O(n)
# space = O(n)
class UnionFind(object):
    def __init__(self, n):
        self.set = range(n)

    def find_set(self, x):
        if self.set[x] != x:
            self.set[x] = self.find_set(self.set[x])  # path compression.
        return self.set[x]

    def union_set(self, x, y):
        x_root, y_root = map(self.find_set, (x, y))
        if x_root == y_root:
            return False
        self.set[min(x_root, y_root)] = max(x_root, y_root)
        return True


class Solution(object):
    def removeStones(self, stones):
        """
        :type stones: List[List[int]]
        :rtype: int
        """
        MAX_ROW = 10000
        union_find = UnionFind(2*MAX_ROW)
        for r, c in stones:
            union_find.union_set(r, c+MAX_ROW)
        return len(stones) - len({union_find.find_set(r) for r, _ in stones})