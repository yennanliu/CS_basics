package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximize-total-cost-of-alternating-subarrays/description/

import java.util.*;

public class maximizeTotalCostOfAlternatingSubarrays {

    // V0
    // IDEA: 1D DP with `2 states` (`+` sign / `-` sign) per index
    /**  NOTE !!!
     *
     *  1. the cost of a sub array nums[l...r] is
     *
     *       nums[l] - nums[l+1] + nums[l+2] - ...
     *
     *     -> so the FIRST element of EVERY sub array always gets a `+` sign,
     *        and the sign then ALTERNATES inside that sub array
     *
     *     -> e.g. the ONLY thing that matters at index i is
     *        `which sign nums[i] is taking`
     *
     *  2. DP def (2 states):
     *
     *      - add = max total cost of nums[0...i], where nums[i] took a `+` sign
     *      - sub = max total cost of nums[0...i], where nums[i] took a `-` sign
     *
     *  3. DP eq:
     *
     *      - add[i] = max(add[i-1], sub[i-1]) + nums[i]
     *
     *         -> nums[i] gets `+` either because it STARTS a new sub array
     *            (prev sign can be anything), or because the prev sign was `-`
     *
     *      - sub[i] = add[i-1] - nums[i]
     *
     *         -> nums[i] gets `-` ONLY if it CONTINUES a sub array
     *            whose prev element took a `+`
     *
     *  4. answer = max(add[n-1], sub[n-1])
     *
     *  -> NOTE the return type is `long`, since sum of nums can overflow int
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maximumTotalCost(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        /** NOTE !!!
         *
         *  nums[0] MUST take a `+` sign (it starts the 1st sub array),
         *  so the `sub` state is IMPOSSIBLE at idx = 0
         *  -> init it as a very small val
         */
        long add = nums[0];
        long sub = Long.MIN_VALUE / 4;

        for (int i = 1; i < nums.length; i++) {
            long newAdd = Math.max(add, sub) + nums[i];
            long newSub = add - nums[i];

            add = newAdd;
            sub = newSub;
        }

        return Math.max(add, sub);
    }

    // V1
    // IDEA : DP
    // https://leetcode.com/problems/maximize-total-cost-of-alternating-subarrays/solutions/5355800/simple-dp-approach-memorization-java-c-python/
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maximumTotalCost_1(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }

        // Initial values for the dynamic programming approach
        long secondPrev = nums[0];
        long firstPrev = Math.max((long) nums[0] + nums[1], (long) nums[0] - nums[1]);

        for (int i = 2; i < nums.length; i++) {
            // Calculate the maximum cost for the subarray ending at the current index
            long current = Math.max(secondPrev + nums[i - 1] - nums[i], firstPrev + nums[i]);
            // Update the previous values for the next iteration
            secondPrev = firstPrev;
            firstPrev = current;
        }

        return firstPrev;
    }

    // V2
    // IDEA : DP
    // https://leetcode.com/problems/maximize-total-cost-of-alternating-subarrays/solutions/5355138/dynamic-programming-and-space-optimized-beats-100-easy-to-understand/
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maximumTotalCost_2(int[] nums) {
        int n = nums.length;
        long addResult = nums[0];
        long subResult = nums[0];
        for (int i = 1; i < n; i++) {
            long tempAdd = Math.max(addResult, subResult) + nums[i];
            long tempSub = addResult - nums[i];

            addResult = tempAdd;
            subResult = tempSub;
        }
        return Math.max(addResult, subResult);
    }

}
