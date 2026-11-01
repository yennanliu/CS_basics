package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/binary-search/description/
// https://neetcode.io/problems/binary-search

/**
 * 704. Binary Search
 * Easy
 *
 * Given an array of integers nums which is sorted in ascending order, and an integer
 * target, write a function to search target in nums. If target exists, then return
 * its index. Otherwise, return -1.
 *
 * You must write an algorithm with O(log n) runtime complexity.
 *
 *
 * Example 1:
 *
 * Input: nums = [-1,0,3,5,9,12], target = 9
 * Output: 4
 * Explanation: 9 exists in nums and its index is 4
 *
 * Example 2:
 *
 * Input: nums = [-1,0,3,5,9,12], target = 2
 * Output: -1
 * Explanation: 2 does not exist in nums so return -1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^4
 * -10^4 < nums[i], target < 10^4
 * All the integers in nums are unique.
 * nums is sorted in ascending order.
 *
 */
public class BinarySearch {

    // V0
    // IDEA: BINARY SEARCH (THE TEXTBOOK CLOSED-INTERVAL TEMPLATE)
    //
    //   Invariant: if target is in nums at all, it is inside nums[left..right].
    //   Every step throws away the half that cannot hold it, so the interval empties
    //   only when target is genuinely absent.
    //
    //   e.g. nums = [-1,0,3,5,9,12], target = 9
    //        left=0 right=5 mid=2 (3) -> 3 < 9 -> left = 3
    //        left=3 right=5 mid=4 (9) -> hit   -> 4
    //
    //   NOTE: `while (left <= right)` together with `right = mid - 1`: with `<` the
    //         single-element interval left == right is never tested.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int search(int[] nums, int target) {
        // edge case: empty input
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            // not (left + right) / 2, which can overflow int
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                // target can only be to the right of mid
                left = mid + 1;
            } else {
                // target can only be to the left of mid
                right = mid - 1;
            }
        }

        return -1;
    }
}
