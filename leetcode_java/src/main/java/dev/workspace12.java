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

    // IDEA 1) DFS + brute force
    // 4.16 - 4.26 pm
    public int rob(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return root.val;
        }

        // ???
        return robHelper(root, false);
    }

    private int robHelper(TreeNode root, boolean robPrevNode){
        // edge
        if(root == null){
            return 0;
        }
//        if(root.left == null && root.right == null){
//            return root.val;
//        }
        // case 1) if `prev` node is rob
        if(robPrevNode){
            return robHelper(root.left, false)
                    + robHelper(root.right, false);
        }
        // case 2) if `prev` node is NOT rob
        else{
            // rob
            int val_1 = root.val +  robHelper(root.left, true)
                    + robHelper(root.right, true);
            // NOT rob
            int val_2 = robHelper(root.left, false)
                    + robHelper(root.right, false);

            return Math.max(val_1, val_2);
        }

    }

    // IDEA 2-2) DFS + `prev` check + MEMORY DP)
    // init hashmap as cache for `dp`
//    Map<TreeNode, Integer> cache = new HashMap<>();
//
//    public int rob(TreeNode root) {
//        return robHelper(root, false);
//    }
//
//    private int robHelper(TreeNode root, boolean robPrev){
//        if(root == null){
//            return 0;
//        }
//
//        // if `key` exists, we return res directly
//        // ??? key type ???
//        if(cache.containsKey(root)){
//            return cache.get(root);
//        }
//
//        // if prev node was rob,
//        // we have NO choice, and can't rob current node
//        if(robPrev){
//            // ??
//            int tmp_val = robHelper(root.left, false)
//                    +  robHelper(root.right, false);
//            // update cache
//            cache.put(root, tmp_val);
//            return tmp_val;
//        }
//        // if prev node was NOT robt
//        else{
//            // rob current node
//            int node_sum_1 = root.val + robHelper(root.left, true)
//                    +  robHelper(root.right, true);
//            // NOT rob current node
//            int node_sum_2 = robHelper(root.left, false)
//                    +  robHelper(root.right, false);
//
//            // update cache
//            cache.put(root, node_sum_1);
//
//            return Math.max(node_sum_1, node_sum_2);
//        }
//
//    }

    // IDEA 2-1) DFS + `prev` check (OK)
//    public int rob(TreeNode root) {
//        return robHelper(root, false);
//    }
//
//    private int robHelper(TreeNode root, boolean robPrev){
//        if(root == null){
//            return 0;
//        }
//        // if prev node was rob,
//        // we have NO choice, and can't rob current node
//        if(robPrev){
//            return robHelper(root.left, false)
//                    +  robHelper(root.right, false);
//        }
//        // if prev node was NOT robt
//        else{
//            // rob current node
//            int node_sum_1 = root.val + robHelper(root.left, true)
//                    +  robHelper(root.right, true);
//            // NOT rob current node
//            int node_sum_2 = robHelper(root.left, false)
//                    +  robHelper(root.right, false);
//
//            return Math.max(node_sum_1, node_sum_2);
//        }
//
//    }




    // IDEA 2) DFS + `prev` check and collect current val and maintain max val
//    int max_node_sum = 0;
//    public int rob(TreeNode root) {
//        // edge
//        if(root == null){
//            return 0;
//        }
//        if(root.left == null && root.right == null){
//            return root.val;
//        }
//
//        // ???
//        //int max_sum = getMaxNodeSum(root, 0, false);
//       // return max_node_sum;
//        getMaxNodeSum(root, 0, false);
//        return max_node_sum;
//    }
//
//    private int getMaxNodeSum(TreeNode root, int curSum, boolean robPreNode){
//        // edge
//        if(root == null){
//            return curSum;
//        }
//        if(root.left == null && root.right == null){
//            return curSum;
//        }
//      //  int tmp_sum = 0; // ???
//        if(robPreNode){
//            max_node_sum = Math.max(max_node_sum, getMaxNodeSum(root.left, curSum, false) +
//                    getMaxNodeSum(root.right, curSum, false));
//
//        }else{
//            max_node_sum = Math.max(
//                    max_node_sum,
//                    root.val + getMaxNodeSum(root.left, curSum, true) +
//                            getMaxNodeSum(root.right, curSum, true)
//            );
//        }
////
////        max_node_sum = Math.max(max_node_sum, tmp_sum);
////
////        getMaxNodeSum(root.left, curSum, true);
////        getMaxNodeSum(root.right, curSum, true);
//        // undo ???
//
//        int max_sum_1 =  root.val + getMaxNodeSum(root.left, curSum, true) +
//                getMaxNodeSum(root.right, curSum, true);
//
//        int max_sum_2 =  getMaxNodeSum(root.left, curSum, false) +
//                getMaxNodeSum(root.right, curSum, false);
//
//        return Math.max(max_sum_1, max_sum_2);
//    }


    // LC 450
    // 4.31 - 4.41 pm
    /**
     *  IDEA 1) DFS
     *
     *   NOTE !!!
     *   -> BST property
     *    ( left < root < right )
     *
     *   (if found a `to delete` node)
     *
     *   case 1) both sub left, and right node are null
     *   case 2)  sub right node is null
     *   case 3)  sub left node is null
     *
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        // edge
        if(root == null || root.val == key){
            return null;
        }
        if(root.left == null && root.right == null){
            return root;
        }

        // helper func ???
        return deleteHelper(root, key);
    }

    public TreeNode deleteHelper(TreeNode root, int key) {
        // edge
        if(root == null){
            return null;
        }

        // case 1) if NOT found
        if(root.val < key){
            //return deleteNode(root.right, key);
            // NOTE !!!! below
            root.right = deleteNode(root.right, key);
        }else if(root.val > key){
            //return deleteNode(root.left, key);
            // NOTE !!!
            root.left = deleteNode(root.left, key);
        }
        // case 2) if found `to delete` node
        else{
      /**
       *      *   case 1) both sub left, and right node are null
       *      *   case 2)  sub right node is null
       *      *   case 3)  sub left node is null
       */
      if(root.left == null && root.right == null){
          return null; // ???
      }else if(root.right == null){
          return root.left; // ??
      }else if(root.left == null){
          return root.right; // ??
      }else{
          /**
           *  should we
           *   1. find the `max` node from sub left tree ?
           *   or
           *   2. find the `min` node from sub right tree ?
           *
           */
          // cache
         // TreeNode _cache = root;
          TreeNode _right = root.right;
          while(_right != null){
              _right = _right.left;
          }
          // swap
          root.val = _right.val;
          // remove `_right` from sub right tree
          TreeNode updated_right_node = deleteHelper(root.right, _right.val);
          root.right = updated_right_node;
        }
      }

        return root; // ?????
    }

    // LC 124
    // 5.15 - 5.25 pm
    /**
     *  1. A node can only appear in the sequence `at most once`
     *  2. the path does NOT need to pass through the `root`.
     *
     *  -> return the `maximum` path sum of any non-empty path.
     *
     *
     *  IDEA 1) DFS
     *   -> post order traverse ???
     *     -> `left -> right - > root`
     *     -> so we know what's the `path sum` looks like
     *        when visit `root`
     *
     *   -> and we'll maintain a local, global max val when traverse tree
     *
     */
    int maxPathSum = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
//        // edge
//        if(root == null){
//            return 0;
//        }
//        if(root.left == null && root.right == null){
//            return root.val;
//        }

        // ??? DFS
        getPathSumHelper(root);
        return maxPathSum;
    }

    private int getPathSumHelper(TreeNode root){
        // edge
        if(root == null){
            return 0; // ???
        }

        // post order (left -> right -> root)
//        int left_sum = getPathSumHelper(root.left, curSum);
//        int right_sum = getPathSumHelper(root.right, curSum);
        int left_sum = Math.max(0, getPathSumHelper(root.left));
        int right_sum = Math.max(0, getPathSumHelper(root.right));

//        curSum = Math.max(curSum,
//                root.val + left_sum + right_sum
//        );

        maxPathSum = Math.max(maxPathSum,
                root.val + left_sum + right_sum);

        // ???
        //return maxPathSum;
       // return  root.val + left_sum + right_sum;
        return root.val + Math.max(left_sum, right_sum);
    }


    // LC 222
    // 4.34 - 4.44 pm
    /**
     *  IDEA 1) BFS
     *
     *  IDEA 2) DFS
     *
     *
     */
    // DFS
    int nodeCnt = 0;
    public int countNodes(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
//        if(root.left == null && root.right == null){
//            return 1;
//        }
        nodeCnt += 1;
        countNodes(root.left);
        countNodes(root.right);

        return nodeCnt;
    }

//    private int countHelper(TreeNode root){
//
//    }


    // BFS
//    public int countNodes(TreeNode root) {
//        // edge
//        if(root == null){
//            return 0;
//        }
//        if(root.left == null && root.right == null){
//            return 1;
//        }
//        //List<TreeNode> cache = new ArrayList<>();
//        int cnt = 0;
//
//        Queue<TreeNode> q = new LinkedList<>();
//        q.add(root);
//
//        while(!q.isEmpty()){
//            TreeNode node = q.poll();
//           // cache.add(node);
//            cnt += 1;
//            if(node.left != null){
//                q.add(node.left);
//            }
//            if(node.right != null){
//                q.add(node.right);
//            }
//        }
//
//        return cnt;
//    }

    // LC 199
    // 4.45 - 4.55 pm
    /**
     *  IDEA 1) BFS
     *
     *  IDEA 2) DFS
     *
     *
     */
    // BFS
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        // edge
        if(root == null){
            return res;
        }
        if(root.left == null && root.right == null){
            res.add(root.val);
            return res;
        }

        // bfs
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        while(!q.isEmpty()){

            // ???
            // cache `current q size`
            int size = q.size(); // ???
            // placeholder
            TreeNode cur = null;

            for(int i = 0; i < size; i++){
                cur = q.poll();
                if(cur.left != null){
                    q.add(cur.left);
                }
                if(cur.right != null){
                    q.add(cur.right);
                }
            }

            // add the `right most` val to res
            // when the `layer traverse` is completed
            res.add(cur.val);

        }

        return res;
    }

    // LC 104
    // 5.03 - 5.13 pm
    /**
     *  IDEA 1) DFS
     *
     *  IDEA 2) BFS
     *
     */
    // IDEA : DFS
    // (post-order traverse) (left -> right -> root)
    // when at root, should know the `depth` of sub tree
    int maxDepth = 0;
    public int maxDepth(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return 1;
        }

        depthHelper(root, 0);
        return maxDepth + 1;
    }

    private void depthHelper(TreeNode root, int depth){
        // edge
        if(root == null){
            return;
        }

        // maintain max depth
        maxDepth = Math.max(
                maxDepth,
                depth
        );

        depthHelper(root.left, depth + 1);
        depthHelper(root.right, depth + 1);
    }


//    int maxDepth = 0;
//    public int maxDepth(TreeNode root) {
//        // edge
//        if(root == null){
//            return 0;
//        }
//        if(root.left == null && root.right == null){
//            return 1;
//        }
//
//        depthHelper(root);
//        return maxDepth + 1;
//    }
//
//    private int depthHelper(TreeNode root){
//        // edge
//        if(root == null){
//            return 0;
//        }
//
//        int _left_depth = depthHelper(root.left);
//        int _right_depth = depthHelper(root.right);
//
//        // maintain max depth
//        maxDepth = Math.max(
//                maxDepth,
//                Math.max(_left_depth, _right_depth)
//        );
//
//        // ???
//        //return 1 + Math.max(_left_depth, _right_depth);
//        return maxDepth;
//    }

    // LC 297
    // 17.20 - 17.30 pm
    /**
     * IDEA 1) DFS
     *
     *  Tree -> string : separator + string op
     *
     *  String -> Tree: array op + binary tree property
     *
     *
     */
    public class Codec {

        // attr
        //String seriData;
        StringBuilder sb;
        //List<String> deserData;
        //TreeNode node;

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            // edge
            if(root == null){
                return "#"; // ??
            }

            this.sb = new StringBuilder();

            if(root.left == null && root.right == null){
                sb.append(root.val);
                sb.append("#");
                return sb.toString();
            }

            // BFS ???
            // (pre-order traverse)
            // root -> left sub tree -> right sub tree
            Queue<TreeNode> q = new LinkedList<>();
            q.add(root);

            while(!q.isEmpty()){

                int _size = q.size();

                for(int i = 0; i < _size; i++){
                    TreeNode cur = q.poll();
                    sb.append(cur.val);
                    sb.append(",");
                    if(cur.left != null){
                        q.add(cur.left);
                    }
                    if(cur.right != null){
                        q.add(cur.right);
                    }
                }

                sb.append("#"); // ??
            }

            return sb.toString();
        }


        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            // edge
            if(data.isEmpty() || data.equals("#")){
                return null; // ???
            }

            String[] _list = data.split("#");

            if(_list.length == 1){
                return new TreeNode(Integer.parseInt(_list[0]));
            }

            // helper func
           // Queue<TreeNode> q = new LinkedList<>();

           // TreeNode res = helper(q); // ???

            List<String> x = Arrays.asList(data.split("."));

            Queue<String> q = new LinkedList<>(Arrays.asList(data.split(".")));

            return helper(q); // ???;
        }

        private TreeNode helper(Queue<String> q){


            //TreeNode node = q.poll();
            String s = q.poll();

            // edge
            if(s.equals("#")){
                return null;
            }

            TreeNode root = new TreeNode(Integer.parseInt(s));

            root.left = helper(q);
            root.right = helper(q);

            return root;
        }
    }



    // LC 1060
    // 7.27 - 7.37 am
    /**
     *
     * Given a `sorted` array A of `unique` numbers,
     * find the `K-th` missing number starting
     * from the `leftmost` number of the array
     *
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) BINARY SEARCH
     *
     *  ex 1)
     *     A = [4,7,9,10], K = 1
     *
     *     -> diff: [ 2, 1, 0]
     *                x
     *
     *  ex 2)
     *
     *      A = [4,7,9,10], K = 3
     *
     *     -> diff: [ 2, 1, 0]
     *                   x
     *
     *
     *  ex 3)
     *
     *    Input: A = [1,2,4], K = 3
     *
     *     -> diff: [0, 1]
     *
     *     ->
     */

    // IDEA 1) BRUTE FORCE
    public int missingElement(int[] nums, int k) {
        // edge
        if(nums.length == 0){
            return -1; // ???
        }
//        if(nums.length == 1){
//            return nums[0] + k;
//        }

        List<Integer> num_list = new ArrayList<>();
        for(int x: nums){
            num_list.add(x);
        }

        int cnt = 0;
        int val = num_list.get(0);
        for(int i = num_list.get(0); i < num_list.get(num_list.size()-1); i++){

            if(!num_list.contains(val)){
                cnt += 1;
                if(cnt == k){
                    return val;
                }
            }

            val += 1;
        }

        return -1; // ??
    }

    // IDEA 2) BINARY SEARCH ??
    public int missingElement_2(int[] nums, int k) {
        // edge
        if(nums.length == 0){
            return -1; // ???
        }

        List<Integer> diff_list = new ArrayList<>();
        for(int i = 1; i < nums.length; i++){
            // add `diff`
            diff_list.add(nums[i] - nums[i-1] - 1);
        }

        // binary search find the `correct` index
        // to get the missing num
        int l = 0;
        int r = diff_list.size() - 1;

        while(r >= l){

            int mid =  (l + r) / 2;

            // if(mis )

        }


        return -1; // ??
    }

    // LC 321
    // 8.42 - 8.52 am
    /**
     *
     *
     *
     */
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {

        return null;
    }


}

