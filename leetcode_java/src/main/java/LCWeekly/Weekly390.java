package LCWeekly;

import java.util.HashMap;
import java.util.Map;

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


    // Q3
    // LC 3092
    // https://leetcode.com/problems/most-frequent-ids/description/

    // Q4
    // LC 3093
    // https://leetcode.com/problems/longest-common-suffix-queries/description/



}
