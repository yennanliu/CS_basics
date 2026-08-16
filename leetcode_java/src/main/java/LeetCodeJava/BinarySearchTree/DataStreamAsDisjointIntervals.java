package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/data-stream-as-disjoint-intervals/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 352. Data Stream as Disjoint Intervals
 * Hard
 *
 * Given a data stream input of non-negative integers a1, a2, ..., an, summarize the numbers
 * seen so far as a list of disjoint intervals.
 *
 * Implement the SummaryRanges class:
 *
 * - SummaryRanges() Initializes the object with an empty stream.
 * - void addNum(int value) Adds the integer value to the stream.
 * - int[][] getIntervals() Returns a summary of the integers in the stream currently as a
 *   list of disjoint intervals [starti, endi]. The answer should be sorted by starti.
 *
 *
 * Example 1:
 *
 * Input
 * ["SummaryRanges", "addNum", "getIntervals", "addNum", "getIntervals", "addNum",
 *  "getIntervals", "addNum", "getIntervals", "addNum", "getIntervals"]
 * [[], [1], [], [3], [], [7], [], [2], [], [6], []]
 * Output
 * [null, null, [[1, 1]], null, [[1, 1], [3, 3]], null, [[1, 1], [3, 3], [7, 7]], null,
 *  [[1, 3], [7, 7]], null, [[1, 3], [6, 7]]]
 *
 * Explanation
 * SummaryRanges summaryRanges = new SummaryRanges();
 * summaryRanges.addNum(1);      // arr = [1]
 * summaryRanges.getIntervals(); // return [[1, 1]]
 * summaryRanges.addNum(3);      // arr = [1, 3]
 * summaryRanges.getIntervals(); // return [[1, 1], [3, 3]]
 * summaryRanges.addNum(7);      // arr = [1, 3, 7]
 * summaryRanges.getIntervals(); // return [[1, 1], [3, 3], [7, 7]]
 * summaryRanges.addNum(2);      // arr = [1, 2, 3, 7]
 * summaryRanges.getIntervals(); // return [[1, 3], [7, 7]]
 * summaryRanges.addNum(6);      // arr = [1, 2, 3, 6, 7]
 * summaryRanges.getIntervals(); // return [[1, 3], [6, 7]]
 *
 *
 * Constraints:
 *
 * 0 <= value <= 10^4
 * At most 3 * 10^4 calls will be made to addNum and getIntervals.
 * At most 10^2 calls will be made to getIntervals.
 *
 *
 * Follow up: What if there are lots of merges and the number of disjoint intervals is small
 * compared to the size of the data stream?
 *
 */
public class DataStreamAsDisjointIntervals {

    /**
     * Your SummaryRanges object will be instantiated and called as such:
     * SummaryRanges obj = new SummaryRanges();
     * obj.addNum(value);
     * int[][] param_2 = obj.getIntervals();
     */

    // V0
    // IDEA: SORTED LIST OF DISJOINT INTERVALS + BINARY SEARCH
    /**
     *  Keep `intervals` sorted by start and always DISJOINT & NON-ADJACENT.
     *  For a new value there are only 4 cases:
     *    1) already COVERED by the interval on the left  -> no-op
     *    2) touches BOTH neighbours  -> MERGE the two intervals into one
     *    3) touches only the LEFT    -> extend its end
     *    4) touches only the RIGHT   -> extend its start
     *    else                        -> insert a fresh [value, value]
     *
     *  time  = O(log n) search + O(n) list shift per addNum, O(n) per getIntervals
     *  space = O(n)
     */
    class SummaryRanges {

        // list of {start, end}, sorted & disjoint
        private List<int[]> intervals;

        public SummaryRanges() {
            this.intervals = new ArrayList<>();
        }

        public void addNum(int value) {
            List<int[]> arr = this.intervals;

            // idx = index of the FIRST interval whose start > value
            int idx = firstStartGreaterThan(arr, value);

            // case 1: value already inside the interval to the left -> nothing to do
            if (idx > 0 && arr.get(idx - 1)[1] >= value) {
                return;
            }

            boolean touchLeft = idx > 0 && arr.get(idx - 1)[1] + 1 == value;
            boolean touchRight = idx < arr.size() && arr.get(idx)[0] == value + 1;

            if (touchLeft && touchRight) {
                /** NOTE !!!
                 *
                 *  value GLUES the two neighbouring intervals together
                 *  -> the left one swallows the right one, then drop the right
                 */
                arr.get(idx - 1)[1] = arr.get(idx)[1];
                arr.remove(idx);
            } else if (touchLeft) {
                arr.get(idx - 1)[1] = value;
            } else if (touchRight) {
                arr.get(idx)[0] = value;
            } else {
                arr.add(idx, new int[] { value, value });
            }
        }

        public int[][] getIntervals() {
            // return a COPY so callers cannot corrupt the internal state
            int[][] res = new int[intervals.size()][2];
            for (int i = 0; i < intervals.size(); i++) {
                res[i][0] = intervals.get(i)[0];
                res[i][1] = intervals.get(i)[1];
            }
            return res;
        }

        /** binary search: index of the first interval with start > value */
        private int firstStartGreaterThan(List<int[]> arr, int value) {
            int lo = 0;
            int hi = arr.size();
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (arr.get(mid)[0] > value) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            return lo;
        }
    }

}
