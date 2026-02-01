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
//    /**
     * time = O(k log k)
     * space = O(k)
     */
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
//
//    }

    // V0-1
    // IDEA: PQ + get first J smallest pair + get extend PQ (fixed by gpt)
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
    /**
     * time = O(k log k)
     * space = O(k)
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

    // V0-2
    // IDEA: PQ + get first J smallest pair + get extend PQ (fixed by gpt)
    /**
     * time = O(k log k)
     * space = O(k)
     */
    public List<List<Integer>> kSmallestPairs_0_2(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0 || k <= 0) {
            return res;
        }

        // Min-heap ordered by pair sum
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> (nums1[a[0]] + nums2[a[1]]) - (nums1[b[0]] + nums2[b[1]]));

        // push first k pairs (nums1[i], nums2[0])
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
        for (int i = 0; i < Math.min(nums1.length, k); i++) {
            pq.offer(new int[] { i, 0 });
        }

        // extract k smallest pairs
        while (k > 0 && !pq.isEmpty()) {
            int[] cur = pq.poll();
            int i = cur[0], j = cur[1];
            res.add(Arrays.asList(nums1[i], nums2[j]));
            k--;

            // move to next pair in nums2 for this i
            if (j + 1 < nums2.length) {
                pq.offer(new int[] { i, j + 1 });
            }
        }

        return res;
    }

    // V0-3
    // IDEA: PQ + get first J smallest pair + get extend PQ (fixed by gemini)
    /**
     * time = O(k log k)
     * space = O(k)
     */
    public List<List<Integer>> kSmallestPairs_0_3(int[] nums1, int[] nums2, int k) {

        // Final result list
        List<List<Integer>> result = new ArrayList<>();
        if (nums1.length == 0 || nums2.length == 0 || k == 0) {
            return result;
        }

        // Min-Heap stores: {sum, index_in_nums1, index_in_nums2}
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(a -> a[0]) // Sort by sum (index 0)
        );

        int m = nums1.length;
        int n = nums2.length;

        // 1. Initial State: Only add the first element of nums2 (index 0)
        // combined with every element in nums1.
        // We only need to start the search from the first column of the conceptual matrix.
        for (int i = 0; i < m && i < k; i++) {
            int sum = nums1[i] + nums2[0];
            // Store {sum, i, 0}
            minHeap.offer(new int[]{sum, i, 0});
        }

        // 2. Extract and Explore K times
        while (k-- > 0 && !minHeap.isEmpty()) {
            int[] current = minHeap.poll();

            int i = current[1]; // index from nums1
            int j = current[2]; // index from nums2

            // Add the pair to the result
            result.add(Arrays.asList(nums1[i], nums2[j]));

            // 3. Explore Successor: Move to the next element in nums2
            // The only new pair we MUST check is (nums1[i], nums2[j+1]).
            int next_j = j + 1;

            if (next_j < n) {
                int sum = nums1[i] + nums2[next_j];
                // Store {sum, i, next_j}
                minHeap.offer(new int[]{sum, i, next_j});
            }
        }

        return result;
    }

    // V0-4
    // IDEA: BIG + SMALL PQ + BRUTE FORCE (fixed by gpt)
    // TODO: fix TLE
    /**
     * time = O(k log k)
     * space = O(k)
     */
    public List<List<Integer>> kSmallestPairs_0_4(int[] nums1, int[] nums2, int k) {
        // edge
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0 || k <= 0) {
            return new ArrayList<>();
        }

        // Step 1: Big PQ (max heap) to keep smallest k pairs
        PriorityQueue<Integer[]> bigPQ = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                // Max heap: bigger sum comes first
                return (o2[0] + o2[1]) - (o1[0] + o1[1]);
            }
        });

        // Step 2: Brute force iterate over all pairs
        for (int i = 0; i < nums1.length; i++) {
            for (int j = 0; j < nums2.length; j++) {
                Integer[] pair = new Integer[] { nums1[i], nums2[j] };
                bigPQ.offer(pair);

                // Keep heap size <= k
                if (bigPQ.size() > k) {
                    bigPQ.poll(); // remove largest sum
                }
            }
        }

        // Step 3: Convert bigPQ to smallPQ (ascending order)
        PriorityQueue<Integer[]> smallPQ = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                return (o1[0] + o1[1]) - (o2[0] + o2[1]);
            }
        });

        while (!bigPQ.isEmpty()) {
            smallPQ.offer(bigPQ.poll());
        }

        // Step 4: Build result list
        List<List<Integer>> res = new ArrayList<>();
        while (!smallPQ.isEmpty() && res.size() < k) {
            Integer[] arr = smallPQ.poll();
            res.add(Arrays.asList(arr[0], arr[1]));
        }

        return res;
    }


    // V1
    // https://leetcode.com/problems/find-k-pairs-with-smallest-sums/editorial/
    // IDEA: PQ
//    /**
     * time = O(k log k)
     * space = O(k)
     */
    public List<List<Integer>> kSmallestPairs_1(int[] nums1, int[] nums2, int k) {
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
    /**
     * time = O(k log k)
     * space = O(k)
     */
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
//    /**
     * time = O(k log k)
     * space = O(k)
     */
    public List<List<Integer>> kSmallestPairs_3(int[] nums1, int[] nums2, int k) {
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
