# Kadane Algorithm


## Intro

Kadane's algorithm is an elegant method of calculating the maximum sum subarray ending at a given position in an array, all in a single pass.

Kadane's Algorithm is a popular dynamic programming technique used to find the maximum sum of a contiguous subarray within a given array. It is primarily used for problems related to subarray sums. 

## LC problems that can be solved via Kadane's algo:

- 53:  Maximum Subarray
- 152: Maximum Product Subarray
- 918: Maximum Sum Circular Subarray
- 1836: Remove Duplicates from an Unsorted Linked List
- 134: Gas Station
- 122: Best Time to Buy and Sell Stock II
- Longest Turbulent Array

## Ref
- https://www.flydean.com/interview/arithmetic/arithmetic-Kadane/
- https://zh.wikipedia.org/zh-tw/%E6%9C%80%E5%A4%A7%E5%AD%90%E6%95%B0%E5%88%97%E9%97%AE%E9%A2%98
- https://leetcode.com/discuss/study-guide/1308617/Dynamic-Programming-Patterns

## Implementation

1. Start by setting the maxSum to the first value in the array and the curSum to 0.
2. Loop through the array and at each iteration, reset curSum to 0 if it's negative. 
3. Increment curSum by adding the current value in our array to it.
4. Update the maxSum at every iteration.
5. Finally, return the maxSum.


## Pattern

```java
// java

/** 
 *  
 * Initialization:

	currentSum: Tracks the sum of the current subarray.
	maxSum: Tracks the maximum subarray sum found so far.
	Iteration:

	For each element in the array (starting from the second one), check if it's better to:
	Start a new subarray with just nums[i].
	Or, add nums[i] to the current subarray.
	Update maxSum with the largest value between maxSum and currentSum.
	Time Complexity:

	This algorithm runs in O(n) time, where n is the length of the input array, because we only traverse the array once.
	Space Complexity:

	O(1): We only use a few variables to store intermediate results, so the space complexity is constant.


	Example:
		Given the array [-2, 1, -3, 4, -1, 2, 1, -5, 4], Kadane's algorithm will find the subarray [4, -1, 2, 1] with the maximum sum, which is 6.

		Edge Cases:
		If the array contains all negative numbers, the algorithm will return the largest single element because it will handle starting the subarray with the least negative number.
		If the array is empty, it will return 0.
		This is the base pattern for Kadane's algorithm. If you're dealing with variations (e.g., maximum product, circular subarrays), you can adapt this pattern accordingly.


 * 
 * 
 */

public class KadaneAlgo {

    // LC 53
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

    // LC 152
    public int maxProduct(int[] nums) {
        // Edge case
        if (nums == null || nums.length == 0) {
            return 0;
        }

        /**
         * 	•	maxProd: Tracks the maximum product up to the current index.
         * 	•	minProd: Tracks the minimum product up to the current index
         * 	             (needed because multiplying by a negative can turn a small value into a large one).
         */
        int maxProd = nums[0];
        int minProd = nums[0];
        int result = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int temp = maxProd; // Store maxProd before updating

            /**
             * NOTE !!!
             *
             *  1) we cache maxProd
             *  2) we re-compute maxProd, minProd per below logic
             *  3) we get `global max` (result) as well within each iteration
             */
            maxProd = Math.max(nums[i], Math.max(nums[i] * maxProd, nums[i] * minProd));
            minProd = Math.min(nums[i], Math.min(nums[i] * temp, nums[i] * minProd));

            result = Math.max(result, maxProd);
        }

        return result;
    }

}
```
