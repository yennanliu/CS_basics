package LeetCodeJava.Array;

// https://leetcode.com/problems/majority-element/

import java.util.HashMap;

public class MajorityElement {

    // V0
    public int majorityElement(int[] nums) {

        if (nums.length == 1){
            return nums[0];
        }

        int res = 0;
        int len = nums.length;
        HashMap<Integer, Integer> map = new HashMap();
        for (int i = 0; i < nums.length; i++){
            int cur = nums[i];
            if (!map.containsKey(cur)){
                map.put(cur, 1);
            }else {
                map.put(cur, map.get(cur)+1);
            }
        }

        for (int key : map.keySet()){
            if (map.get(key) > len / 2){
                return key;
            }
        }
        return res;
    }

    // V1
    // https://leetcode.com/problems/majority-element/solutions/3407133/o-1-java-solution-ternary-operators-in-linear-time-beats-100/
    public int majorityElement_1(int[] nums) {
        int majority_index = 0;
        int count = 1;
        for (int i=1; i < nums.length; i++){
            count += nums[i] == nums[majority_index] ? 1 : -1;

            if (count == 0){
                majority_index = ++i;
                count++;
            }
        }
        return nums[majority_index];
    }
}
