package dev;

import sun.awt.PeerEvent;

import java.util.*;

public class workspace7 {
    public static void main(String[] args) {

        // ------------------- TEST 1
        //    int len = 5;
        //    int[][] updates ={ {1,3,2}, {2,4,3}, {0,2,-2} };
        //    int[] res = getModifiedArray(len, updates);
        //    System.out.println(">>> res = ");
        //    for (int x : res){
        //      // [-2,0,3,5,3]
        //      System.out.println(x);
        //    }

        // ------------------- TEST 2
        //    int[][] mat1 = new int[][]{ {1,0,0}, {-1,0,3} };
        //    int[][] mat2 = new int[][]{ {7,0,0}, {0,0,0}, {0,0,1} };
        //    int[][] res = multiply(mat1, mat2);
        //    System.out.println(">>> res = ");
        //    for (int i = 0; i < res.length; i++){
        //      for (int j = 0; j < res[0].length; j++){
        //        System.out.println(res[i][j]);
        //      }
        //    }

        // ------------------- TEST 3
        //    String a = "a";
        //    String b = "b";
        //    a += b;
        //    System.out.println(">> a = " + a);

        // ------------------- TEST 4
        //    List<String> x = new ArrayList<>();
        //    x.add("a");
        //    x.add("b");
        //    System.out.println(x.toString());

        // ------------------- TEST 5
        // ++i -> It increments i after the current iteration’s execution.
        /**
         * Key Points:
         * 	•	Prefix (++i) vs. Postfix (i++):
         * 	•	Prefix increments the value before use.
         * 	•	Postfix increments the value after use.
         * 	•	In a for loop, this distinction doesn’t matter since the increment happens at the end of each iteration.
         * 	•	Variable Name:
         * 	•	Ensure the loop variable is consistently referenced. Mixing i and I results in a compilation error due to case sensitivity in Java.
         */
//    for (int i = 0; i < 5; ++i) {
//      System.out.println(i);
//    }

        // ------------------- TEST 6 : map put, get
//    Map<String, Integer> map = new HashMap<>();
//    map.put("a", 1);
//    map.put("b", 2);
//    map.put("c", 3);
//
//    System.out.println(">>> (before) map = " + map);
//    map.put("a", 10);
//    //map.put("c", 4);
//
//    System.out.println(">>> (after) map = " + map);

//
//        // ------------------- TEST 6 : sort map on val, key len
//        Map<String, Integer> map = new HashMap<>();
//        map.put("apple", 3);
//        map.put("banana", 2);
//        map.put("lem", 3);
//        map.put("kiwi", 5);
//        map.put("peachhh", 3);
//
//        System.out.println("Original map: " + map);
//
//        //Map<String, Integer> sortedMap = sortMapByValueAndKeyLength(map);
//        //Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
//        //   List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
//        //List<Set<Map.Entry<String, Integer>>> entryList = Arrays.asList(map.entrySet());
//        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
//
//        // sort map
//        Collections.sort(entryList, (Comparator<Map.Entry<String, Integer>>) (entry1, entry2) -> {
//            // if(o1.)
//            int valueCompare = entry2.getValue().compareTo(entry1.getValue());
//            if (valueCompare == 0) {
//                return entry1.getKey().length() - entry2.getKey().length();
//            }
//            return valueCompare;
//        });
//
//
//        // NOTE !!! use `LinkedHashMap` keep ordering in map
//        Map<String, Integer> sortedMap = new LinkedHashMap<>();
//        for (Map.Entry<String, Integer> entry : entryList) {
//            sortedMap.put(entry.getKey(), entry.getValue());
//        }
//
//        System.out.println("Sorted map: " + sortedMap);



//        // ------------------- TEST 7: replace idx in string
//        String myStr = "amazon";
//        System.out.println(myStr);
//        System.out.println(updateStringWithIdx(myStr, "xxxx", 1));
//        System.out.println(updateStringWithIdx(myStr, "xxxx", 0));

        // ------------------- TEST 8: array add val to element
//        int[] arr = new int[3];
//        arr[0] = 0;
//        arr[1] = 1;
//        arr[2] = 2;
//        System.out.println("(before) arr = ");
//        for(int x: arr){
//            System.out.println(x);
//        }
//
//        arr[0] = arr[0]+1;
//
//        System.out.println("(after) arr = " + Arrays.asList(arr));
//        for(int x: arr){
//            System.out.println(x);
//        }

        // ------------------- TEST 9 TEST PRESUM
//        List<Integer> preSum = new ArrayList<>();
//        List<Integer> data = new ArrayList<>();
//        data.add(1);
//        data.add(2);
//        data.add(3);
//        data.add(4);
//
//        int cur = 0;
//        preSum.add(cur);
//        for(int i = 0; i < data.size(); i++){
//            cur += data.get(i);
//            preSum.add(cur);
//        }
//
//        System.out.println(">>> data = " + data);
//        System.out.println(">>> preSum = " + preSum);
//
//
//        System.out.println(">>> sum(1,2) = " + (preSum.get(3) - preSum.get(1)));



        // ------------------- TEST 10 TEST ARRAY SORT
//       int[] nums = new int[]{0,3,7,2,5,8,4,6,0,1};
//       Arrays.sort(nums);
//       //System.out.println(Arrays.asList(nums));
//       for(int x: nums){
//           System.out.println(x);
//       }

        // ------------------- TEST 11 list to str
//        List<String> my_str_10 = new ArrayList<>();
//        my_str_10.add("(");
//        my_str_10.add(")");
//        System.out.println(String.valueOf(my_str_10));


        // ------------------- TEST 12  init array
//        int k = 4;
//        int[][] x = new int[k][2];
//        System.out.println(">>> before " + Arrays.deepToString(x));
//
//        x[0] = new int[]{0,1};
//        x[1] = new int[]{0,2};
//
//        System.out.println(">>> after " + Arrays.deepToString(x));
//
//        int[] x2 = new int[]{1, 2, 3};
//        System.out.println(Arrays.toString(x2));  // Output: [1, 2, 3]

        // LC 127
        // SUB STRING
        /**
         *  1st idx start from 0
         *  2nd ind start from 1
         *
         *   -> e.g.  [1st_idx, 2nd_idx]
         *
         */
        // ------------------- TEST 13  SUB STRING
        String x = "abcd";
        //
        System.out.println(x.substring(0,1)); // a
        System.out.println(x.substring(0,2)); // ab
        System.out.println(x.substring(2,3)); // c
        System.out.println(x.substring(2,4)); // cd
        //System.out.println(x.substring(2,10)); // `StringIndexOutOfBoundsException`

        String y = "apple";
        List<String> replaces = new ArrayList<>();
        replaces.add("1");
        replaces.add("2");
        replaces.add("3");
        replaces.add("4");
        replaces.add("5");

        for(int i = 0; i < y.length(); i++){

            String z = y.substring(0, i) + "?" + y.substring(i+1, y.length());

            String y_ = y.substring(0,i) + replaces.get(i) + y.substring(i+1, y.length());
            System.out.println("y_ = " + y_);
            /**
             *  result:
             *
             * y_ = 1pple
             * y_ = a2ple
             * y_ = ap3le
             * y_ = app4e
             * y_ = appl5
             *
             */
        }

        // ------------------- TEST 14  list -> array

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        // <T> T[] toArray(T[] a);
        Integer[] arr = list2.toArray(new Integer[list2.size()]);


//        PriorityQueue<Integer> minHeap = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o1 - o2;
//                return diff;
//            }
//        });

    }


    private static String updateStringWithIdx(String input, String newStr, int idx){
        StringBuilder sb = new StringBuilder(input);
        return sb.replace(idx, idx+1, newStr).toString();
    }


    public static int[][] multiply(int[][] mat1, int[][] mat2) {
        // Edge case: Single element matrices
        if (mat1.length == 1 && mat1[0].length == 1 && mat2.length == 1 && mat2[0].length == 1) {
            return new int[][]{{mat1[0][0] * mat2[0][0]}};
        }

        int l_1 = mat1.length;    // Number of rows in mat1
        int w_1 = mat1[0].length; // Number of columns in mat1 (and rows in mat2)

        int w_2 = mat2[0].length; // Number of columns in mat2

        // Initialize the result matrix
        int[][] res = new int[l_1][w_2];

        // Perform matrix multiplication
        for (int i = 0; i < l_1; i++) {
            for (int j = 0; j < w_2; j++) {
                int sum = 0; // Sum for res[i][j]
                for (int k = 0; k < w_1; k++) {
                    sum += mat1[i][k] * mat2[k][j];
                }
                res[i][j] = sum;
            }
        }

        return res;
    }

//  public static int[][] multiply(int[][] A, int[][] B) {
//    int m = A.length; // Number of rows in A
//    int n = A[0].length; // Number of columns in A
//    int p = B[0].length; // Number of columns in B
//
//    int[][] result = new int[m][p];
//
//    // Iterate through rows of A
//    for (int i = 0; i < m; i++) {
//      for (int k = 0; k < n; k++) {
//        if (A[i][k] != 0) { // Skip zero entries in A
//          for (int j = 0; j < p; j++) {
//            if (B[k][j] != 0) { // Skip zero entries in B
//              result[i][j] += A[i][k] * B[k][j];
//            }
//          }
//        }
//      }
//    }
//
//    return result;
//  }

//  public static int[][] multiply(int[][] mat1, int[][] mat2) {
//
//    // edge case
//    if (mat1.length == 1 && mat1[0].length == 1 && mat2.length == 1 && mat2[0].length == 1){
//      int[][] res = new int[][]{};
//      res[0][0] = mat1[0][0] * mat2[0][0];
//      return res;
//    }
//
//    int l_1 = mat1.length;
//    int w_1 = mat1[0].length;
//
//    int l_2 = mat2.length;
//    int w_2 = mat2[0].length;
//
//    int[][] res = new int[l_1][w_2]; // ???
//
//    for(int i = 0; i < l_1; i++){
//      int tmp = 0;
//      for (int j = 0; j < w_2; j++){
//        System.out.println(">>> i = " + i + ", j = " + j + ", tmp = " + tmp);
//        tmp += (mat1[i][j] * mat2[j][i]);
//        res[i][j] = tmp;
//      }
//      //res[i][j] = tmp; // ?
//    }
//
//    return res;
//  }

    public static int[] getModifiedArray(int length, int[][] updates) {

        int[] tmp = new int[length + 1]; // ?
        for (int[] x : updates) {
            int start = x[0];
            int end = x[1];
            int amount = x[2];

            // add
            tmp[start] += amount;

            // subtract (remove the "adding affect" on "NEXT" element)
            if (end + 1 < length) { // ??
                tmp[end + 1] -= amount;
            }
        }

        // prepare final result
        for (int i = 1; i < tmp.length; i++) {
            tmp[i] += tmp[i - 1];
        }

        return Arrays.copyOfRange(tmp, 0, length); // ????
        //return tmp;
    }

    // LC 904
    // https://leetcode.com/problems/fruit-into-baskets/description/
    /**
     *
     *  904. Fruit Into Baskets
     * Solved
     * Medium
     * Topics
     * Companies
     * You are visiting a farm that has a single row of fruit trees
     * arranged from left to right. The trees are represented by an integer
     * array fruits where fruits[i] is the type of fruit the ith tree produces.
     *
     * You want to collect as much fruit as possible. However, the owner has some strict rules that you must follow:
     *
     * You only have two baskets, and each basket can only hold a single type of fruit. There is no limit on the amount of fruit each basket can hold.
     * Starting from any tree of your choice, you must pick exactly one fruit from every tree (including the start tree) while moving to the right. The picked fruits must fit in one of your baskets.
     * Once you reach a tree with fruit that cannot fit in your baskets, you must stop.
     * Given the integer array fruits, return the maximum number of fruits you can pick.
     *
     *
     *
     * -> NOTE
     *
     * The trees are represented by an integer
     *      * array fruits where fruits[i] is the type of fruit the ith tree produces.
     *
     * Example 1:
     *
     * Input: fruits = [1,2,1]
     * Output: 3
     * Explanation: We can pick from all 3 trees.
     *
     *
     * Example 2:
     *
     * Input: fruits = [0,1,2,2]
     * Output: 3
     * Explanation: We can pick from trees [1,2,2].
     * If we had started at the first tree, we would only pick from trees [0,1].
     *
     *
     * Example 3:
     *
     * Input: fruits = [1,2,3,2,2]
     * Output: 4
     * Explanation: We can pick from trees [2,3,2,2].
     * If we had started at the first tree, we would only pick from trees [1,2].
     *
     *
     * Constraints:
     *
     *
     *
     * -> Starting from any tree of your choice,
     *   you must pick exactly one fruit from every tree (including the start tree)
     *
     */
    // 10.25 - 10.36 am
    /**
     *  IDEA: 2 pointers (?
     *
     *   -> step 1) l = 0, r = 0
     *   -> step 2) move r, if set(l, r) <= 2, keep moving r (update res)
     *   -> step 3) if set(l, r) > 2, MOVE l to r-1 (update res)
     *   -> step 4) keep moving r, till meets the end
     *
     *  -> so tree[i] is `type` of the tree
     *  -> e.g. trees = [1,2,1,1,2]
     *         -> its type : [type-1, type-2, type-1, type-1, type-2]
     *
     *
     *  Exp 1)  [1,2,1]
     *          lr          -> res =1
     *           l r        -> res= 2
     *           l   rl     -> res = 3
     *
     *
     * Exp 2) [0,1,2,2]
     *         lr         -> res = 1
     *         l r        -> res = 2
     *           l  r     -> res = 2
     *           l    r   -> res = 3
     *
     *
     * Exp 3) [1,2,3,2,2]
     *        lr                -> res = 1
     *        l  r              -> res = 2
     *           l r           -> res = 2
     *           l   r         -> res = 3
     *           l     r       -> res = 4
     *
     *
     */

    public int totalFruit(int[] fruits) {

        if(fruits.length==0){
            return 0;
        }

        int res = 0;
        // 2 pointers
        /**
         *
         *      *   -> step 1) l = 0, r = 0
         *      *   -> step 2) move r, if set(l, r) <= 2, keep moving r (update res)
         *      *   -> step 3) if set(l, r) > 2, MOVE l to r-1 (update res)
         *      *   -> step 4) keep moving r, till meets the end
         *
         */
        int l = 0;
        int r = 0;
        //Set<Integer> set = new HashSet<>();
        Map<Integer, Integer> map = new HashMap<>();
        //int cur = 1;
        while (r < fruits.length){

            map.put(fruits[r], map.getOrDefault(fruits[r],0)+1);

            while (map.keySet().size() > 2) {
                // update map
                map.put(l, map.get(l)-1);
                if (map.get(l) == 0){
                    map.remove(l);
                }
                // move l
                l += 1;
            }

            r += 1;
            res = Math.max(res, r-l+1);
        }

        return res;
    }

    // LC 279
    /**
     *
     *
     Given an integer n, return the
     least number of perfect square numbers that sum to n.

     A perfect square is an integer that is the
     square of an integer; in other words,
     it is the product of some integer with itself.
     For example, 1, 4, 9, and 16 are perfect squares while 3 and 11 are not.


     Example 1:

     Input: n = 12
     Output: 3
     Explanation: 12 = 4 + 4 + 4.


     Example 2:

     Input: n = 13
     Output: 2
     Explanation: 13 = 4 + 9.

     Constraints:

     1 <= n <= 104
     *
     */
    public int numSquares(int n) {

        return 1;
    }

    // LC 139
    // 4.26 pm - 4.36 pm
    /**
     *  IDEA 1) BFS
     *  IDES 2) DFS ???
     *  IDEA 3) DP ???
     *
     */
    public boolean wordBreak(String s, List<String> wordDict) {

        // edge
        if(s == null || s.length() == 0){
            return true;
        }
        if(wordDict.contains(s)){
            return true;
        }
        if(s != null && wordDict.isEmpty()){
            return false;
        }

        Queue<String> q = new LinkedList<>();
        // init by adding words has same `prefix`
//        for(String w: wordDict){
//            if(s.contains(w)){
//                q.add(w);
//            }
//        }
        q.add(""); // ???

        while(!q.isEmpty()){
            String cur = q.poll();
            int idx = cur.length();
            if(cur.equals(s)){
                return true;
            }
            if(cur.length() > s.length()){
                continue;
            }

            for(String w: wordDict){

                // ???
                if(idx + w.length() <= s.length()){
                    if(s.substring(idx, idx + w.length()).equals(w)){

                        if(cur.equals(s)){
                            return true;
                        }

                        cur += w;
                        q.add(cur);
                    }
                }

            }

        }

        return false;
    }

  // LC 300
  // 5.04 pm - 5.15 pm
  /**
   * Given an integer array nums,
   * return the length of the
   * longest strictly increasing subsequence.
   *
   *   -> A subsequence is an array that can be derived
   *      from another array by deleting some or no
   *      elements without changing the order of the remaining elements.
   *
   *
   *  exp 1)
   *  Input: nums = [10,9,2,5,3,7,101,18]
   *  Output: 4
   *
   *  -> [2,3,7,101], so ans = 4
   *
   *  exp 2)
   *
   *  Input: nums = [0,1,0,3,2,3]
   *  Output: 4
   *
   *  -> [0,1,2,3]
   *
   *  exp 3)
   *
   *  Input: nums = [7,7,7,7,7,7,7]
   *  Output: 1
   *
   *  -> [7], ans = 1
   *
   *
   *  IDEA 1) SLIDING WINDOW
   *
   */
//  public int lengthOfLIS(int[] nums) {
//      // edge
//      if(nums == null || nums.length == 0){
//          return 0;
//      }
//      if(nums.length == 1){
//          return 1;
//      }
//      if(nums.length == 2){
//          if(nums[1] > nums[0]){
//              return 2;
//          }
//          return 1;
//      }
//
//      HashSet<Integer> set = new HashSet<>();
//      for(int n: nums){
//          set.add(n);
//      }
//      if(set.size() == 1){
//          return 1;
//      }
//
//      // map : record latest element idx ???
//
//      // sliding window
//      int res = 1;
//
//
//
//      return 1;
//    }

    // IDEA 2) BRUTE FORCE + BINARY SEARCH ???
//  public int lengthOfLIS(int[] nums) {
//      // edge
//      if(nums == null || nums.length == 0){
//          return 0;
//      }
//      if(nums.length == 1){
//          return 1;
//      }
//      if(nums.length == 2){
//          if(nums[1] > nums[0]){
//              return 2;
//          }
//          return 1;
//      }
//
//
//
//      return 1;
//  }

  int maxSubLen = 1;
  // recursion
  public int lengthOfLIS_1_1(int[] nums) {
      dfs(nums, 0, -1);
      return maxSubLen;
  }

   // 5.32 - 5.37
    private void dfs(int[] nums, int i, int j) {

        if(nums[j] > nums[i]){
            maxSubLen += 1;
        }

        for(int i_ = 0; i_ < nums.length; i_++){
//            if(nums[j] > nums[i]){
//                maxSubLen += 1;
//            }
        }

      return;
    }

    // LC 416
    // idea dp ???
    // 5.50 - 6 pm
    /**
     *
     *   1. get total sum of nums
     *   2. [ 1,5, 11, 5]
     *     -> each element can be selected or NOT selected
     *     -> we keep track each option and get the cur_sum
     *     -> then check if there is an option that its cur_sum == sum / 2
     *
     *
     *  exp 1)
     *
     *  ->  [ 1,5, 11, 5] -> sum = 22
     *  -> sum / 2 = 11
     *  -> so if any option sum == 11, we found a solution
     *
     *    [1], [1,5],  [1,11], [1,5]
     *        [1,5,11]
     *        [1,5,5] -> ok
     *
     *
     *  exp 2)
     *   Input: nums = [1,2,3,5] -> sum = 11
     *   -> sum / 2 = 5.5
     *   -> NOT possible to get a sub array equals to 5.5
     *
     */
    public boolean canPartition(int[] nums) {

      return false;
    }

    // LC 62
    // 4.24 - 4.34 pm
    // idea: DP (2D)
    /**
     *   idea 1) DP (2D)
     *
     *   - can -> or down
     *   - the total count of "->" and "down" are fixed
     *      - e.g. we just need to count their count of combination
     *
     *
     *          ->
     *         ->      d
     *       ->    d   ->  d
     *     ->  d  -> d   ...
     *
     *
     *     d[n,n] = max(d[n-1, n] + 1, d[n, n-1] + 1)
     *
     *
     */
    public int uniquePaths(int m, int n) {

        // init val as 0 ??
        // m : y-axis
        // n: x-axis
        int[][]dp = new int[m][n]; // ??
        // init
//        dp[1][0] = 1;
//        dp[0][1] = 1;
        dp[m-1][n-1] = 1; // NOTE !!!


        for(int i = m-1; i >= 0; i--){
            for (int j = n-1; j >= 0 ; j--){
                //dp[i][j] = Math.max(dp[i-1][j]+1, dp[i][j-1]+1);
                dp[i][j] += (dp[i + 1][j] + dp[i][j + 1]);
            }
        }

        System.out.println(">>> dp = " + dp);

        return dp[0][0];
    }

}
