// Graph API (OP)
// https://www.coursera.org/learn/algorithms-part2/lecture/4ZE6G/graph-api


public class Graph{
    private final int V;
    private Bag<Integer>[] adj;

    public Graph(int V){
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }

    public void addEdge(int v, int w){
        adj[v].add(w);
        adj[w].add(v);
    }

    public Iterable<Integer> adj(int v){
        // iterator for vertices adjacent to v
        return adj[v];
    }
}