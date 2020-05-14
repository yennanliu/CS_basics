# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83307822
# IDEA :DFS
# DEMO : collections.defaultdict
# In [41]: flights
# Out[41]: [[0, 1, 100], [1, 2, 100], [0, 2, 500]]
#
# In [42]:  for u, v, e in flights:
#     ...:     graph[u][v]=e
#     ...:    
#
# In [43]: graph
# Out[43]: defaultdict(dict, {0: {1: 100, 2: 500}, 1: {2: 100}})
class Solution(object):
    def findCheapestPrice(self, n, flights, src, dst, K):
        """
        :type n: int
        :type flights: List[List[int]]
        :type src: int
        :type dst: int
        :type K: int
        :rtype: int
        """
        graph = collections.defaultdict(dict)
        for u, v, e in flights:
            graph[u][v] = e
        visited = [0] * n
        ans = [float('inf')]
        self.dfs(graph, src, dst, K + 1, 0, visited, ans)
        return -1 if ans[0] == float('inf') else ans[0]

    def dfs(self, graph, src, dst, k, cost, visited, ans):
        if src == dst:
            ans[0] = cost
            return
        if k == 0:
            return
        for v, e in graph[src].items():
            if visited[v]: continue
            if cost + e > ans[0]: continue
            visited[v] = 1
            self.dfs(graph, v, dst, k - 1, cost + e, visited, ans)
            visited[v] = 0

### Test case : dev

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83307822
# IDEA : BFS
class Solution(object):
    def findCheapestPrice(self, n, flights, src, dst, K):
        """
        :type n: int
        :type flights: List[List[int]]
        :type src: int
        :type dst: int
        :type K: int
        :rtype: int
        """
        graph = collections.defaultdict(dict)
        for u, v, e in flights:
            graph[u][v] = e
        ans = float('inf')
        que = collections.deque()
        que.append((src, 0))
        step = 0
        while que:
            size = len(que)
            for i in range(size):
                cur, cost = que.popleft()
                if cur == dst:
                    ans = min(ans, cost)
                for v, w in graph[cur].items():
                    if cost + w > ans:
                        continue
                    que.append((v, cost + w))
            if step > K: break
            step += 1
        return -1 if ans == float('inf') else ans

# V1''
# https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/267200/Python-Dijkstra
# IDEA : Dijkstra algorithm
class Solution(object):
    def findCheapestPrice(self,n, flights, src, dst, K):
        pq, g = [(0,src,K+1)], collections.defaultdict(dict)
        for s,d,c in flights: g[s][d] = c
        while pq:
            cost, s, k = heapq.heappop(pq)
            if s == dst: return cost
            if k:
                for d in g[s]:
                    heapq.heappush(pq, (cost+g[s][d], d, k-1))
        return -1

# V1'''
# https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/317262/2-Clean-Python-Solution-(BFS-Dijkstra-Explained)
# IDEA : Dijkstra algorithm
class Solution(object):
    def findCheapestPrice(self, n, flights, src, dst, K):
        graph = collections.defaultdict(list)
        pq = []

        for u, v, w in flights: graph[u].append((w, v))

        heapq.heappush(pq, (0, K+1, src))
        while pq:
            price, stops, city = heapq.heappop(pq)

            if city is dst: return price
            if stops>0:
                for price_to_nei, nei in graph[city]:
                    heapq.heappush(pq, (price+price_to_nei, stops-1, nei))
        return -1

# V1''''
# https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/317262/2-Clean-Python-Solution-(BFS-Dijkstra-Explained)
# IDEA : BFS
class Solution(object):
    def findCheapestPrice(self, n, flights, src, dst, K):
        graph = collections.defaultdict(list)
        q = collections.deque()
        min_price = float('inf')

        for u, v, w in flights: 
            graph[u].append((w, v))
        q.append((src, 0, 0))
        while q:
            city, stops, price = q.popleft()
            if city==dst:
                min_price = min(min_price, price)
                continue

            if stops<=K and price<=min_price:
                for price_to_nei, nei in graph[city]:
                    q.append((nei, stops+1, price+price_to_nei))

        return min_price if min_price!=float('inf') else -1

# V1'''''
# https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115541/JavaPython-Priority-Queue-Solution
# IDEA : Priority Queue 
class Solution(object):
    def findCheapestPrice(self, n, flights, src, dst, k):
        f = collections.defaultdict(dict)
        for a, b, p in flights:
            f[a][b] = p
        heap = [(0, src, k + 1)]
        while heap:
            p, i, k = heapq.heappop(heap)
            if i == dst:
                return p
            if k > 0:
                for j in f[i]:
                    heapq.heappush(heap, (p + f[i][j], j, k - 1))
        return -1

# V2 
# Time:  O((|E| + |V|) * log|V|) = O(|E| * log|V|),
#        if we can further to use Fibonacci heap, it would be O(|E| + |V| * log|V|)
# Space: O(|E| + |V|) = O(|E|)
import collections
import heapq
class Solution(object):
    def findCheapestPrice(self, n, flights, src, dst, K):
        """
        :type n: int
        :type flights: List[List[int]]
        :type src: int
        :type dst: int
        :type K: int
        :rtype: int
        """
        adj = collections.defaultdict(list)
        for u, v, w in flights:
            adj[u].append((v, w))
        best = collections.defaultdict(lambda: collections.defaultdict(lambda: float("inf")))
        min_heap = [(0, src, K+1)]
        while min_heap:
            result, u, k = heapq.heappop(min_heap)
            if k < 0 or best[u][k] < result:
                continue
            if u == dst:
                return result
            for v, w in adj[u]:
                if result+w < best[v][k-1]:
                    best[v][k-1] = result+w                    
                    heapq.heappush(min_heap, (result+w, v, k-1))
        return -1