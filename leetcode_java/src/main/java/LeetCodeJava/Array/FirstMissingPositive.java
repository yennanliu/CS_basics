package LeetCodeJava.Array;

// https://leetcode.com/problems/first-missing-positive/
// https://leetcode.ca/all/41.html

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 41. First Missing Positive
 * Solved
 * Hard
 * Topics
 * Companies
 * Hint
 * Given an unsorted integer array nums. Return the smallest positive integer that is not present in nums.
 *
 * You must implement an algorithm that runs in O(n) time and uses O(1) auxiliary space.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,0]
 * Output: 3
 * Explanation: The numbers in the range [1,2] are all in the array.
 * Example 2:
 *
 * Input: nums = [3,4,-1,1]
 * Output: 2
 * Explanation: 1 is in the array but 2 is missing.
 * Example 3:
 *
 * Input: nums = [7,8,9,11,12]
 * Output: 1
 * Explanation: The smallest positive integer 1 is missing.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * -231 <= nums[i] <= 231 - 1
 *
 */
public class FirstMissingPositive {

    // V0
    // IDEA: HASHMAP + MATH
    /**

     * time = O(N)

     * space = O(N)

     */
    public int firstMissingPositive(int[] nums) {

        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // {key : cnt}
        int maxVal = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            maxVal = Math.max(maxVal, n);
            map.put(n, map.getOrDefault(n, 0) + 1);
        }

        int start = 1;
        while (start <= maxVal) {
            if (!map.containsKey(start)) {
                return start;
            }
            start += 1;
        }

        return start;
    }


    // V0-1
    // IDEA: SET + MATH
    public int firstMissingPositive_0_1(int[] nums) {
        // edge

        // ??
        int smallest = Integer.MAX_VALUE; // ????
        int biggest = -1 * Integer.MAX_VALUE; // ????

        Set<Integer> set = new HashSet<>();
        for (int x : nums) {
            if (x > 0) {
                set.add(x);
                smallest = Math.min(smallest, x);
                biggest = Math.max(biggest, x);
            }
        }

        if (smallest > 1) {
            return 1;
        }

        while (smallest <= biggest) {
            if (!set.contains(smallest) && smallest != 0) {
                return smallest;
            }
            smallest += 1;
        }

        return Math.max(biggest + 1, 1);
    }


    // V1-1
    // IDEA: Boolean Array
    // https://leetcode.com/problems/first-missing-positive/editorial/
    public int firstMissingPositive_1_1(int[] nums) {
        int n = nums.length;
        boolean[] seen = new boolean[n + 1]; // Array for lookup

        // Mark the elements from nums in the lookup array
        for (int num : nums) {
            if (num > 0 && num <= n) {
                seen[num] = true;
            }
        }

        // Iterate through integers 1 to n
        // return smallest missing positive integer
        for (int i = 1; i <= n; i++) {
            if (!seen[i]) {
                return i;
            }
        }

        // If seen contains all elements 1 to n
        // the smallest missing positive number is n + 1
        return n + 1;
    }


    // V1-2
    // IDEA:  Index as a Hash Key
    // https://leetcode.com/problems/first-missing-positive/editorial/
    public int firstMissingPositive_1_2(int[] nums) {
        int n = nums.length;
        boolean contains1 = false;

        // Replace negative numbers, zeros,
        // and numbers larger than n with 1s.
        // After this nums contains only positive numbers.
        for (int i = 0; i < n; i++) {
            // Check whether 1 is in the original array
            if (nums[i] == 1) {
                contains1 = true;
            }
            if (nums[i] <= 0 || nums[i] > n) {
                nums[i] = 1;
            }
        }

        if (!contains1)
            return 1;

        // Mark whether integers 1 to n are in nums
        // Use index as a hash key and negative sign as a presence detector.
        for (int i = 0; i < n; i++) {
            int value = Math.abs(nums[i]);
            if (value == n) {
                nums[0] = -Math.abs(nums[0]);
            } else {
                nums[value] = -Math.abs(nums[value]);
            }
        }

        // First positive in nums is smallest missing positive integer
        for (int i = 1; i < n; i++) {
            if (nums[i] > 0)
                return i;
        }

        // nums[0] stores whether n is in nums
        if (nums[0] > 0) {
            return n;
        }

        // If nums contains all elements 1 to n
        // the smallest missing positive number is n + 1
        return n + 1;
    }



    // V1-3
    // IDEA: CYCLE SORT
    // https://leetcode.com/problems/first-missing-positive/editorial/
    public int firstMissingPositive_1_3(int[] nums) {
        int n = nums.length;

        // Use cycle sort to place positive elements smaller than n
        // at the correct index
        int i = 0;
        while (i < n) {
            int correctIdx = nums[i] - 1;
            if (nums[i] > 0 && nums[i] <= n && nums[i] != nums[correctIdx]) {
                swap(nums, i, correctIdx);
            } else {
                i++;
            }
        }

        // Iterate through nums
        // return smallest missing positive integer
        for (i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        // If all elements are at the correct index
        // the smallest missing positive number is n + 1
        return n + 1;
    }

    // Swaps two elements in nums
    private void swap(int[] nums, int index1, int index2) {
        int temp = nums[index1];
        nums[index1] = nums[index2];
        nums[index2] = temp;
    }



    // V2


    // V3


}
