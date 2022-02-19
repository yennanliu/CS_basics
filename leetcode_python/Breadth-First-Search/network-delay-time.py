"""

743. Network Delay Time
Medium

You are given a network of n nodes, labeled from 1 to n. You are also given times, a list of travel times as directed edges times[i] = (ui, vi, wi), where ui is the source node, vi is the target node, and wi is the time it takes for a signal to travel from source to target.

We will send a signal from a given node k. Return the time it takes for all the n nodes to receive the signal. If it is impossible for all the n nodes to receive the signal, return -1.

 

Example 1:


Input: times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
Output: 2
Example 2:

Input: times = [[1,2,1]], n = 2, k = 1
Output: 1
Example 3:

Input: times = [[1,2,1]], n = 2, k = 2
Output: -1
 

Constraints:

1 <= k <= n <= 100
1 <= times.length <= 6000
times[i].length == 3
1 <= ui, vi <= n
ui != vi
0 <= wi <= 100
All the pairs (ui, vi) are unique. (i.e., no multiple edges.)

"""

# V0 
# IDEA : Dijkstra
class Solution:
    def networkDelayTime(self, times, N, K):
        K -= 1
        nodes = collections.defaultdict(list)
        for u, v, w in times:
            nodes[u - 1].append((v - 1, w))
        dist = [float('inf')] * N
        dist[K] = 0
        done = set()
        for _ in range(N):
            smallest = min((d, i) for (i, d) in enumerate(dist) if i not in done)[1]
            for v, w in nodes[smallest]:
                if v not in done and dist[smallest] + w < dist[v]:
                    dist[v] = dist[smallest] + w
            done.add(smallest)
        return -1 if float('inf') in dist else max(dist)

# V1
# IDEA : DFS
# https://leetcode.com/problems/network-delay-time/solution/
# JAVA
# class Solution {
#     // Adjacency list
#     Map<Integer, List<Pair<Integer, Integer>>> adj = new HashMap<>();
#
#     private void DFS(int[] signalReceivedAt, int currNode, int currTime) {
#         // If the current time is greater than or equal to the fastest signal received
#         // Then no need to iterate over adjacent nodes
#         if (currTime >= signalReceivedAt[currNode]) {
#             return;
#         }
#
#         // Fastest signal time for currNode so far
#         signalReceivedAt[currNode] = currTime;
#       
#         if (!adj.containsKey(currNode)) {
#             return;
#         }
#        
#         // Broadcast the signal to adjacent nodes
#         for (Pair<Integer, Integer> edge : adj.get(currNode)) {
#             int travelTime = edge.getKey();
#             int neighborNode = edge.getValue();
#            
#             // currTime + time : time when signal reaches neighborNode
#             DFS(signalReceivedAt, neighborNode, currTime + travelTime);
#         }
#     }
#    
#     public int networkDelayTime(int[][] times, int n, int k) {
#         // Build the adjacency list
#         for (int[] time : times) {
#             int source = time[0];
#             int dest = time[1];
#             int travelTime = time[2];
#            
#             adj.putIfAbsent(source, new ArrayList<>());
#             adj.get(source).add(new Pair(travelTime, dest));
#         }
#        
#         // Sort the edges connecting to every node
#         for (int node : adj.keySet()) {
#             Collections.sort(adj.get(node), (a, b) -> a.getKey() - b.getKey());
#         }
#        
#         int[] signalReceivedAt = new int[n + 1];
#         Arrays.fill(signalReceivedAt, Integer.MAX_VALUE);
#       
#         DFS(signalReceivedAt, k, 0);
#       
#         int answer = Integer.MIN_VALUE;
#         for (int node = 1; node <= n; node++) {
#             answer = Math.max(answer, signalReceivedAt[node]);
#         }
#        
#         // Integer.MAX_VALUE signifies atleat one node is unreachable
#         return answer == Integer.MAX_VALUE ? -1 : answer;
#     }
# }

# V1
# IDEA : BFS
# https://leetcode.com/problems/network-delay-time/solution/
# class Solution {
#     // Adjacency list
#     Map<Integer, List<Pair<Integer, Integer>>> adj = new HashMap<>();
#
#     private void BFS(int[] signalReceivedAt, int sourceNode) {
#         Queue<Integer> q = new LinkedList<>();
#         q.add(sourceNode);
#        
#         // Time for starting node is 0
#         signalReceivedAt[sourceNode] = 0;
#        
#         while (!q.isEmpty()) {
#             int currNode = q.remove();
#            
#             if (!adj.containsKey(currNode)) {
#                 continue;
#             }
#            
#             // Broadcast the signal to adjacent nodes
#             for (Pair<Integer, Integer> edge : adj.get(currNode)) {
#                 int time = edge.getKey();
#                 int neighborNode = edge.getValue();
#                
#                 // Fastest signal time for neighborNode so far
#                 // signalReceivedAt[currNode] + time : 
#                 // time when signal reaches neighborNode
#                 int arrivalTime = signalReceivedAt[currNode] + time;
#                 if (signalReceivedAt[neighborNode] > arrivalTime) {
#                     signalReceivedAt[neighborNode] = arrivalTime;
#                     q.add(neighborNode);
#                 }
#             }
#         }
#     }
#    
#     public int networkDelayTime(int[][] times, int n, int k) {
#         // Build the adjacency list
#         for (int[] time : times) {
#             int source = time[0];
#             int dest = time[1];
#             int travelTime = time[2];
#            
#             adj.putIfAbsent(source, new ArrayList<>());
#             adj.get(source).add(new Pair(travelTime, dest));
#         }
#        
#         int[] signalReceivedAt = new int[n + 1];
#         Arrays.fill(signalReceivedAt, Integer.MAX_VALUE);
#       
#         BFS(signalReceivedAt, k);
#        
#         int answer = Integer.MIN_VALUE;
#         for (int i = 1; i <= n; i++) {
#             answer = Math.max(answer, signalReceivedAt[i]);
#         }
#        
#         // INT_MAX signifies atleat one node is unreachable
#         return answer == Integer.MAX_VALUE ? -1 : answer;
#     }
# }

# V1
# IDEA : Dijkstra
# https://leetcode.com/problems/network-delay-time/solution/
# class Solution {
#     // Adjacency list
#     Map<Integer, List<Pair<Integer, Integer>>> adj = new HashMap<>();
#    
#     private void dijkstra(int[] signalReceivedAt, int source, int n) {
#         Queue<Pair<Integer, Integer>> pq = new PriorityQueue<Pair<Integer,Integer>>
#             (Comparator.comparing(Pair::getKey));
#         pq.add(new Pair(0, source));
#        
#         // Time for starting node is 0
#         signalReceivedAt[source] = 0;
#       
#         while (!pq.isEmpty()) {
#             Pair<Integer, Integer> topPair = pq.remove();
#            
#             int currNode = topPair.getValue();
#             int currNodeTime = topPair.getKey();
#            
#             if (currNodeTime > signalReceivedAt[currNode]) {
#                 continue;
#             }
#            
#             if (!adj.containsKey(currNode)) {
#                 continue;
#             }
#            
#             // Broadcast the signal to adjacent nodes
#             for (Pair<Integer, Integer> edge : adj.get(currNode)) {
#                 int time = edge.getKey();
#                 int neighborNode = edge.getValue();
#               
#                 // Fastest signal time for neighborNode so far
#                 // signalReceivedAt[currNode] + time : 
#                 // time when signal reaches neighborNode
#                 if (signalReceivedAt[neighborNode] > currNodeTime + time) {
#                     signalReceivedAt[neighborNode] = currNodeTime + time;
#                     pq.add(new Pair(signalReceivedAt[neighborNode], neighborNode));
#                 }
#             }
#         }
#     }
#    
#     public int networkDelayTime(int[][] times, int n, int k) {
#         // Build the adjacency list
#         for (int[] time : times) {
#             int source = time[0];
#             int dest = time[1];
#             int travelTime = time[2];
#            
#             adj.putIfAbsent(source, new ArrayList<>());
#             adj.get(source).add(new Pair(travelTime, dest));
#         }
#        
#         int[] signalReceivedAt = new int[n + 1];
#         Arrays.fill(signalReceivedAt, Integer.MAX_VALUE);
#        
#         dijkstra(signalReceivedAt, k, n);
#       
#         int answer = Integer.MIN_VALUE;
#         for (int i = 1; i <= n; i++) {
#             answer = Math.max(answer, signalReceivedAt[i]);
#         }
#        
#         // INT_MAX signifies atleat one node is unreachable
#         return answer == Integer.MAX_VALUE ? -1 : answer;
#     }
# }

# V1 
# IDEA : Dijkstra
# https://blog.csdn.net/fuxuemingzhu/article/details/82862769
class Solution:
    def networkDelayTime(self, times, N, K):
        K -= 1
        nodes = collections.defaultdict(list)
        for u, v, w in times:
            nodes[u - 1].append((v - 1, w))
        dist = [float('inf')] * N
        dist[K] = 0
        done = set()
        for _ in range(N):
            smallest = min((d, i) for (i, d) in enumerate(dist) if i not in done)[1]
            for v, w in nodes[smallest]:
                if v not in done and dist[smallest] + w < dist[v]:
                    dist[v] = dist[smallest] + w
            done.add(smallest)
        return -1 if float('inf') in dist else max(dist)

# V1'
# IDEA : Floyd-Warshall (TLE)
# https://blog.csdn.net/fuxuemingzhu/article/details/82862769
class Solution:
    def networkDelayTime(self, times, N, K):
        d = [[float('inf')] * N for _ in range(N)]
        for time in times:
            u, v, w = time[0] - 1, time[1] - 1, time[2]
            d[u][v] = w
        for i in range(N):
            d[i][i] = 0
        for k in range(N):
            for i in range(N):
                for j in range(N):
                    d[i][j] = min(d[i][j], d[i][k] + d[k][j])
        return -1 if float('inf') in d[K - 1] else max(d[K - 1])

# V1''
# IDEA : Bellman-Ford (TLE)
# https://blog.csdn.net/fuxuemingzhu/article/details/82862769
class Solution:
    def networkDelayTime(self, times, N, K):
        dist = [float('inf')] * N
        dist[K - 1] = 0
        for i in range(N):
            for time in times:
                u = time[0] - 1
                v = time[1] - 1
                w = time[2]
                dist[v] = min(dist[v], dist[u] + w)
        return -1 if float('inf') in dist else max(dist)

# V1''''
# https://www.cnblogs.com/grandyang/p/8278115.html

# V2 
# Time:  O((|E| + |V|) * log|V|) = O(|E| * log|V|) by using binary heap,
#        if we can further to use Fibonacci heap, it would be O(|E| + |V| * log|V|)
# Space: O(|E| + |V|) = O(|E|)
import collections
import heapq
# Dijkstra's algorithm
class Solution(object):
    def networkDelayTime(self, times, N, K):
        """
        :type times: List[List[int]]
        :type N: int
        :type K: int
        :rtype: int
        """
        adj = [[] for _ in range(N)]
        for u, v, w in times:
            adj[u-1].append((v-1, w))

        result = 0
        lookup = set()
        best = collections.defaultdict(lambda: float("inf"))
        min_heap = [(0, K-1)]
        while min_heap and len(lookup) != N:
            result, u = heapq.heappop(min_heap)
            lookup.add(u)
            if best[u] < result:
                continue
            for v, w in adj[u]:
                if v in lookup: continue
                if result+w < best[v]:
                    best[v] = result+w
                    heapq.heappush(min_heap, (result+w, v))
        return result if len(lookup) == N else -1