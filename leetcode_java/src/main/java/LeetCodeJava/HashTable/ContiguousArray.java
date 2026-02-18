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

    // V0-0-1
    // IDEA: HASHMAP (gemini)
    /**
     * To solve **LC 525 (Contiguous Array)**, the goal is to find the maximum length of a subarray with an equal number of `0`s and `1`s.
     *
     * ### 🧠 The Strategy: Transform and Prefix Sum
     *
     * The "equal number" problem becomes much easier if we apply a simple transformation:
     *
     * 1. Treat every **`0` as `-1**`.
     * 2. Treat every **`1` as `+1**`.
     *
     * Now, the problem changes to: **"Find the longest subarray whose sum is 0."**
     *
     * As we saw in LC 560,
     * we can use a **HashMap** to store prefix sums.
     * If we see the same prefix sum twice, it means the elements in between them must have summed to zero.
     *
     * ---
     *
     * ### 🔍 Why this works
     *
     * * **The Zero-Sum Property**: If `prefixSum` at index  is , and at index  it is also , then everything added between  and  must have cancelled out to .
     * * **Greedy for Length**: We only `put` the prefix sum into the map if it's **not already there**. This ensures we keep the leftmost possible index, making the difference `i - prevIndex` as large as possible.
     * * **The `-1` Base Case**: By putting `(0, -1)` in the map, we handle subarrays that start from index  correctly. If `prefixSum` is  at index , the length is .
     *
     * ### 📈 Complexity
     *
     * * **Time Complexity**:  — One pass through the array.
     * * **Space Complexity**:  — In the worst case, we store  unique prefix sums.
     *
     * **This "Map the problem to a Zero-Sum" trick is very powerful. Would you like to see how it's used to solve LC 1124 (Longest Well-Performing Interval)?**
     *
     *
     */
    /**
     *  DEMO:
     *
     *     * ### 📊 Visualization
     *      *
     *      * `nums = [0, 1, 0, 1]`
     *      *
     *      * 1. **Initial**: `map = {0: -1}`
     *      * 2. **i=0 (val 0)**: `sum = -1`. Map: `{0: -1, -1: 0}`
     *      * 3. **i=1 (val 1)**: `sum = 0`. Seen 0! `len = 1 - (-1) = 2`.
     *      * 4. **i=2 (val 0)**: `sum = -1`. Seen -1! `len = max(2, 2 - 0) = 2`.
     *      * 5. **i=3 (val 1)**: `sum = 0`. Seen 0! `len = max(2, 3 - (-1)) = 4`.
     *
     */
    public int findMaxLength_0_0_1(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;

        // Map: { PrefixSum : First_Index_It_Occurred }
        Map<Integer, Integer> map = new HashMap<>();

        // Base Case: A sum of 0 exists at index -1
        map.put(0, -1);

        int maxLength = 0;
        int prefixSum = 0;

        for (int i = 0; i < nums.length; i++) {
            /** NOTE !!!
             *
             *
             *  -> The -1 Base Case:
             *       By putting (0, -1) in the map,
             *       we handle subarrays that start from index 0 correctly.
             *
             *      ->  If prefixSum is 0 at index 3, the length is 3 - (-1) = 4.
             */
            // Transform 0 to -1, 1 stays 1
            prefixSum += (nums[i] == 1) ? 1 : -1;

            /** NOTE !!!
             *
             * The Zero-Sum Property:
             *   If prefixSum at index $A$ is $5$, and at index $B$ it is also $5$,
             *   then everything added between $A$ and $B$
             *   must have cancelled out to $0$.
             */
            if (map.containsKey(prefixSum)) {
                // If we've seen this sum before, the subarray between
                // the previous index and current index has a net sum of 0.
                int prevIndex = map.get(prefixSum);
                /** NOTE !!! `i - prevIndex` ,
                 *
                 *   (NOT `i - prevIndex + 1`)
                 *
                 *
                 *  -> The -1 Base Case:
                 *       By putting (0, -1) in the map,
                 *       we handle subarrays that start from index 0 correctly.
                 *
                 *      ->  If prefixSum is 0 at index 3, the length is 3 - (-1) = 4.
                 */
                maxLength = Math.max(maxLength, i - prevIndex);
            } else {
                // Only store the FIRST occurrence to maximize the length
                map.put(prefixSum, i);
            }
        }

        return maxLength;
    }


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

    // V0-3
    // IDEA: BRUTE FORCE (TLE)
    public int findMaxLength_0_3(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 0;
        }

        int maxLen = 0;
        for (int l = 0; l < nums.length; l++) {
            int zeroCnt = 0;
            int oneCnt = 0;
            for (int r = l; r < nums.length; r++) {
                if (nums[r] == 0) {
                    zeroCnt += 1;
                } else {
                    oneCnt += 1;
                }
                if (zeroCnt == oneCnt) {
                    maxLen = Math.max(maxLen, r - l + 1);
                }
            }
        }

        return maxLen;
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
