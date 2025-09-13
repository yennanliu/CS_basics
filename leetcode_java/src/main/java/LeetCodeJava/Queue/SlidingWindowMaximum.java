package LeetCodeJava.Queue;

// https://leetcode.com/problems/sliding-window-maximum/

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
    // IDEA: DEQUE + SLIDING WINDOW (fixed by gpt)
    public int[] maxSlidingWindow(int[] nums, int k) {
        // Edge case: empty array or k is zero
        if (nums == null || nums.length == 0 || k == 0) {
            return new int[0];
        }

        /**
         *  NOTE !!!
         *
         *  deque is used to store indices of elements in
         *  the current sliding window, with the goal of
         *  ensuring that the largest element in the window is
         *  always at the front of the deque.
         *
         *  -> e.g. deque : [idx_1, idx_2,...]
         *
         */
        Deque<Integer> deque = new LinkedList<>();
        /**
         *  NOTE !!! below trick
         *
         *  e.g. if nums.size = 5, k = 3
         *   -> we will have (5-3) + 1 = 3 windows
         */
        int[] result = new int[nums.length - k + 1];
        int index = 0;

        for (int i = 0; i < nums.length; i++) {
            // Remove elements from the front of the deque if they are out of the current
            // window
            /**
             *  NOTE !!!
             *
             *  - Removing Out-of-Window Elements:
             *
             *     - The first condition checks if the element at the
             *        front of the deque is out of the current window.
             *
             *     -  If the index at the front of the deque (deque.peekFirst())
             *        is less than i - k + 1, it means that index is no longer
             *        in the window (it's too old), so we remove it from
             *        the deque using deque.pollFirst().
             *
             *
             *    -> so via `deque.peekFirst() < i - k + 1`
             *       -> we can check if `An element is OUTSIDE of window`
             */
            if (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }

            // Remove all elements smaller than the current element from the back of the
            // deque
            /**
             *  NOTE !!!
             *
             *  -> maintain dequeue in `DECREASING` order
             *
             *
             *  - This while loop ensures that the deque always maintains
             *    elements in `descending` order of their values (from front to back).
             *
             *     - If the current element nums[i] is greater than the element
             *       at the index of the last element in the deque (nums[deque.peekLast()]),
             *       we remove the last element from the deque using deque.pollLast().
             *       This is because the smaller elements are less likely
             *       to be the maximum when the current element is larger.
             *
             *    -  By maintaining this order, the largest element of the current window
             *       will always be at the front of the deque.
             *
             */
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }

            /**
             *  Add the current element's index to the deque
             *
             *
             * The index i of the current element is added to the
             * back of the deque using deque.offerLast(i).
             *
             */
            deque.offerLast(i);

            // If the window has reached size k, add the max to the result
            if (i >= k - 1) {
                result[index++] = nums[deque.peekFirst()];
            }
        }

        return result;
    }

    // V0-1
    // IDEA: HASHMAP + PQ (GPT)
    public int[] maxSlidingWindow_0_1(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return new int[] {};
        }

        /**
         *  NOTE !!!
         *
         *
         * - 1) PQ is a max heap to store elements in descending order (big PQ).
         *
         * - 2) freqMap helps track the frequency of elements so we can remove elements
         *  logically  (because PriorityQueue does not support O(1) arbitrary deletions).
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
            /**
             *  NOTE !!!
             *
             *   we DON'T have to remove oldVal from PQ as well.
             *   explain below why the code still works per this
             *
             *    -> But the actual PriorityQueue still contains that oldVal inside.
             * 	  -> So pq may have “stale” values that are no longer valid.
             *
             *
             *  NOTE !!!
             *
             *  Why the code still works ??
             *
             *  -> 	After every new insertion, we check the top element of the heap:
             *
             *   while (!pq.isEmpty() && freqMap.get(pq.peek()) == 0) {
             *     pq.poll();
             * }
             *
             *  -> This ensures that if the heap’s maximum
             *     (pq.peek()) has already been logically
             *      removed (its frequency in freqMap is 0), we discard it.
             *
             *  ->  We don’t need to remove all old values
             *     deep inside the heap, just the ones
             *     that “bubble up” to the root.
             *
             *
             *
             *  -> So the trick here is lazy deletion ✅
             * 	    •	Old values remain in the heap → but they’re marked as invalid in freqMap.
             * 	    •	When they reach the top, the while loop removes them.
             * 	    •	That’s why oldVal isn’t directly removed, but the algorithm still produces correct results.
             */
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

    // V0-2
    // IDEA: DEQUEUE + PQ (TLE)
    public int[] maxSlidingWindow_0_2(int[] nums, int k) {
        // edge
        if (nums.length == 0) {
            return null; // ??
        }
        // ???
        if (nums.length <= k) {
            int res = nums[0];
            for (int x : nums) {
                res = Math.max(x, res);
            }
            return new int[] { res };
        }

        List<Integer> tmp = new ArrayList<>();
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < nums.length; i++) {
            int val = nums[i];
            //System.out.println(">>> i = " + i + ", val = " + val);
            deque.add(val);
            // case 0) arr size < k, do nothing

            // case 1) arr size == k, get max val
            if (deque.size() == k) {
                tmp.add(getMaxFromWindow(deque));
            }
            // case 2) arr size > k, pop left most, and get max val
            else if (deque.size() > k) {
                //System.out.println(">>> i = " + i + ", deque = " + deque);
                // pop left most
                deque.pollFirst();
                //System.out.println(">>> i = " + i + ", getMaxFromWindow(deque) = " + getMaxFromWindow(deque));
                // get max ???
                //res[i] = getMaxFromWindow(deque);
                tmp.add(getMaxFromWindow(deque));
            }
        }

        int[] res = new int[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            res[i] = tmp.get(i);
        }

        //System.out.println(">>> tmp = " + tmp + ", res = " + res);
        return res;
    }

    private int getMaxFromWindow(Deque<Integer> deque) {
        if (deque.isEmpty()) {
            return -1;
        }
        // PQ: big -> small
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        pq.addAll(deque);
        return pq.peek();
    }

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

        /**
         *  NOTE !!!
         *
         *  heap is a max-heap (PQ) that stores pairs [value, index]
         *
         *  -> 1st val : value
         *  -> 2nd val : index
         */
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        int[] output = new int[nums.length - k + 1];
        int idx = 0;
        for (int i = 0; i < nums.length; i++) {
            /**
             * In each iteration, we add the current element nums[i]
             * along with its index i as a pair into the priority queue (heap).
             * The heap will ensure that the largest element is at the
             * top based on the value of nums[i].
             */
            heap.offer(new int[]{nums[i], i});
            /**
             * Once the window reaches size k (i.e., i >= k - 1),
             * we need to maintain the sliding window and ensure
             * that elements outside the window are removed from the heap:
             */
            if (i >= k - 1) {
                /**
                 *  NOTE !!!! below
                 *
                 *  1) Here, heap.peek() gives us the `element at the top of the heap`,
                 *  which is the maximum value in the heap.
                 *  We check the index of that element (heap.peek()[1])
                 *  and if it's outside the current window
                 *  (i.e., it’s older than i - k),
                 *  we remove it from the heap using heap.poll().
                 *  This ensures that the heap only contains elements within the current window of size k.
                 *
                 *
                 *  2) heap.peek()[1] is `index` of a value
                 *
                 *  3)  `heap.peek()[1] <= i - k`
                 *      -> to check if it's outside the current window (i.e., it’s older than i - k),
                 */
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
