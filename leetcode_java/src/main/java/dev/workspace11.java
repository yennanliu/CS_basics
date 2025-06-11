package dev;

import LeetCodeJava.DataStructure.TreeNode;

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




}
