package dev;

import LeetCodeJava.BFS.NetworkDelayTime;
import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.Tree.RedundantConnection;
//import LeetCodeJava.DataStructure.Node;

import javax.print.DocFlavor;
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
  public int[] maxSlidingWindow_1(int[] nums, int k) {

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
    /**
     *
     *   least recently used key.
     *
     *  IDEA 1) QUEUE ???
     *   (FIFO)
     *
     *   map_cnt : { val: used_cnt } // record element, and count it has been used
     *   map : { val : val } // k-v, record key and its value
     *   element_cnt : record total elements count
     *   PQ : (small Queue) // record `cnt`, so if `element size > capacity`
     *                      // we can pop the `least used time` and remove the corresponding element
     *   PQ<Inter[]>, ([val: cnt]), sort on 2nd element
     *
     */
    class LRUCache {

        public LRUCache(int capacity) {

        }

        public int get(int key) {
            return 0;
        }

        public void put(int key, int value) {

        }
    }


//    class LRUCache {
//
//        // attr
//        int capacity;
//        //Stack<Integer> leastUsed;
//        // dqeueue : [ key1, key2, .... ]
//        Deque<Integer> leastUsed;
//        Map<Integer, Integer> map;
//
//        public LRUCache(int capacity) {
//            this.capacity = capacity;
//            //this.leastUsed = new Stack<>();
//            this.leastUsed = new LinkedList<>(); // ???
//            this.map = new HashMap<>();
//        }
//
//        public int get(int key) {
//            if(this.map.isEmpty()){
//                return -1;
//            }
//            if(!this.map.containsKey(key)){
//                return -1;
//            }
//
//            int toAdd = -1;
//            // update `least usage`
//            for(int i = 0; i < this.leastUsed.size(); i++){
//                Integer tmp = this.leastUsed.pop();
//                if(tmp == key){
//                    //this.leastUsed.add(tmp);
//                    // NOT change `ordering` of other elements in stack
//                    //break;
//                    toAdd = tmp;
//                    continue;
//                }
//                this.leastUsed.add(tmp);
//            }
//
//            this.leastUsed.add(toAdd);
//
//            return this.map.get(key);
//        }
//
//        public void put(int key, int value) {
//            if(this.leastUsed.size() >= this.capacity){
//                // need to remove element
//                this.map.remove(key);
//                // update `least usage`
//                for(int i = 0; i < this.leastUsed.size(); i++){
//                    Integer tmp = this.leastUsed.pop();
//                    if(tmp == key){
//                        break;
//                    }
//                    this.leastUsed.add(tmp);
//                }
//            }else{
//
//            }
//            // add `least usage`
//            this.leastUsed.add(key); // ???
//            // insert to map
//            this.map.put(key, value);
//        }
//    }

    // LC 25
    // 9.16 - 9.26 AM
    /**
     *   IDEA 1) Linked list op
     *     -> find head_len / k,
     *     -> then for each sub array (len = head_len / k), reverse it
     *     -> keep remaining the same
     *     -> return result
     *
     */
     public ListNode reverseKGroup(ListNode head, int k) {

         // edge
         if(head == null || head.next == null){
             return head; // ??
         }

         // get all node val as list
         List<Integer> nodeValList = new ArrayList<>();
         while(head != null){
             nodeValList.add(head.val);
             head = head.next;
         }

         // get len
         int len = nodeValList.size(); // to fix
         int subStrLen = len / k;

         // iteration reverse
         ListNode dummy = new ListNode();
         ListNode dummy2 = dummy;

         for(int i = 0; i < len; i += subStrLen){
             List<Integer> subList =  null; // ??nodeValList.copyOf(1,2);
             ListNode reversedNode = reverseNode(subList, subStrLen);
            // reversedNode = reversedNode.e
             dummy.next = reversedNode;
             dummy = reversedNode;
         }

        return dummy2.next; // ??
    }

    public ListNode reverseNode(List<Integer> input, int len){

         ListNode _dummy = new ListNode();
        ListNode _dummy2 = _dummy;
         //for(int i = 0)
        List<Integer> subInPut = input.subList(0, len);
        // reverse
        Collections.sort(subInPut, Collections.reverseOrder()); // ?
        for(Integer x: subInPut){
            ListNode _newNode = new ListNode(x);
            _dummy.next = _newNode;
            _dummy = _newNode;
        }

         return _dummy2.next;
    }




//    public ListNode reverseKGroup(ListNode head, int k) {
//        // edge
//        if(head == null || head.next == null){
//            return null;
//        }
//        int len = 0;
//        ListNode head2 = head;
//        while(head2 != null){
//            len += 1;
//            head2 = head2.next;
//        }
//        if(k > len){
//            return head;
//        }
//
//        int subNodeLen = len / k;
//        if(subNodeLen <= 0){
//            return head;
//        }
//        // ??? optimize below
//        List<ListNode> nodeList = new ArrayList<>();
//        ListNode head3 = head;
//        int j = 0;
//        ListNode tmp = null;
//        while(head3 != null &&  j < k){
//            tmp.next = head3;
//            tmp = tmp.next;
//
//            j += 1;
//            head3 = head3.next;
//        }
//
//
//
//        return null;
//    }
//
//    private ListNode reverseNode(ListNode node){
//        if(node == null || node.next == null){
//            return null;
//        }
//        ListNode _prev = null;
//        while(node != null){
//            ListNode _next = node.next;
//            _prev.next = node;
//            _prev = node;
//            node = _next;
//        }
//
//        return _prev.next;
//    }

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

    // LC 17
    // 12.09 - 12.20 pm
    /**
     * Example:
     *
     * Input: "23"
     * Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
     *
     *
     *
     * IDEA 1) BACKTRACK
     *
     *
     *
     */
    List<String> letterRes = new ArrayList<>();
    public List<String> letterCombinations(String digits) {

        HashMap<String,String> letters = new HashMap<>();
        letters.put("3", "def");
        letters.put("4", "ghi");
        letters.put("5", "jkl");
        letters.put("6", "mno");
        letters.put("7", "pqrs");
        letters.put("8", "tuv");
        letters.put("9", "wxyz");

        // edge
        if(digits == null || digits.length() == 0){
            return null;
        }
        if(digits.length() == 1){
            String val = letters.get(String.valueOf(digits.charAt(0)));
            for(String x: val.split("")){
                letterRes.add(x);
            }
            return letterRes;
        }

        // backtrack
        buildLetter(digits, letters, new ArrayList<>(), 0);
        return letterRes;
    }

    private void buildLetter(String digits, HashMap<String,String> letters, List<String> cur, int idx){

        if(cur.size() == digits.length()){
            if(!letterRes.contains(cur)){
                //letterRes.add(cur); // ???
            }
        }

        if(cur.size() > digits.length()){
            return;
        }

        // TODO: validate if `correct cur`

        for(int i = 0; i < digits.length(); i++){
            // d is input, e.g. 23
            String d = String.valueOf(digits.charAt(i));

            System.out.println(">>> d = " + d + ", i = " + i);

            // x is `phone number alphabet val got via key = d
            // e.g. abc, del (from {2: abc, 3: def, ...}
            for(String x: letters.get(d).split("")){
                if(!cur.contains(x)){
                    cur.add(x);
                    buildLetter(digits, letters, cur, idx+1); // ????

                    // undo
                    cur.remove(cur.size() - 1);
                    // idx -= 1; // ???
                }
            }
        }
    }

    // LC 51
    // 9.22 - 9.32 am
    /**
     *  The n-queens puzzle is the problem of placing
     *  n queens on an n x n chessboard such that no two
     *  queens attack each other.
     *
     *
     *  -> Given an integer n, return all distinct solutions to the n-queens puzzle.
     *     You may return the answer in any order.
     *
     *
     *  -> how to define is `queens can attach each other` ?
     *
     *  IDEA 1) BACKTRACK
     *
     *
     *
     */
    public List<List<String>> solveNQueens(int n) {

        List<List<String>> res = new ArrayList<>();

        // edge
        if(n == 0){
            return null;
        }
        if(n == 1){
            List<String> cur = new ArrayList<>();
            cur.add("Q");
            res.add(cur);
            return res;
        }

        // backtrack

        return null;
    }

    // LC 208
    // 9.53 - 10.17
    class MyTreeNode{
        // attr
        //String val;
        //MyTreeNode child;
        Map<MyTreeNode, List<MyTreeNode>> children; // ???
        Boolean isEnd;

        // constructor
        public MyTreeNode(){
           // this.val = val;
            this.children = new HashMap<>(); // new MyTreeNode(null); // ??
            this.isEnd = true;
        }
    }
    class Trie {

        // attr
        MyTreeNode node;
       // Boolean isEnd;
        public Trie(Integer val) {
            this.node = new MyTreeNode();
           // this.children = new HashMap<>();
           // this.isEnd = true;
        }

        public void insert(String word) {
            // get current node
            MyTreeNode node = this.node;
            //Trie trie = this.node;
            if(word == null){
                return;
            }
            for(String x: word.split("")){
                MyTreeNode nextNode = new MyTreeNode();
                if(!node.children.containsKey(x)){
                    List<MyTreeNode> list = new ArrayList<>();
                    list.add(nextNode);
                    node.children.put(node, list);
                    node = nextNode; // ??? move to next node
                }
            }
            //this.node.
            this.node.isEnd = true;
        }

        public boolean search(String word) {
            return false;
        }

        public boolean startsWith(String prefix) {
            return false;
        }

        // LC 211
        // 10.35 - 10.45 AM
        class MyTreeNode2{
            // attr
            Map<MyTreeNode2, List<MyTreeNode2>> children;
            boolean isEnd;

            public MyTreeNode2(){
                this.children = new HashMap<>();
                this.isEnd = false;
            }
        }
        class Trie2{
            MyTreeNode2 node;

            public Trie2(){
                this.node = new MyTreeNode2();
            }

        }
        class WordDictionary {

            Trie2 trie2;

            public WordDictionary() {
                this.trie2 = new Trie2();
            }

            public void addWord(String word) {
                if(word == null || word.length() == 0){
                    return;
                }
                // get current node
                Trie2 trie = this.trie2;
                MyTreeNode2 node = trie.node;
                for(String x: word.split("")){
                    MyTreeNode2 newNode = new MyTreeNode2();
                    if(!node.children.containsKey(x)){
                      List<MyTreeNode2> list = new ArrayList<>();
                      node.children.put(newNode, list);
                      node = newNode;
                    }
                }
                node.isEnd = true;
            }

            public boolean search(String word) {
                if(word == null || word.length() == 0){
                    return false; // ??
                }
                // get current node
                Trie2 trie = this.trie2;
                MyTreeNode2 node = trie.node;

//                // case 1) not "."
//                for(String x: word.split("")){
//                    MyTreeNode2 newNode = new MyTreeNode2();
//                    if(!x.equals(".") && !node.children.containsKey(x)){
//                        return false;
//                    }
//                }
//
//                // case 2) "." (can map to ANY character)
                return false;
            }

            private boolean searchHelper(String word,  MyTreeNode2 node, int idx, String cur){

                if (idx == word.length()){
                    if(node.isEnd){
                        return true;
                    }
                    return false;
                }
                if(idx > word.length()){
                    return false;
                }

                for(int i = 0; i < word.length(); i++){
                    MyTreeNode2 newNode = new MyTreeNode2();
                    String curChar = String.valueOf(word.charAt(i));
                    // case 1) not "."
                    if(!curChar.equals(".")){
                        if( !node.children.containsKey(curChar) ){
                            return false;
                        }
                        // go to next loop
                        this.searchHelper(word, node, idx+1, cur);

                     // case 2) "." (can map to ANY character)
                    }else{
                        // search all children
                        for(List<MyTreeNode2> n: node.children.values()){
                            // ????
                            for(MyTreeNode2 x: n){
                                this.searchHelper(word, node, idx+1, cur);
                            }
                        }
                    }
                }

                return true; // ???
            }

        }
    }

    // LC 200
    // 3.57 PM - 4.07 PM
    /**
     * IDEA : DFS
     *
     */
    int islandCnt = 0;
    public int numIslands(char[][] grid) {

        // edge
        if(grid.length == 0 || grid[0].length == 0){
            return 0;
        }

        int l = 0;
        int w = 0;

        // init visited
       // boolean[][] visited = new boolean[l][w]; // ???

        for(int i = 0; i < l; i++){
            for (int j = 0; j < w; j++){
                if(grid[i][j] == '1'){
                    islandCnt += 1;
                    findIslands(grid, j, i);
                }
            }
        }

        return islandCnt;
    }

    private void findIslands(char[][] grid, int x, int y){

        int l = 0;
        int w = 0;

        if(x < 0 || x >= w ||  y < 0 || y >= l || grid[y][x] != '1'){
           // return false;
            return;
        }

        //visited[y][x] = '#';
        grid[y][x] = '#';

        int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };
        for (int[] d: dirs){
            int x_ = x + d[0];
            int y_ = y + d[1];
            findIslands(grid, x_, y_);
        }

//        if(findIslands(grid, x+1, y) ||
//           findIslands(grid, x-1, y) ||
//           findIslands(grid, x, y-1) ||
//           findIslands(grid, x, y+1)){
//            return true;
//        }

        // undo
       // visited[y][x] = false;

        //return true; // ???
    }

    // LC 695
    // 4.25 - 4.35 PM
    /**
     *  IDEA: DFS
     */
//    int maxArea = 0;
//    public int maxAreaOfIsland(int[][] grid) {
//
//        // edge
//        if(grid.length == 0 || grid[0].length == 0){
//            return 0;
//        }
//
//        if(grid.length == 1 && grid[0].length == 1){
//            return grid[0][0];
//        }
//
//        int l = grid.length;
//        int w = grid[0].length;
//
//        // x: i, y: j
//        for(int i = 0; i < w; i++){
//            for (int j = 0; j < l; j++){
//                if(grid[j][i] == 1){
//                    findMaxArea(grid, i, j, 0);
//                }
//            }
//        }
//
//        return maxArea;
//    }
//
//    private void findMaxArea(int[][] grid, int x, int y, int curArea){
//
//        int l = grid.length;
//        int w = grid[0].length;
//
//        if(x < 0 || x >= w || y < 0 || y > l || grid[y][x] != 1){
//            return;
//        }
//
//        maxArea = Math.max(maxArea, curArea+1);
//
//        grid[y][x] = -1;
//
//        int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };
//        for (int[] d: dirs){
//            int x_ = x + d[0];
//            int y_ = y + d[1];
//            findMaxArea(grid, x_, y_, curArea);
//        }
//
//    }
//
//

    // LC 133
    /*
// Definition for a Node.
class Node {
    public int val;
    public List<Node> neighbors;
    public Node() {
        val = 0;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
}
*/

    // LC 133
    // 5.16 - 5.26 PM
    // IDEA: DFS

    class Node2 {

        public int val;
        public List<Node2> neighbors;

        public Node2() {
            val = 0;
            neighbors = new ArrayList<Node2>();
        }
        public Node2(int _val) {
            val = _val;
            neighbors = new ArrayList<Node2>();
        }
        public Node2(int _val, ArrayList<Node2> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    //Node2 copiedNode = null;
    public Node2 cloneGraph(Node2 node) {
        // edge
        if(node == null){
            return null;
        }
        if(node.neighbors == null || node.neighbors.isEmpty()){
            return node;
        }

        //dfs
        //copiedNode.val = node.val;
        Node2 copiedNode = new Node2(node.val);

        cloneHelper(node, copiedNode); // ???

        return copiedNode;
    }

    private Node2 cloneHelper(Node2 node, Node2 copiedNode){
        // edge
        if(node == null){
            return null;
        }
        if(node.neighbors == null || node.neighbors.isEmpty()){
            return node;
        }

        for(Node2 n: node.neighbors){
            List<Node2> curNeighbors = copiedNode.neighbors;
            if(!curNeighbors.contains(n)){
                curNeighbors.add(n);
            }

            copiedNode.neighbors = curNeighbors;
            //Node2 nextNode = n;

            cloneHelper(node, n); // ???
        }

        return copiedNode;
    }

    // LC 286
    // 5.46 - 6.00 PM
    /**
     *
     * -> Fill each empty room with the distance to
     *    its nearest gate. If it is impossible to reach a gate,
     *    it should be filled with INF.
     *
     *
     *  IDEA 1) DFS
     */
    public class RoomCoor{
        int init_x;
        int init_y;
        int x;
        int y;
        int dist;

        public RoomCoor(int x, int y){
            this.init_x = x;
            this.init_y = y;
            this.x = init_x;
            this.y = init_y;
            this.dist = 0;
        }

        public int getDist() {
            return dist;
        }

        public int getInit_x() {
            return init_x;
        }

        public int getInit_y() {
            return init_y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setDist(int dist) {
            this.dist = dist;
        }

        public void setInit_x(int init_x) {
            this.init_x = init_x;
        }

        public void setInit_y(int init_y) {
            this.init_y = init_y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
    public void wallsAndGates(int[][] rooms) {

        // edge
        if(rooms.length == 0 || rooms[0].length == 0){
            return;
        }

        if(rooms.length == 1 || rooms[0].length == 1){
            return;
        }

        int l = rooms.length;
        int w = rooms[0].length;

        // bfs ????
        Queue<RoomCoor> q = new LinkedList<>();

        // collect all `INF`
        for(int i = 0; i < w; i++){
            for(int j = 0; j < l; j++){
                // Integer.MAX_VALUE ???
                if(rooms[j][i] == Integer.MAX_VALUE){
                    //List<RoomCoor> tmp = new ArrayList<>();
//                    tmp.add(i);
//                    tmp.add(j);
                    RoomCoor roomCoor = new RoomCoor(i, j);
                    //tmp.add(roomCoor);
                    q.add(roomCoor);
                }
            }
        }

        // bfs
        while(!q.isEmpty()){
            RoomCoor roomCoor = q.poll();
            int[][] moves = new int[][] { {1,0}, {-1,0}, {0,1}, {0,-1} };
            int x = roomCoor.x;
            int y = roomCoor.y;


            for(int[] m: moves){
                int x_ = x + m[0];
                int y_ = y + m[1];
//                if(x_ >= 0 && x_ < w && y_  0 || y_ >= l || rooms[y_][x_] == 1){
//
//                }

            }
        }

        return;
    }

  // LC 994
  // 4.36 - 4.46 pm
  /**
   * Return the minimum number of minutes that must
   * elapse until no cell has a fresh orange.
   * If this is impossible, return -1.
   *
   *    0 representing an empty cell,
   *    1 representing a fresh orange, or
   *    2 representing a rotten orange.
   *
   *
   *
   *   2 1 1
   *   0 1 1
   *   1 0 1
   *
   *
   *   IDEA : BFS
   *
   */
  public int orangesRotting(int[][] grid) {

      // edge
      if(grid.length == 0 || grid[0].length == 0){
          return 0;
      }

      if(grid.length == 1 && grid[0].length == 1){
          if(grid[0][0] == 2){
              return -1;
          }
          return 0; // ??
      }

      int l = grid.length;
      int w = grid[0].length;

      int freshCnt = 0;
      int rottenCnt = 0;

      List<List<Integer>> rottenList = new ArrayList<>();

      // get cnt of `fresh` orange
      for(int i = 0; i < l; i++){
          for(int j = 0; j < w; j++){
              if(grid[i][j] == 1){
                  freshCnt += 1;
              }else if (grid[i][j] == 2){
                  rottenCnt += 1;
                  List<Integer> tmp = new ArrayList<>();
                  tmp.add(j);
                  tmp.add(i);
                  rottenList.add(tmp);
              }
          }
      }

      if(freshCnt == 0){
          return 0;
      }

      int minutes = 0;

      Queue<List<Integer>> q = new LinkedList<>();
      for(List<Integer> x: rottenList){
          q.add(x);
      }

      while(!q.isEmpty() || freshCnt <= 0){
          List<Integer> cur = q.poll();
          int x = cur.get(0);
          int y = cur.get(1);

          //grid[y][x] = 2; // make as `rotten`

          int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

          for(int[] d: dirs){
              int x_ = x + d[0];
              int y_ = y + d[1];
              if(x_ < 0 || x_ >= w || y < 0 || y_ >= l || grid[y_][x_] == 0){
                  continue;
              }
              List<Integer> cur2 = new ArrayList<>();
              cur2.add(x_);
              cur2.add(y_);

              q.add(cur2);

              if(grid[y_][x_] == 1){
                  grid[y_][x_] = 2; // make as `rotten`
                  freshCnt -= 1;
              }
          }

          minutes += 1;
      }

      return minutes;
    }

    // LC 417
    // 5.15 - 5.25 PM
    /**
     *
     *  IDEA: BFS ???
     *
     * Return a 2D list of grid coordinates result
     * where result[i] = [ri, ci]
     * denotes that rain water can flow
     * from cell (ri, ci) to both the Pacific and Atlantic oceans.
     *
     *
     *  -> start from `pacific`,  collect all coordinates that can be reached
     *  -> start from `atlantic`,  collect all coordinates that can be reached
     *
     *  -> get the overlap of above, and return as res
     */
    public List<List<Integer>> pacificAtlantic(int[][] heights) {

      return null;
    }

    // LC 130
    // 5.39 - 5.49 pm
    /**
     *  IDE: BFS
     *
     *
     *
     */
    public void solve(char[][] board) {
        // edge
        if(board.length == 0 || board[0].length == 0){
            return;
        }

        int l = board.length;
        int w = board[0].length;

        // get '0' list
        List<Integer[]> zeroList = new ArrayList<>();
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                if(board[i][j] == '0'){
                    Integer[] tmp = new Integer[2];
                    tmp[0] = j;
                    tmp[1] = i;
                    zeroList.add(tmp);
                }
            }
        }


        int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

        // bfs
        Queue<Integer[]> q = new LinkedList<>();
        for(Integer[] x: zeroList){
            q.add(x);
        }

        while(!q.isEmpty()){
            Integer[] tmp = q.poll();
            int x = tmp[0];
            int y = tmp[1];
            for (int[] d: dirs){
                int x_ = x + d[0];
                int y_ = y + d[1];
                if (x_ < 0 || x_ > w || y_ < 0 || y_ > l || board[y_][x_] == 'X'){
                    continue; // ??
                }

                board[y_][x_] = 'O';

                Integer[] tmp2 = new Integer[2];
                tmp2[0] = x_;
                tmp2[1] = y_;
                q.add(tmp2);
            }
        }

        return;
    }

    // LC 207
    // 3.47 pm - 4.57 pm
    /**
     *
     *   prerequisites[i] = [ai, bi]
     *   ->  take course bi first then ai.
     *   e.g.
     *   [ai, bi]
     *   -> bi first, ai
     *
     *   -> Return true if you can finish all courses.
     *      Otherwise, return false.
     *
     *
     *   IDEA 1) TOPOLOGICAL SORT
     *   -> find a `global sorting` based on prerequisites
     *   -> ; if cant' build one, return false directly
     *   -> else return true
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {

        // edge
        if(numCourses == 0){
            return true;
        }

        if(numCourses == 1 && prerequisites.length == 1){
            return true;
        }

        if(TopologicalSort(numCourses, prerequisites) == null){
            return false;
        }

        return true;
    }

    public List<Integer> TopologicalSort(int numNodes, int[][] edges) {

        // Step 1: Build the graph and calculate in-degrees
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] inDegree = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] edge : edges) {
            int cur = edge[0];
            int pre = edge[1];
            graph.get(cur).add(pre);
           // inDegree[cur] += 1;
            inDegree[pre] += 1;
        }

        // Step 2: Initialize a queue with nodes that have in-degree 0
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < numNodes; i++) {
            /**
             * NOTE !!!
             *
             *  we add ALL nodes with degree = 0 to queue at init step
             */
            if (inDegree[i] == 0) {
                q.add(i);
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();

        while(!q.isEmpty()){

            int cur = q.poll();
            topologicalOrder.add(cur);

            for(int pre: graph.get(cur)){
                inDegree[pre] -= 1;

                if(inDegree[pre] == 0){
                    q.add(pre);
                }
            }
        }

        // NOTE !!! below
        if(topologicalOrder.size() != numNodes){
            return null;
        }

        // reverse
        Collections.reverse(topologicalOrder);
        return topologicalOrder;
    }

    // LC 210
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // edge
        if(numCourses == 0){
            return null;
        }
        if(numCourses == 1){
            int[] res = new int[1];
            res[0] = 0;
            return res;  // ???
        }

        List<Integer> topoSortRes = TopologicalSort_(numCourses, prerequisites);

        if(topoSortRes == null || topoSortRes.isEmpty()){
            return new int[]{};
        }

        //int[] x = new int[numCourses]{Arrays.asList(topoSortRes.toArray())};
        int[] res = new int[numCourses];
        for(int j = 0; j < topoSortRes.size(); j++){
            res[j] = topoSortRes.get(j);
        }

        return res;
    }

    public List<Integer> TopologicalSort_(int numNodes, int[][] edges) {
        // Step 1: Build the graph and calculate in-degrees
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] inDegree = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] edge : edges) {
            /**
             *  NOTE !!!
             *
             *  given [ai, bi],
             *  -> means NEED take bi first, then can take ai
             *
             *
             *  prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.
             */
            int cur = edge[0];
            int prev = edge[1];
            graph.get(cur).add(prev);
            // Update in-degree for the next course
            /**
             * NOTE !!!
             *
             *  update `prev` course's degree,
             *  since every time when meet a prev-
             *  means before take that cur course, we need to take `prev` course first
             *  so its (next course) degree increase by 1
             */
            inDegree[prev] += 1;
        }

        // Step 2: Initialize a queue with nodes that have in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numNodes; i++) {
            /**
             * NOTE !!!
             *
             *  we add ALL nodes with degree = 0 to queue at init step
             */
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();

        // Step 3: Process the nodes in topological order
        while (!queue.isEmpty()) {
            /**
             * NOTE !!!
             *
             *  ONLY "degree = 0"  nodes CAN be added to queue
             *
             *  -> so we can add whatever node from queue to final result (topologicalOrder)
             */
            int current = queue.poll();
            topologicalOrder.add(current);

            for (int neighbor : graph.get(current)) {
                inDegree[neighbor] -= 1;
                /**
                 * NOTE !!!
                 *
                 *  if a node "degree = 0"  means this node can be ACCESSED now,
                 *
                 *  -> so we need to add it to the queue
                 *    (for adding to topologicalOrder in the following while loop iteration)
                 */
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }


        /**
         * If topologicalOrder does not contain all nodes,
         * there was a cycle in the graph
         *
         *  NOTE !!!
         *
         *   we use `topologicalOrder.size() != numNodes`
         *   to check if above happened
         */
        if (topologicalOrder.size() != numNodes) {
            //throw new IllegalArgumentException("The graph has a cycle, so topological sort is not possible.");
            return null;
        }

        /** NOTE !!! reverse ordering */
        Collections.reverse(topologicalOrder);
        return topologicalOrder;
    }

    // LC 261
    // 4.50 - 5.05 pm
    /**
     * Given n nodes labeled from 0 to n-1 and a list
     * of undirected edges (each edge is a pair of nodes),
     * write a function to check whether these edges make up a valid tree.
     *
     *
     * IDEA: UNION FIND
     *
     *
     *
     *  exp 1)
     *
     *  Input: n = 5, and edges = [[0,1], [0,2], [0,3], [1,4]]
     *
     *   3 - 0 - 1 -4
     *      |
     *      2
     *
     *   -> true
     *
     *
     *  exp 2)
     *
     *   n = 5, and edges = [[0,1], [1,2], [2,3], [1,3], [1,4]]
     *
     *           4
     *           |
     *       0 - 1 - 2 -3
     *           | -----|
     *
     *  -> false
     *
     *
     *
     */
    public boolean validTree(int n, int[][] edges) {
        // build UF
        UF uf = new UF(n, edges);
        // union
        for(int[] e: edges){
            int x = e[0];
            int y = e[1];
            uf.union(x, y);
        }

        // check -> if `parent` size == 1 -> NOT `cycle` ???
        //int parentCnt = 0;
        HashSet<Integer> set = new HashSet<>();
        for(int i = 0; i < n; i++){
            int parent = uf.findParent(i);
            set.add(parent);
        }
        return set.size() == 1; // ????
    }

    public class UF{
        // attr
        int n;
        int[][] edges;
        int[] parents;

        // constructor
        public UF(int n, int[][] edges){
            this.n = n;
            this.edges = edges; //new int[n][2]; // ???
            // init parents
            parents = new int[n];
            for(int i = 0; i < n; i++){
                parents[i] = i; // init node's parent as itself
            }
        }

        // method
        public boolean union(int x, int y){
//            if(x == y){
//                return true;
//            }
//            int x_ = this.findParent(x);
//            int y_ = this.findParent(y);
//            if(x_ == y_){
//                return;
//            }
//            this.parents[x_] = y_; // ???
            return false; // ????
        }

        public int findParent(int x){
            if(x == this.parents[x]){
                return x;
            }
            //int x_ = this.findParent(x);
            return this.findParent(x); // ???
        }

        private boolean isConnected(int x, int y){
            if(x == y){
                return true;
            }
            int x_ = this.findParent(x);
            int y_ = this.findParent(y);
            if(x_ == y_){
                return true;
            }
            return false;
        }
    }

    // LC 323
    /**
     * 323. Number of Connected Components in an Undirected Graph
     * Given n nodes labeled from 0 to n - 1 and a
     * list of undirected edges (each edge is a pair of nodes),
     * write a function to find the number of connected components in an undirected graph.
     *
     * Example 1:
     *
     * Input: n = 5 and edges = [[0, 1], [1, 2], [3, 4]]
     *
     *      0          3
     *      |          |
     *      1 --- 2    4
     *
     * Output: 2
     *
     * Example 2:
     *
     * Input: n = 5 and edges = [[0, 1], [1, 2], [2, 3], [3, 4]]
     *
     *      0           4
     *      |           |
     *      1 --- 2 --- 3
     *
     * Output:  1
     *
     * Note:
     * You can assume that no duplicate edges will appear in edges.
     * Since all edges are undirected, [0, 1] is the same as [1, 0]
     * and thus will not appear together in edges.
     *
     *
     *
     *  IDEA: UNION FIND -> check conn count
     *
     */
//    public int countComponents(int n, int[][] edges) {
//        // edge
////        // can't form a valis
////        if(n != edges.length - 1){
////            return 0;
////        }
//        myUF2 myUF2 = new myUF2(n, edges);
//        for(int[] e: edges){
//            int x = e[0];
//            int y = e[1];
//            // cyclic graph
//            if(!myUF2.union(x, y)){
//                return 0;
//            };
//
//           // myUF2.union(x, y);
//        }
//
//        return myUF2.clusterCnt;
//    }

//    public class myUF2{
//        // attr
//        int n;
//        int[][] edges;
//        int[] parents;
//        int clusterCnt;
//
//        public myUF2(int n, int[][] edges){
//            this.n = n;
//            this.edges = edges;
//            this.parents = new int[n];
//            for(int i = 0; i < n; i++){
//                this.parents[i] = i;
//            }
//            this.clusterCnt = n;
//        }
//
//        public boolean union(int x, int y){
//            if(x == y){
//                return true;
//            }
//            int x_ = this.findParent(x);
//            int y_ = this.findParent(y);
//            // NOTE !!! below
//            if(x_ == y_){
//                return false; // will cause CYCLE graph
//            }
//            this.parents[x_] = y_;
//            this.clusterCnt -= 1;
//            return true;
//        }
//
//        public int findParent(int x){
//            if(this.parents[x] != x){
//                this.parents[x] = this.findParent(x);
//            }
//            //return this.findParent(x);
//            return this.parents[x];
//        }
//
//        public int getClusterCnt(int x){
//            return this.clusterCnt;
//        }
//    }
//
//
//    public int countComponents(int n, int[][] edges) {
//        myUF2 myUF2 = new myUF2(n, edges);
//        for (int[] e : edges) {
//            int x = e[0];
//            int y = e[1];
//            // If union returns false, it means we encountered a cycle
//            if (!myUF2.union(x, y)) {
//                // If you want to return 0 when a cycle is detected, keep this line
//                // But if you only want the number of components, you can remove it
//                return 0; // Optionally remove if you don't want cycle detection
//            }
//        }
//
//      //  return myUF2.getClusterCnt(); // Return the final count of clusters
//    }
//
//    public class myUF2_ {
//        // Attributes
//        int n;
//        int[] parents;
//        //int[] rank;
//        int clusterCnt;
//
//        public myUF2_(int n, int[][] edges) {
//            this.n = n;
//            this.parents = new int[n];
//            //this.rank = new int[n];  // Union by rank to improve efficiency
//            for (int i = 0; i < n; i++) {
//                this.parents[i] = i;
//                //this.rank[i] = 1; // Initialize rank
//            }
//            this.clusterCnt = n;
//        }
//
//        public boolean union(int x, int y) {
//            int xRoot = findParent(x);
//            int yRoot = findParent(y);
//
//            if (xRoot == yRoot) {
//                return false; // Cycle detected if both have the same root
//            }
//
//            // (optional) Union by rank: attach the smaller tree under the larger one
////            if (rank[xRoot] > rank[yRoot]) {
////                parents[yRoot] = xRoot;
////            } else if (rank[xRoot] < rank[yRoot]) {
////                parents[xRoot] = yRoot;
////            } else {
////                parents[yRoot] = xRoot;
////                rank[xRoot]++; // Increment rank if both roots are of the same rank
////            }
//
//            clusterCnt -= 1; // Decrease cluster count when two components are merged
//            return true;
//        }
//
//        public int findParent(int x) {
//            if (parents[x] != x) {
//                // Path compression: directly link nodes to their root
//                parents[x] = findParent(parents[x]);
//            }
//            return parents[x];
//        }
//
//        public int getClusterCnt() {
//            return clusterCnt;
//        }
//    }

    // LC 684
    // 10.48 - 11.00 am
    /**
     *
     *   -> Return an edge that can be removed so that the resulting graph
     *     is a tree of N nodes. If there are multiple answers,
     *     return the answer that occurs last in the given 2D-array.
     *     The answer edge [u, v] should be in the same format, with u < v.
     *
     *
     *
     *   IDEA 1) remove each `edge` and check if it becomes `NOT CYCLIC` ???
     *
     */
    // 11.45 - 12.00 PM
    // IDEA: UNION FIND -> return directly if meet a `cycle`
//    public int[] findRedundantConnection(int[][] edges) {
//
//        // Initialize union-find data structure
////        RedundantConnection.MyUF3 uf = new RedundantConnection.MyUF3(edges);
////
////        // Union operation for each edge
////        for (int[] e : edges) {
////            int x = e[0];
////            int y = e[1];
////            // If they are already connected, we found the redundant edge
////            if (!uf.union(x, y)) {
////                return e; // The redundant edge is the one that causes the cycle
////            }
////        }
////
////        return null; // No redundant edge found
//    }

    public class MyUF3 {
        // Union-find data structure
        int[] parents;
        //int[] size;
        int n;

        // Constructor to initialize the union-find data structure
        public MyUF3(int[][] edges) {
            HashSet<Integer> set = new HashSet<>();
            for (int[] x : edges) {
                set.add(x[0]);
                set.add(x[1]);
            }
            this.n = set.size();

            // Initialize parent and size arrays
            this.parents = new int[n + 1]; // Using 1-based indexing
           // this.size = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                this.parents[i] = i;
                //this.size[i] = 1;
            }
        }

        // Find the root of the set containing 'x' with path compression
        public int getParent(int x) {
            /**
             * NOTE !!!
             *
             *  we use `!=` logic below to simplify code
             */
            if (x != this.parents[x]) {
                // Path compression: recursively find the parent and update the current node's
                // parent
                /**
                 *  NOTE !!!
                 *
                 *  we should update parent as `getParent(this.parents[x])`,
                 *  e.g. -> use `this.parents[x]` as parameter, send into getParent method,
                 *       -> then assign result to this.parents[x]
                 *
                 */
                this.parents[x] = getParent(this.parents[x]);
            }
            return this.parents[x];
        }

        // Union the sets containing x and y, return false if they are already connected
        public boolean union(int x, int y) {
            int rootX = getParent(x);
            int rootY = getParent(y);

            // If they are already in the same set, a cycle is detected
            if (rootX == rootY) {
                return false;
            }

            // Union by size: attach the smaller tree to the root of the larger tree
//            if (size[rootX] < size[rootY]) {
//                parents[rootX] = rootY;
//                size[rootY] += size[rootX];
//            } else {
//                parents[rootY] = rootX;
//                size[rootX] += size[rootY];
//            }

            parents[rootX] = rootY;
            return true;
        }
    }


//    public int[] findRedundantConnection(int[][] edges) {
//
//        // get n
//        HashSet<Integer> set = new HashSet<>();
//        for(int[] x: edges){
//            set.add(x[0]);
//            set.add(x[1]);
//        }
//        // edge
//        if(edges.length == set.size() - 1){
//            return null; // already NOT a cyclic graph
//        }
//        if(edges == null || edges.length == 0){
//            return null;
//        }
//
//        // union find
//        MyUF3 uf = new MyUF3(edges);
//        // union
//        for(int[] e: edges){
//            int x = e[0];
//            int y = e[1];
//            if(!uf.union(x, y)){
//                int[] res = new int[2];
//                res[0] = x;
//                res[1] = y;
//                return res;
//            }
//        }
//
//        return null;
//    }
//
//    public class MyUF3{
//        // attr
//        int[][] edges;
//        int[] parents;
//        int n; // ??
//
//        // constructor
//        public MyUF3(int[][] edges){
//            this.edges = edges;
//
//            //this.n = 0;
//            HashSet<Integer> set = new HashSet<>();
//            for(int[] x: edges){
//                set.add(x[0]);
//                set.add(x[1]);
//            }
//            this.n = set.size();
//
//            this.parents = new int[n];
//            for(int i = 0; i < n; i++){
//                // init node as its parent
//                this.parents[i] = i;
//            }
//        }
//
//        // method
//        public boolean union(int x, int y){
//            if(x == y){
//                return true;
//            }
//            int x_ = this.getParent(x);
//            int y_ = this.getParent(y);
//            // NOTE !!! below
//            // x != y, but x, y are already connected (same parent)
//            // if we union them AGAIN, will cause `CYCLIC` graph
//            if(x_ == y_){
//                return false;
//            }
//            this.parents[x_] = y_;
//            return true;
//        }
//
//        // NOTE !!! below
//        public int getParent(int x){
//            if(x != this.parents[x]){
//                // note !! update val to x's parent
//                this.parents[x] = this.getParent(x);
//            }
//            return this.parents[x];
//        }
//
//    }


    // LC 127
    // 11.13 - 11.23 AM
    /**
     *
     * Given two words (beginWord and endWord), and a dictionary's word list,
     * find the length of shortest transformation sequence from beginWord to endWord,
     *
     * such that:
     *    - Only one letter can be changed at a time.
     *    - Each transformed word must exist in the word list.
     *      Note that beginWord is not a transformed word.
     *
     *  -> find the length of shortest transformation sequence
     *     from beginWord to endWord, such that:
     *
     *  NOTE:
     *
     *   Return 0 if there is no such transformation sequence.
     *   All words have the same length.
     *   All words contain only lowercase alphabetic characters.
     *   You may assume no duplicates in the word list.
     *   You may assume beginWord and endWord are non-empty and are not the same.
     *
     *
     *   Example 1)
     *
     * Input:
     * beginWord = "hit",
     * endWord = "cog",
     * wordList = ["hot","dot","dog","lot","log","cog"]
     *
     * Output: 5
     *
     * Explanation: As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
     * return its length 5.
     *
     *
     *   IDEA 1) BFS ???
     *
     *    -> for each idx, change and see if it's possible to move forward
     *
     *  exp 1)
     *     input: `hit`
     *     wordList = ["hot","dot","dog","lot","log","cog"]
     *     endWord = "cog",
     *
     *     1)
     *     -> hit, change idx=0 (a-z) -> NO VALID word in wordlist
     *        x
     *
     *     -> hit, change idx=1 (a-z) -> hot, add to queue
     *         x
     *
     *     -> hit, change idx=2 (a-z) -> NO VALID word in wordlist
     *          x
     *
     *
     *    2)
     *      -> hot, change idx=0 (a-z), dot, lot, add to queue
     *         x
     *
     *      -> hot, change idx=0 (a-z), NO VALID word in wordlist
     *          x
     *
     *     -> hot, change idx=0 (a-z), NO VALID word in wordlist
     *          x
     *
     *    3)
     *
     *     -> dot  change idx=0 (a-z) (hot visited, pass), lot in the list as well
     *        x
     *
     *     -> ...  dog
     *
     *     ...
     *
     *   4)
     *   ..
     *
     *    -> dog, change idx=0 (a-z), cog, the expect ans, return pathCnt directly
     *
     *
     *
     *
     *
     *     return pathCnt
     *
     *
     *
     *  IDEA 1) BFS
     *
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        // edge
        if(!wordList.contains(endWord)){
            return 0;
        }
        if(wordList == null || wordList.size() == 0){
            return 0;
        }
        if(beginWord.equals(endWord)){
            return 1; // ???
        }

        // use hashset to NOT visit again
        HashSet<String> visited = new HashSet<>();
        visited.add(beginWord);
//        for(String x: wordList){
//            set.add(x);
//        }

        // bfs
        Queue<String> q = new LinkedList<>();
        int res = 0;
        q.add(beginWord);

        while(!q.isEmpty()){

            for(int j = 0; j < q.size(); j++){

                String cur = q.poll();
                if(cur.equals(endWord)){
                    return res;
                }
                if(cur.length() > endWord.length()){
                    continue; // ?
                }

                String moves = "abcdefghijklmnopqrstuvwxyz";
                // 2 loop
                // 1) go over idx
                // 2) go over moves
                for(int i = 0; i < cur.length(); i++){
                    for(String x: moves.split("")){
                        // ??? get new word that replace alphabet at index i

                        // String nextWord = currentWord.substring(0, j) + c + currentWord.substring(j + 1);
//                    String newWord = String.copyValueOf(cur.toCharArray(), i, i-1)
//                            + x + String.copyValueOf(cur.toCharArray(), i+1, cur.length());
                        String newWord = cur.substring(0, i) + x + cur.substring(i+1, cur.length());

                        if(newWord.equals(endWord)){
                            return res + 1;
                        }

                        // ???
                        boolean shouldProceed = !visited.contains(newWord) && wordList.equals(newWord);
                        if(shouldProceed){
                            q.add(newWord);
                            visited.add(newWord);
                        }
                    }
                }
            }

            res += 1;
        }

        return res;
    }


//    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
//
//        // edge
//        if(!wordList.contains(endWord)){
//            return 0;
//        }
//        if(wordList == null || wordList.isEmpty() || wordList.size() == 0){
//            return 0;
//        }
//        if(beginWord.equals(endWord)){
//            return 0;
//        }
//
//        int pathLen = 0;
//        StringBuilder sb = new StringBuilder();
//        sb.append(beginWord);
//
//        Queue<StringBuilder> q = new LinkedList<>();
//        q.add(sb);
//
//        HashSet<String> visited = new HashSet<>();
//
//        while(!q.isEmpty()){
//
//            StringBuilder curSb = q.poll();
//            if(curSb.toString().equals(endWord)){
//                return pathLen;
//            }
//
//            visited.add(curSb.toString());
//
//            // `moves`
//            String moves = "abcdefghijklmnopqrstuvwxyz";
//            for(String m: moves.split("")){
//                StringBuilder updatedSb = curSb.append(m);
//                // validate
//                boolean shouldProceed = updatedSb.toString().length() < endWord.length() && !visited.contains(updatedSb.toString()) && wordList.contains(updatedSb.toString());
//                if(shouldProceed){
//                    q.add(updatedSb);
//                }
//            }
//
//            pathLen += 1;
//        }
//
//        return pathLen;
//    }

    // LC 743
    // 9.50 - 10.00 am
    /**
     *
     *  743. Network Delay Time
     * There are N network nodes, labelled 1 to N.
     *
     * Given times, a list of travel times as directed edges times[i] = (u, v, w),
     *
     * where u is the source node, v is the target node, and w is the time it takes
     *
     * for a signal to travel from source to target.
     *
     * Now, we send a signal from a certain node K. How long will it take for
     *
     * all nodes to receive the signal? If it is impossible, return -1.
     *
     *
     * -> Now, we send a signal from a certain node K. `How long will it take` for all nodes to
     *    receive the signal? If it is impossible, return -1.
     *
     *
     *
     * Example 1:
     *
     *
     *
     * Input: times = [[2,1,1],[2,3,1],[3,4,1]], N = 4, K = 2
     * Output: 2
     *
     *
     * Note:
     *
     * N will be in the range [1, 100].
     * K will be in the range [1, N].
     * The length of times will be in the range [1, 6000].
     * All edges times[i] = (u, v, w) will have 1 <= u, v <= N and 0 <= w <= 100.
     *
     *
     *  IDEA 1) BFS ???
     *
     *   step 1)  maintain a visited hashset, once all node are visited
     *       return elapsed time
     *
     *   step 2) build the graph,
     *    -> {node: [next_node_1, next_node_2, ...]}
     *    -> visit all nodes via bfs
     *
     */
    // 10.18 - 10.28 am
    /**
     *  IDEA 1) Dijkstra
     *
     *
     *              * Given times, a list of travel times as directed edges
     *              * times[i] = (u, v, w),
     *              *  where u is the source node,
     *              *  v is the target node, and
     *              *  w is the time it takes for a signal to travel from source to target.
     *              *
     *
     *
     *
     *
     */
    public int networkDelayTime(int[][] times, int n, int k) {

        // Edge case: If no nodes exist
        if (times == null || times.length == 0 || n == 0) {
            return -1;
        }
        if (n == 1) {
            return 0; // If there's only one node, no travel time is needed.
        }

        Dijkstra_2 dijkstra = new Dijkstra_2(times, n);
        return dijkstra.getShortestPath(k);
    }

    public class Dijkstra_2 {
        // Attributes
        int[][] times;
        int n;

        // Constructor
        public Dijkstra_2(int[][] times, int n) {
            this.times = times;
            this.n = n;
        }

        // Method to find the shortest path using Dijkstra's algorithm
        public int getShortestPath(int k) {
            // Step 1: Build the graph
            Map<Integer, List<int[]>> edges = new HashMap<>();
            for (int[] time : times) {
               // edges.computeIfAbsent(time[0], key -> new ArrayList<>()).add(new int[] { time[1], time[2] });
                int[] tmp = new int[2];
                tmp[0]= time[1];
                tmp[1] = time[2];
                List<int[]> list = new ArrayList<>();
                list.add(tmp);
                edges.put(time[0], list);
            }

            // Step 2: Initialize the min-heap priority queue (min distance first)
            //PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            PriorityQueue<int[]> minHeap = new PriorityQueue<>(new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    return o1[0] - o2[0];
                }
            });

            minHeap.add(new int[] { 0, k }); // {travel time, node}

            // Step 3: Track visited nodes and the last time value
            Set<Integer> visited = new HashSet<>();
            int t = 0; // The current time to reach the last processed node

            // Step 4: Process the priority queue
            while (!minHeap.isEmpty()) {

                int[] curr = minHeap.poll();
                int w1 = curr[0];
                int n1 = curr[1]; // w1 = current travel time, n1 = node

                // If the node has been visited, skip it
                if (visited.contains(n1)) {
                    continue;
                }

                // Mark the node as visited
                visited.add(n1);
                t = w1; // Update the last travel time

                // Step 5: Process all neighbors of the current node
                if (edges.containsKey(n1)) {
                    for (int[] next : edges.get(n1)) {
                        int n2 = next[0], w2 = next[1]; // n2 = neighbor node, w2 = travel time to neighbor
                        if (!visited.contains(n2)) {
                            minHeap.add(new int[] { w1 + w2, n2 }); // Add neighbor to queue
                        }
                    }
                }
            }

            // Step 6: Check if all nodes are visited, and return the result
            return visited.size() == n ? t : -1; // Return the last time or -1 if not all nodes were visited
        }
    }


//    public class MyNode{
//        // attr
//        int start;
//        int end;
//        int time;
//        public MyNode(int start, int end, int time){
//            this.start = start;
//            this.end = end;
//            this.time = time;
//        }
//    }
//    public int networkDelayTime(int[][] times, int n, int k) {
//        // edge
//        if(times == null || times.length == 0){
//            return 0;
//        }
//        if(n == 1){
//            return 1;
//        }
//        // build graph
//        Map<Integer, List<MyNode>> graph = new HashMap<>();
//
//        HashSet<Integer> visited = new HashSet<>();
//
//        for(int[] t: times){
//            int start = t[0];
//            int end = t[1];
//            int timeCost = t[2];
//            List<MyNode> nextNodes = graph.getOrDefault(start, new ArrayList<>());
//            MyNode node = new MyNode(start, end, timeCost);
//            nextNodes.add(node);
//            graph.put(start, nextNodes);
//        }
//
//        // bfs
//        Queue<MyNode> q = new LinkedList<>();
//        MyNode node = new MyNode(start, end, timeCost);
//        q.add(k); // entry point (node)
//
//        visited.add(k);
//
//        // edge
//        if(!graph.containsKey(k)){
//            return 0;
//        }
//
//        int res = 0;
//
//        while(!q.isEmpty()){
//            MyNode curNode = q.poll();
//
//        }
//
//        return res;
//    }

    // LC 332
    // 10.36 - 10.46 am
    /**
     *  332. Reconstruct Itinerary
     *
     * Given a list of airline tickets represented by pairs of departure and arrival
     *
     * airports [from, to], reconstruct the itinerary in order. All of the tickets
     *
     * belong to a man who departs from JFK. Thus, the itinerary must begin with JFK.
     *
     * Note:
     *
     * If there are multiple valid itineraries, you should return the itinerary that has the smallest lexical order when read as a single string. For example, the itinerary ["JFK", "LGA"] has a smaller lexical order than ["JFK", "LGB"].
     * All airports are represented by three capital letters (IATA code).
     * You may assume all tickets form at least one valid itinerary.
     *
     *
     * Example 1:
     *
     * Input: [["MUC", "LHR"], ["JFK", "MUC"], ["SFO", "SJC"], ["LHR", "SFO"]]
     * Output: ["JFK", "MUC", "LHR", "SFO", "SJC"]
     *
     *
     * Example 2:
     *
     * Input: [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
     * Output: ["JFK","ATL","JFK","SFO","ATL","SFO"]
     * Explanation: Another possible reconstruction is ["JFK","SFO","ATL","JFK","ATL","SFO"].
     *              But it is larger in lexical order.
     * Difficulty:
     *
     *
     *
     */
    public List<String> findItinerary(List<List<String>> tickets) {
        return null;
    }

    // LC 1584
    // 10.57 - 11.10 am
    /**
     *   1584. Min Cost to Connect All Points
     *
     *
     *  You are given an array points representing integer coordinates
     *
     *  of some points on a 2D-plane, where points[i] = [xi, yi].
     *
     * The cost of connecting two points [xi, yi] and [xj, yj] is the
     *
     * manhattan distance between them: |xi - xj| + |yi - yj|,
     *
     * where |val| denotes the absolute value of val.
     *
     * Return the minimum cost to make all points connected.
     *
     * All points are connected if there is exactly one simple path between
     *
     * any two points.
     *
     *
     *  -> Return the minimum cost to make all points connected.
     *    All points are connected if there is exactly one simple path between any two points.
     *
     *
     *
     * Example 1:
     *
     *
     *
     * Input: points = [[0,0],[2,2],[3,10],[5,2],[7,0]]
     * Output: 20
     * Explanation:
     *
     * We can connect the points as shown above to get the minimum cost of 20.
     * Notice that there is a unique path between every pair of points.
     *
     *
     * Example 2:
     *
     * Input: points = [[3,12],[-2,5],[-4,1]]
     * Output: 18
     *
     *
     * Example 3:
     *
     * Input: points = [[0,0],[1,1],[1,0],[-1,1]]
     * Output: 4
     *
     *
     * Example 4:
     *
     * Input: points = [[-1000000,-1000000],[1000000,1000000]]
     * Output: 4000000
     *
     *
     * Example 5:
     *
     * Input: points = [[0,0]]
     * Output: 0
     *
     *
     * Constraints:
     *
     * 1 <= points.length <= 1000
     * -106 <= xi, yi <= 106
     * All pairs (xi, yi) are distinct.
     *
     *
     */
    public int minCostConnectPoints(int[][] points) {

        return 0;
    }

    // LC 269
    // 11.28 - 11.38 AM
    /**
     *  IDEA 1) TOPO SORT
     *
     *   1. init:
     *     - ordering []
     *     - map { node: [sub_node_1, sub_node_2, ..] }
     *     - queue ???
     *
     *   2. BFS
     *     -> visit sub node with key
     *       -> order -= 1, update back to map
     *     -> add node to queue if its `ordering` == 0
     *
     *
     *   3. return res
     *
     */
    public String alienOrder(String[] words){
        // edge
        if(words == null || words.length == 0){
            return null;
        }

        // Topological sort
        HashSet<String> set = new HashSet<>();
        for(String w: words){
            for(String x: w.split("")){
                set.add(x);
            }
        }

        // NOTE !!! instead of using array, use map to track val VS degree
        //int[] degrees = new int[set.size()];
        Map<String, Integer> indegree = new HashMap<>();

        // { word: [sub_word_1, sub_word_2, ...] }
        Map<String, List<String>> map = new HashMap<>();

        for(String w: words){
            for(String x: w.split("")){

                // update indegree
                indegree.put(x, indegree.getOrDefault(x, 0) + 1);

                // update map
                List<String> subList = map.getOrDefault(x, new ArrayList<>());
                //subList.add()
                map.put(x, map.getOrDefault(x, new ArrayList<>()));

            }
        }

        return null;
    }

    // LC 787
    // 10.10 am - 10.20 am
    /**
     *
     * There are n cities connected by m flights.
     *
     * Each fight starts from city u and arrives at v with a price w.
     *
     * Now given all the cities and flights,
     *
     * together with starting city src and the destination dst,
     *
     * your task is to find the cheapest price from src to dst with up to k stops.
     *
     * If there is no such route, output -1.
     *
     *
     *
     *  ->  city u and arrives at v with a price w.
     *
     */
    /**  IDEA 1) Dijkstra
     *
     *   -> use PQ + BFS, check `min cost` within path from start to end point
     *   -> if stops < k, then return cost as res
     *   -> else, return -1 ( can't make it)
     *
     */
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {

        // edge
        if(n == 0 || k == 0){ // ???
            return -1;
        }
        // make cycle ???
        if(flights.length > n - 1){
            return -1;
        }
        if(src == dst){
            return 0;
        }

        // Dijkstra
        Dijkstra_4 dijkstra = new Dijkstra_4(n, flights);
        return dijkstra.getCheapestPrice(src, dst, k);
    }
    public class Dijkstra_4{

        // attr
        int n;
        int[][] flights; // ??

        Map<Integer, List<Integer[]>> graph;

        // constructor
        public Dijkstra_4(int n, int[][] flights){
            this.n = n;
            this.flights = flights;

            this.graph = new HashMap<>();
            for(int[] f: flights){
                int start = f[0];
                int end = f[1];
                int price = f[2];
                List<Integer[]> subList =  this.graph.getOrDefault(start, new ArrayList<>());
                Integer[] sub = new Integer[2];
                sub[0] = end;
                sub[1] = price;
                subList.add(sub);
                this.graph.put(start, subList);
            }

        }

        // method
        public int getCheapestPrice(int src, int dst, int k){
            // edge
            if(src == dst){
                return 0;
            }

            int price = 0;
            /**
             *  Init
             *
             *  1) PQ (small PQ) -> java default PQ is small PQ ???
             *  2) queue ???
             *  3) hashmap ??
             *
             */
            //PriorityQueue<Integer> smallPQ = new PriorityQueue<>(Comparator.reverseOrder()); // ??
            // should sort over `price`
            // PQ should have 2 elements : 1) end 2) price
            PriorityQueue<Integer[]> smallPQ = new PriorityQueue<>(new Comparator<Integer[]>() {
                @Override
                public int compare(Integer[] o1, Integer[] o2) {
                    int diff = o1[2] - o1[1];
                    return diff; // ??
                }
            }); // ??

            HashSet<Integer> visited = new HashSet<>();
            //Queue<Integer> q = new LinkedList<>();

            Integer[] tmp = new Integer[2];
            tmp[0] = src;
            tmp[1] = 0;

            smallPQ.add(tmp);

            int cnt = 0;

           // Map<Integer, Integer> graph = new HashMap<>();
            while(!smallPQ.isEmpty()){  // ???
                Integer[] cur = smallPQ.poll();
                int curEnd = cur[0];
                int curPrice = cur[1];
                if(cnt == k){
                    return price;
                }
                if (cnt > k){
                    continue; // ??? should NOT happen
                }

                if(this.graph.containsKey(curEnd)){
                    for(Integer[] x: this.graph.get(curEnd)){
                        int newEnd = x[0];
                        int newPrice = x[1];
                        if(!visited.contains(newEnd)){

                            Integer[] newSub = new Integer[2];
                            newSub[0] = newEnd;
                            newSub[1] = (price + newPrice);
                            smallPQ.add(newSub); // ???
                            visited.add(newEnd);
                        }
                    }
                }

            }

            return price;
        }

    }

//    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
//
//        // edge
//        if(n == 0 || k == 0){ // ???
//            return -1;
//        }
//        // make cycle ???
//        if(flights.length > n - 1){
//            return -1;
//        }
//        if(src == dst){
//            return 0;
//        }
//
//        // Dijkstra
//        Dijkstra_3 dijkstra = new Dijkstra_3(flights, n);
//
//        return dijkstra.getShortestPath(src, dst, k);
//    }


    public class Dijkstra_3 {
        // Attributes
        int[][] times;
        int n;

        // Constructor
        public Dijkstra_3(int[][] times, int n) {
            this.times = times;
            this.n = n;
        }

        // Method to find the shortest path using Dijkstra's algorithm
        public int getShortestPath(int src, int dst, int k) {
            // Step 1: Build the graph
            Map<Integer, List<int[]>> edges = new HashMap<>();
            for (int[] time : times) {
                edges.computeIfAbsent(time[0], key -> new ArrayList<>()).add(new int[] { time[1], time[2] });
            }

            // Step 2: Initialize the min-heap priority queue (min distance first)
            PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            minHeap.offer(new int[] { 0, src }); // {travel time, node}

            // Step 3: Track visited nodes and the last time value
            Set<Integer> visited = new HashSet<>();
            int t = 0; // The current time to reach the last processed node

            int cnt = 0;

            // Step 4: Process the priority queue
            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int w1 = curr[0], n1 = curr[1]; // w1 = current travel time, n1 = node

                // If the node has been visited, skip it
                if (visited.contains(n1)) {
                    continue;
                }

                if(n1 == dst){
                    if(cnt <= k){
                        return t;
                    }
                    return -1; // ???
                }

                // Mark the node as visited
                visited.add(n1);
                t = w1; // Update the last travel time

                // Step 5: Process all neighbors of the current node
                if (edges.containsKey(n1)) {
                    for (int[] next : edges.get(n1)) {
                        int n2 = next[0], w2 = next[1]; // n2 = neighbor node, w2 = travel time to neighbor
                        if (!visited.contains(n2)) {
                            minHeap.offer(new int[] { w1 + w2, n2 }); // Add neighbor to queue
                        }
                    }
                }

                cnt += 1;
            }

            // Step 6: Check if all nodes are visited, and return the result
            //return visited.size() == k ? t : -1; // Return the last time or -1 if not all nodes were visited
            return cnt <= k ? t : -1;
        }
    }

//    public class MyDijkstra{
//        // attr
//        int n;
//        int[][] flights;
//
//        // { src:  [ [ dest, cost ], [ dest, cost ], ... ]
//        Map<Integer, List<Integer[]>> graph; //????
//
//        // constructor
//        public MyDijkstra(int n, int[][] flights){
//            this.n = n;
//            this.flights = flights;
//            this.graph = new HashMap<>();
//
//            // build graph
//            for(int[] f: flights){
//                int src = f[0];
//                int dest = f[1];
//                int cost = f[2];
//                List<Integer[]> newList = this.graph.getOrDefault(src, new ArrayList<>());
//                Integer[] newVal = new Integer[2];
//                newVal[0] = dest;
//                newVal[1] = cost;
//                newList.add(newVal);
//                this.graph.put(src, newList);
//            }
//        }
//
//        // method
//        public int getShortestPath(int src, int dst, int k){
//
//
//            // min PQ
//            // PQ:  {dest, cost}  -> NO NEED to record SRC,
//            PriorityQueue<Integer[]> minPQ = new PriorityQueue<>(new Comparator<Integer[]>() {
//                @Override
//                public int compare(Integer[] o1, Integer[] o2) {
//                    return o1[2] - o2[2]; // compare on cost (small -> big)
//                }
//            });
//
//            int res = 0;
//            HashSet<Integer> visited = new HashSet<>();
//
//            //minPQ.add(new Integer[]{src, dst, Integer.MAX_VALUE}); // ????
//            minPQ.add(new Integer[]{src, 0}); // ???? since at beginning src == dst, so cost = 0
//
//            int cnt = 0;
//
//            while(!minPQ.isEmpty()){
//
//                Integer[] cur = minPQ.poll();
//                //int _src = cur[0];
//                int _dst = cur[0];
//                int _cost = cur[1];
//                //visited.add(cur);
//
//                if(visited.contains(_dst)){
//                    continue;
//                }
//
//                visited.add(src);
//
////                if(cnt == 0){
////                    res = _cost; // ???
////                }
//
//                if(_dst == dst){
//                    if (cnt <= k){
//                        return res;
//                    }
//                    return -1;
//                }
//
//                // NOTE !!! below
//                if(this.graph.containsKey(_dst)){
//                    for(Integer[] sub: this.graph.get(_dst)){
//
//                        //int new_src = cur[0];
//                        int new_dst = cur[0];
//                        int new_cost = cur[1];
//
//                        res = new_cost;
//
//                        if(!visited.contains(new_dst)){
//                            minPQ.add( new Integer[]{new_dst, new_cost + res} );
//                        }
//                    }
//                }
//
//
//                cnt += 1;
//            }
//
//
//            return res;
//        }
//    }

    // LC 746
    // 11.40 am
    /**
     *   -> either climb one or two steps
     *
     *  ->  you can either start from the step with index 0,
     *      or the step with index 1.
     *
     *
     *
     *
     *  IDEA 1) : DP  (bottom up)
     *
     *   -> get dp[0], dp[1] first
     *   -> recursively, we get dp[2], dp[3], .... dp[k]
     *
     *
     */
    public int minCostClimbingStairs(int[] cost) {
        // edge
        if(cost == null || cost.length == 0){
            return 0;
        }

       int[] dp = new int[cost.length+1];
        //int[] dp = new int[cost.length];
//        dp[0] = cost[0];
//        dp[1] = cost[1]; // ??

        dp[0] = 0;
        dp[1] = 1; // ???

        for(int i = 2; i < cost.length+1 ; i++){
            // do 1 jump or 2 jump
            dp[i] = Math.min( dp[i-2] + dp[i], dp[i-1] );
        }

        System.out.println(">>> dp = " + Arrays.asList(dp));

       // return dp[dp.length-1]; // ???
       return Math.min(dp[dp.length-1], dp[dp.length-2]);
    }

    // LC 005
    // 12.22 - 12.32 pm
    /**
     *  IDEA 1) greedy
     *        step 1) loop over idx
     *               `start from idx`, check the max longest Palindromic
     *                maintain the longest Palindromic sub str at the same time
     *
     */
    public String longestPalindrome(String s) {

        // edge
        if(s == null || s.length() == 0){
            return null;
        }
        if(s.length() == 1){
            return s;
        }
        if(s.length() == 2){
            return s.charAt(0) == s.charAt(1) ? s : null;
        }

        String res = String.valueOf(s.charAt(0)); // init as 1st val

        // 2 pointers
        for(int i = 0; i < s.length(); i++){
            String oddSubStr = getOddPalindromic(s, i);
            String evenSubStr = getEvenPalindromic(s, i);
            if(oddSubStr.length() < evenSubStr.length()){
               if(evenSubStr.length() > res.length()){
                   res = evenSubStr;
               }
            }else{
                if(oddSubStr.length() > res.length()){
                    res = oddSubStr;
                }
            }
        }

        return res; // ???
    }

    public String getOddPalindromic(String s, int start){

        int left = start;
        int right = start;

        StringBuilder sb = new StringBuilder();

        // ???
        while(s.charAt(left) == s.charAt(right) && left > 0 && right < s.length() - 1){
            left -= 1;
            right += 1;
            sb.append(String.valueOf(s.charAt(start)));
            sb.append(String.valueOf(s.charAt(right)));
        }

        return sb.toString(); // ???
    }


    public String getEvenPalindromic(String s, int start){

        int left = start - 1;
        int right = start + 1;

        StringBuilder sb = new StringBuilder();

        // ???
        while(s.charAt(left) == s.charAt(right) && left > 0 && right < s.length() - 1){
            left -= 1;
            right += 1;
            sb.append(String.valueOf(s.charAt(left)));
            sb.append(String.valueOf(s.charAt(right)));
        }

        return sb.toString(); // ???
    }

    // LC 91
    // 9.35 - 9.45 am
    /**
     *
     *
        -> Given a string s containing only digits,
           return `the number of ways` to decode it.
          If the entire string cannot be decoded in any valid way, return 0.

      IDEA 1) DP

      IDEA 2) backtrack

       exp 1)

            s = "12" -> [1, 12] -> 2


           1
       2


      exp 2)  s = "226"   -> [2,2,6], [2,26], [226]

          2               226
        2  26
       6

     exp 3) s = "06" -> invalid -> 0

     */
    public int numDecodings(String encodedString) {

        //edge
        if(encodedString == null || encodedString.length() == 0){
            return 0; // ??
        }

        if(encodedString.charAt(0) == '0'){
            return 0; // ??? 06, 01.. are invalid input
        }

        // DP


        return 0;
    }

    // LC 322
    // 10.08 AM - 10.20 AM
    /**
     *  IDEA 1) BFS
     *  IDES 2) DFS
     *  IDEA 3) DP ???
     *
     */
    public int coinChange(int[] coins, int amount) {
        // edge
        if(coins == null || coins.length == 0){
            return -1;
        }
//        boolean isSmaller = true;
//        for(int c: coins){
//            if(c > amount){
//                isSmaller = false;
//            }
//        }
//        if(isSmaller){
//            return -1;
//        }

        HashSet<Integer> set = new HashSet<>();
        for(int c: coins){
            set.add(c);
        }

        // sort (decreasing) (big -> small)
        //Arrays.sort(Collections.reverseOrder()); // ??

        HashSet<List<Integer>> visited = new HashSet<>();

        // bfs
        Queue<List<Integer>> q = new LinkedList<>();
        // init by add `all unique coin`
        for(int s: set){
            List<Integer> list = new ArrayList<>();
            list.add(s);
            q.add(list);
        }

        //int res = 0;
        while(!q.isEmpty()){

            List<Integer> cur = q.poll();
            int curSum = getListSum(cur);
            if(curSum == amount){
                //Collections.sort(cur);
                if(!visited.contains(cur)){
                   // res += 1;
                   // visited.add(cur);
                   return cur.size();
                }
            }

            if(curSum > amount){
                continue;
            }

            // NOTE !!! below
            for(int c: coins){
                if(curSum + c <= amount){
                    cur.add(c);
                    q.add(cur);
                }
            }
        }

        return -1;
    }

    public int getListSum(List<Integer> input){
        int res = 0;
        for(int x: input){
            res += x;
        }
        return res;
    }

    // LC 152
    // 11.02 - 11.15 am
    /**
     *
     *  IDEA 0) brute force
     *
     *  IDEA 1) SLIDING WINDOW ??
     *
     *   exp 1)
     *
     *     Input: nums = [2,3,-2,4]
     *     Output: 6
     *
     *    -> [2,3,-2,4]  res = 2
     *        l
     *        r
     *
     *   -> [2,3,-2,4]  res = 6
     *       l r
     *
     *   -> [2,3,-2,4]  res = 6
     *       l    r
     *
     *   -> [2,3,-2,4]  res = 6
     *       l      r
     *
     *  exp 2)
     *
     *     Input: nums = [-2,0,-1]
     *     Output: 0
     *
     *
     *   ->  [-2,0,-1], res = -2 ??
     *        l
     *        r
     *
     *   ->  [-2,0,-1], res = 0
     *        l  r
     *
     *   ->  [-2,0,-1], res = 0
     *        l     r
     *
     *
     *
     *
     *
     * IDEA 2) k*** algo   ???
     *   -> algo for getting max / min val from sub array
     *
     *   -> A subarray is a contiguous non-empty sequence
     *      of elements within an array.
     *
     *
     *
     */
    // LC 152
    // IDEA: Kadane algo
    /**
     *   Kadane algo
     *
     *   -> maintain
     *     global max
     *     local max
     *     local min
     *
     *  -> keep compute above within loop, then return global max as final result
     *
     */
    public int maxProduct(int[] nums) {

        // Edge case
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // init
        int global_max = nums[0];
        int local_max = nums[0];
        int local_min = nums[0];

        for(int i = 1; i < nums.length; i++){

            int n = nums[i];

            // cache local_max
            int cache = local_max;

            local_max = Math.max(
                    local_min * n,
                    Math.max(local_max * n, n)
            );

            local_min = Math.min(
                    n * cache,
                    Math.min(local_min * n, n)
            );

            global_max = Math.max(
                    global_max,
                    Math.max(local_max, local_min)
            );
        }

        return global_max;
    }



//    public int maxProduct(int[] nums) {
//
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0; // ?
//        }
//        if(nums.length == 1){
//            return nums[0];
//        }
//        if(nums.length == 2){
//            if (nums[0] * nums[1] > 0){
//                return nums[0] * nums[1];
//            }
//            return Math.max(nums[0], nums[1]);
//        }
//
//        // sliding window
//        int res = Integer.MAX_VALUE; // ??
//        for(int n: nums){
//            res = Math.min(res, n);
//        }
//
//        // ???
//        for(int i = 0; i < nums.length - 1; i++){
//            int cur = 1;
//            for(int j = i; j < nums.length; j++){
//                cur = cur * nums[j];
//                // get max from cur, res, nums[j]
//                res = Math.max(nums[j], Math.max(res, cur));
//            }
//        }
//
//        return res;
//    }

    // V2:
    // IDEA: Kadane’s Algorithm for Maximum Product Subarray (GPT)
//    public int maxProduct(int[] nums) {
//
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0; // ?
//        }
//        if(nums.length == 1){
//            return nums[0];
//        }
//        if(nums.length == 2){
//            if (nums[0] * nums[1] > 0){
//                return nums[0] * nums[1];
//            }
//            return Math.max(nums[0], nums[1]);
//        }
//
//        int res = Integer.MAX_VALUE; // ??
//        for(int n: nums){
//            res = Math.min(res, n);
//        }
//
//        /**
//         * Kadane algo
//         *
//         *  -> is DP algo actually
//         *
//         *  -> maintain
//         *     - local min, max
//         *     - global max
//         *
//         *     -> so, within the iteration, we can still
//         *     -> get possible max val via local_min * cur
//         *     -> on the same time, we calculate local_max (local_max * cur) as well
//         *     -> and update the global max global_max = max(local_min, local_max)
//         */
//
//        // init // ???
//        int global_max = nums[0];
//        int local_max = nums[0];
//        int local_min = nums[0];
//
//        for(int i = 1; i < nums.length - 1; i++){
//            //for(int j = 0)
//            if(nums[i] < 0){
//                local_min = local_min * nums[i];
//                local_max = 1; // ???
//            }else{
//                local_max = local_max * nums[i]; // ??
//            }
//            global_max = Math.max(global_max, Math.max(local_max, local_min));
//        }
//
//        return global_max;
//    }

    // LC 1143
    // 5.03 - 5.20 pm
    /**
     *  -> , return the `length of their longest common subsequence. `
     *      If there is no common subsequence, return 0.
     *
     *   A subsequence of a string is a new string generated from
     *   the original string with some characters (can be none)
     *   deleted without changing the relative order of the remaining characters.
     *
     *
     *   IDEA 1) SLIDING WINDOW
     *
     *   IDEA 2) BRUTE FORCE
     *
     *    -> double loop
     *    -> compare the `longest subsequence` within t1, t2
     *
     *
     *   IDEA 3) DP ???
     *
     *
     *
     *
     *
     */
    public int longestCommonSubsequence(String text1, String text2) {
        // edge

        return 0;
    }

  // LC 309
  // 5.25 - 5.35 pm
  /**
   *
   *  NOTE !
   *
   *  After you sell your stock,
   *  you cannot buy stock on the next day
   *  (i.e., cooldown one day).
   *
   *
   *
   * -> Find the maximum profit you can achieve.
   *
   * DP eq:
   *
   *
   *  exp 1)
   *
   *  Input: prices = [1,2,3,0,2]
   *  Output: 3
   *
   *  -> dp[0] -> buy or no buy max(0, -1)
   *  -> dp[1] -> buy or sell or not buy
   *
   *
   *
   *
   *
   */
  public int maxProfit(int[] prices) {
        return 0;
    }


  // LC 518
  // 10.09 - 10.19 am
  /**
   *  IDEA 1) BACKTRACK ???
   *
   *  IDEA 2) DP ??
   *
   *
   * exp 1)
   *   Input: amount = 5, coins = [1, 2, 5]
   *   -> Output: 4
   *
   *   so, for each element, we hv 2 choices, select or NOT select
   *   -> brute force, we have 2^n time complexity
   *
   *   -> think as a `decision tree`
   *
   *      1    2   5
   *
   *    1    2   5
   *  1 125  125  125 ...
   *  1
   *
   *
   *
   *  -> we start from `last element`, backward calculate
   *     the possible amount of combinations
   *
   *
   *     coins = [1, 2, 5]
   *
   *   dp[2] = 1  (if coins[2] % amount == 0)
   *   dp[1] = math.max(dp[1], dp[2])
   *
   *
   */
  public int change(int amount, int[] coins) {


      return 0;
  }

  // LC 300
  // IDEA: DP
  // 10.30 - 10.40 am
  /**
   *
   *  exp 1)
   *
   *  Input: [10,9,2,5,3,7,101,18]
   *  Output: 4
   *  Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
   *
   *   dp[7] = 1
   *   dp[6] = 1  if dp[7] > dp[6]
   *                    dp[6] = max(dp[6], dp[7] + 1)
   *              else:
   *                  dp[6] = dp[7]
   *
   *  dp[5] = 2   if dp[6] > dp[5]
   *                    dp[5] = dp[6] + 1
   *             else:
   *                   dp[5] = dp[6]
   *
   * ...
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

      int[] dp = new int[nums.length + 1];
      Arrays.fill(dp, 1); // ???
//      // init all val as 1
//      for(int i = 0; i < dp.length; i++){
//          dp[i] = 1;
//      }

      // inverse traversal
      // i start from idx = len - 1 to idx = 1
      for(int i =  nums.length - 1; i > 0; i--){
          if(nums[i] > nums[i-1]){
              dp[i-1] = Math.max(dp[i] + 1, dp[i-1]);
          }else{
              dp[i-1] = dp[i];
          }
      }

      return dp[0]; // ???
  }

  // LC 494
  // 11.08 - 11.20 am
  /**
   * You are given a list of non-negative integers, a1, a2, ..., an,
   *
   * and a target, S. Now you have 2 symbols + and -.
   *
   * For each integer, you should choose one from + and - as its new symbol.
   *
   *
   * -> Find out how many ways to assign symbols to make
   *   sum of integers equal to target S.
   *
   *
   *
   */
  public int findTargetSumWays(int[] nums, int target) {

      return 0;
  }

  // LC 97
  // 11.37 - 11.47 am
  public boolean isInterleave(String s1, String s2, String s3) {
      return false;
  }

  // LC 329
  // 10.08 - 10.18 am
  public int longestIncreasingPath(int[][] matrix) {

      return 0;
  }

  /**
   *  Given two words word1 and word2, find the minimum number of operations required to convert word1 to word2.
   *
   * You have the following 3 operations permitted on a word:
   *
   * Insert a character
   * Delete a character
   * Replace a character
   * Example 1:
   *
   * Input: word1 = "horse", word2 = "ros"
   * Output: 3
   * Explanation:
   * horse -> rorse (replace 'h' with 'r')
   * rorse -> rose (remove 'r')
   * rose -> ros (remove 'e')
   * Example 2:
   *
   * Input: word1 = "intention", word2 = "execution"
   * Output: 5
   * Explanation:
   * intention -> inention (remove 't')
   * inention -> enention (replace 'i' with 'e')
   * enention -> exention (replace 'n' with 'x')
   * exention -> exection (replace 'n' with 'c')
   * exection -> execution (insert 'u')
   * Difficulty:
   *
   *
   *
   */
  // LC 72
  // 10.33 - 10.43 AM
    public int minDistance(String word1, String word2) {
        return 0;
    }

    // LC 139
    public boolean wordBreak(String s, List<String> wordDict) {
        // edge

        Set<String> wordSet = new HashSet<>();
        for(String w: wordDict){
            wordSet.add(w);
        }

        // NOTE !!! below
        Set<Integer> visited = new HashSet<>();

        // bfs
        Queue<Integer> q = new LinkedList<>();
        q.add(0); // ??? // Start from index 0

        while(!q.isEmpty()){

            Integer start = q.poll(); // cur `idx`

            visited.add(start);

            for(String w: wordSet){
                int end = start + w.length();
                // if visited
                if(visited.contains(end)){
                    continue;
                }
                if(end <= s.length() && s.substring(start, end).equals(w)){
                    // NOTE !!! below
                    if(end == s.length()){
                        return true;
                    }
                    q.add(end);
                    // add as visited
                    //visited.add(end);
                }
            }
        }

        return false;
    }

//    public boolean wordBreak(String s, List<String> wordDict) {
//        // edge
//
//        Set<String> visited = new HashSet<>();
//
//        // bfs
//        Queue<String> q = new LinkedList<>();
//        q.add(""); // ?
//
//        while(!q.isEmpty()){
//
//            String cur = q.poll();
//            int len = cur.length();
//
//            for(String w: wordDict){
//
//                if(w.equals(s.substring(len, len + w.length()))){
//
//                    if(cur.equals(s)){
//                        return true;
//                    }
//
//                    // note below
//                    String newStr = (cur + w);
//                    if(newStr.length() > s.length() || visited.contains(newStr)){
//                        continue;
//                    }
//
//                    q.add(newStr);
//                }
//            }
//        }
//
//        return false;
//    }

    // LC 10
    // 10.16 - 10.20 am
    public boolean isMatch(String s, String p) {

        return false;
    }


    // LC 53
    // 10.27 - 10.37 am
    // IDEA 1)  k*** algo
    // IDEA 2) SLIDING WINDOW
    // IDEA 3) ???
    /**
     * Given an integer array nums, find the subarray
     *
     * with the largest sum, and return its sum.
     *
     *
     * -> A subarray is a `contiguous` non-empty
     *   sequence of elements within an array.
     *
     *  Example 1:
     *
     * Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
     * Output: 6
     * Explanation: The subarray [4,-1,2,1] has the largest sum 6.
     *
     *
     * Example 2:
     *
     * Input: nums = [1]
     * Output: 1
     * Explanation: The subarray [1] has the largest sum 1.
     *
     *
     * Example 3:
     *
     * Input: nums = [5,4,-1,7,8]
     * Output: 23
     * Explanation: The subarray [5,4,-1,7,8] has the largest sum 23.
     *
     *
     */
    public int maxSubArray(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0];
        }
        if(nums.length == 2){
            if(nums[0] >= 0 && nums[1] >= 0){
                return nums[0] + nums[1];
            }
            return Math.max(nums[0], nums[1]);
        }

        // k*** algo
        /**
         *  define
         *   - local_min
         *   - local_max
         *   - global_max
         *
         */
        //int res = nums[0]; // ??
        int local_max = nums[0];
       // int local_min = nums[0];
        int global_max = nums[0];

        for(int i = 1; i < nums.length; i++){

            int cur = nums[i];

            // cache local_max ???
            //int cache = local_max;

            if(local_max + cur < 0){
                local_max = cur;
            }else{
                local_max  += cur;
            }


            global_max = Math.max(local_max, global_max);

            System.out.println(">>> cur = " + cur + ", local_max = " + local_max
                    + ", global_max = " + global_max);
        }


        return global_max;
    }


    // LC 55
    // 11.13 - 11.23 AM
    // IDEA: GREEDY
    /**
     *  Given an array of `non-negative` integers,
     *
     *  you are initially positioned at `the first index` of the array.
     *
     *  Each element in the array represents your maximum jump length at that position.
     *
     *  ->  Determine if you are able to reach the last index.
     *
     *
     */
    // 11.35 - 11.45 am
    /**
     *   IDEA: LOOP FROM RIGHT (<----)
     *
     *
     *
     *
     */
    public boolean canJump(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return true;
        }
        if(nums.length == 1){
            return true;
        }

        //int farestReach = 0; // ??
        int rightBoundary = nums.length - 1;

        for(int i = nums.length - 1; i >= 0; i--){

            System.out.println(">>> i = " + i + ", nums[i] = " + nums[i] + ", rightBoundary = " + rightBoundary);

            // if current position can reach the `current destination`
            // -> we update the `current destination` to the current idx
            if(i + nums[i] >= rightBoundary){
               // return false; // ???
                rightBoundary = i;
            }

        }

        return rightBoundary == 0; // check if we can reach the 0 idx
    }

    // IDEA 2) LOOP FORM RIGHT TO LEFT (<----)
//    public boolean canJump(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return false; // ??
//        }
//        if(nums.length == 1){
//            return true;
//        }
//
//        // <---
//        //int cur = nums[nums.length - 1];
//        int lastIdx = nums.length - 1;
//
//        for(int i = nums.length - 1; i >= 0; i--){
//            //cur = (cur - nums[i-1]);
//            if(i + nums[i] >= lastIdx){
//                lastIdx = i;
//            }
//        }
//
//        //return cur <= 0; // ????
//        return lastIdx == 0; // ???
//    }

//    public boolean canJump(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return false; // ??
//        }
//        if(nums.length == 1){
//            return true;
//        }
//
//        int curMax = 0;
//
//        for(int i = 0; i < nums.length; i++){
//
//            //curMax = Math.max(nums[i], curMax); // ???
//
//            if(i + nums[i] >= nums.length - 1){
//                return true;
//            }
//        }
//
//        return false;
//    }

    // LC 45
    // 11.52 - 12.10 pm
    // IDEA:  GREEDY
    public int jump_2(int[] nums) {

        // edge
        if(nums == null || nums.length == 0){
            return -1; // ??
        }
        if(nums.length == 1){
            return 0;
        }

        int step = 0;
        int cur = -1;
        int farthestReach = 0;
        for(int i = 0; i < nums.length; i++){

//            if(cur < 0){
//                cur = nums[i] + i;
//            }
//            // if need to update `cur`
//            else if (nums[i] + i > cur) {
//                cur = nums[i] + i; // ???
//            }

            cur = Math.max(cur, nums[i] + i);
            if(i == farthestReach){
                step += 1;
                farthestReach = cur;
            }

            if(farthestReach >= nums.length - 1){
                return step;
            }

            //step += 1;
        }

        return step;
    }

    // LC 19
    // 12.38 - 12.48 pm
    /**
     *  IDEA: 2 POINTERS
     *
     *  -> fast, slow
     *  -> fast move `n` steps
     *  -> then, slow, fast move together
     *       -> till fast reach the end
     *
     *  1->2->3->4->5   (n=2)
     *  f  f  f
     *
     *        f  f  f
     *  s s   s
     *
     *  1->2->3->4->5   (n=2)
     *
     *
     *
     *  expect res:
     *    1 -> 2 -> 3 -> 5
     *
     *
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // edge
        if(head == null || head.next == null){
            return null; // ???
        }
        // get len
        int len = 0;
        ListNode head2 = head;
        while(head2 != null){
            head2 = head2.next;
            len += 1;
        }

        if(n > len){
            return null; // ??? NOT possible to remove a node by idx
        }
        if(n == len-1){
            return head.next; // ???
        }

        // if n is `at beginning or the end of idx`

        // fast, slow pointer
        ListNode fast = head;
        ListNode slow = head;

        // init dummy, point to slow, for returning answer
        ListNode dummy = new ListNode(); // ?
        dummy.next = slow; // ???

        // move fast ONLY by `n` steps
        while(n+1 > 0){
            fast = fast.next;
            n -= 1;
        }

        // move fast, slow together
        while(fast != null){
            fast = fast.next;
            slow = slow.next;
        }

        // reach the point
        ListNode _next2 = slow.next.next;
        slow.next = _next2;

        return dummy.next;
    }


    // LC 846
    // 10.05 AM - 10.15 AM
    /**
     * Alice has a hand of cards,
     *
     * given as an array of integers.
     *
     * Now she wants to rearrange
     *
     * the cards into groups so that `each group is size W, `
     *
     * `and consists of W consecutive cards.`
     *
     *  Return true if and only if she can.
     *
     *
     *  Example 1:
     *
     * Input: hand = [1,2,3,6,2,3,4,7,8], W = 3
     * Output: true
     * Explanation: Alice's hand can be
     * rearranged as [1,2,3],[2,3,4],[6,7,8].
     *
     *
     * Example 2:
     *
     * Input: hand = [1,2,3,4,5], W = 4
     * Output: false
     * Explanation: Alice's hand
     * can't be rearranged into groups of 4.
     *
     *
     *  IDEA 1)
     *
     *
     *  exp 1)
     *
     *    hand = [1,2,3,6,2,3,4,7,8], W = 3
     *
     *    -> each  sub-array len = 9 / 3 = 3
     *    -> so sub_arr_len = 3, 3 sub_array
     *
     *   -> re-order: [1,2,2,3,3,4,6,7,8]
     *
     *      1, 2, 3
     *      2, 3, 4
     *
     *
     *
     *
     *      -> consecutive cards. each diff = 1.
     *
     *
     *
     */
    public boolean isNStraightHand(int[] hand, int groupSize) {
        // edge
        if(hand == null || hand.length == 0){
            return false;
        }
        if(groupSize == 0){
            return false;
        }
        if(groupSize == 1){
            return true;
        }

        int len = hand.length;
        if(len % groupSize != 0){
            return false;
        }
        int subLen = len / groupSize;

        System.out.println(">>> before sort, hand = " + hand);
        // reorder
        Arrays.sort(hand);
        System.out.println(">>> after sort, hand = " + hand);

        //List<Integer[]> collected = new ArrayList<>();
        Map<Integer, List<Integer>> collected = new HashMap<>();
//        for(int x: hand){
//            if(map.)
//        }

        return true;
    }


    public boolean isNStraightHand_1_1(int[] hand, int groupSize) {
        if (hand.length % groupSize != 0) return false;
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : hand) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        /**
         *
         *  map : {1: 1, 2: 2, 3:2, 4:1, 6:1, 7:1, 8:1}
         *
         */
        Arrays.sort(hand);
        // [1,2,2,3,3,4,6,7,8]
        for (int num : hand) {
            if (count.get(num) > 0) {
                for (int i = num; i < num + groupSize; i++) {
                    if (count.getOrDefault(i, 0) == 0) return false;
                    count.put(i, count.get(i) - 1);
                }
            }
        }

        return true;
    }


    // LC 1899
    // 11.02 - 11.12 am
    /**
     *  IDEA 1) GREEDY
     *
     *
     *  exp 1)
     *   输入：triplets = [[2,5,3],[1,8,4],[1,7,5]], target = [2,7,5]
     *    输出：true
     *
     *  step 1)  loop over src, loop over target,
     *           check if element in `target` exist in
     *           current element
     *           e.g. if target = [2,7,5]
     *           loop over triplets,
     *           check if
     *            - 2 exist
     *            - 7 exist
     *            - 5 exist
     *         choose 2 arr, do max op,
     *          replace existing arr with new arr
     *
     *
     *  exp 2)
     *
     *   输入：triplets = [[2,5,3],[2,3,4],[1,2,5],[5,2,3]],
     *     target = [5,5,5]
     *
     *
     *  IDEA 1) GREEDY
     *
     *   - use hashmap record `different idx`
     *      - map = {idx: [diff_idx_1, diff_idx_2, ....] }
     *      - map = {0: [0,2], 1: [0,1,2], 2: [0,1], 3: [1,2] }
     *
     *   - loop over triplets, start from the key that has `least diff idx`
     *       - pick idx = 0, 2 from triplets
     *
     *   - update triplets, map accordingly
     *   - if there is a key has NO diff, then return true
     *   - otherwise, return false
     *
     */
    public boolean mergeTriplets(int[][] triplets, int[] target) {
        // edge
        if(triplets == null || triplets.length == 0){
            return false; // ?
        }
        // step 1) remove `not qualified` element
        List<int[]> collected = new ArrayList<>();
        //boolean isValid = true;
        for(int[] t: triplets){
            boolean isValid = true;
            for(int i = 0; i < t.length; i++){
                if(t[i] > target[i]){
                    isValid = false;
                    break; // ??
                }
            }
            if(isValid){
                collected.add(t);
            }
        }

        System.out.println(">>> collected = " + collected);

        //int[] cache = new int[triplets.length];
        //List<Integer> cache = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        for(int[] c: collected){
            for(int i = 0; i < c.length; i++){
                System.out.println(">>> c[i]  = " + c[i] + ",  target[i] = " +  target[i]);
                if(c[i] == target[i]){
                    set.add(i);
                }
            }
        }

        System.out.println(">>> set = " + set);

        return set.size() == target.length; // ??
    }

    // LC 763
    // 11.45 am - 12.10 PM
    /**
     *

     A string S of lowercase letters is given.


     We want to partition this string into as many parts as possible so that each letter appears in at most one part, and return a list of integers representing the size of these parts.


     Example 1:

     Input: S = "ababcbacadefegdehijhklij"
     Output: [9,7,8]
     Explanation:
     The partition is "ababcbaca", "defegde", "hijhklij".
     This is a partition so that each letter appears in at most one part.
     A partition like "ababcbacadefegde", "hijhklij" is incorrect, because it splits S into less parts.
     Note:

     S will have length in range [1, 500].
     S will consist of lowercase letters ('a' to 'z') only.
     Difficulty:

     *
     *
     *
     * IDEA 1) 2 POINTERS + HASHMAP ???
     *
     *   -> use hashmap get cnt,
     *      -> map = {a: 3, b:3 ,....}
     *
     *   -> use 2 pointers, loop over s,
     *      once all elements cnt (in map) reached,
     *      we `cut off` the sub str, restart the new pointers
     *
     *   -> repeat above process, and return res
     *
     *
     *
     */
    // 1.38 pm - 1.48 pm
    /**
     *  IDEA 1) HASH MAP
     *  -> map records the `latest idx` of an element existed
     *  -> loop over the string, till `reach the latest idx`, then append cur len to res array
     *  -> reset len, repeat above steps, till reach the end of input string
     */
    public List<Integer> partitionLabels(String s) {

        List<Integer> res = new ArrayList<>();

        // edge
        if(s == null || s.isEmpty()){
            return null;
        }
        if(s.length() == 1){
            res.add(1);
            return res;
        }

        Map<String, Integer> map = new HashMap<>();
        String[] s_arr = s.split("");
        System.out.println(">>> s_arr = " + s_arr);

        for(int i = 0; i < s_arr.length; i++){
            map.put(s_arr[i], i);
        }

        System.out.println(">>> map = " + map);

        int cur_len = 0;
        int cur_max_idx = 0;
        for(int i = 0; i < s_arr.length; i++){
            cur_max_idx = Math.max(cur_max_idx, map.get(s_arr[i]));
            if(cur_max_idx == i){
                res.add(cur_len);
                cur_len = 0;
                //cur_max_idx =
            }else{
                cur_len += 1;
            }
        }

        return res;
    }

//    public List<Integer> partitionLabels(String s) {
//
//        List<Integer> res = new ArrayList<>();
//
//        // edge
//        if(s == null || s.length() == 0){
//            return null;
//        }
//        if(s.length() == 1){
//            res.add(1);
//            return res;
//        }
//
//        // build map
//        // { val: cnt}
//        Map<String, Integer> map = new HashMap<>();
//        for(String x: s.split("")){
//            map.put(x, map.getOrDefault(x, 0)+1);
//        }
//
//        int slow = 0;
//        int fast = 0;
//
//        while(slow < s.length() - 1){
//            //if(map.g)
//            //Map<String, Integer> tmp = new HashMap<>();
//            List<String> tmp = new ArrayList<>();
//
//            //int fast = i; // ?
//            while (fast < s.length()){
//                // if `current window` has all same elements
//                // -> reset 2 pointers, build the next sub str
//                if(hasAllElementsInWindow(map, tmp)){
//                    res.add(tmp.size());
//                    slow = fast + 1;
//                    //fast = fast;
//                    continue;
//                }
//                String curVal = String.valueOf(s.charAt(fast));
//                tmp.add(curVal);
//                fast += 1;
//            }
//
//            slow += 1;
//        }
//
//        return res;
//    }
//
//    public boolean hasAllElementsInWindow(Map<String, Integer> map, List<String> tmp){
//        Map<String, Integer> tmpMap = new HashMap<>();
//        for(String x: tmp){
//            tmpMap.put(x, tmpMap.getOrDefault(x, 0)+1);
//        }
//        for(String k: tmpMap.keySet()){
//            if(tmpMap.get(k) != map.get(k)){
//                return false;
//            }
//        }
//
//        return true;
//    }

    // LC 678
    // 9.40 - 9.50 am
    /**
     *  IDEA 1) QUEUE ??
     *
     *
     *
     *  exp 1)
     *     s = "()"
     *     -> true
     *
     *   (, q = [ "(" ]
     *   ), q = [],
     *
     *
     *   exp 2)
     *
     *     s = "(*)"
     *     (, q = ["("]
     *     *, q = ["("], q2 = ["*"]
     *     ), q = [], q2 = ["*"]
     *
     *  exp 3)
     *
     *     s = "(*))"
     *     (, q = ["("]
     *     *, q = ["("], q2 = ["*"]
     *     ), q = [], q2 = ["*"]
     *     ), q = [], q2 = [],
     *
     *
     *
     */
    // 2.04 PM - 2.14 PM
    /**
     *  IDEA 1) GREEDY
     *
     *  step 1) maintain 2 var
     *        - minParenCnt
     *        - bigParenCnt
     *
     *  step 2) within loop
     *     - if "(",  minParenCnt += 1, bigParenCnt += 1
     *     - if ")",  minParenCnt -= 1, bigParenCnt -= 1
     *     - if "*", `wild card`
     *         - minParenCnt -= 1
     *         - bigParenCnt += 1
     *     - NOTE !!
     *         - if minParenCnt < 0, we reset it as 0
     *         - if bigParenCnt < 0, return false directly
     *
     *    step 3) check if 0 in [minParenCnt, bigParenCnt] ???
     *
     */
    public boolean checkValidString(String s) {
        // edge
        if(s == null || s.isEmpty()){
            return true; // ?
        }
        if(s.length() == 1){
            return s.equals("*"); // only "*", return true, otherwise return false
        }

        int minParenCnt = 0;
        int maxParenCnt = 0;

        for(String x: s.split("")){
            if(x.equals("(")){
                minParenCnt += 1;
                maxParenCnt += 1;
            }
            else if(x.equals(")")){
                minParenCnt -= 1;
                maxParenCnt -= 1;
            }else{
                minParenCnt -= 1;
                maxParenCnt += 1;
            }

            // NOTE !!! below
            if(minParenCnt < 0){
                minParenCnt = 0;
            }
            if(maxParenCnt < 0){
                return false;
            }
        }

        return minParenCnt == 0; // ???
    }


//    public boolean checkValidString(String s) {
//        // edge
//        if(s == null || s.length() == 0){
//            return true; // ??
//        }
//        if(s.length() == 1){
//            if(s.equals("*")){  // ???
//                return true;
//            }
//            return false;
//        }
//
//        // queue
//        Queue<String> q1 = new LinkedList<>();
//        Queue<String> q2 = new LinkedList<>();
//        for(String x: s.split("")){
//            // case 1): "("
//            if(x.equals("(")){
//                q1.add(x);
//            }
//            // case 2): "*"
//            else if (x.equals("*")) {
//                q2.add(x);
//            }
//            // case 3):  ")"
//            else{
//                if(!q1.isEmpty()){
//                    q1.poll();
//                } else if (!q2.isEmpty()) {
//                    q2.poll();
//                }else{
//                    return false;
//                }
//            }
//        }
//
//        System.out.println(">>> q1 = " + q1);
//        System.out.println(">>> q2 = " + q2);
//        return q1.isEmpty();
//    }

    // LC 57
    // IDEA: array op
    // 10.53 - 11.00 am
    public int[][] insert(int[][] intervals, int[] newInterval) {
        // edge
        if(intervals == null || intervals.length == 0){
            return null;
        }
        if(newInterval == null || newInterval.length == 0){
            return intervals;
        }

       //Queue<List<Integer>> q = new LinkedList<>(); // FIFO
        Stack<List<Integer>> st = new Stack<>(); // FILO
        List<List<Integer>> cached = new ArrayList<>();
        List<List<Integer>> collected = new ArrayList<>();

        for(int[] x: intervals){
            List<Integer> tmp = new ArrayList<>();
            tmp.add(x[0]);
            tmp.add(x[1]);
            cached.add(tmp);
          //  Integer[][] tmp = new Integer[][]{{x[0], x[1]}};
        }

        List<Integer> tmp2 = new ArrayList<>();
        tmp2.add(newInterval[0]);
        tmp2.add(newInterval[1]);
        cached.add(tmp2);

        // sorting
        Collections.sort(cached, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                // sort 1) 1st element (small -> big)
                //      2) 2nd element ??? (samll -> big)
                int diff = o1.get(0) - o2.get(0); //
                if(diff == 0){
                    return o1.get(1) - o2.get(1);
                }
                return diff;
            }
        });

        for(List<Integer> x: cached){
            if(st.isEmpty()){
                st.add(x);
            }else{
                //while()
                //while(q.peek().get(1) < x.get(0)){
                List<Integer> last = st.pop(); // ??
                /**
                 *   3 types of overlap
                 *
                 *   1)
                 *       |----------|   old
                 *         |---------------|  new
                 *
                 *  2)
                 *          |------|  old
                 *       |-----|      new
                 *
                 *
                 * 3)
                 *     |--------------|   old
                 *        |-----|        new
                 *
                 *
                 *  NON OVERLAP
                 *
                 *    |----|  old
                 *            |--------|  new
                 *
                 *
                 */
                List<Integer> newList = new ArrayList<>();

                if(  ( last.get(1) > x.get(0) && last.get(1) < x.get(1) )  &&
                     ( last.get(0) < x.get(1) && last.get(1) > x.get(1) ) &&
                     ( last.get(1) > x.get(1) && last.get(0) < x.get(0) ) ){

                    newList.add(Math.min(last.get(0), x.get(0)));
                    newList.add(Math.max(last.get(1), x.get(1)));

                }else{

                    newList.add(x.get(0));
                    newList.add(x.get(0));
                }

                st.add(newList);
            }
        }

        int[][] res = new int[st.size()][2];

        int cnt = 0;
        while(!st.isEmpty()){
            int[] tmp3 = new int[2];
            List<Integer> x = st.pop();
            tmp3[0] = x.get(0);
            tmp3[1] = x.get(1);
            cnt += 1;
        }

        return res;
    }

    // LC 56
    // 3.35 - 3.45 pm
    // IDEA : STACK (LIFO)
    public int[][] merge(int[][] intervals) {
        // edge
        if(intervals == null || intervals.length == 0){
            return null;
        }

        // sort
        // 1st element : small -> big
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[0] - o2[0];
                return diff;
            }
        });

        Stack<int[]> st = new Stack<>();
        List<int[]> collected = new ArrayList<>();
        for(int[] x: intervals){
            // case 1) stack is empty
            if(st.isEmpty()){
                st.add(x);
            }else{
                /**
                 *  since we already sorted intervals on
                 *  1st element as increasing order (small -> big)
                 *  the ONLY non overlap case is as below:
                 *
                 *    |----|  old
                 *            |------| new
                 */
                int[] prev = st.pop();
                // case 2) if NOT overlap
                if(prev[1] < x[0]){
                    st.add(prev);
                    st.add(x);
                }
                // case 3) OVERLAP
                else{
                    st.add(new int[]{ Math.min(prev[0], x[0]), Math.max(prev[1], x[1]) });
                }
            }
        }

        // stack -> list
//        while(!st.isEmpty()){
//            collected.add(st.pop());
//        }

        System.out.println(">>> st = ");
        for(int i = 0; i < st.size(); i++){
            System.out.println("st val = " + st.get(i));
        }


        for(int i = 0; i < st.size(); i++){
            collected.add(st.get(i)); // ???
        }

        //int[][] res = collected.toArray(new int[collected.size()][]);

        // ???
        return collected.toArray(new int[collected.size()][]);
    }

    // LC 435
    // 4.07 - 4.20 pm
    // IDEA: ARRAY OP
    /**
     *   step 1) sort on 2nd element ???
     *         -> big -> small (decreasing order)
     *
     *
     *   exp 1)
     *    intervals = [[1,2],[2,3],[3,4],[1,3]]
     *    -> res = 1
     *
     *    |---|
     *    1   2
     *        |----|
     *        2    3
     *             |---|
     *             3   4
     *   |---------|
     *   1         3
     *
     *
     *   exp 2)
     *
     *   intervals = [[1,2],[2,3]]
     *   -> res = 0
     *
     *   |---|
     *   1   2
     *       |----|
     *       2    3
     *
     *
     *  exp 3)
     *
     *    |--------------------|
     *    1                   100
     *    |------|
     *    1      11
     *       |------|
     *       2      12
     *          |--------|
     *          11       22
     *
     *
     *
     *
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        // edge
        if(intervals == null || intervals.length == 0){
            return 0;
        }

        int res = 0;
        /**
         *  SORT (in below order)
         *
         *  1) 1st element (small -> big)
         *  2) 2nd element (big -> small)
         */
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o2[1] - o1[1];
//                if(diff == 0){
//                    return o2[1] - o1[1];
//                }
                return diff;
            }
        });

        System.out.println(">>> (after sort) intervals = " );
        for(int[] x: intervals){
            System.out.println(">>> x[0] = " + x[0] + ", x[1] = " + x[1] );
        }

        Stack<int[]> st = new Stack<>();
        for(int[] x: intervals){
            //  case 1) st is empty
            if(st.isEmpty()){
                st.add(x);
            } else{
                // case 2) NOT overlap
                /**
                 *  |----| old
                 *           |----| new
                 *
                 */
                int[] prev = st.pop();
                if(prev[1] <= x[0]){
                    //st.add(x);
                    st.add(new int[] { Math.min(prev[0], x[0]), Math.max(prev[1], x[1]) } );
                }
                // case 3) OVERLAP, add `merge` interval
                else{
                    res += 1;
                    //st.add(new int[] { Math.min(prev[0], x[0]), Math.max(prev[1], x[1]) } );
                }
            }
        }

        return res;
    }

    // LC 1851
    // 4.59 - 5.06 pm
    public int[] minInterval(int[][] intervals, int[] queries) {
        return null;
    }

    // LC 50
    // 5.22 - 5.32 pm
    public double myPow(double x, int n) {
        // edge
        if(n == 0){
            return 1.1;
        }
        if(n == 1){
            return x;
        }
        double res = 1.0;
        boolean isNegativePower = n < 0;

        if(isNegativePower){
            n = -1 * n;
        }
        for(int i = 0; i < n; i++){
            res *= x;
        }

        return isNegativePower ? 1.0 / res : res;
    }

    // LC 1899
//    public boolean mergeTriplets(int[][] triplets, int[] target) {
//
//        return false;
//    }

    // LC 34
    // 2.54 - 3.10 pm
    /**
     *  IDEA 1) BRUTE FORCE + HASHMAP
     *   -> loop over nums, use hashmap,
     *      record indexes as val,
     *      then return min, max of it
     *
     *
     *  IDEA 2) binary search ???
     *
     *  -> since the array is `Sorted Array`
     *    ( ascending) (e.g. small -> big)
     *
     *
     */
    public int[] searchRange(int[] nums, int target) {

        int[] res = new int[]{-1, -1}; // default res

        // edge
        if(nums == null || nums.length == 0){
            return res;
        }
        if(nums.length == 1){
            if(nums[0] != target){
                return res;
            }
        }
        if(nums.length == 2){
            if(nums[0] != target &&  nums[1] != target){
                return res;
            }
            if(nums[0] == target &&  nums[1] == target){
                return new int[]{0,1};
            }
            else{
                if(nums[0] == target){
                    return new int[]{0,0};
                }else{
                    return new int[]{1,1};
                }
            }
        }

        // binary search ???
        int l = 0;
        int r = nums.length - 1;
        int mid = -1;
        boolean found = false;
        while(r >= l){
            mid = (l + r) / 2;
            if(nums[mid] == target){
                found = true;
                break; // if found a candidate, leave the while loop
            }
            if(nums[mid] > target){
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }

        // if found a `candidate`, we found the interval via below
        int subLeft = mid;
        int subRight = mid;

        if(!found){
            return res;
        }

        System.out.println(">>> mid = " + mid);

        while(subLeft > 0 && nums[subLeft] == target){
            subLeft -= 1;
        }

        while(subRight < nums.length - 1 && nums[subRight] == target){
            subRight += 1;
        }

        return new int[]{subLeft, subRight};
    }

    // LC 43
    // 3.48 - 3.58 pm
    /**
     *  IDEA 1) STRING OP
     *
     *   -> abc * def
     *   = (a * 100) * (def) + (b * 10) * (def) + (c * 1) * def
     *   = 100 * (a) * (def) +  10 * (b) * (def) +  1 * (c) * (def)
     *
     *   -> so we create a `single val * val` helper func
     *   -> then use above, build our solution
     *
     *
     *
     */
    public String multiply(String num1, String num2) {

        return null;
    }

    // LC 2013
    // 10.10 - 10.20 am
    // 10.39 - 10.45 am
    /**
     *  - Given a query point, counts the number
     *    of ways to choose three points from the data structure
     *    such that the three points and the
     *    query point form an axis-aligned square with positive area.
     *
     *
     *    An axis-aligned square is a square whose edges
     *    are all the same length and are either parallel
     *    or perpendicular to the x-axis and y-axis.
     *
     *
     */
    /**
     *   IDEA 1) HASH MAP
     *   -> use map records below info:
     *
     *      {
     *          x :  [ [x_1, y_1], [x_2, y_2], ... ] ,
     *          y :  [ [x_1, y_1], [x_2, y_2], ... ]
     *
     *      }
     *
     *
     */
    // 11.52 - 12.10 pm
    /**
     *  IDEA 1)
     *
     *   hash map + math + list
     *
     *
     *   -> use hashmap to maintain the x axis and its y coordinations
     *      { [x_1, y_1] : cnt_1, [x_2, y_2] : cnt_2, ....}
     *
     *   -> use list to the collected points ??
     *     -> [ [x_1, y_1], [x_2, y_2], .....  ]
     *
     *   -> and via math, we can know if the query (point)
     *      can form a validated square.
     *      -> `diagonal` points should have same lengh (in x, y direction)
     *      -> e.g.   dist(x1,x2) == dist(y1,y2) ???
     *
     *
     *      *----*  (x1,y1)
     *      |    |
     *      *----*
     *  (x2,y2)
     *
     *
     *
     */
    class DetectSquares {

        // attr
        Map<Integer[], Integer> pointCnt;
        List<Integer[]> points; // ?

        public DetectSquares() {
            this.pointCnt = new HashMap<>();
            this.points = new ArrayList<>();
        }

        public void add(int[] point) {
            Integer[] key = new Integer[]{point[0], point[1]};
            this.pointCnt.put(key, this.pointCnt.getOrDefault(key, 0) + 1);

            points.add(key);
        }

        public int count(int[] point) {
            int cnt = 0;
            // check
            int p_x = point[0];
            int p_y = point[1];
            //if(p_x )
            // case 1) invalid query point
            if(this.points.contains(point)){
                return 0; // ??
            }

            for(Integer[] key: this.pointCnt.keySet()){
                int x1 = key[0];
                int y1 = key[1];

                Integer[] coord1 = new Integer[]{x1, p_y};
                Integer[] coord2 = new Integer[]{p_x, y1};

                boolean condition1 = Math.abs(x1 - p_x) == Math.abs(y1 - p_y);
                boolean condition2 = this.pointCnt.containsKey(coord1) &&
                        this.pointCnt.containsKey(coord2);
                if(condition1 && condition2){
                    return this.pointCnt.get(coord1) * this.pointCnt.get(coord2);
                }
            }

            return cnt;
        }

    }

    /**
     * Your DetectSquares object will be instantiated and called as such:
     * DetectSquares obj = new DetectSquares();
     * obj.add(point);
     * int param_2 = obj.count(point);
     */


    // LC 1100
    /**
     * 1109. Corporate Flight Bookings
     *
     * There are n flights, and they are labeled from 1 to n.
     *
     * We have a list of flight bookings.  The i-th booking
     *
     * bookings[i] = [i, j, k] means that we booked k seats from
     *
     * flights labeled i to j inclusive.
     *
     * Return an array answer of length n, representing the number
     *
     * of seats booked on each flight in order of their label.
     *
     *
     *
     * Example 1:
     *
     * Input: bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
     * Output: [10,55,45,25,25]
     *
     *
     * Constraints:
     *
     * 1 <= bookings.length <= 20000
     * 1 <= bookings[i][0] <= bookings[i][1] <= n <= 20000
     * 1 <= bookings[i][2] <= 10000
     *
     */
    /**
     *
     * * bookings[i] = [i, j, k] means that we booked k seats from
     * * flights labeled i to j inclusive.
     *
     *   -> [i, j, k] :  k seats, from i -> j
     *
     *
     *   -> * Return an array answer of length n, representing the number
     *      * of seats booked on each flight in order of their label.
     *
     *
     *
     *
     *    IDEA: prefix sum
     *
     *
     *    exp 1)
     *
     *    输入：bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
     *    输出：[10,55,45,25,25]
     *
     *    -> presum =
     *          [10,10,0,0,0]
     *          [10,30,20,0,0]
     *          [10,55,45,25,25]
     *
     *
     *     so, for interval sum,
     *
     *     因此，answer = [10,55,45,25,25]
     *
     *
     *   exp 2)
     *
     *   输入：bookings = [[1,2,10],[2,2,15]], n = 2
     *   输出：[10,25]
     *
     *
     *    (for each iteration add presum and element)
     *    -> presum =
     *           [10,20]
     *           [10,35]
     *
     *
     *           [10,10]
     *           [10,25]
     *
     *   ( then, we get val[i] = presum[i] - presum[i-1]
     *    -> sum interval =
     *        [ 10, 25 ]
     *
     *
     *
     *
     */
    public int[] corpFlightBookings(int[][] bookings, int n) {
        // edge
        if(bookings == null || bookings.length == 0){
            return null;
        }
        if(n == 0){
           return new int[]{0}; // ??
        }
        // IDEA: prefix sum
        //List<Integer> prefixSum = new ArrayList<>();
        int[] preSum = new int[n];

        for(int[] b: bookings){
            //for(int i = 0;)
            int start = b[0];
            int end = b[1];
            int amount = b[2];
            for(int i = start-1; i < end; i++){
                preSum[i] += amount;
            }
        }

        System.out.println(">>> preSum = " + preSum);

        return preSum;
    }

    // LC 370
    /**
     * 370. Range Addition
     * Assume you have an array of length n initialized with all 0's and are given k update operations.
     *
     * Each operation is represented as a triplet: [startIndex, endIndex, inc] which increments each element of subarray A[startIndex ... endIndex] (startIndex and endIndex inclusive) with inc.
     *
     * Return the modified array after all k operations were executed.
     *
     * Example:
     *
     * Input: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
     * Output: [-2,0,3,5,3]
     * Explanation:
     *
     * Initial state:
     * [0,0,0,0,0]
     *
     * After applying operation [1,3,2]:
     * [0,2,2,2,0]
     *
     * After applying operation [2,4,3]:
     * [0,2,5,5,3]
     *
     * After applying operation [0,2,-2]:
     * [-2,0,3,5,3]
     *
     */
    public static int[] getModifiedArray(int length, int[][] updates) {
        // edge
        if(length == 0){
            return null;
        }
        if(updates == null || updates.length == 0){
            return new int[length]; // ???
        }
        // prefix sum
        int[] preSum = new int[length];
        for(int[] u: updates){
            int start = u[0];
            int end = u[1];
            int amount = u[2];
            for(int i = start; i < end; i++){
                preSum[i] += amount;
            }
        }

        return preSum;
    }


    // LC 598
    public int maxCount(int m, int n, int[][] ops) {

        return 0;
    }

    // LC 1094
    /**
     *
     * trip[i] = [num_passengers, start_location, end_location]
     *
     *
     * ->  Return true if and only if it is possible
     *     to pick up and drop off all passengers
     *     for all the given trips.
     *
     *
     *  IDEA 1)  `presum array`
     *
     *   -> keep mantaining  `pre sum of customer cnt`, and check if it exceeds capacity
     *      within the trip, if exceeds, return false directly
     *      till reach the `end`, return true
     *
     */
    // 10.52 - 11.10 am
    public boolean carPooling(int[][] trips, int capacity) {
        // edge
        if(trips == null || trips.length == 0){
            return true; // ?
        }
        if(trips.length == 1){
            if(trips[0][0] > capacity){
                return false;
            }
            return true;
        }

        int tripLen = 0;
        for(int[] t: trips){
            tripLen = Math.max(tripLen, t[2]);
        }

        //int preSum = 0;
        int[] pickUp = new int[tripLen];
        int[] dropOff = new int[tripLen];

        for(int i = 0; i < trips.length; i++){
            for(int j = trips[i][1]; j < trips[i][2]; j++){
                // pickUp
                pickUp[j] += trips[i][0];
            }
            // takeOff
            //takeOff[i] -= t[0];
            dropOff[trips[i][2]] -= trips[i][0];
        }

        System.out.println(">>> pickUp = " + pickUp);
        System.out.println(">>> dropOff = " + dropOff);

        // loop over `pickup`, `dropoff`
        for(int i = 0; i < pickUp.length; i++){
            if(pickUp[i] - dropOff[i] > capacity){
                return false;
            }
        }

        return true;
    }

    // LC 912
    /**
     *  912. Sort an Array
     * Given an array of integers nums, sort the array in ascending order.
     *
     *
     *
     * Example 1:
     *
     * Input: [5,2,3,1]
     * Output: [1,2,3,5]
     * Example 2:
     *
     * Input: [5,1,1,2,0,0]
     * Output: [0,0,1,1,2,5]
     *
     *
     * Note:
     *
     * 1 <= A.length <= 10000
     * -50000 <= A[i] <= 50000
     *
     *
     **/
    public int[] sortArray(int[] nums) {


        Integer[] nums_ = new Integer[nums.length];
        for(int i = 0; i < nums.length; i++){
            nums_[i] = nums[i];
        }

        // NOTE !!! below
        /**
         *  NOTE !!!
         *
         *
         *  In Java, you cannot directly use a Comparator with primitive
         *  types like int[] because Comparator works only with objects,
         *  not primitives. However, you can convert the int[] to an Integer[]
         *  and then use a comparator.
         *
         * Here's an example of how to sort an int[] using a
         * Comparator by converting it to Integer[]:
         *
         *
         *
         */
        Arrays.sort(nums_, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return a - b;  // This will sort in descending order
            }
        });

        for(int i = 0; i < nums_.length; i++){
            nums[i] = nums_[i];  // ???
        }

        return nums;
    }

    // LC 75
    // 10.28 - 10.38 am
    public void sortColors(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return;
        }

        Integer[] nums2 = new Integer[nums.length];
        for(int i = 0; i < nums.length; i++){
            nums2[i] = nums[i];
        }

        // sort on nums2
        Arrays.sort(nums2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });


        for(int i = 0; i < nums2.length; i++){
            nums[i] = nums2[i];
        }

        return;
    }

    // LC 304
    // 11.44 - 12.10 pm
    /**
     *  -> Given a 2D matrix matrix, find the sum of the
     *  elements inside the rectangle defined by its upper
     *  left corner (row1, col1) and lower right corner (row2, col2).
     *
     *  -> get sum of matrix from `left corner` to `right corner`
     *     -> preSum within (row1, col1) and (row2, col2)
     *
     *
     *  -> IDEA 1) GREEDY + PRESUM
     *    -> get pre sum and subtract the no needed `corner`
     *
     *
     */
    class NumMatrix {

        // attr
        int[][] matrix;
        //List<List<Integer>> preSumMatrix; // ??
        int[][] preSumMatrix;
        int preSum;

        public NumMatrix(int[][] matrix) {

            this.matrix = matrix;
            this.preSumMatrix = new int[matrix.length + 1][matrix[0].length + 1];
            this.preSum = 0;

            // get Prefix sum
            int l = matrix.length;
            int w = matrix[0].length;
            // note below ????

            for(int i = 0; i < l+1; i++){
                for(int j = 0; j < w+1; j++){
                    if(i > l || j > w){
                        this.preSumMatrix[i][j] = 0; // ???
                    }else{
                        this.preSum += this.matrix[i][j];
                        this.preSumMatrix[i][j] = this.preSum;
                    }
                }
            }

        }

        public int sumRegion(int row1, int col1, int row2, int col2) {

            /**
             *  left corner (row1, col1)
             *        X----*
             *        |    |
             *        *----X
             *          right corner (row2, col2).
             *
             */

            int res = this.preSumMatrix[row2][col2] - this.preSumMatrix[row1][col1];
            // add the `over subtracted` val back
            //res += this

            return res;
        }

    }

    // LC 128
    /**
     *   IDEA 1) SORT + SLIDING WINDOW
     *
     *
     */
    // 10.30 - 10.40 am
    public int longestConsecutive(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 1;
        }
        HashSet<Integer> set = new HashSet<>();
        List<Integer> nums2 = new ArrayList<>();

        for(int i = 0; i < nums.length; i++){
            if(!set.contains(nums[i])){
                set.add(nums[i]);
                nums2.add(nums[i]);
            }
        }

        // sort (ascending) (small -> big)
        Collections.sort(nums2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        System.out.println(">>> nums2 = " + nums2);
        for(int x: nums2){
            System.out.println(" x= " + x);
        }

        int res = 1;
        int l = 0;
        int r = 1;
        //int prev = -1;
        while (r < nums2.size() && l < nums2.size() ){
            //prev = nums[l];
            System.out.println(">>> l = " + l + ", r = " + r + ", res = " + res);
            if(nums2.get(r) == nums2.get(r-1) + 1){
                res = Math.max(res, r  - l  + 1);
            }else{
                // move l to r
                l = r;
            }
            r += 1;
        }

        return res;
    }

    // LC 122
    // 11.04 - 11.20 am
    /**
     *
     *  -> Design an algorithm to find the maximum profit.
     *  You may complete as many transactions as you like
     *  (i.e., buy one and sell one share of the stock multiple times).
     *
     *
     *
     *    IDEA 1) : GREEDY
     *
     *    IDEA 1) : dp ???
     *
     *
     *  exp 1)
     *    Input: [7,1,5,3,6,4]
     *    Output: 7
     *   Explanation: Buy on day 2 (price = 1)
     *               and sell on day 3 (price = 5), profit = 5-1 = 4.
     *              Then buy on day 4 (price = 3)
     *              and sell on day 5 (price = 6), profit = 6-3 = 3.
     *
     *
     *     [7,1,5,3,6,4]
     *      x
     *
     *             1
     *           5  3   6  4
     *         6    6
     *
     *
     *
     *
     *
     *
     */
    public int maxProfit_(int[] prices) {

        return 0;
    }

    // LC 229
    // 10.09 - 10.19 am
    public List<Integer> majorityElement(int[] nums) {
        List<Integer> res = new ArrayList<>();
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }
        if(nums.length == 1){
            res.add(nums[0]);
            return res;
        }
        // hashmap
        Map<Integer, Integer> map = new HashMap<>();
        int cnt = 0;
        for(int n: nums){
            cnt += 1;
            map.put(n, map.getOrDefault(n, 0)+1);
        }

        System.out.println(">>> map = " + map + ", cnt = " + cnt + " cnt / 3  = " + cnt / 3);

        for(int k: map.keySet()){
            if(map.get(k) > cnt / 3){
                res.add(k);
            }
        }

        return res;
    }

    // LC 560
    // 10.19 - 10.29 am
    /**
     *  560. Subarray Sum Equals K
     * Description
     * Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.
     *
     * A subarray is a contiguous non-empty sequence of elements within an array.
     *
     *
     *
     * Example 1:
     *
     * Input: nums = [1,1,1], k = 2
     * Output: 2
     * Example 2:
     *
     * Input: nums = [1,2,3], k = 3
     * Output: 2
     *
     *
     * Constraints:
     *
     * 1 <= nums.length <= 2 * 104
     * -1000 <= nums[i] <= 1000
     * -107 <= k <= 107
     *
     *
     *
     */
    /**
     *  IDEA 1) SLIDING WINDOW + HASHMAP ??
     *
     *
     */

    public int subarraySum(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            if(k != 0){
                return -1; // ?
            }
            if(k == 0){
                return 0;
            }
        }
        if(nums.length == 1){
            if(nums[0] == k){
                return 1;
            }
            return -1; // ??
        }

        int cnt = 0;
        // sliding window
        int l = 0;
        int r = 0;
        // ???
        while( r < nums.length ){
            // cur
            int[] tmp = Arrays.copyOfRange(nums, l, r);
            //int tmpSum = getSum(tmp);

            System.out.println(">>> l = " + l + ", r = " + r + " tmp = " + tmp);

            // case 1) > k
            while (getSum(tmp) > k){
                l += 1;
            }

            // case 2)  == k
            if(getSum(tmp) == k){
                cnt += 1;
                r += 1;
            }
            // case 3) < k
            else{
                r += 1;
            }

        }

        return cnt;
    }

     private int getSum(int[] input){
        int sum = 0;
        for(int x: input){
            sum += x;
        }
        return sum;
    }

    // LC 41
    // 11.18 - 11.28 am
    /**
     *  IDEA 1) ARRAY OP
     *
     */
    public int firstMissingPositive(int[] nums) {

        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }

        // {key : cnt}
        int maxVal = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for(int n: nums){
            maxVal = Math.max(maxVal, n);
            map.put(n, map.getOrDefault(n,0) + 1);
        }


        /**
         *  exp 1)
         *     Input: [1,2,0]
         *     Output: 3
         *
         *
         */
        int start = 1;
        while(start <= maxVal){
            if(!map.containsKey(start)){
                return start;
            }
            start += 1;
        }

        return start; // ??
    }

    // LC 76
    // 11.49 - 12.10 PM
    /**
     *  IDEA 1) HASHMAP + SLIDING WINDOW
     *
     *     - 2 hashmap; s_cur_map, t_map
     *     - `t_map` key cnt
     *     - `s_cur_map` key cnt
     *
     *     - then within sliding window
     *        - we keep checking
     *           if s_cur_map == t_map
     *              if yes, appdend res
     *              and use `while` loop
     *                 - keep moving `left pointer` till s_cur_map != t_map
     *           else:
     *              move `right pointer`
     *
     */
    public String minWindow_(String s, String t) {
        // edge
        if(s == null && t == null){
            return null;
        }

        // init 2 map
        Map<String, Integer> t_map = new HashMap<>();
        Map<String, Integer> s_tmp_map = new HashMap<>();

        // init cnt
        int t_key_cnt = t_map.keySet().size();
        int s_key_cnt = 0;

        // init cache
        List<String> cache = new ArrayList<>();

        // update map
        for(String x: t.split("")){
            t_map.put(x, t_map.getOrDefault(x, 0) + 1);
        }

        String[] s_arr = s.split("");
        StringBuilder sb = new StringBuilder();
        int l = 0;
        for(int r = 0; r < s_arr.length; r++){

            if(t_key_cnt == s_key_cnt){

                cache.add(sb.toString());
                // move left pointer, till it `CAN'T` meet the requirement

               // while ()
            }

        }

        return null;
    }

    // LC 680
    // 9.42 - 9.52 am
    public boolean validPalindrome(String s) {
        // edge
        if(s == null || s.length() == 0){
            return true;
        }
        for(int i = 0; i < s.length(); i++){
            if(isPalindrome(s, i)){
                return true;
            }
        }

        return false;
    }

    public boolean isPalindrome(String input, int i){
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j < input.split("").length; j++){
            if(j != i){
                sb.append(input.charAt(j));
            }
        }
        String x = sb.toString();
        System.out.println(">>> i = " + i + ", input = " + input + ", x = " + x);


        int l = 0;
        int r = x.length() -1;
        boolean res = true;
        while(r > l){
            if (x.charAt(l) != x.charAt(r)){
                return false;
            }
            r -= 1;
            l += 1;
        }

        return res;
    }

    // LC 1769
    public String mergeAlternately(String word1, String word2) {
        // edge
        if(word1 == null && word2 == null){
            return null;
        }
        if(word1 == null || word2 == null){
            if(word1 == null){
                return word2;
            }
            return word1;
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < word1.length() && i < word2.length()){
            sb.append(word1.charAt(i));
            sb.append(word2.charAt(i));

            i += 1;
        }

        if(i < word1.length()){
            for(int j = i; j < word1.length(); j++){
                sb.append(word1.charAt(j));
            }
        }

        if(i < word2.length()){
            for(int j = i; j < word2.length(); j++){
                sb.append(word2.charAt(j));
            }
        }

        return sb.toString();
    }


    // LC 88
    // 10.38 - 10.48 am
    /**
     * Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
     *  -> Output: [1,2,2,3,5,6]
     *
     *
     *  n1 = [1,2,2,5,3,0]
     *        1 2 3 4
     *
     *  n2 = [2.5.6]
     *        1 2 3
     *
     *
     *   [1,
     *
     *
     *
     *
     *
     *
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {

    }

    // Lc 189
    // 11.05 - 11.15 am
    public void rotate(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return;
        }

        // ?? can use below ??
        Deque<Integer> dq = new LinkedList<>();
       // List<Integer> list = new ArrayList<>();
        for(int n: nums){
          //  list.add(n);
            dq.add(n);
        }

        int i = 0;
        for(i = 0; i < k; i++){
           // int last = nums[nums.length - 1];
//           int last = list.get(list.size()-1);
//           list.remove(list.size()-1);
//           list.add(0, last);

          dq.addFirst(dq.pollLast()); // ???
        }

        System.out.println(">>> dq = " + dq);

        int j = 0;
        while(!dq.isEmpty()){
            nums[j] = dq.pollFirst();
            j += 1;
        }
    }

    // LC 881
    // 4.32 - 4.50 pm
    /**
     *   1. people[i] is the weight of the ith person
     *
     *   2. `infinite` number of boats
     *        where each boat can
     *        carry a maximum weight of limit
     *
     *   3. Each boat carries at most two people
     *      at the same time, provided the sum
     *      of the weight of those people is at most limit
     *
     *  -> Return the minimum number of
     *     boats to carry every given person.
     *
     *
     *   -> hashmap ??
     *
     *   -> {val : cnt}
     *
     *  exp 1)
     *     map = {1: 1, 2: 1}
     *     limit = 3
     *     -> res = 1
     *
     *  exp 2)
     *    map = {3: 1, 2:2, 1: 1}
     *    limit = 3
     *      map = {2:2, 1: 1}, res =1
     *      map = {2:1}, res = 2
     *      map = {}, res = 3
     *
     *
     *  exp 3)
     *     map = {3:2, 4: 1, 5:1}
     *     limit = 5
     *
     *       map = {3:1, 4: 1, 5:1}, res = 1
     *       map = {4: 1, 5:1}, res = 2
     *       map = {5:1}, res = 3
     *       map = ,  res = 4
     *
     *
     */
    public int numRescueBoats(int[] people, int limit) {
        // edge
        if(limit == 0){
            return -1; // ?? should NOT happen
        }
        if(people == null || people.length == 0){
            return 0; // ?
        }
        if(people.length == 1){
            if (limit >= people[0]){
                return 1;
            }
            return (limit / people[0]) + 1; // ?
        }

        // init map: {val: cnt}
        Map<Integer, Integer> map = new HashMap<>();
        for(int p: people){
            map.put(p, map.getOrDefault(p, 0) + 1);
        }

        System.out.println(">>> map = " + map);

        int res = 0;

        while(!map.isEmpty()){
            Set<Integer> set = map.keySet();
            int tmp = 0;
            for(int k: set){
                System.out.println(">>> tmp = " + tmp + ", res = " + res + ", map = " + map);
                tmp += k;
                // ???
                if(k > limit){
                    tmp = 0;
                    res += 1;
                    continue;
                }
                // ??
                map.put(k, map.get(k) - 1);
                if(map.get(k) == 0){
                    map.remove(k);
                }
            }
        }

        return res;
    }

    // LC 42
    // 5.27 pm - 5.37 pm
    /**
     *  IDEA 2) 2 POINTERS + leftMax + rightMax
     *
     *  -> amount_of_water = min(left, right) - height[i]
     *
     *  -> maintain 3 arrays:
     *
     *    [left_max, ...]
     *    [right_max, ...]
     *    [min(left_max, right_max), ...]
     *
     *    and get 4 th array:
     *
     *    [ min(left_max, right_max) - height[i], ...]
     *
     *
     *  -> NOTE : if min(left_max, right_max) - height[i] < 0, we make it as 0
     *         -> then res will be sum over [ min(left_max, right_max) - height[i], ...] array
     *
     */
    // 6.15 - 6.30 pm
    public int trap(int[] height) {
        // edge
        if(height == null || height.length == 0){
            return 0;
        }
        if(height.length == 1){
            return 0; // ?
        }
        // get 3 array
        int[] max_at_left = new int[height.length];
        int[] max_at_right = new int[height.length];
        int[] min_left_right = new int[height.length];

        int leftMaxTillNow = 0;
        for(int i = 1; i < height.length; i++){
            max_at_left[i] = leftMaxTillNow;
            leftMaxTillNow = Math.max(leftMaxTillNow, height[i]);
        }

        int rightMaxTillNow = height[height.length-1];
        // ??
        for(int i = height.length-2; i > 0; i--){
            max_at_right[i] = rightMaxTillNow;
            rightMaxTillNow = Math.max(rightMaxTillNow, height[i]);
        }

        for(int i = 0; i < height.length; i++){
            min_left_right[i] = Math.min(max_at_left[i], max_at_right[i]);
        }

        // get 4th array
        int[] min_left_right_height_diff = new int[height.length];
        for(int i = 0; i < height.length; i++){
            int diff = min_left_right[i] - height[i];
            if (diff > 0){
                min_left_right_height_diff[i] = diff;
            }
        }

        System.out.println("max_at_left = " + Arrays.toString(max_at_left));
        System.out.println("max_at_right = " + Arrays.toString(max_at_right));
        System.out.println("min_left_right = " + Arrays.toString(min_left_right));
        System.out.println("min_left_right_height_diff = " + Arrays.toString(min_left_right_height_diff));

        int res = 0;
        for(int i = 0; i < height.length; i++){
            res += min_left_right_height_diff[i];
        }

        return res;
    }

    // LC 219
    // 12.30 - 12.40 pm
    /**
     * -> return true if there are two distinct indices
     *    i and j in the array such that
     *    nums[i] == nums[j] and abs(i - j) <= k.
     *
     * IDEA 1) HASH TABLE
     */
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return false; // ??
        }
        if(nums.length == 1){
            return false;
        }
        // init
        // {val: [idx1, idx2, ...]
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int i = 0; i < nums.length; i++){
            List<Integer> cur = map.getOrDefault(nums[i], new ArrayList<>());
            cur.add(i);
            map.put(nums[i], cur);
        }

        System.out.println(">>> map = " + map);

        for(int i = 0; i < nums.length; i++){
            int val = nums[i];
            if(map.containsKey(val)){
                // TODO: optimize below
                for(int x: map.get(val)){
                    if(Math.abs(x - i) <= k && x != i){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // LC 424
    // 12.54 pm - 1.10 pm
    /**
     *
     *  You are given a string s and an integer k.
     *    You can choose any character of the string
     *    and change it to any other uppercase English character.
     *    You can perform this operation at most k times
     *
     *  -> Return the length of the longest substring containing
     *     the same letter you can get after performing the above operations.
     *
     *
     *   IDEA 1) HASHMAP + 2 pointers
     *    -> use map track the val, and its idx,
     *    -> calculate the `different element that with MOST count`
     *    -> so can get the `longest substring` at the moment
     *    -> maintain the `global longest substring` and return it as final res
     *
     */
    public int characterReplacement_1(String s, int k) {
        // edge
        if(s == null || s.length() == 0){
            return 0; // ?
        }
        if(s.length() == 1){
            return 1; // ?
        }
        if(s.length() < k){
            return s.length(); // ?
        }

        int res = 1;
        // map: {val: cnt}
        Map<Character, Integer> map = new HashMap<>();
        for(int l = 0; l < s.length(); l++){
            for(int r = l; r <  s.length(); r++){
                if(getMapSecondMaxVal(map) > k){
                    break; // ??
                }
                Character val = s.charAt(r);
                map.put(s.charAt(r), map.getOrDefault(val, 0) + 1);
                res = Math.max(res, r - l + 1);
            }
        }

        return res;
    }

    public int getMapSecondMaxVal(Map<Character, Integer> map){
        if(map.isEmpty()){
            return 0;
        }
        List<Integer> cache = new ArrayList<>();
        for(Integer val: map.values()){
            cache.add(val);
        }
        Collections.sort(cache, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });
        if (cache.size() < 2){
            return cache.get(cache.size()-1); // ?
        }
        return cache.get(1); // 2nd biggest val
    }

    // LC 567
    // 1.37 - 1.47 pm
    /**
     *
     *  -> A permutation is a rearrangement
     *     of all the characters of a string.
     *
     *
     *   IDEA 1) HASHMAP + SLIDING WINDOW
     *
     *    1. create 2 map : s1_map, s2_tmp_map
     *       (val: cnt)
     *       add val, cnt to s1_map
     *    2. use sliding window (l, r)
     *       check if `s2_tmp_map` `can contain s1_map` (key same, s1_cnt >= s2_cnt)
     *          - if found, return true directly
     *    3. return false
     */
    public boolean checkInclusion_1(String s1, String s2) {
        // edge
        if(s1 == null && s2 == null){
            return true;
        }
        if(s1 == null || s2 == null){
            if(s2 == null){
                return false;
            }
            return true;
        }
        if(s1.equals(s2)){
            return true;
        }

        // map : {val: cnt}
        Map<String, Integer> s1_map = new HashMap<>();
        Map<String, Integer> s2_tmp_map = new HashMap<>();

        for(String x: s1.split("")){
            s1_map.put(x, s1_map.getOrDefault(x, 0) + 1);
        }

        // 2 pointers
        // NOTE !!! we move r (right pointer), and ONLY move left pointer if the `condition` is NOT met)
        int l = 0;
        for(int r = 0; r < s2.length(); r++){

            // NOTE !!! val below is from `right` idx
            String val = String.valueOf(s2.charAt(r));
            s2_tmp_map.put(val, s2_tmp_map.getOrDefault(val, 0) + 1);

            if(s1_map.equals(s2_tmp_map)){
                return true;
            }

            // NOTE !!! below
            if(r - l + 1 >= s1.length()){

                // UPDATE MAP
                // || !s1_map.containsKey(val)
                //s2_tmp_map.put(val, s2_tmp_map.get(val)-1)

                String leftVal = String.valueOf(s2.charAt(l));
                s2_tmp_map.put(leftVal, s2_tmp_map.get(leftVal)-1);

                if(s2_tmp_map.get(leftVal) == 0){
                    s2_tmp_map.remove(leftVal);
                }

                l += 1; // ??
            }
        }

        return false;
    }


//    public boolean checkInclusion_1(String s1, String s2) {
//        // edge
//        if(s1 == null && s2 == null){
//            return true;
//        }
//        if(s1 == null || s2 == null){
//            if(s2 == null){
//                return false;
//            }
//            return true;
//        }
//        if(s1.equals(s2)){
//            return true;
//        }
//
//        // map : {val: cnt}
//        Map<String, Integer> s1_map = new HashMap<>();
//        Map<String, Integer> s2_tmp_map = new HashMap<>();
//
//        for(String x: s1.split("")){
//            s1_map.put(x, s1_map.getOrDefault(x, 0)+1);
//        }
//
//        // 2 pointers
//        for(int l = 0; l < s2.length(); l++){
//
//            int r = l; /// ??
//            String val = String.valueOf(s2.charAt(l));
//            while(r - l + 1 <= s1.length() && s1.contains(String.valueOf(s2.charAt(l)))){
//                // ??
//                if(s1_map.equals(s2_tmp_map) || isContain(s1_map, s2_tmp_map)){
//                    return true;
//                }
//                s2_tmp_map.put(val, s2_tmp_map.getOrDefault(val, 0)+1);
//                if(s2_tmp_map.get(val) > s1_map.get(val)){
//                    break; // ??
//                }
//
//                r += 1;
//            }
//        }
//
//        return false;
//    }

    public boolean isContain(Map<String, Integer> s1_map , Map<String, Integer> s2_tmp_map){

        if(s1_map.isEmpty()){
            return false;
        }

        for(String k: s1_map.keySet()){
            if(s1_map.get(k) > s2_tmp_map.getOrDefault(k, 0)){
                return false;
            }
        }

        return true;
    }

    // LC 209
    // 10.09 - 10.19 am
    /**
     *  IDEA 1) SLIDING WINDOW
     *
     *   exp 1)  s = 7, nums = [2,3,1,2,4,3]
     *
     *   nums = [2,3,1,2,4,3]
     *           l r r r        res = 4
     *
     *  nums = [2,3,1,2,4,3]
     *          l l l   r     res = 3
     *
     *  nums = [2,3,1,2,4,3]  res = 2
     *                l r r
     *                  l
     */

    public int minSubArrayLen(int target, int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            if(target == 0){
                return 0; // ??
            }
            return -1; // should not happen ??
        }
        if(nums.length == 1){
            if(nums[0] == target){
                return 1;
            }
            return -1; // ??
        }

        // sliding window
        int res = Integer.MAX_VALUE; //Integer.MAX_VALUE; // ???
        int l = 0;
        int curSum = 0;

        /**
         *   s = 7, nums = [2,3,1,2,4,3]
         *
         *  [2,3,1,2,4,3]
         *   l                 cumsum = 2, res = 0
         *   r
         *
         *   [2,3,1,2,4,3]
         *    l r           cumsum = 5, res = 0
         *
         *   [2,3,1,2,4,3]
         *    l   r        cumsum = 6, res = 0
         *
         *  [2,3,1,2,4,3]
         *   l l   r     cumsum = 8, res = 0, cumsum = 6, res = 0
         *
         *
         *  [2,3,1,2,4,3]
         *     l l   r     cumsum = 7, res = 3
         *
         *  [2,3,1,2,4,3]
         *       l l l r    cumsum = 10, res = 3, cumsum = 9, res = 3, cumsum = 7, res = 2
         *
         */

        for(int r = 0; r < nums.length; r++){
            //int curSum = 0;
            //int l = r;
            curSum += nums[r];

            while(curSum >= target && l < r){
                //curSum -= nums[l];
                res = Math.min(res, r - l + 1);
                curSum -= nums[l];
                l += 1;
            }
//            if(curSum == target){
//                res = Math.min(res, r - l + 1);
//            }

        }

        return res == Integer.MAX_VALUE ? 0 : res;
    }

    // LC 658
    // 11.00 am - 11.20 am
    /**
     *
     * Given a sorted array,
     * two integers k and x,
     *
     * -> find the k closest elements to x in the array.
     *
     * `k closest elements`  to x
     *
     *
     *   IDEA 1) LINEAR / BINARY SEARCH + sliding window
     *
     *    -> 1. find the `closest` element
     *       2. sliding window (and collect elements) till `k` is reached
     *       3. return res
     *
     */
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        List<Integer> res = new ArrayList<>();
        // edge
        if(arr == null || arr.length == 0){
            return null; // ?
        }
        if(arr.length == 1){
            res.add(arr[0]);
            return res; // ??
        }

        int closest = Integer.MAX_VALUE; // ??
        int closestIdx = -1; // ??
        for(int i = 0; i < arr.length; i++){
            int a = arr[i];
            int diff1 = Math.abs(closest - x);
            int diff2 = Math.abs(a - x);
            if(diff2 < diff1){
                closest = a;
                closestIdx = i;
            }
        }

        System.out.println(">>> closest = " + closest + ", closestIdx = " + closestIdx);

        //if(arr.c)

        int l = closestIdx - 1;
        int r = closestIdx + 1;
        //System.out.println(">>> l = " + l + ", r = " + r);

        while(res.size() < k && l > 0 && r < arr.length){
            System.out.println(">>> l = " + l + ", r = " + r + ", res = " + res);
            res.add(arr[l]);
            // ???
            if(res.size() == k){
                break;
            }
            res.add(arr[r]);
            l -= 1;
            r += 1;
        }

        // re-order (small -> big)
        Collections.sort(res, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        return res;
    }

    // LC 76
    // 10.22 - 10.32 am
    /**
     *  IDEA 1) HASHMAP + SLIDING WINDOW
     *
     */
    public String minWindow_1(String s, String t) {
        // edge
        if(s == null && t == null){
            return null;
        }
        if(t == null){
            return null;
        }
        if(s.equals(t)){
            return t; // ???
        }

        // map
        // { val : cnt}
        Map<Character, Integer> t_map = new HashMap<>();
        Map<Character, Integer> s_cur_map = new HashMap<>();

        for(int i = 0; i < t.length(); i++){
            t_map.put(t.charAt(i), t_map.getOrDefault(t.charAt(i), 0) + 1);
        }

        String res = "";
        int resLen = Integer.MAX_VALUE;

        // sliding window
        int l = 0;
        for(int r = 0; r < s.length(); r++){

           // String tmp_str = "";
            StringBuilder tmp_sb = new StringBuilder();
            tmp_sb.append(s.charAt(r));
            s_cur_map.put(s.charAt(r), s_cur_map.getOrDefault(s.charAt(r), 0) + 1);

            // check if map is equal or `overhead`
            while(isValidWindow(t_map, s_cur_map)){
                // move left pointer to `make sub str smaller`
                if(tmp_sb.toString().length() < res.length()){
                    res = tmp_sb.toString();
                }

                tmp_sb.deleteCharAt(0);
                l += 1;
                s_cur_map.put(s.charAt(l), s_cur_map.get(s.charAt(l)) - 1);
                // necessary ??
                if(s_cur_map.get(s.charAt(l)) == 0){
                    s_cur_map.remove(s.charAt(l));
                }

            }
        }

        return res;
    }

    public boolean isValidWindow(Map<Character, Integer> t_map, Map<Character, Integer> s_cur_map){
        if(s_cur_map.isEmpty()){
            return false;
        }
        for(Character k: t_map.keySet()){
            if(s_cur_map.get(k) < t_map.get(k)){
                return false;
            }
        }
        return true;
    }

    // LC 239
    // 11.02 - 11.20 am
    /**
     *   IDEA 1) BRUTE FORCE
     *   IDEA 2) PQ
     *   IDEA 3) deque + monopoly queue (increasing)
     *
     */
    // IDEA 2) PQ
    public int[] maxSlidingWindow(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return null; // ??
        }
        if(nums.length == k){
            int res = 0;
            for(int x: nums){
                res = Math.max(x, res);
            }
            return new int[]{res};
        }

        // BIG PQ (big -> small)
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        int curMax = -1 * Integer.MAX_VALUE;
        List<Integer> collected = new ArrayList<>();
        // init
        for(int i = 0; i < k; i++){
            curMax = Math.max(curMax, nums[i]);
            pq.add(nums[i]);
        }
        collected.add(curMax);

        int l = 0;
        // ???
        for(int r = k; r < nums.length - k; r++){
            pq.add(nums[r]);
            //pq.re
            for(int i = 0; i < pq.size(); i++){
                int tmp = pq.poll();
                if(tmp != nums[l]){
                    pq.add(nums[r]);
                }
            }

            l += 1;
        }

        int[] res = new int[collected.size()];
        for(int i = 0; i < collected.size(); i++){
            res[i] = collected.get(i);
        }

        return res;
    }

    // LC 225
    // 10.11 - 10.21 am
    /**
     *  LC 225  Implement Stack using Queues
     *
     *
     *  stack : FILO
     *
     *
     */
    class MyStack {

        // attr
        Deque<Integer> q;
        int size;

        public MyStack() {
            this.q = new LinkedList<>();
            //this.size = 0;
        }

        public void push(int x) {
            this.q.addFirst(x);
            //this.size += 1;
        }

        public int pop() {
            if(!this.q.isEmpty()){
                return this.q.pollFirst();
            }
            return 0; // ???
        }

        public int top() {
            return this.q.peekFirst();
        }

        public boolean empty() {
            return this.q.isEmpty();
        }
    }

    // LC 232
    // 10.48 - 10.58 am
    /**
     * Implement Queue using Stacks
     *
     * queue: FIFO
     *
     * stack : LIFO
     *
     */
    class MyQueue {

        // attr
        Stack<Integer> st;

        public MyQueue() {
            this.st = new Stack<>();
        }

        public void push(int x) {
            if(!this.st.isEmpty()){
                List<Integer> cache = new ArrayList<>();
                for(int i = 0; i < this.st.size(); i++){
                    cache.add(this.st.pop());
                }

                // reverse add back to st
                for(int i = cache.size() - 1; i >= 0; i--){
                    this.st.add(cache.get(i));
                }
            }
        }

        public int pop() {
            if(!this.st.isEmpty()){
                return this.st.pop(); // if the ordering is in `queue` style
            }
            return 0;
        }

        public int peek() {
            if(!this.st.isEmpty()){
                return this.st.peek(); // if the ordering is in `queue` style
            }
            return 0;
        }

        public boolean empty() {
            return this.st.isEmpty();
        }
    }


}
