"""

1135. Connecting Cities With Minimum Cost
Medium

There are n cities labeled from 1 to n. You are given the integer n and an array connections where connections[i] = [xi, yi, costi] indicates that the cost of connecting city xi and city yi (bidirectional connection) is costi.

Return the minimum cost to connect all the n cities such that there is at least one path between each pair of cities. If it is impossible to connect all the n cities, return -1,

The cost is the sum of the connections' costs used.

 

Example 1:


Input: n = 3, connections = [[1,2,5],[1,3,6],[2,3,1]]
Output: 6
Explanation: Choosing any 2 edges will connect all cities so we choose the minimum 2.
Example 2:


Input: n = 4, connections = [[1,2,3],[3,4,4]]
Output: -1
Explanation: There is no way to connect all cities even if all edges are used.
 

Constraints:

1 <= n <= 104
1 <= connections.length <= 104
connections[i].length == 3
1 <= xi, yi <= n
xi != yi
0 <= costi <= 105

"""

# V0

# V1
# IDEA : UNION FIND
# https://leetcode.com/problems/connecting-cities-with-minimum-cost/discuss/831263/Python-very-Concise-Union-Find
class Solution:
    def minimumCost(self, N: int, connections: List[List[int]]) -> int:
        parents = [x for x in range(N)]
        
        def find(x):
            if parents[x] != x: parents[x] = find(parents[x])
            return parents[x]
        
        def union(u, v):
            if find(u) == find(v): return False
            parents[find(v)] = find(u)
            return True

        connections.sort(key = lambda x: x[2])
        ans = 0
        for u, v, val in connections:
            if union(u-1, v-1): ans += val
        return ans if len(set(find(x) for x in parents)) == 1 else -1

# V1
# IDEA : UNION FIND
# https://leetcode.com/problems/connecting-cities-with-minimum-cost/discuss/460868/Python-Union-Find
import heapq 
class Solution(object):
    def minimumCost(self, N, connections):
        """
        :type N: int
        :type connections: List[List[int]]
        :rtype: int
        """
        
        nodes = range(0, N) 
        parents = range(0, N)
        
        def find_root (node):         
            while parents[node] != node : 
                node = parents[node]
            return node
        
        def union (node1, node2) : 
            root1 = find_root(node1)
            root2 = find_root(node2)
            if root1 == root2 : 
                return False
            parents[root1] = root2
            return True
        
        
        heap = []
        
        for node1, node2, weight in connections : 
            heapq.heappush(heap, (weight, node1-1, node2-1))
        
        cost = 0 
        
        for i in range (0, len(connections)) : 
            edge = heapq.heappop(heap)
            if union (edge[1], edge[2])  :
                cost += edge[0]
            
        
        return cost if len(set([find_root(node) for node in range(N)])) ==1 else -1

# V1
# IDEA : UNION FIND
# https://leetcode.com/problems/connecting-cities-with-minimum-cost/discuss/511197/Fast-Python-with-explanation
from heapq import heapify, heappush, heappop
class Solution:
    def find(self, cur, my_union):
        root = cur
        while root != my_union[root]:
            root = my_union[root]
        while cur != root:
            nxt = my_union[cur]
            my_union[cur] = root
            cur = nxt
        return root
    
    def union(self, left_r, right_r, my_union):           
        if left_r > right_r:
            my_union[left_r] = right_r
        else:
            my_union[right_r] = left_r
        return
    
    def minimumCost(self, N: int, connections: List[List[int]]) -> int:
        my_union = [i for i in range(N+1)]
        edge_pool = [(cost, left, right) for left, right, cost in connections]
        heapify(edge_pool)
        res = 0
        connection = 0
        while edge_pool:
            cost, left, right = heappop(edge_pool)
            left_r, right_r = self.find(left, my_union), self.find(right, my_union)
            if left_r != right_r:
                res += cost
                connection += 1
                self.union(left_r, right_r, my_union)
                if connection == N -1:
                    return res
        return -1

# V1
# IDEA : prime
# https://leetcode.com/problems/connecting-cities-with-minimum-cost/discuss/1629009/Prims-Algo-Python
class Solution:
    def minimumCost(self, n: int, connections: List[List[int]]) -> int:
        adj_list= defaultdict(list) 
        for i in range(len(connections)):
            adj_list[connections[i][0]].append([connections[i][2], connections[i][1]])
            adj_list[connections[i][1]].append([connections[i][2], connections[i][0]])
        
        mst_set= set()
        pq= [(0,1)]
        res=0
        while pq and len(mst_set) <n:
            cost, node = heapq.heappop(pq)
            # print(mst_set,"c,n", cost, node, "pq", pq)
            if node not in mst_set:
                res+= cost
                mst_set.add(node)
                for neicost, nei in adj_list[node]:
                    if nei not in mst_set: 
                        heapq.heappush(pq, (neicost, nei))
        return res if len(mst_set)==n else -1

# V1
# IDEA : Kruskal
# https://leetcode.com/problems/connecting-cities-with-minimum-cost/discuss/1711108/Python-Kruskal
class UnionFind:
    def __init__(self, n):
        self.p = list(range(n))
        self.r = [0] * n
        
    def find(self, v):
        if v != self.p[v]:
            self.p[v] = self.find(self.p[v])
        return self.p[v]
    
    def connected(self, u, v):
        return self.find(u) == self.find(v)
    
    def union(self, u, v) -> bool:
        up, vp = self.find(u), self.find(v)
        if up == vp:
            return False
        
        if self.r[up] > self.r[vp]:
            self.p[vp] = up
        elif self.r[up] < self.r[vp]:
            self.p[up] = vp
        else:
            self.p[up] = vp
            self.r[vp] += 1
            
        return True
        

class Solution:
    def minimumCost(self, n: int, connections: List[List[int]]) -> int:
        uf = UnionFind(n+1) # ignoring 0 since it is not in the graph
        connections.sort(key=lambda x:x[2])
        mst = cost = 0
        for u, v, w in connections:
            if uf.connected(u, v):
                continue
            uf.union(u, v)
            mst += 1
            cost += w
            if mst == n - 1:
                return cost
        
        return -1

# V1
# https://www.shangmayuan.com/a/9938a545153b42b58dbc675c.html
class Solution:
    def minimumCost(self, n: int, connections: List[List[int]]) -> int:
        min_idx, min_val = (-1, -1), float("inf")
        graph = collections.defaultdict(dict)
        for edge in connections:
            if edge[1] not in graph[edge[0]] or edge[2] < graph[edge[0]][edge[1]]:
                graph[edge[0]][edge[1]] = edge[2]
                graph[edge[1]][edge[0]] = edge[2]
                if edge[2] < min_val:
                    min_idx, min_val = (edge[0], edge[1]), edge[2]

        ans = 0

        waiting = {i for i in range(1, n + 1)}
        heap = []

        # add shortest side
        ans += min_val
        waiting.remove(min_idx[0])
        waiting.remove(min_idx[1])

        for n2, v2 in graph[min_idx[0]].items():
            if n2 in waiting:
                heapq.heappush(heap, (v2, n2))
        for n2, v2 in graph[min_idx[1]].items():
            if n2 in waiting:
                heapq.heappush(heap, (v2, n2))

        while heap and waiting:
            v1, n1 = heapq.heappop(heap)
            if n1 in waiting:
                ans += v1
                waiting.remove(n1)
                for n2, v2 in graph[n1].items():
                    if n2 in waiting:
                        heapq.heappush(heap, (v2, n2))

        return ans if not waiting else -1

# V1
# https://blog.csdn.net/Changxing_J/article/details/117188248

# V1
# IDEA :  Minimum Spanning Tree (Using Kruskal's algorithm)
# https://leetcode.com/problems/connecting-cities-with-minimum-cost/solution/
# JAVA
# /** Vanilla Disjoint-set Union Find **/
# class DisjointSet {
#     private int[] parents;
#
#     public void Union(int a, int b) {
#         int rootA = Find(a);
#         int rootB = Find(b);
#         // If both a and b have same root, i.e. they both belong to the same set, hence we don't need to take union
#         if (rootA == rootB) return;
#         // Take union by assigning rootA as the parent of rootB
#         this.parents[rootB] = rootA;
#     }
#
#     public int Find(int a) {
#         // Traverse all the way to the top (root) going through the parent nodes
#         while (a != this.parents[a]) {
#             a = this.parents[a];
#         }
#         return a;
#     }
#
#     public boolean isInSameGroup(int a, int b) {
#         // Return true if both a and b belong to the same set, otherwise return false
#         return Find(a) == Find(b);
#     }
#
#     public DisjointSet(int N) {
#         this.parents = new int[N + 1];
#         // Set the initial parent node to itself
#         for (int i = 1; i <= N; ++i) {
#             this.parents[i] = i;
#         }
#     }
# }

# V1
# IDEA :  Minimum Spanning Tree (Using Kruskal's algorithm)
# https://leetcode.com/problems/connecting-cities-with-minimum-cost/solution/
# JAVA
# class DisjointSet {
#     private int[] weights; // Used to store weights of each nodes
#     private int[] parents;
#
#     // Modify Union method to incorporate weighted compression
#     private void Union(int a, int b) {
#         int rootA = Find(a);
#         int rootB = Find(b);
#         // If both a and b have same root, i.e. they both belong to the same set, hence we don't need to take union
#         if (rootA == rootB) return;
#         // Weighted union
#         if (this.weights[rootA] > this.weights[rootB]) {
#             // In case rootA is having more weight
#             // 1. Make rootA as the parent of rootB
#             // 2. Increment the weight of rootA by rootB's weight
#             this.parents[rootB] = rootA;
#             this.weights[rootA] += this.weights[rootB];
#         } else {
#             // Otherwise
#             // 1. Make rootB as the parent of rootA
#             // 2. Increment the weight of rootB by rootA's weight
#             this.parents[rootA] = rootB;
#             this.weights[rootB] += this.weights[rootA];
#         }
#     }
#
#     // Initialize weight for each node to be 1
#     public DisjointSet(int N) {
#         this.parents = new int[N + 1];
#         this.weights = new int[N + 1];
#         // Set the initial parent node to itself
#         for (int i = 1; i <= N; ++i) {
#             this.parents[i] = i;
#             this.weights[i] = 1;
#         }
#     }
# }

# V2