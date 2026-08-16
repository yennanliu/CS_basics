package LeetCodeJava.DFS;

// https://leetcode.com/problems/k-th-smallest-in-lexicographical-order/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
/**
 * 440. K-th Smallest in Lexicographical Order
 * Hard
 *
 * Given two integers n and k, return the kth lexicographically smallest integer in
 * the range [1, n].
 *
 * Example 1:
 *
 * Input: n = 13, k = 2
 * Output: 10
 * Explanation: The lexicographical order is [1, 10, 11, 12, 13, 2, 3, 4, 5, 6, 7,
 * 8, 9], so the second smallest number is 10.
 *
 * Example 2:
 *
 * Input: n = 1, k = 1
 * Output: 1
 *
 * Constraints:
 *
 * 1 <= k <= n <= 10^9
 *
 */
public class KthSmallestInLexicographicalOrder {

    // V0
    // IDEA: 10-ARY TRIE (PREFIX TREE) WALK + SUBTREE COUNTING
    /**
     *  Lexicographical order == PRE-ORDER DFS of a 10-ary trie:
     *
     *     root
     *      |- 1
     *      |   |- 10, 11, ... 19
     *      |        |- 100 ...
     *      |- 2
     *      ...
     *
     *  n can be 10^9 so we CANNOT walk node by node. Instead, at prefix `cur`
     *  we COUNT how many numbers <= n live in that subtree:
     *
     *     count(cur) = sum over levels of  min(n + 1, next) - cur
     *                  where the level ranges are [cur, next), [cur*10, next*10) ...
     *
     *  Then:
     *    - k >= count(cur)  -> the answer is NOT in this subtree
     *                          SKIP it: k -= count, cur += 1   (next sibling)
     *    - k <  count(cur)  -> the answer IS inside
     *                          DESCEND: k -= 1, cur *= 10      (first child)
     *
     *  NOTE !!! `cur * 10` reaches 10^10, which OVERFLOWS int -> use `long`.
     *
     *  time  = O(log(n)^2)
     *  space = O(1)
     */
    public int findKthNumber(int n, int k) {
        long cur = 1;
        k -= 1; // the prefix "1" itself is the 1st number

        while (k > 0) {
            long c = count(cur, n);
            if (k >= c) {
                // skip the WHOLE subtree, move to the next sibling
                k -= c;
                cur += 1;
            } else {
                // go one level DEEPER (first child)
                k -= 1;
                cur *= 10;
            }
        }

        return (int) cur;
    }

    /** how many integers in [1, n] have `cur` as a prefix */
    private long count(long cur, int n) {
        long total = 0;
        long nxt = cur + 1;
        while (cur <= n) {
            // this trie level covers [cur, nxt), clipped by n
            total += Math.min((long) n + 1, nxt) - cur;
            cur *= 10;
            nxt *= 10;
        }
        return total;
    }


    // V1
    // IDEA: BRUTE FORCE -- materialise 1..n and sort as strings
    /**
     *  Lexicographic order is literally String order, so sorting the numbers as
     *  strings and indexing is the definition of the answer.
     *
     *  O(n log n) memory and time, hopeless at n = 10^9, but it is the oracle the
     *  counting versions are validated against.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public int findKthNumber_1(int n, int k) {
        List<Integer> all = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            all.add(i);
        }
        all.sort(Comparator.comparing(String::valueOf));
        return all.get(k - 1);
    }

    // V2
    // IDEA: EXPLICIT PRE-ORDER DFS over the 10-ary trie (walk k nodes)
    /**
     *  Walk the trie in pre-order with an explicit stack and stop after k nodes.
     *
     *  O(k) rather than O(log^2 n) -- worse when k is huge, but it never needs the
     *  subtree-COUNT argument at all, which makes the `lexicographic order == trie
     *  pre-order` insight visible on its own.
     *
     *  time  = O(k)
     *  space = O(log n) stack depth
     */
    public int findKthNumber_2(int n, int k) {
        Deque<Long> stack = new ArrayDeque<>();
        for (long d = 9; d >= 1; d--) {
            if (d <= n) {
                stack.push(d);
            }
        }

        int seen = 0;
        while (!stack.isEmpty()) {
            long cur = stack.pop();
            seen += 1;
            if (seen == k) {
                return (int) cur;
            }
            // children are 10*cur .. 10*cur+9, pushed in REVERSE so the
            // smallest is popped first
            for (long d = 9; d >= 0; d--) {
                long child = cur * 10 + d;
                if (child <= n) {
                    stack.push(child);
                }
            }
        }
        return -1;
    }

    // V3
    // IDEA: BUILD THE ANSWER DIGIT BY DIGIT
    /**
     *  Rather than moving `cur` sideways and downwards, decide the answer one
     *  DIGIT at a time: at each level try the next digit 0..9 and use the subtree
     *  count to see whether the target falls inside that branch.
     *
     *  Same counting primitive as V0 but a top-down construction -- the shape you
     *  want when the question becomes `give me the k-th, and also its rank`.
     *
     *  time  = O(log(n)^2)
     *  space = O(1)
     */
    public int findKthNumber_3(int n, int k) {
        long cur = 0;
        int remain = k;

        while (remain > 0) {
            for (long d = (cur == 0 ? 1 : 0); d <= 9; d++) {
                long cand = cur * 10 + d;
                if (cand > n) {
                    break;
                }
                long c = subtreeCount(cand, n);
                if (remain <= c) {
                    // the answer lives inside this branch
                    cur = cand;
                    remain -= 1;      // consume `cand` itself
                    break;
                }
                remain -= c;          // skip the whole branch
            }
            if (remain == 0) {
                break;
            }
        }
        return (int) cur;
    }

    /** how many integers in [1, n] have `prefix` as a prefix */
    private long subtreeCount(long prefix, int n) {
        long total = 0;
        long lo = prefix;
        long hi = prefix + 1;
        while (lo <= n) {
            total += Math.min((long) n + 1, hi) - lo;
            lo *= 10;
            hi *= 10;
        }
        return total;
    }

}
