package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/squares-of-a-sorted-array/description/

import java.util.*;

/**
 * 977. Squares of a Sorted Array
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums sorted in non-decreasing order, return an array of the squares of each number sorted in non-decreasing order.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [-4,-1,0,3,10]
 * Output: [0,1,9,16,100]
 * Explanation: After squaring, the array becomes [16,1,0,9,100].
 * After sorting, it becomes [0,1,9,16,100].
 * Example 2:
 *
 * Input: nums = [-7,-3,2,3,11]
 * Output: [4,9,9,49,121]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -104 <= nums[i] <= 104
 * nums is sorted in non-decreasing order.
 *
 *
 * Follow up: Squaring each element and sorting the new array is very trivial, could you find an O(n) solution using a different approach?
 *
 *
 */
public class SquaresOfASortedArray {

    // V0
    // IDEA: SORT
    public int[] sortedSquares(int[] nums) {
        // edge

        List<Integer> list = new ArrayList<>();
        for (int x : nums) {
            list.add(x * x);
        }
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }

        return res;
    }

    // V1
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/squares-of-a-sorted-array/solutions/6149748/using-two-pointers-by-niits-0c19/
    public int[] sortedSquares_1(int[] nums) {
        int[] res = new int[nums.length];
        int left = 0;
        int right = nums.length - 1;

        for (int i = nums.length - 1; i >= 0; i--) {
            if (Math.abs(nums[left]) > Math.abs(nums[right])) {
                res[i] = nums[left] * nums[left];
                left++;
            } else {
                res[i] = nums[right] * nums[right];
                right--;
            }
        }
        return res;
    }


    // V2-1
    // IDEA: Radix Sort
    // https://leetcode.com/problems/squares-of-a-sorted-array/solutions/4807685/beats-100-users-cjavapythonjavascript-3-ax3bb/
    public int getMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public void countSort(int[] arr, int exp) {
        int[] output = new int[arr.length];
        int[] count = new int[10];

        for (int i = 0; i < arr.length; i++) {
            count[(arr[i] / exp) % 10]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        for (int i = 0; i < arr.length; i++) {
            arr[i] = output[i];
        }
    }

    public void radixSort(int[] arr) {
        int max = getMax(arr);

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countSort(arr, exp);
        }
    }

    public int[] sortedSquares_2_1(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            nums[i] = nums[i] * nums[i];
        }
        radixSort(nums);
        return nums;
    }


    // V2-2
    // IDEA: Sort Function
    // https://leetcode.com/problems/squares-of-a-sorted-array/solutions/4807685/beats-100-users-cjavapythonjavascript-3-ax3bb/
    public int[] sortedSquares_2_2(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            nums[i] = nums[i] * nums[i];
        }
        Arrays.sort(nums);
        return nums;
    }


    // V2-3
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/squares-of-a-sorted-array/solutions/4807685/beats-100-users-cjavapythonjavascript-3-ax3bb/
    public int[] sortedSquares_2_3(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        int start = 0, end = n - 1;
        for (int i = n - 1; i >= 0; i--) {
            if (Math.abs(nums[start]) >= Math.abs(nums[end])) {
                ans[i] = nums[start] * nums[start];
                start++;
            } else {
                ans[i] = nums[end] * nums[end];
                end--;
            }
        }
        return ans;
    }


    // V3



}
