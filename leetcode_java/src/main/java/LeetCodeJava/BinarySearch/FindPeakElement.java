package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-peak-element/

import java.util.Arrays;
import java.util.OptionalInt;

public class FindPeakElement {

//    public int findPeakElement(int[] nums) {
//
//        if (nums.length == 1){
//            return 0;
//        }
//
//        if (nums.length == 2){
//            int _first = nums[0];
//            int _second = nums[1];
//            if (_second > _first){
//                return 1;
//            }
//            return 0;
//        }
//
//        if (nums.length == 3){
//            return 1;
//        }
//
//        int l = 0;
//        int r = nums.length - 1;
//
//        while (r >= l){
//
//            int mid =  l + (r - l) / 2;
//            int mid_val = nums[mid];
//            int mid_left_val = nums[mid-1];
//            int mid_right_val = nums[mid+1];
//
//            System.out.println("l = " + l + " r = " + r + " mid = " + mid);
//
//            if (mid_val > mid_left_val && mid > mid_right_val){
//                return mid;
//            }
//
//            if (mid_val > mid_right_val){
//                r = mid-1;
//            }else{
//                l = mid+1;
//            }
//
//        }
//
//        return 0;
//    }

    // V1
    // IDEA: LINEAR SCAN
    // https://leetcode.com/problems/find-peak-element/editorial/

    // V2
    // IDEA: RECURSIVE BINARY SEARCH
    // https://leetcode.com/problems/find-peak-element/editorial/
    // NOTE : ONLY have to compare index i with index i + 1 (its right element)
    //        ; otherwise, i-1 already returned as answer
    public int findPeakElement_2(int[] nums) {
        return search(nums, 0, nums.length - 1);
    }
    public int search(int[] nums, int l, int r) {
        if (l == r)
            return l;
        int mid = (l + r) / 2;
        if (nums[mid] > nums[mid + 1])
            return search(nums, l, mid);
        return search(nums, mid + 1, r);
    }

    // V3
    // IDEA: ITERATIVE BINARY SEARCH
    // https://leetcode.com/problems/find-peak-element/editorial/
    public int findPeakElement_3(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] > nums[mid + 1])
                r = mid;
            else
                l = mid + 1;
        }
        return l;
    }

}
