package dev;

import java.util.*;

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

    //public Node copiedNode;

    // BFS
    public Node cloneGraph(Node node) {
        // edge
        if(node == null){
            return null;
        }
        if(node.neighbors == null){
            return node;
        }

        //Node copiedNode = null;

        Queue<Node> q = new LinkedList<>();
        q.add(node);

        // visited
        //  HashMap<Integer, Node> visited = new HashMap<>();
        Map<Integer, Node> visited = new HashMap<>();

        Node copiedNode = null;

        while(!q.isEmpty()){
            Node cur_node= q.poll();
            if(visited.containsKey(cur_node)){
                continue; // ??
            }

            //copiedNode.val = cur_node.val;
            copiedNode = new Node(cur_node.val, new ArrayList<>()); // ???
            visited.put(cur_node.val, cur_node);

//            // ???
//            if(copiedNode.neighbors == null){
//                copiedNode.neighbors = new ArrayList<>();
//            }

            for(Node x: cur_node.neighbors){
                //copiedNode.neighbors.add(x);
                copiedNode.neighbors.add(x);
            }

        }


        return copiedNode;
    }


    // DFS
    public Node cloneGraph_2(Node node) {
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

