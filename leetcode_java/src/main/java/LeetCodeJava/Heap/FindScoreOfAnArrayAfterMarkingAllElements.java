package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-score-of-an-array-after-marking-all-elements/description/

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 2593. Find Score of an Array After Marking All Elements
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an array nums consisting of positive integers.
 *
 * Starting with score = 0, apply the following algorithm:
 *
 * Choose the smallest integer of the array that is not marked. If there is a tie, choose the one with the smallest index.
 * Add the value of the chosen integer to score.
 * Mark the chosen element and its two adjacent elements if they exist.
 * Repeat until all the array elements are marked.
 * Return the score you get after applying the above algorithm.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1,3,4,5,2]
 * Output: 7
 * Explanation: We mark the elements as follows:
 * - 1 is the smallest unmarked element, so we mark it and its two adjacent elements: [2,1,3,4,5,2].
 * - 2 is the smallest unmarked element, so we mark it and its left adjacent element: [2,1,3,4,5,2].
 * - 4 is the only remaining unmarked element, so we mark it: [2,1,3,4,5,2].
 * Our score is 1 + 2 + 4 = 7.
 * Example 2:
 *
 * Input: nums = [2,3,5,1,3,2]
 * Output: 5
 * Explanation: We mark the elements as follows:
 * - 1 is the smallest unmarked element, so we mark it and its two adjacent elements: [2,3,5,1,3,2].
 * - 2 is the smallest unmarked element, since there are two of them, we choose the left-most one, so we mark the one at index 0 and its right adjacent element: [2,3,5,1,3,2].
 * - 2 is the only remaining unmarked element, so we mark it: [2,3,5,1,3,2].
 * Our score is 1 + 2 + 2 = 5.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 106
 *
 */
public class FindScoreOfAnArrayAfterMarkingAllElements {

    // VO
//    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long findScore(int[] nums) {
//
//    }


    // V0-1: IDEA: custom min PQ (fixed by gpt)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long findScore_0_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        long ans = 0L;

        /**
         * We use a min-heap (PriorityQueue) that always
         * gives us the smallest value, breaking ties by index.
         * Each element is stored as [value, index].
         *
         */
        // min-PQ: (value, index)
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);

        // push all elements
        for (int i = 0; i < n; i++) {
            pq.offer(new int[] { nums[i], i });
        }

        boolean[] visited = new boolean[n];

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int val = cur[0];
            int idx = cur[1];

            if (visited[idx])
                continue; // already marked

            // add to score
            ans += val;

            // mark itself and neighbors
            visited[idx] = true;
            if (idx - 1 >= 0)
                visited[idx - 1] = true;
            if (idx + 1 < n)
                visited[idx + 1] = true;
        }

        return ans;
    }


    // V0-2
    // IDEA: PQ (fixed by gemini)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long findScore_0_2(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        long ans = 0L;
        int n = nums.length;

        // A boolean array to track which elements are marked.
        // Defaults to false.
        boolean[] marked = new boolean[n];

        // --- Fix: Use a single Min-Heap (Priority Queue) ---
        // It will store {value, index} pairs.
        // We need a custom comparator to sort:
        // 1. By value (smallest first).
        // 2. If values are tied, by index (smallest first).
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                if (a[0] != b[0]) {
                    return a[0] - b[0]; // Compare by value
                } else {
                    return a[1] - b[1]; // Compare by index if values are tied
                }
            }
        });

        // --- Fix: Populate the heap ---
        // Add all {value, index} pairs to the heap.
        for (int i = 0; i < n; i++) {
            minHeap.offer(new int[] { nums[i], i });
        }

        // Loop until all elements have been processed from the heap.
        while (!minHeap.isEmpty()) {

            // 1. Get the smallest element.
            int[] current = minHeap.poll();
            int val = current[0];
            int idx = current[1];

            // 2. --- Fix: Check if already marked ---
            // If this element was already marked by a neighbor, skip it.
            if (marked[idx]) {
                continue;
            }

            // 3. Process the element:
            // Add its value to the score.
            ans += val;

            // Mark the element itself as "visited".
            marked[idx] = true;

            // 4. Mark its neighbors.
            int left = idx - 1;
            int right = idx + 1;

            if (left >= 0) {
                marked[left] = true;
            }

            // --- Fix: Correct boundary check ---
            // Check should be < n (the length), not < n - 1 (the last index).
            if (right < n) {
                marked[right] = true;
            }
        }

        return ans;
    }

    // V1-1
    // IDEA: SORTING
    // https://leetcode.com/problems/find-score-of-an-array-after-marking-all-elements/editorial/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long findScore_1_1(int[] nums) {
        long ans = 0;
        int[][] customSorted = new int[nums.length][2];
        boolean[] marked = new boolean[nums.length];

        for (int i = 0; i < nums.length; i++) {
            customSorted[i][0] = nums[i];
            customSorted[i][1] = i;
        }

        Arrays.sort(customSorted, (arr1, arr2) -> arr1[0] - arr2[0]);

        for (int i = 0; i < nums.length; i++) {
            int number = customSorted[i][0];
            int index = customSorted[i][1];
            if (!marked[index]) {
                ans += number;
                marked[index] = true;
                // mark adjacent elements if they exist
                if (index - 1 >= 0) {
                    marked[index - 1] = true;
                }
                if (index + 1 < nums.length) {
                    marked[index + 1] = true;
                }
            }
        }

        return ans;
    }

    // V1-2
    // IDEA: PQ (HEAP)
    // https://leetcode.com/problems/find-score-of-an-array-after-marking-all-elements/editorial/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long findScore_1_2(int[] nums) {
        long ans = 0;
        boolean[] marked = new boolean[nums.length];

        PriorityQueue<int[]> heap = new PriorityQueue<>((arr1, arr2) -> {
            if (arr1[0] != arr2[0])
                return arr1[0] - arr2[0];
            return arr1[1] - arr2[1];
        });

        for (int i = 0; i < nums.length; i++) {
            heap.add(new int[] { nums[i], i });
        }

        while (!heap.isEmpty()) {
            int[] element = heap.remove();
            int number = element[0];
            int index = element[1];
            if (!marked[index]) {
                ans += number;
                marked[index] = true;
                // mark adjacent elements if they exist
                if (index - 1 >= 0) {
                    marked[index - 1] = true;
                }
                if (index + 1 < nums.length) {
                    marked[index + 1] = true;
                }
            }
        }

        return ans;
    }

    // V1-3
    // IDEA: SLIDE WINDOW
    // https://leetcode.com/problems/find-score-of-an-array-after-marking-all-elements/editorial/
    public long findScore_1_3(int[] numbers) {
        long ans = 0;
        for (int i = 0; i < numbers.length; i += 2) {
            int currentStart = i;
            while (i + 1 < numbers.length && numbers[i + 1] < numbers[i]) {
                i++;
            }
            for (int currentIndex = i; currentIndex >= currentStart; currentIndex -= 2) {
                ans += numbers[currentIndex];
            }
        }
        return ans;
    }



}
