# https://leetcode.ca/all/1101.html


"""

1101. The Earliest Moment When Everyone Become Friends
Medium

There are n people in a social group labeled from 0 to n - 1. You are given an
array logs where logs[i] = [timestamp_i, x_i, y_i] indicates that x_i and y_i
will be friends at the time timestamp_i.

Friendship is symmetric. That means if a is friends with b, then b is friends
with a. Also, person a is acquainted with a person b if a is friends with b,
or a is a friend of someone acquainted with b.

Return the earliest time for which every person became acquainted with every
other person. If there is no such earliest time, return -1.


Example 1:

Input: logs = [[20190101,0,1],[20190104,3,4],[20190107,2,3],[20190211,1,5],
               [20190224,2,4],[20190301,0,3],[20190312,1,2],[20190322,4,5]], n = 6
Output: 20190301
Explanation:
The first event occurs at timestamp = 20190101, and after 0 and 1 become friends,
we have the following friendship groups [0,1], [2], [3], [4], [5].
The second event occurs at timestamp = 20190104, and after 3 and 4 become friends,
we have the following friendship groups [0,1], [2], [3,4], [5].
The third event occurs at timestamp = 20190107, and after 2 and 3 become friends,
we have the following friendship groups [0,1], [2,3,4], [5].
The fourth event occurs at timestamp = 20190211, and after 1 and 5 become friends,
we have the following friendship groups [0,1,5], [2,3,4].
The fifth event occurs at timestamp = 20190224, and as 2 and 4 are already friends,
nothing happens.
The sixth event occurs at timestamp = 20190301, and after 0 and 3 become friends,
we all become friends.

Example 2:

Input: logs = [[0,2,0],[1,0,1],[3,0,3],[4,1,2],[7,3,1]], n = 4
Output: 3
Explanation: At timestamp = 3, all the persons (i.e., 0, 1, 2, and 3) become friends.


Constraints:

2 <= n <= 100
1 <= logs.length <= 10^4
logs[i].length == 3
0 <= timestamp_i <= 10^9
0 <= x_i, y_i <= n - 1
x_i != y_i
All the values timestamp_i are unique.
All the pairs (x_i, y_i) occur at most one time in the input.

"""

# V0
class Solution(object):
    def earliestAcq(self, logs, n):
        pass


# V0-1
# IDEA: SORT by timestamp + UNION FIND (count components) (gemini)
# https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/union_find.py
class UF(object):

  def __init__(self, n):
    self.n = n
    # 1. 初始時每個人獨立成一個連通塊，數量為 n
    self.cluster_cnt = n
    # 2. 初始時每個人的父節點皆為自己
    self.parents = [x for x in range(n)]

  def get_parent(self, x):
    # 路徑壓縮 (Path Compression)
    if self.parents[x] != x:

      """
      # NOTE !!! 

        use `self.parents[x]` as param


      -> `self.get_parent(self.parents[x])`
      """
      self.parents[x] = self.get_parent(self.parents[x])
    return self.parents[x]

  def union(self, x, y):
    parent_x = self.get_parent(x)
    parent_y = self.get_parent(y)

    # 如果根節點相同，代表已經在同一個連通塊中，不需合併
    if parent_x == parent_y:
      return False

      """
      # NOTE !!! 

        use `parent_x` as param


      ->  point `parent_x` parent to `parent_y`
      """
    # NOTE !!! below
    # 關鍵修正：將 x 的「根節點」指向 y 的「根節點」
    self.parents[parent_x] = parent_y

    # 成功合併兩個獨立連通塊，總數量減 1
    self.cluster_cnt -= 1

    return True

  def get_cluster_cnt(self):
    return self.cluster_cnt


class Solution(object):

  def earliestAcq(self, logs, n):
    """:type logs: List[List[int]]

    :type n: int
    :rtype: int
    """
    uf = UF(n)

    # 1. 將日誌按時間由小到大排序 O(M log M)
    logs.sort(key=lambda log: log[0])

    # 2. 依序合併朋友關係
    for t, x, y in logs:
      uf.union(x, y)

      # 3. 當所有人合併為同一個連通塊（數量變為 1）時，回傳當前時間
      if uf.get_cluster_cnt() == 1:
        return t

    return -1


# V0-2
# IDEA: SORT by timestamp + UNION FIND (count components) (GPT)
class UF(object):

    def __init__(self, n):

        self.n = n

        # Initially, every person is an independent cluster.
        self.cluster_cnt = n

        # Each node is its own parent at first.
        self.parents = [x for x in range(self.n)]

    def union(self, x, y):

        # Find the root parent of x and y.
        parent_x = self.get_parent(x)
        parent_y = self.get_parent(y)

        # x and y are already in the same cluster.
        if parent_x == parent_y:
            return False

        # Merge the two clusters.
        self.parents[parent_x] = parent_y

        # Two clusters become one cluster.
        self.cluster_cnt -= 1

        return True

    def get_parent(self, x):

        # If x is its own parent, x is the root.
        if self.parents[x] == x:
            return x

        # Path compression:
        # directly connect x to the root parent.
        self.parents[x] = self.get_parent(self.parents[x])

        return self.parents[x]

    def is_same_parent(self, x, y):

        return self.get_parent(x) == self.get_parent(y)

    def get_cluster_cnt(self):

        return self.cluster_cnt


class Solution(object):

    def earliestAcq(self, logs, n):

        uf = UF(n)

        # Process logs from earliest time to latest time.
        logs.sort(key=lambda x: x[0])

        for t, x, y in logs:

            # If x and y are in different clusters,
            # union() will merge them and decrease cluster_cnt.
            uf.union(x, y)

            # Everyone is connected.
            if uf.get_cluster_cnt() == 1:
                return t

        return -1



# V0-3
# IDEA: SORT by timestamp + UNION FIND (count components) (claude)
#
#   process the logs in chronological order, every SUCCESSFUL union
#   merges 2 groups -> component count - 1.
#   the answer is the timestamp where the count first drops to 1.
#
# time = O(m log m)
# space = O(n)
#   m = len(logs), n = number of people
class Solution(object):
    def earliestAcq(self, logs, n):
        parent = list(range(n))

        def find(x):
            # path compression
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        groups = n

        # NOTE !!! sort by timestamp (logs[i][0]) first
        for t, x, y in sorted(logs):
            rx, ry = find(x), find(y)
            if rx == ry:
                continue
            parent[rx] = ry
            groups -= 1
            if groups == 1:
                return t

        return -1


# 1-1
# IDEA: DFS
# https://leetcode.ca/2018-12-05-1101-The-Earliest-Moment-When-Everyone-Become-Friends/
class Solution:
    def earliestAcq(self, logs: List[List[int]], n: int) -> int:
        def find(x):
            if p[x] != x:
                p[x] = find(p[x])
            return p[x]

        p = list(range(n))
        for t, x, y in sorted(logs):
            if find(x) == find(y):
                continue
            p[find(x)] = find(y)
            n -= 1
            if n == 1:
                return t
        return -1


# 1-2
# IDEA: Sorting + Union-Find
# https://leetcode.ca/2018-12-05-1101-The-Earliest-Moment-When-Everyone-Become-Friends/
class UnionFind:
    __slots__ = ('p', 'size')

    def __init__(self, n):
        self.p = list(range(n))
        self.size = [1] * n

    def find(self, x: int) -> int:
        if self.p[x] != x:
            self.p[x] = self.find(self.p[x])
        return self.p[x]

    def union(self, a: int, b: int) -> bool:
        pa, pb = self.find(a), self.find(b)
        if pa == pb:
            return False
        if self.size[pa] > self.size[pb]:
            self.p[pb] = pa
            self.size[pa] += self.size[pb]
        else:
            self.p[pa] = pb
            self.size[pb] += self.size[pa]
        return True


class Solution:
    def earliestAcq(self, logs: List[List[int]], n: int) -> int:
        uf = UnionFind(n)
        for t, x, y in sorted(logs):
            if uf.union(x, y):
                n -= 1
                if n == 1:
                    return t
        return -1
