package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-strong-pair-xor-ii/

import java.util.Arrays;

/**
 *  2935. Maximum Strong Pair XOR II
 *  Hard
 *
 *  You are given a 0-indexed integer array nums. A pair of integers x and y is
 *  called a strong pair if it satisfies the condition:
 *
 *    |x - y| <= min(x, y)
 *
 *  You need to select two integers from nums such that they form a strong pair and
 *  their bitwise XOR is the maximum among all strong pairs in the array.
 *
 *  Return the maximum XOR value out of all possible strong pairs in the array nums.
 *  Note that you can pick the same integer twice to form a pair.
 *
 *  Example 1:
 *    Input: nums = [1,2,3,4,5]
 *    Output: 7
 *    Explanation: the maximum XOR over all strong pairs is 3 XOR 4 = 7.
 *
 *  Example 2:
 *    Input: nums = [10,100]
 *    Output: 0
 *    Explanation: the only strong pairs are (10, 10) and (100, 100).
 *
 *  Example 3:
 *    Input: nums = [500,520,2500,3000]
 *    Output: 1020
 *
 *  Constraints:
 *    1 <= nums.length <= 5 * 10^4
 *    1 <= nums[i] <= 2^20 - 1
 */
public class MaximumStrongPairXORII {

    private static final int BITS = 20;

    // flat-array binary trie: children[node][bit] -> node id (0 == absent)
    private int[][] children;
    private int[] cnt;
    private int size;

    // V0
    // IDEA: SORT + SLIDING WINDOW OVER A COUNTING BINARY TRIE
    //
    //  assume x <= y. then |x - y| <= min(x, y) becomes y - x <= x, i.e. y <= 2 * x.
    //  so after sorting, the partners allowed for a given y form a contiguous window
    //  [lo .. current] where 2 * nums[lo] >= y.
    //
    //  slide that window while inserting / removing values in a binary trie keyed by
    //  the low 21 bits of each number. querying the trie greedily walks the opposite
    //  bit whenever a LIVE child exists, giving max(y ^ z) over the window in O(21).
    //
    //  NOTE: removal must be lazy-by-count, not by pruning — a node stays allocated
    //        but its `cnt` drops to 0, so the query has to test `cnt`, not just
    //        "child exists".
    //  NOTE: insert y BEFORE shrinking the window, so the pair (y, y) -> 0 is always
    //        available and the window is never empty.
    //  NOTE: duplicates are fine — cnt is a multiplicity, not a flag.
    /**
     * time = O(n * log n + n * 21)
     * space = O(n * 21)
     */
    public int maximumStrongPairXor(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;

        int cap = n * (BITS + 1) + 2;
        children = new int[cap][2];
        cnt = new int[cap];
        size = 1;                       // node 0 is the root

        int ans = 0;
        int lo = 0;
        for (int i = 0; i < n; i++) {
            int y = nums[i];
            insert(y);
            while ((long) nums[lo] * 2 < y) {
                remove(nums[lo]);
                lo++;
            }
            ans = Math.max(ans, query(y));
        }
        return ans;
    }

    private void insert(int x) {
        int node = 0;
        for (int i = BITS; i >= 0; i--) {
            int v = (x >> i) & 1;
            if (children[node][v] == 0) {
                children[node][v] = size++;
            }
            node = children[node][v];
            cnt[node]++;
        }
    }

    private void remove(int x) {
        int node = 0;
        for (int i = BITS; i >= 0; i--) {
            int v = (x >> i) & 1;
            node = children[node][v];
            cnt[node]--;
        }
    }

    private int query(int x) {
        int node = 0;
        int best = 0;
        for (int i = BITS; i >= 0; i--) {
            int v = (x >> i) & 1;
            int other = children[node][v ^ 1];
            if (other != 0 && cnt[other] > 0) {
                best |= 1 << i;
                node = other;
            } else {
                node = children[node][v];
            }
        }
        return best;
    }
}
