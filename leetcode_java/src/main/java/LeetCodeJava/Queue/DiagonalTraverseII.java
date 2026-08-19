package LeetCodeJava.Queue;

// https://leetcode.com/problems/diagonal-traverse-ii/

import java.util.ArrayList;
import java.util.List;

/**
 *  1424. Diagonal Traverse II
 *  Medium
 *
 *  Given a 2D integer array nums, return all elements of nums in diagonal order.
 *
 *  Example 1:
 *    Input: nums = [[1,2,3],[4,5,6],[7,8,9]]
 *    Output: [1,4,2,7,5,3,8,6,9]
 *
 *  Example 2:
 *    Input: nums = [[1,2,3,4,5],[6,7],[8],[9,10,11],[12,13,14,15,16]]
 *    Output: [1,6,2,8,7,3,9,4,12,10,5,13,11,14,15,16]
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i].length <= 10^5
 *    1 <= sum(nums[i].length) <= 10^5
 *    1 <= nums[i][j] <= 10^5
 */
public class DiagonalTraverseII {

    // V0
    // IDEA: BUCKET BY THE (i + j) DIAGONAL KEY
    //       all cells on one diagonal share the same (i + j), and inside a
    //       diagonal the output order is "bottom-left -> top-right", i.e. the ROW
    //       index DECREASES. so walking the rows in REVERSE order and appending
    //       builds every bucket already in the required order (no sorting needed).
    /**
     * time = O(N)     // N = total number of elements
     * space = O(N)
     */
    public int[] findDiagonalOrder(List<List<Integer>> nums) {
        int rows = nums.size();
        int maxKey = 0;
        int total = 0;
        for (int i = 0; i < rows; i++) {
            int len = nums.get(i).size();
            total += len;
            if (i + len - 1 > maxKey) {
                maxKey = i + len - 1;
            }
        }

        List<List<Integer>> buckets = new ArrayList<>(maxKey + 1);
        for (int k = 0; k <= maxKey; k++) {
            buckets.add(null);
        }

        // NOTE !!! walk the rows bottom -> top
        for (int i = rows - 1; i >= 0; i--) {
            List<Integer> row = nums.get(i);
            for (int j = 0; j < row.size(); j++) {
                int key = i + j;
                List<Integer> bucket = buckets.get(key);
                if (bucket == null) {
                    bucket = new ArrayList<>();
                    buckets.set(key, bucket);
                }
                bucket.add(row.get(j));
            }
        }

        int[] res = new int[total];
        int p = 0;
        for (int key = 0; key <= maxKey; key++) {
            List<Integer> bucket = buckets.get(key);
            if (bucket == null) {
                continue;
            }
            for (int k = 0; k < bucket.size(); k++) {
                res[p++] = bucket.get(k);
            }
        }
        return res;
    }
}
