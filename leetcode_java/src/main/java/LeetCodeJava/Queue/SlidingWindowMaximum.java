package LeetCodeJava.Queue;

// https://leetcode.com/problems/sliding-window-maximum/description/

import java.util.*;

/**
 * 239. Sliding Window Maximum
 * Solved
 * Hard
 * Topics
 * Companies
 * Hint
 * You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right. You can only see the k numbers in the window. Each time the sliding window moves right by one position.
 *
 * Return the max sliding window.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [3,3,5,5,6,7]
 * Explanation:
 * Window position                Max
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7       3
 *  1 [3  -1  -3] 5  3  6  7       3
 *  1  3 [-1  -3  5] 3  6  7       5
 *  1  3  -1 [-3  5  3] 6  7       5
 *  1  3  -1  -3 [5  3  6] 7       6
 *  1  3  -1  -3  5 [3  6  7]      7
 * Example 2:
 *
 * Input: nums = [1], k = 1
 * Output: [1]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * -104 <= nums[i] <= 104
 * 1 <= k <= nums.length
 */
public class SlidingWindowMaximum {

    // V0
//    public int[] maxSlidingWindow(int[] nums, int k) {
//
//    }

    // V1-1
    // IDEA: HASHMAP + PQ (GPT)
    /**
     * The first solution uses a max heap (PriorityQueue with
     * Comparator.reverseOrder())
     * to keep track of the maximum element in the current sliding window.
     * However, since Java’s PriorityQueue does not support efficient removal of
     * arbitrary elements,
     * we use a frequency map (freqMap) to track elements that should be removed.
     */
    public int[] maxSlidingWindow_1(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return new int[] {};
        }

        /**
         * - pq is a max heap to store elements in descending order (big PQ).
         * - freqMap helps track the frequency of elements so we can remove elements
         * logically
         * (because PriorityQueue does not support O(1) arbitrary deletions).
         */
        int n = nums.length;
        int[] result = new int[n - k + 1];
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder()); // Max heap
        Map<Integer, Integer> freqMap = new HashMap<>(); // To track removed elements

        // Initialize first window
        /**
         * • Add the first k elements into the PriorityQueue.
         * • Store their frequency in freqMap.
         * • The first max value (pq.peek()) is stored in result[0].
         */
        for (int i = 0; i < k; i++) {
            pq.add(nums[i]);
            freqMap.put(nums[i], freqMap.getOrDefault(nums[i], 0) + 1);
        }
        result[0] = pq.peek();

        /**
         * • Remove the oldest element from freqMap (decrease its count).
         * • Add the new element to both the PriorityQueue and freqMap.
         */
        for (int i = k; i < n; i++) {
            // Remove the element going out of the window
            int oldVal = nums[i - k];
            freqMap.put(oldVal, freqMap.get(oldVal) - 1);

            // Add the new element
            pq.add(nums[i]);
            freqMap.put(nums[i], freqMap.getOrDefault(nums[i], 0) + 1);

            // Remove invalid elements from the top of the heap
            /**
             * • Why? The PriorityQueue does not automatically remove elements that are no
             * longer in the window.
             * • This loop removes stale elements (those whose freqMap count is 0).
             */
            while (!pq.isEmpty() && freqMap.get(pq.peek()) == 0) {
                pq.poll();
            }

            result[i - k + 1] = pq.peek();
        }

        return result;
    }

    // V1-2
    // IDEA: Deque (gpt)
    public int[] maxSlidingWindow_1_2(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return new int[] {};
        }

        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new LinkedList<>(); // Stores indices of elements

        for (int i = 0; i < n; i++) {
            // Remove elements outside the window
            if (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }

            // Remove smaller elements as they are useless
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }

            deque.offerLast(i); // Add current element index

            // Add max to result once we hit a full window
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }

        return result;
    }

    // V2-1
    // https://youtu.be/DfljaUwZsOk?si=0ZPA9MEoW1bXvnu6
    // https://neetcode.io/problems/sliding-window-maximum
    // IDEA: BRUTE FORCE
    public int[] maxSlidingWindow_2_1(int[] nums, int k) {
        int n = nums.length;
        int[] output = new int[n - k + 1];

        for (int i = 0; i <= n - k; i++) {
            int maxi = nums[i];
            for (int j = i; j < i + k; j++) {
                maxi = Math.max(maxi, nums[j]);
            }
            output[i] = maxi;
        }

        return output;
    }

    // V2-2
    // https://youtu.be/DfljaUwZsOk?si=0ZPA9MEoW1bXvnu6
    // https://neetcode.io/problems/sliding-window-maximum
    // IDEA: Segment Tree
    class SegmentTree {
        int n;
        int[] tree;

        SegmentTree(int N, int[] A) {
            this.n = N;
            while (Integer.bitCount(n) != 1) {
                n++;
            }
            build(N, A);
        }

        void build(int N, int[] A) {
            tree = new int[2 * n];
            Arrays.fill(tree, Integer.MIN_VALUE);
            for (int i = 0; i < N; i++) {
                tree[n + i] = A[i];
            }
            for (int i = n - 1; i > 0; --i) {
                tree[i] = Math.max(tree[i << 1], tree[i << 1 | 1]);
            }
        }

        int query(int l, int r) {
            int res = Integer.MIN_VALUE;
            for (l += n, r += n + 1; l < r; l >>= 1, r >>= 1) {
                if ((l & 1) == 1) res = Math.max(res, tree[l++]);
                if ((r & 1) == 1) res = Math.max(res, tree[--r]);
            }
            return res;
        }
    }

    public int[] maxSlidingWindow_2_2(int[] nums, int k) {
        int n = nums.length;
        SegmentTree segTree = new SegmentTree(n, nums);
        int[] output = new int[n - k + 1];
        for (int i = 0; i <= n - k; i++) {
            output[i] = segTree.query(i, i + k - 1);
        }
        return output;
    }


    // V2-3
    // https://youtu.be/DfljaUwZsOk?si=0ZPA9MEoW1bXvnu6
    // https://neetcode.io/problems/sliding-window-maximum
    // IDEA: HEAP
    public int[] maxSlidingWindow_2_3(int[] nums, int k) {
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        int[] output = new int[nums.length - k + 1];
        int idx = 0;
        for (int i = 0; i < nums.length; i++) {
            heap.offer(new int[]{nums[i], i});
            if (i >= k - 1) {
                while (heap.peek()[1] <= i - k) {
                    heap.poll();
                }
                output[idx++] = heap.peek()[0];
            }
        }
        return output;
    }



    // V2-4
    // https://youtu.be/DfljaUwZsOk?si=0ZPA9MEoW1bXvnu6
    // https://neetcode.io/problems/sliding-window-maximum
    // IDEA: DP
    public int[] maxSlidingWindow_2_4(int[] nums, int k) {
        int n = nums.length;
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];

        leftMax[0] = nums[0];
        rightMax[n - 1] = nums[n - 1];

        for (int i = 1; i < n; i++) {
            if (i % k == 0) {
                leftMax[i] = nums[i];
            } else {
                leftMax[i] = Math.max(leftMax[i - 1], nums[i]);
            }

            if ((n - 1 - i) % k == 0) {
                rightMax[n - 1 - i] = nums[n - 1 - i];
            } else {
                rightMax[n - 1 - i] = Math.max(rightMax[n - i], nums[n - 1 - i]);
            }
        }

        int[] output = new int[n - k + 1];

        for (int i = 0; i < n - k + 1; i++) {
            output[i] = Math.max(leftMax[i + k - 1], rightMax[i]);
        }

        return output;
    }

}
