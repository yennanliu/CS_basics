package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/number-of-pairs-satisfying-inequality/

/**
 *  2426. Number of Pairs Satisfying Inequality
 *  Hard
 *
 *  You are given two 0-indexed integer arrays nums1 and nums2, each of size n, and
 *  an integer diff. Find the number of pairs (i, j) such that:
 *   - 0 <= i < j <= n - 1 and
 *   - nums1[i] - nums1[j] <= nums2[i] - nums2[j] + diff.
 *
 *  Return the number of pairs that satisfy the conditions.
 *
 *  Example 1:
 *    Input: nums1 = [3,2,5], nums2 = [2,2,1], diff = 1
 *    Output: 3
 *    Explanation: the 3 valid pairs are (0,1), (0,2) and (1,2).
 *
 *  Example 2:
 *    Input: nums1 = [3,-1], nums2 = [-2,2], diff = -1
 *    Output: 0
 *    Explanation: no pair satisfies the condition.
 *
 *  Constraints:
 *    n == nums1.length == nums2.length
 *    2 <= n <= 10^5
 *    -10^4 <= nums1[i], nums2[i] <= 10^4
 *    -10^4 <= diff <= 10^4
 */
public class NumberOfPairsSatisfyingInequality {

    private static final int OFFSET = 20000;
    private static final int SIZE = 40002;    // values -20000 .. 20000, 1-indexed

    private int[] tree;

    // V0
    // IDEA: REARRANGE INTO ONE ARRAY, THEN COUNT WITH A FENWICK (BIT) TREE
    //       move each index's terms to its own side:
    //         nums1[i] - nums1[j] <= nums2[i] - nums2[j] + diff
    //         (nums1[i] - nums2[i]) <= (nums1[j] - nums2[j]) + diff
    //       so with a[k] = nums1[k] - nums2[k] the condition is simply
    //         a[i] <= a[j] + diff        for i < j
    //       sweep j left to right and for each j ask "how many earlier a[i] are
    //       <= a[j] + diff?" - a prefix count over the VALUE axis, which a Fenwick
    //       tree answers in O(log V).
    //       values live in [-2*10^4, 2*10^4], so a fixed offset maps them onto
    //       1-indexed tree positions.
    /**
     * time = O(N * log V)
     * space = O(V)
     */
    public long numberOfPairs(int[] nums1, int[] nums2, int diff) {
        this.tree = new int[SIZE + 1];
        long res = 0;

        for (int k = 0; k < nums1.length; k++) {
            int a = nums1[k] - nums2[k];
            int bound = a + diff + OFFSET + 1;   // 1-indexed position of a + diff
            if (bound >= 1) {
                res += query(bound);
            }
            add(a + OFFSET + 1);
        }
        return res;
    }

    private void add(int pos) {
        while (pos <= SIZE) {
            tree[pos] += 1;
            pos += pos & -pos;
        }
    }

    // how many stored values sit at index <= pos
    private int query(int pos) {
        int total = 0;
        if (pos > SIZE) {
            pos = SIZE;
        }
        while (pos > 0) {
            total += tree[pos];
            pos -= pos & -pos;
        }
        return total;
    }
}
