package ws;

//import LeetCodeJava.DataStructure.TreeNode;
//import com.sun.org.apache.bcel.internal.generic.IINC;
//import com.sun.org.apache.bcel.internal.generic.SIPUSH;


import java.math.BigInteger;
import java.util.*;


public class Workspace26 {

    // LC 362
    // 3.34 - 44 pm
    // IDEA:TREE MAP ????
    /**
     *  -> Design a hit counter
     *    which counts the number of
     *    hits received in the `past 5 minutes.`
     *
     *
     *    --------
     *
     *     // IDEA 1) TREE MAP ????
     *
     *
     */
    class HitCounter {

        // attr
        // map: { timestamp : cnt }
        Map<Integer, Integer> map;

//        /** Initialize your data structure here. */
        public HitCounter() {
            //counter = new HashMap<>(); // ?? check
            // ??? via below, we can `loop` over tree map's
            // key in reverse order ?? (e.g. big -> small)
            map = new TreeMap<>(Comparator.reverseOrder()); // ???
        }

        /**
         Record a hit.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public void hit(int timestamp) {
            // ??
            map.put(timestamp, map.getOrDefault(timestamp, 0) + 1);
        }

        /**
         Return the number of hits in the past 5 minutes.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public int getHits(int timestamp) {
            // ???
            int cnt = 0;
            for(int k: map.keySet()){
                // if(k < timestamp - 5 * 60){ ???
//                if(k < timestamp - (5 * 60 - 1)){
//                    break;
//                }
                // outside 5-minute window
                if (k <= timestamp - 300) {
                    break;
                }

                cnt += map.get(k);
            }

            return cnt;
        }
    }



    // LC 678
    // 3.53 - 4.03 pm
    /**
     *
     *  -> Given a string s containing only three
     *     types of characters: '(', ')' and '*',
     *     return true if s is valid.
     *
     *
     * ---------------
     *
     *
     *  IDEA 1) 2 POINTERS ???
     *
     *  IDEA 2) STACK ??
     *
     *  IDEA 3) DP ???
     *
     *  IDEA 4) GREEDY ???
     *    - '(' max, min cnt
     *    - ')' max, min cnt
     *
     *
     * ---------------
     *
     *
     *
     */
    //IDEA 4) GREEDY ???
    public boolean checkValidString(String s) {
        // edge

        int minLCnt = 0;
        int maxLCnt = 0;

//        int minRCnt = 0;
//        int maxRCnt = 0;

        // ??

        for(char ch: s.toCharArray()){
            // '('
            if(ch == '('){
                minLCnt += 1;
                maxLCnt += 1;
            }
            // ')'
            else if(ch == ')'){
                // ???
//                minRCnt += 1;
//                maxRCnt += 1;
                minLCnt -= 1;
                maxLCnt -= 1;
            }
            // '*'
            else{
                // ???
//                minLCnt += 1;
//                minRCnt += 1;
                minLCnt -= 1;
                maxLCnt += 1;
            }


            // ???
            if(maxLCnt <= 0){
                return false;
            }


            // ???
            // we reset `minLCnt` if < 0
            // at this point ????
            minLCnt = Math.max(0, minLCnt); // ???
        }



        // ???
        //return minLCnt == minRCnt || maxLCnt == maxRCnt;
       // return true;
        return  minLCnt == 0; // ?????
    }



    // LC 845
    // 4.37 - 47 pm
    /**
     * -> Given an integer array arr,
     *    return the length of the longest subarray,
     *    which is a `mountain. `
     *
     *    - Return 0 if there is no mountain subarray.
     *
     *
     * -----------
     *
     *  IDEA 1) 2 POINTERS
     *
     *  IDEA 2) SLIDE WINDOW ???
     *
     *
     */
    //IDEA 1) 2 POINTERS  (WITH `RIGHT` pointer moving optimization)
    // 5.08 - 29 pm
    public int longestMountain(int[] arr) {
        // edge
        if(arr == null || arr.length < 3){
            return 0;
        }
        if(arr.length == 3){
            if (arr[1] > arr[0] && arr[1] > arr[2]){
                return 3;
            }
            return 0;
        }

        // ??
        int res = 0;

        // ???
        for(int i = 1; i < arr.length - 1; i++){
            // ???
            //int l = 1;
            int l = i + 1; // ??
            int r = arr.length - 1;
            // ??
            int cur = arr[i];

            // edge
            if(cur < arr[i-1] || cur < arr[i+1]){
                continue;
            }

            // expand `boundary`
            while (l >= 0 && r <= arr.length - 1){

//                if(cur < arr[i-1] || cur < arr[i+1]){
//                    break;
//                }
                // ??
                //int cur = arr[i];
                if(arr[l] > arr[l+1]){
                    l -= 1;
                }
                if(arr[r] > arr[r+1]){
                    l += 1;
                }
            }
            // ??
            res = Math.max(res, r - l + 1);

        }

        return res;
    }










    //  IDEA 1) 2 POINTERS
    // time: O (N * N) // ???
    // space:  O(1) // ??
    public int longestMountain_99(int[] arr) {
        // edge
        if(arr == null || arr.length < 3){
            return 0;
        }
        if(arr.length == 3){
            if (arr[1] > arr[0] && arr[1] > arr[2]){
                return 3;
            }
            return 0;
        }

        int maxLen = 0;
        for(int i = 0; i < arr.length; i++){
            maxLen = Math.max(
                    maxLen,
                    getMountainLen(arr, i)
            );
        }


        return maxLen;
    }



    private int getMountainLen(int[] arr, int start){
        int len = 0;
        // ???
        if(start == 0 || start == arr.length - 1){
            return 0;
        }
        // ??
        int peak = arr[start];
        // ??
        int l = start - 1;
        int r = start + 1;
        while (peak > arr[l] && peak > arr[r] && l > 0 && r < arr.length){
            l -= 1;
            r += 1;
            len += 2;
        }

        return len;
    }



    // LC 1794
    // 3.13 - 23 pm
    /**
     *  -> Return the `number` of such quadruples.
     *
     *
     *  ----------
     *
     *   IDEA 1) GREEDY ??? + hashmap ???
     *
     *
     *   IDEA 2) slide window ???
     *
     *
     *
     *   -> loop over 1st str (i ->)
     *      loop over 2nd str ( <- j)
     *
     *      and check if there is (i,j,a,b)
     *      that meet the conditions ?????
     *
     *  ----------
     *
     *
     */
    public int countQuadruples(String firstString, String secondString) {
        // edge

        /** NOTE !!!
         *
         * The key observation is that
         * the `optimal` quadruples always come
         * from single-character substrings,
         *
         * so for each character we only need:
         *
         * its earliest occurrence in firstString
         * its latest occurrence in secondString
         *
         * Then find the minimum `i - j` and
         * count how many characters achieve it
         *
         *
         *
         */

        // -> Map records below ????
        //         * its earliest occurrence in firstString
        //         * its latest occurrence in secondString
        Map<Character, Integer> map1 = new HashMap<>();
        Map<Character, Integer> map2 = new HashMap<>();

        // ??
        for(int i = 0 ; i < firstString.length(); i++){
            char ch = firstString.charAt(i);
            if(!map1.containsKey(ch)){
                map1.put(ch, i);
            }
        }

        for(int i = 0 ; i < secondString.length(); i++){
            char ch = secondString.charAt(i);
//            if(!map1.containsKey(ch)){
//                map1.put(ch, i);
//            }
            map2.put(ch, i);
        }


        //int res = 2 * 100000; // ???
        int res = 0;
        // ??
        for(int i = 0; i < firstString.length(); i++){
            // for(int j = secondString.length() - 1; j > 0; j--){
//            for(int j = secondString.length() - 1; j >= 0; j--){
//                // ???
//                if(firstString.charAt(i) == secondString.charAt(j)){
//                    //return
//                    //res = Math.min(res, i - j); // ???
//                    res += 1;
//                }
//            }

            // ???
            char ch = firstString.charAt(i); // ??
            if(map2.containsKey(ch)){
                res += 1;
            }

        }



        return res;
    }



    // LC 399
    // 3.46 - 56 pm
    /**
     *  -> Return the answers to all queries.
     *     If a single answer cannot be determined,
     *     return -1.0.
     *
     *
     *
     * --------------
     *
     *  IDEA 1) DFS ???
     *
     *   -> build map:
     *
     *    map: { ai : [bi, res ] }  // ????
     *
     *   -> call dfs. and run the queries
     *
     *  IDEA 2) BFS ???
     *
     *
     * --------------
     */
    // ???
    public class MyVal{
        String b;
        double value;

        MyVal(){

        }

        MyVal(String b, double value){
            this.b = b;
            this.value = value;
        }
    }


    // IDEA 1) DFS ???
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // edge


        // ???
        Map<String, List<MyVal>> map = new HashMap<>();
        for(int i = 0; i < equations.size(); i++){
            List<String> list = equations.get(i);
            // ???
            String a = list.get(0);
            String b = list.get(1);
            double val = values[i];

            // ???
            List<MyVal> list1 = map.getOrDefault(a, new ArrayList<>());
            List<MyVal> list2 = map.getOrDefault(b, new ArrayList<>());

            list1.add(new MyVal(b, val));
            list2.add(new MyVal(a, 1 / val));


            map.put(a, list1);
            map.put(b, list2);
        }


        // ??
        double[] res = new double[queries.size()];
        for(int i = 0; i < queries.size(); i++){
            // ???
            List<String> q = queries.get(i);
            String a = q.get(0);
            String b = q.get(1);
            if(a.equals(b)){
                res[i] =  1.0;
            }
            else if(!map.containsKey(a) || !map.containsKey(b)){
                res[i] =  -1.0;
            }else{
                // NOTE !!! need to use `visited`
                // to `AVOID` duplicated visiting
                Set<String> visited = new HashSet<>();
                // res[i] = dfsHelper(queries.get(i), map);
            }
        }

        return res;
    }


    private double dfsHelper(String a, String b, Map<String, List<MyVal>> map, double res, Set<String> visited){
        // edge
//        String a = q.get(0);
//        String b = q.get(1);


        visited.add(a);


        // ???
//        if(a.equals(b)){
//            return 1.0;
//        }
//        if(!map.containsKey(a) || !map.containsKey(b)){
//            return -1.0; // ???
//        }

        // ???
        //res = map.

        // ???
        for( MyVal next: map.get(a)){
          //  dfsHelper(q, map, res);
            if(!visited.contains(next.b)){
                double resp = dfsHelper(
                        a,
                        next.b,
                        map,
                        res * 1, // ???
                        visited);

                // ???
                if(resp != -1){
                    return resp;
                }
            }

        }



        return -1.0; // /??
    }


    // LC 334
    // 4.32 - 42 pm
    /**
     * -> return true if there exists a triple of
     * indices (i, j, k) such that i < j < k
     * and nums[i] < nums[j] < nums[k].
     * If no such indices exists, return false.
     *
     *
     * ----------
     *
     *  IDEA 1) BRUTE FORCE ???
     *
     *  IDEA 2) 2 POINTERS ???
     *
     *
     *  IDEA 3) GREEDY ???
     *
     *  IDEA 4) 3 VARS ???
     *
     *
     * ----------
     *
     *
     */
    // IDEA 4) 3 VARS ???
    public boolean increasingTriplet(int[] nums) {

//        // ??? min so far
//        int a = -1 * Integer.MAX_VALUE; // ???
        int a = Integer.MAX_VALUE;

//
//        // 2nd min so far
//        int b = -1 * Integer.MAX_VALUE; // ???
        int b = Integer.MAX_VALUE;
//
//        // 3rd min so far ????
//        int c = -1 * Integer.MAX_VALUE; // ???

        // ??
        for(int i = 0; i < nums.length; i++){
            int val = nums[i];
            // ??
            //  if(a == -1 * Integer.MAX_VALUE){
            if(a >= val){
               // val = a;
                a = val;
            }
            // ???
            else if(b >= val){
                b = val;
            }
            else{
                return true;
            }



            // ???
//            else {
//                if(val < a){
//                    a = val;
//                }else{
//                    // ???
//                    if(b != -1 * Integer.MAX_VALUE){
//                        if(val > b){
//                            return true;
//                        }
//                    }
//                    b = val; // ???
//                }
//            }



        }


        return false;
    }






}
