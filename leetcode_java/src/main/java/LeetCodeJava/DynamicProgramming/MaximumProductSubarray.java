package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-product-subarray/description/

import java.util.ArrayList;
import java.util.List;

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
    // IDEA: Kadane’s Algorithm for Maximum Product Subarray (GPT)
    /**
     *  NOTE !!!
     *
     *   1. define: local_min, local_max, global_max. 3 var
     *   2. loop start from idx = 1
     *   3. init local_min, local_max, global_max as nums[0]
     *   4. cache local_max
     *   5. update local_min, local_max logic:
     *      -> max or min from (x, x * local_min, x * local_max)
     */
    /**
     *   NOTE !!!
     *
     *    we define 3 vars:
     *
     *    1. max_prod
     *    2. min_prod
     *    3. res
     */
    /**
     * 1) Kadane’s Algorithm is a dynamic programming approach used to find:
     *
     * 	  1.	Maximum sum subarray → Standard Kadane’s Algorithm
     * 	  2.	Maximum product subarray → A  `modified` version of Kadane’s Algorithm
     *
     *   -> It works in O(n) time complexity,
     *      making it much faster than brute-force approaches (O(n²) or O(n³)).
     *
     *
     * 2) Kadane’s Algorithm for Maximum Sum Subarray
     *
     *   - Problem Statement:
     * 	Given an array of integers, find the contiguous subarray
     * 	(containing at least one number) that has the largest sum.
     *
     *   - Kadane’s Approach
     * 	•	We iterate through the array while maintaining:
     * 	•	maxSum → Stores the maximum subarray sum found so far.
     * 	•	curSum → Stores the current subarray sum.
     * 	•	If curSum ever becomes negative, reset it to 0
     * 	    (since a negative sum will only decrease the next sum).
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
         * 	•	maxProd: Tracks the maximum product up to the current index.
         *
         * 	•	minProd: Tracks the minimum product up to the current index
         * 	             (needed because multiplying by a negative can turn a small value into a large one).
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

            /** NOTE !!!
             *
             *  the local_min, local_max update logic are ~= THE SAME
             *
             *  -> get max or min from
             *    (x, x * local_min, x * local_max). that's it.
             */
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

    // V0-0-1
    // IDEA: Kadane’s Algorithm (fixed by gpt)
    /**
     *  NOTE !!!
     *
     *   1. define: local_min, local_max, global_max. 3 var
     *   2. loop start from idx = 1
     *   3. init local_min, local_max, global_max as nums[0]
     *   4. cache local_max
     *   5. update local_min, local_max logic:
     *      -> max or min from (x, x * local_min, x * local_max)
     */
    public int maxProduct_0_0_1(int[] nums) {
        // edge
        if (nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        int local_min = nums[0];
        int local_max = nums[0];
        int global_max = nums[0];

        // NOTE !!! start from idx = 1
        for (int i = 1; i < nums.length; i++) {

            int x = nums[i];

            /** NOTE !!!
             *
             * cache local_max
             * -> so the `original` val can still be used in local_min calculation
             */
            int _local_max = local_max;

            /** NOTE !!!
             *
             *  the local_min, local_max update logic are ~= THE SAME
             *
             *  -> get max or min from
             *    (x, x * local_min, x * local_max). that's it.
             */
            local_max = Math.max(
                    x, Math.max(x * local_max, x * local_min));

            local_min = Math.min(
                    x, Math.min(x * _local_max, x * local_min));

            global_max = Math.max(
                    global_max, local_max);

        }

        return global_max;
    }

    // V0-1
    // IDEA: Kadane’s Algorithm
    /**
     *   NOTE !!!
     *
     *    we define 3 vars:
     *
     *    1. max_prod
     *    2. min_prod
     *    3. res
     */
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

    // V0-2
    // IDEA : DP
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/maximum-product-subarray.py#L69
    // IDEA : cur max = max (cur, cur * dp[k-1])
    //        But, also needs to consider "minus number"
    //        -> e.g.  (-1) * (-3) = 3
    //        -> so we NEED to track maxSoFar, and minSoFar
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProduct_0_2(int[] nums) {

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

    // V0-3
    // IDEA: BRUTE FORCE (2 pointers) + boundary handling
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProduct_0_3(int[] nums) {

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


    // V1-1
    // https://neetcode.io/problems/maximum-product-subarray
    // IDEA: BRUTE FORCE
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProduct_1_1(int[] nums) {
        int res = nums[0];

        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
            res = Math.max(res, cur);
            for (int j = i + 1; j < nums.length; j++) {
                cur *= nums[j];
                res = Math.max(res, cur);
            }
        }

        return res;
    }

    // V1-2
    // https://neetcode.io/problems/maximum-product-subarray
    // IDEA: Sliding Window
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProduct_1_2(int[] nums) {
        List<List<Integer>> A = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        int res = Integer.MIN_VALUE;

        for (int num : nums) {
            res = Math.max(res, num);
            if (num == 0) {
                if (!cur.isEmpty()) A.add(cur);
                cur = new ArrayList<>();
            } else cur.add(num);
        }
        if (!cur.isEmpty()) A.add(cur);

        for (List<Integer> sub : A) {
            int negs = 0;
            for (int i : sub) {
                if (i < 0) negs++;
            }

            int prod = 1;
            int need = (negs % 2 == 0) ? negs : (negs - 1);
            negs = 0;
            for (int i = 0, j = 0; i < sub.size(); i++) {
                prod *= sub.get(i);
                if (sub.get(i) < 0) {
                    negs++;
                    while (negs > need) {
                        prod /= sub.get(j);
                        if (sub.get(j) < 0) negs--;
                        j++;
                    }
                }
                if (j <= i) res = Math.max(res, prod);
            }
        }
        return res;
    }


    // V1-3
    // https://neetcode.io/problems/maximum-product-subarray
    // IDEA: Kadane's Algorithm ***
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProduct_1_3(int[] nums) {
        int res = nums[0];
        int curMin = 1, curMax = 1;

        for (int num : nums) {
            int tmp = curMax * num;
            curMax = Math.max(Math.max(num * curMax, num * curMin), num);
            curMin = Math.min(Math.min(tmp, num * curMin), num);
            res = Math.max(res, curMax);
        }
        return res;
    }


    // V1-4
    // https://neetcode.io/problems/maximum-product-subarray
    // IDEA: PREFIX + SUFFIX
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProduct_1_4(int[] nums) {
        int n = nums.length;
        int res = nums[0];
        int prefix = 0, suffix = 0;

        for (int i = 0; i < n; i++) {
            prefix = nums[i] * (prefix == 0 ? 1 : prefix);
            suffix = nums[n - 1 - i] * (suffix == 0 ? 1 : suffix);
            res = Math.max(res, Math.max(prefix, suffix));
        }
        return res;
    }


    // V2
    // IDEA : For each index i keep updating the max and min. We are also keeping min because on multiplying with any negative number your min will become max and max will become min. So for every index i we will take max of (i-th element, prevMax * i-th element, prevMin * i-th element).
    // -> max of (i-th element, prevMax * i-th element, prevMin * i-th element)
    // https://leetcode.com/problems/maximum-product-subarray/solutions/1608862/java-3-solutions-detailed-explanation-using-image/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProduct_2(int[] nums) {

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

    // V3
    // IDEA : Just the slight modification of previous approach.As we know that on multiplying with negative number max will become min and min will become max, so why not as soon as we encounter negative element, we swap the max and min already.
    // https://leetcode.com/problems/maximum-product-subarray/solutions/1608862/java-3-solutions-detailed-explanation-using-image/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProduct_3(int[] nums) {

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

    // V4
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/maximum-product-subarray/solutions/1608862/java-3-solutions-detailed-explanation-using-image/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProduct_4(int[] nums) {

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
