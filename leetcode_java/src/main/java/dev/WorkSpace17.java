package dev;

import DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.DynamicProgramming.TargetSum;

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
    // LC 700
    // DFS
    public TreeNode searchBST(TreeNode root, int val) {
       // System.out.println(">>> root val = " + root.val);
        // edge
        if(root == null){
            return null;
        }
//        // dfs
//        if(root.val == val){
//            return root;
//        }
//        if(root.val < val){
//            searchBST(root.right, val);
//        }else{
//            searchBST(root.left, val);
//        }

        return dfsCheck(root, val);
    }

    private TreeNode dfsCheck(TreeNode root, int val){
        // edge
        if(root == null){
            return null;
        }
        System.out.println(">>> root val = " + root.val);
        // dfs
        if(root.val == val){
            return root;
        }

        TreeNode _left =  searchBST(root.left, val);
        TreeNode _right = searchBST(root.right, val);

        return _left == null ? _right: _left;
    }


    // BFS
    public TreeNode searchBST_98(TreeNode root, int val) {
        System.out.println(">>> root val = " + root.val);
        // edge
        if(root == null){
            return null;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            TreeNode cur = q.poll();
            if(cur.val == val){
                return cur;
            }
            if(cur.left != null){
                q.add(cur.left);
            }
            if(cur.right != null){
                q.add(cur.right);
            }
        }

        return null;
    }


    // IDEA 1) iterative
    public TreeNode searchBST_99(TreeNode root, int val) {
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
    //  return the minimum absolute difference between the values of any two different nodes in the tree.
    // dfs or bfs get all nodes, sort, get big diff
    // BFS
    public int getMinimumDifference(TreeNode root) {
        if(root == null){
            return -1; // ????
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        List<Integer> list = new ArrayList<>();
        while(!q.isEmpty()){
            TreeNode node = q.poll();
            list.add(node.val);
            if(node.left != null){
                q.add(node.left);
            }
            if(node.right != null){
                q.add(node.right);
            }
        }
        System.out.println(">>> list = " + list);
        // sort
        Collections.sort(list);
        int minDiff = Integer.MAX_VALUE;
        for(int i = 1; i < list.size(); i++){
            minDiff = Math.min(minDiff, Math.abs(list.get(i) - list.get(i-1)));
        }

        return minDiff;
    }





//    List<Integer> nodes = new ArrayList<>();
//    public int getMinimumDifference_100(TreeNode root) {
//        // edge
//        if(root == null){
//            return -1; // ????
//        }
//        if(root.left == null && root.right == null){
//            return -1; // ???
//        }
//
//        getNodesDFS(root);
//
//        // sort (small -> big)
//        Collections.sort(nodes);
//        System.out.println(">>> nodes = " + nodes);
//
//        int minDiff = Integer.MAX_VALUE;
//
//        for(int i = 0; i < nodes.size() - 1; i++){
//            int diff = Math.abs(nodes.get(i+1) - nodes.get(i));
//            minDiff = Math.min(minDiff, diff);
//        }
//
//        return minDiff;
//    }
//
//    private void getNodesDFS(TreeNode root){
//        if(root == null){
//            return;
//        }
//        nodes.add(root.val);
//        getNodesDFS(root.left);
//        getNodesDFS(root.right);
//    }

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
    // slide window
    /**
     *  IDEA 1) SLIDE WINDOW
     *
     *  -> keep calculating `cum_sum` in cur window
     *     - maintain `max freq cnt ( r - l + 1) on the same time
     *  -> use while loop to check  `nums[r] - cum_sum > k`
     *      - if yes, move left pointer, update cum_sum
     *      - else, jump out of while loop
     *   -> move right pointer,
     *
     *   -> return max freq cnt as ans
     *
     */
    public int maxFrequency(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }

        Arrays.sort(nums); // small -> big arr
        int maxFreqCnt = 0;
        long cumSum = 0L;
        int l = 0;

        for(int r = 0; r < nums.length; r++){

            cumSum += nums[r]; // ???

            while((long) nums[r] * (r - l + 1) - cumSum > k){
                cumSum -= nums[l];
                l += 1;
            }
            maxFreqCnt = Math.max(maxFreqCnt, r - l + 1);
        }

        return maxFreqCnt;
    }




    // binary search
    public int maxFrequency_200(int[] nums, int k) {
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
    // get LCA -> get path between LCA, p, and LCA, q
    public int findDistance(TreeNode root, int p, int q) {
        // edge
        if(root == null){
            return -1;
        }
        if(p == q){
            return 0;
        }

        TreeNode LCANode = getLCA2(root, p, q);

        int len1 = getPathLen2(LCANode, p, 0);
        int len2 = getPathLen2(LCANode, q, 0);

        return len1 + len2;
    }

    private int getPathLen2(TreeNode node, int val, int move){
        if(node == null){
            return -1; //??? // ?????
        }

        if(node.val == val){
            return move; // ???
        }

        //move +=1;

        // get `left` path first
        int _leftPath = getPathLen2(node.left, val, move + 1);
        if(_leftPath != -1){
            return _leftPath;
        }
        // then check `right` path
        int _rightPath = getPathLen2(node.left, val, move + 1);

        // backtrack ???
        move -= 1;

        return _rightPath;
    }

    private TreeNode getLCA2(TreeNode root, int p, int q){
        if(root == null){
            return root; // ??
        }
        if(root.val == p || root.val == q){
            return root; // ??
        }

        TreeNode left = getLCA2(root.left, p, q);
        TreeNode right = getLCA2(root.right, p, q);

        if(left != null && right != null){
            return root;
        }

        return left != null ? left : right;
    }







    // --------------------

    public int findDistance_100(TreeNode root, int p, int q) {
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
     * IDEA 1) LINKED LIST -> list -> swap -> linked list
     *
     * IDEA 2) LINKED LIST OP
     *
     *
     */
    //  IDEA 1) LINKED LIST -> list -> swap -> linked list
    public ListNode swapPairs(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }
        List<Integer> list = new ArrayList<>();
        while(head != null){
            list.add(head.val);
            head = head.next;
        }

        ListNode node = new ListNode();
        ListNode res = node;

        // 2 pointer
        ListNode pointerI = head;
        ListNode pointerJ = head.next; // ???

        ListNode _prev = null;

        while(pointerI != null && pointerI.next.next != null
                && pointerJ != null && pointerJ.next.next != null){

            ListNode _next = head.next;
            ListNode _cur = head;

            _next.next = _cur;
            _cur.next = _prev;


            pointerI = pointerI.next.next; // ???
            pointerJ = pointerJ.next.next;
        }

        return res.next;
    }



    // ????
    private List<Integer> swapList(List<Integer> list){
        // 2 pointers
        int i = 0;
        int j = 1;
        /**
         *    [1,2,3]  -> swap -->  [2,1,3]   -> [2,1,3]
         *     i j                   i j              i j  (terminate)
         *
         *
         *   [1,2,3,4]   - swap ->   [2,1,3,4]  -> [2,1,3,4]   - swap ->  [2,1,4,3]   --> [2,1,4,3]
         *    i j                     i j               i j                    i j                  i  j (terminate)
         *
         */
        while(j < list.size() && i < list.size()){
            Integer val1= list.get(i);
            Integer val2 = list.get(j);
            // swap
            //list.
            // NOTE !!!!
            list.set(1,2);

            i += 2;
            j += 2;
        }

        return list;
    }


    // LC 863
    // 9.49 - 9.59 am
    /**
     *  IDEA 1) DFS
     *    - find the target node
     *      - for  `child` nodes at the SAME side as target node,
     *        use DFS get the nodes fit requirement
     *      - for  `child` nodes at the DIFFERENT side
     *        use DFS, but the dist = `left path to target ` + `k -  `left path to target ``
     *
     *  IDEA 2) BFS
     *
     *
     */
    // 14.09 - 14.19 pm
    /**  IDEA 1) BFS + `map record parent node`
     *    -> so BFS can move UP, left, right
     *    -> collect the `k dist` node
     */
    Map<TreeNode, TreeNode> parentMap = new HashMap<>();
    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        List<Integer> res = new ArrayList<>();
        if(root == null){
            return res;
        }
        //buildParentMap(node, null);
        Queue<TreeNode> q = new LinkedList<>();

        //buildParentMap()
        Set<TreeNode> visited = new HashSet<>();


        // NOTE !!! we START from target
        q.add(target);

        int layer = 0;
        while(!q.isEmpty()){
            int size = q.size();
            //System.out.println(">>> size = " + size);
            for(int i = 0; i < size; i++){
                TreeNode node = q.poll();
                visited.add(node);
                System.out.println(">>> size = " + size +
                        ",  i = " + i +
                        ", node = " + node +
                        ", layer = " + layer);
                if(layer == k){
                    res.add(node.val);
                }
                // move left
                if(node.left != null && !visited.contains(node.left)){
                    buildParentMap(node.left, node);
                    q.add(node.left);
                }
                // move right
                if(node.right != null){
                    buildParentMap(node.right, node);
                    q.add(node.right);
                }
                // move `UP`
                if(parentMap.containsKey(node) && !visited.contains(node.right)){
                    q.add(parentMap.get(node));
                }
            }
            layer += 1;
        }

        return null;
    }

    // { node: parent }
    // NOTE !!! ONLY save node and its `last` parent (e.g. move up by 1 idx)
    private void buildParentMap(TreeNode node, TreeNode parent){
        if(node == null){
            return;
        }
        parentMap.put(node, parent); // ???
        buildParentMap(node.left, node);
        buildParentMap(node.right, node);
    }




    // 17.54 - 18.12 pm
    /**  IDEA 1) BFS + `map record parent node`
     *    -> so BFS can move UP, left, right
     *    -> collect the `k dist` node
     */
    // ?????
    // parentMap: { node, [parent_node_1,  parent_node_2, ...] }
//    Map<TreeNode, List<TreeNode>> parentMap = new HashMap<>();
//    Map<TreeNode, TreeNode> parentMap100 = new HashMap<>();
//    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
//        List<Integer> res = new ArrayList<>();
//        // edge
//        if(root == null){
//            return res;
//        }
//        Queue<TreeNode> q = new LinkedList<>();
//        q.add(root);
//
//        // ??
//        int layer = 0;
//        //int start = 0;
//        boolean shouldStartCnt = false;
//
//        while(!q.isEmpty()){
//
//            int size = q.size();
//
//            for(int i = 0; i < size; i++){
//                TreeNode cur = q.poll();
//                if(cur.equals(target)){
//                    shouldStartCnt = true;
//                }
//
//                // collect res
//                if(layer == k){
//                    res.add(cur.val);
//                }
//
//                // move up
//                if(parentMap.containsKey(cur)){
//                    //q.add()
//                    // ??
//                    for(TreeNode _node: parentMap.get(cur)){
//                        q.add(_node);
//                    }
//                }
//
//                // move left
//                if(cur.left != null){
//                    updateParentMap(root, root.left);
//                    q.add(cur.left);
//                }
//                // move right
//                if(cur.right != null){
//                    updateParentMap(root, root.right);
//                    q.add(cur.right);
//                }
//            }
//
//            // ???
//            if(shouldStartCnt){
//                layer += 1;
//            }
//
//        }
//
//
//        return res;
//    }
//
//    private void updateParentMap(TreeNode parent, TreeNode node){
//      //  List<TreeNode> parents = new ArrayList<>();
////        if(parentMap.containsKey(node)){
////            parents = parentMap.get(node);
////        }
//        if(node == null){
//            return;
//        }
//        parentMap100.put(node, parent);
//        updateParentMap(node, node.left);
//        updateParentMap(node, node.right);
//    }
//
//    private void buildParentMap100(TreeNode parent, TreeNode root){
//        if(root == null){
//            return;
//        }
////        parentMap.put(root, parent);
////
////        buildParentMap100(root.)
//    }
//
//




//
//    private int getNodeDist(){
//
//    }




    List<Integer> res = new ArrayList<>();
    public List<Integer> distanceK_100(TreeNode root, TreeNode target, int k) {;
        // edge
        if(root == null){
            return res;
        }
        if(root.left == null && root.right == null){
            if(k != 0){
                return res;
            }
            // ???
            res.add(root.val);
            return res;
        }

//        List<Integer> list1 = new ArrayList<>();
//        List<Integer> list2 = new ArrayList<>();

        // case 1) dist(node, target) = k, node, target on the SAME side
        getDistKNodes(target, root, k);
        // case 2) dist(node, target) = k, node, target on the DIFFERENT side
        int distTargetRoot = getNodeDist(root, target.val, 0);
        if(k - distTargetRoot > 0){
            getDistKNodes(root, root, k - distTargetRoot);

        }

        return null;
    }

    // dist: the `expect distance` between node and target
    // move: the move we have made
    // DFS visiting all nodes (pre-order traverse)
    // target: the `target` node (to be compared node)
    // root: the current node (the node met when traverse over tree)
    private void getDistKNodes(TreeNode root, TreeNode target, int k){
      if(root == null){
          return; // ??
      }
      //int move = getNodeDist(root, dist);
      int dist = getNodeDist(target, root.val, k);

      if(dist == k){
          res.add(root.val);
      }
      getDistKNodes(root.left, target, k);
      getDistKNodes(root.right, target, k);
    }

    // roo
    // helper func get dist between root and the node has value == val
    private int getNodeDist(TreeNode root, int val, int dist){
        if(root == null){
            return -1; //???
        }
        if(root.val == val){
            return dist;
        }
        int leftDist = getNodeDist(root.left, val, dist + 1);
        int rightDist = getNodeDist(root.left, val, dist + 1);
        // ????
        if(leftDist != -1){
            return leftDist;
        }

        // NOTE !!!  NO NEED to do backtrack for primitive dtyoe (int)

        return rightDist; // ???
    }

    // LC 752
    // 9.19 - 9.29 am
    /**
     *   -> return the `min` op to reach the target, return -1 if NOT possible
     *
     *    - "0000" as init state
     *    - can move `2 direction` on a single wheel each op
     *       - e.g. "0" -> "9" or "9" -> "0"
     *
     *    - if meet a `deadlock` return -1 directly
     *
     *
     *
     *   IDEA 1) BFS
     *
     *
     */
    public int openLock(String[] deadends, String target) {
        // edge
        if(target.equals("0000")){
            return 0;
        }
        // deadLock map
        Map<String, Integer> map = new HashMap<>();
        for(String x: deadends){
            map.put(x, 1);
            if(x.equals(target)){
                return -1;
            }
        }

        System.out.println(">>> map = " + map);
        int minOp = -1;

        // BFS
        // Queue : { "wheel_num_1" , "wheel_num_2", .... }
        Queue<String> q = new LinkedList<>();
        q.add("0000");
        int layer = 0;
        Set<String> visited = new HashSet<>();
        //visited.add("0000");

        while(!q.isEmpty()){
            String cur = q.poll();
            visited.add(cur);
            System.out.println(">>> cur  = " + cur);
            // move 4 dir on each digit
            // e.g. for "0000", move "1000", "0100", "0010", "0001"
            for(int i = 0; i < cur.length(); i++){

                int size = cur.length();

                for(int j = 0; j < size; j++){

                    System.out.println(">>> i  = " + i + ", j = " + j);

                    if(cur.equals(target)){
                        return layer;
                    }

                    int val = Integer.parseInt(String.valueOf(cur.charAt(j)));

                    int valMinus = val - 1;
                    int valPlus = val + 1;
                    if(val == 0){
                        valMinus = 9;
                    }
                    if(val == 9){
                        valPlus = 0;
                    }
                    StringBuilder sb = new StringBuilder(cur);
                    String str1 =  sb.replace(j, j + 1, String.valueOf(valMinus)).toString();
                    String str2 = sb.replace(j, j + 1, String.valueOf(valPlus)).toString();

                    System.out.println(">>> str1  = " + str1 + ", str2 = " + str2);


                    if( !map.containsKey(str1) && visited.contains(str1) ){
                        q.add(str1);
                    }
                    if( !map.containsKey(str2) && visited.contains(str2) ){
                        q.add(str2);
                    }
                }
            }

            layer += 1;
        }

        return minOp;
    }


    // LC 1268
    // 10.22 - 10.32 am
    /**
     *
     *  -> Return a list of lists of the suggested
     *     products after each character of searchWord is typed.
     *
     *   - Design a system that suggests ` at most 3 product` names from products
     *
     *  IDEA 1) TRIE
     *
     *
     *
     */
    // custom class ???
//    public class MyTreeNode{
//        String word;
//        Boolean isEnd;
//
//        public MyTreeNode(String word, Boolean isEnd){
//            this.word = word;
//            this.isEnd = false;
//        }
//    }
//    public class Trie{
//        MyTreeNode node;
//        Map<String, MyTreeNode>  children;
//    }
//    public class Trie{
//
//        // sub class
//        public class TrieNode{
//            // attr
//            TrieNode[] children;
//            boolean isEnd;
//
//            TrieNode(){
//                this.children = new TrieNode[26];
//                this.isEnd = false;
//            }
//        }
//
//        // attr
//        TrieNode root;
//
//        // constructor
//        public Trie(){
//            this.root = new TrieNode();
//        }
//
//        // method
//        public void insert(String word){
//            for(Character x: word.toCharArray()){
//                // `x - 'a'` : index ???
//                int idx = x - 'a';
//                if(this.root.children[idx] == null){
//                    this.root.children[idx] = new TrieNode();
//                }
//                this.root = this.root.children[idx];
//            }
//            this.root.isEnd = true;
//        }
//
//        public boolean search(String word){
//            return false;
//        }
//
//        public boolean startWith(String word){
//            return false;
//        }
//
//    }

    // IDEA: 2 POINTERS ???
    // needcode : https://www.youtube.com/watch?v=D4T2N0yAr20
    /**
     *
     *  step 1) collect all products,
     *          sort by `lexicographically`
     *
     *
     *  step 2)  2 Pointers: left, right
     *   one start from `idx = 0`,
     *   the other start from `idx = len - 1`
     *   move
     *    - left += 1
     *    - right -= 1
     *    till `size = 3`
     *    and collect as res
     *
     *
     */
    // IDEA: TRIE
    static class MyTrie{

        // sub class
        class MyTrieNode{
            // attr
            boolean isEnd;
            Map<String, MyTrieNode> child;

            // constructor
            MyTrieNode(){
                this.isEnd = false;
                this.child = new HashMap<>();
            }
            MyTrieNode(boolean isEnd, Map<String, MyTrieNode> child){
                this.isEnd = isEnd;
                this.child = child;
            }

        }

        // attr
        MyTrieNode node;

        // constructor
        MyTrie(){
            this.node = new MyTrieNode();
        }

        // method
        private void insert(String word){
            if(word == null){
                return;
            }
            MyTrieNode node = this.node;
            for(char c: word.toCharArray()){
                String s = String.valueOf(c);
                // V1
//                if(!node.child.containsKey(c)){
//                    node.child.put(s, new MyTrieNode()); // ???
//                }
                // V2
                node.child.putIfAbsent(s, new MyTrieNode());
                node = node.child.get(s);
            }
            node.isEnd = true; // ???
        }

        private boolean contains(String word){
            return false;
        }

        private boolean startWith(String word){
            return false;
        }

        /** // Recommend up to 3 words starting with prefix
         *
         *  NOTE !!! param: prefix
         */
        // dfs call ???? or bfs ????
        private List<String> recommend(String prefix){
            List<String> res = new ArrayList<>();
            MyTrieNode node = this.node;

            // dfs ????
            // get `prefix` ???
            for(char c: prefix.toCharArray()){
                // ????
                if(!node.child.containsKey(c)){
                    return res;
                }
                node = node.child.get(c);
            }

            dfs(node, new StringBuilder(prefix), res);

            // NOTE !!! pass `node` and prefix to DFS func

//            // bfs  ???
//            Queue<MyTrieNode> q = new LinkedList<>();
//            q.add(node);
//
//            while(!q.isEmpty()){
//                if(res.size() >= 3){
//                    return res; // ?? TODO: get top 3 if res size > 3
//                }
//                MyTrieNode cur = q.poll();
//                if(cur.child.containsKey(w)){
//                    q.add(cur.child.get(w)); // /??
//                }
//
//            }

            return res; // ????
        }

        // DFS helper func
        private void dfs(MyTrieNode node, StringBuilder path, List<String> result){
            if(result.size() >= 3){
                return;
            }
            if(node.isEnd){
                //res.add(nd)
                // ???
                result.add(path.toString());
            }

            // // sort children by key to ensure lexicographic order ????
            List<String> sortedKeys = new ArrayList<>(node.child.keySet());
            Collections.sort(sortedKeys);
            for(String s: sortedKeys){
                path.append(s);
                dfs(node.child.get(s), path, result);
                if(result.size() >= 3){
                    return;
                }
            }


        }

    }

    // IDEA: trie
    public List<List<String>> suggestedProducts(String[] products, String searchWord) {

        List<List<String>> res = new ArrayList<>();
        if (products == null || products.length == 0 || searchWord == null) {
            return res;
        }

        MyTrie trie = new MyTrie();
        for(String p: products){
            trie.insert(p);
        }

        // ???
        for(char c: searchWord.toCharArray()){
           // String s =
            // ???
            res.add(trie.recommend(String.valueOf(c)));
        }

        return res;
    }



    // --------------------







    // IDEA: 2 POINTERS
    public List<List<String>> suggestedProducts_98(String[] products, String searchWord) {
        List<List<String>> res = new ArrayList<>();
        if (products == null || products.length == 0 || searchWord == null) {
            return res;
        }

        // 1️⃣ Sort products lexicographically
        List<String> productList = new ArrayList<>(Arrays.asList(products));
        Collections.sort(productList);

        // 2️⃣ Two pointers to maintain valid prefix window
        int l = 0;
        int r = productList.size() - 1;

        for (int i = 0; i < searchWord.length(); i++) {
            char c = searchWord.charAt(i);

            // shrink left boundary
            while (l <= r && (productList.get(l).length() <= i || productList.get(l).charAt(i) != c)) {
                l++;
            }

            // shrink right boundary
            while (l <= r && (productList.get(r).length() <= i || productList.get(r).charAt(i) != c)) {
                r--;
            }

            // collect up to 3 results
            List<String> cache = new ArrayList<>();
            //int end = Math.min(l + 3, r + 1);
            int size = Math.min(3, r - l + 1);
            for (int j = l; j < l + size; j++) {
                cache.add(productList.get(j));
            }
            res.add(cache);
        }

        return res;
    }


//    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
//        List<List<String>> res = new ArrayList<>();
//        if(products == null || products.length == 0){
//            return res; // ???
//        }
//        if(searchWord == null){
//            return res; // ???
//        }
//        List<String> productList = new ArrayList<>();
//        // NOTE !!! below
//        for(String p: products){
//            productList.add(p);
//        }
//
//        System.out.println(">>> (before sort) productList = " + productList);
//
//        // custom sort: lexicographically
//        productList.sort(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                int diff = o1.compareTo(o2); // ??????
//                return diff; // ??
//            }
//        });
//
//        System.out.println(">>> (after sort) productList = " + productList);
//
//        // 2 pointers
//        int l = 0;
//        int r = productList.size() - 1;
//
//        //String[] searchWordList = searchWord.split("");
//        //String x = searchWordList[0];
//        // for(int i = 0; i < searchWord.toCharArray().length; i++)
//        for(int i = 0; i < searchWord.toCharArray().length; i++){
//            List<String> cache = new ArrayList<>();
//            int size = 3; // ??
//            char c = searchWord.charAt(i);
//
//            /** NOTE !!! deal with left, right pointer separately */
//
//            /** NOTE !!! left pointer */
//            // ???
////            while(l < productList.size() && productList.get(l).charAt(i) != searchWord.charAt(i)){
////                l += 1;
////            }
////            while(l <= r && l < productList.size() && productList.get(l).startsWith(searchWord.substring(0, i+1))){
////                l += 1;
////            }
//            while (l <= r && (productList.get(l).length() <= i || productList.get(l).charAt(i) != c)) {
//                l++;
//            }
//
//            /** NOTE !!! right pointer */
//            // /??
////            while(r > 0 && productList.get(r).charAt(i) != searchWord.charAt(i)){
////                r -= 1;
////            }
////            while(l <= r && r < productList.size() && productList.get(r).startsWith(searchWord.substring(0, i+1))){
////                r += 1;
////            }
//            while (l <= r && (productList.get(r).length() <= i || productList.get(r).charAt(i) != c)) {
//                r--;
//            }
//
//            size = Math.min(size, r - l + 1);
//            // ??
//            for(int j = l; j < l + size; j++){
//                cache.add(productList.get(j));
//            }
//            res.add(cache);
//        }
//
//
//        return res;
//    }




    // -------------

    // IDEA: 2 POINTERS
    public List<List<String>> suggestedProducts_99(String[] products, String searchWord) {
        List<List<String>> res = new ArrayList<>();
        if(products == null || products.length == 0){
            return res; // ???
        }
        if(searchWord == null){
            return res; // ???
        }
        List<String> productList = new ArrayList<>();
        for(String p: productList){
            productList.add(p);
        }

        System.out.println(">>> (before sort) productList = " + productList);

        // custom sort: lexicographically
        productList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = o1.compareTo(o2); // ??????
                return diff; // ??
            }
        });

        System.out.println(">>> (after sort) productList = " + productList);

        for(String x: searchWord.split("")){
            // the `to search` alphabet: x
            // the current product: productList.get(l), productList.get(r)
            // ???
            int l = 0;
            int r = productList.size() - 1;
            List<String> tmp = new ArrayList<>();
            while(r > l && (r - l + 1) < 3  && productList.get(l).equals(x)
                    && productList.get(r).equals(x) ){
                // ??? some condition
                //f()
                l += 1;
                r -= 1;
            }
            // ???
            //tmp.add(productList.co)
            // add tmp to res
            res.add(tmp);
        }

        return res;
    }






    /**
     *  -> Design a system that suggests` at most three product` names from products
     *
     */
    class Trie{
        class TrieNode{
            Map<String, TrieNode> children;
            boolean isEnd;

            TrieNode(){
                this.children = new HashMap<>();
                this.isEnd = false;
            }
        }

        TrieNode node;
        // new attr
        List<String> products;

        Trie(){
            this.node = new TrieNode();
            this.products = new ArrayList<>();
        }

        // method
        private void insert(String word){

            // ... TODO: implement
            this.products.add(word);
        }

        private boolean hasWord(String word){
            return false;
        }

        private boolean startFrom(String word){
            return false;
        }

        // recommend AT MOST 3 products
        // the products SHOULD HAVE the COMMON prefix
        // if there is a tie (more that 3 products)
        // order by lexicographically order (small -> big)
        private List<String> recommend(String word){
            List<String> res = new ArrayList<>();
            String cur = ""; // ??
            for(String x: word.split("")){
                if(res.size() >= 3){
                    return res;
                }
                cur += x;
               // if(this.)

            }

            return res; // if res size < 3 or null
        }

    }


    public List<List<String>> suggestedProducts_100(String[] products, String searchWord) {
        List<List<String>> res = new ArrayList<>();

        // edge
        if(products.length == 1){
             for(int i = 0; i < searchWord.length(); i++){
                 List<String> tmp = new ArrayList<>();
                 tmp.add(products[0]);
                 res.add(tmp);
             }
             return res;
        }

        // init system
        // ??
        Trie trie = new Trie();

        // step 1) insert products
        for(String x: products){
            trie.insert(x);
        }

        // step 2) search
        for(String x: searchWord.split("")){
            //trie.insert(x);
            res.add(trie.recommend(x));
        }

        return res;
    }






    // LC 652
    // 16.02 - 16.12 pm
    /**
     *
     * -> Given the root of a binary tree,
     *    return ALL `duplicate subtrees.`
     *
     *
     *  - Two trees are duplicate if they have the
     *     `same structure with the same node values.`
     *
     *   - For each kind of duplicate subtrees,
     *   you only need to return the root node of any one of them.
     *
     *
     *  IDEA 1) DFS + SAME TREE  + HASHMAP + node path ??
     *
     *    - if 2 `single node` has same value
     *        -> they are `duplicate subtree` as well
     *
     *
     */
    Map<String, Integer> pathCnt = new HashMap<>();
    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        List<TreeNode> res = new ArrayList<>();
        // edge
        if(root == null){
            return res;
        }
        if(root.left == null && root.right == null){
            return res;
        }

        ///  bfs ???
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            TreeNode node = q.poll();
            // 1) path - cnt
            String path = getNodePath2(root);
            pathCnt.put(path, pathCnt.getOrDefault(path, 0) + 1);

            // 2) `cur single node` - cnt
            String singleNodePath = String.valueOf(node.val); // ???
            pathCnt.put(singleNodePath, pathCnt.getOrDefault(singleNodePath, 0) + 1);

            if(node.left != null){
                q.add(node.left);
            }
            if(node.right != null){
                q.add(node.right);
            }
        }

        System.out.println(">>> pathCnt = " + pathCnt);
        for(String k: pathCnt.keySet()){
            if(pathCnt.get(k) > 1){
                TreeNode _node = new TreeNode(k.charAt(0)); // ????
                res.add(_node);
            }
        }

        return null;
    }

    // dfs: post order: left -> right -> root
    private String getNodePath2(TreeNode root){
        if(root == null){
            return "#"; // ???
        }
        return root.val + "," + getNodePath2(root.left)
                + getNodePath2(root.right);
    }



//    private List<Integer> getNodePath_2(TreeNode root, List<Integer> list){
//        // edge
//        if(root == null){
//            return null; // ???
//        }
//
//
//        return list; // ????
//    }

    // LC 160
    // 16.44 - 54 pm
    /**
     *  IDEA 1) HASHSET + FAST - SLOW POINTER ???
     *
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        // edge
        if(headA == null && headB == null){
            return null; // ??
        }
        if(headA == null || headB == null){
            return null; // ??
        }
        // move headA till the end first,
        // save its node, then move headB
        // and check if the node existed in headA hashset
        HashSet<ListNode> set = new HashSet<>();
        while(headA != null){
            set.add(headA);
            headA = headA.next;
        }

        while(headB != null){
            //set.add(headA);
            if (set.contains(headB)) {
                return headB;
            }
            headB = headB.next;
        }

        return null;
    }



    // LC 32
    // 17.04 - 17.14 pm
    /**
     *
     * -> Given a string containing just the characters '(' and ')',
     *   return the length of the `longest` valid
     *    (well-formed) parentheses substring.
     *
     *
     *
     *  ex 1)
     *
     *  Input: s = "(()"
     *  Output: 2
     *  Explanation: The longest valid parentheses substring is "()".
     *
     *
     *  IDEA 1) STACK
     *
     *
     *  ->
     *   (()
     *   x       st = ["("]
     *
     *   (()    st = ["(", "("]
     *    x
     *
     *   (()    st = ["("], ans = 2
     *     x
     *
     *
     *  ex 2)
     *
     *  Input: s = ")()())"
     *  Output: 4
     *  Explanation: The longest valid parentheses substring is "()()".
     *
     *   )()())   st = [")"]
     *   x
     *
     *   )()())   st = [")", "("]
     *    x
     *
     *   )()())   st = [")"], ans = 2
     *     x
     *
     *   ...
     *
     *   )()()),  st = [")", ")"],  and = 4
     *        x
     *
     *
     *  ex 3_
     *
     *   s = "(())"
     *
     *   (())
     *   x      st = ["("]
     *
     *
     *   (())
     *    x    st = ["(", "("]
     *
     *
     *   (())
     *     x   st = ["("], ans = 2
     *
     *
     *  (())
     *     x    st = [], ans = 4
     *
     *
     */
    public int longestValidParentheses(String s) {
        // edge
        if(s.isEmpty()){
            return 0;
        }
        // ??
        if(s.length() < 2){
            return 0;
        }

        int tmp = 0;
        int ans = 0;

        // ???
        String prev = null;

        // stack: FILO
        Stack<String> st = new Stack<>();
        for(String x: s.split("")){
            if(st.isEmpty()){
                st.add(x);
            }
            else if(x.equals(")") && st.peek().equals("(")){
                st.pop();
                tmp += 2;
                ans = Math.max(tmp, ans);
            }else{
                // ??? reset tmp cnt ???
                if(!(x.equals("(") && prev.equals(")"))){
                    tmp = 0;
                }
                st.add(x);
            }

            prev = x; // ???
        }

        return ans;
    }

    // LC 103
    // 12.01 - 12.11 pm
    /**
     *
     *  IDEA 1) BFS + LAYER traverse + inverse
     *
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        List<List<Integer>> cache = new ArrayList<>();
        if(root == null){
            return res;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
       // int layer = 0;

        while(!q.isEmpty()){
            List<Integer> list = new ArrayList<>();
            int size = q.size();
            for(int i = 0; i < size; i++){
                TreeNode node = q.poll();
                System.out.println(">>> node.val = " + node.val
                        + ", list = " + list);
                list.add(node.val);
                if(node.left != null){
                    q.add(node.left);
                }
                if(node.right != null){
                    q.add(node.right);
                }
            }
            cache.add(new ArrayList<>(list)); // ????
            //layer += 1;
        }

        System.out.println(">>> cache = " + cache);
        // i = 0: left -> right, i = 1: right -> right ....
        for(int i = 0; i < cache.size(); i++){
            List<Integer> _list = cache.get(i);
            if(i % 2 == 1){
                Collections.reverse(_list);
            }
            res.add(_list);
        }

        return res;
    }


    // LC 107
    // 12.20 - 12.30 pm
    /**
     * BFS ->  collect in reverse order
     *
     *
     */
    public List<List<Integer>> levelOrderBottom(TreeNode root) {

        List<List<Integer>> res = new ArrayList<>();
        List<List<Integer>> cache = new ArrayList<>();
        if(root == null){
            return res;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        // int layer = 0;

        while(!q.isEmpty()){
            List<Integer> list = new ArrayList<>();
            int size = q.size();
            for(int i = 0; i < size; i++){
                TreeNode node = q.poll();
                System.out.println(">>> node.val = " + node.val
                        + ", list = " + list);
                list.add(node.val);
                if(node.left != null){
                    q.add(node.left);
                }
                if(node.right != null){
                    q.add(node.right);
                }
            }
            cache.add(new ArrayList<>(list)); // ????
            //layer += 1;
        }

        System.out.println(">>> cache = " + cache);
        // ???
        for(int i = cache.size() - 1; i > 0; i--){
            System.out.println(">>> ");
            List<Integer> _list = cache.get(i);
            System.out.println(">>> i = " + i + ", _list = " + _list);
            res.add(_list);
        }

        return res;

    }

    // LC 314
    // 13.37 - 47 pm
    /**
     * Given a binary tree,
     *
     * -> return the vertical order traversal of its nodes' values.
     *
     * (ie, from top to bottom, column by column).
     *
     *
     * - If two nodes are in the same row and column,
     *   the order should be from left to right.
     *
     *
     *   IDEA 1) BFS + CUSTOM class
     *
     *
     *
     *   ex 1)
     *
     *   Input: [3,9,20,null,null,15,7]
     *
     *    3
     *   /\
     *  /  \
     *  9  20
     *     /\
     *    /  \
     *   15   7
     *
     * Output:
     *
     * [
     *   [9],
     *   [3,15],
     *   [20],
     *   [7]
     * ]
     *
     *   BFS res:
     *    [
     *     [3],
     *     [9,20],
     *     [15,7]
     *    ]
     *
     *
     */
    public class NodeIdx{
        int idx;
        TreeNode node;
        public NodeIdx(int idx, TreeNode node){
            this.idx = idx;
            this.node = node;
        }
    }
    public List<List<Integer>> verticalOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if(root == null){
            return res;
        }
        Queue<NodeIdx> q = new LinkedList<>();
        q.add(new NodeIdx(0, root));
        // ??
        // map: { idx: [node_val_1, node_val_2, ...] }
        Map<Integer, List<Integer>> map = new HashMap<>();

        while(!q.isEmpty()){
            int size = q.size();
            for(int i = 0; i < size; i++){
                NodeIdx nodeIdx = q.poll();
                int _idx = nodeIdx.idx;
                TreeNode _node = nodeIdx.node;
                // add to map
                /// //////////////////////
                List<Integer> list = map.get(_idx);
                if(map.containsKey(_idx)){
                    list = map.get(_idx);
                }
                // NOTE !!!
                list.add(_node.val);
                map.put(_idx, list);

                if(_node.left != null){
                    q.add(new NodeIdx(_idx - 1, _node.left));
                }
                if(_node.right != null){
                    q.add(new NodeIdx(_idx + 1, _node.right));
                }
            }
        }

        System.out.println(">>> map = " + map);
        // ??? optimize below
        List<Integer> keys = new ArrayList<>();
        for(Integer x: map.keySet()){
            keys.add(x);
        }
        // sort (small -> big)
        Collections.sort(keys);

        for(Integer k: keys){
            res.add(map.get(k));
        }

        return res;
    }

    // LC 1091
    // 10.14 - 10.24 am
    /**
     *  -> Given an n x n binary matrix grid,
     *  return the `length of the shortest clear path in the matrix. `
     *  If there is NO clear path, return -1.
     *
     *  - A clear path in a binary matrix is a path from the top-left cell (i.e., (0, 0))
     *    to the bottom-right cell (i.e., (n - 1, n - 1)) such that:
     *       - All the visited cells of the path are 0.
     *       - All the adjacent cells of the path are 8-directionally connected
     *            (i.e., they are different and they share an edge or a corner).
     *
     *  -  len  of a clear path is  # of visited cells in path.
     *
     *
     *   IDEA 1) BFS
     */
    public int shortestPathBinaryMatrix(int[][] grid) {
        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }
        if(grid.length == 1 && grid[0].length == 1){
            return  grid[0][0] == 0 ? 1 : 0; // /??
        }
        int l = grid.length;
        int w = grid[0].length;
        if(grid[l-1][w-1] != 0 || grid[0][0] != 0  ){
            return -1;
        }

        int[][] moves = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0}, {1,1} };
        boolean[][] visited = new boolean[l][w];

        // queue: { [x,y, move], ... }
        Queue<int[]> q = new LinkedList<>();

        q.add(new int[] {0,0,1});

        int minMove = l * w; // ???

        while(!q.isEmpty()){
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int move = cur[2];

            // early quit
            if(x == w - 1 && y == l - 1){
                return move; // ???
            }

            minMove = Math.min(move, minMove);

            for(int[] m: moves){
                int x_ = x + m[0];
                int y_ = y + m[1];
                if(x_ < 0 || x_ >= w || y_ < 0 || y_ >= l || visited[y_][x_] || grid[y_][x_] != 0){
                    continue;
                }
                // add to queue
                visited[y_][x_] = true;
                q.add(new int[] {x_,y_,move + 1});
            }

        }


        return minMove == l * w ? -1 : minMove;
    }


    // LC 675
    // 11.10 - 11.20 am
    /**
     *
     *  ->  return the `minimum steps` you need to walk to cut off ALL the trees.
     *      If you cannot cut off all the trees, return -1.
     *
     *    - 0 means the cell CAN NOT be walked through.
     *    - 1 represents an empty cell that CAN be walked through.
     *    - A number > 1 represents a tree in a cell that
     *       CAN be walked through, and this number is the ` tree's height.`
     *     - can move UP, DOWN, LEFT, RIGHT, can decide whether to cut a tree or not
     *     - MUST cut off the trees in order from shortest -> tallest.
     *       When you cut off a tree, the value at its cell becomes 1 (an empty cell).
     *
     *
     *
     *  IDEA 1) BFS + PQ (Dijkstra's algo ???)
     *
     *
     */
    public int cutOffTree(List<List<Integer>> forest) {
        // edge
        int l = forest.size();
        int w = forest.get(0).size();

        if(forest.isEmpty() || l == 0 || w == 0){
            return 0;
        }
        if(l == 1 && w == 1){
            return forest.get(0).get(0) == 0 ? 0 : -1; // ??????
        }
        // get tree cnt
        int treeCnt = 0;
        for(List<Integer> list: forest){
            for(Integer x: list){
                if(x != 0){
                    treeCnt += 1;
                }
            }
        }

        // bfs
        // Queue:  { [x,y, move] }
        int[][] moves = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0}, {1,1} };
        //boolean[][] visited = new boolean[l][w];

        // queue: { [x,y, move, treeCnt], ... }
        // minPQ: sort on `cut off tree cost`
        // small -> big
        //Queue<int[]> q = new LinkedList<>();
        PriorityQueue<int[]> minPq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[3] - o2[3];
                return diff;
            }
        });

        minPq.add(new int[] {0,0,1,0});

        //int minMove = l * w; // ???
        //int curTree

        while(!minPq.isEmpty()){
            int[] cur = minPq.poll();
            int x = cur[0];
            int y = cur[1];
            int cost = cur[2];
            int curTreeCnt = cur[3];

            // early quit
//            if(x == w - 1 && y == l - 1){
//                return move; // ???
//            }
            if(curTreeCnt == treeCnt){
                return cost;
            }

           // minMove = Math.min(move, minMove);

            for(int[] m: moves){
                int x_ = x + m[0];
                int y_ = y + m[1];
//                if(x_ < 0 || x_ >= w || y_ < 0 || y_ >= l || visited[y_][x_] || grid[y_][x_] != 0){
//                    continue;
//                }
                  if(x_ < 0 || x_ >= w || y_ < 0 || y_ >= l || forest.get(y_).get(x_) != 0){
                    continue;
                }
                // add to queue
               // visited[y_][x_] = true;
                minPq.add(new int[] {x_,y_,cost + forest.get(y_).get(x_), curTreeCnt + 1});
            }

        }


        return -1; // ????
    }

    // LC 987
    // 16.29 - 39 pm
    /**
     * -> Given the root of a `binary tree,`
     *    calculate the `vertical order traversal` of the binary tree.
     *
     *    (Return the vertical order traversal of the binary tree.)
     *
     *    - For each node at position (row, col), its left and right children
     *       will be at positions (row + 1, col - 1) and (row + 1, col + 1)
     *       respectively.
     *     - The root of the tree is at (0, 0).
     *     -
     *
     *
     *  IDEA 1) DFS ???
     *
     *  IDEA 1) BFS ???
     *
     */
    // IDEA 1) DFS ???
    List<List<Integer>> verticalNodes = new ArrayList<>();
    Map<Integer, List<Integer>> nodeMap = new HashMap<>();
    // ???
    int minIdx = Integer.MAX_VALUE;
    int maxIdx = Integer.MIN_VALUE;
    public List<List<Integer>> verticalTraversal(TreeNode root) {
        //List<List<Integer>> res = new ArrayList<>();
        // edge
        if(root == null){
            return verticalNodes; // ??
        }
        if(root.left == null && root.right == null){
            List<Integer> tmp = new ArrayList<>();
            tmp.add(root.val);
            verticalNodes.add(tmp);
            return verticalNodes;
        }

        // dfs call
        getVerticalNodes(root, new ArrayList<>(), 0);
        System.out.println(">>> nodeMap " + nodeMap );
        System.out.println(">>> minIdx " + minIdx + ", maxIdx = " + maxIdx );

        for(int i = minIdx; i < maxIdx+1; i++){
           List<Integer> tmp =  nodeMap.get(i);
           System.out.println(">>> (before sort) tmp = " + tmp);
           // ???
           //tmp.addAll(nodeMap.get(i));
           // sort: small -> big
           Collections.sort(tmp);
           System.out.println(">>> (after sort) tmp = " + tmp);
           verticalNodes.add(tmp);
        }

        System.out.println(">>> verticalNodes = " + verticalNodes);

        return verticalNodes; // ???
    }

    // idx: the `index` compared to the `root` idx
    // root idx = 0
    // move 1 left, idx = -1
    // move 1 right, idx = 1
    // ...
    private void getVerticalNodes(TreeNode root, List<Integer> cur, int idx){
        if(root == null){
            // ???
            //verticalNodes.add(new ArrayList<>(cur)); // ???
            return;
        }

        minIdx = Math.min(minIdx, idx);
        maxIdx  = Math.max(maxIdx, idx);

        // add the map
        List<Integer> list = new ArrayList<>();
        if(nodeMap.containsKey(idx)){
            list = nodeMap.get(idx);
        }
        list.add(root.val);
        nodeMap.put(idx, list);

        getVerticalNodes(root.left, cur, idx - 1);
        getVerticalNodes(root.right, cur, idx + 1);
    }

    // LC 1268
//    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
//
//        return null;
//    }

//    // LC 700
//    public TreeNode searchBST(TreeNode root, int val) {
//
//        return null;
//    }

    // LC 530
//    public int getMinimumDifference(TreeNode root) {
//
//        return 0;
//    }

    // LC 333
    // 10.39 - 10.49 am
    /**
     * Given the root of a binary tree, find the largest subtree,
     * which is also a Binary Search Tree (BST),
     * where the largest means subtree has the largest number of nodes.
     *
     * -> find the LARGEST subtree which is a BST.
     *   - largest means subtree has the largest number of nodes.
     *
     *
     *  IDEA 1) DFS + BST property
     *
     */
    // ??
    int maxBSTNodeCnt = 0;
    public int largestBSTSubtree(TreeNode root) {
        if(root == null){
            return 0; // ??
        }
        if(root.left == null && root.right == null){
            return 1;
        }
        // BFS go through nodes
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        while(!q.isEmpty()){
            TreeNode node = q.poll();
            // ????
            if(isBST(node, Integer.MIN_VALUE, Integer.MAX_VALUE)){
                maxBSTNodeCnt = Math.max(maxBSTNodeCnt, getSubTreeNode(node));
            }
            if(node.left != null){
                q.add(node.left);
            }
            if(node.right != null){
                q.add(node.right);
            }
        }


        return maxBSTNodeCnt;
    }

    private boolean isBST(TreeNode root, int minVal, int maxVal){
        if(root == null){
            return true;
        }
//        if(root.val > maxVal){
//            return false;
//        }
//        if(root.val < minVal){
//            return false;
//        }
        if (root.val <= minVal || root.val >= maxVal){
            return false;
        }
        return isBST(root.left, minVal, root.val) &&
                isBST(root.right, root.val, maxVal);
    }

    private int getSubTreeNode(TreeNode root){
        if(root == null){
            return 0; // ???
        }
        // ????
        //getSubTreeNode(root)
        return 1 + getSubTreeNode(root.left) + getSubTreeNode(root.right);
    }



    // LC 336
    // 10.18 - 10.32 am
    /**
     *  -> Return an `array` of ALL the` palindrome pairs` of words.
     *
     *   - You are given a 0-indexed array of unique strings words.
     *   - 0 <= i, j < words.length,
     *   - i != j, and
     *   - words[i] + words[j] (the concatenation of the two strings) is a palindrome.
     *
     *
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) HASHMAP
     *
     *
     */
    // BRUTE FORCE + HAHSHMAP
    public List<List<Integer>> palindromePairs(String[] words) {

        List<List<Integer>> res = new ArrayList<>();
        // map: { str : idx } // ???
        Map<String, Integer> map = new HashMap<>();

        // edge
        if(words == null || words.length == 0){
            return res;
        }
        if(words.length == 1){
            return res;
        }
        // ???
        if(words.length == 2){
            String w1 = words[0];
            String w2 = words[1];
            if(isPalindrome(w1+w2)){
                List<Integer> tmp = new ArrayList<>();
                // ???
                tmp.add(0);
                tmp.add(1);
                res.add(tmp);
            }
            if(isPalindrome(w2 + w1)){
                List<Integer> tmp = new ArrayList<>();
                // ???
                tmp.add(1);
                tmp.add(0);
                res.add(tmp);
            }

            //Map<String, Integer> map = new HashMap<>();
            for(int i = 0; i < words.length; i++){
                map.put(words[i], i);
            }

            // brute force: double loop
            for(int i = 0; i < words.length; i++){
                String word = words[i];
                String wordReversed = reverseStr(word);
                if(map.containsKey(wordReversed)){
                    if(i != map.get(wordReversed)){
                        List<Integer> tmp = new ArrayList<>();
                        tmp.add(i);
                        map.get(wordReversed);
                        res.add(tmp);
                    }
                }
                // check if the `reverse str` existed in map and idx i != idx j

//                for(int j = i+1; j < words.length; j++){
//                    System.out.println(">>> i = " + i + ", j = " + j);
//                    String newWord = words[i] + words[j];
//                    if(isPalindrome(newWord)){
//                        List<Integer> tmp = new ArrayList<>();
//                        // ???
//                        tmp.add(i);
//                        tmp.add(j);
//                        res.add(tmp);
//                    }
//                }

            }


            return res;
        }

        return res;
    }

    private String reverseStr(String str){
        if(str.isEmpty()){
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString(); // ????
    }

    private boolean isPalindrome(String str){
        if(str.isEmpty()){
            return true;
        }
        int l = 0;
        int r = str.length() - 1;

        while(r > l){
            if(str.charAt(l) != str.charAt(r)){
                return false;
            }
            r -= 1;
            l += 1;
        }
        return true;
    }


    // LC 30
    // 16.47 - 17:05 pm
    /**
     * -> Return an array of the starting indices of all
     *    the concatenated substrings in s.
     *    You can return the answer in any order.
     *
     *    - A `concatenated string` is a string that exactly contains
     *       ALL the strings of ANY permutation of words concatenated.
     *
     *    IDEA 1) backtrack + HASH MAP + SLIDE WINDOW ???
     *
     *    - backtrack get all combination
     *    - HASH MAP record above
     *    - SLIDE WINDOW go through s, get the validated idx
     *
     *
     *
     *   ex 1)
     *
     *   Input: s = "barfoothefoobarman", words = ["foo","bar"]
     *   Output: [0,9]
     *
     *
     *   -> candidates: foobar, barfoo
     *   // map : { combination: cnt }
     *   -> map : {"foobar": 1, "barfoo": 1} // ????
     *
     *   "barfoothefoobarman"    ans = [0]
     *    l    r
     *
     *    ...
     *
     *    "barfoothefoobarman"    ans = [0, 9]
     *              l    r
     *
     *
     *   ex 2)
     *
     *    Input: s = "wordgoodgoodgoodbestword",
     *    words = ["word","good","best","word"]
     *    -> `combination cnt = 4! = 24 ????
     *
     *    -> map: {"wordgoodbestword": 1, ....}
     *    -> ans = []
     *
     *
     *   ex 3)
     *
     *   Input: s = "barfoofoobarthefoobarman", words = ["bar","foo","the"]
     *   Output: [6,9,12]
     *
     *   -> combination cnt = 3! = 6 ???
     *   -> map: {
     *       barfoothe: 1,
     *       barthefoo: 1,
     *       foothebar: 1,
     *       foobarthe: 1,
     *       thebarfoo: 1,
     *       thefoobar: 1
     *   }
     *
     *
     */
    // ????
    List<String> combinaitons = new ArrayList<>();
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        // edge
        if(s.isEmpty()){
            return res;
        }
        if(words == null || words.length == 0){
            return res;
        }

        List<String> list = new ArrayList<>();
        for(String w: words){
            list.add(w);
        }

        // map
        Map<String, Integer> map = new HashMap<>();

        //  private void getComgination(String[] words, String s, List<String> cur, int startIdx)
        getComgination(words, s, new ArrayList<>(), 0);

        System.out.println(">>> combinaitons = " + combinaitons);
        
        // slide window
        /**  pattern:
         *
         *  for(int r = 0; r < len; r++){
         *      while(condition){
         *      // ...
         *          l += 1;
         *      }
         *      // ...
         *  }
         *
         */
        int l = 0;
        for(int r = 0; r < s.length(); r++){

            // ????
            while( r > l && !isValid2( s.substring(l, r+1), map )){
                l += 1;
            }

            res.add(l);
        }


        return res;
    }

    private boolean isValid2(String cur, Map<String, Integer> map){
        return map.containsKey(cur); // ????
    }

    private boolean isValid(String s, String cur){
        // ??? check cur, and s is the `same combination`
        // via `char diff` ???
        int diff1 = 0;
        int diff2 = 0;
        for(char ch: s.toCharArray()){
            diff1 += (ch - 'a');
        }
        for(char ch: cur.toCharArray()){
            diff2 += (ch - 'a');
        }
        return diff1 == diff2; // /???
    }

    // ???
    private void getComgination(String[] words, String s, List<String> cur, int startIdx){
        // backtrack get `all combination`
       String str = cur.toString(); ///
        if(str.length() > s.length()){
            return; // ???
        }
        if(str.length() == s.length()){
            // ???
            if(!combinaitons.contains(str)){
                // ???
                combinaitons.add(str); // ???
            }
        }
        for(int i = startIdx; i < words.length; i++){
            cur.add(words[i]);
            getComgination(words, s, cur, startIdx + 1); // ???
        }
        // backtrack ???
        //startIdx -= 1; //???
        cur.remove(cur.size() - 1); // ???
    }

    // LC 640
    public String solveEquation(String equation) {

        return null;
    }


    // LC 594
    // 16.37 - 47 pm
    /**
     *
     *
     *  -> return the length of its `longest harmonious subsequence`
     *  among all its possible subsequences.
     *
     *  - We define a `harmonious array `as an array where the difference
     *    between its `maximum value and its minimum value is exactly 1.`
     *
     *
     *  -> return `LONGEST of harmonious subsequence`
     *    - harmonious: diff(max, min) == 1
     *    - A subsequence is an array that can
     *       be derived from another array by deleting some or no elements
     *       `without changing the order of` the remaining elements.
     *
     *
     *  IDEA 1) HASHMAP
     *
     *   -> map : { val : [idx1, idx2,...] }
     *   -> record min, max idx
     *   -> given a val, we know that the `possible max or min` val compared to that val
     *     -> so we can get the subsequence len based on that
     *
     *  IDEA 2) ARRAY + SORT ???
     *
     *
     *  --------
     *
     *  Example 1:
     *
     *    Input: nums = [1,3,2,2,5,2,3,7]
     *
     *     Output: 5
     *
     *     Explanation:  The longest harmonious subsequence is [3,2,2,2,3].
     *
     *
     *  -> map:  { 1: [0], 2: [2, 3, 5], 3: [1, 6], 7: [7] }
     *
     *  ->
     *     [1,3,2,2,5,2,3,7]    ->  1222, ans = 4
     *      x
     *
     *    [1,3,2,2,5,2,3,7]    ->  32223, ans = 5
     *       x
     *
     *    [1,3,2,2,5,2,3,7]    ->  32223, ans = 5
     *         x
     *
     *   [1,3,2,2,5,2,3,7]  -> ans = 5
     *            x
     *   ..
     *
     *   [1,3,2,2,5,2,3,7]   -> ans = 5
     *                  x
     *
     *
     *
     *  exp 2)
     *
     *  Input: nums = [1,2,3,4]
     *
     * Output: 2
     *
     *  map: {1: [0], 2:[1], 3:[2], 4: [3] }
     *
     *
     *   [1,2,3,4]   -> 12, ans = 2
     *    x
     *  ...
     *
     *  exp 3)
     *
     *  Input: nums = [1,1,1,1]
     *
     *
     * -> map : {1: [0,1,2,3] }  -> ans = 0
     *
     *
     *
     */
    public int findLHS(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }

        HashSet<Integer> set = new HashSet<>();
        // map: { val: [idx1, idx2,...]}
        Map<Integer, List<Integer>> map = new HashMap<>();

        for(int i = 0; i < nums.length; i++){
            int k = nums[i];
            set.add(k);
            List<Integer> list = new ArrayList<>();
            if(map.containsKey(k)){
                list = map.get(k);
            }
            list.add(i);
            map.put(k, list);
        }

        int maxSubLen = 0;

        System.out.println(">>> map = " + map);

        if(map.keySet().size() <= 1){
            return 0;
        }

        for(Integer x: set){
            int bigger = x + 1;
            int smaller = x - 1;
            if(map.containsKey(bigger)){
                maxSubLen = Math.max(maxSubLen, map.get(bigger).size() + map.get(x).size());
            }
            if(map.containsKey(smaller)){
                maxSubLen = Math.max(maxSubLen, map.get(smaller).size() + map.get(x).size());
            }
        }

        return maxSubLen;
    }



    // LC 407
    // 17.04 - 14 pm
    /**
     *
     * -> Given an m x n integer matrix heightMap representing
     *   the height of each unit cell in a 2D elevation map,
     *
     *
     *   -> return the `volume of water` it can trap after raining.
     *
     *
     *   IDEA 1) BFS + PQ ????
     *
     *
     *   exp 1)
     *
     *   Input: heightMap = [[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]]
     *   Output: 4
     *
     *
     *  ->
     *
     *  [
     *   [1,4,3,1,3,2],
     *   [3,2,1,3,2,4],
     *   [2,3,3,2,3,1]
     * ]
     *      1 2   1
     *
     */
    public int trapRainWater(int[][] heightMap) {

        return 0;
    }


    // LC 209
    // 17.53 - 18.07 pm
    /**
     *
     *  -> Given an array of positive integers nums and a positive integer target,
     *  return the minimal length of a subarray whose sum
     *   is greater than or equal to target.
     *  If there is no such subarray, return 0 instead.
     *
     *
     *   -> return the min len of subarray st the sum >= target
     *    if can't find, return 0
     *
     *  - A subarray is a `contiguous` non-empty sequence of elements within an array.
     *
     *
     *  // IDEA 1) SLIDE WINDOW ???
     *
     */
    public int minSubArrayLen(int target, int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }

        /**
         *  slide window pattern:
         *
         *  int l = 0;
         *
         *  for(int r = 0; r < len; r++){
         *      while(condition){
         *          // ..
         *          l += 1;
         *      }
         *      ans = min(ans, r - l + 1);
         *      // ...
         *  }
         *
         */
        /**
         *  Dry run:
         *
         *
         *   exp 1)
         *    Input: target = 7, nums = [2,3,1,2,4,3]
         *    ans = 2
         *
         *   ->
         *
         *    [2,3,1,2,4,3]   sum = 2
         *     l
         *     r
         *
         *    [2,3,1,2,4,3]   sum = 5
         *     l r
         *
         *    [2,3,1,2,4,3]  sum = 6
         *     l   r
         *
         *    [2,3,1,2,4,3]  sum = 8 -> sum = 6
         *     l l   r
         *
         *    [2,3,1,2,4,3]  sum = 10 -> sum = 7, ans = 3
         *       l l   r
         *
         *    [2,3,1,2,4,3]  sum = 10 -> sum = 9 -> sum = 7, ans = 2
         *         l l l   r
         *
         */
        int minLen = Integer.MAX_VALUE;
        int l = 0;

        int curSum = 0;
        for(int r = 0; r < nums.length; r++){
            // ??
            curSum += nums[r];
            System.out.println(">>> (outside while) r = " + r + ", l = " + l + ", curSum = " + curSum);
            while (r > l && curSum > target){
                System.out.println(">>> (inside while) r = " + r + ", l = " + l + ", curSum = " + curSum);
                l += 1;
                curSum -= nums[l]; // ???
            }

            if(curSum == target){
                System.out.println(">>> minLen = " + minLen);
                minLen = Math.min(minLen, r - l + 1);
            }
        }


        return minLen == Integer.MAX_VALUE ? 0: minLen;
    }




}
