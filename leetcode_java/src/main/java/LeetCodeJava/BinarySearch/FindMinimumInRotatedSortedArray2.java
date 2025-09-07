package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/description/
// https://leetcode.ca/all/154.html
/**
 *  154. Find Minimum in Rotated Sorted Array II
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array nums = [0,1,4,4,5,6,7] might become:
 *
 * [4,5,6,7,0,1,4] if it was rotated 4 times.
 * [0,1,4,4,5,6,7] if it was rotated 7 times.
 * Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].
 *
 * Given the sorted rotated array nums that may contain duplicates, return the minimum element of this array.
 *
 * You must decrease the overall operation steps as much as possible.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5]
 * Output: 1
 * Example 2:
 *
 * Input: nums = [2,2,2,0,1]
 * Output: 0
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 5000
 * -5000 <= nums[i] <= 5000
 * nums is sorted and rotated between 1 and n times.
 *
 *
 */
public class FindMinimumInRotatedSortedArray2 {

    // V0

    // V1
    // https://leetcode.ca/2016-05-02-154-Find-Minimum-in-Rotated-Sorted-Array-II/
    public int findMin_1(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) >> 1;
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                --right;
            }
        }
        return nums[left];
    }

    // V2
    // https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/solutions/7157901/easy-code-for-solve-this-problem-by-4_k4-5p13/
    public int findMin_2(int[] nums) {
        int min = Integer.MAX_VALUE;
        for (int i : nums) {
            if (i < min) {
                min = i;
            }
        }
        return min;

    }

    // V3
    // https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/solutions/7135635/java-solution-beats-100-proof-concise-co-2vpe/
    public int findMin_3(int[] nums) {
        int min = Integer.MAX_VALUE;
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            while (left < right && nums[left] == nums[left + 1])
                left++;
            while (left < right && nums[right] == nums[right - 1])
                right--;
            int mid = (left + right) / 2;
            if (nums[mid] > nums[right])
                left = mid + 1; // means minimum will be in the next half
            else {
                min = Math.min(min, nums[mid]);
                right = mid - 1;
            }

        }
        return min;
    }

}
