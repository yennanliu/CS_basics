package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/

import java.util.Arrays;

public class FindMinimumInRotatedSortedArray {


    // V0
    // IDEA : BINARY SEARCH (CLOSED BOUNDARY)
    /**
     *
     * NOTE !!! the turing point (rotation point)
     *          -> is ALWAYS the area that min element located (may at at i, i+1, i-1 place)
     *
     *
     *    case 1) check turing point
     *    case 2) check if left / right sub array is in Ascending order
     */
    public int findMin(int[] nums) {

        if (nums.length == 0 || nums.equals(null)){
            return 0;
        }

        // check if already in ascending order
        if (nums[0] < nums[nums.length - 1]){
            return nums[0];
        }

        if (nums.length == 1){
            return nums[0];
        }

        // binary search
        int l = 0;
        int r = nums.length - 1;

        /** NOTE !!! here : r >= l (closed boundary) */
        while (r >= l){
            int mid = (l + r) / 2;

            /** NOTE !!! we found turing point, and it's the minimum element  (idx = mid + 1) */
            if (nums[mid] > nums[mid+1]){
                return nums[mid+1];

           /** NOTE !!! we found turing point, and it's the minimum element (idx = mid -1) */
            }else if (nums[mid-1] > nums[mid]){
                return nums[mid];

            /** NOTE !!! compare mid with left, and this means left sub array is ascending order,
             *           so minimum element MUST in right sub array
             */
            }else if (nums[mid] > nums[l]){
                l = mid + 1;

            /** NOTE !!! compare mid with left, and this means right sub array is ascending order,
            *            so minimum element MUST in left sub array
            */
            }else{
                r = mid - 1;
            }

        }

        return nums[l];
    }

    // V0'
    // IDEA : BINARY SEARCH (OPEN BOUNDARY)
    public int findMin_2(int[] nums) {

        if (nums.length == 0 || nums.equals(null)){
            return 0;
        }

        // already in ascending order
        if (nums[0] < nums[nums.length - 1]){
            return nums[0];
        }

        // binary search
        int l = 0;
        int r = nums.length - 1;
        /** NOTE !!! here : r > l (open boundary) */
        while (r > l){
            int mid = (l + r) / 2;
            if (nums[mid] > nums[r]){
                /** NOTE !!! here : (open boundary) */
                l = mid + 1;
            }else{
                /** NOTE !!! here : (open boundary) */
                r = mid;
            }
        }

        return nums[l];
    }

    // V1
    // IDEA : BINARY SEARCH (CLOSED BOUNDARY)
    // https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/editorial/
    public int findMin_3(int[] nums) {
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
