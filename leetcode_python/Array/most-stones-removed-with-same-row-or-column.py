# V0 

# V1 
# https://www.jianshu.com/p/30d2058db7f7
# IDEA : DFS 
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
# Time:  O(n)
# Space: O(n)
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