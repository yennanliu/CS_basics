package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-right-interval/

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 *  436. Find Right Interval
 *  Medium
 *
 *  You are given an array of intervals, where intervals[i] = [start_i, end_i]
 *  and each start_i is unique.
 *
 *  The right interval for an interval i is an interval j such that
 *  start_j >= end_i and start_j is minimized. Note that i may equal j.
 *
 *  Return an array of right interval indices for each interval i.
 *  If no right interval exists for interval i, then put -1 at index i.
 *
 *  Example 1:
 *
 *  Input: intervals = [[3,4],[2,3],[1,2]]
 *  Output: [-1,0,1]
 *
 *  Example 2:
 *
 *  Input: intervals = [[1,4],[2,3],[3,4]]
 *  Output: [-1,2,-1]
 *
 *  Constraints:
 *
 *  1 <= intervals.length <= 2 * 10^4
 *  intervals[i].length == 2
 *  -10^6 <= start_i <= end_i <= 10^6
 *  The start point of each interval is unique.
 */
public class FindRightInterval {

    // V0
    // IDEA: sort (start, original idx) pairs by start, then binary search the
    //       smallest start >= end for every interval
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] findRightInterval(int[][] intervals) {
        int n = intervals.length;
        // starts[k] = {start value, original index}
        int[][] starts = new int[n][2];
        for (int i = 0; i < n; i++) {
            starts[i][0] = intervals[i][0];
            starts[i][1] = i;
        }
        Arrays.sort(starts, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int end = intervals[i][1];
            // find first idx in starts whose start >= end
            int l = 0;
            int r = n;
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (starts[mid][0] >= end) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            res[i] = (l == n) ? -1 : starts[l][1];
        }
        return res;
    }

    // V1
    // IDEA: TreeMap<start, idx> + ceilingEntry - let the balanced BST do the
    //       "smallest start >= end" lookup instead of a hand rolled binary search
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] findRightInterval_1(int[][] intervals) {
        int n = intervals.length;
        TreeMap<Integer, Integer> startToIdx = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            startToIdx.put(intervals[i][0], i); // starts are unique
        }

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            Map.Entry<Integer, Integer> e = startToIdx.ceilingEntry(intervals[i][1]);
            res[i] = (e == null) ? -1 : e.getValue();
        }
        return res;
    }

    // V2
    // IDEA: brute force O(n^2) - for every interval scan all others and keep the
    //       smallest start >= end. Kept as a readable correctness reference.
    /**
     * time = O(n^2)
     * space = O(1) (excluding output)
     */
    public int[] findRightInterval_2(int[][] intervals) {
        int n = intervals.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int bestIdx = -1;
            int bestStart = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (intervals[j][0] >= intervals[i][1] && intervals[j][0] < bestStart) {
                    bestStart = intervals[j][0];
                    bestIdx = j;
                }
            }
            res[i] = bestIdx;
        }
        return res;
    }
}
