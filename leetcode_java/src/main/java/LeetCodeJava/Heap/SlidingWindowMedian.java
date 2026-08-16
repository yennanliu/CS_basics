package LeetCodeJava.Heap;

// https://leetcode.com/problems/sliding-window-median/description/

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 480. Sliding Window Median
 * Hard
 *
 * The median is the middle value in an ordered integer list. If the size of the
 * list is even, there is no middle value. So the median is the mean of the two
 * middle values.
 *
 * - For examples, if arr = [2,3,4], the median is 3.
 * - For examples, if arr = [1,2,3,4], the median is (2 + 3) / 2 = 2.5.
 *
 * You are given an integer array nums and an integer k. There is a sliding window
 * of size k which is moving from the very left of the array to the very right. You
 * can only see the k numbers in the window. Each time the sliding window moves
 * right by one position.
 *
 * Return the median array for each window in the original array. Answers within
 * 10^-5 of the actual value will be accepted.
 *
 * Example 1:
 *
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [1.00000,-1.00000,-1.00000,3.00000,5.00000,6.00000]
 * Explanation:
 * Window position                Median
 * ---------------                -----
 * [1  3  -1] -3  5  3  6  7        1
 *  1 [3  -1  -3] 5  3  6  7       -1
 *  1  3 [-1  -3  5] 3  6  7       -1
 *  1  3  -1 [-3  5  3] 6  7        3
 *  1  3  -1  -3 [5  3  6] 7        5
 *  1  3  -1  -3  5 [3  6  7]       6
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,2,3,1,4,2], k = 3
 * Output: [2.00000,3.00000,3.00000,3.00000,2.00000,3.00000,2.00000]
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 10^5
 * -2^31 <= nums[i] <= 2^31 - 1
 *
 */
public class SlidingWindowMedian {

    // V0
    // IDEA: SORTED WINDOW + BINARY SEARCH
    /**
     *  Keep the window itself in SORTED order. Sliding one step is:
     *     - DELETE the outgoing value (binary search to find it, then shift left)
     *     - INSERT the incoming value (binary search for the slot, then shift right)
     *  Then the median is just the middle of the sorted window.
     *
     *  The shifts are O(k) each, but they are `System.arraycopy` (a bulk memory move),
     *  which makes this the simplest correct solution.
     *
     *  NOTE !!! nums[i] spans the FULL int range, so `a + b` for the even-k median
     *  would OVERFLOW -> we cast to double BEFORE adding.
     *
     *  time  = O(n * k)
     *  space = O(k)
     */
    public double[] medianSlidingWindow(int[] nums, int k) {
        int n = nums.length;

        // the sorted window
        int[] window = Arrays.copyOfRange(nums, 0, k);
        Arrays.sort(window);

        double[] res = new double[n - k + 1];
        res[0] = median(window, k);

        for (int i = k; i < n; i++) {
            // 1) remove the element LEAVING the window
            int out = Arrays.binarySearch(window, nums[i - k]);
            /** NOTE !!!
             *
             *  with duplicates, binarySearch may land on ANY matching index,
             *  which is fine -- we only need to drop ONE copy of that value
             */
            System.arraycopy(window, out + 1, window, out, k - 1 - out);

            // 2) add the element ENTERING the window
            int pos = Arrays.binarySearch(window, 0, k - 1, nums[i]);
            if (pos < 0) {
                pos = -(pos + 1); // binarySearch returns (-insertionPoint - 1) when absent
            }
            System.arraycopy(window, pos, window, pos + 1, k - 1 - pos);
            window[pos] = nums[i];

            res[i - k + 1] = median(window, k);
        }

        return res;
    }

    private double median(int[] window, int k) {
        if (k % 2 == 1) {
            return (double) window[k / 2];
        }
        // cast BEFORE adding -> avoids int overflow
        return ((double) window[k / 2 - 1] + (double) window[k / 2]) / 2.0;
    }

    // V0-1
    // IDEA: TWO HEAPS + LAZY DELETION
    /**
     *  `small` : max-heap holding the LOWER half
     *  `large` : min-heap holding the UPPER half
     *  invariant: small.size == large.size  or  small.size == large.size + 1
     *  -> median is small's top (odd k) or the average of the two tops (even k)
     *
     *  A heap CANNOT delete an arbitrary element, so we DEFER it: mark the outgoing
     *  value in `delayed` and only physically pop it once it surfaces to a top.
     *  `smallSize` / `largeSize` track the number of VALID (non-deleted) elements so
     *  the balancing stays correct even while garbage still sits inside the heaps.
     *
     *  -> O(n log n) instead of V0's O(n * k)
     *
     *  time  = O(n * log(n))
     *  space = O(n)
     */

    private PriorityQueue<Integer> small; // max-heap - lower half
    private PriorityQueue<Integer> large; // min-heap - upper half
    private Map<Integer, Integer> delayed; // value -> pending deletions
    private int smallSize;
    private int largeSize;

    public double[] medianSlidingWindow_0_1(int[] nums, int k) {
        this.small = new PriorityQueue<>(Collections.reverseOrder());
        this.large = new PriorityQueue<>();
        this.delayed = new HashMap<>();
        this.smallSize = 0;
        this.largeSize = 0;

        for (int i = 0; i < k; i++) {
            insert(nums[i]);
        }

        double[] res = new double[nums.length - k + 1];
        res[0] = heapMedian(k);

        for (int i = k; i < nums.length; i++) {
            insert(nums[i]);
            erase(nums[i - k]);
            res[i - k + 1] = heapMedian(k);
        }

        return res;
    }

    /** physically drop tops that are already marked deleted */
    private void prune(PriorityQueue<Integer> heap) {
        while (!heap.isEmpty()) {
            int val = heap.peek();
            int pending = delayed.getOrDefault(val, 0);
            if (pending > 0) {
                delayed.put(val, pending - 1);
                heap.poll();
            } else {
                break;
            }
        }
    }

    private void balance() {
        if (smallSize > largeSize + 1) {
            large.add(small.poll());
            smallSize -= 1;
            largeSize += 1;
            prune(small); // the NEW top may be garbage
        } else if (smallSize < largeSize) {
            small.add(large.poll());
            smallSize += 1;
            largeSize -= 1;
            prune(large);
        }
    }

    private void insert(int num) {
        if (small.isEmpty() || num <= small.peek()) {
            small.add(num);
            smallSize += 1;
        } else {
            large.add(num);
            largeSize += 1;
        }
        balance();
    }

    private void erase(int num) {
        delayed.put(num, delayed.getOrDefault(num, 0) + 1);
        if (num <= small.peek()) {
            smallSize -= 1;
            if (num == small.peek()) {
                prune(small);
            }
        } else {
            largeSize -= 1;
            if (num == large.peek()) {
                prune(large);
            }
        }
        balance();
    }

    private double heapMedian(int k) {
        if (k % 2 == 1) {
            return (double) small.peek();
        }
        return ((double) small.peek() + (double) large.peek()) / 2.0;
    }

}
