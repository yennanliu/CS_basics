package dev;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
//import LeetCodeJava.DataStructure.Node;

import java.lang.annotation.Target;
import java.nio.file.Path;
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

    // LC 84
    // 10.32 - 10.47 am
    /**
     *  IDEA 1) HASHMAP + 2 POINTERS
     *
     *  ->
     */
    public int largestRectangleArea(int[] heights) {

        return 0;
    }

    // LC 704
    // https://leetcode.com/problems/binary-search/
    public int search_(int[] nums, int target) {
        if(nums == null || nums.length == 0){
            return 0;
        }

        int l = 0;
        int r = nums.length - 1;
        while(r >= l){
            int mid = ( l + r ) / 2;
            if(nums[mid] == target){
                return mid;
            } else if (nums[mid] > target) {
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }

        // not found
        return -1;
    }

    // LC 74
    // 11.57 am - 12.10 pm
    /**
     * 74. Search a 2D Matrix
     *
     * Write an efficient algorithm that searches for a value
     * in an m x n matrix. This matrix has the following properties:
     *
     * Integers in each row are sorted from left to right.
     * The first integer of each row is greater than the last integer of the previous row.
     *
     *
     *  -> NOTE !!!
     *
     *       [small -> big]
     *       [bigger_than_big -> bigggg]
     *       ....
     *
     *
     * Example 1:
     *
     * Input:
     * matrix = [
     *   [1,   3,  5,  7],
     *   [10, 11, 16, 20],
     *   [23, 30, 34, 50]
     * ]
     * target = 3
     * Output: true
     *
     * Example 2:
     *
     * Input:
     * matrix = [
     *   [1,   3,  5,  7],
     *   [10, 11, 16, 20],
     *   [23, 30, 34, 50]
     * ]
     * target = 13
     * Output: false
     *
     *
     *  IDEA: BINARY SEARCH
     *
     *  -> EXP 1)
     *
     * matrix = [
     *   [1,   3,  5,  7],
     *   [10, 11, 16, 20],
     *   [23, 30, 34, 50]
     * ]
     *    target = 3
     *    Output: true
     *
     *    ->  check if 3 is in  1st row [1,3,5,7], yes -> find 1st row with binary search
     *
     *
     * -> EXP 2)
     *
     * matrix = [
     *   [1,   3,  5,  7],
     *   [10, 11, 16, 20],
     *   [23, 30, 34, 50]
     * ]
     * target = 13
     * Output: false
     *
     *  -> 13 not in 1st row, move next
     *  -> 13 could be in 2nd row, binary search, not found, move to 3rd row
     *  -> 13 not in 3rd row
     *  -> return false
     */
    public boolean searchMatrix(int[][] matrix, int target) {

        int l = matrix.length;
        int w = matrix[0].length;

        // edge
        if(matrix.length == 0 || matrix[0].length == 0){
            return false;
        }
        if(matrix.length == 1 || matrix[0].length == 1){

            if(matrix.length == 1){
                for(int x: matrix[0]){
                    if(x == target){
                        return true;
                    }
                }
                return false;
            }

            if( matrix[0].length == 1){
                for(int i = 0; i < l; i++){
                    if(matrix[i][0] == target){
                        return true;
                    }
                }
                return false;
            }
        }

        boolean isFound = false;

        // binary search
        for(int i = 0; i < l; i++){
            int[] curArray = matrix[i];
            int left = 0;
            int right = curArray.length - 1;
            // do binary search
            if(target >= curArray[left] && target <= curArray[right]){
                while(right >= left){
                    int mid = ( left + right ) / 2;
                    if(curArray[mid] == target){
                        return true;
                    } else if (curArray[mid] > target) {
                        right = mid - 1;
                    }else{
                        left = mid + 1;
                    }
                }
            }

        }

        return false;
    }

    // LC 875
    // 12.23 pm - 12.33 pm
    /**
     * Koko loves to eat bananas.  There are N piles of bananas,
     *
     * the i-th pile has piles[i] bananas.  The guards have gone and will come back in H hours.
     *
     * Koko can decide her bananas-per-hour eating speed of K.  Each hour,
     *
     * she chooses some pile of bananas, and eats K bananas from that pile.
     *
     * If the pile has less than K bananas, she eats all of them instead,
     *
     * and won't eat any more bananas during this hour.
     *
     * Koko likes to eat slowly, but still wants to finish eating all the bananas
     *
     * before the guards come back.
     *
     *
     * -> Return the `minimum integer K` (aka SPEED)
     *    such that she can eat `all` the bananas `within H hours.`
     *
     *
     *
     * -> bananas-per-hour eating speed of K.
     * -> chooses some pile of bananas, and eats K bananas from that pile.Each hour,
     *
     *
     *
     * Example 1:
     *
     * Input: piles = [3,6,7,11], H = 8
     * Output: 4
     *
     *
     * Example 2:
     *
     * Input: piles = [30,11,23,4,20], H = 5
     * Output: 30
     *
     *
     * Example 3:
     *
     * Input: piles = [30,11,23,4,20], H = 6
     * Output: 23
     *
     *
     *  IDEA 1) BINARY SEARCH
     *
     *  -> we setup max, min speed
     *   -> max speed : biggest pile in piles ???
     *   -> min speed: 1 (or smallest ... ???
     *
     *
     *   -> so, num_of_banana / speed = hr
     *   -> speed = num_of_banana / hr
     *
     */
    public int minEatingSpeed(int[] piles, int h) {
        // edge
        if(piles == null || piles.length == 0){
            return -1; // ???
        }
        if(piles.length == 1){
           // return piles[0] / h; // ???
            int quotient = piles[0] / h;
            int remainder = piles[0] % h;
            if(remainder > 0){
                quotient += 1;
            }
            return quotient;
        }

        // init speed
        int minS = 1; // ??
        int maxS = piles[0];
        for(int p: piles){
            maxS = Math.max(p, maxS);
        }


        int res = Integer.MAX_VALUE; // ???

        // binary search
        while(maxS >= minS){
            int midSpeed = (minS + maxS) / 2;
            int curHr = getTotalHr(piles, midSpeed);
            if(curHr == h){
                //return midSpeed; // /????
                res = Math.min(res, midSpeed);
            }
            // eat too slow
            else if (curHr > h){
                minS = midSpeed + 1;
            } // eat too fast
            else{
               maxS = midSpeed - 1;
            }
        }

        return res; //-1; // if not found ????
    }

    private int getTotalHr(int[] piles, int speed){
        int res = 0;
        for(int p: piles){
            int quotient = p / speed;
            int remainder = p % speed;
            res += quotient;
            if(remainder > 0){
                res += 1;
            }
        }
        return res;
    }

  // LC 153
  // https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
  // 7.54 - 8.10 pm
  /**
   *  IDEA 1) BINARY SEARCH
   *
   *  -> KEY:
   *    - array is in ascending at beginning
   *    - array is sorted after k steps
   *       -> so if any `right element` > `left element`
   *          -> the sub array in that MUST in ascending
   *          -> then we can find min based on above conditon
   *
   *
   *  EXP 1)
   *
   *  Input: nums = [3,4,5,1,2]
   *  Output: 1
   *  Explanation: The original array was [1,2,3,4,5] rotated 3 times.
   *
   *  [3,4,5,1,2]
   *   l   m   r
   *
   *   -> 3 < 5, so we know left sub array is in ascending order
   *   -> its min is 3, however num[r] = 2
   *   -> so we search on right
   *
   * [3,4,5,1,2]
   *      l m  r
   *
   *   -> res = 1, and  num[m] < num[r] && num[l] > num[m]
   *   -> return res = 1
   *
   *
   *  EXP 2)
   *
   *  Input: nums = [4,5,6,7,0,1,2]
   *  res = 0
   *
   *  [4,5,6,7,0,1,2]
   *   l     m     r
   *
   *   -> left part is in ascending order
   *   -> nums[l] < nums[r]
   *      -> search right sub array
   *
   *  [4,5,6,7,0,1,2]
   *           l    r
   *
   *   -> res = 1, but nums[mid] > nums[r], we return nums[l]
   *
   *
   *  EXP 3)
   *
   *  Input: nums = [11,13,15,17]
   *  -> res = 11
   *
   *   [11,13,15,17]
   *    l   m     r
   *
   *    -> left sub array is sorted
   *    -> we return the nums[l] directly
   *
   *
   *
   *
   *
   */
  public int findMin(int[] nums) {
      // edge
      if(nums == null || nums.length == 0){
          return 0; // ?
      }

      int res = Integer.MAX_VALUE;

      if(nums.length <= 3){
          for(int x: nums){
              res = Math.min(x, res);
          }
          return res;
      }
      // if array already in ascending order
      if(nums[nums.length - 1] > nums[0]){
          return nums[0];
      }

      // binary search + check which part of sub array is in `ascending ordering`
      int l = 0;
      int r = nums.length - 1;

      while( r >= l ){
          int mid = (l + r) / 2;
          // if left sub array is ascending order
          if(nums[mid] > nums[0]){
              int leftVal = nums[0];
              res = Math.min(res, leftVal);
              if(nums[r] < leftVal){
                  l = mid + 1;
                  res = Math.min(res, nums[r]);
              }else{
                  r = mid - 1;
              }
          }else{
              if(nums[mid] < nums[l]){
                  //res = Math.min(res, nums[mid]); // ?
                  return Math.min(res, nums[mid]);
              }
              res = Math.min(res, nums[mid]); // ?
          }
      }

      return res;
    }

    // LC 33
    // 8.19 - 8.29 pm
    public int search(int[] nums, int target) {

      // edge
      if(nums == null || nums.length == 0){
            return -1; // ?
        }
      if(nums.length == 1){
          return nums[0] == target ? 0 : - 1;
      }
      // edge case: array already in ascending ordrr
      // regular binary search
      int l = 0;
      int r = nums.length - 1;
      if(nums[r] >= nums[l]){
          while(r >= l){
              int mid = (l + r) / 2;
              if(nums[mid] == target){
                  return mid;
              }
              if(nums[mid] > target){
                  r = mid - 1;
              }else{
                  l = mid + 1;
              }
          }
          return -1; // can't find a solution
      }

      /**
       *  check if `mid` is left or right part
       *  -> if mid is in `left part` ( max in left sub array)
       *      -> if mid_val == target -> return mid as res
       *      ->  if  mid_val < target
       *          - search left sub array
       *      -> if mid_val > target
       *          - search right sub array
       *
       *  -> if mid is in `right part` (min in right sub array)
       *      -> if mid_val == target -> return mid as res
       *      ->  if  mid_val > target
       *            - search left sub array
       *       - if  mid_val < target
       *            - search right sub array ??
       *
       */

      while (r >= l){
          int mid = (l + r) / 2;
          if(nums[mid] == target){
              return mid;
          }
          // case 1) if mid is in `left part` (max in left sub array)
          if(nums[mid] >= nums[l]){
              //  search left sub array
              if(nums[mid] > target && target >= nums[l]){
                  r = mid - 1;
              }else{
                  // search right sub array
                  l = mid + 1;
              }
          }
          // case 2) if mid is in `right part` (min in right sub array)
          else{
              // search left sub array
              if(nums[mid] < target && target <= nums[r]){
                  l = mid + 1;
              }else{
                  // search right sub array ??
                  r = mid - 1;
              }
          }
      }

        return -1;
    }

    // LC 981
    // 9.50 - 10.10 AM
    /**
     * Your TimeMap object will be instantiated and called as such:
     * TimeMap obj = new TimeMap();
     * obj.set(key,value,timestamp);
     * String param_2 = obj.get(key,timestamp);
     */
    /**
     * Create a timebased key-value store class TimeMap, that supports two operations.
     *
     * 1. set(string key, string value, int timestamp)
     *
     * Stores the key and value, along with the given timestamp.
     * 2. get(string key, int timestamp)
     *
     * Returns a value such that set(key, value, timestamp_prev) was called previously,
     * with timestamp_prev <= timestamp.
     * If there are multiple such values, it returns the one with the largest timestamp_prev.
     * If there are no values, it returns the empty string ("").
     *
     *
     * Example 1:
     *
     * Input: inputs = ["TimeMap","set","get","get","set","get","get"], inputs = [[],["foo","bar",1],["foo",1],["foo",3],["foo","bar2",4],["foo",4],["foo",5]]
     * Output: [null,null,"bar","bar",null,"bar2","bar2"]
     * Explanation:
     *
     * TimeMap kv;
     * kv.set("foo", "bar", 1); // store the key "foo" and value "bar" along with timestamp = 1
     * kv.get("foo", 1);  // output "bar"
     * kv.get("foo", 3); // output "bar" since there is no value corresponding to foo at timestamp 3 and timestamp 2, then the only value is at timestamp 1 ie "bar"
     * kv.set("foo", "bar2", 4);
     * kv.get("foo", 4); // output "bar2"
     * kv.get("foo", 5); //output "bar2"
     *
     * Example 2:
     *
     * Input: inputs = ["TimeMap","set","set","get","get","get","get","get"], inputs = [[],["love","high",10],["love","low",20],["love",5],["love",10],["love",15],["love",20],["love",25]]
     * Output: [null,null,null,"","high","high","low","low"]
     *
     *
     *
     * -> need to store k-v and its timestamp
     * -> when call get(key, timesatmp), need to k-v that is `latest previous` timestamp, return null if can't find one
     *
     *
     *  IDEA 1) HASHMAP
     *
     *   {k: [v, timestamp]}
     *      -> easy, but hard to get `latest prev` time
     *
     *  IDEA 2) HASHMAP + PQ ??
     *
     *   -> {k: v}
     *   -> pq(t1, t2,...)
     *
     *   IDEA 3) HASHMAP V2
     *
     *   {k: [v, [t1, t2, ....]]}
     *
     *   -> so when a k is called, we know all its prev values (with t)
     *      and we can sort, can check if there is the `latest prev` one
     *
     */
    class TimeMap {

        // attr
        /** keyValueMap : {k: v} */
        Map<String, List<String>> keyValueMap;
        /**
         * NOTE !!!
         *   InsertTimeMap : {v: [t1, t2, ...]}
         */
        Map<String, List<Integer>> InsertTimeMap;

        public TimeMap() {
            this.keyValueMap = new HashMap<>();
            this.InsertTimeMap = new HashMap<>();
        }

        public void set(String key, String value, int timestamp) {

            // update keyValueMap
            List<String> values = this.keyValueMap.getOrDefault(key, new ArrayList<>());
            //values = this.keyValueMap.get(key);
            values.add(value);
            this.keyValueMap.put(key, values);

            // update InsertTimeMap
            List<Integer> times = this.InsertTimeMap.getOrDefault(key, new ArrayList<>());
            times.add(timestamp);
            this.InsertTimeMap.put(key, times);
        }

        public String get(String key, int timestamp) {
            if (!this.keyValueMap.containsKey(key)){
                return "";
            }

            // V1 : linear search (TLE)
            List<Integer> times = this.InsertTimeMap.get(key);
//            while (timestamp >= 0){
//                if (times.contains(timestamp)){
//                    int idx = times.indexOf(timestamp);
//                    return this.keyValueMap.get(key).get(idx);
//                }
//                timestamp -= 1;
//            }

            // V2 : binary search (OK)
            int idx = this.sortGetLatestTime(timestamp, times);

            return idx >= 0 ? this.keyValueMap.get(key).get(idx) : "";
        }

        private int sortGetLatestTime(int timestamp, List<Integer> times){
            Collections.sort(times, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    int diff = o2 - o1;
                    return diff;
                }
            });

            for(int i = 0; i < times.size(); i++){
                if (times.get(i) <= timestamp){
                    return i;
                }
            }

            return -1;
        }
    }

//        private int binaryGetLatestTime(int timestamp, List<Integer> times){
//            int left = 0;
//            int right = times.size() - 1;
//            // NOTE !!!! right >= left
//            while (right >= left){
//                int mid = (left + right) / 2;
//                Integer val = times.get(mid);
//                /**
//                 *  NOTE !!!
//                 *
//                 *   Returns a value such that set(key, value, timestamp_prev) was called previously, with timestamp_prev <= timestamp.
//                 *   If there are multiple such values, it returns the one with the largest timestamp_prev.
//                 *
//                 *
//                 *   -> so what we want is `the biggest time`  that <= input timestamp
//                 *   -> so if `val.equals(timestamp)`, it's the affordable solution
//                 */
//                if (val.equals(timestamp)){
//                    return mid;
//                }
//                if (val > timestamp){
//                    /**
//                     * NOTE !!!
//                     *
//                     *  (binary search pattern)
//                     *  right  = mid - 1;
//                     */
//                    right = mid - 1;
//                }else{
//                    /**
//                     * NOTE !!!
//                     *
//                     *  (binary search pattern)
//                     *  left = mid + 1;
//                     */
//                    left = mid + 1;
//                }
//            }
//
//            //return right <= timestamp ? right : -1;
//            //return -1;
//            /**
//             * NOTE !!!!
//             *
//             *  need to have below handling logic:
//             *  if right is a valid idx (>=0), then return it
//             *  as binary search result, otherwise return -1
//             */
//            return right >= 0 ? right : -1;
//        }
//    }
//    class TimeMap {
//
//        // idea 3
//        // attr
//        // {k: {v: [t1, t2, ....]}}
//        Map<String, Map<String, List<Integer>>> map;
//
//        public TimeMap() {
//            this.map = new HashMap<>();
//        }
//
//        public void set(String key, String value, int timestamp) {
//            Map<String, List<Integer>> valTimestamps = this.map.getOrDefault(key, new HashMap<>());
//            List<Integer> timestamps = valTimestamps.getOrDefault(value, new ArrayList<>());
//            timestamps.add(timestamp);
//            valTimestamps.put(value, timestamps);
//            this.map.put(key, valTimestamps);
//        }
//
//        public String get(String key, int timestamp) {
//            if(!this.map.containsKey(key)){
//                return null;
//            }
//            Map<String, List<Integer>> valTimestamps = this.map.get(key);
//            //List<Integer> timestamps = valTimestamps.values()[0]; // ??
//            // sort on ist<Integer>, decreasing order (big -> small)
//            List<String> keys = new ArrayList<>(valTimestamps.keySet()); // ??
////            Collections.sort(keys, new Comparator<String>() {
////                @Override
////                public int compare(String o1, String o2) {
////                    int diff = valTimestamps.get(o2) - valTimestamps.get(o1);
////                    return 0;
////                }
////            });
//
//            return null;
//        }
//    }

    // LC 004
    // 10.37 - 10.47 am
    /**
     * 4. Median of Two Sorted Arrays
     * There are two sorted arrays nums1 and nums2 of size m and n respectively.
     *
     * Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).
     *
     * You may assume nums1 and nums2 cannot be both empty.
     *
     * Example 1:
     *
     * nums1 = [1, 3]
     * nums2 = [2]
     *
     * The median is 2.0
     * Example 2:
     *
     * nums1 = [1, 2]
     * nums2 = [3, 4]
     *
     * The median is (2 + 3)/2 = 2.5
     *
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        // edge
        if(nums1 == null || nums2 == null){
            if(nums1 == null){
                int mid = (0 + nums2.length - 1) / 2;
                return nums2[mid]; // ???
            }else{
                int mid = (0 + nums1.length - 1) / 2;
                return nums1[mid]; // ???
            }
        }

        // if nums1 len equals nums2 len
        if(nums1.length == nums2.length){
            return ( nums1[nums1.length-1] + nums2[0] ) / 2.0 ; // ???
        }

        int len1 = nums1.length;
        int len2 = nums2.length;

        int len = len1 + len2;
        int mid = len / 2;
        if(mid < nums1.length){
            return nums1[mid];
        }
        return nums2[mid - len1]; // ???
    }

    // LC 206
    // 10.52 - 11.10 am
    public ListNode reverseList(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }

        ListNode prev = null; // NOTE !!! init prev as null, but NOT new ListNode()
        while(head != null){
            ListNode _next = head.next;
            ListNode _cur = head;
            /**
             *  4 steps
             *
             *  1) cache prev (as null)
             *  2) cache next
             *  3) cache current
             *  4) point cur to prev
             *  5) move prev to cur
             *  6) move cur to next
             */
            head.next = prev;
            prev = _cur;
            head = _next;
        }

        return prev; // ??
    }

    // LC 92
    // 11.10 am - 11.20 am
    /**
     * 92. Reverse Linked List II
     *
     * Reverse a linked list from position m to n. Do it in one-pass.
     *
     * Note: 1 ≤ m ≤ n ≤ length of list.
     *
     * Example:
     *
     * Input: 1->2->3->4->5->NULL, m = 2, n = 4
     * Output: 1->4->3->2->5->NULL
     *
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {

        // edge
        if(head == null || head.next == null){
            return head;
        }
        if(right < left){
            return head;
        }

        ListNode res = null;
        res.next = head;

        // move to `left` idx
        int i = 0;
        while (i < left){
            head = head.next;
            i += 1;
        }

        ListNode tmp = head; // ???

        // reverse linked list
        int j = 0;
        ListNode _pvev = tmp;

        while(head != null && j < (right - left)){
            ListNode _next = head.next;
            //ListNode _cur  = head;
            head.next = _pvev;
            _pvev = head;
            head = _next; //head.next;
            j += 1;
        }

        //tmp.next = _pvev;  // ???

        return res.next;
    }

    // LC 143
    // 11.51 - 12.10 pm
    /**
     * 143. Reorder List
     * Given a singly linked list L: L0→L1→…→Ln-1→Ln,
     * reorder it to: L0→Ln→L1→Ln-1→L2→Ln-2→…
     *
     * You may not modify the values in the
     * list's nodes, only nodes itself may be changed.
     *
     * Example 1:
     *
     * Given 1->2->3->4, reorder it to 1->4->2->3.
     *
     * Example 2:
     *
     * Given 1->2->3->4->5, reorder it to 1->5->2->4->3.
     */
    /**
     *  IDEA: LINKED LIST OP
     *
     *  step 1) split (half), get left, right sub linked list
     *  step 2) reverse right sub linked list
     *  step 3) merge: merge left, updated sub linked list in order
     *
     */
    public void reorderList(ListNode head) {
        // edge
        if(head == null || head.next == null){
            //return head;
            return;
        }

        // get linked list len
        int len = 0;
        ListNode head_1 = head;
        while(head_1 != null){
            len += 1;
            head_1 = head_1.next;
        }

        // step 1) split (half), get left, right sub linked list
        ListNode head_2 = head;
        //ListNode head_2_copy = head_2;
        for(int i = 0; i < len / 2; i++){
            head_2 = head_2.next;
        }

        // step 2) reverse right sub linked list
        ListNode _prev = null;
        while(head_2 != null){
            ListNode _next = head_2.next;
            head_2.next = _prev;
            _prev = head_2;
            head_2 = head_2.next;
        }

        // step 3) merge: merge left, updated sub linked list in order
        ListNode leftNode = null; // ??
        ListNode rightNode = _prev; // ??

        ListNode res = null;

        while(leftNode != null && rightNode != null){
            res.next = leftNode;
            leftNode = leftNode.next;
            res = res.next; // ???
            res.next = rightNode;
            rightNode = rightNode.next;
        }

        if(leftNode != null){
            res.next = leftNode;
        }else{
            res.next = rightNode;
        }

        // ??
    }

    // LC 138
    // 10.30 - 10.40 AM
    /*
    // Definition for a Node.
    class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }
    */
    class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }
    /**
     *
     *  A linked list is given such that each node contains an additional
     *  random pointer which could point to any node in the list or null.
     *
     * Return a deep copy of the list.
     *
     *
     *
     *  IDEA : LINKED LIST OP
     *
     *  -> go through linked list, copy node, and its next node ???
     *
     *
     */
    public Node copyRandomList(Node head) {

        // edge
        if(head == null || head.next == null){
            return head;
        }

        List<Integer> nodeValList = new ArrayList<>();
        Node head2 = head;
        while(head2 != null){
            nodeValList.add(head2.val);
            head2 = head2.next;
        }


        Node res = null;
        res.next = head;
        while(head != null){
            //res = head;
            //Node copied = new Node(head.val, null, new Node(getRandom(nodeValList)));
            res = res.next; // ???
            head = head.next;
        }

        return res.next;
    }

//    private int getRandom(List<Integer> nodeValList){
//        Random random = new Random();
//        int idx = random.nextInt(0, nodeValList.size()-1);
//        return nodeValList.get(idx);
//    }

    // LC 002
    // 11.01 - 11.11 am
    /**
     * 2. Add Two Numbers
     * You are given two non-empty linked lists
     * representing two non-negative integers.
     * The digits are stored in reverse order and each
     * of their nodes contain a single digit.
     * Add the two numbers and return it as a linked list.
     *
     * You may assume the two numbers do not contain
     * any leading zero, except the number 0 itself.
     *
     * Example:
     *
     * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
     * Output: 7 -> 0 -> 8
     * Explanation: 342 + 465 = 807.
     *
     */
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
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        while(l1 != null){
            sb1.append(l1.val);
            l1 = l1.next;
        }

        while(l2 != null){
            sb2.append(l2.val);
            l2 = l2.next;
        }

        System.out.println(">>> sb1 str = " + sb1.toString());
        System.out.println(">>> sb2 str = " + sb2.toString());

        int i1 = Integer.parseInt(sb1.reverse().toString());
        int i2 = Integer.parseInt(sb2.reverse().toString());

        int intSum = i1 + i2;
        String resStr = String.valueOf(intSum);
        String[] resList = resStr.split("");

        ListNode res = null;

        for(int i = resList.length -1; i > 0; i--){
            ListNode newNode = new ListNode(Integer.parseInt(resList[i]));
            res.next = newNode;
            res = newNode;
        }

        //return i1 + i2;
        return res.next;
    }


    // LC 287
    // 11.23 - 11.33
    /**
     * 287. Find the Duplicate Number
     *
     * Given an array nums containing n + 1 integers
     * where each integer is between 1 and n (inclusive),
     * prove that at least one duplicate number must exist.
     * Assume that there is only one duplicate number, find the duplicate one.
     *
     * Example 1:
     *
     * Input: [1,3,4,2,2]
     * Output: 2
     *
     *
     * Example 2:
     *
     * Input: [3,1,3,4,2]
     * Output: 3
     * Note:
     *
     * You must not modify the array (assume the array is read only).
     * You must use only constant, O(1) extra space.
     * Your runtime complexity should be less than O(n2).
     * There is only one duplicate number in the array, but it could be repeated more than once.
     *
     */
    public int findDuplicate(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        for(int x: nums){
            if(set.contains(x)){
                return x;
            }
            set.add(x);
        }

        return -1;
    }

    // LC 146
    // 11.29 - 11.40 am
    /**
     *
     *Design and implement a data structure for Least Recently Used (LRU) cache.
     *
     * It should support the following operations: get and put.
     *
     * get(key) - Get the value (will always be positive) of the key
     * if the key exists in the cache, otherwise return -1.
     *
     * put(key, value) - Set or insert the value if the key is
     * not already present. When the cache reached its capacity,
     *
     * it should invalidate the least recently used item before inserting a new item.
     *
     * The cache is initialized with a positive capacity.
     *
     * Follow up:
     * Could you do both operations in O(1) time complexity?
     *
     * Example:
     *
     *
     * ->  Least Recently Used (LRU) cache.
     *
     *
     *
     *  IDEA:
     *      STACK (FILO) + HASHMAP (record k-v)
     *      or DEQUEUE + HASHMAP ??
     *
     *
     */
    class LRUCache {

        // attr
        int capacity;
        //Stack<Integer> leastUsed;
        // dqeueue : [ key1, key2, .... ]
        Deque<Integer> leastUsed;
        Map<Integer, Integer> map;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            //this.leastUsed = new Stack<>();
            this.leastUsed = new LinkedList<>(); // ???
            this.map = new HashMap<>();
        }

        public int get(int key) {
            if(this.map.isEmpty()){
                return -1;
            }
            if(!this.map.containsKey(key)){
                return -1;
            }

            int toAdd = -1;
            // update `least usage`
            for(int i = 0; i < this.leastUsed.size(); i++){
                Integer tmp = this.leastUsed.pop();
                if(tmp == key){
                    //this.leastUsed.add(tmp);
                    // NOT change `ordering` of other elements in stack
                    //break;
                    toAdd = tmp;
                    continue;
                }
                this.leastUsed.add(tmp);
            }

            this.leastUsed.add(toAdd);

            return this.map.get(key);
        }

        public void put(int key, int value) {
            if(this.leastUsed.size() >= this.capacity){
                // need to remove element
                this.map.remove(key);
                // update `least usage`
                for(int i = 0; i < this.leastUsed.size(); i++){
                    Integer tmp = this.leastUsed.pop();
                    if(tmp == key){
                        break;
                    }
                    this.leastUsed.add(tmp);
                }
            }else{

            }
            // add `least usage`
            this.leastUsed.add(key); // ???
            // insert to map
            this.map.put(key, value);
        }
    }

    // LC 25
    // 9.16 - 9.26 AM
    /**
     *   IDEA 1) Linked list op
     *     -> find head_len / k,
     *     -> then for each sub array (len = head_len / k), reverse it
     *     -> keep remaining the same
     *     -> return result
     *
     *
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        // edge
        if(head == null || head.next == null){
            return null;
        }
        int len = 0;
        ListNode head2 = head;
        while(head2 != null){
            len += 1;
            head2 = head2.next;
        }
        if(k > len){
            return head;
        }

        int subNodeLen = len / k;
        if(subNodeLen <= 0){
            return head;
        }
        // ??? optimize below
        List<ListNode> nodeList = new ArrayList<>();
        ListNode head3 = head;
        int j = 0;
        ListNode tmp = null;
        while(head3 != null &&  j < k){
            tmp.next = head3;
            tmp = tmp.next;

            j += 1;
            head3 = head3.next;
        }



        return null;
    }

    private ListNode reverseNode(ListNode node){
        if(node == null || node.next == null){
            return null;
        }
        ListNode _prev = null;
        while(node != null){
            ListNode _next = node.next;
            _prev.next = node;
            _prev = node;
            node = _next;
        }

        return _prev.next;
    }

    // LC 543
    // https://leetcode.com/problems/diameter-of-binary-tree/
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
    // 10.50 am - 11.05 am
    /**
     *
     *  NOTE !!!
     *
     *  The diameter of a binary tree is the length
     *  of the longest path between  `any two nodes in a tree. `
     *
     *  IDEA 1) DFS ??
     *
     *  can we use BFS ??
     *
     *
     *
     */
    public class NodeAndIdx{
        TreeNode node;
        int idx;
        public NodeAndIdx(TreeNode node, int idx){
            this.node = node;
            this.idx = idx;
        }

        @Override
        public String toString() {
            return "NodeAndIdx{" +
                    "node=" + node +
                    ", idx=" + idx +
                    '}';
        }
    }

    List<NodeAndIdx> collected = new ArrayList<>();
    public int diameterOfBinaryTree(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return 0;
        }

        // dfs
        NodeAndIdx nodeAndIdx = new NodeAndIdx(root, 0);
        getNodeAndIdx(nodeAndIdx);

        System.out.println(">>> before sort, collected = " + collected);
        // sort (decreasing order) (big -> small)
        Collections.sort(collected, new Comparator<NodeAndIdx>() {
            @Override
            public int compare(NodeAndIdx o1, NodeAndIdx o2) {
                int diff = o2.idx - o1.idx;
                return diff;
            }
        });

        System.out.println(">>> after sort, collected = " + collected);

        return collected.get(0).idx - collected.get(collected.size()-1).idx;
    }

    private NodeAndIdx getNodeAndIdx(NodeAndIdx nodeAndIdx){
        if(nodeAndIdx.node == null){
            //return 0;
            return null;
            //return;
        }
        // add to cache
        collected.add(nodeAndIdx);

        TreeNode curNode = nodeAndIdx.node;
        int curIdx = nodeAndIdx.idx;
        NodeAndIdx leftNode = new NodeAndIdx(curNode.left, curIdx-1);
        NodeAndIdx rightNode = new NodeAndIdx(curNode.right, curIdx+1);

        return nodeAndIdx; // ?
        //return; // ?
    }



    public class Solution {
        public class TreeNode2 {
            int val;
            TreeNode2 left;
            TreeNode2 right;
            TreeNode2(int x) { val = x; }
        }

        private int maxDiameter = 0;

        public int diameterOfBinaryTree(TreeNode2 root) {
            if (root == null) {
                return 0;
            }
            dfs(root);
            return maxDiameter;
        }

        private int dfs(TreeNode2 node) {
            if (node == null) {
                return 0;
            }
            int leftHeight = dfs(node.left);
            int rightHeight = dfs(node.right);
            maxDiameter = Math.max(maxDiameter, leftHeight + rightHeight);
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    // LC 110
    // https://leetcode.com/problems/balanced-binary-tree/description/
    // 5.24 - 5.34 pm
    /**
     *
     *
     *  Given a binary tree, determine if it is `height-balanced`
     * .
     * -> height-balanced:
     *
     *  A height-balanced binary tree is a binary tree in
     *  which the depth of the two subtrees of every node
     *  `never` differs by more than one.
     *
     *  -> e.g. dist(sub_left, sub_right) <= 1
     *
     *  IDEA 1) DFS
     *   ->
     *
     *
     *
     */
    public boolean isBalanced(TreeNode root) {
        // edge
        if(root == null){
            return true;
        }
        if(root.left == null && root.right == null){
            return true;
        }
        if(root.left == null || root.right == null){
            return false;
        }

        return false;
       // return balanceHelper(root);
    }

    private boolean balanceHelper(TreeNode root, int height){

        if(root == null){
            return true; //?
        }

        balanceHelper(root.left, height+1);
        balanceHelper(root.right, height+1);

        return true;
    }


//    public boolean isBalanced(TreeNode root) {
//        // edge
//        if(root == null){
//            return true;
//        }
//        if(root.left == null && root.right == null){
//            return true;
//        }
//
//        // dfs ??
//        return dfsCheckBalanced(root, 0);
//    }
//
//    private boolean dfsCheckBalanced(TreeNode root, int depth){
//        if(root == null){
//            return true;
//        }
//
////        int leftDepth =  this.dfsCheckBalanced(root.left, depth+1);
////        this.dfsCheckBalanced(root.right, depth+1);
//
//        return false;
//    }

    // LC 199
    // 6.17 - 6.27 pm
    /**
     *  IDEA: BFS
     *
     *  `in-order` traversal
     *   e.g. left -> root -> right
     *
     *  so we know the tree node in order in each layer
     *  so can collect `right view nodes` as result
     *
     */
    public class NodeLayer{
        TreeNode node;
        int layer;

        public NodeLayer(TreeNode node, int layer){
            this.node = node;
            this.layer = layer;
        }
    }
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        // edge
        if(root == null){
            return res;
        }
        if(root.left == null && root.right == null){
            res.add(root.val);
            return res;
        }

        List<List<Integer>> cache = new ArrayList<>();
        cache.add(new ArrayList<>());

        // bfs
        Queue<NodeLayer> q = new LinkedList<>();
        NodeLayer nodeLayer = new NodeLayer(root, 0);
        q.add(nodeLayer);

        while(!q.isEmpty()){
            NodeLayer curNodeLayer = q.poll();
            TreeNode node = curNodeLayer.node;
            int layer = curNodeLayer.layer;

            if(cache.size() < layer - 1){
                cache.add(new ArrayList<>());
            }

            System.out.println(">>> cache size = " + cache.size() + ", layer = " + layer);

            cache.get(layer).add(node.val);

            if(node.left != null){
                q.add(new NodeLayer(node.left, layer+1));
            }
            if(node.right != null){
                q.add(new NodeLayer(node.right, layer+1));
            }
        }

        System.out.println(">>> cache = " + cache);
        for(List<Integer> x: cache){
            res.add(x.get(x.size()-1));
        }

        return res;
    }

  // LC 1448
  // 4.49 - 5.20 pm
  /**
   * Given a binary tree root,
   * a node X in the tree is named good if
   * in the path from root to X there are no
   * nodes with a value greater than X.
   *
   * Return the number of good nodes in the binary tree.
   *
   * -> `good` if in the path from root to X
   *   there are no nodes with a value greater than X.
   *
   */
  /**
   *  IDEA:  BOTTOM UP DFS
   *
   *
   *  --> a node X in the tree is named `good` if
   *  in the `path` from root to X there are no nodes with a value greater than X.
   *
   *
   *   NOTE !!!
   *   -> if a node is `good node`, its sub node MUST are all good nodes
   *
   *    -> step 1) check sub tree, if each of them is `good node`
   *    -> step 2) then we know sub tree good node list
   *    -> step 3) check its parent, if it's good node
   *    -> step 4) repeat above steps,,, update res as well
   *
   *    return res as result
   *
   */
  int goodNodeCnt = 1;
  public int goodNodes(TreeNode root) {
      // edge
      if(root == null){
          return 0;
      }
      if(root.left == null && root.right == null){
          return 1;
      }
      // dfs
      this.checkGoodNode(root, root.val);
      return goodNodeCnt;
  }

  private void checkGoodNode(TreeNode root, int maxSoFar){
      if(root == null){
          //return null;
          return;
      }
      if(root.val > maxSoFar){
          goodNodeCnt += 1;
      }

      if(root.left != null){
          this.checkGoodNode(
                  root.left,
                  Math.max(root.left.val, maxSoFar)
          );
      }

      if(root.right != null){
          this.checkGoodNode(
                  root.right,
                  Math.max(root.right.val, maxSoFar)
          );
      }

      //return root; // /??
  }


//  int res = 1;
//  public int goodNodes(TreeNode root) {
//
//      // edge
//      if(root == null){
//          return 0;
//      }
//      if(root.left == null && root.right == null){
//          return 1;
//      }
//
//      // dfs
//      dfsCheckGoodNode(root, new ArrayList<>());
//      System.out.println(">>> res = " + res);
//
//      return res;
//    }
//
//    private TreeNode dfsCheckGoodNode(TreeNode root, List<TreeNode> visited){
//       if(root == null){
//           return null;
//       }
//
//       // ???
//       if(!isGood(visited, root)){
//           return null;
//        }
//
//        if(isGood(visited, root)){
//            res += 1;
//            return null;
//        }
//
//       visited.add(root);
//
//       root.left = dfsCheckGoodNode(root, visited);
//       root.right = dfsCheckGoodNode(root, visited);
//
//       return root;
//    }
//
//    private boolean isGood(List<TreeNode> visited, TreeNode root){
//      boolean res = true;
//      for(TreeNode x: visited){
//          if(root.val <= x.val){
//              return false;
//          }
//      }
//      return res;
//    }


//    int res = 1;
//    public class NodeAndPath{
//        TreeNode node;
//        List<TreeNode> path;
//
//        public NodeAndPath(TreeNode node, List<TreeNode> path){
//            this.node = node;
//            this.path = path;
//        }
//    }
//    public int goodNodes(TreeNode root) {
//
//        // edge
//        if(root == null){
//            return 0;
//        }
//        if(root.left == null && root.right == null){
//            return 1;
//        }
//
//        NodeAndPath nodeAndPath = new NodeAndPath(root, new ArrayList<>());
//
//        checkGoodNode(nodeAndPath);
//        return res;
//    }
//
//    private NodeAndPath checkGoodNode(NodeAndPath nodeAndPath){
//        if(nodeAndPath.node == null){
//            return null;
//        }
//        if(isGood(nodeAndPath.path, nodeAndPath.node)){
//            res += 1;
//        }
//
//        TreeNode node = nodeAndPath.node;
//        List<TreeNode> path = nodeAndPath.path;
//
//        path.add(node);
//        NodeAndPath leftNodeAndPath = new NodeAndPath(node.left, path);
//        NodeAndPath rightNodeAndPath = new NodeAndPath(node.right, path);
//
//        NodeAndPath nodeAndPath1 = new NodeAndPath(nodeAndPath.node, new ArrayList<>());
//        //return null; // ???
//        return nodeAndPath1;
//    }
//
//    private boolean isGood(List<TreeNode> visited, TreeNode root){
//        if(visited.isEmpty()){
//            return true;
//        }
//        boolean res = true;
//        for(TreeNode x: visited){
//            if(root.val <= x.val){
//                return false;
//            }
//        }
//        return res;
//    }

    // LC 105
    // 4.02 PM - 4.15 PM
    /**
     *  IDEA 1) BINARY TREE property
     *
     *   preorder : root -> left -> right
     *   inorder: left -> root -> right
     *
     *  -> step 1) find root via preorder[0]
     *  -> step 2) get `radius` via distance(left, root) in inorder
     *  -> step 3) build root.left, root.right via sub array at preorder, inorder
     *
     *
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // edge
        if(preorder.length == 0){
            return new TreeNode();
        }
        if(preorder.length == 1){
            return new TreeNode(preorder[0]);
        }
        // ??? can merge to below code ?
//        if(preorder.length == 2){
//            TreeNode node = new TreeNode(preorder[0]);
//            node.left = new TreeNode(preorder[1]);
//            return node;
//        }

        return dfsBuildTree(preorder, inorder);
    }

    private TreeNode dfsBuildTree(int[] preorder, int[] inorder){

        // edge
        if(preorder.length == 0){
            return new TreeNode();
        }
        if(preorder.length == 1){
            return new TreeNode(preorder[0]);
        }

        int root = preorder[0];
        int radius = findRoot(inorder, root);

        // ??
        TreeNode node = new TreeNode(root);

        // get sub left, sub right tree
        node.left = this.dfsBuildTree(
                Arrays.copyOfRange(preorder, 1, 1 + radius),
                Arrays.copyOfRange(inorder, 0, radius)
        );

        node.right = this.dfsBuildTree(
                Arrays.copyOfRange(preorder, 1 + radius, preorder.length),
                Arrays.copyOfRange(inorder, 1 + radius, inorder.length)
        );

        return node;
    }

    private int findRoot(int[] arr, int x){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == x){
                return i;
            }
        }
        return 0;
    }

    // LC 106
    // 4.51 pm - 5.10 pm
    /**
     *  IDEA: BINARY TREE property
     *
     *  ->  inorder: left -> root -> right
     *  ->  postorder: left -> right -> root
     *
     *  -> step 1) find root via preorder[0]
     *  -> step 2) get `radius` via distance(left, root) in inorder
     *  -> step 3) build root.left, root.right via sub array at inorder, postorder
     *
     */
    public TreeNode buildTree_(int[] inorder, int[] postorder) {

        // edge
        if(inorder.length == 0){
            return new TreeNode();
        }
        if(inorder.length == 1){
            return new TreeNode(inorder[0]);
        }

        return dfsBuildTree2(inorder, postorder);
    }

  private TreeNode dfsBuildTree2(int[] postorder, int[] inorder) {

//      // edge
//      if(inorder.length == 0){
//          return new TreeNode();
//      }
//      if(inorder.length == 1){
//          return new TreeNode(inorder[0]);
//      }

        int root = postorder[postorder.length-1];
        int radius = findRoot2(inorder, root);

        // ??
        TreeNode node = new TreeNode(root);

        // get sub left, sub right tree
        node.left = this.dfsBuildTree2(
                Arrays.copyOfRange(postorder, 0, radius),
                Arrays.copyOfRange(inorder, 0, radius)
        );

        node.right = this.dfsBuildTree2(
                Arrays.copyOfRange(postorder, 1 + radius, postorder.length - 1),
                Arrays.copyOfRange(inorder, 1 + radius, inorder.length)
        );

        return node;
    }

    private int findRoot2(int[] arr, int x){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == x){
                return i;
            }
        }
        return 0;
    }

    // LC 124
    // 5.07 - 5.20 pm
    /**
     *  IDEA 1) DFS
     *
     *  -> step 1) visit the end of node first,
     *  -> step 2) init an array, and cumsum, maxSum
     *  -> step 3) keep append new val to array, update cumsum as well
     *      -> and update maxSum
     *
     *  return maxSum
     *
     */
    public int maxPathSum(TreeNode root) {

        return 0;
    }

    // LC 297
    // 2.33 pm - 2.43 pm
//    public class Codec {
//
//        // attr
//        StringBuilder sb;
//        TreeNode resRoot;
//
//        // Encodes a tree to a single string.
//        public String serialize(TreeNode root) {
//            this.sb = new StringBuilder();
//            // edge
//            if(root == null){
//                return "";
//            }
//            if(root.left == null && root.right == null){
//                this.sb.append(root.val);
//                return this.sb.toString();
//            }
//
//            // dfs go through node vals
//            this.nodeToString(root);
//            return sb.toString();
//        }
//
//        // Decodes your encoded data to tree.
//        public TreeNode deserialize(String data) {
//            // edge
//            if(data == null){
//                return new TreeNode();
//            }
//            if(!data.contains(",")){
//                return new TreeNode(Integer.parseInt(data));
//            }
//
//            // string to node
//            return this.resRoot; // ?
//        }
//
//        // ????
//        private TreeNode nodeToString(TreeNode root){
//            if(root == null){
//                return null;
//            }
//            // preorder: root -> left -> right
//            //this.sb.toString();
//            this.sb.append(root.val);
//            this.sb.append(",");
//
//            // ????
////            TreeNode leftRoot = this.nodeToString(root.left);
////            TreeNode rightRoot = this.nodeToString(root.right);
//
//            this.nodeToString(root.left);
//            this.nodeToString(root.right);
//
//            return root;
//        }
//
//        // ???
//        private String stringToNode(String str){
//            if(str == null){
//                return null;
//            }
//            String[] strArray = str.split(",");
//            String rootNode = strArray[0];
//            this.resRoot = new TreeNode(Integer.parseInt(rootNode));
//            //this.resRoot.left =;
//            return null;
//        }
//    }

    // 11.36 am - 11.46 am
    public class Codec{

        String nodeStr;
        TreeNode node;

        public void serialize(TreeNode root) {
            // edge
            if(root == null){
               // return ""; // ?
               this.nodeStr += "#"; // ???
               this.nodeStr += ",";
               return;
            }
            // tree -> string (pre-order) (root -> left -> right)
            this.nodeStr += root.val;
            this.nodeStr += ",";

            this.serialize(root.left);
            this.serialize(root.right);
            return ; // ????
        }

        public TreeNode deserialize(String data) {
            if(data == null || data.length() == 0){
                return this.node;
            }
            // split str, and build tree in pre-order (root -> left -> right)
            this.buildTreeHelper(data, 0);
            return this.node;
        }

        private TreeNode buildTreeHelper(String data, int idx){
            if(data == null || data.length() == 0 || data.equals("#")){
                return null; // ???
            }
            String[] dataArr = data.split(",");
            for(int i = 0; i < dataArr.length; i++){
                if(this.node != null){
                    this.node.left = new TreeNode(Integer.parseInt(dataArr[i]));
                    this.node.right = new TreeNode(Integer.parseInt(dataArr[i]));
                }
            }

            return this.node;
        }

    }


//    public class Codec{
//        public String serialize(TreeNode root) {
//
//            /** NOTE !!!
//             *
//             *     if root == null, return "#"
//             */
//            if (root == null){
//                return "#";
//            }
//
//            /** NOTE !!! return result via pre-order, split with "," */
//            //return root.val + "," + serialize(root.left) + "," + serialize(root.right);
//            serialize(root.left);
//            serialize(root.right);
//
//            return String.valueOf(root.val);
//        }
//
//        public TreeNode deserialize(String data) {
//
//            /** NOTE !!!
//             *
//             *   1) init queue and append serialize output
//             *   2) even use queue, but helper func still using DFS
//             */
//            Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
//            return helper(queue);
//        }
//
//        private TreeNode helper(Queue<String> queue) {
//
//            // get val from queue first
//            String s = queue.poll();
//
//            if (s.equals("#")){
//                return null;
//            }
//            /** NOTE !!! init current node  */
//            TreeNode root = new TreeNode(Integer.valueOf(s));
//            /** NOTE !!!
//             *
//             *    since serialize is "pre-order",
//             *    deserialize we use "pre-order" as well
//             *    e.g. root -> left sub tree -> right sub tree
//             *    -> so we get sub tree via below :
//             *
//             *       root.left = helper(queue);
//             *       root.right = helper(queue);
//             *
//             */
//            root.left = helper(queue);
//            root.right = helper(queue);
//            /** NOTE !!! don't forget to return final deserialize result  */
//            return root;
//        }
//    }

    // LC 703
    // 3.19 - 3.29 PM
    /**
     *  More specifically,
     *  -> we are looking for the kth highest score
     *     in the sorted list of all scores.
     *
     *
     *   int add(int val) Adds a new test score
     *   val to the stream and returns the element representing
     *   the kth largest element in the pool of test scores so far.
     *
     *
     *   IDEA 1) PQ
     *
     *   -> init a `big PQ`, e.g. big -> small (decreasing order)
     *
     */
//    class KthLargest {
//
//        PriorityQueue<Integer> pq;
//        int k;
//        int[] nums;
//
//        public KthLargest(int k, int[] nums) {
//            // ??? use Comparator.reverseOrder() to get `big PQ` ??
//            //this.pq = new PriorityQueue<>(Comparator.reverseOrder());
//            /**
//             *  -> we use `small PQ`, (small -> big)
//             *  -> since then we know the first element is
//             *  the k th element
//             *
//             */
//            //this.pq = new PriorityQueue<>();
//            this.pq = new PriorityQueue<>(Comparator.reverseOrder());
//            this.k = k;
//            this.nums = nums;
//        }
//
//        public int add(int val) {
//            pq.add(val);
//            /**
//             *  -> while PQ size > k,
//             *  -> we remove the `no needed` eleemnts
//             */
//            while(this.pq.size() > k){
//                pq.poll();
//            }
//            if(!pq.isEmpty()){
//                return pq.peek();
//            }
//            return -1; // should not happen
//        }
//    }

    class KthLargest {
        int k;
        int[] nums;
        PriorityQueue<Integer> pq;

        public KthLargest(int k, int[] nums) {
            this.k = k;
            this.nums = nums; // ???
            /**
             * // NOTE !!! we use small PQ
             * -> small -> big (increasing order)
             *
             */
            this.pq = new PriorityQueue<>();
            // NOTE !!! we also need to add eleemnts to PQ
            for(int x: nums){
                pq.add(x);
            }
        }

        public int add(int val) {
            this.pq.add(val);
            // NOTE !!! need to remove elements when PQ size > k
            while(this.pq.size() > k){
                this.pq.poll();
            }
            if(!this.pq.isEmpty()){
                return this.pq.peek();
            }
            return -1;
        }
    }

    // LC 889
    // 3.57 - 4.10 pm
    /**
     *  IDEA : binary tree property + dfs
     *
     *  preorder: root -> left -> right
     *
     *  postorder: left -> right -> root
     *
     *
     *  step 1) get root
     *  step 2) get `radius` via postorder
     *    -> d = distance(0, len_of_postorder)
     *    -> r = d / 2  ???
     *
     *
     */
//    TreeNode resNode = new TreeNode();
//    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {
//
//        this.helper(preorder, postorder);
//        return resNode;
//    }
//
//    private TreeNode helper(int[] preorder, int[] postorder){
//
//        int rootVal = preorder[0];
//        int radius = postorder.length / 2; // ???
//
//        resNode.left = this.helper(
//               Arrays.copyOfRange(preorder, 1, 1+ radius),
//                //Arrays.copyOfRange(preorder, 0,  radius),
//                Arrays.copyOfRange(postorder, 0, radius) // ????
//        );
//        resNode.right = this.helper(
//                Arrays.copyOfRange(preorder, 1 + radius, preorder.length),
//                Arrays.copyOfRange(postorder, radius, postorder.length - 1) // ????
//        );
//
//        return resNode;
//    }



    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {

        // O(n) time | O(h) space

        // base case : 	If the input arrays are null or empty, return null. This is the base case for recursion.
        if(preorder == null || postorder == null || preorder.length == 0 || postorder.length == 0){
            return null;
        }

        TreeNode root = new TreeNode(preorder[0]);
        int mid = 0;

        // Check if there’s only one node:
        if(preorder.length == 1){
            return root;
        }

        /** NOTE : 	Finding the Midpoint:
         *
         *	•	The second element of the preorder array is the root of the left subtree. We find this element in the postorder array.
         * 	•	The index mid represents the boundary between the left and right subtrees in both the preorder and postorder arrays.
         */
        // update mid
        for(int i = 0; i < postorder.length; i++){
            if(preorder[1] == postorder[i]){
                mid = i;
                break;
            }
        }

        // recursive Construction of Left and Right Subtrees:

        /**
         * The left subtree is constructed recursively using:
         * 	•	The preorder subarray from index 1 to 1 + mid + 1 (this includes the elements belonging to the left subtree).
         * 	•	The postorder subarray from index 0 to mid + 1.
         */
        root.left = constructFromPrePost(
                //Arrays.copyOfRange(preorder, 1, 1 + mid + 1),
                Arrays.copyOfRange(preorder, 1, mid + 1),
                Arrays.copyOfRange(postorder, 0, mid + 1));

        /**
         *  The right subtree is constructed recursively using:
         * 	•	The preorder subarray from index 1 + mid + 1 to the end (the elements belonging to the right subtree).
         * 	•	The postorder subarray from mid + 1 to postorder.length - 1.
         */
        root.right = constructFromPrePost(
                //Arrays.copyOfRange(preorder, 1 + mid + 1, preorder.length),
                Arrays.copyOfRange(preorder, mid + 1, preorder.length),
                Arrays.copyOfRange(postorder, mid + 1, postorder.length - 1));

        // After recursively constructing the left and right subtrees, the root node (with its left and right children) is returned.
        return root;
    }

    // LC 1046
    // 4.34 - 4.44 pm
    // IDEA: PQ (big queue, big -> small)
    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for(int x: stones){
            pq.add(x);
            if(pq.size() >= 2){
                int tmp1 = pq.poll();
                int tmp2 = pq.poll();
                int calRes = 0;
                if(tmp1 == tmp2){
                    continue;
                }else{
                    calRes = Math.abs(tmp1 - tmp2);
                }
                pq.add(calRes);
            }
        }
        if(!pq.isEmpty()){
            return pq.peek();
        }
        return -1; // should not reach this point
    }


    // LC 973
    // 10.17 - 10.27 AM
    /**
     *  973. K Closest Points to Origin
     *
     *
     * We have a list of points on the plane.
     *
     * -> Find the `K closest points` to the origin (0, 0).
     *
     * (Here, the distance between two points on a plane is the Euclidean distance.)
     *
     * You may return the answer in any order.
     * The answer is guaranteed to be unique (except for the order that it is in.)
     *
     *
     *
     * Example 1:
     *
     * Input: points = [[1,3],[-2,2]], K = 1
     * Output: [[-2,2]]
     * Explanation:
     * The distance between (1, 3) and the origin is sqrt(10).
     * The distance between (-2, 2) and the origin is sqrt(8).
     * Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
     * We only want the closest K = 1 points from the origin, so the answer is just [[-2,2]].
     *
     *
     * Example 2:
     *
     * Input: points = [[3,3],[5,-1],[-2,4]], K = 2
     * Output: [[3,3],[-2,4]]
     * (The answer [[-2,4],[3,3]] would also be accepted.)
     *
     *
     * Note:
     *
     * 1 <= K <= points.length <= 10000
     * -10000 < points[i][0] < 10000
     * -10000 < points[i][1] < 10000
     *
     *
     *
     *  IDEA 1) : HAHSMAP or (PQ )??
     *  -> {point: distance}
     *  -> so we can get the k `small` point via sorting
     *
     *
     */
    public int[][] kClosest(int[][] points, int k) {
        int[][] res = new int[][]{};
        // edge
        if(points == null || points.length == 0){
            return null; //?
        }
        if(points.length == 1){
            res = points;
            return res; // ???
        }
        // build map
        Map<List<Integer>, Integer> map = new HashMap<>();
        for(int[] p: points){
            int dis = (int) getDistance(p); // ???
            List<Integer> key = new ArrayList<>();
            key.add(p[0]);
            key.add(p[1]);
            map.put(key, dis);
        }

        // sorting
        //List<Set<Integer[]>> keys = Arrays.asList(map.keySet());
        //Object[] k = map.keySet().toArray();
        // List<String> res = new ArrayList(freq.keySet());
        //List<Set<List<Integer>>> keys = Arrays.asList(map.keySet());
        ArrayList<List<Integer>> keys = new ArrayList<>(map.keySet());

        System.out.println(">>> (before sort) keys = " + keys);
        Collections.sort(keys, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                return map.get(o1) - map.get(o2);
            }
        });

        System.out.println(">>> (after sort) keys = " + keys);
        //Set<List<Integer>> tmp = keys.get(k - 1);
        List<Integer> tmp = keys.get(k - 1);

        //List<List<Integer>> res2 =

        return new int[][] { {tmp.get(0), tmp.get(1)} };
    }

    private double getDistance(int[] point){
        int x = point[0];
        int y = point[1];
        return  Math.sqrt(x + y);
    }

    // LC 621
    /**
     * 621. Task Scheduler
     *
     * Given a char array representing tasks  CPU need to do.
     *
     * It contains capital letters A to Z where different letters
     * represent different tasks. Tasks could be done without original order.
     * Each task could be done in one interval. For each interval,
     * CPU could finish one task or just be idle.
     *
     * However, there is a non-negative cooling interval n that
     * means between two same tasks, there must be at least n intervals
     * that CPU are doing different tasks or just be idle.
     *
     * You need to return the least number of intervals the CPU will take
     * to finish all the given tasks.
     *
     *
     *
     * Example:
     *
     * Input: tasks = ["A","A","A","B","B","B"], n = 2
     * Output: 8
     * Explanation: A -> B -> idle -> A -> B -> idle -> A -> B.
     *
     *
     * Note:
     *
     * The number of tasks is in the range [1, 10000].
     * The integer n is in the range [0, 100].
     *
     *
     */
    public int leastInterval(char[] tasks, int n) {
        return 0;
    }

    // LC 355
    // 11.13- 11.28 am
    /**
     * 355. Design Twitter
     * Design a simplified version of Twitter where
     * users can post tweets, follow/unfollow another
     * user and is able to see the 10 most recent tweets
     * in the user's news feed. Your design should support the following methods:
     *
     *
     * -> user and is able to see the 10 most recent tweets
     *
     *
     * postTweet(userId, tweetId): Compose a new tweet.
     *
     * getNewsFeed(userId): Retrieve the 10 most recent tweet ids in the user's news feed. Each item
     *                      in the news feed must be posted by users who the user followed or by the user herself.
     *                      Tweets must be ordered from most recent to least recent.
     *
     * follow(followerId, followeeId): Follower follows a followee.
     *
     * unfollow(followerId, followeeId): Follower unfollows a followee.
     * Example:
     *
     * Twitter twitter = new Twitter();
     *
     * // User 1 posts a new tweet (id = 5).
     * twitter.postTweet(1, 5);
     *
     * // User 1's news feed should return a list with 1 tweet id -> [5].
     * twitter.getNewsFeed(1);
     *
     * // User 1 follows user 2.
     * twitter.follow(1, 2);
     *
     * // User 2 posts a new tweet (id = 6).
     * twitter.postTweet(2, 6);
     *
     * // User 1's news feed should return a list with 2 tweet ids -> [6, 5].
     * // Tweet id 6 should precede tweet id 5 because it is posted after tweet id 5.
     * twitter.getNewsFeed(1);
     *
     * // User 1 unfollows user 2.
     * twitter.unfollow(1, 2);
     *
     * // User 1's news feed should return a list with 1 tweet id -> [5],
     * // since user 1 is no longer following user 2.
     * twitter.getNewsFeed(1);
     */
    class Twitter {

        // {user_id: [list_of_users_followers]
        // list_of_users_followers: users who follow current user
        // user <- follow -- u1
        // user <- follow -- u2
        HashMap<Integer, List<Integer>> followers;
        // {user_id: [list_of_users_following]
        // list_of_users_following : users who such user is following to
        // user -- follow --> u1
        // user -- follow --> u2
        HashMap<Integer, List<Integer>> following;
        // pq : [list_of_post_id] (big Queue, big -> small)
        Stack<Integer> st; // stack : FILO

        // user: [post_ids]
        HashMap<Integer, List<Integer>> posts;

        Integer timestamp;

        /** Initialize your data structure here. */
        public Twitter() {
            this.followers = new HashMap<>();
            this.following = new HashMap<>();
            //this.pq = new PriorityQueue<>(Comparator.reverseOrder());
            this.st = new Stack<>();
            this.posts = new HashMap<>();
            this.timestamp = 0;
        }

        /** Compose a new tweet. */
        public void postTweet(int userId, int tweetId) {
            List<Integer> postIds =  this.posts.getOrDefault(userId, new ArrayList<>());
            postIds.add(tweetId);
            this.posts.put(userId, postIds);

            // update PQ

            // update st
            this.st.add(tweetId);
        }

        /**
         * Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed
         * must be posted by users who the user followed or by the user herself. Tweets must be ordered
         * from most recent to least recent.
         */
        public List<Integer> getNewsFeed(int userId) {
            if(this.posts.isEmpty() || this.posts.get(userId).isEmpty()){
                return null;
            }

            // define tmp stack // ????
            Stack<Integer> tmpSt = this.st;
            List<Integer> res = new ArrayList<>();
            while(res.size() < 10 && !tmpSt.isEmpty()){
                int postId = tmpSt.pop();
                res.add(postId);
            }

            return res;
        }

        /** Follower follows a followee. If the operation is invalid, it should be a no-op. */
        public void follow(int followerId, int followeeId) {
            List<Integer> followings = this.following.getOrDefault(followerId, new ArrayList<>());
            followings.add(followerId);
            this.following.put(followerId, followings);
        }

        /** Follower unfollows a followee. If the operation is invalid, it should be a no-op. */
        public void unfollow(int followerId, int followeeId) {
            if(!this.followers.containsKey(followerId)){
                return;
            }
            List<Integer> followees = this.followers.get(followerId);
            int idx = -1;
            for(int i = 0; i < followees.size(); i++){
                if(followees.get(i) == followeeId){
                    idx = i;
                    break;
                }
            }
            followees.remove(idx);
            this.followers.put(followerId, followees);
        }
    }

    // LC 295
    // 9.46 - 10.00 AM

    /**
     * The median is the middle value in an ordered integer list. If the size of the list is even, there is no middle value, and the median is the mean of the two middle values.
     *
     * For example, for arr = [2,3,4], the median is 3.
     * For example, for arr = [2,3], the median is (2 + 3) / 2 = 2.5.
     * Implement the MedianFinder class:
     *
     * MedianFinder() initializes the MedianFinder object.
     * void addNum(int num) adds the integer num from the data stream to the data structure.
     * double findMedian() returns the median of all elements so far. Answers within 10-5 of the actual answer will be accepted.
     *
     *
     * Example 1:
     *
     * Input
     * ["MedianFinder", "addNum", "addNum", "findMedian", "addNum", "findMedian"]
     * [[], [1], [2], [], [3], []]
     * Output
     * [null, null, null, 1.5, null, 2.0]
     *
     * Explanation
     * MedianFinder medianFinder = new MedianFinder();
     * medianFinder.addNum(1);    // arr = [1]
     * medianFinder.addNum(2);    // arr = [1, 2]
     * medianFinder.findMedian(); // return 1.5 (i.e., (1 + 2) / 2)
     * medianFinder.addNum(3);    // arr[1, 2, 3]
     * medianFinder.findMedian(); // return 2.0
     *
     *
     * Constraints:
     *
     * -105 <= num <= 105
     * There will be at least one element in the data structure before calling findMedian.
     * At most 5 * 104 calls will be made to addNum and findMedian.
     *
     *
     * Follow up:
     *
     * If all integer numbers from the stream are in the range [0, 100], how would you optimize your solution?
     * If 99% of all integer numbers from the stream are in the range [0, 100], how would you optimize your solution?
     *
     *
     */
    /**
     *  IDEA 1) SMALL, BIG PQ
     *
     *  -> [s1, s2, ..sn]
     *  -> [b1, b2,.. bn]
     *
     *   -> set sn is `current median val` ????
     *   -> so we can update small, big PQ accordingly when new element is inserted
     *
     *
     *
     *  -> so sn is biggest element in small PQ
     *  -> b1 is smallest element in big PQ
     *  ->  if number of element is even
     *     -> median = ( sn + b1 ) / 2
     *  ->  if number of element is odd
     *     -> ??
     *
     *  IDEA 2) sorting ???
     *
     */
        class MedianFinder {

        public MedianFinder() {

        }

        public void addNum(int num) {

        }

        public double findMedian() {
            return 0.0;
        }
    }

//    class MedianFinder {
//
//        // attr
//        List<Integer> collected;
//        int cnt;
//
//        public MedianFinder() {
//            this.cnt = 0;
//            this.collected = new ArrayList<>();
//        }
//
//        public void addNum(int num) {
//            this.collected.add(num);
//            // sort (increasing) (small -> big)
//            Collections.sort(this.collected, new Comparator<Integer>() {
//                @Override
//                public int compare(Integer o1, Integer o2) {
//                    int diff = o1 - o2;
//                    return diff;
//                }
//            });
//            this.cnt += 1;
//        }
//
//        public double findMedian() {
//            if(this.cnt == 0 || this.collected == null){
//                return 0;
//            }
//            if(this.cnt == 1){
//                return this.collected.get(0);
//            }
//
//            /**
//             *  if cnt is odd, [1,2,3]
//             *  if cnt is even, [1,2,3,4]
//             */
//            System.out.println(">>> this.cnt= " + this.cnt + ", this.collected = " + this.collected);
//            int midIdx = -1;
//            // size is even
//            if(this.cnt % 2 == 0){
//                midIdx = this.cnt / 2;
//                return (this.collected.get(midIdx) + this.collected.get(midIdx -1)) / 2.0;
//            }
//            // size is odd
//            else{
//                //midIdx = this.cnt / 2;
//                return this.collected.get(midIdx);
//            }
//        }
//    }

    /**
     * 78. Subsets
     * Solved
     * Medium
     * Topics
     * Companies
     * Given an integer array nums of unique elements, return all possible subsets (the power set).
     *
     * The solution set must not contain duplicate subsets. Return the solution in any order.
     *
     *
     *
     * Example 1:
     *
     * Input: nums = [1,2,3]
     * Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
     * Example 2:
     *
     * Input: nums = [0]
     * Output: [[],[0]]
     *
     *
     * Constraints:
     *
     * 1 <= nums.length <= 10
     * -10 <= nums[i] <= 10
     * All the numbers of nums are unique.
     *
     */
    // LC 78
    // 10.18 - 10.28 am
    // backtrack
    List<List<Integer>> res = new ArrayList<>();
    HashSet<Integer> visited = new HashSet<>();
    public List<List<Integer>> subsets(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return res;
        }

        // backtrack
        subSetHelper(nums, 0, new ArrayList<>(), this.visited);
        return res;
    }

    private void subSetHelper(int[] nums, int idx, List<Integer> cur, HashSet<Integer> visited){
        if(cur.size() > nums.length){
            return;
        }

        Collections.sort(cur);
        if(!visited.contains(cur)){
            this.res.add(new ArrayList<>(cur)); // ???
            //visited.add(cur);
        }
        for(int i = 0; i < nums.length; i++){
            if(!cur.contains(nums[i])){
                cur.add(nums[i]);
                this.subSetHelper(nums, idx+1, cur, visited);
                // undo
                idx -= 1; // ?
                cur.remove(cur.size()-1);
            }
        }
    }

    // LC 39
    /**
     * 39. Combination Sum
     * Given a set of candidate numbers (candidates) (without duplicates) and a target number (target), find all unique combinations in candidates where the candidate numbers sums to target.
     *
     * The same repeated number may be chosen from candidates unlimited number of times.
     *
     * Note:
     *
     * All numbers (including target) will be positive integers.
     * The solution set must not contain duplicate combinations.
     * Example 1:
     *
     * Input: candidates = [2,3,6,7], target = 7,
     * A solution set is:
     * [
     *   [7],
     *   [2,2,3]
     * ]
     * Example 2:
     *
     * Input: candidates = [2,3,5], target = 8,
     * A solution set is:
     * [
     *   [2,2,2,2],
     *   [2,3,3],
     *   [3,5]
     * ]
     *
     */

    List<List<Integer>> combinationSumRes = new ArrayList<>();
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        // edge
        // backtrack
        this.combinationSumHelper(candidates, target, new ArrayList<>());
        return combinationSumRes;
    }

    private void combinationSumHelper(int[] candidates, int target, List<Integer> cur){
        if(this.getSum(cur) > target){
            return;
        }
        if(this.getSum(cur) == target){
            Collections.sort(cur);
            if(!this.combinationSumRes.contains(cur)){
                this.combinationSumRes.add(new ArrayList<>(cur));
            }
        }
        for(int i = 0; i < candidates.length; i++){
            int curVal = candidates[i];
            cur.add(curVal);
            this.combinationSumHelper(candidates, target, cur);
            // undo
            cur.remove(cur.size()-1);
        }
    }

    private int getSum(List<Integer> input){
        int res = 0;
        for(int x: input){
            res += x;
        }
        return res;
    }

    // LC 46
    // 11.18 - 11.28 am
    /**
     * Given a collection of distinct integers,
     * return all possible permutations.
     *
     * Example:
     *
     * Input: [1,2,3]
     * Output:
     * [
     *   [1,2,3],
     *   [1,3,2],
     *   [2,1,3],
     *   [2,3,1],
     *   [3,1,2],
     *   [3,2,1]
     * ]
     *
     *
     *  IDEA) BACKTRACK (without idx)
     *
     */
    List<List<Integer>> permuteRes = new ArrayList<>();
    public List<List<Integer>> permute(int[] nums) {

        // edge
        // backtrack
        this.permuteHelper(nums, new ArrayList<>());
        return permuteRes;
    }

    private void permuteHelper(int[] nums, List<Integer> cur){
        if(nums.length == cur.size()){
            if(!this.permuteRes.contains(cur)){
                this.permuteRes.add(new ArrayList<>(cur));
            }
        }
        if(nums.length > cur.size()){
            return;
        }
        for(int i = 0; i < cur.size(); i++){
            int curVal = nums[i];
            if(!cur.contains(nums[i])){
                cur.add(curVal);
                this.permuteHelper(nums, cur);
                // undo
                cur.remove(cur.size() - 1);
            }
        }
    }

    // LC 90
    // 9.51 -10.10 AM

//    List<List<Integer>> subSet2Res = new ArrayList<>();
//    HashSet<Integer> _set = new HashSet<>();
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0){
            return res;
        }
        // backtrack
        int start_idx = 0;
        List<Integer> cur = new ArrayList<>();
        Map<Integer, Integer> numsCnt = new HashMap<>();
        Map<Integer, Integer> curValCnt = new HashMap<>();

        for(int n: nums){
            numsCnt.put(n, numsCnt.getOrDefault(n, 0) + 1);
        }

        //System.out.println("(before) res = " + res);
        this.getSubSet(start_idx, nums, cur, res, numsCnt, curValCnt);
        //System.out.println("(after) res = " + res);
        return res;
    }

    public void getSubSet(int start_idx, int[] nums, List<Integer> cur, List<List<Integer>> res, Map<Integer, Integer> numsCnt, Map<Integer, Integer> curValCnt){

        Collections.sort(cur);
        if (!res.contains(cur)){
            // NOTE !!! init new list via below
            //curValCnt = new HashMap<>(); // ?? necessary
            res.add(new ArrayList<>(cur));
        }

        if (cur.size() > nums.length){
            //curValCnt = new HashMap<>(); // ?? necessary
            return;
        }

        for (int i = start_idx; i < nums.length; i++){
            /**
             * NOTE !!!
             *
             *  for subset,
             *  we need "!cur.contains(nums[i])"
             *  -> to NOT add duplicated element
             */
            int curVal = nums[i];
            boolean shouldProceed = ( !curValCnt.containsKey(curVal)|| curValCnt.get(curVal) < numsCnt.get(curVal) );
            if ( shouldProceed ){
                cur.add(curVal);
                curValCnt.put(curVal, curValCnt.getOrDefault(curVal, 0) + 1);
                /**
                 *  NOTE !!!
                 *
                 *   at LC 78 subset, we need to use `i+1` idx
                 *   in recursive call
                 *
                 *   while at LC 39 Combination Sum,
                 *   we use `i` directly
                 *
                 *
                 *   e.g. next start_idx is ` i+1`
                 */
                this.getSubSet(i+1, nums, cur, res, numsCnt, curValCnt);
                // undo
                curValCnt.put(curVal, curValCnt.getOrDefault(curVal, 0) - 1);
                if(curValCnt.get(curVal) <= 0){
                    curValCnt.remove(curVal);
                }
                cur.remove(cur.size()-1);
            }
        }
    }

    // LC 79
    // 10.37 - 10.47 AM
    // IDEA: DFS
    public boolean exist(char[][] board, String word) {
        // edge
        if(board.length == 0 || board[0].length == 0){
            return false;
        }
        if(word == null || word.length() == 0){
            return true; // ???
        }

        int l = board.length;
        int w = board[0].length;

        Boolean[][] visited = new Boolean[l][w]; //??? init val = false
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                visited[i][j] = false;
            }
        }

        // dfs
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                if(board[i][j] == word.charAt(0)){
                    if(canFind(board, word, j, i, visited, 0)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean  canFind(char[][] board, String word, int x, int y, Boolean[][] visited, int idx){

        int l = board.length;
        int w = board[0].length;

        if(idx == word.length()){
            return true;
        }

        if(idx > word.length()){
            return false;
        }

        // NOTE !!! we validate condition before go into `for loop and recursive call`
        //char z = word.charAt(idx);
        if(x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || board[y][x] != word.charAt(idx)){
            return false;
        }

        int[][] dirs = new int[][]{ {1,0}, {-1,0}, {0,1}, {0,-1} };
//        for(int[] d: dirs){
//
//            int x_ = x + d[0];
//            int y_ = y + d[1];
//
//            return canFind
//        }

        visited[y][x] = true;

//        return canFind(board, word, x+1, y, visited, idx+1) ||
//                canFind(board, word, x-1, y, visited, idx+1) ||
//                canFind(board, word, x, y+1, visited, idx+1) ||
//                canFind(board, word, x, y-1, visited, idx+1);

        if(canFind(board, word, x+1, y, visited, idx+1) ||
                canFind(board, word, x-1, y, visited, idx+1) ||
                canFind(board, word, x, y+1, visited, idx+1) ||
                canFind(board, word, x, y-1, visited, idx+1)){
            return true;
        }

        // undo
        visited[y][x] = false;
        return false;  // NOTE !!! we return false,
    }

    // LC 212
    // 11.08 - 11.18 AM
    public List<String> findWords(char[][] board, String[] words) {

        // edge
        if (board.length == 0 || board[0].length == 0) {
            //return false;
            return null;
        }
        if (words == null || words.length == 0) {
            //return true; // ???
            return null;
        }

        int l = board.length;
        int w = board[0].length;

//        Boolean[][] visited = new Boolean[l][w]; // ??? init val = false
//        for (int i = 0; i < l; i++) {
//            for (int j = 0; j < w; j++) {
//                visited[i][j] = false;
//            }
//        }


        List<String> finalRes = new ArrayList<>();

        // dfs
        // TODO: optimize this O(N ^ 3) time complexity ???
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                for(String word: words){
                    boolean[][] visited = new boolean[l][w];
                    System.out.println(">>> word " + word);
                    if (board[i][j] == word.charAt(0)) {
                        if (canBuild(board, word, j, i, visited, 0)) {
                            if(!finalRes.contains(word)){
                                finalRes.add(word);
                            }
                        }
                    }
                }
            }
        }

        return finalRes;
    }

    private boolean canBuild(char[][] board, String word, int x, int y, boolean[][] visited, int idx) {

        int l = board.length;
        int w = board[0].length;

        if (idx == word.length()) {
            return true;
        }

        if (idx > word.length()) {
            return false;
        }

        // NOTE !!! we validate condition before go into `for loop and recursive call`
        if (x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || board[y][x] != word.charAt(idx)) {
            return false;
        }

        //int[][] dirs = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        visited[y][x] = true;

        /**
         *  NOTE !!!
         *
         *   1) we use below structure
         *      if ( recursive_call_1 or recursive_call_2 ..) {
         *              return true
         *      }
         *
         *   2) since we need to `undo` visited record
         *      so after above logic, we modify visited[y][x] back to false (e.g. non-visited)
         *
         *   3) RETURN `false` at the final of recursive call
         *      -> since it can reach this point,
         *      -> means NOT POSSIBLE to find a solution
         *      -> return false
         */

        if (canBuild(board, word, x + 1, y, visited, idx + 1) ||
                canBuild(board, word, x - 1, y, visited, idx + 1) ||
                canBuild(board, word, x, y + 1, visited, idx + 1) ||
                canBuild(board, word, x, y - 1, visited, idx + 1)) {
            return true;
        }

        // undo
        visited[y][x] = false;

        /**
         * 3) RETURN `false` at the final of recursive call
         */
        return false;
    }

    // LC 131
    // 11.30 - 11.55 am
    /**
     *  IDEA 1) BACKTRACK
     *
     *  -> split all possible sub str
     *     -> loop over idx
     *       ->
     *
     *  -> check if each sub str is palindrome
     *     -> if true, append to result
     *
     *
     *     aab
     *
     *     -> aab
     *     -> a, ab
     *     -> aa, b
     *
     *  IDEA 2) 2 pointers
     *
     *      aab  -> aab
     *      i
     *
     *      aab   -> a, ab
     *       i
     *
     *      aab   -> aa, b
     *        i
     *
     *
     *     aabb
     *
     *            a
     *        a        ab     abb
     *
     *     b  bb      b
     *
     *
     */
    public List<List<String>> partition(String s) {
        return null;

    }



}
