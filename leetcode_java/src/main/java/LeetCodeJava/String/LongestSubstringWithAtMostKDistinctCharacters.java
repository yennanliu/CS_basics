package LeetCodeJava.String;

// https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/description/
// https://leetcode.ca/all/340.html

import java.util.HashMap;
import java.util.Map;

/**
 * 340. Longest Substring with At Most K Distinct Characters
 * Given a string, find the length of the longest substring T that contains at most k distinct characters.
 *
 * Example 1:
 *
 * Input: s = "eceba", k = 2
 * Output: 3
 * Explanation: T is "ece" which its length is 3.
 * Example 2:
 *
 * Input: s = "aa", k = 1
 * Output: 2
 * Explanation: T is "aa" which its length is 2.
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Amazon AppDynamics Bloomberg Citadel Coupang Facebook Google Microsoft Uber
 * Problem Solution
 * 340-Longest-Substring-with-At-Most-K-Distinct-Characters
 */
public class LongestSubstringWithAtMostKDistinctCharacters {

    // V0
//    public int lengthOfLongestSubstringKDistinct(String s, int k) {
//    }

    // V1
    // https://leetcode.ca/2016-11-04-340-Longest-Substring-with-At-Most-K-Distinct-Characters/
    public int lengthOfLongestSubstringKDistinct_1(String s, int k) {
        Map<Character, Integer> cnt = new HashMap<>();
        int n = s.length();
        int ans = 0, j = 0;
        for (int i = 0; i < n; ++i) {
            char c = s.charAt(i);
            cnt.put(c, cnt.getOrDefault(c, 0) + 1);
            while (cnt.size() > k) {
                char t = s.charAt(j);
                cnt.put(t, cnt.getOrDefault(t, 0) - 1);
                if (cnt.get(t) == 0) {
                    cnt.remove(t);
                }
                ++j;
            }
            ans = Math.max(ans, i - j + 1);
        }
        return ans;
    }

    // V2

}
