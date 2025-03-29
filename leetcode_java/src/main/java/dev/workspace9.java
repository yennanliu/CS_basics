package dev;

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
    // 11.46 - 11.56 am
    /**
     *  IDEA 1) `2 nums` + monotonic stack
     */
    public int[] nextGreaterElements(int[] nums) {
        if(nums == null || nums.length == 0){
            return null;
        }
        // `duplicate` nums
        int k = 0;

        int[] res = new int[nums.length];
        Arrays.fill(res, -1);

        List<Integer> keys = new ArrayList<>();
        for(int j = 0; j < 2; j++){
            for(int i = 0; i < nums.length; i++){
                int val = nums[i];
                keys.add(val);
            }
        }

        //System.out.println(">>> nums2 = " + nums2);
        System.out.println(">>> keys = " + keys);
        System.out.println(">>> res (before) = " + res);

        Stack<Integer> st = new Stack<>();
        // ???
        for(int i = 0; i < nums.length; i++){
            for(int j = i+1; j < keys.size(); j++){
                int k_ = keys.get(j);
                if(k_ > nums[i]){
                    res[i] = k_;
                    break;
                }
            }
        }

        return res;
    }


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
  public int carFleet(int target, int[] position, int[] speed) {
      // edge
      if(position == null || position.length == 0){
          return 0;
      }
      if(position.length == 1){
          return 1;
      }
      // map
      Map<Integer, Integer> map = new HashMap<>();
      for(int i = 0; i < position.length; i++){
          map.put(position[i], speed[i]);
      }

      // stack
      Stack<Integer> st = new Stack<>();
      // init // ???
      for(int p: position){
          st.add(p);
      }

      //boolean terminated = false;
      int prev = -1;
      while(!isReached(position, target)){
          if(prev == -1){
            //  prev =
          }
      }

      return st.size(); // ??
    }

    public boolean isReached(int[] position, int target){
      for(int p: position){
          if (p < target){
              return false;
          }
      }
      return true;
    }

    // LC 71
    // 5.34 - 5.44 pm
    public String simplifyPath(String path) {

      return null;
    }


}
