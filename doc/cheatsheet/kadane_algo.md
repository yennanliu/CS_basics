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


## Keys

```
// LC 152
/**
 *  key:
 *
 *       * ## 🧠 How “Select / Not Select” Happens Internally
 *
 *      * Each step implicitly considers **3 choices** for the current `nums[i]`:
 *      *
 *      * 1. **Start new subarray** at `i` → just take `nums[i]`
 *      * 2. **Extend previous max product subarray** → `nums[i] * maxProd`
 *      * 3. **Extend previous min product subarray** → `nums[i] * minProd`
 */
```


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


## LC examples

### 2-1) Maximum Product Subarray

```java
// java

// LC 152

// V0
// IDEA: Kadane’s Algorithm for Maximum Product Subarray (GPT)
/**
 * 1) Kadane’s Algorithm is a dynamic programming approach used to find:
 *
 *    1.    Maximum sum subarray → Standard Kadane’s Algorithm
 *    2.    Maximum product subarray → A  `modified` version of Kadane’s Algorithm
 *
 *   -> It works in O(n) time complexity,
 *      making it much faster than brute-force approaches (O(n²) or O(n³)).
 *
 *
 * 2) Kadane’s Algorithm for Maximum Sum Subarray
 *
 *   - Problem Statement:
 *  Given an array of integers, find the contiguous subarray
 *  (containing at least one number) that has the largest sum.
 *
 *   - Kadane’s Approach
 *  •   We iterate through the array while maintaining:
 *  •   maxSum → Stores the maximum subarray sum found so far.
 *  •   curSum → Stores the current subarray sum.
 *  •   If curSum ever becomes negative, reset it to 0
 *      (since a negative sum will only decrease the next sum).
 *
 */
/**
 *  Question: how Kadane algo work ?
 *
 *   can it track `all select, not select` options ?
 *
 *   -> check below:
 *
 * ## ✅ Problem Summary
 *
 * Given an integer array `nums`, **find the contiguous subarray (at least one number)** that has the **largest product**, in **O(n)** time.
 *
 * ---
 *
 * ## 🔄 Why We Track Both `maxProd` and `minProd`
 *
 * Multiplication introduces **nonlinear behavior**, especially when:
 *
 * * Negative × Negative = Positive
 * * Negative × Positive = Negative
 * * Zero resets the subarray
 *
 * So we **must track both the maximum and minimum product** ending at each position.
 *
 * ---
 *
 * ## 🔍 Step-by-Step Visualization
 *
 * Let’s walk through this input:
 *
 * ```java
 * int[] nums = {2, 3, -2, 4};
 * ```
 *
 * We initialize:
 *
 * ```java
 * maxProd = nums[0] = 2
 * minProd = nums[0] = 2
 * result = 2
 * ```
 *
 * ### i = 1 → nums\[1] = 3
 *
 * ```
 * temp = maxProd = 2
 *
 * maxProd = max(3, 3×2=6, 3×2=6) → 6   (NOTE !!! this)
 * minProd = min(3, 3×2=6, 3×2=6) → 3
 * result = max(2, 6) = 6
 * ```
 *
 * ### i = 2 → nums\[2] = -2
 *
 * ```
 * temp = maxProd = 6
 *
 * maxProd = max(-2, -2×6=-12, -2×3=-6) → -2   (NOTE !!! this)
 * minProd = min(-2, -2×6=-12, -2×3=-6) → -12
 * result = max(6, -2) = 6
 * ```
 *
 * ### i = 3 → nums\[3] = 4
 *
 * ```
 * temp = maxProd = -2
 *
 * maxProd = max(4, 4×-2=-8, 4×-12=-48) → 4
 * minProd = min(4, 4×-2=-8, 4×-12=-48) → -48
 * result = max(6, 4) = 6
 * ```
 *
 * ✅ Final Answer: `6`
 *
 * ---
 *
 * ## 🧠 How “Select / Not Select” Happens Internally
 *
 * Each step implicitly considers **3 choices** for the current `nums[i]`:
 *
 * 1. **Start new subarray** at `i` → just take `nums[i]`
 * 2. **Extend previous max product subarray** → `nums[i] * maxProd`
 * 3. **Extend previous min product subarray** → `nums[i] * minProd`
 *
 * We **don't need to explicitly track selection** — it’s captured by:
 *
 * ```java
 * maxProd = max(nums[i], nums[i] * maxProd, nums[i] * minProd);
 * ```
 *
 * This is the elegant part:
 *
 *  **all paths are considered through this max/min logic
 *  without branching explicitly.**
 *
 * ---
 *
 * ## ✍️ Visual Diagram
 *
 * ```text
 * Step-by-step (nums = {2, 3, -2, 4}):
 *
 * Index | nums[i] | maxProd     | minProd     | result
 * -----------------------------------------------------
 *   0   |    2    |     2       |     2       |   2
 *   1   |    3    | max(3,6,6)=6| min(3,6,6)=3|   6
 *   2   |   -2    | max(-2,-12,-6)=-2 | min(-2,-12,-6)=-12 | 6
 *   3   |    4    | max(4,-8,-48)=4 | min(4,-8,-48)=-48 | 6
 * ```
 *
 */
public int maxProduct(int[] nums) {
    // Edge case
    if (nums == null || nums.length == 0) {
        return 0;
    }

    /**
     *  •   maxProd: Tracks the maximum product up to the current index.
     *
     *  •   minProd: Tracks the minimum product up to the current index
     *               (needed because multiplying by a negative can turn a small value into a large one).
     */
    /**
     *  key:
     *
     *       * ## 🧠 How “Select / Not Select” Happens Internally
     *
     *      * Each step implicitly considers **3 choices** for the current `nums[i]`:
     *      *
     *      * 1. **Start new subarray** at `i` → just take `nums[i]`
     *      * 2. **Extend previous max product subarray** → `nums[i] * maxProd`
     *      * 3. **Extend previous min product subarray** → `nums[i] * minProd`
     */
    int maxProd = nums[0];
    int minProd = nums[0];
    // NOTE !!! we init final result as well
    int result = nums[0];
    
    
    for (int i = 1; i < nums.length; i++) {

        // NOTE !!! we cache maxProd as `temp` before updating
        int temp = maxProd; // Store maxProd before updating

        /**
         *  NOTE !!! below
         *
         *   get max from
         *
         *    1. nums[i]
         *    2. nums[i] * max_prod
         *    2. nums[i] * min_prod
         *
         */
        maxProd = Math.max(nums[i],
                Math.max(nums[i] * maxProd, nums[i] * minProd)
        );

        /**
         *  NOTE !!! below
         *
         *   get min from
         *
         *    1. nums[i]
         *    2. nums[i] * max_prod
         *    2. nums[i] * min_prod
         *
         */
        minProd = Math.min(nums[i],
                Math.min(nums[i] * temp, nums[i] * minProd)
        );

        // NOTE !!! update final result here
        result = Math.max(result, maxProd);
    }

    return result;
}

// V0-1
// IDEA: Kadane’s Algorithm
public int maxProduct_0_1(int[] nums) {

    // Edge case
    if (nums == null || nums.length == 0) {
        return 0;
    }

    // NOTE !!! we init val as below
    int global_max = nums[0];
    int local_max = nums[0];
    int local_min = nums[0];

    // NOTE !!! we start from idx = 1 (NOT 0)
    for (int i = 1; i < nums.length; i++) {

        int n = nums[i];

        /** NOTE !!!
         *
         *  cache local_max below (for local_min)
         */
        int cache = local_max;

        /** NOTE !!!
         *
         *  for local_max,
         *
         *  we get from:
         *
         *    1. n
         *    2. local_max * n
         *    3. local_min * n
         */
        local_max = Math.max(
                local_min * n,
                Math.max(local_max * n, n));

        /** NOTE !!!
         *
         *  for local_min,
         *
         *  we get from:
         *
         *    1. n
         *    2. cache * n  (NOTE: cache if local_max cache)
         *    3. local_min * n
         */
        local_min = Math.min(
                n * cache,
                Math.min(local_min * n, n));

        /** NOTE !!!
         *
         *  for global_max,
         *
         *  we get  global_max from
         *
         *    1. global_max
         *    2. local_max
         *    3. local_min
         */
        global_max = Math.max(
                global_max,
                Math.max(local_max, local_min));
    }

    return global_max;
}

```


### 2-2) Maximum Sum Circular Subarray

```java
// java
// LC 918
// https://leetcode.com/problems/maximum-sum-circular-subarray/editorial/
// IDEA: Calculate the "Minimum Subarray"
public int maxSubarraySumCircular_2_2(int[] nums) {
    int curMax = 0;
    int curMin = 0;
    int maxSum = nums[0];
    int minSum = nums[0];
    int totalSum = 0;

    for (int num : nums) {
        // Normal Kadane's
        curMax = Math.max(curMax, 0) + num;
        maxSum = Math.max(maxSum, curMax);

        // Kadane's but with min to find minimum subarray
        curMin = Math.min(curMin, 0) + num;
        minSum = Math.min(minSum, curMin);

        totalSum += num;
    }

    if (totalSum == minSum) {
        return maxSum;
    }

    return Math.max(maxSum, totalSum - minSum);
}
```