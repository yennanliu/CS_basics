package LeetCodeJava.HashTable;

// https://leetcode.com/problems/contains-duplicate/

import java.util.HashMap;

public class ContainsDuplicate {

    public boolean containsDuplicate(int[] nums) {

        if (nums.length == 0 || nums.equals(null)){
            return true;
        }

        HashMap<Integer, Integer> map = new HashMap();
        for (int i = 0; i <  nums.length; i++){
            if (map.containsKey(nums[i])){
                map.put(nums[i], map.get(nums[i])+1);
            }else{
                map.put(nums[i], 1);
            }
        }

        return map.values().size() < nums.length;
    }

}
