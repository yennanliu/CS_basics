package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/single-element-in-a-sorted-array/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 540. Single Element in a Sorted Array
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given a sorted array consisting of only integers where every element appears exactly twice, except for one element which appears exactly once.
 *
 * Return the single element that appears only once.
 *
 * Your solution must run in O(log n) time and O(1) space.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2,3,3,4,4,8,8]
 * Output: 2
 * Example 2:
 *
 * Input: nums = [3,3,7,7,10,11,11]
 * Output: 10
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 105
 *
 *
 */
public class SingleElementInASortedArray {

    // V0
    // IDEA: HASHMAP
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int singleNonDuplicate(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return -1; // /??
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }

        int ans = -1;
        for (int k : map.keySet()) {
            if (map.get(k) == 1) {
                return k;
            }
        }

        return ans;
    }

    // V1

    // V2
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/single-element-in-a-sorted-array/solutions/3212178/day-52-binary-search-easiest-beginner-fr-sqyl/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int singleNonDuplicate_2(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (mid % 2 == 1) {
                mid--;
            }
            if (nums[mid] != nums[mid + 1]) {
                right = mid;
            } else {
                left = mid + 2;
            }
        }
        return nums[left];
    }

    // V3
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/single-element-in-a-sorted-array/solutions/627921/java-c-python3-easy-explanation-ologn-o1-71nt/
    public int singleNonDuplicate_3(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if ((mid % 2 == 0 && nums[mid] == nums[mid + 1]) || (mid % 2 == 1 && nums[mid] == nums[mid - 1]))
                left = mid + 1;
            else
                right = mid;
        }
        return nums[left];
    }




}
