package LeetCodeJava.HashTable;

// https://leetcode.com/problems/subarrays-with-k-different-integers/description/

import java.util.HashMap;
import java.util.Map;

/**
 *
 Code
 Testcase
 Testcase
 Test Result
 992. Subarrays with K Different Integers
 Solved
 Hard
 Topics
 premium lock icon
 Companies
 Hint
 Given an integer array nums and an integer k, return the number of good subarrays of nums.

 A good array is an array where the number of different integers in that array is exactly k.

 For example, [1,2,3,1,2] has 3 different integers: 1, 2, and 3.
 A subarray is a contiguous part of an array.



 Example 1:

 Input: nums = [1,2,1,2,3], k = 2
 Output: 7
 Explanation: Subarrays formed with exactly 2 different integers: [1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2]
 Example 2:

 Input: nums = [1,2,1,3,4], k = 3
 Output: 3
 Explanation: Subarrays formed with exactly 3 different integers: [1,2,1,3], [2,1,3], [1,3,4].


 Constraints:

 1 <= nums.length <= 2 * 104
 1 <= nums[i], k <= nums.length
 *
 *
 */
public class SubarraysWithKDifferentIntegers {

    // V0
//    public int subarraysWithKDistinct(int[] nums, int k) {
//
//    }

    // V1-1
    // IDEA: SLIDE WINDOW
    // https://leetcode.com/problems/subarrays-with-k-different-integers/editorial/
    public int subarraysWithKDistinct_1_1(int[] nums, int k) {
        return slidingWindowAtMost(nums, k) - slidingWindowAtMost(nums, k - 1);
    }

    // Helper function to count the number of subarrays with at most k distinct elements.
    private int slidingWindowAtMost(int[] nums, int distinctK) {
        // To store the occurrences of each element.
        Map<Integer, Integer> freqMap = new HashMap<>();
        int left = 0, totalCount = 0;

        // Right pointer of the sliding window iterates through the array.
        for (int right = 0; right < nums.length; right++) {
            freqMap.put(nums[right], freqMap.getOrDefault(nums[right], 0) + 1);

            // If the number of distinct elements in the window exceeds k,
            // we shrink the window from the left until we have at most k distinct elements.
            while (freqMap.size() > distinctK) {
                freqMap.put(nums[left], freqMap.get(nums[left]) - 1);
                if (freqMap.get(nums[left]) == 0) {
                    freqMap.remove(nums[left]);
                }
                left++;
            }

            // Update the total count by adding the length of the current subarray.
            totalCount += (right - left + 1);
        }
        return totalCount;
    }


    // V1-2
    // IDEA: SLIDE WINDOW + ONE PASS
    // https://leetcode.com/problems/subarrays-with-k-different-integers/editorial/
    public int subarraysWithKDistinct_1_2(int[] nums, int k) {
        // Array to store the count of distinct values encountered
        int[] distinctCount = new int[nums.length + 1];

        int totalCount = 0;
        int left = 0;
        int right = 0;
        int currCount = 0;

        while (right < nums.length) {
            // Increment the count of the current element in the window
            if (distinctCount[nums[right++]]++ == 0) {
                // If encountering a new distinct element, decrement K
                k--;
            }

            // If K becomes negative, adjust the window from the left
            if (k < 0) {
                // Move the left pointer until the count of distinct elements becomes valid again
                --distinctCount[nums[left++]];
                k++;
                currCount = 0;
            }

            // If K becomes zero, calculate subarrays
            if (k == 0) {
                // While the count of left remains greater than 1, keep shrinking the window from the left
                while (distinctCount[nums[left]] > 1) {
                    --distinctCount[nums[left++]];
                    currCount++;
                }
                // Add the count of subarrays with K distinct elements to the total count
                totalCount += (currCount + 1);
            }
        }
        return totalCount;
    }




    // V2



}
