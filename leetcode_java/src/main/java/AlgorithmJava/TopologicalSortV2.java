package AlgorithmJava;

import java.util.*;

public class TopologicalSortV2 {

    // V2
    // Function to perform Topological Sort (GPT)
    public static List<Integer> topologicalSort(int numNodes, List<List<Integer>> edges) {
        // Step 1: Build the graph and calculate in-degrees
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] inDegree = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (List<Integer> edge : edges) {
            int from = edge.get(0);
            int to = edge.get(1);
            graph.get(from).add(to);
            inDegree[to]++;
        }

        // Step 2: Initialize a queue with nodes that have in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numNodes; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();

        // Step 3: Process the nodes in topological order
        while (!queue.isEmpty()) {
            int current = queue.poll();
            topologicalOrder.add(current);

            for (int neighbor : graph.get(current)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // If topologicalOrder does not contain all nodes, there was a cycle in the graph
        if (topologicalOrder.size() != numNodes) {
            throw new IllegalArgumentException("The graph has a cycle, so topological sort is not possible.");
        }

        return topologicalOrder;
    }
}
