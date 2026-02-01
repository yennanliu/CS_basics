package LeetCodeJava.HashTable;

// https://leetcode.com/problems/contiguous-array/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 525. Contiguous Array
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a binary array nums, return the maximum length of a contiguous subarray with an equal number of 0 and 1.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1]
 * Output: 2
 * Explanation: [0, 1] is the longest contiguous subarray with an equal number of 0 and 1.
 * Example 2:
 *
 * Input: nums = [0,1,0]
 * Output: 2
 * Explanation: [0, 1] (or [1, 0]) is a longest contiguous subarray with equal number of 0 and 1.
 * Example 3:
 *
 * Input: nums = [0,1,1,1,1,1,0,0,0]
 * Output: 6
 * Explanation: [1,1,1,0,0,0] is the longest contiguous subarray with equal number of 0 and 1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * nums[i] is either 0 or 1.
 *
 */
public class ContiguousArray {

    // V0
//    public int findMaxLength(int[] nums) {
//
//    }

    // V0-1
    // IDEA: HASHMAP (fixed by gpt)
    /**
     * 	 IDEA)
     *
     * 	 1. Convert the problem into tracking equal number of
     * 	    1s and 0s as a zero net count.
     *
     * 	 2. Maintain a count where:
     * 	     - Add +1 for each 1
     * 	     - Subtract -1 for each 0
     *
     * 	3.	If the same count value appears again,
     * 	    the subarray between the first and current index is balanced.
     *
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int findMaxLength_0_1(int[] nums) {

    // Map: {count -> first index where this count occurred}
    /**
     *
     * 	•	We use a map to store the first index where a specific count value occurs.
     * 	•	count = 0 is added with index = -1 to handle subarrays starting at index 0.
     * 	•	Why? If from index 0 to i, the net count is 0, that means the subarray is balanced.
     *
     *
     * 	-> map : { count : first_idx_when_cnt_existed }
     */
    Map<Integer, Integer> map = new HashMap<>();

        map.put(0, -1); // important: count 0 initially at index -1

        int max_len = 0;
        int count = 0;

        for (int i = 0; i < nums.length; i++) {

      // Treat 0 as -1 and 1 as +1
      // count += nums[i] == 1 ? 1 : -1;
      /**
       *  NOTE !!!
       *
       * 	•	count: keeps a running sum where:
       * 	    •	+1 for each 1
       * 	    •	-1 for each 0
       */
      if (nums[i] == 1) {
                count += 1;
            }else{
                count -= 1;
            }

      /**
       *  NOTE !!!
       *
       *    Check if the count has been seen before
       *
       *
       *    - If this count has occurred before at some earlier index j, then:
       * 	    - count(i) - count(j) == 0 ⇒ balanced from j+1 to i
       * 	    - So the length is i - j
       * 	    - We update max_len if this subarray is longer.
       *
       *
       *  -> if map contains `count` (key)
       *  -> means we see this `count` before
       *  -> the `count` before is the sum between (0, a)
       *  -> so the `sub array sub` between (a, b) can be calculated via
       *       count(a) - count(b) == 0
       *
       *    -> so the `sub array is balanced
       *    -> we should update the max_len accordingly
       */
      if (map.containsKey(count)) {
                max_len = Math.max(max_len, i - map.get(count));
            } else {
                map.put(count, i); // only put the first occurrence
            }
        }

        return max_len;
    }

    // V0-2
    // IDEA; BRUTE FORCE (TLE)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int findMaxLength_0_2(int[] nums) {

        // edge
        if(nums == null || nums.length <= 1){
            return 0;
        }
        if(nums.length == 2){
            return nums[0] != nums[1] ? 2 : 0;
        }

        // brute force
        int max_len = 0;

        for(int i = 0; i < nums.length; i++){
            int o_cnt = 0;
            int z_cnt = 0;
            for(int j = i; j < nums.length; j++){
                if(nums[j] == 0){
                    z_cnt += 1;
                }else{
                    o_cnt += 1;
                }

                if(z_cnt == o_cnt){
                    max_len = Math.max(max_len, j - i + 1);
                }
            }
        }

        return max_len;
    }

    // V1-1
    // https://leetcode.com/problems/contiguous-array/editorial/
    // IDEA: BRUTE FORCE
    /**
     * time = O(N)
     * space = O(N)
     */
    public int findMaxLength_1_1(int[] nums) {
        int maxlen = 0;
        for (int start = 0; start < nums.length; start++) {
            int zeroes = 0, ones = 0;
            for (int end = start; end < nums.length; end++) {
                if (nums[end] == 0) {
                    zeroes++;
                } else {
                    ones++;
                }
                if (zeroes == ones) {
                    maxlen = Math.max(maxlen, end - start + 1);
                }
            }
        }
        return maxlen;
    }

  // V1-2
  // https://leetcode.com/problems/contiguous-array/editorial/
  // IDEA: HASHMAP
    /**
     * time = O(N)
     * space = O(N)
     */
    public int findMaxLength_1_2(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int maxlen = 0, count = 0;
        for (int i = 0; i < nums.length; i++) {
            count = count + (nums[i] == 1 ? 1 : -1);
            if (map.containsKey(count)) {
                maxlen = Math.max(maxlen, i - map.get(count));
            } else {
                map.put(count, i);
            }
        }
        return maxlen;
    }

    // V2

}
