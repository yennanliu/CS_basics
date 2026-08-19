package LeetCodeJava.Heap;

// https://leetcode.com/problems/minimum-difference-in-sums-after-removal-of-elements/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 *  2163. Minimum Difference in Sums After Removal of Elements
 *  Hard
 *
 *  You are given a 0-indexed integer array nums consisting of 3 * n elements.
 *
 *  You are allowed to remove any subsequence of elements of size exactly n from
 *  nums. The remaining 2 * n elements will be divided into two equal parts:
 *   - the first n elements belong to the first part, sum = sumFirst
 *   - the next n elements belong to the second part, sum = sumSecond
 *
 *  Return the minimum possible value of sumFirst - sumSecond.
 *
 *  Example 1:
 *    Input: nums = [3,1,2]
 *    Output: -1
 *    Explanation: n = 1. removing nums[0] leaves [1,2] -> 1 - 2 = -1, the minimum.
 *
 *  Example 2:
 *    Input: nums = [7,9,5,8,1,3]
 *    Output: 1
 *    Explanation: n = 2. removing 9 and 1 leaves [7,5,8,3] -> (7+5) - (8+3) = 1.
 *
 *  Constraints:
 *    nums.length == 3 * n
 *    1 <= n <= 10^5
 *    1 <= nums[i] <= 10^5
 */
public class MinimumDifferenceInSumsAfterRemovalOfElements {

    // V0
    // IDEA: SPLIT POINT + TWO HEAPS (minimise the left sum, maximise the right)
    //       the kept elements keep their original order, so there is a cut index i:
    //       the first part comes from nums[0..i-1], the second from nums[i..],
    //       with i ranging over [n, 2n].
    //         left[i]  = SMALLEST sum of n elements from nums[0..i-1]
    //                    -> sweep forward keeping the n smallest in a MAX-heap
    //         right[i] = LARGEST sum of n elements from nums[i..]
    //                    -> mirror sweep with a MIN-heap
    //       answer = min over i of (left[i] - right[i]).
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public long minimumDifference(int[] nums) {
        int total = nums.length;
        int n = total / 3;

        // left[i] : min sum of n elements among nums[0..i-1], for i in [n, 2n]
        long[] left = new long[total + 1];
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        long cur = 0L;
        for (int i = 0; i < 2 * n; i++) {
            maxHeap.add(nums[i]);
            cur += nums[i];
            if (maxHeap.size() > n) {
                cur -= maxHeap.poll(); // drop the largest kept so far
            }
            if (i + 1 >= n) {
                left[i + 1] = cur;
            }
        }

        // right[i] : max sum of n elements among nums[i..], for i in [n, 2n]
        long[] right = new long[total + 1];
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        cur = 0L;
        for (int i = total - 1; i >= n; i--) {
            minHeap.add(nums[i]);
            cur += nums[i];
            if (minHeap.size() > n) {
                cur -= minHeap.poll(); // drop the smallest kept so far
            }
            if (total - i >= n) {
                right[i] = cur;
            }
        }

        long res = Long.MAX_VALUE;
        for (int i = n; i <= 2 * n; i++) {
            res = Math.min(res, left[i] - right[i]);
        }
        return res;
    }
}
