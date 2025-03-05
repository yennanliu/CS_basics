# Dijkstra

> Algorithm get `minimum path` (for given point to any other points)


Dijkstra's algorithm is a famous algorithm used to solve the single-source shortest path problem for a graph with non-negative edge weights. It finds the shortest path from a starting node (source) to all other nodes in the graph. The algorithm ensures that once a node’s shortest distance is finalized, it will not change, which makes it very efficient for many graph-related problems.


- "importance" can only >= 0


- Key Concepts:
    - Graph: A collection of vertices (nodes) connected by edges (links), where
        each edge has a weight (cost, distance, etc.).

    - Non-negative weights: Dijkstra’s algorithm only works with graphs where
        the edge weights are `non-negative` (i.e., no negative weights).

    - Single-Source Shortest Path: The goal is to find the shortest path from a
         starting node to all other nodes in the graph.

- How Dijkstra's Algorithm Works:

1. Initialization:

    - Mark the distance to the source node as 0 (since the distance from the source to itself is zero).
    - Mark the distance to all other nodes as infinity (since they are initially unreachable).
    - Keep track of the visited nodes and nodes that still need to be processed.

2. Processing the Node with the Smallest Tentative Distance:

    - Start at the source node, and for each node, calculate the tentative distance to its neighbors through the current node.
    - If a shorter path to a neighboring node is found, update its tentative distance.
    - Mark the current node as "visited" and never revisit it again.

3. Repeat:

    - Select the unvisited node with the smallest tentative distance and mark it as visited.
    - Update the distances to its neighboring nodes.

4. Termination:

    - Repeat this process until all nodes have been visited, meaning the shortest distance to every node has been found.



- Algorithm Steps:
    1. Start at the source node. Initialize the tentative distance of the source to 0 and all other nodes to infinity.
    
    2. Visit the node with the smallest tentative distance (starting with the source).
    
    3. For each unvisited neighbor of this node:
        - Calculate the tentative distance to that neighbor (current node’s distance + weight of the edge).
        - If this tentative distance is smaller than the current stored distance, update the distance.
    
    4. Once all neighbors of the current node have been processed, mark the node as visited.
    
    5. Repeat the process with the node that has the smallest tentative distance among the unvisited nodes.
    
    6. When all nodes have been visited, the algorithm ends, and the shortest distances to all nodes from the source are finalized.



## 0) Concept  

### 0-1) Types
 - LC 743
 - LC 787
 - Lc 778

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Network Delay Time

```python
# LC 743 Network Delay Time
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
```

### 2-2) Cheapest Flights Within K Stops
```python
# LC 787 Cheapest Flights Within K Stops
# V1
# IDEA : Dijkstra
# https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/267200/Python-Dijkstra
# IDEA
# -> To implement Dijkstra, we need a priority queue to pop out the lowest weight node for next search. In this case, the weight would be the accumulated flight cost. So my node takes a form of (cost, src, k). cost is the accumulated cost, src is the current node's location, k is stop times we left as we only have at most K stops. I also convert edges to an adjacent list based graph g.
# -> Use a vis array to maintain visited nodes to avoid loop. vis[x] record the remaining steps to reach x with the lowest cost. If vis[x] >= k, then no need to visit that case (start from x with k steps left) as a better solution has been visited before (more remaining step and lower cost as heappopped beforehand). And we should initialize vis[x] to 0 to ensure visit always stop at a negative k.
# -> Once k is used up (k == 0) or vis[x] >= k, we no longer push that node x to our queue. Once a popped cost is our destination, we get our lowest valid cost.
# -> For Dijkstra, there is not need to maintain a best cost for each node since it's kind of greedy search. It always chooses the lowest cost node for next search. So the previous searched node always has a lower cost and has no chance to be updated. The first time we pop our destination from our queue, we have found the lowest cost to our destination.
import collections
import math
class Solution:
    def findCheapestPrice(self, n, flights, src, dst, K):
        graph = collections.defaultdict(dict)
        for s, d, w in flights:
            graph[s][d] = w
        pq = [(0, src, K+1)]
        vis = [0] * n
        while pq:
            w, x, k = heapq.heappop(pq)
            if x == dst:
                return w
            if vis[x] >= k:
                continue
            vis[x] = k
            for y, dw in graph[x].items():
                heapq.heappush(pq, (w+dw, y, k-1))
        return -1
```