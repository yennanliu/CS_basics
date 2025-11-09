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
    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {

        return false;
    }


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
    public String[] findRelativeRanks(int[] score) {

        return null;
    }



}
