package LeetCodeJava.Heap;

// https://leetcode.com/problems/maximum-subsequence-score/description/

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 2542. Maximum Subsequence Score
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given two 0-indexed integer arrays nums1 and nums2 of equal length n and a positive integer k. You must choose a subsequence of indices from nums1 of length k.
 *
 * For chosen indices i0, i1, ..., ik - 1, your score is defined as:
 *
 * The sum of the selected elements from nums1 multiplied with the minimum of the selected elements from nums2.
 * It can defined simply as: (nums1[i0] + nums1[i1] +...+ nums1[ik - 1]) * min(nums2[i0] , nums2[i1], ... ,nums2[ik - 1]).
 * Return the maximum possible score.
 *
 * A subsequence of indices of an array is a set that can be derived from the set {0, 1, ..., n-1} by deleting some or no elements.
 *
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,3,3,2], nums2 = [2,1,3,4], k = 3
 * Output: 12
 * Explanation:
 * The four possible subsequence scores are:
 * - We choose the indices 0, 1, and 2 with score = (1+3+3) * min(2,1,3) = 7.
 * - We choose the indices 0, 1, and 3 with score = (1+3+2) * min(2,1,4) = 6.
 * - We choose the indices 0, 2, and 3 with score = (1+3+2) * min(2,3,4) = 12.
 * - We choose the indices 1, 2, and 3 with score = (3+3+2) * min(1,3,4) = 8.
 * Therefore, we return the max score, which is 12.
 * Example 2:
 *
 * Input: nums1 = [4,2,3,1,1], nums2 = [7,5,10,9,6], k = 1
 * Output: 30
 * Explanation:
 * Choosing index 2 is optimal: nums1[2] * nums2[2] = 3 * 10 = 30 is the maximum possible score.
 *
 *
 * Constraints:
 *
 * n == nums1.length == nums2.length
 * 1 <= n <= 105
 * 0 <= nums1[i], nums2[j] <= 105
 * 1 <= k <= n
 *
 *
 *
 */
public class MaximumSubsequenceScore {

    // V0
//    public long maxScore(int[] nums1, int[] nums2, int k) {
//
//    }

    // V1
    // IDEA: PQ
    // https://leetcode.ca/2023-03-10-2542-Maximum-Subsequence-Score/
    public long maxScore_1(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;
        int[][] nums = new int[n][2];
        for (int i = 0; i < n; ++i) {
            nums[i] = new int[] {nums1[i], nums2[i]};
        }
        Arrays.sort(nums, (a, b) -> b[1] - a[1]);
        long ans = 0, s = 0;
        PriorityQueue<Integer> q = new PriorityQueue<>();
        for (int i = 0; i < n; ++i) {
            s += nums[i][0];
            q.offer(nums[i][0]);
            if (q.size() == k) {
                ans = Math.max(ans, s * nums[i][1]);
                s -= q.poll();
            }
        }
        return ans;
    }

    // V2
    // IDEA: PQ
    // https://leetcode.com/problems/maximum-subsequence-score/solutions/3082106/javacpython-priority-queue-by-lee215-s52z/
    public long maxScore_2(int[] speed, int[] efficiency, int k) {
        int n = speed.length;
        int[][] ess = new int[n][2];
        for (int i = 0; i < n; ++i)
            ess[i] = new int[] {efficiency[i], speed[i]};
        Arrays.sort(ess, (a, b) -> b[0] - a[0]);
        PriorityQueue<Integer> pq = new PriorityQueue<>(k, (a, b) -> a - b);
        long res = 0, sumS = 0;
        for (int[] es : ess) {
            pq.add(es[1]);
            sumS = (sumS + es[1]);
            if (pq.size() > k) sumS -= pq.poll();
            if (pq.size() == k) res = Math.max(res, (sumS * es[0]));
        }
        return res;
    }


    // V3
    // IDEA: PQ
    // https://leetcode.com/problems/maximum-subsequence-score/solutions/6127501/beats-100-proof-beginner-freindly-java-c-c4lm/
    public long maxScore_3(int[] speed, int[] efficiency, int k) {
        int n = speed.length;
        int[][] ess = new int[n][2];

        for (int i = 0; i < n; ++i)
            ess[i] = new int[] { efficiency[i], speed[i] };

        Arrays.sort(ess, (a, b) -> b[0] - a[0]);

        PriorityQueue<Integer> pq = new PriorityQueue<>(k, (a, b) -> a - b);

        long res = 0, sumS = 0;

        for (int[] es : ess) {
            pq.add(es[1]);
            sumS = (sumS + es[1]);

            if (pq.size() > k)
                sumS -= pq.poll();
            if (pq.size() == k)
                res = Math.max(res, (sumS * es[0]));
        }

        return res;
    }



}
