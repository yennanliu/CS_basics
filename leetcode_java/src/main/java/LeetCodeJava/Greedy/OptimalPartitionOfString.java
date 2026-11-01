package LeetCodeJava.Greedy;

// https://leetcode.com/problems/optimal-partition-of-string/description/

import java.util.*;

/**
 * 2405. Optimal Partition of String
 * Medium
 *
 * Given a string s, partition the string into one or more substrings such that the characters in each substring are unique. That is, no letter appears in a single substring more than once.
 *
 * Return the minimum number of substrings in such a partition.
 *
 * Note that each character should belong to exactly one substring in a partition.
 *
 *
 * Example 1:
 *
 * Input: s = "abacaba"
 * Output: 4
 * Explanation:
 * Two possible partitions are ("a","ba","cab","a") and ("ab","a","ca","ba").
 * It can be shown that 4 is the minimum number of substrings needed.
 *
 * Example 2:
 *
 * Input: s = "ssssss"
 * Output: 6
 * Explanation:
 * The only valid partition is ("s","s","s","s","s","s").
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of only English lowercase letters.
 *
 */
public class OptimalPartitionOfString {

    // V0
    // IDEA: GREEDY (EXTEND THE CURRENT PIECE, CUT ON THE FIRST REPEATED LETTER)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int partitionString(String s) {
        int pieces = 1;
        // letters used by the piece being built
        boolean[] seen = new boolean[26];

        for (char c : s.toCharArray()) {
            int idx = c - 'a';
            if (seen[idx]) {
                // this piece cannot grow any further: start a new one with c
                pieces++;
                seen = new boolean[26];
            }
            seen[idx] = true;
        }
        return pieces;
    }
}
