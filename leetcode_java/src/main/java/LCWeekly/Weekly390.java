package LCWeekly;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * LeetCode weekly contest 390
 * https://leetcode.com/contest/weekly-contest-390/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-390/
 */
public class Weekly390 {

    // Q1
    // LC 3090
    // https://leetcode.com/problems/maximum-length-substring-with-two-occurrences/description/
    // 13.54 - 16.04
    /**
     *
     *  -> return the maximum length of a substring
     *    - it contains AT MOST 2 occurrences of each character.
     *
     *   - string s
     *
     *   - A substring
     *      - is a contiguous sequence of characters within a string.
     *
     *    ---------------
     *
     *    IDEA 1) BRUTE FORCE
     *
     *    IDEA 2) SLIDE WINDOW
     *
     *
     *    ---------------
     *
     */
    public int maximumLengthSubstring(String s) {
        // edge
        if(s.isEmpty()){
            return 0;
        }
        if(s.length() == 1){
            return 1;
        }

        int maxLen = 1;
        int l = 0;
        // ??
        // { val : cnt }
        Map<String, Integer> map = new HashMap<>();
        for(int r = 0; r < s.length(); r++){

            String rightVal = String.valueOf(s.charAt(r));
            map.put(rightVal, map.get(rightVal) + 1);

            // ??? l < r
            while (!isValid(map) && l < r){
                String leftVal = String.valueOf(s.charAt(l));
                map.put(leftVal, map.get(leftVal) - 1);
                if(map.get(leftVal) == 0){
                    map.remove(leftVal);
                }
                l += 1;
            }
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    private boolean isValid(Map<String, Integer> map){
        if(map.isEmpty()){
            return true;
        }
        for(int val: map.values()){
            if(val > 2){
                return false;
            }
        }

        return true;
    }


    // Q2
    // LC 3091
    // https://leetcode.com/problems/apply-operations-to-make-sum-of-array-greater-than-or-equal-to-k/
    // 14.09 - 19 pm
    /**
     *  -> Return the minimum number of operations required to make the sum
     *    of elements of the final array  (>= ) greater than or equal to k
     *
     *    -  positive integer k
     *    -  Initially, nums = [1]
     *
     *    - can perform any of the OP on the array any
     *      number of times (possibly zero)
     *
     *      - Choose any element in the array and
     *         increase its value by 1.
     *
     *      - Duplicate any element in the array and
     *        add it to the end of the array.
     *
     *  ------------------
     *
     *   IDEA 1) GREEDY
     *
     *     - `plus val` first
     *     - then `duplicate the updated val
     *
     *
     *   IDEA 2) BRUTE FORCE
     *
     *   IDEA 3) DP ???
     *
     *   IDEA 4) BACKTRACK ???
     *
     *  ------------------
     *
     *  ex 1)
     *
     *  Input: k = 11
     *  Output: 5
     *
     *   1 1 1 1 ....  , cnt = 11
     *   1,   2,2,2,2,2,2   cnt = 7
     *   1,2,3  3,3,3,3    cnt = 5
     *   1,2,3,4  4,4,4    cnt = 5
     *   1,2,3,4,5   5,5,5  cnt = 6
     *   1,2,3,4,5,6  6,6  cnt = 6
     *   1,2,3,4,5,6,7  7,7 cnt = 7
     *
     */
    //  IDEA 2) BRUTE FORCE ???
    public int minOperations(int k) {
        // edge
//        if(k == 1){
//            return 0;
//        }
//        if(k == 2){
//            return 1;
//        }
//        if(k == 3){
//            return 2;
//        }
        if(k <= 3){
            return k - 1; // ???
        }

        int minOp = Integer.MAX_VALUE; // ???

        // addition OP: 1 -> 2 -> 3....
        // Choose any element in the array and increase its value by 1.
        int op1 = 0;

        // duplicate OP. [1] -> [1,1] -> [1,1,1] -> ...
        // Duplicate any element in the array and add it to the end of the array.
        int op2 = 0;

        int curVal = 1;

        // ??? double loop
        for(int x = 1; x < k; x++){
            op1 += x;  // ???
            // ??
            //x += op1;
            curVal += 1;
            op2 = (k / curVal);
            if(k % curVal != 0){
                op2 += 1;
            }
            minOp = Math.min(minOp, op1 + op2);
            // ??? early quit ???
        }


        return minOp;
    }




    // Q3
    // LC 3092
    // https://leetcode.com/problems/most-frequent-ids/description/
    // https://leetcode.cn/problems/most-frequent-ids/
    // 14.37 - 47 pm
    /**
     *  -> Return an array ans of length n, where ans[i]
     *     represents the `count` of the `most frequent ID` in the
     *     collection after the ith step.
     *
     *
     *    NOTE: If the collection is empty at any step, ans[i] should be 0 for that step.
     *
     *
     *  -----------
     *
     *
     *  -----------
     *
     *   ex 1)
     *
     *   Input: nums = [2,3,2,1], freq = [3,2,-3,1]
     *   Output: [3,3,2,2]
     *
     *   ->
     *    idx = 0,  [2,2,2]
     *    idx = 1, [2,2,2,3,3]
     *    idx = 2, [3,3]
     *    idx = 3, [3,3,1]
     *
     *
     */
    public long[] mostFrequentIDs(int[] nums, int[] freq) {
        // edge

        long[] res = new long[nums.length];

        // { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        //TreeMap<Integer, Integer> map = new TreeMap<>();
        //map.

        for(int i = 0; i < nums.length; i++){
            if(freq[i] > 0){
                map.put(nums[i], freq[i]);
            }else{
                if(map.containsKey(nums[i])){
                    // ??
                    map.put(nums[i],  map.get(nums[i]) - freq[i]);
                }
            }
            // TODO: optimize below, via PQ ??? or maintain a global max cnt
            // get max cnt of cur values
            res[i] = getMaxCnt(map);
        }

        return res;
    }

    private int getMaxCnt(Map<Integer, Integer> map){
        // edge
        if(map.isEmpty()){
            return -1; // ???
        }
        int res = 0;
        for(int x: map.values()){
            res = Math.max(x, res);
        }
        return res;
    }



    // Q4
    // LC 3093
    // https://leetcode.com/problems/longest-common-suffix-queries/description/
    // 15.10 - 20 pm
    /**
     *
     * Return an array of integers ans,
     * where ans[i] is the index of the string
     * in wordsContainer that has the longest common
     * suffix with wordsQuery[i].
     *
     *
     * ----------
     *
     *  IDEA 1) BRUTE FORCE ???
     *
     *  IDEA 2) TRIE ???
     *
     *
     *
     *
     * ----------
     *
     *
     *
     */
    // IDEA 2) TRIE ???
    class MyTrieNode99{
        // attr

        // constructor

        // method
        public void add(String word){

        }

        public boolean isStartWith(String prefix){
            return false;
        }



    }
    class MyTrie99{

    }
    public int[] stringIndices(String[] wordsContainer, String[] wordsQuery) {
        // edge

        // NOTE !!! we reverse BOTH wordsContainer, wordsQuery
        String[] wordsContainer2 = new String[wordsContainer.length];
        String[] wordsQuery2 = new String[wordsQuery.length];

        // ??
        for(int i = 0; i < wordsContainer.length; i++){
            StringBuilder sb1 = new StringBuilder(wordsContainer[i]);
            StringBuilder sb2 = new StringBuilder(wordsQuery2[i]);

            wordsContainer2[i] = sb1.reverse().toString();
            wordsQuery2[i] = sb2.reverse().toString();
        }

        MyTrieNode99 node = new MyTrieNode99(); // ??
        // build
        for(String s: wordsContainer2){
            // ????
            node.add(s);
        }

        int[] res = new int[wordsQuery.length];
        for(int i = 0; i < wordsQuery2.length; i++){
            String query = wordsQuery2[i];
            // ???
//            if(node.isStartWith(query)){
//
//            }
        }
        
        return res;
    }



}
