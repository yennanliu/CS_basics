package LeetCodeJava.Array;

// https://leetcode.com/problems/missing-ranges/description/
// https://leetcode.ca/all/163.html

import java.util.ArrayList;
import java.util.List;

/**
 * 163. Missing Ranges
 * Given a sorted integer array nums, where the range of elements are in the inclusive range [lower, upper], return its missing ranges.
 * <p>
 * Example:
 * <p>
 * Input: nums = [0, 1, 3, 50, 75], lower = 0 and upper = 99,
 * Output: ["2", "4->49", "51->74", "76->99"]
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Oracle
 * Problem Solution
 */
public class MissingRanges {

    // V0
    // IDEA : ARRAY + BOUNDARY HANDLING (fix by gpt)
    public List<List<Integer>> findMissingRanges(int[] nums, int lower, int upper) {

        List<List<Integer>> res = new ArrayList<>();

        // Edge case: if nums is empty, return the whole range
        if (nums.length == 0) {
            addRange(res, lower, upper);
            return res;
        }

        // Check for missing range before the first element
        if (nums[0] > lower) {
            addRange(res, lower, nums[0] - 1);
        }

        // Find missing ranges between the numbers in nums
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1] + 1) {
                addRange(res, nums[i - 1] + 1, nums[i] - 1);
            }
        }

        // Check for missing range after the last element
        if (nums[nums.length - 1] < upper) {
            addRange(res, nums[nums.length - 1] + 1, upper);
        }

        return res;
    }

    // Helper method to add the range to the result list
    private void addRange(List<List<Integer>> res, int start, int end) {
        List<Integer> range = new ArrayList<>();
        range.add(start);
        if (start != end) {
            range.add(end);
        }
        res.add(range);
    }


    // V1
    // https://leetcode.ca/2016-05-11-163-Missing-Ranges/
//    public List<List<Integer>> findMissingRanges_1(int[] nums, int lower, int upper) {
//        int n = nums.length;
//        if (n == 0) {
//            return List.of(List.of(lower, upper));
//        }
//        List<List<Integer>> ans = new ArrayList<>();
//        if (nums[0] > lower) {
//            ans.add(List.of(lower, nums[0] - 1));
//        }
//        for (int i = 1; i < n; ++i) {
//            if (nums[i] - nums[i - 1] > 1) {
//                ans.add(List.of(nums[i - 1] + 1, nums[i] - 1));
//            }
//        }
//        if (nums[n - 1] < upper) {
//            ans.add(List.of(nums[n - 1] + 1, upper));
//        }
//        return ans;
//    }

    // V2
}
