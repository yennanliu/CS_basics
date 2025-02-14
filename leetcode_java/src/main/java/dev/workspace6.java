package dev;


import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.LinkedList.InsertIntoACyclicSortedList;
import LeetCodeJava.Recursion.PopulatingNextRightPointersInEachNode2;
import LeetCodeJava.Recursion.SumOfLeftLeaves;
//import jdk.javadoc.internal.doclets.toolkit.util.Utils;

import javax.print.DocFlavor;
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
//    public boolean exist(char[][] board, String word) {
//      return false;
//    }
//


//    public boolean exist(char[][] board, String word) {
//
//      if (board.length == 1 && board[0].length == 1){
//          return String.valueOf(board[0][0]).equals(word);
//      }
//
//      // backtrack
//      Set<List<Integer>> visited = new HashSet<>();
//      int l = board.length;
//      int w = board[0].length;
//      for(int i = 0; i < l; i++){
//          for(int j = 0; j < w; j++){
//              StringBuilder sb = new StringBuilder();
//              if(canFind(board, word, j, i, sb, visited)){
//                  return true;
//              }
//          }
//      }
//
//      return false;
//    }

//    private boolean canFind(char[][] board, String word, int x, int y, StringBuilder cur, Set<List<Integer>> visited){
//      int l = board.length;
//      int w = board[0].length;
//      int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
//      if (cur.toString().length() == word.length() && cur.toString().equals(word)){
//          return true;
//      }
//      if (cur.toString().length()  > word.length()){
//          return false; // ??
//      }
//
//      for (int[] move: moves){
//          int x_ = x + move[0];
//          int y_ = y + move[1];
//          List<Integer> tmp = new ArrayList<>();
//          tmp.add(x);
//          tmp.add(y);
//          String tmpVal = String.valueOf(board[y_][x_]);
//          if (x_ >= 0 && x_ < w && y_ >= 0 &&
//                  y_ < l && !visited.contains(tmp) &&
//                  tmpVal.equals(word.charAt(cur.length()+1))){
//              visited.add(tmp);
//              cur.append(board[y][x]); // ???
//              canFind(board, word, x_, y_, cur, visited);
//              // undo
//              cur.deleteCharAt(cur.toString().length() - 1);
//              visited.remove(tmp); // ??
//          }
//      }
//
//      return false;
//    }


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

    // LC 20
    public boolean isValid(String s) {

      // edge
      if(s.isEmpty()){
          return true;
      }

      Map<String, String> map = new HashMap<>();
      map.put("(", ")");
      map.put("[", "]");
      map.put("{", "}");

      // stack: FIL0
      Stack<String> stack = new Stack<>();

      for(String x: s.split("")){
          if(stack.isEmpty() && ! map.containsKey(x)){
              return false;
          }
          if(map.containsKey(x)){
              stack.add(x);
          }
          else{
              String prev = stack.pop();
              if(!map.get(prev).equals(x)){
                  return false;
              }
          }
      }

      if(!stack.isEmpty()){
          return false;
      }

      return true;
    }

  // LC 153
  // 5.19 - 5.30 pm
  /**
   *  Suppose an array of length n sorted in `ascending`
   *  order is rotated between 1 and n times.
   *
   *
   *  For example,
   *  the array nums = [0,1,2,4,5,6,7] might become:
   *
   * [4,5,6,7,0,1,2] if it was rotated 4 times.
   * [0,1,2,4,5,6,7] if it was rotated 7 times.
   *
   *
   *  -> array is in `ascending` order originally
   *
   *
   * Given the sorted rotated array nums of unique elements,
   *
   * -> return the `minimum` element of this array.
   *
   * You must write an algorithm that runs in `O(log n)` time.
   *
   */
  /**
   *  IDEA : binary search
   *
   *  -> key : check `which part` of array is `ascending` (sorted)
   *
   *
   *  EXP 1)
   *
   *   [4,5,6,7,0,1,2]
   *    l     m     r      num(l) < nums(m), so left is ascending, so we know left part smallest is 4
   *
   *
   *
   *  EXP 2)
   *
   *  [2,4,5,6,7,0,1]   num(l) < nums(m), so left is ascending, and we can check if l is smaller than r or not
   *   l     m     r
   *
   *
   *  EXP 3)
   *
   *   nums = [3,4,5,1,2]
   *           l   m   r  `left` is sorted
   *
   *
   * EXP 4)
   *
   *   nums = [4,5,6,7,0,1,2]
   *           l     m     r
   */
//  public int findMin(int[] nums) {
//
//      // edge
//      if(nums == null || nums.length == 0){
//          return -1; //?
//      }
//
//      if (nums.length <= 3){
//          int res = 0;
//          for(int x : nums){
//              res = Math.min(x, res);
//          }
//          return res;
//      }
//
//      // 2 pointers
//      int res = Integer.MAX_VALUE; // ???
//      int l = 0;
//      int r = nums.length-1;
//      while (r > l){
//          int mid = (l + r) / 2;
//          // left part is sorted
//          // [4,5,6,7,0,1,2]
//          // [7,0,1,2,4,5,6]
//          //  l     m     r
//          //if (nums[mid] > nums[l]){
//          if (nums[mid] > nums[0]){ // NOTE !!! nums[0]
//              // if `r` is smaller than 1st element in left
//              //nums = [3,4,5,1,2]
//              if(nums[r] < nums[l]){
//                  l = mid + 1;
//              }else{
//                  r = mid - 1; // ???
//              }
//              //l = mid + 1;
//          }
//          // right part is sorted
//          else{
//             // r = mid-1;
//              // nums = [4,0,1,2,3]
//              if(nums[mid] > nums[l]){
//                  r = mid - 1; // ???
//              }else{
//                  l = mid + 1;
//              }
//          }
//      }
//
//      return res;
//    }

    // LC 206
    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode() {}
     *     ListNode(int val) { this.val = val; }
     *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    /**
     * IDEA : LINKED LIST
     *
     *  define prev, next node
     *  point cur to prev,
     *  move prev to cur
     *  move cur to next
     *
     */
    public ListNode reverseList(ListNode head) {
        // edge
        if (head == null || head.next == null){
            return head;
        }

        /**
         *  NOTE !!!!
         *
         *   4 operations
         *
         *    Step 0) set _prev as null
         *    step 1) cache next
         *    step 2) point cur to prev
         *    step 3) move prev to cur  (NOTE !!! the step)
         *    step 4) move cur to next
         */

        // 2 pointers
        // 1 -> 2 -> 3
        // prev  1 -> 2 -> 3
        // so,  prev <- 1
        // move prev to cur
        ListNode cur = head;
        ListNode _prev = null;
        while(cur != null){
            ListNode _next = cur.next;
            cur.next = _prev;
            _prev = cur;
            cur = _next; // ????
        }

        return _prev; // ??
    }

  // LC 92
  // 3.18 - 3.30 pm
  /**
   * Given the head of a singly linked list and two integers left and right
   * where left <= right,
   * reverse the nodes of the list from position left to position right,
   * and return the reversed list.
   *
   * EXP 1)
   *
   * Input: head = [1,2,3,4,5], left = 2, right = 4
   *  -> Output: [1,4,3,2,5]
   *  (reverse nodes within left and right ONLY)
   *
   *
   * IDEA 1): find left, right node, then do reverse linked list op
   *
   * IDEA 2): loop over node, if reach `left` then start doing reverse,
   *          STOP reverse op when reach `right` node
   *
   *  IDEA 3): go till left node, do reverse in [0, r-l] loop, then end the program
   */
  ListNode reverseBetween(ListNode head, int left, int right){

      // edge

      for(int i = 0; i < left; i++){
          head = head.next;
      }


      // 1 ->   2 -> 3 -> 4   -> 5, left = 2, right = 4,
      // so now head = 2
      // 1 ->   2 -> 3 -> 4   -> 5,
      //
      //ListNode _cur = head;
      //ListNode _next = head.next;

      for(int i = 0; i < right - left; i++){
          ListNode _next = head.next;
          ListNode _next_next = head.next.next;
          _next.next = head;
          head = _next;
          _next =_next_next; // ????
      }

      return head; // ???
  }
//  public ListNode reverseBetween(ListNode head, int left, int right) {
//
//      // edge
//      if (head == null || head.next == null){
//          return head;
//      }
//
//      if (right < left){
//          return head;
//      }
//
//      // IDEA 2):
//      ListNode _prev = null;
//      Boolean shouldReverse = false;
//
//      while(head != null){
//          ListNode _next = head.next;
//      if (head.equals(new ListNode(left))){
//              shouldReverse = true;
//          }
//          if (head.equals(new ListNode(right))){
//              shouldReverse = false;
//              _prev.next = head; // ????
//              break;
//          }
//          // ONLY do reverse if left is reached
//          if(shouldReverse){
//              head.next = _prev;
//              _prev = head;
//          }
//          if(!shouldReverse){
//              //_prev.next = head;
//              _prev = head; // ???
//          }
//          head = _next;
//      }
//
//        return _prev;
//    }

    // LC 21
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

      // edge
      if (list1 == null || list2 == null){
        if(list1 == null){
            return list2;
        }
        return list1;
      }

      ListNode node = new ListNode();
      ListNode head = node;

      while (list1 != null && list2 != null){
          if (list1.val <= list2.val){
              node.next = list1;
              //node = list1; // ???
              list1 = list1.next;
          }else{
              node.next = list2;
              //node = list2; // ???
              list2 = list2.next;
          }

          // NOTE !!! below
          node = node.next;
        }

      if (list1 != null){
          node.next = list1;
      } // ???
      else{
          node.next = list2;
      }

//      if (list2 != null){
//          node.next = list2;
//      }

      return head.next; // ?
    }

    // LC 141
    public boolean hasCycle(ListNode head) {
      // edge
      if(head == null || head.next == null){
          return false;
      }

      HashSet<ListNode> set = new HashSet<>();
      while(head != null){
          if(set.contains(head)){
              return true;
          }
          set.add(head);
          head = head.next;
      }

      return false;
    }

  // LC 143
  // 5.20 - 5.40 pm
  /**
   *
   * You are given the head of a singly linked-list.
   *
   * The list can be represented as:
   *
   * L0 → L1 → … → Ln - 1 → Ln
   * Reorder the list to be on the following form:
   *
   * L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
   * You may not modify the values in the list's nodes.
   * Only nodes themselves may be changed.
   *
   *
   *
   * -> e.g.:
   *
   *   L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ....
   *
   *
   *   step 1) find `mid` point (slow, fast pointer)
   *
   *    L0 -> L1 -> ... Ln/2-> ..... Ln
   *
   *   step 2)  reverse  Ln/2-> ..... Ln sub node
   *
   *   step 3) go over L0 -> ... Ln/2, Ln/2-> ..... Ln
   *           and reconnect nodes as below:
   *           L0 -> Ln -> L1 -> Ln-1 -> ....
   *
   *
   *  EXP 1)
   *
   *  Input: head = [1,2,3,4]
   *  -> Output: [1,4,2,3]
   *
   *
   *
   *  EXP 2)
   *
   *  Input: head = [1,2,3,4,5]
   *  -> Output: [1,5,2,4,3]
   *
   */
  public void reorderList(ListNode head) {
      // edge ??
      if(head == null || head.next == null){
          //return head;
          return;
      }

      // step 1) find `mid` node
      ListNode slow = head;
      ListNode fast = head;
      while(fast != null && fast.next != null){
          fast = fast.next.next;
          slow = slow.next;
      }

      // so, now `slow` is `mid` node

      // step 2) reverse node
      ListNode reverseSlow = reverseNode_(slow);

      // step 3) reconnect
      //ListNode res = new ListNode();
      ListNode head_ = head;
      //res.next = head_;

      /**
       *
       *   L0 -> L1 -> ... Ln/2
       *
       *   Ln/2+1 -> .... Ln
       *
       *
       */
      ListNode firstHalf = head_;
      ListNode secondHalf = reverseSlow;

      // while(head_ != slow && reverseSlow != null){
      while(secondHalf != null){

//          ListNode _headNext = head.next;
//          ListNode _reverseFastNext = secondHalf.next;
//
//          head_.next = secondHalf;
//          head_ = secondHalf;
//
//          secondHalf.next = _headNext; // ??
//          secondHalf = _headNext; // ???

          ListNode _firstHalfNext = firstHalf.next;
          ListNode _secondHalfNext = secondHalf.next;


          firstHalf.next = secondHalf;
          firstHalf = _firstHalfNext; // ?


//          secondHalf.next = _firstHalfNext;
//          secondHalf = _secondHalfNext; // ?

          firstHalf = _firstHalfNext;
          secondHalf = _secondHalfNext;
      }

  }

  private ListNode reverseNode_(ListNode node){
      //ListNode res = new ListNode();
      //res.next = node;
      ListNode _prev = new ListNode();
      while(node != null){
          ListNode _next = node.next;
          node.next = _prev;
          _prev = node;
          node = _next;
      }
      return _prev; // ??
  }

  // LC 19
  /**
   *  IDEA: fast, slow pointer
   *
   *  EXP 1)
   *
   *  Input: head = [1,2,3,4,5], n = 2
   *  -> Output: [1,2,3,5]
   *
   *  n = 2 (from end)
   *  -> so we need to iterate head.size - 2 = 5 - 2 = 3
   *
   *  [1,2,3,4,5]
   *       i
   *
   *  then reconeect nodes
   *
   */
  public ListNode removeNthFromEnd(ListNode head, int n) {

      // edge
      if(head == null){
          return head;
      }

      if(head.next == null){
          //return new ListNode();
          return null;
    }

      ListNode cur = new ListNode();
      cur.next = head;
      ListNode tmp = head;

      // get node len
      int len = 0;
      while(tmp != null){
          len += 1;
          tmp = tmp.next;
      }

      // edge case 2
      // if n == len
      if (n == len){
          head = head.next;
          cur.next = head;
          cur = cur.next;
      }

      int idx = len - n; // NOTE !!! we need to visit `previous` node,

      System.out.println(">>> len = " + len + ", idx = " + idx);

      for(int i = 0; i < idx; i++){
          head = head.next;
          //idx -= 1;
      }

      if(head.next != null){
          ListNode _nextOfNext = head.next.next;
          head.next = _nextOfNext;
          head = _nextOfNext;
      }

      return cur.next; // ???
  }

  // LC 226
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
   /**
    *  IDEA: DFS
    *
    *
    *
    *
    *
    */
  public TreeNode invertTree(TreeNode root) {

      //edge
      if(root == null || (root.left == null && root.right == null)){
          return root;
      }
      //dfs
      TreeNode reversedRoot = reverseTree(root);
      System.out.println(">>> reversedRoot = " + reversedRoot);

      return reversedRoot;
  }

  private TreeNode reverseTree(TreeNode root){
      if(root == null){
          return root;
      }

      // cache `reversed right tree`, so this object remains unchanged, and can be used in following code
      TreeNode tmpRight = reverseTree(root.right);
      root.right = reverseTree(root.left);
      root.left = tmpRight;

      return root;
  }

  // LC 104
//  public int maxDepth(TreeNode root) {
//      // edge
//      if (root == null){
//          return 0;
//      }
//      // dfs
//      int res = getMaxDepth(root, 1);
//      System.out.println(">>> res = " + res);
//      return res;
//  }
//
//  private TreeNode getMaxDepth(TreeNode root, int depth){
//      if (root == null){
//          return null;
//      }
//      if(root.left != null){
//          root.left =  getMaxDepth(root.left, depth+1);
//      }
//      if(root.right != null){
//          root.right =  getMaxDepth(root.right, depth+1);
//      }
//
//      //int leftMax = getMaxDepth(root.left)
//      //return Math.max(getMaxDepth(root.left), getMaxDepth(root.right));
//  }


    // LC 100
    public boolean isSameTree_(TreeNode p, TreeNode q) {
      // edge
//      if (p != null || q != null){
//          return false;
//      }
        if ((p == null && q != null) || (p != null && q == null)){
            return false;
        }

        // dfs
      return isSame(p, q);
    }

    private boolean isSame(TreeNode p, TreeNode q){
        if ((p == null && q != null) || (p != null && q == null)){
            return false;
        }
        if(p.val != q.val){
            return false;
        }
        if(( p.left != null && q.left == null) ||
                ( p.left == null && q.left != null) ||
                ( p.right != null && q.right == null) ||
                ( p.right == null && q.right != null)
        ){
            return false;
        }
        return (isSame(p.left, q.left)) && (isSame(p.right, q.right));
    }

    // LC 572
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
      // edge
      if(root == null && subRoot == null){
          return true;
      }
      // bfs // ???
      Queue<TreeNode> q = new LinkedList<>();
      q.add(root);
      while(!q.isEmpty()){
          TreeNode cur = q.poll();
          if(isSameTree(cur, subRoot)){
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

    private boolean isSameTree(TreeNode p, TreeNode q){
        if(p == null && q == null){
            return true;
        }
       if (p == null || q == null){
           return false;
       }
       if(p.val != q.val){
           return false;
       }
       return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    // LC 33
    /**
     *  33. Search in Rotated Sorted Array
     * Solved
     * Medium
     * Topics
     * Companies
     * There is an integer array nums sorted in ascending order (with distinct values).
     *
     * Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].
     *
     * Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.
     *
     * You must write an algorithm with O(log n) runtime complexity.
     *
     *
     *
     * Example 1:
     *
     * Input: nums = [4,5,6,7,0,1,2], target = 0
     * Output: 4
     * Example 2:
     *
     * Input: nums = [4,5,6,7,0,1,2], target = 3
     * Output: -1
     * Example 3:
     *
     * Input: nums = [1], target = 0
     * Output: -1
     *
     *
     * Constraints:
     *
     * 1 <= nums.length <= 5000
     * -104 <= nums[i] <= 104
     * All values of nums are unique.
     * nums is an ascending array that is possibly rotated.
     * -104 <= target <= 104
     */
    /**
     *  IDEA: BINARY SEARCH
     *
     */
    public int search(int[] nums, int target) {
      // edge
      if(nums == null || nums.length == 0){
          return -1;
      }
      if(nums.length == 1){
          if(nums[0] == target){
              return 0;
          }
          return -1;
      }
      // binary search
      int res = -1;
      int l = 0;
      int r = nums.length -1;
      while(r >= l){
          int mid = (l + r) / 2;
          // case 1) if `mid` is part of `left sub array`
          // case 2) if `mid is part of `right sub array`

      }


      return res;
    }

    // LC 153
    public int findMin(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return -1;
        }
        if(nums.length == 1){
            return nums[0];
        }
        // edge : already in ascending order or all elements are the same
        // [1,2,3,4,5]
        if(nums[nums.length - 1] >= nums[0]){
            return nums[0];
        }
        // binary search
        int res = nums[0]; //Integer.MAX_VALUE;
        int l = 0;
        int r = nums.length -1;
        while(r >= l){
            int mid = (l + r) / 2;
            int cur = nums[mid];


            // ?????
            // edge case : is array already in increasing order (e.g. [1,2,3,4,5])
//            if (nums[l] < nums[r]) {
//                res = Math.min(res, nums[l]);
//                break;
//            }

            res = Math.min(res, Math.min(cur, nums[l]));

            // case 1) if `mid` is part of `left sub array`
            // search  right ->
            // [3,4,5,1,2], mid = 5
            if(cur >= nums[l]){
                l = mid + 1;
            }else{
                // case 2) if `mid is part of `right sub array`
                // search left <-
                // [5,1,2,3,4], mid = 2
                r = mid - 1; // ??
            }
        }

        return res;
    }

    // LC 230
    List<Integer> treeVal = new ArrayList<>();
    public int kthSmallest(TreeNode root, int k) {

        //edge
        if(root == null){
            return -1; // ?
        }
        if(root.left == null &&  root.right == null){
            return root.val;
        }

        System.out.println(">>> (before) treeVal = " + treeVal);
        // get all tree
        //List<Integer> treeVal = new ArrayList<>();
        getTreeVal(root);
        System.out.println(">>> (after) treeVal = " + treeVal);

        // reorder (decreasing order)
        Collections.sort(treeVal);

        return treeVal.get(k-1);
    }

    private void getTreeVal(TreeNode root){
        if(root == null){
            return; // ??
        }
        // pre-order traversal
        treeVal.add(root.val);
        getTreeVal(root.left);
        getTreeVal(root.right);
    }

  // LC 98
  // 2.05 pm - 2.25 pm
  /**
   * IDEA: DFS
   *
   * -> keep checking nodes if left <= root <= right
   *    and its sub tree, if any violation, return false directly
   *    otherwise, return true
   *
   *  EXP 1)
   *
   *  Input: root = [2,1,3]
   *  -> Output: true
   *
   *        2
   *      1   3
   *
   *   -> for 2, 1 <= 2 <= 3
   *   -> for 1, no sub root
   *   -> for 3, no sub root
   *   -> return true
   *
   *
   *  EXP 2)
   *
   *  Input: root = [5,1,4,null,null,3,6]
   *  -> Output: false
   *
   *       5
   *    1   4
   *      3   6
   *
   *
   *   for 5,  1 <= 5 !<= 4 -> return fasle
   *   -> since one node breaks BST, no need to traverse more
   *   -> return false directly
   *
   *
   *  -> key !!!
   *  -> maintain `biggest` left sub node
   *            and `smallest` right sub node
   *  -> so we're sure if current node is BST and its sub nodes are BST
   *  -> via    if  biggest_left_sub.val  <= root.val <= smallest_sub_right.val
   *
   */
  public boolean isValidBST(TreeNode root) {
        // edge
        if(root == null || (root.left == null && root.right == null)){
            return true;
        }

        //return false;
        long smallest_val = Long.MIN_VALUE;
        long biggest_val = Long.MAX_VALUE;
        return isValidateBST(root, biggest_val, smallest_val); // ????
    }

    private boolean isValidateBST(TreeNode root, long biggestLeftNode, long smallestLeftNode){

      // ???
        if(root == null){
            return true; // ??
        }
//        if(root.val >= smallestLeftNode || root.val <= biggestLeftNode){
//            return false;
//        }
        if(root.val <= smallestLeftNode || root.val >= biggestLeftNode){
            return false;
        }

        boolean leftRes = false;
        boolean rightRes = false;

        if(root.left != null){
//            leftRes = isValidateBST(root.left,
//                    Math.max(root.left.val, biggestLeftNode), smallestLeftNode
//            );
            leftRes = isValidateBST(root.left,
                    Math.max(root.left.val, biggestLeftNode), root.val
            );
        }

        if(root.right != null){
//            rightRes = isValidateBST(root.right,
//                    biggestLeftNode, Math.min(root.right.val, smallestLeftNode)
//            );
            rightRes = isValidateBST(root.right,
                    biggestLeftNode, Math.min(root.right.val, smallestLeftNode)
            );
        }

        // ONLY `ALL` node and their sub nodes are BST, then can return ture
        return leftRes && rightRes; // ???
    }

    // LC 102
    // IDEA: BFS
//    public class MyTreeLayer{
//      // attr
//      int layer;
//      TreeNode root;
//      // constructor
//      MyTreeLayer(int layer, TreeNode root){
//            this.layer = layer;
//            this.root = root;
//        }
//      // getter, setter
//        public int getLayer() {
//            return layer;
//        }
//
//        public void setLayer(int layer) {
//            this.layer = layer;
//        }
//
//        public TreeNode getRoot() {
//            return root;
//        }
//
//        public void setRoot(TreeNode root) {
//            this.root = root;
//        }
//    }
//    public List<List<Integer>> levelOrder(TreeNode root) {
//      // edge
//      if(root == null){
//          return new ArrayList<>();
//      }
//      if (root.left == null && root.right == null){
//          List<Integer> tmp = new ArrayList<>();
//          tmp.add(root.val);
//          List<List<Integer>> res = new ArrayList<>();
//          res.add(tmp);
//          return res;
//      }
//
//     List<List<Integer>> res = new ArrayList<>();
//      // bfs
//      Queue<MyTreeLayer> q = new LinkedList<>();
//      q.add(new MyTreeLayer(0, root));
//      while(!q.isEmpty()){
//
//          MyTreeLayer myTreeLayer = q.poll();
//          int depth = myTreeLayer.getLayer();
//          TreeNode curRoot = myTreeLayer.getRoot();
//
//          if(res.size() < depth+1){
//              res.add(new ArrayList<>());
//          }
//
//         // List<Integer> tmp = new ArrayList<>();
//         //tmp.add(root.val);
//
////          List<Integer> tmp = res.get(depth);
////          tmp.add(curRoot.val);
////          res.add(depth, tmp); // /??
//
//          //   res.get(layer).add(cur.val);
//          res.get(depth).add(curRoot.val); // ???
//
//          if(curRoot.left != null){
//              q.add(new MyTreeLayer(depth+1, curRoot.left));
//          }
//          if(curRoot.right != null){
//              q.add(new MyTreeLayer(depth+1, curRoot.right));
//          }
//
//      }
//
//      System.out.println(">>> res = " + res);
//
//      return res;
//    }

    // LC 107
    public class MyTreeLayer{
        // attr
        int layer;
        TreeNode root;
        // constructor
        MyTreeLayer(int layer, TreeNode root){
            this.layer = layer;
            this.root = root;
        }
        // getter, setter
        public int getLayer() {
            return layer;
        }

        public void setLayer(int layer) {
            this.layer = layer;
        }

        public TreeNode getRoot() {
            return root;
        }

        public void setRoot(TreeNode root) {
            this.root = root;
        }
    }

    public List<List<Integer>> levelOrderBottom(TreeNode root) {

        // edge
        if(root == null){
            return new ArrayList<>();
        }
        if (root.left == null && root.right == null){
            List<Integer> tmp = new ArrayList<>();
            tmp.add(root.val);
            List<List<Integer>> res = new ArrayList<>();
            res.add(tmp);
            return res;
        }

        List<List<Integer>> res = new ArrayList<>();
        // bfs
        Queue<MyTreeLayer> q = new LinkedList<>();
        q.add(new MyTreeLayer(0, root));
        while(!q.isEmpty()){

            MyTreeLayer myTreeLayer = q.poll();
            int depth = myTreeLayer.getLayer();
            TreeNode curRoot = myTreeLayer.getRoot();

            if(res.size() < depth+1){
                res.add(new ArrayList<>());
            }
            res.get(depth).add(curRoot.val); // ???

            if(curRoot.left != null){
                q.add(new MyTreeLayer(depth+1, curRoot.left));
            }
            if(curRoot.right != null){
                q.add(new MyTreeLayer(depth+1, curRoot.right));
            }

        }

//        System.out.println(">>> res (before sorting) = " + res);

        // sort on idx (`big` idx first, then small ...)
//        Collections.sort(res, new Comparator<List<Integer>>() {
//            @Override
//            public int compare(List<Integer> o1, List<Integer> o2) {
//
//                return 0;
//            }
//        });

        List<List<Integer>> res2 = new ArrayList<>();
        // reverse loop over res
        for(int i = res.size() - 1; i >= 0; i--){
            res2.add(res.get(i));
        }

        System.out.println(">>> res = " + res);
        System.out.println(">>> res2 = " + res2);

        return res2;
    }

    // LC 235
    // 3.21 pm - 3.35 pm
    /**
     *  if both go right -> find right sub root
     *  if both go left -> find left sub root
     *  if one goes left, one goes right, root is the LCA
     *
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
      // edge
      if(root == null){
          return root; // ?
      }
      if(p == q){
          return root; // ???
      }
      // ???
//      if(q.val == root.val || p.val == root.val){
//          return root; // ???
//      }

        // if root equals p or q, return root as LCA
        if (root.equals(p) || root.equals(q)) {
            return root;
        }

      // /??
      return findLCA(root, root, p, q); // ancestor ???
    }

    private TreeNode findLCA(TreeNode root, TreeNode ancestor, TreeNode p, TreeNode q){
        // edge /??
        if(root == null){
            return ancestor; // ?
        }
//        if(q.val == root.val || p.val == root.val){
//            return ancestor; // ???
//        }
        // if root equals p or q, return root as LCA
        if (root.equals(p) || root.equals(q)) {
            return root;
        }

        // case 1) p, q at the `different side` of root
        // e.g.  [left, right] or [right, left]
        if((root.val > p.val && root.val < q.val)
         || (root.val < p.val && root.val > q.val) ){
            return ancestor;
        }

        // case 2) both p, and q < root.val
        // -> root is too big
        // -> move to `left` sub tree
        if(root.val > p.val && root.val > q.val){
            //findLCA(root.left, ancestor, p, q);
            return findLCA(root.left, ancestor, p, q);
        }

        // case 2) both p, and q > root.val
        // -> root is too small
        // -> move to `right` sub tree
        if(root.val < p.val && root.val < q.val){
           // findLCA(root.right, ancestor, p, q);
            return findLCA(root.right, ancestor, p, q);
        }

        return ancestor;
    }

    // LC 39
    // IDEA: BACKTRACK
    /**
     *
     *  Example 1:
     *
     * Input: candidates = [2,3,6,7], target = 7
     * Output: [[2,2,3],[7]]
     * Explanation:
     * 2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
     * 7 is a candidate, and 7 = 7.
     * These are the only two combinations.
     * Example 2:
     *
     * Input: candidates = [2,3,5], target = 8
     * Output: [[2,2,2,2],[2,3,3],[3,5]]
     * Example 3:
     *
     * Input: candidates = [2], target = 1
     * Output: []
     *
     *
     */

//    List<List<Integer>> comSumRes = new ArrayList<>();
//    public List<List<Integer>> combinationSum(int[] candidates, int target) {
//
//        // edge
//        if(candidates == null || candidates.length == 0){
//            return new ArrayList<>();
//        }
//
//        if(candidates.length == 1 && candidates[0] > target){
//            return new ArrayList<>();
//        }
//
//        // backtrack
//        System.out.println(">>> (before) comSumRes = " + comSumRes);
//        //List<Integer> tmp = new ArrayList<>();
//        backTrack(candidates, target, new ArrayList<>(), 0);
//        System.out.println(">>> (after) comSumRes = " + comSumRes);
//        return comSumRes;
//    }
//
//    private void backTrack(int[] candidates, int target, List<Integer> cache, int idx){
//        if(candidates == null || candidates.length == 0){
//            return;
//        }
//        // == or equals
//        if(getSum(cache) == target){
//            // sort
//            Collections.sort(cache);
//            if(!comSumRes.contains(cache)){
//                comSumRes.add(new ArrayList<>(cache));
//                //cache = new ArrayList<>(); // ???
//                return;
//            }
//        }
//        if(getSum(cache) > target){
//            //cache = new ArrayList<>(); // ???
//            return;
//        }
//
//        // loop
//        for(int i = 0; i < candidates.length; i++){
//            cache.add(candidates[i]);
//            if(getSum(cache) <= target){
//                backTrack(candidates, target, cache, i); // set i so can reuse current element (idx)
//            }
//            // undo
//            cache.remove(cache.size()-1); // ?? remove last element
//        }
//    }
//
//    private int getSum(List<Integer> cache){
//        int res = 0;
//        for(int x : cache){
//            res += x;
//        }
//        return res;
//    }

    // LC 40
//    List<List<Integer>> comSumRes = new ArrayList<>();
//    //HashSet<Integer> usedIdx = new HashSet<>();
//    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
//
//        // Edge case check for empty array or null input
//        if (candidates == null || candidates.length == 0) {
//            return new ArrayList<>();
//        }
//
//        HashSet<Integer> usedIdx = new HashSet<>();
//
//        // Backtracking step
//        backTrack(candidates, target, new ArrayList<>(), 0, usedIdx);
//        return comSumRes;
//
//    }
//
//    private void backTrack(int[] candidates, int target, List<Integer> cache, int startIdx, HashSet<Integer> usedIdx) {
//        int sum = getSum(cache);
//
//        // If the sum of the current combination equals target, add it to the result
//        if (sum == target) {
//            comSumRes.add(new ArrayList<>(cache)); // Store a copy to avoid reference issues
//            return;
//        }
//
//        // If the sum exceeds target, stop the recursion
//        if (sum > target) {
//            return;
//        }
//
//        // Loop over the candidates starting from the current index to avoid duplicates
//        for (int i = startIdx; i < candidates.length; i++) {
//            // element can only be used ONCE
//            if(!usedIdx.contains(i)){
//                usedIdx.add(i);
//                cache.add(candidates[i]); // Choose current candidate
//                backTrack(candidates, target, cache, i, usedIdx); // Recurse with the same index to allow repetition
//                usedIdx.remove(i); // ???
//                cache.remove(cache.size() - 1); // Undo the choice
//            }
//        }
//    }
//
//    private int getSum(List<Integer> cache) {
//        int res = 0;
//        for (int x : cache) {
//            res += x;
//        }
//        return res;
//    }


    List<List<Integer>> comSumRes = new ArrayList<>();

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        // Edge case check for empty array or null input
        if (candidates == null || candidates.length == 0) {
            return new ArrayList<>();
        }

        // Sort candidates to handle duplicates efficiently
        Arrays.sort(candidates);

        HashSet<Integer> usedIdx = new HashSet<>();
        // Backtracking step
        backTrack(candidates, target, new ArrayList<>(), 0, usedIdx);
        return comSumRes;
    }

    private void backTrack(int[] candidates, int target, List<Integer> cache, int startIdx, HashSet<Integer> usedIdx) {
        int sum = getSum(cache);

        // If the sum of the current combination equals target, add it to the result
        if (sum == target) {
            // ???
            Collections.sort(cache);
            if(!comSumRes.contains(cache)){
                comSumRes.add(new ArrayList<>(cache)); // Store a copy to avoid reference issues
            }
            return;
        }

        // If the sum exceeds target, stop the recursion
        if (sum > target) {
            return;
        }

        // Loop over the candidates starting from the current index
        for (int i = startIdx; i < candidates.length; i++) {
            // Skip duplicates: if the current element is the same as the previous, skip it
//            if (i > startIdx && candidates[i] == candidates[i - 1]) {
//                continue;
//            }

            if (usedIdx.contains(i)) {
                continue;
            }

            usedIdx.add(i);
            cache.add(candidates[i]); // Choose current candidate
            backTrack(candidates, target, cache, i + 1, usedIdx); // Move to the next index (no repetition of the same element)
            usedIdx.remove(i);
            cache.remove(cache.size() - 1); // Undo the choice (backtrack)
        }
    }

    private int getSum(List<Integer> cache) {
        int res = 0;
        for (int x : cache) {
            res += x;
        }
        return res;
    }


    // LC 1057
    // 7.06 - 7.26 pm
    /**
     *  EXP 1)
     *
     *   Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
     *   Output: [3,9,20,null,null,15,7]
     *
     *  EXP 2)
     *   Input: preorder = [-1], inorder = [-1]
     *   Output: [-1]
     *
     *          3
     *      9      20
     *           15   7
     *
     *
     *  IDEA: RECURSION + Preorder and Inorder Traversal property
     *
     *   -> preorder: root -> left -> right
     *   -> so we know root is the 1st element
     *
     *   -> inOrder: left -> root -> left
     *   -> so we can split the tree via root val index
     *
     *   -> and the `width` = root - left
     *
     *   -> so we can recursion call buildTree and
     *     define the following
     *
     *     for left subtree:
     *        preorder = [1, root_idx] ??
     *        inorder = [0, root_idx]
     *
     *    for right subtree:
     *         preorder = [root_idx+1, len-1] ??
     *         inorder = [root_idx+1, len-1] ??
     *
     *
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {

        // edge
        if(preorder.length == 0 || inorder.length ==0){
            return null;
        }
        if(preorder.length == 1 && inorder.length ==1){
            return new TreeNode(preorder[0]);
        }

        // recursion
        TreeNode res = new TreeNode(preorder[0]);
        // ???
        res = buildT(preorder, inorder, res);
        System.out.println(">>> res = " + res);
        return res;
    }

    private TreeNode buildT(int[] preorder, int[] inorder, TreeNode res){
        if(preorder.length == 0 && inorder.length ==0){
            return null;
        }
        if(preorder.length == 1 && inorder.length ==1){
            return new TreeNode(preorder[0]);
        }

        int root = preorder[0];
        int rootIdxInorder = getValIdx(inorder, root);
        System.out.println(">>> rootIdxInorder = " + rootIdxInorder);

        res.left = buildT(
                Arrays.copyOfRange(preorder, 1, rootIdxInorder+1),
                Arrays.copyOfRange(inorder, 0, rootIdxInorder),
                res
        );
        res.right = buildT(
                Arrays.copyOfRange(preorder, rootIdxInorder+1, preorder.length),
                Arrays.copyOfRange(inorder, rootIdxInorder+1, inorder.length),
                res
        );

        return res; // ???
    }

    private int getValIdx(int[] values, int val){
        for(int i = 0; i < values.length; i++){
            if (values[i] == val){
                return i;
            }
        }
        return -1;
    }

    // LC 124
    // 10.36 - 10.50 pm
    /**
     *  The path sum of a path is the sum of the node's values in the path.
     *
     *  Given the root of a binary tree, return the maximum path sum of any non-empty path.
     *
     *
     *  IDEA: RECURSION ??
     *  -> record `sub tree` for each node, so can get path sum, and we can get global max path sum
     *
     */
    public class NodeAndPath{
        // attr
        TreeNode node;
        List<Integer> path;
        // constructor
        NodeAndPath(TreeNode node, List<Integer> path){
            this.node = node;
            this.path = path;
        }
        // getter, setter
        public TreeNode getNode() {
            return node;
        }

        public void setNode(TreeNode node) {
            this.node = node;
        }

        public List<Integer> getPath() {
            return path;
        }

        public void setPath(List<Integer> path) {
            this.path = path;
        }
    }
    public int maxPathSum(TreeNode root) {

        // edge
        if(root == null || (root.left == null && root.right == null)){
            return root.val;
        }

        Map<TreeNode, List<Integer>> map = new HashMap<>();

        // bfs
        Queue<NodeAndPath> q = new LinkedList<>();
        q.add(new NodeAndPath(root, new ArrayList<>()));
        while(!q.isEmpty()){

            NodeAndPath nodeAndPath = q.poll();
            TreeNode node = nodeAndPath.getNode();
//            List<Integer> path = map.getOrDefault(node, new ArrayList<>());
//            path.add(node.val);
//            map.put(node, path);

            if(node.left != null){
//                List<TreeNode> tmp = new ArrayList<>(nodeAndPath.getPath());
//                tmp.add(node.left);
                List<Integer> path = map.getOrDefault(node, new ArrayList<>());
                path.add(node.val);
                map.put(node, path);
                q.add(new NodeAndPath(node.left, path));
            }
            if(node.right != null){
//                List<TreeNode> tmp = new ArrayList<>(nodeAndPath.getPath());
//                tmp.add(node.left);
                List<Integer> path = map.getOrDefault(node, new ArrayList<>());
                path.add(node.val);
                map.put(node, path);
                q.add(new NodeAndPath(node.right, path));
            }
        }

        System.out.println(">>> map = " + map);
        // sort map on path sum (decreasing order)
        int res = -1 * Integer.MAX_VALUE;
        for(List<Integer> x: map.values()){
            res = Math.max(res, getListSum(x));
        }

        return res;
    }

    private int getListSum(List<Integer> cache) {
        int res = 0;
        for (int x : cache) {
            res += x;
        }
        return res;
    }

    // LC 79
    // 7.30 - 7.50 pm
    public boolean exist(char[][] board, String word) {
        // edge
        if((board.length == 0 || board[0].length == 0) && word.length() > 0){
            return false;
        }
        // DFS
        int l = board.length;
        int w = board[0].length;
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                boolean[][] visited = new boolean[l][w];
                if(board[i][j] == word.charAt(0) && canFind(board, word, j, i, visited, 0)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean canFind(char[][] board, String word, int x, int y, boolean[][] visited, int idx){
        int l = board.length;
        int w = board[0].length;

        if(idx == word.length() - 1){
            return true;
        }

        if(idx >= word.length()){
            return false;
        }

        // check if not valid
        if(x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || board[y][x] != word.charAt(idx)){
            return false;
        }

        visited[y][x] = true;

        int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

//        for(int[] dir: dirs){
//            int x_ = x + dir[0];
//            int y_ = y + dir[1];
//            return canFind(board, word, x_, y_, visited, idx+1);
//        }

        if (canFind(board, word, x+1, y, visited, idx+1) ||
                canFind(board, word, x-1, y, visited, idx+1) ||
                canFind(board, word, x, y+1, visited, idx+1) ||
                canFind(board, word, x, y-1, visited, idx+1)){
            return true;
        }

        // backtrack
        visited[y][x] = false;

        return false;
    }


//    public boolean exist(char[][] board, String word) {
//        // edge
//        if(board.length == 0 || board[0].length == 0){
//            return false;
//        }
//
//        if(board.length == 1 && board[0].length == 1){
//            return String.valueOf(board[0]).equals(word); // ??
//        }
//
//        int l = board.length;
//        int w = board[0].length;
//
//        boolean[][] visited = new boolean[l][w];
//
//        for(int i = 0; i < w; i++){
//            for(int j = 0; j < l; j++){
//                // new boolean[l][w] ???
//                if( board[j][i] == word.charAt(0) && canFind(board, word, i, j, 0, visited)){
//                    return true;
//                };
//            }
//        }
//
//        return false;
//    }
//
//    private boolean canFind(char[][] board, String word, int x, int y, int idx, boolean[][] visited){
//
//        // NOTE !!! check idx == word len at beginning
//        // ?? put here or before above check ??
//        if(idx == word.length()){
//            return true;
//        }
//
//        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
//
//        int l = board.length;
//        int w = board[0].length;
//
//        // NOTE !!! validate first
//        if(x < 0 || x >= w || y < 0 || y >= l || board[y][x] != word.charAt(idx) || visited[y][x]){
//            return false;
//        }
//
////        // ?? put here or before above check ??
////        if(idx == word.length()){
////            return true;
////        }
//
//        // NOTE !!! mark as `visited` after validaiton
//        visited[y][x] = true;
//
//        // move over 4 dirs
//        for(int[] m: moves){
//            int x_ = x + m[0];
//            int y_ = y + m[1];
//            //visited[y_][x_] = true; // ???
//            if(canFind(board, word, x_, y_, idx+=1, visited)){
//                return true;
//            }
//            // undo
//            //visited[y_][x_] = false;
//            idx -=1;
//        }
//
//        visited[y][x] = false;
//
//        return true;
//    }


    /**
     *  IDEA: DFS + BACKTRACK ???
     *
     */
    //List<Boolean> results = new ArrayList<>(); // ???
//    public boolean exist(char[][] board, String word) {
//
//        // edge
//        if(board.length == 0 || board[0].length == 0){
//            return false;
//        }
//
//        if(board.length == 1 && board[0].length == 1){
//            return String.valueOf(board[0]).equals(word); // ??
//        }
//
//        int l = board.length;
//        int w = board[0].length;
//
//        // dfs + backtrack
//        for (int i = 0; i < l; i++){
//            for(int j = 0; j < w; j++){
//                if(containWords(board, word, new StringBuilder(), j, i, new HashSet<>(), 0)){
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

//    private boolean containWords(char[][] board, String word, StringBuilder cache, int x, int y, HashSet<List<Integer>> visited, int idx){
//
//        int l = board.length;
//        int w = board[0].length;
//
//        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
//
//        String curStr = cache.toString();
//
//        if(curStr.length() == word.length() && curStr.equals(word)){
//            //return true; // found, return true directly
//            //results.add(true);
//            return true;
//        }
//
//        if(cache.length() > word.length()){
//            //return false; // ?
//            //return;
//            return false;
//        }
//
//        if(board[y][x] != word.charAt(idx)){
//            return false;
//        }
//
//        for(int[] move: moves){
//            int x_ = x + move[0];
//            int y_ = y + move[1];
//            List<Integer> tmp = new ArrayList<>();
//            tmp.add(x_);
//            tmp.add(y_);
//            String val = String.valueOf(board[y_][x_]);
//            if (x_ >= 0 && x_ < w && y_ >= 0 && y < l && !visited.contains(tmp) && val.equals(String.valueOf(word.charAt(idx)))){
//                cache.append(val);
//                visited.add(tmp);
//                containWords(board, word, cache, x_, y_, visited,idx+1);
//                // undo
//                cache.deleteCharAt(cache.length()-1); // ???
//                visited.remove(tmp); // ???
//            }
//        }
//
//        return false; // ??
//    }
//


    // LC 208
    // 8.08-8.20 pm
    class MyNode{

        // attr
        //MyNode myNode;
        Map<String, MyNode> children; // ?
        boolean isEnd;

        // constructor
        public MyNode(){
            //this.myNode = new MyNode();
            this.children = new HashMap<>();
            this.isEnd = false;
        }

        public MyNode(Map<String, MyNode> children){
            //this.myNode = myNode;
            this.children = children;
        }

    }

    class Trie {

        // attr
//        MyNode node;
//        Boolean isEnd;
//
//        Trie trie;

        MyNode node;

        // constructor
        public Trie(){
            this.node = new MyNode();
//            this.isEnd = false; // ?
//            this.trie = new Trie();
        }

        public void insert(String word) {
            //MyNode node = this.node;
            //Trie trie = this.node;
            // init a new trie ???
//            Trie trie = new Trie();
//            MyNode myNode = trie.node;
            MyNode node = this.node;
            for(String x: word.split("")){
                node.children.putIfAbsent(x, new MyNode());
                // move node
                node = node.children.get(x);
            }
        }

        public boolean search(String word) {
            // init a new trie ???
//            Trie trie = new Trie();
//            MyNode myNode = trie.node;
            MyNode node = this.node;
            for(String x: word.split("")){
                if(!node.children.containsKey(x)){
                    return false;
                }
                // move node
                node = node.children.get(x);
            }
            return node.isEnd; // ??
        }

        public boolean startsWith(String prefix) {
            // init a new trie ???
//            Trie trie = new Trie();
//            MyNode myNode = trie.node;
            MyNode node = this.node;
            for(String x: prefix.split("")){
                if(!node.children.containsKey(x)){
                    return false;
                }
                // move node
                node = node.children.get(x);
            }
            //return !node.isEnd; // ??
            return true;
        }

    }

//    class TreeNode_{
//        // attr
//        Map<String, TreeNode_> children;
//        boolean isEnd;
//
//        public TreeNode_(){
//            this.children = new HashMap<>();
//            this.isEnd = false; // ?
//        }
//    }
//    class Trie {
//
//        // attr
//        TreeNode_ root;
//
//        public Trie(){
//            this.root = new TreeNode_(); // root is `null` treeNode
//        }
//
//        public void insert(String word) {
////            for(String x: word.split("")){
////                if(!this.trie.children.containsKey(x)){
////                    this.trie.children.put(x, new Trie(trie, new HashMap<>(), false)); // ???
////                }
////            }
//            for(String x: word.split("")){
//                this.root.children.putIfAbsent(x, new TreeNode_());
//                this.root = this.root.children.get(x);
//               // this.root.isEnd = false; // needed ???
//            }
//            this.root.isEnd = true;
//        }
//
//        public boolean search(String word) {
//            for(String x: word.split("")){
//                if(this.root.children.containsKey(x)){
//                    this.root = this.root.children.get(x);
//                }else{
//                    return false; // ?
//                }
//            }
//            return this.root.isEnd;
//        }
//
//        public boolean startsWith(String prefix) {
//            for(String x: prefix.split("")){
//                if(this.root.children.containsKey(x)){
//                    this.root = this.root.children.get(x);
//                }else{
//                    return false; // ?
//                }
//            }
//
//            return true;
//        }
//    }

    /**
     * Your WordDictionary object will be instantiated and called as such:
     * WordDictionary obj = new WordDictionary();
     * obj.addWord(word);
     * boolean param_2 = obj.search(word);
     */
    // LC 211
        
    class WordDictionary {

        public WordDictionary() {

        }

        public void addWord(String word) {

        }

        public boolean search(String word) {

            return false;
        }
    }

    // LC 200
    // IDEA: DFS
//    // 10.01 - 10.12 PM
//    public int numIslands(char[][] grid) {
//        // edge
//        if(grid == null || grid.length == 0 || grid[0].length == 0){
//            return 0;
//        }
//
//        int cnt = 0;
//        int l = grid.length;
//        int w = grid[0].length;
//        //boolean[][] visited = new boolean[l][w]; // ??? default is false ??
//
//        for(int i = 0; i < w; i++){
//            for(int j = 0; j < l; j++){
//                if(grid[j][i] == '1'){
//                    cnt += 1;
//                    bfsVisit(grid, i, j);
//                }
//            }
//        }
//
//        return cnt;
//    }
//
//    private void bfsVisit(char[][] grid, int x, int y){
//        int l = grid.length;
//        int w = grid[0].length;
//        int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
//        // NOTE !!! mark current (x,y) as `visited` before move & recursive call
//        grid[y][x] = 'x';
//        // Boundary and land check
//        if (x < 0 || x >= w || y < 0 || y >= l || grid[x][y] != '1') {
//            return;
//        }
//
//        for (int[] m: moves){
//            int x_ = x + m[0];
//            int y_ = y + m[1];
////            if (x_ < 0 || x_ >= w || y_ <= 0 || y_ >= l || grid[y_][x_] != '1'){
////                return;
////            }
////            // mark as `visited`
////            grid[y_][x_] = 'x';
//            bfsVisit(grid, x_, y_);
//        }
//    }
//    public int numIslands(char[][] grid) {
//        // Edge case
//        if (grid == null || grid.length == 0 || grid[0].length == 0) {
//            return 0;
//        }
//
//        int rows = grid.length;
//        int cols = grid[0].length;
//        int count = 0;
//
//        // Iterate over each cell in the grid
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                if (grid[i][j] == '1') { // Found land
//                    count++; // One new island found
//                    dfs(grid, i, j);
//                }
//            }
//        }
//
//        return count;
//    }

//    private void dfs(char[][] grid, int x, int y) {
//        int rows = grid.length;
//        int cols = grid[0].length;
//
//        // Boundary and land check
////        if (x < 0 || x >= rows || y < 0 || y >= cols || grid[x][y] != '1') {
////            return;
////        }
//
//        // Mark the current cell as visited by changing it to '0'
//        grid[x][y] = '0';
//
//        // Possible movements: up, down, left, right
//        int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
//        for (int[] move : moves) {
//            if (x + move[0] < 0 || x + move[0] >= rows || y + move[1] < 0 || y + move[1] >= cols || grid[x + move[0]][y + move[1]] != '1') {
//                return;
//            }
//            dfs(grid, x + move[0], y + move[1]);
//        }
//    }

        public int numIslands(char[][] grid) {
        // Edge case
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

            int l = grid.length;
            int w = grid[0].length;
            int count = 0;

//        // Iterate over each cell in the grid
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i][j] == '1') { // Found land
                    count++; // One new island found
                    dfs(grid, i, j);
                }
            }
        }

        return count;
    }

    private void dfs(char[][] grid, int x, int y){

        int l = grid.length;
        int w = grid[0].length;

        int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        // NOTE !!! we validate x,y before for loop and recursion call
        if(x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != '1'){
            return;
        }

        // mark as `visited`
        grid[y][x] = '#';

        // dfs
        for(int[] m: moves){
            dfs(grid, x + m[0], y + m[1]);
        }
    }


    // LC 211


    // LC 261
    // 9.10 - 9.25 pm
    /**
     *  TODO: check if there is a `cycle` in tree
     *
     *  IDEA: UNION FIND
     *
     *
     */
//    private int[] p;
//
//    public boolean validTree(int n, int[][] edges) {
//        UnionFind uf = new UnionFind(n);
//        for(int[] e: edges){
//            int x = uf.find(e[0]);
//            int y = uf.find(e[1]);
//            if(x == y){
//                return false;
//            }
//            uf.union(x, y);
//        }
//        return true;
//    }
//
//    public class UnionFind{
//        // attr
//        int[] p;
//
//        // constructor
//        public UnionFind(int n){
//            this.p = new int[n];
//            for(int i = 0; i < n; i++){
//                p[i] = i;
//            }
//        }
//
//        public void union(int x, int y){
//            p[find(x)] = find(y);
//        }
//
//        public int find(int x){
//            if(p[x] != x){
//                p[x] = find(p[x]);
//            }
//            return p[x];
//        }
//    }

    // LC 207
    // 9.40 - 10.00 pm
    /**
     *
     *  where prerequisites[i] = [ai, bi]
     *  -> indicates that you must take course bi first if you want to take course ai.
     *
     *  so [ai, bi] -> bi first, then ai
     *
     *
     *
     *  IDEA 1) DFS
     *
     *  IDEA 2) TOPOLOGICAL SORT
     *  -> check if input (with prerequisites) can form a ordering
     *     without conflicts
     *
     *
     *      so [ai, bi] -> bi first, then ai
     *
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {

//        if( topoSort(numCourses, prerequisites) != null){
//            List<Integer> res = topoSort(numCourses, prerequisites);
//            System.out.println(">>> res  = " + res);
//            return true;
//        }

        List<Integer> res = topoSort(numCourses, prerequisites);
        System.out.println(">>> res  = " + res);
        return res.size() == numCourses; // ???
    }

    private List<Integer> topoSort(int numCourses, int[][] prerequisites){
        // build `degree`
        int[] degrees = new int[numCourses]; // ?
        for(int i = 0; i < numCourses; i++){
            degrees[i] = 0;
        }
        // build graph
        // { cur: [prev_1, prev_2, ...] }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int[] p: prerequisites){
            int prev = p[1];
            int cur = p[0];
            List<Integer> prevList = map.getOrDefault(cur, new ArrayList<>());
            prevList.add(prev);
            map.put(cur, prevList); // ???
            // update degree
            //degrees[cur] += 1;
            degrees[cur] += 1;
        }

        Queue<Integer> q = new LinkedList<>();
        // ONLY add `degree=0` idx to queue
        for(int j = 0; j < numCourses; j++){
            if(degrees[j] == 0){
                q.add(j);
            }
        }

        List<Integer> res = new ArrayList<>();

        // BFS
        while(!q.isEmpty()){
            Integer cur = q.poll();
            // ??? add to res
            res.add(cur);

            if(map.containsKey(cur)){
                for(Integer x: map.get(cur)){
                    degrees[x] -= 1;
                    if(degrees[x] == 0){
                        q.add(x);
                    }
                }
            }
        }

        return res;
    }

    // LC 323
    // 9.37 PM - 9.55 PM
    // IDEA: UNION FIND
    private int[] p;

    public int countComponents(int n, int[][] edges) {
        //UnionFind2 uf2 = new UnionFind2(n, edges);
        UnionFind2 uf2 = new UnionFind2(n);
        // union
        for(int[] e: edges){
            int start = e[0];
            int end = e[1];
            uf2.union(start, end);
        }

        return uf2.indCnt;
    }

    public class UnionFind2{
        // attr
        int n;
        int indCnt;
        int[][] edges;
        int[] parents;

        // constructor
//        public UnionFind2(int n, int[][] edges){
//            this.n = n;
//            this.edges = edges;
//            this.indCnt = n;
//            this.parents = new int[n]; // ?
//            for(int i = 0; i < this.n; i++){
//                // init each node is its parent
//                this.parents[i] = i;
//            }
//        }

        public UnionFind2(int n){
            this.n = n;
            //this.edges = edges;
            this.indCnt = n;
            this.parents = new int[n]; // ?
            for(int i = 0; i < this.n; i++){
                // init each node is its parent
                this.parents[i] = i;
            }
        }

        // method
        public void union(int x, int y){
            // ?
            if(x == y) {
                return;
            }
            // NOTE !!! we get parent via `find` method
            int rootX = this.find(x);
            int rootY = this.find(y);
            // should be y or this.parents[y] ????
            if(rootX != rootY){
                this.indCnt -= 1;
                this.parents[rootX] = rootY; // ???
            }
        }

        public int find(int x){
            if(x == this.parents[x]) {
                return x;
            }
           return find(this.parents[x]); // ???
        }

        public boolean isConnected(int x, int y){
            return find(x) == find(y);
        }

    }

    // LC 269
    // IDEA: TOPO SORT

    public String alienOrder(String[] words){
        return null;
    }

    public class TopoSort3{
        // attr
        String[] words;
        Map<String, List<String>> graph;
        int[] degrees;
        public TopoSort3(String[] words){
           this.words = words;

           int wordCnt = 0;
           HashSet<String> set = new HashSet<>();
           // ??
           for(String x: words){
               for(String y: x.split("")){
                   if(!set.contains(y)){
                       set.add(y);
                       wordCnt += 1;
                   }
               }
           }
           this.degrees = new int[wordCnt];

           // ???
           this.graph = new HashMap<>();
            for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                String k = it.next();
                this.graph.put(k, new ArrayList<>());
            }
        }

        public String sort(String[][] deps){
            // update degree
            for(String[] x: deps){
                String prev = x[0];
                String next = x[1];
                // NOTE !!! update prev,
                // since it needs finish `prev` first, then be able do `next`
                // ??? this.degrees[prev] += 1;
            }

            // update graph
            for(String[] x: deps){
                String prev = x[0];
                String next = x[1];

                List<String> cur1 = this.graph.getOrDefault(prev, new ArrayList<>());
                cur1.add(next);
                this.graph.put(prev, cur1);

            }

            // bfs
            return null;
        }
    }

    // LC 70
    // 11.26 - 11. 40 am
    /**
     *  IDEA: DP (bottom up)
     *
     *  -> so,
     *  -> f(0) = 1 -> 1 way to climb
     *  -> f(1) = 1 -> 1 way ..
     *  -> f(2) = 2 -> 2 way ... (1 +1  or 2)
     *          = max( f(2-2) + 1, f(2-1) + 1 )
     *  -> f(3) = f(2) + 1 st climb or f(2) + 1 st NOT climb
     *          = max( f(3-2), f(3-1) + 1)
     *          = max( f(1), f(2) + 1)
     *          = max(1, 3) = 3
     *
     *
     *  -> ...
     *
     *  -> f(K) = Math.max(f(k-2), f(k-1) + 1)
     *
     *
     */
    public int climbStairs(int n) {

        // edge
        if(n <= 2){
            return n;
        }
        // dp
        // base
        int[] dp = new int[n+1];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 2;
        System.out.println(">>> dp (before) = " + dp);
        for(int i = 2; i <= n; i++){
            //dp[n] = Math.max(dp[n-2], dp[n-1]+1);
            dp[n] = dp[n-2]+ dp[n-1];
        }

        System.out.println(">>> dp (after) = " + new ArrayList<>(Collections.singleton(dp)));
        return dp[dp.length-1]; // ???
    }

    // LC 198
    // 11.57 - 12.10
    /**
     *  IDEA: DP
     *
     *  -> f(1
     *
     *
     *  EXP 1)
     *
     *  Input: nums = [1,2,3,1]
     *  -> 4
     *
     *  -> [1,2,3,1]
     *      x   x
     *
     *  -> [1,2,3,1]
     *        x   x
     *
     *  -> [1,2,3,1]
     *          x
     *
     *  -> [1,2,3,1]
     *            x
     *
     *
     */
    public int rob(int[] nums) {

        // edge
        if(nums.length == 0){
            return 0;
        }

        if(nums.length == 1){
            return nums[0];
        }

        if(nums.length == 2){
            return Math.max(nums[0], nums[1]);
        }

        // dp
        int[] dp = new int[nums.length + 1];
        // init
        dp[0] = 0; // ?
        dp[1] = nums[0];
        dp[2] = Math.max(dp[1], dp[2]);

        for(int i = 2; i < nums.length; i++){
            //dp[i] = Math.max(dp[i-2], dp[i-1]);
            dp[i] = Math.max(dp[i-2] + nums[i], dp[i-1]); // ???
        }

        return dp[dp.length-1]; // ???
    }

    // LC 213
    // 4.01 - 4.11 pm
    public int rob_(int[] nums) {
        // Edge cases
        if (nums.length == 0)
            return 0;
        if (nums.length == 1)
            return nums[0];
        if (nums.length == 2)
            return Math.max(nums[0], nums[1]);

        // DP array
        int[] dp = new int[nums.length];

        // Base cases
        dp[0] = nums[0]; // First house
        dp[1] = Math.max(nums[0], nums[1]); // Pick max of first two houses
        dp[2] = Math.max(Math.max(nums[0], nums[1]), nums[2]);

        /**
         * since now house is in `cycle`
         *  -> dp needs to be updated
         *  ->
         *
         */
        // DP transition
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        }

        return dp[nums.length - 1]; // Return the last computed value
    }

    // LC 005
    /**
     *  IDEA 1) BRUTE FORCE (double loop)
     *
     *  IDEA 2) dp ???
     *
     *  IDEA 3) 2 pointers ???
     *
     */
    public String longestPalindrome(String s) {

        return null;
    }

    // LC 647
    // 12.33 - 12.45 pm
    public int countSubstrings(String s) {
        // edge
        if(s == null || s.length() == 0){
            return 0;
        }
        if(s.length() == 1){
            return 1;
        }
        if(s.length() == 2){
            // if true, return 1, else 0
            return s.charAt(0) == s.charAt(1) ? 1 : 0;
        }

        int res = 0;

        // IDEA 1)
        // brute force
        for(int i = 0; i < s.length(); i++){
            for(int j = i; j < s.length(); j++){
                if(isPalindromic(s, i, j)){
                    res += 1;
                }
            }
        }

        // IDEA 2)
        // 2 pointers
//        int res = 0;
//        for(int i = 0; i < s.length(); i++){
//            int l = i;
//            int r = s.length();
//            while(r > l){
//                if(s.charAt(l) == s.charAt(r)){
//                    r -= 1;
//                    l += 1;
//                    res += 1;
//                }
//            }
//        }

        return res;
    }

    private boolean isPalindromic(String str, int l, int r){
//        int l = 0;
//        int r = str.length() -1;
        while(r > l){
            if(str.charAt(l) != str.charAt(r)){
                return false;
            }
            r -= 1;
            l += 1;
        }
        return true;
    }

  // LC 91
  // 12.47 - 1.10 pm
  /**
   * Given a string s containing only digits,
   * -> return the number of ways to decode it.
   * If the entire string cannot be decoded in any valid way,
   * return 0.
   *
   *  IDEA : DP
   *
   *  EXP 1)
   *    s = "12"
   *    -> [1,2], [12]
   *    -> 2
   *
   *  EXP 2)
   *    s = "226"
   *    -> [2,26], [22,6], [226]
   *
   *  EXP 3)
   *    s = "06"
   *    -> 0 (not valid input)
   *
   *
   *
   *
   */
  public int numDecodings(String s) {

      // edge
      if(s == null || s.length() == 0){
          return 0;
      }
      // not valid input
      if(s.charAt(0) == '0'){
          return 0;
      }

      // dp

      return 0;
    }

    // LC 322
    // 1.01 - 1.20 pm
    /**
     *  Return the `fewest` number of coins that you need to make up that amount.
     *  If that amount of money cannot be made up by any combination
     *  of the coins, return -1.
     *
     *
     *  IDEA 1) BACK TRACK
     *  IDEA 2) BFS (shortest path)
     *  IDEA 3) DP ???
     *
     *  EXP 1)
     *
     *   coins = [1,2,5], amount = 11
     *   -> 3
     *   -> 11 = 5+5+1
     *
     *     1   2       5
     *             1  2   5
     *          ...      1
     *
     *
     */
    public class CoinStatus{
        // attr
        Integer coin;
        Integer curSum;
        List<Integer> coins;
        // constructor
        public CoinStatus(Integer coin, Integer curSum, List<Integer> coins){
            this.coin = coin;
            this.curSum = curSum;
            this.coins = coins;
        }
    }
    public int coinChange(int[] coins, int amount) {
        // edge
        if(amount == 0){
            return 0;
        }

        int min = -1 * Integer.MAX_VALUE;
        for(int x: coins){
            min = Math.min(min, x);
        }
        if(amount < min){
            return -1;
        }

        // bfs
        Queue<CoinStatus> q = new LinkedList<>();
        // init ?? so we start from `all coins` as init status
        // sort coins first, `big` first
//        Arrays.sort(coins, new Comparator<>() {
//            @Override
//            public int compare(Object o1, Object o2) {
//                return 0;
//            }
//
//            @Override
//            public boolean equals(Object obj) {
//                return false;
//            }
//        });

        // Arrays.sort(array, Collections.reverseOrder());
        Integer[] coins_ = new Integer[coins.length];
        for(int i = 0; i < coins.length; i++){
            coins_[i] = coins[i];
        }

        System.out.println(">>> (before) coins_ = " + coins_);

        Arrays.sort(coins_, Collections.reverseOrder());

        System.out.println(">>> (after) coins_ = " + coins_);


        for(int c: coins_){
            q.add(new CoinStatus(c, 0, new ArrayList<>()));
        }

        while(!q.isEmpty()){
            CoinStatus coinStatus = q.poll();
            int curSum = coinStatus.curSum;
            List<Integer> coinList = coinStatus.coins;
            if(curSum == amount){
                System.out.println(">>>  coinStatus.coins.size() = " +  coinList.size());
                return coinList.size();
            }
            for(int c: coins_){
                int curSum_ = curSum + c;
                if (curSum_ <= amount){
                    coinList.add(c);
                    q.add(new CoinStatus(c, curSum_,coinList));
                }
                //q.add(new CoinStatus(c, 0, new ArrayList<>()));
            }

        }
        return -1; // can't find any solution
    }

  // LC 152
  // 1.46 - 2.06 pm
  /**
   * Given an integer array nums, find a `subarray`
   * that has the largest product, and return the product.
   *  The test cases are generated so that the answer
   *  will fit in a 32-bit integer.
   *
   *  -> A subarray is a contiguous non-empty sequence
   *     of elements within an array.
   *
   *  EXP 1)
   *    nums = [2,3,-2,4]
   *    -> [2,3], so 2 * 3 = 6
   *    -> 6
   *
   *   [2,3,-2,4]
   *    i j        6
   *    i   j     -12
   *    i      j  - 48
   *      i  j     -6
   *      i     j   -24
   *         i  j  -8
   *
   *
   *  IDEA 1) BRUTE FORCE + 2 pinters
   *
   *  IDEA 2) DP ???
   *
   *  IDEA 3) kadane ALGO
   *
   *
   * EXP 1) nums =  [-2,3,-4]
   * Output : 3
   * Expected: 24
   *
   *
   */
  // 10.31 - 10.35
  public int maxProduct(int[] nums) {
      // edge
      if(nums == null || nums.length == 0){
          return 0;
      }
      if(nums.length == 1){
          return nums[0];
      }
      // kadane algo
      int minSoFar = nums[0];
      int maxSoFar = nums[0];
      int globalMax = nums[0];
      for(int i = 1; i < nums.length; i++){
//          localMax = Math.max(localMax * nums[i], nums[i]);
//          globalMax = Math.max(localMax, globalMax);
          minSoFar = Math.min(minSoFar, minSoFar * nums[i]);
          maxSoFar = Math.max(maxSoFar, nums[i]);
          globalMax = Math.max(globalMax, Math.max(maxSoFar, minSoFar)); // ???
      }

      return globalMax;
  }
//  public int maxProduct(int[] nums) {
//
//      // edge
//      if(nums == null || nums.length == 0){
//          return 0;
//      }
//      if(nums.length == 1){
//          return nums[0];
//      }
//
//      if(nums.length == 2){
//          return Math.max(Math.max(nums[0], nums[1]), nums[0] * nums[1]);
//      }
//
//      // 2 pointers
//      int res = 0;
//      for(int i = 0; i < nums.length-1; i++){
//          int prod = nums[i];
//          res = Math.max(res, nums[i]);
//          for(int j = i+1; j < nums.length; j++){
//              prod = prod * nums[j];
//              res = Math.max(prod, res);
//          }
//      }
//
//      // handling last element
//      res = Math.max(res, nums[nums.length-1]);
//
//      return res;
//    }

    // LC 152
    // 2.26 - 2.36 pm
    /**
     *  IDEA 1) BFS ??
     *  IDEA 2) DP ???
     *  ...
     *
     */
//    public class WordStatuds{
//        // attr
//        String cur;
//        Integer idx;
//        public WordStatuds(String cur, Integer idx){
//            this.cur = cur;
//            this.idx = idx;
//        }
//    }
//    public boolean wordBreak(String s, List<String> wordDict) {
//        // edge
//        if(s == null || s.length() == 0){
//            return true;
//        }
//        for(String x: wordDict){
//            if(s.equals(x)){
//                return true;
//            }
//        }
//
//        // bfs
//        // NOTE !!! use hashmap to avoid `duplicated` visit
//        Map<String, Integer> visited = new HashMap<>();
//        Queue<WordStatuds> q = new LinkedList<>();
//        q.add(new WordStatuds("", 0)); // ??? init `cur` as null string ???
//        while(!q.isEmpty()){
//            WordStatuds wordStatuds = q.poll();
//            String cur = wordStatuds.cur;
//            Integer idx = wordStatuds.idx;
//            if(cur.length() == s.length() && cur.equals(s)){
//                return true;
//            }
//            // add candidates to queue
//            for(String x: wordDict){
//                String cur_ = cur + x;
//                boolean shouldProceed = (!visited.containsKey(cur_)
//                        && cur_.length() <= s.length()
//                        && cur_.equals(s.substring(idx, idx+cur_.length()))
//                );
//                if(shouldProceed){
//                    visited.put(cur_, 1);
//                    q.add(new WordStatuds(cur_, idx+1));
//                }
//            }
//        }
//
//        return false;
//    }

    // LC 140
        public class WordStatus{
        // attr
        String cur;
        Integer idx;
        List<String> words;
        public WordStatus(String cur, Integer idx, List<String> words){
            this.cur = cur;
            this.idx = idx;
            this.words = words;
        }
    }
    public List<String> wordBreak(String s, List<String> wordDict) {

        // Convert wordDict to a HashSet for O(1) lookups
        Set<String> wordSet = new HashSet<>(wordDict);

        // BFS queue (stores indices where words end)
        Queue<WordStatus> queue = new LinkedList<>();
        queue.add(new WordStatus("", 0, new ArrayList<>())); // Start from index 0

        // Track visited indices to avoid reprocessing
        /** NOTE !!!
         *
         * we use `visited` to avoid duplicated visiting
         */
        Set<Integer> visited = new HashSet<>();

        List<String> res = new ArrayList<>();

        while (!queue.isEmpty()) {
            //int start = queue.poll();
            WordStatus wordStatus = queue.poll();
            int start = wordStatus.idx;

            /** NOTE !!!
             *
             * Skip if already visited
             */
            if (visited.contains(start)) {
                continue;
            }
            visited.add(start);

            // Try all words in the dictionary
            for (String word : wordSet) {
                int end = start + word.length();
                /** NOTE !!!
                 *
                 *  `s.substring(start, end).equals(word)` condition
                 *   -> can avoid `not valid word` added to queue
                 *      , so avoid no-needed visit in while loop
                 *      , so BFS efficiency is improved
                 */
                if (end <= s.length() && s.substring(start, end).equals(word)) {
                    if (end == s.length()) {
                        //return true; // Successfully segmented
                        res.add(s);
                    }
                    //queue.add(new WordStatus(end, end, new ArrayList<>())); // Add new starting index
                }
            }
        }

        return res;
    }

    // LC 300
    // 9.44 - 10.00 am
    /**
     *
     *  A subsequence is an array that can be derived
     *  from another array by deleting some
     *  or no elements without changing the order of the remaining elements.
     *
     *
     *
     *  Example 1:
     *
     * Input: nums = [10,9,2,5,3,7,101,18]
     * Output: 4
     * Explanation: The longest increasing subsequence is
     * [2,3,7,101], therefore the length is 4.
     *
     * Example 2:
     *
     * Input: nums = [0,1,0,3,2,3]
     * Output: 4
     *
     * Example 3:
     *
     * Input: nums = [7,7,7,7,7,7,7]
     * Output: 1
     *
     */
    public int lengthOfLIS(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 1;
        }
        // if all elements are the same
        HashSet<Integer> set = new HashSet<>();
        for(int x: nums){
            if(!set.contains(x)){
                set.add(x);
            }
        }
        if(set.size() == 1){
            return 1;
        }

        // 2 pointers + sliding window ??
        int res = 1;
//        int l = 0;
//        int r = nums.length - 1;
        /**
         *  Dry run 1)
         *
         *   Input: nums = [10,9,2,5,3,7,101,18]
         *                  i  j                      res =1
         *                  i   j
         *                  ..
         *                  i            j          tmp=2 res = 2
         *                     i j                  tmp=1 res=2
         *                     ..
         *                     i        j           tmp=2 res=2
         *                       i j                tmp=2 res=2
         *                       i  j
         *
         *
         *
         *
         */
        for(int i = 0; i < nums.length - 1; i++){
            int tmp = 1;
            int j = i+1;
            while (j < nums.length){
                if(nums[j] > nums[i]){
                    tmp += 1;
                    res = Math.max(res, tmp);
                }
                j += 1;
            }
        }

       return res;
    }

    // LC 62
    // 10.31 - 10.40 am
    /**
     * A robot is located at the top-left corner of a m x n grid
     * (marked 'Start' in the diagram below).
     *
     * The robot can only move either down or right at any point in time.
     * The robot is trying to reach the bottom-right corner of the grid
     * marked 'Finish' in the diagram below).
     *
     * How many possible unique paths are there?
     *
     *
     *
     * EXP 1)
     *
     * Input: m = 3, n = 2
     * Output: 3
     * Explanation:
     * From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
     *
     * 1. Right -> Right -> Down
     * 2. Right -> Down -> Right
     * 3. Down -> Right -> Right
     *
     *
     * EXP 2)
     *
     * Input: m = 7, n = 3
     * Output: 28
     *
     *
     *
     *  IDEA 1) MATH
     *  IDEA 2) DP ??
     *  IDEA 3) BFS OR DFS ??
     */
    public int uniquePaths(int m, int n) {
        // edge
        if(m == 0 || n == 0){
            return 0;
        }
        if(m == 0 && n == 0){
            return 0;
        }
        if(m == 1 || n == 1){
            return 1;
        }
       // int res
       int m_ = m - 1;
       int n_ = n - 1;

        Long a = getFactorial(m_ + n_);
        Long b = getFactorial(m_) * getFactorial(n_);

       System.out.println(">>> a = " + a + ", b = " + b);

        return  Integer.parseInt(String.valueOf(a / b));
    }

    private Long getFactorial(int i){
        Long res = 1L;
        for(int j = 1; j < i+1; j++){
            res = (res * j);
        }
        return res;
    }

    // LC 1143
    // 10.55 am - 11.10 am
    /**
     *
     * Given two strings text1 and text2, return the length of their longest common subsequence.
     *
     * A subsequence of a string is a new string generated from the original
     * string with some characters(can be none) deleted without changing the relative
     * order of the remaining characters. (eg, "ace" is a subsequence of "abcde" while "aec" is not).
     * A common subsequence of two strings is a subsequence that is common to both strings.
     *
     *
     *
     * If there is no common subsequence, return 0.
     *
     *
     * Example 1:
     *
     * Input: text1 = "abcde", text2 = "ace"
     * Output: 3
     * Explanation: The longest common subsequence is "ace" and its length is 3.
     *
     *
     * Example 2:
     *
     * Input: text1 = "abc", text2 = "abc"
     * Output: 3
     * Explanation: The longest common subsequence is "abc" and its length is 3.
     *
     *
     * Example 3:
     *
     * Input: text1 = "abc", text2 = "def"
     * Output: 0
     * Explanation: There is no such common subsequence, so the result is 0.
     *
     *  IDEA 1) DP
     *
     *   so,
     *
     *    Input: text1 = "abcde", text2 = "ace"
     *
     *    "abcde"   "ace"
     *     i         i       tmp = 1, res = 1
     *
     *     "abcde"   "ace"   tmp = 1, res = 1
     *       i         i
     *
     *     "abcde"   "ace"  tmp = 2, res = 2
     *        i        i
     *
     *    "abcde"   "ace"   tmp = 2, res = 2
     *        i        i
     *
     *   "abcde"   "ace"   tmp = 3, res = 3
     *        i       i
     *
     *   -> dp[i] = Math.max(dp[i-1]+1, dp[i-2])
     *
     *
     */
    public int longestCommonSubsequence(String text1, String text2) {
        // edge
        if(text1 == null && text2 == null){
            return 0;
        }
        if(text1 == null || text2 == null){
            return 0;
        }
        if(text1.equals(text2)){
            return text1.length();
        }
        if(text1.length() > text2.length()){
            if(text1.contains(text2)){
                return text2.length(); // ???
            }
        }
        if(text1.length() < text2.length()){
            if(text2.contains(text1)){
                return text1.length(); // ???
            }
        }

        // dp
        return 0;

    }

    // LC 53
    // 11.22 - 11.35 AM
    /**
     * Given an integer array nums, find the contiguous subarray (containing at
     * least one number) which has the largest sum and return its sum.
     *
     * Example:
     *
     * Input: [-2,1,-3,4,-1,2,1,-5,4],
     * Output: 6
     * Explanation: [4,-1,2,1] has the largest sum = 6.
     * Follow up:
     *
     * If you have figured out the O(n) solution, try coding
     * another solution using the divide and conquer approach, which is more subtle.
     *
     *
     *
     * NOTE !!!
     *  -> A subarray is a `contiguous` non-empty sequence of elements within an array.
     *
     *
     *  IDEA 1) 2 POINTERS + SLIDE WINDOW + presum ??
     *
     *  -> step 0) prepare preSum array -> so we can get sub array (i, j) sum in O(1) time complexity
     *
     *  -> step 1) set i, j, get curSum in window,
     *      if curSum < 0, move i to i+1 ????, keep looping
     *      else, keep moving j, and maintain the global longest len
     *
     *  -> step 3) return the final result (global longest len)
     *
     *
     *    Input: [-2,1,-3,4,-1,2,1,-5,4],
     *            i  j                    presum= -1, move i
     *               i  j                 presum = -2, move i
     *                  i j               presum = 1, res = 1
     *                  i    j            presum = 0, res = 1
     *                  i      j          presum = 2, res = 2
     *                  i        j        presum = 3, res = 3
     *                  i          j      presum = -2, move i
     *                    i  j            presum = 3
     *                    i    j          presum = 5, res = 5
     *                    i      j        presum = 6, res = 6
     *                    i        j      presum = 1, res = 6
     *                    i          j    presum = 5, res = 6
     *                     ....
     *
     *                     ??? need to loop from i+1 ... when j reach the end, while i is not ?
     *
     *
     *    -> res = 3
     */
    // 10.06 - 10.16 AM
    /**
     *  IDEA: KADANE ALGO
     *
     *  -> since this is a `max sub array` problem
     */
    public int maxSubArray(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0];
        }
        // kadane algo
        int globalMax = nums[0];
        int localMax = nums[0];
        for(int i = 1; i < nums.length; i++){
            localMax = Math.max(localMax + nums[i], nums[i]);
            globalMax = Math.max(globalMax, localMax);
        }
        return globalMax;
    }
//    public int maxSubArray(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0;
//        }
//        if(nums.length == 1){
//            return 1;
//        }
//        if(nums.length == 2){
//            if(nums[1] >  nums[0]){
//                return 2;
//            }
//            return 1;
//        }
//
//        // presum array
//        Integer[] preSum = new Integer[nums.length+1];
//        preSum[0] = 0; // ??
//        int curSum = 0;
//        for(int i = 0; i < nums.length; i++){
//            curSum += nums[i];
//            preSum[i] = curSum;
//        }
//
//        /**
//         *
//         *  2 pointers + slide window
//         *
//         *  left pointer : i
//         *  right : j
//         *
//         */
//        int res = 1;
//        int i = 0;
//        int j = 1;
//        while(i < nums.length && j < nums.length){
//            int tmp = 1;
//            if(tmp + nums[j] >= 0){
//                tmp += 1;
//                res = Math.max(res, tmp);
//            }else{
//                i += 1;
//            }
//            j += 1;
//        }
//
//        return res;
//    }

    // LC 55
    // 9.53 - 10 AM
    /**
     * Given an array of non-negative integers, you are initially positioned at the first index of the array.
     *
     * Each element in the array represents your maximum jump length at that position.
     *
     * Determine if you are able to reach the last index.
     *
     *
     * Example 1:
     *
     * Input: [2,3,1,1,4]
     * Output: true
     * Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
     *
     * -> loop over val in nums
     * -> check if `cur idx + val` can >= the end
     * ->   if yes, return true directly
     * ->   if not, keep loop till the end, if still can't find a solition -> return false
     *
     *
     *
     *
     *
     * Example 2:
     *
     * Input: [3,2,1,0,4]
     * Output: false
     * Explanation: You will always arrive at index 3 no matter what. Its maximum
     *              jump length is 0, which makes it impossible to reach the last index.
     * Difficulty:
     *
     *
     *
     * IDEA 1: DP
     *
     * IDEA 2: BRUTE FORCE
     *
     *
     */
    public boolean canJump(int[] nums) {
        // edge
        if(nums == null || nums.length == 0 || nums.length == 1){
            return true;
        }
        if(nums[0] == 0){
            return false;
        }
        /**
         *  Dry run 1)
         *
         *  Input: [2,3,1,1,4]
         *
         *  [2,3,1,1,4]
         *   i          i + nums[i] = 2 < 4
         *     i        i + nums[i] = 4 <= 4 -> true
         *
         *
         *  Dry run 2)
         *
         *  Input: [3,2,1,0,4]
         *
         *  [3,2,1,0,4]
         *   i            i + nums[i] = 3 < 4
         *     i          i + nums[i] = 3 < 4
         *       i        i + nums[i] = 3 < 4
         *         i      i + nums[i] = 3 < 4
         *
         *  Dry run 3)
         *
         *  input = [0,2,3]
         *
         *  Dry run 4)
         *
         *  input = [1,0,1,0]
         *
         *
         *
         *
         */
        int len = nums.length;
        int cur = 0;
        for(int i = 0; i < nums.length - 1; i++){
            if(i + nums[i] >= len - 1){
                return true;
            }
            if(cur < i){
                return false;
            }
            cur = Math.max(cur, nums[i] + i);
        }

        return false;
    }


    // LC 45
    /**
     *  45. Jump Game II
     * Given an array of non-negative integers, you are initially positioned at the first index of the array.
     *
     * Each element in the array represents your maximum jump length at that position.
     *
     * Your goal is to reach the last index in the minimum number of jumps.
     *
     * Example:
     *
     * Input: [2,3,1,1,4]
     * Output: 2
     * Explanation: The minimum number of jumps to reach the last index is 2.
     *     Jump 1 step from index 0 to 1, then 3 steps to the last index.
     * Note:
     *
     * You can assume that you can always reach the last index.
     *
     *
     *
     *  -> Your goal is to reach the last index in the minimum number of jumps.
     */
    public int jump(int[] nums) {
        // edge
        if(nums == null || nums.length <= 1){
            return 0;
        }

        int cur = 0;
        int res = 0;
        for(int i = 0; i < nums.length; i++){
            if(cur < i){
                return -1; // false
            }

            int oldCur = cur;
            cur = Math.max(cur, nums[i] + i);
            if(cur > oldCur){
                res += 1;
            }

            if(cur >= nums.length-1){
                return res;
            }
        }
        return nums.length-1; // ???
    }

    // LC 57
    // 5.39 - 5.50 pm
    /**
     * Example 1:
     *
     * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
     * Output: [[1,5],[6,9]]
     *
     * ->  Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
     *    [ [1,3]]
     *    [ [2,5]]
     *
     *    -> [ [2,5] ]
     *
     *
     * Example 2:
     *
     * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
     * Output: [[1,2],[3,10],[12,16]]
     * Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
     *
     *
     * ->  [[1,2],[3,5],[6,7],[8,10],[12,16]],
     *   [[1,2]]
     *   [[1,2], [4,5]]
     *  [[1,2], [3,5], [6,7]]
     *  [[1,2], [3,5], [6,7]]
     *
     *
     * IDEA: ARRAY OP
     *
     *  -> loop over intervals,
     *
     *    step 1) check overlap compare prev and cur
     *    step 2) get new interval as [ max(prev_start, cur_start), min(prev_end, cur_end)]
     *    step 3) if not overlap, append to res
     */
    /**
     *
     * Given a set of non-overlapping intervals,
     * insert a new interval into the intervals (merge if necessary) !!!
     *
     *
     * Example 1:
     *
     * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
     * Output: [[1,5],[6,9]]
     *
     * Example 2:
     *
     * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
     * Output: [[1,2],[3,10],[12,16]]
     * Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
     * NOTE: input types have been changed on April 15, 2019. Please reset to default code definition to get new method signature.
     *
     *
     */
    // 11.33 - 11.40 am

    public int[][] insert(int[][] intervals, int[] newInterval) {
        // edge
        if(intervals.length == 0 || newInterval.length == 0){
            int[][] res = new int[1][]; // ???
            if(intervals.length == 0){
                res[0] = newInterval;
                return res; // ??
            }
            if(newInterval.length == 0){
                return intervals;
            }
        }
        // merge newInterval to intervals
        List<int[]> cache = new ArrayList<>();
        for(int[] x: intervals){
            cache.add(x);
        }
        cache.add(newInterval);
        // sort: sort on 2nd ELEMENT (ascending order, small -> big)
        Collections.sort(cache, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                /**
                 *  TODO:
                 *
                 *  compare / check when to sort on 1st or 2nd element
                 *
                 */
                //return o2[1] - o1[1]; // ??? validate
                return o2[0] - o1[0]; // ??? validate
            }
        });

        List<int[]> res = new ArrayList<>();
        for(int i = 0; i < cache.size(); i++){
            //case 1) res is empty or NO OVERLAP
            if(res.isEmpty() || res.get(res.size()-1)[1] < cache.get(0)[0]){
                res.add(cache.get(i));
            }else{
                // case 2) OVERLAP
                int newStart = Math.min(res.get(res.size()-1)[0], cache.get(i)[0]);
                int newEnd = Math.max(res.get(res.size()-1)[1], cache.get(i)[1]);
                int[] tmp = new int[2];
                tmp[0] = newStart;
                tmp[1] = newEnd;
                res.add(tmp);
            }
        }

//        int[][] finalRes = new int[res.size()][2]; // ?
//        int j_ = 0;
//        for(int[] x: res){
//            finalRes[j_] = x;
//            j_ += 1;
//        }
//        return finalRes;

        return res.toArray(new int[res.size()][]);
    }

//    public int[][] insert(int[][] intervals, int[] newInterval) {
//        // edge
//        if(intervals == null || intervals.length == 0){
//            //int[][] res = new int[][]{{}}; // ???
////            int[][] res = new int[][]{newInterval};
////            return res; // ???
//            return new int[][]{newInterval};
//        }
////        if(newInterval[0] > intervals[intervals.length-1][0]){
////
////        }
//
//        // queue: FIFO
//        Queue<int[]> q = new LinkedList<>();
//        List<int[]> cache = new ArrayList<>();
//        q.add(intervals[0]);
//        for(int i = 0; i < intervals.length; i++){
//            int[] interval = intervals[i];
//            int start = interval[0];
//            int end = interval[1];
//            /**
//             *
//             *   OVERLAP CASES
//             *
//             *   case 1)
//             *      |--------|
//             *         |-----------|
//             *
//             *   case 2)
//             *      |----------|
//             *  |------|
//             *
//             *  case 3)
//             *      |-------|
//             *        |---|
//             *
//             *  case 4)
//             *
//             *     |---|
//             *   |----------|
//             *
//             */
//           while(newInterval[1] > start || newInterval[0] < end){
//               int[] cur = q.poll();
//               int newStart = Math.min(start, cur[0]);
//               int newEnd = Math.max(end, cur[1]);
//               int[] new_ = new int[]{newStart, newEnd};
//               q.add(new_);
//           }
//           q.add(interval);
//        }
//
//        int[][] res = new int[q.size()][]; // ?
//        int j = 0;
//        while(!q.isEmpty()){
//            res[j] = q.poll();
//            j += 1;
//        }
//
//        return res;
//    }

    // LC 56
    /**
     * Given a collection of intervals, merge all overlapping intervals.
     *
     * Example 1:
     *
     * Input: [[1,3],[2,6],[8,10],[15,18]]
     * Output: [[1,6],[8,10],[15,18]]
     * Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
     *
     * Example 2:
     *
     * Input: [[1,4],[4,5]]
     * Output: [[1,5]]
     * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
     * NOTE: input types have been changed on April 15, 2019. Please reset to default code definition to get new method signature.
     *
     */
    public int[][] merge(int[][] intervals) {
        // edge
        // sort
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                // sort on 1st element !!! (small -> big)
                return o1[0] - o2[0];
            }
        });


        List<int[]> cache = new ArrayList<>();
        List<int[]> res = new ArrayList<>();
        //for(int i = )

        return res.toArray(new int[res.size()][]); // ???
    }
//    public int[][] merge(int[][] intervals) {
//        // array op
//        // edge
//        if(intervals == null || intervals.length == 0){
//            return new int[][]{}; // ??
//        }
//        if(intervals.length == 1){
//            return new int[][]{intervals[0]}; // ??
//        }
//
//        // sorting
//        Arrays.sort(intervals, new Comparator<int[]>() {
//            @Override
//            public int compare(int[] o1, int[] o2) {
//                int diff = o1[0] - o2[0];
//                if (diff == 0){
//                    return o1[1] - o2[1];
//                }
//                return diff; // ??
//            }
//        });
//
//        List<int[]> cache = new ArrayList<>();
//        for(int i = 0; i < intervals.length; i++){
//            // case 1) cache is empty or NO overlap
//            // NO OVERLAP :  new[0] > old[1]
//            if(cache.isEmpty() || cache.get(cache.size()-1)[1] < intervals[i][0]){
//                cache.add(new int[]{intervals[i][0], intervals[i][1]}); // ???
//            }// case 2) overlap
//            else{
//                // ???
//                int newStart = Math.min(intervals[i][0], cache.get(cache.size()-1)[0]);
//                int newEnd = Math.max(intervals[i][1], cache.get(cache.size()-1)[1]);
//            }
//        }
//
//        //int[][] res = new int[][];
//        //int[][] res = new int[][]{Arrays.asList(cache)};
//
//        // NOTE !!! below op
//        return res.toArray(new int[res.size()][]);
//        //return null;
//    }

    // LC 435
    // 11.06 - 11.16 pm
    /**
     *  IDEA : ARRAY OP + scanning line
     *
     *  step 1) sort (small -> big)
     *  step 2) loop over elements
     *     scanning line ??
     *     -> find the max overlap
     *  step 3) return res
     *
     *
     *  EXP 1)
     *
     *  Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
     * Output: 1
     *  Explanation: [1,3] can be removed and the rest of the intervals are non-overlapping.
     *
     *  ->  sort [[1,2],[1,3],[2,3],[3,4]]
     *
     *  ->  [[1,3],[1,2],[2,3],[3,4]]
     *        x                        res = 0
     *
     *     [[1,3],[1,2],[2,3],[3,4]]
     *              x                 res = 1
     *
     *  EXP 2)
     *  Input: intervals = [[1,2],[1,2],[1,2]]
     *  Output: 2
     *
     *   -> sort : [[1,2],[1,2],[1,2]]
     *
     */
    public int eraseOverlapIntervals(int[][] intervals) {

        // edge
        if (intervals == null || intervals.length == 0){
            return 0;
        }
        if(intervals.length == 1){
            return 0;
        }
        if(intervals.length == 2){
            if(intervals[1][0] >= intervals[0][1]){
                return 0;
            }
            return 1; // ??
        }

        // sort
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                // 1st element : small -> big

                int diff = o1[0] - o2[0];
                if(diff == 0){
                    // 2nd element: big -> small ??
                    return o2[1] - o1[1];
                }
                return diff;
            }
        });

        // loop over element, compare
        int res = 0;
        List<int[]> cache = new ArrayList<>();
        for(int[] x: intervals){
            int start = x[0];
            int end = x[1];
            // case 1) cache is empty or NO overlap
            if(cache.isEmpty() || start > cache.get(cache.size()-1)[0]){
                cache.add(x);
            }else{
                res += 1;
                // remove the `bigger interval`, add the `smaller interval` back to cache
                // or no need to check above, since we already sort intervals array ????
                cache.remove(cache.size()-1);
                cache.add(x);
            }

        }

        return res;
    }

    // LC 252
    // 9.54 am - 10.10 am
    /**
     * Given an array of meeting time intervals consisting of start and end times
     * [[s1,e1],[s2,e2],...] (si < ei), determine if a person could attend all meetings.
     *
     * Example 1:
     *
     * Input: [[0,30],[5,10],[15,20]]
     * Output: false
     *
     * Example 2:
     *
     * Input: [[7,10],[2,4]]
     * Output: true
     *
     *
     *  IDEA 1) SORT
     *
     *  Input: [[0,30],[5,10],[15,20]]
     *
     *  -> sort:
     *
     *  IDEA 1) SCANNING LINE
     *  -> if any `cnt` >1`, return false dircetly
     *
     * NOTE: input types have been changed on April 15, 2019. Please reset to default code definition to get new method signature.
     *
     *
     */
    public boolean canAttendMeetings(int[][] intervals) {
        // edge
        if(intervals == null || intervals.length == 0){
            return true;
        }
        if(intervals.length == 1){
            return true;
        }
        // scanning line
        // [ [val, open_close] ], open : 1, close : 0
        List<List<Integer>> cache = new ArrayList<>();
        for(int[] x: intervals){
            // TODO: optimize it
            List<Integer> tmp1 = new ArrayList<>();
            List<Integer> tmp2 = new ArrayList<>();
            int open = x[0];
            int close = x[1];
            tmp1.add(open);
            tmp1.add(1);
            tmp2.add(close);
            tmp2.add(0);
            cache.add(tmp1);
            cache.add(tmp2);
        }

        // sort on val
        System.out.println(">>> before sort, cache = " + cache);
        Collections.sort(cache, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                return o1.get(0) - o2.get(0);
            }
        });
        System.out.println(">>> after sort, cache = " + cache);

        int cnt = 0;
        for(List<Integer> x: cache){
            if(cnt > 1){
                return false;
            }
            if(x.get(1) == 1){
                cnt += 1;
            }
            else if(x.get(1) == 0){
                cnt -= 1;
            }

        }

        return true;
    }

    // LC 48
    // 10.19-10.29 AM
    /**
     * 48. Rotate Image
     * Solved
     * Medium
     * Topics
     * Companies
     * You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees (clockwise).
     *
     * You have to rotate the image in-place, which means you have to modify the input 2D matrix directly. DO NOT allocate another 2D matrix and do the rotation.
     *
     *
     *
     * Example 1:
     *
     *
     * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
     * Output: [[7,4,1],[8,5,2],[9,6,3]]
     * Example 2:
     *
     *
     * Input: matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
     * Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
     *
     *
     * Constraints:
     *
     * n == matrix.length == matrix[i].length
     * 1 <= n <= 20
     * -1000 <= matrix[i][j] <= 1000
     *
     *
     * Given input matrix =
     * [
     *   [1,2,3],
     *   [4,5,6],
     *   [7,8,9]
     * ],
     *
     * rotate the input matrix in-place such that it becomes:
     * [
     *   [7,4,1],
     *   [8,5,2],
     *   [9,6,3]
     * ]
     *
     *
     *  IDEA 1) ARRAY OP
     *
     *   (i,j) -> (j,i) ??
     *
     *   [1 4 7]
     *   [2 5 8]
     *   [3 6 9]
     *
     *   -> inverse ??
     *
     *   [ 7 4 1]
     *   [ 8 5 2 ]
     *   [ 9 6 3]
     *
     */
    public void rotate(int[][] matrix){

        System.out.println(">>> matrix = " + matrix);

        // edge
        if(matrix.length == 0 || matrix[0].length == 0){
            return;
        }
        if(matrix.length == 1 || matrix[0].length == 1){
            return;
        }

        // step 1) i,j -> j,i
        int l = matrix.length;
        int w = matrix[0].length;
        // NOTE !!! ONLY LOOP `HALF OF THE MATRIX` (right upper part)
        // TODO: validate ????
        for(int i = 0; i < l/ 2; i++){
            for(int j = 0; j < w/2; j++){
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }
        // step 2) reverse
        for(int i = 0; i < l; i++){
            // reverse
            reverse(matrix[i]);
        }

        System.out.println(">>> (after op) matrix = " + matrix);
        return;
    }

    private Integer[] reverse(int[] input){
        List<Integer> cache = new ArrayList<>();
        for(int x: input){
            cache.add(x);
        }
        Collections.reverse(cache);
        // Integer [] arr2 = list2.toArray(new Integer[list2.size()]);
        return cache.toArray(new Integer[cache.size()]);
    }

    // LC 54
    // 10.58 - 11.05 am
    /**
     * Given a matrix of m x n elements (m rows, n columns),
     * return all elements of the matrix in spiral order.
     *
     * Example 1:
     *
     * Input:
     * [
     *  [ 1, 2, 3 ],
     *  [ 4, 5, 6 ],
     *  [ 7, 8, 9 ]
     * ]
     * Output: [1,2,3,6,9,8,7,4,5]
     * Example 2:
     *
     * Input:
     * [
     *   [1, 2, 3, 4],
     *   [5, 6, 7, 8],
     *   [9,10,11,12]
     * ]
     * Output: [1,2,3,4,8,12,11,10,9,5,6,7]
     *
     *
     *  IDEA : ARRAY OP
     *
     *  -> dirs ALWAYS LIKE THIS (in cycle)
     *    -> down <-- up, -> down <-- up, ....
     *    so, we simply repeat above cycle, and collect the reached val
     *    when need to `change` dir ? -> when `reach the bounrary`
     *
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        // edge
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0){
            return null;
        }
        if (matrix.length == 1 || matrix[0].length == 1){
            List<Integer> res = new ArrayList<>();
            // [1,2,3...]
            if(matrix.length == 1){
                for(int x: matrix[0]){
                    res.add(x);
                }
                return res;
            }
            /**
             * [1,
             *  2,
             *  3,
             *  ...
             *  ]
             *
             */
            for(int i = 0; i < matrix.length; i++){
                res.add(matrix[i][0]);
            }
            return res;
        }

        return null;
    }

    // LC 73
    /**
     * 73. Set Matrix Zeroes
     *
     * Given a m x n matrix, if an element is 0,
     * set its entire row and column to 0. Do it in-place.
     *
     * Example 1:
     *
     * Input:
     * [
     *   [1,1,1],
     *   [1,0,1],
     *   [1,1,1]
     * ]
     * Output:
     * [
     *   [1,0,1],
     *   [0,0,0],
     *   [1,0,1]
     * ]
     * Example 2:
     *
     * Input:
     * [
     *   [0,1,2,0],
     *   [3,4,5,2],
     *   [1,3,1,5]
     * ]
     * Output:
     * [
     *   [0,0,0,0],
     *   [0,4,5,0],
     *   [0,3,1,0]
     * ]
     * Follow up:
     *
     * A straight forward solution using O(mn) space is probably a bad idea.
     * A simple improvement uses O(m + n) space, but still not the best solution.
     * Could you devise a constant space solution?
     *
     */
    public void setZeroes(int[][] matrix) {
        // edge
        if (matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        if (matrix.length == 1 && matrix[0].length == 1) {
            return;
        }
        // find all `0` index
        int l = matrix.length;
        int w = matrix[0].length;

        List<int[]> cache = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    int[] tmp = new int[2];
                    tmp[0] = j;
                    tmp[1] = i;
                    cache.add(tmp);
                }
            }
            // make 0 in place
            for (int[] x: cache){
                // make elements with same `x` coordination as 0
                for(int i_ = 0; i_ < matrix.length; i_++){
                    matrix[i_][x[0]] = 0;
                }
                // make elements with same `y` coordination as 0
                for(int j_ = 0; j_ < matrix[0].length; j_++){
                    matrix[x[1]][j_] = 0;
                }
            }

            return;
        }
    }

    /**
     *
     *  input = [1,-2,3,-1,3,5]
     *
     *  max sub array = ?
     *    -> [3,-1,3,5]
     *
     *    -> local max, global max
     *    -> loop over nums
     *    -> compare local and global max ???
     *
     */
    public class KadaneAlgorithm{
        public int maxSubArray(int[] input){
            int localMax = input[0]; // ???
            int globalMax = input[0];
            for(int i = 1; i < input.length; i++){

//                if(input[i] + input[i-1] >= 0){
//                    localMax += input[i];
//                }else{
//                    globalMax = Math.max(globalMax, localMax);
//                }

                // choice: whether 1) add curSum with new val or 2) reset curSum, use new val directly
                localMax = Math.max(localMax+ input[i], input[i]);
                globalMax = Math.max(localMax, globalMax); // ??
            }
            return globalMax;
        }
    }

    // LC 918
    // 11.08 am - 11.18 am
    /**
     *  EXP 1)
     *   [5,-3,5]
     *
     *   -> exp : 10
     *   -> output: 19
     *
     *   -> [5,-3,5,5,-3,5]
     *
     */
    public int maxSubarraySumCircular(int[] nums) {
        // Edge cases
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        // Step 1: Find the normal max subarray sum using Kadane's algorithm
        int maxNormal = kadane(nums);

        // Step 2: Find the min subarray sum
        int totalSum = 0;
        int minLocalMax = nums[0];
        int minGlobalMax = nums[0];
        for (int i = 0; i < nums.length; i++) {
            totalSum += nums[i];
            minLocalMax = Math.min(nums[i], minLocalMax + nums[i]);
            minGlobalMax = Math.min(minGlobalMax, minLocalMax);
        }

        // The max sum for the circular case is totalSum minus minGlobalMax,
        // but it should not be the case where the entire array is included
        int maxCircular = totalSum - minGlobalMax;

        // If maxCircular equals totalSum, then all elements are negative
        // In such case, maxCircular will be incorrect, so return maxNormal
        if (maxCircular == 0) {
            return maxNormal;
        }

        // Step 3: The result is the max of maxNormal and maxCircular
        return Math.max(maxNormal, maxCircular);
    }

    // Function to apply Kadane's algorithm to find the max subarray sum
    private  int kadane(int[] nums_) {
        int localMax = nums_[0];
        int globalMax = nums_[0];
        for (int i = 1; i < nums_.length; i++) {
            localMax = Math.max(nums_[i], localMax + nums_[i]);
            globalMax = Math.max(globalMax, localMax);
        }
        return globalMax;
    }
//    public int maxSubarraySumCircular(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0;
//        }
//        if(nums.length == 1){
//            return nums[0];
//        }
//        // double array -> to simulate `circular` array
//        List<Integer> tmp = new ArrayList<>();
////        for(int i = 0; i < 2; i++){
////            for(int j = 0; j < nums.length; j++){
////                tmp.add(nums[j]);
////            }
////        }
//        for(int i = 0; i < nums.length; i++){
//            tmp.add(nums[i]);
//        }
//        // NOTE !!! also append `last` element in nums
//        tmp.add(nums[nums.length - 1]);
//        // list to array
//        Integer[] nums_ = tmp.toArray(new Integer[tmp.size()]);
//
//
//        System.out.println(">>> nums_ = " + nums_);
//
//        // kadane algo
//        int localMax = nums_[0];
//        int globalMax = nums_[0];
//        for(int i = 0; i < nums_.length; i++){
//            localMax = Math.max(nums_[i], localMax + nums_[i]);
//            globalMax = Math.max(globalMax, localMax);
//        }
//
//        return globalMax;
//    }

    // LC 251
    // 11.41 am - 12.00 pm
    /**
     * 261. Graph Valid Tree
     * Given n nodes labeled from 0 to n-1 and a list of
     * undirected edges (each edge is a pair of nodes), write a
     * function to check whether these edges make up a valid tree.
     *
     * Example 1:
     *
     * Input: n = 5, and edges = [[0,1], [0,2], [0,3], [1,4]]
     * Output: true
     *
     *
     * Example 2:
     *
     * Input: n = 5, and edges = [[0,1], [1,2], [2,3], [1,3], [1,4]]
     * Output: false
     *
     * Note: you can assume that no duplicate edges will appear in edges.
     * Since all edges are undirected, [0,1] is the same as [1,0] and thus will not appear together in edges.
     */
    /**
     *  IDEA: UNION FIND
     *
     *  -> check if there is a `cycle` in the graph
     *  -> if yes, return false (CAN NOT form a tree)
     *  -> ; otherwise, return true
     */
    public boolean validTree_0_1(int n, int[][] edges) {
        // edge
        if(n == 0){
            return false;
        }
        MyUF myUF = new MyUF(n, edges);
        // union
        for(int[] x: edges){
            if(!myUF.union(x[0], x[1])){
                return false;
            }
        }

        /**
         *  NOTE !!!
         *
         *  FINAL CHECK
         *
         *   based on math,
         *   n nodes, should ONLY has `n-1` edges
         *   -> so there is NO cycle in graph
         *   -> so can form a valid tree
         *
         *
         *   -> Check if the number of edges is exactly n - 1
         */
        return n - 1 == edges.length;
    }

    public class MyUF{
        // attr
        int n;
        int cnt; // `cluster cnt`
        int[][] edges;
        int[] parents; // records node's parent
        // constructor
        public MyUF(int n, int[][] edges){
            this.n = n;
            this.cnt = n;
            this.edges = edges;
            this.parents = new int[n]; // ???
            for(int i = 0; i < n; i++){
                this.parents[i] = i;
            }
        }
        // method
        // union
        public boolean union(int a, int b){
            if(a == b){
                return true;
            }
            int aParent = this.find(a);
            int bParent = this.find(b);
            /**
             * NOTE !!!
             *
             *  is a node's parent == b node's parent
             *  -> there must be a CYCLE,
             *  -> CAN'T form a valid tree -> return false directly
             */
            if(aParent == bParent){
                return false;
            }
            // this.parents[bParent] = APartent;  // equivalent as below
            this.parents[aParent] = bParent;
            this.cnt -= 1;
            return true;
        }
        // find a node's parent
        public int find(int a){
            if(a == this.parents[a]){
                return a;
            }
            this.parents[a] = this.find(a);
            return this.parents[a];
        }

        // get `cluster cnt`
        public int getCnt(){
            return this.cnt;
        }
    }


    // LC 36
    // 9.31 - 9.50 am
    public boolean isValidSudoku(char[][] board) {
        // edge
        if(board.length == 0 || board[0].length == 0){
            return true; // ??
        }
//        if(board.length == 1 || board[0].length == 1){
//            return true; // ??
//        }
        int l = board.length;
        int w = board[0].length;

        // check x-axis
        for(int y = 0; y < l; y++){
            HashSet<Character> set = new HashSet<>();
            for(int i = 0; i < w; i++){
                // ??
                if(!String.valueOf(board[y][i]).equals("")){
                    if(set.contains(board[y][i])){
                        return false;
                    }
                    set.add(board[y][i]);
                }
            }
        }

        // check y-axis
        for(int x = 0; x < w; x++){
            HashSet<Character> set = new HashSet<>();
            for(int i = 0; i < l; i++){
                // ??
                if(!String.valueOf(board[l][x]).equals("")){
                    if(set.contains(board[l][x])){
                        return false;
                    }
                    set.add(board[l][x]);
                }
            }
        }

        // check `3x3` small matrix
        for(int i = 0; i < l; i += 2){ // ??
            for(int j = 0; j < w; j += 2){ // ??
                HashSet<Character> set = new HashSet<>();
            }
        }

        return true;
    }


}