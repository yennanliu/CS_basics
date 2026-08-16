package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/search-in-a-sorted-array-of-unknown-size/description/
/**
 * 702. Search in a Sorted Array of Unknown Size
 * Medium
 * Lock: Prime
 *
 * This is an interactive problem.
 *
 * You have a sorted array of unique elements and an unknown size. You do not have an access
 * to the array but you can use the ArrayReader interface to access it.
 * You can call ArrayReader.get(i) that:
 *
 *   - returns the value at the ith index (0-indexed) of the secret array (i.e., secret[i]), or
 *   - returns 2^31 - 1 if the i is out of the boundary of the array.
 *
 * You are also given an integer target.
 *
 * Return the index k of the hidden array where secret[k] == target or return -1 otherwise.
 *
 * You must write an algorithm with O(log n) runtime complexity.
 *
 *
 * Example 1:
 *
 * Input: secret = [-1,0,3,5,9,12], target = 9
 * Output: 4
 * Explanation: 9 exists in secret and its index is 4.
 *
 * Example 2:
 *
 * Input: secret = [-1,0,3,5,9,12], target = 2
 * Output: -1
 * Explanation: 2 does not exist in secret so return -1.
 *
 *
 * Constraints:
 *
 * 1 <= secret.length <= 10^4
 * -10^4 <= secret[i], target <= 10^4
 * secret is sorted in a strictly increasing order.
 *
 */
public class SearchInASortedArrayOfUnknownSize {

    // dummy API for passing java syntax check
    // offered by LC platform when submission
    interface ArrayReader {
        int get(int index);
    }

    // V0
    // IDEA: EXPONENTIAL SEARCH + BINARY SEARCH
    /**
     *   Step 1) we do NOT know the array length, so GROW a right bound by DOUBLING
     *           until reader.get(right) >= target. Out of bound reads return 2^31 - 1,
     *           which is >= target, so the doubling ALWAYS terminates.
     *
     *   Step 2) target (if present) now lies in [right / 2, right]
     *           -> plain binary search for the leftmost index with value >= target.
     *
     *   time  = O(log M), M = index of the target
     *   space = O(1)
     */
    public int search(ArrayReader reader, int target) {

        // step 1: exponentially widen the search window
        int right = 1;
        while (reader.get(right) < target) {
            right *= 2;
        }

        /** NOTE !!!
         *
         *  the previous bound `right / 2` is a SAFE left edge:
         *  we already know get(right / 2) < target (that is why we doubled),
         *  except on the very first iteration where right / 2 == 0
         */
        int left = right / 2;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (reader.get(mid) >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return reader.get(left) == target ? left : -1;
    }


    // V1
    // IDEA: DISCOVER THE LENGTH FIRST, THEN A PLAIN BINARY SEARCH
    /**
     *  Two clearly separated phases: double an index until reader.get() returns the
     *  out-of-bounds sentinel (so we know the array ends before it), then run a
     *  textbook binary search over [0, len).
     *
     *  Slightly more reads than V0, but the second phase is an ORDINARY binary
     *  search over a known range -- much easier to get right.
     *
     *  time  = O(log n)
     *  space = O(1)
     */
    public int search_1(ArrayReader reader, int target) {
        final int OUT = Integer.MAX_VALUE;

        // phase 1 : find any index past the end
        int hi = 1;
        while (reader.get(hi) != OUT) {
            hi *= 2;
        }
        // phase 2 : narrow to the exact length
        int lo = hi / 2;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (reader.get(mid) == OUT) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        int len = lo; // first out-of-bounds index == length

        // phase 3 : plain binary search over [0, len)
        int left = 0;
        int right = len - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int v = reader.get(mid);
            if (v == target) {
                return mid;
            }
            if (v < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    // V2
    // IDEA: EXPONENTIAL SEARCH WITH AN EARLY EXIT
    /**
     *  While doubling the bound we already read the value there -- so if it happens
     *  to BE the target we can return immediately instead of finishing the search.
     *
     *  Also keeps `lo` at the previous bound so the binary search runs on the
     *  tightest possible window.
     *
     *  time  = O(log M), M = index of the target
     *  space = O(1)
     */
    public int search_2(ArrayReader reader, int target) {
        int lo = 0;
        int hi = 1;

        while (true) {
            int v = reader.get(hi);
            if (v == target) {
                return hi;       // early exit -- we were reading it anyway
            }
            if (v > target) {
                break;
            }
            lo = hi + 1;
            hi *= 2;
        }

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int v = reader.get(mid);
            if (v == target) {
                return mid;
            }
            if (v < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return -1;
    }

    // V3
    // IDEA: BINARY SEARCH OVER THE CONSTRAINT BOUND (no growth phase)
    /**
     *  The statement caps the array at 10^4 entries, so we can simply binary search
     *  [0, 10^4) directly and let the out-of-bounds sentinel (2^31 - 1) act as a
     *  value larger than every possible target.
     *
     *  No doubling phase at all; the trade-off is that the read count is tied to
     *  the CONSTRAINT rather than to where the target actually sits.
     *
     *  time  = O(log 10^4) = O(1) reads
     *  space = O(1)
     */
    public int search_3(ArrayReader reader, int target) {
        int lo = 0;
        int hi = 10000; // the problem's stated maximum length

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            /** NOTE !!!
             *
             *  an out-of-range read returns 2^31 - 1, which is >= every legal
             *  target, so it behaves exactly like `too big` -- no special case
             */
            if (reader.get(mid) >= target) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        return reader.get(lo) == target ? lo : -1;
    }

}
