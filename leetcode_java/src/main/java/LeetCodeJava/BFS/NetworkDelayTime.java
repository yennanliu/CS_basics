package LeetCodeJava.BFS;

// https://leetcode.com/problems/network-delay-time/description/

import java.util.*;

/**
 *
 * 743. Network Delay Time
 *
 * You are given a network of n nodes, labeled from 1 to n. You are also given times, a list of travel times as directed edges times[i] = (ui, vi, wi), where ui is the source node, vi is the target node, and wi is the time it takes for a signal to travel from source to target.
 *
 * We will send a signal from a given node k. Return the minimum time it takes for all the n nodes to receive the signal. If it is impossible for all the n nodes to receive the signal, return -1.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
 * Output: 2
 * Example 2:
 *
 * Input: times = [[1,2,1]], n = 2, k = 1
 * Output: 1
 * Example 3:
 *
 * Input: times = [[1,2,1]], n = 2, k = 2
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= k <= n <= 100
 * 1 <= times.length <= 6000
 * times[i].length == 3
 * 1 <= ui, vi <= n
 * ui != vi
 * 0 <= wi <= 100
 * All the pairs (ui, vi) are unique. (i.e., no multiple edges.)
 *
 *
 */
public class NetworkDelayTime {

    // V0
    // IDEA: Dijkstra (fixed by gpt)
    // ref video: https://neetcode.io/problems/network-delay-time
    public int networkDelayTime(int[][] times, int n, int k) {
        // Edge case: If no nodes exist
        if (times == null || times.length == 0 || n == 0) {
            return -1;
        }
        if (n == 1) {
            return 0; // If there's only one node, no travel time is needed.
        }

        Dijkstra dijkstra = new Dijkstra(times, n);
        return dijkstra.getShortestPath(k);
    }

    public class Dijkstra {
        // Attributes
        int[][] times;
        int n;

        // Constructor
        public Dijkstra(int[][] times, int n) {
            this.times = times;
            this.n = n;
        }

        // Method to find the shortest path using Dijkstra's algorithm
        public int getShortestPath(int k) {
            // Step 1: Build the graph
            Map<Integer, List<int[]>> edges = new HashMap<>();
            for (int[] time : times) {
                edges.computeIfAbsent(time[0], key -> new ArrayList<>()).add(new int[] { time[1], time[2] });
            }

            // Step 2: Initialize the min-heap priority queue (min distance first)
            PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            minHeap.offer(new int[] { 0, k }); // {travel time, node}

            // Step 3: Track visited nodes and the last time value
            Set<Integer> visited = new HashSet<>();
            int t = 0; // The current time to reach the last processed node

            // Step 4: Process the priority queue
            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                // w1 = current travel time
                int w1 = curr[0];
                // n1 = node
                int n1 = curr[1];

                // If the node has been visited, skip it
                if (visited.contains(n1)) {
                    continue;
                }

                // Mark the node as visited
                visited.add(n1);
                t = w1; // Update the last travel time

                // Step 5: Process all neighbors of the current node
                if (edges.containsKey(n1)) {
                    for (int[] next : edges.get(n1)) {
                        int n2 = next[0], w2 = next[1]; // n2 = neighbor node, w2 = travel time to neighbor
                        if (!visited.contains(n2)) {
                            minHeap.offer(new int[] { w1 + w2, n2 }); // Add neighbor to queue
                        }
                    }
                }
            }

            // Step 6: Check if all nodes are visited, and return the result
            return visited.size() == n ? t : -1; // Return the last time or -1 if not all nodes were visited
        }
    }

    // V0-1
    // IDEA: Dijkstra (fixed by gpt)
    public int networkDelayTime_0_1(int[][] times, int n, int k) {
        if (times == null || times.length == 0 || n == 0) {
            return -1;
        }
        if (n == 1) {
            return 0;
        }

        MyDijkstra dijkstra = new MyDijkstra(times, n);
        return dijkstra.getShortestPath(k);
    }

    public class MyDijkstra {

        Map<Integer, List<int[]>> graph = new HashMap<>();
        int n;

        public MyDijkstra(int[][] times, int n) {
            this.n = n;

            // Build graph with weights
            for (int[] edge : times) {
                int src = edge[0], dest = edge[1], weight = edge[2];
                graph.computeIfAbsent(src, x -> new ArrayList<>()).add(new int[] { dest, weight });
            }
        }

        public int getShortestPath(int k) {
            // Min-heap of [time, node]
            PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            minHeap.offer(new int[] { 0, k });

            Map<Integer, Integer> dist = new HashMap<>();

            while (!minHeap.isEmpty()) {
                int[] current = minHeap.poll();
                int time = current[0];
                int node = current[1];

                if (dist.containsKey(node))
                    continue;
                dist.put(node, time);

                if (graph.containsKey(node)) {
                    for (int[] neighbor : graph.get(node)) {
                        int nextNode = neighbor[0], weight = neighbor[1];
                        if (!dist.containsKey(nextNode)) {
                            minHeap.offer(new int[] { time + weight, nextNode });
                        }
                    }
                }
            }

            if (dist.size() != n)
                return -1;

      /**
       * Great question.
       *
       * In the context of the Network Delay Time problem,
       * the maxTime represents the total time it takes
       * for the last node to receive the signal.
       *
       * ⸻
       *
       * 💡 Why do we take the max of all distances?
       *
       * Dijkstra’s algorithm gives us the shortest time to reach every node
       * from the source node k. So:
       *
       * 	•	dist.get(node) tells you how long it takes for the signal
       *     	to reach node from k.
       *
       * 	•	But since the signal spreads in parallel,
       *     	the network is considered to have received the signal
       *       	only when the last node gets it.
       *
       * 	•	So we need the maximum among all the shortest times.
       *
       * ⸻
       *
       * 📊 Example:
       *
       * Input:
       * times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
       *
       * From node 2, we have:
       * 	•	Node 1 gets the signal in 1 unit
       * 	•	Node 3 gets it in 1 unit
       * 	•	Node 4 gets it via node 3 in 2 units
       *
       * dist = {1: 1, 2: 0, 3: 1, 4: 2}
       * (0 for node 2 because it’s the start node)
       *
       * The last node (node 4) receives the signal at 2 units, so we return max(dist.values()) = 2.
       *
       * ⸻
       *
       * ✅ Summary:
       *
       * We return maxTime because it answers the question:
       *
       * “How long until all nodes receive the signal?”
       *
       */
      int maxTime = 0;
            for (int t : dist.values()) {
                maxTime = Math.max(maxTime, t);
            }
            return maxTime;
        }
    }


    // V1-1
    // https://neetcode.io/problems/network-delay-time
    // IDEA: DFS
    public int networkDelayTime_1_1(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int[] time : times) {
            adj.computeIfAbsent(time[0],
                    x -> new ArrayList<>()).add(new int[]{time[1], time[2]});
        }

        Map<Integer, Integer> dist = new HashMap<>();
        for (int i = 1; i <= n; i++) dist.put(i, Integer.MAX_VALUE);

        dfs(k, 0, adj, dist);
        int res = Collections.max(dist.values());
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    private void dfs(int node, int time,
                     Map<Integer, List<int[]>> adj,
                     Map<Integer, Integer> dist) {
        if (time >= dist.get(node)) return;
        dist.put(node, time);
        if (!adj.containsKey(node)) return;
        for (int[] edge : adj.get(node)) {
            dfs(edge[0], time + edge[1], adj, dist);
        }
    }

    // V1-2
    // https://neetcode.io/problems/network-delay-time
    // IDEA: Floyd Warshall Algorithm
    public int networkDelayTime_1_2(int[][] times, int n, int k) {
        int inf = Integer.MAX_VALUE / 2;
        int[][] dist = new int[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], inf);
            dist[i][i] = 0;
        }

        for (int[] time : times) {
            int u = time[0] - 1, v = time[1] - 1, w = time[2];
            dist[u][v] = w;
        }

        for (int mid = 0; mid < n; mid++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    dist[i][j] = Math.min(dist[i][j],
                            dist[i][mid] + dist[mid][j]);

        int res = Arrays.stream(dist[k-1]).max().getAsInt();
        return res == inf ? -1 : res;
    }


    // V1-3
    // https://neetcode.io/problems/network-delay-time
    // IDEA:  Bellman Ford Algorithm
    public int networkDelayTime_1_3(int[][] times, int n, int k) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k - 1] = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int[] time : times) {
                int u = time[0] - 1, v = time[1] - 1, w = time[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                }
            }
        }

        int maxDist = Arrays.stream(dist).max().getAsInt();
        return maxDist == Integer.MAX_VALUE ? -1 : maxDist;
    }


    // V1-4
    // https://neetcode.io/problems/network-delay-time
    // IDEA:  Shortest Path Faster Algorithm
    public int networkDelayTime_1_4(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int i = 1; i <= n; i++) adj.put(i, new ArrayList<>());
        for (int[] time : times) {
            adj.get(time[0]).add(new int[] {time[1], time[2]});
        }
        Map<Integer, Integer> dist = new HashMap<>();
        for (int i = 1; i <= n; i++) dist.put(i, Integer.MAX_VALUE);
        dist.put(k, 0);

        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[] {k, 0});

        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int node = curr[0], time = curr[1];
            if (dist.get(node) < time) {
                continue;
            }
            for (int[] nei : adj.get(node)) {
                int nextNode = nei[0], weight = nei[1];
                if (time + weight < dist.get(nextNode)) {
                    dist.put(nextNode, time + weight);
                    q.offer(new int[] {nextNode, time + weight});
                }
            }
        }

        int res = Collections.max(dist.values());
        return res == Integer.MAX_VALUE ? -1 : res;
    }


    // V1-5
    // https://neetcode.io/problems/network-delay-time
    // IDEA: Dijkstra's Algorithm
    public int networkDelayTime_1_5(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> edges = new HashMap<>();
        for (int[] time : times) {
            edges.computeIfAbsent(time[0],
                    key -> new ArrayList<>()).add(new int[]{time[1], time[2]});
        }

        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(a -> a[0]));
        minHeap.offer(new int[]{0, k});

        Set<Integer> visited = new HashSet<>();
        int t = 0;
        while (!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int w1 = curr[0], n1 = curr[1];
            if (visited.contains(n1)) {
                continue;
            }
            visited.add(n1);
            t = w1;

            if (edges.containsKey(n1)) {
                for (int[] next : edges.get(n1)) {
                    int n2 = next[0], w2 = next[1];
                    if (!visited.contains(n2)) {
                        minHeap.offer(new int[]{w1 + w2, n2});
                    }
                }
            }
        }

        return visited.size() == n ? t : -1;
    }


    // V2
    // IDEA : Dijlstra's Algorithm
    // https://leetcode.com/problems/network-delay-time/solutions/2310813/dijkstra-s-algorithm-template-list-of-problems/
    /*
    Step 1: Create a Map of start and end nodes with weight
            1 -> {2,1},{3,2}
            2 -> {4,4},{5,5}
            3 -> {5,3}
            4 ->
            5 ->

    Step 2: create a result array where we will keep track the minimum distance to rech end of the node from start node

    Step 3: Create a Queue and add starting position with it's weight and add it's reachable distance with increament of own't weight plus a weight require to reach at the end node from start node.
            We keep adding and removing pairs from queue and updating result array as well.

    Step 4: find the maximum value from result array:

    */
    public int networkDelayTime_2(int[][] times, int n, int k) {

        //Step 1
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();

        for(int[] time : times) {
            int start = time[0];
            int end = time[1];
            int weight = time[2];

            map.putIfAbsent(start, new HashMap<>());
            map.get(start).put(end, weight);
        }

        // Step 2
        int[] dis = new int[n+1];
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[k] = 0;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{k,0});

        //Step 3:
        while(!queue.isEmpty()) {
            int[] cur = queue.poll();
            int curNode = cur[0];
            int curWeight = cur[1];

            for(int next : map.getOrDefault(curNode, new HashMap<>()).keySet()) {
                int nextweight = map.get(curNode).get(next);

                if(curWeight + nextweight < dis[next]) {
                    dis[next] = curWeight + nextweight;
                    queue.add(new int[]{next, curWeight + nextweight});
                }
            }
        }

        //Step 4:
        int res = 0;
        for(int i=1; i<=n; i++) {
            if(dis[i] > res) {
                res = Math.max(res, dis[i]);
            }
        }

        return res == Integer.MAX_VALUE ? -1 : res;
    }

    // V3
    // IDEA : ADOPT BFS (with PQ, wright, already very close to Dijlstra) (modified by gpt)
    /**
     *
     * You can solve the problem using BFS (Breadth-First Search),
     * but BFS is generally less efficient than Dijkstra’s
     * Algorithm when working with weighted graphs
     * because BFS doesn’t account for varying edge weights,
     * as it assumes uniform cost edges.
     * In a scenario where edges have different weights
     * (which is the case in the network delay problem),
     * Dijkstra’s algorithm is preferred since it optimizes
     * the process of finding the shortest paths.
     *
     * That said, you could still use a modified BFS approach
     * if you track the total cost at each step.
     * However, BFS would need to be adapted to handle the weight differences between edges.
     *
     * Here’s how you can do it:
     *
     * Steps for BFS with Edge Weights:
     *
     * 	1.	Track Time for Each Node: You would need to maintain a time array (or map) that stores the shortest time to reach each node from the starting node k.
     * 	2.	Priority Queue: Similar to Dijkstra’s, use a priority queue (min-heap) to always explore the node with the shortest accumulated time.
     * 	3.	Relaxation of Edges: At each step, you explore the neighbors of the current node, and if the new time to reach a neighbor is shorter than the previously recorded time, update it and continue the BFS from that neighbor.
     *
     *
     *
     * Differences from Standard BFS:
     *
     * 	•	Priority Queue: Normally, BFS uses a FIFO queue. Here, we mimic Dijkstra’s by always processing the node with the smallest current time.
     * 	•	Tracking Distance: We maintain an array dist to track the shortest time to reach each node, updating it as we explore the graph.
     *
     * Why Dijkstra’s Algorithm is Better:
     *
     * 	•	Dijkstra’s algorithm is naturally suited for graphs with different edge weights and ensures that you always find the shortest path by exploring the smallest distances first.
     * 	•	BFS would need these modifications (min-heap) to work efficiently in such cases, making it very similar to Dijkstra’s approach.
     *
     * Conclusion:
     *
     * While you can adapt BFS to handle this problem,
     * it ends up being very close to Dijkstra’s algorithm.
     * The standard BFS without adaptation would not give the correct solution
     * because it doesn’t account for edge weights,
     * which are crucial in this problem. Dijkstra is the better approach here.
     */
    public int networkDelayTime_3(int[][] times, int n, int k) {
        // Step 1: Build graph (adjacency list)
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] time : times) {
            graph.putIfAbsent(time[0], new ArrayList<>());
            graph.get(time[0]).add(new int[]{time[1], time[2]});
        }

        // Step 2: Track the minimum time to reach each node
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;  // Start node distance is 0

        // Step 3: Use a priority queue (min-heap) for BFS-like traversal
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{k, 0}); // {node, time}

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int node = current[0];
            int time = current[1];

            // Explore all neighbors
            if (!graph.containsKey(node)) {
                continue;
            }

            for (int[] neighbor : graph.get(node)) {
                int nextNode = neighbor[0];
                int travelTime = neighbor[1];
                int newTime = time + travelTime;

                // If we found a faster way to nextNode
                if (newTime < dist[nextNode]) {
                    dist[nextNode] = newTime;
                    queue.add(new int[]{nextNode, newTime});
                }
            }
        }

        // Step 4: Calculate the maximum time to reach all nodes
        int maxTime = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                return -1; // Not all nodes can be reached
            }
            maxTime = Math.max(maxTime, dist[i]);
        }

        return maxTime;
    }


    // V4_1
    // IDEA : Dijlstra's Algorithm V1
    // https://leetcode.com/problems/network-delay-time/submissions/1409037231/


    // V4_2
    // IDEA : Dijlstra's Algorithm V2
    // https://leetcode.com/problems/network-delay-time/submissions/1409037231/

    // V5
    // IDEA : Dijlstra
    // https://leetcode.com/problems/network-delay-time/submissions/1409038407/
    public int networkDelayTime_5(int[][] times, int n, int K) {
        int[][] graph = new int[n][n];
        for(int i = 0; i < n ; i++) Arrays.fill(graph[i], Integer.MAX_VALUE);
        for( int[] rows : times) graph[rows[0] - 1][rows[1] - 1] =  rows[2];

        int[] distance = new int[n];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[K - 1] = 0;

        boolean[] visited = new boolean[n];
        for(int i = 0; i < n ; i++){
            int v = minIndex(distance, visited);
            if(v == -1)continue;
            visited[v] = true;
            for(int j = 0; j < n; j++){
                if(graph[v][j] != Integer.MAX_VALUE){
                    int newDist = graph[v][j] + distance[v];
                    if(newDist < distance[j]) distance[j] = newDist;
                }
            }
        }
        int result = 0;
        for(int dist : distance){
            if(dist == Integer.MAX_VALUE) return -1;
            result = Math.max(result, dist);
        }
        return result;
    }

    private int minIndex(int[] distance, boolean[] visited){
        int min = Integer.MAX_VALUE, minIndex = -1;
        for(int i = 0; i < distance.length; i++){
            if(!visited[i] && distance[i] < min){
                min = distance[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

}
