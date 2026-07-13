# https://leetcode.com/problems/path-with-maximum-probability/description/

"""

1514. Path with Maximum Probability
Medium
Topics
premium lock icon
Companies
Hint
You are given an undirected weighted graph of n nodes (0-indexed), represented by an edge list where edges[i] = [a, b] is an undirected edge connecting the nodes a and b with a probability of success of traversing that edge succProb[i].

Given two nodes start and end, find the path with the maximum probability of success to go from start to end and return its success probability.

If there is no path from start to end, return 0. Your answer will be accepted if it differs from the correct answer by at most 1e-5.

 

Example 1:



Input: n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5,0.5,0.2], start = 0, end = 2
Output: 0.25000
Explanation: There are two paths from start to end, one having a probability of success = 0.2 and the other has 0.5 * 0.5 = 0.25.
Example 2:



Input: n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5,0.5,0.3], start = 0, end = 2
Output: 0.30000
Example 3:



Input: n = 3, edges = [[0,1]], succProb = [0.5], start = 0, end = 2
Output: 0.00000
Explanation: There is no path between 0 and 2.
 

Constraints:

2 <= n <= 10^4
0 <= start, end < n
start != end
0 <= a, b < n
a != b
0 <= succProb.length == edges.length <= 2*10^4
0 <= succProb[i] <= 1
There is at most one edge between every two nodes.

"""


# V0
# IDEA: Dijkstra (gpt)
# time = O(1) (unimplemented stub)
# space = O(1)
import heapq

class Solution(object):
    def maxProbability(self, n, edges, succProb, start_node, end_node):
        if start_node == end_node:
            return 1.0

        # Build graph: node -> [(neighbor, probability)]
        graph = {}

        for i in range(len(edges)):
            u, v = edges[i]
            p = succProb[i]

            graph.setdefault(u, []).append((v, p))
            graph.setdefault(v, []).append((u, p))

        # best[i] = maximum probability of reaching i
        best = [0.0] * n
        """
        NOTE !!! below
        """
        best[start_node] = 1.0

        # Max heap using negative probabilities
        pq = [(-1.0, start_node)]
        heapq.heapify(pq)

        while pq:
            neg_prob, cur_node = heapq.heappop(pq)
            prob = -neg_prob

            # Ignore stale entries
            """
            NOTE !!! below
            """
            if prob < best[cur_node]:
                continue

            if cur_node == end_node:
                return prob

            for next_node, edge_prob in graph.get(cur_node, []):
                new_prob = prob * edge_prob

                """
                NOTE !!! below
                """
                if new_prob > best[next_node]:
                    best[next_node] = new_prob
                    heapq.heappush(pq, (-new_prob, next_node))

        return 0.0


# V1-1
# IDEA: Dijkstra (gpt)
# build a graph + use a priority queue (Dijkstra).
# time = O(E log V), E = len(edges), V = n
# space = O(V + E)
import heapq
class Solution(object):
    def maxProbability(self, n, edges, succProb, start_node, end_node):

        if start_node == end_node:
            return 1.0

        graph = {}

        for i in range(len(edges)):
            u, v = edges[i]
            p = succProb[i]

            graph.setdefault(u, []).append((v, p))
            graph.setdefault(v, []).append((u, p))

        # max heap (implemented using negative probabilities)
        pq = [(-1.0, start_node)]


        """
        NOTE !!!


        we need `best[]`  setting !!!
        """

        """ 
        Q :  Why we need `best[]` 


        ->


         Suppose

            ```
            0 --0.5--> 1
             \         ^
              \       /
               0.9   0.8
                 \   /
                   2
            ```

            From node `0`:

            ```
            0 -> 1 = 0.5
            ```

            Later you discover

            ```
            0 -> 2 -> 1

            0.9 × 0.8 = 0.72
            ```

            which is better.

            If you don't store the best probability for each node, you'll either:

            * process the same node many unnecessary times, or
            * miss a better path.

        """
        best = [0.0] * n
        best[start_node] = 1.0

        while pq:

            neg_prob, node = heapq.heappop(pq)
            prob = -neg_prob

            if node == end_node:
                return prob

            # stale (陳舊) entry
            if prob < best[node]:
                continue

            for nxt, edge_prob in graph.get(node, []):

                new_prob = prob * edge_prob

                if new_prob > best[nxt]:
                    best[nxt] = new_prob
                    heapq.heappush(pq, (-new_prob, nxt))

        return 0.0



# V1-2
# IDEA: Dijkstra (gemini)
# build a graph + use a priority queue (Dijkstra).
# time = O(E log V), E = len(edges), V = n
# space = O(V + E)
import collections
import heapq

class Solution(object):
    def maxProbability(self, n, edges, succProb, start_node, end_node):
        """
        :type n: int
        :type edges: List[List[int]]
        :type succProb: List[float]
        :type start_node: int
        :type end_node: int
        :rtype: float
        """
        # 1. Build the Graph cleanly using defaultdict
        graph = collections.defaultdict(list)
        for i in range(len(edges)):
            u = edges[i][0]
            v = edges[i][1]
            prob = succProb[i]
            
            graph[u].append((v, prob))
            graph[v].append((u, prob))
            
        # 2. Track the maximum probability found to reach each node
        # Initialize all to 0.0
        max_probs = [0.0] * n
        max_probs[start_node] = 1.0
        
        # 3. Initialize Max-Heap
        # Store tuples of (probability, node). 
        # We make the probability negative to trick Python's Min-Heap into acting like a Max-Heap!
        pq = [(-1.0, start_node)]
        
        while pq:
            # Pop the path with the highest probability
            cur_prob, cur_node = heapq.heappop(pq)
            
            # Flip it back to a positive fraction
            cur_prob = -cur_prob
            
            # If we reached the end, we are guaranteed this is the max probability
            if cur_node == end_node:
                return cur_prob
                
            # If we already found a better way to reach this node earlier, skip processing it again
            if cur_prob < max_probs[cur_node]:
                continue
                
            # Check all neighbors
            for nxt_node, edge_prob in graph[cur_node]:
                new_prob = cur_prob * edge_prob
                
                # If this new path gives us a higher probability to reach 'nxt_node', update and push
                if new_prob > max_probs[nxt_node]:
                    max_probs[nxt_node] = new_prob
                    heapq.heappush(pq, (-new_prob, nxt_node))
                    
        # If we exit the loop, the end_node is unreachable
        return 0.0


# V2-1
# IDEA: Bellman-Ford Algorithm
# https://leetcode.com/problems/path-with-maximum-probability/editorial/
# time = O(V * E), V = n, E = len(edges)
# space = O(V)
class Solution:
    def maxProbability(
        self,
        n: int,
        edges: List[List[int]],
        succProb: List[float],
        start: int,
        end: int,
    ) -> float:
        max_prob = [0] * n
        max_prob[start] = 1

        for i in range(n - 1):
            # If there is no larger probability found during an entire round of updates,
            # stop the update process.
            has_update = 0
            for j in range(len(edges)):
                u, v = edges[j]
                path_prob = succProb[j]
                if max_prob[u] * path_prob > max_prob[v]:
                    max_prob[v] = max_prob[u] * path_prob
                    has_update = 1
                if max_prob[v] * path_prob > max_prob[u]:
                    max_prob[u] = max_prob[v] * path_prob
                    has_update = 1
            if not has_update:
                break

        return max_prob[end]


# V2-2
# IDEA:  Shortest Path Faster Algorithm
# https://leetcode.com/problems/path-with-maximum-probability/editorial/
# time = O(V * E) worst case, V = n, E = len(edges)
# space = O(V + E)
class Solution:
    def maxProbability(
        self,
        n: int,
        edges: List[List[int]],
        succProb: List[float],
        start: int,
        end: int,
    ) -> float:
        graph = defaultdict(list)
        for i, (a, b) in enumerate(edges):
            graph[a].append([b, succProb[i]])
            graph[b].append([a, succProb[i]])

        max_prob = [0.0] * n
        max_prob[start] = 1.0

        queue = deque([start])
        while queue:
            cur_node = queue.popleft()
            for nxt_node, path_prob in graph[cur_node]:

                # Only update max_prob[nxt_node] if the current path increases
                # the probability of reach nxt_node.
                if max_prob[cur_node] * path_prob > max_prob[nxt_node]:
                    max_prob[nxt_node] = max_prob[cur_node] * path_prob
                    queue.append(nxt_node)

        return max_prob[end]

# V2-3
# IDEA: Dijkstra's Algorithm
# https://leetcode.com/problems/path-with-maximum-probability/editorial/
# time = O(E log V), E = len(edges), V = n
# space = O(V + E)
class Solution:
    def maxProbability(
        self,
        n: int,
        edges: List[List[int]],
        succProb: List[float],
        start: int,
        end: int,
    ) -> float:
        graph = defaultdict(list)
        for i, (u, v) in enumerate(edges):
            graph[u].append((v, succProb[i]))
            graph[v].append((u, succProb[i]))

        max_prob = [0.0] * n
        max_prob[start] = 1.0

        pq = [(-1.0, start)]
        while pq:
            cur_prob, cur_node = heapq.heappop(pq)
            if cur_node == end:
                return -cur_prob

            # Mark the node as processed by clearing its adjacency list
            if graph[cur_node]:  # Only clear if the list is not already empty
                for nxt_node, path_prob in graph[cur_node]:
                    if -cur_prob * path_prob > max_prob[nxt_node]:
                        max_prob[nxt_node] = -cur_prob * path_prob
                        heapq.heappush(pq, (-max_prob[nxt_node], nxt_node))
                graph[
                    cur_node
                ].clear()  # Clear the adjacency list after processing

        return 0.0
