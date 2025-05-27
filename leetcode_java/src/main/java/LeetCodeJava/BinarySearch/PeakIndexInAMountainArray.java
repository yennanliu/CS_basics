package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/peak-index-in-a-mountain-array/description/
// https://leetcode.cn/problems/peak-index-in-a-mountain-array/description/
/**
 * 852. Peak Index in a Mountain Array
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an integer mountain array arr of length n where the values increase to a peak element and then decrease.
 *
 * Return the index of the peak element.
 *
 * Your task is to solve it in O(log(n)) time complexity.
 *
 *
 *
 * Example 1:
 *
 * Input: arr = [0,1,0]
 *
 * Output: 1
 *
 * Example 2:
 *
 * Input: arr = [0,2,1,0]
 *
 * Output: 1
 *
 * Example 3:
 *
 * Input: arr = [0,10,5,2]
 *
 * Output: 1
 *
 *
 *
 * Constraints:
 *
 * 3 <= arr.length <= 105
 * 0 <= arr[i] <= 106
 * arr is guaranteed to be a mountain array.
 *
 */
public class PeakIndexInAMountainArray {

    // V0
    // IDEA: BINARY SEARCH
    // NOTE !!! `int r = arr.length - 2; // End at length - 2 to avoid checking arr[arr.length]`
    public int peakIndexInMountainArray(int[] arr) {
        if (arr == null || arr.length < 3) {
            return -1; // Return -1 if the array length is less than 3
        }

        // Binary search
        int l = 1; // Start from 1 to avoid checking arr[-1]
        /**
         *  NOTE !!!
         *
         *   int r = arr.length - 2;
         *    -> so can avoid `out of boundary`
         *
         */
        int r = arr.length - 2; // End at length - 2 to avoid checking arr[arr.length]

        while (r >= l) {
            int mid = l + (r - l) / 2;

            // Check if mid is the peak
            if (arr[mid] > arr[mid - 1] && arr[mid] > arr[mid + 1]) {
                return mid;
            }
            // If the element at mid is smaller than the next element, peak is on the right
            else if (arr[mid] < arr[mid + 1]) {
                l = mid + 1;
            }
            // Otherwise, peak is on the left
            else {
                r = mid - 1;
            }
        }

        return -1; // Shouldn't happen in a valid mountain array
    }

    // V0-0-1
    // IDEA: BINARY SEARCH
    // NOTE !!! `r = arr.length - 1;`
    public int peakIndexInMountainArray_0_0_1(int[] arr) {
        if (arr == null || arr.length < 3) {
            return -1; // Return -1 if the array length is less than 3
        }

        // Binary search
        int l = 1; // Start from 1 to avoid checking arr[-1]
        int r = arr.length - 1;

        while (r >= l) {
            int mid = l + (r - l) / 2;

            // Check if mid is the peak
            /**
             *  NOTE !!!
             *
             *   mid < arr.length-1
             *    -> so can avoid `out of boundary`
             *
             */
            if (mid < arr.length-1 && arr[mid] > arr[mid - 1] && arr[mid] > arr[mid + 1]) {
                return mid;
            }
            // If the element at mid is smaller than the next element, peak is on the right
            else if (arr[mid] < arr[mid + 1]) {
                l = mid + 1;
            }
            // Otherwise, peak is on the left
            else {
                r = mid - 1;
            }
        }

        return -1; // Shouldn't happen in a valid mountain array
    }


    // V0-1
    // IDEA: BINARY SEARCH
    // TODO: validate & fix
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Binary_Search/peak-index-in-a-mountain-array.py#L55
    public int peakIndexInMountainArray_0_1(int[] arr) {
        if (arr.length < 3) {
            return -1; // Return -1 if the array length is less than 3
        }

        // Binary search
        int l = 0;
        int r = arr.length - 1;
        //int r = arr.length - 2;

        while (r >= l) {
            int mid = l + (r - l) / 2;

            // Check if mid is the peak
            if (arr[mid] > arr[mid - 1] && arr[mid] > arr[mid + 1]) {
                return mid;
            }
            // If the element at mid is smaller than the next element, peak is on the right
            else if (arr[mid] < arr[mid + 1]) {
                l = mid + 1;
            }
            // Otherwise, peak is on the left
            else {
                r = mid - 1;
            }
        }
        return -1; // Return -1 if no peak is found (though this case shouldn't happen with a valid
        // mountain array)
    }

    // V0-2
    // IDEA: BINARY SEARCH + `increasing array`, `decreasing array` (fixed by gpt)
    public int peakIndexInMountainArray_0_2(int[] arr) {
        if (arr == null || arr.length < 3) {
            throw new IllegalArgumentException("Input must be a valid mountain array with length >= 3");
        }

        int l = 0;
        int r = arr.length - 1;

        while (l < r) {
            int mid = l + (r - l) / 2;

            if (arr[mid] < arr[mid + 1]) {
                // Peak is to the right
                l = mid + 1;
            } else {
                // Peak is at mid or to the left
                r = mid;
            }
        }

        return l; // or return r; since l == r
    }

    // V0-3
    // IDEA: BINARY SEARCH + `increasing array`, `decreasing array` (fixed by gpt)
    public int peakIndexInMountainArray_0_3(int[] arr) {
        if (arr == null || arr.length < 3) {
            throw new IllegalArgumentException("Input must be a valid mountain array with length >= 3");
        }

        int l = 0;
        int r = arr.length - 1;

        while (r >= l) {
            int mid = l + (r - l) / 2;

            // Edge case safety: mid > 0 and mid < arr.length - 1
            if (mid > 0 && mid < arr.length - 1) {
                if (arr[mid] > arr[mid - 1] && arr[mid] > arr[mid + 1]) {
                    return mid;
                } else if (arr[mid] > arr[mid - 1]) {
                    // Ascending — move right
                    l = mid + 1;
                } else {
                    // Descending — move left
                    r = mid - 1;
                }
            } else if (mid == 0) {
                // In a valid mountain array, peak can't be at index 0
                l = mid + 1;
            } else if (mid == arr.length - 1) {
                // In a valid mountain array, peak can't be at last index
                r = mid - 1;
            }
        }

        // Should not reach here in a valid mountain array
        return -1;
    }

    // V1-1
    // https://leetcode.com/problems/peak-index-in-a-mountain-array/editorial/
    // IDEA : LINEAR SCAN
    public int peakIndexInMountainArray_1_1(int[] arr) {
        int i = 0;
        while (arr[i] < arr[i + 1]) {
            i++;
        }
        return i;
    }

    // V1-2
    // https://leetcode.com/problems/peak-index-in-a-mountain-array/editorial/
    // IDEA : Binary Search
    public int peakIndexInMountainArray_1_2(int[] arr) {
        int l = 0, r = arr.length - 1, mid;
        while (l < r) {
            mid = (l + r) / 2;
            if (arr[mid] < arr[mid + 1])
                l = mid + 1;
            else
                r = mid;
        }
        return l;
    }

    // V2
    // https://leetcode.com/problems/peak-index-in-a-mountain-array/solutions/6237785/binary-search-by-retr0sec-si97/
    // IDEA: BINARY SEARCH
    public int peakIndexInMountainArray_2(int[] arr) {

        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (mid >= 1 && mid <= arr.length - 1 && arr[mid] < arr[mid - 1]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return right;
    }

    // V3
    // https://leetcode.com/problems/peak-index-in-a-mountain-array/solutions/6235886/check-out-this-solution-if-you-want-most-si4s/
    // IDEA: BINARY SEARCH
    public int peakIndexInMountainArray_3(int[] arr) {
        int start = 0;
        int end = arr.length - 1;
        while (start != end) {
            int mid = start + (end - start) / 2;
            if (arr[mid] < arr[mid + 1]) {
                start = mid + 1;
            } else if (arr[mid] > arr[mid + 1]) {
                end = mid;
            }
        }
        return start;
    }

}
