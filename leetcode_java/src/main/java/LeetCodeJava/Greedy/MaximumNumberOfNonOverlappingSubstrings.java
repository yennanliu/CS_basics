package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-non-overlapping-substrings/description/

import java.util.*;

/**
 * 1520. Maximum Number of Non-Overlapping Substrings
 * Hard
 *
 * Given a string s of lowercase letters, you need to find the maximum number of non-empty
 * substrings of s that meet the following conditions:
 *
 * 1. The substrings do not overlap, that is for any two substrings s[i..j] and s[x..y],
 *    either j < x or i > y is true.
 * 2. A substring that contains a certain character c must also contain all occurrences of c.
 *
 * Find the maximum number of substrings that meet the above conditions. If there are multiple
 * solutions with the same number of substrings, return the one with minimum total length.
 * It can be shown that there exists a unique solution of minimum total length.
 *
 * Notice that you can return the substrings in any order.
 *
 *
 * Example 1:
 *
 * Input: s = "adefaddaccc"
 * Output: ["e","f","ccc"]
 * Explanation: The following are all the possible substrings that meet the conditions:
 * [
 *   "adefaddaccc"
 *   "adefadda",
 *   "ef",
 *   "e",
 *   "f",
 *   "ccc",
 * ]
 * If we choose the first string, we cannot choose anything else and we'd get only 1. If we
 * choose "adefadda", we are left with "ccc" which is the only one that doesn't overlap, thus
 * obtaining 2 substrings. Notice also, that it's not optimal to choose "ef" since it can be
 * split into two. Therefore, the optimal way is to choose ["e","f","ccc"] which gives us 3
 * substrings. No other solution of the same number of substrings exist.
 *
 * Example 2:
 *
 * Input: s = "abbaccd"
 * Output: ["d","bb","cc"]
 * Explanation: Notice that while the set of substrings ["d","abba","cc"] also has length 3,
 * it's considered incorrect since it has larger total length.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s contains only lowercase English letters.
 *
 */
public class MaximumNumberOfNonOverlappingSubstrings {

    // V0
    // IDEA: MINIMAL VALID INTERVAL PER LETTER + GREEDY BY EARLIEST END (interval scheduling)
    /**
     * time = O(26 * N)
     * space = O(1) (excluding the output)
     */
    public List<String> maxNumOfSubstrings(String s) {
        int n = s.length();

        // first / last index of every letter (-1 = letter absent)
        int[] first = new int[26];
        int[] last = new int[26];
        Arrays.fill(first, -1);
        for (int i = 0; i < n; i++) {
            int letter = s.charAt(i) - 'a';
            if (first[letter] == -1) {
                first[letter] = i;
            }
            last[letter] = i;
        }

        // for each letter, grow [first, last] until it contains every occurrence of
        // every letter inside it. if some letter inside starts before `left`, no valid
        // interval starts at `left` (that one is produced from the other letter instead)
        List<int[]> intervals = new ArrayList<>();
        for (int letter = 0; letter < 26; letter++) {
            if (first[letter] == -1) {
                continue;
            }
            int left = first[letter];
            int right = last[letter];
            boolean valid = true;
            for (int i = left; i <= right; i++) {
                int inside = s.charAt(i) - 'a';
                if (first[inside] < left) {
                    valid = false;
                    break;
                }
                right = Math.max(right, last[inside]);
            }
            if (valid) {
                intervals.add(new int[] {left, right});
            }
        }

        // surviving intervals are pairwise nested or disjoint, so the classic
        // "earliest end first" greedy maximises the count, and since a nested
        // interval ends earlier it also picks the shortest ones
        intervals.sort((a, b) -> Integer.compare(a[1], b[1]));

        List<String> result = new ArrayList<>();
        int previousEnd = -1;
        for (int[] interval : intervals) {
            if (interval[0] > previousEnd) {
                result.add(s.substring(interval[0], interval[1] + 1));
                previousEnd = interval[1];
            }
        }
        return result;
    }
}
