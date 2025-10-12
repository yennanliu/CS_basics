package dev.LCWeekly;

import java.util.*;

public class Weekly97 {

    // https://leetcode.com/contest/biweekly-contest-97/

    // Q1: OK
    // LC 2553
    // 15.24 - 15.34 PM
    // https://leetcode.com/problems/separate-the-digits-in-an-array/description/
    /**
     *  ->  return an array answer that consists of the digits of each
     *     integer in nums AFTER separating them
     *     in the same order they appear in nums.
     *
     *     - To separate the digits of an integer is to get all the digits it has in the same order.
     *       e.g. 10921 -> [1,0,9,2,1]
     *
     *
     *   IDEA 1) BRUTE FORCE OR ARRAY APPEND
     *
     *
     */
    public int[] separateDigits(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[] {}; // ??
        }

        //List<String> cache = new ArrayList<>();
        List<String> cache = splitHelper(nums);
        int[] res = new int[cache.size()];
        for (int i = 0; i < cache.size(); i++) {
            res[i] = Integer.parseInt(cache.get(i));
        }

        System.out.println(">>> cache = " + cache + " , res = " + res);

        return res;
    }

    private List<String> splitHelper(int[] nums) {
        List<String> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }

        // ???
        for (int x : nums) {
            String str = String.valueOf(x);
            for (String y : str.split("")) {
                res.add(y);
            }
        }

        return res;
    }


    // Q2: to fix
    // LC 2554
    // 15.31 - 15.41 pm
    // https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-i/description/
    /**
     *
     *  -> Return the `MAX number of integers`
     *    you can choose following the mentioned rules.
     *
     *    - given:
     *        - array (banned)
     *        - n
     *        - maxSum
     *    - rules:
     *      - chose arr in [1,n]
     *      - EACH int CAN be chosen AT MOST ONCE (or can NOT be used)
     *      - the chosen int NOT in the arr (banned)
     *      - sum of the chosen int <= maxSum
     *
     *
     *    IDEA 1) BRUTE FORCE
     *
     *    IDEA 2) binary search
     *
     *    IDEA 3) 2 POINTERS ****** + prefix sum arr
     *
     *
     *   ex 1) banned = [1,6,5], n = 5, maxSum = 6
     *
     *    -> candidates: [2,3,4]
     *    -> could be  2+3, 2+4
     *
     *    -> prefix sum = [2,5,9]
     *
     *
     *     [2,3,4]
     *      l   r     l+r = 6 <= maxsum, ans = 2
     *
     *     [2,3,4]
     *      l r    l+r = 5 <= maxsum, ans = 2
     *
     *
     *   ex 2)
     *    Input: banned = [11], n = 7, maxSum = 50
     *
     *    -> candidates: [1,2,3,4,5,6.7]
     *
     *
     *    [1,2,3,4,5,6.7]
     *
     *    -> prefix sum = [1,3,6,10,15,21,28]
     *
     *
     *   ex 3)
     *
     *   Input: banned = [11], n = 7, maxSum = 21
     *
     *    -> prefix sum = [1,3,6,10,15,21,28]
     *
     *
     */
    // IDEA 1) GREEDY
    public int maxCount(int[] banned, int n, int maxSum) {
        // edge
        if(n <= 1){
            return n;
        }
        if(maxSum == 1){
            return 0;
        }

        Set<Integer> bannedSet = new HashSet<>();
        for(int x : banned){
            bannedSet.add(x);
        }

        int cnt = 0;
        int cumSum = 0;

        for(int i = 1; i <= n ; i++){
            if(bannedSet.contains(i)){
                continue;
            }
            if(cumSum > maxSum){
                break;
            }

            //ans = Math.max(ans, i + 1); // ??
            cnt += 1;
            cumSum += i;
        }


        return cnt; // ???
    }




//    public int maxCount(int[] banned, int n, int maxSum) {
//        // edge
//        if(n <= 1){
//            return n;
//        }
//        if(maxSum == 1){
//            return 0;
//        }
//        int ans = 0;
//
//        // ???
//        List<Integer> bannedList = new ArrayList<>();
//        for(int x: banned){
//            bannedList.add(x);
//        }
//
//        List<Integer> candidates = new ArrayList<>();
//        for(int i = 0; i < n; i++){
//            if(!bannedList.contains(i)){
//                candidates.add(i);
//            }
//        }
//
//        // prefix sum array
//        List<Integer> prefixSum = new ArrayList<>();
//        int preSum = candidates.get(0);
//        for(int i = 1; i < candidates.size(); i++){
//            preSum += candidates.get(i);
//            prefixSum.add(preSum);
//        }
//
//        // ???
//        prefixSum.add(preSum);
//
//
//        System.out.println(">>> prefixSum = " + prefixSum);
//
//        // ???
//        for(int i = 0; i < prefixSum.size(); i++){
//            if(maxSum < prefixSum.get(i)){
//                break;
//            }
//            ans = Math.max(ans, i+1);
//        }
//
//        return ans;
//    }



    // Q3: to fix
    // LC 2555
    // 16.00 - 16.10 pm
    // https://leetcode.com/problems/maximize-win-from-two-segments/description/
    /**
     *
     *  -> Return the `MAX number of prizes`
     *    you can win if you choose the `two segments` optimally.
     *
     *  - The `length` of `EACH segment` must be k
     *
     *  - prizePositions is SORT in INCREASING order
     *  - prizePositions[i] is the idx of i th prize
     *  - There could be different prizes at the same position on the line.
     *  - You are allowed to select `2 segments` with integer endpoints.
     *
     *
     *
     *   IDEA 1) HASHMAP + BRUTE FORCE
     *
     *
     *    ex 1)
     *
     *      Input: prizePositions = [1,1,2,2,3,3,5], k = 2
     *      Output: 7
     *
     *      -> map : {1: 2, 2: 2, 3: 2, 5 : 1}
     *
     *
     *
     */
    public int maximizeWin(int[] prizePositions, int k) {
        // edge
        if(prizePositions == null || prizePositions.length == 0){
            return 0;
        }
        if(k > prizePositions.length){
            return -1; // ???
        }
        // ???
        if(k == 0){
            return 2;
        }

        int ans = 0;
        // map : { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        for(int x: prizePositions){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        System.out.println(">>> map = " + map);

        // ??? brute force



        return ans;
    }




    // Q4
    // LC 2556
    // https://leetcode.com/problems/disconnect-path-in-a-binary-matrix-by-at-most-one-flip/description/



}
