package dev;

import java.util.*;

public class Workspace21 {

    // LC 62
    public int uniquePaths(int m, int n) {
        // edge
        if (m == 1 || n == 1) {
            return 1;
        }

        int[][] dp = new int[m][n];
        // init
        for(int y = 0; y < m; y++){
            dp[y][0] =  1;
        }
        for(int x = 0; x < n; x++){
            dp[0][x] =  1;
        }


        for(int y = 1; y < m; y++){
            for(int x = 1; x < n; x++){
                dp[y][x] = dp[y-1][x] + dp[y][x-1];
            }
        }

        ///  /???
        return dp[m-1][n-1];
    }


    // LC 63
    // 17.18 - 28 pm
    /**
     * -> Return the number of possible unique paths
     *   that the robot can take to reach the bottom-right corner.
     *
     *    NOTE:
     *
     *     - An obstacle and space are marked as 1 or 0 respectively in grid.
     *       A path that the robot takes cannot include any square that is an obstacle.
     *
     *  ---------------------
     *
     *   IDEA 1) MULTI SOURCE BFS  -> TLE ???
     *
     *   IDEA 2) DP
     *
     *
     *  ---------------------
     *
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if(obstacleGrid == null){
            return 0;
        }
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        // edge // ???
        if (m == 0 || n == 0) {
            return 0;
        }
        if(obstacleGrid[m-1][n-1] == 1){
            return 0;
        }

        int[][] dp = new int[m][n];
        // init
        for(int y = 0; y < m; y++){
            //dp[y][0] =  1;
            if(obstacleGrid[y][0] != 1){
                dp[y][0] =  1;
            }
        }
        for(int x = 0; x < n; x++){
           // dp[0][x] =  1;
            if(obstacleGrid[0][x] != 1){
                dp[0][x] =  1;
            }
        }


        for(int y = 1; y < m; y++){
            for(int x = 1; x < n; x++){
                // case 1) (x,y) is NOT obstacle
                if(obstacleGrid[y][x] != 1){
                    if(dp[y-1][x] != -1){
                        dp[y][x] += dp[y-1][x];
                    }
                    if(dp[y][x-1] != -1){
                        dp[y][x] += dp[y][x-1];
                    }
                   // dp[y][x] = dp[y-1][x] + dp[y][x-1];
                }
                // case 1) (x,y) is obstacle
                else{
                    dp[y][x] = -1; // ??? // mark as `CAN'T visit`
                }
            }
        }

        ///  /???
        return Math.max(dp[m - 1][n - 1], 0);
    }



    // LC 980
    // 14.35 - 45 pm
    /**
     *
     *   -> Return the `number` of 4-directional walks from the starting square to
     *     the ending square,
     *     that walk over every non-obstacle square exactly once.
     *
     *     - m x n matrix
     *     - 1:  start
     *     - 2: end
     *     - 0: empty
     *     - -1: obstacles
     *
     *
     *     NOTE !!!
     *
     *      1. 4-directional walk
     *      2. can visit `obstacle` once
     *
     *
     *
     *  -----------------
     *
     *   IDEA 1) MULTI SOURCE BFS ???
     *   IDEA 2) 2D DP
     *
     *   IDEA 3) DFS ????
     *
     *
     *  -----------------
     *
     *
     *
     */

    // 11.35 - 45 am
    /**
     *
     *
     *
     */
    // IDEA 1) DFS
    int pathCnt;
    public int uniquePathsIII(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int[] start = null; // ???
        int[] end = null; // ???

        // 0. get start, end cell
        for (int y = 0; y < m; y++) {
            for (int x = 0; x < n; x++) {
                if(grid[y][x] == 1){
                    start = new int[]{x, y};
                }
                if(grid[y][x] == 2){
                    end = new int[]{x, y};
                }
            }
        }

        //int cnt = 0;
       // canReachEnd(start[0], start[1], end[0], end[1]);
//        for (int y = 0; y < m; y++) {
//            for (int x = 0; x < n; x++) {
//                if(canReachEnd(x, y, end[0], end[1])){
//                    cnt += 1;
//                }
//            }
//        }

        canReachEnd(grid, start[0], start[1], end[0], end[1]);

        return pathCnt;
    }


    private void canReachEnd(int[][] grid, int startX, int startY, int endX, int endY){

        int m = grid.length;
        int n = grid[0].length;
        int[][] moves = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

        if(startX == endX && startY == endY){
            // ??
           // return cnt + 1;
            pathCnt += 1;
        }

        for(int[] move: moves){
            int x_ = startX + move[1];
            int y_ = startY + move[0];

            if(x_ >= 0 && x_ < m && y_ >= 0 && y_ < n){
                // visited check ??
                canReachEnd(grid, x_, y_, endX, endY);
            }

        }

       // return cnt;
    }










    // IDEA 2) 2D DP
    public int uniquePathsIII_99(int[][] grid) {

        // edge

        int m = grid.length;
        int n = grid[0].length;

        int[] start = null; // ???
        int[] end = null; // ???

        // 0. get start, end cell
        for (int y = 0; y < m; y++) {
            for (int x = 0; x < n; x++) {
                if(grid[y][x] == 1){
                   start = new int[]{x, y};
                }
                if(grid[y][x] == 2){
                    end = new int[]{x, y};
                }
            }
        }

        System.out.println(">>> start = " + Arrays.toString(start) +
                ", end = " + Arrays.toString(end));

        // 1. Define DP table: dp[y][x] is the number of ways to reach cell (y, x)
        int[][] dp = new int[m][n];

        // 2. Initialize first column: only one way (keep going down)
        for (int y = 0; y < m; y++) {
            // ????
//            if(dp[y][0] == -1){
//                break;
//            }
            dp[y][0] = 1;
        }

        // 3. Initialize first row: only one way (keep going right)
        for (int x = 0; x < n; x++) {
//            if(dp[0][x] == -1){
//                break;
//            }
            dp[0][x] = 1;
        }


        // ???
        return dp[end[1]][end[0]];
    }







    // LC 1920
    // 12.34 - 12.44 PM
    /**
     *  -> build an array ans of the same length where ans[i] = nums[nums[i]]
     *   for each 0 <= i < nums.length and return it.
     *
     *    nums: zero based arr
     *
     *
     *  --------------------
     *
     *
     *
     *  --------------------
     *
     *
     */
    public int[] buildArray(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }
        int[] res = new int[nums.length];
        for(int i = 0; i < nums.length; i++){
            res[i] = nums[nums[i]];
        }

        return res;
    }


    // LC 1089
    // 12.43 - 53 PM
    /**
     *
     *  -> Given a fixed-length integer array arr,
     *     duplicate each occurrence of zero,
     *     shifting the remaining elements to the right.
     *
     *  --------------------
     *
     *   IDEA 1) 2 POINTER
     *
     *   IDEA 2) LIST ???
     *
     *
     *  --------------------
     */
    public void duplicateZeros(int[] arr) {
        // edge
        if(arr == null || arr.length == 0){
            return;
        }

        List<Integer> list = new ArrayList<>();
        for(int x: arr){
            System.out.println(">>> x = " + x +
                    ", x == 0 ?" + (x==0) );
            list.add(x);
            if(x == 0){
                list.add(0);
            }
        }

        arr = new int[list.size()]; // ???
        for(int i = 0; i < list.size(); i++){
            arr[i] = list.get(i);
        }
    }



    // LC 720
    // 13.02 - 12 pm
    /**
     *
     *  -> return the longest word in words that can
     *   be built one character at a time by other words in words.
     *
     *
     *   NOTE:
     *
     *    - If there is more than one possible answer,
     *        - return the longest word with the smallest lexicographical order.
     *
     *    - If there is no answer,
     *       - return the empty string.
     *
     *  ---------------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *     - loop over word in words,
     *     - check if the `word` can be built from remaining word
     *     - maintain the candidate on the same time:
     *        { len : [w1, w2, ...] }
     *     - get the longest word, if there is a tie
     *       , return the one with smallest lexicographical order
     *
     *   IDEA 2) TRIE + DFS ????
     *
     *
     *  ---------------------
     *
     */
    // IDEA 1) BRUTE FORCE
    public String longestWord(String[] words) {
        // edge
        if(words == null || words.length == 0){
            return ""; // ???
        }
        if(words.length == 1){
            return ""; // /??
        }

        // NOTE !!!
        // 1. Sort words:
        // Primary: by length (small to large)
        // Secondary: lexicographical (alphabetical)
        Arrays.sort(words);


        // cache
        // map: { len : [w1, w2, .. ] }
        Map<Integer, List<String>> map = new HashMap<>();

        int maxLen = 0;

        for(String w: words){
            if(canBuilt(w, words)){
                int len = w.length();
                if(!map.containsKey(len)){
                    map.put(len, new ArrayList<>());
                }
                List<String> list = map.get(len);
                list.add(w);
                map.put(len, list);
                maxLen = Math.max(maxLen, len);
            }
        }

        List<String> list = map.get(maxLen);

        if(list.isEmpty()){
            return ""; // /??
        }


        // sort on lexicographical (small -> big)
        // ??
        // sort anyway
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // ????
                int diff = o1.compareTo(o2); // /???
                return diff;
            }
        });


        return list.get(0);
    }



    private boolean canBuilt(String word, String[] words){
        for(String w: words){
            if(!word.startsWith(w)){
                return false;
            }
        }

        return true;
    }




    // LC 1023
    // 13.50 - 14.09 PM
    /**
     *
     *  ->  return
     *        true: if queries[i] matches pattern
     *        false: otherwise.
     *
     *     queries: query list
     *     pattern: str
     *
     *     NOTE:
     *
     *        `query matches word`
     *          -  if you can insert lowercase English letters
     *             into the pattern so that it equals the query.
     *
     *          - You may insert a character `at any position in pattern `
     *             or you may choose NOT to insert any characters at all.
     *
     *
     *  --------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) 2 POINTERS ????
     *
     *   IDEA 3) DP ???
     *
     *    -
     *
     *  --------------
     *
     *
     */
    public List<Boolean> camelMatch(String[] queries, String pattern) {

        return null;
    }


    // LC 881
    // 15.50 - 16.00
    /**
     *
     *  -> Return the minimum number
     *  of boats to carry every given person.
     *
     *  - people[i] is the weight of i person
     *  - infinite number of boats
     *  - limit:  each boat can carry a maximum weight
     *
     *   NOTE:
     *    -  Each boat carries
     *      `at most` two people at the same time
     *
     *
     *  -----------------------
     *
     *
     *   IDEA 1) SORTING + BRUTE FORCE
     *
     *   IDEA 2) SORTING + GREEDY
     *
     *
     *
     *  -----------------------
     *
     *
     *
     *
     */
    public int numRescueBoats(int[] people, int limit) {
        // edge

        // sort (small -> big)
        // ???
        Arrays.sort(people);
        System.out.println(">>> (after sort) people = " + Arrays.toString(people));

        int i = 0;
        int weightSum = 0;
        int cnt  = 0;
        int boat = 1;

        while(i < people.length){
            weightSum += people[i];
            cnt += 1;
            if(weightSum >= limit || cnt > 2){
                boat += 1;
                // reset
                weightSum = people[i];
                cnt = 1;
            }
            i += 1;
        }

        return boat;
    }


    // LC 455
    // 16.19 - 29 pm
    /**
     *  ->  Your goal is to maximize the
     *    number of your content children
     *    and output the maximum number.
     *
     *
     *
     *
     *  But, you should give each child at most one cookie.
     *
     *  -------------
     *
     *   IDEA 1) SORT ????
     *
     *
     *  -------------
     *
     *
     *
     *
     */
    // IDEA 1) SORT ????
    public int findContentChildren_99(int[] g, int[] s) {
        // edge

        // ??? sort (small -> big)
        Arrays.sort(g);
        Arrays.sort(s);

        int cnt = 0;

        // g pointer
        int i = 0;
        // s pointer
        int j = s.length -1;

        while(i < g.length && j > 0){
            if(s[j] >= g[i]){
                i += 1;
                j -= 1;
                cnt += 1;
            }else{
                j -= 1;
            }
        }

        return cnt;
    }




    public int findContentChildren(int[] g, int[] s) {
        // edge

        // ??? sort (small -> big)
        Arrays.sort(g);
        Arrays.sort(s);

        int cnt = 0;

        // g pointer
        int i = 0;
        // s pointer
        int j = 0;

        while (i < g.length && j < s.length) {
            if (s[j] >= g[i]) {
                i += 1;
                //j += 1;
                cnt += 1;
            }

            j += 1;
        }

        return cnt;
    }



    // LC 1094
    // 16.55 - 17.05 pm
    /**
     *
     *   -> Return true if it is possible to pick up
     *     and drop off all passengers for all the given trips,
     *     or false otherwise.
     *
     *     - trips[i] = [numPassengersi, fromi, toi]
     *
     *     - car can ONLY drive to `east`
     *     -
     *
     *
     *
     *   --------------------
     *
     *    IDEA 1) PREFIX SUM
     *
     *
     *   --------------------
     */
    //  IDEA 1) PREFIX SUM
    public boolean carPooling(int[][] trips, int capacity) {
        // edge
        if (trips == null || trips.length == 0)
            return true;


        int globalEnd = 0;
        for(int i = 0; i < trips.length; i++){
            globalEnd = Math.max(globalEnd, trips[i][2]);
        }


        // prefix sum
        //int[] prefix = new int[globalEnd]; // ???
        int[] prefix = new int[globalEnd + 1]; // ???



        // ??
        for(int i = 0; i < globalEnd; i++){
            int[] trip = trips[i];
            int cap = trip[0];
            int start = trip[1];
            int end = trip[2];

            // ????
            prefix[start] += cap;
            //prefix[end] -= cap;
            /** NOTE !!!
             *
             *  since `DROP OFF` at `end` index
             *  -> so we should do the `minus op` at `end` index (instead of `end + 1`)
             */
            if (end < prefix.length) {
                prefix[end] -= cap; // drop them off before `end`
            }
        }

        // update prefix sum arr
        int prefixSum = 0;
//        for(int i = 0; i < prefix.length; i++){
//            prefixSum += prefix[i];
//            prefix[i] = prefixSum;
//        }

        System.out.println(">>> prefix =  " + Arrays.toString(prefix));

        // validate
        for(int i = 0; i < prefix.length; i++){
            if(prefix[i] > capacity){
                return false;
            }
        }


        return true;
    }




    // LC 682
    // 8.51 - 9.01 am
    /**
     *  -> Return the sum of all the scores
     *  on the record after applying all the operations.
     *
     *
     *  ----------------
     *
     *  IDEA 1) STACK (FILO)
     *
     *  ----------------
     *
     */
    // IDEA 1) STACK (FILO)
    public int calPoints(String[] operations) {
        // edge
        if(operations == null || operations.length == 0){
            return 0;
        }

        Stack<Integer> st = new Stack<>();

        for(String op: operations){
            if(op.equals("C")){
                if(!st.isEmpty()){
                    st.pop();
                }
            }else if(op.equals("D")){
                if(!st.isEmpty()){
//                    int prev = st.peek();
//                    st.add(prev * 2);
                    st.push(st.peek() * 2);
                }
            }else if(op.equals("+")){
                if(st.size() >= 2){
//                    int prev1 = st.pop();
//                    int prev2 = st.pop();
//                    int newVal = prev1 + prev2;
//                    //st.add(prev1 + prev2);
//                    st.add(prev1);
//                    st.add(prev2);
//                    st.add(newVal);
                    int last = st.pop();
                    int secondLast = st.peek();
                    int newScore = last + secondLast;

                    // Put 'last' back, then add the 'newScore'
                    st.push(last);
                    st.push(newScore);

                }
            }else{
                st.add(Integer.parseInt(op));
            }
        }

        int res = 0;
        while(!st.isEmpty()){
            res += st.pop();
        }

        return res;
    }




    // LC 402
    // 9.11 am - 21 am
    /**
     *  ->  return the smallest possible
     *    integer after removing k digits from num.
     *
     *    - num: int
     *    - k: int
     *
     *
     *  ------------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) 2 POINTERS ???
     *
     *   IDEA 3) greedy ???
     *
     *
     *  ------------------
     *
     *   ex 1)
     *
     *   Input: num = "1432219", k = 3
     *   Output: "1219"
     *
     *
     *  ->
     *
     *    1432219
     *
     *    order from big -> small
     *      9432211
     *
     *
     *
     */

    // IDEA 1) MONO STACK
    // 17.44 - 54 pm
    /**
     *
     *   ->
     *
     *    return the smallest possible integer
     *    after removing k digits from num.
     *
     *
     */
    public String removeKdigits(String num, int k) {
        // edge
        int n = num.length();
        if (k == n){
            return "0";
        }

        // mono stack
        // big -> small
        Stack<Integer> st = new Stack<>();

        for(String x: num.split("")){
            int val = Integer.parseInt(x);
            while(k > 0 && !st.isEmpty() && st.peek() > val){
                st.pop();
                k -= 1;
            }
            st.add(val);
        }

        StringBuilder sb = new StringBuilder();

        /** NOTE !!!
         *
         *  below:
         *
         */


        // 4. Remove leading zeros correctly
//        String res = sb.toString();
//        int nonZeroIndex = 0;
//        while (nonZeroIndex < res.length() && res.charAt(nonZeroIndex) == '0') {
//            nonZeroIndex++;
//        }


        // edge: stack size = 0
        // ???
        if(st.isEmpty()){
            return null;
        }

        // edge: stack size = 1
        if(st.size() == 1){
            sb.append(st.pop());
            return sb.toString();
        }

        // edge: handle `0` start
      //  int cnt = 0;
        for(Integer x: st){
//            if(cnt == 0 && x == 0){
//                continue;
//            }
            sb.append(x);
           // cnt += 1;
        }

       // ??? remove start from 0
        String res = sb.toString();
        while(res.startsWith("0")){
            res = res.substring(1, res.length()-1);
        }


        return res;
    }







    // IDEA 1) MONO STACK
    public String removeKdigits_99(String num, int k) {
        // edge
        int n = num.length();
        if (k == n)
            return "0";

        //Deque<Integer> deque = new ArrayDeque<>();
        Deque<Character> stack = new ArrayDeque<>();
        for(int i = 0; i < n; i++){
            //int val = Integer.parseInt(x);

           char digit = num.charAt(i);


            // While we still need to remove digits and the current digit
            // is smaller than the top of the stack, pop the stack.
            while (k > 0 && !stack.isEmpty() && stack.peekLast() > digit) {
                stack.removeLast();
                k--;
            }
            stack.addLast(digit);


            // If k is still > 0, remove digits from the tail
            while (k > 0) {
                stack.removeLast();
                k--;
            }


//            if(deque.isEmpty()){
//                deque.addLast(val);
//            }
//            else{
//                if(val < deque.peek() && k > 0){
//                    deque.pop();
//                    deque.addLast(val);
//                    k -= 1;
//                }
//                else{
//                    deque.addLast(val);
//                }
//            }


        }

        // edge case


        return null;
    }


    // LC 496
    // 9.50 - 10.00 am
    /**
     *  -> Return an array ans of length nums1.length
     *    such that ans[i] is the next greater
     *    element as described above.
     *
     *
     *  ------------------
     *
     *   IDEA 1) 2 POINTERS ???
     *
     *   IDEA 2)  STACK ???
     *
     *   IDEA 3)  brute force ??
     *
     *
     *  ------------------
     *
     *
     */

    // 18.21 - 31 pm
    /**
     *  IDEA 1) MONO STACK
     *
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        // edge

        int[] res = new int[nums1.length];
        Arrays.fill(res, - 1);

        // map : { val : next_big_index } nums2
        Map<Integer, Integer> map = new HashMap<>();

        // loop over nums2
        // mono st ???
        // small -> big
        Stack<Integer> st = new Stack<>();

        for(int i = 0; i < nums2.length; i++){
            int x = nums2[i];
            while(!st.isEmpty() && st.peek() < x){
                int val = st.pop(); // ??
                if(!map.containsKey(x)){
                    map.put(val, i);
                }
            }
            st.add(x);
        }

        for(int i = 0; i < nums1.length; i++){
            int x = nums1[i];
            if(map.containsKey(x)){
                res[i] = map.get(x);
            }
        }


        return res;
    }







    // IDEA 3)  brute force ??
    public int[] nextGreaterElement_99(int[] nums1, int[] nums2) {
        // edge

        int[] res = new int[nums1.length];
        Arrays.fill(res, - 1);

        for(int i = 0; i < nums1.length; i++){
            int x1 = nums1[i];
            Queue<Integer> q = arrToQueue(nums2);
            boolean found = false;
           // int j = 0;
            while(!q.isEmpty()){
                int val = q.poll();
                if(val == x1){
                    found = true;
                }
                if(val > x1 && found){
                    res[i] = val;
                    break;
                }
               // j += 1;
            }
        }

        return res;
    }


    private Queue<Integer> arrToQueue(int[] nums){
        Queue<Integer> q = new LinkedList<>();
        for(int x: nums){
            q.add(x);
        }
        return q;
    }


    // LC 901
    // 10.35 - 10 am
    /**
     *
     *  -> Implement the StockSpanner class:
     *
     * StockSpanner() Initializes the object of the class.
     * int next(int price) Returns the span of the
     * stock's price given that today's price is price.
     *
     *
     * --------------------
     *
     *  IDEA 1) MONO STACK
     *
     *
     * --------------------
     *
     */
    // 20.21 - 31 pm
    /**
     *
     *
     *  - the span of the stock's price in one day
     *   is the maximum number of consecutive days
     *   (starting from that day and going backward)
     *  for which the stock price was less than
     *   or equal to the price of that day.
     *
     *
     *   ---------------
     *
     *   IDEA 1)
     *
     *    MONO STACK + HASHMAP (record next bigger val idx )
     *
     *    { val : next_bigger_val_idx }
     *
     *   ---------------
     *
     */
    class StockSpanner {

        // attr
        Map<Integer, Integer> map;
        // ???
        //Stack<Integer> st;
        // stack : { [idx, val] } // ????
        Stack<Integer[]> st;

        public StockSpanner() {
            this.map = new HashMap<>();
            this.st = new Stack<>();
        }

        public int next(int price) {
//            // ??
//            if(this.st.isEmpty()){
//                // ???
//                this.st.add(new Integer[]{0, price}); // ???
//                return 0;
//            }

            // ???
            int span = 1;

            // mono stack
            // small -> big
            // ???
            Integer[] val = null; // /?
            while(!st.isEmpty() && st.peek()[1] <= price){
//                val = st.pop();
//                // record via map ????
//                map.put(val[1], 1);
                span += st.pop()[0];
            }
            st.add(new Integer[]{span, price } );

            return span;
        }

    }








    // IDEA 1) MONO STACK
    class StockSpanner_99 {

        // attr
        //Stack<Integer> st;
        Deque<Integer> deque;

        public StockSpanner_99() {
            this.deque = new ArrayDeque<>();
        }

        public int next(int price) {

            int span = 1; // Today counts as 1


            // MONO STACK
            while (!this.deque.isEmpty() && this.deque.peek() <= price){
                span += this.deque.pop();

            }

            if(this.deque.isEmpty()){
                this.deque.add(price);
                return 1;
            }

            List<Integer> cache = new ArrayList<>();
            int cnt = 0;
            while(this.deque.peek() <= price){
                // ???
                cache.add(this.deque.pollLast());
                cnt += 1;
            }
            // add them back
            for(int x: cache){
                this.deque.addLast(x);
            }
            this.deque.addLast(price);

            return cnt;
        }

    }



    // LC 844
    // 9.39 - 49 am
    /**
     *
     *  -> return true if they are equal when both
     *  are typed into empty text editors.
     *  '#' means a backspace character.
     *
     *
     * ----------------
     *
     *  IDEA 1) STACK
     *
     *
     * ----------------
     *
     *
     */
    // IDEA 1) STACK
    public boolean backspaceCompare(String s, String t) {
        // edge

        Stack<String> st1 = new Stack<>();
        Stack<String> st2 = new Stack<>();

        for(String x: s.split("")){
            if(x.equals("#")){
                if(!st1.isEmpty()){
                    st1.pop();
                }
            }else{
                st1.push(x); // ???
            }
        }

        for(String x: t.split("")){
            if(x.equals("#")){
                if(!st2.isEmpty()){
                    st2.pop();
                }
            }else{
                st2.push(x); // ???
            }
        }

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        while (!st1.isEmpty()){
            sb1.append(st1.pop());
        }

        while (!st2.isEmpty()){
            sb2.append(st2.pop());
        }

        System.out.println(">>> sb1 = " + sb1.toString());
        System.out.println(">>> sb2 = " + sb2.toString());

        return sb1.toString().contentEquals(sb2);
    }


    // LC 316
    // 9.52 - 10.02 am
    /**
     *
     *  -> remove duplicate letters so
     *  that every letter appears once and only once.
     *
     *  -------------------
     *
     *   IDEA 1) SET + REORDER ???
     *
     *
     *  -------------------
     *
     *
     *
     */
    // 19.51 - 20.00 pm
    /**
     *  -> remove duplicate letters
     *  so that every letter appears once and only once.
     *
     *  ------------
     *
     *   IDEA 1)
     *
     *    MONO STACK + MAP record `last idx`
     *
     */
    public String removeDuplicateLetters(String s) {
        // edge
        if (s == null || s.length() == 0) {
            return "";
        }


        // ???
        // { val : last_exist_idx }
        Map<String, Integer> map = new HashMap<>();
        int i = 0;
        for(String x: s.split("")){
            map.put(x, i);
            i += 1;
        }

        // 2. We need a way to check if a character is already in our result stack
        Set<String> inStack = new HashSet<>();


        Stack<String> st = new Stack<>();
        int j = 0; // ???
        for(String x: s.split("")){
            // ???
            /**  pop last element from stack when all below condition met
             *
             *  1. stack is NOT empty
             *  2. stack last element > new element (lexicographically)
             *  3. stack last element last idx > cur idx (will appear again later)
             *
             */
            while(!st.isEmpty() && st.peek().compareTo(x) > 0 && map.get(st.peek()) > j ){
                st.pop();
            }
            // ???
            if(!inStack.contains(x)){
                st.add(x);
                inStack.add(x);
            }
            j += 1;
        }

        StringBuilder sb = new StringBuilder();
        for(String y: st){
            sb.append(y);
        }


        return sb.toString();
    }








    // IDEA 1) MONO STACK
    public String removeDuplicateLetters_98(String s) {
        // edge
        if(s.isEmpty()){
            return s;
        }
        if(s.length() == 1){
            return s;
        }

        // ???
        //Set<String> set = new HashSet<>();
        HashMap<String, Integer> map = new HashMap<>();

        // mono stack
        // small -> big
        Stack<String> st = new Stack<>();

        for(String x: s.split("")){
            // last element in stack is `bigger` then x
            while(!st.isEmpty() && st.peek().compareTo(x) > 0) {
                String val = st.pop();
                map.remove(val);
            }

            if(!map.containsKey(x)){
                st.add(x);
                map.put(x, 1);
            }
        }

        StringBuilder sb = new StringBuilder();
        while(!st.isEmpty()){
            sb.append(st.pop());
        }


        return sb.reverse().toString();
    }







    // IDEA 1) SET + REORDER ???
    public String removeDuplicateLetters_99(String s) {
        // edge
        Set<String> set = new HashSet<>();
        for(String x: s.split("")){
            set.add(x);
        }
        System.out.println(">>> set = " + set);
        StringBuilder sb = new StringBuilder();
        //char[] chars = new char[];
        for(String x: set){
            sb.append(x);
        }
        // ???
        String res = sb.toString();
        char[] chars = res.toCharArray();
        Arrays.sort(chars);

        // ???
        return String.valueOf(chars); // ???
    }



    // LC 71
    // 10.46 - 56 am
    /** *
     *
     *  -> Return the simplified canonical path.
     *
     *
     *  ---------------
     *
     *   IDEA 1) STACK
     *
     *
     *  ---------------
     *
     *
     */
    public String simplifyPath(String path) {
        // edge
        if(path.isEmpty()){
            return path;
        }
        if(path.equals("/../")){
            return "/";
        }
        // ???
        if(path.equals("..") || path.equals(".")){
            return ".";
        }

        //Stack<String> st = new Stack<>();
        Deque<String> deque = new ArrayDeque<>();

        // 2. loop over element
        for(String p: path.split("/")){
            System.out.println(">>> p = " + p);
            if(p.equals("..")){
                if(!deque.isEmpty()){
                    deque.pollLast();
                }
            }
            // ???
            else if(p.equals(".") || p.isEmpty()){
                continue;
            }else{
                //st.add(p);
                deque.addLast(p);
            }
        }


        // 2. If the deque is empty, the simplified path is just the root "/"
        if (deque.isEmpty()) {
            return "/";
        }

        StringBuilder sb = new StringBuilder();
        // transform to str
        int cnt = 0;
        while(!deque.isEmpty()){
//            sb.append(deque.pollFirst());
//            if(cnt < deque.size()){
//                sb.append("/");
//            }
//            cnt += 1;

            sb.append("/"); // Prepend the slash
            sb.append(deque.pollFirst());
        }


        return sb.toString();
    }



    // LC 591
    public boolean isValid(String code) {

        return false;
    }

    
    // LC 35
    // 18.08-18
    /**
     * -> return the index if the target is found.
     *  If not, return the index where it
     *  would be if it were inserted in order.
     *
     *  Given a sorted array of distinct integers
     *
     *
     *  ------------------
     *   IDEA 1) BINARY SEARCH
     *
     *
     *  ------------------
     *
     *
     *
     */
    // IDEA 1) BINARY SEARCH
    public int searchInsert(int[] nums, int target) {
        // edge
        if(target < nums[0]){
            return 0;
        }
        if(target > nums[nums.length - 1]){
            return nums.length;
        }

        int l = 0;
        int r = nums.length - 1;
        // ???
        int mid = -1;
        while (r >= l) {
            // ??
            mid = (l + r) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }

        System.out.println(">>> mid = " + mid);
        // ??
        //        if(target > nums[mid]){
        //            return nums[mid] + 1;
        //        }
        //
        //        return  nums[mid] -1;
        return target > nums[mid] ? mid + 1 : mid - 1;
    }


    // LC 374
    // 18.50 - 19.00
    /**
     *  -> Return the number that I picked.
     *
     *   - I pick a number from 1 to n.
     *
     *
     *  ----------------
     *
     *  ----------------
     *
     */
    public int guessNumber(int n) {
        // edge

        int l = 0;
        int r = n; // ???
        while(r >= l){
            //int mid = (l + r) / 2;
            int mid =  l +  ( (r - l) / 2 ); // ???
            int resp = guess(mid);
            if(resp == 0){
                return mid;
            }else if(resp == -1){
                r = mid -1;
            }else{
                l = mid + 1;
            }
        }

        // ???
        return -1;
    }

    // NOTE !! below is just a placeholder func
    // so in LC console, it has pre-defined guess func
    // we have below func just for avoid java syntax complaining.
    private int guess(int num){
        return 0;
    }


    // LC 852
    // 19.00 -18 pm
    /**
     *
     *  -> Return the index of the peak element.
     *
     *  - where the values increase
     *    to a peak element and then decrease.
     *
     *  ------------
     *
     *  IDEA 1) BINARY SEARCH
     *
     *     - case 1)
     *         mid is part of `left increase` sub arr
     *           - if mid < right
     *           - if mid > left
     *
     *     - case 2)
     *         mid is part of `right decrease` sub arr
     *            - if mid < right
     *            - if mid > left
     *
     *  ------------
     */
    // IDEA 1) BINARY SEARCH
    public int peakIndexInMountainArray(int[] arr) {
        // edge
        if (arr == null || arr.length < 3) {
            return -1; // Return -1 if the array length is less than 3
        }

        //int l = 0;
        int l = 1; // ?????
        //int r = arr.length - 1; // ??
        int r = arr.length - 2; // ??


        while(r >= l){

            int mid =  l +  ( (r - l) / 2 ); // ???

            // ???
            /**
             *  case 1) peak is found
             */
            // if found ??
            if(arr[mid] > arr[mid - 1] && arr[mid] > arr[mid + 1]){
                return mid;
            }

            /**
             *  case 2) `left` part is increasing. peak in on the `right` part
             *
             *   345 7 43
             *
             *   345 4 43
             *
             *   365743
             */
            if(arr[mid] < arr[mid + 1]){
                l = mid + 1;
            }else{
                r = mid - 1;
            }


            /**
             *      *     - case 1)
             *      *         mid is part of `left increase` sub arr
             *      *           - if mid < right
             *      *           - if mid > left
             */
            // ???
//            if(arr[mid] >= arr[l]){
//              //  if()
//            }

            /**
             *     *     - case 2)
             *      *         mid is part of `right decrease` sub arr
             *      *            - if mid < right
             *      *            - if mid > left
             *
             */
        }



        // ???
        return -1;
    }


    // LC 33
    // 14.28 - 38 pm
    /**
     *  -> Given the array nums after the possible rotation and an integer target,
     *  return the index of target
     *  if it is in nums, or -1 if it is not in nums.
     *
     * nums:  sorted in ascending order
     *
     * --------------------
     *
     *  IDEA 1) BINARY SEARCH
     *    case 1) target is in left ascending order
     *        target < mid
     *        target > mid
     *
     *    case 1) target is in right ascending order
     *         target < mid
     *
     * --------------------
     */
    public int search(int[] nums, int target) {
        // edge
        if(nums == null || nums.length == 0){
            return -1;
        }
        if(nums.length == 1){
            return nums[0] == target ? 0 : -1;
        }


        // ???
//        int l = 1;
//        int r = nums.length -2;
        int l = 0;                     // FIXED
        int r = nums.length - 1;       // FIXED


        while(r >= l){

            int mid =  l +  ( (r - l) / 2 ); // ???
            int val = nums[mid];
            if(val == target){
                return mid;
            }
            // ???
            // else if(nums[mid] < nums[mid + 1]){
            else if(nums[mid] >= nums[l]){
                // /?? ???
                if(nums[l] <= target && target < val){
                    r = mid - 1;
                }else{
                    l = mid + 1;
                }
            }else{
                // if(target > nums[mid]){
                if(target > val && target <= nums[r]){
                    l = mid + 1;
                }else{
                    r = mid - 1;
                }
            }

        }


        return -1;
    }


    // LC 153
    // 14.53 - 15.03 pm
    /**
     *
     *  Given the sorted rotated array nums of unique elements,
     *
     *  -> return the minimum element of this array.
     *
     *
     *  -----------------
     *
     *  IDEA 1) BINARY SEARCH
     *   -> find pivot
     *
     *    case 1) mid is in `left increasing sub arr`
     *
     *    case 2) mid is in `right decreasing sub arr`
     *
     *
     *  -----------------
     *
     */
    public int findMin(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return -1;
        }
        if(nums.length == 1){
            return nums[0];
        }
        // if already sort
        if(nums[0] < nums[nums.length - 1]){
            return nums[0];
        }

        int l = 0;
        int r = nums.length - 1;

        while(r >= l){

            int mid =  l +  ( (r - l) / 2 ); // ???
            int val = nums[mid];

            if(nums[l] < nums[r]){
                return Math.min(val, nums[l]);
            }

            // case 0) found a pivot ???

            // case 1) mid is in `left increasing sub arr`
            if(val >= nums[l]){
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }

            // case 2) mid is in `right decreasing sub arr`

//            // case 1) mid is in `left increasing sub arr`
//            // ???
//            if(val > nums[l]){
//                // ???
//                if(nums[l] < nums[r]){
//                    r = mid - 1;
//                }else{
//                    l = mid + 1;
//                }
//            }
//            /**
//             *
//             * [5,6,7,0,1,2,4]
//             */
//            // case 2) mid is in `right decreasing sub arr`
//            else{
//                if(nums[r] < nums[l]){
//                    l = mid + 1;
//                }else{
//                    r = mid - 1;
//                }
//            }
//        }



        return nums[l]; // /??
    }


    // LC 336
    // 15.31 - 41 pm
    /**
     *  -> Return an array of
     *     ALL the `palindrome` pairs of words.
     *
     *  - palindrome pair:
     *    - (i, j)
     *    - 0 <= i, j < words.length,
     *    - i != j
     *    - words[i] + words[j]
     *      (the concatenation of the two strings)
     *      is a` palindrome.`
     *
     *
     *   NOTE:
     *    - A palindrome is a string
     *      that reads the same forward and backward.
     *
     *  --------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) HASHMAP ???
     *
     *  --------------
     *
     *
     */
    // IDEA 1) BRUTE FORCE
    public List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> res = new ArrayList<>();
        // edge
        if(words == null || words.length == 0){
            return res;
        }


        // double loop
        for(int i = 0; i < words.length; i++){
            for(int j = i+1; j < words.length; j++){
                StringBuilder sb = new StringBuilder();
                // normal order
                sb.append(words[i]);
                sb.append(words[j]);
                if(isPalindrome(sb.toString())){
                    List<Integer> tmp1 = new ArrayList<>();
                    tmp1.add(i);
                    tmp1.add(j);
                    res.add(tmp1);
                }

                //StringBuilder sb2 = new StringBuilder();
                // rever order
                if(isPalindrome(sb.reverse().toString())){
                    List<Integer> tmp2= new ArrayList<>();
                    tmp2.add(j);
                    tmp2.add(i);
                    res.add(tmp2);
                }

            }
        }

        // sort
        Collections.sort(res, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                int diff = o1.get(0).compareTo(o2.get(0));
                return diff;
            }
        });
        return res;
    }

    private boolean isPalindrome(String x){
        int l = 0;
        int r = x.length() - 1;
        while(r > l){
            if(x.charAt(l) != x.charAt(r)){
                return false;
            }
            l += 1;
            r -= 1;
        }
        return true;
    }


    // LC 1032
    // 16.03 - 37 pm
    /**
     *
     *
     *  -> Design an algorithm that accepts a stream
     *     of characters and checks if a `suffix`
     *     of these characters is a string of a
     *     given array of strings words.
     *
     *
     *  ---------------
     *
     *   IDEA 1) trie ?? (inverse)
     *
     *   IDEA 2) brute force  ?? (inverse)
     *
     *   IDEA 3) dfs ?????
     *
     *
     *
     *  ---------------
     */
    class MyNode{
        // /??
        Map<String, MyNode> child;
        boolean isEnd;

        MyNode(){
            this.child = new HashMap<>();
            this.isEnd = false; // ??
        }

    }
    class MyTrie{
        // ???
        MyNode node;

        MyTrie(){
            this.node = new MyNode(); // /??
        }

        // ???
        public void add(String word){
            MyNode node = this.node;
            // /??
            if(word == null){
               // return true;
                return; // ???
            }
            for(String x: word.split("")){
                if(node.child.containsKey(x)){
                    node.child.put(x, new MyNode());
                }
                node = node.child.get(x);
            }

            node.isEnd = true;
           // return true;
        }

        private boolean isCotain(String word){
            MyNode node = this.node;
            // /??
            if(word == null){
                return true; // ???
            }
            for(String x: word.split("")){
                if(!node.child.containsKey(x)){
                    return false;
                }
                node = node.child.get(x);
            }
            return node.isEnd;
        }

        private boolean isStartWith(String word){
            MyNode node = this.node;
            if(word == null){
                return true; // ???
            }
            for(String x: word.split("")){
                if(!node.child.containsKey(x)){
                    return false;
                }
                node = node.child.get(x);
            }
            return true;
        }

    }


    class StreamChecker {

        MyTrie trie;
        String[] words;
        // ???
        StringBuilder sb;

        public StreamChecker(String[] words) {
            this.trie = new MyTrie();
            this.words = words;
            this.sb = new StringBuilder();
        }

        public boolean query(char letter) {
            sb.append(letter);

            // /?? reverse
            String str = sb.reverse().toString();
            // add again to trie
          //  for(String st)

            return false;
        }
    }







}
