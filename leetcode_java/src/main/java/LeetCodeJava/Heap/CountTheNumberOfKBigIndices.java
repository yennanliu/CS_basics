package LeetCodeJava.Heap;

// https://leetcode.com/problems/count-the-number-of-k-big-indices/

import java.util.PriorityQueue;

/**
 *  2519. Count the Number of K-Big Indices
 *  Hard
 *
 *  You are given a 0-indexed integer array nums and a positive integer k.
 *
 *  We call an index i k-big if the following conditions are satisfied:
 *    - There exist at least k different indices idx1 such that idx1 < i and nums[idx1] < nums[i].
 *    - There exist at least k different indices idx2 such that idx2 > i and nums[idx2] < nums[i].
 *
 *  Return the number of k-big indices.
 *
 *  Example 1:
 *    Input: nums = [2,3,6,5,2,3], k = 2
 *    Output: 2
 *    Explanation: the 2-big indices are i = 2 and i = 3.
 *
 *  Example 2:
 *    Input: nums = [1,1,1], k = 3
 *    Output: 0
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i], k <= nums.length
 */
public class CountTheNumberOfKBigIndices {

    // V0
    // IDEA: TWO SIZE-K MAX HEAPS (keep only the k smallest values on each side)
    //
    //   "at least k earlier elements are smaller than nums[i]" is the same as
    //   "the k-th smallest value among nums[0..i-1] is < nums[i]", so the whole
    //   prefix is not needed - only its k smallest values.
    //
    //   Keep a MAX-heap capped at size k of the k smallest values seen so far;
    //   its root is exactly the k-th smallest, so the test is root < nums[i]
    //   (with size == k required, else fewer than k candidates exist at all).
    //
    //   Do one left-to-right pass and one right-to-left pass; an index is k-big
    //   iff both passes flagged it.
    //
    //   NOTE: the strict `<` matters (equal values do NOT count), and the current
    //         value is pushed only AFTER being tested so it never counts itself.
    /**
     * time = O(n log k)
     * space = O(n)
     */
    public int kBigIndices(int[] nums, int k) {
        int n = nums.length;
        boolean[] left = scan(nums, k, true);
        boolean[] right = scan(nums, k, false);

        int res = 0;
        for (int i = 0; i < n; i++) {
            if (left[i] && right[i]) {
                res++;
            }
        }
        return res;
    }

    /** forward = true scans left-to-right, otherwise right-to-left. */
    private boolean[] scan(int[] nums, int k, boolean forward) {
        int n = nums.length;
        boolean[] flag = new boolean[n];
        // max-heap of the k smallest values seen so far
        PriorityQueue<Integer> heap = new PriorityQueue<>(java.util.Collections.reverseOrder());

        for (int step = 0; step < n; step++) {
            int i = forward ? step : (n - 1 - step);
            int v = nums[i];
            if (heap.size() == k && heap.peek() < v) {
                flag[i] = true;
            }
            heap.add(v);
            if (heap.size() > k) {
                heap.poll();
            }
        }
        return flag;
    }
}
