package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/check-if-a-number-is-majority-element-in-a-sorted-array/description/
// https://leetcode.ca/2019-01-23-1150-Check-If-a-Number-Is-Majority-Element-in-a-Sorted-Array/

import java.util.HashMap;
import java.util.Map;

/**
 * 1150. Check If a Number Is Majority Element in a Sorted Array
 * Given an array nums sorted in non-decreasing order, and a number target, return True if and only if target is a majority element.
 *
 * A majority element is an element that appears more than N/2 times in an array of length N.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,4,5,5,5,5,5,6,6], target = 5
 * Output: true
 * Explanation:
 * The value 5 appears 5 times and the length of the array is 9.
 * Thus, 5 is a majority element because 5 > 9/2 is true.
 * Example 2:
 *
 * Input: nums = [10,100,101,101], target = 101
 * Output: false
 * Explanation:
 * The value 101 appears 2 times and the length of the array is 4.
 * Thus, 101 is not a majority element because 2 > 4/2 is false.
 *
 *
 * Note:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 10^9
 * 1 <= target <= 10^9
 * Difficulty:
 * Easy
 * Lock:
 * Prime
 * Company:
 * Salesforce
 * Problem Solution
 * 1150-Check-If-a-Number-Is-Majority-Element-in-a-Sorted-Array
 *
 *
 */

public class CheckIfANumberIsMajorityElementInASortedArray {

    // V0
    // IDEA: BRUTE FORCE
    // time: O(N)
    // space: O(N)
    // TODO: validate
    public boolean isMajorityElement(int[] nums, int target) {
        int count = 0;
        for (int x : nums) {
            if (x == target) {
                count++;
            }
        }
        return count > nums.length / 2;
    }

    // V0-0-1
    // IDEA: BINARY SEARCH + FIND FIRST IDX (gemini)
    // TODO: validate
    /**
     * Metric,Complexity,Explanation
     * Time,O(logN),We perform one binary search to find the start of the target range.
     * Space,O(1),No extra data structures needed.
     *
     */
    public boolean isMajorityElement_0_0_1(int[] nums, int target) {
        int n = nums.length;

        // 1. Find the first occurrence of target using Binary Search
        int firstIndex = findFirst_0_0_1(nums, target);

        // 2. If target isn't in the array, it can't be the majority
        if (firstIndex == -1) return false;

        // 3. Check if the element at index (firstIndex + n/2) is still the target.
        // If it is, then there are at least (n/2 + 1) occurrences.
        int majorityIndex = firstIndex + n / 2;
        return majorityIndex < n && nums[majorityIndex] == target;
    }

    private int findFirst_0_0_1(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        int ans = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] >= target) {
                if (nums[mid] == target) ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }



    // V0-0-0-1
    // IDEA: HASHMAP
    // time: O(N)
    // space: O(N)
    // TODO: validate
//    public boolean isMajorityElement_0_0_1(int[] nums, int target) {
//        // edge
//        if(nums == null){
//            return false;
//        }
//        if(nums.length == 1){
//            return nums[0] == target;
//        }
//
//        // map: { val : cnt }
//        Map<Integer, Integer> map = new HashMap<>();
//        for(int x: nums){
//            map.put(x, map.getOrDefault(x, 0) + 1);
//        }
//
//        // edge
//        if(!map.containsKey(target)){
//            return false;
//        }
//
//        return map.get(target) > nums.length / 2;
//    }



    // V0-1
    // IDEA: BINARY SEARCH (GEMINI)
    // time: O(log N)
    // space: O(1)
    public boolean isMajorityElement_0_1(int[] nums, int target) {
        int n = nums.length;
        int firstIdx = findFirst(nums, target);

        // If target is not found or the "jump" index is out of bounds
        int majorityIdx = firstIdx + n / 2;
        return firstIdx != -1 && majorityIdx < n && nums[majorityIdx] == target;
    }

    /** NOTE !!!
     *
     *   this help func tends to find the `first idx` of target in nums,
     *   via binary search
     */
    private int findFirst(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        int res = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] >= target) {
                if (nums[mid] == target){
                    res = mid;
                    high = mid - 1; // Keep looking left for the 'first' one
                }
            } else {
                low = mid + 1;
            }
        }
        return res;
    }


    // V0-2
    // IDEA: BINARY SEARCH (GPT)
    // time: O(log N)
    // space: O(1)
    public boolean isMajorityElement_0_2(int[] nums, int target) {
        int n = nums.length;
        int first = firstOccurrence(nums, target);

        if(first == -1) return false;

        return first + n/2 < n && nums[first + n/2] == target;
    }

    /** NOTE !!!
     *
     *   this help func tends to find the `first idx` of target in nums,
     *   via binary search
     */
    private int firstOccurrence(int[] nums, int target){
        int left = 0, right = nums.length - 1;

        while(left <= right){
            int mid = left + (right - left) / 2;

            if(nums[mid] >= target){
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        if(left < nums.length && nums[left] == target){
            return left;
        }

        return -1;
    }



    // V1
    // IDEA: BINARY SEARCH
    // https://leetcode.ca/2019-01-23-1150-Check-If-a-Number-Is-Majority-Element-in-a-Sorted-Array/
    public boolean isMajorityElement_1(int[] nums, int target) {
        int left = search(nums, target);
        int right = search(nums, target + 1);
        return right - left > nums.length / 2;
    }

    private int search(int[] nums, int x) {
        int left = 0, right = nums.length;
        while (left < right) {
            int mid = (left + right) >> 1;
            if (nums[mid] >= x) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }


    // V2



    // V3




}
