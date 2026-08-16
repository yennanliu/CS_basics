package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/data-stream-as-disjoint-intervals/description/

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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


    // V1
    // IDEA: TreeMap<start, end> + floorEntry / ceilingEntry
    /**
     *  The idiomatic Java shape: a TreeMap keyed by interval START gives the
     *  neighbour on each side of the new value in O(log n) via floorEntry and
     *  ceilingEntry -- no manual binary search and no list splice.
     *
     *  addNum becomes a genuine O(log n) instead of V0's O(n) ArrayList shift.
     *
     *  time  = O(log n) per addNum, O(n) per getIntervals
     *  space = O(n)
     */
    class SummaryRanges_1 {

        private TreeMap<Integer, Integer> map; // start -> end

        public SummaryRanges_1() {
            this.map = new TreeMap<>();
        }

        public void addNum(int value) {
            Map.Entry<Integer, Integer> lo = map.floorEntry(value);
            Map.Entry<Integer, Integer> hi = map.ceilingEntry(value);

            // already covered by the interval on the left
            if (lo != null && lo.getValue() >= value) {
                return;
            }

            boolean touchLeft = lo != null && lo.getValue() + 1 == value;
            boolean touchRight = hi != null && hi.getKey() == value + 1;

            if (touchLeft && touchRight) {
                map.put(lo.getKey(), hi.getValue());
                map.remove(hi.getKey());
            } else if (touchLeft) {
                map.put(lo.getKey(), value);
            } else if (touchRight) {
                map.remove(hi.getKey());
                map.put(value, hi.getValue());
            } else {
                map.put(value, value);
            }
        }

        public int[][] getIntervals() {
            int[][] res = new int[map.size()][2];
            int i = 0;
            for (Map.Entry<Integer, Integer> e : map.entrySet()) {
                res[i][0] = e.getKey();
                res[i][1] = e.getValue();
                i += 1;
            }
            return res;
        }
    }

    // V2
    // IDEA: BOOLEAN PRESENCE ARRAY over the bounded value range
    /**
     *  The statement caps values at 10^4, so a plain boolean[10001] can record
     *  membership and getIntervals() just walks it once collapsing runs.
     *
     *  addNum becomes O(1) with no structure at all -- the right trade when
     *  getIntervals is called rarely (the problem says at most 100 times) and
     *  addNum often (up to 3 * 10^4).
     *
     *  time  = O(1) per addNum, O(V) per getIntervals
     *  space = O(V), V = 10^4
     */
    class SummaryRanges_2 {

        private static final int LIMIT = 10001;
        private boolean[] seen;

        public SummaryRanges_2() {
            this.seen = new boolean[LIMIT];
        }

        public void addNum(int value) {
            seen[value] = true;
        }

        public int[][] getIntervals() {
            List<int[]> out = new ArrayList<>();
            int i = 0;
            while (i < LIMIT) {
                if (!seen[i]) {
                    i += 1;
                    continue;
                }
                int start = i;
                while (i < LIMIT && seen[i]) {
                    i += 1;
                }
                out.add(new int[] { start, i - 1 });
            }
            return out.toArray(new int[0][]);
        }
    }

    // V3
    // IDEA: UNION FIND over the value line
    /**
     *  Treat each seen value as a node and UNION it with any seen neighbour. Each
     *  component is then exactly one interval, and keeping the min/max per root
     *  gives its endpoints directly.
     *
     *  A different mental model entirely: intervals emerge from CONNECTIVITY rather
     *  than being maintained as objects.
     *
     *  time  = O(alpha) per addNum, O(V alpha) per getIntervals
     *  space = O(V)
     */
    class SummaryRanges_3 {

        private Map<Integer, Integer> parent;
        private Map<Integer, Integer> lo;
        private Map<Integer, Integer> hi;

        public SummaryRanges_3() {
            this.parent = new HashMap<>();
            this.lo = new HashMap<>();
            this.hi = new HashMap<>();
        }

        public void addNum(int value) {
            if (parent.containsKey(value)) {
                return;
            }
            parent.put(value, value);
            lo.put(value, value);
            hi.put(value, value);

            if (parent.containsKey(value - 1)) {
                join(value, value - 1);
            }
            if (parent.containsKey(value + 1)) {
                join(value, value + 1);
            }
        }

        public int[][] getIntervals() {
            Set<Integer> roots = new HashSet<>();
            for (int v : parent.keySet()) {
                roots.add(find(v));
            }
            List<int[]> out = new ArrayList<>();
            for (int r : roots) {
                out.add(new int[] { lo.get(r), hi.get(r) });
            }
            out.sort(Comparator.comparingInt(x -> x[0]));
            return out.toArray(new int[0][]);
        }

        private int find(int x) {
            while (parent.get(x) != x) {
                parent.put(x, parent.get(parent.get(x)));
                x = parent.get(x);
            }
            return x;
        }

        private void join(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) {
                return;
            }
            parent.put(ra, rb);
            lo.put(rb, Math.min(lo.get(ra), lo.get(rb)));
            hi.put(rb, Math.max(hi.get(ra), hi.get(rb)));
        }
    }

}
