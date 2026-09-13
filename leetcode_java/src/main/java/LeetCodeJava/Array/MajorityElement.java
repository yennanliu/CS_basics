package LeetCodeJava.Array;

// https://leetcode.com/problems/majority-element/

import java.util.HashMap;

/**
 * 169. Majority Element
 * Easy
 *
 * Given an array nums of size n, return the majority element.
 *
 * The majority element is the element that appears more than ⌊n / 2⌋ times. You may assume that the majority element always exists in the array.
 *
 * Example 1:
 *
 * Input: nums = [3,2,3]
 * Output: 3
 *
 * Example 2:
 *
 * Input: nums = [2,2,1,1,1,2,2]
 * Output: 2
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 5 * 10^4
 * -10^9 <= nums[i] <= 10^9
 * The input is generated such that a majority element will exist in the array.
 *
 * Follow-up: Could you solve the problem in linear time and in O(1) space?
 *
 */
public class MajorityElement {

    // V0
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
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
