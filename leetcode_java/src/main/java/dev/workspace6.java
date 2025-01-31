package dev;


import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.LinkedList.InsertIntoACyclicSortedList;
import LeetCodeJava.Recursion.PopulatingNextRightPointersInEachNode2;
import LeetCodeJava.Recursion.SumOfLeftLeaves;
//import jdk.javadoc.internal.doclets.toolkit.util.Utils;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.*;
import java.util.List;

public class workspace6 {
    public static void main(String[] args) {

        // inverse order
        int[] array_1 = new int[3];
        array_1[0] = 1;
        array_1[1] = 2;
        array_1[2] = 3;

//        for (int i = array_1.length - 1; i >= 0; i--) {
//            System.out.println(array_1[i]);
//        }

        // s = "3[a]2[bc]", return "aaabcbc".
        /**
         *
         *      * s = "3[a]2[bc]", return "aaabcbc".
         *      * s = "3[a2[c]]", return "accaccacc".
         *      * s = "2[abc]3[cd]ef", return "abcabccdcdcdef".
         *
         */
        String x = "3[a]2[bc]"; //"3[a2[c]]"; //"2[abc]3[cd]ef"; //"3[a2[c]]"; //"3[a]2[bc]"; //"2[abc]3[cd]ef"; //"3[a2[c]]"; //"3[a]2[bc]";
        String y = decodeString(x);
       // System.out.println(">>> y = " + y);
    }

    public static String decodeString(String s) {
        if (s.length() == 0) {
            return null;
        }
        // init
        Stack<String> stack = new Stack<>(); // ??
        StringBuilder sb = new StringBuilder();
        String A_TO_Z = "abcdefghijklmnopqrstuvwxyz";
        for (String x : s.split("")) {
            System.out.println(">>> x = " + x);
            String tmp = "";
            StringBuilder tmpSb = new StringBuilder();
            if (!x.equals("]")) {
                if (!x.equals("[")) {
                    stack.add(x);
                }
            } else {
                // pop all elements from stack, multiply, and add to res
                //String tmp = "";
                while (!stack.isEmpty()) {
                    String cur = stack.pop(); // ??
                    if (A_TO_Z.contains(cur)) {
                        tmp = cur + tmp;
                        //tmpSb.append(tmpSb);
                    } else {
                        tmp = getMultiplyStr(tmp, Integer.parseInt(cur));
                    }
                }

            }

            sb.append(tmp);
        }

        //String tmpRes = sb.toString();

        StringBuilder tmpSb = new StringBuilder();

        // add remaining stack element to result
        while(!stack.isEmpty()){
            tmpSb.append(stack.pop());
            //tmpRes =  tmpRes + stack.pop();
        }

        sb.append(tmpSb.reverse());

        return sb.toString();
    }

    private static String getMultiplyStr(String cur, Integer multiply) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < multiply; x++) {
            sb.append(cur);
        }
        return sb.toString();
    }

    // LC 815
    // https://leetcode.com/problems/bus-routes/
    // 12.47 pm - 1.00 pm

    /**
     * For example, if routes[0] = [1, 5, 7],
     * this means that the 0th bus travels in the
     * sequence 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1 -> ... forever.
     * <p>
     * -> Return the least number of buses you must take to
     * travel from source to target.
     * Return -1 if it is not possible.
     * <p>
     * <p>
     * <p>
     * IDEA : BFS
     * <p>
     * step 1) build graph
     * step 2) find min path via BFS
     * step 3) if can't find, return -1
     * <p>
     * <p>
     * exp 1)
     * <p>
     * routes = [[1,2,7],[3,6,7]], source = 1, target = 6
     * <p>
     * -> graph = {
     * 1: [2,7],
     * 2: [1,7],
     * 7: [1,2, 3,6],
     * 3: [6,7],
     * 6: [3,7],
     * // 7: [3,6]
     * }
     * <p>
     * -> so, we start src = 1,
     * then can visit 2, 7
     * - for 2, can visit 1,7 (already visited)
     * - for 7, can visit 1,2,3,6
     * and 6 is the target
     * -> so return 2 as result
     **/
    public int numBusesToDestination(int[][] routes, int source, int target) {

        // edge case
        if (routes.length == 1) {
            if (source == target) {
                return 0;
            }
            return 1;
        }
        // build graph
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] x : routes) {
            int start = x[0];
            int end = x[1];
            List<Integer> cur = graph.getOrDefault(start, new ArrayList<>());
            cur.add(end);
            graph.put(start, cur); // double check ???
        }
        // bfs
        int cnt = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        Set<Integer> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            Integer curr = queue.poll();
            // visit "neighbor"
            if (!graph.keySet().isEmpty()) {
                for (Integer x : graph.get(curr)) {
                    if (!visited.contains(x)) {
                        if (target == x) {
                            return cnt;
                        }
                        cnt += 1;
                        queue.add(x);
                        visited.add(x);
                    }
                }
            }
        }

        //return cnt > 0 ? cnt : -1;
        return -1;
    }

    // LC 593
    // https://leetcode.com/problems/valid-square/
    // 2.05 pm - 2.20 pm
    /**
     *  Idea 1)  math
     *
     *   A----B
     *   |     |
     *   C----D
     *
     *
     *  -> so, dist(A,B) ==  dist(C,D)
     *
     *  step 1) create a check func
     *   1) check if same dist
     *   2) check if "90 degree"
     *
     *  step 2) go through each point, and check "if at least 2 combinations"
     *          fit the check logic, if any violation, return false directly
     *
     *  step 3) return true
     *
     */
    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {

        List<int[]> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);

        for (int i = 0; i < list.size(); i++){
            int violationCnt = 0;
            for (int j = 0; j < list.size(); j++){
                if (i != j){
                    if (!canBuildSqaure(list.get(i), list.get(j))){
                        violationCnt += 1;
                    }
                }
                if (violationCnt > 1){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canBuildSqaure(int[] x1, int[] x2){

        return true;
    }

  // LC 1109
  // https://leetcode.com/problems/corporate-flight-bookings/
  // 3.56 pm - 4.15 pm
  /**
   *  示例 1：
   *
   * 输入：bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
   * 输出：[10,55,45,25,25]
   * 解释：
   * 航班编号        1   2   3   4   5
   * 预订记录 1 ：   10  10
   * 预订记录 2 ：       20  20
   * 预订记录 3 ：       25  25  25  25
   * 总座位数：      10  55  45  25  25 <-----------
   * 因此，answer = [10,55,45,25,25]
   *
   *
   * 示例 2：
   *
   * 输入：bookings = [[1,2,10],[2,2,15]], n = 2
   * 输出：[10,25]
   * 解释：
   * 航班编号        1   2
   * 预订记录 1 ：   10  10
   * 预订记录 2 ：       15
   * 总座位数：      10  25  <-----------
   * 因此，answer = [10,25]
   *
   */
  /**
   *  Idea : array op (presum ??)
   *
   *  Exp 1: bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
   *
   *  (idx start from 1)
   *
   *
   *    * 输入：bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
   *    * 输出：[10,55,45,25,25]
   *    * 解释：
   *    * 航班编号        1   2   3   4   5
   *    * 预订记录 1 ：   10  10
   *    * 预订记录 2 ：       20  20
   *    * 预订记录 3 ：       25  25  25  25
   *    * 总座位数：      10  55  45  25  25 <-----------
   *    * 因此，answer = [10,55,45,25,25]
   *
   *
   *  -> arr1 = [10,10,0,0,0], presum1 = [10,20,20,20,20]
   *  -> arr2 = [0,20,20,0,0], presum2 = [0,20,40,40,40]
   *  -> arr3 = [0,25,25,25,25], presum = [0,25,50,75,100]
   *  ...
   *
   *
   */
  /**
   *  IDEA : presum + `difference` array
   *
   *
   */
  public int[] corpFlightBookings(int[][] bookings, int n) {
      //int presum = 0;
      //int[] tmpArray = new int[n+1]; // size = n + 1 ??
      int[] tmpArray = new int[n]; // size = n + 1 ??
      for (int[] booking: bookings){
          int start = booking[0];
          int end = booking[1];
          int seat = booking[2];

          //presum += seat;
          //int presum = seat;
          // append all "seat" between start, end index
          // will `adjust` the overhead adding in the following code
          for (int i = start-1; i < end; i++){
              tmpArray[i] = seat; // ???
          }
      }

      // adjust

      return tmpArray; // ???
  }

//  public int[] corpFlightBookings(int[][] bookings, int n) {
//        int[] res = new int[n];
//        List<Integer[]> preSumList = new ArrayList<>();
//        for (int[] x : bookings){
//           Integer[] preSumArr = createPreSumArr(x, n);
//           System.out.println(">>> preSumArr = " + Arrays.asList(preSumArr));
//            preSumList.add(createPreSumArr(x, n));
//        }
//
//        // merge presum array to a single array
//        List<Integer> mergedPreSum = new ArrayList<>();
//        // TODO : optimize double loop ??
//        for (Integer[] list : preSumList){{
//            int cur = 0;
//            for (int i = 0; i < list.length; i++){
//                cur = mergedPreSum.get(i); // ???
//                cur += list[i];
//               // mergedPresum.add(cur);
//            }
//            mergedPreSum.add(cur);
//        }}
//
//        // add result to res
//        for (int j = 0; j < mergedPreSum.size(); j++){
//            res[j] = mergedPreSum.get(j+1) - mergedPreSum.get(j);
//        }
//        return res;
//    }
//
//
//    private Integer[] createPreSumArr(int[] input, int n){
//      int presum = 0;
//      Integer[] res = new Integer[n+1];
//      res[0] = 0;
//      for (int i = 0; i < input.length; i++){
//          presum += input[i];
//          res[i+1] = presum;
//      }
//      return res;
//    }

    // LC 523
    // https://leetcode.com/problems/continuous-subarray-sum/
    // 3.11 pm - 3.20 pm
    /**
     *  idea : presum + hashmap
     *
     */
    public boolean checkSubarraySum(int[] nums, int k) {

      if (nums.length < 2){
          return false;
      }

      Map<Integer, Integer> map = new HashMap<>();
      // NOTE!! init as below
      map.put(0, -1);

      int preSum = 0;
      for(int i = 0; i < nums.length; i++){
          int cur = nums[i];
          //  int remainder = (k != 0) ? presum % k : presum;
          preSum = k != 0 ? (preSum + cur) % k : preSum; // edge case : if k = 0
          /**
           *  sum(i,j) = presum(j+1) - presum(i)
           *
           *  -> sum(i,j) = a * k + r  - b * k + r
           *  ->
           *
           */
          if (map.containsKey(preSum)){ // TODO : double check
              if (i - map.get(preSum) >= 2){
                  return true;
              }
          }
          map.put(preSum, i);
      }

      return false;
    }

  // LC 560
  // https://leetcode.com/problems/subarray-sum-equals-k/description/
  // 3.37 - 3.47 pm
  /**
   * Given an array of integers nums and an integer k,
   * return the total number of subarrays whose sum equals to k.
   *
   * A subarray is a contiguous non-empty
   * sequence of elements within an array.
   *
   * IDEA : presum + hashmap
   */
  public int subarraySum(int[] nums, int k) {

      if (nums.length == 1){
          if (nums[0] == k){
              return 1;
          }
          return 0;
      }

      int presum = 0;
      int cnt = 0;
      /**
       *  map{ presum : cnt}
       */
      Map<Integer, Integer> map = new HashMap<>();
      // init map as below
      map.put(0,1);

      for (int i = 0; i < nums.length; i++){
          int cur = nums[i];
          presum += cur;
          /**
           *  sum(i,j) = presum(j+1) - presum(i)
           *
           *  -> presum(j+1) - presum(i) = k
           *  -> presum(i) =   presum(j+1) - k
           */
          if (map.containsKey(presum - k)){
              cnt += map.get(presum - k);
          }

          map.put(presum, map.getOrDefault(presum, 0)+1);
      }

      return cnt;
    }

  // LC 370
  // 5.03 pm - 5.20 pm
  // https://leetcode.ca/2016-12-04-370-Range-Addition/
  /**
   * IDEA : DIFF ARRAY
   *
   * EXP 1)
   * Input: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
   * Output: [-2,0,3,5,3]
   *
   *  -> arrays =
   *   [0,2,2,2,0]
   *   [0,0,3,3,3]
   *   [-2,-2,-2,0,0]
   *
   *  -> sum over all arrays
   *  -> [-2,0,3,5,3]
   *
   */
  public int[] getModifiedArray(int length, int[][] updates) {

      int[] tmp = new int[length+1]; // ?
      for (int[] x : updates){
          int start = x[0];
          int end = x[1];
          int amount = x[2];

          // add
          tmp[start] += amount;

          // subtract (remove the "adding affect" on "NEXT" element)
          if (end + 1 < length){ // ??
              tmp[end] -= amount;
          }
      }

      // prepare final result
      for(int i = 1; i < tmp.length; i++){
          tmp[i] += tmp[i-1];
      }

      return Arrays.copyOfRange(tmp, 0, length); // ????
    }

    // LC 369
    // https://leetcode.ca/all/369.html
    // 5.35 - 5.50 pm
    /**
     *  IDEA 1 : linked list -> int, +1, transform back to linked list
     *
     *  Idea 2: do in linked list directly
     *       -> reverse
     *       -> add 1, if > 9, move to next digit
     *       -> reverse again
     */
    /**
     *  IDEA 3:
     *
     *   step 1) reverse
     *   step 2) `add 1` op
     *   step 3) reverse

     */
    // V3
    public ListNode plusOne(ListNode head) {
        // edge case
        if (head == null){
            return new ListNode(1);
        }

        ListNode reversedHead = reverseNode(head);

        int extra = 0;

        while(reversedHead != null){
            int val = reversedHead.val;
            val += (1 + extra);
            // if val <= 9, no need to handle "carry", end the while loop
            if (val <= 9){
                break;
            }
            extra = val - 10;

            reversedHead = reversedHead.next;
        }

        // if extra != 0, need to carry to next digit
        if(extra > 0){
            reversedHead.next = new ListNode(extra);
        }

        ListNode res = reverseNode(reversedHead);
        return res;
    }

    private ListNode reverseNode(ListNode node){
        ListNode _prev = null;
        ListNode cur = node;

        while(cur != null){
            ListNode _next = cur.next;
            cur.next = _prev;
            _prev = cur;
            cur = _next; // NOTE this !!!
        }
        return _prev; // NOTE this !!!
    }


    // v2
//    public ListNode plusOne(ListNode head) {
//        if (head == null){
//            return new ListNode(1); // ??
//        }
//        ListNode tmp = new ListNode();
////        // reverse
////        while(head != null){
////            ListNode _next = head.next;
////            ListNode _prev = tmp; // ??
////            head.next = _prev;
////            head = _next;
////        }
////        tmp.val += 1;  // ??
//
//
//        return null;
//    }

  // v1
  //    public ListNode plusOne_1(ListNode head) {
  //      if (head == null){
  //          return new ListNode(1); // ??
  //      }
  //      List<Integer> list = new ArrayList<>();
  //      while(head != null){
  //          list.add(head.val);
  //          head = head.next;
  //      }
  //      // ???
  //      int cur = 0;
  //      for (int j = list.size(); j >= 0; j--){
  //          cur += (10 ^ j) * list.get(j);
  //      }
  //      cur += 1;
  //      ListNode res = new ListNode();
  //      String string = String.valueOf(cur);
  //      for (String x : string.split("")){
  //          ListNode tmp = new ListNode();
  //          tmp.val = Integer.parseInt(x);
  //          res.next = tmp;
  //          res = res.next;
  //      }
  //
  //      return res;
  //    }

  // LC 311
  // https://leetcode.ca/2016-10-06-311-Sparse-Matrix-Multiplication/
  // 4.39 - 4.50 pm
  /**
   * IDEA 1: ARRAY OP (brute force)
   *
   *  Input:
   *
   * A = [
   *   [ 1, 0, 0],
   *   [-1, 0, 3]
   * ]
   *
   * B = [
   *   [ 7, 0, 0 ],
   *   [ 0, 0, 0 ],
   *   [ 0, 0, 1 ]
   * ]
   *
   * Output:
   *
   *      |  1 0 0 |   | 7 0 0 |   |  7 0 0 |
   * AB = | -1 0 3 | x | 0 0 0 | = | -7 0 3 |
   *                   | 0 0 1 |
   *
   */
  public int[][] multiply(int[][] mat1, int[][] mat2) {

      // edge case
      if (mat1.length == 1 && mat1[0].length == 1 && mat2.length == 1 && mat2[0].length == 1){
          int[][] res = new int[][]{};
          res[0][0] = mat1[0][0] * mat2[0][0];
          return res;
      }

      int l_1 = mat1.length;
      int w_1 = mat1[0].length;

      int l_2 = mat2.length;
      int w_2 = mat2[0].length;

      int[][] res = new int[l_1][w_2]; // ???

      for(int i = 0; i < l_1; i++){
          int tmp = 0;
          for (int j = 0; j < w_2; j++){
              System.out.println(">>> i = " + i + ", j = " + j + ", tmp = " + tmp);
              tmp += (mat1[i][j] * mat2[j][i]);
              res[i][j] = tmp;
          }
          //res[i][j] = tmp; // ?
      }

      return res;
    }

    // LC 079
    // https://leetcode.com/problems/word-search/
    // 2.31 pm - 2.45 pm
    public boolean exist(char[][] board, String word) {

      if (board.length == 1 && board[0].length == 1){
          return String.valueOf(board[0][0]).equals(word);
      }

      // backtrack
      Set<List<Integer>> visited = new HashSet<>();
      int l = board.length;
      int w = board[0].length;
      for(int i = 0; i < l; i++){
          for(int j = 0; j < w; j++){
              StringBuilder sb = new StringBuilder();
              if(canFind(board, word, j, i, sb, visited)){
                  return true;
              }
          }
      }

      return false;
    }

    private boolean canFind(char[][] board, String word, int x, int y, StringBuilder cur, Set<List<Integer>> visited){
      int l = board.length;
      int w = board[0].length;
      int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
      if (cur.toString().length() == word.length() && cur.toString().equals(word)){
          return true;
      }
      if (cur.toString().length()  > word.length()){
          return false; // ??
      }

      for (int[] move: moves){
          int x_ = x + move[0];
          int y_ = y + move[1];
          List<Integer> tmp = new ArrayList<>();
          tmp.add(x);
          tmp.add(y);
          String tmpVal = String.valueOf(board[y_][x_]);
          if (x_ >= 0 && x_ < w && y_ >= 0 &&
                  y_ < l && !visited.contains(tmp) &&
                  tmpVal.equals(word.charAt(cur.length()+1))){
              visited.add(tmp);
              cur.append(board[y][x]); // ???
              canFind(board, word, x_, y_, cur, visited);
              // undo
              cur.deleteCharAt(cur.toString().length() - 1);
              visited.remove(tmp); // ??
          }
      }

      return false;
    }


    // LC 362
    // https://leetcode.ca/all/362.html
    // 3.38 PM - 3.50 PM
    class HitCounter {

        /**  counter : {timestamp : cnt } ??
         *
         */
        private Map<Integer, Integer> counter;

        /** Initialize your data structure here. */
        public HitCounter() {
            counter = new HashMap<>(); // ?? check
        }

        /**
         Record a hit.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public void hit(int timestamp) {
            // counter.put(timestamp, counter.getOrDefault(timestamp, 0) + 1);
            int curCnt = counter.getOrDefault(counter,0);
            //counter.putIfAbsent(timestamp, curCnt+1);
            counter.put(timestamp, curCnt+1);
        }

        /**
         Return the number of hits in the past 5 minutes.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public int getHits(int timestamp) {
            int val = 0;
            for(Integer key : counter.keySet()){
                if (key >= 0 && key <= timestamp - 60 * 5){
                    //val += 1; // ??
                    val += counter.get(key);
                }
            }
            return val;
        }
    }

  // LC 849
  // https://leetcode.com/problems/maximize-distance-to-closest-person/
  // 4.10 pm - 4.20 pm
  /**
   *
   * Alex wants to sit in the seat such that the
   * distance between him and the closest person
   * to him is maximized.
   *
   * Return that maximum distance to the closest person.
   *
   */
  /**
   *
   * Idea :
   *
   *  array collect cur distances ? (dist between "1", and "1") ???
   *  sorting the distance
   *  select the "max" distance
   *
   *
   * Exp 1:
   *
   * Input: seats = [1,0,0,0,1,0,1]
   * Output: 2
   *
   * ->
   *
   *
   */
  // 5.45 pm - 6.00 pm
  /**
   *  IDEA : 2 pointers
   *
   *    2 cases
   *   -> seat between "1"
   *      -> dist = dist(1,1) / 2
   *   -> set is NOT between "1"
   *      -> dist = dist(1, array_len)
   *
   *    and maintain the max dist within the looping
   *
   *
   *    Exp :
   *
   *    1001001
   *
   *    100100100
   *
   *    1000
   *
   *    1000010
   *
   *    10100000
   *
   */
  public int maxDistToClosest(int[] seats) {

      if (seats.length == 2){
          return 1; // ?? check
      }

      int maxDist = 0;
     // int seatedCnt = 0;
      int lastIdx = -1;
      //int closed = -1; // ???

      for(int i = 0; i < seats.length; i++){
//          if (seats[i] == 1){
//              //seatedCnt += 1;
//              //closed = closed * closed;
//          }
          if (seats[i] == 1){
              if (lastIdx == -1){
                  lastIdx = i;
              }else{
                  int dist = (i - lastIdx) / 2;
                  maxDist = Math.max(maxDist, (dist));
                  }
          }
      }

      // NOTE !!! ONLY need to check if the "last 0s" is NOT encclosed by "1"
      // -> then we NEED to treat it as 2) case
      // -> e.g. calculate dist from `last seen 1` to `last 0`
      if (seats[seats.length-1] == 0){
          //return seats.length - lastIdx;
          maxDist = Math.max(maxDist, (seats.length - lastIdx));
      }

      return maxDist;
  }
//  public int maxDistToClosest(int[] seats) {
//
//      List<Integer> distances = new ArrayList<>();
//      int lastIdx = -1;
//      for(int i = seats.length - 1; i >= 0; i--){
//          if (seats[i] == 1){
//              if (lastIdx != -1){
//                  int diff = Math.abs(i - lastIdx);
//                  distances.add(diff);
//              }
//              lastIdx = i;
//          }
//      }
//
//      System.out.println(">>> (before sort) distances = " + distances);
//      distances.sort(Integer::compareTo);
//      System.out.println(">>> (after sort) distances = " + distances);
//
//      // edge case : if only one "1"
//      if (distances.isEmpty()){
//          return seats.length-1;
//      }
//      // return the max dist
//      return distances.get(distances.size()-1) / 2; // ??
//    }

    // LC 855
    // https://leetcode.com/problems/exam-room/description/
    // 5.21 - 5.40 pm
    /**
     * Your ExamRoom object will be instantiated and called as such:
     * ExamRoom obj = new ExamRoom(n);
     * int param_1 = obj.seat();
     * obj.leave(p);
     */
    /**
     *  IDEA: PQ (min queue)
     *
     *
     */
    class ExamRoom {

        PriorityQueue<Integer> pq;
        int seated;

        public ExamRoom(int n) {
            pq = new PriorityQueue<>(); // ??
            seated = 0;
        }

        public int seat() {
            if(this.seated == 0){
                this.pq.add(0);
                return 0;
            }
            //pq.poll();
            return pq.peek(); // ???
        }

        public void leave(int p) {
            List<Integer> tmp = new ArrayList<>();
            while (this.pq.peek() != p){
                this.pq.poll();
            }
            //return this.pq.poll();
            this.pq.poll();
            // add element back to PQ
            for(int x : tmp){
                this.pq.add(x);
            }
        }
    }
//    class ExamRoom {
//
//        int seated;
//        List<Integer> seats;
//
//        public ExamRoom(int n) {
//            this.seated = 0;
//            this.seats = new ArrayList<>();
//            // ?? optimize
//            for(int i = 0; i < n; i++){
//                this.seats.add(0);
//            }
//        }
//
//        public int seat() {
//            if (this.seated == 0){
//                //this.seats. = 1;
//            }
//            return 0;
//        }
//
//        public void leave(int p) {
//
//        }
//    }

  // LC 379
  // https://leetcode.ca/all/379.html
  // 4.27 PM - 4.40 PM
  /**
   *  IDEA : HASHMAP
   *
   */
  class PhoneDirectory {

      // attr
      //int assignedCnt;
      /**
       * assignedPhone : {assigned_number: 1}
       *
       */
      Map<Integer, Integer> assignedPhone;
      int maxNumbers;
      //Random random;

      /** Initialize your data structure here
       @param maxNumbers - The maximum numbers that can be stored in the phone directory. */
      public PhoneDirectory(int maxNumbers) {
          //this.assignedCnt = 0;
          this.assignedPhone = new HashMap<>();
          this.maxNumbers = maxNumbers;
          //this.random = new Random();
      }

      /** Provide a number which is not assigned to anyone.
       @return - Return an available number. Return -1 if none is available. */
      public int get() {
          if(this.assignedPhone.isEmpty()){
              return -1;
          }
          int key = -1;
          for (int i = 0; i < this.maxNumbers; i++){
              if(!this.assignedPhone.containsKey(i)){
                  key = i;
                  break;
              }
          }
          if(key != -1){
              this.assignedPhone.put(key, 1);
          }
          return key;
      }

      /** Check if a number is available or not. */
      public boolean check(int number) {
          return this.assignedPhone.containsKey(number);
      }

      /** Recycle or release a number. */
      public void release(int number) {
          if(this.assignedPhone.containsKey(number)){
              this.assignedPhone.remove(number);
          }
      }
  }

  // LC 173
  // https://leetcode.com/problems/binary-search-tree-iterator/
  // 4.58 - 5.20 pm
  /**
   * In a binary search tree ordered such that in each node the
   * key is greater than all keys in its left subtree and
   * less than all keys in its right subtree,
   *
   * -> in-order traversal retrieves the keys in ascending sorted order.
   *
   */
  class BSTIterator {

      // attr
      TreeNode treeNode;
      List<Integer> cache;

      public BSTIterator(TreeNode root) {
          this.treeNode = root;
          this.cache = new ArrayList<>();
          this.getValues(root);
          // ordering (ascending order)
          this.cache.sort(Integer::compareTo); // ???
      }

      public int next() {
          int tmp = this.cache.get(0);
          this.cache.remove(0);
          return tmp;
      }

      public boolean hasNext() {
          return !this.cache.isEmpty();
      }

      private void getValues(TreeNode root){
          if (root == null){
              return; // ?
          }
          // pre-order traversal (root -> left -> right)
          this.cache.add(root.val);

          if (root.left != null){
              this.getValues(root.left);
          }
          if (root.right != null){
              this.getValues(root.right);
          }
      }
  }

  // LC 498
  // https://leetcode.com/problems/diagonal-traverse/
  // 4.26 pm - 4,40 pm
  /**
   *
   * Given an m x n matrix mat,
   * return an array of all the elements of the array
   * in a diagonal order.
   *
   * Exp 1
   * Input: mat = [[1,2,3],[4,5,6],[7,8,9]]
   * Output: [1,2,4,7,5,3,6,8,9]
   *
   *
   * Exp 2
   * Input: mat = [[1,2],[3,4]]
   * Output: [1,2,3,4]
   *
   */
  /**
   *  IDEA : ARRAY OP
   *
   *  Input: mat = [[1,2,3],[4,5,6],[7,8,9]]
   *
   *  -> (0,0) -> (1,0) -> (0,1) -> (2,0) -> (1,1) -> (2,0)
   *  -> (2,1)
   *
   *  ↖ ↘
   *
   *
   *  ->, ↙.
   *
   */
  public int[] findDiagonalOrder(int[][] mat) {
      // edge
      if(mat.length == 1 && mat[0].length == 1){
          return new int[]{mat[0][0]};
      }

      int l = mat.length;
      int w = mat[0].length;

      int[] res = new int[l * w];

      int[][] rightUpMove = new int[][]{{1,1}};
      int[][] leftDownMove = new int[][]{{-1,1}};


      return res;
  }

  // LC 934
  // https://leetcode.com/problems/shortest-bridge/
  // 5.04 - 5.20 PM
  /**
   *
   *  An island is a 4-directionally connected group of 1's
   *  not connected to any other 1's.
   *  `There are exactly two islands in grid.`
   *
   *
   * Return the smallest number
   * of 0's you must flip to connect the two islands.
   *
   *
   *  1 represents land and 0 represents water.
   *
   *
   *  Exp 1
   *
   *  Input: grid = [[0,1],
   *                 [1,0]]
   *  Output: 1
   *
   *
   *  EXP 2
   *
   *  Input: grid = [[0,1,0],
   *                [0,0,0],
   *                [0,0,1]]
   *
   *
   *  Output: 2
   *
   *
   *  EXP 3
   *
   *
   *  Input: grid = [[1,1,1,1,1],
   *                [1,0,0,0,1],
   *                [1,0,1,0,1],
   *                [1,0,0,0,1],
   *                [1,1,1,1,1]]
   *
   *  Output: 1
   */
  /**
   *  IDEA: BFS (??
   *
   *
   *  step 1) get the coordination of 2 islands ("1" collections split by "0")
   *  step 2) start from "smaller" island, find the min dist (get the "flip cnt" as well) between it and the other island (??
   *
   */
  public int shortestBridge(int[][] grid) {

      // edge
      if(grid.length == 2 && grid[0].length == 2){
          return 1;
      }

      // get small, big island coordination
      List<List<Integer>> smallIsland = new ArrayList<>();
      List<List<Integer>> bigIsland = new ArrayList<>();

      Set<List<Integer>> visited = new HashSet<>();

      int l = grid.length;
      int w = grid[0].length;

      for(int i = 0; i < l; i++){
          for(int j = 0; j < w; j++){
              getIsland(grid, j, i, smallIsland, visited);
          }
      }

      // bfs

      return 0;
    }

    private List<List<Integer>> getIsland(int[][] grid, int x, int y, List<List<Integer>> cur, Set<List<Integer>> visited){
      if(grid.length == 0 || grid[0].length == 0){
          return null;
      }
      int l = grid.length;
      int w = grid[0].length;

      int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };

      if(grid[y][w] == 1){
          List<Integer> tmp = new ArrayList<>();
          tmp.add(x);
          tmp.add(y);
          cur.add(tmp);
      }

      for(int[] move: moves){
          int x_ = x + move[0];
          int y_ = y + move[1];
          List<Integer> tmp2 = new ArrayList<>();
          tmp2.add(x_);
          tmp2.add(y_);
          if(x_ >= 0 && x_ <= w && y_ >= 0 && y_ <= l && !visited.contains(tmp2)){
              visited.add(tmp2);
              getIsland(grid, x_, y_, cur, visited);
          }
      }

      return cur; // ?
    }

    // LC 769
    // https://leetcode.com/problems/max-chunks-to-make-sorted/
    // 1.24 pm - 1.35 pm
    /**
     *
     * We split arr into some number of chunks (i.e., partitions),
     * and individually sort each chunk. After concatenating them,
     * the result should equal the sorted array.
     *
     *
     * Return the largest number of chunks we can make to sort the array.
     *
     */
    /**
     *  IDEA 1) 2 POINTERS ?
     *
     *
     *  exp 1: arr = [4,3,2,1,0] -> res = 1
     *
     *  -> sorted arr = [0,1,2,3,4]
     *
     *  -> compare before VS after
     *  ->  [4,3,2,1,0] VS [0,1,2,3,4]
     *
     *  -> [4,3,2,1,0]
     *      i j j j j
     *
     *
     *  exp 2: arr = [1,0,2,3,4] -> res = 4
     *
     *  -> sorted arr = [0,1,2,3,4]
     *
     *  -> compare before VS after
     *
     *  -> [1,0,2,3,4]
     *      i j
     *
     *
     *
     *
     *
     *
     */
    /**
     *  IDEA V2: pre-fix sum
     *
     *  -> to check if the array can be split into sub array and sorted,
     *     then finally can concatenate and as same as the original array
     *
     *   Exp 1: arr = [4,3,2,1,0] -> res = 1
     *
     *   -> sorted arr = [0,1,2,3,4]
     *
     *   -> arrList = [4,3,2,1,0]
     *   -> preFixSum = [4,7,9,10,10]
     *
     *
     *
     *   Exp 2:
     *
     *    arr = [1,0,2,3,4] -> res = 4
     *
     *    -> sorted arr = [0,1,2,3,4]
     *
     *    [1,0,2,3,4]
     *
     *  ->  arrList =   [1,0,2,3,4]
     *  ->  preFixSum = [1,0,3,6,10]
     *
     *
     *
     *
     */
    public int maxChunksToSorted(int[] arr) {

      // edge
      if (arr.length == 1) {
          return 1;
      }

      List<Integer> preFixSortedSum = new ArrayList<>();
      List<Integer> arrList = new ArrayList<>(); // ??

      int preSortedSum = 0;
      int preSum = 0;

      // prefix sum
      int cnt = 0;
      for(int i = 0; i < arr.length; i++){
          preSum += i;
          preSortedSum += arr[i];

          if(preSum != preSortedSum){
              cnt += 1;
          }else{
              arrList.add(arr[i]);
              preFixSortedSum.add(preSum);
          }
      }

      return cnt;
    }


//    public int maxChunksToSorted(int[] arr) {
//
//      // edge
//      if (arr.length == 1) {
//          return 1;
//      }
//
//      // copy
//      int[] arr_before = arr; // ??
//      Arrays.sort(arr);
//
//      // 2 pointers
//      int res = 1;
//      List<Integer> tmp = new ArrayList<>();
//      for (int i = 0; i < arr.length-1; i++){
//          for(int j = i+1; j < arr_before.length; j++){
//              tmp.add(arr[j]);
//              // copy
//              List<Integer> tmp_copy = tmp; // ???
//              // sort
//              tmp_copy.sort(Integer::compareTo);
////              if(tmp_copy  == tmp_copy){
////
////              }
//          }
//      }
//
//      String x = "abc";
//      String[] x_ = x.split("");
//
//
//
//      return res;
//    }

    // LC 817
    // https://leetcode.com/problems/linked-list-components/
    // 1.10 pm - 1.20 pm
    public int numComponents(ListNode head, int[] nums) {

        // edge
        if (head.next == null && nums.length == 1){
            if(head.val == nums[0]){
                return 1;
            }
            return 0;
        }

        Set<Integer> set = new HashSet<>();
        for(int x: nums){
            set.add(x);
        }

        int cnt = 0;
        boolean pvevInSet = false;

        while(head != null){

            if(!set.contains(head.val)){
                if(pvevInSet){
                    cnt += 1;
                }
                pvevInSet = false;
            }else{
                pvevInSet = true;
            }

            head = head.next;
        }

        return pvevInSet ? cnt+1 : cnt;
    }


//    public int numComponents(ListNode head, int[] nums) {
//
//        // edge
//        if (head.next == null && nums.length == 1){
//            if(head.val == nums[0]){
//                return 1;
//            }
//            return 0;
//        }
//
//        // ListNode -> list
//        List<Integer> head_list = new ArrayList<>();
//        List<Integer> nums_list = new ArrayList<>();
//
//        for(int i = 0; i < nums.length; i++){
//            nums_list.add(nums[i]);
//        }
//
//        // sort nums_list
//        nums_list.sort(Integer::compareTo);
//
//        while(head != null){
//            head_list.add(head.val);
//            head = head.next;
//        }
//
//        System.out.println(">>> nums_list = " + nums_list);
//        System.out.println(">>> head_list = " + head_list);
//
//        int cnt = 0;
//        boolean prevInNums = false;
//        // 2 pointers
//        for(int i = 0; i < head_list.size()-1; i++){
//            List<Integer> tmp = new ArrayList<>();
//            for(int j = i; j < head_list.size(); j++){
//                tmp.add(head_list.get(j));
//                if(!nums_list.contains(tmp)){
//                    if (!tmp.isEmpty()){
//                        cnt += 1;
//                    }
//                    break;
//                }
//            }
//        }
//
//        if(prevInNums){
//            cnt += 1;
//        }
//
//        return cnt;
//    }

    // LC 729
    // https://leetcode.com/problems/my-calendar-i/
    // 2.04 PM - 2.24 PM
    /**
     * The event can be represented as a pair of integers startTime and endTime that represents
     * a booking on the half-open interval [startTime, endTime),
     * the range of real numbers x such that startTime <= x < endTime.
     *
     *
     *  IDEA 1:
     *
     *    use a linear data structure that can save SORTED
     *    intervals ([startTime, endTime)),
     *    so it enable to check if the new interval overlap with
     *    the existing intervals or not
     *
     *
     *  IDEA 2: use an array, sort everytime when a new interval is added
     *
     *
     *
     */
    class MyCalendar {

        List<List<Integer>> booked;

        public MyCalendar() {
            this.booked = new ArrayList<>();
        }

        public boolean book(int startTime, int endTime) {

            List<Integer> tmp = new ArrayList<>();
            tmp.add(startTime);
            tmp.add(endTime);

            if(this.booked.isEmpty()){
                this.booked.add(tmp);
                // ??? TODO : check
               // sort(this.booked);
                return true;
            }

            for(List<Integer> x: booked){
                int existingStart = x.get(0);
                int existingEnd = x.get(1);
                /**
                 *   |----|
                 *     |------| (old)
                 *
                 *     or
                 *
                 *    |-----|
                 *  |----|  (old)
                 *
                 *    or
                 *
                 *    |---|
                 *  |----------|  (old)
                 *
                 *
                 */
//                if ( endTime > existingStart || startTime < existingEnd ){
//                    return false;
//                }
                if (startTime < existingEnd && existingStart < endTime) {
                    return false; // Overlapping interval found
                }
            }

            this.booked.add(tmp);
            //sort(this.booked);

            return true;
        }

        private void sort(List<List<Integer>> bookedDays){
            // descending sorting on 1) 1st element, if same, sort on 2nd element
            bookedDays.sort((x,y) -> {
                if(x.get(0) < y.get(0)){
                    return 1;
                }else if(x.get(0) > y.get(0)){
                    return -1;
                }else if (x.get(0) == y.get(0)){
                   if(x.get(1) < y.get(1)){
                       return 1;
                   }else if (x.get(1) > y.get(1)){
                       return -1;
                   }
                   return 0; // ???
                }
                return 0;
            });
        }

    }

  // LC 731
  // https://leetcode.com/problems/my-calendar-ii/
  // 5.20 - 6.35 pm
  /**
   * A triple booking happens when three events have some
   * non-empty intersection (i.e., some moment
   * is common to all the three events.).
   *
   * <p>The event can be represented as a pair of integers startTime and endTime that represents a
   * booking on the half-open interval [startTime, endTime), the range of real numbers x such that
   * startTime <= x < endTime.
   *
   * <p>IDEA: SCANNING LINE
   */
  class MyCalendarTwo {

    // attr
    // int curBooked;
    /**
     * statusList : { time, status, curBooked}
     *  time: starttime or endtime
     *  status: 0: not booked, 1: booked
     *  curBooked: 1,2,3 ...
     */
    List<Integer[]> statusList;

      //List<Integer> cntList;

      // constructore
      public MyCalendarTwo() {
          // this.curBooked = 0;
          this.statusList = new ArrayList<>();
          //this.cntList = new ArrayList<>();
      }

      public boolean book(int startTime, int endTime) {

          Integer[] cur1 = new Integer[3];
          cur1[0] = startTime;
          cur1[1] = 1;
          //cur1[2] = 1;

          Integer[] cur2 = new Integer[3];
          cur2[0] = endTime;
          cur2[1] = 0;
          //cur2[2] = 1;

          if (this.statusList.isEmpty()) {

              cur1[2] = 1;
              cur2[2] = 1;

              this.statusList.add(cur1);
              this.statusList.add(cur2);
              return true;
          }

          // scanning line
          // re-order
          System.out.println(">>> before sort : " + this.statusList);
          for(Integer[] x : this.statusList){
              System.out.println(">>> time = " + x[0] + ", open/close = " + x[1] + ", cnt = " + x[2]);
          }

          this.statusList.sort((x, y) -> {
              if (x[0] > y[0]) {
                  return 1;
              } else if (x[0] < y[0]) {
                  return -1;
              }
              return 0;
          });

          System.out.println(">>> after sort : " + this.statusList);
          for(Integer[] x : this.statusList){
              System.out.println(">>> time = " + x[0] + ", open/close = " + x[1] + ", cnt = " + x[2]);
          }

          //int curCnt = 0;
          for (Integer[] x : this.statusList) {
              int time = x[0];
              int openClose = x[1];
              int cnt = x[2];
              //int timeCnt = this.cntList.get(time);
              if (openClose == 0) {
                  cnt -= 1;
                  cur2[2] = cnt;
              } else {
                  cnt += 1;
                  cur1[2] = cnt;
                  if (cnt >= 3) {
                      return false;
                  }
              }


              this.statusList.add(cur1);
              this.statusList.add(cur2);
          }

          return true;
      }
  }

  //    class MyCalendarTwo {
  //
  //        List<List<Integer>> booked;
  //        Map<List<Integer>, Integer> overlapCnt;
  //
  //        public MyCalendarTwo() {
  //            this.booked = new ArrayList<>();
  //            this.overlapCnt = new HashMap<>();
  //        }
  //
  //        public boolean book(int startTime, int endTime) {
  //
  //            List<Integer> tmp = new ArrayList<>();
  //            tmp.add(startTime);
  //            tmp.add(endTime);
  //
  //            // case 1) booked is empty
  //            if(this.booked.isEmpty()){
  //                this.booked.add(tmp);
  //                return true;
  //            }
  //
  //            boolean lessEqualsThreeOverlap = false;
  //
  //
  //            for(List<Integer> x: this.booked){
  //                /**
  //                 *   |----|
  //                 *     |------| (old)
  //                 *
  //                 *     or
  //                 *
  //                 *    |-----|
  //                 *  |----|  (old)
  //                 *
  //                 *    or
  //                 *
  //                 *    |---|
  //                 *  |----------|  (old)
  //                 *
  //                 *
  //                 */
  //                int existingStart = x.get(0);
  //                int existingEnd = x.get(1);
  //
  //                if (startTime < existingEnd && existingStart < endTime) {
  //                    // case 2) has overlap, but `overlap count` <= 3
  //                    List<Integer> tmpExisting = new ArrayList<>();
  //                    tmpExisting.add(existingStart);
  //                    tmpExisting.add(existingEnd);
  //                    if(this.overlapCnt.get(tmpExisting) <= 3){
  //                        // update existing start, end
  //                        existingStart = Math.min(existingStart, startTime);
  //                        existingEnd  = Math.max(existingEnd, endTime);
  //                        List<Integer> tmp2 = new ArrayList<>();
  //                        tmp2.add(existingStart);
  //                        tmp2.add(existingEnd);
  //                        this.overlapCnt.put(tmp2, this.overlapCnt.get(tmpExisting)+1); // update
  // overlap cnt
  //                        this.overlapCnt.remove(tmpExisting);
  //                        return true;
  //                    }else{
  //                        // case 3) has overlap, and `overlap count` > 3
  //                        return false;
  //                    }
  //                }
  //
  //            }
  //
  //            // case 4) no overlap
  //            this.booked.add(tmp);
  //            this.overlapCnt.put(tmp, 1); // update overlap cnt
  //            return true;
  //        }
  //    }

  // LC 1031
  // https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/
  // 4.21 - 4.40 pm
  /**
   * return the maximum sum of elements in two non-overlapping subarrays with lengths firstLen and
   * secondLen.
   *
   * <p>The array with length firstLen could occur before or after the array with length secondLen,
   * but they have to be non-overlapping.
   *
   * <p>A subarray is a contiguous part of an array.
   *
   * <p>IDEA: PREFIX SUM
   *
   * <p>exp 1)
   *
   * <p>Input: nums = [3,8,1,3,2,1,8,9,0], firstLen = 3, secondLen = 2
   *
   * <p>-> prefix sum = [3,11,12,15,17,18,26,37,0]
   *
   * <p>Sum(i,j) = preSum(j+1) - preSum(i)
   *
   * <p>-> pre(15) - pre(3) = 12
   */
  public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {

        // edge
        if(nums.length == 3){
            return firstLen + secondLen;
        }

        // prefix Sum
        List<Integer> prefixSum = new ArrayList<>();
        prefixSum.add(0);
        int curSum = 0;
        for(int i = 0; i < nums.length; i++){
            curSum += nums[i];
            prefixSum.add(curSum);
        }

        int longerLen = 0;
        int len = 0;
        if(firstLen > secondLen){
            longerLen = firstLen;
            len = secondLen;
        }else{
            len = firstLen;
            longerLen = secondLen;
        }

        int firstLenVal = 0;
        int secondLenVal = 0;

       // Set<Integer> seletectedIdx = new HashSet<>();
        int seletectedIdx = -1;

        // get "max" val of firstLen
        for(int i = firstLen; i < nums.length - firstLen; i++){ // ???
            int cur = prefixSum.get(i+1) - prefixSum.get(i - firstLen);
            if (firstLenVal == 0){
                seletectedIdx = i;
                firstLenVal = cur;
            }else if (cur > firstLenVal){
                seletectedIdx = i;
                firstLenVal = cur;
            }
        }

        // get "max" val of secondLen
        for(int i = secondLen; i < nums.length - secondLen; i++){ // ???
            if (i != seletectedIdx){
                int cur = prefixSum.get(i+1) - prefixSum.get(i - secondLen);
                secondLenVal = Math.max(secondLenVal, cur);
            }
        }

        System.out.println(">>> firstLenVal = " + firstLenVal);
        System.out.println(">>> secondLenVal = " + secondLenVal);

        return firstLenVal + secondLenVal;
    }

  // LC 399
  // https://leetcode.com/problems/evaluate-division/
  // 6.34 - 6.50
  /**
   *
   * Exp 1)
   *
   *
   * Input: equations = [["a","b"],["b","c"]],
   * values = [2.0,3.0],
   * queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
   *
   *
   * Output: [6.00000,0.50000,-1.00000,1.00000,-1.00000]
   * Explanation:
   * Given: a / b = 2.0, b / c = 3.0
   * queries are: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
   * return: [6.0, 0.5, -1.0, 1.0, -1.0 ]
   * note: x is undefined => -1.0
   *
   *
   */
  /**
   *  IDEA: DFS
   *
   *  ->
   *   step 0) define custom class to store below relation
   *   step 1) build relation
   *        -> {a: [b, 2], b:[c, 3], c:[b, 1/3] }
   *   step 2) check if every input in relation,
   *          if not, return -1
   *          if yes, do the op, return res
   */

  public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {

      // edge
      if(equations.isEmpty() || values.length == 0){
          return null;
      }

      double[] res = new double[queries.size()]; // ?

      //  HashMap<String, HashMap<String, Double>> !!!!
      // Map<String, List<Map<String, Double>>> graph = new HashMap<>();
      Map<String, Map<String, Double>> graph = new HashMap<>();
      for(int i = 0; i < equations.size(); i++){
          List<String> eq =  equations.get(i);
          String firstVal = eq.get(0);
          String secondVal = eq.get(1);

          graph.putIfAbsent(firstVal, new HashMap<>());
          graph.putIfAbsent(secondVal, new HashMap<>());


          Map<String, Double> cur = graph.get(firstVal);
          cur.put(secondVal, values[i]);

          Map<String, Double> cur2 = graph.get(firstVal);
          cur2.put(secondVal, 1 / values[i]); // ???

          graph.get(firstVal).put(firstVal, values[i]); // ??
          graph.get(secondVal).put(secondVal, 1 / values[i]); // ??

          //graph.get(1).put(1, 2);
      }

      // check if element in graph
      for(int i = 0; i < equations.size(); i++){
          List<String> q =  equations.get(i);
          String firstVal = q.get(0);
          String secondVal = q.get(1);
          if (!graph.containsKey(firstVal) || !graph.containsKey(secondVal)){
              res[i] = -1.0;
          }else if (firstVal.equals(secondVal)){
              res[i] = 1.0; // ??
          }
          else{
              // dfs call
              double cur = 1.0;
              Set<String> visited = new HashSet<>();
              res[i] = cur;
          }

      }

      return res;
  }

  private double dfsCal(Map<String, Map<String, Double>> graph, List<String> query, double cur, Set<String> visited){

      String firstVal = query.get(0);
      String secondVal = query.get(1);

      if (firstVal.equals(secondVal)){
          return 1.0;
      }

      if(graph.containsKey(firstVal) && graph.get(firstVal).containsKey(secondVal)){
          cur = cur * graph.get(firstVal).get(secondVal);
          //this.dfsCal(graph, query, cur, visited);
          return cur;
      }

      if (graph.containsKey(firstVal) && !graph.get(firstVal).containsKey(secondVal)){
//          for (String x: graph.get(firstVal)){
//
//          }
      }

      return cur;
  }

//  private class EquationRes{
//      // attr
//      String variable;
//      Double result;
//
//      public Double getResult() {
//          return result;
//      }
//
//      public String getVariable() {
//          return variable;
//      }
//
//      // constructor
//      EquationRes(String variable, Double result){
//          this.variable = variable;
//          this.result = result;
//      }
//  }
//
//    // init relation
//    Map<String, List<EquationRes>> relations = new HashMap();
//    //double[] res = new double[];
//
//  public double[] calcEquation(
//
//    List<List<String>> equations, double[] values, List<List<String>> queries) {
//
//      // build
//      buildRelation(equations, values);
//      // get
//      double[] res = new double[queries.size()];
//      for(int i = 0; i < queries.size(); i++){
//          res[i] = getResult(queries.get(i), 1);
//      }
//
//      System.out.println(">>> res = " + res);
//
//      return res;
//    }
//
//    // dfs
//    private double getResult(List<String> queries, double res){
//      // check if in list
//      String firstVal = queries.get(0);
//      String secondVal = queries.get(1);
//      if (!this.relations.containsKey(firstVal) || !this.relations.containsKey(secondVal)){
//          return -1.0;
//      }
//
//      //double res = 1;
//      //List<EquationRes> x = this.relations.get(firstVal);
//      for(EquationRes equationRes: this.relations.get(firstVal)){
//          res = res * equationRes.result;
//
//
//      }
//
//      return res;
//    }
//
//    // build relation
//    private void buildRelation(List<List<String>> equations, double[] values){
//      for(int i = 0; i < equations.size(); i++){
//          List<String> equation = equations.get(i);
//          String firstVal = equation.get(0);
//          String secondVal = equation.get(1);
//
//          EquationRes equationRes = new EquationRes(secondVal, values[i]);
//
//          List<EquationRes> equationAndRes = new ArrayList<>();
//          if (this.relations.containsKey(firstVal)){
//              equationAndRes =  this.relations.get(firstVal);
//          }
//
//          this.relations.put(firstVal, equationAndRes);
//      }
//
//    }

  // LC 1091
  // 7.09 am - 7.15 am
  // https://leetcode.com/problems/shortest-path-in-binary-matrix/
  /**
   * Given an n x n binary matrix grid,
   * return the length of the `shortest clear` path in the matrix.
   *
   * If there is no clear path, return -1.
   *
   * A clear path in a binary matrix is a path from the
   * top-left cell (i.e., (0, 0)) to the bottom-right cell (i.e., (n - 1, n - 1))
   *
   * such that:
   *
   * All the visited cells of the path are 0.
   * All the adjacent cells of the path are 8-directionally connected (i.e., they are different and they share an edge or a corner).
   * The length of a clear path is the number of visited cells of this path.
   *
   *
   */
  /**
   *  IDEA: BFS
   *
   *
   *
   */
  public int shortestPathBinaryMatrix(int[][] grid) {
      // edge
      if (grid.length == 1 && grid[0].length == 1) {
          if (grid[0][0] == 0) {
              return 1;
          }
          return -1;
      }

      //int res = -1;

      int[][] dirs = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { -1, -1 }, { -1, 1 }, { 1, 1 },
              { 1, -1 } };
      /**
       * Queue : {x, y, path_len}
       */
      Queue<List<Integer>> queue = new LinkedList<>();
      Set<List<Integer>> seen = new HashSet<>();
      int l = grid.length;
      int w = grid[0].length;

      // init
      queue.add(Arrays.asList(0, 0, 1));
      seen.add(Arrays.asList(0, 0));

      List<Integer> candidate = new ArrayList<>();

      // bfs
      while (!queue.isEmpty()) {
          List<Integer> tmp = queue.poll();
          int x = tmp.get(0);
          int y = tmp.get(1);
          int pathLen = tmp.get(2);
          // res = pathLen;
          for (int[] dir : dirs) {
              int x_ = x + dir[0];
              int y_ = y + dir[1];

              if (x_ == w - 1 && y_ == l - 1) {
                  candidate.add(pathLen+1);
                  break;
              }

              List<Integer> tmp2 = Arrays.asList(x_, y_);
              seen.add(tmp2);

              System.out.println(">>> x_ = " + x_ + ", y_ = " + y_);

              if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && !seen.contains(tmp2) && grid[y_][x_] == 0) {
                  queue.add(Arrays.asList(x_, y_, pathLen+1));
              }else{
                  // undo add
                  seen.remove(tmp2);
              }

          }
      }

      System.out.println(">>> candidate = " + candidate);
      System.out.println(">>> candidate.isEmpty() = " + candidate.isEmpty());

      if(candidate.isEmpty()){
          return -1;
      }

      int res = Integer.MAX_VALUE;
      if (candidate.size() > 1) {
          for(int x: candidate){
              res = Math.min(res, x);
          }
      }
      return res;
  }

//  public int shortestPathBinaryMatrix(int[][] grid) {
//
//      int l = grid.length;
//      int w = grid[0].length;
//
//      // edge
//      if (l == 1 && w == 1){
//          return 1;
//      }
//
//      // bfs
//      int res = 0;
//      int[][] dirs = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0}, {-1,-1}, {-1,1} , {1,1}, {1,-1} };
//      /**
//       *  Queue : {x, y, path_len}
//       */
//      Queue<List<Integer>> queue = new LinkedList<>();
//      //int[] tmp = new int[]{0};
//      //int[][] tmp = new int[][]{ {0}, {0} };
//      List<Integer> tmp = new ArrayList<>();
//      tmp.add(0);
//      tmp.add(0);
//      tmp.add(0); // path len
//
//      Set<List<Integer>> visited = new HashSet<>();
//
//      //queue
//      queue.add(tmp);
//
//      int curLen = 0;
//
//      while(!queue.isEmpty()){
//          List<Integer> cur = queue.poll();
//          int x = cur.get(0);
//          int y = cur.get(1);
//          int dist = cur.get(2);
//          for (int[] dir: dirs){
//              int x_ = x + dir[0];
//              int y_ = y + dir[1];
//              List<Integer> tmp2 = new ArrayList<>();
//              tmp2.add(x_);
//              tmp2.add(y_);
//
//              if (x_ == w-1 && y_ == l-1){
//                  curLen = dist;
//                  break;
//              }
//
//
//              if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l &&  !visited.contains(tmp2) && grid[y_][x_] == 0){
//                  dist += 1;
//                  tmp2.add(2, dist);
//                  queue.add(tmp2);
//                  visited.add(tmp2);
//              }
//          }
//      }
//
//
//      return curLen != 0 ? res : -1;
//    }

    // LC 247
    // https://leetcode.ca/2016-08-03-247-Strobogrammatic-Number-II/
    // 9.57 pm - 10:15 pm
    /**
     *  strobogrammatic number is a number that looks the same when rotated 180 degrees (looked at upside down).
     *
     *  ->  Find all strobogrammatic numbers that are of `length = n.`
     *
     *  -> exp 1: n=2, ["11","69","88","96"]
     *  -> exp 2: n=1, ["0","1","8"]
     *  -> exp 3: n=3, ["101","111","181","609","619","689","808","818","888","906","916","986"]
     *  -> exp 4 : n=4, ["1001","1111","1691","1881","1961","6009","6119","6699","6889","6969","8008","8118","8698","8888","8968","9006","9116","9696","9886","9966"]
     *  ...
     *
     */
    /**
     *  IDEA 1) DFS
     *
     *  -> for n = n
     *   -> check 0 -> 9, if is strobogrammatic number, add to list
     *   -> repeat ...
     *
     *
     */
    List<String> res = new ArrayList<>();

    public List<String> findStrobogrammatic_1_1(int n) {
        for(int i = 0; i < n; i++){
            HashSet<String> cache = new HashSet<>();
            StringBuilder sb = new StringBuilder();
            findNumbers(n, sb, cache);
        }
        return res;
    }

    private void findNumbers(int n, StringBuilder sb, Set<String> cache){

        String[] digits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String str = sb.toString();
        if (str.length() == n){
            if (isStrobogrammatic(str)){
                if(!cache.contains(str)){
                    cache.add(str);
                    res.add(str);
                }
            }
            return;
        }

        if (str.length() > n){
            return;
        }

        for (String x: digits){
            findNumbers(n, sb.append(x), cache);
        }
    }

    private boolean isStrobogrammatic(String x){
        Map<String, String> symmertricMapping = new HashMap<>();
        symmertricMapping.put("0", "0");
        symmertricMapping.put("1", "1");
        symmertricMapping.put("8", "8");
        symmertricMapping.put("6", "9");
        symmertricMapping.put("9", "6");
        int l = 0;
        int r = x.length() - 1;
        while (r > l){
//            if (x.charAt(l) != x.charAt(r)){
//                return false;
//            }
            String left = String.valueOf(x.charAt(l));
            String right = String.valueOf(x.charAt(r));
            if (!symmertricMapping.get(left).equals(right)){
                return false;
            }
            r -= 1;
            l += 1;
        }

        return true;
    }

    // LC 852
    // https://leetcode.com/problems/peak-index-in-a-mountain-array/
    /**
     * You are given an integer mountain array arr
     * of length n where the values increase to a peak element and then decrease.
     *
     * Return the index of the peak element.
     *
     * Your task is to solve it in O(log(n)) time complexity.
     *
     *
     */
    /**
     *
     *
     *  Example 1:
     *
     * Input: arr = [0,1,0]
     *
     * Output: 1
     *
     * Example 2:
     *
     * Input: arr = [0,2,1,0]
     *
     * Output: 1
     *
     * Example 3:
     *
     * Input: arr = [0,10,5,2]
     *
     * Output: 1
     *
     * Exp 1 : [0,3,2,1,0] -> 1
     *
     * IDEA: BINARY SEARCH
     *
     * -> if there is a peak at index i
     * then 0 - i-1 MUST be increasing
     *
     */
    public int peakIndexInMountainArray(int[] arr) {

        // edge
        int maxIdx = -1;
        int maxVal = -1;
        if (arr.length == 3){
            for(int i = 0; i < arr.length; i++){
                if(arr[i] > maxVal){
                    maxVal = arr[i];
                    maxIdx = i;
                }
            }
            return maxIdx;
        }

        // binary search
        int l = 0;
        int r = arr.length - 1;
        int mid = (l + r) / 2;
        while (r >= l && r >= 0){

            mid = (l + r) / 2;

            // case 1)  cur > left and cur > right (find peak)
            if (arr[mid] > arr[mid-1] && arr[mid] > arr[mid+1]){
                return mid;
            }
            // Exp 1 : [0,0,0, 3,2,1,0] -> 1
            // case 2) cur < left && cur < left most (left is increasing order)
            else if (arr[mid] >= arr[mid-1] && arr[mid] >= arr[l]){
                l = mid + 1;
            }
            // case 3) cur < right and cur > right most (right is decreasing order ?)
            else if  (arr[mid] >= arr[mid+1] && arr[mid] >= arr[r]){
                r = mid - 1;
        }
    }

        return mid;
    }

    // LC 692
    /**
     * Given an array of strings words and an integer k, return the k most frequent strings.
     *
     * Return the answer sorted by the frequency from highest to lowest. Sort the words with the same frequency by their lexicographical order.
     *
     *
     *
     * Example 1:
     *
     * Input: words = ["i","love","leetcode","i","love","coding"], k = 2
     * Output: ["i","love"]
     * Explanation: "i" and "love" are the two most frequent words.
     * Note that "i" comes before "love" due to a lower alphabetical order.
     * Example 2:
     *
     * Input: words = ["the","day","is","sunny","the","the","the","sunny","is","is"], k = 4
     * Output: ["the","is","sunny","day"]
     * Explanation: "the", "is", "sunny" and "day" are the four most frequent words, with the number of occurrence being 4, 3, 2 and 1 respectively.
     *
     *
     */
    /**
     *  IDEA 1) treeMap ?? (map with sorting feature)
     *  IDEA 2) map + sorting with val, and get key
     *  IDEA 3) map + PQ ??
     *
     *
     *  -> use IDEA 2) first
     */

    // IDEA: sort on map key and val
    public List<String> topKFrequent(String[] words, int k) {
        // edge
        if (words == null || words.length == 0){
            return new ArrayList<>();
        }
        if (words.length == 1 && k == 1){
            List<String> res = new ArrayList<>();
            res.add(words[0]);
            return res;
        }

        // get map
        Map<String, Integer> map = new HashMap<>();
        for(String x: words){
            int cur = map.getOrDefault(x, 0);
            map.put(x, cur+1);
        }

        // get keys
        List<String> keyList = new ArrayList<>(map.keySet());

        /** sort map
         *
         *   1) sort on val (decreasing order)
         *   2) if same val, sort on key (lexicographical order)
         */

//        Collections.sort(keyList, new Comparator<List<String>>(){
//
//            @Override
//            public int compare(List<String> o1, List<String> o2) {
//                return 0;
//            }
//        })

        Collections.sort(keyList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = map.get(o2) - map.get(o1);
                if (diff == 0){
                   // return o1.length() - o2.length(); ???
                   return o1.compareTo(o2);
                }
                return diff;
            }
        });


      List<String> res = new ArrayList<>();
//      while (k >= 0){
//          res.add();
//      }
      return null;
    }

//    public List<String> topKFrequent(String[] words, int k) {
//
//        // IDEA: map sorting
//        HashMap<String, Integer> freq = new HashMap<>();
//        for (int i = 0; i < words.length; i++) {
//            freq.put(words[i], freq.getOrDefault(words[i], 0) + 1);
//        }
//        List<String> res = new ArrayList(freq.keySet());
//
//        /**
//         * NOTE !!!
//         *
//         *  we directly sort over map's keySet
//         *  (with the data val, key that read from map)
//         *
//         *
//         *  example:
//         *
//         *          Collections.sort(res,
//         *                 (w1, w2) -> freq.get(w1).equals(freq.get(w2)) ? w1.compareTo(w2) : freq.get(w2) - freq.get(w1));
//         */
//        Collections.sort(res, (x, y) -> {
//            int valDiff = freq.get(y) - freq.get(x); // sort on `value` bigger number first (decreasing order)
//            if (valDiff == 0){
//                // Sort on `key length` short key first (increasing order)
//                //return y.length() - x.length(); // ?
//                return x.compareTo(y);
//            }
//            return valDiff;
//        });
//
//        // get top K result
//        return res.subList(0, k);
//    }


//    public List<String> topKFrequent(String[] words, int k) {
//
//        // edge
//        if (words.length == 0) {
//            return new ArrayList<>();
//        }
//
//        List<String> res = new ArrayList<>();
//        Map<String, Integer> map = new HashMap<>();
//        // biggest queue
//        // Queue<Integer> cntQueue = new LinkedList<>();
//
//        for (String x : words) {
//
//            map.putIfAbsent(x, 1);
//            Integer cur = map.get(x);
//            map.put(x, cur + 1);
//        }
//
//        System.out.println(">>> (before sort) map = " + map);
//
//        // sort map by value and key lenth
//        // Convert the map to a list of entries
//        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
//
//        // Sort the entry list
//        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
//                // First, compare by value in decreasing order
//                int valueCompare = entry2.getValue().compareTo(entry1.getValue());
//
//                // If values are equal, compare by key length in increasing order
//                if (valueCompare == 0) {
//                    return entry1.getKey().length() - entry2.getKey().length();
//                }
//
//                return valueCompare;
//            }
//        });
//
//        System.out.println(">>> (after sort) map = " + map);
//        for (Map.Entry<String, Integer> x : entryList) {
//            if (k == 0){
//                break;
//            }
//            res.add((String) x.getKey());
//            k -= 1;
//        }
//
//        return res;
//    }

//    public List<String> topKFrequent(String[] words, int k) {
//
//        // edge
//        if (words.length == 0){
//            return new ArrayList<>();
//        }
//
//        List<String> res = new ArrayList<>();
//        Map<String, Integer> map = new HashMap<>();
//        // biggest queue
//        Queue<Integer> cntQueue = new LinkedList<>();
//
//        for(String x : words){
//
//            map.putIfAbsent(x, 1);
//            Integer cur = map.get(x);
//            map.put(x, cur+1);
//        }
//
//        // save top K values in queue
//        for(Integer val: map.values()){
//            if(cntQueue.size() > k){
//                cntQueue.poll();
//            }
//            cntQueue.add(val);
//        }
//
//        // prepare result
//        // {"the": 1, "appl" : 2, "cos" : 3}  ...
//        //List<>
//        while(!cntQueue.isEmpty()){
//           // res.add(cntQueue.poll());
//        }
//
//        return res;
//    }

    // LC 752
    /**
     * Given a target representing the value of the wheels that will unlock the lock,
     *
     *
     *  You are given a list of deadends dead ends,
     *  meaning if the lock displays any of these codes, the wheels of
     *  the lock will stop turning and you will be unable to open it.
     *
     * -> return the minimum total number of turns required to open the lock, or -1 if it is impossible.
     *
     *
     *  IDEA: BFS
     *
     *   -> 1) for every idx, move its `digit` one by one via BFS
     *         if any combination in deadends, jump out that idx's BFS call,
     *         move the next idx
     *
     *   -> 2) check if the `least move` exists, if not, return -1
     *
     */
    public class Move{
        // attr
        int move;
        String state;

        // constructor
        public Move(String state, int move){
            this.move = move;
            this.state = state;
        }

        // getter, setter
        public int getMove() {
            return move;
        }

        public void setMove(int move) {
            this.move = move;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
    public int openLock(String[] deadends, String target) {

        // edge
        List<String> deadendsList = Arrays.asList(deadends);
        if(deadendsList.contains("0000")){
            return -1;
        }

        if (deadends.length == 1){
            if (deadends[0].equals("0000")){
                return -1;
            }
            if (target.equals("0000")){
                return 0;
            }
            // other cases will be processed below
        }

        //List<String> deadendsList = Arrays.asList(deadends);

        String[] moves = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        //Map<String, List<String>> moves = new HashMap<>();
       // moves.put("0", new ArrayList<>("1", "9"));
        Queue<Move> queue = new LinkedList<>();
        String initState = "0000";
        Set<String> visited = new HashSet<>();
        queue.add(new Move(initState, 0));

        while(!queue.isEmpty()){
            Move cur = queue.poll();
            int move = cur.getMove();
            String curState = cur.getState();
            // if found, return directly, since we use BFS, it should be `shortest` move
            if (curState.equals(target)){
                return move;
            }

            // ??? need to loop over idx ??? or we add "4 moved idx string" to queue at first
            for(int i = 0; i < initState.length(); i++){
                for (String x: moves){
                    String curNew = updateStringWithIdx(curState, x, i);
                    //boolean isEqaulOnIdx = curNew.charAt(i) == target.charAt(i);
                    if (!deadendsList.contains(curNew) && !visited.contains(curNew)){
                        // add to queue
                        visited.add(curNew);
                        queue.add(new Move(curNew, move+1));
                    }
                }
            }
        }

        return -1;
    }

    private String updateStringWithIdx(String input, String newStr, int idx){
        StringBuilder sb = new StringBuilder(input);
        return sb.replace(idx, idx+1, newStr).toString();
    }

    // LC 116
    /**
     * You are given a perfect binary tree where all leaves are on the same level,
     * and every parent has two children. The binary tree has the following definition:
     *
     *
     *
     * Populate each next pointer to point to its next right node.
     * If there is no next right node, the next pointer should be set to NULL.
     *
     * Initially, all next pointers are set to NULL.
     *
     *
     * Exp 1)
     *
     * Input: {"$id":"1",
     *      "left":{"$id":"2","left":{"$id":"3","left":null,"next":null,
     *      "right":null,"val":4},"next":null,
     *      "right":{"$id":"4","left":null,"next":null,"right":null,"val":5},"val":2},
     *      "next":null,"right":{"$id":"5","left":{"$id":"6","left":null,"next":null,"right":null,"val":6},"next":null,"right":{"$id":"7","left":null,"next":null,"right":null,"val":7},"val":3},"val":1}
     *
     * Output: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":{"$id":"4","left":null,"next":{"$id":"5","left":null,"next":{"$id":"6","left":null,"next":null,"right":null,"val":7},"right":null,"val":6},"right":null,"val":5},"right":null,"val":4},"next":{"$id":"7","left":{"$ref":"5"},"next":null,"right":{"$ref":"6"},"val":3},"right":{"$ref":"4"},"val":2},"next":null,"right":{"$ref":"7"},"val":1}
     *
     * Explanation: Given the above perfect binary tree (Figure A), your function should populate each next pointer to point to its next right node, just like in Figure B.
     *
     *
     * IDEA: RECURSION (?
     *
     *
     */
//    class Node {
//        public int val;
//        public workspace6.Node left;
//        public workspace6.Node right;
//        public workspace6.Node next;
//
//        public Node() {}
//
//        public Node(int _val) {
//            val = _val;
//        }
//
//        public Node(int _val, workspace6.Node _left, workspace6.Node _right, workspace6.Node _next) {
//            val = _val;
//            left = _left;
//            right = _right;
//            next = _next;
//        }
//    };
//    public Node connect(Node root) {
//
//        return null;
//    }

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

    public class Solution {
        public Node connect(Node root) {
            if (root == null) {
                return null;
            }

            // Start with the root node
            Node currentLevel = root;

            while (currentLevel != null) {
                // Traverse through the current level
                Node temp = currentLevel;
                while (temp != null) {
                    if (temp.left != null) {
                        // Connect the left child to the right child
                        temp.left.next = temp.right;
                    }

                    if (temp.right != null && temp.next != null) {
                        // Connect the right child to the next node's left child
                        temp.right.next = temp.next.left;
                    }

                    // Move to the next node in the current level
                    temp = temp.next;
                }

                // Move to the next level (leftmost node of the next level)
                currentLevel = currentLevel.left;
            }

            return root;
        }
    }

  // LC 43
  // https://leetcode.com/problems/multiply-strings/
  // 6.46 pm - 7.20 pm
  /**
   * Example 1:
   *
   * Input: num1 = "2", num2 = "3"
   * Output: "6"
   * Example 2:
   *
   * Input: num1 = "123", num2 = "456"
   * Output: "56088"
   *
   * -> actual op:
   *    123
   *  x 456
   *  ---------
   *
   *
   *  -> (100 * 1 + 10 * 2 + 1 * 3) * (100 * 4 + 10 * 5 + 1 * 6)
   *
   *  ->
   *
   *
   * IDEA: STRING OP
   *
   *
   */
  public String multiply(String num1, String num2) {

      // edge
      if (num1.equals("0") || num2.equals("0")){
          return "0";
      }

      if (num1.equals("1") || num2.equals("1")){
          if (num1.equals("1")){
              return num2;
          }
          return num1;
      }

      if (num1.length() == 1 && num2.length() == 1){
          // ???
          return String.valueOf((num1.charAt(0) - '0') * (num2.charAt(0) - '0'));
      }

      // loop over `digits`
      StringBuilder sb = new StringBuilder();
      String longer = null;
      String shorter = null;
      if(num1.length() > num2.length()){
          longer = num1;
          shorter = num2;
      }else{
          longer = num2;
          shorter = num1;
      }


//      // double loop ??
//      for(int i = 0; i < longer.length(); i++){
//          for(int j = 0; j < shorter.length(); j++){
//              in
//          }
//      }


      return null;
    }

  // LC 714
  // 7.17 pm - 7.30 pm
  // https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/description/
  /**
   *
   *
   *  IDEA: DP
   *
   *
   *  1. transaction fee need to be paid at EVERY sale
   *  2.
   *
   *
   *  Exp 1)
   *
   *  Input: prices = [1,3,2,8,4,9], fee = 2
   *  -> Output: 8
   *
   *
   *  [1,3,2,8,4,9], fee = 2
   *   b s         (rev = (3-1-2) = 0)
   *   b    s      (rev = (8-1-2) = 5
   *
   */
  public int maxProfit(int[] prices, int fee) {

      int res = 0;
      // edge
      if (prices.length == 1){
          return 0;
      }
      if(prices.length == 2){
          if(prices[1] > prices[0]){
              return  Math.max(0, prices[1] - prices[0] - fee);
          }
      }

      // dp
      int[] dp = new int[prices.length+1];
      // fill dp with 0
      Arrays.fill(dp, 0); // ??
      // init state
      dp[0] = 0;
     if(prices[1] > prices[0]){
         dp[1] = Math.max(0, prices[1] - prices[0] - fee);
     }

     // 2 option:
     // if not buy: buy or not buy
     // if already buy: sale or hold


      return res;
    }

    // LC 708
    // 3.09 pm - 3.25 pm
    /**
     *  IDEA:
     *
     *   1) get all elements (existing + new)
     *   2) sort
     *   3) loop over existing linked list, if new node not existed,
     *      add it to linked list
     */



//    class Node {
//        public int val;
//        public Node next;
//
//        public Node() {}
//
//        public Node(int _val) {
//            val = _val;
//        }
//
//        public Node(int _val, Node _next) {
//            val = _val;
//            next = _next;
//        }
//    };
//
    /**
     *  IDEA: LIST NODE OP
     *
     *  loop over node (head)
     *  if   heed.next < insertVal < head.prev
     *     -> insert (via node op)
     *
     *  if insertVal < `end node` && insertVal > `start node`
     *    -> insert (via node op)
     *     -> use a hashSet track visited node
     *     -> if "already visited node" is reached
     *     -> means `reach the end of node`
     *
     */
    public Node insert(Node head, int insertVal) {
        // edge
        if (head == null){
            return new Node(insertVal);
        }
        if (head.next == null){
            head.next = new Node(insertVal);
            return head; // ??
        }
        Set<Integer> visited = new HashSet<>();
        // ???
        //int prev = -1; // ???
        Node _prev = null;
        // ??? DOUBLE check end condition
        while (true){
            int val = head.val;
            visited.add(val);

            Node _next = head.next;
            //Node _prev = new
            Node _new = new Node(insertVal);

            // case 1) found "to-insert" within node loop
            if(_prev != null && insertVal >= _prev.val && insertVal <= _next.val){
                Node _cur = head;
                //Node _new = new Node(insertVal);
                _prev.next = _new;
                _new.next = _next;
                break; // already inserted, quit loop
            }

            // case 2) if insertVal < `end node` && insertVal > `start node`
            if (visited.contains(head.val)){
                _prev.next = _new;
                _new.next = head;
                // ???
                break;
            }

            _prev = head;
            head = head.next;
        }

        return null;
    }


    public Node insert_1(Node head, int insertVal) {

      // edge
      if(head == null){
          return new Node(insertVal);
      }

      // get all elements
      List<Integer> list = new ArrayList<>();
      Node head2 = head;
      while(head2 != null){
          list.add(head2.val);
          head2 = head2.next;
      }

    // sort (assume the ordering is `same` as the order we tranverse linked list)
    Collections.sort(list);

    // edge case 2) if new element < all elements in list or > all elements list
    if (insertVal < list.get(0)){
        Node res = new Node(insertVal);
        res.next = head;
        return res;
    }

    if (insertVal > list.get(list.size()-1)){
        while(head != null){
            head = head.next;
        }
        head.next = new Node(insertVal);
        return head; // ?? or need to define a `res` object ?
    }

    // create `double length` list
//    List<Integer> doubleList = new ArrayList<>();
//    for(int i = 0; i < 2; i++){
//        for(int x: list){
//            doubleList.add(x);
//        }
//    }

    // find `entry point`
    Node res = head;
    int idx = 0;
    while(head != null){
        int x = list.get(idx);
        if (head.next.val != x){
            Node _new = new Node(x);
            Node _next = head.next;
            Node _cur = head;
            head.next = _new;
            _new.next = _next;
            //head = head.next;
            break;
        }
        head = head.next;
        idx += 1;
    }

    return res;
    }

  // LC 210
  // https://leetcode.com/problems/course-schedule-ii/
  // 4.07 - 4.20 pm
  /**
   *  prerequisites[i] = [ai, bi]
   *  indicates that you must
   *  take course bi first if you want to take course ai.
   *
   *  -> [ai, bi] -> prev: bi, next: ai
   *
   *
   *  IDEA:  TOPOLOGICAL SORT
   *
   *  1) build graph, orders, queue
   *  2)
   *
   *   init orders (all 0)
   *   init queue
   *   add all `order = 0` to queue
   *
   *  3)
   *
   *
   */
  public int[] findOrder(int numCourses, int[][] prerequisites) {

      // edge
      if(prerequisites.length==0){
          int[] res = new int[1];
          res[0] = 0;
          return res;
      }

      if(numCourses == 2 && prerequisites.length == 1){
          int[] res = new int[2];
          res[0] = prerequisites[0][1];
          res[1] = prerequisites[0][0];
          return res;
      }

      // init orders
      int[] orders = new int[numCourses];
      // fill all with 0
      Arrays.fill(orders, 0);
      // init graph
      // {next : [prev1, prev2, ...] }
      Map<Integer, List<Integer>> graph = new HashMap<>();
      for(int i = 0; i < prerequisites.length; i++){
          int[] x = prerequisites[i];
          int prev = x[1];
          int next = x[0];
          System.out.println(">> i = " + i + ", prev = " + prev);

          //List<Integer> nextList = graph.getOrDefault(prev, new ArrayList<>());
          List<Integer> preList = graph.getOrDefault(next, new ArrayList<>());
          preList.add(prev);
          graph.put(next, preList);

          // update orders
          orders[prev] += 1; // ???
      }

      System.out.println(">>> graph = " + graph);

      Queue<Integer> queue = new LinkedList<>();
      // add all `order=0` to queue
      for(int x: orders){
          if(x == 0){
              queue.add(x);
          }
      }

    //List<Integer> cache = new ArrayList<>();
    int[] res = new int[numCourses];
    int idx = 0;
    System.out.println(">>> queue = " + queue);
    while (!queue.isEmpty()){
       int cur = queue.poll();
       System.out.println(">>> cur = " + cur);
       //cache.add(cur);
       res[idx] = cur;
       idx += 1;
       for(Integer prev: graph.get(cur)){
           //graph.put(cur, graph.get(cur)-1);
           // update order
           orders[prev] = orders[prev] - 1; // ??
           // NOTE !!! if order == 0, add to queue
           if (orders[prev] == 0){
               queue.add(prev);
           }
       }
    }

    return res;
  }

  // LC 241
  // https://leetcode.com/problems/different-ways-to-add-parentheses/
  // 6.27 pm - 6.40 pm
  /**
   *
   *  You may return the answer in any order.
   *
   *
   *  EXP 1)
   *
   * Input: expression = "2-1-1"
   * Output: [0,2]
   * Explanation:
   * ((2-1)-1) = 0
   * (2-(1-1)) = 2
   *
   * EXP 2)
   *
   * Input: expression = "2*3-4*5"
   * Output: [-34,-14,-10,-10,10]
   * Explanation:
   * (2*(3-(4*5))) = -34
   * ((2*3)-(4*5)) = -14
   * ((2*(3-4))*5) = -10
   * (2*((3-4)*5)) = -10
   * (((2*3)-4)*5) = 10
   *
   *
   * IDEA: RECURSION
   *  (all combination, validate "computation, collect result)
   *
   *  EXP 1) -> expression = "2-1-1"
   *     2 -1 -1, so we can have 3-1 -1  = 1 brackets e.g. ()
   *     and 2 "-"
   *     -> so we need to get all combination of
   *     [2,1,1, -, -, (, )]
   *     -> could be
   *       ((2 -1 -1 ))
   *      ( 2 - (1-1) )
   *
   *
   *  EXP 2) -> expression = "2*3-4*5"
   *     4 - 1 - 1 = 2 brackets, e.g. (), ()
   *     and 1 "-" and 2 "*"
   *     -> so we need to get all combination of
   *     [2,3,4,5,-,*,*,(,(,),)]
   *
   *
   *
   *
   */
  public List<Integer> diffWaysToCompute(String expression) {

   // edge
   if (expression == null || expression.isEmpty()){
       return new ArrayList<>();
   }

   List<Integer> res = new ArrayList<>();
   // recursion call

   return res;
  }

  // LC 986
  // https://leetcode.com/problems/interval-list-intersections/
  // 7.06 - 7.36 pm
  /**
   * firstList[i] = [starti, endi] and
   *
   * secondList[j] = [startj, endj].
   *
   * -> Return the intersection of these two interval lists.
   *
   *
   * The intersection of two closed intervals is a set
   * of real numbers that are either empty or
   * represented as a closed interval.
   * For example, the intersection of [1, 3] and [2, 4] is [2, 3].
   *
   *
   *  IDEA 1: 2 POINTERS
   *
   *  IDEA 2: Scanning line ??
   *
   */

  // IDEA: SCANNING LINE
  public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
      // edge
      if(firstList.length == 0 || secondList.length == 0){
          return new int[][]{};
      }
      // prepare list
      // [ val, status ], status: 1 (start), -1 (end)
      List<List<Integer>> statusList = new ArrayList<>();
      //
      for(int[] x: firstList){
          List<Integer> tmp1 = new ArrayList<>();
          tmp1.add(x[0]);
          tmp1.add(1);

          List<Integer> tmp2 = new ArrayList<>();
          tmp2.add(x[1]);
          tmp2.add(-1);

          statusList.add(tmp1);
          statusList.add(tmp2);
      }

      for(int[] x: secondList){
          List<Integer> tmp1 = new ArrayList<>();
          tmp1.add(x[0]);
          tmp1.add(1);

          List<Integer> tmp2 = new ArrayList<>();
          tmp2.add(x[1]);
          tmp2.add(-1);

          statusList.add(tmp1);
          statusList.add(tmp2);
      }

      System.out.println(">>> (before) statusList = " + statusList);

      // sort
      Collections.sort(statusList, new Comparator<List<Integer>>(){
          @Override
          public int compare(List<Integer> a, List<Integer> b){
              // 1) compare on idx (small first) 2) compare on status (start (1) first)
              int res = a.get(0) - b.get(0);
              if(res == 0){
                  return b.get(1) - a.get(1);
              }
              return res;
          }
      });

      System.out.println(">>> (after) statusList = " + statusList);

      int prev = -1; // ???
      int cnt = 0;
      List<List<Integer>> preRes = new ArrayList<>();
      for(List<Integer> x: statusList){
          // ????
          if (cnt == 2 && prev != -1){
              List<Integer> tmp2 = new ArrayList<>();
              // ???
              tmp2.add(prev);
              tmp2.add(x.get(1)); // ?????
              preRes.add(tmp2);
              cnt = 0; // ???
          }

          if (x.get(1) == 1){
              cnt += 1;
          }

          prev = x.get(0); // ??
      }

      int[][] res = new int[preRes.size()][2];

      for(int k = 0; k < preRes.size(); k++){
          res[k] = new int[]{preRes.get(k).get(0), preRes.get(k).get(1)}; // ??? TODO: check !!!!
      }

      System.out.println(">>> res = " + res);

      return res;
  }

      // IDEA: 2 POINTERS
    // 10.01 - 10.30 am
  /**
   *
   * Input: A = [[0,2],[5,10],[13,23],[24,25]], B = [[1,5],[8,12],[15,24],[25,26]]
   * Output: [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
   * Reminder: The inputs and the desired output are lists of Interval objects, and not arrays or lists.
   *
   *
   *
   *
   */
//  public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
//
//      // edge
//      if(firstList.length == 0 || secondList.length == 0){
//          return new int[][]{};
//      }
//
//      // 2 pointers
//      // define 2 pointers, one for firstList, the other for secondList
//      int i = 0;
//      int j = 0;
//      List<List<Integer>> collected = new ArrayList<>();
//      // ONLY loop while i < firstList len and j < secondList
//      while(i < firstList.length && j < secondList.length){
//          int[] firstVal = firstList[i];
//          int[] secondVal = secondList[j];
//          int maxStart = Math.max(firstVal[0], secondVal[0]);
//          int minEnd = Math.min(firstVal[1], secondVal[1]);
//          // check if there is an overlap
//          if (maxStart <= minEnd){
//              List<Integer> cur = new ArrayList<>();
//              // start idx
//              cur.add(maxStart);
//              // end idx
//              cur.add(minEnd);
//              collected.add(cur);
//          }
//
//          // NOTE !!! move idx in list if it NOT possible to make any new overlap
//          //          per current idx
//          // ??? check ????
////          if (maxStart > firstVal[1]){
////              i += 1;
////          }
////          if (maxStart > secondVal[1]){
////              j += 1;
////          }
//          // Move the pointer for the interval that ends first
//          if (firstVal[1] < secondVal[1]) {
//              i++;
//          } else {
//              j++;
//          }
//      }
//
//      System.out.println(">>> collected = " + collected);
//
//      //int[][] res = new int[collected.size()][collected.size()];
//      int[][] res = new int[collected.size()][2];
//      for(int k = 0; k < collected.size(); k++){
//          //res[i] = new int[][]{ { collected[0], collected[1] } };
//          //res[k] = new int[]{ 1,2 };
//          res[k] = new int[]{collected.get(k).get(0), collected.get(k).get(1)}; // ??? TODO: check !!!!
//      }
//
//      System.out.println(">>> res = " + res);
//
//      return res;
//  }

//  public class IntervalStatus{
//      int idx;
//      String status;
//
//      public int getIdx() {
//          return idx;
//      }
//
//      public void setIdx(int idx) {
//          this.idx = idx;
//      }
//
//      public String getStatus() {
//          return status;
//      }
//
//      public void setStatus(String status) {
//          this.status = status;
//      }
//
//      public IntervalStatus(int idx, String status){
//          this.idx = idx;
//          this.status = status;
//      }
//  }
//  public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
//
//      // edge
//      if (firstList == null || secondList == null){
//          if (firstList == null){
//              return new int[][]{};
//          }
//          return new int[][]{};
//      }
//
//      // prepare to-scan list
//      List<IntervalStatus> statusList = new ArrayList<>();
//      for(int[] x: firstList){
//          int open = x[0];
//          int close = x[1];
//          statusList.add(new IntervalStatus(open, "open-1"));
//          statusList.add(new IntervalStatus(close, "close-1"));
//      }
//
//      for(int[] x: secondList){
//          int open = x[0];
//          int close = x[1];
//          statusList.add(new IntervalStatus(open, "open-2"));
//          statusList.add(new IntervalStatus(close, "close-2"));
//      }
//
//      List<List<Integer>> cache = new ArrayList<>();
//
//      for(IntervalStatus intervalStatus: statusList){
//
//      }
//
//
//      return null;
//  }


  // LC 307
  /**
   *  307. Range Sum Query - Mutable
   * Given an integer array nums,
   * find the sum of the elements between indices i and j (i ≤ j), inclusive.
   *
   * The update(i, val) function modifies nums by updating the element at index i to val.
   *
   * Example:
   *
   * Given nums = [1, 3, 5]
   *
   * sumRange(0, 2) -> 9
   * update(1, 2)
   * sumRange(0, 2) -> 8
   * Note:
   *
   * The array is only modifiable by the update function.
   * You may assume the number of calls to update and sumRange function is distributed evenly.
   * Difficulty:
   *
   *
   *
   *  IDEA 1) brute force
   *        -> maintain an array, keep updating element per idx,
   *        -> once there is a query, get sub-array sum at a time
   *
   *  IDEA 2) brute force with pre-sum array
   *       -> maintain an array, keep updating element per idx,
   *       -> update presum array accordingly (per above)
   *       -> then the sub-array sum can be obtained via
   *           - sum(i,j) = preSum(j+1) - preSum(i)
   *
   */
  class NumArray {

      List<Integer> preSum;
      int[] nums;

      public NumArray(int[] nums) {
          this.nums = nums;
          // pre-sum array
          this.getPreSumArray(nums);
      }

      public void update(int index, int val) {
          this.nums[index] = val;
          // update preSum
          this.getPreSumArray(nums);
      }

      public int sumRange(int left, int right) {

          if (right < left){
              return -1; // or throw exception
          }

          return this.preSum.get(right+1) - this.preSum.get(left);
      }

      private void getPreSumArray(int[] nums){
          this.preSum = new ArrayList<>();
          this.preSum.add(0);
          int cur = 0;
          for(int i = 0; i < nums.length; i++){
              cur += nums[i];
              this.preSum.add(cur);
          }
      }

  }

    /**
     * Your NumArray object will be instantiated and called as such:
     * NumArray obj = new NumArray(nums);
     * obj.update(index,val);
     * int param_2 = obj.sumRange(left,right);
     */

    // LC 332
    // ReconstructItinerary
    /**
     *  332. Reconstruct Itinerary
     *
     *
     * Given a list of airline tickets represented by pairs
     * of departure and arrival airports [from, to],
     *
     * -----> `reconstruct the itinerary in order.`
     *
     * All of the tickets belong to a man who departs from JFK.
     * Thus, the itinerary must begin with JFK.
     *
     * ----> the itinerary must begin with JFK.
     *
     * Note:
     *
     * If there are multiple valid itineraries, you should
     * return the itinerary that has the smallest lexical order
     * when read as a single string. For example,
     * the itinerary ["JFK", "LGA"] has a smaller lexical order than ["JFK", "LGB"].
     * All airports are represented by three capital letters (IATA code).
     * You may assume all tickets form at least one valid itinerary.
     * Example 1:
     *
     * Input: [["MUC", "LHR"], ["JFK", "MUC"], ["SFO", "SJC"], ["LHR", "SFO"]]
     * Output: ["JFK", "MUC", "LHR", "SFO", "SJC"]
     * Example 2:
     *
     * Input: [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
     * Output: ["JFK","ATL","JFK","SFO","ATL","SFO"]
     * Explanation: Another possible reconstruction is ["JFK","SFO","ATL","JFK","ATL","SFO"].
     *              But it is larger in lexical order.
     *
     *
     *
     *
     * NOTE !!!
     *
     *  1) [from, to]
     *
     *  2) If there are multiple valid itineraries,
     *    you should return the itinerary that has the smallest lexical order
     *
     *
     *  EXP 1)
     *
     *  Input: [["MUC", "LHR"], ["JFK", "MUC"], ["SFO", "SJC"], ["LHR", "SFO"]]
     *
     *  -> Output: ["JFK", "MUC", "LHR", "SFO", "SJC"]]
     *
     *
     *  EXP 2)
     *
     *  Input: [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
     *
     *  -> Output: ["JFK","ATL","JFK","SFO","ATL","SFO"]
     *
     * Explanation: Another possible reconstruction is ["JFK","SFO","ATL","JFK","ATL","SFO"].
     *              But it is larger in lexical order.
     *
     *
     *
     *  10.50 AM
     *
     *  IDEA 1) TOPOLOGICAL SORT -> accept `duplicated` element ????
     *
     *
     *  IDEA 2) DFS
     */
    public List<String> findItinerary(List<List<String>> tickets) {

        return null;
    }

    // LC 554
    // 9.51 am - 10.30 am
    /**
     * 554. Brick Wall
     * There is a brick wall in front of you.
     * The wall is rectangular and has several rows of bricks.
     * The bricks have the same height but different width.
     * You want to draw a vertical line from the top to the bottom and cross the least bricks.
     *
     * The brick wall is represented by a list of rows.
     * Each row is a list of integers representing the width of
     * each brick in this row from left to right.
     *
     * If your line go through the edge of a brick, then the
     * brick is not considered as crossed. You need to find
     * out how to draw the line to cross the least bricks
     * and return the number of crossed bricks.
     *
     * You cannot draw a line just along one of the two vertical
     * edges of the wall, in which case the line will obviously cross no bricks.
     *
     *
     *
     * Example:
     *
     * Input: [[1,2,2,1],
     *         [3,1,2],
     *         [1,3,2],
     *         [2,4],
     *         [3,1,2],
     *         [1,3,1,1]]
     *
     * Output: 2
     *
     * Explanation:
     *
     *
     *
     * Note:
     *
     * The width sum of bricks in different rows are the same and won't exceed INT_MAX.
     * The number of bricks in each row is in range [1,10,000].
     * The height of wall is in range [1,10,000].
     * Total number of bricks of the wall won't exceed 20,000.
     *
     *
     * ->  you want to draw a vertical line from the top
     *     to the bottom and cross the least bricks.
     *
     *
     *
     *  IDEA:  COUNTER + SCANNING LINE ???
     *
     *
     *
     *
     *  EXP 1)
     *
     *      * Input: [[1,2,2,1],
     *      *         [3,1,2],
     *      *         [1,3,2],
     *      *         [2,4],
     *      *         [3,1,2],
     *      *         [1,3,1,1]]
     *      *
     *
     *
     *      -> cnt = {1:1, 3: 1, 5: 1, 6:1}
     *      -> cnt = {1:1, 3: 2, 4:1, 5: 1, 6:2}
     *      -> cnt = {1:2, 3: 2, 4:2, 5: 1, 6:3}
     *      -> cnt = {1:2, 2:1, 3: 2, 4:2, 5: 1, 6:4}
     *      -> cnt = {1:2, 2:1, 3: 2, 4:3, 5: 1, 6:5}
     *
     *
     *
     *      -> cnt = {1:3, 2:1, 3: 2, 4:4, 5: 2, 6:6}
     *
     **/
    public int leastBricks(List<List<Integer>> wall) {

        // edge
        if (wall.isEmpty()){
            return 0;
        }

        int res = 0;
        Map<Integer, Integer> cnt = new HashMap<>();
        // build counter map
        // TODO: optimize double loop
        for(List<Integer> w: wall){
            int cumSum = 0;
            for(Integer x: w){
                cumSum += x;
                int val = cnt.getOrDefault(x, 0);
                cnt.put(cumSum, val+1);
            }
        }

        // sort on val
        List<Integer> keys = new ArrayList<>(cnt.keySet());

        // edge case : wall are all the same
        if(keys.size() == 1){
            return wall.size();
        }

        System.out.println(">>> (before) keys = " + keys);

        Collections.sort(keys, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // sort on val, decreasing order
                return cnt.get(o2) - cnt.get(o1);
            }
        });

        System.out.println(">>> (before) keys = " + keys);

        return wall.size() - keys.get(0);
    }

    // LC 325
    /**
     *  325. Maximum Size Subarray Sum Equals k
     *
     *
     * Given an array nums and a target value k,
     * find the maximum length of a subarray that sums to k.
     * If there isn't one, return 0 instead.
     *
     * Note:
     * The sum of the entire nums array is guaranteed to fit
     * within the 32-bit signed integer range.  ???
     *
     * Example 1:
     *
     * Input: nums = [1, -1, 5, -2, 3], k = 3
     * Output: 4
     * Explanation: The subarray [1, -1, 5, -2] sums to 3 and is the longest.
     *
     *
     * Example 2:
     *
     * Input: nums = [-2, -1, 2, 1], k = 1
     * Output: 2
     * Explanation: The subarray [-1, 2] sums to 1 and is the longest.
     *
     *
     * Follow Up:
     * Can you do it in O(n) time?
     *
     *
     *
     * -> find the maximum length of a subarray that sums to k.
     *
     *
     *  TODO: check if `preSum == k` already happended before (within loop over nunms)
     *
     *  -> if preSum == k existed
     *     (let's say current idx = j, and a previous idx = i, can make sum(i, j) == k)
     *     -> preSum(j) - preSum(i) = k
     *     -> preSum(j) if what we have  (preSum)
     *     -> so need to check if `preSum(j) - k` exists in map
     *
     *
     *
     *
     *
     *
     *  IDEA 1) HASH HAP
     *
     *   -> step 1) build map and record `pre sum` and its index
     *       { preSum : idx } ???
     *
     *   -> step 2) loop over nums
     *       since  x + preSUm = k  ( x is current nums val)
     *       -> then preSum = k - x
     *
     *       presum + x  = k
     *
     *       -> x = presum - k (x is the val in map)
     *
     *
     *       sum - k = x
     *
     *       -> k = sum - x
     *
     *
     *       -> so need to check if `k-x` is in map
     *       -> if yes, get its distance ( curIdx - (idx of `k-x` in map)
     *       -> maintain the `longest distance`
     *
     *    -> step 3) return result
     *

     *   Exp 1)
     *
     *   Input: nums = [1, -1, 5, -2, 3], k = 3
     *
     *   -> preSum = [1, 0, 5, 3, 6]
     *
     *    { preSum : idx } ???
     *    map = {0:0} // init state ?????
     *
     *   -> map = {0:0, 1:0, 0:1, 5:2, 3:3, 6: 5}
     *
     *   -> loop over nums
     *     -> num = 1, k-x = 2, 2 not in map
     *     -> num = -1, k - x = 4, not in map
     *     -> num = 5, k - x = -2, not in map
     *     -> num = -2, k - x = 5, in map, -> dist = abs(2 - 3 + 1) = 2
     *     -> num = 3, k - x = 0, in map -> dist = abs(4 - 1 + 1) = 4
     *
     *     -> so max dist = 3, -> res = 4
     *
     *
     *
     *
     *  EXP 2)
     *
     *  Input: nums = [-2, -1, 2, 1], k = 1
     *
     *  -> preSum = [-2, -3, -1, 0]
     *
     *  {preSum, idx}
     *  -> map = {0:0, -2:0, -3:1, -1:2, 0:3}
     *
     *  -> loop over nums
     *
     *  -> num = -2, k-x = 3, not in map
     *  -> num = -1, k - x = 2, not in map
     *  -> num = 2, k - x = -1, in map, dist = abs(2-2+1) = 1
     *
     *
     *  -> num = 1, k - x = 0, in map, dist = abs(3-0+1) = 4
     *
     *
     *
     *
     */
    public int maxSubArrayLen(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0){
            return 0;
        }

        int res = 0;

        // build map
        // {preSum: idx}
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0,0); // init state ????

        int preSum = 0;

        for(int i = 0; i < nums.length; i++){
            int val = nums[i];
            preSum += val;
            int cnt = map.getOrDefault(preSum, 0);
            map.put(preSum, cnt);
        }
        System.out.println(">>> map = " + map);

        // loop over nums
        for(int i = 0; i < nums.length; i++){
            int val = nums[i];
            if(map.containsKey(k - val)){
                res = Math.max(res, Math.abs(i - map.get(k-val) + 1));
            }
        }

        return res;

    }

    // LC 310
    // https://leetcode.com/problems/minimum-height-trees/
    // 3.10 pm - 3.30 pm
    /**
     *
     *
     *  in the tree,
     *  you can choose any node of the tree as the root.
     *  When you select a node x as the root,
     *  the result tree has height h.
     *
     *  -> those with minimum height (i.e. min(h))
     *  are called minimum height trees (MHTs).
     *
     *  minimum height trees (MHTs).
     *
     *  The `height` of a rooted tree is the number of edges on
     *  the longest downward path between the root and a leaf.
     *
     *  (dist : root -> leaf)
     *
     *
     */
    /**
     *  IDEA : BFS
     *
     *  step 1) get all nodes (as set)
     *  step 2) go through nodes, set each node as root
     *          , get and save dist between root and other nodes
     *  step 3) get the min dist, return the node have same dist as result
     *
     */
    class NodeDist{
        Integer root;
        Integer node;
        Integer dist;

        public Integer getDist() {
            return dist;
        }

        public void setDist(Integer dist) {
            this.dist = dist;
        }

        public Integer getNode() {
            return node;
        }

        public void setNode(Integer node) {
            this.node = node;
        }

        public Integer getRoot() {
            return root;
        }

        public void setRoot(Integer root) {
            this.root = root;
        }

        NodeDist(Integer root, Integer node, Integer dist){
            this.root = root;
            this.node = node;
            this.dist = dist;
        }
    }
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {

        // edge
        if (n == 0){
            return new ArrayList<>();
        }

        // get node set
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < n; i++){
            set.add(i);
        }

        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] e: edges){
            List<Integer> tmp = map.getOrDefault(e[0], new ArrayList<>());
            tmp.add(e[1]);
            map.put(e[0], tmp);
        }

        // init result
        List<Integer> res = new ArrayList<>();


        // init queue
        Queue<NodeDist> queue = new LinkedList<>();
        queue.add(new NodeDist(0, 0, 0)); // default add `root` to queue
        while(!queue.isEmpty()){
            NodeDist nodeDist = queue.poll();
            Integer dist = nodeDist.dist;
            //Integer node = nodeDist.node;
            Integer root = nodeDist.root;
            if(map.containsKey(root)){
                for(int x : map.get(root)){
                    queue.add(new NodeDist(root, x, dist+1));
                }
            }
        }

        return res;
    }

    // LC 529
    // https://leetcode.com/problems/minesweeper/
    // 4.44 pm - 5.10 pm
    /**
     *
     *  IDEA : BFS
     *
     */
    public char[][] updateBoard(char[][] board, int[] click) {

        // edge
        if (board.length == 1 && board[0].length == 1){
            if(board[0][0] == 'M'){
                board[0][0] = 'X';
            }
            return board;
        }

        Set<List<Integer>> set = new HashSet(); // ?
        int l = board.length;
        int w = board[0].length;
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                char cur = board[i][j];
                if (cur == 'M'){
                    List<Integer> tmp = new ArrayList<>();
                    tmp.add(j);
                    tmp.add(i);
                    set.add(tmp);
                }
            }
        }

        if (set.isEmpty()){
            return null;
        }

        // update `mine` neighbor
        Set<List<Integer>> visited = new HashSet<>();
//        for(List<Integer> coord: set){
//            // update board
//            updateMineNeighborVal(coord, visited, board);
//        }

        // case 1) if "click" 'X'
        List<Integer> clickPosition = new ArrayList<>();
        clickPosition.add(click[0]);
        clickPosition.add(click[1]);
        if (set.contains(clickPosition)){
            board[click[0]][click[1]] = 'X';
            updateMineNeighborVal(clickPosition, visited, board);
        }

        // case 2) if NOT "click" 'X'
        return board;
    }

    public void updateMineNeighborVal(List<Integer> coord, Set<List<Integer>> visited, char[][] board){
        int x = coord.get(0);
        int y = coord.get(1);

        int l = board.length;
        int w = board[0].length;

        int[][] dirs = new int[][]{ {1,0}, {-1,0}, {0,1}, {0,-1} };
        Queue<List<Integer>> q = new LinkedList<>();

        // update 'X' ????
        for(int[] dir: dirs){
            int x_ = x + dir[0];
            int y_ = y + dir[1];
            List<Integer> newPosition = new ArrayList<>();
            newPosition.add(x_);
            newPosition.add(y_);
            if (x_ >= 0 && x_ < w && y_ >= 0 && y_ <= l && !visited.contains(newPosition) && board[y_][x_] == 'M'){
                visited.add(newPosition);
                //board[y_][x_] = In + 1;
                board[y_][x_] = 'X';
                q.add(newPosition);
            }
        }
    }

    // LC 217
    public boolean containsDuplicate(int[] nums) {

        // edge
        if (nums == null || nums.length == 0){
            return false;
        }

        Set<Integer> set = new HashSet<>();
        for(int x: nums){
            if(set.contains(x)){
                return false;
            }
            set.add(x);
        }

        return true;
    }

    // LC 242
    public boolean isAnagram(String s, String t) {
        // edge
        if (s == null || t == null){
            return false;
        }
        if (s.equals(t)){
            return false;
        }
        Map<String, Integer> map_s = new HashMap<>();
        for(String x: s.split("")){
            int cnt = map_s.getOrDefault(x,0);
            map_s.put(x, cnt+1);
        }

        Map<String, Integer> map_t = new HashMap<>();
        for(String x: t.split("")){
            int cnt = map_t.getOrDefault(x,0);
            map_t.put(x, cnt+1);
        }

        for(String k: map_s.keySet()){
            if(!map_t.containsKey(k) || !map_t.get(k).equals(map_s.get(k))){
                return false;
            }
        }

        return true;
    }

    // LC 001
    public int[] twoSum(int[] nums, int target) {
        // map
        // {val: idx}
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < nums.length; i++){
            map.put(nums[i], i);
        }

        for(int i = 0; i < nums.length; i++){
            // x + y = target
            // -> y = target - x
           if(map.containsKey(target - nums[i]) && map.get(target - nums[i]) != i){
               return new int[]{i,map.get(target - nums[i])};
           }
        }

        // if not found, return {-1,-1}
        //int[] res = new int[]{-1,-1};
        return new int[]{-1,-1};
    }

    // LC 49
    public List<List<String>> groupAnagrams(String[] strs) {

        // edge
        if (strs == null || strs.length == 0){
            return null; // ?
        }

        Map<String, List<String>> map = new HashMap<>();
        for(String x: strs){
            // copy and reorder
            String[] x_Array = x.split("");
            Arrays.sort(x_Array);
            String sortedStr = Arrays.toString(x_Array);
            List<String> tmp = new ArrayList<>();
            if(map.containsKey(sortedStr)){
                tmp = map.get(sortedStr);
//                tmp.add(x);
//                map.put(sortedStr, tmp);
            }
            tmp.add(x);
            map.put(sortedStr, tmp);
        }

        System.out.println(">>> map = " + map);

        List<List<String>> res = new ArrayList<>();
        /** NOTE !!! below */
        for(String val: map.keySet()){
            res.add(map.get(val));
        }
        return res;
    }

    // LC 374
    // idea: custom sorting on map value
    public int[] topKFrequent(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return new int[]{};
        }
        // map
        // {val: cnt}
        Map<Integer, Integer> map = new HashMap<>();
        for(int x: nums){
            int cnt = map.getOrDefault(x, 0);
            map.put(x, cnt+1);
        }

        //Set<Integer> keys = map.keySet();
        //List<Set<Integer>> x = Arrays.asList(map.keySet());
        ArrayList<Integer> keyList = new ArrayList<>(map.keySet());

        System.out.println(">>> before sort, keyList = " + keyList);

        // sort on value (decreasing order, e.g. big -> small)
        Collections.sort(keyList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff =  map.get(o2) - map.get(o1);
                return diff; // ??
            }
        });

        System.out.println(">>> after sort, keyList = " + keyList);

        int[] res = new int[k];

        int w = 0;
        while(w < k){
            res[w] = keyList.get(w); // ??
            w += 1;
        }

        return res;
    }

    // LC 2284
    // https://leetcode.com/problems/sender-with-largest-word-count/
    // 3.57 pm - 4.15 pm
    // idea: map custom sorting ( with val and key lex)
    public String largestWordCount(String[] messages, String[] senders) {
        // edge
        if(messages.length == 0 || senders.length == 0){
            return null;
        }

        // map: {sender, word_cnt}
        Map<String, Integer> map = new HashMap<>();
//        for(int i = 0; i < messages.length; i++){
//            String sender = senders[i];
//            String msg = messages[i];
//            Integer tmp = map.getOrDefault(sender, 0);
//            map.put(sender, tmp + msg.split("").length);
//        }

        for (int i = 0; i < messages.length; i++) {
            String sender = senders[i];
            int wordCount = messages[i].split("\\s+").length; // Fix: Correct word split
            map.put(sender, map.getOrDefault(sender, 0) + wordCount);
        }


        // get key list
        List<String> keyList = new ArrayList<>(map.keySet());

        System.out.println(">>> before sort keyList = " + keyList);

        // sort
        Collections.sort(keyList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = map.get(o2) - map.get(o1);
//                if (lengthComparison == 0) {
//                    return o1.compareTo(o2); // lexicographical order
//                }
                if(diff == 0){
                    return o1.compareTo(o2);
                }
                return diff;
            }
        });

        System.out.println(">>> after sort keyList = " + keyList);

        return keyList.get(0);
    }

    // LC 271
    // https://leetcode.ca/2016-08-27-271-Encode-and-Decode-Strings/
    // 4.33 - 4.50 pm
    public class Codec {

        // Encodes a list of strings to a single string.
        public String encode(List<String> strs) {
            // edge
            if(strs == null || strs.isEmpty()){
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for(String x: strs){
                sb.append(x);
                sb.append(",");
            }
            return sb.toString();
        }

        // Decodes a single string to a list of strings.
        public List<String> decode(String s) {
            // edge
            if(s == null || s.isEmpty()){
                return new ArrayList<>();
            }

            List<String> res = new ArrayList<>();

            String[] tmp = s.split(",");
            for(String x: tmp){
                if(x != null && !x.isEmpty()){
                    res.add(x);
                }
            }

            return res;
        }
    }

    // LC 238
    public int[] productExceptSelf(int[] nums) {

        // edge
        if(nums == null || nums.length == 0){
            return new int[]{};
        }

        int[] res = new int[nums.length];
        // get all product
        int product = 1;
        for(int x: nums){
            product = product * x;
        }

        for(int i = 0; i < nums.length; i++){
            if (nums[i] != 0){
                res[i] = product / nums[i];
            }else{
                res[i] = getProductExceptIdx(nums, i);
            }
        }

        return res;
    }

    private int getProductExceptIdx(int[] nums, int idx){
        int res = 1;
        for(int i = 0; i < nums.length; i++){
            if(i != idx){
                res = res * nums[i];
            }
        }

        return res;
    }

  // LC 128
  // 4.59 - 5.20 pm
  /**
   * Given an unsorted array of integers nums,
   * return the length of the longest consecutive elements sequence.
   *
   * You must write an algorithm that runs in O(n) time.
   *
   *
   *
   *  exp 1
   *
   *  nums = [100,4,200,1,3,2]
   *
   *  -> sorted = [1,2,3,4,100,200]
   *  -> so, 4 ([1,2,3,4])
   *
   *
   *  exp 2
   *
   *  nums = [0,3,7,2,5,8,4,6,0,1]
   *  -> sorted = [0,0,1,2,3,4,5,6,7,8]
   *
   *
   *  -> IDEA: SORT + 2 POINTERS
   *
   *  exp 1)
   *
   *  step 1) sort:
   *   nums = [100,4,200,1,3,2]
   *   -> sorted = [1,2,3,4,100,200]
   *
   *
   * step 2) 2 pointers
   *   [1,2,3,4,100,200]
   *    i j            len = 1
   *    i   j          len = 2
   *    i    j         len = 3
   *    i      j       len = 4
   *            ij     len = 1
   *                ji len = 4
   *                   len = 4
   *
   *  exp 2)
   *
   *   step 1)
   *   nums = [0,3,7,2,5,8,4,6,0,1]
   *    -> sorted = [0,0,1,2,3,4,5,6,7,8]
   *
   *   step 2) 2 pointers
   *
   *
   *   [0,0,1,2,3,4,5,6,7,8]
   *   i  j                  len=1
   *      i j                len=2
   *      i   j              len=3
   *      i     j            len=4
   *      i      j           len=5
   *      i        j
   *      ....
   *
   *      i              j  len=9
   *
   *  exp 3)
   *
   *  nums = [1,2,0,1]
   *
   *  -> sorted :  [0,1,1,2]
   *
   */
  public int longestConsecutive(int[] nums) {

      // edge
      if(nums == null || nums.length == 0){
          return 0;
      }

      // use set, to only collect `non-duplicated` elements
      Set<Integer> set = new HashSet<>();
      for(int x: nums){
          set.add(x);
      }

      List<Integer> numList = new ArrayList<>();
      for(int x: set){
          numList.add(x);
      }

      // sort
      //Arrays.sort(nums);
      Collections.sort(numList);
      int res = 1;
      int i = 0;
      int j = 1;

      while(j < numList.size()){
          if(numList.get(j) == numList.get(j-1)+1){
              res = Math.max(res, j-i+1);
          }else{
              i = j;
          }
          j += 1;
      }

     return res;
  }

  // quick sort
  /**
   *  ALGO:
   *
   *  input: [1,3,2,5,4]
   *
   *  step 1) find `pivot`, can be random select
   *   -> choose `middle` element (3) as pivot
   *
   *  step 2) move `smaller than pivot` element to pivot left,
   *          move `bigger than pivot` element to pivot right,
   *          -> [1,2,   3,   5,4,7]
   *
   *  step 3) for each sub set, repeat step 1) - step 2)
   *         [ 1,2, 3,  4,5,7]
   *
   *  step 4) merge sorted sub set as final result
   *
   *        -> [ 1,2, 3, 4,5,7]
   *
   *
   *
   *  exp 1)
   *
   *  input: [1,3,3,5,4,99,100]
   *
   *  -> pivot = 5
   *
   *  -> [ 1,3,3,4,  5,  99,100]
   *
   *  -> [ 1,3,3,4,5,99,100]
   *
   *
   *
   *  exp 2)
   *
   *  input = [5,4,3,2,1]
   *
   *  -> [ 3,2,1,  4,  5]
   *  -> [1,2,3, 4, 5]
   *  -> [1,2,3,4,5]
   */
    public List<Integer> quickSort(Integer[] input){
        // edge
        if (input == null || input.length == 0){
            return new ArrayList<>();
        }

        // find pivot
        int l = 0;
        int r = input.length;
        int mid = (l + r) / 2;
        int pivot = input[mid];

        //Object x = Arrays.copyOfRange(input, 0, pivot);
        List<Integer> left = quickSort(Arrays.copyOfRange(input, 0, pivot));
        List<Integer> right = quickSort(Arrays.copyOfRange(input, pivot, input.length-1));

        // ????
        List<Integer> res = new ArrayList<>();
        for(Integer val: left){
            res.add(val);
        }

        for(Integer val: right){
            res.add(val);
        }

        return res; // ????
    }

    // LC 125
    // https://leetcode.com/problems/valid-palindrome/description/
    public boolean isPalindrome(String s) {

        String s_ = s
                .replace(" ", "")
                .replace(",", "")
                .replace(".", "")
                .replace(":", "");
        if(s_ == null || s_.isEmpty()){
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for(String x: s_.split("")){
            sb.append(x.toLowerCase());
        }


        String str = sb.toString();
        String str_rev = sb.reverse().toString();

        System.out.println(">>> str = " + str);
        System.out.println(">>> str_rev = " + str_rev);

        return str_rev.equals(str);
    }

    // LC 15
    // idea : for loop + 2 sums
    public List<List<Integer>> threeSum(int[] nums) {

        // edge
        if (nums == null || nums.length == 0 || nums.length < 3){
            return new ArrayList<>();
        }

        if (nums.length == 3){
            if(nums[0] + nums[1] + nums[2] == 0){
                List<List<Integer>> res = new ArrayList<>();
                List<Integer> tmp = new ArrayList<>();
                tmp.add(nums[0]);
                tmp.add(nums[1]);
                tmp.add(nums[2]);
                res.add(tmp);
                return res;
            }
            return new ArrayList<>();
        }

        // loop over 2 sums
        // {val: idx}
        // if x + y + z = 0
        // -> x + y = -z
        // -> so, all we need to do is to find if `-(x+y)` exists in map
        Map<Integer, Integer> map = new HashMap<>();
        List<List<Integer>> res = new ArrayList<>();

        for(int i = 0; i < nums.length; i++){
            int cur = nums[i];
            // 2 sum
            for(int j = i+1; j < nums.length; j++){
                List<Integer> tmp = new ArrayList<>();
                // x + y + z = 0
                // -> x + y = -z
                int x = nums[j];
                int target = -cur - x;
                if(map.containsKey(target) && map.get(target) != j){
                    tmp.add(cur);
                    tmp.add(x);
                    tmp.add(target);
                    res.add(tmp);
                }
                map.put(x, j);
            }

        }

        return res;
    }

    // LC 11
    /**
     *  IDEA: 2 POINTERS
     *
     *  Step 0) init left, right pointer
     *  Step 1) get cur area, then move the `smaller val` pointer
     *  Step 2) repeat above steps, and maintain the `biggest` area
     *  Step 3) return the biggest area as result
     *
     */
    public int maxArea(int[] height) {
        // edge
        if (height == null || height.length == 0){
            return 0;
        }
        if (height.length == 1){
            return Math.abs(height[1] - height[0]);
        }

        // 2 pointers
        int res = 0;
        int l = 0;
        int r = height.length - 1;
        while(r > l){
            int h = Math.min(height[l], height[r]);
            int w = r - l;
            res = Math.max(res, h * w);
            if(height[l] < height[r]){
                l += 1;
            }else{
                r -= 1;
            }
        }

        return res;
    }

    // LC 121
    /**
     *  IDEA: 2 POINTERS
     *  maintain local min, max, and global max
     *  (global max as result)
     *
     *
     *  EXP 1)
     *
     *  Input: prices = [7,1,5,3,6,4]
     *  Output: 5
     *
     *
     *  prices = [7,1,5,3,6,4]
     *            x
     *           l_min = 7
     *
     *
     *  prices = [7,1,5,3,6,4]
     *              x
     *              l_min = 1
     *
     *
     *   prices = [7,1,5,3,6,4]
     *                 x
     *                 l_min = 1
     *                 l_max = 5
     *                 profit = 4
     *                 g_profit = 4
     *
     *   prices = [7,1,5,3,6,4]
     *                   x
     *                   l_min = 1
     *                   l_max = 3
     *                   profit = 2
     *                   g_profit = 4
     *
     *
     *  prices = [7,1,5,3,6,4]
     *                    x
     *                    l_min = 1
     *                    l_max = 6
     *                    profit = 5
     *                    g_profit = 5
     *
     *   prices = [7,1,5,3,6,4]
     *                       x
     *                       l_min = 1
     *                       l_max = 4
     *                       profit = 3
     *                       g_profit = 5
     *
     *
     *
     *  -> res = 5
     *
     */
    public int maxProfit(int[] prices) {

        // edge
        if(prices == null || prices.length <= 1){
            return 0;
        }

        int globalProfit = 0;
        int localMin = -1;
        int localMax = -1;
        for(int x: prices){
            if(localMin == -1){
                localMin = x;
            } else if (x < localMin){
                localMin = x;
            }else{
                localMax = x;
                int profit = localMax - localMin;
                globalProfit = Math.max(globalProfit, profit);
            }
        }

        return globalProfit;
    }

    // LC 121
    /**
     *  IDEA: HASH MAP + 2 POINTERS
     *
     *
     *  EXP 1)
     *
     *  Input: s = "abcabcbb"
     *  -> Output: 3
     *
     *
     *  "abcabcbb"
     *   s
     *   f      l = 1
     *
     *   "abcabcbb"
     *    sf      l = 2
     *
     *
     *   "abcabcbb"
     *    s f      l = 3
     *
     *  "abcabcbb"
     *      sf      l = 3
     *
     *  "abcabcbb"
     *      s f      l = 3
     *
     *  "abcabcbb"
     *         sf    l = 3
     *
     *
     *
     *
     */
    public int lengthOfLongestSubstring(String s) {

        // edge
        if (s == null || s.isEmpty()){
            return 0;
        }

        // hash map
        // {val: idx}
        Map<String, Integer> map = new HashMap<>();
        // 2 pointers
        // check, update map if there is duplicated val, and update max length
        // if duplicated val, update start pointer
        int res = 0;
        int slow = 0; // slow pointer
        int fast = 0; // fast pointer
        while(fast < s.length()){
            String cur = String.valueOf(s.charAt(fast));
            System.out.println(">>> cur = " + cur + " fast = " + fast + " slow = " + slow + " map = " + map);
            if (map.containsKey(cur)){
                slow = fast;
                // reset map
                map = new HashMap<>(); // ???
            }else{
                res = Math.max(res, fast - slow+1);
                map.put(cur, fast);
            }
            fast += 1;
        }

        return res;
    }

  // LC 424
  // https://leetcode.com/problems/longest-repeating-character-replacement/
  // 4.08-4.23 pm
  /**
   *  IDEA: 2 POINTERS + HASHMAP ??
   *
   *  EXP 1)
   *
   *  Input: s = "ABAB", k = 2
   *  -> Output: 4
   *
   *
   *  s = "AAAB", k = 2
   *       ij      l = 2
   *
   *  s = "AAAB",
   *       i j   l = 3
   *
   *   s = "AAAA",
   *        i  j   l = 4
   *
   *  -> res = 4
   *
   *
   *  EXP 2)
   *
   *  Input: s = "AABABBA", k = 1
   *  -> Output: 4
   *
   *
   *  s = "AABABBA"
   *       ij       l = 2
   *
   * s = "AAAABBA"
   *      i j       l = 3
   *
   * s = "AAAABBA"
   *      i  j      l  = 4
   *
   * s = "AAAABBA"
   *      i   j     l = 4
   *
   *  (move i to `last different element idx`, and reset string)
   *
   *  s = "AABBBBA",
   *         ij     l 2
   *
   *  s = "AABBBBA",
   *         i j     l = 3
   *
   *  s = "AABBBBA",
   *         i  j     l = 4
   *
   *  s = "AABBBBA",
   *         i   j    l = 4, terminate program, since fast pointer reach the end
   *
   */
  public int characterReplacement(String s, int k) {

      int kCopy = k;

      // edge
      if (s.isEmpty()){
          return 0;
      }

      if(s.length() == 1){
          return 1;
      }

      if (k > s.length()){
          return s.length();
      }

      int res = 1;
      // queue that record last `different element` idx (FIFO)
      Queue<Integer> q = new LinkedList<>();

      // 2 pointers
      int slow = 0;
      for(int fast = 1; fast < s.length(); fast++){
          //int tmp = 1;
          String prev = String.valueOf(s.charAt(fast-1));
          String cur = String.valueOf(s.charAt(fast));
          System.out.println(">>> fast = " + fast + ", slow = " + slow + ", cur = " + cur +  ", prev = " + prev + " res = " + res + " k = " + k);

          if(prev.equals(cur)){
              //tmp += 1;
              res = Math.max(fast - slow + 1, res);
          }else{
              // record last `different element` idx
              q.add(fast);
              k -= 1;
              // if `k` still > 0
              if (k >= 0){
                  res = Math.max(fast - slow + 1, res);
              }// if `k` <= 0, reset string, and restart from last `different element` idx
              else{
                  slow = q.poll();
                  fast = slow + 1;
                  k = kCopy; // reset k
              }
          }
      }

        return res;
    }

}


