package dev;

import LeetCodeJava.BinarySearchTree.LowestCommonAncestorOfABinaryTree3;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.Tree.DiameterOfNAryTree;

import java.util.*;

public class workspace11 {

  // LC 122
  // 3.34 - 3.44 pm
  /**
   * On each day, you may decide to buy and/or sell the stock.
   *  You can only hold at most one share of the stock at any time.
   *  However, you can buy it then immediately sell it on the same day.
   *
   *
   *  -> Find and return the `maximum profit` you can achieve.
   *
   *
   *   IDEA 1) GREEDY
   *
   *      -> define 3 var
   *        -> local_max, local_min, global_max ??
   *
   *
   *   IDEA 2) DP
   *
   *   IDEA 3) k**** algo ???
   *
   *
   *
   *
   *   exp 1)
   *
   *   Input: prices = [7,1,5,3,6,4]
   *   Output: 7
   *
   *
   *  [7,1,5,3,6,4]
   *     b s         -> p = 4
   *         b s     -> p = 4 + (3) = 7
   *
   *  -> 7
   *
   *  [7,1,5,3,6,4]
   *     x
   *   l_min = 1
   *   l_max= 7
   *   pr=
   *
   *
   */
  public int maxProfit(int[] prices) {
      // edge
      if(prices == null || prices.length == 0){
          return 0;
      }
      if(prices.length == 1){
          return 0;
      }
      if(prices.length == 2){
          if(prices[1] > prices[0]){
              return prices[1] - prices[0];
          }
          return 0;
      }

      // greedy
      int profit = 0;
//      int local_min = prices[0];
//      int local_max = prices[1];

      int prev_price = prices[0];

      for(int i = 1; i < prices.length; i++){

          int val = prices[i];
          if(val < prev_price){
              prev_price = val;
          }else{
              profit += (val - prev_price);
              prev_price = val; // ???
             // prev_price = -1;
          }

      }

      return profit;
    }

  // LC 978
  // 4.13 - 4.23 pm
  /**
   *
   *  -> Given an integer array arr,
   *    return the length of a `maximum` size
   *     turbulent subarray of arr.
   *
   *
   *    turbulent array:
   *      given [arr[i], arr[i + 1], ..., arr[j]]
   *
   *      ->  for  i <= k < j,
   *          arr[k] > arr[k+1] when k is odd
   *          and
   *          arr[k] < arr[k + 1] when k is even
   *
   *      or
   *
   *      ->  for  i <= k < j,
   *          arr[k] > arr[k + 1] when k is even, and
   *          and
   *          arr[k] < arr[k + 1] when k is odd.
   *
   *
   *   IDEA 1) GREEDY ???
   *
   */
  public int maxTurbulenceSize_(int[] arr) {
      // edge
      if(arr == null || arr.length == 0){
          return 0;
      }
      if(arr.length == 1){
          return 1;
      }

      int max_len = 1;
      for(int i = 0; i < arr.length; i++){
          // ??
          for(int j = i+1; j < arr.length - 1; j++){
              if(arr[j] > arr[j-1] && arr[j] < arr[j+1]){
                  max_len = Math.max(max_len, j - i + 1);
              }else{
                  // NOT possible to go further
                  // per given (i, and the current j)
                  // so we break the 2nd loop
                  // and start a `new i` loop
                  break;
              }
          }
      }

      return max_len;
    }


    ////////


//    public int maxTurbulenceSize(int[] arr) {
//        int n = arr.length;
//        if (n == 1)
//            return 1;
//
//        /**
//         * maxLen:
//         *    stores the maximum turbulent subarray length found so far.
//         *    Starts at 1 (any element alone is trivially turbulent).
//         *
//         * start: the start index of the current turbulent window.
//         *
//         */
//        int maxLen = 1;
//        int start = 0;
//
//        /**
//         * Start from the second element.
//         *
//         * We’re comparing arr[i - 1] and arr[i], so i starts at 1.
//         *
//         */
//        for (int i = 1; i < n; i++) {
//
//            /**
//             * Compares adjacent elements and stores the result in cmp:
//             *
//             *   - Returns -1 if arr[i - 1] < arr[i]
//             *
//             *   - Returns 0 if arr[i - 1] == arr[i]
//             *
//             *   - Returns 1 if arr[i - 1] > arr[i]
//             *
//             *
//             *  -> This is how we capture the direction of change.
//             *
//             */
//            int cmp = Integer.compare(arr[i - 1], arr[i]);
//
//            /**
//             * If two adjacent elements are equal, the turbulence breaks.
//             *
//             * We reset the start of our current window to i.
//             *
//             */
//            if (cmp == 0) {
//                // Equal values: turbulence broken, reset window
//                // NOTE !!! we `reset` window below
//                start = i;
//            }
//            /**
//             *  NOTE !!!
//             *
//             *  We check two things:
//             *
//             * 1) i == n - 1:
//             *         if we’re at the `last` element,
//             *         we can’t compare with the next one,
//             *         so we treat this as the END of the window.
//             *
//             *
//             * 2) cmp * Integer.compare(arr[i], arr[i + 1]) != -1:
//             *
//             *   - Checks whether the comparison direction `flips`.
//             *
//             *   - If cmp = 1 (descending), the next should be ascending -1 → product = -1
//             *
//             *   - If cmp = -1, the next should be descending 1 → product = -1
//             *
//             *  -  Anything else (1*1, -1*-1, or 0) → turbulence is broken
//             *
//             */
//            else if (i == n - 1 || cmp * Integer.compare(arr[i], arr[i + 1]) != -1) {
//                // Turbulence breaks at next step — record max
//                /**
//                 *
//                 *  NOTE !!!  if `Turbulence` breaks
//                 *
//                 *
//                 *  - Update the maxLen if the current turbulent
//                 *    sub-array is longer.
//                 *
//                 * -  Reset start to begin a new window from i.
//                 *
//                 */
//                maxLen = Math.max(maxLen, i - start + 1);
//                start = i;
//            }
//        }
//
//        // Return the maximum turbulent window size found.
//        return maxLen;
//    }

    // LC 2013
    // 10.06 - 10.16 am
    class DetectSquares {

        // hash set : track if existed
        HashSet<String> set;

        // map: check `point` count
        // { "x-y": cnt }
        Map<String, Integer> map;


        public DetectSquares() {
            this.set = new HashSet<>();
            this.map = new HashMap<>();
        }

        public void add(int[] point) {
            String key  = point[0] + "-" + point[1];
            this.set.add(key);

            this.map.put(key, this.map.getOrDefault(key, 0) + 1);
        }

        public int count(int[] point) {

            // edge case
            if(this.set.isEmpty()){
                return 0;
            }
            // not possible to make a `square`
            if(this.map.keySet().size() < 3){
                return 0;
            }

            int cnt = 0;

            int input_x = point[0];
            int input_y = point[1];
            String input_val = input_x + "-" + input_y;

//            // edge case : if the point is same as one of the saved points
//            // ???
//            if(this.map.containsKey(input_val)){
//                return 0; // ???
//            }

            // loop over saved points
            for(String k: this.map.keySet()){

                String[] k_arr = k.split("-");
                int _x = Integer.parseInt(k_arr[0]);
                int _y = Integer.parseInt(k_arr[1]);

                // NOTE !!! make sure we are NOT check the `same point` as the input one
                if(_x == input_x && _y == input_y){
                    continue;
                }


                String key_1 = _x + "-" + input_y;
                String key_2 = input_x + "-" + _y;

                String key_3 = _x + "-" + _y;

                boolean shouldProceed = Math.abs(_x - input_x) == Math.abs(_y - input_y);


//                boolean shouldProceed = this.map.containsKey(key_1) &&
//                        this.map.containsKey(key_2) && this.map.containsKey(key_3);

                if(shouldProceed){

                    boolean existed = this.map.containsKey(key_1) &&
                      this.map.containsKey(key_2) && this.map.containsKey(key_3);

                    if(existed){
                        cnt += (this.map.get(key_1) * this.map.get(key_2) * this.map.get(key_3));
                    }

                }

            }

            return cnt;
        }
    }

    // LC 918
    // 11.50 - 12,00 pm
    /**
     *  define 4 var
     *
     *  1. local_max
     *  2. local_min
     *  3. global_max
     *  4. global_min
     *
     *
     */
    public int maxSubarraySumCircular(int[] nums) {

        int res = nums[0];
        int local_max = nums[0];
        int local_min = nums[0];

        int max_val = nums[0];


      // edge
      for(int i = 1; i < nums.length; i++){

          int val = nums[i];

          local_max = Math.max(val, local_max + val);
          local_min = Math.min(val, local_min + val);

          max_val = Math.max(max_val, val);


          res = Math.max(local_max, local_min);
      }

      //res = Math.max(res, r)

      // ???
      return res > 0 ? Math.max(res, res - local_min) : max_val;

    }

    // LC 678
    // 12.57 - 1.07 pm
    /**
     *
     *   IDEA 1) GREEDY + MIN, MAX left, right parenthesis
     *
     *
     *   exp 1)
     *
     *   s = "(  )"
     *        x
     *   left  = 1
     *
     *    (  )
     *       x
     *
     *       min_right = 1
     *       max_right = 1
     *
     *
     *  exp 2)
     *
     *    s =  "( * )"
     *
     *    ( * )
     *    x
     *    left = 1
     *
     *
     *    ( * )
     *      x
     *      min_right = 0
     *      max_right = 1
     *      left = 1
     *
     *    ( * )
     *        x
     *        min_right = 1
     *        max_right = 2
     *        left = 1
     *
     *
     *  exp 3)  s = "(*))"
     *
     *    ( * ) )
     *    x
     *    left = 1
     *
     *
     *    ( * ) )
     *      x
     *      left = 1
     *      min_right = 0
     *      max_right = 1
     *
     *    ( * ) )
     *        x
     *        left = 1
     *        min_right = 0
     *        max_right = 2
     *
     *   ( * ) )
     *         x
     *         left =1
     *         min_right = 0
     *         max_right = 3
     *
     *
     *
     */
    public boolean checkValidString(String s) {
        // edge
        if(s == null || s.length() == 0){
            return true;
        }
        if(s.length() == 1){
            if(!s.equals("*")){
                return false;
            }
        }

        int left_paren = 0;
        int min_right_paren = 0;
        int max_right_paren = 0;

        for(String x: s.split("")){

            System.out.println(">>> x = " + x + ", left_paren = " + left_paren
                    + ", min_right_paren = " + min_right_paren
                    + ", max_right_paren = " + max_right_paren);

            if(x.equals("(")){
                left_paren += 1;
            }else{
                min_right_paren += 1;
                max_right_paren += 1;
            }

            // `adjust` the `< 0` min_right_paren
            if(min_right_paren < 0){
                min_right_paren = 0;
            }

            // NOTE !!! validate if `still possible to form a valid parenthesis`
            if((max_right_paren < left_paren  && left_paren < min_right_paren)|| max_right_paren < 0){
                return false;
            }
        }

        return true;
    }

    // LC 846
    // 10.26 - 10.36 am
    /**
     *  IDEA 1) SORTING + MAP + PQ ??
     *
     *   -> sort, and collect the `non distinect val` to small PQ
     *   -> loop and collect `group size count` of element from PQ,
     *      update hashmap cnt on the same time.
     *      if any violated, return false directly
     *
     *    -> keep above
     *    -> return ture
     *
     *
     */
    public boolean isNStraightHand(int[] hand, int groupSize) {

        int len = hand.length;

        // edge
        if(hand == null || len == 0){
            return groupSize == 0; // ??
        }
        if(len % groupSize != 0){
            return false;
        }

        // map : {val : cnt}
        Map<Integer, Integer> cnt_map = new HashMap<>();

        // small PQ
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        for(int x: hand){

            cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);

            if(!pq.contains(x)){
                pq.add(x);
            }
        }

        // try to form the `sub array with group-size`
        //int cnt = hand.length;

        //System.out.println(">>> cnt_map = " + cnt_map + ", PQ = " + pq);

        // ???
        while(!cnt_map.isEmpty()){

            int val = pq.peek();
            for(int j = 0; j < groupSize; j++){

//                System.out.println(">>> j = " + j + ", val = " + val
//                        + ", cnt_map = " + cnt_map + ", PQ = " + pq);

                if(!cnt_map.containsKey(val)){
                    return false;
                }

                cnt_map.put(val, cnt_map.get(val) - 1);

                // if `cnt = 0`,  pop from PQ
                if(cnt_map.get(val) == 0){
                    pq.poll();

                    cnt_map.remove(val);
                }

                val += 1;
            }
        }

        return true;
    }

    // LC 226
    // 10.50 - 11.00 am
    /**
     *
     * -> Given the root of a binary tree,
     *   invert the tree, and return its root.
     */
    // IDEA: DFS
    public TreeNode invertTree(TreeNode root) {
        // edge
        if(root == null || (root.left == null && root.right == null)){
            return root;
        }
        if(root.left == null || root.right == null){
            TreeNode _left = root.left;
            root.left = root.right;
            root.right = _left;
        }

        // call helper func
//        root.left = inverseHelper(root.right);
//        root.right = inverseHelper(root.left);
        TreeNode reversedNode = inverseHelper(root); // ???

        return reversedNode;
    }

    // ???
    public TreeNode inverseHelper(TreeNode root){
        // edge
        if(root == null){
            return root;
        }

//        TreeNode _left = root.left;
//        TreeNode _right = root.right;

        // ???
        TreeNode _right = inverseHelper(root.right);
        TreeNode _left = inverseHelper(root.left);

        root.left = _right; //???
        root.right = _left; // ???

        return root;
    }

    ///////////////////

//    public TreeNode invertTree(TreeNode root) {
//        // edge
//        if (root == null || (root.left == null && root.right == null)) {
//            return root;
//        }
//        if (root.left == null || root.right == null) {
//            TreeNode _left = root.left;
//            root.left = root.right;
//            root.right = _left;
//        }
//
//        // call helper func
//        //        root.left = inverseHelper(root.right);
//        //        root.right = inverseHelper(root.left);
//        TreeNode reversedNode = inverseHelper(root); // ???
//
//        return reversedNode;
//    }
//
//    // ???
//    public TreeNode inverseHelper(TreeNode root) {
//        // edge
//        if (root == null) {
//            return root;
//        }
//
//        //        TreeNode _left = root.left;
//        //        TreeNode _right = root.right;
//
//        // ???
//        TreeNode _right = inverseHelper(root.right);
//        TreeNode _left = inverseHelper(root.left);
//
//        root.left = _right; //???
//        root.right = _left; // ???
//
//        return root;
//    }


    // LC 2415
    // 11.31 - 11.41 am
    /**
     *  -> Given the root of a perfect binary tree,
     *    reverse the node values at each  `odd` level of the tree.
     *
     *
     *  IDEA 1) DFS + `layer controller` that only implement swap on `odd` layer
     *
     */
//    public TreeNode reverseOddLevels(TreeNode root) {
//
//        // edge
//        if (root == null || (root.left == null && root.right == null)) {
//            return root;
//        }
//
//        // call helper func
//        int layer = 0;
//        TreeNode reversedNode = inverseHelper_(root, layer);
//        return reversedNode;
//    }
//
//    public TreeNode inverseHelper_(TreeNode root, int layer) {
//        // edge
//        if (root == null) {
//            return root;
//        }
//
//        layer += 1;
//
//        TreeNode _right = inverseHelper_(root.right, layer + 1);
//        TreeNode _left = inverseHelper_(root.left, layer + 1);
//
//        // ????
//        if (layer % 2 == 1) {
//            root.left = _right;
//            root.right = _left;
//
//        }
//
//        return root;
//    }


    public TreeNode reverseOddLevels(TreeNode root) {

        // edge
        if (root == null || (root.left == null && root.right == null)) {
            return root;
        }

        // call helper func
        int layer = 0;
        TreeNode reversedNode = inverseHelper_(root, layer);
        return reversedNode;
    }

    public TreeNode inverseHelper_(TreeNode root, int layer) {
        // edge
        if (root == null) {
            return root;
        }

        layer += 1;

        TreeNode _right = inverseHelper_(root.right, layer + 1);
        TreeNode _left = inverseHelper_(root.left, layer + 1);

        // ????
        if (layer % 2 == 1) {
            root.left = _right;
            root.right = _left;

        }

        return root;
    }

    // LC 104
    // 12.08 - 12.18 pm
    /**
     *  * Given the root of a binary tree, return its maximum depth.
     *  *
     *  * A binary tree's maximum depth is the number
     *   of nodes along the longest path from the root
     *   node down to the farthest leaf node.
     *  *
     *
     *
     *   -> depth: `dist from root to farthest leaf node`
     */
   // int max_depth = 0;

    public int maxDepth(TreeNode root) {
        // edge
        if (root == null) {
            return 0;
        }
        if(root.left == null && root.right == null){
            return 1;
        }

        // call helper func
        depthHelper(root);

        //return max_depth + 1;
        return depthHelper(root);
    }

    public int depthHelper(TreeNode root) {
        // edge
        if (root == null) {
            return 0;
        }

        //        TreeNode _left =  depthHelper(root.left);
        //        TreeNode _right =  depthHelper(root.right);

        // ????
        int left_depth = depthHelper(root.left) + 1;
        int right_depth = depthHelper(root.right) + 1;

        // get bigger one
//        int depth = Math.max(left_depth, right_depth);

        // ????
//        max_depth = Math.max(
//                max_depth,
//                depth);
//
//        //return root;

        return Math.max(
                left_depth,
                right_depth);
    }


    // LC 543
    // 10.31 - 10.41 am
    /**
     *  -> return the length of the diameter of the tree.
     *
     *   (The length of a path between two nodes is represented
     *  by the number of edges between them.)
     *
     *
     *            1
     *          / \
     *         2   3
     *        / \
     *       4   5
     *
     * Return 3, which is the length of the
     * path [4,2,1,3] or [5,2,1,3].
     *
     *
     *
     *   IDEA 1) DFS  (post transverse )???
     *
     *
     *
     */
    int diameter = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        // edge
        if(root == null){
            return 0; // ??
        }
        if(root.left == null && root.right == null){
            return 0;
        }

        // dfs
        diaHelper(root);

        return diameter;
    }

    public int diaHelper(TreeNode root){
        if(root == null){
            //return root; // ??
            return 0;
        }
        // ???
//        if(root.left == null && root.right == null){
//            //return root; // ??
//            return 0;
//        }

        int _left_depth = diaHelper(root.left);
        int _right_depth = diaHelper(root.right);


        diameter = Math.max(
                diameter,
                _left_depth + _right_depth
        );

        //return diameter; // ???
        //return _left_depth + _right_depth + 1; // ???
        return Math.max(_left_depth, _right_depth) + 1;
    }



    // LC 1522
    // 11.04 - 11.14 am
    /**
     * -> Given a root of an N-ary tree,
     *    you need to compute the length of the diameter of the tree.
     *
     *
     *
     *   IDEA 1) DFS
     *
     *
     *
     */

    class Node {
        public int val;
        public List<Node> children;


        public Node() {
            children = new ArrayList<Node>();
        }

        public Node(int _val) {
            val = _val;
            children = new ArrayList<Node>();
        }

        public Node(int _val,ArrayList<Node> _children) {
            val = _val;
            children = _children;
        }
    };

    int max_diameter = 0;

    public int diameter(Node root) {

        // edge
        if(root == null){
            return 0; // ??
        }
        if(root.children == null || root.children.size() == 0){
            return 0;
        }

        // dfs
        nDiaHelper(root);

        return max_diameter;
    }

    public int nDiaHelper(Node root){
        // edge
        if(root == null){
            return 0; // ??
        }

        List<Integer> cur_depth_list = new ArrayList<>();
        // ??
        for(Node x: root.children){
            cur_depth_list.add(nDiaHelper(x));
        }

        // sort, and get the `top 2 max` val as the cur `left, right depth` ???
        Collections.sort(cur_depth_list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        int _left_depth = 0;
        int _right_depth = 0;

        // ???
        if(cur_depth_list.size() < 2){
            if(cur_depth_list.size() == 1){
                _left_depth = cur_depth_list.get(0);
            }
        }

        // get max from `all possible pairs` from cur_depth_list
        max_diameter = Math.max(max_diameter, _left_depth + _right_depth); // ???

        // NOTE !!! (for upper layer, we need to return val as below)
        return Math.max(_left_depth, _right_depth) + 1;
    }

    // LC 110
    // 11.34 - 11.44 am
    /**
     *
     *  -> Given a binary tree, determine if it is height-balanced.
     *
     *
     *
     * A height-balanced binary tree is a binary tree in which the
     *
     * `depth`  of the two subtrees of every
     * node never differs by more than `one.`
     *
     *
     *  IDEA 1) DFS
     *
     */
    /** NOTE !!!
     *
     *  depth of tree is the distance from `root` to the node
     */
    public boolean isBalanced(TreeNode root) {

        // edge
        if(root == null){
            return true;
        }
        if(root.left == null && root.right == null){
            return true;
        }

        // dfs
        return isBalanceHelper(root);
    }

    public boolean isBalanceHelper(TreeNode root){

        int _left_depth = getDepthHelper(root.left);
        int _right_depth = getDepthHelper(root.right);

        if (Math.abs(_left_depth - _right_depth) > 1){
            return false;
        }

        // ???
        return  isBalanceHelper(root.left)
                && isBalanceHelper(root.right);
    }

    public int getDepthHelper(TreeNode root){
        // edge
        if(root == null){
            return 0;
        }

        int _left_depth = getDepthHelper(root.left);
        int _right_depth = getDepthHelper(root.right);

        // ???
        return Math.max(_left_depth, _right_depth) + 1;
    }


    // LC 235
    // 9.17 - 9.33 am
    /**
     *
     * -> Given a binary search tree (BST),
     *   find the lowest common ancestor (LCA) node of two given nodes in the BST.
     *
     *
     *  What's LCA ?
     *
     *  -> “The lowest common ancestor is defined between two nodes p and q as the
     *     lowest node in T that has both p and q as descendants
     *    (where we allow a node to be a descendant of itself).”
     *
     *
     *
     *   // IDEA 1) DFS
     */
//    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
//        // edge
//        if(root == null){
//            return null;
//        }
//        if(root.left == null && root.right == null){
//            return null;
//        }
//        // ??
//        if(p == q){
//            return p;
//        }
////        if(p == null || q == null){
////            return null; // ???
////        }
//
//        // dfs
//        return lcaHelper(root, p, q); // ???
//    }
//
//    public TreeNode lcaHelper(TreeNode root, TreeNode p, TreeNode q){
//        // edge
//        if(root == null){
//            return null;
//        }
//        if(root.left == null && root.right == null){
//            return null;
//        }
//        if(p == null && q == null){
//            return root;
//        }
//        if(p == null || q == null){
//            return root; // ???
//        }
//
//        // BST property
//
//        // case 1) root val > p, q
//        if(root.val > p.val && root.val > q.val){
//            return lcaHelper(root.left, p, q);
//        }
//        // case 2) root val < p, q
//        if(root.val < p.val && root.val < q.val){
//            return lcaHelper(root.right, p, q);
//        }
//        // case 3) q < roo val < p or p < roo val < q
////        else{
////            return root;
////        }
//        return root; // ??
//
//        //return null;
//    }



    // LC 1644
    // 9.49 - 9.59 am
    /**
     *
     * -> Given the root of a binary tree, return the lowest common ancestor (LCA)
     * of two given nodes, p and q.
     * If either node p or q does not exist in the tree,
     * return `null` !!!
     * All values of the nodes in the tree are unique.
     *
     *
     *  IDEA 1) DFS + LC 235 + BST property
     *
     *  -> NOTE !!!  p or q could NOT exist, if this is the case, return `null` as LCA
     *
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

        // LC 235

        /** handle cases if p or q DOES NOT exist */
        if(p == null || q == null){
            return null; // ???
        }

        // if root equals p or q, return root as LCA
        if (root.equals(p) || root.equals(q)) {
            return root;
        }

        /** NOTE !!! BST property : right > root > left */
        // search on right sub tree
        if (p.val > root.val && q.val > root.val){
            return this.lowestCommonAncestor(root.right, p, q);
        }
        // search on left sub tree
        if (p.val < root.val && q.val < root.val){
            return this.lowestCommonAncestor(root.left, p, q);
        }

        /**
         * NOTE !!!
         *
         *  if can reach below
         *  -> p, q are in different side (sub tree)
         *  -> then return root as LCA directly
         */
        return root;
    }


    // LC 1650
    // 10.27 - 10.37 am

    class Node_3 {
        public int val;
        public Node_3 left;
        public Node_3 right;
        public Node_3 parent;
    };

    /**
     *  IDEA 1) HASH SET
     *
     */
    Set<Node_3> set = new HashSet<>();
    public Node_3 lowestCommonAncestor(Node_3 p, Node_3 q) {
        // edge
        if(p == null && q == null){
            return null;
        }
        if(p == null || q == null){
            return null; // ???
        }
        if(p.equals(q)){
            return p; // ???
        }


        // dfs
        Node_3 lca = lcaHelper3(p, q);

        return lca; // ???
    }

    public Node_3 lcaHelper3(Node_3 p, Node_3 q){
        // edge
        if(p == null && q == null){
            return null;
        }
        if(p == null || q == null){
            return null; // ???
        }
        if(p.equals(q)){
            return p; // ???
        }


        // ???
        if(set.contains(p) && set.contains(q)){
            return p.parent; // ??
        }

        // ???
        set.add(p);
        set.add(q);

        // recursive call
        lcaHelper3(p.left, q.left);
        lcaHelper3(p.right, q.right);


        //if(!set.contains(p))

        return null;
    }



    // LC 1257
    // 3.02 - 3.12 pm
    /**
     * -> Given two regions `region1, region2, `
     *    find out the `smallest` region that contains `both` of them.
     *
     *
     *   You are given some lists of regions where the `first` region
     *   of each list includes all other regions in that list.
     *
     *
     *   IDEA 1) HASHMAP ???
     *
     *   IDEA 2) BRUTE FORCE ???
     *
     */

    // IDEA 1) HASHMAP
    public String findSmallestRegion_1(List<List<String>> regions, String region1, String region2) {

        // edge
        if(regions == null || regions.isEmpty()){
            return null;
        }

        // build map
        Map<String, List<String>> parentMap = new HashMap<>();
        for(List<String> r: regions){
            List<String> sub = r.subList(1, r.size() - 1);
            parentMap.put(r.get(0), sub); // ???
        }

        // edge: region1 == region2
        if(region1 == region2){
            for(String k: parentMap.keySet()){
                if(parentMap.get(k).contains(region1)){
                    return k;
                }
            }
        }

        String min_parent = null;

        // if region1 != region2
        //List<String> parent_list_1 = new ArrayList<>();
        for(String k: parentMap.keySet()){
            if(parentMap.get(k).contains(region1)){
                if(parentMap.get(k).contains(region2)){
                    return k;
                }
            }
        }

        return min_parent;
    }


    // LC 3319
    public int kthLargestPerfectSubtree(TreeNode root, int k) {

        return 0;
    }

}
