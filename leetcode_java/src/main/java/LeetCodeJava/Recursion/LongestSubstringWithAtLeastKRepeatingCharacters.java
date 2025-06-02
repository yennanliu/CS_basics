package LeetCodeJava.Recursion;

// https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/description/

import java.util.Arrays;

/**
 * 395. Longest Substring with At Least K Repeating Characters
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a string s and an integer k, return the length of the longest substring of s such that the frequency of each character in this substring is greater than or equal to k.
 *
 * if no such substring exists, return 0.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aaabb", k = 3
 * Output: 3
 * Explanation: The longest substring is "aaa", as 'a' is repeated 3 times.
 * Example 2:
 *
 * Input: s = "ababbc", k = 2
 * Output: 5
 * Explanation: The longest substring is "ababb", as 'a' is repeated 2 times and 'b' is repeated 3 times.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 104
 * s consists of only lowercase English letters.
 * 1 <= k <= 105
 *
 *
 */
public class LongestSubstringWithAtLeastKRepeatingCharacters {

    // V0
//    public int longestSubstring(String s, int k) {
//
//    }

    // V0-2
    // IDEA: DIVIDE AND CONQUER (gpt)
    public int longestSubstring_0_2(String s, int k) {
        return longestSubstringHelper(s, 0, s.length(), k);
    }

    private int longestSubstringHelper(String s, int start, int end, int k) {
        if (end - start < k)
            return 0;

        // Count frequency of each character in the substring
        int[] count = new int[26];
        for (int i = start; i < end; i++) {
            count[s.charAt(i) - 'a']++;
        }

        // Find split point: character with freq > 0 but less than k
        for (int i = start; i < end; i++) {
            char ch = s.charAt(i);
            if (count[ch - 'a'] > 0 && count[ch - 'a'] < k) {
                int left = longestSubstringHelper(s, start, i, k);
                int right = longestSubstringHelper(s, i + 1, end, k);
                return Math.max(left, right);
            }
        }

        // All characters in this range occur at least k times
        return end - start;
    }

    // V1-1
    // https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/editorial/
    // IDEA: BRUTE FORCE
    public int longestSubstring_1_1(String s, int k) {
        if (s == null || s.isEmpty() || k > s.length()) {
            return 0;
        }
        int[] countMap = new int[26];
        int n = s.length();
        int result = 0;
        for (int start = 0; start < n; start++) {
            // reset the count map
            Arrays.fill(countMap, 0);
            for (int end = start; end < n; end++) {
                countMap[s.charAt(end) - 'a']++;
                if (isValid(s, start, end, k, countMap)) {
                    result = Math.max(result, end - start + 1);
                }
            }
        }
        return result;
    }

    private boolean isValid(String s, int start, int end, int k, int[] countMap) {
        int countLetters = 0, countAtLeastK = 0;
        for (int freq : countMap) {
            if (freq > 0)
                countLetters++;
            if (freq >= k)
                countAtLeastK++;
        }
        return countAtLeastK == countLetters;
    }


    // V1-2
    // https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/editorial/
    // IDEA: Divide And Conquer
    public int longestSubstring_1_2(String s, int k) {
        return longestSubstringUtil(s, 0, s.length(), k);
    }

    int longestSubstringUtil(String s, int start, int end, int k) {
        if (end < k)
            return 0;
        int[] countMap = new int[26];
        // update the countMap with the count of each character
        for (int i = start; i < end; i++)
            countMap[s.charAt(i) - 'a']++;
        for (int mid = start; mid < end; mid++) {
            if (countMap[s.charAt(mid) - 'a'] >= k)
                continue;
            int midNext = mid + 1;
            while (midNext < end && countMap[s.charAt(midNext) - 'a'] < k)
                midNext++;
            return Math.max(longestSubstringUtil(s, start, mid, k),
                    longestSubstringUtil(s, midNext, end, k));
        }
        return (end - start);
    }


    // V1-3
    // https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/editorial/
    // IDEA: SLIDING WINDOW
    public int longestSubstring_1_3(String s, int k) {
        char[] str = s.toCharArray();
        int[] countMap = new int[26];
        int maxUnique = getMaxUniqueLetters(s);
        int result = 0;
        for (int currUnique = 1; currUnique <= maxUnique; currUnique++) {
            // reset countMap
            Arrays.fill(countMap, 0);
            int windowStart = 0, windowEnd = 0, idx = 0, unique = 0, countAtLeastK = 0;
            while (windowEnd < str.length) {
                // expand the sliding window
                if (unique <= currUnique) {
                    idx = str[windowEnd] - 'a';
                    if (countMap[idx] == 0)
                        unique++;
                    countMap[idx]++;
                    if (countMap[idx] == k)
                        countAtLeastK++;
                    windowEnd++;
                }
                // shrink the sliding window
                else {
                    idx = str[windowStart] - 'a';
                    if (countMap[idx] == k)
                        countAtLeastK--;
                    countMap[idx]--;
                    if (countMap[idx] == 0)
                        unique--;
                    windowStart++;
                }
                if (unique == currUnique && unique == countAtLeastK)
                    result = Math.max(windowEnd - windowStart, result);
            }
        }

        return result;
    }

    // get the maximum number of unique letters in the string s
    int getMaxUniqueLetters(String s) {
        boolean map[] = new boolean[26];
        int maxUnique = 0;
        for (int i = 0; i < s.length(); i++) {
            if (!map[s.charAt(i) - 'a']) {
                maxUnique++;
                map[s.charAt(i) - 'a'] = true;
            }
        }
        return maxUnique;
    }


    // V2

}
