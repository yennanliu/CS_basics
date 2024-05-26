package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/search-in-rotated-sorted-array/

public class SearchInRotatedSortedArray {

    // V0
    // IDEA : BINARY SEARCH
    // CASE 1) sub array left is sorted
    // CASE 2) sub array right is sorted
    public int search(int[] nums, int target) {

        if (nums.length == 0 || nums.equals(null)){
            return -1;
        }

        int l = 0;
        int r = nums.length - 1;

        while (r >= l){
            int mid = (l + r) / 2;
            int cur = nums[mid];
            if (cur == target){
                return mid;
            }

//            else if (cur > nums[0] && target > cur){
//                //l = mid + 1;
//                nums_sub = Arrays.copyOfRange(nums, mid+1, nums.length-1);
//            }else if (cur > nums[0] && target < cur){
//                //r = mid - 1;
//                nums_sub = Arrays.copyOfRange(nums, l, mid-1);
//            }
//            else if (cur < nums[0] && target > cur){
//                //r = mid - 1;
//                nums_sub = Arrays.copyOfRange(nums, mid+1, nums.length-1);
//            }else{
//                nums_sub = Arrays.copyOfRange(nums, l, mid-1);
//            }
//
//            return _binarySearch(nums_sub, target);

            /** NOTE !!! we use below logic for dealing with
             *  cases such as
             *      - 1) left sub array is sorted
             *          - nums[mid] >= nums[l] && target < nums[mid]
             *          - nums[mid] < nums[l] || target > nums[mid]
             *      - 2) right sub array is sorted
             *          - target <= nums[r] && target > nums[mid]
             *          - target > nums[r] || target < nums[mid]
             *
             */
            // Case 1: subarray on mid's left is sorted
            /** NOTE !!! we compare mid with left, instead of 0 idx element */
            else if (nums[mid] >= nums[l]) {
                if (target >= nums[l] && target < nums[mid]) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }

            // Case 2: subarray on mid's right is sorted
            else {
                if (target <= nums[r] && target > nums[mid]) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }

        }

        return -1;
    }

    // V0'
    // IDEA : BINARY SEARCH (fixed by GPT)
    public int search_0(int[] nums, int target) {

        if (nums.length == 0){
            return -1;
        }

        int l = 0;
        int r = nums.length - 1;

        // NOTE : "r >= l" as binary search condition
        while (r >= l){

            int mid = (l + r) / 2;
            int cur = nums[mid];

            // case 1)
            if (cur == target){
                return mid;
            }

            /**
             *  case 2) mid is pivot,  left half is sorted.
             *  left array is sorted (increasing)
             */
            else if (nums[mid] >= nums[l]){
                /**
                 *  NOTE !!!
                 *
                 *  since left sub array is sorted (increasing)
                 *  the only condition we can use is : left sub array
                 *  e.g. : check if target is bigger than left boundary or not
                 */
                /**
                 *
                 *  NOTE !!!
                 *  below is WRONG!!! (right sub array may have "pivot",
                 *  can't use right sub array  as condition
                 */
//                if (target > nums[mid] && nums[r] >= target){
//                    l = mid + 1;
//                }else{
//                    r = mid - 1; // NOTE !!!! not "r = mid"
//                }
                if (target >= nums[l] && target < cur) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            /**
             *  Case 3): right half is sorted (increasing)
             */
            /**
             *  NOTE !!!
             *
             *  since right sub array is sorted (increasing)
             *  the only condition we can use is : right sub array
             *  e.g. : check if target is smaller than right boundary or not
             */
            else{
                if (target > nums[mid] && target <= nums[r]){
                    l = mid + 1;
                }else{
                    r = mid - 1; // NOTE !!! not " r = mid"
                }
            }
        }

        return -1;
    }


    // V1
    // IDEA : Find Pivot Index + Binary Search
    // https://leetcode.com/problems/search-in-rotated-sorted-array/editorial/
    public int search_2(int[] nums, int target) {
        int n = nums.length;
        int left = 0, right = n - 1;

        // Find the index of the pivot element (the smallest element)
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] > nums[n - 1]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Binary search over elements on the pivot element's left
        int answer = binarySearch(nums, 0, left - 1, target);
        if (answer != -1) {
            return answer;
        }

        // Binary search over elements on the pivot element's right
        return binarySearch(nums, left, n - 1, target);
    }

    // Binary search over an inclusive range [left_boundary ~ right_boundary]
    private int binarySearch(int[] nums, int leftBoundary, int rightBoundary, int target) {
        int left = leftBoundary, right = rightBoundary;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    // V2
    // IDEA : Find Pivot Index + Binary Search with Shift
    // https://leetcode.com/problems/search-in-rotated-sorted-array/editorial/
    public int search_3(int[] nums, int target) {
        int n = nums.length;
        int left = 0, right = n - 1;

        // Find the index of the pivot element (the smallest element)
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] > nums[n - 1]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return shiftedBinarySearch(nums, left, target);
    }

    // Shift elements in a circular manner, with the pivot element at index 0.
    // Then perform a regular binary search
    private int shiftedBinarySearch(int[] nums, int pivot, int target) {
        int n = nums.length;
        int shift = n - pivot;
        int left = (pivot + shift) % n;
        int right = (pivot - 1 + shift) % n;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[(mid - shift + n) % n] == target) {
                return (mid - shift + n) % n;
            } else if (nums[(mid - shift + n) % n] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    // V3
    // IDEA : One Binary Search
    // https://leetcode.com/problems/search-in-rotated-sorted-array/editorial/
    public int search_4(int[] nums, int target) {
        int n = nums.length;
        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Case 1: find target
            if (nums[mid] == target) {
                return mid;
            }

            // Case 2: subarray on mid's left is sorted
            else if (nums[mid] >= nums[left]) {
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }

            // Case 3: subarray on mid's right is sorted
            else {
                if (target <= nums[right] && target > nums[mid]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }

        return -1;
    }

}
