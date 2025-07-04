package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-k-pairs-with-smallest-sums/description/

//import jdk.internal.net.http.common.Pair;

import java.util.*;

/**
 * 373. Find K Pairs with Smallest Sums
 * Medium
 * Topics
 * Companies
 * You are given two integer arrays nums1 and nums2 sorted in non-decreasing order and an integer k.
 *
 * Define a pair (u, v) which consists of one element from the first array and one element from the second array.
 *
 * Return the k pairs (u1, v1), (u2, v2), ..., (uk, vk) with the smallest sums.
 *
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,7,11], nums2 = [2,4,6], k = 3
 * Output: [[1,2],[1,4],[1,6]]
 * Explanation: The first 3 pairs are returned from the sequence: [1,2],[1,4],[1,6],[7,2],[7,4],[11,2],[7,6],[11,4],[11,6]
 * Example 2:
 *
 * Input: nums1 = [1,1,2], nums2 = [1,2,3], k = 2
 * Output: [[1,1],[1,1]]
 * Explanation: The first 2 pairs are returned from the sequence: [1,1],[1,1],[1,2],[2,1],[1,2],[2,2],[1,3],[1,3],[2,3]
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 105
 * -109 <= nums1[i], nums2[i] <= 109
 * nums1 and nums2 both are sorted in non-decreasing order.
 * 1 <= k <= 104
 * k <= nums1.length * nums2.length
 *
 *
 */
public class FindKPairsWithSmallestSums {

    // V0
//    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
//
//    }

    // V0-1
    // IDEA: PQ (fixed by gpt)
    /**
     *  IDEA:
     *
     *  ✅ Use a min-heap (priority queue) to:
     *
     *  - Always retrieve the next smallest sum pair
     *
     *  - Efficiently keep track of candidates
     *
     */
    public List<List<Integer>> kSmallestPairs_0_1(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> res = new ArrayList<>();

        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0 || k <= 0) {
            return res;
        }

        // Min-heap to store [sum, index in nums1, index in nums2]
        /**
         *  NOTE !!!
         *
         *  min PQ structure:
         *
         *   [ sum, nums_1_idx, nums_2_idx ]
         *
         *
         *   - Heap stores: int[] {sum, index in nums1, index in nums2}
         *
         *   - It's sorted by sum = nums1[i] + nums2[j]
         *
         */
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        // Add the first k pairs (nums1[0] + nums2[0...k])
        /**  NOTE !!!
         *
         *  we init PQ as below:
         *
         *  - We insert first k pairs: (nums1[i], nums2[0])
         *
         *   - Why nums2[0]?
         *     -> Because nums2 is sorted,
         *       so (nums1[i], nums2[0]) is the smallest possible for that row.
         *
         *
         *   -> so, we insert `nums_1[i] + nums_2[0]`  to PQ for now
         *
         *
         */
        for (int i = 0; i < nums1.length && i < k; i++) {
            minHeap.offer(new int[] { nums1[i] + nums2[0], i, 0 });
        }

        /** NOTE !!!   Pop from Heap and Expand
         *
         * - Poll the `smallest` sum pair (i, j) and add it to result.
         *
         * - You now consider the next element in that row, which is (i, j + 1).
         *
         */
        while (k > 0 && !minHeap.isEmpty()) {

            // current smallest val from PQ
            int[] current = minHeap.poll();
            int i = current[1]; // index in nums1
            int j = current[2]; // index in nums2

            res.add(Arrays.asList(nums1[i], nums2[j]));

            /**
             *  NOTE !!! Push the Next Pair in the Same Row
             *
             *  - This ensures you're exploring pairs in increasing sum order:
             *
             *      - From (i, 0) → (i, 1) → (i, 2) ...
             *
             * - Since the arrays are sorted, this gives increasing sums
             *
             *
             */
            if (j + 1 < nums2.length) {
                minHeap.offer(new int[] { nums1[i] + nums2[j + 1], i, j + 1 });
            }

            k--;
        }

        return res;
    }

    // V1
    // https://leetcode.com/problems/find-k-pairs-with-smallest-sums/editorial/
    // IDEA: PQ
//    public List<List<Integer>> kSmallestPairs_1(int[] nums1, int[] nums2, int k) {
//        int m = nums1.length;
//        int n = nums2.length;
//
//        List<List<Integer>> ans = new ArrayList<>();
//        Set<Pair<Integer, Integer>> visited = new HashSet<>();
//
//        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> (a[0] - b[0]));
//        minHeap.offer(new int[] { nums1[0] + nums2[0], 0, 0 });
//        visited.add(new Pair<Integer, Integer>(0, 0));
//
//        while (k-- > 0 && !minHeap.isEmpty()) {
//            int[] top = minHeap.poll();
//            int i = top[1];
//            int j = top[2];
//
//            ans.add(List.of(nums1[i], nums2[j]));
//
//            if (i + 1 < m && !visited.contains(new Pair<Integer, Integer>(i + 1, j))) {
//                minHeap.offer(new int[] { nums1[i + 1] + nums2[j], i + 1, j });
//                visited.add(new Pair<Integer, Integer>(i + 1, j));
//            }
//
//            if (j + 1 < n && !visited.contains(new Pair<Integer, Integer>(i, j + 1))) {
//                minHeap.offer(new int[] { nums1[i] + nums2[j + 1], i, j + 1 });
//                visited.add(new Pair<Integer, Integer>(i, j + 1));
//            }
//        }
//
//        return ans;
//    }

    // V2
    // https://leetcode.com/problems/find-k-pairs-with-smallest-sums/solutions/3686980/from-dumb-to-pro-with-just-one-visit-my-u1ig6/
    // IDEA: PQ
    public List<List<Integer>> kSmallestPairs_2(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> resV = new ArrayList<>(); // Result list to store the pairs
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        // Priority queue to store pairs with smallest sums, sorted by the sum

        // Push the initial pairs into the priority queue
        for (int x : nums1) {
            pq.offer(new int[] { x + nums2[0], 0 }); // The sum and the index of the second element in nums2
        }

        // Pop the k smallest pairs from the priority queue
        while (k > 0 && !pq.isEmpty()) {
            int[] pair = pq.poll();
            int sum = pair[0]; // Get the smallest sum
            int pos = pair[1]; // Get the index of the second element in nums2

            List<Integer> currentPair = new ArrayList<>();
            currentPair.add(sum - nums2[pos]);
            currentPair.add(nums2[pos]);
            resV.add(currentPair); // Add the pair to the result list

            // If there are more elements in nums2, push the next pair into the priority queue
            if (pos + 1 < nums2.length) {
                pq.offer(new int[] { sum - nums2[pos] + nums2[pos + 1], pos + 1 });
            }

            k--; // Decrement k
        }

        return resV; // Return the k smallest pairs
    }

    // V3
    // https://leetcode.com/problems/find-k-pairs-with-smallest-sums/solutions/6616196/simple-min-heap-priority-queue-by-kdhaka-8np5/
    // IDEA: PQ
//    public List<List<Integer>> kSmallestPairs_3(int[] nums1, int[] nums2, int k) {
//        List<List<Integer>> list = new ArrayList<>();
//
//        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
//
//        for (int x : nums1) {
//            pq.offer(new int[] { x + nums2[0], 0 });
//        }
//
//        while (k > 0 && !pq.isEmpty()) {
//            int[] pair = pq.poll();
//            int sum = pair[0];
//            int pos = pair[1];
//
//            list.add(List.of(sum - nums2[pos], nums2[pos]));
//
//            if (pos + 1 < nums2.length) {
//                pq.offer(new int[] { sum - nums2[pos] + nums2[pos + 1], pos + 1 });
//            }
//            k--;
//        }
//
//        return list;
//    }


}
