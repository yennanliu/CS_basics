package dev;

import LeetCodeJava.DataStructure.TreeNode;

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


    // LC 1109
    // 17.20 - 30 pm
    /**
     *   - Return an array answer of length n,
     *     where answer[i] is the `total number` of seats
     *     reserved for flight i.
     *
     *  - n flights from 1 to n
     *  - bookings[i] = [firsti, lasti, seatsi]
     *
     *  IDEA 1) DIFF ARRAY
     *
     *   -> loop over booking in bookings, update cache with below:
     *     - for open idx, val += cur_val
     *     - for close idx, val -= cur_val
     *
     *   -> loop over cache with below:
     *    - do the `accumulated sum`
     *    - update the cur `accumulated sum` to cur idx
     *
     *   -> return cache as final result
     *
     */
    public int[] corpFlightBookings(int[][] bookings, int n) {
        // edge
        if(bookings.length == 0 || bookings[0].length == 0 || n == 0){
            return new int[0]; // ????
        }
        if(n == 1){
            return bookings[0]; // /??
        }
        //List<Integer> cache = new ArrayList<>();
        int[] cache =new int[ n + 1]; // ???? // new int[n]; //???

        for (int[] b: bookings){
            int start = b[0];
            int end = b[1];
            int seat = b[2];
            // ???
            //cache.set(start, seat);
            cache[start - 1] = seat;
            // ???
            if(end < cache.length){
               // cache.set(end+1, -1 * seat);
                cache[end] = - 1 * seat;
            }
        }

        System.out.println(">>> cache = " + cache);

        //List<Integer> tmp = new ArrayList<>();
        int[] tmp = new int[n]; //???
        int accSum = 0; // /??

        for(int i = 0; i < cache.length; i++){
           // accSum += cache.get(i);
            accSum += cache[i];
            //tmp.set(i, accSum);
            tmp[i] = accSum;
        }

        System.out.println(">>> tmp = " + tmp);
//
//        int[] res = new int[tmp.size()];
//        for(int j = 0; j < tmp.size(); j++){
//            res[j] = tmp.get(j);
//        }

        return tmp;
    }


    // LC 1094
    // 15.51 - 18.01 pm
    /**
     *  -> Return true if it is possible to pick up
     *    and drop off all passengers for all the given trips,
     *    or false otherwise.
     *
     *
     *  - There is a car with capacity empty seats.
     *     The vehicle ONLY drives EAST
     *
     *   - trips[i] = [numPassengersi, fromi, toi]
     *      - i th trip
     *      - # of passenger
     *      - start from `from`
     *      - end at `to`
     *
     *
     * IDEA 1) DIFF ARRAY
     *
     *
     */
    public boolean carPooling(int[][] trips, int capacity) {
        if(trips.length == 0 || trips[0].length == 0 || capacity == 0){
            return true;
        }
        if(trips.length == 1){
            return capacity >= trips[0][0];
        }
        // ???
        int maxEnd = 0;
        for(int[] t: trips){
            maxEnd = Math.max(maxEnd, t[2]);
        }
        int[] diffArr = new int[maxEnd + 1]; // ??

        for(int i = 0; i < trips.length; i++){
            int passengers = trips[i][0];
            int start = trips[i][1];
            int end = trips[i][2];
            diffArr[start] += passengers;
            if(end < diffArr.length){
                diffArr[end] -= passengers;
            }
        }

        for(int i = 1; i < diffArr.length; i++){
            diffArr[i] = diffArr[i - 1] + diffArr[i]; // ???
        }

        for(int i = 0; i < diffArr.length; i++){
            if(diffArr[i] > capacity){
                return false;
            }
        }

        return true;
    }



    // LC 785
    /**
     *
     *  -> Return true if and only if it is bipartite.
     *
     *
     *
     *  IDEA 1) DFS
     *    - 3 states:
     *      - 0: not visited
     *      - 1: visiting
     *      - 2: visited
     *    -> via DFS, we can check if it's possible to split graph
     *       into 2 group ???
     *
     *
     *  IDEA 2) BFS ???
     *
     *
     */
    // 11.05 - 15 am
    public boolean isBipartite(int[][] graph) {
        // edge
        if (graph == null || graph.length == 0) {
            return true;
        }
        // ???
        if(graph.length == 1 || graph[0].length == 1){
            return true;
        }

        Set<Integer> set = new HashSet<>();
        for(int[] g: graph){
            set.add(g[0]);
            set.add(g[1]);
        }

        /** team status
         *
         *  - 0: NOT assigned
         *  - 1: team A
         *  - -1: team B
         */
        int[] teamStatus = new int[set.size()];

        // neighbor map
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int i = 0; i < set.size(); i++){
            map.put(i, new ArrayList<>());
        }
        // update map
        for(int[] g: graph){
            int from = g[0];
            // ???
            for(int i = 1; i < g.length; i++){
                map.get(from).add(g[i]);
            }
        }

        System.out.println(">>> map = " + map + ", teamStatus = " + teamStatus);

        for(int i = 0; i < set.size(); i++){
            if(teamStatus[i] == 0){
                if(!isBipartiteHelper(i, teamStatus, map, 1)){
                    return false;
                }
            }
        }

        return true;
    }


    private boolean isBipartiteHelper(int node, int[] teamStatus, Map<Integer, List<Integer>> map, int team){
        if(teamStatus[node] != 0){
            return teamStatus[node] == team;
        }
        // color (assign to team)
        teamStatus[node] = team;
        // loop over neighbors
        for(int neighbor: map.get(node)){
            if(!isBipartiteHelper(neighbor, teamStatus, map, -1 * team)){
                return false;
            }
        }

        return true;
    }






//    // IDEA 3) DFS + NEIGHBOR COLOR CHECK
//    public boolean isBipartite(int[][] graph) {
//        // edge
//        if (graph == null || graph.length == 0) {
//            return true;
//        }
//        // ???
//        if(graph.length == 1 || graph[0].length == 1){
//            return true;
//        }
//
//        /** NOTE !!!
//         *
//         *
//         * We use 3 states:
//         *   0 -> not colored yet
//         *   1 -> color A
//         *  -1 -> color B
//         */
//        int[] colorState = new int[graph.length]; // ???
//
//        // ??? apply dfs color check on every node
//        for(int i = 0; i < graph.length; i++){
//            // NOTE !!! ONLY call the dfs if `uncolored`
//            // e.g. if color state = 0
//            if(colorState[i] == 0){
//                // NOTE !!! color as `1` color
//                if(!dfsNeighborColorCheck(graph, colorState, i, 1)){
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }
//
//    // int[][] graph, int[] color, int node, int c
//    private boolean dfsNeighborColorCheck(int[][] graph, int[] colorState, int node, int c){
////        if(colorState[node] == c){
////            return false; // ??? conflict
////        }
////        if(colorState[node] == -1 * c){
////            return true; // ??? OK ? since it's `different color`
////        }
//        // NOTE !!!! // if already colored, check consistency
//        // NOTE !!! here we are NOT checking neighbor color
//        // but check if the same node has conflict on color
//        // e.g. since the node colored already, we check if
//        // the new color is DIFFERENT from the prev color
//        if (colorState[node] != 0) {
//            // if color conflict, return false
//            return colorState[node] == c;
//        }
//
//        // NOTE !!! we color cur node
//        colorState[node] = c;
//
//        // visit neighbors
//        for(int neighbor: graph[node]){
//            // NOTE !!! since we mark cur node as `1 color`
//            // so we should check if its neighbor node can has `different color`
//            // e.g. `-1 color`
//            if(!dfsNeighborColorCheck(graph, colorState, neighbor, -1 * c)){
//                return false;
//            }
//        }
//
//        return true;
//    }





//    public boolean isBipartite(int[][] graph) {
//        // edge
//        if(graph == null || graph.length == 0 || graph[0].length == 0){
//            return true;
//        }
//        // ???
//        if(graph.length == 1 || graph[0].length == 1){
//            return true;
//        }
//
//        // build graph
//        // { node: [neighbor_1, neighbor_2,...] }
//        Map<Integer, List<Integer>> map = new HashMap<>();
//        for(int[] g: graph){
//            int start = g[0];
//            int end = g[1];
//
//            List<Integer> list1 = new ArrayList<>();
//            List<Integer> list2 = new ArrayList<>();
//
//            if(map.containsKey(start)){
//                list1 = map.get(start);
//            }
//            list1.add(end);
//            map.put(start, list1);
//
//            if(map.containsKey(end)){
//                list2 = map.get(end);
//            }
//            list2.add(start);
//            map.put(end, list2);
//        }
//
//        /**
//         *      *    - 3 states:
//         *      *      - 0: not visited
//         *      *      - 1: visiting
//         *      *      - 2: visited
//         *      *    -> via DFS, we can check if it's possible to split graph
//         *      *       into 2 group ???
//         */
//        //Boolean[] visited = new Boolean[map.keySet().size()]; // ????
//        // ???
//        Integer[] status = new Integer[map.keySet().size()];
//        for(Integer k: map.keySet()){
//            if(!canSplit(map, status, k)){
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    private boolean canSplit(Map<Integer, List<Integer>> map, Integer[] status, int node){
//        if(map.isEmpty()){
//            return true; // /??
//        }
//        if(status[node] == 1){
//            return false;
//        }
//        if(status[node] == 2){
//            return true; // ???
//        }
//        // mark as visiting
//        status[node] = 1;
//
//        // loop over neighbors
//        for(Integer x: map.get(node)){
//            // ????
//            if(!canSplit(map, status, x)){
//                return false;
//            }
//        }
//
//        // mark as visited
//        status[node] = 2;
//
//        return false;
//    }

    // LC 886
    // 9.16 - 26 am
    /**
     *
     *   -> return true if it;s possible to split to 2 groups
     *
     *  - split a group of n people (labeled from 1 to n)
     *     into TWO groups of ANY size
     *
     *   - Each person may dislike some other people,
     *      -> they should NOT go into the SAME group.
     *
     *    - dislikes where dislikes[i] = [ai, bi]
     *        -> a DISLIKE B
     *
     *
     *   IDEA 1) DFS
     *
     *   IDEA 2) BFS
     *
     *
     */
    // IDEA 1) DFS
    // 10.28 - 38 am
    public boolean possibleBipartition(int n, int[][] dislikes) {
        // edge
        if(dislikes == null || dislikes.length == 0 || dislikes[0].length == 0){
            return false;
        }
        if(n <= 1){
            return true;
        }

        // dislike map
        Map<Integer, List<Integer>> dislikeMap = new HashMap<>();
        // NOTE !!! below init way
        // NOTE !!!  n is 1 based on this problem
        for(int i = 1; i < n + 1; i++){
            dislikeMap.put(i, new ArrayList<>());
        }
        for(int[] d: dislikes){
            int a = d[0];
            int b = d[1];

            // V1
//            List<Integer> list1 = dislikeMap.get(a);
//            List<Integer> list2 = dislikeMap.get(b);
//
//            list1.add(b);
//            list2.add(a);
//
//            dislikeMap.put(a, list1);
//            dislikeMap.put(b, list2);

            // V2
            dislikeMap.get(a).add(b);
            dislikeMap.get(b).add(a);
        }

        /**
         *  group:
         *   - 0: not assigned yet
         *   - 1: group 1
         *   -  -1 : group 2 ????
         *
         */
        // group status list
        int[] groupStatus = new int[n];

        for(int i = 0; i < n; i++){
            if(groupStatus[i] == 0){
                // assign to team 1 as deault
                if(!assignTeamDFS(n, i, groupStatus, dislikeMap, 1)){
                    return false;
                }
            }
        }

        return true;
    }

    private boolean assignTeamDFS(int n, int node, int[] groupStatus, Map<Integer, List<Integer>> dislikeMap, int team){
        // check if already assigned
        if(groupStatus[node] != 0){
            // check if conflict ???
            if(groupStatus[node] != -1){
                return false; // ???
            }
//            // check if other team already has such node
//            if(dislikeMap.containsKey(-1 * team)){
//                return false;
//            }
            // if the same team
            return true; // ??
        }

        // assign team to cur node
        // NOTE !!! assign team to the cur node
        groupStatus[node] = team; // ???

//        // loop over other people
//        for(int i = 0; i < n; i++){
//            if(!assignTeamDFS(n, i, groupStatus, dislikeMap, -1 * team)){
//                return false;
//            }
//        }

        // NOTE !!! loop over `dislike neighbors`
        // NOTE !!! since the neighbor are the people NOT like by current node
        // so we need to assign them to different team
        // do above via dfs call, (and validate if it's possible)
        for(int neighbor: dislikeMap.get(node)){
            if(!assignTeamDFS(n, neighbor, groupStatus, dislikeMap, -1 * team)){
                return false;
            }
        }

        return true;
    }





//    // IDEA 1) DFS
//    public boolean possibleBipartition(int n, int[][] dislikes) {
//        // edge
//        if(dislikes == null || dislikes.length == 0 || dislikes[0].length == 0){
//            return false;
//        }
//        if(n <= 1){
//            return false;
//        }
//
//        // ???
//        Set<Integer> people = new HashSet<>();
//        /**
//         *
//         *
//         */
//        Map<Integer, List<Integer>> dislikeMap = new HashMap<>();
//        for(int[] d: dislikes){
//            int a = d[0];
//            int b = d[1];
//            List<Integer> list1 = new ArrayList<>();
//            List<Integer> list2 = new ArrayList<>();
//
//            // ???
//            people.add(a);
//            people.add(b);
//
//            if(dislikeMap.containsKey(a)){
//                list1 = dislikeMap.get(a);
//            }
//            list1.add(b);
//            dislikeMap.put(a, list1);
//
//            if(dislikeMap.containsKey(b)){
//                list2 = dislikeMap.get(b);
//            }
//            list2.add(a);
//            dislikeMap.put(b, list2);
//        }
//
//        Map<Integer, List<Integer>> splitGroup = new HashMap<>();
//
//        for(int x: people){
//            // int person, Set<Integer> people, Map<Integer, List<Integer>> dislikeMap, Map<Integer, List<Integer>> splitGroup, int group
//            // assign to group 0 as deafult (e.g. NOT visited yet)
//            if(!canSplitCheck(x, people, dislikeMap, splitGroup, 0)){
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     *  person: cur person
//     *  dislikeMap: this person - dislike list map
//     *  splitGroup:  the group we split people in
//     *  group: the group id:
//     *       - group 0: NOT assigned yet
//     *       - group 1: group #1
//     *       - group 2: group #2
//     */
//    private boolean canSplitCheck(int person, Set<Integer> people, Map<Integer, List<Integer>> dislikeMap, Map<Integer, List<Integer>> splitGroup, int group){
//        // check if conflicts:
//        //if(dislikeMap.get(person))
//        if(group != 0){
//            // if NOT conflict, the `splitGroup` with same group should have such person
//            return splitGroup.get(group).contains(person);
//        }
//
//        // put the person into a group
//        // chose group 1 as default
//        List<Integer> list1 = new ArrayList<>();
//        if(splitGroup.containsKey(1)){
//            list1 = splitGroup.get(1);
//        }
//        list1.add(person);
//        splitGroup.put(person, list1);
//
//
//        // navigate to other person
//        for(int next: people){
//            // if `NOT dislike`, call DFS
//            if(!dislikeMap.get(person).contains(next)){
//                // int person, Set<Integer> people, Map<Integer, List<Integer>> dislikeMap, Map<Integer, List<Integer>> splitGroup, int group
//                // since we assign `group 1` to cur person,
//                // so for next people, we SHOULD assign them to `group 2`
//                if(!canSplitCheck(next, people, dislikeMap, splitGroup, 2)){
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }


    // LC 1145
//    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {
//
//        return false;
//    }

    // LC 2640
    // 6.30 - 40 am
    public long[] findPrefixScore(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return new long[0];
        }

        long[] cache = new long[nums.length];
        long[] res = new long[nums.length];
        int maxTillNow = 0;
        for(int i = 0; i < nums.length; i++){
            //accSum += nums[i];
            /**
             * conver[i] = arr[i] + max(arr[0..i])
             * where max(arr[0..i]) is the maximum
             * value of arr[j] over 0 <= j <= i.
             */
            maxTillNow = Math.max(maxTillNow, nums[i]);
            cache[i] = (nums[i] + maxTillNow);
        }

        int accSum = 0;
        for(int i = 0; i < cache.length; i++){
            accSum += cache[i];
            res[i] = accSum;
        }


        return res;
    }


    // LC 2641
    // 7.14 - 24 am
    /**
     *   IDEA 1) BFS
     *
     *   IDEA 2) DFS
     *
     *
     */
    //  IDEA 1) BFS
    /**
     *   1. BFS visit layer by layer
     *   2. get the `layer sum` list
     *    e.g. [sum_1, sum_2,...], idx is the layer
     *   3. check if the cousin exists
     *      -. if yes, sum val up the other nodes
     *      NOTE !!! the `cousin sum` = layer_sum - cur node val
     *      so we can use the layer sum list we got above, with the cur node
     *      to get the cousin sum
     *      - set 0 otherwise
     *   4. repeat above steps
     *
     */
    public TreeNode replaceValueInTree(TreeNode root) {
        // edge
        if (root == null){
            return null;
        }

        // v1: queue: [ [node, layer], ...]
        // v2: queue: [ node1, node2, ... ]
        // v3: queue: [ [parent1 node1] ,  [parent2 node2] , ... ]
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        List<Integer> layerSum = new ArrayList<>();
        while(!q.isEmpty()){
            int size = q.size();
            int tmpSum = 0;
            for(int i = 0; i < size; i++){
                TreeNode cur = q.poll();
                tmpSum += cur.val;
                if (cur.left != null){
                    q.add(cur.left);
                }
                if (cur.right != null){
                    q.add(cur.right);
                }
            }
            layerSum.add(tmpSum);
        }

        System.out.println(">>> layerSum = " + layerSum);

        // ??? init root val as 0 (since root MUST has not cousin)
        // NOTE !!! update root val DOES NOT affect its sub node relation
        // e.g. root.left, root.right still unchanged
        root.val = 0;

        int layer = 0;
        while(!q.isEmpty()){
            int size = q.size();
            // init tmp list
            List<Integer> list = new ArrayList<>();
            for(int i = 0; i < size; i++){
                TreeNode cur = q.poll();
                list.add(cur.val);
                if (cur.left != null){
                    q.add(cur.left);
                }
                if (cur.right != null){
                    q.add(cur.right);
                }
                // op ????

            }

            layer += 1;
        }


        return root; // ?????
    }


    // LC 2642
    // 18.11 - 21 pm
    /**
     *   IDEA 1) Dijkstra algo
     */
    class Graph {

        // attr
        // small PQ:  { [node, cost], .... } ???
        // PQ sort based on `cost`
        PriorityQueue<int[]> pq;

        // map: { node : [ [neighbor_1, cost_1], [neighbor_2, cost_2], .... ] }
        Map<Integer, List<int[]>> map;

        public Graph(int n, int[][] edges) {
            this.pq = new PriorityQueue<>(new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    int diff = o1[1] - o2[1];
                    return diff;
                }
            });

            // ???
            this.map = new HashMap<>();
            for(int i = 0; i < n; i++){
                this.map.put(i, new ArrayList<>());
            }

            // NOTE !!! we need to init edges
            for(int[] e: edges){
                addEdge(e);
            }

        }

        public void addEdge(int[] edge) {
            int from = edge[0];
            int to = edge[1];
            int cost = edge[2];

            // update map
            int[] tmp = new int[]{to, cost};
            this.map.get(from).add(tmp); // /????
        }

        public int shortestPath(int node1, int node2) {
            // edge
            if(this.map.isEmpty()){
                return 0;
            }
            if(node1 == node2){
                return 0;
            }

            // run Dijkstra
            int cost = 0;

            this.pq.add(new int[]{node1, 0}); // /???
            // ???
            while(!pq.isEmpty()){
                int[] cur = this.pq.poll();
                int curNode = cur[0];
                int curCost = cur[1];
                // if reach `node2`, early exit
                if(curNode == node2){
                    return curCost;
                }
                // ????
                if(this.map.containsKey(curNode)){
                    // loop over cur node neighbor
                    for(int[] x: this.map.get(curNode)){
                        int nextNode = x[0];
                        int nextCost = x[1];
                        // ???
                        if(nextCost + cost < cost){
                            this.pq.add(new int[]{nextNode, nextCost + cost});
                        }
                    }
                }

            }

            return -1; // if CAN'T go from node1 to node2
        }

    }

    // LC 215
    public int findKthLargest(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return -1; // ???
        }
        if(nums.length == 1){
           if(k == 1){
               return nums[0]; // ?????
           }
           return -1;
        }
        // big PQ
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        for(int x: nums){
            pq.add(x);
        }


        // pop
        int j = 0;
        int res = -1;
        while(j < k){
            res = pq.poll();
            j += 1;
        }

        return res;
    }


    // LC 373
    // 7.05 - 15 am
    /**
     *
     *  -> Return the k pairs (u1, v1), (u2, v2), ..., (uk, vk) with the SMALLEST sums.
     *
     *   - Nums1 and nums2 sorted in `non-decreasing `order
     *     and an integer k.
     *
     *   - Define a pair (u, v) which consists of one element
     *     from the first array and one element from the second array.
     *
     *  IDEA 1) PQ
     *
     *
     */
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        // edge
        if(nums1 == null && nums2 == null){
            return null;
        }
        // ???
        if(nums1 == null || nums2 == null){
            return null;
        }
//        if(k > nums1.length || k > nums2.length){
//            return null; // ????
//        }

        // PQ: big PQ over int sum
        PriorityQueue<Integer[]> bigPQ = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                // ???
                //int diff = (o1[0] + o1[1]) - (o2[0] + o2[1]);
                int diff = (o2[0] + o2[1]) - (o1[0] + o1[1]);
                return diff;
            }
        });

        // brute force - double loop // TODO: optimize
        // i: loop over nums1
        // j: loop over nums2
        // ???

        // reverse nums1, nums2 ???

        for(int i = 0; i < nums1.length; i++){
            for(int j = 0; j < nums2.length; j++){
                System.out.println(">>> i = " + i + ", j = " + j);
                int val1 = nums1[i];
                int val2 = nums2[j];
                bigPQ.add(new Integer[]{val1, val2});

                // if PQ size > k, remove the `top biggest ones`
                while(bigPQ.size() > k){
                    bigPQ.poll();
                }

            }
        }


        // define the other small PQ
        // that get all values from big PQ
        // so we can return top K small pairs

        // PQ: small PQ over int sum
        PriorityQueue<Integer[]> smallPQ = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                // ???
                int diff = (o1[0] + o1[1]) - (o2[0] + o2[1]);
                return diff;
            }
        });

        while(!bigPQ.isEmpty()){
            smallPQ.add(bigPQ.poll());
        }


        System.out.println(">>> bigPQ = " + bigPQ + ", smallPQ = " + smallPQ);

        List<List<Integer>> res = new ArrayList<>();

        while(k > 0){
            Integer[] arr = smallPQ.poll();
            List<Integer> tmp = new ArrayList<>();
            tmp.add(arr[0]);
            tmp.add(arr[1]);
            res.add(tmp);
            k -= 1;
        }



        return res;
    }


    // LC 973
    // 10.03 - 13 am
    /**
     *
     *  ->  return the k closest points to the origin (0, 0).
     *
     *
     *  IDEA 1) PQ ??
     *
     *  IDEA 2) SORT ???
     *
     *
     */
    public int[][] kClosest(int[][] points, int k) {
        // edge
        if(points == null || points.length == 0 || points[0].length == 0){
            return null;
        }
        // big PQ
        // { [x, y, dist] }
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[2] - o1[2];
                return diff;
            }
        });

        for(int[] p: points){
            int x = p[0];
            int y = p[1];
            int dist = (x * x) + (y * y);
            pq.add(new Integer[]{x, y, dist});
            // pop elements with PQ size > K
            while(pq.size() > k){
                pq.poll();
            }
        }

        int[][] res = new int[k][2]; // ???
        for(int i = 0; i < k; i++){
            Integer[] arr = pq.poll();
            int x_ = arr[0];
            int y_ = arr[1];
            res[i] = new int[]{x_, y_};
        }

        return res;
    }


    // LC 378
    // 10.28 - 38 am
    /**
     *
     *  -> Return the kth smallest element in the matrix.
     *
     *  -  n x n matrix where each of the rows
     *     and columns is sorted in ascending order, (small -> big)
     *
     *  - Note that it is the kth smallest element in the sorted order,
     *    NOT the kth DISTINCT element.
     *
     *
     *  IDEA 1) PQ
     *
     *  IDEA 2) SORT
     *
     *  IDEA 3) BINARY SEARCH ????
     *
     */
    // BINARY SEARCH
    // 11.10 - 20 am
    public int kthSmallest(int[][] matrix, int k) {
        // edge
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0){
            return 0; // ???
        }
        if(k == 1){
            return matrix[0][0];
        }

        // adjust the k. since the matrix is rows and columns is sorted in ascending order, // /??
        int l = matrix.length;
        int w = matrix[0].length;




        return 0;
    }

    // brute force + PQ ???
//    public int kthSmallest(int[][] matrix, int k) {
//        // edge
//        if(matrix == null || matrix.length == 0 || matrix[0].length == 0){
//            return 0; // ???
//        }
//        if(k == 1){
//            return matrix[0][0];
//        }
//        // adjust the k. since the matrix is rows and columns is sorted in ascending order, // /??
//        int l = matrix.length;
//        int w = matrix[0].length;
//
//        //int adjustedK = k % l;
//
//        // big PQ
//        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                //int diff = o1 - o2;
//                int diff = o2 - o1;
//                return diff;
//            }
//        });
//
//        // loop over row in matrix
//        // check if the possible answer in in the specific row
//        //int size = 0;
//        for(int i = 0; i < l; i++){
//            for(int j = 0; j < w; j++){
//                pq.add(matrix[i][j]);
//               // size += 1;
//                System.out.println(">>> pq = " + pq);
//                // early quit
//                // NOTE !!! use pq.size() directly
//                if(pq.size() == k){
//                    int val = pq.poll();
//                    return val;
//                }
//            }
//        }
//
//        return -1; // SHOULD NOT visit here ???
//    }


    // LC 703
    // 16.34 - 44 pm
    /**
     *
     *  Implement the KthLargest class:
     *
     *  - KthLargest(int k, int[] nums):
     *      Initializes the object with the integer k
     *      and the stream of test scores nums.
     *
     *
     *   - int add(int val):
     *     Adds a new test score val to
     *     the stream and returns the element representing
     *     the `kth largest element` in the pool of test scores so far.
     *
     *
     *
     *  IDEA 1) PQ
     *
     *  IDEA 2) BRUTE FORCE
     *
     */
    class KthLargest {

        // attr
        PriorityQueue<Integer> pq;
        int size;
        int k;

        public KthLargest(int k, int[] nums) {
            this.k = k;
            this.size = 0; // ??

            // `kth largest element`
            // so we need `SMALL PQ` and keep max size as `k`
            // PQ in java is small PQ by default ?????
            this.pq = new PriorityQueue<>();
            for(int x: nums){
                this.size += 1;
                pq.add(x);
            }
//            // pop
//            while(this.pq.size() > k){
//                this.size -= 1;
//                this.pq.poll();
//            }
        }

        public int add(int val) {
            this.pq.add(val);
            // pop
            while(this.pq.size() > k){
                this.size -= 1;
                this.pq.poll();
            }
            if(this.pq.isEmpty()){
                //return -1; //???
                throw new RuntimeException("empty PQ");
            }

            // ???
            return pq.peek();
        }
    }

    // LC 264
    // 16.56 - 17.06 pm
    /**
     *  -> Given an integer n, return the `nth ugly number.`
     *
     *
     *
     *  - An ugly number is a positive integer
     *     whose prime factors are limited to 2, 3, and 5.
     *
     *
     *  IDEA 1) MATH + BRUTE FORCE
     *
     *  IDEA 2) PQ ????
     *
     *  IDEA 3)  DP ???
     *
     *
     *
     *  ---------
     *
     *
     *  exp 1)
     *
     *   Input: n = 10
     *   Output: 12
     *   Explanation: [1, 2, 3, 4, 5, 6, 8, 9, 10, 12]
     *     is the sequence of the first 10 ugly numbers.
     *
     *
     *
     *  -> [2,3,5]
     *
     *          2          3          5
     *       2  3 5     2  3 5    2  3 5
     *
     * -> 4 , 6, 10      6,9,15     10,15,25
     *
     *
     *
     * -> loop 2, 3, 5 k times
     *
     *
     */
    public int nthUglyNumber(int n) {
        // edge
        if(n <= 1){
            return n; // ???
        }

        // NOTE !! since we want to return n-th ugly number
        // so it's like `n biggest number` from a list
        // -> we need a SMALL PQ, and keep the PQ with
        //    max size = n
        // NOTE !!! PQ is small PQ in java by default

        // use big PQ ????
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });
        // ??
        for(int i = 1; i < 1700; i++){
            if(isUgly(i)){
                pq.add(i);
            }
        }
        System.out.println(">>> (before pop) pq = " + pq);
        // keep the PQ max size as n
        while(pq.size() > n){
            pq.poll();
        }

        // edge
        if(pq.isEmpty()){
            throw new RuntimeException("empty PQ");
        }
        System.out.println(">>> (after pop) pq = " + pq);

        return pq.peek();
    }

    private boolean isUgly(int x){
        if(x == 1){
            return true;
        }
        // ??
        while(x > 1){
            if(x % 2 == 0){
                x = (x / 2);
            }
            else if(x % 3 == 0){
                x = (x / 3);
            }
            else if(x % 5 == 0){
                x = (x / 5);
            }else{
                return false; // ???
            }
        }
        return true;
    }


    // LC 2594
    // 17.39 - 53 PM
    /**
     *  -> Return the` minimum time` taken to repair ALL the cars.
     *     (min needed time)
     *
     *   - Note: All the mechanics can repair the cars simultaneously
     *   - ranks : the ranks of some mechanics
     *   - ranksi: is the rank of the ith mechanic.
     *       - A mechanic with a rank r can repair n cars in r * n2 minutes.
     *
     *
     *   - IDEA 1) PQ
     *      - add the `time cost to fix a car with a mechanic`
     *      - use small PQ
     *      - / Create a Min-heap storing [time, rank, n, count] ?????
     *
     *   - IDEA 2) BINARY SEARCH ???
     *
     *
     *
     *   ex 1)
     *   Input: ranks = [4,2,3,1], cars = 10
     *   Output: 16
     *
     *    -> small PQ: [ 4,2,3,1]
     *
     *    -> cur
     *
     *
     */
    public long repairCars(int[] ranks, int cars) {

        return 0L;
    }

    // LC 2593
    // 18.18 - 28 PM
    
    public long findScore(int[] nums) {

        return 0L;
    }


    // LC 506
    // 6.41 - 51 am
    /**
     *   -> Return an `array` answer of size `n` where
     *     answer[i] is the rank of the ith athlete.
     *
     *
     *   - score arr with size n
     *   - score[i[:  score of i-th athlete
     *   - All scores are unique
     *   - 1st: highest  score
     *   - 2nd: 2nd highest score ...
     *   -
     *
     *
     *
     *   ex 1)
     *
     *   Input: score = [5,4,3,2,1]
     *   Output: ["Gold Medal","Silver Medal","Bronze Medal","4","5"]
     *
     *
     *   ex 2)
     *
     *  Input: score = [10,3,8,9,4]
     *  Output: ["Gold Medal","5","Bronze Medal","Silver Medal","4"]
     *
     *
     *
     *   IDEA 1) GET & SORT + HASHMAP
     *
     *   IDEA 2) PQ
     *
     *
     *
     *
     */
    // IDEA 2) PQ
    public String[] findRelativeRanks(int[] score) {
        // edge
        if(score.length == 1){
            return new String[]{"Gold Medal"};
        }
        String GOLD = "Gold Medal";
        String SILVER = "Silver Medal";
        String BRONE = "Bronze Medal";

        // PQ: big PQ
        // NOTE !!! PQ needs to record { idx, val }
        // PQ: { idx, val }
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[1] - o1[1];
                return diff;
            }
        });

        for(int i = 0; i < score.length; i++){
            int s = score[i];
            pq.add(new Integer[] {i, s});
        }

        String[] res = new String[pq.size()];
        int cnt = 0;
        while(!pq.isEmpty()){

            Integer[] cur = pq.poll();
            int idx = cur[0];
            int val = cur[1];

            if(cnt == 0){
                res[idx] = GOLD;
            }
            else if(cnt == 1){
                res[idx] = SILVER;
            }
            else if(cnt == 2){
                res[idx] = BRONE;
            }
            else{
                res[idx] = String.valueOf(cnt);
            }
            cnt += 1;
        }


        return res;
    }


    // LC 767
    // 7.08 - 18 am
    /**
     *  -> Return any `possible rearrangement` of s
     *     or return "" if not possible.
     *
     *     - Given a string s, rearrange the characters of s
     *       so that `any two adjacent characters` are NOT the same.
     *
     *   IDEA 1) HASHMAP + QUEUE ??? (FILO)
     *       - map record cnt of val
     *       - queue: a simple FILO mechanisms that can loop over
     *                possible string candidates and add them to res
     *
     *
     *
     *       - PQ record the `the cnt of val, from most to least (big -> small)`
     *
     *
     *   IDEA 2) BRUTE FORCE ???
     *
     *
     *
     *  ----
     *
     *   ex 1)
     *   Input: s = "aab"
     *   Output: "aba"
     *
     *   -> map = { a: 2, b: 1}
     *
     *   cur = a,   map = { a: 1, b: 1}
     *   cur = ab,   map = { a: 1}
     *   cur = aba
     *
     *
     *
     *
     *
     */
    // IDEA: PQ + HASH MAP
    public String reorganizeString(String s) {
        // edge
        if (s == null || s.isEmpty()){
            return "";
        }
        if (s.length() <= 2){
            return s;
        }

        // hashmap
        // map: {val: cnt}
        Map<Character, Integer> map = new HashMap<>();
        for(char x: s.toCharArray()){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        // PQ: big PQ. sort on count (freq)
        // big PQ:
        // NOTE !!!  sort on MAP val(freq)
//        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = map.get(o2) - map.get(o1);
//                return diff;
//            }
//        });
        PriorityQueue<Character> pq = new PriorityQueue<>(new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                int diff = map.get(o2) - map.get(o1); // ????
                return diff;
            }
        });

        // add vals to PQ
        for(Character k: map.keySet()){
            pq.add(k);
        }

        // NOTE !!! define prev
        Character prev = null;
        String res = "";

        // NOTE !!! while PQ is NOT empty
        while(!pq.isEmpty()){

            //Integer cnt = pq.poll();
            Character cur = pq.poll();

            // case 1)
            if(prev != null && prev == cur){
                if(pq.isEmpty()){
                    return "";
                }
                char next = pq.poll();
                res += String.valueOf(next);

                map.put(next, map.get(next) - 1);
                if(map.get(next) > 0){
                    pq.add(next);
                }

                pq.add(cur);
                prev = next;
            }else{
                res += String.valueOf(cur);

                map.put(cur, map.get(cur) - 1);
                if(map.get(cur) > 0){
                    pq.add(cur);
                }

                prev = cur;
            }

//            if(pq.isEmpty()){
//                // case 1-1) PQ is empty and prev == cur
//                if(prev.equals(curChar)){
//                    return ""; // ????
//                }
//                // case 1-2) PQ is empty and prev != cur
//                res += curChar; // ???
//                prev = String.valueOf(curChar); // ???
//            }else{
//                // case 2-1) PQ is NOT empty and prev == cur
//                // ???
//                if(prev.equals(String.valueOf(curChar))){
//                    // try `2nd candidates`
//                    Character nextChar = pq.poll();
//
//                    // add to res
//                    res += nextChar;
//                    // update map
//                    map.put(nextChar, map.get(nextChar) - 1);
//                    // if cnt == 0, remove key
//                    if(map.get(nextChar) == 0){
//                        map.remove(nextChar);
//                    }
//
//                    // NOTE !!! add the `1st candidates` back to PQ
//                    pq.add(curChar);
//                }
//                // case 2-1) PQ is NOT empty and prev != cur
//                else{
//                    res += curChar;
//                    // update map
//                    map.put(curChar, map.get(curChar) - 1);
//                    // if cnt == 0, remove key
//                    if(map.get(curChar) == 0){
//                        map.remove(curChar);
//                    }
//                }
//            }


        }

        return res;
    }





//    // custom class
//    public class StringCnt{
//        String str;
//        Integer cnt;
//        public StringCnt(String str, Integer cnt){
//            this.str = str;
//            this.cnt = cnt;
//        }
//    }
//    public String reorganizeString(String s) {
//        // edge
//        if(s.isEmpty()){
//            return "";
//        }
//        if(s.length() == 1){
//            return "";
//        }
//
//        // map: {val: cnt}
//        Map<Character, Integer> map = new HashMap<>();
//        for(char x: s.toCharArray()){
//            map.put(x, map.getOrDefault(x, 0) + 1);
//        }
//
//        // big PQ: sort on cnt (freq)
//        PriorityQueue<StringCnt> pq = new PriorityQueue<>(new Comparator<StringCnt>() {
//            @Override
//            public int compare(StringCnt o1, StringCnt o2) {
//                int diff = o2.cnt - o1.cnt;
//                return diff;
//            }
//        });
//
//        for(Character k: map.keySet()){
//            String k_ = String.valueOf(k);
//            Integer cnt = map.get(k);
//            pq.add(new StringCnt(k_, cnt));
//        }
//
//        String res = ""; // ????
//
//        // NOTE !!! we define prev
//        String prev = null;
//
//        // NOTE !!! while PQ is not null
//        while (!pq.isEmpty()){
//
//            StringCnt sc = pq.poll();
//            int cnt = sc.cnt;
//            String str = sc.str;
//
//            // edge
//            if(prev == null || prev.equals(str)){
//                if(pq.isEmpty()){
//                    return null;
//                }
//            }
//
//            StringCnt scNext = pq.poll();
//            res += scNext.str;
//           // map.put(scNext.str, scNext.cnt - 1); // ??
//
//
//
//        }
//
//
////
////        while(!map.isEmpty()){
////            if(map.keySet().size() == 1){
////                // get the only ket
////                return ""; // ?????
////            }
////
////            StringCnt strCnt = pq.poll();
////            // ???
////            if(!res.isEmpty()){
////                String last = String.valueOf(res.charAt(res.length() - 1));
////                if(last.equals(strCnt.str)){
////                    return "";
////                }
////            }
////
////            res += strCnt.str;
////            // update map
////           // map.put(strCnt)
////
////        }
//
//
//
//        return null;
//    }
//

    // LC 502
    // 6.54 - 7.04 am
    /**
     *
     *   -> Pick a list of at `most k distinct projects` from given
     *     projects to `MAXIMIZE` your `final capital, `
     *     and `return the final maximized capital.`
     *
     *
     *     - n projects where the ith project has a pure profit profits[i]
     *       and a minimum capital of capital[i] is needed to start it.
     *
     *     -   have w capital. When you finish a project, you will obtain its
     *         pure profit and the profit will be added to your total capital.
     *
     *
     *
     *   IDEA 1) PQ
     *
     *
     *   IDEA 2) BRUTE FORCE  ??
     *
     *
     *
     *
     */
    // IDEA 1) SMALL + BIG PQ
    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        // edge
        if(k == 1){
            if(w < capital[0]){
                return 0;
            }
            return profits[0] - w; // ???
        }

//        // 2 PQ:
//        // 1. profits PQ: (big -> small sorted on profit)
//        // PQ: { [profit_1, capital_1], [profit_2, capital_2] .... }
//        PriorityQueue<Integer[]> proPQ = new PriorityQueue<>(new Comparator<Integer[]>() {
//            @Override
//            public int compare(Integer[] o1, Integer[] o2) {
//                int diff = o2[0] - o1[0];
//                return diff;
//            }
//        });
//
//        // ???
////        for(int i = 0; i < capital.length; i++){
////            proPQ.add(new Integer[]{profits[i], capital[i]});
////        }
//
//        // 2. capital PQ: (small -> big sorted on capital)
//        // PQ: { [capital_1], [ capital_2] .... }
//        PriorityQueue<Integer> capitalPQ = new PriorityQueue<>();
//        capitalPQ.add(w); // ??? init with w capital
//        //proPQ.add(new Integer[]{0, w}); // ??? init with w capital



        /** capital PQ: small PQ */
        /** NOTE !!! structure:  [capital, profit] */
        // Min-heap ordered by capital
        //  - structure : { [capital, profit] }  // <---- NOTE this !!!!
        PriorityQueue<int[]> capitalPQ = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        for (int i = 0; i < profits.length; i++) {
            capitalPQ.offer(new int[] { capital[i], profits[i] });
        }


//        // ???
//        capitalPQ.add(new int[]{w,0});
//

        /** profit PQ: big PQ */
        /** NOTE !!! structure:  [profit] */
        // Max-heap ordered by profit
        // - structure : { profit }
        PriorityQueue<Integer> profitPQ = new PriorityQueue<>((a, b) -> b - a);


        int maxCap = 0;

        // ???
//        while(!capitalPQ.isEmpty()){
//            int[] cur = capitalPQ.poll();
//            // ???
//            while(profitPQ.peek() < cur){
//
//            }
//
//
//        }




        return maxCap;
    }






    // IDEA 1) PQ
//    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
//        // edge
//        if(k == 1){
//            if(w < capital[0]){
//                return 0;
//            }
//            return profits[0] - w; // ???
//        }
//
//        // capital PQ: small PQ
//        // java default is small PQ
//        PriorityQueue<Integer> capPQ = new PriorityQueue<>();
//
//        // profits PQ: big PQ
//        PriorityQueue<Integer> profitsPQ = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o2 - o1;
//                return diff;
//            }
//        });
//
//        for(int p: profits){
//            profitsPQ.add(p);
//        }
//
//        for(int c: capital){
//            capPQ.add(c);
//        }
//
//        int prevCap = 0;
//
//        int cnt = 0;
//        int maxCapital = 0;
//
//        // ????
//        int nextCap = -1;
//        int nextProfit = -1;
//
//        while(!capPQ.isEmpty()){
//
//            nextCap = capPQ.poll();
//            maxCapital += nextCap;
//
//            // ???
//            nextProfit = profitsPQ.poll();
//            //while( profitsPQ.peek() )
//
//            System.out.println(">>> cnt = " + cnt +
//                    " nextCap = " + nextCap +
//                    " nextProfit = " + nextProfit +
//                    " maxCapital = " + maxCapital);
//
//            // ??
//            if(nextCap > prevCap){
//                return maxCapital;
//            }
//
//            maxCapital += nextProfit;
//            prevCap = nextCap;
//
//            if(cnt == k){
//                return maxCapital; //???
//            }
//
//            cnt += 1;
//        }
//
//        return maxCapital;
//    }

    // LC 2682
    // 10.13 - 23 am
    /**
     *  IDEA 1) set ???
     *
     */
    public int[] circularGameLosers(int n, int k) {
        // edge
        if(n == 0 || k == 0){
           return new int[]{}; // ???
        }

        // set ???
        Set<Integer> set = new HashSet<>();

        //int cur = -1;
        int cnt = 1;
        int idx = 1;
        List<Integer> list = new ArrayList<>();

        while(!set.contains(idx)){
            System.out.println(">>> idx = " + idx
                    + ", set = " + set);
            idx += (cnt * k);
            // adjust
            idx = idx % n;
            // update
            set.add(idx);
            list.add(idx);
            cnt += 1;
        }

        // ??? optimize below
        System.out.println(">>> list = " + list);

        int[] res = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            res[i] = list.get(i);
        }

        return res;
    }


    // LC 2683
    // 10.27 - 37 AM
    /**
     *  IDEA 1) BIT OP
     *
     *
     */
    public boolean doesValidArrayExist(int[] derived) {
        // edge
        if(derived == null || derived.length == 0){
            return true;
        }

        //  int result = a ^ b; // Binary: 0110 (which is 6 in decimal)

        // cache as original
        int[] original = derived;

        // ??? op
        int n = derived.length;
        // OP
        for(int i = 0; i < n; i++){
//            // check if `can form as original`
//            if(n > 0){
//                if(isSameBySort(original, derived)){
//                    return true;
//                }
//            }
            // case 1) i == n - 1
            if(i == n - 1){
                derived[i] = (original[i] ^ original[0]); // ???
            }
            // case 2) otherwise
            else{
                derived[i] = (original[i] ^ original[i + 1]); // ???
            }
        }

        // check if can make derived equals to original
        // after some sorting
        // check if `can form as original`
        return isSameBySort(original, derived);
    }

    private boolean isSameBySort(int[] original, int[] cur){
        // the correct way to sort array ??
        // ???
        System.out.println(">>> (before sort) original = " +
                Arrays.toString(original) +
                " cur = " + Arrays.toString(cur));

        Arrays.sort(original);
        Arrays.sort(cur);

        System.out.println(">>> (after sort) original = " +
                Arrays.toString(original) +
                " cur = " + Arrays.toString(cur));

        //????
        if(original.length != cur.length){
            return false;
        }
        for(int i = 0; i < original.length; i++){
            if(original[i] != cur[i]){
                return false;
            }
        }

        // ???
        // return original.equals(cur);
        return true;
    }


    // LC 2684
    // 8.56 - 9.06 am
    /**
     *  IDEA 1) DFS
     *
     *  IDEA 2) bFS
     *
     *  IDEA 3) DP
     *
     *
     */
    // IDEA 2) DFS
    int maxMoveCnt;
    public int maxMoves(int[][] grid) {

        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }
        if(grid[0].length == 1 || grid.length == 1){
            return 1; // ???
        }

        int l = grid.length;
        int w = grid[0].length;

        // init value = false ???
        boolean[][] visited = new boolean[l][w]; // /??

        for(int i = 0; i < l; i++){
            // NOTE !!! mark cur x,y as visited
            visited[i][0] = true;
            //maxMoveCnt = Math.max(maxMoveCnt, getMaxMove(0, i, grid, visited));
            getMaxMove(0, i, grid, visited, 0);
        }

        return maxMoveCnt;
    }

    private void getMaxMove(int x, int y, int[][] grid,  boolean[][] visited, int moveCnt){

        int l = grid.length;
        int w = grid[0].length;

        // (row - 1, col + 1), (row, col + 1) and (row + 1, col + 1)
        int[][] moves = new int[][]{ {-1,1}, {0,1}, {1,1} };
        // ???
        maxMoveCnt = Math.max(maxMoveCnt, moveCnt);

        for(int[] m: moves){
            int x_ = x + m[0];
            int y_ = y + m[1];
            // validate
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                if(grid[y_][x_] > grid[y][x] && !visited[y_][x_]){
                    visited[y_][x_] = true;
                    // ???
                    getMaxMove(x_, y_, grid, visited, moveCnt + 1);
                }
            }
        }

       // return 0; // ???
    }





    // IDEA 2) BFS
    public int maxMoves_99(int[][] grid) {
        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }
        if(grid[0].length == 1 || grid.length == 1){
            return 1; // ???
        }

        int l = grid.length;
        int w = grid[0].length;

        boolean[][] vissited = new boolean[l][w]; // /??


        // do we need this ????
       // boolean[][] vissited = new boolean[l][w]; // /??

        // ??
        // (row - 1, col + 1), (row, col + 1) and (row + 1, col + 1)
        //int[][] moves = new int[][]{ {-1,1}, {0,1}, {1,1} };
        int[][] moves = new int[][]{ {1,-1}, {1,0}, {1,1} };

        // queue: [ (x, y, moveCnt) ] // ????
        Queue<Integer[]> q = new LinkedList<>();

        // add all possible start points
        // -> You can start at any cell in the first column of the matrix,
        for(int i = 0; i < l; i++){
            q.add(new Integer[]{i, 0, 0});
        }

        int maxMoveCnt = 0;

        while(!q.isEmpty()){
            // NOTE !!!  for BFS, we loop `layer by layer`
            int size = q.size();
            for(int i = 0; i < size; i++){
                Integer[] cur = q.poll();
                int x = cur[0];
                int y = cur[1];
                int moveCnt = cur[2];

                // check the max move at the moment
                maxMoveCnt = Math.max(maxMoveCnt, moveCnt);

                for(int[] m: moves){
                    int x_ = x + m[0];
                    int y_ = y + m[1];
                    // validate
                    if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                        // ??
                        System.out.println(">>> x = " + x + ", y = " + y +
                                "x_ = " + x_ + ", y_ = " + y_ );
                        if(grid[y_][x_] > grid[y][x]){
                            q.add(new Integer[]{x_, y_, moveCnt + 1});
                        }
                    }
                }
            }
        }

        return maxMoveCnt;
    }






//        public int maxMoves(int[][] grid) {
//        // edge
//        if(grid == null || grid.length == 0 || grid[0].length == 0){
//            return 0;
//        }
//        if(grid[0].length == 1 || grid.length == 1){
//            return 1; // ???
//        }
//
//
//        int maxMove = 0;
//
//        // PQ: {x, y, cost, moves}
//        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
//            @Override
//            public int compare(Integer[] o1, Integer[] o2) {
//                int diff = o1[2] - o2[2];
//                return diff;
//            }
//        });
//
//        int l = grid.length;
//        int w = grid[0].length;
//
//        // ???
//        //  can start at ANY cell in the FIRST column,
//        for(int i = 0; i < l; i++){
//            int x = 0;
//            int y = i;
//            // init cost = 0
//            // init move = 0
//            pq.add(new Integer[]{x, y, 0, 0});
//        }
//
//        // 2D array record cost by (x, y) ???
//        // int init as 0 // /????
//        int[][] costStaus = new int[l][w]; // ???
//        for(int i = 0; i < l; i++){
//            for(int j = 0; j < w; j++){
//                // ?? init as max val
//                // so we know that which grid (x,y)
//                // is NOT visited yet
//                costStaus[i][j] = Integer.MAX_VALUE; // ?????
//            }
//        }
//
//        // (row - 1, col + 1), (row, col + 1) and (row + 1, col + 1)
//        int[][] moves = new int[][]{ {-1,1}, {0,1}, {1,1} };
//
//        // ?? dijkstra: BFS + PQ + cost status update ???
//        while (!pq.isEmpty()){
//            // edge: if even CAN'T move from 1st iteration ??
//
//            Integer[] cur = pq.poll();
//            int x = cur[0];
//            int y = cur[1];
//            int cost = cur[2];
//            int movesCnt = cur[3];
//
//            // ???
//            maxMove = Math.max(maxMove, movesCnt);
//
//            for(int[] m: moves){
//                int x_ = x + m[1];
//                int y_ = y + m[0];
//
//                // check 1) if still in grid boundary
//                if(x_ >= 0 && x_ < w && y_ >= 0 && y < l){
//                    // check 2) if next val is bigger than prev val
//                    if(grid[y_][x_] > grid[y][x]){
//
//                        // ???
//                        int cost_ = cost + grid[y_][x_];
//
//                        //  check 3) check if `cost if less the cur status` // ???
//                        if(grid[y_][x_] > grid[y][x] + cost_){
//                            // add to PQ
//                            pq.add(new Integer[]{x_, y_, cost_, movesCnt + 1});
//
//                            // update cost status ???
//                            // relation ???
//                            grid[y_][x_] = cost_; // ???
//                        }
//                    }
//                }
//            }
//        }
//
//        return maxMove;
//    }


  // LC 703
//  class KthLargest {
//
//      public KthLargest(int k, int[] nums) {
//
//      }
//
//      public int add(int val) {
//
//      }
//  }


    // LC 414
    // Third Maximum Number
    // 7.11 - 21 am
    /**
     *
     *  ->  the third DISTINCT maximum number in this array.
     *      - If the third maximum does not exist, return the maximum number.
     *
     *
     *   IDEA 1) SORT + SET
     *
     *   IDEA 2) PQ + SET
     *
     */
    public int thirdMax(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0; // ???
        }
        if(nums.length == 1){
            return nums[0]; // ???
        }

        List<Integer> list = new ArrayList<>();

        Set<Integer> set = new HashSet<>();
        for(int x: nums){
            if(!set.contains(x)){
                set.add(x);
                list.add(x);
            }
        }

        System.out.println(">>> (before sort) list = " + list);
        // sort (big -> small)
        list.sort(Comparator.reverseOrder()); // ????
        System.out.println(">>> (after sort) list = " + list);

        if(list.size() < 3){
            return list.get(0);
        }

        return list.get(2);
    }


    // LC 324
    // 7.30 - 7.40 am
    /**
     *  -> Given an integer array nums,
     *    reorder it such that nums[0] < nums[1] > nums[2] < nums[3]....
     *
     *    - NOTE:
     *      - You may assume the input array always has a valid answer.
     *
     *
     *  IDEA 1) SORT + pick the `most freq` big nums
     *
     *
     *  IDEA 2) BRUTE FORCE
     *
     *
     *  IDEA 3) 2 pointers ????
     *
     *
     *
     *  ex 1)
     *
     *  Input: nums = [1,5,1,1,6,4]
     *  Output: [1,6,1,5,1,4]
     *  Explanation: [1,4,1,5,1,6] is also accepted.
     *
     *
     *  -> sort (small -> big)
     *    [1,1,1,4,5,6]
     *
     *  -> res = [1,6], arr = [1,1,4,5]
     *  -> res = [1,6,1,5], arr = [1,4]
     *  -> res = [1,6,1,5,1,4], arr = []
     *
     *
     *
     *  ex 2)
     *
     *   Input: nums = [1,3,2,2,3,1]
     *   Output: [2,3,1,3,1,2]
     *
     *   -> sort (small -> big)
     *   [1,1,2,2,3,3]
     *
     *   -> res = [1,3], arr =[1,2,2,3]
     *   -> res = [1,3,1,3], arr =[2,2]
     *
     *
     *
     *
     */
    public void wiggleSort(int[] nums) {

    }


    // LC 2685
    public int countCompleteComponents_1(int n, int[][] edges) {

        return 0;
    }


    public int countCompleteComponents_99(int n, int[][] edges) {
        // edge
        // ????
//        if(edges == null || edges.length == 0 || edges[0].length == 0 || n == 0){
//            return 0;
//        }
//        if(edges.length == 1 || edges[0].length == 1){
//            return 1; // ????
//        }

        // ??? build graph
        // { val : [neighbor_1, neighbor_2, ...] }
        Map<Integer, List<Integer>> graph = new HashMap<>();
        // init
        for(int i = 0; i < n; i++){
            graph.put(i, new ArrayList<>());
        }
        // add neighbors
        for(int[] e: edges){
            int start = e[0];
            int end = e[1];

            // ok
            graph.get(start).add(end); // ???
            graph.get(end).add(start); // ???
        }

        System.out.println(">>> graph = " + graph);

        boolean[] visited = new boolean[n];

        int completeNodeCnt = 0;

        // loop over n
        for(int i = 0; i < n; i++){
            //graph.put(i, new ArrayList<>());
            // ???
            if(isCycled(i, graph, visited, new HashSet<Integer>())){
                completeNodeCnt += 1;
            }
        }

        return completeNodeCnt;
    }

    private boolean isCycled(int node, Map<Integer, List<Integer>> graph, boolean[] visited, HashSet<Integer> set){
        // ??
        if(set.contains(node)){
            return false;
        }
        // mark as visited
        visited[node] = true;
        // update set
        set.add(node);

        // dfs call
        for(int i: graph.get(node)){
            if(!isCycled(i, graph, visited, set)){
                return false;
            }
        }

        return true;
    }


    // LC 1145
    // 6.43 - 53 am
    /**
     *  -> You are the SECOND player.
     *  If it is `possible` to choose y
     *  to ensure `you win the game`, return TRUE.
     *  otherwise, return false
     *
     *   -  the winner is the player that colored more nodes.
     *
     *
     *
     *   - from 1 to n, n is odd
     *   - each node has a distinct val from 1 to n
     *   - step 1) 1st player choose x ( 1 <= x <= n)
     *             - red color
     *
     *   - step 2) 2nd player choose y ( 1 <= y <= n)
     *           - blue color
     *             NOTE: x != y
     *
     *   - if one can't choose such color  -> pass the game
     *   - if both player can't choose such color  -> game end
     *
     *
     *
     *  -----
     *
     *  •	There are n nodes labeled 1..n.
     * 	•	Player 1 colors node x first.
     * 	•	Player 2 colors any other node y.
     * 	•	Then players alternate coloring adjacent uncolored nodes.
     * 	•	The player who colors more nodes wins.
     *
     * We need to check if player 2 can pick a node so that they can guarantee to win.
     *
     * ⸻
     *
     *
     *
     *   IDEA 1) DFS
     *
     *      -> get the `sub root` count
     *
     *   IDEA 2) DP
     *
     *
     *
     */
    // 9.52 - 10.10 am
    // V1: DFS +  `sum > n/2` check
    // check `parent + sub left + sub right sum > n / 2 ?`
//    Map<Integer,Integer> colorMap = new HashMap<>();
//    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {
//        // edge
//        if(root == null || n == 0){
//            return false;
//        }
//        if((root.left == null && root.right == null) || n == 1){
//            return false;
//        }
//
//        TreeNode node1 = findNode1(root, x);
//        int player1LeftCnt = getNodeCnt(node1.left);
//        int player1RightCnt = getNodeCnt(node1.right);
//
//        // ???
//        int player1Sum = 1 + player1LeftCnt + player1RightCnt;
//
//        //int player2Sum =
//
//        return player1Sum < n / 2; // ???
//
//
////        // bfs build map ???
////        Queue<TreeNode> q = new LinkedList<>();
////        int nodeSum = 0;
////        while(!q.isEmpty()){
////            TreeNode cur = q.poll();
////            int curColorSum = colorHelper(root, null); // ??
////            // NOTE !!! all node val in tree is UNIQUE
////            colorMap.put(cur.val, curColorSum);
////
////            nodeSum += cur.val;
////
////            if(cur.left != null){
////                q.add(cur.left);
////            }
////            if(cur.right != null){
////                q.add(cur.right);
////            }
////        }
////
////        // ??? let's say we already built the `color map`
////        for(Integer k: colorMap.keySet()){
////            if(colorMap.get(k) > nodeSum / 2){
////                return true;
////            }
////        }
//
//        // return false;
//    }

    // helper func: for player1 finds his first to-color node
    private TreeNode findNode1(TreeNode root, int x){
        if(root.val == x){
            return root;
        }
        TreeNode left = findNode1(root.left, x);
        TreeNode right = findNode1(root.right, x);
        return left == null ? left : right;
    }

    private int getNodeCnt(TreeNode root){
        if(root == null){
            return 0;
        }
        return 1 + getNodeCnt(root.left) + getNodeCnt(root.right);
    }

    // ???
//    private int colorHelper(TreeNode root, TreeNode parent){
//        if(root == null){
//            return 0;
//        }
//        // ???
////        return root.val
////                + Math.max(colorHelper(root.left, root) ,
////                colorHelper(root.right, root)
////                );
//        // ???
//        return root.val + colorHelper(root.left, root) + colorHelper(root.right, root);
//    }








    // V1: DFS + `HALF TREE CNT CHECK
    // ??? { node_val: sub_node_cnt }
//    Map<Integer, Integer> subNodeCntMap = new HashMap<>();
//    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {
//        // edge
//        if(root == null || n == 0){
//            return false;
//        }
//        if((root.left == null && root.right == null) || n == 1){
//            return false;
//        }
//        // build sub node cnt map
//        getSubNodeCount();
//        // dfs check if 2nd player can win
//        return can2ndPlayerWin(root, null, n, x);
//    }
//
//    /**
//     *  1, find a node
//     *  2. check if any if below has sub node cnt >= n/2,
//     *     if yes, return true directly
//     *       - node.left
//     *       - node.right
//     *       - parent
//     *
//     *  3. keep above loop, till  the end
//     *     if still NOT found a node satisfy above,
//     *     return false
//     *
//     */
//    private boolean can2ndPlayerWin(TreeNode root, TreeNode parant, int n, int x){
//        // edge ???
//        if(root == null){
//            return false; // ???
//        }
//        int cnt = subNodeCntMap.get(root.val);
////        int leftCnt = subNodeCntMap.get(root.left.val);
////        int rightCnt = subNodeCntMap.get(root.right.val);
////        int parentCnt = subNodeCntMap.get(parant.val);
//
//        if(cnt >= n / 2){
//            return true; // ????
//        }
//
//
//        return  subNodeCntMap.get(root.left.val) >= 2/n ||
//                subNodeCntMap.get(root.right.val) >= 2/n||
//                subNodeCntMap.get(parant.val)  >= 2/n;
//    }
//
//    private void getSubNodeCount(){
//        // return 0;
//    }



    // V1: DFS
//    // ??? { node_val: sub_node_cnt }
//    Map<Integer, Integer> subNodeCntMap = new HashMap<>();
//    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {
//        // edge
//
//        getSubNodeCount();
//
//        // DFS to check if 2nd player can win the game ????
//        int firstPlayerCnt = subNodeCntMap.get(x);
//
//        return false;
//    }
//
//    private boolean can2ndPlayerWin(TreeNode root, int n, int x){
//        // edge ???
//        if(root == null){
//            return false; // ???
//        }
//        int firstPlayerCnt = subNodeCntMap.get(x);
//        if(subNodeCntMap.get(root.val) > firstPlayerCnt){
//            return false;
//        }
//
//        //int subLeftCnt =
//
//        // ???
//        return can2ndPlayerWin(root.left, n, x) ||
//                can2ndPlayerWin(root.right, n, x);
//    }
//
//
//    private void getSubNodeCount(){
//       // return 0;
//    }


    // LC 2542
    // 7.02 - 27 am
    /**
     *   -> Return the maximum possible score.
     *
     *   - 2 nums ( 0 index ): nums1, nums2,
     *     has same len (n)
     *   - a positive int: k
     *   - must choose a subsequence of indices from nums1
     *      of length k.
     *   - GET the max from below:
     *      - (nums1[i0] + nums1[i1] +...+ nums1[ik - 1]) *
     *        min(nums2[i0] , nums2[i1], ... ,nums2[ik - 1]).
     *
     *     -> NOTE !!!
     *      the chose index in nums1, nums2 MUST BE same
     *
     *
     *   - NOTE:
     *       - A subsequence of indices of an array is a
     *        set that can be derived from the set
     *       {0, 1, ..., n-1} by deleting some or no elements.
     *
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) PQ ???
     *
     *    -> to get max
     *     ` (nums1[i0] + nums1[i1] +...+ nums1[ik - 1])
     *      * min(nums2[i0] , nums2[i1], ... ,nums2[ik - 1]).`
     *
     *    -> either 1st term is max or 2nd term is max ??
     *    -> so we have 2 cases
     *       - case 1: try to get ` 1st term` max, calculate above val
     *       - case 2: try to get ` 2nd term` max, calculate above val
     *
     *    -> then we return the bigger one as final answer
     *
     *
     */
    public long maxScore(int[] nums1, int[] nums2, int k) {
        // edge
        if(nums1.length == 1){
            if(k == 1){
                return (long) nums1[0] * nums2[0];
            }
            return 0L; // ???
        }

       // long res = 0L;

        long res1 = 0L;
        long res2 = 0L;

        // - case 1: try to get ` 1st term` max, calculate above val
        // NOTE !!! we want to get max val from nums1
        // so we need a SMALL PQ
        // small PQ: pq_1_1: [ [idx_1, val_1] , [idx_2, val_2], ..] ???
        PriorityQueue<Integer[]> pq_1_1 =  new PriorityQueue<Integer[]>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[1] - o2[1];
                return diff;
            }
        });

        for(int i = 0; i < nums1.length; i++){
            while(pq_1_1.size() > k){
                pq_1_1.poll();
            }
            // NOTE !!!
            pq_1_1.add(new Integer[]{i, nums1[i]});
        }

        int nums1_case1_sum = 0;
        int nums2_case1_min = Integer.MAX_VALUE;

        while(!pq_1_1.isEmpty()){
            Integer[] cur = pq_1_1.poll();
            int idx = cur[0];
           // int val = cur[1];
            nums1_case1_sum += nums1[idx];
            nums2_case1_min = Math.min(nums2_case1_min, nums2[idx]);  // ???
        }

        res1 = ((long) nums1_case1_sum * nums2_case1_min);


        // - case 2: try to get ` 2nd term` max, calculate above val
        // NOTE !!! we want to get min val from nums2
        // so we need a BIG PQ
        // big PQ: pq_2_2: [ [idx_1, val_1] , [idx_2, val_2], ..] ???
        PriorityQueue<Integer[]> pq_2_2 =  new PriorityQueue<Integer[]>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[1] - o1[1];
                return diff;
            }
        });


        for(int i = 0; i < nums2.length; i++){
            while(pq_2_2.size() > k){
                pq_2_2.poll();
            }
            // NOTE !!!
            pq_2_2.add(new Integer[]{i, nums2[i]});
        }


        int nums1_case2_sum = 0;
        int nums2_case2_min = Integer.MAX_VALUE;

        while(!pq_2_2.isEmpty()){
            Integer[] cur = pq_2_2.poll();
            int idx = cur[0];
            //int val = cur[1];
            nums1_case2_sum += nums1[idx];
            nums2_case2_min = Math.min(nums2_case2_min, nums2[idx]);  // ???
        }

        res2 = ((long) nums1_case2_sum * nums2_case2_min);

        System.out.println(">>> res1 = " + res1 + ", res2 = " + res2);


        return Math.max(res1, res2);
    }


    // LC 124
    // 9.07 - 17 am
    /**
     *  -> return the MAX `path sum` of any non-empty path.
     *     (give a binary tree)
     *
     *    - The path sum of a path is the sum of the node's values in the path.
     *
     *
     *    IDEA 1) DFS + HASHMAP
     *
     *    IDEA 1) BFS ???
     */
    //Map<Integer, List<Integer>> pathMap = new HashMap<>();
    // ??? { node.val: path_sum_with_the_node }
    Map<Integer, Integer> pathMap = new HashMap<>();
    int maxPathSum99 = 0;
    public int maxPathSum(TreeNode root) {
        // edge
        if(root == null){
            return 0; // ?
        }
        if(root.left == null && root.right == null){
            return root.val;
        }

        int res = -1 * Integer.MAX_VALUE; // ??

        // run BFS, enrich pathMap // ???
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root); // ??
        while(!q.isEmpty()){
            int size = q.size();
            for(int i = 0; i < size; i++){
                TreeNode cur = q.poll();
                // edge: if `cur single root` is the max val
                res = Math.max(cur.val, res);
                int pathSum = getNodeDepth(cur.left) + getNodeDepth(cur.right);
                pathMap.put(cur.val, pathSum + cur.val);

                if(cur.left != null){
                    q.add(cur.left);
                }
                if(cur.right != null){
                    q.add(cur.right);
                }
            }
        }

        System.out.println(">>> pathMap = " + pathMap);

        // loop over map, get the max path sum
        for(int k: pathMap.keySet()){
            res = Math.max(pathMap.get(k), res);
        }
        return res;
    }

    // ???
    private int getNodeDepth(TreeNode root){
        // edge
        if(root == null){
            return 0;
        }
        // ???
        int leftPathSum = getNodeDepth(root.left);
        int rightPathSum = getNodeDepth(root.right);

        return root.val + Math.max(leftPathSum, rightPathSum); // ???
    }

    private int dfs_99(TreeNode root){
        if(root == null){
            return 0;
        }
        int left = Math.max(0, dfs_99(root.left));
        int right = Math.max(0, dfs_99(root.right));

        maxPathSum99 = Math.max(left + right + root.val, maxPathSum99);


        return Math.max(left, right) + root.val; // ????
    }

    // LC 1145
//    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {
//
//        return false;
//    }


    // LC 1268
    // 10.52 - 11.02
    /**
     *  -> If there are more than three products
     *    with a common prefix return the three lexicographically minimums products.
     *
     *
     *
     *  IDEA 1) TRIE (text - dict)
     *
     *  IDEA 2) DFS ??
     *
     *  IDEA 3) BFS ??
     *
     */
//    class MyNode{
//        // attr
//        String val;
//        Map<String, List<MyNode>> children;
//        // constructor
//        // ????
////        public MyNode(){
////            this.val = "#"; // ???
////            this.children = new HashMap<>();
////        }
//        public MyNode(String val){
//            this.val = val;
//            this.children = new HashMap<>();
//        }
//        public MyNode(String val, Map<String, List<MyNode>> children){
//            this.val = val;
//            this.children = children;
//        }
//    }

    class MyNode99{
        // attr
        boolean isEnd;
        /** NOTE !!!! */
        Map<Character, MyNode99> child;
        // constructor
        MyNode99(){
            this.isEnd = false;
            this.child = new HashMap<>();
        }
    }

    class MyTrie99{
        // attr
        MyNode99 node;

        // constructor
        MyTrie99(){
            this.node = new MyNode99();
            //this.node.child.put('xxx', new MyNode99());
        }


//        // attr
//        MyNode node;
//        boolean isEnd;
//        // constructor
//        // ???
//        public MyTrie99(){
//            this.node = new MyNode("#");
//            this.isEnd = true; // ???
//        }
//        public MyTrie99(MyNode node){
//            this.node = node;
//            this.isEnd = true; // ???
//        }
//        public MyTrie99(MyNode node, boolean isEnd){
//            this.node = node;
//            this.isEnd = isEnd; // ???
//        }
//
//        // method
//        public void addWord(String word){
//            if(word == null || word.length() == 0){
//                return;
//            }
//
//            // /??
//            MyTrie99 trie = new MyTrie99();
//
//            for(char ch: word.toCharArray()){
//                String str = String.valueOf(ch);
//                // ???
//               // if(!trie.node.children)
//            }
//        }
    }

    class MyNode98{
        // ??
        Map<String, MyNode98> child;
        boolean isEnd;
       // List<List<String>> collected = new ArrayList<>();

        MyNode98(){
            this.child = new HashMap<>();
            this.isEnd = false;
        }

        MyNode98(Map<String, MyNode98> child, boolean isEnd){
            this.child = child;
            this.isEnd = isEnd;
        }

    }

    class MyTrie98{
        // attr
        MyNode98 node;
        // /???
        List<List<String>> recommendList;

        // constructor
        MyTrie98(){
            this.node = new MyNode98();
            this.recommendList = new ArrayList<>();
        }

        // method
        public void addProducts(List<String> products){
            if(products.isEmpty() || products.size() == 0){
                return;
            }
            for(String p: products){
                // ???
                this.addProduct(p);
            }
        }

        public void addProduct(String product){
            if(product.isEmpty()){
                return;
            }

            //MyTrie98 trie = this.node;
            MyNode98 node = this.node; // ???
            for(char ch: product.toCharArray()){
                String s = String.valueOf(ch);
                if(!node.child.containsKey(s)){
                    node.child.put(s, new MyNode98()); // ???
                }
                node = node.child.get(s); // /??
            }

            node.isEnd = true;
        }

        public boolean isStartWith(String product){
            if(product.isEmpty() || product.length() == 0){
                return false;
            }

            MyNode98 node = this.node; // ???

            for(char ch: product.toCharArray()){
                String s = String.valueOf(ch);
                if(!node.child.containsKey(s)){
                    return false;
                }
                node = node.child.get(s);
            }

            return true;
        }

        public boolean isContain(String product){
            if(product.isEmpty()){
                return false;
            }

            MyNode98 node = this.node; // ???

            for(char ch: product.toCharArray()){
                String s = String.valueOf(ch);
                if(!node.child.containsKey(s)){
                    return false;
                }
                node = node.child.get(s);
            }

            return node.isEnd; // /??
        }

        public List<List<String>> recommend(String searchWord){
            if(searchWord.isEmpty()){
                return new ArrayList<>(); // ??
            }

            MyNode98 node = this.node; // ???

           // List<String> list = new ArrayList<>();
            this.recommendHelper(searchWord, node, new ArrayList<>(), new StringBuilder(), 0);

            return null;
        }

        private void recommendHelper(String searchWord, MyNode98 node, List<String> list, StringBuilder sb, int idx){
            if(searchWord.isEmpty()){
                //return new ArrayList<>(); // ??
                return;
            }

            // ???
            char ch = searchWord.charAt(idx);
            String s = String.valueOf(ch);
            if(!node.child.containsKey(s)){
                return;
            }
            // ???
            if(node.isEnd && !sb.toString().isEmpty()){
                // ???
                List<String> tmp = new ArrayList<>();
                tmp.add(sb.toString());
                recommendList.add(tmp);// ???
            }
            // NOTE !!! loop over all `possible child`
            // and process by recursive dfs call ???
            // ????
            for(MyNode98 childNode: node.child.values()){
                recommendHelper(searchWord, childNode, list, sb, idx + 1);
                // backtrack: undo last op ????
            }
        }


        /**
         *
         *  RecommendV2 :
         *
         *
         * 1. collect the words with prefix (one alphabet by one)
         * 2. sort lexicographically (whenever a new word is added)
         * 2. if collected > 3, STOP, and return top 3 words from collected
         *
         */
//        List<List<String>> res = new ArrayList<>(); // ???
//        public List<List<String>> recommendV2(String searchWord){
//            if(searchWord.isEmpty()){
//                return new ArrayList<>(); // ??
//            }
//
//            MyNode98 node = this.node; // ???
//
//            // ???
//            //List<List<String>> collected = new ArrayList<>();
//
//            for(char ch: searchWord.toCharArray()){
//
//                if(!node.child.containsKey(ch)){
//                    return collected; // ???
//                }
//
//                node = node.child.get(ch);
//
//            }
//
//
//           // return collected;
//        }

        private void recommendHelperV2(char ch, int idx, StringBuilder sb, String searchWord){
            if(idx == searchWord.length()){
                //return collected; // ???
                String curStr = sb.toString();
                List<String> tmp = new ArrayList<>();
                tmp.add(curStr);
                if(!this.recommendList.contains(tmp)){
                    recommendList.add(tmp);
                }
                return;
            }
            MyNode98 node = this.node; // ???

            if(!node.child.containsKey(ch)){
                return;
            }


            this.node.child.get("1");
//            for(MyNode98 node98: this.node.child.get("1")){
//
//            }


            // ???
            //return collected;
            return;
        }




    }


    // 17.50 - 18.00 pm
    //IDEA 1) TRIE (text - dict)
    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        // edge
        if(products == null || products.length == 0){
            return new ArrayList<>(); // /??
        }
        // init class
        MyTrie98 trie = new MyTrie98();
        // add all products to system
        trie.addProducts(Arrays.asList(products)); // ???
        // get recommend
        return trie.recommend(searchWord);  // ??
    }







    // LC 387
    // 15.55 - 16.05
    /**
     *  -> Given a string s, find the `first non-repeating
     *  character` in it and return its index.
     *  If it does not exist, return -1.
     *
     *
     *  // IDEA 1) HASHMAP
     *
     */
    public int firstUniqChar(String s) {
        // edge
        if(s == null || s.isEmpty()){
            return -1;
        }
        if(s.length() == 1){
            return 0;
        }
        // { val: cnt }
        Map<String, Integer> map = new HashMap<>();
        for(char ch: s.toCharArray()){
            String key = String.valueOf(ch);
            map.put(key, map.getOrDefault(key, 0) + 1);
        }

        // edge
//        boolean isFound = false;
//        for(Integer cnt: map.values()){
//            if(cnt == 1){
//                isFound = true;
//            }
//        }
//
//        if(!isFound){
//            return -1;
//        }

        for(int i = 0; i < s.length(); i++){
            char ch = s.charAt(i);
            String key = String.valueOf(ch);
            if(map.get(key) == 1){
                return i;
            }
        }

        return -1;
    }


    // LC 1054
    // 16.11 - 21 pm
    /**
     *
     *  -> `Rearrange` the barcodes so that `NO two adjacent barcodes are equal.`
     *    You may return any answer, and it is guaranteed an answer exists.
     *
     *   - there is a row of barcodes,
     *     where the ith barcode is barcodes[i].
     *
     *   - NOTE !!!
     *      - it is guaranteed an answer exists.
     *
     *
     *  IDEA 1) HASHMAP
     *
     *  IDEA 2) PQ ????
     *
     */
    // IDEA 1) HASHMAP
    public int[] rearrangeBarcodes(int[] barcodes) {
        // edge
//        if(barcodes == null || barcodes.length == 0){
//            return new int[]{}; // ??
//        }
//        if(barcodes.length <= 2){
//            //return new int[]{barcodes[0]};
//            return barcodes; // ???
//        }
        if (barcodes == null || barcodes.length <= 1) return barcodes;

        // { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        // NOTE !!!
        // PQ, sort on map val
        // a `big pq`
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // ???
                int diff = map.get(o2) - map.get(o1);
                return diff;
            }
        });

        // add to map, PQ
        for(int x: barcodes){
            map.put(x, map.getOrDefault(x, 0) + 1);
            //pq.add(x);
        }

        // NOTE !!! add DISTINCT val to PQ
        pq.addAll(map.keySet());

        //List<Integer> list = new ArrayList<>();
        int[] res = new int[barcodes.length];
        Arrays.fill(res, -1); // init all idx with value = -1

        // ???
        int prev = -1;
        for(int i = 0; i < res.length; i++){
            // ???
            if(!pq.isEmpty()){
                int first = pq.poll();
                // case 1) i == 0 || prev != cur
                if(i == 0 || prev != first){
                    res[i] = first;
                    // update map
                    map.put(first, map.get(first) - 1);
                    if(map.get(first) == 0){
                        map.remove(first);
                    }else{
                        pq.add(first);
                    }
                }
                // case 2  prev == cur
                else{
                    int second = pq.poll();
                    res[i] = second;
                    // update map
                    map.put(second, map.get(second) - 1);
                    if(map.get(second) == 0){
                        map.remove(second);
                    }else{
                        pq.add(second);
                    }
                    // add back to PQ
                    pq.add(first);
                }
            }
        }

        return res;
    }

    // LC 358
    // 16.54 - 17.04 pm
    /**
     *  -> string s, and integer k,
     *   rearrange the string such that the same characters
     *    are `AT LEAST distance k from each other.`
     *
     *   -  All input strings are given in lowercase letters.
     *   - If it is not possible to rearrange the string,
     *     return an empty string "".
     *
     *
     *  IDEA 1) PQ + `last idx for distinct val` + hashmap
     *
     *
     *
     *
     *  -----
     *
     *  ex 1)
     *
     *  Input: s = "aabbcc", k = 3
     *  Output: "abcabc"
     *  -> Explanation: The same letters are at least distance 3 from each other.
     *
     *
     *   map: {a: 2, b: 2, c: 2}
     *   PQ: [2,2,2]
     *
     *
     *   -> abcabc
     *
     *
     *   ex 3)
     *
     *   Input: s = "aaadbbcc", k = 2
     *  Output: "abacabcd"
     *   Explanation: The same letters are at least distance 2 from each other.
     *
     *   map: {a:3, b:2, c:2, d: 1}
     *
     *
     *   -> abcabcd
     *
     *
     *
     */
    public String rearrangeString(String s, int k) {
        // edge
        if(s.isEmpty() || s.length() == 0){
            return "";
        }
        if(s.length() == 1){
            if(k == 0){
                return s;
            }
            return ""; // ???
        }

        // map: { val : cnt }
        Map<String, Integer> map = new HashMap<>();
        for(char ch: s.toCharArray()){
            String key = String.valueOf(ch);
            map.put(key, map.getOrDefault(key, 0) + 1);
        }

        // PQ: big PQ,
        // sort on map val
        PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = map.get(o2) - map.get(o1);
                return diff;
            }
        });

        // NOTE !!! add DISTINCT val to PQ
        for(String key: map.keySet()){
            pq.add(key);
        }

        // NOTE !!! the map record the `idx for last same value`
        Map<String, Integer> lastSeenIdx = new HashMap<>();

        String res = "";
        String prev = "";
        int idx = 0;
        // ???
        while(!pq.isEmpty()){
            String first = pq.poll();
            // case 1) prev == "" or
            //         prev != ""  && the dist(same_prev, cur) > k
            if(prev.isEmpty() || (!lastSeenIdx.containsKey(first) || idx - lastSeenIdx.get(first) > k) ){
                res += first;
                prev = first;
                // update to last seen
                lastSeenIdx.put(first, idx);
                if(map.get(first) == 0){
                    map.remove(first);
                }else{
                    map.put(first, map.get(first) - 1);
                    pq.add(first);
                }
            }else{
                // ???
                // case 2) prev != ""  && the dist(same_prev, cur) < k
                List<String> cache = new ArrayList<>();
                while(lastSeenIdx.get(pq.peek()) < k){
                    cache.add(pq.poll());
                }
                // add `newFirst` to res
                String newFirst = pq.poll();
                res += newFirst;
                prev = newFirst;
                // update to last seen
                lastSeenIdx.put(newFirst, idx);
                if(map.get(newFirst) == 0){
                    map.remove(newFirst);
                }else{
                    map.put(first, map.get(newFirst) - 1);
                    pq.add(newFirst);
                }
                // add all tmp val back to PQ
                for(String tmp: cache){
                    pq.add(tmp);
                }
            }

            idx += 1;
        }


        return res.length() == s.length() ? res : "";
    }



    // LC 383
    // 15.22 - 32 pm
    /**
     *  ->  true if ransomNote can be
     *   constructed by using the letters from magazine and false otherwise.
     *
     *
     *   IDEA 1) MAP
     *
     */
    public boolean canConstruct(String ransomNote, String magazine) {
        // edge
        if(ransomNote == null){
            return true;
        }
        // ???
        if(magazine == null){
            return false;
        }

        Map<Character, Integer> map1 = new HashMap<>();
        Map<Character, Integer> map2 = new HashMap<>();

        for(char ch: ransomNote.toCharArray()){
            map1.put(ch, map1.getOrDefault(ch, 0) + 1);
        }

        for(char ch: magazine.toCharArray()){
            map2.put(ch, map2.getOrDefault(ch, 0) + 1);
        }

        System.out.println(">>> map1 = " + map1 +
                ", map2 = " + map2);

        // if ransomNote can be constructed by using the letters from magazine and false otherwise.
        for(char ch: map1.keySet()){
            if(!map2.containsKey(ch) || map2.get(ch) < map1.get(ch)){
                return false;
            }
        }

        return true;
    }

    // LC 2279
    // 15.39 - 49 pm
    /**
     *  -> Return the MAX number of `bags` that
     *    could have `full capacity` after `placing the
     *    additional rocks in some bags.`
     *
     *
     *    -  n bags numbered from 0 to n - 1
     *    -  two 0-indexed integer arrays capacity and rocks.
     *         - capacity[i]
     *         - rocks[i]
     *    -   `additionalRocks`
     *              - the number of additional `rocks` you can place in ANY of the bags.
     *
     *
     *
     *   ----------
     *
     *   IDEA 1) HASH MAP + SORT ????
     *
     *    -> 1. prepare the arr: [ [idx, diff] ]
     *        - idx: original idx of rock, capacity
     *        - diff: diff(cap, rock)
     *       2. sort (small -> big) on diff
     *       3. check how many `max cap` we can make
     *          before use all the additionalRocks,
     *          maintain a `max cap` val as answer as well
     *
     */
    public int maximumBags(int[] capacity, int[] rocks, int additionalRocks) {
        // edge
        if(capacity == null || rocks == null){
            return 0;
        }
        if(capacity.length == 1 || rocks.length == 1){
            if(capacity[0] == rocks[0]){
                return 1;
            }
            return rocks[0] + additionalRocks >= capacity[0] ? 1 : 0;
        }

        // list: [ [idx, diff] ]
        List<Integer[]> list = new ArrayList<>();
        for(int i = 0; i < capacity.length; i++){
            int cap = capacity[i];
            int rock = rocks[i];
            list.add(new Integer[]{i, cap - rock});
        }

        // sort (small -> big) on diff
        Collections.sort(list, new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[1] - o2[1];
                return diff;
            }
        });

        int cnt = 0;

        // ??
        for(Integer[] cur: list){
            // NOTE !!! since we sort on diff (samll -> big)
            // then thr `diff == 0` case should occur first
            // -> it's Ok to early exit when `additionalRocks == 0`
            // without missing any `already full capacity case with an index`
            if(additionalRocks == 0){
                return cnt;
                //break;
            }
            int diff = cur[1];
            // case 1) already `full capacity`
            if(diff == 0){
                cnt += 1;
            }else{
                if(additionalRocks >= diff){
                    additionalRocks -= diff;
                    cnt += 1;
                }else{
                    additionalRocks = 0; // ???
                }
            }

           // cnt += 1;
        }

        return cnt;
    }

    // LC 739
    // 16.12 - 22 pm
    /**
     *  ->  such that answer[i] is the `number of days `
     *      you have to wait after the ith day to get a WARMER temperature.
     *        - if NOT possible, keep answer[i] == 0 instead.
     *
     *
     *
     *  ------
     *
     *   IDEA 1) Stack (FILO, first in last out)
     *
     *    -> `mono increasing Stack)
     *    -> e.g. small -> big Stack
     *
     *
     *    ??? `decreasing` ???
     *
     *
     * -----
     *
     *  ex 1)
     *
     *   Input: temperatures = [73,74,75,71,69,72,76,73]
     *   Output: [1,1,4,2,1,1,0,0]
     *
     *   ->
     *      [73,74,75,71,69,72,76,73], st = [73], ans = [0,0,0,0,0,0,0]
     *       x
     *
     *      [73,74,75,71,69,72,76,73], st = [73], ans = [0,0,0,0,0,0,0]
     *          x                      74 > 73, pop, st = [74], and ans = [1,0,0,0,0,0,0]
     *
     *      [73,74,75,71,69,72,76,73], st = [74], ans = [0,0,0,0,0,0,0]
     *             x                  75 > 75, pop, st = [75], and ans = [1,1,0,0,0,0,0]
     *
     *
     *      [73,74,75,71,69,72,76,73], st = [74, 71], ans = [1,1,0,0,0,0,0]
     *                x         71 < 74
     *
     *      [73,74,75,71,69,72,76,73], st = [74, 71, 69], ans = [1,1,0,0,0,0,0]
     *                   x
     *
     *      [73,74,75,71,69,72,76,73], st = [74, 71, 69], ans = [1,1,0,0,0,0,0]
     *                      x       72 > 69, st = [74, 71], ans = [1,1,0,0,1,0,0]
     *                              72 > 71, st = [74], ans = [1,1,0,2,1,0,0]
     *                              st = [74, 72], ans = [1,1,0,2,1,0,0]
     *
     *
     *    [73,74,75,71,69,72,76,73], st = [74, 71, 69], ans = [1,1,0,0,0,0,0]
     *                       x, st = [76], ans = [1,1,0,2,1,0,0]
     *
     *   [73,74,75,71,69,72,76,73], st = [74, 71, 69], ans = [1,1,0,0,0,0,0]
     *                         x
     *
     *
     */
    public int[] dailyTemperatures(int[] temperatures) {
        // edge
        if(temperatures == null || temperatures.length == 0){
            return null; // ???
        }
        if(temperatures.length == 1){
            return new int[]{0};
        }

        int[] ans = new int[temperatures.length];
        Arrays.fill(ans, 0);

        // hashmap: record val and idx
        // {val : idx}
        Map<Integer, Integer> map = new HashMap<>();
//        for(int i = 0; i < temperatures.length; i++){
//            map.put(temperatures[i], i);
//        }

        System.out.println(">>> (init) ans = " + ans);
        //System.out.println(">>> (init) map = " + map);

        // small stack
        // mono `decreasing` stack
        // big -> small
        // stack: [ [idx, val] ]
        Stack<Integer[]> st = new Stack<>();

        for(int i = 0; i < temperatures.length; i++){
            int cur =  temperatures[i];
            System.out.println(">>> i = " + i +
                    ", cur = " + cur +
                    ", st = " + st);
            if(st.isEmpty()){
                st.add(new Integer[]{i, cur});
            }else{
                while(!st.isEmpty() && st.peek()[1] < cur){
                    Integer[] last = st.pop();
                    int lastIdx = last[0];
                    int lastVal = last[1];
                   // ans[map.get(last)] = (i - map.get(last));
                    ans[lastIdx] = (i - lastIdx);
                }
                st.add(new Integer[]{i, cur});
            }
        }

        System.out.println(">>> (final) ans = " + ans);

        return ans;
    }

    // LC 149
    public int maxPoints(int[][] points) {

        return 0;
    }




}
