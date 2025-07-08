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



//    private Node cloneHelper(Node node){
//        // edge
//        if(node == null){
//            return null;
//        }
//
//        // copy val
//        copiedNode.val = node.val;
//
//        // copy neighbors
//        copiedNode.neighbors = node.neighbors; // ???
//
//        // loop over `neighbors`
//        for(Node x: node.neighbors){
//            cloneHelper(x);
//        }
//
//        return node;
//    }

    // LC 567
    // 18.55 - 19.05 pm
    /**
     *
     *  Given two strings s1 and s2, return true if s2 contains
     *  a permutation of s1, or false otherwise.
     *
     *
     *  -> A permutation is a rearrangement of all
     *     the characters of a string.
     *
     *
     *   IDEA 1) SLIDING WINDOW + HASHMAP
     */
    public boolean checkInclusion(String s1, String s2) {
        // edge
        if((s1 == null && s2 == null) || s1.equals(s2)){
            return true;
        }
        if(s1 != null && s2 == null){
            return false;
        }
        if(s1.length() > s2.length()){
            return false;
        }

        // element cnt map in s1
        HashMap<String, Integer> s1_map = new HashMap<>();
        for(String x: s1.split("")){
            s1_map.put(x, s1_map.getOrDefault(x, 0) + 1);
        }

        System.out.println(">>> s1_map = " + s1_map);

        // sliding window
        String[] s2_arr = s2.split("");
        for(int i = 0; i < s2_arr.length; i++){

            //HashMap<String, Integer> s2_map = new HashMap<>();
            HashMap<String, Integer> s1_map_tmp = s1_map;
            System.out.println(">>> s1_map_tmp = " + s1_map_tmp);

            for(int j = i; j < s2_arr.length; j++){

                if(s1_map_tmp.isEmpty()){
                    return true;
                }

                String val = s2_arr[j];
                if(!s1_map.containsKey(val)){
                    break;
                }

                if(j - i + 1 == s1.length()){
                    return true;
                }

                //s2_map.put(val, s2_map.getOrDefault(val, 0) + 1);
                s1_map_tmp.put(val, s1_map_tmp.get(val) - 1);
                if(s1_map_tmp.get(val) == 0){
                    s1_map_tmp.remove(val);
                }

                j += 1;
            }

        }

        return false;
    }


}

