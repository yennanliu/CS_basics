package AlgorithmJava;

/**
 *  Dijkstra V1 (gpt)
 *
 *   Min Heap + BFS
 *
 */

import java.util.*;

class Dijkstra_1 {
    private int vertices;
    private List<List<Node>> adjList;

    public Dijkstra_1(int vertices) {
        this.vertices = vertices;
        adjList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(new Node(destination, weight));
        adjList.get(destination).add(new Node(source, weight)); // if it's an undirected graph
    }

    public void dijkstra(int start) {
        // Priority queue to store vertex with its shortest distance from the start
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));

        // Array to hold the shortest distance from start to each vertex
        int[] distances = new int[vertices];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[start] = 0;

        // Add the start node to the priority queue
        pq.add(new Node(start, 0));

        while (!pq.isEmpty()) {
            // Extract the node with the minimum distance
            Node currentNode = pq.poll();
            int u = currentNode.vertex;

            // Explore neighbors of the current node
            for (Node neighbor : adjList.get(u)) {
                int v = neighbor.vertex;
                int weight = neighbor.distance;

                // If a shorter path to neighbor is found
                if (distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    pq.add(new Node(v, distances[v]));
                }
            }
        }

        // Output the shortest distances from the start node to all other nodes
        printSolution(distances);
    }

    private void printSolution(int[] distances) {
        System.out.println("Vertex\tDistance from Source");
        for (int i = 0; i < vertices; i++) {
            System.out.println(i + "\t" + (distances[i] == Integer.MAX_VALUE ? "INF" : distances[i]));
        }
    }

    static class Node {
        int vertex, distance;

        public Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public static void main(String[] args) {
        Dijkstra_1 graph = new Dijkstra_1(9);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 7, 8);
        graph.addEdge(1, 2, 8);
        graph.addEdge(1, 7, 11);
        graph.addEdge(2, 3, 7);
        graph.addEdge(2, 5, 4);
        graph.addEdge(2, 8, 2);
        graph.addEdge(3, 4, 9);
        graph.addEdge(3, 5, 14);
        graph.addEdge(4, 5, 10);
        graph.addEdge(5, 6, 2);
        graph.addEdge(6, 7, 1);
        graph.addEdge(6, 8, 6);
        graph.addEdge(7, 8, 7);

        // Run Dijkstra's algorithm starting from vertex 0
        graph.dijkstra(0);
    }
}
