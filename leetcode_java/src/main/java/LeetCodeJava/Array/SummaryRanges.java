package LeetCodeJava.Array;

// https://leetcode.com/problems/summary-ranges/

import java.util.ArrayList;
import java.util.List;

public class SummaryRanges {

    // V0

    // V1
    // https://leetcode.com/problems/summary-ranges/solutions/1805391/concise-solution-in-0-n-with-approach-explained-in-detail/
    public List<String> summaryRanges(int[] nums) {
        ArrayList<String> al = new ArrayList<>();

        for(int i=0;i<nums.length;i++){
            int start=nums[i];
            while(i+1<nums.length && nums[i]+1==nums[i+1]){
                i++;
            }

            if(start!=nums[i]){
                al.add(""+start+"->"+nums[i]);
            }
            else{
                al.add(""+start);
            }
        }
        return al;
    }

    // V2
    // https://leetcode.com/problems/summary-ranges/solutions/2813052/java-100-faster-solution/
    public List<String> summaryRanges_2(int[] nums) {
        List<String> list = new ArrayList<>();
        if (nums.length == 0) return list;
        int start = nums[0], end = nums[0];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] + 1 != nums[i + 1]) {
                if (start == end) {
                    sb.append(start);
                    list.add(sb.toString());
                } else {
                    sb.append(start).append("->").append(end);
                    list.add(sb.toString());
                }
                sb.setLength(0);
                start = nums[i + 1];
                end = nums[i + 1];
            } else {
                end = nums[i + 1];
            }
        }
        if (start == end) {
            sb.append(start);
            list.add(sb.toString());
        } else {
            sb.append(start).append("->").append(end);
            list.add(sb.toString());
        }
        return list;
    }

}
