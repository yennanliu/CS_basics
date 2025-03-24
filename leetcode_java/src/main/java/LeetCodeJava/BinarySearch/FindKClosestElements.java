package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-k-closest-elements/description/

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 658. Find K Closest Elements
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a sorted integer array arr, two integers k and x, return the k closest integers to x in the array. The result should also be sorted in ascending order.
 *
 * An integer a is closer to x than an integer b if:
 *
 * |a - x| < |b - x|, or
 * |a - x| == |b - x| and a < b
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,3,4,5], k = 4, x = 3
 *
 * Output: [1,2,3,4]
 *
 * Example 2:
 *
 * Input: arr = [1,1,2,3,4,5], k = 4, x = -1
 *
 * Output: [1,1,2,3]
 *
 *
 *
 * Constraints:
 *
 * 1 <= k <= arr.length
 * 1 <= arr.length <= 104
 * arr is sorted in ascending order.
 * -104 <= arr[i], x <= 104
 *
 */
public class FindKClosestElements {

    // V0
//    public List<Integer> findClosestElements(int[] arr, int k, int x) {
//
//    }

    // V1

    // V2

    // V3
    // https://leetcode.com/problems/find-k-closest-elements/solutions/6550496/optimized-solution-two-pointer-approach-p2wfl/
    public List<Integer> findClosestElements_3(int[] arr, int k, int x) {
        int l = 0, r = arr.length - 1; // left and right pointers

        while (r - l >= k) { // Check if the window is within range
            if (Math.abs(arr[l] - x) > Math.abs(arr[r] - x))
                l++; // eliminate the left if the right side is closer
            else
                r--; // else, eliminate the right
        }

        //List<Integer> list = new ArrayList<>(); // result list
        List<Integer> list = new ArrayList<>(); // result list

        for (int i = l; i <= r; i++)
            list.add(arr[i]); // add the array elements

        return list; // return list
    }

    // V4-1
    // https://leetcode.com/problems/find-k-closest-elements/solutions/2636647/java-explained-in-detail-binary-search-t-ywo5/
    // IDEA: 2 POINTERS
    // Approach:
    // Using two pointers, we are going the 'start' and 'end' pointers towards each
    // other,
    // until only k elements between 'start' and 'end'.
    public List<Integer> findClosestElements_4_1(int[] arr, int k, int x) {

        int start = 0;
        int end = arr.length - 1;
        // Between the 'start' and 'end' pointers, inclusive, contains all the k
        // integers that is closest to x.
        while (end - start >= k) {
            // Move 'start' to the right if 'end' is closer to x, or move 'end' to the left
            // if 'start' is closer to x.
            if (Math.abs(arr[start] - x) > Math.abs(arr[end] - x)) {
                start++;
            } else {
                end--;
            }
        }

        // Input all the k closest integers into the result.
        List<Integer> result = new ArrayList<>(k);
        for (int i = start; i <= end; i++) {
            result.add(arr[i]);
        }
        return result;
    }


    // V4-2
    // https://leetcode.com/problems/find-k-closest-elements/solutions/2636647/java-explained-in-detail-binary-search-t-ywo5/
    // IDEA: BINARY SEARCH
    // Approach:
    // Using binary search and a sliding window, find the midpoint where,
    // the integers between midpoint and midpoint + k is the k closest integers to x
    public List<Integer> findClosestElements_4_2(int[] arr, int k, int x) {

        // The sliding window is between 'mid' and 'mid' + k.
        int left = 0, right = arr.length - k;
        while (left < right) {
            int midpoint = left + (right - left) / 2; // same as (left + right) / 2

            // With midpoint on the left, we use x - arr[midpoint], while arr[midpoint + k]
            // - x because it is on the right.
            // This is important!
            // Rather than using Math.abs(), we need the direction keep the x within the
            // sliding window.
            // If the window is too far left, we shift the window to the right.
            if (x - arr[midpoint] > arr[midpoint + k] - x) {
                left = midpoint + 1;
            }
            // If the window is too far right, we shift the window to the left.
            else {
                right = midpoint;
            }
        }

        // Input all the k closest integers into the result.
        List<Integer> result = new ArrayList<>(k);
        for (int i = left; i < left + k; i++) {
            result.add(arr[i]);
        }
        return result;
    }


    // V4-3
    // https://leetcode.com/problems/find-k-closest-elements/solutions/2636647/java-explained-in-detail-binary-search-t-ywo5/
    // IDEA: PQ
    // Approach:
    // Using a min heap priority queue, add all the smallest integers up to k
    // integers.
    // Then, traverse the 'arr' array will replacing the priority queue with integer
    // closer to x.
    public List<Integer> findClosestElements_4_3(int[] arr, int k, int x) {

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        // Traverse the array with,
        // First, add all the smallest integers up to k number.
        // Second, if found a closer integer to x, remove the smallest integer from the
        // priority queue, and add the new integer.
        // This is because the smallest integer is always the further to x, if a larger
        // number is closer to x.
        for (int integer : arr) {
            if (k > 0) {
                minHeap.offer(integer);
                k--;
            } else if (Math.abs(minHeap.peek() - x) > Math.abs(integer - x)) {
                minHeap.poll();
                minHeap.offer(integer);
            }
        }

        // Add the integers from the priority queue to the result.
        // This will automatically add in ascending order, from smallest to largest k
        // integers closest to x.
        List<Integer> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll());
        }
        return result;
    }


    // V5

}
