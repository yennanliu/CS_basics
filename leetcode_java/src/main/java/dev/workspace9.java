package dev;

import LeetCodeJava.DataStructure.ListNode;

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
    // 10.26 - 10.36 am
    public int minEatingSpeed_(int[] piles, int h) {

        // edge
        if(piles == null || piles.length == 0){
            return 0;
        }
        if(piles.length == 1){
            int times = piles[0]/ h;
            int remain = piles[0] % h;
            if (remain != 0){
                times += 1;
            }
            return times;
        }

        Integer[] piles2 = new Integer[piles.length];
        for(int i = 0; i < piles.length; i++){
            piles2[i] = piles[i];
        }
        // sort piles (small -> big)
        Arrays.sort(piles2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - 02;
                return diff;
            }
        });

        int minSpeed = 0;
        // binary search
        // ???
        int l = piles2[0]; // 0;
        int r = piles2[piles2.length - 1]; //piles.length - 1;
        while(r >= l){
            int mid = (l + r) / 2;
            int hours = getTotalHour(piles2, mid);
            if(hours == h){
                minSpeed = mid;
             // speed is too slow
            }else if (hours > h){
                l = mid + 1;
            // speed is too fast
            }else{
                r = mid - 1;
            }

        }

        return minSpeed;
    }

    public int getTotalHour(Integer[] piles2, int speed){
        int res = 0;
        for (Integer p: piles2){
            int times = p / speed;
            int remain = p % speed;
            if(remain != 0){
                times += 1;
            }
            res += times;
        }

        return res;
    }


    public int minEatingSpeed(int[] piles, int h) {

        if (piles.length == 0 || piles.equals(null)){
            return 0;
        }

        int l = 1; //Arrays.stream(piles).min().getAsInt();
        int r = Arrays.stream(piles).max().getAsInt();

        while (r >= l){
            System.out.println(">>> l = " + l + ", r = " + r);
            int mid = (l + r) / 2;
            int _hour = getCompleteTime_(piles, mid);
            /**
             *  Return the minimum integer k such that she can eat all the bananas within h hours.
             *
             *  -> NOTE !!! so any speed make hr <= h hours could work
             *
             *  -> NOTE !!! ONLY  when _hour <= h, we update r
             */
            if (_hour <= h){
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }


        System.out.println(">>> FINAL l = " + l + ", r = " + r);
        return l;
    }


    private int getCompleteTime_(int[] piles, int speed) {

        int _hour = 0;
        for (int pile : piles) {
            _hour += Math.ceil((double) pile / speed);
        }

        return _hour;
    }

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
    // 4.03 - 4.16 pm
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

        /**
         *  ListNode : reverse nodes withon start, end idx
         */
        ListNode dummy = null;
        dummy.next = head;
        ListNode _prev = null;

        boolean shouldReverse = false;
        int idx = 0;

        while(head != null){
            if(idx < left || idx > right){
                shouldReverse = false;
            }
            else{
                shouldReverse = true;
            }

            // get `prev` node before reverse
            if(idx == left - 1){
                _prev = head;
            }
            // start `reverse`
            if(shouldReverse){
                ListNode _next = head.next;
                head.next = _prev;
                _prev = head;
                head = _next;
                //continue; // /?
            }

            _prev.next = head; // ???
            // end reverse
            if(!shouldReverse){
                head = head.next;
            }
            //head = _next;
            idx += 1;
        }

        return dummy.next; // ???
    }

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
    class MyCircularQueue {

        // attr
        //ListNode node;
        Stack<Integer> st;
        Queue<Integer> q;
        int capacity;
        int elementCnt;

        public MyCircularQueue(int k) {
            //this.node = null;
            this.st = new Stack<>();
            this.q = new LinkedList<>();
            this.capacity = k;
            this.elementCnt = 0;
        }

        public boolean enQueue(int value) {
            this.st.add(value);
            this.q.add(value);
            this.elementCnt += 1;
            return true;
        }

        public boolean deQueue() {
            this.q.poll();
            this.st.pop();
            return true; // ???
        }

        public int Front() {
            //return this.node.next.val; // ???
            if(this.q.isEmpty()){
                return -1;
            }
            return this.q.peek();
        }

        public int Rear() {
            if(this.st.isEmpty()){
                return -1;
            }
            return this.st.peek();
        }

        public boolean isEmpty() {
            return this.elementCnt > 0;
        }

        public boolean isFull() {
            return this.elementCnt == this.capacity;
        }

    }

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



}
