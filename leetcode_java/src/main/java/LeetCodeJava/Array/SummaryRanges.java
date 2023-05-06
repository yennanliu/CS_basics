package LeetCodeJava.Array;

// https://leetcode.com/problems/summary-ranges/

import java.util.ArrayList;
import java.util.List;

public class SummaryRanges {

    public List<String> summaryRanges(int[] nums) {

        List<String> res = new ArrayList<>();
        if (nums.equals(null) || nums.length == 0){
            return res;
        }

        String cur = "";
        int last = Integer.parseInt("0"); // DOUBLE CHECK
        for (int i = 0; i < nums.length; i++){
            Integer curDigit = nums[i];
            if (cur.equals("")){
                cur += curDigit.toString();
                last = curDigit;

            } else if (curDigit == last + 1) {
                last = curDigit;
            }
        }

        return null;
    }

}
