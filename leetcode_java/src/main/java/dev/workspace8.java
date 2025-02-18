package dev;

import java.util.*;
import java.util.List;

public class workspace8 {

  // LC 003
  // https://leetcode.com/problems/longest-substring-without-repeating-characters/
  // 2.31 - 2.40 pm
  /**
   * Given a string s, find the length of the longest
   * `substring` without duplicate characters.
   *
   * -> A substring is a contiguous non-empty
   *    sequence of characters within a string.
   *
   *
   * IDEA: hashmap + sliding window
   *
   */
  public int lengthOfLongestSubstring(String s) {
      // edge
      if(s == null || s.length() == 0){
          return 0;
      }
      if(s.length() == 1){
          return 1;
      }
      /**
       * map : {val: min_idx}
       */
      Map<String, Integer> map = new HashMap<>();
      int res = 1;
      int l = 0;
      for(int r = 0; r < s.length(); r++){
          System.out.println(">>> l = " + l + ", r = " + r + ", map = " + map + ", res = " + res);
          String k = s.split("")[r];
          // NOTE !!! if `duplicated val is meet`
          while (map.containsKey(k)){
              // keep moving `left` idx, till NO duplicated val is found
              map.put(k, map.get(k) - 1);
              if (map.get(k) == 0){
                  map.remove(k);
              }
              l += 1;
          }
          if (map.isEmpty() || !map.containsKey(k)){
              map.put(k, r);
              res = Math.max(res, r - l + 1);
          }
      }

      return res;
    }

    // LC 424
    // IDEA: HASHMAP + 2 POINTERS
    public int characterReplacement(String s, int k) {
        // edge
        if(s == null || s.length() == 0){
            return 0;
        }
        if(s.length() == 1){
            return 1;
        }
        // map: {val, cnt}
        Map<String, Integer> map = new HashMap<>();
        int l = 0;
        int res = 1;
        for(int r = 0; r < s.length(); r++){
            String key = s.split("")[r];
            map.put(key, map.getOrDefault(key, 0) + 1);
            if(map.containsKey(key)){
                // NOTE !!!
                // if (r - l  + 1)  > k
                // -> there are still `more than k different val
                // -> so we CAN'T  get `same character sub string` via k times modification
                while ( (r - l  + 1) - getMaxCnt(map) > k ){
                   String pevK = s.split("")[l];
                   map.put(pevK, map.get(pevK) - 1);
                   if(map.get(pevK) == 0){
                       map.remove(pevK);
                   }
                   l += 1;
                }
            }
            res = Math.max(res, r - l + 1);
        }

        return res;
    }

    private int getMaxCnt(Map<String, Integer> map){
        int res = 0;
        for(Integer x: map.values()){
            res = Math.max(res, x);
        }
        return res;
    }

  //    public int characterReplacement(String s, int k) {
  //      // edge
  //      if(s == null || s.length() == 0){
  //          return 0;
  //      }
  //      if(s.length() == 1){
  //         return 1;
  //      }
  //      // map: {val, cnt}
  //      Map<String, Integer> map = new HashMap<>();
  //      int res = 0;
  //      int l = 0;
  //      for(int r = 0; r < s.length(); r++){
  //          String key = s.split("")[r];
  //          String prev = s.split("")[r];
  //          map.put(key, map.getOrDefault(key, 1)+1);
  //          if(map.keySet().size() > 1 && getMapSecondCnt(map) > k){
  //              // if most cnt > move l
  //              //l = Math.max(map.get(key), r);
  //              l = r;
  //              // reset map
  //              map = new HashMap<>();
  //          }
  //          res = Math.max(res, r - l + 1);
  //      }
  //      return res;
  //    }
  //
  //    private int getMapSecondCnt(Map<String, Integer> map){
  //        List<Integer> values = new ArrayList<>();
  //        for(Integer x: map.values()){
  //            values.add(x);
  //        }
  //
  //        System.out.println(">>> (before)  values = " + values);
  //        Collections.sort(values, new Comparator<Integer>() {
  //            @Override
  //            public int compare(Integer o1, Integer o2) {
  //                return o2 - o1;
  //            }
  //        });
  //
  //        System.out.println(">>> (after)  values = " + values);
  //        return values.get(1); // get 2nd biggest
  //    }

  // LC 567
  // 4.04 - 4.19 pm
  /**
   *
   * Given two strings s1 and s2, return true if s2 contains a
   * permutation of s1, or false otherwise.
   *
   * In other words, return true if one of
   * s1's permutations is the substring of s2.
   *
   * -> A permutation is a rearrangement of all the characters of a string.
   *
   */
  public boolean checkInclusion(String s1, String s2) {
      if(!s1.isEmpty() && s2.isEmpty()){
          return false;
      }
      if(s1.equals(s2)){
          return true;
      }
      Map<String, Integer> map1 = new HashMap<>();
      Map<String, Integer> map2 = new HashMap<>();
      for(String x: s1.split("")){
          String k = String.valueOf(x);
          map1.put(x, map1.getOrDefault(k, 0 ) + 1);
      }

      // 2 pointers (for s2)
      int l = 0;
      //int r = 0;
      for(int r = 0; r < s2.length(); r++){
          String val = String.valueOf(s2.charAt(r));
          map2.put(val, map2.getOrDefault(val, 0) + 1);

          // NOTE !!! below trick
          if(map2.equals(map1)){
              return true;
          }
          // NOTE !!! if reach below, means a permutation string is NOT FOUND YET
          if( (r - l + 1) >= s1.length() ){
              // update map
              String leftVal = String.valueOf(s2.charAt(l));
              map2.put(leftVal, map2.get(leftVal) - 1);
              l += 1; // ?
              if(map2.get(leftVal) == 0){
                  map2.remove(leftVal);
              }
          }
      }

      return false;
  }

  //  public boolean checkInclusion(String s1, String s2) {
  //      // edge
  //      if(s1.equals(s2)){
  //          return true;
  //      }
  //      if(s2.contains(s1) || s2.isEmpty()){
  //          return true;
  //      }
  //      if(s1.isEmpty() && !s2.isEmpty()){
  //          return false;
  //      }
  //      // ??
  //      if(s1.length() > s2.length()){
  //          return false;
  //      }
  //
  //      // map: {val, cnt}
  //      Map<String, Integer> map_s1 = new HashMap<>();
  //      Map<String, Integer> map = new HashMap<>();
  //
  //      for(String x: s1.split("")){
  //          map_s1.put(x, map_s1.getOrDefault(x, 0) + 1);
  //      }
  //      System.out.println(">>> map_s1 = " + map_s1);
  //
  //      int s_1_idx = 0;
  //      int s_2_r_idx = 0;
  //      int s_2_l_idx = 0;
  //      String cache = "";
  //      while (s_2_r_idx < s2.length() && s_1_idx < s1.length()){
  //          String s2Val = String.valueOf(s2.charAt(s_2_r_idx));
  //          if(!map_s1.containsKey(s2Val)){
  //              cache = "";
  //              map = new HashMap<>();
  //              //s_1_idx = s_2_idx;
  //              s_2_l_idx += 1;
  //              s_2_r_idx = s_2_l_idx;
  //          }else{
  //              map.put(s2Val, map.getOrDefault(s2Val, 0) + 1);
  //              if(map.get(s2Val) <= map_s1.get(s2Val)){
  //                  cache += s2Val;
  //                  if(cache.length() == s1.length()){
  //                      return true;
  //                  }
  //                  s_1_idx += 1;
  //              }else{
  //                  cache = "";
  //                  map = new HashMap<>();
  //              }
  //          }
  //          s_2_r_idx += 1;
  //      }
  //
  //      return false;
  //    }

  // LC 76
  // 5.25 - 5.35 pm
  /**
   * Given two strings s and t of lengths m and n respectively,
   * return the minimum window substring
   * of s such that every character in t
   * (including duplicates) is included in the window.
   * If there is no such substring, return the empty string "".
   *
   * The testcases will be generated such that the answer is unique.
   *
   *
   * A substring ->
   * is a contiguous non-empty sequence of characters within a string.
   *
   *
   *
   * IDEA : 2 POINTERS
   *
   * -> l, r
   * -> once l `reach 1st char in t`, then we stop l,
   * -> then we start r, till `all sub str` covers char in t, record the len
   * -> then we move l to `next char in t in s`, move r
   * -> ... repeat above steps, and get min len
   * -> return min len
   */
  public String minWindow(String s, String t) {

      return null;
    }

  // LC 239
  // 2.39 - 2.49 PM
  /**
   * You are given an array of integers nums,
   * there is a sliding window of size k which is moving
   * from the very left of the array to the very right.
   * You can only see the `k numbers in the window.`
   * Each time the sliding window moves right by `one position`.
   *
   * ->  Return the `max sliding window`.
   *
   *  EXP 1)
   *
   *  Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
   *  -> Output: [3,3,5,5,6,7]
   *
   *
   *  -> w (window) = [1,3,-1]  -> 3
   *    [3,-1,-3] -> 3
   *    [-1,-3,5] -> 5
   *    [-3,5,3] -> 5
   *    [5,3,6] -> 6
   *    [3,6,7] -> 7
   *
   *
   *  EXP 2)
   *
   *  Input: nums = [1], k = 1
   *  Output: [1]
   *
   *  -> 1
   *
   *
   *  IDEA: QUEUE (FIFO)
   *
   *  -> maintain a size = k queue,
   *     collect the max in each iteration,
   *     then, insert new, and pop oldest
   *     ... till reach the end of nums
   *
   */
  public int[] maxSlidingWindow(int[] nums, int k) {

      // edge
      if (nums == null || nums.length == 0){
          return new int[]{};
      }
      if(nums.length <= k){
          int res = 0;
          for(int x: nums){
              res = Math.max(res, x);
          }
          return new int[]{res};
      }

      List<Integer> collected = new ArrayList<>();

      // queue
      Queue<Integer> q = new LinkedList<>();
      // big heap ?? (PQ) default is small queue ??? in java ???
      PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());

      // init (firstly, add k element to queue)
      for(int i = 0; i < k; i++){
          int cur = nums[i];
          q.add(cur);
          pq.add(cur); // for using `big heap`, we use this trick
      }

     // int curMax = nums[0];
      for(int i = k; i < nums.length; i++){
          int cur = nums[i];
          q.add(cur);
          pq.add(cur); // for using `big heap`, we use this trick
          //curMax = Math.max(curMax, cur);
          System.out.println(">>> i = " + i + ", cur = " + cur + ", q = " + q + ", pq = " + pq);
          // if q size > k, pop the oldest element
          if(q.size() >= k){
              q.poll();
              pq.poll();
          }
          //collected.add(curMax);
          collected.add(pq.peek()); // via this trick, we can get the top element at PQ (biggest element)
      }

      System.out.println(">>> collected = " + collected);

      // ???? optimize ?
      int[] res = new int[collected.size()];
      int j = 0;
      for (int x: collected){
          res[j] = x;
          j += 1;
      }

      return res;
    }

    // LC 155
    // 3.41 - 4.10 PM
    /**
     *
     * Design a stack that supports push, pop, top,
     * and retrieving the minimum element in constant time.
     *
     * -> stack : FILO (first in, last out)
     *
     *   IDEA 1) queue ?
     *   IDEA 2) ARRAY ?
     *   IDEA 3) double queue ?
     */
    class MinStack {

        List<Integer> list;

        public MinStack() {
            this.list = new ArrayList<>();
        }

        public void push(int val) {
            this.list.add(val);
        }

        public void pop() {
            if (!list.isEmpty()) {
                list.remove(list.size() - 1); // Remove last element
            }
        }

        public int top() {
            if (!list.isEmpty()) {
                return list.get(list.size() - 1); // Return last element
            }
            throw new EmptyStackException(); // Proper error handling
        }

        public int getMin() {
            if (list.isEmpty()) {
                throw new EmptyStackException(); // Prevent access on empty list
            }

            // Create a copy to avoid modifying original list
            List<Integer> tmp = new ArrayList<>(list);

            // Sort to get the minimum
            Collections.sort(tmp);

            return tmp.get(0); // Smallest element
        }
    }

  // LC 150
  // 4.11 - 4.21 PM
  /**
   * You are given an array of strings tokens that
   * represents an arithmetic expression in a Reverse Polish Notation.
   *
   * Evaluate the expression. Return an integer
   * that represents the value of the expression.
   *
   *
   *  -> QUEUE ???
   *
   *  -> 2 Q
   *  -> one save numerical val
   *  -> the other save `operator`
   *  -> once meet operator, pop last 2 element from numerical q
   *     -> calculate, save back to numerical queue
   *  -> ... repeat above steps
   *
   *
   *
   *  EXP 1)
   *
   *  ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
   *
   *  -> 10, n_q = [10], op_q = [], res = 0
   *  -> 6, n_q = [10,6], op_q = [], res = 0
   *  -> 9, n_q = [10,6,9], op_q = [], res = 0
   *  -> 3, n_q = [10,6,9,3], op_q = [], res = 0
   *  -> "+", n_q = [1,6]  , op_q = [], res = 0
   *       -> 9+3 = 12 -> n_q = [1,6,12]
   *   -> -11, n_q = [1,6,12,-11] , op_q = [], res = 0
   *   -> "*",  n_q = [1,6] , op_q = [], res = 0
   *       -> 12 * -11 = -132, n_q = [1,6,-132]
   *   -> "/",  n_q = [1], op_q = [], res = 0
   *       -> 6 / -132 = 0, n_q = [1,0]
   *   -> "*" ,  n_q = [], op_q = [], res = 0
   *       -> 1 * 0 = 0, n_q = [0]
   *   -> 17, n_q = [0, 17]
   *   -> "+", n_q = [17]
   *   -> 5,  n_q = [17,5]
   *   -> "+", n_q = []
   *       -> 17+5 = 22, n_q = [22]
   *
   *   -> res = 22
   *
   */
  public int evalRPN(String[] tokens) {
      String[] numArr = new String[]{"1","2","3","4","5","6","7","8","9","0"};
      List<String> numList = Arrays.asList(numArr);
      // edge
      if(tokens == null || tokens.length == 0){
          return 0;
      }
      if(tokens.length == 1){
          if(numList.contains(tokens[0])){
              return Integer.parseInt(tokens[0]);
          }
      }

      // init num q
      Queue<Integer> q = new LinkedList<>();

      int res = 0;
      for(String t: tokens){
          if(numList.contains(t)){
              q.add(Integer.parseInt(t));
          }else{
              int lastSec = q.poll();
              int lastFirst = q.poll();
              int tmpRes = calculate(lastFirst, lastSec, t);
              q.add(tmpRes);

          }
      }

      res = q.poll();
      return res;
    }

    private int calculate(int a, int b, String operator){
       if (operator.equals("+")){
           return a + b;
       }
       if (operator.equals("-")){
            return a - b;
        }
        if (operator.equals("*")){
            return a * b;
        }
        else{
            if (b == 0){
                return 0;
            }
            return a / b;
        }
    }

    // LC 22
    // 10.04 - 10.19 am
    /**
     * Given n pairs of parentheses, write a
     * function to generate all combinations of well-formed parentheses.
     *
     * Example 1:
     *
     * Input: n = 3
     * Output: ["((()))","(()())","(())()","()(())","()()()"]
     * Example 2:
     *
     * Input: n = 1
     * Output: ["()"]
     *
     *
     * Constraints:
     *
     * 1 <= n <= 8
     *
     *
     *  n = 3 -> must be 3 "(", 3 ")"
     *
     *
     *  IDEA 1) BACKTRACK ???
     *
     *
     *  n = 3 -> must be 3 "(", 3 ")"
     *
     *                   (                                  )
     *            ((            ()
     *      (((     (()      ()(   ())
     *    ((()    (()(  (())
     *
     *  ....
     *
     */
    List<String> resParenthesis = new ArrayList<>();
    public List<String> generateParenthesis(int n) {
        //List<String> res = new ArrayList<>();
        // edge
        if(n == 0) {
            return resParenthesis;
        }
        if(n == 1){
            resParenthesis.add("()");
            return resParenthesis;
        }

        // backtrack
        backtrack(n, new StringBuilder(), 0);
        System.out.println(">>> resParenthesis = " + resParenthesis);
        return resParenthesis;
    }

    private void backtrack(int n, StringBuilder cur, int idx){
        String[] word = new String[]{"(", ")"};
        String curStr = cur.toString();
        if(curStr.length() == n * 2){
            // validate
            //String curStr = cur.toString(); // ???
            if(isValidParenthesis(curStr)){
                resParenthesis.add(curStr);
            }
            return;
        }
        if(curStr.length() > n * 2){
            return;
        }
        for(int i = 0; i < word.length; i++){
            String w = word[i];
            //cur.add(w);
            cur.append(w);
            backtrack(n, cur, idx+1);
            // undo
            idx -= 1;
            //cur.remove(cur.size()-1);
            cur.deleteCharAt(cur.length()-1);
        }
    }

    private boolean isValidParenthesis(String s) {
        int balance = 0;
        for (char ch : s.toCharArray()) {
            if (ch == '(') {
                balance++;
            } else if (ch == ')') {
                balance--;
            }
            if (balance < 0) {
                return false;  // If balance goes negative, it's invalid
            }
        }
        return balance == 0;  // Valid if balance ends up being 0
    }



//    private boolean isValidParenthesis(String x){
//        if(x == null || x.length() == 0){
//            return true;
//        }
//        if(x.length() == 1){
//            return false;
//        }
//        Queue<String> q = new LinkedList<>();
//        Map<String, String> map = new HashMap<>();
//        map.put("(", ")");
//        for(String val: x.split("")){
//            if(map.isEmpty() && val.equals(")")){
//                return false;
//            }
//            if(val.equals("(")){
//                q.add(val);
//            }else{
//                String tmp = q.poll();
//            }
//        }
//
//        return true;
//    }

    // LC 739
    // 10.45 - 11.00 am
    /**
     *  Given an array of integers temperatures represents
     *  the daily temperatures, return an array answer such that
     *  answer[i] is the number of days you have to wait after
     *  the ith day to get a warmer temperature. If there is no
     *  future day for which this is possible, keep answer[i] == 0 instead.
     *
     *
     *
     * Example 1:
     *
     * Input: temperatures = [73,74,75,71,69,72,76,73]
     * Output: [1,1,4,2,1,1,0,0]
     *
     *
     * Example 2:
     *
     * Input: temperatures = [30,40,50,60]
     * Output: [1,1,1,0]
     *
     *
     * Example 3:
     *
     * Input: temperatures = [30,60,90]
     * Output: [1,1,0]
     *
     *
     *  IDEA : `MONOTONIC STACK`
     *  -> a stack that is in `increasing` order
     *  -> we keep add new element and maintain the stack that meet above feature
     *
     *
     *  -> temperatures = [73,74,75,71,69,72,76,73]
     *  -> Output: [1,1,4,2,1,1,0,0]
     *
     *     [73,74,75,71,69,72,76,73]
     *      x                         s = [73], res = []
     *
     *     [73,74,75,71,69,72,76,73]
     *         x                      s = [74], res = [1]
     *
     *     [73,74,75,71,69,72,76,73]
     *            x                  s = [75], res = [1,1]
     *
     *     [73,74,75,71,69,72,76,73]
     *               x                s = [75, 71], res = [1,1]
     *
     *     [73,74,75,71,69,72,76,73]
     *                  x             s = [75, 71, 69], res = [1,1]
     *
     *     [73,74,75,71,69,72,76,73]
     *                     x         s = [75], res = [1,1,-1, 2, 1]
     *
     *     [73,74,75,71,69,72,76,73]
     *                        x      s = [76], res = [1,1,4,2,1]
     *
     *     [73,74,75,71,69,72,76,73]
     *                            x   s=[76], res = [1,1,4,2,1,0,0]
     *
     *
     *
     *
     */
    public int[] dailyTemperatures(int[] temperatures) {
        // edge
        if (temperatures == null || temperatures.length == 0) {
            return new int[] {};
        }

        // init res
        int[] res = new int[temperatures.length];
        for (int i = 0; i < temperatures.length; i++) {
            res[i] = 0; // init res as 0

            // update map: {val: idx}
            int cur = temperatures[i];
        }

        Map<Integer, Integer> map = new HashMap<>();
        System.out.println(">>> map = " + map);

        // stack: FILO
        Stack<Integer> st = new Stack<>();
        for (int i = 0; i < temperatures.length; i++) {
            int cur = temperatures[i];
            // System.out.println(">>> cur = " + cur + ", st = " + st);
            // use map record val and its idx
            map.put(cur, i);
            System.out.println(">>> cur = " + cur + ", st = " + st + ", res = " + res);
            if (st.isEmpty()) {
                st.add(cur);
            } else {
                while (!st.isEmpty() && cur > st.peek()) {
                    int tmp = st.pop();
                    // update res
                    res[map.get(tmp)] = i - map.get(tmp);
                }
                st.add(cur);
            }
        }

        System.out.println(">>> res = " + res);

        return res;
    }

//    public int[] dailyTemperatures(int[] temperatures) {
//        // edge
//        if(temperatures == null || temperatures.length == 0){
//            return new int[]{};
//        }
//
//        Map<Integer, Queue<Integer>> map = new HashMap<>();
//
//        // init res
//        int[] res = new int[temperatures.length];
//        for(int i = 0; i < temperatures.length; i++){
//            res[i] = 0; // init res as 0
//
//            // update map: {val: idx}
//            int cur = temperatures[i];
//            // use map record val and its idx
//            //map.put(cur, i);
//            Queue<Integer> tmpList = map.getOrDefault(cur, new LinkedList<>());
//            tmpList.add(i);
//            map.put(cur, tmpList);
//        }
//
//        System.out.println(">>> map = " + map);
//
//        // stack: FILO
//        Stack<Integer> st = new Stack<>();
//        for(int i = 0; i < temperatures.length; i++){
//            int cur = temperatures[i];
//            System.out.println(">>> cur = " + cur + ", st = " + st + ", res = " + res);
//            if(st.isEmpty()){
//                st.add(cur);
//            }else{
//                while(!st.isEmpty() && cur > st.peek()){
//                    int tmp = st.pop();
//                    // update res
//                    Queue<Integer> tmpList = map.get(tmp);
//                    int idx = tmpList.poll();
//
//                    res[idx] = i - idx;
//
//                    // update map
//                    map.put(cur, tmpList);
//                }
//                st.add(cur);
//            }
//        }
//
//        System.out.println(">>> res = " + res);
//
//        return res;
//    }

    // LC 853
    // 9.48 am - 10.10 am
    /**
     *  1. car at `same idx` will be merged
     *    -> speed will be updated as `slowest` speed after merge
     *  2. count the `number of fleet` when arrive destination
     *    -> fleet: collection of car
     *
     *
     *
     *  EXP 1)
     *
     *  Input: target = 12, position = [10,8,0,5,3], speed = [2,4,1,1,3]
     *  -> Output: 3
     *
     *  [10,8,0,5,3], t = 12
     *
     *  [12,12,1,6,6], t = 12
     *    -> merge, [12,1,6], speed = [2,1,1]
     *    -> 12 reach t, [1,6], speed = [1,1], res = 1
     *
     *  [2,7], speed = [1,1]
     *
     *  [3,8], speed = [1,1]
     *
     *  [4,9], speed = [1,1]
     *
     *  ...
     *
     *  [7,12], speed = [1,1]
     *   -> [7], res = 2
     *
     *   ...
     *
     *   -> [], res = 3
     *
     *
     *   IDEA 1) QUEUE (FIFO)
     *
     */
    public int carFleet(int target, int[] position, int[] speed) {
        if(position == null || position.length == 0){
            return 0;
        }
        if(position.length == 1){
            return 1;
        }

        // sorting ???
//        Map<Integer, Integer> posSpeedMap = new HashMap<>();
//        // {position : speed}
//        for(int i = 0; i < position.length; i++){
//            posSpeedMap.put(position[i], speed[i]);
//        }


        int res = 0;
        // IDEA: queue ???
        // init a hashmap that record car speed
        // { idx: speed }
        Map<Integer, Integer> speedMap = new HashMap<>();
        for(int i = 0; i < speed.length; i++){
            speedMap.put(i, speed[i]);
        }

        Queue<Integer> q = new LinkedList<>();
        for(int p: position){
            q.add(p);
        }

        boolean isArrived = false;

        while (!q.isEmpty()){
            // ??
            for(int i = 0; i < q.size(); i++){
                int cur = q.poll();
                cur += speed[i]; // update idx
                q.add(cur);
            }
            // check if there is `car reach target`
            for(int i = 0; i < q.size(); i++){
                int cur = q.poll();
                if(cur == target){
                    isArrived = true;
                    // if reach target, NOT append back to queue,
                    // and need to remove this car from speedMap
                    speedMap.remove(i);
                }else{
                    q.add(cur);
                }
            }

            if(isArrived){
                res += 1;
            }

            isArrived = false;
        }

        return res;
    }


}
