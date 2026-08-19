package LeetCodeJava.Heap;

// https://leetcode.com/problems/final-array-state-after-k-multiplication-operations-i/

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  3264. Final Array State After K Multiplication Operations I
 *  Easy
 *
 *  You are given an integer array nums, an integer k, and an integer multiplier.
 *
 *  You need to perform k operations on nums. In each operation:
 *    - Find the minimum value x in nums. If there are multiple occurrences of the
 *      minimum value, select the one that appears first.
 *    - Replace the selected minimum value x with x * multiplier.
 *
 *  Return an integer array denoting the final state of nums after performing all
 *  k operations.
 *
 *  Example 1:
 *    Input: nums = [2,1,3,5,6], k = 5, multiplier = 2
 *    Output: [8,4,6,5,6]
 *
 *  Example 2:
 *    Input: nums = [1,2], k = 3, multiplier = 4
 *    Output: [16,8]
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    1 <= nums[i] <= 100
 *    1 <= k <= 10
 *    1 <= multiplier <= 5
 */
public class FinalArrayStateAfterKMultiplicationOperationsI {

    // V0
    // IDEA: MIN-HEAP KEYED BY (VALUE, INDEX) - THE TIE-BREAK IS THE INDEX
    //
    //   "the first occurrence of the minimum" is exactly the ordering
    //   (value, index), so one heap reproduces the rule directly: each operation
    //   pops that pair, multiplies it, and pushes it back. The array is then
    //   rebuilt from the heap contents.
    //
    //   k is only 10 here; the sequel (LC 3266) pushes k to 10^9 and needs the
    //   bulk-multiplication shortcut.
    /**
     * time = O((n + k) log n)
     * space = O(n)
     */
    public int[] getFinalState(int[] nums, int k, int multiplier) {
        int n = nums.length;
        // {value, index}
        PriorityQueue<long[]> heap = new PriorityQueue<>(new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                if (a[0] != b[0]) {
                    return Long.compare(a[0], b[0]);
                }
                return Long.compare(a[1], b[1]);
            }
        });
        for (int i = 0; i < n; i++) {
            heap.add(new long[]{nums[i], i});
        }

        for (int step = 0; step < k; step++) {
            long[] cur = heap.poll();
            heap.add(new long[]{cur[0] * multiplier, cur[1]});
        }

        int[] res = new int[n];
        for (long[] e : heap) {
            res[(int) e[1]] = (int) e[0];
        }
        return res;
    }
}
