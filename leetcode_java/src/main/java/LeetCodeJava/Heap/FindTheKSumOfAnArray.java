package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-the-k-sum-of-an-array/

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  2386. Find the K-Sum of an Array
 *  Hard
 *
 *  You are given an integer array nums and a positive integer k. You can choose any
 *  subsequence of the array and sum all of its elements together.
 *
 *  We define the K-Sum of the array as the kth largest subsequence sum that can be
 *  obtained (not necessarily distinct). Return the K-Sum of the array.
 *
 *  Note that the empty subsequence is considered to have a sum of 0.
 *
 *  Example 1:
 *    Input: nums = [2,4,-2], k = 5
 *    Output: 2
 *    Explanation: the sums in decreasing order are 6, 4, 4, 2, 2, 0, 0, -2, so the
 *                 5th largest is 2.
 *
 *  Example 2:
 *    Input: nums = [1,-2,3,4,-10,12], k = 16
 *    Output: 10
 *
 *  Constraints:
 *    n == nums.length
 *    1 <= n <= 10^5
 *    -10^9 <= nums[i] <= 10^9
 *    1 <= k <= min(2000, 2^n)
 */
public class FindTheKSumOfAnArray {

    // V0
    // IDEA: REDUCE TO "k SMALLEST SUBSET SUMS OF THE ABSOLUTE VALUES"
    //
    //   the largest possible sum takes every positive element:
    //       total = sum(x for x in nums if x > 0)
    //   every other subsequence sum is total minus some non-negative "loss":
    //   dropping a positive x costs x, adding a negative x costs |x|. So the losses
    //   are exactly the subset sums of a = sorted(|nums|), and
    //
    //       k-th LARGEST subsequence sum = total - (k-th SMALLEST subset sum of a)
    //
    //   Those are enumerated in order with a min-heap over (loss, next index),
    //   expanding each popped state two ways:
    //       include a[i]                 -> (loss + a[i],            i + 1)
    //       swap a[i-1] out for a[i]     -> (loss + a[i] - a[i-1],   i + 1)
    //   which reaches every subset exactly once in non-decreasing loss order.
    //   Only k pops are needed, and k <= 2000.
    /**
     * time = O(n log n + k log k)
     * space = O(n + k)
     */
    public long kSum(int[] nums, int k) {
        int n = nums.length;
        long total = 0L;
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            if (nums[i] > 0) {
                total += nums[i];
            }
            a[i] = Math.abs((long) nums[i]);
        }
        Arrays.sort(a);

        // {loss so far, next index to consider}
        PriorityQueue<long[]> heap = new PriorityQueue<>(new Comparator<long[]>() {
            @Override
            public int compare(long[] x, long[] y) {
                return Long.compare(x[0], y[0]);
            }
        });
        heap.add(new long[]{0L, 0L});

        long res = total;
        for (int step = 0; step < k; step++) {
            long[] cur = heap.poll();
            long loss = cur[0];
            int i = (int) cur[1];
            res = total - loss;
            if (i < n) {
                heap.add(new long[]{loss + a[i], i + 1});
                if (i > 0) {
                    heap.add(new long[]{loss + a[i] - a[i - 1], i + 1});
                }
            }
        }
        return res;
    }
}
