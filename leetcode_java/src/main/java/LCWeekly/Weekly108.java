package LCWeekly;

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

    // Q3
    // LC 2767
    // https://leetcode.com/problems/partition-string-into-minimum-beautiful-substrings/description/

    // Q4
    // LC 2768
    // https://leetcode.com/problems/number-of-black-blocks/description/


}
