package LeetCodeJava.Array;

// https://leetcode.com/problems/merge-intervals/
/**
 * 56. Merge Intervals
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.
 *
 *
 *
 * Example 1:
 *
 * Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
 * Output: [[1,6],[8,10],[15,18]]
 * Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
 * Example 2:
 *
 * Input: intervals = [[1,4],[4,5]]
 * Output: [[1,5]]
 * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
 *
 *
 * Constraints:
 *
 * 1 <= intervals.length <= 104
 * intervals[i].length == 2
 * 0 <= starti <= endi <= 104
 *
 */
import java.util.*;

public class MergeIntervals {

    // V0
    // IDEA: LC 57 + `interval op`
    // time: O(N log N), space: O(N)
    public int[][] merge(int[][] intervals) {

        if (intervals == null || intervals.length == 0) {
            return null;
        }

        List<int[]> intervalList = new ArrayList<>(Arrays.asList(intervals));

        // sort
        intervalList.sort(Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();

        for (int[] x : intervalList) {
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
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < x[0]) {
                merged.add(x);
            }
            // case 3) if overlapped, update boundary
            else {
                /**
                 *  if overlap
                 *   last : |-----|
                 *   x :      |------|
                 */
                // NOTE : we set 0 idx as SMALLER val from merged last element (0 idx), input
                merged.get(merged.size() - 1)[0] = Math.min(merged.get(merged.size() - 1)[0], x[0]);
                // NOTE : we set 1 idx as BIGGER val from merged last element (1 idx), input
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], x[1]);
            }
        }

        return merged.toArray(new int[merged.size()][]);
    }

    /**
     *  Exp 1:
     *      input = [[1,3],[2,6],[8,10],[15,18]]
     *
     *      Step 1) : sort (1st element small -> big, then 2nd element, small -> big)
     *
     *           [[1,3],[2,6],[8,10],[15,18]]
     *
     *     Step 2) : merge interval
     *
     *          [[1,3]]
     *
     *          [[1,6]]
     *
     *          [[1,6], [8,10]]
     *
     *          [[1,6], [8,10], [15,18]]
     */
    // V0-0-1
    // IDEA : ARRAY OP + BOUNDARY OP
    // time: O(N log N), space: O(N)
    public int[][] merge_0_0_1(int[][] intervals) {
        /**
         *
         *
         *  1) Arrays.sort(intervals, ...) is used to sort the intervals array.
         *
         *  2) (a, b) -> Integer.compare(a[0], b[0]) is a lambda expression used as a comparator for sorting.
         *
         *  3) a and b are two intervals (arrays) being compared.
         *     a[0] and b[0] are the first elements of the intervals, which represent the start values of the intervals.
         *     Integer.compare(a[0], b[0])
         *     compares the start values of intervals a and b and
         *     returns -1 if a should come before b,
         *     0 if they are equal,
         *     and 1 if b should come before a.
         *
         *  -> Putting it all together, the Arrays.sort method uses the provided comparator (a, b) -> Integer.compare(a[0], b[0]) to sort the intervals array based on the start values of the intervals.
         *
         */
        // NOTE !!! sort on 1st element
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        // NOTE !!! we set res as linkedlist type (can use queue as well)
        LinkedList<int[]> res = new LinkedList<>();
        for (int[] interval : intervals) {
            // if the list of merged intervals is empty or if the current
            // interval does not overlap with the previous, simply append it.
            if (res.isEmpty() || res.getLast()[1] < interval[0]) {
                res.add(interval);
            }
            // otherwise, there is overlap, so we merge the current and previous
            // intervals.
            else {
                res.getLast()[1] = Math.max(res.getLast()[1], interval[1]);
            }
        }
        return res.toArray(new int[res.size()][]);
    }

    // V0-1
    // IDEA: SORTING + BOUNDARY OP
    // time: O(N log N), space: O(N)
    public int[][] merge_0_1(int[][] intervals) {
        // edge
        if(intervals == null || intervals.length == 0){
            return null;
        }

        // sort
        // 1st element : small -> big
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[0] - o2[0];
                return diff;
            }
        });

        Stack<int[]> st = new Stack<>();
        List<int[]> collected = new ArrayList<>();
        for(int[] x: intervals){
            // case 1) stack is empty
            if(st.isEmpty()){
                st.add(x);
            }else{
                /**
                 *  LC 57
                 *
                 *  since we already sorted intervals on
                 *  1st element as increasing order (small -> big)
                 *  the ONLY non overlap case is as below:
                 *
                 *    |----|  old
                 *            |------| new
                 */
                int[] prev = st.pop();
                // case 2) if NOT overlap
                if(prev[1] < x[0]){
                    st.add(prev);
                    st.add(x);
                }
                // case 3) OVERLAP
                else{
                    st.add(new int[]{ Math.min(prev[0], x[0]), Math.max(prev[1], x[1]) });
                }
            }
        }

        for(int i = 0; i < st.size(); i++){
            collected.add(st.get(i));
        }

        /** NOTE !!! below */
        return collected.toArray(new int[collected.size()][]);
    }

    // V0-2
    // IDEA: SORT + ARRAY OP + BOUNDARY HANDLING
    // time: O(N log N), space: O(N)
    public int[][] merge_0_2(int[][] intervals) {

        if (intervals.length <= 1){
            return intervals;
        }

        /** NOTE !!! array -> list */
        List<int[]> intervalList = new ArrayList<>(Arrays.asList(intervals));

        // sort on 1st element (0 idx)
        intervalList.sort(Comparator.comparingInt(x -> x[0]));

        List<int[]> tmp = new ArrayList<>();
        for (int[] x : intervalList){
            // case 1 : tmp is null
            if (tmp.size() == 0){
                tmp.add(x);
            }
            // case 2 : no overlap
            if (tmp.get(tmp.size() - 1)[1] < x[0]){
                tmp.add(x);
            }
            // case 3 : overlap
            else{
                int[] last = tmp.get(tmp.size()-1);
                tmp.remove(tmp.size()-1);
                tmp.add(new int[]{Math.min(last[0], x[0]), Math.max(last[1], x[1])});
            }
        }

        /** NOTE !!! list -> array */
        return tmp.toArray(new int[tmp.size()][]);
    }

    // V0-3
    // IDEA: ARRAY OP (GPT)
    // time: O(N log N), space: O(N)
    public int[][] merge_0_3(int[][] intervals) {
        // Edge case: If intervals is null or empty, return an empty array
        if (intervals == null || intervals.length == 0) {
            return new int[][] {}; // Return empty 2D array
        }

        // Edge case: If only one interval, return the same interval
        if (intervals.length == 1) {
            return new int[][] { intervals[0] };
        }

        // Sorting the intervals based on the start of the intervals
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]); // Compare based on the start time
            }
        });

        List<int[]> cache = new ArrayList<>();

        // Iterate through the sorted intervals and merge them if necessary
        for (int i = 0; i < intervals.length; i++) {
            // If the cache is empty or no overlap with the last interval
            if (cache.isEmpty() || cache.get(cache.size() - 1)[1] < intervals[i][0]) {
                cache.add(new int[] { intervals[i][0], intervals[i][1] });
            } else {
                // Overlapping intervals: merge them
                int[] last = cache.get(cache.size() - 1);
                last[1] = Math.max(last[1], intervals[i][1]); // Merge the intervals by updating the end time
            }
        }

        // Convert the List<int[]> to a 2D array and return the result
        /** NOTE !!! below op */
        return cache.toArray(new int[cache.size()][]);
    }

    // V1-1
    // https://neetcode.io/problems/merge-intervals
    // IDEA: SORTING
    // time: O(N log N), space: O(N)
    public int[][] merge_1_1(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> output = new ArrayList<>();
        output.add(intervals[0]);

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];
            int lastEnd = output.get(output.size() - 1)[1];

            if (start <= lastEnd) {
                output.get(output.size() - 1)[1] = Math.max(lastEnd, end);
            } else {
                output.add(new int[]{start, end});
            }
        }
        return output.toArray(new int[output.size()][]);
    }


    // V1-2
    // https://neetcode.io/problems/merge-intervals
    // IDEA:  Sweep Line Algorithm
    // time: O(N log N), space: O(N)
    public int[][] merge_1_2(int[][] intervals) {
        TreeMap<Integer, Integer> map = new TreeMap<>();

        for (int[] interval : intervals) {
            map.put(interval[0], map.getOrDefault(interval[0], 0) + 1);
            map.put(interval[1], map.getOrDefault(interval[1], 0) - 1);
        }

        List<int[]> res = new ArrayList<>();
        int have = 0;
        int[] interval = new int[2];

        for (int point : map.keySet()) {
            if (have == 0) interval[0] = point;
            have += map.get(point);
            if (have == 0) {
                interval[1] = point;
                res.add(new int[] {interval[0], interval[1]});
            }
        }

        return res.toArray(new int[res.size()][]);
    }


    // V1-3
    // https://neetcode.io/problems/merge-intervals
    // IDEA: GREEDY
    // time: O(N log N), space: O(N)
    public int[][] merge_1_3(int[][] intervals) {
        int max = 0;
        for (int i = 0; i < intervals.length; i++) {
            max = Math.max(intervals[i][0], max);
        }

        int[] mp = new int[max + 1];
        for (int i = 0; i < intervals.length; i++) {
            int start = intervals[i][0];
            int end = intervals[i][1];
            mp[start] = Math.max(end + 1, mp[start]);
        }

        int r = 0;
        int have = -1;
        int intervalStart = -1;
        for (int i = 0; i < mp.length; i++) {
            if (mp[i] != 0) {
                if (intervalStart == -1) intervalStart = i;
                have = Math.max(mp[i] - 1, have);
            }
            if (have == i) {
                intervals[r++] = new int[] { intervalStart, have };
                have = -1;
                intervalStart = -1;
            }
        }

        if (intervalStart != -1) {
            intervals[r++] = new int[] { intervalStart, have };
        }
        if (intervals.length == r) {
            return intervals;
        }

        int[][] res = new int[r][];
        for (int i = 0; i < r; i++) {
            res[i] = intervals[i];
        }

        return res;
    }


    // V2
    // IDEA : Sorting
    // https://leetcode.com/problems/merge-intervals/editorial/
    // time: O(N log N), space: O(N)
    public int[][] merge_2(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        // NOTE !!! we set res as linkedlist type
        LinkedList<int[]> merged = new LinkedList<>();
        for (int[] interval : intervals) {
            // if the list of merged intervals is empty or if the current
            // interval does not overlap with the previous, simply append it.
            if (merged.isEmpty() || merged.getLast()[1] < interval[0]) {
                merged.add(interval);
            }
            // otherwise, there is overlap, so we merge the current and previous
            // intervals.
            else {
                merged.getLast()[1] = Math.max(merged.getLast()[1], interval[1]);
            }
        }
        return merged.toArray(new int[merged.size()][]);
    }

    // V3
    // IDEA : Connected Components
    // https://leetcode.com/problems/merge-intervals/editorial/
    // time: O(N^2), space: O(N)
    private Map<int[], List<int[]>> graph;
    private Map<Integer, List<int[]>> nodesInComp;
    private Set<int[]> visited;

    // return whether two intervals overlap (inclusive)
    private boolean overlap(int[] a, int[] b) {
        return a[0] <= b[1] && b[0] <= a[1];
    }

    // build a graph where an undirected edge between intervals u and v exists
    // iff u and v overlap.
    private void buildGraph(int[][] intervals) {
        graph = new HashMap<>();
        for (int[] interval : intervals) {
            graph.put(interval, new LinkedList<>());
        }

        for (int[] interval1 : intervals) {
            for (int[] interval2 : intervals) {
                if (overlap(interval1, interval2)) {
                    graph.get(interval1).add(interval2);
                    graph.get(interval2).add(interval1);
                }
            }
        }
    }

    // merges all of the nodes in this connected component into one interval.
    private int[] mergeNodes(List<int[]> nodes) {
        int minStart = nodes.get(0)[0];
        for (int[] node : nodes) {
            minStart = Math.min(minStart, node[0]);
        }

        int maxEnd = nodes.get(0)[1];
        for (int[] node : nodes) {
            maxEnd = Math.max(maxEnd, node[1]);
        }

        return new int[] {minStart, maxEnd};
    }

    // use depth-first search to mark all nodes in the same connected component
    // with the same integer.
    private void markComponentDFS(int[] start, int compNumber) {
        Stack<int[]> stack = new Stack<>();
        stack.add(start);

        while (!stack.isEmpty()) {
            int[] node = stack.pop();
            if (!visited.contains(node)) {
                visited.add(node);

                if (nodesInComp.get(compNumber) == null) {
                    nodesInComp.put(compNumber, new LinkedList<>());
                }
                nodesInComp.get(compNumber).add(node);

                for (int[] child : graph.get(node)) {
                    stack.add(child);
                }
            }
        }
    }

    // gets the connected components of the interval overlap graph.
    private void buildComponents(int[][] intervals) {
        nodesInComp = new HashMap<>();
        visited = new HashSet<>();
        int compNumber = 0;

        for (int[] interval : intervals) {
            if (!visited.contains(interval)) {
                markComponentDFS(interval, compNumber);
                compNumber++;
            }
        }
    }

    public int[][] merge_3(int[][] intervals) {
        buildGraph(intervals);
        buildComponents(intervals);

        // for each component, merge all intervals into one interval.
        List<int[]> merged = new LinkedList<>();
        for (int comp = 0; comp < nodesInComp.size(); comp++) {
            merged.add(mergeNodes(nodesInComp.get(comp)));
        }

        return merged.toArray(new int[merged.size()][]);
    }

}
