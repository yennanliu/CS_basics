package AlgorithmJava;

/**
 *  KADANE'S ALGORITHM -- best contiguous subarray
 *
 *  A one-pass DP for "find the best CONTIGUOUS subarray". The insight
 *  that collapses O(N^2) to O(N):
 *
 *      at each index there are only TWO choices --
 *        EXTEND the best subarray ending at the previous index, or
 *        START a new subarray here
 *
 *  and you never need to know where the winning subarray began, only
 *  the best value ENDING at the current index. So the whole DP table
 *  collapses to a single variable.
 *
 *      nums     -2   1  -3   4  -1   2   1  -5   4
 *      current  -2   1  -2   4   3   5   6   1   5
 *                    ^           ^-- extending beats restarting
 *                    +-- 1 > -2 + 1, so restart here
 *      maxSum   -2   1   1   4   4   5   6   6   6      answer 6
 *
 *  THE RULE OF THUMB: `current + nums[i] < nums[i]` exactly when
 *  `current < 0`. A negative running sum can only hurt whatever comes
 *  next, so drop it and start fresh.
 *
 *  Both variants below start at index 1 with the accumulators seeded
 *  from nums[0], which is what keeps them correct on an ALL-NEGATIVE
 *  array -- initialising to 0 instead would wrongly return 0.
 *
 *  Time  : O(N)
 *  Space : O(1)
 *
 *  Reference: https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/kadane_algo.md
 */
public class KadaneAlgo {

    /**
     *  LC 53 Maximum Subarray -- the largest contiguous SUM.
     */
    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int currentSum = nums[0];   // best sum ENDING at index i
        int maxSum = nums[0];       // best sum seen anywhere

        // start at 1: index 0 already seeded both accumulators
        for (int i = 1; i < nums.length; i++) {
            // extend the previous subarray, or start a new one here.
            // starting fresh wins exactly when currentSum is negative.
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        return maxSum;
    }

    /**
     *  LC 152 Maximum Product Subarray -- the largest contiguous PRODUCT.
     *
     *  Products need a second accumulator that sums do not. A large
     *  NEGATIVE running product becomes the largest positive one the
     *  moment it meets another negative, so the smallest product must be
     *  tracked alongside the largest:
     *
     *      nums     2   -3   -4
     *      maxProd  2   -3    24    <- (-3) * (-4) needs the min from step 2
     *      minProd  2   -6   -4
     *
     *  Zeros reset both accumulators, which the `nums[i]` branch of each
     *  Math.max/min handles for free.
     */
    public int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int maxProd = nums[0];      // largest product ending at index i
        int minProd = nums[0];      // smallest (most negative) product ending at index i
        int result = nums[0];

        for (int i = 1; i < nums.length; i++) {
            // cache maxProd BEFORE overwriting it -- minProd's update
            // needs the old value, and using the new one is the classic bug
            int previousMax = maxProd;

            maxProd = Math.max(nums[i], Math.max(nums[i] * maxProd, nums[i] * minProd));
            minProd = Math.min(nums[i], Math.min(nums[i] * previousMax, nums[i] * minProd));

            result = Math.max(result, maxProd);
        }
        return result;
    }

    public static void main(String[] args) {
        KadaneAlgo solution = new KadaneAlgo();

        //--- LC 53 ---------------------------------------------------
        assertThat(solution.maxSubArray(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6,
                "[4,-1,2,1] sums to 6");
        assertThat(solution.maxSubArray(new int[] {1}) == 1, "single element");
        assertThat(solution.maxSubArray(new int[] {5, 4, -1, 7, 8}) == 23, "the whole array");

        // all negative: the answer is the least-bad single element, not 0
        assertThat(solution.maxSubArray(new int[] {-3, -1, -2}) == -1, "all negative");
        assertThat(solution.maxSubArray(new int[] {-1}) == -1, "one negative element");

        assertThat(solution.maxSubArray(new int[] {0, 0}) == 0, "all zeros");
        assertThat(solution.maxSubArray(null) == 0, "null input");
        assertThat(solution.maxSubArray(new int[0]) == 0, "empty input");

        //--- LC 152 --------------------------------------------------
        assertThat(solution.maxProduct(new int[] {2, 3, -2, 4}) == 6, "[2,3] multiplies to 6");
        assertThat(solution.maxProduct(new int[] {-2, 0, -1}) == 0, "the zero wins");

        // two negatives make a positive -- this is why minProd exists
        assertThat(solution.maxProduct(new int[] {2, -3, -4}) == 24, "(-3) * (-4) * 2");
        assertThat(solution.maxProduct(new int[] {-2, 3, -4}) == 24, "the whole array");

        // an odd count of negatives means one must be dropped
        assertThat(solution.maxProduct(new int[] {-2, 3, -4, 5}) == 120, "3 * -4 * 5 * -2");
        assertThat(solution.maxProduct(new int[] {-2}) == -2, "single negative");

        // a zero resets both accumulators
        assertThat(solution.maxProduct(new int[] {-2, 0, 3, 4}) == 12, "restart after the zero");

        assertThat(solution.maxProduct(new int[] {2}) == 2, "single element");
        assertThat(solution.maxProduct(null) == 0, "null input");
        assertThat(solution.maxProduct(new int[0]) == 0, "empty input");

        System.out.println("maxSubArray: " + solution.maxSubArray(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        System.out.println("maxProduct : " + solution.maxProduct(new int[] {2, 3, -2, 4}));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
