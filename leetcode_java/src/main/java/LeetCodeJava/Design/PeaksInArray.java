package LeetCodeJava.Design;

// https://leetcode.com/problems/peaks-in-array/

import java.util.ArrayList;
import java.util.List;

/**
 *  3187. Peaks in Array
 *  Hard
 *
 *  A peak in an array arr is an element that is greater than its previous and next
 *  element in arr.
 *
 *  You are given an integer array nums and a 2D integer array queries. You have to
 *  process queries of two types:
 *    queries[i] = [1, l_i, r_i], determine the count of peak elements in the
 *      subarray nums[l_i..r_i].
 *    queries[i] = [2, index_i, val_i], change nums[index_i] to val_i.
 *
 *  Return an array answer containing the results of the queries of the first type
 *  in order.
 *
 *  Note: the first and the last element of an array or a subarray cannot be a peak.
 *
 *  Example 1:
 *    Input: nums = [3,1,4,2,5], queries = [[2,3,4],[1,0,4]]
 *    Output: [0]
 *    Explanation: nums becomes [3,1,4,4,5], which has no peak.
 *
 *  Example 2:
 *    Input: nums = [4,1,4,2,1,5], queries = [[2,2,4],[1,0,2],[1,0,4]]
 *    Output: [0,1]
 *    Explanation: nums[2] is already 4; [4,1,4] has no peak; in [4,1,4,2,1] the
 *                 second 4 is a peak.
 *
 *  Constraints:
 *    3 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^5
 *    1 <= queries.length <= 10^5
 *    queries[i][0] == 1 or queries[i][0] == 2
 *    if queries[i][0] == 1: 0 <= queries[i][1] <= queries[i][2] <= nums.length - 1
 *    if queries[i][0] == 2: 0 <= queries[i][1] <= nums.length - 1,
 *                           1 <= queries[i][2] <= 10^5
 */
public class PeaksInArray {

    // V0
    // IDEA: FENWICK TREE OVER "IS THIS INDEX A PEAK", REPAIRED LOCALLY ON UPDATES
    //
    //       being a peak is a purely LOCAL property -- index i depends only on
    //       nums[i-1], nums[i], nums[i+1]. so keep a 0/1 flag per index in a
    //       Fenwick tree and a range query becomes a prefix-sum difference.
    //
    //       writing nums[p] can only disturb the flags of p-1, p and p+1, so an
    //       update recomputes exactly those three.
    //
    //       a type-1 query excludes the subarray's own endpoints (they can never be
    //       peaks), so it sums the flags over [l+1, r-1] -- empty when r - l < 2.
    /**
     * time = O((n + q) log n)
     * space = O(n)
     */
    private int n;
    private int[] nums;
    private int[] bit;   // fenwick over the peak flags
    private int[] flag;  // flag[i] = 1 if i is currently a peak

    public List<Integer> countOfPeaks(int[] nums, int[][] queries) {
        this.nums = nums;
        this.n = nums.length;
        this.bit = new int[n + 1];
        this.flag = new int[n];
        for (int i = 1; i + 1 < n; i++) {
            if (isPeak(i)) {
                flag[i] = 1;
                add(i, 1);
            }
        }

        List<Integer> res = new ArrayList<>();
        for (int[] q : queries) {
            if (q[0] == 1) {
                int l = q[1];
                int r = q[2];
                if (r - l < 2) {
                    res.add(0);
                } else {
                    res.add(sum(r - 1) - sum(l)); // flags over [l+1, r-1]
                }
            } else {
                int p = q[1];
                this.nums[p] = q[2];
                for (int i = p - 1; i <= p + 1; i++) {
                    refresh(i);
                }
            }
        }
        return res;
    }

    private boolean isPeak(int i) {
        return i >= 1 && i + 1 < n && nums[i] > nums[i - 1] && nums[i] > nums[i + 1];
    }

    /** recompute index i's flag and push the delta into the tree */
    private void refresh(int i) {
        if (i < 1 || i + 1 >= n) {
            return;
        }
        int want = isPeak(i) ? 1 : 0;
        if (want != flag[i]) {
            add(i, want - flag[i]);
            flag[i] = want;
        }
    }

    /** fenwick point update, 0-indexed position */
    private void add(int i, int d) {
        for (int x = i + 1; x <= n; x += x & (-x)) {
            bit[x] += d;
        }
    }

    /** fenwick prefix sum of flags over [0, i], 0-indexed (i < 0 -> 0) */
    private int sum(int i) {
        int s = 0;
        for (int x = i + 1; x > 0; x -= x & (-x)) {
            s += bit[x];
        }
        return s;
    }
}
