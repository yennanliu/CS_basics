package LeetCodeJava.Tree;

// https://leetcode.com/problems/handling-sum-queries-after-update/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 2569. Handling Sum Queries After Update
 * Hard
 *
 * You are given two 0-indexed arrays nums1 and nums2 and a 2D array queries of queries. There are three types of queries:
 *
 * 1. For a query of type 1, queries[i] = [1, l, r]. Flip the values from 0 to 1 and from 1 to 0 in nums1 from index l to index r. Both l and r are 0-indexed.
 * 2. For a query of type 2, queries[i] = [2, p, 0]. For every index 0 <= i < n, set nums2[i] = nums2[i] + nums1[i] * p.
 * 3. For a query of type 3, queries[i] = [3, 0, 0]. Find the sum of the elements in nums2.
 *
 * Return an array containing all the answers to the third type queries.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,0,1], nums2 = [0,0,0], queries = [[1,1,1],[2,1,0],[3,0,0]]
 * Output: [3]
 * Explanation: After the first query nums1 becomes [1,1,1]. After the second query, nums2 becomes [1,1,1], so the answer to the third query is 3. Thus, [3] is returned.
 *
 * Example 2:
 *
 * Input: nums1 = [1], nums2 = [5], queries = [[2,0,0],[3,0,0]]
 * Output: [5]
 * Explanation: After the first query, nums2 remains [5], so the answer to the second query is 5. Thus, [5] is returned.
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length,nums2.length <= 10^5
 * nums1.length = nums2.length
 * 1 <= queries.length <= 10^5
 * queries[i].length = 3
 * 0 <= l <= r <= nums1.length - 1
 * 0 <= p <= 10^6
 * 0 <= nums1[i] <= 1
 * 0 <= nums2[i] <= 10^9
 *
 */
public class HandlingSumQueriesAfterUpdate {

    // V0
    // IDEA: LAZY SEGMENT TREE (range flip / count of ones) + RUNNING TOTAL
    //       nums2 never has to be materialised: a type-2 query adds p * nums1[i] to
    //       every nums2[i], so the total grows by p * (number of ones in nums1), and a
    //       type-3 query just reads the total. only the range flip must be fast.
    //       flipping a segment maps its ones to (length - ones), computable from the
    //       node alone; the lazy tag is one bit, since flipping twice is the identity.
    //       the tag is pushed down before any partial descent, so a child never gets
    //       updated while it still owes its parent's flip.
    /**
     * time = O(N + Q log N)
     * space = O(N)
     */
    public long[] handleQuery(int[] nums1, int[] nums2, int[][] queries) {
        int n = nums1.length;
        int[] ones = new int[4 * n];           // count of 1s in the node's range
        boolean[] pendingFlip = new boolean[4 * n];
        build(ones, nums1, 1, 0, n - 1);

        long total = 0;
        for (int value : nums2) {
            total += value;
        }

        List<Long> results = new ArrayList<>();
        for (int[] query : queries) {
            int type = query[0];
            if (type == 1) {
                flip(ones, pendingFlip, 1, 0, n - 1, query[1], query[2]);
            } else if (type == 2) {
                // every 1 in nums1 adds p to the total; ones[1] is the root's count
                total += (long) query[1] * ones[1];
            } else {
                results.add(total);
            }
        }

        long[] answer = new long[results.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = results.get(i);
        }
        return answer;
    }

    /** Fills ones[] for the node covering [low, high]. */
    private void build(int[] ones, int[] nums1, int node, int low, int high) {
        if (low == high) {
            ones[node] = nums1[low];
            return;
        }
        int mid = low + (high - low) / 2;
        build(ones, nums1, 2 * node, low, mid);
        build(ones, nums1, 2 * node + 1, mid + 1, high);
        ones[node] = ones[2 * node] + ones[2 * node + 1];
    }

    /** Flips every bit in the node's range and records that its children still owe the flip. */
    private void applyFlip(int[] ones, boolean[] pendingFlip, int node, int low, int high) {
        int length = high - low + 1;
        ones[node] = length - ones[node];
        pendingFlip[node] = !pendingFlip[node];
    }

    /** Flips nums1[left .. right] inside the node covering [low, high]. */
    private void flip(int[] ones, boolean[] pendingFlip, int node, int low, int high, int left, int right) {
        // no overlap
        if (right < low || high < left) {
            return;
        }
        // fully covered: flip the summary and stop here (lazily)
        if (left <= low && high <= right) {
            applyFlip(ones, pendingFlip, node, low, high);
            return;
        }
        int mid = low + (high - low) / 2;
        // push the owed flip down before touching the children
        if (pendingFlip[node]) {
            applyFlip(ones, pendingFlip, 2 * node, low, mid);
            applyFlip(ones, pendingFlip, 2 * node + 1, mid + 1, high);
            pendingFlip[node] = false;
        }
        flip(ones, pendingFlip, 2 * node, low, mid, left, right);
        flip(ones, pendingFlip, 2 * node + 1, mid + 1, high, left, right);
        ones[node] = ones[2 * node] + ones[2 * node + 1];
    }
}
