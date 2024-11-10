package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-subarray/
/**
 * 53. Maximum Subarray
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums, find the
 * subarray
 *  with the largest sum, and return its sum.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
 * Output: 6
 * Explanation: The subarray [4,-1,2,1] has the largest sum 6.
 * Example 2:
 *
 * Input: nums = [1]
 * Output: 1
 * Explanation: The subarray [1] has the largest sum 1.
 * Example 3:
 *
 * Input: nums = [5,4,-1,7,8]
 * Output: 23
 * Explanation: The subarray [5,4,-1,7,8] has the largest sum 23.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * -104 <= nums[i] <= 104
 *
 *
 * Follow up: If you have figured out the O(n) solution, try coding another solution using the divide and conquer approach, which is more subtle.
 *
 *
 */
public class MaximumSubarray {

    // V0
    // IDEA : BRUTE FORCE + SUB ARRAY
    public int maxSubArray(int[] nums) {

        if (nums == null || nums.length == 0){
            return 0;
        }

        if (nums.length == 1){
            return nums[0];
        }

        int cumsum = 0;
        int maxSum = -1 * Integer.MAX_VALUE;

        for (int i = 0; i < nums.length; i++){
            int cur = nums[i];
            // NOTE !!! only add cur if cumsum + cur >= 0
            //          -> e.g. add cur element to cur sub array
            /**
             * NOTE !!!
             *  we DON'T need double loop to for loop over i, j
             *  -> simply RESET cumsum as 0 if cumsum + cur < 0
             */
            if (cumsum + cur >= 0){
                cumsum = cumsum + cur;
                maxSum = Math.max(maxSum, cumsum);
            /**
             *  NOTE !!!
             *      need to consider "negative all input cases" (Math.max(maxSum, cur);)
             *      e.g.  nums = [-1, -2]
             *      -> so we get max between maxSum and cur
             *      -> since whatever sub array sum in this case is < 0
             *      -> so we choose the "biggest" negative element in nums
             */
            }else{
                cumsum = 0;
                maxSum = Math.max(maxSum, cur);
            }
        }

        return maxSum;
    }

    // V0'-1
    // IDEA : PRE-SUM ARRAY (fixed by gpt)
    public int maxSubArray_0_1_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
        }

        int n = nums.length;
        int[] prefixSum = new int[n + 1];

        // Build the prefix sum array
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }

        int maxSum = Integer.MIN_VALUE;
        int minPrefixSum = 0;

        // Calculate max subarray sum using prefix sums
        for (int i = 1; i <= n; i++) {
            maxSum = Math.max(maxSum, prefixSum[i] - minPrefixSum);
            minPrefixSum = Math.min(minPrefixSum, prefixSum[i]);
        }

        return maxSum;
    }


    // V0'
    // IDEA : BRUTE FORCE
    // https://www.bilibili.com/video/BV1aY4y1Z7ya/?share_source=copy_web&vd_source=49348a1975630e1327042905e52f488a
    public int maxSubArray_(int[] nums) {

        if (nums.length == 0 || nums == null){
            return 0;
        }

        if (nums.length == 1){
            return nums[0];
        }

        // loop over nums, reset start-idx if "current sub array" < current num
        // maintain the max value (ans) as well while looping

        int ans = -Integer.MIN_VALUE;
        int cumSum = 0;

        for (int i = 0; i < nums.length; i++){
            /** NOTE !!!
             *
             *  ONLY use "cumSum + nums[i]" as condition
             *  to check whether give up current cumsum, and create a new one
             *  or keep cumsum
             */
            if (cumSum + nums[i] < 0){
                /**
                 * NOTE !!!
                 *    if cumSum + nums[i] < 0,
                 *    still need to get max val from (ans, cumSum + nums[i])
                 */
                ans = Math.max(ans, cumSum + nums[i]);
                cumSum = 0;
            }else{
                cumSum += nums[i];
                ans = Math.max(ans, cumSum);
            }
        }

        return ans;
    }

    // V0''
    // IDEA : BRUTE FORCE (by GPT)
    public int maxSubArray_0(int[] nums) {
        // If there is only one element, the maximum subarray is the element itself
        if (nums.length == 1) {
            return nums[0];
        }

        // Initialize variables
        int res = nums[0]; // This will store the maximum subarray sum
        int cur = nums[0]; // This will store the current subarray sum

        // Iterate through the array starting from the second element
        for (int i = 1; i < nums.length; i++) {
            // Update current subarray sum
            cur = Math.max(nums[i], cur + nums[i]);

            // Update the result with the maximum value found so far
            res = Math.max(res, cur);
        }

        return res;
    }

    // V0'''
    // IDEA : GREEDY (modified by GPT)
    public int maxSubArray_0_1(int[] nums) {

        if (nums.length == 1) {
            return nums[0];
        }

        int res = nums[0];
        int cur = nums[0];

        // Left pointer (l) and right pointer (r) initialized to the first element
        //int l = 0;
        int r = 1;

        while (r < nums.length) {
            // If current sum is negative, reset it to the current element
            if (cur < 0) {
                //l = r;
                cur = nums[r];
            } else {
                // Otherwise, add the current element to the current sum
                cur += nums[r];
            }
            // Update the result with the maximum value found so far
            res = Math.max(res, cur);
            r++;
        }

        return res;
    }


    // V1
    // IDEA : OPTIMIZED BRUTE FORCE (TLE)
    // https://leetcode.com/problems/maximum-subarray/editorial/
    public int maxSubArray_2(int[] nums) {
        int maxSubarray = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int currentSubarray = 0;
            for (int j = i; j < nums.length; j++) {
                currentSubarray += nums[j];
                maxSubarray = Math.max(maxSubarray, currentSubarray);
            }
        }

        return maxSubarray;
    }

    // V2
    // IDEA : Dynamic Programming, Kadane's Algorithm
    // https://leetcode.com/problems/maximum-subarray/editorial/
    /**
     * Explanation of the Code
     *
     * 	1.	Edge Case Handling: If the input array is null or empty, an IllegalArgumentException is thrown.
     *
     * 	2.	Kadane’s Algorithm:
     * 	    •	currentSum keeps track of the maximum sum of the subarray ending at the current index.
     * 	    •	For each element in the array, we decide whether to:
     * 	    •	Start a new subarray from this element, or
     * 	    •	Extend the existing subarray to include this element.
     * 	    •	maxSum keeps track of the maximum subarray sum encountered so far.
     *
     * 	3.	Final Output: After iterating through the array, maxSum contains the maximum subarray sum.
     *
     *
     */
    public int maxSubArray_3(int[] nums) {
        // Initialize our variables using the first element.
        int currentSubarray = nums[0];
        int maxSubarray = nums[0];

        // Start with the 2nd element since we already used the first one.
        for (int i = 1; i < nums.length; i++) {
            int num = nums[i];
            // If current_subarray is negative, throw it away. Otherwise, keep adding to it.
            currentSubarray = Math.max(num, currentSubarray + num);
            maxSubarray = Math.max(maxSubarray, currentSubarray);
        }

        return maxSubarray;
    }

    // V3
    // IDEA : Divide and Conquer
    // https://leetcode.com/problems/maximum-subarray/editorial/
    private int[] numsArray;

    public int maxSubArray_4(int[] nums) {
        numsArray = nums;

        // Our helper function is designed to solve this problem for
        // any array - so just call it using the entire input!
        return findBestSubarray(0, numsArray.length - 1);
    }

    private int findBestSubarray(int left, int right) {
        // Base case - empty array.
        if (left > right) {
            return Integer.MIN_VALUE;
        }

        int mid = Math.floorDiv(left + right, 2);
        int curr = 0;
        int bestLeftSum = 0;
        int bestRightSum = 0;

        // Iterate from the middle to the beginning.
        for (int i = mid - 1; i >= left; i--) {
            curr += numsArray[i];
            bestLeftSum = Math.max(bestLeftSum, curr);
        }

        // Reset curr and iterate from the middle to the end.
        curr = 0;
        for (int i = mid + 1; i <= right; i++) {
            curr += numsArray[i];
            bestRightSum = Math.max(bestRightSum, curr);
        }

        // The bestCombinedSum uses the middle element and the best
        // possible sum from each half.
        int bestCombinedSum = numsArray[mid] + bestLeftSum + bestRightSum;

        // Find the best subarray possible from both halves.
        int leftHalf = findBestSubarray(left, mid - 1);
        int rightHalf = findBestSubarray(mid + 1, right);

        // The largest of the 3 is the answer for any given input array.
        return Math.max(bestCombinedSum, Math.max(leftHalf, rightHalf));
    }

}
