package LeetCodeJava.HashTable;

// https://leetcode.com/problems/contains-duplicate/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 217. Contains Duplicate
 * Easy
 *
 * Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,1]
 * Output: true
 * Explanation:
 *
 * The element 1 occurs at the indices 0 and 3.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4]
 * Output: false
 * Explanation:
 *
 * All elements are distinct.
 *
 * Example 3:
 *
 * Input: nums = [1,1,1,3,3,4,3,2,4,2]
 * Output: true
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class ContainsDuplicate {

    // V0
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int x : nums){
            set.add(x);
        }
        return set.size() != nums.length; // true : has duplicated element
    }

    // V1
    /**
     * time = O(N)
     * space = O(N)
     */
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
