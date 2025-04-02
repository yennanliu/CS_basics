package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
// https://youtu.be/nIVW4P8b1VA?si=AMhTJOUhDziBz-CV
/**
 * 153. Find Minimum in Rotated Sorted Array
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array nums = [0,1,2,4,5,6,7] might become:
 * <p>
 * [4,5,6,7,0,1,2] if it was rotated 4 times.
 * [0,1,2,4,5,6,7] if it was rotated 7 times.
 * Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].
 * <p>
 * Given the sorted rotated array nums of unique elements, return the minimum element of this array.
 * <p>
 * You must write an algorithm that runs in O(log n) time.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: nums = [3,4,5,1,2]
 * Output: 1
 * Explanation: The original array was [1,2,3,4,5] rotated 3 times.
 * Example 2:
 * <p>
 * Input: nums = [4,5,6,7,0,1,2]
 * Output: 0
 * Explanation: The original array was [0,1,2,4,5,6,7] and it was rotated 4 times.
 * Example 3:
 * <p>
 * Input: nums = [11,13,15,17]
 * Output: 11
 * Explanation: The original array was [11,13,15,17] and it was rotated 4 times.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * n == nums.length
 * 1 <= n <= 5000
 * -5000 <= nums[i] <= 5000
 * All the integers of nums are unique.
 * nums is sorted and rotated between 1 and n times.
 */

public class FindMinimumInRotatedSortedArray {

    // V0
    // IDEA : BINARY SEARCH (CLOSED BOUNDARY)
    // https://youtu.be/nIVW4P8b1VA?si=AMhTJOUhDziBz-CV
    /**
     *  NOTE !!!
     *
     *  key : check current `mid point` is at  `left part` or `right part`
     *  if `at left part`
     *   -> nums[l] ~ nums[mid] is in INCREASING order
     *   -> need to search `RIGHT part`, since right part is ALWAYS SMALLER then left part
     *
     *  else, `at right part`
     *   -> need to search `LEFT part`
     */
    public int findMin(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        int res = nums[0];

        /** NOTE !!! closed boundary */
        while (l <= r) {

            /** NOTE !!!
             *
             *  below edge case check is necessary
             *
             *  NOTE !!! -> compare min between LEFT boundary (nums[l]) and res
             *
             *  and jump out while loop directly if the condition (nums[l] < nums[r]) is met
             */
            // edge case : is array already in increasing order (e.g. [1,2,3,4,5])
            if (nums[l] < nums[r]) {
                res = Math.min(res, nums[l]);
                //break;
                return res;
            }

            int m = l + (r - l) / 2;
            res = Math.min(res, nums[m]);

            // case 1) mid point is at `LEFT part`
            // e.g. [3,4,5,1,2]
            if (nums[m] >= nums[l]) {
                l = m + 1;
            }
            // case 2) mid point is at `RIGHT part`
            // e.g. [5,1,2,3,4]
            else {
                r = m - 1;
            }
        }
        return res;
    }

    // V0_1
    // IDEA : BINARY SEARCH (CLOSED BOUNDARY)
    public int findMin_0_1(int[] nums) {

        // edge case 1): if len == 1
        if (nums.length == 1) {
            return nums[0];
        }

        // edge case 1): array already in ascending order
        int left = 0;
        int right = nums.length - 1;
        if (nums[right] > nums[0]) {
            return nums[0];
        }

        // binary search
        while (right >= left) {
            int mid = (left + right) / 2;
            // turning point case 1
            if (nums[mid] > nums[mid + 1]) {
                return nums[mid + 1];
            }
            // TODO : check why below
            // turning point case 2
            // if (nums[mid] > nums[mid-1]){
            //     return nums[mid - 1];
            // }
            if (nums[mid - 1] > nums[mid]) {
                return nums[mid];
            }
            // left sub array is ascending
            if (nums[mid] > nums[0]) {
                left = mid + 1;
            }
            // right sub array is ascending
            else {
                right = mid - 1;
            }
        }

        return -1;
    }

    // V0-2
    // IDEA : BINARY SEARCH (CLOSED BOUNDARY)

    /**
     * NOTE !!! the turing point (rotation point)
     * -> it's ALWAYS the place that min element located (may at at i, i+1, i-1 place)
     * <p>
     * case 1) check turing point
     * case 2) check if left / right sub array is in Ascending order
     */
    public int findMin_0_2(int[] nums) {

        if (nums.length == 0 || nums.equals(null)) {
            return 0;
        }

        // check if already in ascending order
        if (nums[0] < nums[nums.length - 1]) {
            return nums[0];
        }

        if (nums.length == 1) {
            return nums[0];
        }

        // binary search
        int l = 0;
        int r = nums.length - 1;

        /** NOTE !!! here : r >= l (closed boundary) */
        while (r >= l) {
            int mid = (l + r) / 2;

            /** NOTE !!! BELOW 4 CONDITIONS
             *
             *  - turning point, check mid, mid+1
             *  - turning point, check mid, mid-1
             *  - left sub array is sorted (increasing order)
             *  - right sub array is sorted (increasing order)
             *
             */

            /** NOTE !!! we found turing point, and it's the minimum element  (idx = mid + 1) */
            if (nums[mid] > nums[mid + 1]) {
                return nums[mid + 1];

                /** NOTE !!! we found turing point, and it's the minimum element (idx = mid -1) */
            } else if (nums[mid - 1] > nums[mid]) {
                return nums[mid];

                /** NOTE !!! compare mid with left, and this means left sub array is ascending order,
                 *           so minimum element MUST in right sub array
                 */
            } else if (nums[mid] > nums[l]) {
                l = mid + 1;

                /** NOTE !!! compare mid with left, and this means right sub array is ascending order,
                 *            so minimum element MUST in left sub array
                 */
            } else {
                r = mid - 1;
            }

        }

        return nums[l];
    }

    // V0-3
    // IDEA : BINARY SEARCH (OPEN BOUNDARY)
    public int findMin_0_3(int[] nums) {

        if (nums.length == 0 || nums.equals(null)) {
            return 0;
        }

        // already in ascending order
        if (nums[0] < nums[nums.length - 1]) {
            return nums[0];
        }

        // binary search
        int l = 0;
        int r = nums.length - 1;
        /** NOTE !!! here : r > l (open boundary) */
        while (r > l) {
            int mid = (l + r) / 2;
            if (nums[mid] > nums[r]) {
                /** NOTE !!! here : (open boundary) */
                l = mid + 1;
            } else {
                /** NOTE !!! here : (open boundary) */
                r = mid;
            }
        }

        return nums[l];
    }

    // V1
    // https://youtu.be/nIVW4P8b1VA?si=AMhTJOUhDziBz-CV
    // IDEA : BINARY SEARCH (CLOSED BOUNDARY)
    // https://neetcode.io/solutions/find-minimum-in-rotated-sorted-array
    public int findMin_1(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        int res = nums[0];

        while (l <= r) {
            if (nums[l] < nums[r]) {
                res = Math.min(res, nums[l]);
                break;
            }

            int m = l + (r - l) / 2;
            res = Math.min(res, nums[m]);
            if (nums[m] >= nums[l]) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return res;
    }

    // V2
    // IDEA : BINARY SEARCH (CLOSED BOUNDARY)
    // https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/editorial/
    public int findMin_2(int[] nums) {
        // If the list has just one element then return that element.
        if (nums.length == 1) {
            return nums[0];
        }

        // initializing left and right pointers.
        int left = 0, right = nums.length - 1;

        // if the last element is greater than the first element then there is no
        // rotation.
        // e.g. 1 < 2 < 3 < 4 < 5 < 7. Already sorted array.
        // Hence the smallest element is first element. A[0]
        if (nums[right] > nums[0]) {
            return nums[0];
        }

        // Binary search way
        while (right >= left) {
            // Find the mid element
            int mid = left + (right - left) / 2;

            // if the mid element is greater than its next element then mid+1 element is the
            // smallest
            // This point would be the point of change. From higher to lower value.
            if (nums[mid] > nums[mid + 1]) {
                return nums[mid + 1];
            }

            // if the mid element is lesser than its previous element then mid element is
            // the smallest
            if (nums[mid - 1] > nums[mid]) {
                return nums[mid];
            }

            // if the mid elements value is greater than the 0th element this means
            // the least value is still somewhere to the right as we are still dealing with
            // elements
            // greater than nums[0]
            if (nums[mid] > nums[0]) {
                left = mid + 1;
            } else {
                // if nums[0] is greater than the mid value then this means the smallest value
                // is somewhere to
                // the left
                right = mid - 1;
            }
        }
        return Integer.MAX_VALUE;
    }

}
