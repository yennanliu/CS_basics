package dev;


import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

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
    // 2.53 - 3.30 PM
    class MyCalendarTwo {

        List<List<Integer>> booked;
        Map<List<Integer>, Integer> overlapCnt;

        public MyCalendarTwo() {
            this.booked = new ArrayList<>();
            this.overlapCnt = new HashMap<>();
        }

        public boolean book(int startTime, int endTime) {

            List<Integer> tmp = new ArrayList<>();
            tmp.add(startTime);
            tmp.add(endTime);

            // case 1) booked is empty
            if(this.booked.isEmpty()){
                this.booked.add(tmp);
                return true;
            }

            boolean lessEqualsThreeOverlap = false;


            for(List<Integer> x: this.booked){
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
                int existingStart = x.get(0);
                int existingEnd = x.get(1);

                if (startTime < existingEnd && existingStart < endTime) {
                    // case 2) has overlap, but `overlap count` <= 3
                    List<Integer> tmpExisting = new ArrayList<>();
                    tmpExisting.add(existingStart);
                    tmpExisting.add(existingEnd);
                    if(this.overlapCnt.get(tmpExisting) <= 3){
                        // update existing start, end
                        existingStart = Math.min(existingStart, startTime);
                        existingEnd  = Math.max(existingEnd, endTime);
                        List<Integer> tmp2 = new ArrayList<>();
                        tmp2.add(existingStart);
                        tmp2.add(existingEnd);
                        this.overlapCnt.put(tmp2, this.overlapCnt.get(tmpExisting)+1); // update overlap cnt
                        this.overlapCnt.remove(tmpExisting);
                        return true;
                    }else{
                        // case 3) has overlap, and `overlap count` > 3
                        return false;
                    }
                }

            }

            // case 4) no overlap
            this.booked.add(tmp);
            this.overlapCnt.put(tmp, 1); // update overlap cnt
            return true;
        }
    }

    // LC 1031
    // https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/
    // 4.21 - 4.40 pm
    /**
     * return the maximum sum of elements in two non-overlapping
     * subarrays with lengths firstLen and secondLen.
     *
     * The array with length firstLen could occur before or after
     * the array with length secondLen,
     * but they have to be non-overlapping.
     *
     *
     * A subarray is a contiguous part of an array.
     *
     *
     * IDEA:  PREFIX SUM
     *
     * exp 1)
     *
     * Input: nums = [3,8,1,3,2,1,8,9,0], firstLen = 3, secondLen = 2
     *
     * -> prefix sum = [3,11,12,15,17,18,26,37,0]
     *
     * Sum(i,j)  = preSum(j+1) - preSum(i)
     *
     * -> pre(15) - pre(3)  = 12
     *
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


}
