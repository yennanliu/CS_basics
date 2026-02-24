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
    public String removeKdigits(String num, int k) {
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
    // IDEA 3)  brute force ??
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
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
    // IDEA 1) MONO STACK
    class StockSpanner {

        // attr
        //Stack<Integer> st;
        Deque<Integer> deque;

        public StockSpanner() {
            this.deque = new ArrayDeque<>();
        }

        public int next(int price) {
            // MONO STACK
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





}
