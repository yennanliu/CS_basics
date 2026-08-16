package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/russian-doll-envelopes/description/

import java.util.Arrays;

/**
 * 354. Russian Doll Envelopes
 * Hard
 *
 * You are given a 2D array of integers envelopes where envelopes[i] = [wi, hi] represents
 * the width and the height of an envelope.
 *
 * One envelope can fit into another if and only if both the width and height of one envelope
 * are greater than the other envelope's width and height.
 *
 * Return the maximum number of envelopes you can Russian doll (i.e., put one inside the other).
 *
 * Note: You cannot rotate an envelope.
 *
 *
 * Example 1:
 *
 * Input: envelopes = [[5,4],[6,4],[6,7],[2,3]]
 * Output: 3
 * Explanation: The maximum number of envelopes you can Russian doll is 3
 * ([2,3] => [5,4] => [6,7]).
 *
 * Example 2:
 *
 * Input: envelopes = [[1,1],[1,1],[1,1]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= envelopes.length <= 10^5
 * envelopes[i].length == 2
 * 1 <= wi, hi <= 10^5
 *
 */
public class RussianDollEnvelopes {

    // V0
    // IDEA: SORT (w ASC, h DESC) then LIS on heights via BINARY SEARCH
    /**
     *  Sorting by WIDTH ascending reduces the 2D problem to a 1D LIS on heights.
     *
     *  KEY TRICK: for EQUAL widths, sort the heights DESCENDING. That way two envelopes
     *  with the same width can NEVER both be picked by the (strictly) increasing
     *  subsequence - a descending run contains no increasing pair.
     *
     *  Then run the classic patience-sorting LIS, using a LOWER-BOUND binary search
     *  (`strictly` increasing: an equal height REPLACES, it does not append).
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public int maxEnvelopes(int[][] envelopes) {
        // edge
        if (envelopes == null || envelopes.length == 0) {
            return 0;
        }

        /** NOTE !!!
         *
         *  width ASC, but height DESC when widths tie
         */
        Arrays.sort(envelopes, (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);

        // tails[i] = smallest possible tail of an increasing subseq of length i+1
        int[] tails = new int[envelopes.length];
        int size = 0;

        for (int[] e : envelopes) {
            int h = e[1];

            // lower bound: first index with tails[idx] >= h
            int i = lowerBound(tails, size, h);
            if (i == size) {
                tails[size] = h;
                size += 1;
            } else {
                tails[i] = h;
            }
        }

        return size;
    }

    /** first index in tails[0, size) whose value is >= target */
    private int lowerBound(int[] tails, int size, int target) {
        int lo = 0;
        int hi = size;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (tails[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

}
