package LCWeekly;

import java.util.*;

/**
 * 本周題目
 * LeetCode biweekly contest 108
 * https://leetcode.com/contest/biweekly-contest-108/
 * 中文題目
 * https://leetcode.cn/contest/biweekly-contest-108/
 */
public class Weekly108 {

    // Q1
    // LC 2765
    // https://leetcode.com/problems/longest-alternating-subarray/
    // 12.31 - 41 PM
    /**
     *
     * -> Return the `maximum length` of `all alternating`
     *    subarrays present in nums
     *     or -1 if no such subarray exists.
     *
     *
     *
     *   - 0 idx array : nums
     *   - a subarray `s` is `alternating` if
     *     -  m > 1
     *     -  s1 = s0 + 1
     *     - the subarray is a below:
     *        - s1 - s0 = 1
     *        - s2 - s1 =  -1
     *        - s3 - s2 = 1
     *        - s4 - s3 = -1
     *        ...
     *        - s[m-1] - s[m-2] = (-1)^m
     *
     *
     *   - A subarray is a contiguous non-empty sequence of
     *     elements within an array.
     *
     *
     * -------------------
     *
     *   IDEA 1) GREEDY V1
     *
     *     - s1 - s0 = 1
     *      *        - s2 - s1 =  -1
     *      *        - s3 - s2 = 1
     *      *        - s4 - s3 = -1
     *      *        ...
     *
     *
     *    so if len =3
     *    -> (s1-s0) + (s2-s1) + (s3-s2) = 1
     *    -> s3 - s0 = 1
     *
     *   IDEA 1) GREEDY V2 ??
     *
     *    a,b,c,d,e
     *
     *    if valid
     *
     *    -> b - a = 1
     *       c - b = 1
     *
     *    - so we just need to check
     *
     *   IDEA 2) BRUTE FORCE ???
     *
     *   IDEA 3) SLIDE WINDOW ???
     *
     *
     * -------------------
     *
     */
    // SLIDE WINDOW
    public int alternatingSubarray(int[] nums) {
        // edge

        int maxLen = 0;

        /**
         *  slide window template:
         *
         *   for(int r = 0; r < nums.len; r++){
         *      while(condition){
         *          //
         *          l += 1;
         *      }
         *      //
         *   }
         */
        int l = 0; // ??
        for(int r = 0; r < nums.length; r++){
            while(!isValid(nums, l, r)){
                l += 1;
            }
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    private boolean isValid(int[] nums, int l, int r){
        // ??? edge
        if(l >= r){
            return false;
        }
        if(nums == null || nums.length == 0){
            return false; // ???
        }
        // ???
        int cnt = 0;
        for(int i = l+1; i < r; i++){
            if(cnt % 2 == 0){
                if(nums[l] - nums[l-1] != 1){
                    return false;
                }
            }else{
                if(nums[l] - nums[l-1] != -1){
                    return false;
                }
            }
            cnt += 1;
        }
        return true;
    }


    // Q2
    // LC 2766
    // https://leetcode.com/problems/relocate-marbles/description/
    // 12.56 - 1.06 pm
    /**
     *
     * -> After completing all the steps,
     *    return the sorted list of occupied positions.
     *
     *
     *  - nums: 0 idx array
     *          - init positions of marbles (彈珠)
     *
     *  -  two 0-indexed integer (same len)
     *        - moveFrom
     *        - moveTo
     *
     *  - op:
     *      - change the position of marbles
     *      - on i th step
     *         - move ALL marbles at moveFrom[i] to moveTo[i]
     *
     *      - ....
     *
     *   - NOTE:
     *     - occupied: AT LEAST one marble in that position
     *     - could be MULTIPLE marbles at the same position
     *
     * ------------------------
     *
     *  IDEA 1) BRUTE FORCE ???
     *
     *  IDEA 2) DIFF ARRAY ???
     *
     *  IDEA 3) HAHSMAP ??
     *    map : { position: [idx_1, idx_2 ,...] }
     *
     *
     * ------------------------
     *
     *  ex 1)
     *
     *  Input: nums = [1,6,7,8], moveFrom = [1,7,2], moveTo = [2,9,5]
     *
     *  -> map: { 1: [0], 6: [1], 7: [2], 8: [3] }
     *
     *  -> idx = 0, move : 1 -> 2
     *     map: { 2: [0], 6: [1], 7: [2], 8: [3] }
     *
     *     idx = 1, move: 7 -> 9
     *     map: { 2: [0], 6: [1], 9: [2], 8: [3] }
     *
     *     idx =2, move: 2 -> 5
     *     map: { 5: [0], 6: [1], 9: [2], 8: [3] }
     *
     *     -> res = [5, 6, 9, 8]  ???
     *
     *
     *  ex 2)
     *   nums = [1,1,3,3], moveFrom = [1,3], moveTo = [2,2]
     *
     *   ->
     *    [1,1,3,3]  , idx = 0, move : 1->2
     *    map : { position: [idx_1, idx_2 ,...] }
     *    map: {1: [0,1], 3: [2, 3] }
     *
     *    -> {2: [0,1], 3: [2, 3]}
     *
     *    move: 3 -> 2
     *
     *    map: {2: [0,1], 3: [2, 3]}
     *
     *    map: {2: [0,1,2,3] }
     *
     *    -> [2,2,2,2]
     *
     *
     */
    //      *  IDEA 3) HAHSMAP ??
    //     *    map : { position: [idx_1, idx_2 ,...] }
    public List<Integer> relocateMarbles(int[] nums, int[] moveFrom, int[] moveTo) {
        // edge

        Map<Integer, List<Integer>> map = new HashMap<>();
        // map : { position: [idx_1, idx_2 ,...] }
        for(int i = 0; i < nums.length; i++){
            int position = nums[i];
            if(!map.containsKey(position)){
                map.put(position, new ArrayList<>());
            }
            map.get(position).add(i);
        }

        System.out.println(">>> (before OP) map = " + map);

        // do op
        for(int i = 0; i < moveFrom.length; i++){
            int start = moveFrom[i];
            int end = moveTo[i];
            if(map.containsKey(start)){
                List<Integer> integerList = map.get(start);
                map.remove(start);
                map.put(end, integerList); // ???
            }
        }

        //Integer[]
        List<Integer> res = new ArrayList<>(nums.length); // /??
 
        System.out.println(">>> (after OP) map = " + map);
        // ???
        for(int key: map.keySet()){
            for(int idx: map.get(key)){
                res.set(idx, key);
            }
        }

        return res;
    }

    // Q3
    // LC 2767
    // https://leetcode.com/problems/partition-string-into-minimum-beautiful-substrings/description/

    // Q4
    // LC 2768
    // https://leetcode.com/problems/number-of-black-blocks/description/


}
