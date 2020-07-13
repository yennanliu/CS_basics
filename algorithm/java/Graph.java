// Graph basic API (OP)
// https://www.coursera.org/learn/algorithms-part2/lecture/4ZE6G/graph-api


public class Graph {

    public static int degree(Graph G, int v){
        int degree = 0;
        for (int w : G.adj(v)) degree++;
        return degree; 
    }

    public static int maxDegree(Graph G){
        int max = 0;
        for (int v = 0; v < G.V(); v++)
            if (degree(G, v) > max)
                max = degree(G, v);
        return max; 
    }

    public static double averageDegree(Graph G){
        return 2.0 * G.E() / G.V();
    }


    public static int numberOfSelfLoops(Graph G){
        int count = 0;
        for (int v = 0; v < G.V(); v++)
            for (int w : G.adj(v))
                if (v == w) count ++; 
        return count / 2 ;
    }

}