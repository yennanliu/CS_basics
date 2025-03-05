package AlgorithmJava;

import java.util.*;

/**
 *  Dijkstra V2 (gpt)
 *
 *  // LC 743
 *
 *   Min Heap + BFS
 *
 */

public class Dijkstra_2 {
    // Attributes
    int[][] times;
    int n;

    // Constructor
    public Dijkstra_2(int[][] times, int n) {
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
            int w1 = curr[0], n1 = curr[1]; // w1 = current travel time, n1 = node

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