package LeetCodeJava.Design;

// https://leetcode.com/problems/range-module/description/

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

}
