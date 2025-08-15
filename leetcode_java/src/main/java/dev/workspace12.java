package dev;

import javax.print.DocFlavor;
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

    // LC 271
    // 17.57 - 18.07 pm
    /**
     *  IDEA 1) string op + queue
     *
     */
    public String encode(List<String> strs) {
        // edge
        if( strs == null || strs.isEmpty() ){
            return ",";
        }

        StringBuilder sb = new StringBuilder();

        for(String x: strs){
            sb.append(x);
            sb.append(",");
        }

        return sb.toString();
    }
    public List<String> decode(String s) {
        // edge
        if(s.equals(",")){
            return new ArrayList<>();
        }

        List<String> res = new ArrayList<>();
        for(String x: s.split(",")){
            if(!x.isEmpty()){
                res.add(x);
            }
        }

        return res;
    }

    // LC 84
    // 18.14 - 18.28 pm
    /**
     *  IDEA 2) STACK (monotonic stack)
     *
     */
    public int largestRectangleArea(int[] heights) {
        // edge
        if(heights == null || heights.length == 0){
            return 0;
        }
        if(heights.length == 1){
            return heights[0];
        }
        int max_area = 0;

        // mono decrease stack ???
        // so the new element can be added to stack
        // ONLY if cur < last_stack_element
        // if meet a `bigger val`
        // need to pop ALL elements in stack
        Stack<Integer> st = new Stack<>();

        int min_height = 0;

        for(int i = 0; i < heights.length; i++){

            int cur = heights[i];

            min_height = Math.min(min_height, cur);

            if(st.isEmpty()){
                max_area = Math.max(max_area, cur);
                st.add(cur);
            }else{
               int cnt = 0;
               int tmp_min_height = 0;
               while(!st.isEmpty() && st.peek() < cur){
                   tmp_min_height = Math.min(tmp_min_height, st.pop());
                    cnt += 1;
                }
                max_area = Math.max(max_area, tmp_min_height * cnt);
                st.add(cur);
            }
        }

        return Math.max(max_area, min_height * heights.length);
    }


    /**
     *  IDEA 1)  BRUTE FORCE -> DOUBLE LOOP
     *
     */
    public int largestRectangleArea_1(int[] heights) {
        // edge
        if(heights == null || heights.length == 0){
            return 0;
        }
        if(heights.length == 1){
            return heights[0];
        }
        int max_area = 0;

        for(int i = 0; i < heights.length; i++){
            int min_height = heights[i];
            for(int j = i; j < heights.length; j++){

                min_height = Math.min(min_height, heights[j]);

                max_area = Math.max(max_area, Math.max(
                        heights[j],  (j - i + 1) * min_height
                ));
            }
        }

        return max_area;
    }


    // LC 337
    // 16.15 - 16.25 pm
    /**
     *
     * Given the root of the binary tree,
     * return the `maximum` amount of money
     * the thief can rob without alerting the police.
     *
     * ALWAYS need to start from `root`,
     * but it's optional to `rub` `root`
     *
     * It will automatically contact the police if
     * two directly-linked houses were broken into
     * on the same night.
     *
     *
     *  target:  get the `max` total amount
     *           without collect to `directly-linked` nodes
     *
     *  IDEA 1) DFS + brute force ???
     *
     *   DFS -> get path to an array ??
     *
     *   preorder: 3 -> 2 -> 3 -> 3 -> 1
     *
     *
     *  IDEA 2) DFS + `prev` check and collect current val and maintain max val
     *        + backtrack ???
     *
     *  IDEA 3) DFS + dp ??
     *
     */
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // IDEA 2) DFS + `prev` check and collect current val and maintain max val
    int max_node_sum = 0;
    public int rob(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return root.val;
        }

        // ???
        //int max_sum = getMaxNodeSum(root, 0, false);
       // return max_node_sum;
        getMaxNodeSum(root, 0, false);
        return max_node_sum;
    }

    private int getMaxNodeSum(TreeNode root, int curSum, boolean robPreNode){
        // edge
        if(root == null){
            return curSum;
        }
        if(root.left == null && root.right == null){
            return curSum;
        }
      //  int tmp_sum = 0; // ???
        if(robPreNode){
            max_node_sum = Math.max(max_node_sum, getMaxNodeSum(root.left, curSum, false) +
                    getMaxNodeSum(root.right, curSum, false));

        }else{
            max_node_sum = Math.max(
                    max_node_sum,
                    root.val + getMaxNodeSum(root.left, curSum, true) +
                            getMaxNodeSum(root.right, curSum, true)
            );
        }
//
//        max_node_sum = Math.max(max_node_sum, tmp_sum);
//
//        getMaxNodeSum(root.left, curSum, true);
//        getMaxNodeSum(root.right, curSum, true);
        // undo ???

        int max_sum_1 =  root.val + getMaxNodeSum(root.left, curSum, true) +
                getMaxNodeSum(root.right, curSum, true);

        int max_sum_2 =  getMaxNodeSum(root.left, curSum, false) +
                getMaxNodeSum(root.right, curSum, false);

        return Math.max(max_sum_1, max_sum_2);
    }



}

