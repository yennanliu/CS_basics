package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-product-subarray/description/
/**
 * 152. Maximum Product Subarray
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums, find a
 * subarray
 *  that has the largest product, and return the product.
 *
 * The test cases are generated so that the answer will fit in a 32-bit integer.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,-2,4]
 * Output: 6
 * Explanation: [2,3] has the largest product 6.
 * Example 2:
 *
 * Input: nums = [-2,0,-1]
 * Output: 0
 * Explanation: The result cannot be 2, because [-2,-1] is not a subarray.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 104
 * -10 <= nums[i] <= 10
 * The product of any subarray of nums is guaranteed to fit in a 32-bit integer.
 */
public class MaximumProductSubarray {

    // V0
    // IDEA : DP
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/maximum-product-subarray.py#L69
    // IDEA : cur max = max (cur, cur * dp[k-1])
    //        But, also needs to consider "minus number"
    //        -> e.g.  (-1) * (-3) = 3
    //        -> so we NEED to track maxSoFar, and minSoFar
    public int maxProduct(int[] nums) {

        // null check
        if (nums.length == 0){
            return 0;
        }
        // init
        int maxSoFar = nums[0];
        int minSoFar = nums[0];
        int res = maxSoFar;

        for (int i = 1; i < nums.length; i++){

            int cur = nums[i];
            /**
             *  NOTE !!!
             *
             *   keep track below
             *   1) global max (maxSoFar)
             *   2) global min (minSoFar)
             *   3) local max (tmpMax)
             *
             *   DP equation :
             *     d
             */
            /**
             *  or, can use below trick to get max in 3 numbers
             *
             *   max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
             *   min = Math.min(Math.min(temp * nums[i], min * nums[i]), nums[i]);
             *
             */
            int tmpMax = findMax(cur, maxSoFar * cur, minSoFar * cur);
            minSoFar = findMin(cur, maxSoFar * cur, minSoFar * cur);
            maxSoFar = tmpMax;

            res = Math.max(maxSoFar, res);
        }

        return res;
    }

    private int findMax(int a, int b, int c){
        if (a >= b && a >= c){
            return a;
        }
        else if (b >= a && b >= c){
            return b;
        }else{
            return c;
        }
    }

    private int findMin(int a, int b, int c){
        if (a <= b && a <= c){
            return a;
        }
        else if (b <= a && b <= c){
            return b;
        }else{
            return c;
        }
    }

    // V0-1
    // IDEA: BRUTE FORCE (2 pointers) + boundary handling
    public int maxProduct_0_1(int[] nums) {

        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        if (nums.length == 2) {
            return Math.max(Math.max(nums[0], nums[1]), nums[0] * nums[1]);
        }

        // 2 pointers
        int res = Integer.MIN_VALUE; //0;
        for (int i = 0; i < nums.length - 1; i++) {
            int prod = nums[i];
            res = Math.max(res, nums[i]);
            for (int j = i + 1; j < nums.length; j++) {
                prod = prod * nums[j];
                res = Math.max(prod, res);
            }
        }

        // handling last element
        res = Math.max(res, nums[nums.length - 1]);

        return res;
    }

  // V0-2
  // IDEA: Kadane’s Algorithm for Maximum Product Subarray (GPT)
  /**
   * 1) Kadane’s Algorithm is a dynamic programming approach used to find:
   * 	1.	Maximum sum subarray → Standard Kadane’s Algorithm
   * 	2.	Maximum product subarray → A modified version of Kadane’s Algorithm
   *
   *   -> It works in O(n) time complexity, making it much faster than brute-force approaches (O(n²) or O(n³)).
   *
   *
   * 2_ Kadane’s Algorithm for Maximum Sum Subarray
   *
   *   - Problem Statement:
   * 	Given an array of integers, find the contiguous subarray
   * 	(containing at least one number) that has the largest sum.
   *
   *   - Kadane’s Approach
   * 	•	We iterate through the array while maintaining:
   * 	•	maxSum → Stores the maximum subarray sum found so far.
   * 	•	curSum → Stores the current subarray sum.
   * 	•	If curSum ever becomes negative, reset it to 0 (since a negative sum will only decrease the next sum).
   *
   */
  public int maxProduct_0_2(int[] nums) {
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

            maxProd = Math.max(nums[i], Math.max(nums[i] * maxProd, nums[i] * minProd));
            minProd = Math.min(nums[i], Math.min(nums[i] * temp, nums[i] * minProd));

            result = Math.max(result, maxProd);
        }

        return result;
    }

    // V1
    // IDEA : For each index i keep updating the max and min. We are also keeping min because on multiplying with any negative number your min will become max and max will become min. So for every index i we will take max of (i-th element, prevMax * i-th element, prevMin * i-th element).
    // -> max of (i-th element, prevMax * i-th element, prevMin * i-th element)
    // https://leetcode.com/problems/maximum-product-subarray/solutions/1608862/java-3-solutions-detailed-explanation-using-image/
    public int maxProduct_1(int[] nums) {

        int max = nums[0], min = nums[0], ans = nums[0];

        for (int i = 1; i < nums.length; i++) {

            int temp = max;  // store the max because before updating min your max will already be updated

            max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
            min = Math.min(Math.min(temp * nums[i], min * nums[i]), nums[i]);

            if (max > ans) {
                ans = max;
            }
        }

        return ans;
    }

    // V2
    // IDEA : Just the slight modification of previous approach.As we know that on multiplying with negative number max will become min and min will become max, so why not as soon as we encounter negative element, we swap the max and min already.
    // https://leetcode.com/problems/maximum-product-subarray/solutions/1608862/java-3-solutions-detailed-explanation-using-image/
    public int maxProduct_2(int[] nums) {

        int max = nums[0], min = nums[0], ans = nums[0];
        int n = nums.length;

        for (int i = 1; i < n; i++) {

            // Swapping min and max
            if (nums[i] < 0){
                int temp = max;
                max = min;
                min = temp;
            }

            max = Math.max(nums[i], max * nums[i]);
            min = Math.min(nums[i], min * nums[i]);

            ans = Math.max(ans, max);
        }

        return ans;
    }

    // V3
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/maximum-product-subarray/solutions/1608862/java-3-solutions-detailed-explanation-using-image/
    public int maxProduct_3(int[] nums) {

        int n = nums.length;
        int l=1,r=1;
        int ans=nums[0];

        for(int i=0;i<n;i++){

            //if any of l or r become 0 then update it to 1
            l = l==0 ? 1 : l;
            r = r==0 ? 1 : r;

            l *= nums[i];   //prefix product
            r *= nums[n-1-i];    //suffix product

            ans = Math.max(ans,Math.max(l,r));

        }

        return ans;
    }

}
