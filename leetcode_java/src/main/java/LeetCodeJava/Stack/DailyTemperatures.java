package LeetCodeJava.Stack;

// https://leetcode.com/problems/daily-temperatures/
/**
 * 739. Daily Temperatures
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an array of integers temperatures represents the daily temperatures, return an array answer such that answer[i] is the number of days you have to wait after the ith day to get a warmer temperature. If there is no future day for which this is possible, keep answer[i] == 0 instead.
 *
 *
 *
 * Example 1:
 *
 * Input: temperatures = [73,74,75,71,69,72,76,73]
 * Output: [1,1,4,2,1,1,0,0]
 * Example 2:
 *
 * Input: temperatures = [30,40,50,60]
 * Output: [1,1,1,0]
 * Example 3:
 *
 * Input: temperatures = [30,60,90]
 * Output: [1,1,0]
 *
 *
 * Constraints:
 *
 * 1 <= temperatures.length <= 105
 * 30 <= temperatures[i] <= 100
 */
import java.util.*;

public class DailyTemperatures {

    // V0
    // IDEA: STACK + HASHMAP ( {idx : val} )
    public int[] dailyTemperatures(int[] temperatures) {
        // edge
        if(temperatures == null || temperatures.length == 0){
            return null;
        }
        if(temperatures.length == 1){
            return new int[]{0};
        }
    // NOTE !!! map : {idx : val}
    /**
     *  use the { idx : val } structure.
     *  with this setting, we can get the `val - idx` mapping
     *  and avoid the `duplicated value` issue
     */
    Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < temperatures.length; i++){
            map.put(i, temperatures[i]);
        }

        Stack<Integer> st = new Stack<>();

        int[] ans = new int[temperatures.length];
        // init val as 0
        Arrays.fill(ans, 0);

       // System.out.println(">> stack = " + st);
        for(int i = 0; i < temperatures.length; i++){

            //System.out.println(">> i = " + i + ", stack = " + st + ", ans = " + Arrays.toString(ans));
            int cur = temperatures[i];

            while(!st.isEmpty() && map.get(st.peek()) < cur){
                int val = st.pop();
                //System.out.println(" (while) i = " + i + ", val = " + val + ", map.get(val) = " + map.get(val));
                ans[val] = (i - val);
            }
            
            st.push(i);
        }

        return ans;
    }

    // V0-0-1
    // IDEA : STACK (MONOTONIC STACK)
    // LC 496
    public int[] dailyTemperatures_0_0_1(int[] temperatures) {

        if (temperatures.length == 1){
            return temperatures;
        }

        /**
         *  Stack :
         *
         *   -> cache elements (temperature) that DOESN'T have (NOT found) next warmer temperature yet
         *   -> structure : stack ([temperature, idx])
         *
         *
         *   NOTE !!!
         *
         *   Stack val is as `[temperature, idx]` structure
         *   -> so we can track temperature and idx at once
         */
        Stack<List<Integer>> st = new Stack<>(); // element, idx
        /** NOTE !!!
         *
         *    can't use map, since there will be "duplicated" temperature
         *   -> which will cause different val has same key (hashMap key)
         *
         *
         *   (20250629 update)
         *   -> if still want to use map, can use the { idx : val } structure.
         *      with this setting, we can get the `val - idx` mapping
         *      and avoid the `duplicated value` issue
         */
        //Map<Integer, Integer> map = new HashMap<>(); // {temperature : idx-of-next-warmer-temperature}
        /**
         *  NOTE !!!
         *
         *   we use nextGreater collect answer,
         *   -> idx : temperature, val : idx-of-next-warmer-temperature
         */
        int[] nextGreater = new int[temperatures.length];
        Arrays.fill(nextGreater, 0); // idx : temperature, val : idx-of-next-warmer-temperature
        for (int j = 0; j < temperatures.length; j++){
            int x = temperatures[j];
            /**
             *  NOTE !!!
             *   1) while loop
             *   2) stack is NOT empty
             *   3) cache temperature smaller than current temperature
             *
             *   st.peek().get(0) is cached temperature
             */
            while (!st.isEmpty() && st.peek().get(0) < x){
                /**
                 *  st.peek().get(1) is idx
                 *
                 */
                nextGreater[st.peek().get(1)] = j - st.peek().get(1);
                st.pop();
            }
            List<Integer> cur = new ArrayList<>();
            cur.add(x); // element
            cur.add(j); // idx
            st.add(cur);
        }

        //System.out.println("nextGreater = " + nextGreater);
        return nextGreater;
    }

    // V0-1
    // IDEA : STACK (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Stack/daily-temperatures.py#L34
    public int[] dailyTemperatures_0_1(int[] temperatures) {
        // Edge case: if the input array is null or empty, return an empty array
        if (temperatures == null || temperatures.length == 0) {
            return new int[0];
        }

        int n = temperatures.length;
        int[] res = new int[n];
        Stack<int[]> stack = new Stack<>();

        // Loop through the temperatures in normal order
        for (int i = 0; i < n; i++) {
            /**
             * While stack is not empty
             * and the current temperature is greater
             * than the temperature at the index stored at the top of the stack
             */
            while (!stack.isEmpty() && stack.peek()[1] < temperatures[i]) {
                /**
                 * Pop the top element from the stack
                 * and calculate the number of days to wait
                 */
                int[] top = stack.pop();
                int idx = top[0];
                res[idx] = i - idx;
            }
            /**
             *  NOTE !!!
             *
             *  push 2 elements to stack
             *  1) current idx
             *  2) temperature
             *
             *  e.g. : Push the current index and temperature onto the stack
             */
            stack.push(new int[] { i, temperatures[i] });
        }

        return res;
    }


    // VO-2
    // IDEA : INCREASING STACK
    // https://www.bilibili.com/list/525438321?sort_field=pubtime&spm_id_from=333.999.0.0&oid=779764003&bvid=BV1my4y1Z7jj
    /**  NOTE !!! WE USE "INCREASING"  STACK HERE
     *
     *   It's critical to define whether "increasing" or "decreasing" stack
     *   We're going to use in stack LC before implement it
     */
    public int[] dailyTemperatures_0_2(int[] temperatures) {

        if (temperatures.length == 0 || temperatures.equals(null)){
            return new int[temperatures.length];
        }

        int[] res = new int[temperatures.length];
        // TODO : double check this
        Stack<int[]> stack = new Stack<>();

        int[] init = new int[2];
        init[0]  = temperatures[0];
        init[1] = 0;
        stack.push(init);

        for (int i = 1; i < temperatures.length; i++){

            int cur_tmp = temperatures[i];

            int[] _cur = new int[2];
            /**
             *  data structure : [cur_temperature, index]
             *  so we save cur temperature as 1st element
             *  index of above element as 2nd element
             *  so we can compare temperature and get index difference via above
             */
            _cur[0] = temperatures[i];
            _cur[1] = i;

            // case 1 : cur < stack top element
            if (cur_tmp < stack.peek()[0]){
                stack.push(_cur);
            // case 2 : cur == stack top element
            }else if (cur_tmp == stack.peek()[0]){
                stack.push(_cur);
            } // case 3 : cur > stack top element
            else{
                // make sure stack is NOT empty
                while(!stack.empty() && stack.peek()[0] < cur_tmp){
                    int[] _top = stack.pop();
                    res[_top[1]] = i - _top[1];
                }
                int[] to_push = new int[2];
                to_push[0] = cur_tmp;
                to_push[1] = i;
                stack.push(to_push);
            }
        }

        return res;
    }

    // V0-3
    // IDEA: MONOTONIC STACK (gpt)
    public int[] dailyTemperatures_0_3(int[] temperatures) {
        // edge case
        if (temperatures == null || temperatures.length == 0) {
            return new int[] {};
        }

        // result array to store the number of days to wait for a warmer temperature
        int[] res = new int[temperatures.length];

        // stack to store the indices of the temperatures
        Stack<Integer> st = new Stack<>();

        // iterate through the temperatures
        for (int i = 0; i < temperatures.length; i++) {
            // while stack is not empty and the current temperature is greater than the
            // temperature at the index of the top element of the stack
            while (!st.isEmpty() && temperatures[i] > temperatures[st.peek()]) {
                int idx = st.pop(); // get the index of the previous temperature
                res[idx] = i - idx; // calculate the number of days to wait
            }
            st.push(i); // push the current index onto the stack
        }

        // return the result array
        return res;
    }

    // V1
    // IDEA : Monotonic Stack
    // https://leetcode.com/problems/daily-temperatures/editorial/
    public int[] dailyTemperatures_1(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int currDay = 0; currDay < n; currDay++) {
            int currentTemp = temperatures[currDay];
            // Pop until the current day's temperature is not
            // warmer than the temperature at the top of the stack
            while (!stack.isEmpty() && temperatures[stack.peek()] < currentTemp) {
                int prevDay = stack.pop();
                answer[prevDay] = currDay - prevDay;
            }
            stack.push(currDay);
        }

        return answer;
    }

    // V2
    // IDEA : Array, Optimized Space
    // https://leetcode.com/problems/daily-temperatures/editorial/
    public int[] dailyTemperatures_2(int[] temperatures) {
        int n = temperatures.length;
        int hottest = 0;
        int answer[] = new int[n];

        for (int currDay = n - 1; currDay >= 0; currDay--) {
            int currentTemp = temperatures[currDay];
            if (currentTemp >= hottest) {
                hottest = currentTemp;
                continue;
            }

            int days = 1;
            while (temperatures[currDay + days] <= currentTemp) {
                // Use information from answer to search for the next warmer day
                days += answer[currDay + days];
            }
            answer[currDay] = days;
        }

        return answer;
    }

}
