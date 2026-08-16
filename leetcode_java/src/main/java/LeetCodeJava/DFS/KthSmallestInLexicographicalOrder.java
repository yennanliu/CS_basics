package LeetCodeJava.DFS;

// https://leetcode.com/problems/k-th-smallest-in-lexicographical-order/description/
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

}
