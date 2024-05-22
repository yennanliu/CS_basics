package LeetCodeJava.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// https://leetcode.com/problems/insert-interval/description/
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

        /** NOTE !!! add newInterval first, then sort */
        intervalList.add(newInterval);

        /** NOTE !!! need to SORT list first */
        intervalList.sort(Comparator.comparingInt(a -> a[0]));

        /** NOTE !!! setup result list */
        List<int[]> merged = new ArrayList<>();

        for (int[] x : intervalList){
            // case 1) : if merged is empty, nothing to remove, add new item to merged directly
            // & case 2) : if no overlap, add new item to merged directly
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
                // NOTE : we set 0 idx as smaller val from merged last element (0 idx), input
                merged.get(merged.size()-1)[0] = Math.min(merged.get(merged.size()-1)[0], x[0]);
                // NOTE : we set 1 idx as bigger val from merged last element (1 idx), input
                merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1], x[1]);
            }
        }

        /** NOTE !!! transform list to array */
        return merged.toArray(new int[merged.size()][]);
    }

    // V0'
    // IDEA : ARRAY + BOUNDARY OP (by GPT)
    public int[][] insert_0(int[][] intervals, int[] newInterval) {

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

}
