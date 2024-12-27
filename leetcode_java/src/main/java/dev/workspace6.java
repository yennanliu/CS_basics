package dev;


import LeetCodeJava.DataStructure.ListNode;

import javax.print.DocFlavor;
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
    // 3.42 PM - 4.00 pm
    /**
     *  IDEA 1 : linked list -> int, +1, transform back to linked list
     *
     *  Idea 2: do in linked list directly
     *       -> reverse
     *       -> add 1, if > 9, move to next digit
     *       -> reverse again
     *
     */
    // v2
    public ListNode plusOne(ListNode head) {
        if (head == null){
            return new ListNode(1); // ??
        }
        ListNode tmp = new ListNode();
//        // reverse
//        while(head != null){
//            ListNode _next = head.next;
//            ListNode _prev = tmp; // ??
//            head.next = _prev;
//            head = _next;
//        }
//        tmp.val += 1;  // ??


        return null;
    }

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
  public int maxDistToClosest(int[] seats) {

      List<Integer> distances = new ArrayList<>();
      int lastIdx = -1;
      for(int i = seats.length - 1; i >= 0; i--){
          if (seats[i] == 1){
              if (lastIdx != -1){
                  int diff = Math.abs(i - lastIdx);
                  distances.add(diff);
              }
              lastIdx = i;
          }
      }

      System.out.println(">>> (before sort) distances = " + distances);
      distances.sort(Integer::compareTo);
      System.out.println(">>> (after sort) distances = " + distances);

      // edge case : if only one "1"
      if (distances.isEmpty()){
          return seats.length-1;
      }
      // return the max dist
      return distances.get(distances.size()-1) / 2; // ??
    }

}
