package LeetCodeJava.Greedy;

// https://leetcode.com/problems/partition-labels/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 763. Partition Labels
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given a string s. We want to partition the string into as many parts as possible so that each letter appears in at most one part. For example, the string "ababcc" can be partitioned into ["abab", "cc"], but partitions such as ["aba", "bcc"] or ["ab", "ab", "cc"] are invalid.
 *
 * Note that the partition is done so that after concatenating all the parts in order, the resultant string should be s.
 *
 * Return a list of integers representing the size of these parts.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "ababcbacadefegdehijhklij"
 * Output: [9,7,8]
 * Explanation:
 * The partition is "ababcbaca", "defegde", "hijhklij".
 * This is a partition so that each letter appears in at most one part.
 * A partition like "ababcbacadefegde", "hijhklij" is incorrect, because it splits s into less parts.
 * Example 2:
 *
 * Input: s = "eccbbbbdec"
 * Output: [10]
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 500
 * s consists of lowercase English letters.
 *
 *
 */
public class PartitionLabels {

    // V0
    // IDEA: HASHMAP + 2 POINTERS (fixed by gpt)
    public List<Integer> partitionLabels(String s) {
        List<Integer> res = new ArrayList<>();

        // Edge case: if string is null or empty, return an empty list
        if (s == null || s.length() == 0) {
            return res;
        }

        // Map to store the last index of each character
        Map<Character, Integer> lastIndexMap = new HashMap<>();

        // Build the last index map
        for (int i = 0; i < s.length(); i++) {
            lastIndexMap.put(s.charAt(i), i);
        }

        int start = 0; // Start pointer for the partition
        int end = 0; // End pointer for the partition

        for (int i = 0; i < s.length(); i++) {
            /**
             *  NOTE !!!  below
             *
             *    1.  Update the end pointer to the maximum of current end and the last index of
             *        the current character
             */
            end = Math.max(end, lastIndexMap.get(s.charAt(i)));

            // If the current position is the end of the partition
            /**
             *  NOTE !!!  below
             *
             *    1.  If the current position is the end of the partition
             */
            if (i == end) {
                // Add the size of the partition
                res.add(i - start + 1);
                start = i + 1; // Move the start to the next character
            }
        }

        return res;
    }

    // V1-1
    // https://neetcode.io/problems/partition-labels
    // IDEA: 2 POINTERS
    public List<Integer> partitionLabels_1_1(String s) {
        Map<Character, Integer> lastIndex = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            lastIndex.put(s.charAt(i), i);
        }

        List<Integer> res = new ArrayList<>();
        int size = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            size++;
            end = Math.max(end, lastIndex.get(s.charAt(i)));

            if (i == end) {
                res.add(size);
                size = 0;
            }
        }
        return res;
    }


    // V2
    // https://leetcode.ca/2018-01-01-763-Partition-Labels/
    public List<Integer> partitionLabels_2(String s) {
        int[] last = new int[26];
        int n = s.length();
        for (int i = 0; i < n; ++i) {
            last[s.charAt(i) - 'a'] = i;
        }
        List<Integer> ans = new ArrayList<>();
        int mx = 0, j = 0;
        for (int i = 0; i < n; ++i) {
            mx = Math.max(mx, last[s.charAt(i) - 'a']);
            if (mx == i) {
                ans.add(i - j + 1);
                j = i + 1;
            }
        }
        return ans;
    }

}
