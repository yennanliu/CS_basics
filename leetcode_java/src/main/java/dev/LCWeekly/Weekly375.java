package dev.LCWeekly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LeetCode weekly contest 375
 * https://leetcode.com/contest/weekly-contest-375/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-375/
 */
public class Weekly375 {

    // Q1
    // LC 2960
    // https://leetcode.com/problems/count-tested-devices-after-test-operations/
    // 15.08 - 18 pm
    /**
     *
     *  -> Return an integer denoting (表示) the number of devices
     *     that `will be tested` after
     *     performing the `test operations` `in order`.
     *
     *           - 0 idx based arr: batteryPercentages
     *               - battery pct of device n (0 to n-1)
     *    - OP:
     *      - if batteryPercentages[i] > 0, do below:
     *           - 1. increase cnt of tested devices
     *           - 2. decrease battery pct of all devices
     *                in [i+1, n-1] range by 1,
     *                and ensure that battery pct  >= 0
     *                e.g. batteryPercentages[j] = max(0, batteryPercentages[j] - 1)
     *
     *
     *  -------------
     *
     *      IDEA 1) BRUTE FORCE
     *
     *      IDEA 2) DIFF (PREFIX SUM) ARRAY ???
     *
     *
     *  -------------
     *
     *
     *  ex 1)
     *  Input: batteryPercentages = [1,1,2,1,3]
     *  Output: 3
     *
     *  ->
     *  // batteryPercentages[j] = Math.max(0, batteryPercentages[j-1]);
     *
     *  [1,1,2,1,3] b[i] > 0, do op,  [1,0,1,0,2]
     *   x
     *
     *  [1,0,1,0,2] b[i] == 0, NOT do op
     *     x
     *
     * [1,0,1,0,2] b[i] > 0, do op, [1,0,1,0,1]
     *      x
     *
     * [1,0,1,0,2] b[i] == 0, NOT do op
     *        x
     *
     * [1,0,1,0,2] b[i] > 0, but `no remaining element to do op`
     *          x
     *
     *
     *  ex 2)
     *
     *  Input: batteryPercentages = [0,1,2]
     *  Output: 2
     *
     *
     *  ->
     *
     *  [0,1,2]
     *   x
     *
     *  [0,1,2]   ans=1, [0,1,1]
     *     x
     *
     *  [0,1,1] ans=2
     *       x
     *
     */
    public int countTestedDevices(int[] batteryPercentages) {
        int ans = 0;
        // edge
        if(batteryPercentages == null || batteryPercentages.length == 0){
            return 0;
        }
        // ???
        if(batteryPercentages.length == 1){
            if(batteryPercentages[0] > 0){
                ans += 1;
            }
            return ans;
        }

        // brute force
        for(int i = 0; i < batteryPercentages.length; i++){
            // op trigger condition
            if(batteryPercentages[i] > 0){
                // op 1
                ans += 1;
                // op 2
                for(int j = i+1; j < batteryPercentages.length; j++){
                    batteryPercentages[j] = Math.max(0, batteryPercentages[j-1]);
                }

            }
        }

        return ans;
    }


    // Q2
    // LC 2961
    // https://leetcode.com/problems/double-modular-exponentiation/description/
    // 15.26 - 40 pm
    /**
     *  -> Return an array consisting
     *     of `good indices` in ANY order.
     *
     *
     *   - 0 idx 2D array
     *      - variables[i] = [ai, bi, ci, mi]
     *   - target
     *
     *   - An idx `i` is good if below is TRUE
     *
     *      - 0  <= I < variables len
     *      -  ( (ai^bi % 10)^ci ) % mi = target
     *
     *  ----------
     *
     *   IDEA 1) brute force ???
     *
     *   IDEA 2) math ???
     *
     *
     * ----------
     *
     */
    public List<Integer> getGoodIndices(int[][] variables, int target) {
        List<Integer> ans = new ArrayList<>();
        // edge
        if(variables == null || variables.length == 0){
            return ans;
        }
//        if(variables.length == 1){
//            int ai = variables[0][0];
//            int bi = variables[0][1];
//            int ci = variables[0][2];
//            int mi = variables[0][3];
//            if(compute(ai, bi, ci, mi) == target){
//                ans.add(0);
//            }
//            return ans;
//        }

        for(int i = 0; i < variables.length; i++){
            int ai = variables[i][0];
            int bi = variables[i][1];
            int ci = variables[i][2];
            int mi = variables[i][3];
            if(compute(ai, bi, ci, mi) == target){
                ans.add(i);
            }
        }

        return ans;
    }

    //  ( (ai^bi % 10)^ci ) % mi = target
    private int compute(int ai, int bi, int ci, int mi){
        // edge
        if(mi == 0){
            return -1; // ???
        }

        //  ( (ai^bi % 10)^ci ) % mi = target

        // double resultDouble = Math.pow(base, exponent);
        // ???
        int x1 = (int) Math.pow(ai, bi);
        int x2 = x1 % 10;
        int x3 =  (int) Math.pow(x2, ci);

        return x3 % mi;
    }


    // Q3
    // LC 2962
    // https://leetcode.com/problems/count-subarrays-where-max-element-appears-at-least-k-times/description/
    // 15.49 - 59 PM
    /**
     * -> Return the number of subarrays where
     *    the MAXIMUM element of nums appears
     *     `AT LEAST k times` in that subarray.
     *
     *
     *     - nums: int array
     *     - k: positive int
     *
     *     - NOTE:
     *       - A subarray is a contiguous
     *         sequence of elements within an array.
     *
     * ----------------
     *
     *  IDEA 1) HASHMAP + SLIDE WINDOW
     *
     *    slide window template:
     *
     *    i : right pointer
     *    j: left pointer
     *
     *    for(int i = 0; i < nums.len; i++){
     *        while(condition){
     *            // do sth
     *        }
     *        j += 1;
     *    }
     *
     *    -> get the element cnt via hashmap
     *    -> if cnt > k, collect the cur sub array
     *
     *    ... (repeat above)
     *
     *
     *  IDEA 2) HASHMAP + BRUTE FORCE
     *
     * ----------------
     *
     *  ex 1)
     *
     *  Input: nums = [1,3,2,3,3], k = 2
     *  Output: 6
     *
     *  ->
     *
     *   [1,3,2,3,3], k = 2
     *    r
     *    l
     *
     *   [1,3,2,3,3], k = 2
     *    l   r
     *
     *   [1,3,2,3,3], k = 2 , cache = [ [1,3,2,3] ] , ans = 1
     *    l     r
     *
     *   [1,3,2,3,3], k = 2 , cache = [ [1,3,2,3], [1,3,2,3,3] ] , ans = 2
     *    l       r
     *
     *  [1,3,2,3,3], k = 2 , cache = [ [1,3,2,3], [1,3,2,3,3], [3,2,3,3] ] , ans = 3
     *     l     r
     *
     *  [1,3,2,3,3], k = 2 , cache = [ [1,3,2,3], [1,3,2,3,3], [3,2,3,3], [2,3,3] ] , ans = 4
     *       l   r
     *
     *  [1,3,2,3,3], k = 2 , cache = [ [1,3,2,3], [1,3,2,3,3], [3,2,3,3], [2,3,3], [3,3] ] , ans = 5
     *         l  r
     *
     *
     *    // TODO: check how to get `[3,2,3]` case ???
     *
     *     -> when the sub array meet the condition (e.g. cnt >= k)
     *        -> move left pointer to right, till it NOT meet the condition
     *        -> then move right pointer again
     *        -> repeat above ...
     *
     */
    public long countSubarrays(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return 0L; // ???
        }
        if(nums.length == 1){
//            if(k == 1){
//                return 1;
//            }
//            return 0;
            return k;
        }

        int cnt = 0;
        int l = 0;

        // slide window
        /**
         *      *     -> when the sub array meet the condition (e.g. cnt >= k)
         *      *        -> move left pointer to right, till it NOT meet the condition
         *      *        -> then move right pointer again
         *      *        -> repeat above ...
         *      *
         */
        for(int r = 0; r < nums.length; r++){
            // cnt nums[r] (e.g. add nums[r] to map)
            Map<Integer, Integer> cntMap = new HashMap<>();
            int curVal = nums[r];
            cntMap.put(curVal, cntMap.getOrDefault(curVal, 0) + 1);
            // move left pointer while the sub array is still VALID
            while(isValid(cntMap, k)){
                int toReduce = nums[l];
                cntMap.put(toReduce, cntMap.get(toReduce) - 1);
                l += 1;
                cnt += 1;
            }
           // cnt += 1;
        }

        return cnt;
    }

    private boolean isValid(Map<Integer, Integer> cntMap, int k){
        // edge
        if(cntMap.isEmpty() || k == 0){
            return false; // ???
        }
        for(int valCnt: cntMap.values()){
            if(valCnt >= k){
                return true;
            }
        }

        return false;
    }


    // Q4
    // LC 2963
    // https://leetcode.com/problems/count-the-number-of-good-partitions/description/



}
