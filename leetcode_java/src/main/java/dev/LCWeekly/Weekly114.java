package dev.LCWeekly;

import java.util.*;

/**
 * Leetcode weekly contest 114
 * https://leetcode.com/contest/biweekly-contest-114/
 * 力扣 第 114 场双周赛(中文版)
 * https://leetcode.cn/contest/biweekly-contest-114/
 */
public class Weekly114 {

    // Q1
    // LC 2869: OK
    // https://leetcode.com/problems/minimum-operations-to-collect-elements/description/
    // 23.06 - 23.16 pm
    /**
     *   -> Return the `min` number of
     *   op needed to collect elements 1, 2, ..., k.
     *
     *
     *   - array nums of positive integers and an integer k.
     *   - op: remove the last element of the array and add it to your collection.
     *
     *
     *   IDEA 1) BRUTE FORCE
     *
     *
     */
    public int minOperations(List<Integer> nums, int k) {
        // edge
        if (nums == null || nums.isEmpty()) {
            return 0;
        }
        // ??
        if (k <= 0) {
            return 0;
        }
        if (k == nums.size()) {
            return k;
        }

        List<Integer> list = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        // ???
        for (int i = nums.size() - 1; i >= 0; i--) {
            if (set.size() == k) {
                return list.size();
            }
            int val = nums.get(i);
            System.out.println(">>> i = " + i + ", val = " + val);
            if (val <= k) {
                set.add(val);
            }
            list.add(val);
        }

        return list.size();
    }




    // Q2
    // LC 2870
    // 23.19 - 23.31 pm
    // Note: This question is the same as 2244: Minimum Rounds to Complete All Tasks.
    // https://leetcode.com/problems/minimum-number-of-operations-to-make-array-empty/
    /**
     *
     *  -> Return the `min` number of op required to
     *    make the array empty, or -1 if it is NOT possible.
     *
     *    - 0-indexed array nums with positive integers.
     *    - two op can apply on array ANY number of times
     *       - Choose 2 elements with `equal values` and `delete` them
     *       - Choose 3 elements with `equal values` and `delete` them
     *
     *
     *  IDEA 1) HASH MAP
     *
     *   -> map : { val : cnt }
     *   -> loop over val in map, to see if there op1 or op2
     *      can `eliminate the val with cnt` in map
     *   -> if yes, keep updating the total op cnt
     *
     *  IDEA 2) BRUTE FORCE
     *
     *
     *  ----------
     *
     *
     *  exp 1) nums = [2,3,3,2,2,4,2,3,4], ans = 4
     *
     *  -> map: {2: 4, 3: 3, 4: 2}
     *
     *
     *  exp 2) nums = [2,3,3,2,2,4,2,3,4,2,2,2], ans = 5
     *
     *    -> map: {2: 7, 3: 3, 4: 2}
     *
     */
//    public int minOperations(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return -1;
//        }
//        // map: { val: cnt}
//        Map<Integer, Integer> map = new HashMap<>();
//        for(int x: nums){
//            map.put(x, map.getOrDefault(x, 0) + 1);
//        }
//        System.out.println(">>> (before 1st update) map = " + map);
//        int cnt = 0;
//
//
//        for(int k: map.keySet()){
//            int valCnt = map.get(k);
//            // edge
//            if(valCnt == 1){
//                return -1;
//            }
//            if(valCnt / 3 == 0){
//                cnt +=  (valCnt / 3);
//                // remove val from map
//                map.remove(k);
//            }
//            else if(valCnt / 2 == 0){
//                cnt +=  (valCnt / 2);
//                // remove val from map
//                map.remove(k);
//            }
//            else{
//                map.put(k, (valCnt % 3)); // ???
//            }
//        }
//        System.out.println(">>> (after 1st update) map = " + map);
//
//        return cnt; // ???
//    }

    // FIX BY gpt
    public int minOperations(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int x : nums) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        int ops = 0;

        for (int count : map.values()) {
            if (count == 1)
                return -1; // cannot remove a single element

            // Greedy rule: use as many groups of 3 as possible
            // but if remainder == 1, adjust by converting one 3-group into 2+2
            if (count % 3 == 0) {
                ops += count / 3;
            } else {
                ops += count / 3 + 1;
            }
        }

        return ops;
    }



    // Q3
    // LC 2871
    // https://leetcode.com/problems/split-array-into-maximum-number-of-subarrays/

    // Q4
    // LC 2872
    // https://leetcode.com/problems/maximum-number-of-k-divisible-components/description/

}
