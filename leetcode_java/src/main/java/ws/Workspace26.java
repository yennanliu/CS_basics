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
    //  IDEA 1) 2 POINTERS
    // time: O (N * N) // ???
    // space:  O(1) // ??
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
    public int countQuadruples(String firstString, String secondString) {

        return 0;
    }




}
