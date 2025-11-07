package AlgorithmJava;

// offer by gemini

public class FloydWarshall_1 {

    // Define a large number to represent infinity (no path)
    // Use a value that, when added to another weight, won't cause Integer.MAX_VALUE overflow.
    private static final int INF = 100000000;

    /**
     * Implements the Floyd-Warshall algorithm to find all-pairs shortest paths.
     * @param graph The adjacency matrix representing the graph, where graph[i][j]
     * is the weight of the direct edge i -> j.
     * @return The distance matrix where matrix[i][j] is the shortest distance.
     */
    public int[][] floydWarshall(int[][] graph) {
        int V = graph.length;
        // Create a copy of the graph to serve as the distance matrix
        int[][] dist = new int[V][V];

        // 1. Initialization
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                // If an edge exists, use its weight.
                if (graph[i][j] != 0 || i == j) {
                    dist[i][j] = graph[i][j];
                }
                // If no edge exists and it's not the diagonal (i=j), set to INF
                else {
                    dist[i][j] = INF;
                }
            }
        }
        // Distance from a node to itself must be 0
        for (int i = 0; i < V; i++) {
            dist[i][i] = 0;
        }

        // 2. The Core Triple Loop (Dynamic Programming)
        // k is the intermediate vertex
        for (int k = 0; k < V; k++) {
            // i is the source vertex
            for (int i = 0; i < V; i++) {
                // j is the destination vertex
                for (int j = 0; j < V; j++) {

                    // Avoid overflow by checking if the intermediate path contains INF
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        // Relaxation Step: dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
                        if (dist[i][k] + dist[k][j] < dist[i][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }

        // Optional: Check for negative cycles (if dist[i][i] < 0)
        // You can add this logic here if needed.

        return dist;
    }

    // Example usage and helper method
    public static void main(String[] args) {
        // V = 4
        // Example Graph (0: no edge)
        int[][] graph = {
                {0, 5, INF, 10},
                {INF, 0, 3, INF},
                {INF, INF, 0, 1},
                {INF, INF, INF, 0}
        };

        FloydWarshall_1 fw = new FloydWarshall_1();
        int[][] shortestPaths = fw.floydWarshall(graph);

        System.out.println("Shortest distances between every pair of vertices:");
        printSolution(shortestPaths);
    }

    private static void printSolution(int[][] dist) {
        int V = dist.length;
        for (int i = 0; i < V; ++i) {
            for (int j = 0; j < V; ++j) {
                if (dist[i][j] == INF) {
                    System.out.print("INF\t");
                } else {
                    System.out.print(dist[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

}