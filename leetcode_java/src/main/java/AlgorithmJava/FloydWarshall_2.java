package AlgorithmJava;

public class FloydWarshall_2 {

    final static int INF = 99999; // A value representing infinity

    // Function to perform the Floyd-Warshall algorithm
    void floydWarshall(int[][] graph) {
        int V = graph.length;
        int[][] dist = new int[V][V];

        // Initialize the solution matrix same as input graph matrix
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
            }
        }

        // Main Floyd-Warshall algorithm
        for (int k = 0; k < V; k++) {
            // Pick all vertices as source one by one
            for (int i = 0; i < V; i++) {
                // Pick all vertices as destination for the above source
                for (int j = 0; j < V; j++) {
                    // If vertex k is on the shortest path from i to j, then update dist[i][j]
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        // Print the shortest distance matrix
        printSolution(dist);
    }

    // Utility function to print the solution
    void printSolution(int[][] dist) {
        int V = dist.length;
        System.out.println("Shortest distances between every pair of vertices:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (dist[i][j] == INF)
                    System.out.print("INF ");
                else
                    System.out.print(dist[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Example usage
    public static void main(String[] args) {
        int[][] graph = {
                {0, 5, INF, 10},
                {INF, 0, 3, INF},
                {INF, INF, 0, 1},
                {INF, INF, INF, 0}
        };

        FloydWarshall_2 fw = new FloydWarshall_2();
        fw.floydWarshall(graph);
    }

}
