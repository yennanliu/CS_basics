package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/search-insert-position/description/
/**
 * 35. Search Insert Position
 * Solved
 * Easy
 * Topics
 * Companies
 * Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return the index where it would be if it were inserted in order.
 *
 * You must write an algorithm with O(log n) runtime complexity.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5,6], target = 5
 * Output: 2
 * Example 2:
 *
 * Input: nums = [1,3,5,6], target = 2
 * Output: 1
 * Example 3:
 *
 * Input: nums = [1,3,5,6], target = 7
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -104 <= nums[i] <= 104
 * nums contains distinct values sorted in ascending order.
 * -104 <= target <= 104
 *
 *
 */
public class SearchInsertPosition {

    // V0
//    public int searchInsert(int[] nums, int target) {
//
//    }

    // V1

    // V2
    // https://leetcode.com/problems/search-insert-position/solutions/5361984/video-return-middle-or-left-pointer-by-n-dj1y/
    public int searchInsert_2(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    // V3
    // https://leetcode.com/problems/search-insert-position/solutions/6592447/runtime-0-ms-beats-10000-by-deepakkr_567-x8es/
    public int searchInsert_3(int[] nums, int target) {
        int st = 0;
        int end = nums.length - 1;
        boolean find = false;

        while (st <= end) {
            int mid = (st + end) / 2;

            if (nums[mid] == target) {
                find = true;
                return mid;
            } else if (nums[mid] < target)
                st = mid + 1;
            else
                end = mid - 1;
        }
        if (find == false) {
            for (int i = nums.length - 1; i >= 0; i--) {
                if (target > nums[i])
                    return i + 1;
                else if (i == 0) {
                    return i;
                }
            }
        }

        return -1;//Unreachable statement
    }

}
