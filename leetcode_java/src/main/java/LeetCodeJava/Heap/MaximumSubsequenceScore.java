package LeetCodeJava.Heap;

// https://leetcode.com/problems/maximum-subsequence-score/description/

import java.util.Arrays;
import java.util.Comparator;
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

    // V0-1
    // IDEA: SORT + PQ (fixed by gpt)
    public long maxScore_0_1(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;

        int[][] pairs = new int[n][2];
        for (int i = 0; i < n; i++) {
            pairs[i][0] = nums2[i];
            pairs[i][1] = nums1[i];
        }

        // Sort by nums2 descending
        Arrays.sort(pairs, (a, b) -> b[0] - a[0]);

        // min-heap for largest k nums1
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        long sum = 0;
        long result = 0;

        for (int i = 0; i < n; i++) {
            sum += pairs[i][1];
            minHeap.add(pairs[i][1]);

            if (minHeap.size() > k) {
                sum -= minHeap.poll(); // remove smallest nums1
            }

            if (minHeap.size() == k) {
                long candidate = sum * pairs[i][0]; // pairs[i][0] = nums2 (current min)
                result = Math.max(result, candidate);
            }
        }

        return result;
    }

    // V0-2
    // IDEA: PQ (gemini)
    /**
     * Finds the maximum score: (Sum of k largest nums1) * (Min of corresponding nums2).
     * Time Complexity: O(N log N + N log K)
     * Space Complexity: O(N + K)
     */
    public long maxScore_0_2(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;

        // 1. Combine nums1 and nums2 into pairs (nums1[i], nums2[i])
        // Use a 2D array: [nums1 value, nums2 value]
        int[][] pairs = new int[n][2];
        for (int i = 0; i < n; i++) {
            pairs[i][0] = nums1[i];
            pairs[i][1] = nums2[i];
        }

        // 2. Sort the pairs based on nums2[i] in DESCENDING order.
        // This greedy step ensures that as we iterate, pairs[i][1] is the minimum
        // nums2 value for any subset chosen from pairs[0...i].
        Arrays.sort(pairs, (a, b) -> b[1] - a[1]);

        // 3. Min-Heap (PriorityQueue) to maintain the K largest nums1 values seen so far.
        // We use a Min-Heap so the smallest element (the one to be replaced) is at the top.
        PriorityQueue<Integer> topK_nums1 = new PriorityQueue<>(k);

        long currentSum_nums1 = 0;
        long maxScore = 0;

        // 4. Iterate and calculate the max score
        for (int i = 0; i < n; i++) {
            int n1 = pairs[i][0];
            int n2_min = pairs[i][1];

            // Add the current nums1 value and update the sum
            topK_nums1.add(n1);
            currentSum_nums1 += n1;

            // If the heap size exceeds k, remove the smallest nums1 value to maintain the top K sum
            if (topK_nums1.size() > k) {
                int smallest_n1 = topK_nums1.poll();
                currentSum_nums1 -= smallest_n1;
            }

            // Once the heap has k elements, calculate the potential score
            if (topK_nums1.size() == k) {
                // Score = (Sum of top K nums1) * (Current nums2 min, which is pairs[i][1])
                long currentScore = currentSum_nums1 * n2_min;
                maxScore = Math.max(maxScore, currentScore);
            }
        }

        return maxScore;
    }

    // V0-2
    // NOTE !!! below is WRONG (reason as below)
    /**
     *  why below logic is wrong ?
     *
     *  Your solution is incorrect because:
     *
     * 1. You separate into Case 1 and Case 2
     * 	•	Case 1: pick largest nums1
     * 	•	Case 2: pick smallest nums2
     *
     * This will not work, because the best combination depends
     * on the joint sorting of nums2, not independent extremes of nums1 or nums2.
     *
     * ❗ Correct logic:
     *
     * We must sort by nums2 descending, because the min(nums2)
     * of the chosen set is determined by the last picked element in this sorted order.
     *
     */
//    public long maxScore(int[] nums1, int[] nums2, int k) {
//        // edge
//        if(nums1.length == 1){
//            if(k == 1){
//                return (long) nums1[0] * nums2[0];
//            }
//            return 0L; // ???
//        }
//
//        // long res = 0L;
//
//        long res1 = 0L;
//        long res2 = 0L;
//
//        // - case 1: try to get ` 1st term` max, calculate above val
//        // NOTE !!! we want to get max val from nums1
//        // so we need a SMALL PQ
//        // small PQ: pq_1_1: [ [idx_1, val_1] , [idx_2, val_2], ..] ???
//        PriorityQueue<Integer[]> pq_1_1 =  new PriorityQueue<Integer[]>(new Comparator<Integer[]>() {
//            @Override
//            public int compare(Integer[] o1, Integer[] o2) {
//                int diff = o1[1] - o2[1];
//                return diff;
//            }
//        });
//
//        for(int i = 0; i < nums1.length; i++){
//            while(pq_1_1.size() > k){
//                pq_1_1.poll();
//            }
//            // NOTE !!!
//            pq_1_1.add(new Integer[]{i, nums1[i]});
//        }
//
//        int nums1_case1_sum = 0;
//        int nums2_case1_min = Integer.MAX_VALUE;
//
//        while(!pq_1_1.isEmpty()){
//            Integer[] cur = pq_1_1.poll();
//            int idx = cur[0];
//            // int val = cur[1];
//            nums1_case1_sum += nums1[idx];
//            nums2_case1_min = Math.min(nums2_case1_min, nums2[idx]);  // ???
//        }
//
//        res1 = ((long) nums1_case1_sum * nums2_case1_min);
//
//
//        // - case 2: try to get ` 2nd term` max, calculate above val
//        // NOTE !!! we want to get min val from nums2
//        // so we need a BIG PQ
//        // big PQ: pq_2_2: [ [idx_1, val_1] , [idx_2, val_2], ..] ???
//        PriorityQueue<Integer[]> pq_2_2 =  new PriorityQueue<Integer[]>(new Comparator<Integer[]>() {
//            @Override
//            public int compare(Integer[] o1, Integer[] o2) {
//                int diff = o2[1] - o1[1];
//                return diff;
//            }
//        });
//
//
//        for(int i = 0; i < nums2.length; i++){
//            while(pq_2_2.size() > k){
//                pq_2_2.poll();
//            }
//            // NOTE !!!
//            pq_2_2.add(new Integer[]{i, nums2[i]});
//        }
//
//
//        int nums1_case2_sum = 0;
//        int nums2_case2_min = Integer.MAX_VALUE;
//
//        while(!pq_2_2.isEmpty()){
//            Integer[] cur = pq_2_2.poll();
//            int idx = cur[0];
//            //int val = cur[1];
//            nums1_case2_sum += nums1[idx];
//            nums2_case2_min = Math.min(nums2_case2_min, nums2[idx]);  // ???
//        }
//
//        res2 = ((long) nums1_case2_sum * nums2_case2_min);
//
//        System.out.println(">>> res1 = " + res1 + ", res2 = " + res2);
//
//
//        return Math.max(res1, res2);
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
