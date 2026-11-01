package LeetCodeJava.HashTable;

// https://leetcode.com/problems/check-array-formation-through-concatenation/description/

import java.util.*;

/**
 * 1640. Check Array Formation Through Concatenation
 * Easy
 *
 * You are given an array of distinct integers arr and an array of integer arrays pieces, where the
 * integers in pieces are distinct. Your goal is to form arr by concatenating the arrays in pieces in
 * any order. However, you are not allowed to reorder the integers in each array pieces[i].
 *
 * Return true if it is possible to form the array arr from pieces. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: arr = [15,88], pieces = [[88],[15]]
 * Output: true
 * Explanation: Concatenate [15] then [88]
 *
 * Example 2:
 *
 * Input: arr = [49,18,16], pieces = [[16,18,49]]
 * Output: false
 * Explanation: Even though the numbers match, we cannot reorder pieces[0].
 *
 * Example 3:
 *
 * Input: arr = [91,4,64,78], pieces = [[78],[4,64],[91]]
 * Output: true
 * Explanation: Concatenate [91] then [4,64] then [78]
 *
 *
 * Constraints:
 *
 * 1 <= pieces.length <= arr.length <= 100
 * sum(pieces[i].length) == arr.length
 * 1 <= pieces[i].length <= arr.length
 * 1 <= arr[i], pieces[i][j] <= 100
 * The integers in arr are distinct.
 * The integers in pieces are distinct (i.e., If we flatten pieces in a 1D array, all the integers in this array are distinct).
 *
 */
public class CheckArrayFormationThroughConcatenation {

    // V0
    // IDEA: HASH TABLE keyed on each piece's first value
    //       all integers are distinct, so the value at arr[i] decides which piece must start there
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean canFormArray(int[] arr, int[][] pieces) {
        Map<Integer, int[]> pieceByHead = new HashMap<>();
        for (int[] piece : pieces) {
            pieceByHead.put(piece[0], piece);
        }

        int i = 0;
        while (i < arr.length) {
            int[] piece = pieceByHead.get(arr[i]);
            if (piece == null) {
                return false;
            }
            // the whole piece must match arr from position i, in order
            for (int value : piece) {
                if (i >= arr.length || arr[i] != value) {
                    return false;
                }
                i++;
            }
        }
        return true;
    }
}
