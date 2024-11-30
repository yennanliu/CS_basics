package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/description/
/**
 * 34. Find First and Last Position of Element in Sorted Array
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.
 *
 * If target is not found in the array, return [-1, -1].
 *
 * You must write an algorithm with O(log n) runtime complexity.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [5,7,7,8,8,10], target = 8
 * Output: [3,4]
 * Example 2:
 *
 * Input: nums = [5,7,7,8,8,10], target = 6
 * Output: [-1,-1]
 * Example 3:
 *
 * Input: nums = [], target = 0
 * Output: [-1,-1]
 *
 *
 * Constraints:
 *
 * 0 <= nums.length <= 105
 * -109 <= nums[i] <= 109
 * nums is a non-decreasing array.
 * -109 <= target <= 109
 *
 */
public class FindFirstAndLastPositionOfElementInSortedArray {

    // V0
    // TODO : implement
//    public int[] searchRange(int[] nums, int target) {
//
//    }

    // V1
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/solutions/14734/easy-java-o-logn-solution/
    public int[] searchRange_1(int[] nums, int target) {
        int[] result = new int[2];
        result[0] = findFirst(nums, target);
        result[1] = findLast(nums, target);
        return result;
    }

    private int findFirst(int[] nums, int target) {
        int idx = -1;
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;

            /** NOTE !!!
             *
             * 1) nums[mid] >= target (find right boundary)
             * 2) we put equals condition below  (nums[mid] == target)
             */
            if (nums[mid] >= target) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
            if (nums[mid] == target)
                idx = mid;
        }
        return idx;
    }

    private int findLast(int[] nums, int target) {
        int idx = -1;
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            /** NOTE !!!
             *
             * 1) nums[mid] <= target (find left boundary)
             * 2) we put equals condition below  (nums[mid] == target)
             */
            if (nums[mid] <= target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
            if (nums[mid] == target)
                idx = mid;
        }
        return idx;
    }

    // V2
    // IDEA : Binary Tree
    // https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/solutions/14701/a-very-simple-java-solution-with-only-one-binary-search-algorithm/
    public int[] searchRange_2(int[] A, int target) {
        int start = firstGreaterEqual(A, target);
        if (start == A.length || A[start] != target) {
            return new int[]{-1, -1};
        }
        return new int[]{start, firstGreaterEqual(A, target + 1) - 1};
    }

    //find the first number that is greater than or equal to target.
    //could return A.length if target is greater than A[A.length-1].
    //actually this is the same as lower_bound in C++ STL.
    private static int firstGreaterEqual(int[] A, int target) {
        int low = 0, high = A.length;
        while (low < high) {
            int mid = low + ((high - low) >> 1);
            //low <= mid < high
            if (A[mid] < target) {
                low = mid + 1;
            } else {
                //should not be mid-1 when A[mid]==target.
                //could be mid even if A[mid]>target because mid<high.
                high = mid;
            }
        }
        return low;
    }

    // V3
    // IDEA : binary Search
    // https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/solutions/5378191/video-binary-search-solution/
    public int[] searchRange_3(int[] nums, int target) {
        int[] result = { -1, -1 };
        int left = binarySearch(nums, target, true);
        int right = binarySearch(nums, target, false);
        result[0] = left;
        result[1] = right;
        return result;
    }

    private int binarySearch(int[] nums, int target, boolean isSearchingLeft) {
        int left = 0;
        int right = nums.length - 1;
        int idx = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                idx = mid;
                if (isSearchingLeft) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        return idx;
    }

    // V4
    // IDEA : binary Search (gpt)
    public int[] searchRange_4(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }

        int start = findBoundary(nums, target, true);  // Find the left boundary
        if (start == -1) {
            return new int[]{-1, -1}; // Target not found
        }

        int end = findBoundary(nums, target, false); // Find the right boundary

        return new int[]{start, end};
    }

    private int findBoundary(int[] nums, int target, boolean findStart) {
        int left = 0;
        int right = nums.length - 1;
        int boundary = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                boundary = mid;
                if (findStart) {
                    right = mid - 1; // Narrow down to the left side
                } else {
                    left = mid + 1; // Narrow down to the right side
                }
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return boundary;
    }

}
