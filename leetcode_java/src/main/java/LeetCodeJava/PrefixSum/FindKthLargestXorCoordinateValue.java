package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/find-kth-largest-xor-coordinate-value/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 1738. Find Kth Largest XOR Coordinate Value
 * Medium
 *
 * You are given a 2D matrix of size m x n, consisting of non-negative integers. You are also given
 * an integer k.
 *
 * The value of coordinate (a, b) of the matrix is the XOR of all matrix[i][j] where
 * 0 <= i <= a < m and 0 <= j <= b < n (0-indexed).
 *
 * Find the kth largest value (1-indexed) of all the coordinates of matrix.
 *
 *
 * Example 1:
 *
 * Input: matrix = [[5,2],[1,6]], k = 1
 * Output: 7
 * Explanation: The value of coordinate (0,1) is 5 XOR 2 = 7, which is the largest value.
 *
 * Example 2:
 *
 * Input: matrix = [[5,2],[1,6]], k = 2
 * Output: 5
 * Explanation: The value of coordinate (0,0) is 5 = 5, which is the 2nd largest value.
 *
 * Example 3:
 *
 * Input: matrix = [[5,2],[1,6]], k = 3
 * Output: 4
 * Explanation: The value of coordinate (1,0) is 5 XOR 1 = 4, which is the 3rd largest value.
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 1000
 * 0 <= matrix[i][j] <= 10^6
 * 1 <= k <= m * n
 *
 */
public class FindKthLargestXorCoordinateValue {

    // V0
    // IDEA: 2D PREFIX XOR + SORT
    // inclusion-exclusion works for XOR too, and "subtracting" the double-counted corner is itself
    // an XOR (a ^ a = 0):
    //   prefix[i][j] = prefix[i-1][j] ^ prefix[i][j-1] ^ prefix[i-1][j-1] ^ matrix[i-1][j-1]
    /**
     * time = O(M * N * log(M * N))
     * space = O(M * N)
     */
    public int kthLargestValue(int[][] matrix, int k) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // prefix[i][j] = XOR of the top-left i x j rectangle (row 0 / column 0 are the empty border)
        int[][] prefix = new int[rows + 1][cols + 1];
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                prefix[i][j] = prefix[i - 1][j] ^ prefix[i][j - 1] ^ prefix[i - 1][j - 1] ^ matrix[i - 1][j - 1];
                values.add(prefix[i][j]);
            }
        }

        Collections.sort(values);
        // kth largest = index (size - k) in ascending order
        return values.get(values.size() - k);
    }

    // V1
    // IDEA: 2D PREFIX XOR + MIN-HEAP OF SIZE K
    // only the kth largest is needed, not a full order: keep the k largest values seen so far in a
    // min-heap; its root is the answer once every coordinate has been offered.
    /**
     * time = O(M * N * log K)
     * space = O(M * N + K)
     */
    public int kthLargestValue_1(int[][] matrix, int k) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] prefix = new int[rows + 1][cols + 1];
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                prefix[i][j] = prefix[i - 1][j] ^ prefix[i][j - 1] ^ prefix[i - 1][j - 1] ^ matrix[i - 1][j - 1];
                minHeap.offer(prefix[i][j]);
                // evict the smallest so the heap always holds the k largest values
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }
        }
        return minHeap.peek();
    }
}
