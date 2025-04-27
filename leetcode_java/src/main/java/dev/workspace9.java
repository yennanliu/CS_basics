package dev;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.Heap.SingleThreadedCPU;
import LeetCodeJava.Trie.DesignAddAndSearchWordsDataStructure;
import com.sun.org.apache.bcel.internal.generic.IINC;
import com.sun.org.apache.bcel.internal.generic.INEG;
import com.sun.org.apache.bcel.internal.generic.PUSH;

import java.util.*;

public class workspace9 {


    // LC 22
    // 10.13 - 10.23 am
    /**
     *  IDEA 1) BRUTE FORCE
     *  IDEA 2) BACKTRACK ???
     *
     *     - backtrack get all possible candidates
     *     - collect the validated ones
     *
     */
    // ???
    List<String> collected1 = new ArrayList<>();
    public List<String> generateParenthesis_1(int n) {
        List<String> res = new ArrayList<>();
        // edge
        if(n == 0){
            return res;
        }
        if(n == 1){
            res.add("()");
            return res;
        }

        // backtrack
        backtrackHelper(n, new ArrayList<>());

        // validate
        for(String c: collected1){
            if(validParentheses(c)){
                res.add(c);
            }
        }

        System.out.println(">>> collected1 = " + collected1);
        System.out.println(">>> res = " + res);

        return res;
    }

    public void backtrackHelper(int n, List<String> cache){
        int size = cache.size(); //sb.toString().length();
        if(size == n * 2){
            // ???
//            // NOTE !!! init new list via below
//            res.add(new ArrayList<>(cur));

            List<String> newCache = new ArrayList<>(cache);
            collected1.add(newCache.toString()); // ???
            return;
        }
        if(size > n * 2){
            //sb = new StringBuilder(); // ??
            return; // ??
        }
        String[] src = new String[]{"(", ")"};
        for(int i = 0; i < src.length; i++){
            cache.add(src[i]);
            backtrackHelper(n, cache);
            // undo
            cache.remove(cache.size()-1); // ??
        }
    }

    public boolean validParentheses(String input){
//        int l = 0;
//        int r = input.length() - 1;
        Queue<String> q = new LinkedList<>();
        //Map<String, String> map = new HashMap<>();
        //map.put("(", ")");
        for(String x: input.split("")){
            if(q.isEmpty() && x.equals(")")){
                return false;
            }
            else if(x.equals("(")){
                q.add(x);
            }else{
                if(q.equals(q.poll())){
                    return true;
                }
                return false;
            }
        }

        return true;
    }

    // ------------

    List<String> resParenthesis = new ArrayList<>();

    public List<String> generateParenthesis(int n) {
        // List<String> res = new ArrayList<>();
        // edge
        if (n == 0) {
            return resParenthesis;
        }
        if (n == 1) {
            resParenthesis.add("()");
            return resParenthesis;
        }

        // backtrack
        backtrack(n, new StringBuilder());
        System.out.println(">>> resParenthesis = " + resParenthesis);
        return resParenthesis;
    }

    private void backtrack(int n, StringBuilder cur) {
        String[] word = new String[] { "(", ")" };
        String curStr = cur.toString();
        if (curStr.length() == n * 2) {
            // validate
            if (isValidParenthesis(curStr)) {
                resParenthesis.add(curStr);
            }
            return;
        }
        if (curStr.length() > n * 2) {
            return;
        }
        for (int i = 0; i < word.length; i++) {
            String w = word[i];
            // cur.add(w);
            cur.append(w);
            backtrack(n, cur);
            // undo
            //idx -= 1;
            cur.deleteCharAt(cur.length() - 1);
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
                return false; // If balance goes negative, it's invalid
            }
        }
        return balance == 0; // Valid if balance ends up being 0
    }

    // LC 739
    // 11.05 - 11.15 am
    /**
     *  IDEA 1) MONOPOLY STACK (??
     *       (increasing stack)
     *
     *   1. maintain an `increasing stack` that
     *      save element from smallest to biggest (small -> big)
     *
     *   2. iterate elements, pop, and record the `diff` to res
     *      - while the current temp is bigger than `top temp` in stack,
     *        we keep `popping` top stack element, and record the `date diff`
     *      - then save current temp to stack
     *
     *
     *   3. return res
     *
     *
     */
    public int[] dailyTemperatures(int[] temperatures) {
        // edge
        if(temperatures == null || temperatures.length == 0){
            return null; // ?
        }
        if(temperatures.length == 1){
            return new int[]{0}; // ?
        }
        // init
        // res init value should be all 0 ???
        int[] res = new int[temperatures.length];

        // stack : [ [val_1, idx_1], [val_x, idx_2], ... ]
        Stack<Integer[]> st = new Stack<>();

        for(int i = 0; i < temperatures.length; i++){

            int curTemp = temperatures[i];
            System.out.println(">>> stack = " + String.valueOf(st) + ", curTemp = " + curTemp + ", res = " + res);

            while(!st.isEmpty() && curTemp > st.peek()[0]){
                Integer[] tmp =  st.pop();
                int prevTemp = tmp[0];
                int prevTempIdx = tmp[1];
                //temperatures
                res[prevTempIdx] = i - prevTempIdx;
            }

            st.add(new Integer[]{curTemp, i});

        }

        return res;
    }


    // LC 2104
    // 11.41 - 11.51 am
    /**
     *  IDEA 1) 2 for loop ??
     *  IDEA 2) MONO STACK ???
     *
     *
     */

    //IDEA 1) 2 for loop ??
    public long subArrayRanges(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0];
        }
        if(nums.length == 2){
            return nums[0] + nums[1] + (nums[0] + nums[1]);
        }
        // 2 for loop
        long res = 0L;
        for(int i = 0; i < nums.length; i++){
            int cur = 0;
            for(int j = i; j < nums.length; j++){
               // System.out.println(">>> i = " + i + ", j = " + j + ", cur = " + cur + ", res = " + res);
                cur += nums[j];
                res += cur;
            }
        }

        return res;
    }

    // LC 496
    // 9.42 - 9.52 am
    /**
     *
     *  find the index j such that nums1[i] == nums2[j]
     *  and determine the next `greater` element of nums2[j] in nums2.
     *  If there is no next greater element, then the answer for this query is -1.
     *
     *
     *  IDEA 1) 2 LOOP
     *
     *  IDEA 2) 1 LOOP + SORTING (sort nums2)
     *
     *  IDEA 3)  1 LOOP + SORTING (sort nums2) + BINARY SEARCH
     *
     *  IDEA 4) MONO INCREASING STACK ???
     *
     *
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        // edge
        if(nums1 == null || nums1.length == 0){
            return null;
        }
        if(nums1.length == 1){
            for(int x: nums2){
                if(x > nums1[0]){
                    return new int[]{x};
                }
            }
        }

        int[] res = new int[nums1.length];
        Arrays.fill(res, -1); // ??
        System.out.println(">>> res (before) = " + res);

        for(int i = 0; i < nums1.length; i++){
            for(int j = i; j < nums2.length; j++){
                System.out.println(">>> i = " + i + ", j = " + j + ", nums2[j] = " + nums2[j] + ",  nums1[i] = " +  nums1[i]);
                if(nums2[j] > nums1[i]){
                    res[i] = nums2[j];
                    break;
                }
            }
        }

        System.out.println(">>> res (after) = " + res);
        return res;
    }

    // LC 503
    //  1.30 - 1.40 pm
    /**
     *  IDEA 1) `duplicated stack` +  `remainder` op ( x % y = z)
     *
     *
     */
    public int[] nextGreaterElements(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }

        int[] cache = new int[2 * nums.length];
        int k = 0;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < nums.length; j++){
                cache[k] = nums[j];
                k += 1;
            }
        }

        int[] res = new int[nums.length];
        Arrays.fill(res, -1); // ??

        // stack: [ [idx_1, next_big_1], .... ]
        Stack<Integer[]> st = new Stack<>();

        for(int i = 0; i < cache.length; i++){
//            if(st.isEmpty()){
//                // NOTE !!!
//                // `adjusted idx` is `i % nums.length`
//                st.add(new Integer[]{ i % nums.length, cache[i % nums.length] });
//                continue;
//            }

            while(!st.isEmpty() && st.peek()[1] <= cache[i]){
                Integer[] tmp = st.pop();
                //res[tmp[0]] = tmp[1];
            }

            st.add(new Integer[]{ (i % nums.length) - 1, cache[i % nums.length] });
        }

        // backfill val to res // /?? needed ?
//        while(!st.isEmpty()){
//            Integer[] tmp = st.pop();
//            res[tmp[0]] = tmp[1];
//         }

        return res;
    }

    /**
     *  IDEA 1) `2 nums` + monotonic stack
     */
//    public int[] nextGreaterElements(int[] nums) {
//        if(nums == null || nums.length == 0){
//            return null;
//        }
//        // `duplicate` nums
//        int k = 0;
//
//        int[] res = new int[nums.length];
//        Arrays.fill(res, -1);
//
//        List<Integer> keys = new ArrayList<>();
//        for(int j = 0; j < 2; j++){
//            for(int i = 0; i < nums.length; i++){
//                int val = nums[i];
//                keys.add(val);
//            }
//        }
//
//        //System.out.println(">>> nums2 = " + nums2);
//        System.out.println(">>> keys = " + keys);
//        System.out.println(">>> res (before) = " + res);
//
//        Stack<Integer> st = new Stack<>();
//        // ???
//        for(int i = 0; i < nums.length; i++){
//            for(int j = i+1; j < keys.size(); j++){
//                int k_ = keys.get(j);
//                if(k_ > nums[i]){
//                    res[i] = k_;
//                    break;
//                }
//            }
//        }
//
//        return res;
//    }


    // Lc 901
    // 12.51 - 1.10 pm
    class StockSpanner {

        public StockSpanner() {

        }

        public int next(int price) {
            return 0;
        }
    }

  // LC 853
  // 4.45 - 5.05 pm
  /**
   *  Exp 1)
   *
   *  Input: target = 12,
   *  position = [10,8,0,5,3], speed = [2,4,1,1,3]
   *
   *  -> map : {position, speed}
   *     stack, pop while (cur_pos + cur_speed <= last_post + last_speed)
   *     if stack update, update map as well
   *  -> return res
   *
   *  -> 12-10 = 2, 2 / 2 = 1
   *     12-8 = 4, 4 / 4 = 1
   *
   *     12-0 = 12, 12 / 1 = 12
   *
   *     12-5 = 7, 7 / 1 = 7
   *     12-3 = 9, 9 / 3 = 3
   *
   *  -> [10,8], [0], [5,3]
   *
   *  -> Output: 3
   *
   *
   *  Exp 2)
   *
   *  Input: target = 10, position = [3], speed = [3]
   *
   *  -> [3]
   *
   *  -> Output: 1
   *
   * Exp 3)
   *
   *  Input: target = 100, position = [0,2,4], speed = [4,2,1]
   *
   *  [0,2], [4]
   *
   * -> Output: 1
   *
   *
   *
   *
   */
  // LC 853
  // 12.46 - 12.56 PM
  /**
   *  IDEA 1) SORT + MAP + FOR LOOP
   *   arrived_time = (target - position) / speed
   *
   *
   *
   */
  public int carFleet(int target, int[] position, int[] speed) {
      // edge
      if(position == null || position.length == 0){
          return 0;
      }
      if(position.length == 1){
          return 1;
      }
      // map, {position : arrived_time} // ???
      Map<Integer, Double> map = new HashMap<>();
      for(int i = 0; i < position.length; i++){
          Double arrived_time = (double) (target - position[i]) / speed[i];
          map.put(position[i], arrived_time);
      }

      // sort on `position` (decreasing order) (big -> small)
      Integer[] position2 = new Integer[position.length];
      for(int i = 0; i < position.length; i++){
          position2[i] = position[i];
      }
      Arrays.sort(position2, new Comparator<Integer>() {
          @Override
          public int compare(Integer o1, Integer o2) {
              int diff = o2 - o1;
              return diff;
          }
      });

      Stack<Double> st = new Stack<>();
      Double prev_arrived_time = -1.0; // ??
      // loop over position,
      // and check if `cur arrived_time > prev arrived_time`
      // if `yes`, -> a  new fleet is created,
      // stack, prev_arrived_time needs to be updated,

      System.out.println(">>> map = " + map);
      System.out.println(">>> position2 = " + position2);

      for(int i = 0; i < position2.length; i++){
          Double cur_arrived_time = map.get(position2[i]);
          System.out.println(">>> prev_arrived_time = " + prev_arrived_time  + ", cur_arrived_time = "
                  + cur_arrived_time + ", stack = " + st);
          // ??
          if(prev_arrived_time.equals(-1.0)){
              prev_arrived_time = cur_arrived_time;
              st.add(prev_arrived_time);
          }else{
              // if can form a `new fleet cluster`
              if(prev_arrived_time < cur_arrived_time){
                  prev_arrived_time = cur_arrived_time;
                  st.add(prev_arrived_time);
              }
          }
      }

      return st.size();
  }

//  public int carFleet_1(int target, int[] position, int[] speed) {
//      // edge
//      if(position == null || position.length == 0){
//          return 0;
//      }
//      if(position.length == 1){
//          return 1;
//      }
//      // map
//      Map<Integer, Integer> map = new HashMap<>();
//      for(int i = 0; i < position.length; i++){
//          map.put(position[i], speed[i]);
//      }
//
//      // stack
//      Stack<Integer> st = new Stack<>();
//      // init // ???
//      for(int p: position){
//          st.add(p);
//      }
//
//      //boolean terminated = false;
//      int prev = -1;
//      while(!isReached(position, target)){
//          if(prev == -1){
//            //  prev =
//          }
//      }
//
//      return st.size(); // ??
//    }
//
//    public boolean isReached(int[] position, int target){
//      for(int p: position){
//          if (p < target){
//              return false;
//          }
//      }
//      return true;
//    }

    // LC 71
    // 5.34 - 5.44 pm
    public String simplifyPath(String path) {

      return null;
    }

  // LC 394
  // 2.06 pm - 2.15 pm
  /**
   *  IDEA 1) STACK ??
   *
   *   -> if `num`, append to num_stack
   *      if [, append to num_stack
   *      if ], pop all elements in num_stack, till reach `[' (save as cache)
   *         -> and do the `multiply op`, e.g. k * cache, append to res
   *
   *   -> repeat above steps..
   *
   *
   * Example 1:
   *
   * Input: s = "3[a]2[bc]"
   * Output: "aaabcbc"
   *
   *  -> 3[a]2[bc],  n_s = [3]
   *     x
   *
   *    3[a]2[bc],  n_s = [3, '[' ]
   *     x
   *
   *    3[a]2[bc],  n_s = [3, '[' , a]
   *      x
   *
   *   3[a]2[bc],  n_s = [3, '[' , a]  -> cache =  3 * a = aaa, res = aaa
   *      x
   *
   *   3[a]2[bc],  n_s = [2]
   *       x
   *
   *   3[a]2[bc],  n_s = [2, '[']
   *        x
   *
   *   3[a]2[bc],  n_s = [2, '[', b]
   *         x
   *
   *   3[a]2[bc],  n_s = [2, '[', b, c]
   *          x
   *
   *  3[a]2[bc],  n_s = [2, '[', b, c], cache = 2 * bc = bcbc, res = aaabcbc
   *          x
   *
   *
   * Example 2:
   *
   * Input: s = "3[a2[c]]"
   * Output: "accaccacc"
   *
   *
   * Example 3:
   *
   * Input: s = "2[abc]3[cd]ef"
   * Output: "abcabccdcdcdef"
   *
   *
   */
  public String decodeString(String s) {
      // edge
      if(s == null || s.length() == 0){
          return null;
      }
      if(s.length() == 1){
          return s;
      }
      StringBuilder sb = new StringBuilder();
      Stack<String> st = new Stack<>();
      Stack<Integer> num_st = new Stack<>();
      for(int i = 0; i < s.length(); i++){
          String cur = String.valueOf(s.charAt(i));
          // case 1) cur is `numerical` element, e.g. 1,2,3,...
          if("123456789".contains(cur)){
              num_st.add(Integer.parseInt(cur));
          }
          // case 2) cur is `]`, need to pop all elements in stack, and do `getMutiply op`
          else if(cur.equals("]")){
              String cache = "";
              while(!st.isEmpty()){
                  String tmp = st.pop();
                  if(!tmp.equals("[")){
                      cache += tmp;
                  }
              }
              sb.append(getMutiply(cache, num_st.pop()));
          }
          // case 3) cur is alphabet element (e.g. a,b,c...)
          else{
              st.add(cur);
          }
      }

      // deal with `remaining element in stack`
      StringBuilder sb2 = new StringBuilder();
      while(!st.isEmpty()){
          sb2.append(st.pop());
      }

      sb.append(sb2.reverse().toString());

      return sb.toString();
    }

    public String getMutiply(String input, int cnt){
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < cnt; i++){
          sb.append(input);
      }
      return sb.reverse().toString(); // since stack if `FILO`
    }

    // LC 895
    // 10.10 am - 10.20 am
    /**
     *  IDEA 1)  HASHMAP (record cnt) + stack (record `top` element) ??
     *
     *
     *  IDEA 2) map<cnt: Stack[val_1, val_2, .....] )
     *     so we `group` the val itself and its count
     *     e.g.  for [3,4,4,5,5,2]
     *     -> our group is like below:
     *        { 1: [3,2, 4,5], 2: [4,5]  }
     *
     *     -> after building above data structure, we know
     *        which val to pop, and are able to maintain the `most freq` val
     */
        class FreqStack {
            int maxCnt;
            Map<Integer, Stack<Integer>> freqMap;
            Map<Integer, Integer> cntMap ;

        public FreqStack() {
            this.maxCnt = 0;
            this.freqMap = new HashMap<>();
            this.cntMap = new HashMap<>();
        }

        public void push(int val) {
            // update freqMap
            Stack<Integer> st = freqMap.getOrDefault(val, new Stack<>());
            st.add(val);
            this.freqMap.put(val, st);

            // update cntMap
            this.cntMap.put(val, this.cntMap.getOrDefault(val, 0) + 1);

            // update maxCnt
            this.maxCnt = Math.max(this.maxCnt + 1, this.cntMap.get(val));
        }

        public int pop() {
            if(this.freqMap.isEmpty()){
                throw new RuntimeException("no element to pop");
            }
            // get most freq val
            Stack<Integer> st = this.freqMap.get(this.maxCnt);
            if(st.isEmpty()){
                return 0;
            }
            int val = st.pop();

            // update cntMap
            this.cntMap.put(val, this.cntMap.getOrDefault(val, 0) - 1);
            if(this.cntMap.get(val) == 0){
                this.cntMap.remove(val);
            }

            // update max val
            if(st.isEmpty()){
                this.freqMap.remove(val);
                for(Integer k: this.cntMap.keySet()){
                    this.maxCnt = Math.max(k, this.maxCnt);
                }
            }

            return val;
        }

    }

//    class FreqStack {
//
//        // attr
//        // map : {val : cnt}
//        Map<Integer, Integer> map;
//        //Stack<Integer> st;
//       // Deque<Integer> dq;
//        List<Integer> dq;
//        int maxCnt;
//
//        public FreqStack() {
//            this.map = new HashMap<>();
//            //this.st = new Stack<>();
//            this.dq = new ArrayList<>(); //new LinkedList<>();
//            this.maxCnt = 0;
//        }
//
//        public void push(int val) {
//            this.map.put(val, this.map.getOrDefault(val, 0) + 1);
//            //this.st.push(val);
//            this.dq.add(val);
//            this.maxCnt = Math.max(this.maxCnt, map.get(val));
//        }
//
//        public int pop() {
//            if(this.dq.isEmpty()){
//               // return 0; // ???
//                throw new RuntimeException("empty stack");
//            }
//
//            // get most freq one
//            List<Integer> list = new ArrayList<>();
//            for(Integer k: map.keySet()){
//                if(map.get(k) == this.maxCnt){
//                    list.add(k);
//                }
//            }
//
////            List<Integer> x = new ArrayList<>();
////            x.remove()
//
//            for(int i = this.dq.size(); i > 0; i--){
//                if(this.dq.get(i) == this.maxCnt){
//                    this.dq.remove(i);
//                    return this.maxCnt;
//                }
//            }
//            return 0;
//
//        }
//    }

    // LC 84
    // 10.48 - 10.58 am
    public int largestRectangleArea(int[] heights) {
        return 0;
    }

    // LC 35
    /**
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) BINARY SEARCH
     *
     *
     *
     */
    public int searchInsert(int[] nums, int target) {
        // edge
        if(nums == null || nums.length == 0){
            return -1; //???
        }
        if(nums.length <= 1){
            return nums.length;
        }
        if(target > nums[nums.length - 1]){
            return nums.length;
        }
        if(nums.length == 2){
            if(target > nums[0] && target < nums[1]){
                return 1;
            }
        }

        int res = 0;
        for(int i = 1; i < nums.length - 1; i++){
            if(nums[i] == target){
                return i;
            }
            if(target > nums[i-1] && target < nums[i]){
                return i;
            }
        }

        return res;
    }

    // LC 374
    public int guessNumber(int n) {
        int l = 0;
        int r = n - 1;
        int[] n_array = new int[n];
        for(int i = 0; i < n; i++){
            n_array[i] = i;
        }
        while (r >= l){
            int mid = ( l + r ) / 2;
            int resp = guess(n);
            if(resp == 0){
                // return mid;
                return n_array[mid];
            }
            return resp;
//            else if (resp == 1){
//                r = mid -1;
//            }else{
//                l = mid + 1;
//            }
        }

        return -1; // should NOT happen
    }

    public int guess(int num){
        return 0;
    }


    // LC 875
    /**
     *
     *  ->  Return the minimum integer k such that she can eat
     *      all the bananas within h hours.
     *
     *
     *
     *   binary search find the `speed` that
     *   can eat ALL banana on time
     *
     */
    // 12.23 - 12.33 pm
    /**
     *  IDEA 1) BINARY SEARCH (with `smaller val`)
     *
     */
    public int minEatingSpeed(int[] piles, int h) {

        // edge
        if(piles == null || piles.length == 0){
            return 0;
        }
        if(piles.length == 1){
            return minEatingSpeed_(piles, piles[0]);
        }

        //Integer[] piles2 = new Integer[piles.length];
        int maxVal = 0;
        for (int pile : piles) {
            maxVal = Math.max(maxVal, pile);
        }

        int l = 1; // ?? smallest speed
        int r = maxVal;

        /**
         *  NOTE !!!
         *
         *   when `jump out of the loop` (while r>= l ..)
         *   `l` is the `minimum val that fit the requirement
         *   (e.g. minEatingSpeed_(piles, mid) <= h)
         *
         *   Reason:
         *
         *
         *
         */
        while(r >= l){
            int mid = ( l + r ) / 2;
            // too `fast`
            if(minEatingSpeed_(piles, mid) <= h){
                // try to find `smaller` speed
                r = mid - 1;
            }
            // to `slow`
            else{
                l = mid + 1;
            }
        }

        return l;
    }

    public int minEatingSpeed_(int[] piles, int speed) {
        int hr = 0;
        for(int p: piles){
            // ??? TODO: double check `floor` op
            //hr += Math.floor( p / speed );
            hr += Math.ceil( (double) p / speed );
        }
        return hr;
    }

//    public int minEatingSpeed_(int[] piles, int h) {
//
//        // edge
//        if(piles == null || piles.length == 0){
//            return 0;
//        }
//        if(piles.length == 1){
//            int times = piles[0]/ h;
//            int remain = piles[0] % h;
//            if (remain != 0){
//                times += 1;
//            }
//            return times;
//        }
//
//        Integer[] piles2 = new Integer[piles.length];
//        for(int i = 0; i < piles.length; i++){
//            piles2[i] = piles[i];
//        }
//        // sort piles (small -> big)
//        Arrays.sort(piles2, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o1 - 02;
//                return diff;
//            }
//        });
//
//        int minSpeed = 0;
//        // binary search
//        // ???
//        int l = piles2[0]; // 0;
//        int r = piles2[piles2.length - 1]; //piles.length - 1;
//        while(r >= l){
//            int mid = (l + r) / 2;
//            int hours = getTotalHour(piles2, mid);
//            if(hours == h){
//                minSpeed = mid;
//             // speed is too slow
//            }else if (hours > h){
//                l = mid + 1;
//            // speed is too fast
//            }else{
//                r = mid - 1;
//            }
//
//        }
//
//        return minSpeed;
//    }
//
//    public int getTotalHour(Integer[] piles2, int speed){
//        int res = 0;
//        for (Integer p: piles2){
//            int times = p / speed;
//            int remain = p % speed;
//            if(remain != 0){
//                times += 1;
//            }
//            res += times;
//        }
//
//        return res;
//    }
//
//
//    public int minEatingSpeed(int[] piles, int h) {
//
//        if (piles.length == 0 || piles.equals(null)){
//            return 0;
//        }
//
//        int l = 1; //Arrays.stream(piles).min().getAsInt();
//        int r = Arrays.stream(piles).max().getAsInt();
//
//        while (r >= l){
//            System.out.println(">>> l = " + l + ", r = " + r);
//            int mid = (l + r) / 2;
//            int _hour = getCompleteTime_(piles, mid);
//            /**
//             *  Return the minimum integer k such that she can eat all the bananas within h hours.
//             *
//             *  -> NOTE !!! so any speed make hr <= h hours could work
//             *
//             *  -> NOTE !!! ONLY  when _hour <= h, we update r
//             */
//            if (_hour <= h){
//                r = mid - 1;
//            }else{
//                l = mid + 1;
//            }
//        }
//
//
//        System.out.println(">>> FINAL l = " + l + ", r = " + r);
//        return l;
//    }
//
//
//    private int getCompleteTime_(int[] piles, int speed) {
//
//        int _hour = 0;
//        for (int pile : piles) {
//            _hour += Math.ceil((double) pile / speed);
//        }
//
//        return _hour;
//    }

    // LC 1011
    // 11.24 - 11.34 am
    /**
     *  IDEA 1) BINARY SEARCH
     *
     *
     */
    public int numRescueBoats(int[] people, int limit) {
        int maxWeight = 0;
        for (int p: people){
            maxWeight = Math.max(maxWeight, p);
        }
        // ???
        int l = 1;
        int r = maxWeight;
        while (r >= l){
            int mid = ( l + r ) / 2;
            int days = getShipDays(people, mid);
            // too fast
            if(days <= limit){
                r = mid -1;
            }
            // too slow
            else{
                l = mid + 1;
            }
        }

        // similar as LC 875
        return l;
    }

    public int getShipDays(int[] people, int capacity){
        int days = 0;
        int tmpSum = 0;
        for(int p: people){
            tmpSum += p;
            if(tmpSum >= capacity){
                tmpSum = p;
                days += 1;
            }
        }

        // handle remaining
        if(tmpSum > 0){
            days += 1;
        }
        return days;
    }


    // LC 153
    // 10.18 - 10.28 am
    /**
     *  IDEA 1) BINARY SEARCH
     *
     *   case 0) all arr is in `ascending order`
     *
     *     -> return arr[0] directly
     *
     *
     *  case 1) left is in `ascending order`
     *      if left < right
     *         -> move right
     *      else
     *         -> move left
     *
     *
     *   case 2)  right is in `ascending order`
     *
     *       if right < left
     *          move right
     *       else
     *          move left
     *
     *
     *
     *
     *   exp 1)
     *
     *   Input: [3,4,5,1,2]
     *   Output: 1
     *
     *   [3,4,5,1,2]
     *        m
     *
     *
     *  exp 2)
     *    Input: [4,5,6,7,0,1,2]
     *    Output: 0
     *
     *
     *     [4,5,6,7,0,1,2]
     *            m
     *
     */
    public int findMin(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return -1; // ??
        }
        int res = Integer.MAX_VALUE;
        int l = 0;
        int r = nums.length - 1;
        while(r >= l){

            int mid = (l + r) / 2;
            // if arr already in `ascending` order
            if(nums[r] > nums[l]){
                return nums[0];
            }

            // NOTE !!! update res
            res = Math.min(res, nums[mid]);

            // case 1) mid + left is ascending
            // e.g. [3,4,5,1,2]
            if(nums[mid] < nums[r]){
                l = mid + 1;
            }
            // case 2) mid + right is ascending
            // [7,1,2,3,4,5,6]
            else{
                r = mid - 1;
            }
        }
        return res;
    }


//    public int findMin(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return -1; // ??
//        }
//        // if arr already in `ascending` order
//        if(nums[nums.length-1] > nums[0]){
//            return nums[0];
//        }
//
//        int l = 0;
//        int r = nums.length - 1;
//        int res = -1;
//
//        while(r >= l){
//            int mid = (l + r) / 2;
//            res = Math.min(res, nums[mid]);
//            // case 1) mid + right is in `ascending order`
//            if(nums[nums.length - 1] > nums[mid+1]){
//                // case 1-1) mid > mid-1
//                if(nums[mid-1] < nums[mid]) {
//                    r = mid - 1;
//                // case 1-2)
//                }else{
//                   l = mid + 1;
//                }
//
//            }
//            // case 2)  mid + left is in `ascending order`
//            else{
//                // case 2-1) left > mid+1
//                if(nums[l] > nums[r]){
//                    l = mid+1;
//                }
//                // case 2-2) left < mid + 1
//                else{
//                    r = mid - 1;
//                }
//            }
//        }
//
//        return res;
//    }


    // LC 33
    // 11.22 - 11.32 am
//    public int search(int[] nums, int target) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return -1;
//        }
//        if(nums.length == 1){
//            if(nums[0] == target){
//                return 0;
//            }
//            return -1;
//        }
//        if(nums.length <= 3){
//            for(int i = 0; i < nums.length; i++){
//                if(nums[i] == target){
//                    return i;
//                }
//            }
//            return -1;
//        }
//        // binary search
//        int l = 0;
//        int r = nums.length - 1;
//        while(r >= l){
//            int mid = (l + r) / 2;
//            if(nums[mid] == target){
//                return mid;
//            }
//            // case 1)  `left + mid` is in ascending order
//            // [3,4,5,1,2]
//            if(nums[mid] >= nums[l]){
//                // case 1-1)  target < mid && target > l
//                if(target < nums[mid] && target >= nums[l]){
//                     r = mid - 1;
//                }
//                // case 1-2) target > nums[l]
//                else{
//                    l = mid + 1;
//                }
//            }
//            // case 2)  `right + mid` is in ascending order
//            // [5,1,2,3,4]
//            else{
//                // case 2-1)  target > min && target < r
//                if(target > nums[mid] && target <= nums[r]){
//                    l = mid + 1;
//                }
//                // case 2-2) target > right (minimum in right sub array)
//                else{
//                    r = mid - 1;
//                }
//            }
//        }
//
//        return -1;
//    }


    // LC 81
    public boolean search(int[] nums, int target) {

        if (nums.length == 0 || nums.equals(null)){
            return false;
        }

        List<Integer> collected = new ArrayList<>();
        for(int n: nums){
            if(!collected.contains(n)){
                collected.add(n);
            }
        }

        int l = 0;
        int r = collected.size() - 1;

        while (r >= l){

            int mid = (l + r) / 2;
            int cur = collected.get(mid);
            if (cur == target){
                return true;
            }
            // Case 1: `left + mid` is in ascending order
            /** NOTE !!! we compare mid with left, instead of 0 idx element */
            else if (collected.get(mid) >= collected.get(l)) {
                // case 1-1)  target < mid && target > l
                if (target >= collected.get(l) && target < collected.get(mid)) {
                    r = mid - 1;
                }
                // case 2-2)
                else {
                    l = mid + 1;
                }
            }

            // Case 2:  `right + mid` is in ascending order
            else {
                // case 2-1)  target > min && target <= r
                if (target <= collected.get(r) && target >collected.get(mid)) {
                    l = mid + 1;
                }
                // case 2-2)
                else {
                    r = mid - 1;
                }
            }

        }

        return false;
    }

    /**
     * Your TimeMap object will be instantiated and called as such:
     * TimeMap obj = new TimeMap();
     * obj.set(key,value,timestamp);
     * String param_2 = obj.get(key,timestamp);
     */
    // LC 981
    // 4.44 -4.54 pm
    /**
     *  IDEA 1)
     *
     *  map1 : { k : v }
     *  map2 : { k : [t_1, t_2, ...] }
     *  map3 : {k_1_t_1 : v_1, k_2_t_2 : v_2, ...}
     *
     *
     *
     */
    class TimeMap {

        // attr
        /** keyValueMap : { k: [v_1, v_2, ...] } */
        Map<String, List<String>> keyValueMap;

        /** valueTimeMap : { v: [t_1, t_2, ...] } */
        Map<String, List<Integer>> insertTimeMap;


        public TimeMap() {
            this.keyValueMap = new HashMap<>();
            this.insertTimeMap = new HashMap<>();
        }

        public void set(String key, String value, int timestamp) {
            // update keyValueMap
            List<String> valList =  this.keyValueMap.getOrDefault(key, new ArrayList<>());
            valList.add(value);
            this.keyValueMap.put(key, valList);

            // update insertTimeMap
            List<Integer> timeList = this.insertTimeMap.getOrDefault(value, new ArrayList<>());
            timeList.add(timestamp);
            this.insertTimeMap.put(value, timeList);
        }

        public String get(String key, int timestamp) {
            if(!this.keyValueMap.containsKey(key)){
                return ""; //null;
            }

            List<String> vals = this.keyValueMap.get(key);
            //List<String> valCollected = new ArrayList<>();
            String res = null;
            int tmpTime = 0;

            for(String v : vals){
                for(Integer t: this.insertTimeMap.get(v)){
                    // get the `latest` timestamp and its val
                    if(t <= timestamp && t > tmpTime){
                        tmpTime = t;
                        res = v;
                    }
                }
            }

            // get min
            return res;
        }
    }

  // LC 410
  // 5.31 - 5.41 pm
  /**
   *
   *
   *
   *  exp 1)
   *
   *  Input: nums = [7,2,5,10,8], k = 2
   *  Output: 18
   *
   *  -> sort, nums = [2,5,7,8,10]
   *
   *
   *  exp 2)
   *
   *  Input: nums = [1,2,3,4,5], k = 2
   *  Output: 9
   *
   *
   */
  public int splitArray(int[] nums, int k) {

        return 0;
    }

  // LC 004
  // 6.12 - 6.22 pm
  /**
   *  -> NOTE !!!
   *     nums1, nums2 are sorted
   *
   *  IDEA 0)  BRUTE FORCE
   *
   *  IDEA 1) 2 stack : small, big
   *    - small_st : vals < median
   *    - big_st:  vals >= median
   *
   *
   *
   *  exp 1)
   *   Input: nums1 = [1,3], nums2 = [2]
   *   Output: 2.00000
   *
   *   ->
   *     nums1 = [1,3]     small_st = [1]
   *              x
   *
   *     nums2 = [2]
   *
   *
   *
   */
  public double findMedianSortedArrays(int[] nums1, int[] nums2) {
      // edge
      if(nums1.length == 0 && nums2.length == 0){
          return 0.0; // ?
      }
      if(nums1.length == 0 || nums2.length == 0){
          if(nums1.length == 0){
              //return nums2[(nums2.length - 1) / 2]; // ?
              return getMedian(nums2);
          }
         // return nums1[(nums1.length - 1) / 2]; // ?
          return getMedian(nums1);
      }

      //Integer[] nums_merge = new Integer[nums1.length + nums2.length];
      List<Integer> cache = new ArrayList<>();
      for(int i = 0; i < nums1.length; i++){
          cache.add(nums1[i]);
      }

      for(int i = 0; i < nums2.length; i++){
          cache.add(nums2[i]);
      }

      System.out.println(">>> (before sort) cache = " + cache);

      // sort (small -> big)
      Collections.sort(cache, new Comparator<Integer>() {
          @Override
          public int compare(Integer o1, Integer o2) {
              int diff = o1 - o2;
              return diff;
          }
      });

      System.out.println(">>> (after sort) cache = " + cache);
      // size is even
      if(cache.size() % 2 == 0){
          double val1 = cache.get( ( cache.size() / 2 ));
          double val2 = cache.get( ( cache.size() / 2 ) - 1);

          System.out.println(">>> val1 = " + val1 + ", val2 = " + val2);

          // [1,2,3,4]
          // [1,2,3,4,5,6]
          return  (val1 + val2) / 2;
      }
      // size is odd
      return  cache.get(cache.size() / 2);
    }

    public double getMedian(int[] num){
       int size = num.length;
       if(size % 2 == 0){
           double val1 = num[num.length / 2];
           double val2 = num[(num.length / 2) - 1];
           return (val1 + val2) / 2;
       }
       return num[(num.length / 2)];
    }

    class MountainArray {
       public int get(int index) {return 0;}
       public int length() {return 0;}
    }

  // LC 1095
  // 3.22 - 3.32 pm
  /**
   *  IDEA 1) BINARY SEARCH
   *
   *   1. via get, check below cases
   *
   *    *    case 1) `mid + left` is in ascending order
   *    *    case 2) `mid + right` is in ascending order
   *
   *
   *   2. if can't find a solution,return -1
   *
   *
   *   exp 1)
   *
   *   Input: mountainArr = [1,2,3,4,5,3,1], target = 3
   *   Output: 2
   *
   *    case 1) `mid + left` is in ascending order
   *    case 2) `mid + right` is in ascending order
   *
   *   [1,2,3,4,5,3,1]
   *
   *   [1,2,3,7,5]
   *
   */
  public int findInMountainArray(int target, MountainArray mountainArr) {
      return 0;
    }

    // LC 206
    public ListNode reverseList(ListNode head) {
      // edge
      if(head == null || head.next == null){
          return head;
      }
      /**
       *  4 steps
       *
       */
//      ListNode dummy = new ListNode();
//      dummy.next = head;
      ListNode _prev = null; //new ListNode();

      while(head != null){
          ListNode _next = head.next;
          head.next = _prev;
         // _prev.next = head;
          _prev = head;
          head = _next;
      }

      return _prev;
    }

    // LC 92
    // 1.04 - 1.14 pm
    /**
     *  IDEA 1) list -> reverse -> listNode
     *
     *
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        // edge
        if(head == null || head.next == null){
            return head;
        }
        // ??
        if(right == left){
            return head;
        }
        if(right < left){
            throw new RuntimeException("invalid left, right val");
        }

        // step 1) find idx `prev` than left
        ListNode dummy = null;
        dummy.next = head;
        ListNode head2 = head;
        for(int i = 0; i < left - 1; i++){
            head2 = head2.next;
        }

        // init !!!
        ListNode _leftPrev = head2; // ???
        ListNode _left = head2.next;
        ListNode _prev = null;
        //ListNode _next = null;

        // -> setup prev, leftPrev, ...
        // step 2) reverse from left to right
        for(int i = left; i < right - 1; i++){
            //ListNode _ = head2.next;
            ListNode _next = _left.next;
            //_prev.next = head2;
            _left.next = _prev;
            _prev = _left;
            _left = _next;
        }

        ListNode _right = _left.next;

        /**
         *   exp 1)
         *
         *    input: 1->2->3->4->5, left = 1, right = 3
         *             |------|
         *
         *    output: 1-> 4 -> 3 -> 2 -> 5
         *
         *
         *   -> input: 1->2->3->4->5
         *             x
         *
         */

        // step 3) reconnect prev, leftPrev, to right pointer
        // ???
        _leftPrev.next = _left; //head2;
        _leftPrev.next.next = _right;

        return dummy.next; // ???
    }



//    public ListNode reverseBetween(ListNode head, int left, int right) {
//        // edge
//        if(head == null || head.next == null){
//            return head;
//        }
//        // ??
//        if(right == left){
//            return head;
//        }
//        if(right < left){
//            throw new RuntimeException("invalid left, right val");
//        }
//
//        /**
//         *  ListNode : reverse nodes withon start, end idx
//         */
//        ListNode dummy = null;
//        dummy.next = head;
//        ListNode _prev = null;
//
//        boolean shouldReverse = false;
//        int idx = 0;
//
//        while(head != null){
//            if(idx < left || idx > right){
//                shouldReverse = false;
//            }
//            else{
//                shouldReverse = true;
//            }
//
//            // get `prev` node before reverse
//            if(idx == left - 1){
//                _prev = head;
//            }
//            // start `reverse`
//            if(shouldReverse){
//                ListNode _next = head.next;
//                head.next = _prev;
//                _prev = head;
//                head = _next;
//                //continue; // /?
//            }
//
//            _prev.next = head; // ???
//            // end reverse
//            if(!shouldReverse){
//                head = head.next;
//            }
//            //head = _next;
//            idx += 1;
//        }
//
//        return dummy.next; // ???
//    }

//   public ListNode reverseBetween(ListNode head, int left, int right) {
//      // edge
//      if(head == null || head.next == null){
//          return head;
//      }
//      // ??
//      if(right == left){
//          return head;
//      }
//      if(right < left){
//          throw new RuntimeException("invalid left, right val");
//      }
//
//      List<Integer> cache = new ArrayList<>();
//      int startIdx = -1;
//      int endIdx = -1;
//      int idx = 0;
//
//      ListNode head2 = head;
//
//      while(head2 != null){
//          if(head2.val == left){
//              startIdx = idx;
//          }
//          if(head2.val == right){
//              endIdx = idx;
//          }
//          cache.add(head2.val);
//          head2 = head2.next;
//          idx += 1;
//      }
//
//       List<Integer> leftSub = new ArrayList<>();
//       List<Integer> rightSub = new ArrayList<>();
//       List<Integer> toReverse = new ArrayList<>();
//
//       // 0,1,2
//      for(int i = 0; i < cache.size(); i++){
//          if(i < startIdx){
//              leftSub.add(cache.get(i));
//          }else if (i > startIdx && i < endIdx){
//              toReverse.add(cache.get(i));
//          }else{
//              rightSub.add(cache.get(i));
//          }
//      }
//
//      // reverse
//      Collections.reverse(toReverse);
//
//      List<Integer> finalCache = new ArrayList<>();
//      for(int i = 0; i < leftSub.size(); i++){
//          finalCache.add(leftSub.get(i));
//      }
//       for(int i = 0; i < toReverse.size(); i++){
//           finalCache.add(toReverse.get(i));
//       }
//       for(int i = 0; i < rightSub.size(); i++){
//           finalCache.add(rightSub.get(i));
//       }
//
//      ListNode dummy = null;
//      for(Integer x: finalCache){
//          dummy.next = new ListNode(x);
//          dummy = dummy.next;
//      }
//
//      return dummy.next;
//   }

    // LC 141
    // 6.12 - 6.22 pm
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

    // LC 42
    // 6.21 - 6.31 pm
    public ListNode detectCycle(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return null;
        }
        // use map record idx
        // map : {ListNode: idx}
        //Map<ListNode, Integer> map = new HashMap<>();

        // check if there is a `cycle`
        ListNode head2 = head;
        HashSet<ListNode> set = new HashSet<>();
        boolean foundCycle = false;
        int idx = 0;
        int cycleIdx = -1;
        ListNode circuleNode = null;

        while(head2 != null){

            if(set.contains(head2)){
                // there is a cycle
                circuleNode = head2;
                cycleIdx = idx;
                foundCycle = true;
                break;
            }

            set.add(head2);
            //map.put(head2, idx);

            head2 = head2.next;
            idx += 1;
        }
        if(!foundCycle){
            return null;
        }

        System.out.println(">>> circuleNode val = " + circuleNode.val);
        System.out.println(">>> circuleNode next = " + circuleNode.next);
        //return map.get(cycleIdx); // ???
        return circuleNode.next;
    }


    // LC 642
    // 6.48 - 6.58 pm
    /**
     *  IDEA 1) `CIRCULAR` LINKED LIST + OP
     *
     *  IDEA 2) DEQUEUE ???
     *
     *  IDEA 3) use `queue` + `stack`
     *       -> queue for `first element`
     *       -> stack for `last element`
     *
     */
    // 11.56 - 12.21 pm
    // IDEA : ARRAY + ` x % y = z ` op
    class MyCircularQueue {
        int head;
        int tail; // ??
        int capacity;
        //Deque<Integer> dq;
        Integer[] arr;

        public MyCircularQueue(int k) {
            this.capacity = k;
            this.head = 0;
            this.tail = 0; // ???
            //this.dq = new LinkedList<>(); // ???
            this.arr = new Integer[this.capacity];
        }

        public boolean enQueue(int value) {
            if(this.isFull()){
                return false;
            }
            // ???
//            int adjustedIdx = (this.head + 1) % this.capacity;
//            this.arr[adjustedIdx] = value; // ???
            this.tail = (this.tail + 1) % this.capacity;
            this.arr[this.tail] = value;
            return true;

        }

        public boolean deQueue() {
            if(this.isEmpty()){
                return false;
            }

            // ???
            if(this.head == this.tail){
                this.head = 0;
                this.tail = -1;
            }else{
                this.head = (this.head + 1) % this.capacity;
            }
            return true;
        }

        public int Front() {
            if(this.isEmpty()){
                return -1;
            }
           // return this.dq.getFirst(); // Queue: FIFO
            return this.arr[this.head]; // ???
        }

        public int Rear() {
            if(this.isEmpty()){
                return -1;
            }
            //return this.dq.getLast(); // Queue: FIFO
            return this.arr[this.tail]; // ???
        }

        public boolean isEmpty() {

            return this.head == this.tail; // ???? //this.dq.isEmpty();
        }

        public boolean isFull() {

            //return this.dq.size() == this.capacity;
            return (this.tail - this.head) + 1 == this.capacity; // ???
        }

    }


    // IDEA: DQEUEU
//    class MyCircularQueue {
//        int head;
//        int tail; // ??
//        int capacity;
//        Deque<Integer> dq;
//
//        public MyCircularQueue(int k) {
//            this.capacity = k;
//            this.head = 0;
//            this.tail = 0; // ???
//            this.dq = new LinkedList<>(); // ???
//        }
//
//        public boolean enQueue(int value) {
//            if(this.isFull()){
//                return false;
//            }
//            // ???
//            //this.dq.add
//            //int adjustedIdx =
//            this.dq.addLast(value);
//            return true;
//
//        }
//
//        public boolean deQueue() {
//            if(this.isEmpty()){
//                return false;
//            }
//            // ???
//            this.dq.pollFirst();
//            return true;
//        }
//
//        public int Front() {
//            if(this.isEmpty()){
//                return -1;
//            }
//            return this.dq.getFirst(); // Queue: FIFO
//        }
//
//        public int Rear() {
//            if(this.isEmpty()){
//                return -1;
//            }
//            return this.dq.getLast(); // Queue: FIFO
//        }
//
//        public boolean isEmpty() {
//
//            return this.dq.isEmpty();
//        }
//
//        public boolean isFull() {
//
//            return this.dq.size() == this.capacity;
//        }
//
//    }

//    class MyCircularQueue {
//        int[] arr;
//        int elementCnt;
//        int capacity;
//        int head;
//        int tail;
//
//        public MyCircularQueue(int k) {
//            this.arr = new int[k];
//            this.elementCnt = 0;
//            this.capacity = k;
//            this.head = 0;
//            // ???
//            this.tail = -1; //0;
//        }
//
//        public boolean enQueue(int value) {
//            // ???
//            if(this.isFull()){
//                return false;
//            }
//            this.tail = (this.tail + 1) % this.capacity;
//            //this.tail += 1;
//            this.arr[this.tail] = value;
//            this.elementCnt += 1;
//            return true;
//        }
//
//        public boolean deQueue() {
//            // ???
//            if(this.isEmpty()){
//                return false;
//            }
//            if(this.head == this.tail){
//                this.head = 0;
//                this.tail = -1;
//            }else{
//                this.head = (this.head - 1) % this.capacity;
//            }
////            this.head = (this.head + 1) % this.capacity;
////            this.arr[this.head] = 0; // ???
//            //this.head += 1;
//            this.elementCnt -= 1;
//            return true;
//        }
//
//        public int Front() {
//            if(this.isEmpty()){
//                return -1;
//            }
//            return this.arr[this.head]; // ??
//        }
//
//        public int Rear() {
//            if(this.isEmpty()){
//                return -1;
//            }
//            return this.arr[this.tail]; // ??
//        }
//
//        public boolean isEmpty() {
//            return this.elementCnt == 0;
//        }
//
//        public boolean isFull() {
//            return this.elementCnt == this.capacity;
//        }
//
//    }


//    class MyCircularQueue {
//
//        // attr
//        //ListNode node;
//        Stack<Integer> st;
//        Queue<Integer> q;
//        int capacity;
//        int elementCnt;
//
//        public MyCircularQueue(int k) {
//            //this.node = null;
//            this.st = new Stack<>();
//            this.q = new LinkedList<>();
//            this.capacity = k;
//            this.elementCnt = 0;
//        }
//
//        public boolean enQueue(int value) {
//            this.st.add(value);
//            this.q.add(value);
//            this.elementCnt += 1;
//            return true;
//        }
//
//        public boolean deQueue() {
//            this.q.poll();
//            this.st.pop();
//            return true; // ???
//        }
//
//        public int Front() {
//            //return this.node.next.val; // ???
//            if(this.q.isEmpty()){
//                return -1;
//            }
//            return this.q.peek();
//        }
//
//        public int Rear() {
//            if(this.st.isEmpty()){
//                return -1;
//            }
//            return this.st.peek();
//        }
//
//        public boolean isEmpty() {
//            return this.elementCnt > 0;
//        }
//
//        public boolean isFull() {
//            return this.elementCnt == this.capacity;
//        }
//
//    }

    // LC 232
    // 7.16 PM - 7.26 PM
    /**
     *  IDEA 1) 2 STACK
     *   st1 : collect `push` element
     *   st2 : `reversely` save element pop from st1, so can simulate queue
     *
     */
    class MyQueue {

        Stack<Integer> st1;
        Stack<Integer> st2;
       // int elementCnt;

        public MyQueue() {
            st1 = new Stack<>();
            st2 = new Stack<>();
        }

        public void push(int x) {
            this.st1.push(x);
        }

        public int pop() {
            if(this.empty()){
                throw new RuntimeException("queue is empty");
            }
            if(!this.st2.isEmpty()){
                return this.st2.pop();
            }
            this.rebalance();
            return this.st2.pop();
        }

        public int peek() {
            if(this.empty()){
                throw new RuntimeException("queue is empty");
            }
            if(!this.st2.isEmpty()){
                return this.st2.peek();
            }
            this.rebalance();
            return this.st2.peek();
        }

        public boolean empty() {
            return this.st1.isEmpty() && this.st2.isEmpty();
        }
        
        private void rebalance(){
            // push element to st2
            while(!this.st1.isEmpty()){
                this.st2.add(this.st1.pop());
            }
        }
        
    }

    // LC 225
    // 7.28 - 7.38 pm
    /**
     *  IDEA 1) 2 QUEUE
     *
     *    q1 : accept new val
     *    q2 : simulate stack (for peek, pop op)
     *
     */
    class MyStack {

        Queue<Integer> q1;
        Queue<Integer> q2;

        public MyStack() {
            this.q1 = new LinkedList<>();
            this.q2 = new LinkedList<>();
        }

        public void push(int x) {
            this.q1.add(x);
        }

        public int pop() {
            if(this.empty()){
                throw new RuntimeException("stack is empty");
            }
            if(!this.q2.isEmpty()){
                return this.q2.poll();
            }
            this.rebalance();
            return this.q2.poll();
        }

        public int top() {
            if(this.empty()){
                throw new RuntimeException("stack is empty");
            }
            this.rebalance();
            return this.q2.peek();
        }

        public boolean empty() {
            return this.q1.isEmpty() && this.q2.isEmpty();
        }

        private void rebalance(){
            while(!this.q1.isEmpty()){
                this.q2.add(this.q1.poll());
            }
        }
    }

    // LC 146
    // 6.03 - 6.13 am
    /**
     * Design a data structure that
     * follows the constraints of a
     * Least Recently Used (LRU) cache.
     *
     *  `Least Recently Used (LRU)`
     *
     *
     * IDEA :  MAP + dq
     *   1) k-v map
     *   2) dq : save `recent used val`
     *
     */
    class LRUCache {
        int capacity;
        Map<Integer, Integer> map;
        Deque<Integer> dq;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.map = new HashMap<>();
            // this.dq = new ArrayDeque<>(); // ??
            this.dq = new LinkedList<>(); // ??
        }

        public int get(int key) {
            if(!this.map.containsKey(key)){
                return -1;
            }
            int val = this.map.get(key);
            // update dq
            //Deque<Integer> tmp = new ArrayDeque<>();
//            for(Integer x: this.dq){
//                //this.dq.re
//                if(x == key){
//                    this.dq.remove(x);
//                }
//            }
            this.dq.remove(key);
            // `add val back`, but put as `tail` (as recent used)
            this.dq.addLast(key);
            return val;
        }

        public void put(int key, int value) {
            if(this.map.keySet().size() >= this.capacity){
                // remove `least recent used` element (e.g. Least Recently Used)
                int toRemove = this.dq.removeFirst();
                this.map.remove(toRemove);
            }

            // remove `least recent used` element (e.g. Least Recently Used)
            this.map.put(key, value);
            this.dq.addFirst(key);
        }
    }




//    class LRUCache {
//        int capacity;
//        // map : { key : val }
//        Map<Integer, Integer> map;
//        // `double direction` queue
//        Deque<Integer> dq;
//
//        public LRUCache(int capacity) {
//            this.capacity = capacity;
//            this.map = new HashMap<>();
//            this.dq = new LinkedList<>();
//        }
//
//        public int get(int key) {
//            if(!this.map.containsKey(key)){
//                return -1;
//            }
//            int val = this.map.get(key);
//            // update `recent used element`
//            // ???
//            this.dq.remove(key);
//            this.dq.addLast(key);
//            return val;
//        }
//
//        public void put(int key, int value) {
//            // only do the put op if key is NOT existed yet
//            if(this.map.containsKey(key)){
//                return;
//            }
//            //  ??
//            while(!this.dq.isEmpty() && this.dq.size() >= this.capacity){
//                // if size >= capacity
//                // keep removing `last element` (least used)
//                int k = this.dq.pollLast();
//                if(this.map.containsKey(k)){
//                    this.map.remove(k);
//                }
//            }
//
//            this.dq.addLast(key);
//            this.map.put(key, value);
//        }
//
//    }

    // LC 460
    // 10.36 - 10.46 am
    /**
     *  IDEA 1) HASHMAP + DEQUEUE
     */
    class LFUCache {
        int capacity;
        Map<Integer, Integer> kvMap;
        Map<Integer, Integer> kFreqMap;
        //Deque<Integer> dq;

        public LFUCache(int capacity) {
            this.capacity = capacity;
            this.kvMap = new HashMap<>();
            this.kFreqMap = new HashMap<>();
           // this.dq = new LinkedList<>();
        }

        public int get(int key) {
            if(!this.kvMap.containsKey(key)){
                return -1;
            }
            int val = this.kvMap.get(key);
            this.kFreqMap.put(key, this.kFreqMap.getOrDefault(key, 0) + 1);
            return val;
        }

        public void put(int key, int value) {
            if(this.kvMap.keySet().size() >= this.capacity){
                // remove `least freq used` element
                int k = this.getLeastFreqUsed();
                this.kvMap.remove(k);
                this.kFreqMap.remove(k);
            }
            this.kFreqMap.put(key, this.kFreqMap.getOrDefault(key, 0) + 1);
        }

        private int getLeastFreqUsed(){
            //int key = -1;
            int val = Integer.MAX_VALUE;
            for(int k : this.kFreqMap.keySet()){
                val = Math.min(val, this.kFreqMap.get(k));
            }

            return val;
        }
    }

    // LC 23
    // 11.18 - 11. 28 am
    /**
     *  IDEA 1) LINKED LIST -> ARRAY -> SORT -> LINKED LIST
     *  IDEA 2) SORT one by one (linked list op)
     *  IDEA 3)
     *
     *
     */
    public ListNode mergeKLists(ListNode[] lists) {
        // edge
        if(lists == null || lists.length == 0){
            return null; // ??
        }
        if(lists.length == 1){
            return lists[0];
        }
        // TODO: optimize below
        List<Integer> list = new ArrayList<>();
        for(ListNode node: lists){
            while(node != null){
                list.add(node.val);
                node = node.next;
            }

        }

        // sort (small -> big)
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        ListNode dummy = new ListNode();
        ListNode dummy2 = dummy; // ???

        for(Integer x: list){
            ListNode _node = new ListNode(x);
            dummy.next = _node;
            dummy = _node;
        }

        return dummy2.next;
    }

    // LC 25
    // 9.48 - 9.58 am
    /**
     *  IDEA 1)  reverse in iteration
     *  IDEA 2) reverse in recursion
     *  IDEA 3) linked list -> list -> reverse -> linked list
     *
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        // edge
        if(head == null || head.next == null){
            return head;
        }

        // get len
        int len = 0;
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode head2 = head;
        ListNode head3 = head;

        while(head2 != null){
            len += 1;
            head2 = head2.next;
        }

        // reverse linked list
        ListNode _prev = null;
        int cnt = len / k;
        for(int i = 0; i < cnt; i++){
            ListNode reversed = reverseLinkedList(head3, k);
            _prev.next = reversed;
            _prev = reversed;
        }
        return _prev.next;
    }


    public ListNode reverseLinkedList(ListNode input, int k){
        ListNode dummy = new ListNode();
        dummy.next = input;
        ListNode _prev = null;
        while(k >= 0){
            ListNode _next = input.next;
            input.next = _prev;
            _prev = input;
            input = _next;
        }

        return dummy.next; // ????
    }

    // LC 94
//    List<Integer> res = new ArrayList<>();
//    public List<Integer> inorderTraversal(TreeNode root) {
//        //List<Integer> res = new ArrayList<>();
//        // edge
//        if(root == null){
//            return res;
//        }
//        if(root.left == null && root.right == null){
//            res.add(root.val);
//            return res;
//        }
//
//        inorderDfs(root);
//        return res;
//    }
//
//    public void inorderDfs(TreeNode root){
//        if(root == null){
//            return;
//        }
//        // inorder : left -> root -> right
//        inorderDfs(root.left);
//        res.add(root.val);
//        inorderDfs(root.right);
//    }

    // IDEA: BFS
    public List<Integer> inorderTraversal(TreeNode root) {

        List<Integer> res = new ArrayList<>();
        // edge
        if(root == null){
            return res;
        }
        if(root.left == null && root.right == null){
            res.add(root.val);
            return res;
        }

        // QUEUE
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        while(!q.isEmpty()){
            TreeNode _root = q.poll();
            // inorder : left -> root -> right
            if(_root.left != null){
                q.add(_root.left);
            }
            res.add(root.val);
            if(_root.right != null){
                q.add(_root.right);
            }
        }

        return res;
    }

    // LC 144
    List<Integer> res = new ArrayList<>();
    public List<Integer> preorderTraversal(TreeNode root) {
        // edge
        if(root == null){
            return res;
        }
        if(root.left == null && root.right == null){
            res.add(root.val);
            return res;
        }

        preOrderDfs(root);
        return res;
    }

    public void preOrderDfs(TreeNode root){
        if(root == null){
            return;
        }
        // preorder : root -> left  -> right
        res.add(root.val);
        preOrderDfs(root.left);
        preOrderDfs(root.right);
    }

    // LC 145
    List<Integer> res2 = new ArrayList<>();
    public List<Integer> postorderTraversal(TreeNode root) {
            // edge
            if(root == null){
                return res2;
            }
            if(root.left == null && root.right == null){
                res2.add(root.val);
                return res2;
            }

            postOrderDfs(root);
            return res2;
    }

    public void postOrderDfs(TreeNode root){
        if(root == null){
            return;
        }
        // postorder :  left  -> right -> root
        postOrderDfs(root.left);
        postOrderDfs(root.right);
        res2.add(root.val);
    }

    // LC 226
    // 10.19 - 10.29 am
    /**
     *  IDEA 1) DFS
     *  IDEA 2) BFS
     *
     */
    public TreeNode invertTree(TreeNode root) {
        // edge
        if( root == null || (root.left == null && root.right == null) ){
            return root;
        }

        // dfs
        TreeNode reversedRoot = reverseDfs(root);
        return reversedRoot;
    }

    public TreeNode reverseDfs(TreeNode root){
        if(root == null || (root.left == null && root.right == null)){
            return root;
        }
        TreeNode cacheLeft = root.left; //reverseDfs(root.left); //root.left;
        root.left = reverseDfs(root.right);
        root.right = reverseDfs(cacheLeft);

        return root;
    }

    // IDEA: BFS
    public TreeNode invertTree_1(TreeNode root) {
        // edge
        if( root == null || (root.left == null && root.right == null) ){
            return root;
        }

        TreeNode res = null; // ???

        // bfs
        Stack<TreeNode> st = new Stack<>();
        st.add(root);
        while(!st.isEmpty()){
            //TreeNode left = roo
            TreeNode cur = st.pop();
            res = cur; // /???

            TreeNode leftCache = cur.left;
            res.left = cur.right;
            res.right = leftCache;

            if(cur.left != null){
                st.add(cur.left);
            }
            if(cur.right != null){
                st.add(cur.right);
            }
        }

        return res;
    }

    // LC 104
    // 10.52 - 11.02 am
    public int maxDepth(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }

        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
   }

//    int maxDepth = 0;
//    public int maxDepth(TreeNode root) {
//        // edge
//        if(root == null){
//            return 0;
//        }
//        if(root.left == null && root.right == null){
//            return 1;
//        }

        //int maxDepth = 1;
//        return Math.max(Math.max(getMaxDepthDfs(root, maxDepth), getMaxDepthDfs(root.left, maxDepth)),
//                getMaxDepthDfs(root.right, maxDepth)
//        );
//        return getMaxDepthDfs(root);
//    }

//    public int getMaxDepthDfs(TreeNode root){
//        if(root == null){
//            return 0; // ???
//        }
//
//        return Math.max(
//                Math.max(getMaxDepthDfs(root), (getMaxDepthDfs(root.left) + 1)),
//                (getMaxDepthDfs(root.right) + 1)
//        );
//
//    }

    // LC 543
    // 11.18 - 11.35 am
    /**
     *
     *  * The diameter of a binary tree is the length of the
     *   `longest` path between any two nodes in a tree.
     *   This path may or may not pass through the root.
     *
     *
     *  IDEA 1) DFS
     */
    // 4.20 - 4.30 pm
    int maxDiameter = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return 0;
        }

        getMaxDiameter(root);
        return maxDiameter; // ??
    }

    // NOTE !!! diameter = `left sub tree depth` + `right sub tree depth`
    public int getMaxDiameter(TreeNode root){
        if(root == null){
            return 0;
        }
        //int
        int leftDepth = getDepth(root.left);
        int rightDepth = getDepth(root.right);

        // ????
        maxDiameter = Math.max(maxDiameter, leftDepth + rightDepth);

        return Math.max(getMaxDiameter(root.left), getMaxDiameter(root.right)); // ???
    }

    public int getDepth(TreeNode root){
        if(root == null){
            return 0;
        }
        return Math.max(getDepth(root.left), getDepth(root.right)) + 1;
    }


//    public int diameterOfBinaryTree(TreeNode root) {
//        // edge
//        if(root == null){
//            return 0;
//        }
//        if(root.left == null && root.right == null){
//            return 0;
//        }
//
//        // ???
//        int leftMaxDiaMeter = diameterOfBinaryTree(root.left) + 1;
//        int rightMaxDiaMeter = diameterOfBinaryTree(root.left) + 1;
//
//        return Math.max(leftMaxDiaMeter, rightMaxDiaMeter);
//    }

  // LC 110
  // 3.00 - 3.10 pm
  /**
   * Given a binary tree, determine
   * if it is height-balanced.
   *
   *
   *  A height-balanced binary tree is a
   *  binary tree in which the
   *  depth of the two subtrees of every
   *  node `NEVER differs by more than one`.
   *
   */
  /**
   *  IDEA 1) DFS
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

      //TreeNode left = root.left;
      int leftDepth = getDepthDFS(root.left);
      int rightDepth = getDepthDFS(root.right);

      // check if `current` node is `balanced`
      if(Math.abs(leftDepth - rightDepth) > 1){
          return false;
      }

      // dfs call
      // recursively check if `sub left node` and  `sub right node` are `balanced`
      return isBalanced(root.left) && isBalanced(root.right);
    }

    public int getDepthDFS(TreeNode root){
      if(root == null){
          return 0;
      }

      return Math.max(getDepthDFS(root.left), getDepthDFS(root.right)) + 1;
    }

    // LC 111
    // 4.40 - 4.50 pm
    int miniDepth = Integer.MAX_VALUE; // ????
    public int minDepth(TreeNode root) {
      // edge
      if(root == null){
          return 0;
      }
      if(root.left == null && root.right == null){
          return 0;
      }

      // dfs call
      //getMiniDepth(root);
      return getMiniDepth(root); // ???
    }

    public int getMiniDepth(TreeNode root){
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return 0;
        }

        return Math.min(getMiniDepth(root.left), getMiniDepth(root.right)) + 1;
    }

    // LC 100
    public boolean isSameTree(TreeNode p, TreeNode q) {
        // edge
        if(p == null && q == null){
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }

        return isSameTree(p.left, q.left) &&
                isSameTree(p.right, q.right);
    }

    // LC 572
    // 5.18 pm - 5.28 pm
    /**
     *  IDEA 1) DFS
     *
     */
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if(root == null && subRoot == null){
            return true;
        }
        if(root == null || subRoot == null){
            return true;
        }

        // bfs : QUEUE !!! (FIFO)
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            TreeNode cur = q.poll();
            if(isSameTree_(cur, subRoot)){
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

    public boolean isSameTree_(TreeNode p, TreeNode q){
        // edge
        if(p == null && q == null){
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }

        return isSameTree_(p.left, q.left) &&
                isSameTree_(p.right, q.right);
    }

    // LC 235
    // 9.37 - 9.47 am
    /**
     *  IDEA 1) DFS ??
     *
     *  -> to check if
     *     1) p, q are in `left, right sub tree` separately,
     *        is yes, ancestor is `current node`
     *     2) if p, q are both in `left sub` tree,
     *        keep searching on left sub tree
     *     3) ... right...
     *
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // edge
        if(root == null){
            return root; // ??
        }
        if(root.left  == null && root.right == null){
            return root; // ???
        }
        if(root.left == null || root.right == null){
            return root; // ??
        }
        // dfs call

        return null;
    }

//    public boolean isSubtree_(TreeNode root, TreeNode subRoot) {
//        // edge
//        if (root == null && subRoot == null) {
//            return true;
//        }
//        // BFS : use `QUEUE` (FIFO)
//        Queue<TreeNode> q = new LinkedList<>();
//        q.add(root);
//        while (!q.isEmpty()) {
//            TreeNode cur = q.poll();
//            if (isSameTree_0(cur, subRoot)) {
//                return true;
//            }
//            if (cur.left != null) {
//                q.add(cur.left);
//            }
//            if (cur.right != null) {
//                q.add(cur.right);
//            }
//        }
//        return false;
//    }
//
//    // same tree : LC 100
//    private boolean isSameTree_0(TreeNode p, TreeNode q) {
//        if (p == null && q == null) {
//            return true;
//        }
//        if (p == null || q == null) {
//            return false;
//        }
//        if (p.val != q.val) {
//            return false;
//        }
//        return isSameTree_0(p.left, q.left) && isSameTree_0(p.right, q.right);
//    }

    // LC 701
    // 10.33 - 10.43 am
    /**
     *  BINARY SEARCH TREE (BST)
     *
     *       ->  node.val < node.right.val &&  node.val > node.left.val
     *       ->  so we can leverage property above
     *
     *
     *  IDEA 1) DFS (RECURSION)
     *
     */
    public TreeNode insertIntoBST(TreeNode root, int val) {
        // edge case
        if(root == null){
            return new TreeNode(val);
        }
        if(root.left == null && root.right == null){
            if(root.val < val){
                root.right = new TreeNode(val);
            }else{
                root.left = new TreeNode(val);
            }
            return root;
        }

        // ???
        insertNodeHelper(root, val);
        return root;
    }

    public void insertNodeHelper(TreeNode root, int val) {
        if (val < root.val) {
            if (root.left == null) {
                root.left = new TreeNode(val);
            } else {
                insertNodeHelper(root.left, val);
            }
        } else {
            if (root.right == null) {
                root.right = new TreeNode(val);
            } else {
                insertNodeHelper(root.right, val);
            }
        }
    }

//    public TreeNode insertNodeHelper(TreeNode root, int val){
//        // edge case
//        if(root == null){
//            return new TreeNode(val);
//        }
//        if(root.val < val){
//            // ??? with or without `return`
//            return insertIntoBST(root.right, val);
//        }
//        if(root.val > val){
//            return insertIntoBST(root.left, val);
//        }
//
//        return root;
//    }

    // LC 450
    // 6.03 - 6.13 pm
    /**   IDEA 1) DFS
     *
     *  NOTE !!! we're NOT deleting a node,
     *        instead, just `reconnect` to the `new node`
     *        so it's equivalent to the tree deletion
     *
     *  ->
     *    step 1) traverse over tree, till meet the node with `same key`
     *    step 2) check if `left sub tree exist`
     *           -> if no, simply reconnect to `right sub tree`
     *               (since ALL right sub tree >= left sub tree,
     *                so NOTHING to fix if reconnect to right sub tree)
     *           -> if yes, `move over `sub left tree`, till the END.
     *                      then reconnect
     *                (fine the `minimum` node in left sub tree,
     *                 then we reconnect, and we're sure that the `fixing step`
     *                 is simplest)
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        // edge
        if(root == null){
            return null;
        }
        if(root.left == null && root.right == null){
            if(root.val == key){
                return null;
            }
            return root;
        }

        // dfs call
        deleteNodeHelper(root, key);
        return root;
    }

    public void deleteNodeHelper(TreeNode root, int key){
        // edge
        if(root == null){
            //return null;
            return;
        }
        // if found the `root`
        if(root.val == key){
            // case 1) root.left is null
            if(root.left == null){
                //root.
                //return root
                root = root.right; // ???
            }
            // case 2) root.left is NOT null
            else{
                // move till the end of sub left tree
                while(root.left != null){
                    root = root.left;
                    // /??
                   // root = root; // /??
                }

            }
        }

       // return null;
    }

//    public TreeNode deleteNode(TreeNode root, int key) {
//        // edge
//        if(root == null){
//            return root;
//        }
//        if(root.left == null && root.right == null){
//            if(root.val == key){
//                return null;
//            }
//            return root;
//        }
//        // check if `val` is in BST
//        boolean isExisted = false;
//        checkIfExisted(root, key);
//        // ???
//        if(!isExisted){
//            return root;
//        }
//
//        deleteNodeHelper(root, key);
//
//        return root;
//    }
//
//    public void deleteNodeHelper(TreeNode root, int key){
//       // if(root.)
//        return;
//    }

    public boolean checkIfExisted(TreeNode root, int key){
        // edge
        if(root.val == key){
            return true; // ?
        }
        if(root.val > key){
            return checkIfExisted(root.left, key);
        }else if (root.val < key){
            return checkIfExisted(root.right, key);
        }

        return false; // ONLY none of node's val equals to key, then this line of code is reached
    }

    // LC 199
    // 4.02 pm - 4.15 pm
    // IDEA : BFS
    public List<Integer> rightSideView(TreeNode root) {
        // edge
        if(root == null){
            return new ArrayList<>();
        }
        if(root.left == null && root.right == null){
            return new ArrayList<>(root.val); // ??
        }

        List<List<Integer>> cache = new ArrayList<>();
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();

        int layer = 0;
        q.add(root);

        while(!q.isEmpty()){

            if(layer >= cache.size()){
                cache.add(new ArrayList<>());
            }

            // NOTE !! below, we need to get `right side of view`
            TreeNode cur = q.poll();
            cache.get(layer).add(cur.val);

            if(cur.left != null){
                q.add(cur.left);
            }
            if(cur.right != null){
                q.add(cur.right);
            }

            layer += 1;
        }

        for(List<Integer> x: cache){
            if(!x.isEmpty()){
                res.add(x.get(x.size()-1));
            }
        }

        return res;
    }

    // LC 427
    // 4.36 - 4.46 pm
//    class Node {
//        public boolean val;
//        public boolean isLeaf;
//        public Node topLeft;
//        public Node topRight;
//        public Node bottomLeft;
//        public Node bottomRight;
//
//
//        public Node() {
//            this.val = false;
//            this.isLeaf = false;
//            this.topLeft = null;
//            this.topRight = null;
//            this.bottomLeft = null;
//            this.bottomRight = null;
//        }
//
//        public Node(boolean val, boolean isLeaf) {
//            this.val = val;
//            this.isLeaf = isLeaf;
//            this.topLeft = null;
//            this.topRight = null;
//            this.bottomLeft = null;
//            this.bottomRight = null;
//        }
//
//        public Node(boolean val, boolean isLeaf, Node topLeft, Node topRight, Node bottomLeft, Node bottomRight) {
//            this.val = val;
//            this.isLeaf = isLeaf;
//            this.topLeft = topLeft;
//            this.topRight = topRight;
//            this.bottomLeft = bottomLeft;
//            this.bottomRight = bottomRight;
//        }
//    }
//
//    public Node construct(int[][] grid) {
//
//        return null;
//    }

    // LC 1448
    // 4.53 - 5.03 pm
    /**
     * Given a binary tree root, a node X in the tree is named good if in
     * the path from root to X there are `no nodes with a value greater than X`.
     *
     *
     *
     * in the path from root to X there are no nodes with a value greater than X.
     *
     *  IDEA 1) DFS
     *
     *  -> record `root val`, then recursively visiting all nodes
     *     , on the same time, maintain the `till now MAX val`, and
     *     compare with current node `node.val > max_till_now` then
     *     count += 1
     */
    int goodNodeCnt = 1;
    public int goodNodes(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
//        if(root.left == null && root.right == null){
//            return 1;
//        }
        getGoodCnt(root, root.val);
        return goodNodeCnt;
    }

    // ??? use void or TreeNode as return type ???
    public void getGoodCnt(TreeNode root, int maxValTillNow){
        // edge
        if(root == null){
            return;
        }

        // ???
//        if(root.left == null && root.right == null){
//            return;
//        }

        if(root.val >= maxValTillNow){
            goodNodeCnt += 1;
        }

        // update maxValTillNow
        maxValTillNow = Math.max(maxValTillNow, root.val);

        getGoodCnt(root.left, maxValTillNow);
        getGoodCnt(root.right, maxValTillNow);
    }

  // LC 98
  // 5.33 - 5.43 pm
  /**
   * Given the root of a binary tree,
   *
   * -> determine if it is a `valid` binary search tree (BST).
   *
   * A valid BST is defined as follows:
   *
   *  The left subtree of a node contains only nodes with keys less than the node's key.
   *  The right subtree of a node contains only nodes with keys greater than the node's key.
   *  Both the left and right subtrees must also be binary search trees.
   *
   *  -> e.g. a valid BST
   *       -> root node and ALL of its sub node need to omit below
   *       -> root.left.val < root.val < root.right.val
   *
   */
  public boolean isValidBST(TreeNode root) {
      // edge
      if(root == null){
          return true;
      }
      if(root.left == null && root.right == null){
          return true;
      }

      // ???
      return checkIsValidBst(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public boolean checkIsValidBst(TreeNode node, int maxTillNow, int smallTillNow){
        // edge
        if(node == null){
            return true;
        }
//        if(node.left != null && node.right != null){
//            if(node.val >= node.right.val){
//                return false;
//            }
//            // ???
//            if(node.val <= node.left.val){
//                return false;
//            }
//        }
//        if(node.left == null || node.right == null){
//            if(node.left != null){
//                if(node.left != null && node.val <= node.left.val){
//                    return false;
//                }
//            }else{
//                if(node.right != null && node.val >= node.right.val){
//                    return false;
//                }
//            }
//        }
        if(node.val >= maxTillNow || node.val <= smallTillNow){
            return false;
        }

        return
               checkIsValidBst(node.left, node.val, smallTillNow) &&
               checkIsValidBst(node.right, maxTillNow, node.val);
    }

    // LC 105
    // 3.27 - 3.37 pm
    /**
     *  IDEA 1) RECURSIVE
     *
     *   preorder -> find `root`, then `width` is the dist between left and root
     *   inorder -> can get the sub left, right node
     *
     *
     *   repeat above ...
     *
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
      // edge
      if (preorder == null || preorder.length == 0){
          return null;
      }
      if(preorder.length == 1){
          return new TreeNode(preorder[0]);
      }

      int rootVal = preorder[0];
      int width = 0;
      for(int i = 0; i < inorder.length; i++){
          if(inorder[i] == rootVal){
              width = i;
              break;
          }
      }

      TreeNode root = new TreeNode(preorder[0]);
      root.left = this.buildTree(
              // root -> left -> right
              Arrays.copyOfRange(preorder, 1,1 + width),
              // left -> root -> right
              Arrays.copyOfRange(inorder, 0, width)
      );

      root.right = this.buildTree(
                Arrays.copyOfRange(preorder, width + 1,preorder.length),
                Arrays.copyOfRange(inorder, width + 1,inorder.length)
      );

      return root;
    }

    // LC 337
    // 4.12 - 4.22 pm
    /**
     *  IDEA 1) DFS traverse over tree
     *   -> get `array of node`
     *   -> dp or greedy get the `max` amount that thief can get
     *
     *  IDEA 2) DFS with DP ???
     *
     *   EXP 1)
     *         3
     *      2    3
     *       3     1
     *
     *
     */
    public int rob(TreeNode root) {

        return 0;
    }

    // LC 1325
    // 12.51 - 1.01 pm
    // IDEA : DFS

    public TreeNode removeLeafNodes(TreeNode root, int target) {
        // edge
        if(root == null){
            return null;
        }
        if(root.left == null && root.right == null){
            if(root.val == target){
                return null;
            }
            return root;
        }

        //return deleteLeafHelper(root, target);
        TreeNode res = deleteLeafHelper(root, target);
        return res; // ??
    }

    public TreeNode deleteLeafHelper(TreeNode root, int target){
        // edge
        if(root == null){
            return null;
            //return;
        }

        root.left = deleteLeafHelper(root.left, target);
        root.right = deleteLeafHelper(root.right, target);

        if(root.val == target && root.left == null && root.right == null){
            return null;
        }

//        root.left = deleteLeafHelper(root.left, target);
//        root.right = deleteLeafHelper(root.right, target);

        return root;
    }


//    public TreeNode removeLeafNodes(TreeNode root, int target) {
//        // edge
//        if(root == null){
//            return null;
//        }
//        if(root.left == null && root.right == null){
//            if(root.val == target){
//                return null;
//            }
//            return root;
//        }
//        // dfs call
//        deleteLeafHelper(root, target);
//
//        return root; // ???
//    }
//
//    public void deleteLeafHelper(TreeNode root, int target){
//        if(root == null){
//            return;
//        }
//        // case 1) root == target
//        if(root.val == target){
//            // case 1-1) cur node's left, right sub node are all null
//            if(root.left == null && root.right == null){
//                return;
//            }else{
//                // need to do anything ???
//            }
//        }
//
//        // case 2) move left
//        deleteNodeHelper(root.left, target);
//
//        // case 3) move right
//        deleteNodeHelper(root.right, target);
//
//    }
    ///
//
//    public TreeNode removeLeafNodes_0_1(TreeNode root, int target) {
//        deleteLeafHelper(root, target);
//        //return deleteLeafHelper(root, target);
//        return root;
//    }
//
//    private void deleteLeafHelper(TreeNode root, int target) {
//        if (root == null) {
//            //return null;
//            return;
//        }
//
//        // Recursively process left and right children
////        root.left = deleteLeafHelper(root.left, target);
////        root.right = deleteLeafHelper(root.right, target);
//
//        deleteLeafHelper(root.left, target);
//        deleteLeafHelper(root.right, target);
//
//
//        // After processing children, check if current node is a target leaf
//        if (root.left == null && root.right == null && root.val == target) {
//           // return null; // remove this leaf
//            return;
//        }
//
//        //return root; // keep current node
//        return;
//    }

    // LC 124
    // 10.08 - 10.18 am
    /**
     *  IDEA 1) DFS + MAP (record path sum)
     *
     *  IDEA 2) BFS  + MAP (record path sum) ????
     *
     */
    //Map<Integer, Integer> pathMap = new HashMap();
    int maxPathSum = Integer.MIN_VALUE; //0;
    public int maxPathSum(TreeNode root) {
        // edge
        if(root == null){
            return maxPathSum;
        }
        if(root.left == null && root.right == null){
            return root.val;
        }
        // dfs
        // get max path from pathMap
        //int maxPathSum = 0;
//        for(int val: pathMap.values()){
//            maxPathSum = Math.max(val, maxPathSum);
//        }
        //getMaxPathSumHelper(root);
        //return maxPathSum;
        getMaxPathSumHelper(root);
        return maxPathSum;
    }

    public int getMaxPathSumHelper(TreeNode root){
        // edge
        if(root == null){
            //return 0;
            return 0; // ???
        }

        int leftMaxDepth = getMaxPathSumHelper(root.left);
        int rightMaxDepth = getMaxPathSumHelper(root.right);

        maxPathSum = Math.max(maxPathSum,
                root.val + leftMaxDepth + rightMaxDepth
        );

        // ???
        return  root.val + Math.max(
                getMaxPathSumHelper(root.left),
                getMaxPathSumHelper(root.right)
        );
    }

    // LC 297
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode(int x) { val = x; }
     * }
     */

    // 12.36  - 12.46 pm
    public class Codec {

        StringBuilder sb = new StringBuilder(); // ?

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            if(root == null){
                return "#"; // ?
            }
            sb.append(root.val + ","); // ???
            serialize(root.left);
            serialize(root.right);

            return sb.toString();
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {

            Queue<String> q = new LinkedList<>();
            for(String x: data.split(",")){
                q.add(x);
            }

            TreeNode res =  helper(q);
            return res;
        }

        public TreeNode helper(Queue<String> q){
            //TreeNode cur = q.poll();
            String cur = q.poll();
            if(cur == "#"){
                return null; // ??
            }
            TreeNode root = new TreeNode(Integer.parseInt(cur));
            root.left = helper(q);
            root.right = helper(q);
           // cur.left = helper(cur)
           // return null;
            return root;
        }

    }


    // 11.14 am - 11. 32 am
//    public class Codec {
//
//        // Encodes a tree to a single string.
//        // attr
//        StringBuilder sb;
//        TreeNode decodeNode;
//
//        public Codec(){'
//            this.sb = new StringBuilder();
//            this.decodeNode = new TreeNode(); // ???
//        }
//
//        // IDEA 1) DFS 2) BFS
//        public String serialize(TreeNode root) {
//            // edge
//            if(root == null){
//                return "";
//            }
//            //sb.
//            // pre-order
//            sb.append(String.valueOf(root.val) + ", ");
////            sb.append(serialize(root.left )+ ",");
////            sb.append(serialize(root.right )+ ",");
//            serialize(root.left );
//            serialize(root.right );
//
//            return sb.toString(); // ??
//        }
//
//        // Decodes your encoded data to tree.
//        // BFS ???
//        public TreeNode deserialize(String data) {
//            // edge
//            if(data == null || data.equals("")){
//                //return new TreeNode();
//                return this.decodeNode; // ??
//            }
//            Queue<String> q = new LinkedList<>();
//            TreeNode node = this.decodeNode;
//            for(String x: this.sb.toString().split(",")){
//                if(x != null){
//                    node = new TreeNode(Integer.parseInt(x)); // ??
//                }
//
//            }
//
//            return node;
//        }
//    }


//    public class Codec {
//
//        // IDEA 1) DFS 2) BFS
//        public String serialize(TreeNode root) {
//            // edge
//            if(root == null){
//                return "#";
//            }
//            return root.val + "," + serialize(root.left) + "," + serialize(root.right);
//        }
//
//        // Decodes your encoded data to tree.
//        // BFS ???
//        public TreeNode deserialize(String data) {
//            Queue<String> q = new LinkedList<>();
//            for(String x: data.split(",")){
//              q.add(x); // ???
//            }
//            return helper(q);
//           // return null;
//        }
//
//        public TreeNode helper(Queue<String> q){
//            String s = q.poll();
//            if(s.equals("#")){
//                return null;
//            }
//            TreeNode root = new TreeNode(Integer.valueOf(s));
//            //root.left = helper(q.)
//            root.left = helper(q);
//            root.right = helper(q);
//
//            return root;
//        }
//
//    }

    // LC 703
    // 12.15 - 12.25 pm
    /**
     *
     * You are tasked to implement a class which, for a given integer k,
     * maintains a stream of test scores and continuously
     * returns the kth highest test score after a new score has been submitted.
     *
     * More specifically,
     * we are looking for the kth highest score in the sorted list of all scores.
     *
     *
     *  IDEA: PQ (small PQ) (small -> big)
     */
    class KthLargest {
        int k;
        PriorityQueue<Integer> pq;

        public KthLargest(int k, int[] nums){
            this.k = k; // ???
            this.pq = new PriorityQueue<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    int diff = o1 - o2;
                    return diff;
                }
            });

            for(int x: nums){
                this.pq.add(x);
            }
        }

        public int add(int val) {

            this.pq.add(val);

            while(this.pq.size() > k){
                this.pq.poll();
            }
//            if(!this.pq.isEmpty()){
//                return this.pq.peek();
//            }
//            return -1; // ????
            return this.pq.peek();
        }

    }

    // LC 1046
    // 10.59 - 11.10 am
    /**
     *  IDEA 1) PQ (max PQ)
     *
     */
    public int lastStoneWeight(int[] stones) {
        if(stones == null || stones.length == 0){
            return 0;
        }
        if(stones.length == 1){
            return stones[0];
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        for(int x: stones){
            pq.add(x);
        }

        while(pq.size() > 1){
            int big_1 = pq.poll();
            int big_2 = pq.poll();
            int diff = big_2 - big_1;
            if(diff > 0){
                pq.add(diff);
            }
        }

        if(pq.isEmpty()){
            return 0;
        }

        return pq.peek();
    }

    // LC 973
    // 11.11 - 11.21 am
    public int[][] kClosest(int[][] points, int k) {
        // edge
        if(points == null || points.length == 0){
            return null; // ?
        }
        if(points.length <= k){
            return points;
        }

        // map : {dist, coordination)
        //Map<Double, List<int[]>> distMap = new HashMap<>();
        Map<Double, int[]> distMap = new HashMap<>();

        // small PQ ( small -> big )
//        PriorityQueue<Double> pq = new PriorityQueue<>(new Comparator<Double>() {
//            @Override
//            public int compare(int[] o1, int[] o2) {
//                int diff = (int) (o1 - o2);
//                //int diff = (int) getDist(o1, o2);
//                return diff;
//            }
//        });

        PriorityQueue<Double> pq = new PriorityQueue<>();

        for(int[] p: points){
            double dist = getDist(p[0], p[1]);
            pq.add(dist);
            //List<int[]> cur =  distMap.getOrDefault(dist, new ArrayList<>());
            //cur.add(p);
            distMap.put(dist, p);
        }

        int[][] res = new int[k][2];
        for(int i = 0; i < k; k++){
            Double distKey = pq.poll();
            res[i] = distMap.get(distKey);
        }

        return res;
    }

    public double getDist(int[] input){
        return input[0] * input[0] + input[1] * input[1];
    }

    public double getDist(int x, int y){
        return Math.sqrt(x * x  + y * y);
    }


    // LC 621
    // 11.51 - 12.10 pm
    /**
     *  IDEA 1) PQ (big PQ)
     *
     *   - use the max PQ, track `cnt of element`
     *   - everytime, pop the max val, update cnt, and add `wait`, update res
     *   - return res (total length of op)
     *
     *
     *   but there's a constraint: there has to be a gap of
     *   at least `n` intervals between two tasks with the same label.
     *
     *
     */
    public int leastInterval(char[] tasks, int n) {
        return 0;
    }

    // LC 1834
    // 5.04 - 5.14 PM
    /**
     *  You are given n​​​​​​ tasks labeled
     *  from 0 to n - 1 represented by a 2D integer array tasks,
     *  where tasks[i] = [enqueueTimei, processingTimei]
     *  means that the i th task
     * will be available to process
     *  at enqueueTimei and will take processingTimei to finish processing.
     *
     *
     *  -> Return the order in which the CPU will process the tasks.
     */
    /**
     *  IDEA 1) PQ
     *
     *  tasks[i] = [enqueueTime_i, processingTime_i]
     *
     *  ->
     *  1) use a `min PQ` that track the
     *     `task with idx, enqueue, time, process time`
     *
     *  2) every time, we pop a task with `min process time`
     *     add it to order, and update time.
     *
     *  3) .. repeat above steps
     *
     */
    // 6.23 - 6.33 pm
    /**
     *  NOTE !!!
     *
     *   we need init below 2 data structure
     *
     *   1. an array (with Task class info)
     *   2. min PQ (with Task class info)
     *
     */
    public class Task{
        int idx;
        int enqueueTime;
        int processingTime;

        public Task(int idx, int enqueueTime, int processingTime){
            this.idx = idx;
            this.enqueueTime = enqueueTime;
            this.processingTime = processingTime;
        }
    }
    public int[] getOrder(int[][] tasks) {
        // edge
        if(tasks == null || tasks.length == 0){
            return new int[]{};
        }
        if(tasks.length == 1){
            return new int[]{0}; // ?
        }

        //taskWithIndex
        List<Task> taskWithIndex = new ArrayList<>();
        for(int i = 0; i < tasks.length; i ++){
            Task task = new Task(i, tasks[i][0], tasks[i][1]);
            taskWithIndex.add(task);
        }

        // sort `taskWithIndex` with `enqueueTime`
        Collections.sort(taskWithIndex, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                int diff = o1.enqueueTime - o2.enqueueTime;
                return diff;
            }
        });

        // PQ
        PriorityQueue<Task> pq = new PriorityQueue<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                // NOTE !!! we order small PQ
                // via (process time, if same process time, compare idx) (small -> big)
                int diff = o1.processingTime - o2.processingTime;
                if(diff == 0){
                    return o1.idx - o2.idx;
                }
                return diff;
            }
        });

        int[] order = new int[tasks.length];
        int time = 0;
        int idx = 0;

        // ???
        while(idx < tasks.length){

            // ??? need to loop over taskWithIndex ???
            for(int i = 0; i < taskWithIndex.size(); i++){

                while(i < taskWithIndex.size() && taskWithIndex.get(i).enqueueTime < time){
                    int enTime = tasks[i][0];
                    int processTime = tasks[i][1];
                    pq.add(new Task(i, enTime, processTime));
                }

                if(pq.isEmpty()){
                    time = taskWithIndex.get(i).enqueueTime;
                }else{
                    Task _task = pq.poll();
                    order[idx] = _task.idx;
                    time += _task.processingTime;
                    idx += 1; // ???
                }

            }

           // idx += 1;
        }


        return order;
    }

    // LC 767
    // 11.32 - 11.45 am
    /**
     *  IDEA 1) PQ + HASHMAP
     *
     *   1. use map record `element cnt`
     *   2. use PQ pop `max cnt` element
     *   3.
     *
     *
     *   Exp 1) s = "aab"
     *
     *   -> map : {a: 2, b: 1}
     *   -> pq = [2,1]
     *
     *   res = "a", map = {a:1, b:1}, pq =[1,1]
     *   res "ab", map = {a:1}, pq = [1]
     *   res = "aba", ...
     *   -> true
     *
     *
     *   Exp 2) s = "aaab"
     *
     *   -> map : {a: 3, b: 1}
     *   -> pq = [3,1]
     *   -> res = "a", {a:2, b:1}, pq = [2,1]
     *   -> res = "ab", {a:2}, pq = [2]
     *   -> false
     *
     *
     */
    public String reorganizeString(String s) {
        // Edge cases
        if (s == null || s.length() == 0) {
            return "";
        }
        if (s.length() == 1) {
            return s;
        }

        // Count the frequency of each character
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : s.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }

        // Use a max-heap (PriorityQueue) to store characters based on their frequency
        // V1
        //PriorityQueue<Character> maxHeap = new PriorityQueue<>(Comparator.comparingInt(charCount::get).reversed());

        // V2
        PriorityQueue<Character> maxHeap = new PriorityQueue<>(new Comparator<Character>() {
            private final Map<Character, Integer> counts = charCount; // Access the charCount map

            @Override
            public int compare(Character char1, Character char2) {
                // Compare based on the frequencies from the charCount map (descending order)
                return counts.get(char2) - counts.get(char1);
            }
        });

        for (char c : charCount.keySet()) {
            maxHeap.offer(c);
        }

        StringBuilder result = new StringBuilder();
        Character prevChar = null;
        int idx = 0;

        while (!maxHeap.isEmpty()) {
            boolean isFound = false;
            Character currentChar = maxHeap.poll();
            result.append(currentChar);
            charCount.put(currentChar, charCount.get(currentChar) - 1);

            if (prevChar != null && charCount.get(prevChar) > 0) {
                isFound = true;
                maxHeap.offer(prevChar);
            }
            prevChar = currentChar;

            // early terminated ??
            if(!isFound && idx != 0){
                return ""; // ??
            }

            idx += 1;
        }

        // If the length of the reorganized string is not equal to the original length,
        // it means it was not possible to reorganize the string according to the rules.
        return result.length() == s.length() ? result.toString() : "";
    }


//    public String reorganizeString(String s) {
//        // edge
//        if(s == null || s.length() == 0){
//            return null;
//        }
//        if(s.length() == 1){
//            return s;
//        }
//        if(s.length() == 2){
//            if(s.charAt(0) == s.charAt(1)){
//                return s;
//            }
//            return null;
//        }
//        // ???
//        // map : {k: cnt}
//        Map<String, Integer> cntMap = new HashMap<>();
//        // PQ : max PQ (big -> small)
//        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o2 - o1;
//                return diff;
//            }
//        });
//
//        for(String x: s.split("")){
//            cntMap.put(x, cntMap.getOrDefault(x, 0) + 1);
//        }
//
//        for(String k: cntMap.keySet()){
//            pq.add(cntMap.get(k));
//        }
//
//        System.out.println(">>> cntMap = " + cntMap);
//        System.out.println(">>> pq = " + pq);
//
//        StringBuilder sb = new StringBuilder();
//
//        //boolean isFound = false;
//        String prev = null;
//
//        while (!pq.isEmpty()){
//
//            int cnt = pq.poll();
//            boolean isFound = false;
//            //prev = s.charAt()
//
//            System.out.println(">>> cnt = " + cnt + ", res = " + res);
//
//            for(String k: cntMap.keySet()){
//                System.out.println(">>> k = " + k + ", prev = " + prev);
//                if(cntMap.get(k) == cnt && k != prev){
//                    //res += k;
//                    sb.append(k);
//                    prev = k;
//                    isFound = true;
//                    // update map, PQ
//                    cntMap.put(k, cntMap.get(k));
//                    pq.add(cnt-1); // ??
//                }
//            }
//
//            if(!isFound){
//                return null; // ???
//            }
//        }
//
//        return sb.toString().length() > 0 ? sb.toString() : null;
//    }

    // LC 1405
    // 1.09 - 1.19 pm
    /**
     *  IDEA 1) PQ + MAP
     *  (similar as LC 737 ???)
     *
     *
     *  PQ -> track element with most cnt
     *
     *  1. append to res, till reach `len=2`
     *  2. update map, PQ accordingly
     *  3. return res
     *
     */
    public String longestDiverseString(int a, int b, int c) {

        StringBuilder sb = new StringBuilder();

        // edge
        if(a == 0 && b == 0 && c == 0){
            return null;
        }
        if(a < 3 && b < 3 && c < 3){
            sb.append(getStrProduct(a, "a"));
            sb.append(getStrProduct(b, "b"));
            sb.append(getStrProduct(c, "c"));
            return sb.toString();
        }

        Map<String, Integer> cntMap = new HashMap<>();
        cntMap.put("a", a);
        cntMap.put("b", b);
        cntMap.put("c", c);

        PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {

            // NOTE !! below
            private final Map<String, Integer> counts = cntMap;

            @Override
            public int compare(String o1, String o2) {
                return counts.get(o2) - counts.get(o1);
            }
        });

        StringBuilder sb2 = new StringBuilder();

        while(!pq.isEmpty()){

            String cur = pq.poll();
            int len = sb2.toString().length();

            if(len < 3){
                sb.append(cur);
                cntMap.put(cur, cntMap.get(cur) - 1);
            }

            else if(len >= 3){
                String prev1 = String.valueOf(sb2.toString().charAt(len - 2));
                String prev2 = String.valueOf(sb2.toString().charAt(len - 1));
                // ???
                // case 1) can append cur
                if( ! ( (prev1 == prev2) && (prev2 == cur) ) ){
                    sb.append(cur);
                    // update map, PQ
                    cntMap.put(cur, cntMap.get(cur) - 1);
                    //pq.add(cur - 1);
                }
                // case 2) can't append cur (e.g. aa "a" , or bb "b"..)
                else{

                }
            }

        }


        // ???
        return sb2.toString().length() > 0 ? null : sb2.toString();
    }

    public String getStrProduct(int cnt, String x){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < cnt; i ++){
            sb.append(x);
        }
        return sb.toString();
    }

    // LC 1094
    // 10.21 - 10.31 am
    /**
     *  Given a list of trips,
     *  trip[i] = [num_passengers, start_location, end_location]
     *
     *   -> num_passengers : the number of passengers that must be picked up,
     *   -> start_location :  the locations to pick them up.
     *   -> end_location :  the locations to drop them off.
     *
     *
     *  -> Return `true` if and only if
     *     it is possible to pick up and drop off
     *     all passengers for all the given trips.
     *
     *
     *   Constraints:
     *
     *  1 <= trips.length <= 1000
     *  trips[i].length == 3
     *  1 <= numPassengersi <= 100
     *  0 <= fromi < toi <= 1000
     *  1 <= capacity <= 105
     *
     *
     *
     */
    /**  IDEA 1) PREFIX SUM ???
     *
     *  -> maintain a `cum-sum` passenger count
     *  -> ans check if `capacity` can handle it ( >= )
     *  -> if not, return false directly
     *  -> otherwise, if can reach the end, return true
     *
     *
     *   Exp 1)
     *
     *   Input: trips = [[2,1,5],[3,3,7]], capacity = 4
     *   Output: false
     *
     *
     *   prefix = [2
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    public boolean carPooling(int[][] trips, int capacity) {
        // edge
        if(trips == null || trips.length == 0){
            return true;
        }
        if(trips.length == 1){
            return capacity > trips[0][0];
        }

        // init prefix sum
        int[] prefixSum = new int[1001]; // ??
      //  int cumsum = 0;
        for(int[] t: trips){
            int amount = t[0];
            int start = t[1];
            int end = t[2];
           // cumsum += amount;
            // ???
//            for(int i = start; i < end; i++){
//                prefixSum[i] += cumsum; // ???
//            }

            prefixSum[start] += amount;
            prefixSum[end] -= amount;
        }

        // update `prefix sum` array
        for(int i = 1; i < prefixSum.length; i++){
            prefixSum[i] += prefixSum[i-1];
        }

        // check if `possible` to get all passenger
        for (int j : prefixSum) {
            if (capacity < j) {
                return false;
            }
        }

        return true;
    }

    // LC 502
    // 11.17 - 11. 27 am
    /**
     * You are given several projects.
     * For each project i, it has a pure profit Pi
     * and a minimum capital of Ci is needed
     * to start the corresponding project.
     *
     *
     *
     * Initially, you have `W` capital.
     * When you finish a project,
     * you will obtain its pure profit and the profit
     * will be added to your total capital.
     *
     * ->
     *
     *  k : it can only finish at most k distinct projects before the IPO
     *  w: Initially, you have `W` capital.
     *  Profits : For each project i, it has a pure profit Pi
     *  Capital : (continue above) a minimum capital of Ci is needed
     *
     *
     *
     */
    /**
     *  IDEA 1)  PQ (max PQ)
     *
     *   -> we use MAX PQ to track the `most profit`
     *   -> 1) go through `Capital`, find the capital = 0, as start point, and get its profit
     *      2) init a new PQ, collect capitals which <= current profit
     *      3) pop from above PQ (as the new selected profit), repeat step 1) - step 3)
     *      ...
     *
     *      return final profit as result
     */
    // 1.14 - 1.24 pm
    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {



        return 0;
    }

    // LC 295
    // Find Median from Data Stream
    // IDEA 1) SORTING
//    class MedianFinder {
//
//        //int size;
//        List<Integer> collected;
//
//        public MedianFinder() {
//           // this.size = 0;
//            this.collected = new ArrayList<>();
//        }
//
//        public void addNum(int num) {
//           // this.size += 1;
//            this.collected.add(num); // ??
//            // sort again
//            sorting();
//        }
//
//        public double findMedian() {
//            int size = this.collected.size();
//            // edge
//            if(size == 2){
//                return (this.collected.get(0) + this.collected.get(1)) / 2.0;
//            }
//            if(size % 2 == 1){
//                this.collected.get(size / 2); // ??
//            }
//            int idx1 = size / 2;
//            int idx2 = (size / 2) - 1;
//            //double val = ( this.collected.get(idx1) + this.collected.get(idx2) ) / 2.0;
//            return ( this.collected.get(idx1) + this.collected.get(idx2) ) / 2.0;
//        }
//
//        private void sorting(){
//            Collections.sort(this.collected);
//        }
//    }

    // IDEA 2) PQ
    /**
     *
     *  IDEA : PQ
     *
     *  - init small, big PQ
     *
     *   - small PQ : maintain ALL element <= `medium val`
     *   - big PQ :  maintain ALL element > `medium val`
     *
     *   -> so if `total size` % 2 == 0 -> medium = ( top_small_pq + top_big_pq ) / 2
     *         if `total size` % 2 == 1 -> medium =  top_small_pq
     *
     *
     */
    class MedianFinder {

        PriorityQueue<Integer> small_pq;
        PriorityQueue<Integer> big_pq;

        public MedianFinder() {

            small_pq = new PriorityQueue<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    int diff = o1 - o2;
                    return diff;
                }
            });

            big_pq = new PriorityQueue<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    int diff = o2 - o1;
                    return diff;
                }
            });

        }

        public void addNum(int num) {

            if(this.small_pq.isEmpty()){
                this.small_pq.add(num);
            }else{
                int smallest = this.small_pq.peek();
                if(num < smallest){
                    this.small_pq.add(num);
                }else{
                    this.big_pq.add(num);
                }
            }

            // need to `rebalance` small, big PQ ???

        }

        public double findMedian() {

            int size = this.small_pq.size() + this.big_pq.size();

            if(size % 2 == 1){
                if(!this.small_pq.isEmpty()){
                    return this.small_pq.peek(); // ???
                }
                return -1; // handle null pointer here, but should NOT happen ???
            }

            int val1 = 0;
            int val2 = 0;

            if(!this.small_pq.isEmpty()){
                val1 = this.small_pq.peek();
            }

            if(!this.big_pq.isEmpty()){
                val2 = this.big_pq.peek();
            }

            return (val1 + val2) / 2.0; // ???
        }

    }

    // LC 78
    // 9.41 - 9.51 am
    // IDEA: BACKTRACK
    List<List<Integer>> res_subsets = new ArrayList<>();
    public List<List<Integer>> subsets(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }

        // backtrack call
        getSubSets(nums, new ArrayList<>(), 0);
        return res_subsets;
    }

    public void getSubSets(int[] nums, List<Integer> cur, int start_idx){

        if(cur.size() > nums.length){
            return;
        }

        // sort
        Collections.sort(cur);
        if(!res_subsets.contains(cur)){
            res_subsets.add(new ArrayList<>(cur));
        }

        // ??
        for(int j = start_idx; j < nums.length; j++){
//            if(!cur.contains(nums[j])){
//                cur.add(nums[j]);
//                getSubSets(nums, cur, start_idx + 1); //???
//                // undo
//                cur.remove(cur.size() - 1);
//            }
            cur.add(nums[j]);
            getSubSets(nums, cur, start_idx + 1); //???
            // undo
            cur.remove(cur.size() - 1);
        }

    }


    // LC 90
    // 10.22 - 10.32 am
//    public List<List<Integer>> subsetsWithDup(int[] nums) {
//
//        return null;
//    }

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        /**
         *  NOTE !!!
         *
         *   in order to skip duplicates via `compare cur and prev val`,
         *   we need to sort array first
         */
        Arrays.sort(nums);
        backtrack(list, new ArrayList<>(), nums, 0);
        return list;
    }

    private void backtrack(List<List<Integer>> list, List<Integer> tempList, int [] nums, int start){
        list.add(new ArrayList<>(tempList));
        for(int i = start; i < nums.length; i++){
            // skip duplicates
            /**
             *  NOTE !!!
             *
             *   below is the key shows how to simply skip duplicates
             *   (instead of using hashmap counter)
             */
            if(i > start && nums[i] == nums[i-1]){
                continue;
            }
            tempList.add(nums[i]);
            backtrack(list, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    // LC 39
    // 11.01 - 11.23 am
    // IDEA: BACKTRACK (without start_idx !!!)
    List<List<Integer>> combineSumRes = new ArrayList<>();
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        // edge
        if(candidates == null || candidates.length == 0){
            return null; // ??
        }

        List<Integer> cur =  new ArrayList<>();
        getCombineSumHelper(candidates, target, 0, cur);
        return combineSumRes;
    }

    public void getCombineSumHelper(int[] candidates, int target, int start_idx, List<Integer> cur){
        if(getArraySum(cur) > target){
            return;
        }

        if(getArraySum(cur) == target){
            // sort
            Collections.sort(cur);
            if(!combineSumRes.contains(cur)){
                combineSumRes.add(new ArrayList<>(cur));
            }
        }

        for(int i = start_idx; i < candidates.length; i++){
            cur.add(candidates[i]);
            getCombineSumHelper(candidates, target, start_idx + 1,cur);
            // undo
            cur.remove(cur.size() - 1);
        }

    }

    public int getArraySum(List<Integer> input){
        int res = 0;
        for(int x: input){
            res += x;
        }
        return res;
    }

    // LC 40
    // 2.08 - 2.24 pm
    /**
     *
     *  -> Each number in candidates may only be used
     *     `once` in the combination.
     *
     *  IDEA 1) BACKTRACK + start_idx
     *
     */
    List<List<Integer>> comRes = new ArrayList<>();
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        // edge
        if(candidates == null || candidates.length == 0){
            return new ArrayList<>();
        }
        if(candidates.length == 1){
            if(candidates[0] == target){
                return new ArrayList<>(new ArrayList<>(candidates[0]));
            }
            return new ArrayList<>();
        }

        // NOTE !!! we sort array first
        Arrays.sort(candidates);
        System.out.println(">>> (sorted) candidates =  " + candidates);

        combinationSum2Helper(candidates, target, 0, new ArrayList<>());

        // check if `element counts are within candidates count`
        Map<Integer, Integer> cntMap = new HashMap<>();
        for(int c: candidates){
            cntMap.put(c, cntMap.getOrDefault(c, 0) + 1);
        }

        List<List<Integer>> comRes2 = new ArrayList<>();
        for(List<Integer> x : comRes){
            if(isValid(cntMap, x)){
                comRes2.add(x);
            }
        }

        return comRes2;
    }

    public void combinationSum2Helper(int[] candidates, int target, int start_idx, List<Integer> cur){

        int tmpSum = getArraySum2(cur);
        if(tmpSum == target){
            if(!comRes.contains(cur)){
                comRes.add(new ArrayList<>(cur));
            }
        }

        if(tmpSum > target){
            //continue;
            return; // ??
        }

        // NOTE !!!
        //      -> i start from `start_idx`
        //      -> use `i` in the recursive call
        for(int i = start_idx; i < candidates.length; i++){
            // NOTE !!! via below, we avoid use `duplicated
            cur.add(candidates[i]);
            combinationSum2Helper(candidates, target, i, cur);
            // undo
            cur.remove(cur.size() - 1);
        }

    }

    public int getArraySum2(List<Integer> input){
        int res = 0;
        for(int x: input){
            res += x;
        }
        return res;
    }

    public boolean isValid(Map<Integer, Integer> cntMap, List<Integer> arr){
        Map<Integer, Integer> arrMap = new HashMap<>();
        System.out.println(">>> arrMap = " + arrMap + ", cntMap = " + cntMap);
        for(int c: arr){
            arrMap.put(c, arrMap.getOrDefault(c, 0) + 1);
        }
        for(int k: arrMap.keySet()){
            if(arrMap.get(k) > cntMap.get(k)){
                return false;
            }
        }

        return true;
    }

    // LC 77
    // 3.21 - 3.31 pm
    // Combinations
    /**
     *  IDEA 1) : BACKTRACK
     *
     */
    List<List<Integer>> combineRes = new ArrayList<>();
    public List<List<Integer>> combine(int n, int k) {
        // edge
        if(n == 0 || k == 0){
            return new ArrayList<>();
        }
        if(n == 1){
            List<Integer> tmp = new ArrayList<>();
            tmp.add(1);
            combineRes.add(tmp);
            return combineRes;
        }

//        List<Integer> candidates = new ArrayList<>();
//        for(int i = 1; i < n+1; i++){
//            candidates.add(i);
//        }

        combineHelper(n, k, 1, new ArrayList<>());

        return combineRes;
    }

    public void combineHelper(int n, int k, int start_idx, List<Integer> cur){

        if(cur.size() > k){
            return;
        }

        if(cur.size() == k){
            // sort
            //Collections.sort(cur);
            if(!combineRes.contains(cur)){
                combineRes.add(new ArrayList<>(cur));
            }

            return;  // ??
        }

        // NOTE !!! start_idx as `start idx`
        for(int i = start_idx; i < n + 1; i++){
            //int x = candidates.get(i);
            int val =  i;
            if(!cur.contains(val)){
                cur.add(val);
                combineHelper(n, k, i, cur);
                // undo
                cur.remove(cur.size() - 1);
            }
        }

    }

    // LC 46
    // Permutations
    // 4.04 - 4.14 pm
    /**
     *
     * Given an array nums of distinct integers,
     * return all the possible permutations.
     * You can return the answer in any order.
     *
     *
     * -> A permutation is a rearrangement of all the elements of an array.
     *
     *
     *  IDEA: BACKTRACK (no need start_idx !!!)
     *
     */
    List<List<Integer>> permuteRes = new ArrayList<>();
    public List<List<Integer>> permute(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return new ArrayList<>();
        }
        if(nums.length == 1){
            List<Integer> tmp = new ArrayList<>();
            tmp.add(nums[0]);
            permuteRes.add(tmp);
        }

        permuteHelper(nums, new ArrayList<>());
        return permuteRes;
    }

    public void permuteHelper(int[] nums, List<Integer> cur){
        if(cur.size() > nums.length){
            return;
        }
        if(cur.size() == nums.length){
            if(!permuteRes.contains(cur)){
                permuteRes.add(new ArrayList<>(cur));
            }
        }

        for(int i = 0; i < nums.length; i++){
            // ?? needed
            if(!cur.contains(nums[i])){
                cur.add(nums[i]);
                permuteHelper(nums, cur);
                // undo
                cur.remove(cur.size() - 1);
            }
        }

    }

    // LC 47
    // 3.47 - 3.58 pm
    List<List<Integer>> permuteRes2 = new ArrayList<>();
    public List<List<Integer>> permuteUnique(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return new ArrayList<>();
        }
        if(nums.length == 1){
            List<Integer> tmp = new ArrayList<>();
            tmp.add(nums[0]);
            permuteRes2.add(tmp);
        }

        permuteHelper2(nums, new ArrayList<>());
        return permuteRes2;
    }

    public void permuteHelper2(int[] nums, List<Integer> cur){
        if(cur.size() > nums.length){
            return;
        }
        if(cur.size() == nums.length){
            if(!permuteRes2.contains(cur)){
                permuteRes2.add(new ArrayList<>(cur));
            }
        }

        for(int i = 0; i < nums.length; i++){
            // ?? needed
            /**
             *  NOTE !!! below trick (skip `duplicated element` in same layer)
             *
             *  condition ????
             */
            if(nums[i] != nums[i-1]){
                cur.add(nums[i]);
                permuteHelper2(nums, cur);
                // undo
                cur.remove(cur.size() - 1);
            }
        }

    }

    // LC 79
    // 3.50 - 4. 00 pm
    // IDEA: BACKTRACK + DFS
    public boolean exist(char[][] board, String word) {

        // edge
        if(board == null || board.length == 0){
            return false;
        }
        if(word == null || word.length() == 0){
            return true;
        }

        // dfs
        int l = board.length;
        int w = board[0].length;

        // [ [x_1, y_1], [x_2, y_2], ... ]
        List<int[]> startPoints = new ArrayList<>();
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                if(board[i][j] == word.charAt(0)){
                    //List<Integer> tmp = new ArrayList<>();
//                    int[] tmp = new int[2];
////                    tmp[0] = j;
////                    tmp[1] = i;
                    //int[] tmp = new int[]{j, i};
                    startPoints.add(new int[]{j, i});
                }
            }
        }

        // terminate early (if no start points)
        if(startPoints.isEmpty()){
            return false;
        }

        for(int[] point: startPoints){

            int start_idx = 0;
            boolean[][] visited = new boolean[l][w]; // default : false ... ??

            if(dfsFind(board, word, point[0], point[1], visited, start_idx)){
                return true;
            }
        }

        return false;
    }

    public boolean dfsFind(char[][] board, String word, int x, int y, boolean[][] visited, int start_idx){

        int l = board.length;
        int w = board[0].length;

        // NOTE !!! below condition
        // found a solution (word)
        if(start_idx == word.length()){
            return true;
        }
        // should not visit below, but in case ...
        if(start_idx > word.length()){
            return false; // ??
        }

        // check if 1) out of boundary 2) idx not equals 3) NOT YET visited
        if( x < 0 || x >= w || y < 0 || y >= l || word.charAt(start_idx) != board[y][x] || visited[y][x]){
            return false; // ???
        }

        visited[y][x] = true;
        //start_idx += 1;

        // note !!! below trick
        // if `ANY` of the recursive call (4 direction) found a word, true directly
        if( dfsFind(board, word, x+1, y, visited, start_idx + 1) ||
            dfsFind(board, word, x, y+1, visited, start_idx + 1) ||
            dfsFind(board, word, x-1, y, visited, start_idx + 1 ) ||
            dfsFind(board, word, x, y-1, visited, start_idx + 1)
        ){
            return true;
        }

        // undo
        visited[y][x] = false;
        start_idx -= 1; // ???

        return false;
    }

    // LC 131
    // 4.37 - 4.47 pm
    /**
     *  IDEA 1) BACKTRACK + PALINDROME CHECK
     *  IDEA 2) TWO POINTER (??
     *  IDEA 3) DP ???
     *
     */
    List<List<String>> partitionRes = new ArrayList<>();
    public List<List<String>> partition(String s) {
        // edge
        if(s == null || s.isEmpty()){
            return new ArrayList<>();
        }
        if(s.length() == 1){
            List<String> tmp = new ArrayList<>();
            tmp.add(s);
            partitionRes.add(tmp);
            return partitionRes;
        }

        // double loop ???
        // ?? optimize way ???
        for(int i = 0; i < s.length()-1; i++){
            List<String> tmp = new ArrayList<>();
            for(int j = i; j < s.length(); j++){
                //List<String> tmp = new ArrayList<>();
                if(j == i){
                    tmp.add(String.valueOf(s.charAt(j)));
                }
                else if(isPalindrome(String.copyValueOf(s.toCharArray(), i, j))){
                    // ???
                    //String.copyValueOf(s.toCharArray(), i, j);
                    tmp.add(String.copyValueOf(s.toCharArray(), i, j));
                };
            }
            if(!tmp.isEmpty()){
                partitionRes.add(tmp);
            }
        }

        return partitionRes;
    }

    public boolean isPalindrome(String x){
        int l = 0;
        int r = x.length() - 1;
        while(r > l){
            if(x.charAt(l) != x.charAt(r)){
                return false;
            }
            r -= 1;
            l += 1;
        }
        return true;
    }

//    public void partitionHelper(String s, StringBuilder sb){
//
//    }

    // LC 17
    // 5.28 - 5.38 pm
    // IDEA: BACKTRACK
    List<String> letterRes = new ArrayList<>();
    public List<String> letterCombinations(String digits) {

        HashMap<String, String> letters = new HashMap<>();
        letters.put("2", "abc");
        letters.put("3", "def");
        letters.put("4", "ghi");
        letters.put("5", "jkl");
        letters.put("6", "mno");
        letters.put("7", "pqrs");
        letters.put("8", "tuv");
        letters.put("9", "wxyz");

        // edge
        if(digits == null || digits.isEmpty()){
            return letterRes;
        }
        if(digits.length() == 1){
            String val = letters.get(String.valueOf(digits.charAt(0)));
            for(String x : val.split("")){
                letterRes.add(x);
            }
            return letterRes;
        }

        // backtrack
        letterResHelper(digits, letters, 0, new StringBuilder());
        return letterRes;
    }

    public void letterResHelper(String digits, HashMap<String, String> letters, int start_idx , StringBuilder sb){
        String str = sb.toString();
        if (str.length() == digits.length()){
            if(!letterRes.contains(str)){
                letterRes.add(str); // ??
            }
        }
        // ???
        if (str.length() > digits.length()){
            return; // ???
        }

        // ???
        for(int i = 0; i < digits.length(); i ++){
            String k = String.valueOf(digits.charAt(i));
            for(String x: letters.get(k).split("")){
                String _str = sb.toString();

                boolean shouldProcced = _str.isEmpty() ||
                        !_str.contains(x) ||
                        (_str.length() > 0 && letters.get(_str.charAt(_str.length()-1)) != x);

                if(shouldProcced){
                    sb.append(x);
                    letterResHelper(digits, letters, i, sb); // ???
                    // undo
                    sb.deleteCharAt(sb.length() - 1); // ??
                }
            }
        }

    }

    // LC 473
    // 10.11 - 10.28 am
    /**
     *  Step 1) check if `cnt == 4`
     *   -> if yes, compare there are all equals directly
     *   -> if no, get the `element cnt map`
     *       -> and loop over the `least cnt val`
     *          -> to see if it's possible to merge them, and form a  `square`
     *
     *
     *
     *   Exp 1)
     *
     *    input = [1,1,2,2,2]
     *    cnt_map = {1: 2, 2: 3}
     *
     *
     *    -> total len = 8
     *     8 / 4 = 2
     *
     *
     *  Exp 2)
     *
     *    input = [3,3,3,3,4]
     *    cnt_map = {3:4, 4: 1}
     *
     *    -> total_len = 16
     *    16 / 4 = 4
     *
     *
     *
     */
    public boolean makesquare(int[] matchsticks) {

        return false;
    }


    // LC 698
    // 12.07 - 12.26 pm
    /**
     *  IDEA 1) BACKTRACK
     *
     *   -> a `decision tree` that everytime pick a solution...
     *      if it is still `validated`, keep going (move further at tree)
     *      .... if found a solution, return true directly;
     *      Otherwise, return false at the end of the code
     *      (means NO any solution is found)
     *
     *
     */
     public boolean canPartitionKSubsets(int[] nums, int k) {
         // edge
         if(nums == null || nums.length == 0){
            return false; // ???
        }
        // ???
        if(k == 0){
            return false;
        }
        if(k == 1){
            return true; // ???
        }

        // can't divide by k
        int _sum = 0;
        for(int x: nums){
            _sum += x;
        }
        if(_sum % k != 0){
            return false;
        }

        int sub_sum = _sum / k; // ???


        List<Integer> num_list = new ArrayList<>();
        Map<Integer, Integer> cnt_map = new HashMap<>();

        for(int x : nums){

            num_list.add(x);

            cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);
        }

        // sort list (big -> small), so we quit earlier (if element > target)
         Collections.sort(num_list, new Comparator<Integer>() {
             @Override
             public int compare(Integer o1, Integer o2) {
                 int diff = o2 - o1;
                 return diff;
             }
         });


        // backtrack
        //return false;
        return canPartitionKSubsetsHelper(nums, k, sub_sum, cnt_map, new ArrayList<>(), new ArrayList<>());
    }

    public boolean canPartitionKSubsetsHelper(int[] nums, int k, int sub_sum, Map<Integer, Integer> cnt_map, List<List<Integer>> buckets, List<Integer> cur){

         // if all elements are used ?????
         if(cnt_map.isEmpty()){
             if(isValidBuckets(sub_sum, buckets)){
                 return true;
             }
             return false; // ???
         }

        int curSum = getListSum(cur);

         for(Integer key: cnt_map.keySet()){
             if(curSum + key > sub_sum){
                 continue;
             }

             cur.add(key);
             cnt_map.put(key, cnt_map.get(key) - 1);
             if(cnt_map.get(key) == null){
                 cnt_map.remove(key);
             }

             // undo
             cur.remove(cur.size() - 1);
             cnt_map.put(key, cnt_map.get(key) + 1); // ???

         }

         return false;
    }

    // ?? optimize ???
    public boolean isValidBuckets(int sub_sum, List<List<Integer>> buckets){
         for(List<Integer> b: buckets){
             int sum = 0;
             for(int x: b){
                 sum += x;
             }
             if(sum != sub_sum){
                 return false;
             }
         }

         return true;
    }

    public int getListSum(List<Integer> list){
         int res = 0;
         for(int x: list){
             res += x;
         }
         return res;
    }


//    public boolean canPartitionKSubsets(int[] nums, int k) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return false; // ???
//        }
//        // ???
//        if(k == 0){
//            return false;
//        }
//        if(k == 1){
//            return true; // ???
//        }
//
//        // can't divide by k
//        int _sum = 0;
//        for(int x: nums){
//            _sum += x;
//        }
//        if(_sum % k != 0){
//            return false;
//        }
//
//        // map : { k : cnt }
//        Map<Integer, Integer> cnt_map = new HashMap<>();
//        List<Integer> num_list = new ArrayList<>();
//
//        for(int x : nums){
//
//            cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);
//
//            num_list.add(x);
//        }
//
//        // sort (small -> big)
//        Collections.sort(num_list, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o1 - o2;
//                return diff;
//            }
//        });
//
//        int avg_val = _sum / k;
//        //for(i)
//
//
//        return true;
//    }

    // LC 51
    // 11.46 - 11. 56 am
    List<List<String>> queenRes = new ArrayList<>();
    public List<List<String>> solveNQueens(int n) {
        // edge
        if(n == 0){
            return queenRes;
        }
        if(n == 1){
            List<String> tmp = new ArrayList<>();
            tmp.add("Q");
            queenRes.add(tmp);
            return queenRes;
        }

        // backtrack

        return queenRes;
    }

    public void queenHelper(int n, String[][] cur, int x, int y){

        // if `reach the end`
        if(x == n - 1 && y == n - 1){
            if(!queenRes.contains(cur)){
                queenRes.add(new ArrayList<>()); // ???
            }
        }

        // check if `queen can attack each other`
        if(canAttack(cur)){
            return;
        }

        for(int i = 0; i < n; i ++){
            for(int j = 0; j < n; j++){

                // place new `queen`
                cur[j][i] = "Q";
                queenHelper(n, cur, x, y);
                // undo
                cur[j][i] = ".";
            }
        }

    }


    public boolean canAttack(String[][] cur){
        return false;
    }

    // LC 52
    /**
     *  IDEA 1) BACKTRACK
     *  IDEA 2) LC 51 + unique cnt
     *
     *
     */
    public int totalNQueens(int n) {
        // edge
        if(n == 1 || n == 0){
            return n; // ???
        }

        // LC 51 N QUEENS

        List<List<String>> result = new ArrayList<>();

        char[][] board = new char[n][n];
        for (char[] row : board)
            Arrays.fill(row, '.');

        boolean[] cols = new boolean[n]; // tracks columns
        boolean[] diag1 = new boolean[2 * n - 1]; // tracks ↘ diagonals (row - col + n - 1)
        boolean[] diag2 = new boolean[2 * n - 1]; // tracks ↙ diagonals (row + col)

        nQueenBacktrack(0, board, result, cols, diag1, diag2);


        return result.size(); // ????
    }

    private void nQueenBacktrack(int row, char[][] board, List<List<String>> result,
                           boolean[] cols, boolean[] diag1, boolean[] diag2) {
        int n = board.length;
        if (row == n) {
            result.add(constructBoard(board));
            return;
        }

        for (int col = 0; col < n; col++) {
            int d1 = row - col + n - 1; // ↘ diagonal index
            int d2 = row + col; // ↙ diagonal index

            if (cols[col] || diag1[d1] || diag2[d2])
                continue;

            // Place queen
            board[row][col] = 'Q';
            cols[col] = diag1[d1] = diag2[d2] = true;

            nQueenBacktrack(row + 1, board, result, cols, diag1, diag2);

            // Backtrack
            board[row][col] = '.';
            cols[col] = diag1[d1] = diag2[d2] = false;
        }
    }

    private List<String> constructBoard(char[][] board) {
        List<String> res = new ArrayList<>();
        for (char[] row : board) {
            res.add(new String(row));
        }
        return res;
    }

    // LC 139
    // 10.23 - 10.33 am
    // IDEA: BACKTRACK
//    public boolean wordBreak(String s, List<String> wordDict) {
//        // edge
//        if(s == null || s.length() == 0){
//            return true;
//        }
//        if(s.length() == 1){
//            return wordDict.contains(s);
//        }
//        if(wordDict == null){
//            return false;
//        }
//        if(wordDict.contains(s)){
//            return true;
//        }
//
//        // backtrack
//        int start_idx = 0;
//        return findWordHelper(s, wordDict, start_idx);
//    }
//
//    public boolean findWordHelper(String s, List<String> wordDict, int start_idx){
//        // found a solution
//        if(start_idx == s.length() - 1){
//            return true;
//        }
//        // should NOT visit this point ????
//        if(start_idx > s.length() - 1){
//            return false;
//        }
//
//        // ???
//        for(String x: wordDict){
//            int start = start_idx;
//            int end = start_idx + x.length();
//            if(end < s.length() && x.equals(s.substring(start, end))){
////                // move forward
////                start_idx += x.length();
////                findWordHelper(s, wordDict, start_idx);
////                // undo
////                start_idx -= x.length();
//                // NOTE !!! below, return true if found a solution
//                if(findWordHelper(s, wordDict, end)){
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }


    // IDEA: BFS
    // 10.46 - 10.56 AM
//    public class WordInfo{
//        int start_idx;
//        String word;
//
//        public WordInfo(int start_idx, String word){
//            this.start_idx = start_idx;
//            this.word = word;
//        }
//    }
//
//    public boolean wordBreak(String s, List<String> wordDict) {
//        // edge
//        if(s == null || s.length() == 0){
//            return true;
//        }
//        if(s.length() == 1){
//            return wordDict.contains(s);
//        }
//        if(wordDict == null){
//            return false;
//        }
//        if(wordDict.contains(s)){
//            return true;
//        }
//
//        // BFS
//        Queue<WordInfo> q = new LinkedList<>();
//        for(String x: wordDict){
//            // ???
//            if(x.equals(s.substring(0, x.length()))){
//                q.add(new WordInfo(x.length(), x));
//            }
//        }
//
//        while(!q.isEmpty()){
//
//            WordInfo word_info = q.poll();
//            //int end_idx = word_info.start_idx + word_info.word.length();
//            int start_idx = word_info.start_idx;
//            // found a solution
//            if(start_idx == s.length()){
//                return true;
//            }
//            // ???
//            if(start_idx > s.length()){
//                continue;
//            }
//
//            for(String x: wordDict){
//                // ???
//                if(x.equals(s.substring(start_idx, start_idx + x.length()))){
//                   q.add(new WordInfo(start_idx + x.length(), x));
//                }
//            }
//
//        }
//
//
//        return false;
//    }

    // LC 140
    // 10.21 - 11.15 am
    /**
     *
     *  IDEA 1) BFS
     *  IDEA 2) DFS (backtrack ???)
     *  IDEA 3)  DP ????
     *
     */
    // BFS
        public class WordInfo{
        int start_idx;
        StringBuilder sb;

        public WordInfo(int start_idx, StringBuilder sb){
            this.start_idx = start_idx;
            this.sb = sb;
        }
    }
    List<String> wordBreak2Res = new ArrayList<>();
    public List<String> wordBreak(String s, List<String> wordDict) {

        // edge
        if(s == null || s.length() == 0){
            return wordBreak2Res;
        }
        if(s.length() == 1){
            if(wordDict.contains(s)){
                wordBreak2Res.add(s);
            }
            return wordBreak2Res;
        }
        if(wordDict == null){
            return wordBreak2Res;
        }
        if(wordDict.contains(s)){
            wordBreak2Res.add(s);
            return wordBreak2Res;
        }

        // bfs
        Queue<WordInfo> q = new LinkedList<>();
        while(!q.isEmpty()){

            WordInfo info = q.poll();
            int start = info.start_idx;
            StringBuilder _sb = info.sb;
            String word = info.sb.toString();
            if(start == s.length()){
                if(!wordBreak2Res.contains(word)){
                    wordBreak2Res.add(word);
                }
            }

            if(info.start_idx > s.length()){
                continue; // ???
            }

            for(String x: wordDict){
                if(start + x.length() < s.length() && x.equals(s.substring(start, start + x.length()))){
                    // NOTE !!! below
//                    word += "";
//                    word += x;
                    _sb.append("");
                    _sb.append(x);
                    q.add(new WordInfo(start + x.length(), _sb));
                    // undo
                   // word.de
                }
            }

        }

        return wordBreak2Res; //???
    }

    // LC 208
    // 10.21 - 10.42 pm
    // IDEA : TREENODE + TRIE
//    class MyTreeNode{
//
//        String val;
//        boolean isEnd;
//
//        Map<MyTreeNode, List<MyTreeNode>> children;
//
//        public MyTreeNode(String val, MyTreeNode child, boolean isEnd){
//            this.val = val;
//            this.isEnd = true; // ??
//
//            this.children = new HashMap<>(); // ??
//            this.children.put(child, new ArrayList<>()); // ?
//        }
//
//        public MyTreeNode(String val){
//            this.val = val;
//            this.isEnd = true; // ??
//            this.children = new HashMap<>(); // ??
//        }
//
//    }
    class MyTreeNode{
       // String val;
        Map<String, MyTreeNode> children;
        boolean isEnd;

        public MyTreeNode(){
            this.children = new HashMap<>();
            this.isEnd = false;
        }
    }
    class Trie {

        MyTreeNode node;
        //Map<MyTreeNode, List<MyTreeNode>> children;

        public Trie() {
            //this.node = new MyTreeNode(null);
           // this.children = new HashMap<>(); // ??
            this.node = new MyTreeNode();
        }

        public void insert(String word) {
            //if(!this.)
            if(word == null || word.length() == 0){
                return;
            }

            MyTreeNode node = this.node; //??

            for(String x: word.split("")){

                if(!node.children.containsKey(x)){
                    node.children.put(x, new MyTreeNode());
                }

                node = node.children.get(x); // ??
            }

            node.isEnd = true; // /??
        }

        public boolean search(String word) {

            if(word == null || word.length() == 0){
                return false;
            }

            MyTreeNode node = this.node;

            for(String x: word.split("")){
                if(!this.node.children.containsKey(x)){
                    return false;
                }
                node = node.children.get(x);
            }

            return node.isEnd;
        }

        public boolean startsWith(String prefix) {
            if(prefix == null || prefix.length() == 0){
                return false;
            }

            MyTreeNode node = this.node;

            for(String x: prefix.split("")){
                if(!this.node.children.containsKey(x)){
                    return false;
                }
                node = node.children.get(x);
            }

            return true;
        }

    }

    // LC 211
    // IDEA: TRIE
    // 11.20 - 11.30 am
    class MyTreeNode2{
        Map<String, MyTreeNode2> children;
        boolean isEnd;

        public MyTreeNode2(){
            this.children = new HashMap<>(); // ?
            this.isEnd = false;
        }

    }
    class WordDictionary {

        MyTreeNode2 node;

        /** Initialize your data structure here. */
        public WordDictionary() {
            this.node = new MyTreeNode2();
        }

        /** Adds a word into the data structure. */
        public void addWord(String word) {

            if(word == null || word.length() == 0){
                return;
            }

            MyTreeNode2 node = this.node; //??

            for(String x: word.split("")){

                if(!node.children.containsKey(x)){
                    node.children.put(x, new MyTreeNode2());
                }

                node = node.children.get(x); // ??
            }

            node.isEnd = true; // /??
        }

        /** Returns if the word is in the node. */
        // ????
        public boolean searchInNode(String word, MyTreeNode2 node) {

            MyTreeNode2 node2 = this.node; // ??
            // ???
            return this.search(word);
        }

        /** Returns if the word is in the data structure.
         *  A word could contain the dot character '.'
         *  to represent any one letter.
         *
         */
        // helper func
        public boolean search(String word) {

            if(word == null || word.length() == 0){
                return true;
            }

            MyTreeNode2 node = this.node;

            for(int i = 0; i < word.length(); i++){

                String x = String.valueOf(word.charAt(i));

                // case 1) if `trie` has NO such key
                if(!node.children.containsKey(x)){

                    // case 1-1) x != '.'
                    // NOTE !!! if x != '.' AND node has NO such children, return false directly
                    if(x != "."){
                        return false;
                    }
                    // case 1-2) x == '.'
                    // -> loop over all `children, since '.' could by any string
                    else{
                        for(String k : node.children.keySet()){
                            // NOTE !!! below recursive call and res
                            // ?????
                            // get sub string of word (remove current 1st element)
                            //String _word = word.substring(1, word.length()-1);
                            // NOTE !!! sub str should start from the next of current (idx + 1)
                            String _word = word.substring(i+1);
//                            if(! this.search(_word)){
//                                return false;
//                            }
//                            this.search(_word); // ???
                            if(this.search(_word)){
                                return true;
                            }
                        }
                    }

                }
                // case 2) if `trie` has such key
                else{
                    node = node.children.get(x);
                }

            }

            return true;
        }

    }

    // LC 2707
    // 12.07 - 12.17 pm
    public int minExtraChar(String s, String[] dictionary) {
        return 0;
    }

    // LC 212
    // 12.13 PM - 12.23 PM
    // IDEA: DFS + BACKTRACK
    List<String> word2Res = new ArrayList<>();
    public List<String> findWords(char[][] board, String[] words) {
        // edge
        if(words == null || words.length == 0){
            return word2Res;
        }
        // word is NOT null, but board is null
        if(board == null || board[0].length == 0){
           return word2Res;
        }

        int l = board.length;
        int w = board[0].length;

//        for(String word: words){
//
//            //boolean[][] visited = new boolean[l][w];
//
//            if(isFound(board, word, 0, 0, new boolean[l][w], 0)){
//                word2Res.add(word);
//            }
//        }

        // NOTE !!! we also need to loop over (x,y)
        // e.g. try from different start points
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                for(String word: words){
                    if(isFound(board, word, j, i, new boolean[l][w], 0)){
                        word2Res.add(word);
                    }
                }
            }
        }

        return word2Res;
    }

    public boolean isFound(char[][] board, String word, int x, int y, boolean[][] visited, int idx){

        int l = board.length;
        int w = board[0].length;

        // if found a word
        if(idx == word.length() - 1){
            return true;
        }
        // ??
        if(idx > word.length() - 1){
            //return false
            return false;
        }

        // NOTE !!! below, we validate first
        if( x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || board[y][x] != word.charAt(idx) ){
            return false;
        }

        visited[y][x] = true;

        // move 4 dirs at the same time
        // ????
        if( isFound(board, word, x + 1, y, visited, idx+1) ||
                isFound(board, word, x - 1, y, visited, idx+1) ||
                isFound(board, word, x, y - 1, visited, idx+1) ||
                isFound(board, word, x, y + 1, visited, idx+1)){
            return true; // NOTE !!!!!
        }

        // undo
        visited[y][x] = false;

        return false; // ???
    }

    // IDEA 2) TRIE

    // LC 463
    // 10.22 - 10.32 am
    /**
     *  IDEA 1) DFS
     *
     *  -> count `Perimeter` via check `cell surroundings`
     *    -> if at border or surrounded by `0` : + 1
     *    -> else : do nothing
     *
     *    -> get cumsum, ... then return as final res
     *
     *
     *  IDEA 2) BRUTE FORCE
     *
     */
    int islandPerimeterVal = 0; // ??
    public int islandPerimeter(int[][] grid) {

        int res = 0;

        // edge
        if(grid == null || grid.length == 0){
            return 0;
        }
        if(grid[0].length == 0){
            return 0;
        }
        // dfs // ???
        int l = grid.length;
        int w = grid[0].length;

        List<int[]> isLands = new ArrayList<>();

        for(int i = 0; i < w; i++){
            for(int j = 0; j < l; j++){
                if(grid[j][i] == 1){
                    isLands.add(new int[]{i, j}); // ???
                }
            }
        }

        if(isLands.isEmpty()){
            return 0; //??
        }

        for(int[] x: isLands){
            islandPerimeterVal += getPerimeter(grid, x[0], x[1]);
        }

        return islandPerimeterVal;
    }

    public int getPerimeter(int[][] grid, int x, int y){

        int res = 0;
        int[][] moves = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

        for(int[] move: moves){
            int x_ = x + move[0];
            int y_ = y + move[1];
            if(isWaterOrBorder(grid, x_, y_)){
                res += 1;
            }
        }

        return res;
    }

    public boolean isWaterOrBorder(int[][] grid, int x, int y){

        int l = grid.length;
        int w = grid[0].length;

        if(x < 0 || x >= w || y < 0 || y >= l){
            return true;
        }

        return grid[y][x] == 0;
    }



//    public void getPerimeterHelper(int[][] grid, int x, int y, boolean[][] visited){
//
//        if
//
//    }


    // LC 953
    // 1.01 - 1.11 pm
    // IDEA: BRUTE FORCE
    public boolean isAlienSorted(String[] words, String order) {
        // edge
        if(words == null || words.length <= 1){
            return true;
        }
        if(order == null || order.isEmpty()){
            return true; // ??
        }

        for(int i = 1; i < words.length; i++){
            String prev = words[i-1];
            String cur = words[i];
            if(!alienHelper(prev, cur, order)){
                return false;
            }
        }

        return true; // ???
    }

    // w1 : prev word
    // w2 : current word
    public boolean alienHelper(String prev, String cur, String order){

        int minLen = Math.min(prev.length(), cur.length());

        // ??
        for(int i = 0; i < minLen; i++){
            int order_prev = order.indexOf(prev.indexOf(i));
            int order_cur = order.indexOf(cur.indexOf(i));

            if(order_prev > order_cur){
                return false;
            }

            //i += 1;
        }

        /**
         *  IDEA:
         *
         *  words = ["apple","app"], order = "abcdefghijklmnopqrstuvwxyz"
         *
         *  - According to lexicographical rules "apple" > "app",
         *    because 'l' > '∅', where '∅' is defined as the
         *    blank character which is less than any other character
         *
         *
         *    -> so if such case happened, should return false
         *
         *    -> e.g.: check if `prev len` > minLen
         *
         */
        return minLen >= prev.length(); // ??
    }



//    public boolean isAlienSorted(String[] words, String order) {
//
//        // edge
//        if(words == null || words.length <= 1){
//            return true;
//        }
//
//        // loop over `word`, then compare every alphabet
//        // between prev and cur word
//        // if eny violated, return false directly
//        for(int i = 1; i < words.length; i++){
//            String prev = words[i-1];
//            String cur = words[i];
//            if(!isValidOrder(prev, cur, order)){
//                return false;
//            }
//        }
//;
//        return true;
//    }
//
//    public boolean isValidOrder(String prev, String cur, String order){
//
//        int i = 0; // pointer for prev
//        int j = 0; // pointer for cur
//
//        while(i < prev.length() || j < cur.length()){
//
//            if(i == prev.length()){
//                if(j < cur.length()){
//                    return false;
//                }
//            }
//
//            int order1 = order.charAt(prev.charAt(i));
//            int order2 = order.charAt(cur.charAt(j));
//
//            if(order1 > order2){
//                return false;
//            }
//
//            // ???
//            i += 1;
//            j += 1;
//        }
//
//        return true;
//    }

    // LC 997
    // 11.53 - 12.03 pm
    /**
     *
     *  -> n: there are n people labeled from 1 to n.
     *
     * -> [ai, bi] : trust[i] = [ai, bi] representing that the
     *     person labeled ai trusts the person labeled bi.
     *
     * -> [ai, bi]
     *     -> ai `trusts` bi
     *
     * ->  Return the label of the town judge
     *   if the town judge exists and can be identified,
     *    or return -1 otherwise.
     *
     */
    /**
     *  IDEA 1) DFS / GRAPH
     *
     *  IDEA 2) BRUTE FORCE
     *
     */
    // 12.12 pm - 12.22 pm
    // IDEA: BRUTE FORCE
    /**
     *  NOTE !!!
     *
     *  -> trust[i] = [ai, bi]
     *
     *   ->   ai trusts the person labeled bi.
     *
     */
    public int findJudge(int n, int[][] trust) {
        // edge
//        if(n == 0 || trust == null || trust.length == 0){
//            return -1;
//        }
//        if(trust.length == 1){
//            return trust[0][1];
//        }
//        if(n == 1){
//            return 1;
//        }
        if (n == 1) {
            return 1;
        }

        // people the person (idx) trusts
        // e.g.  idx --- trust --> person
        int[] toTrust = new int[n + 1];

        // people who trusts the person (idx)
        // e.g.  person --- trust --> idx
        int[] trusted = new int[n + 1];

        for(int[] t: trust){
            // NOTE !!!
            // [ai, bi]:  `ai` trusts `bi`
            int ai = t[0];
            int bi = t[1];

            toTrust[ai] += 1;
            trusted[bi] += 1;
        }

        System.out.println(">>> toTrust = " +  Arrays.toString(toTrust));
        System.out.println(">>> trusted = " +  Arrays.toString(trusted));

        for(int j = 0; j < toTrust.length; j++){
            if(toTrust[j] == 0 && trusted[j] == n - 1){
                return j;
            }
        }

        return -1;
    }

//    public int findJudge(int n, int[][] trust) {
//
//        // edge
//        if(n == 0 || trust == null || trust.length == 0){
//            return -1;
//        }
//        if(trust.length == 1){
//            return trust[0][1];
//        }
//
//        //HashSet<Integer> candidates = new HashSet<>();
//        List<Integer> candidates = new ArrayList<>();
//        for(int[] x : trust){
//            if(!candidates.contains(x[0])){
//                candidates.add(x[0]);
//            }
//            if(!candidates.contains(x[1])){
//                candidates.add(x[1]);
//            }
//        }
//
//        for(int[] x: trust){
//            if(!candidates.isEmpty() && candidates.contains(x[0])){
//              //  candidates.re
//            }
//        }
//
//        System.out.println(">>> candidates = " + candidates);
//
//        if(candidates.isEmpty()){
//            return  -1;
//        }
//
//        // ???
//        return candidates.get(0);
//    }

    // LC 200
    // 9.40 - 9.50 am
    // IDEA: DFS
    public int numIslands(char[][] grid) {

        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }

        if(grid.length == 1 && grid[0].length == 1){
            if(grid[0][0] == '1'){
                return 1;
            }
            return 0;
        }

        int cnt = 0;

        int l = grid.length;
        int w = grid[0].length;

        boolean[][] visited = new boolean[l][w];

        for(int i = 0; i < w; i++){
            for(int j = 0; j < l; j++){
                if(grid[j][i] == '1' && !visited[j][i]){
                    islandCntHelper(grid, i, j, visited);
                    cnt += 1;
                }
            }
        }

        return cnt;
    }

    public void islandCntHelper(char[][] grid, int x, int y, boolean[][] visited){

        int l = grid.length;
        int w = grid[0].length;

        // NOTE !!! we validate below first
        if(x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || grid[y][x] == '0'){
            return;  // ???
        }

        visited[y][x] = true;

        islandCntHelper(grid, x + 1, y, visited);
        islandCntHelper(grid, x - 1, y, visited);
        islandCntHelper(grid, x, y + 1, visited);
        islandCntHelper(grid, x, y - 1, visited);
    }

    // LC 695
    // 10.14 - 10.28 am
    // IDEA: DFS
    int maxArea = 0;
    public int maxAreaOfIsland(int[][] grid) {
        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }

        if(grid.length == 1 && grid[0].length == 1){
            if(grid[0][0] == '1'){
                return 1;
            }
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;

        for(int i = 0; i < w; i++){
            for(int j = 0; j < l; j++){
                if(grid[j][i] == '1'){
                    //int tmpArea = 0;
                    maxArea = Math.max(maxArea, getMaxArea(grid, i, j));
                    //getMaxArea(grid, i, j);
                }
            }
        }

        return maxArea;
    }



    public int getMaxArea(int[][] grid, int x, int y){

        // NOTE !!! we validate below first
        int l = grid.length;
        int w = grid[0].length;

        // NOTE !!! we validate below first
        if(x < 0 || x >= w || y < 0 || y >= l || grid[y][x] == '0'){
            return 0;  // ???
        }

        // NOTE !!! mark visited cell as '#'
        grid[y][x] = '#';
        //area += 1; // ???

        return 1 +
                getMaxArea(grid, x + 1, y) +
                getMaxArea(grid, x - 1, y) +
                getMaxArea(grid, x, y + 1) +
                getMaxArea(grid, x, y - 1);
    }

    // LC 133
    // 11.08 - 11. 32 am
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

    // IDEA: DFS
    //Node cloned;
    public Node cloneGraph(Node node) {
        // edge
        if(node == null){
            return node;
        }
        if(node.neighbors == null || node.neighbors.isEmpty()){
            return new Node(node.val);
        }

        Node cloned = new Node(node.val);

        // dfs
        cloneHelper(node, cloned, new HashSet<>()); // ??? cloned as param ???
        return cloned; // ???
    }

    public void cloneHelper(Node node, Node cloned, HashSet<Node> visited){
        // edge
        if(node == null){
            return;
        }
        if(visited.contains(node)){
            return;
        }

        // mark as `visited`
        visited.add(node);

        for(Node n: node.neighbors){
            Node newNode = new Node(n.val); // ?
            cloned.neighbors.add(newNode);
            cloned = newNode; //new Node(n.val); // ???
            cloneHelper(node, cloned, visited);
        }

    }

    // LC 286
    // 3.30 - 3.40 pm
    /**
     *
     *  -> Fill each empty room with the distance to its nearest gate.
     *   If it is impossible to reach a gate, it should be filled with INF.
     *
     *   -> 'IMF' to '0'
     *
     *    -1 - A wall or an obstacle.
     *   0 - A gate.
     *   INF - Infinity means an empty room.
     *        We use the value 231 - 1 = 2147483647
      *       to represent INF as you may assume
     *        that the distance to a gate is less than 2147483647.
     *
     *   IDEA: BFS (distance to the nearest)
     *
     */
    public void wallsAndGates(int[][] rooms) {
        // edge
        if(rooms == null || rooms.length == 0 || rooms[0].length == 0){
            return;
        }

        int l = rooms.length;
        int w = rooms[0].length;

        for(int i = 0; i < w; i++){
            for(int j = 0; j < l; j++){
                // update rooms with nearest dist
                if(rooms[j][i] == 0){
                    // update
                    // NOTE !!! visited matrix size
                    boolean[][] visited = new boolean[l][w];
                    distanceUpdater(rooms, i, j, visited);
                }
            }
        }
    }

    // BFS
    public void distanceUpdater(int[][] rooms, int x, int y, boolean[][] visited){

        int l = rooms.length;
        int w = rooms[0].length;

        Queue<DistInfo> q = new LinkedList<>();
        q.add(new DistInfo(x, y, 0));

        while(!q.isEmpty()){

            DistInfo info = q.poll();
            int cur_x = info.x;
            int cur_y = info.y;
            int dist = info.dist;

            // NOTE !!! we validate below
            if(cur_x < 0 || cur_x >= w ||  cur_y < 0 || cur_y >= l || visited[cur_y][cur_x] ||  rooms[cur_y][cur_x] != Integer.MAX_VALUE ){
                // NOTE !!! instead of return directly, we SHOULD use `continue`
                //return;
                continue;
            }

            rooms[cur_y][cur_x] = Math.min(dist,  rooms[cur_y][cur_x]); // ???

            visited[cur_y][cur_x] = true;

            q.add(new DistInfo(cur_x + 1, cur_y, dist + 1));
            q.add(new DistInfo(cur_x - 1, cur_y, dist + 1));
            q.add(new DistInfo(cur_x, cur_y + 1, dist + 1));
            q.add(new DistInfo(cur_x, cur_y - 1, dist + 1));
        }

    }

    public class DistInfo{
        int x;
        int y;
        int dist;

        public DistInfo(int x, int y, int dist){
            this.x = x;
            this.y = y;
            this.dist = dist;
        }
    }


    // LC 417
    // 4.19 - 4.29 pm
    /**
     *
     *  -> Return a 2D list of grid coordinates result
     *     where result[i] = [ri, ci] denotes that rain
     *     water can flow from cell (ri, ci) to
     *     `both the Pacific and Atlantic oceans.`
     *
     *
     *  water can flow to neighboring cells directly
     *  north, south, east, and west
     *  if the neighboring cell's height
     *  is `less than or equal` to the current cell's height
     *
     *
     *  IDEA 1) DFS
     *    -> split result to 2 array
     *      - canFlowToPacific
     *      - canFlowToAtlantic
     *    -> ONLY the overlap cells are collected as final result
     *
     *    -> NOTE !!! we `flow` from `ocean` to the `cell`,
     *                like the `inverse way`
     *
     *  IDEA 2) BFS
     *
     */
    public List<List<Integer>> pacificAtlantic(int[][] heights) {

       // List<List<Integer>> res = new ArrayList<>();
        // edge
        if(heights == null || heights.length == 0 || heights[0].length == 0){
            return new ArrayList<>();
        }

        int l = heights.length;
        int w = heights[0].length;

        List<List<Integer>> canFlowToPacific = new ArrayList<>();
        List<List<Integer>> canFlowToAtlantic = new ArrayList<>();

        // dfs call


        // find `overlap`
        return getOverLap(canFlowToPacific, canFlowToAtlantic);
    }

    public List<List<Integer>> getCanFlowCell(int[][] heights, int x, int y, int last_x, int last_y, boolean[][] visited, List<List<Integer>> collected){

        int l = heights.length;
        int w = heights[0].length;

        visited[y][x] = true;

        // NOTE !!! we validate below
        if(x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || heights[last_y][last_x] >= heights[y][x]){
            return null;
        }

        List<Integer> tmp = new ArrayList<>();
        tmp.add(x);
        tmp.add(y);
        collected.add(tmp);

        getCanFlowCell(heights, x + 1, y, x, y, visited, collected);
        getCanFlowCell(heights, x - 1, y, x, y, visited, collected);
        getCanFlowCell(heights, x, y + 1, x, y, visited, collected);
        getCanFlowCell(heights, x, y - 1, x, y, visited, collected);

        return collected; // ???
    }


    public List<List<Integer>> getOverLap(List<List<Integer>> arr_a, List<List<Integer>> arr_b){

        List<List<Integer>> res = new ArrayList<>();
        // assume arr_a has `longer` len
        int max_len = Math.max(arr_a.size(), arr_b.size());
        // swap arr_a, arr_b
        if(max_len == arr_b.size()){
            List<List<Integer>> cache = arr_a;
            arr_a = arr_b;
            arr_b = cache;
        }

        for(int i = 0; i < arr_a.size(); i++){
            /// ???
            if(arr_b.contains(arr_a.get(i))){
                res.add(arr_a.get(i));
            }
        }

        return res;
    }

    // LC 130
    // 4.58 - 5.08 pm
    /**
     *  IDEA 1) DFS
     *
     *  IDES 2) BFS
     */
    public void solve(char[][] board) {

        // edge
        if(board == null || board.length == 0 || board[0].length == 0){
            return;
        }

        if(board.length == 1 || board[0].length == 1){
            return;
        }

        int l = board.length;
        int w = board[0].length;

        /**
         *  3 steps
         *
         *  step 1) mark all `0` but connected to `boundary cell as '#'
         *
         *  step 2) mark rest of the `0` as 'X`
         *
         *  step 3) mark all '#' as '0'
         *
         */

        //boolean[][] visited = new boolean[l][w];

        // Step 1: Mark boundary-connected 'O's as '#'
        for(int i = 0; i < w; i++){
            markAsNotAffected(board, i, 0);
            markAsNotAffected(board, i, l - 1);
        }

        for(int j = 0; j < l; j++){
            markAsNotAffected(board, 0, j);
            markAsNotAffected(board, w - 1, j);
        }

        // Step 2: Flip the remaining 'O's to 'X'
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                /**
                 *  NOTE !!!
                 *
                 *  board[y][x],
                 *
                 *  so the first is Y-coordination
                 *  and the second is X-coordination
                 *
                 */
                if(board[i][j] == 'O'){
                    board[i][j] = 'X';
                }
            }
        }

        // mark `all #` as `O`
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                if(board[i][j] == '#'){
                    board[i][j] = 'O';
                }
            }
        }

    }

    public void markAsNotAffected(char[][] board, int x, int y){

        int l = board.length;
        int w = board[0].length;

        // NOTE !!! we validate below
        if(x < 0 || x >= w || y < 0 || y >= l || board[y][x] != 'O'){
            return;
        }

        // mark as visited
        board[y][x] = '#';

        markAsNotAffected(board, x + 1, y);
        markAsNotAffected(board, x - 1, y);
        markAsNotAffected(board, x, y + 1);
        markAsNotAffected(board, x, y - 1);
    }

    // LC 752
    // 3.34 - 3.44 pm
    /**  IDEA : BFS
     *
     *   step 1) try all possible moves (for each `lock`)
     *           if meat deadlock, `give up` such option
     *   step 2) if reach any target, return cnt directly
     *           otherwise, return -1
     *
     */
    public class LockInfo{
        String[] lock;
        int moves;

        public LockInfo(String[] lock, int moves){
            this.lock = lock;
            this.moves = moves;
        }
    }
    public int openLock(String[] deadends, String target) {
        // edge
        if(target == "0000"){
            return 0;
        }

        List<String> deadendList = new ArrayList<>();
        for(String d: deadends){
            deadendList.add(d);
        }

        // edge 2, init status is in deadends
        if(deadendList.contains("0000")){
            return -1;
        }

        Queue<LockInfo> q = new LinkedList<>();
        q.add(new LockInfo("0000".split(""), 0));

        // bfs
        while(!q.isEmpty()){

            LockInfo cur = q.poll();
            String[] _lock = cur.lock;
            int moves = cur.moves;

            if(_lock.toString().equals(target)){
                return moves;
            }
            // deadlock
           if(deadendList.contains(Arrays.toString(_lock))){
               continue;
           }

           // move
           for(int i = 0; i < cur.lock.length; i++){
               // update val at idx `i`
               // case 1) move `clockwise`
               String[] lock_1 = lockMover(_lock, i, true);

               // case 2) move `anti clockwise`
               String[] lock_2 = lockMover(_lock, i, false);

               // add to queue
               q.add(new LockInfo(lock_1, moves + 1));
               q.add(new LockInfo(lock_2, moves + 1));
           }

        }

        return -1;
    }

    public String[] lockMover(String[] lock, int idx, boolean clockwise){

        int val = Integer.parseInt(lock[idx]);

        if(val == 0){
            if(clockwise){
                val = 1;
            }else{
                val = 9;
            }
        }
        else if(val == 9){
            if(clockwise){
                val = 0;
            }else{
                val = 8;
            }
        }else{
            if(clockwise){
                val += 1;
            }else{
                val -= 1;
            }
        }

        lock[idx] = String.valueOf(val);

        return lock;
    }



//    public int lockHelper(String[] deadends, String target, List<String> cur){
//
//        return 0;
//    }


}

