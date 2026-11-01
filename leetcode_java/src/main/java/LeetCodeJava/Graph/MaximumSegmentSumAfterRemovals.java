package LeetCodeJava.Graph;

// https://leetcode.com/problems/maximum-segment-sum-after-removals/description/

/**
 * 2382. Maximum Segment Sum After Removals
 * Hard
 *
 * You are given two 0-indexed integer arrays nums and removeQueries, both of length n. For the ith query, the element in nums at the index removeQueries[i] is removed, splitting nums into different segments.
 *
 * A segment is a contiguous sequence of positive integers in nums. A segment sum is the sum of every element in a segment.
 *
 * Return an integer array answer, of length n, where answer[i] is the maximum segment sum after applying the ith removal.
 *
 * Note: The same index will not be removed more than once.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,5,6,1], removeQueries = [0,3,2,4,1]
 * Output: [14,7,2,2,0]
 * Explanation: Using 0 to indicate a removed element, the answer is as follows:
 * Query 1: Remove the 0th element, nums becomes [0,2,5,6,1] and the maximum segment sum is 14 for segment [2,5,6,1].
 * Query 2: Remove the 3rd element, nums becomes [0,2,5,0,1] and the maximum segment sum is 7 for segment [2,5].
 * Query 3: Remove the 2nd element, nums becomes [0,2,0,0,1] and the maximum segment sum is 2 for segment [2].
 * Query 4: Remove the 4th element, nums becomes [0,2,0,0,0] and the maximum segment sum is 2 for segment [2].
 * Query 5: Remove the 1st element, nums becomes [0,0,0,0,0] and the maximum segment sum is 0 for segment [].
 * Finally, we return [14,7,2,2,0].
 *
 * Example 2:
 *
 * Input: nums = [3,2,11,1], removeQueries = [3,2,1,0]
 * Output: [16,5,3,0]
 * Explanation: Using 0 to indicate a removed element, the answer is as follows:
 * Query 1: Remove the 3rd element, nums becomes [3,2,11,0] and the maximum segment sum is 16 for segment [3,2,11].
 * Query 2: Remove the 2nd element, nums becomes [3,2,0,0] and the maximum segment sum is 5 for segment [3,2].
 * Query 3: Remove the 1st element, nums becomes [3,0,0,0] and the maximum segment sum is 3 for segment [3].
 * Query 4: Remove the 0th element, nums becomes [0,0,0,0] and the maximum segment sum is 0 for segment [].
 * Finally, we return [16,5,3,0].
 *
 *
 * Constraints:
 *
 * n == nums.length == removeQueries.length
 * 1 <= n <= 10^5
 * 1 <= nums[i] <= 10^9
 * 0 <= removeQueries[i] < n
 * All the values of removeQueries are unique.
 *
 */
public class MaximumSegmentSumAfterRemovals {

    // V0
    // IDEA: RUN THE QUERIES BACKWARDS SO REMOVALS BECOME INSERTIONS (UNION-FIND)
    //       splitting segments is hard to maintain; merging them is easy. so start
    //       from the fully-removed array and add the indices back in reverse order.
    //       adding index i creates a segment of sum nums[i], then unions it with the
    //       left / right neighbours that are already present. the running best only
    //       grows, which is exactly what union-find supports.
    //       the answer for query t is the best BEFORE index removeQueries[t] is added
    //       back -- record first, then insert.
    /**
     * time = O(N * alpha(N))
     * space = O(N)
     */
    public long[] maximumSegmentSum(int[] nums, int[] removeQueries) {
        int n = nums.length;
        int[] parent = new int[n];
        long[] segmentSum = new long[n];      // valid only at a root
        boolean[] present = new boolean[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        long[] answer = new long[n];
        long best = 0;
        for (int t = n - 1; t >= 0; t--) {
            // state after query t == state before index removeQueries[t] comes back
            answer[t] = best;

            int index = removeQueries[t];
            present[index] = true;
            segmentSum[index] = nums[index];

            // merge with the neighbours that are already back in the array
            if (index - 1 >= 0 && present[index - 1]) {
                union(parent, segmentSum, index, index - 1);
            }
            if (index + 1 < n && present[index + 1]) {
                union(parent, segmentSum, index, index + 1);
            }
            best = Math.max(best, segmentSum[find(parent, index)]);
        }
        return answer;
    }

    /** Root of x, compressing the path on the way up. */
    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    /** Merges the segments of a and b, keeping the combined sum at the new root. */
    private void union(int[] parent, long[] segmentSum, int a, int b) {
        int rootA = find(parent, a);
        int rootB = find(parent, b);
        if (rootA == rootB) {
            return;
        }
        parent[rootB] = rootA;
        segmentSum[rootA] += segmentSum[rootB];
    }
}
