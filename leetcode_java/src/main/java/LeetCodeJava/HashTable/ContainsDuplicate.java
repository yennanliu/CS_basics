package LeetCodeJava.HashTable;

// https://leetcode.com/problems/contains-duplicate/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ContainsDuplicate {

    // V0
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int x : nums){
            set.add(x);
        }
        return set.size() != nums.length; // true : has duplicated element
    }

    // V1
    public boolean containsDuplicate_1(int[] nums) {

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
