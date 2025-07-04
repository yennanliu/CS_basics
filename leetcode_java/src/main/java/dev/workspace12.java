package dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class workspace12 {

    // LC 133
    // 9.55 - 10.05 am
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

    public Node copiedNode;

    public Node cloneGraph(Node node) {
        // edge
        if(node == null){
            return null;
        }
        if(node.neighbors == null){
            return node;
        }

        //copiedNode.val = node.val;

        // dfs
       // cloneHelper(node);

        //return copiedNode; // ???
        return  cloneHelper2(node);
    }

  HashMap<Integer, Node> visited = new HashMap<>();

    private Node cloneHelper2(Node node){
        // edge
        if(node == null){
            return null;
        }

        // get current val
        int val = node.val;

        if(visited.containsKey(val)){
            return visited.get(val); // ???
        }

        // update `visited`
        visited.put(val, node);

        // init copied node
        Node copiedNode = new Node(val, new ArrayList<>());

        // loop over neighbors
        for(Node x: node.neighbors){

            copiedNode.neighbors.add(cloneHelper2(x));
        }

        return copiedNode;
    }



    private Node cloneHelper(Node node){
        // edge
        if(node == null){
            return null;
        }

        // copy val
        copiedNode.val = node.val;

        // copy neighbors
        copiedNode.neighbors = node.neighbors; // ???

        // loop over `neighbors`
        for(Node x: node.neighbors){
            cloneHelper(x);
        }

        return node;
    }


}

