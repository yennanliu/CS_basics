package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/search-in-rotated-sorted-array-ii/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 81. Search in Rotated Sorted Array II
 * Solved
 * Medium
 * Topics
 * Companies
 * There is an integer array nums sorted in non-decreasing order (not necessarily with distinct values).
 * <p>
 * Before being passed to your function, nums is rotated at an unknown pivot index k (0 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,4,4,5,6,6,7] might be rotated at pivot index 5 and become [4,5,6,6,7,0,1,2,4,4].
 * <p>
 * Given the array nums after the rotation and an integer target, return true if target is in nums, or false if it is not in nums.
 * <p>
 * You must decrease the overall operation steps as much as possible.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: nums = [2,5,6,0,0,1,2], target = 0
 * Output: true
 * Example 2:
 * <p>
 * Input: nums = [2,5,6,0,0,1,2], target = 3
 * Output: false
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= nums.length <= 5000
 * -104 <= nums[i] <= 104
 * nums is guaranteed to be rotated at some pivot.
 * -104 <= target <= 104
 * <p>
 * <p>
 * Follow up: This problem is similar to Search in Rotated Sorted Array, but nums may contain duplicates. Would this affect the runtime complexity? How and why?
 */
public class SearchInRotatedSortedArray2 {

    // V0
    // IDEA : BINARY SEARCH + SET
    // LC 33
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean search(int[] nums, int target) {

        if (nums.length == 1) {
            return nums[0] == target;
        }
        Set<Integer> set = new HashSet<>();
        List<Integer> nonDuplicatedNums = new ArrayList<>();

        for (int x : nums) {
            if (!set.contains(x)) {
                set.add(x);
                nonDuplicatedNums.add(x);
            }
        }

        // binary search
        int left = 0;
        int right = nonDuplicatedNums.size() - 1;
        while (right >= left) {
            int mid = (left + right) / 2;
            if (nonDuplicatedNums.get(mid) == target) {
                return true;
            }
            // if right sub array is ascending
            if (nonDuplicatedNums.get(mid) < nonDuplicatedNums.get(right)) {
                // if  mid < target <= right
                if (target > nonDuplicatedNums.get(mid) && target <= nonDuplicatedNums.get(right)) {
                    left = mid + 1;
                }
                // else
                else {
                    right = mid - 1;
                }
            }
            // if left sub array is ascending
            else {
                // if mid > target > left
                if (target >= nonDuplicatedNums.get(left) && target < nonDuplicatedNums.get(mid)) {
                    right = mid - 1;
                }
                // else
                else {
                    left = mid + 1;
                }

            }

        }

        return false;
    }


    // V0-0-1
    // IDEA: PURE BINARY SEARCH (no dedup) (gemini)
    public boolean search_0_0_1(int[] nums, int target) {
        if (nums == null || nums.length == 0)
            return false;

        int l = 0;
        int r = nums.length - 1;

        while (l <= r) {
            int mid = l + (r - l) / 2;
            int cur = nums[mid];

            if (cur == target)
                return true;

            // --- THE KEY FIX FOR LC 81 ---
            // If we can't tell which side is sorted because of duplicates,
            // just shrink the window from both sides.
            if (nums[l] == cur && nums[r] == cur) {
                l++;
                r--;
            }
            // Case 1: Left side is sorted
            else if (nums[l] <= cur) {
                // Check if target is within the sorted left half
                if (target >= nums[l] && target < cur) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            // Case 2: Right side is sorted
            else {
                // Check if target is within the sorted right half
                if (target > cur && target <= nums[r]) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
        }

        return false;
    }


    // V0-0-2
    // IDEA: PURE BINARY SEARCH (no dedup) (GPT)
    public boolean search_0_0_2(int[] nums, int target) {

        if (nums == null || nums.length == 0) {
            return false;
        }

        int l = 0;
        int r = nums.length - 1;

        while (l <= r) {

            int mid = l + (r - l) / 2;

            if (nums[mid] == target) {
                return true;
            }

            // Handle duplicates (key difference from LC 33)
            if (nums[l] == nums[mid] && nums[mid] == nums[r]) {
                l++;
                r--;
            }
            // Left half is sorted
            else if (nums[l] <= nums[mid]) {

                if (nums[l] <= target && target < nums[mid]) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            // Right half is sorted
            else {

                if (nums[mid] < target && target <= nums[r]) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
        }

        return false;
    }


    // V0-1
    // IDEA: BINARY SEARCH + `DEDUP` + LC 33
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean search_0_1(int[] nums, int target) {

        if (nums.length == 0 || nums.equals(null)){
            return false;
        }

        List<Integer> collected = new ArrayList<>();
        for(int n: nums){
            if(!collected.contains(n)){
                collected.add(n);
            }
        }

        int l = 0;
        int r = collected.size() - 1;

        while (r >= l){

            int mid = (l + r) / 2;
            int cur = collected.get(mid);
            if (cur == target){
                return true;
            }
            // Case 1: `left + mid` is in ascending order
            /** NOTE !!! we compare mid with left, instead of 0 idx element */
            else if (collected.get(mid) >= collected.get(l)) {
                // case 1-1)  target < mid && target > l
                if (target >= collected.get(l) && target < collected.get(mid)) {
                    r = mid - 1;
                }
                // case 2-2)
                else {
                    l = mid + 1;
                }
            }

            // Case 2:  `right + mid` is in ascending order
            else {
                // case 2-1)  target > min && target <= r
                if (target <= collected.get(r) && target >collected.get(mid)) {
                    l = mid + 1;
                }
                // case 2-2)
                else {
                    r = mid - 1;
                }
            }

        }

        return false;
    }

    // V1
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/search-in-rotated-sorted-array-ii/solutions/3888242/100-binary-search-video-with-rotation-handling-optimal/
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean search_1(int[] nums, int target) {
        int low = 0, high = nums.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (nums[mid] == target) return true;

            if (nums[low] == nums[mid]) {
                low++;
                continue;
            }

            if (nums[low] <= nums[mid]) {
                if (nums[low] <= target && target <= nums[mid]) high = mid - 1;
                else low = mid + 1;
            } else {
                if (nums[mid] <= target && target <= nums[high]) low = mid + 1;
                else high = mid - 1;
            }
        }
        return false;
    }

    // V2
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/search-in-rotated-sorted-array-ii/solutions/2689441/java-best-solution-for-understanding/
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean search_2(int[] nums, int target) {
        int start = 0, end = nums.length - 1;
        while(start <= end) {
            int mid = start + (end - start) / 2;
            if(nums[mid] == target) return true;

            //if there are duplicates
            if(nums[start] == nums[mid] && nums[mid] == nums[end]) {
                start ++;
                end --;
            }

            //left half is sorted
            else if(nums[start] <= nums[mid]) {
                if(target >= nums[start] && target <= nums[mid])
                    end = mid - 1;

                else
                    start = mid + 1;
            }

            //right half is sorted
            else {
                if(target <= nums[end] && target >= nums[mid])
                    start = mid + 1;

                else
                    end = mid - 1;
            }
        }
        return false;
    }





}
