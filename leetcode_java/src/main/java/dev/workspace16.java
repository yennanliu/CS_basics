package dev;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class workspace16 {

    // LC 876
    // 14.50 - 15.00 pm
    /**
     *  IDEA: LINKED LIST
     *
     */
    public ListNode middleNode(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }

        // fast, slow pointers
        ListNode fast = head; // ??
        ListNode slow = head; // ??

        while(fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
        }

        return slow;
    }

    // LC 142
    // 14.57 - 15.10 pm
    /**
     *
     *  -> return the node where the cycle begins
     *  -> if NO cycle, return null
     *
     *  IDEA 1) LINKED LIST OP
     *
     */
    public ListNode detectCycle(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return null;
        }
        HashSet<ListNode> set = new HashSet<>();
        while(head != null){
            if(set.contains(head)){
                return head; // ???
            }
            set.add(head);
            head = head.next;
        }

        return null;
    }

    // LC 82
    // 9.33 - 10.00 am
    /**
     * IDEA 1) LINKED LIST OP
     *
     * IDEA 2) LINKED LIST -> array -> dedup -> linkedlist
     *
     *
     */
    // 10.18 - 10.23 am
    // IDEA 1) pure LINKED LIST  op + 2 pointers ???
    public ListNode deleteDuplicates(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }

        ListNode dummy = new ListNode();
        ListNode res = dummy;

        ListNode _prev = null;
        ListNode _next = null;

        while(head != null){

            _prev = head;

            // case 1) _prev != cur
            _prev.next = new ListNode(head.val); // ???

            // case 2) _prev == cur
            while(_prev.val == head.val){
                _prev = _prev.next;
            }



            head = head.next;
        }

        return res.next;
    }
    // IDEA 2) LINKED LIST -> array -> dedup -> linkedlist
    public ListNode deleteDuplicates_100(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }
        //Set<Integer> set = new HashSet<>(); // order
        Set<Integer> set = new LinkedHashSet<>();
        Map<Integer, Integer> map =new HashMap<>(); // {val : cnt}

        while(head != null){
            Integer val = head.val;
            map.put(val, map.getOrDefault(val, 0) + 1);
            set.add(val);
            head = head.next;
        }
        ListNode dummy = new ListNode();
        ListNode res = dummy; // ???

        System.out.println(">>> map = " + map);
        System.out.println(">>> set = " + set);

        for(Integer x: set){
            if(map.get(x) == 1){
                ListNode node = new ListNode(x);
                dummy.next = node;
                dummy = node;
            }
        }

        System.out.println(">>> dummy = " + dummy);

        return res.next;
    }


    // LC 61
    // 14.32 - 14.43 pm
    /**
     *
     * -> Given the head of a linked list,
     *   `rotate` the list to the` right by k places.`
     *
     *
     *  IDEA 1) LINKED LIST OP
     *
     *
     *
     */
    public ListNode rotateRight(ListNode head, int k) {
        // edge
        if(k == 0 || head == null || head.next == null){
            return head;
        }

        //List<Integer> list = new ArrayList<>();
        Deque<Integer> dequeue = new ArrayDeque<>(); // ???

        ListNode head2 = head;

        // get len
        //int len = 0;
        while(head != null){
           // len += 1;
            //list.add(head.val);
            dequeue.add(head.val);
            head = head.next;

        }

        int len = dequeue.size();

        System.out.println(">>> (before normalized) k = " + k);
        System.out.println(">>> len = " + len);


        // edge
        if(k >= len){
            if(len % k == 0){
                return head2;
            }
            // ??
            k = (k %len);
        }

        System.out.println(">>> (after normalized) k = " + k);

        // `rotate`
        int i = 0;
        while(!dequeue.isEmpty() && i < k){
            // pop `last`
            int val = dequeue.pollLast(); // ??
            // append pop val to first idx
            dequeue.addFirst(val);
            i += 1;
        }

        ListNode dummy = new ListNode();
        ListNode res = dummy;

        System.out.println(">>> dequeue = " + dequeue);

//        for(Integer x: dequeue){
//            ListNode cur = new ListNode(x);
//            dummy.next = cur;
//            dummy = cur;
//        }

        while(!dequeue.isEmpty()){
            ListNode cur = new ListNode(dequeue.pollFirst());
            dummy.next = cur;
            dummy = cur;
        }

        return res.next; // ???
    }

    // LC 101
    // 9.36 - 9.46 am
    /**
     *  IDEA 1): DFS
     *
     */
    public boolean isSymmetric(TreeNode root) {
        // edge
        if (root == null) {
            return true;
        }

        return isSymmetricHelper(root.left, root.right);
    }

    // t1: sub left tree, t2: sub right tree
    private boolean isSymmetricHelper(TreeNode t1, TreeNode t2){
        if(t1 == null && t2 == null){
            return true;
        }
        if(t1 == null || t2 == null){
            return false;
        }
        if(t1.val != t2.val){
            return false;
        }

        // ONLY need to compare `one`  ???
        return isSymmetricHelper(t1.left, t2.right)  &&
                isSymmetricHelper(t1.right, t2.left);
    }


    // LC 100
    // 9.47 - 9.57 am
    /**
     *  IDEA 1) DFS
     *
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        // edge
        if(p == null && q == null){
            return true;
        }
        if(p == null || q == null){
            return false;
        }
        if(p.val != q.val){
            return false;
        }

        return isSameTree(p.left, q.left) &&
                isSameTree(p.right, q.right);
    }


    // LC 226
    // 10.00 - 10.10 am
    /**
     * IDEA 1) DFS
     *
     * IDEA 2) BFS
     *
     *
     */
    public TreeNode invertTree(TreeNode root) {
        // edge
        if(root == null){
            return root;
        }
        // ???
        if(root.left == null && root.right == null){
            return root;
        }
        if(root.left == null || root.right == null){
            if(root.left == null){
                root.left = root.right;
            }else{
                root.right = root.left;
            }
            return root;
        }

        // cache
//        TreeNode _right = root.right;
//        TreeNode _left = root.left;
        // ???
        TreeNode _right = invertTree(root.left); // ????
        TreeNode _left = invertTree(root.right);

        root.left = _left;
        root.right = _right;

        return root;
    }

    //private boolean

    // LC 617
    // 10.19 - 10.29 am
    /**
     *  IDEA 1) DFS
     *
     */
    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
        // edge
        if(root1 == null && root2 == null){
            return root1;
        }
//        if(root1 == null || root2 == null){
//            if(root2 == null){
//                return root1;
//            }
//            return root2;
//        }

        int val1 = root1 != null ? root1.val: 0;
        int val2 = root2 != null ? root2.val: 0;

        // dfs
        // pre-order traverse (root -> left -> right)
        //int newVal = root1.val + root2.val;
        root1.val = val1 + val2;

        // ????
        TreeNode _left = mergeTrees(root1.left, root2.left);
        TreeNode _right = mergeTrees(root1.right, root2.right);

        root1.left = _left;
        root1.right = _right;

        return root1;
    }


    // LC 116
    // 10.38 - 10.55 AM
    /**  IDEA
     *
     *
     *
     */
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }

    /**
     *  IDEA 1) BFS (visit layer by layer)
     *
     *  IDEA 2) DFS
     *
     */
    // IDEA 1) BFS (visit layer by layer)
    public Node connect(Node root) {
        // edge
        if(root == null){
            return root;
        }
        if(root.left == null && root.right == null){
            root.next = null;
            return root;
        }

        // bfs
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        //int layer = 0;

        Node dummy = new Node(); // ???
        Node res = dummy;

        while(!q.isEmpty()){
            int _size = q.size();
            System.out.println(">>> _size = " + _size + ", q = " + q);
            //dummy = q.poll();
            Node cur = null;
            for(int i = 0; i < _size; i++){
                cur = q.poll();
                System.out.println(">>> cur = " + cur);
                dummy.next = cur;
                dummy = cur;
            }

            // ???
            dummy.next = null; // ??
        }

        System.out.println(">>> dummy = " + dummy + ", res = " + res);

        return res.next;
    }

    // LC 2290
    // 18.06 - 18.16 PM
    /**
     *
     *
     *  -> return the `minimum` number of `obstacles` to `remove` so
     *  you can move from the upper left corner (0, 0) to
     *   the lower right corner (m - 1, n - 1).
     *
     *
     *   1. ALWAYS move from (0,0) to (m-1, n-1)
     *   2. can move up, down, left, or right from and to an empty cell.
     *   3.  0 : empty cell
     *       1: obstacle cell
     *
     *
     *
     *
     *  IDEA 1) Dijkstra ALGO
     *
     * IDEA 2) bfs
     */
    // 11.02 - 11.12 am
    // IDEA 2) Dijkstra ALGO v2
    /**
     *  Dijkstra ALGO v2
     *
     *  1. use `grid` to record the `cost` of op
     *  2. NOT need to maintain a `visit` var / grid,
     *     the nature of Dijkstra algo will take care of it
     *
     */
    public int minimumObstacles(int[][] grid) {
        // edge
        if(grid == null){
            return 0;
        }
        if(grid.length == 0 || grid[0].length == 0){
            return 0;
        }

        int l = grid.length; // y
        int w = grid[0].length; // x

        int[][] costs = new int[l][w];
        // init all costs as `0` ????
        // ????
        //Arrays.fill(costs, Integer.MAX_VALUE); // ???? MAX_VALUE or 0 ????
        for (int i = 0; i < l; i++) {
            Arrays.fill(costs[i], Integer.MAX_VALUE);
        }

        int minCost = 0;

        Integer[][] moves = new Integer[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };

        // PQ: [cost, x, y]
        // sort on `cost`
        // small -> big
        PriorityQueue<List<Integer>> pq = new PriorityQueue<>(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                int diff = o1.get(0) - o2.get(0);
                return diff; // ??
            }
        });

        // ??
        List<Integer> tmp = new ArrayList<>();
        tmp.add(0);
        tmp.add(0);
        tmp.add(0);
        pq.add(tmp);

        while(!pq.isEmpty()){

            List<Integer> cur = pq.poll();
            int _cost = cur.get(0);
            int x = cur.get(1);
            int y = cur.get(2);

            // early quit
            if(x == w - 1 && y == l - 1){
                return minCost; // ???
                //return Math.min(costs[l-2][w-1], costs[l-1][w-2]); // ????
            }

            // loop over all possible dirs
            for(Integer[] mv: moves){
                int _x = x + mv[0];
                int _y = y + mv[1];
                if(_x >= 0 && _x < w && _y >= 0 && _y < l && costs[_y][_x] < _cost){
                    // ???
                    int newCost = 0;
                    if(costs[_y][_x] == 1){
                        newCost = 1;
                    }
                    // update costs grid
                    costs[_y][_x] = costs[_y][_x] + newCost;
                    // update minCost ???
                    minCost = Math.min(minCost, costs[_y][_x]);
                    // ???
                    List<Integer> tmp2 = new ArrayList<>();
                    tmp2.add(costs[_y][_x]);
                    tmp2.add(_x);
                    tmp2.add(_y);
                    pq.add(tmp2);
                }
            }

        }

        // SHOULD NOT meet this logic
        return -1;
    }




    // IDEA 1) Dijkstra ALGO
    public int minimumObstacles_100(int[][] grid) {
        // edge
        if(grid == null){
            return 0;
        }
        if(grid.length == 0 || grid[0].length == 0){
            return 0;
        }

        int m = grid.length; // y
        int n = grid[0].length; // x
        // ???
        boolean[][] visited = new boolean[m][n];
        System.out.println(">>> visited = " + visited);

        int opCnt = 0;

        // Dijkstra
        // PQ:  pq([x1, y1], [x2, y2], ....)
        //    it's a `small PQ`, so ordering based on `matrix value`
        //    small -> big
        PriorityQueue<List<Integer>> pq = new PriorityQueue<>(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                int x1 = o1.get(1);
                int y1 = o1.get(0);
                int x2 = o2.get(0);
                int y2 = o2.get(1);
                // ???
                int diff = grid[y1][x1] - grid[y2][x2];
                return diff;
            }
        });

        Integer[][] moves = new Integer[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };

        // ??? a better way??
        List<Integer> tmp = new ArrayList<>();
        tmp.add(0);
        tmp.add(0);
        pq.add(tmp);

        while(!pq.isEmpty()){
            List<Integer> cur = pq.poll();
            int x = cur.get(0);
            int y = cur.get(1);

            System.out.println(">>> x = " + x + ", y = " + y + ", cur = " + cur);

            // if `reach destination` quit earlier
            if(x == n-1 && y == m-1){
                System.out.println(">>> if `reach destination` quit earlier");
                return opCnt;
            }

            // check if `need move obstacle`
            if(grid[y][x] == 1){
                opCnt += 1;
            }

            // update visited
            visited[y][x] = true;

            // NOTE !!! validate first
            for(Integer[] move: moves){
                int _x = x + move[0];
                int _y = y + move[1];

                System.out.println(">>> _x = " + _x + ", _y = " + _y + ", move = " + move);

                if( _x >= 0 && _x < n && _y >= 0 && _y < m && !visited[_y][_x]){
                    List<Integer> tmp2 = new ArrayList<>();
                    tmp2.add(_x);
                    tmp2.add(_y);
                    pq.add(tmp2);
                }
            }
        }

        return opCnt;
    }

    // LC 111
    // 9.36 - 9.46 am
    /**
     *
     *  -> Given a `binary tree`, find its `minimum depth.`
     *
     *  - depth:
     *
     *     - The `minimum depth` is
     *       the number of nodes along the
     *       shortest path from the root node
     *       down to the nearest leaf node.
     *
     *     - min path from root to nearest sub node
     *
     *
     *  IDEA 1) DFS
     *
     *  IDES 2) BFS
     *
     *
     */
    public int minDepth(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }

        // post order traverse
        // left -> right -> root

        int leftDepth = minDepth(root.left);
        int rightDepth = minDepth(root.right);

        return 1 + Math.max(leftDepth, rightDepth);
    }

    // LC 543
    // 10.13 am - 10.23 am
    /**
     *
     * -> Given the root of a binary tree,
     *    return the length of the `diameter` of the tree.
     *
     *    1. The `diameter` of a binary tree is the `length` of the
     *      `longest` path between any `two nodes` in a tree.
     *      This path `may or may not pass` through the root.
     *
     *    2. length = `number of edges between 2 nodes)
     *
     *   IDEA 1) DFS
     *
     *     -> post order traverse
     *     -> `left -> right -> root`
     *        -> so when visit a `node`
     *           we CAN already the `possible max length` that pass this node via
     *           int maxLen = maxLeftLen + maxRightLen
     *       -> do above recursively
     *       -> we can get the `global max len` when compute on `root node` as final step
     *
     *   IDEA 2) BFS
     *
     */
    // IDEA 1) DFS
    int maxLen = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
//        if(root.left == null && root.right == null){
//            return 1;
//        }
//
//        int maxLeftLen = diameterOfBinaryTree(root.left);
//        int maxRightLen = diameterOfBinaryTree(root.right);

        // ???
        //maxLen = Math.max(maxLeftLen, maxRightLen);
        getLenHelper(root);

        return maxLen;
    }

    private int getLenHelper(TreeNode root){
        // edge
        if(root == null){
            return 0;
        }

        int maxLeftLen = getLenHelper(root.left);
        int maxRightLen = getLenHelper(root.right);

        // ??? update the global max len
        maxLen = Math.max(maxLen, maxLeftLen + maxRightLen);

        //return maxLeftLen + maxRightLen + 1;
        return 1 + Math.max(maxLeftLen, maxRightLen); // ????
    }

    // LC 110
    // 14.10 - 14.20 pm
    /**
     * -> Given a binary tree, determine if it is ` height-balanced.`
     *
     *  - A `height-balanced` binary tree is a binary tree in which the depth of
     *    the` two subtrees` of every node `never` differs by `more than one.`
     *
     *  - e.g. if `height-balanced`, for every node in two subtrees,
     *         the diff <= 1
     *
     *
     *  IDEA 1) DFS
     *
     *  IDEA 2) BFS
     *
     */
    // IDEA 1) DFS
    public boolean isBalanced(TreeNode root) {
        // edge
        if(root == null){
            return true;
        }
        if(root.left == null && root.right == null){
            return true;
        }

        // DFS: post order traverse: left -> right -> root
        // check if `right - left` bigger than 1

        int leftDepth = getNodeDepth2(root.left);
        int rightDepth = getNodeDepth2(root.left);

        System.out.println(">>> (isBalanced) leftDepth = " + leftDepth);
        System.out.println(">>> (isBalanced) rightDepth = " + rightDepth);

        if(Math.abs(leftDepth - rightDepth) > 1){
            return false;
        }

        // ????
        return isBalanced(root.left)
                && isBalanced(root.right);
    }

    private int getNodeDepth2(TreeNode root){
        System.out.println(">>> (getNodeDepth) root = " + root.val);
        // edge
        if(root == null){
            return 0;
        }
//        int depth = 0;
//        depth = Math.max(
//                getNodeDepth2(root.left),
//                getNodeDepth2(root.right)
//        ) + 1; // ????

        return Math.max(
                getNodeDepth2(root.left),
                getNodeDepth2(root.right)
        ) + 1;

    }


    // get the `depth` of a specific node
//    private int getNodeDepth(TreeNode root){
//        System.out.println(">>> (getNodeDepth) root = " + root.val);
//        // edge
//        if(root == null){
//            return 0;
//        }
//        int depth = 0;
//        // ???
//        if(root != null){
//            depth += getNodeDepth(root.)
//        }
//        return getNodeDepth(root) + 1;
//    }


    // LC 112
    // 15.05 - 15.15 pm
    /**
     * Given the root of a binary tree and an integer targetSum,
     * return `true` if the tree has a root-to-leaf path such
     * that adding up all the values along the path equals targetSum.
     *
     * -> return true if `there exists a path that its path sum equals to target`
     *
     * - A leaf is a node with no children.
     *
     *  IDEA 1) DFS
     *
     *  IDEA 2) BFS
     *
     *
     *   ex 1)
     *
     *   [1,2,null,3,null,4,null,5]
     *
     *          1
     *        2
     *      3
     *    4
     * 5
     *
     */
    // ???
    Map<Integer, Integer> pathSumMap = new HashMap<>();
    public boolean hasPathSum(TreeNode root, int targetSum) {
        // edge
        if (root == null) {
            return false;
        }
        if(root.left == null && root.right == null){
            return root.val == targetSum;
        }

        System.out.println(">>> (before op) pathSumMap = " + pathSumMap);

        // post order traverse: left -> right -> root
        getPathHelper(root, 0);

        //pathSumMap.remove(root.val); //????
        System.out.println(">>> (after op) pathSumMap = " + pathSumMap);

        if(pathSumMap.containsValue(targetSum)){
            return true;
        }

        return false;
    }

    private void getPathHelper(TreeNode root, Integer curSum){
        //return 0;
        if(root == null){
            return;
        }

        int newSum = curSum + root.val;
        // ONLY add to map if `has NO children`
        if(root.left == null && root.right == null){
            pathSumMap.put(newSum, newSum); // ???
        }

        getPathHelper(root.left, newSum);
        getPathHelper(root.right, newSum);
    }

    // LC 113
    // 15.45 - 15.55 pm
    /**
     *
     * -> return `all root-to-leaf paths`
     *  where the` sum` of the node values in the path equals `targetSum`
     *
     *  -  Each path should be returned as a list of the node values,
     *
     *  IDEA 1) DFS
     *
     *
     */
    // ???
    Map<List<Integer>, Integer> pathSumMap2 = new HashMap<>();
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> res = new ArrayList<>();
        // edge
        if(root == null){
            return res;
        }
        if(root.left == null && root.right == null){
            if(root.val == targetSum){
                List<Integer> tmp = new ArrayList<>();
                tmp.add(root.val);
                res.add(tmp);
            }
            return res;
        }

        // dfs
        getPathHelper2(root, 0, new ArrayList<>());
        System.out.println(">>> pathSumMap2 = " + pathSumMap2);

        for(List<Integer> key: pathSumMap2.keySet()){
            // ????
            // or a simpler way to compare `int` type directly ??
            System.out.println(">>> key = " + key +
                    ", pathSumMap2.get(key) = " + pathSumMap2.get(key)
                    + " new Integer(targetSum) =  " + new Integer(targetSum) );

            if(pathSumMap2.get(key).equals(new Integer(targetSum))){
                res.add(key);
            }
        }

        return res;
    }

    private void getPathHelper2(TreeNode root, Integer curSum, List<Integer> nodes){
        //return 0;
        if(root == null){
            return;
        }

        int newSum = curSum + root.val;
        nodes.add(root.val);

        // ONLY add to map if `has NO children`
        if(root.left == null && root.right == null){
            pathSumMap2.put(nodes, newSum); // ???
        }

        getPathHelper2(root.left, newSum, nodes);
        getPathHelper2(root.right, newSum, nodes);
        // backtrack ??? // undo ????
        nodes.remove(nodes.size() - 1); //????
    }

    /// ////////////////////////////

    // LC 92
    // 17.17 - 17.33 pm
    /**
     *
     *  IDEA 1) LINKED LIST OP (iterative)
     *   - prev, dummy
     *
     *
     *  ex 1)
     *
     *  Input: head = [1,2,3,4,5], left = 2, right = 4
     *  -> Output: [1,4,3,2,5]
     *
     *    [1,2,3,4,5]    _next = 2 == left, _left_prev = 1, quit first while loop ???
     *     x
     *
     *    [1,2,3,4,5]
     *       x
     *
     *    [1,2,3,4,5]
     *         x
     *
     *   [1,2,3,4,5]
     *          x
     *
     *  [1,2,3,4,5]  _right_next = 5
     *           x
     *
     *
     *    reverse node [2,3,4], -> [4,3,2]
     *    _left = 4, _right = 2
     *
     *   so, what we need to do is:
     *    reconnect node
     *     _left_prev.next = _right
     *     _right.next = _right_next
     *
     *
     *
     *
     *  IDEA 2) array -> reverse -> LINKED LIST
     *
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        // edge cases: empty list, single node, or no reversal needed
        if (head == null || head.next == null || left == right) {
            return head;
        }

        ListNode dummy = new ListNode(0); // ??
        ListNode res = dummy;
        dummy.next = head;

        ListNode _prev = null; // ???dummy; // ???? null;

        for(int i = 0 ; i < left; i++){
            head = head.next;
        }

        _prev = head; //????

        ListNode _cur = _prev.next;
        ListNode _head = null; // ??

        // reverse
        for(int i = 0; i < right - left; i++){
            ListNode _next = _cur.next; // ???
            // ??????
            _prev.next = _head; //_cur.next;
            // ??? _prev = _cur.next;
            _head = _cur;
            _cur = _next;
        }

        ListNode _tail = _prev.next;

        _tail.next = _cur;

        return res.next;
    }




    // IDEA 1) LINKED LIST OP (iterative)
    public ListNode reverseBetween_100(ListNode head, int left, int right) {
        // edge
        if (head == null || head.next == null || left == right) {
            return head;
        }

        // ??
        ListNode dummy = new ListNode();
        ListNode res = dummy;
        dummy.next = head; // ???

        // node before `left node`
        ListNode _left_prev = null;
        // node after `right node`
        ListNode _right_next = null;

        for(int i = 0; i < left; i++){
            head = head.next;
        }

        // left border node of the `to-reverse node`
        ListNode _left = head;
        // right border node of the `to-reverse node`
        //ListNode _right = null;

        for(int i = 0; i < right - left; i++){
            head = head.next;
        }

        ListNode _right = head;





        // loop over node, and get _prev_left, _next_right
        while(head != null && head.next != null){
            if(head.next.val == left){
                _left_prev = head;
            }
            if(head.val == right){
                _right_next = head.next;
            }

            if(head.val == left){
                _left = head;
            }

            if(head.val == right){
                _right = head;
            }

            head = head.next;
        }

        // reverse
        ListNode _prev = null;
        // ???
        while(_left.val != right){
            ListNode _next = _left.next;
            _prev.next = _left;
            _prev = _left;
            _left = _next;
        }

        // reconnect
        _left_prev.next = _right;
        _right.next = _right_next;


        return res.next;
    }




    //---------------------------

    // LC 572
    // 15.29 - 39 pm
    /**
     *  IDEA 1) DFS
     *
     *  IDEA 2) BFS
     *
     */
    // IDEA 1) DFS
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        // edge
        if(root == null && subRoot == null){
            return true;
        }
        if(subRoot == null){
            return true; // ???
        }
        if(root == null){
            return false;
        }

        // ???
        if(isSameTree2(root, subRoot)){
            return true;
        }

        // ???
        //return isSubTreeHelper(root, subRoot);
        /** NOTE !!! `or` condition */
        return isSubtree(root.left, subRoot) ||
                isSubtree(root.right, subRoot);
    }

//    private boolean isSubTreeHelper(TreeNode root, TreeNode subRoot){
//        // bfs ???
//        Queue<TreeNode> q = new LinkedList<>();
//        q.add(root);
//        while(!q.isEmpty()){
//            TreeNode cur = q.poll();
//            if(isSameTree2(cur, subRoot)){
//                return true;
//            }
//            if(cur.left != null){
//                q.add(cur.left);
//            }
//            if(cur.right != null){
//                q.add(cur.right);
//            }
//        }
//        return false;
//    }

    private boolean isSameTree2(TreeNode t1, TreeNode t2){
        if(t1 == null && t2 == null){
            return true;
        }
        if(t1 == null || t2 == null){
            return false;
        }
        if(t1.val != t2.val){
            return false;
        }
        return isSameTree2(t1.left, t2.left)
                && isSameTree2(t1.right, t2.right);
    }

    // LC 652
    // 16.02 - 12 pm
    /**
     *
     * -> Given the root of a binary tree,
     *   return `all duplicate subtrees.`
     *
     *    - ONLY need to return the `root node` of any one of them.
     *
     *    - Two trees are duplicate if
     *       they have the `same structure` with the `same node values.`
     *
     *
     *  IDEA 1) DFS + HASHMAP
     *
     */

    Map<TreeNode, List<String>> pathMap = new HashMap<>();
    public List<TreeNode> findDuplicateSubtrees_0_1(TreeNode root) {
        List<TreeNode> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        System.out.println(">>> (before dfs) pathMap = " + pathMap);
        // dfs
        duplicateSubtreesHelper(root, new HashMap<>());
        System.out.println(">>> (after dfs) pathMap = " + pathMap);

        // check duplicates
        for (TreeNode node : pathMap.keySet()) {
            if (pathMap.get(node).size() > 1) {
                res.add(node);
            }
        }

        return res;
    }

    private String duplicateSubtreesHelper(TreeNode root, Map<String, TreeNode> seen) {
        if (root == null) {
            return "#"; // marker for null
        }

        // build serialization
        String path = root.val + "," +
                duplicateSubtreesHelper(root.left, seen) + "," +
                duplicateSubtreesHelper(root.right, seen);

        // if this path already exists, add it under the original root node
//        List<String> paths = new ArrayList<>();
//        if (seen.containsKey(path)) {
//            TreeNode firstRoot = seen.get(path);
//            pathMap.computeIfAbsent(firstRoot, k -> new ArrayList<>()).add(path);
//        } else {
//            seen.put(path, root);
//            pathMap.computeIfAbsent(root, k -> new ArrayList<>()).add(path);
//        }
        if (seen.containsKey(path)) {
            TreeNode firstRoot = seen.get(path);
            if (!pathMap.containsKey(firstRoot)) {
                pathMap.put(firstRoot, new ArrayList<>());
            }
            pathMap.get(firstRoot).add(path);
        } else {
            seen.put(path, root);
            if (!pathMap.containsKey(root)) {
                pathMap.put(root, new ArrayList<>());
            }
            pathMap.get(root).add(path);
        }

        return path;
    }


    /// /////

    // ???
//    Map<TreeNode, List<String>> pathMap = new HashMap<>();
//    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
//        List<TreeNode> res = new ArrayList<>();
//        // edge
//        if(root == null){
//            return res;
//        }
//        if(root.left == null && root.right == null){
//            return res;
//        }
//
//        System.out.println(">>> (before dfs) pathMap = " + pathMap);
//        // dfs
//        duplicateSubtreesHelper(root);
//        System.out.println(">>> (after dfs) pathMap = " + pathMap);
//
//        // ???
//        for(TreeNode k: pathMap.keySet()){
//            // ???
//            if(pathMap.get(k).size() > 1){
//                res.add(k);
//            }
//        }
//
//        return res;
//    }
//
//    private void duplicateSubtreesHelper(TreeNode root){
//        if(root == null){
//            return;
//        }
//
//        System.out.println(">>> (duplicateSubtreesHelper) root = " + root);
//
//        String path = getNodePath(root);
//        List<String> paths = new ArrayList<>();
//        if(pathMap.containsKey(root)){
//            paths = pathMap.get(root);
//        }
//        paths.add(path);
//
//        pathMap.put(root, paths);
//
//        // dfs traverse ???
//        // pre order ?? (root -> left -> right)
//        duplicateSubtreesHelper(root.left);
//        duplicateSubtreesHelper(root.right);
//    }
//
//    // dfs
//    private String getNodePath(TreeNode root){
//        System.out.println(">>> (getNodePath) root = " + root);
//
//        if(root == null){
//            return ""; // ??
//        }
//        return root + ","
//                + getNodePath(root.left) + ","
//                + getNodePath(root.right);
//    }


}
