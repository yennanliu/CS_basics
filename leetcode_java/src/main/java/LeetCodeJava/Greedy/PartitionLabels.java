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
    // IDEA: HASH MAP (latest idx) + array op
    // https://neetcode.io/problems/partition-labels
    public List<Integer> partitionLabels(String s) {

        List<Integer> res = new ArrayList<>();

        // edge
        if (s == null || s.isEmpty()) {
            return null;
        }
        if (s.length() == 1) {
            res.add(1);
            return res;
        }

        Map<String, Integer> map = new HashMap<>();
        String[] s_arr = s.split("");
        //System.out.println(">>> s_arr = " + s_arr);

        for (int i = 0; i < s_arr.length; i++) {
            map.put(s_arr[i], i);
        }

        //System.out.println(">>> map = " + map);

        int cur_len = 1;
        int cur_max_idx = 0;

        /**
         *  NOTE !!!
         *
         *   step 1) use map record `element latest idx`
         *   step 2) within loop, we maintain the `max latest idx` of visited elements
         *   step 3) once `max latest idx` == i, we know that `the SAME elements are all reached`
         *      -> so we can append `current sub array len` to result
         *   step 4) we repeat above steps till reach the end of input string
         *
         */
        for (int i = 0; i < s_arr.length; i++) {
            cur_max_idx = Math.max(cur_max_idx, map.get(s_arr[i]));
            if (cur_max_idx == i) {
                res.add(cur_len);
                cur_len = 1;
            } else {
                cur_len += 1;
            }
        }

        return res;
    }

    // V0-0-1
    // IDEA: HASHMAP + 2 POINTERS (fixed by gpt)
    public List<Integer> partitionLabels_0_0_1(String s) {
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

    // V0-2
    // IDEA: GREEDY + hashMap record last idx + sliding window (fixed by gpt)
    public List<Integer> partitionLabels_0_2(String s) {
        List<Integer> res = new ArrayList<>();

        if (s == null || s.length() == 0) {
            return res;
        }

        // Map each character to its last index
        Map<Character, Integer> lastIndexMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            lastIndexMap.put(s.charAt(i), i);
        }

        int l = 0;
        while (l < s.length()) {
            int end = lastIndexMap.get(s.charAt(l));
            int r = l;

            // Expand the window to include all characters in the current segment
            while (r < end) {
                end = Math.max(end, lastIndexMap.get(s.charAt(r)));
                r++;
            }

            res.add(end - l + 1);
            l = end + 1;
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
