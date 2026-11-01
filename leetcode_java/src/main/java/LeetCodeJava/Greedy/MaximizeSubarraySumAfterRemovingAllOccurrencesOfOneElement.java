package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximize-subarray-sum-after-removing-all-occurrences-of-one-element/description/

import java.util.*;

/**
 * 3410. Maximize Subarray Sum After Removing All Occurrences of One Element
 * Hard
 *
 * You are given an integer array nums.
 *
 * You can do the following operation on the array at most once:
 *
 * Choose any integer x such that nums remains non-empty on removing all
 * occurrences of x.
 * Remove all occurrences of x from the array.
 *
 * Return the maximum subarray sum across all possible resulting arrays.
 *
 * Example 1:
 *
 * Input: nums = [-3,2,-2,-1,3,-2,3]
 *
 * Output: 7
 *
 * Explanation:
 *
 * We can have the following arrays after at most one operation:
 *
 * The original array is nums = [-3, 2, -2, -1, 3, -2, 3]. The maximum subarray sum
 * is 3 + (-2) + 3 = 4.
 * Deleting all occurences of x = -3 results in nums = [2, -2, -1, 3, -2, 3]. The
 * maximum subarray sum is 3 + (-2) + 3 = 4.
 * Deleting all occurences of x = -2 results in nums = [-3, 2, -1, 3, 3]. The
 * maximum subarray sum is 2 + (-1) + 3 + 3 = 7.
 * Deleting all occurences of x = -1 results in nums = [-3, 2, -2, 3, -2, 3]. The
 * maximum subarray sum is 3 + (-2) + 3 = 4.
 * Deleting all occurences of x = 3 results in nums = [-3, 2, -2, -1, -2]. The
 * maximum subarray sum is 2.
 *
 * The output is max(4, 4, 7, 4, 2) = 7.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4]
 *
 * Output: 10
 *
 * Explanation:
 *
 * It is optimal to not perform any operations.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^6 <= nums[i] <= 10^6
 *
 */
public class MaximizeSubarraySumAfterRemovingAllOccurrencesOfOneElement {

    // V0
    // IDEA: MAX-SUBARRAY SEGMENT TREE, "DELETE" A VALUE BY RE-PRICING ITS CELLS TO 0
    //       deleting every copy of x then taking a subarray == keeping the copies but
    //       valuing them at 0. only x < 0 is worth deleting; plain Kadane covers
    //       "no operation". for each negative x: set its positions to 0, read the
    //       root's best subarray, set them back. each index is updated twice over all
    //       x, so O(n) point updates in total.
    //       node = (total, best prefix, best suffix, best anywhere) — the classic merge.
    //       guards: x that fills the whole array cannot be deleted; an all-negative
    //       array is answered by its largest element up front.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long maxSubarraySum(int[] nums) {
        int n = nums.length;

        // Kadane: the answer with no deletion
        long best = Long.MIN_VALUE;
        long running = 0;
        for (int v : nums) {
            running = (running < 0) ? v : running + v;
            best = Math.max(best, running);
        }
        if (best < 0) {
            // every element is negative: deleting can't help, the max single element wins
            return best;
        }

        // positions of each negative value
        Map<Integer, List<Integer>> positions = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (nums[i] < 0) {
                positions.computeIfAbsent(nums[i], key -> new ArrayList<>()).add(i);
            }
        }
        if (positions.isEmpty()) {
            return best;
        }

        // segment tree over the array, leaves at [size, 2 * size)
        size = 1;
        while (size < n) {
            size *= 2;
        }
        total = new long[2 * size];
        prefix = new long[2 * size];
        suffix = new long[2 * size];
        bestInside = new long[2 * size];
        Arrays.fill(prefix, NEG_INF);
        Arrays.fill(suffix, NEG_INF);
        Arrays.fill(bestInside, NEG_INF);
        for (int i = 0; i < n; i++) {
            setLeaf(i, nums[i]);
        }
        for (int node = size - 1; node >= 1; node--) {
            pull(node);
        }

        for (Map.Entry<Integer, List<Integer>> entry : positions.entrySet()) {
            int value = entry.getKey();
            List<Integer> indexes = entry.getValue();
            if (indexes.size() == n) {
                continue; // deleting it would empty the array
            }
            for (int i : indexes) {
                update(i, 0);
            }
            best = Math.max(best, bestInside[1]);
            for (int i : indexes) {
                update(i, value);
            }
        }
        return best;
    }

    // padding leaves must never be chosen; /4 keeps sums of two of them from overflowing
    private static final long NEG_INF = Long.MIN_VALUE / 4;

    private int size;
    private long[] total;
    private long[] prefix;
    private long[] suffix;
    private long[] bestInside;

    /** Writes value v into leaf i without updating ancestors. */
    private void setLeaf(int i, long v) {
        int leaf = size + i;
        total[leaf] = v;
        prefix[leaf] = v;
        suffix[leaf] = v;
        bestInside[leaf] = v;
    }

    /** Recomputes node from its two children (classic max-subarray merge). */
    private void pull(int node) {
        int left = 2 * node;
        int right = 2 * node + 1;
        total[node] = total[left] + total[right];
        prefix[node] = Math.max(prefix[left], total[left] + prefix[right]);
        suffix[node] = Math.max(suffix[right], total[right] + suffix[left]);
        // the only new candidate: a subarray crossing the middle
        long crossing = suffix[left] + prefix[right];
        bestInside[node] = Math.max(crossing, Math.max(bestInside[left], bestInside[right]));
    }

    /** Point update: leaf i becomes v, then every ancestor is recomputed. */
    private void update(int i, long v) {
        setLeaf(i, v);
        int node = (size + i) / 2;
        while (node >= 1) {
            pull(node);
            node /= 2;
        }
    }
}
