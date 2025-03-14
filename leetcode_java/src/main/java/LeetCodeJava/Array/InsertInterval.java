package LeetCodeJava.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// https://leetcode.com/problems/insert-interval/description/
/**
 * 57. Insert Interval
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an array of non-overlapping intervals intervals where intervals[i] = [starti, endi] represent the start and the end of the ith interval and intervals is sorted in ascending order by starti. You are also given an interval newInterval = [start, end] that represents the start and end of another interval.
 *
 * Insert newInterval into intervals such that intervals is still sorted in ascending order by starti and intervals still does not have any overlapping intervals (merge overlapping intervals if necessary).
 *
 * Return intervals after the insertion.
 *
 * Note that you don't need to modify intervals in-place. You can make a new array and return it.
 *
 *
 *
 * Example 1:
 *
 * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
 * Output: [[1,5],[6,9]]
 * Example 2:
 *
 * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
 * Output: [[1,2],[3,10],[12,16]]
 * Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
 *
 *
 * Constraints:
 *
 * 0 <= intervals.length <= 104
 * intervals[i].length == 2
 * 0 <= starti <= endi <= 105
 * intervals is sorted by starti in ascending order.
 * newInterval.length == 2
 * 0 <= start <= end <= 105
 *
 */
public class InsertInterval {

    // V0
    // IDEA : ARRAY + BOUNDARY OP + sort
    public int[][] insert(int[][] intervals, int[] newInterval) {

        if (intervals.length == 0){
            if (newInterval.length == 0){
                return new int[][]{};
            }
            return new int[][]{newInterval};
        }

        /** NOTE !!!  create list from array */
        List<int[]> intervalList = new ArrayList<>(Arrays.asList(intervals));

        /** NOTE !!!
         *
         * 1) add newInterval to array
         */
        intervalList.add(newInterval);

        /** NOTE !!!
         *
         * SORT list (ascending order) (small -> big)
         */
        intervalList.sort(Comparator.comparingInt(a -> a[0]));

        /** NOTE !!! setup result list */
        List<int[]> merged = new ArrayList<>();

        for (int[] x : intervalList){
            /**
             *  NOTE !!!
             *
             *   since we already added `new interval`, and sort array in increasing order (small order)
             *
             *   -> all `old` interval's left boundary should be SMALLER than `new`interval's  left boundary
             *   -> e.g. old[0] < new[0]
             *   -> so, when consider `NON OVERLAP` case,  ONLY 1 case could happen (as below)
             *
             *     |-----|      old
             *             |-----------|  new
             *
             *
             *
             *
             *  case 1) : if merged is empty, nothing to remove, add new item to merged directly
             *  case 2) : if no overlap, add new item to merged directly
             *               -> NOTE !!!
             *                   since array already sorted, so THE ONLY possible NON-OVERLAP case is as below:
             *                     |----|          (old)
             *                            |-----|  (new)
             *                   -> so ALL we need to check is:
             *                          `new[0] > old[1]` or not
             *
             */
            if (merged.isEmpty() || merged.get(merged.size()-1)[1] < x[0]){
                merged.add(x);
            }
            // case 3) if overlapped, update boundary
            else{
                /**
                 *  if overlap
                 *   last : |-----|
                 *   x :      |------|
                 */
                // NOTE : we set 0 idx as SMALLER val from merged last element (0 idx), input
                merged.get(merged.size()-1)[0] = Math.min(merged.get(merged.size()-1)[0], x[0]);
                // NOTE : we set 1 idx as BIGGER val from merged last element (1 idx), input
                merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1], x[1]);
            }
        }

        /** NOTE !!! transform list to array */
        return merged.toArray(new int[merged.size()][]);
    }

    // V0-1
    // IDEA : ARRAY + BOUNDARY OP (by GPT)
    public int[][] insert_0_1(int[][] intervals, int[] newInterval) {

        // Convert the intervals array to a list for easier manipulation
        List<int[]> intervalList = new ArrayList<>(Arrays.asList(intervals));
        // Add the new interval to the list
        intervalList.add(newInterval);

        // Sort the intervals by their starting point
        intervalList.sort(Comparator.comparingInt(a -> a[0]));

        // List to hold the merged intervals
        List<int[]> merged = new ArrayList<>();

        // Iterate over the sorted intervals
        for (int[] interval : intervalList) {
            // If merged list is empty or there's no overlap, add the interval directly
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(interval);
            } else {
                // If there is an overlap, merge the intervals
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], interval[1]);
            }
        }

        // Convert the merged list back to an array and return
        /** NOTE !!! transform list to array */
        return merged.toArray(new int[merged.size()][]);
    }

    // V0-2
    // IDEA: ARRAY OP (GPT)
    public int[][] insert_0_2(int[][] intervals, int[] newInterval) {
        // Edge case: if intervals are empty, just return the newInterval
        if (intervals == null || intervals.length == 0) {
            return new int[][] { newInterval };
        }

        List<int[]> result = new ArrayList<>();
        int i = 0;

        // Add all intervals that end before the newInterval starts
        while (i < intervals.length && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]);
            i++;
        }

        // Merge overlapping intervals
        while (i < intervals.length && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval); // Add the merged interval

        // Add all intervals that start after the newInterval ends
        while (i < intervals.length) {
            result.add(intervals[i]);
            i++;
        }

        // Convert List to 2D array and return the result
        return result.toArray(new int[result.size()][]);
    }

    // V1-1
    // https://neetcode.io/problems/insert-new-interval
    // IDEA: LINEAR SEARCH
    public int[][] insert_1_1(int[][] intervals, int[] newInterval) {
        int n = intervals.length, i = 0;
        List<int[]> res = new ArrayList<>();

        while (i < n && intervals[i][1] < newInterval[0]) {
            res.add(intervals[i]);
            i++;
        }

        while (i < n && newInterval[1] >= intervals[i][0]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        res.add(newInterval);

        while (i < n) {
            res.add(intervals[i]);
            i++;
        }

        return res.toArray(new int[res.size()][]);
    }


    // V1-2
    // https://neetcode.io/problems/insert-new-interval
    // IDEA: BINARY SEARCH
    public int[][] insert_1_2(int[][] intervals, int[] newInterval) {
        if (intervals.length == 0) {
            return new int[][] { newInterval };
        }

        int n = intervals.length;
        int target = newInterval[0];
        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (intervals[mid][0] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < left; i++) {
            result.add(intervals[i]);
        }
        result.add(newInterval);
        for (int i = left; i < n; i++) {
            result.add(intervals[i]);
        }

        List<int[]> merged = new ArrayList<>();
        for (int[] interval : result) {
            if (merged.isEmpty() ||
                    merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(interval);
            } else {
                merged.get(merged.size() - 1)[1] = Math.max(
                        merged.get(merged.size() - 1)[1],
                        interval[1]
                );
            }
        }

        return merged.toArray(new int[0][]);
    }


    // V1-3
    // https://neetcode.io/problems/insert-new-interval
    // IDEA: GREEDY
    public int[][] insert_1_3(int[][] intervals, int[] newInterval) {
        List<int[]> res = new ArrayList<>();
        for (int[] interval : intervals) {
            if (newInterval == null || interval[1] < newInterval[0]) {
                res.add(interval);
            } else if (interval[0] > newInterval[1]) {
                res.add(newInterval);
                res.add(interval);
                newInterval = null;
            } else {
                newInterval[0] = Math.min(interval[0], newInterval[0]);
                newInterval[1] = Math.max(interval[1], newInterval[1]);
            }
        }
        if (newInterval != null) res.add(newInterval);
        return res.toArray(new int[res.size()][]);
    }

}
