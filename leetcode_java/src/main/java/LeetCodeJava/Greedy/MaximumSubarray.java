package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-subarray/

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
                maxSum = Math.max(maxSum, cur);
                cumsum = 0;
            }
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
            if (cumSum + nums[i] < 0){
                ans = Math.max(ans, cumSum + nums[i]);
                cumSum = 0;
            }else{
                cumSum += nums[i];
                ans = Math.max(ans, cumSum);
            }
        }

        return ans;
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
