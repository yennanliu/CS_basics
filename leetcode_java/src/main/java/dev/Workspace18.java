package dev;

import LeetCodeJava.DataStructure.TreeNode;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;

import java.util.*;

public class Workspace18 {

    ///WorkSpace17.MyTrie trie = new WorkSpace17.MyTrie();
    //WorkSpace17.MyTrie.MyTrieNode.


    // LC 2871
    // 6.21 - 31 am
    /**
     *   IDEA: BRUTE FORCE + AND bit op
     *
     *
     */
    public int maxSubarrays(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }

        int cnt = 0;
        int cur = -1; // ???? // nums[0];

        for(int i = 0; i < nums.length; i++){
            if(cur == -1){
                cur = nums[i];
            }else{
                cur = cur & nums[i];
            }
            if(cur == 0){
                cnt += 1;
                // reset cur
                cur = -1;
            }
        }

        return cnt == 0 ? 1: cnt;
    }

    // LC 2870
    // 6.32 - 42 am
    /**
     *   IDEA: hashmap + BRUTE FORCE + math
     *
     *   KEY:
     *
     *    x % 3 = 0 or 1 or 2
     *    -> 0, ans unchanged
     *    -> 1, ans += 1
     *    -> 2, ans += 1
     *
     *    -> since x % 3 = 1 or 2
     *    -> we will the other `remove 2 op` to empty the arr
     *
     */
    public int minOperations(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int x : nums) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        int ops = 0;

        for (int x : nums) {

            // edge
            if(map.get(x) == 1){
                return -1;
            }

            //map.put(x, map.getOrDefault(x, 0) + 1);
            if(map.containsKey(x)){
                int cnt = map.get(x);
                // case 1) cnt % 3 == 0
                if(cnt % 3 == 0){
                    map.remove(x);
                    ops += 1; // ???
                }
                // case 2) cnt % 3 == 1
                if(cnt % 3 == 1){
                    if(map.get(x) < 4){
                        return -1; // ???
                    }
                    map.put(x, map.get(x) - 4);
                }
                // case 3) cnt % 3 == 2
                else{
                    if(map.get(x) < 5){
                        return -1; // ???
                    }
                    map.put(x, map.get(x) - 5);
                }
            }

        }


        return ops;
    }


    // LC 653
    /**
     *
     * Given the root of a BST and an integer k, r
     * -> return true if there exist two elements in the BST such that
     * their sum is equal to k, or false otherwise.
     *
     *  -> return true if ANY node in BST sum equals as k, otherwise return false
     *
     *
     *
     */
    public boolean findTarget(TreeNode root, int k) {
        // edge
        if(root == null){
            return false;
        }
        if(root.left == null && root.right == null){
            return false;
        }
        // map : { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        // bfs
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            TreeNode cur = q.poll();
            map.put(cur.val, 1);  // node val should be unique
            // x + y = k
            // -> x = k - y
            // node val should be unique
            if(map.containsKey(k - cur.val) && k - cur.val != cur.val){
                return true;
            }
            if(cur.left != null){
                q.add(cur.left);
            }
            if(cur.right != null){
                q.add(cur.right);
            }
        }

        return false;
    }


    // LC 1382
    // 10.09 - 19 am
    /**
     *  -> return a `balanced` BST with the same node values.
     *
     *    -  balanced: if the depth of the two subtrees of every node never differs by more than 1.
     *    - NOTE: the returned tree still has to be BST
     *
     *
     *   IDEA 1) DFS -> reconstruct tree ???  + keep `depth diff` <= 1
     *     BST property:  left < root < right
     *
     *  IDEA 2) BFS -> reconstruct tree ??? + keep `depth diff` <= 1
     *
     *  IDEA 3) cut `BST` directly, check how to cut, and add cut node back to BST
     *          can make it balanced
     *
     */
    // IDEA 1) INORDER + reconstruct tree
    public TreeNode balanceBST(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        inorderGetNodes(root, list);
        return buildTree(list, 0, list.size() - 1);
    }

    private void inorderGetNodes(TreeNode root, List<TreeNode> list){
        // inorder: left -> root -> right
        if(root == null){
            return;
        }
        inorderGetNodes(root.left, list);
        list.add(root);
        inorderGetNodes(root.right, list);
        //return list;
    }

    // l: left boundary, r: right boundary
    private TreeNode buildTree(List<TreeNode> list, int l, int r){
        // ???
        if(list == null){
            return null;
        }
        // get mid idx, and radius
        /**
         *        int mid = (l + r) / 2; // radius
         *         TreeNode root = nodes.get((mid));
         *         root.left = rebuildBST(nodes, l, mid - 1); // ???
         *         root.right = rebuildBST(nodes, mid + 1, r);
         */
        int mid = (l + r) / 2;
        TreeNode root = list.get(mid); // ???
        root.left = buildTree(list, l, mid - 1);
        root.right = buildTree(list, mid + 1, r);

        return root;
    }







    // IDEA 1) BFS -> reconstruct tree ???
//    public TreeNode balanceBST(TreeNode root) {
//
//        List<TreeNode> nodes = new ArrayList<>();
//        inorder(root, nodes);
//
//        // ???
//        return rebuildBST(nodes, 0, nodes.size() - 1);
//    }
//
//    private void inorder(TreeNode root, List<TreeNode> nodes) {
//        if (root == null){
//            return;
//        }
//        inorder(root.left, nodes);
//        nodes.add(root);
//        inorder(root.right, nodes);
//    }
//
//    private TreeNode rebuildBST(List<TreeNode> nodes, int l, int r){
//        // edge
//        if(nodes == null || nodes.size() == 0){
//            return null; // ???
//        }
//        if(l > r){
//            return null; // ?????
//        }
//        // ??
//        int mid = (l + r) / 2; // radius ???
//        TreeNode root = nodes.get((mid));
//        root.left = rebuildBST(nodes, l, mid - 1); // ???
//        root.right = rebuildBST(nodes, mid + 1, r);
//
//        // ???
//        return root;
//    }





    // IDEA 3) cut `BST` directly, check how to cut, and add cut node back to BST
    //     *          can make it balanced
//    public TreeNode balanceBST(TreeNode root) {
//        // edge
//        if(root == null){
//            return root;
//        }
//        if(root.left == null && root.right == null){
//            return root;
//        }
//        if(isBalanced(root)){
//            return root;
//        }
//
//        // ??
//        while(!isBalanced(root)){
//            modifyTree(root);
//        }
//
//        return root;
//    }
//
//    private void modifyTree(TreeNode root){
//
//    }
//
//    // max depth <= 2 ??????
//    // or diff (left, right) <= 2 ???
//    private boolean isBalanced(TreeNode root){
//        if(root == null){
//            return true;
//        }
//        return Math.abs(getDepth(root.left) - getDepth(root.right)) <= 2;
//    }
//
//    int maxDepth = 0;
//    private int getDepth(TreeNode root){
//        if(root == null){
//            return 0;
//        }
//        int leftDepth = getDepth(root.left);
//        int rightDepth = getDepth(root.right);
//        maxDepth = Math.max(leftDepth, rightDepth);
//        return Math.max(getDepth(root.left), getDepth(root.right)) + 1;
//    }




    //  IDEA 1) BFS -> reconstruct tree ??? + keep `depth diff` <= 1
//    public TreeNode balanceBST(TreeNode root) {
//        // edge
//        if(root == null){
//            return root;
//        }
//        if(root.left == null && root.right == null){
//            return root;
//        }
//        // BFS collect tree (in order: left -> root -> right)
//        Queue<TreeNode> q = new LinkedList<>();
//        q.add(root);
//        List<TreeNode> list = new ArrayList<>();
//        while(!q.isEmpty()){
//            // (in order: left -> root -> right) ?????
//            TreeNode cur = q.poll();
//            // ???
//            if(cur.left != null){
//                q.add(cur.left);
//            }
//            // ???
//            list.add(cur);
//            if(cur.right != null){
//                q.add(cur.right);
//            }
//        }
//
//        System.out.println(">>> list = " + list);
//        TreeNode newRoot = list.get(list.size() / 2); // ???
//        // DFS build tree
//
//        return buildBalancedBST(newRoot, list);
//    }
//
//    /**   ???
//     *
//     *  1. find mid point of list
//     *  2. set mid point as root
//     *  3. build tree
//     */
//    private TreeNode buildBalancedBST(TreeNode root, List<TreeNode> list){
//        return null;
//    }


    // LC 2872
    // 18.11 - 30 pm
    /**
     * -> Return the `maximum` number of components in any valid split.
     *
     *
     *   IDEA 1) acc sum + DFS ??
     *
     *
     *
     */
    ///  ??
    int maxComCnt = 0;
    public int maxKDivisibleComponents(int n, int[][] edges, int[] values, int k) {

        Map<Integer, List<Integer>> neighbors = new HashMap<>();

        for(int[] e: edges){
            int start = e[0];
            int end = e[1];
            List<Integer> list1 = new ArrayList<>();
            if(neighbors.containsKey(start)){
                list1 = neighbors.get(start);
            }
            list1.add(end);
            neighbors.put(start, list1);

            List<Integer> list2 = new ArrayList<>();
            if(neighbors.containsKey(end)){
                list2 = neighbors.get(end);
            }
            list2.add(start);
            neighbors.put(end , list2);
        }

        System.out.println(">>> neighbors = " + neighbors);


        return maxComCnt;
    }

    // ???
    private int dfsHelper(Map<Integer, List<Integer>> neighbors, int curNode, int[] values, int k){
        if(neighbors.isEmpty()){
            return 0; // ???
        }
        if(curNode + dfsHelper(neighbors, curNode, values, k) == k){

        }


        return 0; // /??
    }







    // LC 173
    // 16.14 -24 PM
    /**
     * -> Implement the `BSTIterator` class that represents an `iterator `
     *    over the IN-ORDER traversal of a binary search tree (BST):
     *
     *    - in-order traversal
     *
     *    - boolean hasNext(): Returns true if there
     *      exists a number in the traversal to the right of the pointer,
     *      otherwise returns false.
     *
     *    - int next() Moves the pointer to the right,
     *      then returns the number at the pointer.
     *
     */
    class BSTIterator {

        // attr
        List<TreeNode> list;
        TreeNode root;
        int idx;

        public BSTIterator(TreeNode root) {
            this.root = root;
            this.list = new ArrayList<>();
            this.idx = 0;

            buildInorder(root);
        }

        // helper func: inorder: left -> root -> right
        private void buildInorder(TreeNode root){
            if(root == null){
                return;
            }
            buildInorder(root.left);
            list.add(root);
            buildInorder(root.right);
        }

        public int next() {
            // edge
            if(this.idx >= this.list.size()){
                System.out.println("out of boundary");
                return -1;
            }
            TreeNode cur = this.list.get(this.idx);
            this.idx += 1;
            return cur.val;
        }

        public boolean hasNext() {
            return this.idx < this.list.size();
        }

    }


    // LC 272
    // 16.38 - 48 pm
    /**
     * ->  find `k values` in the BST that are closest to the target.
     *
     *
     *  - target value is a floating point.
     *  -  k is always valid, that is: k ≤ total nodes.
     *  - guaranteed to have ONLY ONE UNIQUE set of k values
     *    in the BST that are closest to the target.
     *
     *
     *  IDEA 1) inorder traverse + 2 pointers collect k closest values
     *
     */
    public List<Integer> closestKValues(TreeNode root, double target, int k) {
        List<Integer> res = new ArrayList<>();
        // edge
        if(root == null){
            return res;
        }
        if(k == 0){
            return res;
        }
        if(k == 1 && (root.left == null && root.right == null)){
            res.add(root.val);
            return res;
        }

        List<TreeNode> list = new ArrayList<>();
        buildInorder2(root, list);

        // 2 pointers
        int closetIdx = findCloset(list, target);
        int l = closetIdx - 1;
        int r = closetIdx;
        while(k > 0){
            if(l >= 0){
                res.add(list.get(l).val);
            }
            if(r < list.size()){
                res.add(list.get(r).val);
            }
            l -= 1;
            r += 1;
            k -= 1;
        }

        return res;
    }

    // ??? binary search
    private int findCloset(List<TreeNode> list, double target){
        int l = 0;
        int r = list.size() -1;
        int idx = -1;
        int diff = Integer.MAX_VALUE; // ???
        while(r >= l){
            int mid = (l + r) / 2;
            int val = list.get(mid).val;
            int curDiff = (int) Math.abs(val - target);
            if(curDiff < diff){
                idx = mid;
                diff = curDiff; // ???
            }
        }
        return idx; // ????
    }


    private void buildInorder2(TreeNode root, List<TreeNode> list){
        if(root == null){
            return;
        }
        buildInorder2(root.left, list);
        list.add(root);
        buildInorder2(root.right, list);
    }


    // LC 99
    // 17.27 - 37 pm
    /**
     *
     *
     *
     */
    public void recoverTree(TreeNode root) {
        // edge
        // ???
        if(isValidBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE)){
            return;
        }
        // swap make as valid BST
        // step 1) record prev, cur node
        // step 2) go through BST nodes
        // step 3) if found a `not valid` node,
        //         swap that node with prev node ???

        // ????
        swapBST(null, root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void swapBST(TreeNode prevNode, TreeNode root, int smallest, int biggest){
        if(root == null){
           // return true;
            return;
        }
        if(smallest >= root.val){
            // ???
            TreeNode tmp = prevNode;
            prevNode = root;
            root = tmp;
        }
        if(root.val >= biggest){
            // ???
            TreeNode tmp = prevNode;
            prevNode = root;
            root = tmp;
        }

        // ???
        swapBST(root, root.left, smallest, root.val);
        swapBST(root, root.right, smallest, root.val);
    }

    //???
    private boolean isValidBST(TreeNode root, int smallest, int biggest){
        if(root == null){
            return true;
        }
        if(smallest >= root.val){
            return false;
        }
        if(root.val >= biggest){
            return false;
        }
        return isValidBST(root.left, smallest, root.val) &&
                isValidBST(root.right, root.val, biggest);

    }

    // LC 450
    // 16.37 - 47 pm
    /**
     *  - Delete node in BST
     *
     *  IDEA 1) dfs + key check + delete BST op
     *
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        // edge
        if(root == null){
            return root;
        }
        if(root.left == null && root.right == null){
            if(root.val == key){
                return null;
            }
            return root;
        }
        // collect bst nodes
        List<Integer> list = new ArrayList<>();
        inOrderGetBSTNode(root, list);
        if(!list.contains(key)){
            return root;
        }

        //deleteNodeHelper(root, key);
        return deleteNodeHelper(root, key); // ???
    }

    // 1. check the node val equals key and cache as `cacheNode`
    // 2. go to right sub tree
    // 3. keep going to left sub tree, till reach the end
    // 4. swap `cacheNode` and the cur node
    private TreeNode deleteNodeHelper(TreeNode root, int key){
        if(root == null){
            return null; // ???
        }
        // case 1) root.val > key
        if(root.val > key){
            // ???
            //return deleteNodeHelper(root.left, key);
            // ????
            root.left = deleteNodeHelper(root.left, key);
        }
        // case 2) root.val < key
        else if(root.val < key){
            //return deleteNodeHelper(root.right, key);
            root.right = deleteNodeHelper(root.right, key); // ?????
        }
        // case 3) root.val == key
        else{
            // case 3-1) both sub left AND right is null
            if(root.left == null && root.right == null){
                return null; // /??
            }
            // case 3-1) both sub left AND right is NOT null
            if(root.left != null && root.right != null){
                TreeNode cachedRoot = root;
                TreeNode rightSub = root.right;
                while(rightSub.left != null){
                    rightSub = rightSub.left;
                }
//                // NOTE !!!
//                cachedRoot = rightSub;
//                rightSub = deleteNodeHelper(cachedRoot, rightSub.val); // ????

                /** NOTE BELOW !!! */
                root.val = rightSub.val; // ?????
                root.right = deleteNodeHelper(root.right, rightSub.val);
            }


            // case 3-1)  sub left is NOT null
            if(root.left != null){
                return root.left; // ????
            }
            // case 3-2)  sub right is NOT null
            if(root.right != null){
                return root.right;
            }
        }

        return root; // ????
    }

    // ???
    private void inOrderGetBSTNode(TreeNode root, List<Integer> list){
        if(root == null){
            return;
        }
        inOrderGetBSTNode(root.left, list);
        list.add(root.val);
        inOrderGetBSTNode(root.right, list);
    }


    // LC 94
    List<Integer> list5 = new ArrayList<>();
    public List<Integer> inorderTraversal(TreeNode root) {
        if(root == null){
            return list5;
        }
        inorderTraversal(root.left);
        list5.add(root.val);
        inorderTraversal(root.right);
        return list5;
    }




}
