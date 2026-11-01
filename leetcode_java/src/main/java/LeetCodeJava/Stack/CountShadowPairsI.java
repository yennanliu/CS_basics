package LeetCodeJava.Stack;

// https://leetcode.com/problems/count-shadow-pairs-i/description/

import java.util.*;

/**
 * 4054. Count Shadow Pairs I
 * Medium
 *
 * You are given an integer array nums of length n.
 *
 * A pair of indices (i, j) is called a shadow pair if all of the following
 * conditions are satisfied:
 *
 * 0 <= i < j < n
 * nums[i] < nums[j]
 * There does not exist an index k such that i < k < j and
 * nums[k] < nums[i] < nums[j].
 *
 * Return the total number of shadow pairs.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,1,4,1,5]
 * Output: 3
 * Explanation:
 * (1, 2), (1, 4) and (3, 4) are shadow pairs.
 * for (1, 4) : nums[2] = 4 and nums[3] = 1 are not smaller than nums[1] = 1.
 *
 * Example 2:
 *
 * Input: nums = [6,7,6,6,7]
 * Output: 4
 * Explanation:
 * (0, 1), (0, 4), (2, 4) and (3, 4) are shadow pairs.
 * for (2, 4) : nums[3] = 6 is not smaller than nums[2] = 6.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3,4]
 * Output: 6
 * Explanation:
 * all 6 pairs qualify - nothing sitting between i and j is ever smaller
 * than nums[i].
 *
 *
 * Constraints:
 *
 * 3 <= n == nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class CountShadowPairsI {

    // V0
    // IDEA: MONOTONIC STACK (NEXT STRICTLY SMALLER) + THE EQUAL-VALUE CHAIN
    //
    //   the killing condition only mentions nums[i], so for a FIXED i the pair (i, j)
    //   dies the moment some k in (i, j) has nums[k] < nums[i].
    //
    //   let nextSmaller[i] = first index > i with a STRICTLY smaller value (n if none):
    //     - every j in (i, nextSmaller[i]) has nums[j] >= nums[i], so nothing between
    //       can kill the pair -> it only needs nums[j] > nums[i]
    //     - no j at or past nextSmaller[i] can pair
    //
    //   so  pairs(i) = (nextSmaller[i] - i - 1) - #{ j in (i, nextSmaller[i]) : nums[j] == nums[i] }
    //
    //   and the EQUAL ones chain: if the next index `same` with nums[same] == nums[i]
    //   lands before nextSmaller[i], then nextSmaller[same] == nextSmaller[i], so
    //   equalInside[i] = 1 + equalInside[same].
    //
    //   NOTE !!! the stack pops on `>=`, which is what makes nextSmaller[i] the first
    //            STRICTLY smaller one -- popping on `>` would stop at an equal value.
    //
    //   e.g. nums = [6,7,6,6,7], i = 0: nextSmaller = 5 -> 4 candidates, of which
    //        indices 2 and 3 equal 6 -> 4 - 2 = 2 pairs: (0,1) and (0,4)
    //
    //   brute force (for each i walk j right, stop at the first smaller value) is
    //   O(n^2) on an increasing array and TLEs at n = 10^5.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long shadowPairs(int[] nums) {
        // edge case: fewer than 2 elements -> no pair
        if (nums == null || nums.length < 2) {
            return 0;
        }

        int n = nums.length;

        int[] nextSmaller = new int[n];   // first idx > i whose value is strictly smaller
        int[] equalInside = new int[n];   // how many nums[j] == nums[i] sit inside (i, nextSmaller[i])
        Map<Integer, Integer> nearestSameIdx = new HashMap<>(); // value -> smallest idx > i seen so far

        // indices; values strictly increasing from bottom to top
        Deque<Integer> stack = new ArrayDeque<>();

        long pairs = 0;

        // right -> left, so the stack already holds everything after i
        for (int i = n - 1; i >= 0; i--) {
            // drop everything not strictly smaller than nums[i]
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                stack.pop();
            }

            if (stack.isEmpty()) {
                nextSmaller[i] = n;
            } else {
                nextSmaller[i] = stack.peek();
            }

            // equal values inside the window chain through the nearest equal one
            int same = nearestSameIdx.getOrDefault(nums[i], n);
            if (same < nextSmaller[i]) {
                equalInside[i] = 1 + equalInside[same];
            } else {
                equalInside[i] = 0;
            }

            // window (i, nextSmaller[i]) minus the equal values = strictly larger j's
            pairs += (nextSmaller[i] - i - 1) - equalInside[i];

            stack.push(i);
            nearestSameIdx.put(nums[i], i);
        }

        return pairs;
    }

    // V1
    // IDEA: MONOTONIC STACK OF [value, freq] GROUPS, LEFT -> RIGHT
    //
    //   scan left -> right; the stack holds every earlier index that is still a valid
    //   LEFT end (no smaller value has appeared after it), grouped by value with a
    //   frequency, values non-decreasing from bottom to top. `total` = sum of freqs.
    //
    //   for each x:
    //     1) pop every group with value > x -- x is smaller and now sits between
    //        them and every future j, so they can never be a left end again
    //     2) every remaining candidate has value <= x; those STRICTLY smaller pair
    //        with x: total minus the freq of the group equal to x (if on top)
    //     3) push x (merging into the top group when equal)
    /**
     * time = O(N)
     * space = O(N)
     */
    public long shadowPairs_1(int[] nums) {
        // edge case: fewer than 2 elements -> no pair
        if (nums == null || nums.length < 2) {
            return 0;
        }

        // each entry is {value, freq}
        Deque<long[]> stack = new ArrayDeque<>();
        long total = 0;   // number of live left-end candidates on the stack
        long pairs = 0;

        for (int x : nums) {
            // step 1: values > x can no longer be a valid left end
            while (!stack.isEmpty() && stack.peek()[0] > x) {
                total -= stack.peek()[1];
                stack.pop();
            }

            if (!stack.isEmpty() && stack.peek()[0] == x) {
                // step 2: equal values do not pair (nums[i] < nums[j] is strict)
                long[] top = stack.peek();
                pairs += total - top[1];

                // step 3: one more candidate with this value
                top[1]++;
            } else {
                // step 2: every remaining candidate is strictly smaller than x
                pairs += total;

                // step 3: start a new group for x
                stack.push(new long[] { x, 1 });
            }
            total++;
        }

        return pairs;
    }
}
