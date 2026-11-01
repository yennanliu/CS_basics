package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/length-of-the-longest-increasing-path/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3288. Length of the Longest Increasing Path
 * Hard
 *
 * You are given a 2D array of integers coordinates of length n and an integer k, where 0 <= k < n.
 *
 * coordinates[i] = [x_i, y_i] indicates the point (x_i, y_i) in a 2D plane.
 *
 * An increasing path of length m is defined as a list of points (x_1, y_1), (x_2, y_2), (x_3, y_3), ..., (x_m, y_m) such that:
 *
 * x_i < x_i + 1 and y_i < y_i + 1 for all i where 1 <= i < m.
 * (x_i, y_i) is in the given coordinates for all i where 1 <= i <= m.
 *
 * Return the maximum length of an increasing path that contains coordinates[k].
 *
 *
 * Example 1:
 *
 * Input: coordinates = [[3,1],[2,2],[4,1],[0,0],[5,3]], k = 1
 * Output: 3
 * Explanation:
 * (0, 0), (2, 2), (5, 3) is the longest increasing path that contains (2, 2).
 *
 * Example 2:
 *
 * Input: coordinates = [[2,1],[7,0],[5,6]], k = 2
 * Output: 2
 * Explanation:
 * (2, 1), (5, 6) is the longest increasing path that contains (5, 6).
 *
 *
 * Constraints:
 *
 * 1 <= n == coordinates.length <= 10^5
 * coordinates[i].length == 2
 * 0 <= coordinates[i][0], coordinates[i][1] <= 10^9
 * All elements in coordinates are distinct.
 * 0 <= k <= n - 1
 *
 */
public class LengthOfTheLongestIncreasingPath {

    // V0
    // IDEA: TWO STRICT 2D LIS RUNS - ONE BEFORE THE PIVOT, ONE AFTER
    // a path through coordinates[k] splits into points strictly below-left of it and points
    // strictly above-right of it, and the two halves are independent:
    //   answer = LIS(below-left) + 1 + LIS(above-right).
    // Each half is a 2D chain: sort by x ascending and, for equal x, by y DESCENDING (so two
    // points sharing an x can never both be picked), then a strictly increasing LIS on y.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxPathLength(int[][] coordinates, int k) {
        int pivotX = coordinates[k][0];
        int pivotY = coordinates[k][1];

        List<int[]> below = new ArrayList<>();
        List<int[]> above = new ArrayList<>();
        for (int[] point : coordinates) {
            if (point[0] < pivotX && point[1] < pivotY) {
                below.add(point);
            } else if (point[0] > pivotX && point[1] > pivotY) {
                above.add(point);
            }
        }
        return longestChain(below) + 1 + longestChain(above);
    }

    /** Longest chain with both x and y strictly increasing. */
    private int longestChain(List<int[]> points) {
        points.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(b[1], a[1]);
        });

        // tails[len - 1] = smallest possible last y of a strictly increasing run of length len
        int[] tails = new int[points.size()];
        int length = 0;
        for (int[] point : points) {
            int y = point[1];
            // first tail >= y (strictly increasing -> lower bound)
            int left = 0;
            int right = length;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] >= y) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            tails[left] = y;
            if (left == length) {
                length++;
            }
        }
        return length;
    }
}
