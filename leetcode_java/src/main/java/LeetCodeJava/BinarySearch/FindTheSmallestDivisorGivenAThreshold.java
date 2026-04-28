package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-the-smallest-divisor-given-a-threshold/description/

import java.util.Arrays;

/**
 *  1283. Find the Smallest Divisor Given a Threshold
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array of integers nums and an integer threshold, we will choose a positive integer divisor, divide all the array by it, and sum the division's result. Find the smallest divisor such that the result mentioned above is less than or equal to threshold.
 *
 * Each result of the division is rounded to the nearest integer greater than or equal to that element. (For example: 7/3 = 3 and 10/2 = 5).
 *
 * The test cases are generated so that there will be an answer.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,5,9], threshold = 6
 * Output: 5
 * Explanation: We can get a sum to 17 (1+2+5+9) if the divisor is 1.
 * If the divisor is 4 we can get a sum of 7 (1+1+2+3) and if the divisor is 5 the sum will be 5 (1+1+1+2).
 * Example 2:
 *
 * Input: nums = [44,22,33,11,1], threshold = 5
 * Output: 44
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 104
 * 1 <= nums[i] <= 106
 * nums.length <= threshold <= 106
 *
 *
 *
 */
public class FindTheSmallestDivisorGivenAThreshold {

    // V0
//    public int smallestDivisor(int[] nums, int threshold) {
//
//    }


    // V1-1
    // IDEA: BINARY SEARCH (GPT)
    public int smallestDivisor_1_1(int[] nums, int threshold) {
        if (nums == null || nums.length == 0)
            return 0;

        int l = 1;
        int r = 0;

        // find max value
        for (int x : nums) {
            r = Math.max(r, x);
        }

        int ans = r;

        while (l <= r) {
            int mid = l + (r - l) / 2;

            if (canDivide(nums, threshold, mid)) {
                ans = mid; // valid divisor
                r = mid - 1; // try smaller
            } else {
                l = mid + 1; // need larger divisor
            }
        }

        return ans;
    }

    private boolean canDivide(int[] nums, int threshold, int d) {
        int sum = 0;

        for (int val : nums) {
            // ceil(val / d) without using double
            sum += (val + d - 1) / d;

            if (sum > threshold)
                return false; // early stop
        }

        return sum <= threshold;
    }



    // V1-2
    // IDEA: BINARY SEARCH (GEMINI)
    public int smallestDivisor_1_2(int[] nums, int threshold) {
        // 1. Define the search range
        int left = 1;
        int right = 0;
        for (int x : nums) {
            right = Math.max(right, x);
        }

        int ans = right;

        // 2. Binary Search on the result (the divisor)
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (calculateSum(nums, mid) <= threshold) {
                ans = mid; // Possible, try to find a smaller divisor
                right = mid - 1;
            } else {
                left = mid + 1; // Sum too large, need a larger divisor
            }
        }
        return ans;
    }

    private int calculateSum(int[] nums, int divisor) {
        int sum = 0;
        for (int val : nums) {
            // Ceiling division: (val + divisor - 1) / divisor
            sum += (val + divisor - 1) / divisor;
        }
        return sum;
    }

    

    // V2
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/find-the-smallest-divisor-given-a-threshold/solutions/8104731/binary-search-easy-clean-readable-code-b-e86y/
    public int smallestDivisor_2(int[] nums, int threshold) {
        int n = nums.length;

        int low = 1;
        int high = Arrays.stream(nums).max().getAsInt();

        while (low <= high) {
            int mid = low + (high - low) / 2;

            int sum = getSum(nums, mid);

            if (sum <= threshold) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return low;
    }

    public int getSum(int[] arr, int divisor) {
        int n = arr.length;
        long sum = 0;

        for (int i = 0; i < n; i++) {
            sum = sum + (int) Math.ceil((double) arr[i] / divisor);
        }

        return (int) sum;
    }


    // V3
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/find-the-smallest-divisor-given-a-threshold/solutions/8095251/smallest-divisor-binary-search-on-answer-wecq/
    public int smallestDivisor_3(int[] nums, int threshold) {
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            max = Math.max(max, num);
        }

        // Optimization: If the threshold allows for the largest possible sum,
        // the smallest divisor is 1.
        // Optimization: If threshold is exactly the number of elements,
        // each element must result in 1, so the divisor must be the max element.
        if (nums.length == threshold)
            return max;

        int left = 1, right = max;
        int ans = max;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (isValid(nums, mid, threshold)) {
                ans = mid; // Potential answer, look for smaller ones
                right = mid - 1;
            } else {
                left = mid + 1; // Sum too high, need larger divisor
            }
        }

        return ans;
    }

    private boolean isValid(int[] nums, int divisor, int threshold) {
        long sum = 0; // Use long to prevent overflow during summation
        for (int num : nums) {
            // Integer trick for Math.ceil(num / divisor)
            sum += (num + divisor - 1) / divisor;
        }
        return sum <= threshold;
    }






}
