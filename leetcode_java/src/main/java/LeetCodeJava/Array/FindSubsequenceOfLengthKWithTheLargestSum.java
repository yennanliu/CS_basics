package LeetCodeJava.Array;

// https://leetcode.com/problems/find-subsequence-of-length-k-with-the-largest-sum/description/

import java.util.*;

/**
 * 2099. Find Subsequence of Length K With the Largest Sum
 * Easy
 *
 * You are given an integer array nums and an integer k. You want to find a subsequence of nums of length k that has the largest sum.
 *
 * Return any such subsequence as an integer array of length k.
 *
 * A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1,3,3], k = 2
 * Output: [3,3]
 * Explanation:
 * The subsequence has the largest sum of 3 + 3 = 6.
 *
 * Example 2:
 *
 * Input: nums = [-1,-2,3,4], k = 3
 * Output: [-1,3,4]
 * Explanation:
 * The subsequence has the largest sum of -1 + 3 + 4 = 6.
 *
 * Example 3:
 *
 * Input: nums = [3,4,3,3], k = 2
 * Output: [3,4]
 * Explanation:
 * The subsequence has the largest sum of 3 + 4 = 7.
 * Another possible subsequence is [4, 3].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * -10^5 <= nums[i] <= 10^5
 * 1 <= k <= nums.length
 *
 */
public class FindSubsequenceOfLengthKWithTheLargestSum {

    // V0
    // IDEA: SORT INDICES BY VALUE, KEEP THE TOP k, THEN RESTORE ORIGINAL ORDER
    /**
     *  The sum ignores order, so the best k values are the k largest. The output
     *  must still be a subsequence, so remember which INDICES won and emit them
     *  in increasing index order.
     *
     * time = O(N log N)
     * space = O(N)
     */
    public int[] maxSubsequence(int[] nums, int k) {
        int n = nums.length;
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        // largest value first
        Arrays.sort(indices, (a, b) -> Integer.compare(nums[b], nums[a]));

        // the winners, put back in their original order
        int[] chosen = new int[k];
        for (int i = 0; i < k; i++) {
            chosen[i] = indices[i];
        }
        Arrays.sort(chosen);

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = nums[chosen[i]];
        }
        return result;
    }

    // V1
    // IDEA: SIZE-k MIN-HEAP OF (VALUE, INDEX), THEN SORT THE SURVIVORS BY INDEX
    /**
     *  Keep the best k (value, index) pairs seen so far; whenever the heap holds
     *  k + 1, evict the smallest. Better when k is much smaller than n.
     *
     * time = O(N log K)
     * space = O(K)
     */
    public int[] maxSubsequence_1(int[] nums, int k) {
        // min-heap on value: the root is the weakest of the current top k
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 0; i < nums.length; i++) {
            heap.offer(new int[] {nums[i], i});
            if (heap.size() > k) {
                heap.poll();
            }
        }

        List<int[]> survivors = new ArrayList<>(heap);
        // restore the subsequence order
        survivors.sort((a, b) -> Integer.compare(a[1], b[1]));

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = survivors.get(i)[0];
        }
        return result;
    }
}
