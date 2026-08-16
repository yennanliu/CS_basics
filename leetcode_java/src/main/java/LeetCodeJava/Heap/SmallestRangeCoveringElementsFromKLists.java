package LeetCodeJava.Heap;

// https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/description/

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

}
