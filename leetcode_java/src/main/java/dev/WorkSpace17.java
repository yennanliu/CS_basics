package dev;

import DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class WorkSpace17 {

    // LC 510
    // 16.51 - 17:07 pm
    // Definition for a Node.
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent;
    };

    /**
     *
     *  Given a `binary search tree` BST and a node in it,
     *   find the `in-order` `successor` of that node in the BST.
     *
     *   - The successor of a node p is the node with
     *     the smallest key greater than p.val.
     *
     *
     *  IDEA 1)
     *
     */
    // V0
    public Node inorderSuccessor(Node node) {

        return null;
    }

    // LC 700
    // 15.00 - 10 pm
    /**
     *  IDEA 1) DFS
     *
     */
    // IDEA 1) iterative
    public TreeNode searchBST(TreeNode root, int val) {
        // edge
        if(root == null){
            return null;
        }

        while(root != null){
            if(root.val == val){
                return root;
            }else if(root.val < val){
                root = root.right;
            }else{
                root = root.left;
            }
        }

        // ????
        return null;
    }

    //---------------------------

    // IDEA 2) dfs
    public TreeNode searchBST_100(TreeNode root, int val) {
        // edge
        if(root == null){
            return null;
        }
//        if(root.left == null && root.right == null){
//            if(root.val == val){
//                return root;
//            }
//            return null;
//        }

        if(root.val == val){
            return root;
        }

        if(root.val < val){
            return searchBST_100(root.right, val);
        }

        // ???
        return searchBST_100(root.left, val);
    }

    // LC 530
    // 16.28 - 38 pm
    /**
     *
     * Given the root of a Binary Search Tree (BST),
     * return the `minimum absolute difference` between
     * the values of `any two different nodes `in the tree.
     *
     *
     * IDEA 1) get all nodes as list, sort, maintain the `minimum absolute difference`
     *         between any 2 nodes
     *
     *
     * IDEA 2) BST property
     */
    List<Integer> nodes = new ArrayList<>();
    public int getMinimumDifference(TreeNode root) {
        // edge
        if(root == null){
            return -1; // ????
        }
        if(root.left == null && root.right == null){
            return -1; // ???
        }

        getNodesDFS(root);

        // sort (small -> big)
        Collections.sort(nodes);
        System.out.println(">>> nodes = " + nodes);

        int minDiff = Integer.MAX_VALUE;

        for(int i = 0; i < nodes.size() - 1; i++){
            int diff = Math.abs(nodes.get(i+1) - nodes.get(i));
            minDiff = Math.min(minDiff, diff);
        }

        return minDiff;
    }

    private void getNodesDFS(TreeNode root){
        if(root == null){
            return;
        }
        nodes.add(root.val);
        getNodesDFS(root.left);
        getNodesDFS(root.right);
    }

    // LC 235
    // 16.39 - 49 pm
    /**
     * -> Given a `binary search tree (BST),`
     *    find the `lowest common ancestor (LCA)`
     *    node of two given nodes in the BST.
     *
     * NOTE !!!
     *  - BST
     *  - find `LCA`
     *  - All Node.val are unique.
     *  - P != Q
     *  - p and q will exist in the BST.
     *
     *
     *
     *
     *  IDEA 1) DFS
     *
     */
    public TreeNode lowestCommonAncestor_100(TreeNode root, TreeNode p, TreeNode q) {
        // edge
        if(root == null){
            return root;
        }
        // ????
//        if(root.val == p.val || root.val == q.val){
//            return root;
//        }

        // BST property:
        // left < root < right

        // case 1) p, q is on the DIFFERENT size of current node
//        if( (p.val < root.val && root.val < q.val) ||
//                (q.val < root.val && root.val < p.val) ){
//            return root;
//        }
        if( isBigger(root.val, p.val) * isBigger(root.val, q.val) < 1 ){
            return root;
        }

        // case 2) p, q are the LEFT side of current node
        if(root.val > p.val && root.val > q.val){
            return lowestCommonAncestor_100(root.left, p, q);
        }
        // case 3) p, q are the RIGHT side of current node
        else{
            return lowestCommonAncestor_100(root.right, p, q);
        }

       // return root; // ????
    }

    private int isBigger(int a, int b){
        if(a > b){
            return 1;
        }
        return -1;
    }

    // LC 236
    // 17.18 - 28 pm
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null){
            return null;
        }

        // Early exit: if root matches p or q
        if (root.val == p.val || root.val == q.val){
            return root;
        }

//        if (isBigger(root.val, p.val) * isBigger(root.val, q.val) < 0) {
//            return root;
//        }

        ///  ???
        TreeNode _right = lowestCommonAncestor(root.left, p, q);
        TreeNode _left = lowestCommonAncestor(root.right, p, q);

        if(_right != null && _left != null){
            return root; // ???
        }
        if(_left != null){
            return _left;
        }
        return _right;

        //return root; // ???
    }

//    private int isBigger(int a, int b) {
//        return a > b ? 1 : -1;
//    }

    // LC 173
    /**
     * Your BSTIterator object will be instantiated and called as such:
     * BSTIterator obj = new BSTIterator(root);
     * int param_1 = obj.next();
     * boolean param_2 = obj.hasNext();
     */
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode() {}
     *     TreeNode(int val) { this.val = val; }
     *     TreeNode(int val, TreeNode left, TreeNode right) {
     *         this.val = val;
     *         this.left = left;
     *         this.right = right;
     *     }
     * }
     */
    // 9.48 - 58 am
    /**
     * Implement the `BSTIterator` class that represents an iterator
     * over the `in-order` traversal of a binary search tree (BST):
     *
     * -> `in-order`  traverse of BST
     *
     *  - hasNext() Returns `true `if there is a number in the traversal
     *    to the `right` of the pointer
     *
     *  - Moves the pointer to the `right`, then `returns the number at the pointer.`
     *
     *   - when init pointer to a non-existent smallest number,
     *     the first call to next() will return the smallest element in the BST.
     *
     *
     *   IDEA 1) DFS + build BST + BST inorder traverse
     *
     *   // NOTE !!! BST inorder traverse if actually the
     *      `small -> big` ordering array
     *
     *   // NOTE !!! for BST,
     *      left < root < right
     *
     */
    class BSTIterator {


        // attr
        List<Integer> list = new ArrayList<>();
        int idx = 0;

        public BSTIterator(TreeNode root) {
            getAllNodes(root);
            // sort (small -> big)
            System.out.println(">>> (before sort) list = " + list);
            //Collections.sort(list); // default: small -> big ???
            System.out.println(">>> (after sort) list = " + list);
        }

        public int next() {
            if(list.isEmpty() || idx > list.size() - 1){
                return 0; // ???
            }
            int preIdx = idx;
            idx += 1;
            return list.get(preIdx);
        }

        public boolean hasNext() {
            // ???
            System.out.println(">>> (hasNext) idx = " + idx + ", ");
            return idx < list.size() - 1;
        }

        private void getAllNodes(TreeNode root){
            if(root == null){
                return;
            }

            getAllNodes(root.left);
            list.add(root.val);
            getAllNodes(root.right);
            //return root.val + "," + getAllNodes(root.left) + "," + getAllNodes(root.right);
        }
    }

    // LC 98
    // 10.27 - 37 am
    /**
     *   IDEA 1) DFS + BST PROPERTY
     *
     *      *   // NOTE !!! for BST,
     *      *      left < root < right
     */
    public boolean isValidBST(TreeNode root) {
        // edge
        if(root == null){
            return true;
        }
        if(root.left == null && root.right == null){
            return true;
        }

        long smallest_val = Long.MIN_VALUE;
        long biggest_val = Long.MAX_VALUE;

        // ???
        return isValidBSTHelper(root, smallest_val, biggest_val);
    }

    private boolean isValidBSTHelper(TreeNode root, long smallestVal, long biggestVal){

        if(root == null){
            return true; // ???
        }

        // check `current` node
//        if(root.right != null && root.val >= root.right.val){
//            return false;
//        }
//        if(root.left != null && root.val <= root.left.val){
//            return false;
//        }

        if(root.val <= smallestVal){
            return false;
        }
        if(root.val >= biggestVal){
            return false;
        }


        // recursively check sub left, right tree
        return isValidBSTHelper(root.left, smallestVal, root.val) &&
                isValidBSTHelper(root.right, root.val, biggestVal);
    }


    // LC 501
    // 9.45 - 55 am
    /**
     * Given the `root` of a binary search tree (BST) with duplicates,
     * return all the mode(s) (i.e., the most frequently occurred element) in it.
     *
     */
    //List<Integer> cache = new ArrayList<>();
    // {val: cnt}
    Map<Integer, Integer> map = new HashMap<>();
    //Integer topCnt = 0;
    public int[] findMode(TreeNode root) {
        // edge
        if(root == null){
            return new int[]{}; // ??
        }

        getNodes(root);
        List<Integer> list = new ArrayList<>();

        int topCnt = 0;
        for(Integer x: map.values()){
            topCnt = Math.max(topCnt, x);
        }

        System.out.println(">>> map = " + map);
        System.out.println(">>> topCnt = " + topCnt);
        for(Integer x: map.keySet()){
            if(map.get(x).equals(topCnt)){
                list.add(x);
            }
        }
        int[] res = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            res[i] = list.get(i);
        }

        return res;
    }

    private void getNodes(TreeNode root){
        if(root == null){
            return;
        }
        map.put(root.val, map.getOrDefault(root.val, 0) + 1);
        //topCnt = Math.max(topCnt, map.getOrDefault(root.val, 0) + 1);
        getNodes(root.left);
        getNodes(root.right);
    }



    // LC 108
    // 10.11 am - 21 am
    /**
     * Given an  `integer array nums` where the elements are
     * `sorted` in ascending order,
     * convert it to a `height-balanced binary search tree.`
     *
     *  - given sorted (small -> big) arr, covert it a height balanced
     *    BST
     *
     *
     *  IDEA 1) DFS (in-order traverse) + BST
     *
     *   - NOTE !!! for BST (left < root < right)
     *      the in-order traverse is an ascending arr (small -> big)
     *
     */
    //TreeNode node = new TreeNode();
    public TreeNode sortedArrayToBST(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }
        if(nums.length == 1){
            return new TreeNode(nums[0]);
        }

        //TreeNode node = new TreeNode(nums[0]);
        return bstBuilder(nums, 0, nums.length - 1); // ???
    }

    private TreeNode bstBuilder(int[] nums, int start, int end){
        // edge
        if(start > end){
            return null;
        }
        if(nums == null){
            return null; // ???
        }

        int width = (end - start) / 2; // ???
        TreeNode node = new TreeNode(nums[start + width]);

        node.left = bstBuilder(nums, start, start + width - 1); // ???
        node.right = bstBuilder(nums, start + 1 + width, end); // ???

        return node; // ????
    }

    // LC 124
    // 10.53 - 11.03 am
    /**
     *
     * -> Given the root of a `binary tree`,
     *  return the `maximum path sum `of any non-empty path.
     *
     *
     *  - A path in a binary tree is a sequence of nodes where each pair of
     *    adjacent nodes in the sequence has an edge connecting them
     *
     *  -  A node can only appear in the sequence at most once.
     *
     *  - Note that the path does not need to pass through the root.
     *
     *
     *  IDEA 1) DFS + HASHMAP
     *     - `post order traverse`
     *       -> so we know the `path from is sub root` when visit a node
     *     - get max from _left_sub_path, _right_sub_path
     *
     *
     *
     */
    // {node_val: path_sum}
    //Map<Integer, Integer> paths = new HashMap<>();
    int maxPath = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        //edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return root.val;
        }
        //if()

        gerPathHelper(root);
        System.out.println(", maxPath = " + maxPath);

        // edge case: `root` itself could form a `path` as well
        // below check if it could be a `biggest path`
        return Math.max(root.val, maxPath);
    }

    // NOTE !!!
    // this helper func gets path from `a sub node` to current node
    // e.g. path of left_sub_node, and node
    //      path of right_sub_node, and node
    private int gerPathHelper(TreeNode root){
        //edge
        if(root == null){
            //return 0;
            return 0;
        }
        // post order: left -> right -> root
        int _leftPathSum = gerPathHelper(root.left);
        int _rightPathSum = gerPathHelper(root.right);

        System.out.println(">>> root = " + root.val
                + ", _leftPathSum = " + _leftPathSum +
                " _rightPathSum = " + _rightPathSum);

        // update global max path sum
        int pathSum = _leftPathSum + _rightPathSum + root.val;
        maxPath = Math.max(maxPath, pathSum);
        // update map
        //paths.put(root.val, pathSum);

        return Math.max(_leftPathSum, _rightPathSum) + root.val;
    }

    // LC 129
    //  9.52 - 10.02 am
    /**
     *  IDEA 1) DFS (pre order traverse) + `path sum`
     *
     *
     */
    List<String> cache = new ArrayList<>();
    public int sumNumbers(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return root.val;
        }

        int res = 0;
        sumHelper(root, new ArrayList<>());

        System.out.println(">>> cache = " + cache);

        for(String x: cache){
            res += Integer.parseInt(x);
        }

        return res;
    }

    private void sumHelper(TreeNode root, List<String> cur){
        // edge
        if(root == null){
            // ???
            //return cur.toString(); // ??
            //return null;
            return;
        }

        cur.add(String.valueOf(root.val));

        if(root.left == null && root.right == null){
            //return root.val;
            cache.add(listToStr(cur)); // ???
        }

        // ??
//        if(root != null){
//            cur.add(String.valueOf(root.val));
//        }
        //cur.add(String.valueOf(root.val));
        sumHelper(root.left, cur);
        sumHelper(root.right, cur);

        // undo ??
        cur.remove(cur.size() - 1);
        //return cur.toString(); // ????
    }

    private String listToStr(List<String> input){
        String res = "";
        for(String x: input){
            res += x;
        }
        return res;
    }

    // LC 437
    // 10.47 - 10.57 am
    /**
     *  -> return the `number of paths` where the sum of the
     *    values along the path equals `targetSum.`
     *
     *  - The path `DOES NOT` need to` start or end at the root or a leaf,`
     *     but it must go `downwards `
     *    (i.e., traveling only from parent nodes to child nodes).
     *
     *  IDEA 1) DFS (pre-order traverse) + HASHMAP
     *
     */
    // map:  { path_sum: paths }
    Map<Integer, List<Integer>> pathCntMap = new HashMap<>();
    //List<Integer> cur = new ArrayList<>();
    public int pathSum(TreeNode root, int targetSum) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return root.val == targetSum ? 1: 0;
        }

        // dfs
        getPathHelper2(root, new ArrayList<>());
        System.out.println(">>> pathCntMap = " + pathCntMap);

        if(!pathCntMap.containsKey(targetSum)){
            return 0;
        }
        return pathCntMap.get(targetSum).size();
    }

    private void getPathHelper2(TreeNode root, List<Integer> cur){
        // edge
        if(root == null){
            return;
        }
        // NOTE !!! add cur root val to cache first
        cur.add(root.val);

        // update to map
        int _sum = getListSum(cur);
        List<Integer> collected = new ArrayList<>();
        if(pathCntMap.containsKey(_sum)){
            collected = pathCntMap.get(_sum);
        }
        collected.add(_sum);
        pathCntMap.put(_sum, collected);
//
//        // ??? do the same on `single cur node` ???
//        List<Integer> collected2 = new ArrayList<>();
//        if(pathCntMap.containsKey(root.val)){
//            collected2 = pathCntMap.get(_sum);
//        }
//        collected2.add(root.val);
//        pathCntMap.put(root.val, collected2);
//        pathCntMap.put(_sum, getToUpdateValues(pathCntMap, root, _sum));
//        pathCntMap.put(_sum, getToUpdateValues(pathCntMap, root, root.val));

        getPathHelper2(root.left, cur);
        getPathHelper2(root.right, cur);

        // NOTE !!! backtrack
        cur.remove(cur.size() - 1);

    }

    private List<Integer> getToUpdateValues(Map<Integer, List<Integer>> pathCntMap, TreeNode node, int val){
        List<Integer> collected = new ArrayList<>();
        //int val = node.val;
        if(pathCntMap.containsKey(val)){
            collected = pathCntMap.get(val);
        }
        collected.add(val);
        return collected;
    }

    private int getListSum(List<Integer> list){
        int res = 0;
        for(int x: list){
            res += x;
        }
        return res;
    }


    // LC 298
    // 17.32 - 42 pm
    /**
     * -> Given a `binary tree`,
     *    find the `length` of
     *    the `longest consecutive sequence path.`
     *
     *
     *  - The` path` refers to any sequence
     *   of nodes from some starting node to any node
     *    in the tree along the parent-child connections. The longest consecutive
     *    path need to be from parent to child (cannot be the reverse)
     *
     *  - e.g. path: from ANY node, to its child (top -> down)
     *
     *
     *  IDEA 1) DFS + PATH + HASHMAP
     *
     *   -> get path per node and save in hashMap
     *   -> then iterate over key in map
     *   -> and find the path with max length
     *
     */
    // map: { node : path(child_1, child_2,....) }
    Map<TreeNode, List<Integer>> pathMap2 = new HashMap<>();
    public int longestConsecutive(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return 1;
        }
        int maxConsecutivePathLen = 0;
        for(List<Integer> path: pathMap2.values()){
            if(isConsecutive(path)){
                maxConsecutivePathLen = Math.max(maxConsecutivePathLen, path.size());
            }
        }
        return maxConsecutivePathLen;
    }

    private void getNodePath(TreeNode node, List<Integer> path){
        // edge
        if(node == null){
            return;
        }
        // NOTE !!! add path to cur first
        path.add(node.val);

        // ????
        pathMap2.put(node, new ArrayList<>(path));

//        // add to
//
//        List<Integer> savedPath = new ArrayList<>();
//        // ???
//        if(pathMap2.containsKey(node)){
//            savedPath = pathMap2.get(node);
//        }
//        savedPath.add(node.val);
//
//        // ?????
//        /** NOTE !!!
//         *
//         *  trick below:
//         *
//         *   via iterate the `collected` path, we can get all possible `sub path`
//         *   from the path
//         */
////        for(int i = 0; i < savedPath.size(); i++){
////            List<Integer> subPath = savedPath.subList(0, i);
////            pathMap2.put()
////        }
//        pathMap2.put(node, savedPath);


        // recursive call (DFS)
        getNodePath(node.left, path);
        getNodePath(node.right, path);

        // backtrack !!!! (undo)
        path.remove(path.size() - 1);
    }

    private boolean isConsecutive(List<Integer> path){
        //int res = 0;
        int prev = path.get(0);
        for(int i = 1; i < path.size(); i++){
            if(path.get(i) != prev + 1){
                return false;
            }
            prev = path.get(i);
            //res += 1;
        }

        return true;
    }

    // LC 1838
    // 16.20 - 16.35 pm
    /**
     *  -> Return the `maximum` possible frequency
     *   of an element `after performing at most k operations.`
     *
     *   - The frequency of an element is the number
     *     of times it occurs in an array.
     *
     *   - given nums and an integer k.
     *
     *    - In one op, you can choose an `index` of nums
     *      and `increment the element at that index by 1.`
     *
     *
     *
     *  IDEA 1) sort + SLIDE WINDOW
     *   -> sort from small -> big
     *   - target is the `biggest` one
     *   -> try to `do op` within window under the condition ( op times <= k)
     *   - and maintain the `max freq` of current array
     *
     *
     */
    // binary search
    public int maxFrequency(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 1;
        }

        Arrays.sort(nums);

        int res = 0;
        long windowSum = 0L;
        int l = 0;
        int r = nums.length - 1;
        while(r >= l){
            int mid = (l + r) / 2;
            int val = nums[mid];


        }


//        for(int r = 0; r < nums.length; r++){
//            // cur window sum
//            windowSum += nums[r];
//            //???
//            while( (long) (r - l + 1) * nums[r] - windowSum > k){
//                windowSum -= nums[l];
//                l += 1;
//            }
//            res = Math.max(res, r - l + 1);
//        }

        return res;
    }




    // slide window
    public int maxFrequency_99(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }

        Arrays.sort(nums);

        int res = 0;
        long windowSum = 0L;
        int l = 0;

        for (int r = 0; r < nums.length; r++) {
            // cur window sum
            windowSum += nums[r];
            //???
            while ((long) (r - l + 1) * nums[r] - windowSum > k) {
                windowSum -= nums[l];
                l += 1;
            }
            res = Math.max(res, r - l + 1);
        }

        return res;
    }






    public int maxFrequency_100(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 1;
        }
        // map: {val : cnt}
        Map<Integer, Integer> map = new HashMap<>();
        for(int x: nums){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        int res = 1;
        res = getMaxCnt(map);

        // ??? sort arr (small -> big)
        Arrays.sort(nums); // ????

        // slide window
        /**
         *  for (...){
         *      while(...){
         *
         *  }
         */
        // ???
        //int kBackup = k;
        int l = 0;
        for(int r = 0; r < nums.length; r++){
            int opCnt = 0;
            int target = nums[r];
            while(nums[l] < target && opCnt < k && l < r){
                int diff = nums[r] - nums[l];
                //k -= (diff);
                opCnt += diff;
                l += 1;
            }
            // reset k
            //k = kBackup;
            res = Math.max(res, getMaxCnt(map));
        }


        return res;
    }


    private int getMaxCnt(Map<Integer, Integer> map){
        int res = 0;
        for(int val: map.values()){
            res = Math.max(res, val);
        }
        return res;
    }

    // LC 1740
    // 17.29 - 39 pm
    /**
     * Given the root of a binary tree and two integers p and q,
     * return the `distance` between the nodes of value` p and value q in the tree.`
     *
     * The distance between two nodes
     * is the number of edges on the path from one to the other.
     *
     *
     *  IDEA 1) DFS
     *
     *  IDEA 2) LCA ??
     *   -> find the LCA (the least common ancestor) of p, q
     *   and get the path sum of that root to p, q
     *
     */
    public int findDistance(TreeNode root, int p, int q) {
        // edge
        if(root == null){
            return -1;
        }
        if(p == q){
            return 0;
        }

        TreeNode nodeLCA = getLCA(root, p, q);
        int len1 = getPathLen(nodeLCA, p, 0);
        int len2 = getPathLen(nodeLCA, q, 0);

        return len1 + len2;
    }

    private TreeNode getLCA(TreeNode root, int p, int q){
        if(root == null){
            return null; // ???
        }
        // ???
//        if(root.val == p){
//            return root; // ???
//        }
//        if(root.val == q){
//            return root; // ???
//        }
        if(root.val == p || root.val == q){
            return root;
        }

        TreeNode _left = getLCA(root.left, p, q);
        TreeNode _right = getLCA(root.right, p, q);

        if(_left != null && _right != null){
            return root;
        }

        if(_left == null){
            return _right;
        }

        return _left;
    }

    // ???
    private int getPathLen(TreeNode root, int childVal, int move){
        // edge ???
        if(root == null){
            return 0;
        }
        if(root.val == childVal){
            return move; // ?????
        }
//        getPathLen(root.left, childVal, move + 1);
//        getPathLen(root.right, childVal, move + 1);
//
//        // backtrack
//        move -= 1; // ??
//
//        return move; // ????

        int _left = getPathLen(root.left, childVal, move + 1);
        if(_left != -1){
            return _left; //????
        }

        int _right = getPathLen(root.right, childVal, move + 1);
        return _right; // ???
    }


    // LC 21
    // 14.52 - 15.02 PM
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // edge
        if(list1 == null && list2 == null){
            return null;
        }
        if(list1 == null || list2 == null){
            return list1 == null ? list1: list2;
        }

        ListNode node = new ListNode();
        ListNode res = node;

        //int prev = Math.min(list1.val, list2.val);

        while(list1 != null && list2 != null){
            if(list1.val < list2.val){
                node.next = new ListNode(list1.val);
                list1 = list1.next;
            }else{
                node.next = new ListNode(list2.val);
                list2 = list2.next;
            }
            node = node.next;
        }

        // check if list1 still has node or list2 still has node
        if(list1 != null){
            node.next = list1;
        }else{
            node.next = list2;
        }

        return res.next;
    }


    // LC 86
    // 15.06 - 15.16 pm
    /**
     *  -> modify the linked list such that
     *    all nodes <= x, are at the `LEFT HAND SIDE`
     *    of the nodes that >= x. need to KEEP the
     *    `relative ordering` of input nodes
     *
     *   ex 1)
     *    [1,4,3,2,5,2], x = 3
     *
     *    -> [1,2,2, 4,3,5]
     *
     *    ex 2)
     *     [2,1], x = 2
     *     -> [1,2]
     *
     *  IDEA 1) linked list -> array -> split -> reconnect
     *
     *
     */
    public ListNode partition(ListNode head, int x) {
        // edge
        if(head == null || head.next == null){
            return head; // ??
        }

        List<Integer> smaller = new ArrayList<>();
        List<Integer> bigger = new ArrayList<>();

        ListNode head2 = head;

        while(head2 != null){
            if(head2.val < x){
                smaller.add(head2.val);
            }else{
                bigger.add(head2.val);
            }
            head2 = head2.next;
        }

        System.out.println(">>> smaller = " + smaller);
        System.out.println(">>> bigger = " + bigger);

        ListNode node = new ListNode();
        ListNode res = node;

        // reconnect smaller, then bigger
        for(Integer val: smaller){
            node.next = new ListNode(val);
            node = node.next;
        }

        for(Integer val: bigger){
            node.next = new ListNode(val);
            node = node.next;
        }

        System.out.println(">>> node = " + node + ", res = " + res);

        return res.next;
    }


    // LC 24
    // 15.20 - 15.30 pm
    /**
     *
     *
     *
     */
    public ListNode swapPairs(ListNode head) {

        return null;
    }



}
