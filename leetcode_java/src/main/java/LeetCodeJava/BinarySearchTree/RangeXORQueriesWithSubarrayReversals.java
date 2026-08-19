package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/range-xor-queries-with-subarray-reversals/

import java.util.Random;

/**
 *  3526. Range XOR Queries with Subarray Reversals
 *  Hard
 *
 *  You are given an integer array nums of length n and a 2D integer array queries
 *  of length q, where each query is one of the following three types:
 *   - Update: queries[i] = [1, index, value] -> set nums[index] = value.
 *   - Range XOR Query: queries[i] = [2, left, right] -> compute the bitwise XOR of
 *     all elements in the subarray nums[left...right], and record this result.
 *   - Reverse Subarray: queries[i] = [3, left, right] -> reverse the subarray
 *     nums[left...right] in place.
 *
 *  Return an array of the results of all range XOR queries in the order they were
 *  encountered.
 *
 *  Example 1:
 *    Input: nums = [1,2,3,4,5], queries = [[2,1,3],[1,2,10],[3,0,4],[2,0,4]]
 *    Output: [5,8]
 *    Explanation:
 *      [2,1,3] -> XOR of [2,3,4] = 5
 *      [1,2,10] -> nums becomes [1,2,10,4,5]
 *      [3,0,4] -> nums becomes [5,4,10,2,1]
 *      [2,0,4] -> XOR of [5,4,10,2,1] = 8
 *
 *  Example 2:
 *    Input: nums = [7,8,9], queries = [[1,0,3],[2,0,2],[3,1,2]]
 *    Output: [2]
 *    Explanation:
 *      [1,0,3] -> nums becomes [3,8,9]
 *      [2,0,2] -> XOR of [3,8,9] = 2
 *      [3,1,2] -> nums becomes [3,9,8]
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    0 <= nums[i] <= 10^9
 *    1 <= queries.length <= 10^5
 *    queries[i].length == 3
 *    queries[i][0] is 1, 2 or 3
 *    if queries[i][0] == 1 : 0 <= index < nums.length, 0 <= value <= 10^9
 *    otherwise             : 0 <= left <= right < nums.length
 */
public class RangeXORQueriesWithSubarrayReversals {

    // node of an IMPLICIT treap (keyed by subtree size, not by value)
    public static class TreapNode {
        public int val;
        public int pri;
        public int size;
        public int xor;
        public boolean rev;
        public TreapNode left;
        public TreapNode right;

        public TreapNode(int val, int pri) {
            this.val = val;
            this.pri = pri;
            this.size = 1;
            this.xor = val;
        }
    }

    private final Random rnd = new Random(20130831L);
    private int[] src;

    // split results (kept in fields to avoid allocating a pair per recursion step)
    private TreapNode splitA;
    private TreapNode splitB;

    // V0
    // IDEA: IMPLICIT TREAP (BALANCED BST KEYED BY POSITION) + LAZY REVERSE FLAG
    //       a Fenwick / segment tree cannot survive query type 3: reversing a range
    //       PERMUTES positions and every later query addresses the NEW positions,
    //       so the sequence itself must be reorderable. the structure that supports
    //       "cut a positional range out and paste it back" is a balanced BST keyed
    //       by subtree SIZE rather than by value - an implicit treap.
    //       split(t, i) peels off the first i elements and merge(a, b) concatenates
    //       two sequences; with those two primitives all three query types are the
    //       same three lines - split out [left, right], act on the middle piece,
    //       merge everything back.
    //       the reversal is never performed for real (that would cost O(size)): the
    //       middle piece only gets a `rev` flag, and push() swaps a node's children
    //       and hands the flag down the moment that node is first visited. XOR is
    //       order independent, so a pending flag never invalidates a stored
    //       aggregate - that is exactly what makes lazy reversal cheap here.
    //       the tree is built once, bottom-up and balanced, with the random
    //       priorities sifted into heap order, which keeps the expected depth at
    //       O(log N) for all later operations.
    /**
     * time = O(N + Q * log N)   // expected
     * space = O(N)
     */
    public int[] getResults(int[] nums, int[][] queries) {
        this.src = nums;
        TreapNode root = build(0, nums.length);

        int outCnt = 0;
        for (int i = 0; i < queries.length; i++) {
            if (queries[i][0] == 2) {
                outCnt++;
            }
        }
        int[] res = new int[outCnt];
        int p = 0;

        for (int i = 0; i < queries.length; i++) {
            int kind = queries[i][0];
            if (kind == 1) {
                split(root, queries[i][1]);
                TreapNode a = splitA;
                split(splitB, 1);
                TreapNode mid = splitA;
                TreapNode b = splitB;
                mid.val = queries[i][2];
                mid.xor = queries[i][2];
                root = merge(merge(a, mid), b);
            } else {
                int lo = queries[i][1];
                int hi = queries[i][2];
                split(root, lo);
                TreapNode a = splitA;
                split(splitB, hi - lo + 1);
                TreapNode mid = splitA;
                TreapNode b = splitB;
                if (kind == 2) {
                    res[p++] = mid.xor;
                } else {
                    mid.rev = !mid.rev;
                }
                root = merge(merge(a, mid), b);
            }
        }
        return res;
    }

    // build a balanced treap over src[lo, hi), then sift the priority into heap order
    private TreapNode build(int lo, int hi) {
        if (lo >= hi) {
            return null;
        }
        int mid = (lo + hi) >>> 1;
        TreapNode t = new TreapNode(src[mid], rnd.nextInt());
        t.left = build(lo, mid);
        t.right = build(mid + 1, hi);

        TreapNode cur = t;
        while (true) {
            TreapNode top = cur;
            if (cur.left != null && cur.left.pri > top.pri) {
                top = cur.left;
            }
            if (cur.right != null && cur.right.pri > top.pri) {
                top = cur.right;
            }
            if (top == cur) {
                break;
            }
            int tmp = cur.pri;
            cur.pri = top.pri;
            top.pri = tmp;
            cur = top;
        }

        pull(t);
        return t;
    }

    private int size(TreapNode t) {
        return t == null ? 0 : t.size;
    }

    private int xor(TreapNode t) {
        return t == null ? 0 : t.xor;
    }

    private void pull(TreapNode t) {
        t.size = 1 + size(t.left) + size(t.right);
        t.xor = t.val ^ xor(t.left) ^ xor(t.right);
    }

    // NOTE !!! apply a pending reversal before ever looking at t's children
    private void push(TreapNode t) {
        if (!t.rev) {
            return;
        }
        t.rev = false;
        TreapNode tmp = t.left;
        t.left = t.right;
        t.right = tmp;
        if (t.left != null) {
            t.left.rev = !t.left.rev;
        }
        if (t.right != null) {
            t.right.rev = !t.right.rev;
        }
    }

    private TreapNode merge(TreapNode a, TreapNode b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a.pri > b.pri) {
            push(a);
            a.right = merge(a.right, b);
            pull(a);
            return a;
        }
        push(b);
        b.left = merge(a, b.left);
        pull(b);
        return b;
    }

    // the first `cnt` elements land in splitA, the rest in splitB
    private void split(TreapNode t, int cnt) {
        if (t == null) {
            splitA = null;
            splitB = null;
            return;
        }
        push(t);
        int ls = size(t.left);
        if (cnt <= ls) {
            split(t.left, cnt);
            t.left = splitB;      // NOTE !!! read the inner result right away
            pull(t);
            splitB = t;           // splitA still holds the inner left part
        } else {
            split(t.right, cnt - ls - 1);
            t.right = splitA;
            pull(t);
            splitA = t;           // splitB still holds the inner right part
        }
    }
}
