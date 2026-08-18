import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 *  DEPTH-FIRST ORDER -- and topological sort via reverse post-order
 *
 *  Run DFS over a DIRECTED graph and record when each vertex is
 *  finished (post-order). Reverse that list and you have a TOPOLOGICAL
 *  SORT: every edge v -> w points forwards in the ordering.
 *
 *      0 -> 1 -> 3
 *      |         ^
 *      v         |
 *      2 --------+
 *
 *      post-order         3, 1, 2, 0     (a vertex is added only after
 *                                         everything it can reach)
 *      reverse post-order 0, 2, 1, 3     <- a valid topological order
 *
 *  WHY REVERSING WORKS: a vertex is pushed only once every vertex
 *  reachable from it has already been pushed. So in the post-order
 *  list, v always appears AFTER everything it points to -- reversing
 *  puts it before them. A stack gives the reversal for free.
 *
 *  THE PRECONDITION: this is only a topological order if the graph is a
 *  DAG. Reverse post-order still produces *an* ordering on a cyclic
 *  graph, just not a valid one, so {@link #hasCycle()} is checked here
 *  rather than left implicit.
 *
 *  Time  : O(V + E)  -- each vertex marked once, each edge followed once
 *  Space : O(V)      -- marks, the output stack, and the recursion depth
 *
 *  See also leetcode_java/.../TopologicalSort.java for Kahn's algorithm,
 *  the BFS/in-degree formulation of the same problem.
 *  Reference: https://www.coursera.org/learn/algorithms-part2/lecture/RAMNS/topological-sort
 */
public class DepthFirstOrder {

    private final boolean[] marked;        // marked[v] = has DFS reached v?
    private final boolean[] onStack;       // onStack[v] = is v on the current DFS path?
    private final Deque<Integer> reversePost;
    private boolean hasCycle;

    public DepthFirstOrder(Digraph g) {
        marked = new boolean[g.V()];
        onStack = new boolean[g.V()];
        reversePost = new ArrayDeque<>();

        // start a DFS from every unvisited vertex, so disconnected
        // parts of the graph are covered too
        for (int v = 0; v < g.V(); v++) {
            if (!marked[v]) {
                dfs(g, v);
            }
        }
    }

    private void dfs(Digraph g, int v) {
        marked[v] = true;
        onStack[v] = true;

        for (int w : g.adj(v)) {
            if (!marked[w]) {
                dfs(g, w);
            } else if (onStack[w]) {
                // w is an ancestor on the current path -> back edge -> cycle
                hasCycle = true;
            }
        }

        onStack[v] = false;
        // pushed only AFTER every vertex reachable from v is done;
        // pushing onto a stack is what reverses the post-order
        reversePost.push(v);
    }

    /** Vertices in reverse DFS post-order. A topological order when the graph is a DAG. */
    public Iterable<Integer> reversePost() {
        return new ArrayList<>(reversePost);
    }

    /** True if the digraph contains a directed cycle. */
    public boolean hasCycle() {
        return hasCycle;
    }

    /** A topological order, or null if none exists (the graph has a cycle). */
    public Iterable<Integer> topologicalOrder() {
        return hasCycle ? null : reversePost();
    }

    /** Minimal directed graph, adjacency-list representation. */
    public static class Digraph {
        private final int V;
        private final List<List<Integer>> adj;

        public Digraph(int V) {
            this.V = V;
            this.adj = new ArrayList<>(V);
            for (int v = 0; v < V; v++) {
                adj.add(new ArrayList<>());
            }
        }

        public int V() {
            return V;
        }

        /** Add the DIRECTED edge v -> w. Recorded once, unlike an undirected edge. */
        public void addEdge(int v, int w) {
            adj.get(v).add(w);
        }

        public Iterable<Integer> adj(int v) {
            return adj.get(v);
        }
    }

    public static void main(String[] args) {
        //  0 -> 1 -> 3
        //  |         ^
        //  v         |
        //  2 --------+
        Digraph dag = new Digraph(4);
        dag.addEdge(0, 1);
        dag.addEdge(0, 2);
        dag.addEdge(1, 3);
        dag.addEdge(2, 3);

        DepthFirstOrder order = new DepthFirstOrder(dag);
        assertThat(!order.hasCycle(), "a DAG has no cycle");

        List<Integer> topo = new ArrayList<>();
        order.topologicalOrder().forEach(topo::add);
        assertThat(topo.size() == 4, "every vertex appears exactly once");
        assertThat(topo.get(0) == 0, "0 has no incoming edges, so it comes first");
        assertThat(topo.get(3) == 3, "3 has no outgoing edges, so it comes last");

        // the defining property: every edge points forwards in the order
        assertThat(before(topo, 0, 1) && before(topo, 0, 2)
                && before(topo, 1, 3) && before(topo, 2, 3),
                "every edge v -> w has v before w");

        // a cycle is detected, and no ordering is offered
        Digraph cyclic = new Digraph(3);
        cyclic.addEdge(0, 1);
        cyclic.addEdge(1, 2);
        cyclic.addEdge(2, 0);
        DepthFirstOrder cycleCheck = new DepthFirstOrder(cyclic);
        assertThat(cycleCheck.hasCycle(), "0 -> 1 -> 2 -> 0 is a cycle");
        assertThat(cycleCheck.topologicalOrder() == null, "no topological order exists");

        // a self-loop is a cycle too
        Digraph selfLoop = new Digraph(1);
        selfLoop.addEdge(0, 0);
        assertThat(new DepthFirstOrder(selfLoop).hasCycle(), "a self-loop is a cycle");

        // disconnected vertices are still covered
        DepthFirstOrder isolated = new DepthFirstOrder(new Digraph(3));
        List<Integer> all = new ArrayList<>();
        isolated.reversePost().forEach(all::add);
        assertThat(all.size() == 3, "vertices with no edges are still ordered");

        System.out.println("topological order: " + Arrays.toString(topo.toArray()));
        System.out.println("Success.");
    }

    private static boolean before(List<Integer> order, int v, int w) {
        return order.indexOf(v) < order.indexOf(w);
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
