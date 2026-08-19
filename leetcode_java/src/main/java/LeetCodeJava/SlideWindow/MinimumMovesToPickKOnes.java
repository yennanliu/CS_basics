package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/minimum-moves-to-pick-k-ones/

/**
 *  3086. Minimum Moves to Pick K Ones
 *  Hard
 *
 *  You are given a binary array nums of length n, a positive integer k and a
 *  non-negative integer maxChanges.
 *
 *  Alice picks any index aliceIndex and stands there. If nums[aliceIndex] == 1
 *  she picks up that one for free. After this she may repeatedly do exactly one
 *  of:
 *   - Select any index j != aliceIndex with nums[j] == 0 and set nums[j] = 1.
 *     This action can be performed at most maxChanges times.
 *   - Select adjacent indices x, y with nums[x] == 1, nums[y] == 0 and swap
 *     them. If y == aliceIndex, Alice picks up that one.
 *
 *  Return the minimum number of moves required by Alice to pick exactly k ones.
 *
 *  Example 1:
 *    Input: nums = [1,1,0,0,0,1,1,0,0,1], k = 3, maxChanges = 1
 *    Output: 3
 *
 *  Example 2:
 *    Input: nums = [0,0,0,0], k = 2, maxChanges = 3
 *    Output: 4
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    0 <= nums[i] <= 1
 *    1 <= k <= 10^5
 *    0 <= maxChanges <= 10^5
 *    maxChanges + sum(nums) >= k
 */
public class MinimumMovesToPickKOnes {

    // V0
    // IDEA: PRICE EVERY ONE, THEN NOTICE ONLY ~4 REAL/CREATED SPLITS MATTER
    //       costs, measured from Alice's standing index:
    //           a real one AT her index     -> 0 moves (free pickup)
    //           a real one at distance d    -> d moves (swap it along)
    //           a created one (maxChanges)  -> 2 moves (set adjacent, swap in)
    //       so collecting r real ones and creating x = k - r costs
    //           2 * x + (sum of distances from her index to those r ones)
    //       and for a fixed r the best real ones are a CONTIGUOUS block of
    //       ones with Alice standing at its median — the classic
    //       minimum-sum-of-distances arrangement, computed for every window
    //       with prefix sums.
    //       the search over r collapses: a created one costs 2, so it matches
    //       or beats any real one at distance >= 2, and only three real ones
    //       can be cheaper than that (the one under her feet and its two
    //       neighbours). so beyond max(0, k - maxChanges) real ones there is
    //       no point taking more than 3 extra — four values of r to try.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long minimumMoves(int[] nums, int k, int maxChanges) {
        int n = nums.length;
        int[] pos = new int[n];
        int m = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                pos[m++] = i;
            }
        }

        long[] pre = new long[m + 1];
        for (int i = 0; i < m; i++) {
            pre[i + 1] = pre[i] + pos[i];
        }

        int base = Math.max(0, k - maxChanges); // real ones we are forced to take
        long res = Long.MAX_VALUE;
        for (int r = base; r <= Math.min(m, base + 3); r++) {
            if (r > k) {
                break;
            }
            long cost = 2L * (k - r) + windowCost(pos, pre, m, r);
            res = Math.min(res, cost);
        }
        return res;
    }

    /** min total distance to gather r ones, standing at their median. */
    private long windowCost(int[] pos, long[] pre, int m, int r) {
        if (r == 0) {
            return 0L;
        }
        long best = Long.MAX_VALUE;
        for (int lo = 0; lo + r - 1 < m; lo++) {
            int hi = lo + r - 1;
            int mid = lo + r / 2;
            long med = pos[mid];
            // the two halves, each measured against the median
            long right = (pre[hi + 1] - pre[mid]) - med * (hi - mid + 1);
            long left = med * (mid - lo) - (pre[mid] - pre[lo]);
            best = Math.min(best, left + right);
        }
        return best;
    }
}
