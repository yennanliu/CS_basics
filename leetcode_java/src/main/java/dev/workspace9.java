package dev;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

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
    class Node {
        public boolean val;
        public boolean isLeaf;
        public Node topLeft;
        public Node topRight;
        public Node bottomLeft;
        public Node bottomRight;


        public Node() {
            this.val = false;
            this.isLeaf = false;
            this.topLeft = null;
            this.topRight = null;
            this.bottomLeft = null;
            this.bottomRight = null;
        }

        public Node(boolean val, boolean isLeaf) {
            this.val = val;
            this.isLeaf = isLeaf;
            this.topLeft = null;
            this.topRight = null;
            this.bottomLeft = null;
            this.bottomRight = null;
        }

        public Node(boolean val, boolean isLeaf, Node topLeft, Node topRight, Node bottomLeft, Node bottomRight) {
            this.val = val;
            this.isLeaf = isLeaf;
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }
    }

    public Node construct(int[][] grid) {

        return null;
    }

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
    // 4.48 - 4.58 pm
    // IDEA : DFS
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

    public TreeNode removeLeafNodes_0_1(TreeNode root, int target) {
        deleteLeafHelper(root, target);
        //return deleteLeafHelper(root, target);
        return root;
    }

    private void deleteLeafHelper(TreeNode root, int target) {
        if (root == null) {
            //return null;
            return;
        }

        // Recursively process left and right children
//        root.left = deleteLeafHelper(root.left, target);
//        root.right = deleteLeafHelper(root.right, target);

        deleteLeafHelper(root.left, target);
        deleteLeafHelper(root.right, target);


        // After processing children, check if current node is a target leaf
        if (root.left == null && root.right == null && root.val == target) {
           // return null; // remove this leaf
            return;
        }

        //return root; // keep current node
        return;
    }

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
    // 11.14 am - 11. 32 am
//    public class Codec {
//
//        // Encodes a tree to a single string.
//        // attr
//        StringBuilder sb;
//        TreeNode decodeNode;
//
//        public Codec(){
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


    public class Codec {

        // IDEA 1) DFS 2) BFS
        public String serialize(TreeNode root) {
            // edge
            if(root == null){
                return "#";
            }
            return root.val + "," + serialize(root.left) + "," + serialize(root.right);
        }

        // Decodes your encoded data to tree.
        // BFS ???
        public TreeNode deserialize(String data) {
            Queue<String> q = new LinkedList<>();
            for(String x: data.split(",")){
              q.add(x); // ???
            }
            return helper(q);
           // return null;
        }

        public TreeNode helper(Queue<String> q){
            String s = q.poll();
            if(s.equals("#")){
                return null;
            }
            TreeNode root = new TreeNode(Integer.valueOf(s));
            //root.left = helper(q.)
            root.left = helper(q);
            root.right = helper(q);

            return root;
        }

    }


}
