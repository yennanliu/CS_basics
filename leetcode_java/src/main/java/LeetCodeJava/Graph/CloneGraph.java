package LeetCodeJava.Graph;

import LeetCodeJava.DataStructure.Node;

import java.util.*;

// https://leetcode.com/problems/clone-graph/

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
    Node clonedNode = new Node();
    public Node cloneGraph(Node node) {

        if (node == null){
            return node;
        }

        // build map : {node_val : node_neighbors}
        Map<Integer, Set<Node>> map = new HashMap<>();
        for (Node x : node.neighbors){
            int _val = x.val;
            List<Node> _neighbors = x.neighbors;
            if (!map.containsKey(_val)){
                map.put(_val, new HashSet<>());
            }
            for (Node y : _neighbors){
                map.get(_val).add(y);
            }
        }

        List<Integer> visited = new ArrayList<>();
        // (status) 0 : not visited, 1 : visiting, 2 : visited
        int status = 0;
        _help(node, visited, map, status);
        return this.clonedNode;
    }

    private void _help(Node node, List<Integer> visited, Map<Integer, Set<Node>> map, int status){

        // all nodes are visited
        if (visited.size() == map.keySet().size()){
            return;
        }

        if (!visited.contains(node)){
            this.clonedNode = node;
            visited.add(node.val);
            if (map.get(node).isEmpty()){
                status = 2;
                map.remove(node.val);
            }
        }

        for (Node _node : map.get(node)){
            // remove visiting node in map val
            map.get(_node.val).remove(_node);
            _help(_node, visited, map, 1);
        }

    }

    public static void main(String[] args) {
        System.out.println(new Node());
    }

}
