package LeetCodeJava.Heap;

// https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/description/

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 378. Kth Smallest Element in a Sorted Matrix
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an n x n matrix where each of the rows and columns is sorted in ascending order, return the kth smallest element in the matrix.
 *
 * Note that it is the kth smallest element in the sorted order, not the kth distinct element.
 *
 * You must find a solution with a memory complexity better than O(n2).
 *
 *
 *
 * Example 1:
 *
 * Input: matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
 * Output: 13
 * Explanation: The elements in the matrix are [1,5,9,10,11,12,13,13,15], and the 8th smallest number is 13
 * Example 2:
 *
 * Input: matrix = [[-5]], k = 1
 * Output: -5
 *
 *
 * Constraints:
 *
 * n == matrix.length == matrix[i].length
 * 1 <= n <= 300
 * -109 <= matrix[i][j] <= 109
 * All the rows and columns of matrix are guaranteed to be sorted in non-decreasing order.
 * 1 <= k <= n2
 *
 *
 * Follow up:
 *
 * Could you solve the problem with a constant memory (i.e., O(1) memory complexity)?
 * Could you solve the problem in O(n) time complexity? The solution may be too advanced for an interview but you may find reading this paper fun.
 */
public class KthSmallestElementInASortedMatrix {

    // V0
//    public int kthSmallest(int[][] matrix, int k) {
//
//    }

    // V0-1
    // IDEA: PQ (fixed by gpt)
    public int kthSmallest_0_1(int[][] matrix, int k) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int n = matrix.length;
        int m = matrix[0].length;

        /** NOTE !!!
         *
         *  use max PQ
         *
         *  since the problem is asking
         *  `kth smallest element` ~= biggest element from a PQ
         */
        // Max-heap: largest value at top
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                pq.offer(matrix[i][j]);
                /** NOTE !!!
                 *
                 *  we use `pq.size()` check if reach the `k smallest` status.
                 *
                 *  -> NO NEED to use the other param such as size, or cnt to track elements count
                 */
                if (pq.size() > k) {
                    pq.poll(); // remove largest, keep only k smallest
                }
            }
        }

        // top of max-heap = kth smallest
        return pq.peek();
    }

    // V0-2
    // IDEA: PQ (fixed by gemini)
    public int kthSmallest_0_2(int[][] matrix, int k) {
        // --- FIX 1: Correct Edge Case Handling ---
        // For an LC problem, return 0 if constraints ensure matrix is non-empty,
        // otherwise return the requested value from the non-empty matrix.
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            // Given the problem constraints, this case is often guaranteed not to happen,
            // but we return the requested type.
            return 0;
        }

        /** NOTE !!! Max-Heap (Big PQ) to store the K smallest elements. */
        // Max-Heap (Big PQ) to store the K smallest elements.
        // The largest element among the K smallest will be at the root (peek).
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // o2 - o1 for descending order (Max-Heap)
                return o2 - o1;
            }
        });

        int l = matrix.length;
        int w = matrix[0].length;

        // 2. Iterate through ALL elements (N^2 time)
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                int val = matrix[i][j];

                if (maxHeap.size() < k) {
                    // Fill the heap until size k
                    maxHeap.add(val);
                } else if (val < maxHeap.peek()) {
                    // Current element is smaller than the largest element currently in the top K
                    // Remove the largest (farthest) and add the new smaller element
                    maxHeap.poll();
                    maxHeap.add(val);
                }
            }
        }

        // 3. The element at the root of the Max-Heap is the K-th smallest element.
        // If the heap is empty (should not happen based on constraints), return 0.
        return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }

    // V0-3
    // IDEA: BINARY SEARCH (gemini)
    public int kthSmallest_0_3(int[][] matrix, int k) {
        int N = matrix.length;
        int low = matrix[0][0]; // Smallest possible answer
        int high = matrix[N - 1][N - 1]; // Largest possible answer
        int ans = -1;

        // Binary search on the answer space (the values in the matrix)
        while (low <= high) {
            int mid = low + (high - low) / 2;

            // Count how many elements are <= mid
            int count = countLessEqual(matrix, mid);

            if (count >= k) {
                // mid is a possible answer. Try to find a smaller one.
                ans = mid;
                high = mid - 1;
            } else {
                // Not enough elements are <= mid. Need a larger value.
                low = mid + 1;
            }
        }

        return ans;
    }

    // Helper function to count how many elements in the matrix are less than or equal to 'target'
    private int countLessEqual(int[][] matrix, int target) {
        int N = matrix.length;
        int count = 0;
        int r = 0; // Start at the first row
        int c = N - 1; // Start at the last column (similar to search in sorted matrix)

        while (r < N && c >= 0) {
            if (matrix[r][c] <= target) {
                // If the current element is <= target, all elements in this row
                // to its left are also <= target (since the row is sorted).
                count += (c + 1);
                r++; // Move to the next row
            } else {
                // Current element is > target, so we move one column left
                c--;
            }
        }
        return count;
    }


    // V1-1
    // https://leetcode.ca/2016-12-12-378-Kth-Smallest-Element-in-a-Sorted-Matrix/
    class Node {
        int x;
        int y;
        int val;
        public Node(int x, int y, int val) {
            this.x = x;
            this.y = y;
            this.val = val;
        }
    }

    public int kthSmallest_1_1(int[][] matrix, int k) {

        if (matrix == null || matrix.length == 0 || k <= 0) {
            return 0;
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.val - o2.val;
            }
        });

        for (int i = 0; i < matrix.length; i++) {
            pq.offer(new Node(i, 0, matrix[i][0])); // 第一列入heap
        }

        while (!pq.isEmpty()) {
            Node n = pq.poll();

            if (k == 1) {
                return n.val;
            }

            if (n.y + 1 < matrix.length) {
                pq.offer(new Node(n.x, n.y + 1, matrix[n.x][n.y+1])); // 入current的row的下一个
            }

            k--;
        }

        return 0;
    }

    // V1-2
    // https://leetcode.ca/2016-12-12-378-Kth-Smallest-Element-in-a-Sorted-Matrix/
    public int kthSmallest_1_2(int[][] matrix, int k) {

        if (matrix == null || matrix.length == 0 || k <= 0) {
            return 0;
        }

        int n = matrix.length;
        int min = matrix[0][0];
        int max = matrix[n-1][n-1];

        while (min < max) {
            int mid = min + (max - min) / 2;

            int smallerCount = countSmallerThanMid(matrix, mid);

            if (smallerCount < k) {
                min = mid + 1;
            } else {
                max = mid;
            }
        }

        return min; // return max will also work
    }

    private int countSmallerThanMid(int[][] matrix, int mid) {
        int count = 0;

        // start from top-right corner, where all its left is smaller but all its below is larger
        int i = 0;
        int j = matrix[0].length - 1;

        while (i < matrix[0].length && j >= 0) {

            if (matrix[i][j] > mid) {
                j--;
            } else {
                count += j + 1;
                i++;
            }
        }

        return count;
        }


    // V1-3
    // https://leetcode.ca/2016-12-12-378-Kth-Smallest-Element-in-a-Sorted-Matrix/
    public int kthSmallest_1_3(int[][] matrix, int k) {
        int n = matrix.length;
        int left = matrix[0][0], right = matrix[n - 1][n - 1];
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (check(matrix, mid, k, n)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private boolean check(int[][] matrix, int mid, int k, int n) {
        int count = 0;
        int i = n - 1, j = 0;
        while (i >= 0 && j < n) {
            if (matrix[i][j] <= mid) {
                count += (i + 1);
                ++j;
            } else {
                --i;
            }
        }
        return count >= k;
    }


    // V2-1
    // https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/solutions/1547354/3-different-approaches-for-interview-wit-3mg1/
    public int kthSmallest_2_1(int[][] matrix, int k) {
        int n = matrix.length;
        int[] arr = new int[n * n];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[idx++] = matrix[i][j];
            }
        }

        Arrays.sort(arr);

        return arr[k - 1];
    }

    // V2-2
    // https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/solutions/1547354/3-different-approaches-for-interview-wit-3mg1/
    // IDEA: PQ
    public int kthSmallest_2_2(int[][] matrix, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        int n = matrix.length;

        for(int i = 0;i<n;i++){
            for(int j = 0;j<n;j++){
                if(pq.size() < k){
                    pq.add(matrix[i][j]);
                }else{ //equal to k
                    if(matrix[i][j] < pq.peek()){ //if incoming element is less than peek
                        pq.poll();
                        pq.add(matrix[i][j]);
                    }
                }
            }
        }

        return pq.peek();
    }

    // V2-3
    // https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/solutions/1547354/3-different-approaches-for-interview-wit-3mg1/
    // IDEA: BINARY SEARCH
    public int kthSmallest_2_3(int[][] matrix, int k) {
        int rows = matrix.length, cols = matrix[0].length;

        int lo = matrix[0][0], hi = matrix[rows - 1][cols - 1];
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int count = 0, maxNum = lo;

            for (int r = 0, c = cols - 1; r < rows; r++) {
                while (c >= 0 && matrix[r][c] > mid)
                    c--;

                if (c >= 0) {
                    count += (c + 1); // count of nums <= mid in matrix
                    maxNum = Math.max(maxNum, matrix[r][c]);
                    // mid might be value not in  matrix, we need to record the actually max num;
                } else { //it means c < 0
                    break;
                }
            }

            // adjust search range
            if (count == k)
                return maxNum;
            else if (count < k)
                lo = mid + 1;
            else
                hi = mid - 1;
        }
        return lo;
    }



}




