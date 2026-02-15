package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/range-sum-query-immutable/description/
/**
 *  303. Range Sum Query - Immutable
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums, handle multiple queries of the following type:
 *
 * Calculate the sum of the elements of nums between indices left and right inclusive where left <= right.
 * Implement the NumArray class:
 *
 * NumArray(int[] nums) Initializes the object with the integer array nums.
 * int sumRange(int left, int right) Returns the sum of the elements of nums between indices left and right inclusive (i.e. nums[left] + nums[left + 1] + ... + nums[right]).
 *
 *
 * Example 1:
 *
 * Input
 * ["NumArray", "sumRange", "sumRange", "sumRange"]
 * [[[-2, 0, 3, -5, 2, -1]], [0, 2], [2, 5], [0, 5]]
 * Output
 * [null, 1, -1, -3]
 *
 * Explanation
 * NumArray numArray = new NumArray([-2, 0, 3, -5, 2, -1]);
 * numArray.sumRange(0, 2); // return (-2) + 0 + 3 = 1
 * numArray.sumRange(2, 5); // return 3 + (-5) + 2 + (-1) = -1
 * numArray.sumRange(0, 5); // return (-2) + 0 + 3 + (-5) + 2 + (-1) = -3
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -105 <= nums[i] <= 105
 * 0 <= left <= right < nums.length
 * At most 104 calls will be made to sumRange.
 *
 */
public class RangeSumQueryImmutable {

    // V0
    // IDEA: PREFIX SUM
    class NumArray {

        // attr
        int[] nums;
        int[] prefix;
        int n;

        public NumArray(int[] nums) {
            this.nums = nums;
            this.n = this.nums.length;
            this.prefix = new int[this.n];
            // ??
            int prefixSum = 0;
            for (int i = 0; i < n; i++) {
                prefixSum += this.nums[i];
                this.prefix[i] = prefixSum;
            }
        }

        public int sumRange(int left, int right) {
            if (left > right) {
                return -1;
            }
            if (left == right) {
                return this.nums[right];
            }
            if (left == 0) {
                return this.prefix[right];
            }

            return this.prefix[right] - this.prefix[left - 1];
        }
    }

    // V0-1
    // IDEA: PREFIX SUM (fixed by gemini)
    class NumArray_0_1 {
        // We only need the prefix sum array
        private int[] prefix;

        public NumArray_0_1(int[] nums) {
            int n = nums.length;
            // Create prefix array with size n + 1
            // prefix[i] stores the sum of nums[0...i-1]
            this.prefix = new int[n + 1];

            for (int i = 0; i < n; i++) {
                this.prefix[i + 1] = this.prefix[i] + nums[i];
            }
        }

        public int sumRange(int left, int right) {
            // The sum from index left to right is simply:
            // (Sum of first right+1 elements) - (Sum of first left elements)
            return prefix[right + 1] - prefix[left];
        }
    }

    
    /**
     * Your NumArray object will be instantiated and called as such:
     * NumArray obj = new NumArray(nums);
     * int param_1 = obj.sumRange(left,right);
     */

    // V1
    // IDEA: PRE FIX SUM
    // https://leetcode.com/problems/range-sum-query-immutable/solutions/6704484/dont-you-understand-youll-understand-now-5eyp/
    class NumArray_1 {

        private int[] prefixSum;

        public NumArray_1(int[] nums) {
            int n = nums.length;
            prefixSum = new int[n + 1];

            prefixSum[0] = 0;
            for (int i = 1; i <= n; i++) {
                prefixSum[i] = prefixSum[i - 1] + nums[i - 1];
            }
        }

        public int sumRange(int left, int right) {
            return prefixSum[right + 1] - prefixSum[left];
        }
    }



    // V2
    // IDEA: PRE FIX SUM
    // https://leetcode.com/problems/range-sum-query-immutable/solutions/1494994/java-tc-init-on-sumrange-o1-using-prefix-bwhm/
    /**
     * Using a prefix sum array
     *
     * prefixSum[i] = prefixSum[i-1] + nums[i-1]. Length of prefixSum array is 1 +
     * length of input array.
     *
     * sumRange(1,3) = prefixSum[3+1] - prefixSum[1]
     *
     * Time Complexity: NumArray() -> O(N). sumRange() -> O(1)
     *
     * Space Complexity: O(N) (Can be O(1) if allowed to modify nums array)
     *
     * N = Length of input array.
     */
    class NumArray_2 {

        int[] prefixSum;

        public NumArray_2(int[] nums) {
            if (nums == null) {
                throw new IllegalArgumentException("Input is null");
            }

            prefixSum = new int[nums.length + 1];

            for (int i = 1; i <= nums.length; i++) {
                prefixSum[i] = prefixSum[i - 1] + nums[i - 1];
            }
        }

        public int sumRange(int left, int right) {
            if (left < 0 || right >= prefixSum.length - 1) {
                throw new IndexOutOfBoundsException("Input indices are out of bounds");
            }

            // Here both left and right are inclusive.
            // right maps to right+1 in prefixSum. left maps to left+1 in prefixSum.
            // To get the result subtract the prefixSum before left index from prefixSum at
            // right index.
            return prefixSum[right + 1] - prefixSum[left];
        }
    }


    // V3
    // IDEA: DP
    // https://leetcode.com/problems/range-sum-query-immutable/solutions/75278/my-3ms-clean-java-dp-solution-may-help-u-d5jk/
    public class NumArray_3 {

        private int[] sum;

        public NumArray_3(int[] nums) {
            for (int i = 1; i < nums.length; ++i)
                nums[i] += nums[i - 1];
            this.sum = nums;
        }

        public int sumRange(int i, int j) {
            return sum[j] - (i == 0 ? 0 : sum[i - 1]);
        }
    }



}
