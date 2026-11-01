package LeetCodeJava.Trie;

// https://leetcode.com/problems/count-pairs-with-xor-in-a-range/description/
/**
 * 1803. Count Pairs With XOR in a Range
 * Hard
 *
 * Given a (0-indexed) integer array nums and two integers low and high, return the
 * number of nice pairs.
 *
 * A nice pair is a pair (i, j) where 0 <= i < j < nums.length and
 * low <= (nums[i] XOR nums[j]) <= high.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,4,2,7], low = 2, high = 6
 * Output: 6
 * Explanation: All nice pairs (i, j) are as follows:
 *     - (0, 1): nums[0] XOR nums[1] = 5
 *     - (0, 2): nums[0] XOR nums[2] = 3
 *     - (0, 3): nums[0] XOR nums[3] = 6
 *     - (1, 2): nums[1] XOR nums[2] = 6
 *     - (1, 3): nums[1] XOR nums[3] = 3
 *     - (2, 3): nums[2] XOR nums[3] = 5
 *
 * Example 2:
 *
 * Input: nums = [9,8,4,2,1], low = 5, high = 14
 * Output: 8
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 10^4
 * 1 <= nums[i] <= 2 * 10^4
 * 1 <= low <= high <= 2 * 10^4
 *
 */
public class CountPairsWithXorInARange {

    // V0
    // IDEA: BINARY TRIE, count(x XOR y < limit) WALKING BITS FROM HIGH TO LOW
    /**
     *  answer = #pairs with XOR < high + 1  -  #pairs with XOR < low.
     *
     *  Insert numbers one by one; before inserting x, count earlier y with
     *  (x ^ y) < limit. Walk the trie bit by bit (high to low):
     *     - limit bit = 1: every y whose XOR bit here is 0 is already smaller ->
     *       add the whole subtree on the child equal to x's bit, then continue
     *       on the opposite child (XOR bit 1, still tied with limit).
     *     - limit bit = 0: the XOR bit must be 0 -> follow the child equal to x's bit.
     *
     *  time = O(N * B), B = 15 bits
     *  space = O(N * B)
     */
    private static final int HIGHEST_BIT = 14; // 2 * 10^4 < 2^15

    /** One trie node: children by bit, and how many numbers pass through it. */
    private static class TrieNode {
        TrieNode[] children = new TrieNode[2];
        int count = 0;
    }

    public int countPairs(int[] nums, int low, int high) {
        TrieNode root = new TrieNode();
        int pairs = 0;
        for (int x : nums) {
            pairs += countLess(root, x, high + 1) - countLess(root, x, low);
            insert(root, x);
        }
        return pairs;
    }

    /** Adds x to the trie, bumping the pass-through count of every node on its path. */
    private void insert(TrieNode root, int x) {
        TrieNode node = root;
        for (int bit = HIGHEST_BIT; bit >= 0; bit--) {
            int b = (x >> bit) & 1;
            if (node.children[b] == null) {
                node.children[b] = new TrieNode();
            }
            node = node.children[b];
            node.count++;
        }
    }

    /** Counts inserted y with (x XOR y) strictly less than limit. */
    private int countLess(TrieNode root, int x, int limit) {
        TrieNode node = root;
        int total = 0;
        for (int bit = HIGHEST_BIT; bit >= 0 && node != null; bit--) {
            int xBit = (x >> bit) & 1;
            int limitBit = (limit >> bit) & 1;
            if (limitBit == 1) {
                // XOR bit 0 here (y bit == x bit) makes the XOR smaller than limit
                if (node.children[xBit] != null) {
                    total += node.children[xBit].count;
                }
                // XOR bit 1 ties with limit; keep comparing lower bits
                node = node.children[1 - xBit];
            } else {
                // XOR bit must be 0 to stay <= limit so far
                node = node.children[xBit];
            }
        }
        // reaching the end means XOR == limit exactly, which is not < limit
        return total;
    }
}
