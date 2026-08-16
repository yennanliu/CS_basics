package LeetCodeJava.Heap;

// https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/description/

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 632. Smallest Range Covering Elements from K Lists
 * Hard
 *
 * You have k lists of sorted integers in non-decreasing order.
 * Find the smallest range that includes at least one number from each of the k lists.
 *
 * We define the range [a, b] is smaller than range [c, d] if b - a < d - c
 * or a < c if b - a == d - c.
 *
 * Example 1:
 *
 * Input: nums = [[4,10,15,24,26],[0,9,12,20],[5,18,22,30]]
 * Output: [20,24]
 * Explanation:
 * List 1: [4, 10, 15, 24, 26], 24 is in range [20,24].
 * List 2: [0, 9, 12, 20], 20 is in range [20,24].
 * List 3: [5, 18, 22, 30], 22 is in range [20,24].
 *
 * Example 2:
 *
 * Input: nums = [[1,2,3],[1,2,3],[1,2,3]]
 * Output: [1,1]
 *
 * Constraints:
 *
 * nums.length == k
 * 1 <= k <= 3500
 * 1 <= nums[i].length <= 50
 * -10^5 <= nums[i][j] <= 10^5
 * nums[i] is sorted in non-decreasing order.
 *
 */
public class SmallestRangeCoveringElementsFromKLists {

    // V0
    // IDEA: MIN HEAP (k-way merge, one pointer per list)
    /**
     *   Keep EXACTLY ONE candidate per list in a min heap, plus the running MAX of
     *   those k candidates. The heap top is the min, so [min, max] is always a VALID
     *   covering range. To shrink it, the only useful move is to advance the list
     *   holding the MINIMUM (advancing anything else can only raise the max).
     *   Stop when a list is EXHAUSTED -- past that point no range can cover it.
     *
     *   Tie-break note: candidates are visited in increasing `min` order, so a
     *   STRICT `<` comparison naturally keeps the smallest `a` among equal widths.
     *
     *   time  = O(n * log(k)), n = total number of elements
     *   space = O(k)
     */
    public int[] smallestRange(List<List<Integer>> nums) {
        // {value, which list, index inside that list}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        int curMax = Integer.MIN_VALUE;
        for (int i = 0; i < nums.size(); i++) {
            int v = nums.get(i).get(0);
            pq.add(new int[] { v, i, 0 });
            curMax = Math.max(curMax, v);
        }

        int[] best = new int[] { pq.peek()[0], curMax };

        while (true) {
            int[] cur = pq.poll();
            int val = cur[0];
            int i = cur[1];
            int j = cur[2];

            if (curMax - val < best[1] - best[0]) {
                best = new int[] { val, curMax };
            }

            /** NOTE !!!
             *
             *  this list has no more elements
             *  -> NO further range can cover it -> we are done
             */
            if (j + 1 == nums.get(i).size()) {
                break;
            }

            int nxt = nums.get(i).get(j + 1);
            curMax = Math.max(curMax, nxt);
            pq.add(new int[] { nxt, i, j + 1 });
        }

        return best;
    }

    // V0-1
    // IDEA: SORT ALL (value, list_id) PAIRS + SLIDING WINDOW
    /**
     *  distinct trick: turns `cover all k lists` into the classic
     *  `smallest window containing all k kinds` problem
     *
     *  time  = O(n * log(n))
     *  space = O(n)
     */
    public int[] smallestRange_0_1(List<List<Integer>> nums) {
        List<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < nums.size(); i++) {
            for (int x : nums.get(i)) {
                pairs.add(new int[] { x, i });
            }
        }
        pairs.sort(Comparator.comparingInt(a -> a[0]));

        int k = nums.size();
        int[] cnt = new int[k];
        int covered = 0;
        int left = 0;
        int[] best = new int[] { -1000000, 1000000 };

        for (int[] pair : pairs) {
            int val = pair[0];
            int i = pair[1];

            if (cnt[i] == 0) {
                covered += 1;
            }
            cnt[i] += 1;

            // shrink while the window STILL covers every list
            while (covered == k) {
                int lo = pairs.get(left)[0];
                if (val - lo < best[1] - best[0]) {
                    best = new int[] { lo, val };
                }
                cnt[pairs.get(left)[1]] -= 1;
                if (cnt[pairs.get(left)[1]] == 0) {
                    covered -= 1;
                }
                left += 1;
            }
        }

        return best;
    }

    /** helper: same as V0 but taking a raw int[][] (handy for local testing) */
    public int[] smallestRange(int[][] nums) {
        List<List<Integer>> ls = new ArrayList<>();
        for (int[] row : nums) {
            List<Integer> r = new ArrayList<>();
            for (int x : row) {
                r.add(x);
            }
            ls.add(r);
        }
        return smallestRange(ls);
    }


    // V1
    // IDEA: BRUTE FORCE POINTER ADVANCE (rescan all k heads each step)
    /**
     *  Keep one pointer per list and, each round, LINEARLY SCAN the k heads to find
     *  the min and the max. Record the range, then advance the minimum pointer.
     *
     *  O(n * k) instead of O(n log k) -- the heap in V0 exists purely to make that
     *  scan logarithmic. Worth keeping because it needs no auxiliary structure and
     *  is the clearest statement of the invariant.
     *
     *  time  = O(n * k)
     *  space = O(k)
     */
    public int[] smallestRange_1(List<List<Integer>> nums) {
        int k = nums.size();
        int[] ptr = new int[k];

        int[] best = null;
        while (true) {
            int minVal = Integer.MAX_VALUE;
            int maxVal = Integer.MIN_VALUE;
            int minIdx = -1;

            for (int i = 0; i < k; i++) {
                int v = nums.get(i).get(ptr[i]);
                if (v < minVal) {
                    minVal = v;
                    minIdx = i;
                }
                maxVal = Math.max(maxVal, v);
            }

            if (best == null || maxVal - minVal < best[1] - best[0]) {
                best = new int[] { minVal, maxVal };
            }

            // the list holding the minimum is the only useful one to advance
            ptr[minIdx] += 1;
            if (ptr[minIdx] == nums.get(minIdx).size()) {
                break; // that list is exhausted -> no later range can cover it
            }
        }

        return best;
    }

    // V2
    // IDEA: TreeMap AS AN ORDERED MULTISET OF THE k CURRENT HEADS
    /**
     *  A TreeMap<value, count> gives BOTH ends in O(log k): firstKey() is the
     *  window minimum and lastKey() the maximum.
     *
     *  V0's heap only exposes the minimum, which is why it has to track `curMax`
     *  by hand -- here both come from the same structure, so there is no separate
     *  running maximum to keep in sync.
     *
     *  time  = O(n log k)
     *  space = O(k)
     */
    public int[] smallestRange_2(List<List<Integer>> nums) {
        int k = nums.size();
        int[] ptr = new int[k];

        // value -> which lists currently sit on it
        TreeMap<Integer, List<Integer>> live = new TreeMap<>();
        for (int i = 0; i < k; i++) {
            live.computeIfAbsent(nums.get(i).get(0), x -> new ArrayList<>()).add(i);
        }

        int[] best = new int[] { live.firstKey(), live.lastKey() };

        while (true) {
            int lo = live.firstKey();
            int hi = live.lastKey();
            if (hi - lo < best[1] - best[0]) {
                best = new int[] { lo, hi };
            }

            List<Integer> owners = live.get(lo);
            int listIdx = owners.remove(owners.size() - 1);
            if (owners.isEmpty()) {
                live.remove(lo);
            }

            ptr[listIdx] += 1;
            if (ptr[listIdx] == nums.get(listIdx).size()) {
                break;
            }
            live.computeIfAbsent(nums.get(listIdx).get(ptr[listIdx]), x -> new ArrayList<>())
                .add(listIdx);
        }

        return best;
    }

    // V3
    // IDEA: BINARY SEARCH ON THE RANGE WIDTH
    /**
     *  `can all k lists be covered by a window of width w?` is MONOTONE in w, so
     *  binary search w over [0, maxValue - minValue] and check feasibility by
     *  sweeping the merged (value, list) array once.
     *
     *  -> the runtime depends on log(VALUE RANGE) rather than on how the elements
     *     interleave, which the pointer/heap versions are sensitive to.
     *
     *  time  = O(n log n + n log W)
     *  space = O(n)
     */
    public int[] smallestRange_3(List<List<Integer>> nums) {
        int k = nums.size();

        List<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            for (int v : nums.get(i)) {
                pairs.add(new int[] { v, i });
            }
        }
        pairs.sort(Comparator.comparingInt(p -> p[0]));

        int lo = 0;
        int hi = pairs.get(pairs.size() - 1)[0] - pairs.get(0)[0];
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (feasible(pairs, k, mid) != null) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        return feasible(pairs, k, lo);
    }

    /** the FIRST window of width <= w covering all k lists, or null */
    private int[] feasible(List<int[]> pairs, int k, int w) {
        int[] cnt = new int[k];
        int covered = 0;
        int left = 0;

        for (int right = 0; right < pairs.size(); right++) {
            if (cnt[pairs.get(right)[1]]++ == 0) {
                covered += 1;
            }
            // shrink to keep the width within w
            while (pairs.get(right)[0] - pairs.get(left)[0] > w) {
                if (--cnt[pairs.get(left)[1]] == 0) {
                    covered -= 1;
                }
                left += 1;
            }
            if (covered == k) {
                return new int[] { pairs.get(left)[0], pairs.get(right)[0] };
            }
        }
        return null;
    }

}
