package LeetCodeJava.Design;

// https://leetcode.com/problems/range-module/description/

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;

/**
 * 715. Range Module
 * Hard
 *
 * A Range Module is a module that tracks ranges of numbers. Design a data structure
 * to track the ranges represented as half-open intervals and query about them.
 *
 * A half-open interval [left, right) denotes all the real numbers x where
 * left <= x < right.
 *
 * Implement the RangeModule class:
 *
 *   - RangeModule() Initializes the object of the data structure.
 *   - void addRange(int left, int right) Adds the half-open interval [left, right),
 *     tracking every real number in that interval. Adding an interval that partially
 *     overlaps with currently tracked numbers should add any numbers in the interval
 *     [left, right) that are not already tracked.
 *   - boolean queryRange(int left, int right) Returns true if every real number in the
 *     interval [left, right) is currently being tracked, and false otherwise.
 *   - void removeRange(int left, int right) Stops tracking every real number currently
 *     being tracked in the half-open interval [left, right).
 *
 *
 * Example 1:
 *
 * Input
 * ["RangeModule", "addRange", "removeRange", "queryRange", "queryRange", "queryRange"]
 * [[], [10, 20], [14, 16], [10, 14], [13, 15], [16, 17]]
 * Output
 * [null, null, null, true, false, true]
 *
 * Explanation
 * RangeModule rangeModule = new RangeModule();
 * rangeModule.addRange(10, 20);
 * rangeModule.removeRange(14, 16);
 * rangeModule.queryRange(10, 14); // return True
 * rangeModule.queryRange(13, 15); // return False
 * rangeModule.queryRange(16, 17); // return True
 *
 *
 * Constraints:
 *
 * 1 <= left < right <= 10^9
 * At most 10^4 calls will be made to addRange, queryRange, and removeRange.
 *
 */
public class RangeModule {

    /**
     * Your RangeModule object will be instantiated and called as such:
     * RangeModule obj = new RangeModule();
     * obj.addRange(left,right);
     * boolean param_2 = obj.queryRange(left,right);
     * obj.removeRange(left,right);
     */

    // V0
    // IDEA: SORTED LIST OF BOUNDARIES
    /**
     *   Keep a SINGLE flat sorted list `bounds` of the endpoints of the tracked,
     *   pairwise DISJOINT, half-open intervals:
     *
     *       bounds = [s0, e0, s1, e1, ...]   ->  [s0,e0) U [s1,e1) U ...
     *
     *   So an EVEN index is a range START and an ODD index is a range END.
     *   That single PARITY fact drives all three operations:
     *
     *     - a point x is tracked  <=>  bisectRight(bounds, x) is ODD
     *
     *     - adding    [l, r): splice out everything between l and r, keeping l
     *                 only if l was NOT already inside a range (index even),
     *                 and keeping r only if r was NOT inside a range.
     *
     *     - removing  [l, r): the MIRROR IMAGE (keep the boundary when the index is
     *                 ODD, i.e. when we are cutting a hole inside an existing range).
     *
     *   time  = O(n) per addRange / removeRange (list splice), O(log n) per queryRange
     *   space = O(n), n = number of tracked disjoint intervals
     */
    class RangeModuleImpl {

        // flat sorted boundary list; even idx = start, odd idx = end
        private List<Integer> bounds;

        public RangeModuleImpl() {
            this.bounds = new ArrayList<>();
        }

        public void addRange(int left, int right) {
            int i = bisectLeft(left);
            int j = bisectRight(right);

            List<Integer> merged = new ArrayList<>();
            // `left` sits OUTSIDE any tracked range -> it becomes the new start
            if (i % 2 == 0) {
                merged.add(left);
            }
            // `right` sits OUTSIDE any tracked range -> it becomes the new end
            if (j % 2 == 0) {
                merged.add(right);
            }

            // everything strictly between is SWALLOWED by the new range
            splice(i, j, merged);
        }

        public boolean queryRange(int left, int right) {
            int i = bisectRight(left);
            int j = bisectLeft(right);
            /** NOTE !!!
             *
             *  no boundary in between (i == j) AND `left` is inside a range (ODD index)
             */
            return i == j && i % 2 == 1;
        }

        public void removeRange(int left, int right) {
            int i = bisectLeft(left);
            int j = bisectRight(right);

            List<Integer> kept = new ArrayList<>();
            // `left` is INSIDE a tracked range -> that range now ends at `left`
            if (i % 2 == 1) {
                kept.add(left);
            }
            // `right` is INSIDE a tracked range -> a new range starts at `right`
            if (j % 2 == 1) {
                kept.add(right);
            }

            splice(i, j, kept);
        }

        /** replace bounds[i, j) with `repl` (python's `bounds[i:j] = repl`) */
        private void splice(int i, int j, List<Integer> repl) {
            List<Integer> next = new ArrayList<>(bounds.subList(0, i));
            next.addAll(repl);
            next.addAll(bounds.subList(j, bounds.size()));
            this.bounds = next;
        }

        /** first index whose value is >= target */
        private int bisectLeft(int target) {
            int lo = 0;
            int hi = bounds.size();
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (bounds.get(mid) < target) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            return lo;
        }

        /** first index whose value is > target */
        private int bisectRight(int target) {
            int lo = 0;
            int hi = bounds.size();
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (bounds.get(mid) <= target) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            return lo;
        }
    }


    // V1
    // IDEA: TreeMap<start, end> OF DISJOINT INTERVALS
    /**
     *  Store the intervals as real (start, end) pairs rather than as a flat
     *  boundary list. floorEntry / ceilingEntry then locate the neighbours in
     *  O(log n), and only the intervals actually overlapped are touched.
     *
     *  -> addRange / removeRange become O(log n + overlapped) instead of V0's O(n)
     *     list splice, and the state is directly readable as intervals.
     *
     *  time  = O(log n + overlapped)
     *  space = O(n)
     */
    class RangeModule_1 {

        private TreeMap<Integer, Integer> map; // start -> end (half open)

        public RangeModule_1() {
            this.map = new TreeMap<>();
        }

        public void addRange(int left, int right) {
            Map.Entry<Integer, Integer> e = map.floorEntry(left);
            if (e != null && e.getValue() >= left) {
                left = Math.min(left, e.getKey());
                right = Math.max(right, e.getValue());
            }
            // swallow every interval that starts within [left, right]
            e = map.floorEntry(right);
            if (e != null && e.getValue() >= right) {
                right = Math.max(right, e.getValue());
            }
            map.subMap(left, true, right, true).clear();
            map.put(left, right);
        }

        public boolean queryRange(int left, int right) {
            Map.Entry<Integer, Integer> e = map.floorEntry(left);
            return e != null && e.getValue() >= right;
        }

        public void removeRange(int left, int right) {
            Map.Entry<Integer, Integer> e = map.floorEntry(right);
            if (e != null && e.getValue() > right) {
                map.put(right, e.getValue()); // keep the tail past `right`
            }
            e = map.floorEntry(left);
            if (e != null && e.getValue() > left) {
                map.put(e.getKey(), left);    // keep the head before `left`
            }
            map.subMap(left, true, right, false).clear();
        }
    }

    // V2
    // IDEA: SORTED LIST OF [start, end] PAIRS + binary search
    /**
     *  The same interval model as V1 but on an ArrayList, so the merge logic is
     *  spelled out step by step instead of being delegated to subMap().clear().
     *
     *  Slower (the splice is O(n)) yet it is the easiest of the three to trace by
     *  hand, which is what you want while debugging the boundary conditions.
     *
     *  time  = O(n) per add / remove, O(log n) per query
     *  space = O(n)
     */
    class RangeModule_2 {

        private List<int[]> intervals;

        public RangeModule_2() {
            this.intervals = new ArrayList<>();
        }

        public void addRange(int left, int right) {
            List<int[]> next = new ArrayList<>();
            int i = 0;
            int n = intervals.size();

            while (i < n && intervals.get(i)[1] < left) {
                next.add(intervals.get(i++));       // entirely before
            }
            while (i < n && intervals.get(i)[0] <= right) {
                left = Math.min(left, intervals.get(i)[0]);   // overlapping -> merge
                right = Math.max(right, intervals.get(i)[1]);
                i += 1;
            }
            next.add(new int[] { left, right });
            while (i < n) {
                next.add(intervals.get(i++));       // entirely after
            }
            intervals = next;
        }

        public boolean queryRange(int left, int right) {
            int lo = 0;
            int hi = intervals.size() - 1;
            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;
                int[] itv = intervals.get(mid);
                if (itv[1] <= left) {
                    lo = mid + 1;
                } else if (itv[0] > left) {
                    hi = mid - 1;
                } else {
                    return itv[1] >= right;
                }
            }
            return false;
        }

        public void removeRange(int left, int right) {
            List<int[]> next = new ArrayList<>();
            for (int[] itv : intervals) {
                if (itv[1] <= left || itv[0] >= right) {
                    next.add(itv);                  // untouched
                    continue;
                }
                if (itv[0] < left) {
                    next.add(new int[] { itv[0], left });
                }
                if (itv[1] > right) {
                    next.add(new int[] { right, itv[1] });
                }
            }
            intervals = next;
        }
    }

    // V3
    // IDEA: DYNAMIC SEGMENT TREE WITH LAZY ASSIGNMENT
    /**
     *  Build nodes on demand over [0, 10^9) and store, per node, whether its whole
     *  range is tracked plus a lazy `set the whole range to X` tag.
     *
     *  -> every operation is O(log C) regardless of how many intervals exist,
     *     which is the only version that stays flat as the interval count grows.
     *
     *  time  = O(log C) per operation, C = 10^9
     *  space = O(operations * log C)
     */
    class RangeModule_3 {

        private class SegNode {
            boolean all;      // the whole range is tracked
            boolean none;     // the whole range is untracked
            Integer lazy;     // pending assignment: 1 = add, 0 = remove
            SegNode left;
            SegNode right;

            SegNode() {
                this.none = true;
            }
        }

        private SegNode root = new SegNode();
        private static final int HI = 1_000_000_000;

        public void addRange(int left, int right) {
            assign(root, 0, HI, left, right - 1, true);
        }

        public boolean queryRange(int left, int right) {
            return query(root, 0, HI, left, right - 1);
        }

        public void removeRange(int left, int right) {
            assign(root, 0, HI, left, right - 1, false);
        }

        private void push(SegNode node) {
            if (node.left == null) {
                node.left = new SegNode();
            }
            if (node.right == null) {
                node.right = new SegNode();
            }
            if (node.lazy != null) {
                applyTag(node.left, node.lazy == 1);
                applyTag(node.right, node.lazy == 1);
                node.lazy = null;
            }
        }

        private void applyTag(SegNode node, boolean tracked) {
            node.all = tracked;
            node.none = !tracked;
            node.lazy = tracked ? 1 : 0;
        }

        private void assign(SegNode node, int lo, int hi, int ql, int qr, boolean tracked) {
            if (qr < lo || hi < ql) {
                return;
            }
            if (ql <= lo && hi <= qr) {
                applyTag(node, tracked);
                return;
            }
            push(node);
            int mid = lo + (hi - lo) / 2;
            assign(node.left, lo, mid, ql, qr, tracked);
            assign(node.right, mid + 1, hi, ql, qr, tracked);
            node.all = node.left.all && node.right.all;
            node.none = node.left.none && node.right.none;
        }

        private boolean query(SegNode node, int lo, int hi, int ql, int qr) {
            if (qr < lo || hi < ql) {
                return true;   // nothing required here
            }
            if (node.all) {
                return true;
            }
            if (node.none) {
                return false;
            }
            if (ql <= lo && hi <= qr) {
                return node.all;
            }
            push(node);
            int mid = lo + (hi - lo) / 2;
            return query(node.left, lo, mid, ql, qr) && query(node.right, mid + 1, hi, ql, qr);
        }
    }

}
