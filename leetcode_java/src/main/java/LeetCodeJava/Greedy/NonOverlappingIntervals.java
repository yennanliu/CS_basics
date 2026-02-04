package LeetCodeJava.Greedy;

// https://leetcode.com/problems/non-overlapping-intervals/description/
/**
 * 435. Non-overlapping Intervals
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of intervals intervals where intervals[i] = [starti, endi], return the minimum number of intervals you need to remove to make the rest of the intervals non-overlapping.
 *
 * Note that intervals which only touch at a point are non-overlapping. For example, [1, 2] and [2, 3] are non-overlapping.
 *
 *
 *
 * Example 1:
 *
 * Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
 * Output: 1
 * Explanation: [1,3] can be removed and the rest of the intervals are non-overlapping.
 * Example 2:
 *
 * Input: intervals = [[1,2],[1,2],[1,2]]
 * Output: 2
 * Explanation: You need to remove two [1,2] to make the rest of the intervals non-overlapping.
 * Example 3:
 *
 * Input: intervals = [[1,2],[2,3]]
 * Output: 0
 * Explanation: You don't need to remove any of the intervals since they're already non-overlapping.
 *
 *
 * Constraints:
 *
 * 1 <= intervals.length <= 105
 * intervals[i].length == 2
 * -5 * 104 <= starti < endi <= 5 * 104
 *
 */
import java.util.*;

public class NonOverlappingIntervals {

    // V0
    // IDEA: SORT + INTERVAL overlap OP (gemini)
    public int eraseOverlapIntervals(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return 0;
        }

        // 1. Sort by start time (your current approach)
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> collected = new ArrayList<>();
        int op = 0;

        for (int i = 0; i < intervals.length; i++) {
            int[] cur = intervals[i];

            if (i == 0) {
                collected.add(cur);
            } else {
                int[] prev = collected.get(collected.size() - 1);

                /**  since we ALREADY sort on 1st element (small -> big),
                 *   below are overlap case:
                 *
                 *    |----| prev
                 *       |---------| cur
                 *
                 *   |-----|  prev
                 *     |--|   cur
                 *
                 */
                // Check if they overlap
                if (prev[1] > cur[0]) {
                    /** NOTE
                     *
                     *  need to increase op, no matter which type of `overlap`
                     */
                    op++; // One MUST be removed

                    /** NOTE
                     *
                     *  if below type overlapping,
                     *  we should remove prev, and add cur to cache
                     *
                     *    |------|  prev
                     *      |--|     cur
                     *
                     */
                    // If current ends EARLIER than prev, it's better to keep current.
                    // We "remove" prev by replacing it with current.
                    if (cur[1] < prev[1]) {
                        collected.set(collected.size() - 1, cur);
                    }
                    // If cur ends LATER or SAME as prev, we just "remove" cur (do nothing)
                } else {
                    // No overlap: just add it to our "kept" list
                    collected.add(cur);
                }
            }
        }

        return op;
    }

    // V0-0-0-1
    // IDEA: SORT + INTERVAL (fixed by gemini)
    public int eraseOverlapIntervals_0_0_0_1(int[][] intervals) {
        if (intervals == null || intervals.length <= 1)
            return 0;


        /** NOTE !!!
         *
         *  sort on `start time` ONLY (1st idx) (small -> big)
         */
        // 1. Sort by Start Time (using Integer.compare to prevent overflow)
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        Deque<int[]> deque = new LinkedList<>();

        for (int[] x : intervals) {
            // if dequeue is empty
            if (deque.isEmpty()) {
                deque.add(x);
            }
            // if dequeue is NOT empty
            else {
                int[] prev = deque.getLast();

                /** NOTE !!!
                 *
                 *  we need to handle 2 cases
                 *
                 *   1. NO overlap
                 *   2. overlap
                 */
                // Case A: NO overlap (New starts after/at prev ends)
                if (x[0] >= prev[1]) {
                    deque.addLast(x);
                }
                // Case B: OVERLAP detected
                else {
                    /** NOTE !!!
                     *
                     *  if overlapped,
                     *  we need to keep the `the one that ends EARLIER` one,
                     *  so it ensures that we can have
                     *  `least` remove op in the future
                     *  (greedy idea)
                     *
                     *   -> `x[1] < prev[1]`
                     */
                    // Keep the one that ends EARLIER
                    if (x[1] < prev[1]) {
                        // NOTE !!! pop last one
                        deque.removeLast();
                        // NOTE !!! add cur one
                        deque.addLast(x);
                    }
                    /** NOTE !!! */
                    // If x[1] >= prev[1], we just ignore x (effectively removing it)
                }
            }
        }

        return intervals.length - deque.size();
    }


    // V0-0-1
    // IDEA : sorting + intervals (modified by GPT)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/non-overlapping-intervals.py
    /**
     *
     *  NOTE !!!
     *
     *   1. sort on 2nd element in Ascending order (small -> big)
     *   2. No need to use queue
     *
     */
    /**
     *
     *   Consider examples:
     *
     *   exp 1)
     *
     *    A: [1, 3]
     *    B: [2, 5]
     *
     *
     *  exp 2)
     *
     *    intervals = [[1, 100], [2, 3], [3, 4], [4, 5]]
     *
     */
    /**
     *  NOTE !!!
     *
     * > why NOT sort `1st element (small -> big), then 2nd element (big -> small) ?
     * ---
     *
     * ### ✅ Short Answer:
     *
     * We sort **by end time only** because:
     *
     * > 👉 **To keep the maximum number of non-overlapping intervals,
     *    we always want to keep the interval that ends the earliest.**
      *
     * -> e.g. sort on `2nd element (small -> big) allow us to have
     *         GLOBAL maximum on the `most space` in the future
     *
     * This leaves **the most "space"** for future intervals —
     * a **greedy strategy** that is **provably optimal** for this problem.
     *
     * ---
     *
     * ### 🔍 Let’s break it down step by step:
     *
     * #### 🎯 **Your Goal:**
     *
     * Minimize the number of intervals to remove → i.e.,
     * **maximize** the number of non-overlapping intervals to keep.
     *
     * ---
     *
     * ### 🧠 The Greedy Insight:
     *
     * Let’s look at two intervals:
     *
     * ```
     * A: [1, 3]
     * B: [2, 5]
     * ```
     *
     * Both overlap. Which one should we keep?
     *
     * * If you **keep A** (ends earlier), you’re more likely to fit more intervals after it.
     * * If you **keep B** (ends later), it blocks future intervals.
     *
     * ✅ So: **prefer intervals that end earlier** → more room for what's next.
     *
     * ---
     *
     * ### 💡 Why Not Sort by Start Time?
     *
     * If we sort by start time (like your original logic), you might make **locally suboptimal decisions**.
     *
     * Example:
     *
     * ```java
     * intervals = [[1, 100], [2, 3], [3, 4], [4, 5]]
     * ```
     *
     * If you sort by **start time**, you process `[1, 100]` first and remove the rest.
     *
     * But if you sort by **end time**, you process `[2, 3]`, `[3, 4]`, `[4, 5]` — and only remove `[1, 100]`.
     *
     * ✅ **Result: 3 kept vs. 1 kept** — greedy on end time is clearly better.
     *
     * ---
     *
     * ### 📊 Formal Reason (from Algorithm Theory):
     *
     * This problem is a variation of the **Activity Selection Problem** (Interval Scheduling Maximization), where:
     *
     * > Sorting by end time allows a greedy approach to select the maximal number of non-overlapping activities.
     *
     * It's a **well-known optimal greedy strategy** for interval problems in algorithm literature.
     *
     * ---
     *
     * ### ✍️ Summary Table:
     *
     * | Sort By          | Behavior                                  | Result                               |
     * | ---------------- | ----------------------------------------- | ------------------------------------ |
     * | Start Time (ASC) | Prioritizes early-starting intervals      | Not optimal — may block better ones  |
     * | End Time (ASC) ✅ | Prioritizes short, early-ending intervals | ✅ Optimal — maximizes future options |
     *
     */
    public int eraseOverlapIntervals_0_0_1(int[][] intervals) {

        /** NOTE !!!
         *
         *   sort on 2nd element in Ascending order (small -> big)
         *
         *   same as this op: `Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));`
         *
         *
         *
         *   1) Why Not Sort by Start Time ?
         *
         *    -> If we sorted by the start time (intervals[i][0]),
         *       it would not necessarily minimize the number of intervals to
         *       remove because we wouldn't have the benefit of always selecting the
         *       interval that finishes the earliest, which is crucial for maximizing
         *       the number of non-overlapping intervals.
         *
         *    -> In conclusion, sorting by end time provides the correct and
         *       optimal solution for this problem.
         *
         *
         *  2)  Key Concept:
         *     -> The goal of the problem is to remove the minimum number of intervals to
         *        ensure that no two intervals overlap. To solve this optimally,
         *        sorting by the end time is the preferred approach. This is because:
         *
         *     -> Once you have sorted intervals by their `end time`,
         *        you can greedily choose the intervals that end earliest,
         *        leaving more room for subsequent intervals. This helps you avoid
         *        overlap and minimize the number of intervals you need to remove.
         *
         *
         *  3) Why Sorting by Start Time is Problematic:
         *     -> If you sort by the start time instead of the end time,
         *        you might end up with overlapping intervals that are harder to manage.
         */
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));

        int cnt = 0;
        int[] last = intervals[0];

        // NOTE !! start from idx = 1
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < last[1]) {
                cnt++;
            } else {
                /**
                 *  NOTE !!!
                 *
                 *  if NOT overlap, need to use `bigger` val as 2nd element in last element
                 *
                 *  e.g.
                 *  // No overlap, update the lastEndTime
                 */
                last[1] = Math.max(intervals[i][1], last[1]);
            }
        }
        return cnt;
    }

    // V0-1
    // IDEA : array + boundary op (GPT)
    public int eraseOverlapIntervals_0_1(int[][] intervals) {

        if (intervals.length <= 1) {
            return 0;
        }

        int res = 0;

        // Sort intervals by their end time (second element)
        /** NOTE !!!
         *
         *   sort on 2nd element
         *
         *   -> Ascending order (small -> big)
         *
         *   same as this op: `Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));`
         */
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[1]));

        // Keep track of the end time of the last interval that doesn't overlap
        int lastEndTime = intervals[0][1];

        // NOTE !! start from idx = 1
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < lastEndTime) {
                // Overlapping interval, increment the count
                res++;
            } else {
                // No overlap, update the lastEndTime
                lastEndTime = intervals[i][1];
            }
        }

        return res;
    }

    // V0-2
    // IDEA: SORT + ARRAY OP (GPT)
    public int eraseOverlapIntervals_0_2(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }

        // Step 1: Sort intervals by `end` time
        /** NOTE !!!
         *
         *   sort on 2nd element
         *
         *   -> Ascending order (small -> big)
         *
         *   same as this op: `Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));`
         */
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

        int count = 0;
        int prevEnd = intervals[0][1]; // Track last added interval's end

        // Step 2: Iterate through intervals
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < prevEnd) {
                count++; // Overlapping, remove one interval
            } else {
                prevEnd = intervals[i][1]; // Update end time if no overlap
            }
        }

        return count; // Number of intervals removed
    }

    // V0-3
    // IDEA: GREEDY + SORTING (gpt)
    public int eraseOverlapIntervals_0_3(int[][] intervals) {
        // Edge case: empty or single interval, nothing to remove
        if (intervals == null || intervals.length <= 1) {
            return 0;
        }

        // Sort intervals by end time (ascending) — Greedy strategy
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

        int count = 0;
        int prevEnd = intervals[0][1];

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < prevEnd) {
                // Overlapping: remove current interval
                count++;
            } else {
                // Non-overlapping: update end marker
                prevEnd = intervals[i][1];
            }
        }

        return count;
    }


    // V0''
    // TODO : implement it
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/non-overlapping-intervals.py
//    public int eraseOverlapIntervals(int[][] intervals) {
//        return 0;
//    }

    // V1
    // IDEA : STACK
    // https://leetcode.com/problems/non-overlapping-intervals/solutions/4683650/4-line-solution-easy-to-understand-java-stack/
    public int eraseOverlapIntervals_1(int[][] intervals) {
        Arrays.sort(intervals,(a, b)->a[1]-b[1]);
        Stack<int[]> stk = new Stack<>();
        int res=0;
        for(int[] i : intervals){
            if(!stk.isEmpty() && i[0]< stk.peek()[1]){
                res++;
            }else{
                stk.push(i);
            }
        }

        return res;
    }

    // V2
    // IDEA : SORT
    // https://leetcode.com/problems/non-overlapping-intervals/solutions/3786438/java-easy-solution-using-sorting-explained/
    public int eraseOverlapIntervals_2(int[][] intervals) {
        // Sort by ending time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
        int prev = 0, count  = 1;
        // if end is same, sort by start, bigger start in front
        for(int i = 0; i < intervals.length; i ++) {
            if(intervals[i][0] >= intervals[prev][1]) {
                prev = i;
                count ++;
            }
        }
        return intervals.length - count;
    }

    // V3
    // https://leetcode.com/problems/non-overlapping-intervals/submissions/1202459919/
    public int eraseOverlapIntervals_3(int[][] in) {
        Arrays.sort(in, (a,b)->a[1]-b[1]);
        int res=-1, p[]=in[0];
        for(int[] i: in)
            if(i[0]<p[1]) res++;
            else p=i;
        return res;
    }



}
