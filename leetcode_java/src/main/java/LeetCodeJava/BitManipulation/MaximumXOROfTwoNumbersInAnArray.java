package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/

import java.util.HashSet;
import java.util.Set;

/**
 *  421. Maximum XOR of Two Numbers in an Array
 *  Medium
 *
 *  Given an integer array nums, return the maximum result of
 *  nums[i] XOR nums[j], where 0 <= i <= j < n.
 *
 *  Example 1:
 *  Input: nums = [3,10,5,25,2,8]
 *  Output: 28
 *  Explanation: The maximum result is 5 XOR 25 = 28.
 *
 *  Example 2:
 *  Input: nums = [14,70,53,83,49,91,36,80,92,51,66,70]
 *  Output: 127
 *
 *  Constraints:
 *  1 <= nums.length <= 2 * 10^5
 *  0 <= nums[i] <= 2^31 - 1
 */
public class MaximumXOROfTwoNumbersInAnArray {

    // V0
    // IDEA: greedy bit by bit from the MSB - guess the answer has the next bit
    //       set, then check via a prefix hash set whether some pair can realize it
    /**
     * time = O(32 * n)
     * space = O(n)
     */
    public int findMaximumXOR(int[] nums) {
        int max = 0;
        int mask = 0;
        for (int bit = 31; bit >= 0; bit--) {
            mask |= (1 << bit);
            Set<Integer> prefixes = new HashSet<>();
            for (int num : nums) {
                prefixes.add(num & mask);
            }
            int candidate = max | (1 << bit);
            for (int p : prefixes) {
                // if p ^ q == candidate for some prefix q, candidate is reachable
                if (prefixes.contains(p ^ candidate)) {
                    max = candidate;
                    break;
                }
            }
        }
        return max;
    }

    // V1
    // IDEA: binary trie - insert every number, then for each number walk the trie
    //       preferring the opposite bit at each level
    /**
     * time = O(32 * n)
     * space = O(32 * n)
     */
    public int findMaximumXOR_1(int[] nums) {
        TrieNode root = new TrieNode();
        for (int num : nums) {
            TrieNode cur = root;
            for (int bit = 31; bit >= 0; bit--) {
                int b = (num >> bit) & 1;
                if (cur.children[b] == null) {
                    cur.children[b] = new TrieNode();
                }
                cur = cur.children[b];
            }
        }

        int res = 0;
        for (int num : nums) {
            TrieNode cur = root;
            int curXor = 0;
            for (int bit = 31; bit >= 0; bit--) {
                int b = (num >> bit) & 1;
                int want = 1 - b;
                if (cur.children[want] != null) {
                    curXor |= (1 << bit);
                    cur = cur.children[want];
                } else {
                    cur = cur.children[b];
                }
            }
            res = Math.max(res, curXor);
        }
        return res;
    }

    public static class TrieNode {
        public TrieNode[] children = new TrieNode[2];
    }
}
