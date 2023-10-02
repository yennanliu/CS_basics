package LeetCodeJava.Graph;

import LeetCodeJava.DataStructure.Node;

import java.util.*;

// https://leetcode.com/problems/clone-graph/
// NOTE !!! :  Node.val is unique for each node.
/**
 *  Constraints:
 *
 * - The number of nodes in the graph is in the range [0, 100].
 * - 1 <= Node.val <= 100
 * - Node.val is unique for each node.
 * - There are no repeated edges and no self-loops in the graph.
 * - The Graph is connected and all nodes can be visited starting from the given node.
 */


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

public class CloneGraph {

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

    // V0'
//    Node clonedNode = new Node();
//    public Node cloneGraph(Node node) {
//
//        if (node == null){
//            return node;
//        }
//
//        // build map : {node_val : node_neighbors}
//        Map<Integer, Set<Node>> map = new HashMap<>();
//        for (Node x : node.neighbors){
//            int _val = x.val;
//            List<Node> _neighbors = x.neighbors;
//            if (!map.containsKey(_val)){
//                map.put(_val, new HashSet<>());
//            }
//            for (Node y : _neighbors){
//                map.get(_val).add(y);
//            }
//        }
//
//        List<Integer> visited = new ArrayList<>();
//        // (status) 0 : not visited, 1 : visiting, 2 : visited
//        int status = 0;
//        _help(node, visited, map, status);
//        return this.clonedNode;
//    }
//
//    private void _help(Node node, List<Integer> visited, Map<Integer, Set<Node>> map, int status){
//
//        // all nodes are visited
//        if (visited.size() == map.keySet().size()){
//            return;
//        }
//
//        if (!visited.contains(node)){
//            this.clonedNode = node;
//            visited.add(node.val);
//            if (map.get(node).isEmpty()){
//                status = 2;
//                map.remove(node.val);
//            }
//        }
//
//        for (Node _node : map.get(node)){
//            // remove visiting node in map val
//            map.get(_node.val).remove(_node);
//            _help(_node, visited, map, 1);
//        }
//
//    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/clone-graph/editorial/
    private HashMap <Node, Node> visited = new HashMap <> ();
    public Node cloneGraph_1(Node node) {
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
            cloneNode.neighbors.add(cloneGraph_1(neighbor));
        }

        // NOTE !!! after dfs, we return final result
        return cloneNode;
    }

    // V2
    // IDEA : BFS
    // https://leetcode.com/problems/clone-graph/editorial/
    public Node cloneGraph_2(Node node) {
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
