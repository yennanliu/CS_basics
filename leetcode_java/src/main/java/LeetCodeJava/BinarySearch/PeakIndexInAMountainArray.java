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
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 928.3K
 * Submissions
 * 1.4M
 * Acceptance Rate
 * 67.9%
 * Topics
 * Companies
 * Similar Questions
 * Discussion (183)
 *
 *
 */
public class PeakIndexInAMountainArray {

    // V0
    // TODO: fix below:
//    public int peakIndexInMountainArray(int[] arr) {
//
//        // edge
//        int maxIdx = -1;
//        int maxVal = -1;
//        if (arr.length == 3){
//            for(int i = 0; i < arr.length; i++){
//                if(arr[i] > maxVal){
//                    maxVal = arr[i];
//                    maxIdx = i;
//                }
//            }
//            return maxIdx;
//        }
//
//        // binary search
//        int l = 0;
//        int r = arr.length - 1;
//        int mid = (l + r) / 2;
//        while (r >= l && r >= 0){
//
//            mid = (l + r) / 2;
//
//            // case 1)  cur > left and cur > right (find peak)
//            if (arr[mid] > arr[mid-1] && arr[mid] > arr[mid+1]){
//                return mid;
//            }
//            // Exp 1 : [0,0,0, 3,2,1,0] -> 1
//            // case 2) cur < left && cur < left most (left is increasing order)
//            else if (arr[mid] >= arr[mid-1] && arr[mid] >= arr[l]){
//                l = mid + 1;
//            }
//            // case 3) cur < right and cur > right most (right is decreasing order ?)
//            else if  (arr[mid] >= arr[mid+1] && arr[mid] >= arr[r]){
//                r = mid - 1;
//            }
//        }
//
//        return mid;
//    }

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

    // V1

    // V2
}
