package LeetCodeJava.HashTable;

// https://leetcode.com/problems/longest-substring-with-at-most-two-distinct-characters/description/

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 159. Longest Substring with At Most Two Distinct Characters
 * Medium
 * Lock: Prime
 *
 * Given a string s, return the length of the longest substring that contains
 * at most two distinct characters.
 *
 *
 * Example 1:
 *
 * Input: s = "eceba"
 * Output: 3
 * Explanation: The substring is "ece" which its length is 3.
 *
 * Example 2:
 *
 * Input: s = "ccaabbb"
 * Output: 5
 * Explanation: The substring is "aabbb" which its length is 5.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of English letters.
 *
 */
public class LongestSubstringWithAtMostTwoDistinctCharacters {

    // V0
    // IDEA: SLIDING WINDOW + HASH TABLE (char -> count inside window)
    /**
     *  grow the RIGHT edge, shrink the LEFT edge while there are > 2 distinct chars
     *
     *  NOTE !!! when a count drops to 0 the key must be REMOVED from the map,
     *           otherwise `cnt.size()` still counts it and the window never shrinks.
     *
     *  time  = O(n)
     *  space = O(1)   // the map holds at most 3 keys
     */
    public int lengthOfLongestSubstringTwoDistinct(String s) {
        Map<Character, Integer> cnt = new HashMap<>();
        int res = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            cnt.put(c, cnt.getOrDefault(c, 0) + 1);

            // too many distinct chars -> shrink from the left
            while (cnt.size() > 2) {
                char lc = s.charAt(left);
                cnt.put(lc, cnt.get(lc) - 1);
                if (cnt.get(lc) == 0) {
                    cnt.remove(lc);
                }
                left += 1;
            }

            res = Math.max(res, right - left + 1);
        }

        return res;
    }


    // V1
    // IDEA: LAST-OCCURRENCE MAP -- jump `left` instead of shrinking it
    /**
     *  Keep, for each of the (at most 3) live characters, the LAST index it was
     *  seen at. When a third character appears, the character whose last
     *  occurrence is smallest is the one to drop, and `left` jumps straight past
     *  it -- no inner while loop.
     *
     *  Each index is touched exactly once.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int lengthOfLongestSubstringTwoDistinct_1(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int left = 0;
        int res = 0;

        for (int right = 0; right < s.length(); right++) {
            lastSeen.put(s.charAt(right), right);

            if (lastSeen.size() > 2) {
                // evict the character whose last occurrence is the EARLIEST
                int evictAt = Collections.min(lastSeen.values());
                lastSeen.remove(s.charAt(evictAt));
                left = evictAt + 1;
            }

            res = Math.max(res, right - left + 1);
        }
        return res;
    }

    // V2
    // IDEA: FIXED int[128] COUNTER (no HashMap)
    /**
     *  The alphabet is ASCII letters, so a flat int[128] plus a `distinct` counter
     *  replaces the map -- no hashing, no boxing, no size() call.
     *
     *  Also makes the `at most K distinct` generalisation a one-character edit.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int lengthOfLongestSubstringTwoDistinct_2(String s) {
        final int K = 2;
        int[] cnt = new int[128];
        int distinct = 0;
        int left = 0;
        int res = 0;

        for (int right = 0; right < s.length(); right++) {
            if (cnt[s.charAt(right)]++ == 0) {
                distinct += 1;
            }
            while (distinct > K) {
                if (--cnt[s.charAt(left)] == 0) {
                    distinct -= 1;
                }
                left += 1;
            }
            res = Math.max(res, right - left + 1);
        }
        return res;
    }

    // V3
    // IDEA: BRUTE FORCE over every start index
    /**
     *  For each start, extend right while the distinct count stays <= 2.
     *
     *  O(n^2), hopeless at n = 10^5, but it is the direct reading of the statement
     *  and therefore the oracle for the two linear versions.
     *
     *  time  = O(n^2)
     *  space = O(1)
     */
    public int lengthOfLongestSubstringTwoDistinct_3(String s) {
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            int[] cnt = new int[128];
            int distinct = 0;
            for (int j = i; j < s.length(); j++) {
                if (cnt[s.charAt(j)]++ == 0) {
                    distinct += 1;
                }
                if (distinct > 2) {
                    break;
                }
                res = Math.max(res, j - i + 1);
            }
        }
        return res;
    }

}
