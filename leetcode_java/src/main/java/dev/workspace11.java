package dev;

import LeetCodeJava.BinarySearchTree.LowestCommonAncestorOfABinaryTree3;
import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.Tree.DiameterOfNAryTree;
import com.sun.org.apache.bcel.internal.generic.PUSH;

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
    // 10.04 - 10.14 am
    /**
     *
     * -> Given the root of a binary tree,
     *   invert the tree, and return its root.
     */
//    public TreeNode invertTree(TreeNode root) {
//        // edge
//        if(root == null || (root.left == null && root.right == null)){
//            return root;
//        }
////        if(root.left == null || root.right == null){
////            TreeNode _left = root.left;
////            root.left = root.right;
////            root.right = _left;
////        }
//
//        // dfs call
//        return invertHelper2(root);
//    }
//
//    private TreeNode invertHelper2(TreeNode root){
//        // edge
//        if(root == null){
//            return root;
//        }
//
//        TreeNode _left = invertHelper2(root.left);
//        TreeNode _right = invertHelper2(root.right);
//
//        // ???
//        root.left = _right; //invertHelper2(root.right); //_right;
//        root.right = _left;
//
//        return root;
//    }

//    // IDEA: DFS
//    public TreeNode invertTree(TreeNode root) {
//        // edge
//        if(root == null || (root.left == null && root.right == null)){
//            return root;
//        }
//        if(root.left == null || root.right == null){
//            TreeNode _left = root.left;
//            root.left = root.right;
//            root.right = _left;
//        }
//
//        // call helper func
////        root.left = inverseHelper(root.right);
////        root.right = inverseHelper(root.left);
//        TreeNode reversedNode = inverseHelper(root); // ???
//
//        return reversedNode;
//    }
//
//    // ???
//    public TreeNode inverseHelper(TreeNode root){
//        // edge
//        if(root == null){
//            return root;
//        }
//
////        TreeNode _left = root.left;
////        TreeNode _right = root.right;
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
    /**
     *  IDEA 1) DFS + LCA I
     *
     *
     */

    // 12.19 - 12.29 pm
    List<Integer> node_list = new ArrayList<>();
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

        // edge
        if ((root == null) || (root.equals(p) || root.equals(q))){
            return root;
        }


        nodeCollector(root);
        // check if p, and q are BOTH exist in root
        if(!collected.contains(p.val) || !collected.contains(q.val)){
            return null;
        }

        // LC 235
        return LCA2Helper(root, p, q);
    }

    public TreeNode LCA2Helper(TreeNode root, TreeNode p, TreeNode q){

        // NOTE !!! below edge condition
        // -> so, we will return right away if `root equals p` or `root equals q`
        if ((root == null) || (root.equals(p) || root.equals(q))){
            return root;
        }

        TreeNode _left = LCA2Helper(root.left, p, q);
        TreeNode _right = LCA2Helper(root.right, p, q);

        // NOTE !!! below
        if(_left != null && _right != null){
            return root; // ??
        }

        return _left == null ? _right : _left;
    }


    public void nodeCollector(TreeNode root){
        if(root == null){
            return;
        }
        node_list.add(root.val);

        nodeCollector(root.left);
        nodeCollector(root.right);
    }








//    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
//
//        // LC 235
//
//        /** handle cases if p or q DOES NOT exist */
//        if(p == null || q == null){
//            return null; // ???
//        }
//
//        // if root equals p or q, return root as LCA
//        if (root.equals(p) || root.equals(q)) {
//            return root;
//        }
//
//        /** NOTE !!! BST property : right > root > left */
//        // search on right sub tree
//        if (p.val > root.val && q.val > root.val){
//            return this.lowestCommonAncestor(root.right, p, q);
//        }
//        // search on left sub tree
//        if (p.val < root.val && q.val < root.val){
//            return this.lowestCommonAncestor(root.left, p, q);
//        }
//
//        /**
//         * NOTE !!!
//         *
//         *  if can reach below
//         *  -> p, q are in different side (sub tree)
//         *  -> then return root as LCA directly
//         */
//        return root;
//    }


    // LC 1650
    // 11.43 - 11.53 am
    /**
     *
     *
     * Given two nodes of a binary tree p and q,
     * return their lowest common ancestor (LCA).
     */
    class Node_3 {
        public int val;
        public Node_3 left;
        public Node_3 right;
        public Node_3 parent;
    };

    // IDEA 1) HASHSET + `2 POINTER`
    // e.g.
    //      setup a pointer traverse and collect all p's parent
    //      the other pointer traverse and collect all q's parent
    public Node_3 lowestCommonAncestor(Node_3 p, Node_3 q) {

        if(p == null && q == null){
            return null;
        }
        if(p == null || q == null){
            return null; // ???
        }

        HashSet<Node_3> parent_p = new HashSet<>();
        while(p != null){
            parent_p.add(p);
            p = p.parent;
        }

        while(q != null){
            if (parent_p.contains(q)){
                return q;
            }
            q = q.parent;
        }

        return null;
    }




//    public Node_3 lowestCommonAncestor(Node_3 root, Node_3 p, Node_3 q) {
//
//        // edge
//        if(p == null && q == null){
//            return null;
//        }
//        if(p == null || q == null){
//            return null; // ???
//        }
//        if(p.equals(q)){
//            return p; // ???
//        }
//
//        // dfs call
//        return ancestorHelpr(root, p, q);
//    }
//
//    public Node_3 ancestorHelpr(Node_3 root, Node_3 p, Node_3 q){
//
//        // edge
//        if(p == null && q == null){
//            return null;
//        }
//        if(p == null || q == null){
//            if(q == null){
//                return p.parent; // ???
//            }
//            return q.parent; // ???
//        }
//
//        Node_3 _left = ancestorHelpr(root.left, p, q);
//        Node_3 _right = ancestorHelpr(root.right, p, q);
//
//        if(_left == null && _right == null){
//            return root;
//        }
//
//        return _left != null ? _left.parent : _right.parent;
//    }
//
//


    /**
     *  IDEA 1) HASH SET
     *
     */
//    Set<Node_3> set = new HashSet<>();
//    public Node_3 lowestCommonAncestor(Node_3 p, Node_3 q) {
//        // edge
//        if(p == null && q == null){
//            return null;
//        }
//        if(p == null || q == null){
//            return null; // ???
//        }
//        if(p.equals(q)){
//            return p; // ???
//        }
//
//
//        // dfs
//        Node_3 lca = lcaHelper3(p, q);
//
//        return lca; // ???
//    }
//
//    public Node_3 lcaHelper3(Node_3 p, Node_3 q){
//        // edge
//        if(p == null && q == null){
//            return null;
//        }
//        if(p == null || q == null){
//            return null; // ???
//        }
//        if(p.equals(q)){
//            return p; // ???
//        }
//
//
//        // ???
//        if(set.contains(p) && set.contains(q)){
//            return p.parent; // ??
//        }
//
//        // ???
//        set.add(p);
//        set.add(q);
//
//        // recursive call
//        lcaHelper3(p.left, q.left);
//        lcaHelper3(p.right, q.right);
//
//
//        //if(!set.contains(p))
//
//        return null;
//    }



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

    // 10.36 - 10.46 am
    /**
     *  IDEA 1) HASHMAP  : { region : parent }
     *
     *
     *   { brazil: s-usa,  Quebec: canada, Ontario: canada,
     *    Boston: US, NY: US, Canada: n-usa, us : n-usa,
     *    s-us: earth, n-us : earth}
     *
     *
     */
    public String findSmallestRegion_1(List<List<String>> regions, String region1, String region2) {

        // edge
        if(regions == null || regions.isEmpty()){
            return null;
        }

        // hashmap : { region : parent }
        Map<String, String> map = new HashMap<>();
        for(List<String> r: regions){
            String parent = r.get(0);
            for(int i = 1; i < parent.length(); i++){
                String child = r.get(i);
                map.put(child, parent); // ???
            }
        }

        String min_parent = null;

        // check the `min` ancestor
        // use Queue (FIFO), so we can get `min` ancestor
        Queue<String> region_1_parents = new LinkedList<>();

        // for region 1

        // Note !!! below trick
        while(region1 != null){
            region_1_parents.add(region1);
            region1 = map.get(region1);
        }

//        for(String k: map.keySet()){
//            if(k.equals(region1)){
//                String _parent = map.get(region1);
////                if(map.get(_parent).contains(region2)){
////                    min_parent = _parent;
////                }
//                region_1_parents.add(_parent);
//
//                region1 = _parent;
//            }
//        }

        // for region 2
        while(!region_1_parents.isEmpty()){
            String cur = region_1_parents.poll();
            if(map.get(cur).equals(region2)){
                return cur;
            }
        }


        return min_parent;
    }




    // IDEA 1) HASHMAP
//    public String findSmallestRegion_1(List<List<String>> regions, String region1, String region2) {
//
//        // edge
//        if(regions == null || regions.isEmpty()){
//            return null;
//        }
//
//        // build map
//        Map<String, List<String>> parentMap = new HashMap<>();
//        for(List<String> r: regions){
//            List<String> sub = r.subList(1, r.size() - 1);
//            parentMap.put(r.get(0), sub); // ???
//        }
//
//        // edge: region1 == region2
//        if(region1 == region2){
//            for(String k: parentMap.keySet()){
//                if(parentMap.get(k).contains(region1)){
//                    return k;
//                }
//            }
//        }
//
//        String min_parent = null;
//
//        // if region1 != region2
//        //List<String> parent_list_1 = new ArrayList<>();
//        for(String k: parentMap.keySet()){
//            if(parentMap.get(k).contains(region1)){
//                if(parentMap.get(k).contains(region2)){
//                    return k;
//                }
//            }
//        }
//
//        return min_parent;
//    }

  // LC 3319
  // 3.52 - 4.10 pm
  /**
   *
   * -> Return an` integer` denoting the `size` of the
   *    `kth` `largest` perfect binary subtree,
   *    or -1 if it doesn't exist.
   *
   *
   *   (A perfect binary tree is a tree where all leaves
   *   are on the same level, and every parent has two children)
   *
   *
   *
   *   IDEA 1) DFS
   *
   */

  // 12.38 - 12.48 pm
  // IDEA: custom class
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

  public class PerfectNode{
      // attr
      TreeNode node;
      boolean isPerfect;
      int level;

      // constructor
      public PerfectNode(TreeNode node, boolean isPerfect, int level){
          this.node = node;
          this.isPerfect = isPerfect;
          this.level = level;
      }
  }

  List<Integer> collected = new ArrayList<>();
  public int kthLargestPerfectSubtree(TreeNode root, int k) {
      // edge
      if(root == null){
          return -1;
      }
      if(root.left == null && root.right == null){
          return 1; // ???
      }

      // init
      PerfectNode node = new PerfectNode(root, true, 0);

      // call helper func
      perfectHelper(node);

      // sort on len (max -> small)
      Collections.sort(collected, new Comparator<Integer>() {
          @Override
          public int compare(Integer o1, Integer o2) {
              int diff = o2 - o1;
              return diff;
          }
      });

      return collected.get(k); // ??
  }

  // DFS, `post order`
  // e.g. : left -> right -> root
  // since we NEED to know if `left and right` are perfect first,
  // then we know if `root` is perfect
  public PerfectNode perfectHelper(PerfectNode node){
      // edge
      if(node == null){
          return new PerfectNode(null, true, 0); // ???
      }

      boolean isPerfect = false;
      int lavel = node.level;

      if(node.node.left == null && node.node.right == null){
          isPerfect = true;
      }

      // ??
      //PerfectNode _left2 = new PerfectNode(node.node.left);

      PerfectNode _left = new PerfectNode(node.node.left, isPerfect, lavel);
      PerfectNode _right = new PerfectNode(node.node.right, isPerfect, lavel);

      // ???
      if(_left.isPerfect && _right.isPerfect && _left.level == _right.level){
          collected.add(node.node.val);
      }


      return node;
  }






//  List<List<Integer>> collected = new ArrayList<>();
//  public int kthLargestPerfectSubtree(TreeNode root, int k) {
//
//      // edge
//      if(root == null){
//          return -1;
//      }
//      if(root.left == null && root.right == null){
//         //if(roo)
//          return root.val; // ???
//      }
//
//
//      // dfs
//
//      // edge
//      if(collected == null || collected.isEmpty() || collected.size() < k){
//          return -1;
//      }
//
//     // for(int i = 0)
//     // sort on len (max -> small)
//     Collections.sort(collected, new Comparator<List<Integer>>() {
//         @Override
//         public int compare(List<Integer> o1, List<Integer> o2) {
//             int diff = o2.size() - o1.size();
//             return diff;
//         }
//     });
//
//      return collected.get(k).size(); // ??
//    }
//
//    public TreeNode perfectSubTreeHelper(TreeNode root){
//      // edge
//        if(root == null){
//           // return -1;
//            return null;
//        }
//        if(root.left == null && root.right == null){
//            //if(roo)
//           // return root.val; // ???
//           if(!collected.contains(root)){
//               List<Integer> cur = new ArrayList<>();
//               cur.add(root.val);
//              collected.add(cur); // ???
//           }
//        }
//
//        TreeNode _left = perfectSubTreeHelper(root.left);
//        TreeNode _right = perfectSubTreeHelper(root.right);
//
//
//
//        return null;
//    }

    // LC 496
    // 10.21 - 10.31 am
    /**
     *  -> Return an array ans of length `nums1`.
     *     length such that ans[i] is the next `greater` element as described above.
     *
     *
     *  You are given two distinct 0-indexed integer arrays nums1 and nums2,
     *  where `nums1` is a `subset` of `nums2`.
     *
     *  nums1[i] == nums2[j] and determine the next greater
     *  element of nums2[j] in nums2. If there is no next greater element,
     *  then the answer for this query is -1.
     *
     *
     *
     *  ->
     *
     *
     *
     */
    // 11.24 - 11.34 am
    // IDEA: STACK
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int[] res = new int[nums1.length];
        Arrays.fill(res, -1);

        // use map to record `next bigger number` in nums2 ??
        // map : {val : next_big_val}
        Map<Integer, Integer> map = new HashMap<>();

        // stack: FILO
        // mono increase stack (small -> big)
        Stack<Integer> st = new Stack<>();
        for(Integer x: nums2){

            while(!st.isEmpty() && st.peek() < x){
                int prev = st.pop();
                map.put(prev, x);
            }

            st.add(x);
        }

        // update res
        for(int i = 0; i < nums1.length; i++){
            int val = nums1[i];
            if(map.containsKey(val)){
                res[i] = map.get(val);
            }
        }

        return res;
    }





    // IDEA: HASHMAP + BRUTE FORCE
//    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
//
//        int[] res = new int[nums1.length];
//        Arrays.fill(res, -1);
//
//        // use map to record `next bigger number` in nums2 ??
//        // map : {val : next_big_val}
//        Map<Integer, Integer> map = new HashMap<>();
//
//        // update the map by setting val, and nex_big_val in nums2
//        for(int i = 0; i < nums2.length; i++){
//            for(int j = i+1; j < nums2.length; j++){
//                if(nums2[j] > nums2[i]){
//                    map.put(nums2[i], nums2[j]);
//                    break;
//                }
//            }
//        }
//
//        System.out.println(">>> map = " + map);
//
//        // q : mono increase queue
//        Queue<Integer> q = new LinkedList<>();
//        // loop over nums1
//        for(int i = 0; i < nums1.length; i++){
//            int val = nums1[i];
//            if(map.containsKey(val)){
//                res[i] = map.get(val);
//            }
//        }
//
//
//        return res;
//    }


//    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
//
//        int[] res = new int[nums1.length];
//        Arrays.fill(res, -1);
//
//
//        // map : `collect next big element
//        // { val : next_big_element_idx }
//        Map<Integer, Integer> map = new HashMap<>();
//
//        // mono stack (FILO)
//        // `increase mono stack`
//        Stack<Integer> st = new Stack<>();
//
//        for(int i = 0; i < nums2.length; i++){
//
//            int val = nums2[i];
//
//            if(st.isEmpty()){
//                st.add(val);
//            }else{
//                if(st.peek() > val){
//                   // st.add(val);
//                   map.put(val, i);
//                }else{
//                   st.pop();
//                   st.add(val);
//                }
//            }
//        }
//
//
//        for(int i = 0; i < nums1.length-1; i++){
//            int val_1 = nums1[i];
//            if(map.containsKey(val_1)){
//                res[i] = map.get(val_1);
//            }
//        }
//
//        return res;
//    }




//    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
//
//        int[] res = new int[nums1.length];
//        Arrays.fill(res, -1);
//
//        // edge
//        if (nums1 == null || nums1.length == 0){
//            return new int[]{};
//        }
//        if(nums2 == null){
//            return res;
//        }
//
//        Stack<Integer> st = new Stack<>();
//        for(int x: nums2){
//            st.add(x);
//        }
//
//        // mono stack (mono increasing stack, FILO)
//        for(int i = 0; i < nums1.length; i++){
//
//            int val_1 = nums1[i];
//
//            // copy stack as current stack
//            Stack<Integer> st_ = st; // ???
//            boolean start = false;
//
//            // NOTE !!! find corresponding idx (with same val) in nums2
//            while(!st_.isEmpty()){
//                int val_2 = st.pop();
//                if(val_2 == val_1){
//                    start = true;
//                }
//                if(val_2 > val_1 && start){
//                    res[i] = val_2;
//                    break;
//                }
//            }
//        }
//
//
//        return res;
//    }

    // LC 503
    // 11.05 - 11.15 am
    /**
     *  IDEA 1) mono stack + ` % len` handling
     *
     *
     *
     *
     */
    // 11.54 - 12.04 pm
    /**
     *  IDEA 1) STACK + `idx % len`  op
     *
     */
    public int[] nextGreaterElements(int[] nums) {

        // edge
        if (nums == null || nums.length == 0) {
            return new int[0]; // Return empty array instead of null
        }

        int[] res = new int[nums.length];
        Arrays.fill(res, -1);

        // map: { idx : next_bigger_val_idx }
        Map<Integer, Integer> map = new HashMap<>();

        // mono stack
        Stack<Integer> st = new Stack<>();
        // double size of nums

        int _len = nums.length;

        for(int i = 0; i < _len * 2; i++){

            // adjust `idx`
            int adjusted_idx = i % _len;
            int val = nums[adjusted_idx];

            while(!st.isEmpty() && nums[st.peek()] < val){
                int prev_idx = st.pop();

                map.put(prev_idx, adjusted_idx);
            }

            // NOTE !!! ONLY push the `first n` element
            if(i < _len){
                st.add(adjusted_idx);
            }

        }

        for(int i = 0; i < nums.length; i++){
            if(map.containsKey(i)){
                res[i] = nums[map.get(i)];
            }
        }

       return res;

    }




//    public int[] nextGreaterElements(int[] nums) {
//
//        int[] res = new int[nums.length];
//        Arrays.fill(res, -1);
//
//        // edge
//        if(nums == null || nums.length == 0){
//            return null; // ??
//        }
//
//        // map : {val : idx}
//        Map<Integer, Integer> map = new HashMap<>();
//
//        // stack : mono increase stack (idx)
//        Stack<Integer> st = new Stack<>();
//
//        for(int i = 0; i < nums.length; i++){
//            map.put(nums[i], i);
//        }
//
////        for(int i = 0; i < nums.length * 2; i++){
////            int val = nums[i];
////            while(!st.isEmpty() && st.peek() < val){
////                int tmp = st.pop();
////            }
////
////            st.push(val);
////        }
//
//        for(int i = 0; i < nums.length * 2; i++){
//            int val = nums[ i % nums.length ];
//            while(!st.isEmpty() && nums[st.peek()] < val){
//                //int tmp = st.pop();
//                res[i % nums.length] = val;
//            }
//
//            if(i < nums.length){
//                st.push(val);
//            }
//
//        }
//
//
//        return res;
//    }


    // LC 98
    // 10.16 - 10.26 am
    /**
     *  NOTE !!!
     *
     *   BST :
     *
     *    left < root < right
     *
     *    and same property for all its sub tree
     *
     *
     *    // IDEA 1) DFS + BST
     *
     */
    public boolean isValidBST_(TreeNode root) {
        // edge
        if(root == null){
            return true;
        }
        if(root.left == null && root.right == null){
            return true;
        }

        // ???
        // dfs call
        return validBSTHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean validBSTHelper(TreeNode root, long smallestVal, long biggestVal){
        // edge
        if(root == null){
            return true;
        }

        // validate
        if(root.val >= biggestVal || root.val <= smallestVal){
            return false;
        }


        //return true;
        return validBSTHelper(root.left, smallestVal, root.val) &&
                validBSTHelper(root.right, root.val, biggestVal);
    }

    // LC 501
    // 10.59 - 11.10 am
    /**
     *  ->  return all the mode(s)
     *  (i.e., the most frequently occurred element) in it.
     *
     *
     *   IDEA 1) DFS
     *
     */
    Map<Integer, Integer> node_cnt = new HashMap<>();
    public int[] findMode(TreeNode root) {
        // edge
        if(root == null){
            return null;
        }
        if(root.left == null && root.right == null){
            return new int[]{root.val};
        }

        // dfs call
        nodeHelper(root);

        int max_val = 0;
        for(Integer k: node_cnt.keySet()){
            if(node_cnt.get(k) > max_val){
                max_val = node_cnt.get(k);
            }
        }

        List<Integer> cache = new ArrayList<>();
        for(Integer k: node_cnt.keySet()){
            if(node_cnt.get(k) == max_val){
                cache.add(k);
            }
        }

        //return res;
        int[] res2 = new int[cache.size()];
        for(int i = 0; i < cache.size(); i++){
            res2[i] = cache.get(i);
        }

        return res2;
    }

    public TreeNode nodeHelper(TreeNode root){
        if(root == null){
            return null;
        }

        node_cnt.put(root.val, node_cnt.getOrDefault(root.val, 0) + 1);

        nodeHelper(root.left);
        nodeHelper(root.right);

        return root;
    }

    // LC 49
    // 5.48 - 5. 59 pm
    public List<List<String>> groupAnagrams(String[] strs) {
        // edge
        if(strs == null || strs.length == 0){
            return null;
        }

        Map<String, List<String>> cache = new HashMap<>();
        for(String x: strs){
            char[] x_ = x.toCharArray();
            Arrays.sort(x_);
            String x_sorted = Arrays.toString(x_);
            List<String> cur = cache.getOrDefault(x_sorted, new ArrayList<>());
            cur.add(x);
            cache.put(x_sorted, cur);
        }

        System.out.println(">>> cache = " + cache);

        List<List<String>> res = new ArrayList<>();


        for(String k: cache.keySet()){
            res.add(cache.get(k));
        }

        return res;
    }

    // LC 271
    // 6.04 - 6.14 pm
    public String encode(List<String> strs) {
        // edge
        if(strs == null || strs.isEmpty()){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(String x: strs){
            sb.append(x);
            sb.append("#");  // NOTE !!! handle cases if strs has special character
        }

        return sb.toString();
    }

    public List<String> decode(String s) {
        // edge
        if(s.isEmpty()){
            return null;
        }

        List<String> res = new ArrayList<>();

        //String[] str_arr = s.split("#");
        String[] str_arr = s.split("#", -1); // NOTE: We remove the last element because it's an empty string resulting from the final delimiter
        for(String x: str_arr){
            if(!x.isEmpty()){
                res.add(x);
            }
        }

        return res;
    }

  // LC 11
  // 6.23 - 6.33 pm
  /**
   * Find two lines that together with the
   * x-axis form a container,
   * such that the container contains the most water.
   *
   * Return the maximum amount of water a container can store.
   *
   *
   *
   *  IDEA 1) 2 POINTERS
   */
  public int maxArea(int[] height) {
      // edge
      if(height == null || height.length == 0){
          return 0;
      }
      if(height.length == 2){
          return Math.min(height[0], height[1]);
      }

      // 2 pointers
      int max_area = 0;
      int l = 0;
      int r = height.length - 1;

      while(r > l){
          int cur_area = Math.min(height[l], height[r]) * (r - l);
          max_area = Math.max(max_area, cur_area);

          // if `left` height is smaller than `right` height
          if(height[l] < height[r]){
              l += 1;
          }
          // if `right` height is smaller than `left` height
          else{
              r -= 1;
          }

      }

      return max_area;
    }

    // LC 003
    // 6.37 - 6.47 pm
    /**
     *
     * Given a string s,
     * find the length of the longest substring without
     * duplicate characters.
     *
     *  IDEA 1) SLIDING WINDOW + hashmap
     */
    public int lengthOfLongestSubstring(String s) {
        // edge
        if(s.isEmpty()){
            return 0;
        }
        if(s.equals(" ") || s.length() == 1){
            return 1;
        }

        int max_len = 0;


        // Map -> to store the `latest` index of characters
        //Map : {val : latest idx of val}
        Map<String, Integer> map = new HashMap<>();
        //Set<String> set = new HashSet<>();
        int l = 0;
        int r = 0;

        String[] s_arr = s.split("");

        while(l < s_arr.length && r < s_arr.length){
            String val = s_arr[r];
            System.out.println(">>>  l = " + l + ", r = " + r + ", map = " + map + ", max_len = " + max_len);

            // if `not duplicated` element : move `right` pointer
            if(!map.containsKey(val)){
                r += 1;
            }
            // if `duplicated` element:  move `left` pointer to the `last` idx + 1 that same element occured
            else{
                l = Math.max(l, map.get(val) + 1);
            }

            map.put(val, r);
            max_len = Math.max(max_len, r - l + 1);
        }


        return max_len;
    }

    // LC 135
    // 4.19 - 4.29 pm
    /**
     *  IDEA 1) BINARY SEARCH
     *
     *
     *  exp 1)
     *    [4,5,6,7,0,1,2]
     *
     *   -> case 1) if mid `belongs to left` group
     *          -> `left - mid` is in `increasing order`
     *          -> if mid > mid+1
     *              -> search right
     *          -> else
     *              -> return left directly
     *
     * exp 2)
     *     [7,0,1,2,4,5,6]
     *
     *   if mid `belongs to right` group
     *
     *     -> `mid - right` is in `increasing order`
     *
     *        -> search left ??
     */
    public int findMin(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return -1; // ???
        }
//        if(nums[nums.length-1] > nums[0]){
//            return nums[0];
//        }
        int ans = nums[0];
        if(nums.length <= 3){
            for(int x: nums){
                ans = Math.min(x, ans);
            }
            return ans;
        }

        int l = 0;
        int r = nums.length - 1;

        while (r > l){

            if(nums[r] > nums[l]){
               // return nums[l];
                return Math.min(ans, nums[l]);
            }

            int mid = ( l + r ) / 2;
            ans = Math.min(nums[mid], ans);
            System.out.println(">>> l = " + l + ", r = " + r + ", mid = " + mid + ", ans =  " + ans);
            // case 1) if mid `belongs to left` group
            if(nums[mid] >= nums[l]){
//               if(nums[mid] > nums[mid+1]){
//                   l = mid + 1;
//               }else{
//                   return nums[l]; // ??
//               }
                l = mid + 1;
            }
            // case 2) if mid `belongs to right` group
            else{
                r = mid - 1;
            }
        }


        return ans;
    }

    // LC 150
    public int evalRPN(String[] tokens) {
        return 0;
    }

    // LC 22
    public List<String> generateParenthesis(int n) {

        return null;
    }

    // LC 739
    // 5.10 - 5.20 pm
    /**
     *   such that answer[i] is the number of days
     *   you have to wait after the ith day to get
     *   a warmer temperature
     *
     *
     *   IDEA 1) STACK + HASHMAP
     *     -> FILO
     *
     *
     *   exp 1)
     *     [73,74,75,71,69,72,76,73]
     *
     *     -> map : {val: idx}
     *
     *     ans : [0,0,0,0..]
     *
     *
     *   ->
     *
     *    [73,74,75,71,69,72,76,73]   st=[73],  ans : [0,0,0,0..]
     *     x
     *
     *    [73,74,75,71,69,72,76,73]   st=[74],  ans : [1,0,0,0..]
     *        x
     *
     *   [73,74,75,71,69,72,76,73]   st=[75],  ans : [1,1,0,0..]
     *          x
     *
     *   [73,74,75,71,69,72,76,73]   st=[75,71],  ans : [1,1,0,0..]
     *             x
     *
     *  [73,74,75,71,69,72,76,73]   st=[75,71,69],  ans : [1,1,0,0..]
     *               x
     *
     *  [73,74,75,71,69,72,76,73]   st=[75,72],  ans : [1,1,0,2,1,0,0,0]
     *                   x
     *
     *  [73,74,75,71,69,72,76,73]   st=[76],  ans : [1,1,4,2,1,1,0,0]
     *                      x
     *
     *  [73,74,75,71,69,72,76,73]   st=[76,73],  ans : [1,1,4,2,1,1,0,0]
     *                         x
     */
    public int[] dailyTemperatures(int[] temperatures) {
        // edge
        if(temperatures == null || temperatures.length == 0){
            return null;
        }
        if(temperatures.length == 1){
            return new int[]{0}; // ???
        }
        // map : {idx : val}
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < temperatures.length; i++){
           // map.put(temperatures[i], i);
            map.put(i, temperatures[i]);
        }

        Stack<Integer> st = new Stack<>();

        int[] ans = new int[temperatures.length];
        // init val as 0
        Arrays.fill(ans, 0);

        System.out.println(">> stack = " + st);

        for(int i = 0; i < temperatures.length; i++){

            System.out.println(">> i = " + i + ", stack = " + st + ", ans = " + Arrays.toString(ans));
            int cur = temperatures[i];

            while(!st.isEmpty() && map.get(st.peek()) < cur){
                int val = st.pop();
                System.out.println(" (while) i = " + i + ", val = " + val + ", map.get(val) = " + map.get(val));
                //ans[map.get(val)] = (i - map.get(val));
                ans[val] = (i - val);
            }

           // st.push(cur);
            st.push(i);
        }

        return ans;
    }


    // LC 206
    // 11.04 - 11.14 am
    public ListNode reverseList(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }

        ListNode _prev = null; //new ListNode(); // ??
        //_prev.next = head;

        while (head != null){
            ListNode _next = head.next;
            //_prev.next = head;
            //head.next = _next;
            head.next = _prev;
            _prev = head;
            head = _next;
        }

        return _prev;
    }

    // LC 92
    // 11.23 - 11.33 am
    /**
     *   IDEA 1) LINKED LIST -> ARRAY -> REVERSE -> LINKED LIST
     *
     *   IDEA 2) REVERSE LINKED LIST BY start, end point
     *
     *
     *  --------------
     *
     *  IDEA 2)
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || left >= right)
            return head;

        // Dummy node to handle edge cases like reversing from head
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode _prev = dummy;


        // move till `left` idx
        for(int i = 0; i < left; i++){
            _prev.next = _prev;
        }

        // cache
        ListNode _left = _prev;

        ListNode _head = null;

        // reverse
        for(int i = 0; i < right - left; i++){
            _head = _prev.next;
            ListNode _next = _head.next; // ???
            _head.next = _prev;
            _prev = _head;
            _head = _next;
        }

        ListNode reversedEnd = _head; // ???

        ListNode reversedFirst = _prev; // ???


        _left.next = reversedFirst;


        return null;
    }








    public ListNode reverseBetween_(ListNode head, int left, int right) {
        // edge
        if(head == null || head.next == null){
            return head;
        }
        if(left >= right){
            return head;
        }

        ListNode res = head;

        int idx = 0;

        // move till `left` idx
        while(idx < left && head != null){
            head = head.next;
            idx += 1;
        }

        // cache `left` node
        ListNode _left = head;

        // move till `right` idx
        while(idx < right && head != null){
            head = head.next;
            idx += 1;
        }

        // cache `right` node
        ListNode _right = head;


        ListNode reversedNode = reverseHelper(head, right - left + 1);

        /**
         *   so,
         *
         *   before:
         *
         *     ---------------------> head
         *
         *
         *   after (reversed):
         *
         *     ----> left   <--- reversed  ----> right
         *
         */

        _left.next = reversedNode;
        reversedNode.next = _right;

        return res;
    }

    public ListNode reverseHelper(ListNode head, int len){
        // edge
        if(head == null || head.next == null){
            return head;
        }

        ListNode _prev = null;

        int cur_len = 0;

        while(head != null && cur_len < len){
            ListNode _next = head.next;
            head.next = _prev;
            _prev = head;
            head = _next;
            cur_len += 1;
        }

        return _prev;
    }


    // LC 226
    // 12.05 - 12.15 pm
    // IDEA: DFS
    public TreeNode invertTree(TreeNode root) {
        // edge
        if(root == null){
            return root;
        }
//        if(root.left == null || root.right == null){
//            if(root.left == null){
//                root.left = root.right;
//            }else{
//                root.right = root.left;
//            }
//            return root;
//        }

        // dfs
        treeReverseHelper(root);
        return root;
    }

    public TreeNode treeReverseHelper(TreeNode root){
        // edge
        if(root == null){
            return root;
        }

        TreeNode _left = treeReverseHelper(root.left);
        TreeNode _right = treeReverseHelper(root.right);

        root.left = _right;
        root.right = _left;

        return root;
    }


    // LC 102
    // 2.24 - 2.34 pm
    // idea: bfs
    public List<List<Integer>> levelOrder(TreeNode root) {

        List<List<Integer>> res = new ArrayList<>();

        // edge
        if(root == null){
            return res;
        }
        if(root.left == null && root.right == null){
            List<Integer> tmp = new ArrayList<>();
            tmp.add(root.val);
            res.add(tmp);
            return res;
        }

        // bfs
        Queue<LayerNode> q = new LinkedList<>();
        q.add(new LayerNode(0, root));

        while(!q.isEmpty()){

            LayerNode ln = q.poll();
            TreeNode node = ln.node;
            int layer = ln.layer;

            if(layer >= res.size()){
                res.add(new ArrayList<>());
            }

            List<Integer> cur = res.get(layer);
            cur.add(node.val);

            if(node.left != null){
                q.add(new LayerNode(layer+1, node.left));
            }

            if(node.right != null){
                q.add(new LayerNode(layer+1, node.right));
            }

        }

        return res;
    }

    public class LayerNode{
        // attr
        int layer;
        TreeNode node;
        public LayerNode(int layer, TreeNode node){
            this.layer = layer;
            this.node = node;
        }
    }


    // LC 98
    public boolean isValidBST(TreeNode root) {

        // edge
        if(root == null){
            return true;
        }
        if(root.left == null && root.right == null){
            return true;
        }

//        int minVal = Integer.MIN_VALUE; // ??
//        int maxVal = Integer.MAX_VALUE; //Integer.MIN_VALUE; // ???

        long minVal = Long.MIN_VALUE;
        long maxVal = Long.MAX_VALUE;

        return validHelper(root, minVal, maxVal); //&&
//                validHelper(root.left, minVal, root.val) &&
//                validHelper(root.right, root.val, maxVal);
    }

    public boolean validHelper(TreeNode root, long minVal, long maxVal){

        // edge
        if(root == null){
            return true;
        }

        // ?? check if not valid
        if(root.val >= maxVal || root.val <= minVal){
            return false;
        }

        return validHelper(root.left, minVal, root.val) &&
                validHelper(root.right, root.val, maxVal);
    }


    // LC 124
    // 2.44 - 2.54 PM
    /**
     * Given the root of a binary tree,
     * return the maximum path sum of any non-empty path.
     *
     *
     *   A node can only appear in the sequence `at most once`.
     *   Note that the path does not need to pass through the root.
     *
     *
     *
     *   IDEA 1) DFS
     *      -> post order ???
     *         -> left -> right -> root
     *         -> so we can know the `path sum` before visit root
     *
     */
    int max_path_sum = 0;
    public int maxPathSum(TreeNode root) {
        // edge
        if(root == null){
            return 0; // ??
        }
        if(root.left == null && root.right == null){
            return root.val;
        }

        //dfs
        getMaxPathHelper(root);

        return max_path_sum;
    }

    public int getMaxPathHelper(TreeNode root){

        if(root == null){
            return 0; // ??
        }

        // int curSum = root.val +

        int _left = Math.max(getMaxPathHelper(root.left), 0);
        int _right = Math.max(getMaxPathHelper(root.right), 0);

        max_path_sum = Math.max(max_path_sum,
                root.val + _left + _right);

       // return max_path_sum; // ??
        return root.val + Math.max(_left, _right);
    }



//    public TreeNode getMaxPathHelper(TreeNode root){
//
//        if(root == null){
//            return null; // ??
//        }
//
//       // int curSum = root.val +
//
//        TreeNode _left = getMaxPathHelper(root.left);
//        TreeNode _right = getMaxPathHelper(root.right);
//
//        return root;
//    }

    // LC 297
    // 3.47 - 3.57 PM
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode(int x) { val = x; }
     * }
     */
    // Your Codec object will be instantiated and called as such:
    // Codec ser = new Codec();
    // Codec deser = new Codec();
    // TreeNode ans = deser.deserialize(ser.serialize(root));
    public class Codec {

        // attr
        String serialized = null; // ???
        TreeNode deserialiazed = null;
        StringBuilder sb = new StringBuilder();

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            // edge
            if(root == null){
                return "#";
            }

            sb.append(root.val);
            sb.append(","); // new node, but same layer

            serialize(root.left);
            sb.append(","); // new node, but same layer

            serialize(root.right);
            //sb.append("#"); // ?? new layer

            return sb.toString();
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {

            // edge
            if(data.equals("#")){
                return null;
            }

            String[] data_arr = data.split(",");
            Queue<String> q = new LinkedList<>(Arrays.asList(data_arr));

            return helper(q);
//
//            if(data_arr.length==1){
//                return new TreeNode(Integer.parseInt(data_arr[0]));
//            }
//
//            TreeNode res = new TreeNode(Integer.parseInt(data_arr[0]));
//
//
//            // ????
//            //TreeNode res = new TreeNode()
//            for(int i = 0; i < data_arr.length; i++){
//                if(i == 0){
//                    continue;
//                }
//
//                res.left = new TreeNode(Integer.parseInt(data_arr[i]));
//            }
//
//            return res;
        }

        private TreeNode helper(Queue<String> q){

            // edge
            if(q.isEmpty()){
                return null;
            }

            String str = q.poll();
            if(str.isEmpty()){
                return null;
            }

            TreeNode root = new TreeNode(Integer.valueOf(str));

            root.left = helper(q);
            root.right = helper(q);

            return root;
        }

    }


    // LC 200
    public int numIslands(char[][] grid) {

        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }
        if(grid.length == 1 && grid[0].length == 1){
            return grid[0][0] == '1' ? 1: 0;
        }

        int cnt = 0;

        int l = grid.length;
        int w = grid[0].length;

        // set : { x-y }
       // HashSet<String> visited = new HashSet<>();

        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                if(grid[i][j] == '1'){
                    isLandHelper(j, i, grid);
                    cnt += 1;
                }
            }
        }

        return cnt;
    }

    public void isLandHelper(int x, int y, char[][] grid){

        int l = grid.length;
        int w = grid[0].length;

        int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

        if(x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != '1'){
            return;
        }

        grid[y][x] = '@'; // ???

        for(int[] d: dirs){

            int x_ = x + d[0];
            int y_ = y + d[1];

            isLandHelper(x_, y_, grid);
        }

        //return true;
    }


    // LC 525
    // 10.58 - 11.08 am
    /**
     *
     *
     *   Given a binary array nums, return the maximum length of a
     *   contiguous subarray with an ` equal number of 0 and 1.` !!!!
     *
     *  IDEA 1) GREEDY
     *
     *  IDEA 2) HASH MAP
     *
     *      -> 1. get all 0, 1 count
     *      -> 2. loop over nums
     *          -> check if `cur window` 0,1 is a valid contiguous subarray
     *          -> check if `remove cur widnwo` can form a valid contiguous subarray
     *          -> maintain the max sub array len
     *
     *       -> 3. return max sub array len
     *
     *
     */

    // hashMap
    public int findMaxLength(int[] nums) {

        // edge
        if(nums == null || nums.length <= 1){
            return 0;
        }
        if(nums.length == 2){
            return nums[0] != nums[1] ? 2 : 0;
        }

        // brute force
        int max_len = 0;

        Map<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i < nums.length; i++){
            int o_cnt = 0;
            int z_cnt = 0;
            for(int j = i; j < nums.length; j++){
                if(nums[j] == 0){
                    z_cnt += 1;
                }else{
                    o_cnt += 1;
                }

                if(z_cnt == o_cnt){
                    max_len = Math.max(max_len, j - i + 1);
                }
            }
        }


        return max_len;
    }

    // brute force
    public int findMaxLength_(int[] nums) {

        // edge
        if(nums == null || nums.length <= 1){
            return 0;
        }
        if(nums.length == 2){
            return nums[0] != nums[1] ? 2 : 0;
        }

        // brute force
        int max_len = 0;

        for(int i = 0; i < nums.length; i++){
            int o_cnt = 0;
            int z_cnt = 0;
            for(int j = i; j < nums.length; j++){
                if(nums[j] == 0){
                    z_cnt += 1;
                }else{
                    o_cnt += 1;
                }

                if(z_cnt == o_cnt){
                    max_len = Math.max(max_len, j - i + 1);
                }
            }
        }


        return max_len;
    }

}
