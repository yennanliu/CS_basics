package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/russian-doll-envelopes/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    // V1
    // IDEA: CLASSIC O(n^2) LIS DP
    /**
     *  Sort by width, then the plain quadratic LIS recurrence
     *      dp[i] = 1 + max(dp[j]) over j < i with envelope j fitting inside i
     *
     *  NOTE !!! because the fit test is done EXPLICITLY on both dimensions here,
     *           this version does NOT need the `height descending on equal width`
     *           trick that V0 depends on -- it is the version to trust when that
     *           trick looks suspicious.
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public int maxEnvelopes_1(int[][] envelopes) {
        int n = envelopes.length;
        if (n == 0) {
            return 0;
        }

        int[][] e = envelopes.clone();
        Arrays.sort(e, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int best = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (e[j][0] < e[i][0] && e[j][1] < e[i][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    // V2
    // IDEA: PATIENCE LIS ON AN ArrayList VIA Collections.binarySearch
    /**
     *  Same patience sorting as V0, but the tails live in an ArrayList and the
     *  lower bound comes from the JDK's own binarySearch (whose negative return
     *  encodes the insertion point).
     *
     *  Shorter and library-idiomatic; the manual lowerBound of V0 exists only to
     *  avoid the boxing this version accepts.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public int maxEnvelopes_2(int[][] envelopes) {
        if (envelopes.length == 0) {
            return 0;
        }

        int[][] e = envelopes.clone();
        Arrays.sort(e, (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);

        List<Integer> tails = new ArrayList<>();
        for (int[] env : e) {
            int h = env[1];
            int idx = Collections.binarySearch(tails, h);
            if (idx < 0) {
                idx = -(idx + 1); // JDK encodes the insertion point this way
            }
            if (idx == tails.size()) {
                tails.add(h);
            } else {
                tails.set(idx, h);
            }
        }
        return tails.size();
    }

    // V3
    // IDEA: FENWICK TREE (BIT) FOR PREFIX MAXIMUM over compressed heights
    /**
     *  After sorting by width, `longest chain ending at height h` is a
     *  PREFIX MAXIMUM query over heights < h -- exactly what a max-Fenwick tree
     *  answers in O(log n).
     *
     *  Same O(n log n) as patience sorting, but this shape generalises to 3-D
     *  (add a second BIT dimension) where the tails array does not.
     *
     *  NOTE !!! equal widths must be processed as a BATCH, otherwise one envelope
     *           could `nest` inside another of the same width.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public int maxEnvelopes_3(int[][] envelopes) {
        int n = envelopes.length;
        if (n == 0) {
            return 0;
        }

        int[][] e = envelopes.clone();
        Arrays.sort(e, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

        // compress heights to 1..m
        int[] hs = new int[n];
        for (int i = 0; i < n; i++) {
            hs[i] = e[i][1];
        }
        int[] sorted = hs.clone();
        Arrays.sort(sorted);
        Map<Integer, Integer> rank = new HashMap<>();
        int r = 0;
        for (int v : sorted) {
            if (!rank.containsKey(v)) {
                rank.put(v, ++r);
            }
        }

        int[] tree = new int[r + 1]; // max-BIT
        int best = 0;

        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && e[j][0] == e[i][0]) {
                j += 1;
            }
            // query the whole equal-width batch BEFORE updating with any of it
            int[] got = new int[j - i];
            for (int t = i; t < j; t++) {
                got[t - i] = queryMax(tree, rank.get(e[t][1]) - 1) + 1;
                best = Math.max(best, got[t - i]);
            }
            for (int t = i; t < j; t++) {
                updateMax(tree, rank.get(e[t][1]), got[t - i], r);
            }
            i = j;
        }

        return best;
    }

    private int queryMax(int[] tree, int i) {
        int res = 0;
        for (; i > 0; i -= i & (-i)) {
            res = Math.max(res, tree[i]);
        }
        return res;
    }

    private void updateMax(int[] tree, int i, int v, int size) {
        for (; i <= size; i += i & (-i)) {
            tree[i] = Math.max(tree[i], v);
        }
    }

}
