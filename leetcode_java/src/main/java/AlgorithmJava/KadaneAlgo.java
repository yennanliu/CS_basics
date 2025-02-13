package AlgorithmJava;

/**
 * Kadane Algo V1
 *
 * Kadane's Algorithm is a popular dynamic programming technique
 * used to find the `MAX sum of a contiguous SUBARRAY` within a given array.
 * It is primarily used for problems related to subarray sums.
 *
 * - https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/kadane_algo.md
 *
 */
public class KadaneAlgo {

    public int maxSubArray(int[] nums) {
        // Edge case: if nums is empty, return 0
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // Initialize variables
        int currentSum = nums[0];  // Starting from the first element
        int maxSum = nums[0];      // Keep track of the maximum sum found

        // Iterate over the array starting from the second element
        /**
         *  NOTE !!!
         *
         *   we start from idx = 1, since we init local, global max as nums[0]
         */
        for (int i = 1; i < nums.length; i++) {
            // Update current sum: either extend the existing subarray or start a new one
            /**
             *  NOTE !!!
             *
             *   we compare `nums[i]` and `nums[i] + currentSum`,
             *   since if `nums[i] + currentSum < nums[i]'
             *   -> we SHOULD start a new sub array
             */
            currentSum = Math.max(nums[i], currentSum + nums[i]);

            // Update the maximum sum found so far
            maxSum = Math.max(maxSum, currentSum);
        }

        return maxSum;
    }

}
