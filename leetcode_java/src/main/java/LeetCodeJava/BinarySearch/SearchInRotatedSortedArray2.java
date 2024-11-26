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

    // V1
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/search-in-rotated-sorted-array-ii/solutions/3888242/100-binary-search-video-with-rotation-handling-optimal/
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
