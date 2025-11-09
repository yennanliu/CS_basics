package dev;

public class FloydWarshallTest1 {

     // test-1

    public class MyFloydWarshall1{
        // attr
        int n;
        int[][] edges;

        // constructor
        // edges: [u, v, weight]
        public MyFloydWarshall1(int n, int[][] edges){
            this.n = n;
            this.edges = edges;
        }

        public int[][] run(){
            int[][] dist = new int[n][n];
            // ???
            for(int i = 0; i < n; i++){
                // /??
                //dist[i] = Integer.MAX_VALUE; // ????
                for(int j = 0; j < n; j++){
                    dist[i][j] = Integer.MAX_VALUE; // ????
                }
            }

            // add edges
            for(int[] x: edges){
                int u = x[0];
                int v = x[1];
                int weight = x[2];
                dist[u][v] = weight;
            }

            // # Floyd-Warshall: try all intermediate vertices
            for(int k = 0; k < n; k++){
                for(int i = 0; i < n; k++){
                    for(int j = 0; j < n; k++){
                        // NOTE !!!
                        if(dist[i][k] + dist[k][j] < dist[i][j]){
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }

            return dist;
        }

    }




}

