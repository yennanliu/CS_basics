package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-x-value-of-array-ii/description/

/**
 * 3525. Find X Value of Array II
 * Hard
 *
 * You are given an array of positive integers nums and a positive integer k. You are also
 * given a 2D array queries, where queries[i] = [index_i, value_i, start_i, x_i].
 *
 * You are allowed to perform an operation once on nums, where you can remove any suffix
 * from nums such that nums remains non-empty.
 *
 * The x-value of nums for a given x is defined as the number of ways to perform this
 * operation so that the product of the remaining elements leaves a remainder of x modulo k.
 *
 * For each query in queries you need to determine the x-value of nums for x_i after
 * performing the following actions:
 *
 * Update nums[index_i] to value_i. Only this step persists for the rest of the queries.
 * Remove the prefix nums[0..(start_i - 1)] (where nums[0..(-1)] will be used to represent
 * the empty prefix).
 *
 * Return an array result of size queries.length where result[i] is the answer for the
 * i^th query.
 *
 * Note that the prefix and suffix to be chosen for the operation can be empty.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5], k = 3, queries = [[2,2,0,2],[3,3,3,0],[0,1,0,1]]
 * Output: [2,2,2]
 *
 * Example 2:
 *
 * Input: nums = [1,2,4,8,16,32], k = 4, queries = [[0,2,0,2],[0,2,0,1]]
 * Output: [1,0]
 *
 * Example 3:
 *
 * Input: nums = [1,1,2,1,1], k = 2, queries = [[2,1,0,1]]
 * Output: [5]
 *
 *
 * Constraints:
 *
 * 1 <= nums[i] <= 10^9
 * 1 <= nums.length <= 10^5
 * 1 <= k <= 5
 * 1 <= queries.length <= 2 * 10^4
 * queries[i] == [index_i, value_i, start_i, x_i]
 * 0 <= index_i <= nums.length - 1
 * 1 <= value_i <= 10^9
 * 0 <= start_i <= nums.length - 1
 * 0 <= x_i <= k - 1
 *
 */
public class FindXValueOfArrayII {

    /** Summary of one segment: product of the whole slice mod k, and prefix counts by residue. */
    private static class Node {
        int product;
        int[] prefixCount;

        Node(int k) {
            prefixCount = new int[k];
        }
    }

    private int k;
    private Node[] tree;

    // V0
    // IDEA: SEGMENT TREE WHOSE NODE SUMMARISES ALL PREFIXES OF ITS SEGMENT
    /**
     *  After removing nums[0 .. start-1], "remove a suffix, stay non-empty" picks one
     *  prefix of nums[start ..]; so count prefixes of nums[start .. n-1] with product % k == x.
     *
     *  Node(segment): product % k of the whole segment, prefixCount[r] = number of
     *  prefixes of the segment with product % k == r.
     *
     *  Merge (A then B), O(K):
     *     - prefixes of A stay as they are
     *     - all of A followed by a prefix of B with residue r -> residue A.product * r % k
     *     - product = A.product * B.product % k
     *  NOTE !!! the merge is NOT commutative: queries must fold left to right.
     *
     *  time  = O((N + Q log N) * K)
     *  space = O(N * K)
     */
    public int[] resultArray(int[] nums, int k, int[][] queries) {
        this.k = k;
        int n = nums.length;
        tree = new Node[4 * n];
        build(1, 0, n - 1, nums);

        int[] result = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int index = queries[q][0];
            int value = queries[q][1];
            int start = queries[q][2];
            int x = queries[q][3];

            // the update persists for later queries
            update(1, 0, n - 1, index, value);
            Node suffix = query(1, 0, n - 1, start, n - 1);
            result[q] = suffix.prefixCount[x];
        }
        return result;
    }

    /** Builds the subtree for nums[left .. right] rooted at node. */
    private void build(int node, int left, int right, int[] nums) {
        if (left == right) {
            tree[node] = leaf(nums[left]);
            return;
        }
        int mid = left + (right - left) / 2;
        build(2 * node, left, mid, nums);
        build(2 * node + 1, mid + 1, right, nums);
        tree[node] = merge(tree[2 * node], tree[2 * node + 1]);
    }

    /** Sets nums[position] = value and refreshes the path above it. */
    private void update(int node, int left, int right, int position, int value) {
        if (left == right) {
            tree[node] = leaf(value);
            return;
        }
        int mid = left + (right - left) / 2;
        if (position <= mid) {
            update(2 * node, left, mid, position, value);
        } else {
            update(2 * node + 1, mid + 1, right, position, value);
        }
        tree[node] = merge(tree[2 * node], tree[2 * node + 1]);
    }

    /** Summary of nums[queryLeft .. queryRight]; null if the node is outside it. */
    private Node query(int node, int left, int right, int queryLeft, int queryRight) {
        if (queryRight < left || right < queryLeft) {
            return null;
        }
        if (queryLeft <= left && right <= queryRight) {
            return tree[node];
        }
        int mid = left + (right - left) / 2;
        Node leftPart = query(2 * node, left, mid, queryLeft, queryRight);
        Node rightPart = query(2 * node + 1, mid + 1, right, queryLeft, queryRight);
        // left part first: the merge is order-sensitive
        return merge(leftPart, rightPart);
    }

    /** A one-element segment: its only prefix is itself. */
    private Node leaf(int value) {
        Node node = new Node(k);
        node.product = value % k;
        node.prefixCount[node.product] = 1;
        return node;
    }

    /** Summary of segment a immediately followed by segment b (either may be null). */
    private Node merge(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        Node merged = new Node(k);
        // prefixes that end inside a
        for (int r = 0; r < k; r++) {
            merged.prefixCount[r] = a.prefixCount[r];
        }
        // all of a, then a prefix of b with residue r
        for (int r = 0; r < k; r++) {
            int combined = (a.product * r) % k;
            merged.prefixCount[combined] += b.prefixCount[r];
        }
        merged.product = (a.product * b.product) % k;
        return merged;
    }
}
