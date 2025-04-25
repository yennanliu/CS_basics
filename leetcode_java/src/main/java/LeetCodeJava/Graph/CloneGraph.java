package LeetCodeJava.Graph;

import LeetCodeJava.DataStructure.Node;

import java.util.*;

// https://leetcode.com/problems/clone-graph/
// NOTE !!! :  Node.val is unique for each node.
/**
 * 133. Clone Graph
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a reference of a node in a connected undirected graph.
 *
 * Return a deep copy (clone) of the graph.
 *
 * Each node in the graph contains a value (int) and a list (List[Node]) of its neighbors.
 *
 * class Node {
 *     public int val;
 *     public List<Node> neighbors;
 * }
 *
 *
 * Test case format:
 *
 * For simplicity, each node's value is the same as the node's index (1-indexed). For example, the first node with val == 1, the second node with val == 2, and so on. The graph is represented in the test case using an adjacency list.
 *
 * An adjacency list is a collection of unordered lists used to represent a finite graph. Each list describes the set of neighbors of a node in the graph.
 *
 * The given node will always be the first node with val = 1. You must return the copy of the given node as a reference to the cloned graph.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
 * Output: [[2,4],[1,3],[2,4],[1,3]]
 * Explanation: There are 4 nodes in the graph.
 * 1st node (val = 1)'s neighbors are 2nd node (val = 2) and 4th node (val = 4).
 * 2nd node (val = 2)'s neighbors are 1st node (val = 1) and 3rd node (val = 3).
 * 3rd node (val = 3)'s neighbors are 2nd node (val = 2) and 4th node (val = 4).
 * 4th node (val = 4)'s neighbors are 1st node (val = 1) and 3rd node (val = 3).
 * Example 2:
 *
 *
 * Input: adjList = [[]]
 * Output: [[]]
 * Explanation: Note that the input contains one empty list. The graph consists of only one node with val = 1 and it does not have any neighbors.
 * Example 3:
 *
 * Input: adjList = []
 * Output: []
 * Explanation: This an empty graph, it does not have any nodes.
 *
 *
 * Constraints:
 *
 * The number of nodes in the graph is in the range [0, 100].
 * 1 <= Node.val <= 100
 * Node.val is unique for each node.
 * There are no repeated edges and no self-loops in the graph.
 * The Graph is connected and all nodes can be visited starting from the given node.
 *
 */
/**
 *  Constraints:
 *
 * - The number of nodes in the graph is in the range [0, 100].
 * - 1 <= Node.val <= 100
 * - Node.val is unique for each node.
 * - There are no repeated edges and no self-loops in the graph.
 * - The Graph is connected and all nodes can be visited starting from the given node.
 */


public class CloneGraph {

    /*
    // Definition for a Node.
    class Node {
        public int val;
        public List<Node> neighbors;
        public Node() {
            val = 0;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }
    */

    // V0
    // IDEA : DFS
    public Node cloneGraph(Node node) {

        // init hashmap
        // NOTE : hashmap form : <Integer, Node>
        HashMap<Integer, Node> _visited = new HashMap<>();
        return _clone(_visited, node);
    }

    private Node _clone(HashMap<Integer, Node> visited, Node node){

        // NOTE !!! handle edge case, if node is null, return itself directly
        if (node == null) {
            return node;
        }

        int cur_val = node.val;

        //  case 1) already visited, then return hashmap 's value (Node type) directly
        if (visited.containsKey(cur_val)){
            return visited.get(cur_val);
        }

        // NOTE !!! we init copied node as below
        Node copiedNode = new Node(node.val, new ArrayList());
        visited.put(cur_val, copiedNode);

        // case 2) node is NOT visited yet, we go through all its neighbors,
        for (Node _node : node.neighbors){
            // NOTE !!! op here : we add new copied node to copiedNode.neighbors,
            //          (e.g. : copiedNode.neighbors.add)
            //          instead of return it directly
            copiedNode.neighbors.add(_clone(visited, _node));
        }

        // NOTE !!! we return copiedNode as final step in code
        //         -> once code reach here, means all after all recursive call are completed
        //         -> copiedNode should have all collected nodes
        return copiedNode;
    }

    // V0-1
    // IDEA: DFS (fixed by gpt)
    Map<Node, Node> visited_0_1 = new HashMap<>();

    public Node cloneGraph_0_1(Node node) {
        if (node == null)
            return null;
        return dfs(node);
    }

    private Node dfs(Node node) {
        // If already cloned
        if (visited_0_1.containsKey(node)) {
            return visited_0_1.get(node);
        }

        // Clone the node
        Node clone = new Node(node.val);
        visited_0_1.put(node, clone); // Mark this node as cloned

        // Clone neighbors recursively
        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(dfs(neighbor));
        }

        return clone;
    }

    // V1-1
    // https://neetcode.io/problems/clone-graph
    // IDEA: DFS
    public Node cloneGraph_1_1(Node node) {
        Map<Node, Node> oldToNew = new HashMap<>();

        return dfs(node, oldToNew);
    }

    private Node dfs(Node node, Map<Node, Node> oldToNew) {
        if (node == null) {
            return null;
        }

        if (oldToNew.containsKey(node)) {
            return oldToNew.get(node);
        }

        Node copy = new Node(node.val);
        oldToNew.put(node, copy);

        for (Node nei : node.neighbors) {
            copy.neighbors.add(dfs(nei, oldToNew));
        }

        return copy;
    }

    // V1-2
    // https://neetcode.io/problems/clone-graph
    // IDEA: BFS
    public Node cloneGraph_1_2(Node node) {
        if (node == null) return null;
        Map<Node, Node> oldToNew = new HashMap<>();
        Queue<Node> q = new LinkedList<>();
        oldToNew.put(node, new Node(node.val));
        q.add(node);

        while (!q.isEmpty()) {
            Node cur = q.poll();
            for (Node nei : cur.neighbors) {
                if (!oldToNew.containsKey(nei)) {
                    oldToNew.put(nei, new Node(nei.val));
                    q.add(nei);
                }
                oldToNew.get(cur).neighbors.add(oldToNew.get(nei));
            }
        }
        return oldToNew.get(node);
    }

    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/clone-graph/editorial/
    private HashMap <Node, Node> visited = new HashMap <> ();
    public Node cloneGraph_2(Node node) {
        if (node == null) {
            return node;
        }

        // If the node was already visited before.
        // Return the clone from the visited dictionary.
        if (visited.containsKey(node)) {
            return visited.get(node);
        }

        // Create a clone for the given node.
        // Note that we don't have cloned neighbors as of now, hence [].
        Node cloneNode = new Node(node.val, new ArrayList());
        // The key is original node and value being the clone node.
        visited.put(node, cloneNode);

        // Iterate through the neighbors to generate their clones
        // and prepare a list of cloned neighbors to be added to the cloned node.
        for (Node neighbor: node.neighbors) {
            cloneNode.neighbors.add(cloneGraph_2(neighbor));
        }

        // NOTE !!! after dfs, we return final result
        return cloneNode;
    }

    // V3
    // IDEA : BFS
    // https://leetcode.com/problems/clone-graph/editorial/
    public Node cloneGraph_3(Node node) {
        if (node == null) {
            return node;
        }

        // Hash map to save the visited node and it's respective clone
        // as key and value respectively. This helps to avoid cycles.
        HashMap<Node, Node> visited = new HashMap();

        // Put the first node in the queue
        LinkedList<Node> queue = new LinkedList<Node> ();
        queue.add(node);
        // Clone the node and put it in the visited dictionary.
        visited.put(node, new Node(node.val, new ArrayList()));

        // Start BFS traversal
        while (!queue.isEmpty()) {
            // Pop a node say "n" from the front of the queue.
            Node n = queue.remove();
            // Iterate through all the neighbors of the node "n"
            for (Node neighbor: n.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    // Clone the neighbor and put in the visited, if not present already
                    visited.put(neighbor, new Node(neighbor.val, new ArrayList()));
                    // Add the newly encountered node to the queue.
                    queue.add(neighbor);
                }
                // Add the clone of the neighbor to the neighbors of the clone node "n".
                visited.get(n).neighbors.add(visited.get(neighbor));
            }
        }

        // Return the clone of the node from visited.
        return visited.get(node);
    }

}
