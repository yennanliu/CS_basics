package LeetCodeJava.Recursion;

// https://leetcode.com/problems/beautiful-pairs/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  2613. Beautiful Pairs
 *  Hard
 *
 *  You are given two 0-indexed integer arrays nums1 and nums2 of the same length.
 *  A pair of indices (i,j) is called beautiful if
 *  |nums1[i] - nums1[j]| + |nums2[i] - nums2[j]| is the smallest amongst all
 *  possible indices pairs where i < j.
 *
 *  Return the beautiful pair. In the case that there are multiple beautiful pairs,
 *  return the lexicographically smallest pair.
 *
 *  Note that
 *   - |x| denotes the absolute value of x.
 *   - A pair of indices (i1, j1) is lexicographically smaller than (i2, j2)
 *     if i1 < i2 or i1 == i2 and j1 < j2.
 *
 *  Example 1:
 *    Input: nums1 = [1,2,3,2,4], nums2 = [2,3,1,2,3]
 *    Output: [0,3]
 *    Explanation: Consider index 0 and index 3. The value of
 *                 |nums1[i]-nums1[j]| + |nums2[i]-nums2[j]| is 1, which is the
 *                 smallest value we can achieve.
 *
 *  Example 2:
 *    Input: nums1 = [1,2,4,3,2,5], nums2 = [1,4,2,3,5,1]
 *    Output: [1,4]
 *    Explanation: Consider index 1 and index 4. The value of
 *                 |nums1[i]-nums1[j]| + |nums2[i]-nums2[j]| is 1.
 *
 *  Constraints:
 *    2 <= nums1.length, nums2.length <= 10^5
 *    nums1.length == nums2.length
 *    0 <= nums1[i] <= nums1.length
 *    0 <= nums2[i] <= nums2.length
 */
public class BeautifulPairs {

    // points sorted by x (then y, then original index); pts[k] = {x, y, idx}
    private int[][] pts;

    // V0
    // IDEA: CLOSEST PAIR OF POINTS (DIVIDE & CONQUER) UNDER THE MANHATTAN METRIC
    //       read (nums1[i], nums2[i]) as a 2D point -> the score of a pair is its
    //       L1 distance, so we want the CLOSEST PAIR. the classic divide & conquer
    //       works for L1 exactly as it does for L2.
    //
    //       1) DUPLICATES FIRST. two identical points have distance 0, which no
    //          other pair can beat. the group with the SMALLEST first index gives
    //          the lexicographically smallest such pair -> return it right away.
    //          this also makes every remaining point DISTINCT, so all recursive
    //          distances are >= 1 (never 0), which keeps the strip step bounded.
    //
    //       2) sort by x, split at the middle index, solve both halves and let d
    //          be the better of the two. any CROSS pair beating d must have both
    //          endpoints inside the vertical STRIP |x - xMid| <= d; sort that
    //          strip by y and, per point, only compare forward while the y gap
    //          stays <= d (a constant number of them, by a packing argument).
    //
    //       NOTE: the candidate is carried as the triple (dist, i, j) with i < j,
    //             so plain lexicographic comparison of the triple gives exactly
    //             the required tie break (smallest distance, then smallest pair).
    //       NOTE: the strip bounds use "<=" and not "<" on purpose - with "<" we
    //             would prune pairs that TIE with the current best, and a tying
    //             pair may still be lexicographically smaller.
    /**
     * time = O(N * log(N)^2)
     * space = O(N)
     */
    public int[] beautifulPair(int[] nums1, int[] nums2) {
        int n = nums1.length;

        // ---- 1) identical points -> distance 0, answer is immediate
        // key -> {firstIdx, secondIdx}, secondIdx = -1 while the point is unique
        Map<Long, int[]> seen = new HashMap<>();
        long base = n + 1L;
        for (int i = 0; i < n; i++) {
            long key = nums1[i] * base + nums2[i];
            int[] slot = seen.get(key);
            if (slot == null) {
                seen.put(key, new int[]{i, -1});
            } else if (slot[1] == -1) {
                slot[1] = i;
            }
        }
        int dupFirst = -1;
        int dupSecond = -1;
        for (int[] slot : seen.values()) {
            if (slot[1] != -1 && (dupFirst == -1 || slot[0] < dupFirst)) {
                dupFirst = slot[0];
                dupSecond = slot[1];
            }
        }
        if (dupFirst != -1) {
            return new int[]{dupFirst, dupSecond};
        }

        // ---- 2) divide & conquer on the distinct points
        this.pts = new int[n][3];
        for (int i = 0; i < n; i++) {
            pts[i][0] = nums1[i];
            pts[i][1] = nums2[i];
            pts[i][2] = i;
        }
        Arrays.sort(pts, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                if (a[0] != b[0]) {
                    return Integer.compare(a[0], b[0]);
                }
                if (a[1] != b[1]) {
                    return Integer.compare(a[1], b[1]);
                }
                return Integer.compare(a[2], b[2]);
            }
        });

        long[] best = solve(0, n);
        return new int[]{(int) best[1], (int) best[2]};
    }

    // returns {dist, i, j} for the closest pair inside pts[lo, hi)
    private long[] solve(int lo, int hi) {
        if (hi - lo < 2) {
            return new long[]{Long.MAX_VALUE, -1, -1};
        }
        int mid = (lo + hi) / 2;
        long xMid = pts[mid][0];

        long[] left = solve(lo, mid);
        long[] right = solve(mid, hi);
        long[] best = better(left, right);
        long d = best[0];

        List<int[]> strip = new ArrayList<>();
        for (int p = lo; p < hi; p++) {
            if (Math.abs(pts[p][0] - xMid) <= d) {
                strip.add(pts[p]);
            }
        }
        Collections.sort(strip, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[1], b[1]);
            }
        });

        for (int a = 0; a < strip.size(); a++) {
            int[] pa = strip.get(a);
            for (int b = a + 1; b < strip.size(); b++) {
                int[] pb = strip.get(b);
                if (pb[1] - pa[1] > d) {
                    break;
                }
                long dist = Math.abs(pa[0] - pb[0]) + Math.abs(pa[1] - pb[1]);
                long[] cand = new long[]{
                        dist,
                        Math.min(pa[2], pb[2]),
                        Math.max(pa[2], pb[2])
                };
                if (better(cand, best) == cand) {
                    best = cand;
                    d = best[0];
                }
            }
        }
        return best;
    }

    // lexicographic compare on (dist, i, j); returns the winner (a on a tie)
    private long[] better(long[] a, long[] b) {
        if (a[0] != b[0]) {
            return a[0] < b[0] ? a : b;
        }
        if (a[1] != b[1]) {
            return a[1] < b[1] ? a : b;
        }
        return a[2] <= b[2] ? a : b;
    }
}
