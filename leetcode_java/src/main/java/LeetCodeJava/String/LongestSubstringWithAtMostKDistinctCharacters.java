package LeetCodeJava.String;

// https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/description/
// https://leetcode.ca/all/340.html

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    // V0-0-1
    // IDEA: SLIDE WINDOW PATTERN + HASHMAP (fixed by gpt)
    /**
     *   slide window pattern
     *
     *   for(int r = 0; r < s.size(); r++){
     *
     *       while(isValid()){
     *          // do sth
     *           l += 1;
     *       }
     *        // do sth
     *   }
     *
     */
    public int lengthOfLongestSubstringKDistinct_0_0_1(String s, int k) {
        // edge
        if (s == null || s.isEmpty() || k == 0) {
            return 0;
        }

        int ans = 0;
        // {val: cnt}
        Map<Character, Integer> cntMap = new HashMap<>();

        int l = 0;
        for (int r = 0; r < s.length(); r++) {
            char val = s.charAt(r);
            cntMap.put(val, cntMap.getOrDefault(val, 0) + 1);

            // shrink window if too many distinct chars
            // while still `valid`, move left pointer
            while (cntMap.size() > k) {
                char leftVal = s.charAt(l);
                cntMap.put(leftVal, cntMap.get(leftVal) - 1);
                if (cntMap.get(leftVal) == 0) {
                    cntMap.remove(leftVal);
                }
                l++;
            }

            // get the max length
            ans = Math.max(ans, r - l + 1);
        }

        return ans;
    }

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
    // https://www.cnblogs.com/grandyang/p/5351347.html

    // V3
    // https://algo.monster/liteproblems/340
    public int lengthOfLongestSubstringKDistinct_3(String s, int k) {
        // Map to store character frequencies in the current window
        Map<Character, Integer> charFrequency = new HashMap<>();

        // Left pointer of the sliding window
        int leftPointer = 0;

        // Convert string to char array for easier access
        char[] characters = s.toCharArray();

        // Iterate through each character with right pointer (implicitly)
        for (char currentChar : characters) {
            // Add current character to the window and increment its frequency
            charFrequency.merge(currentChar, 1, Integer::sum);

            // If we have more than k distinct characters, shrink the window from left
            if (charFrequency.size() > k) {
                // Decrement the frequency of the character at left pointer
                char leftChar = characters[leftPointer];
                if (charFrequency.merge(leftChar, -1, Integer::sum) == 0) {
                    // Remove the character from map if its frequency becomes 0
                    charFrequency.remove(leftChar);
                }
                // Move left pointer to shrink the window
                leftPointer++;
            }
        }

        // The maximum window size is the total length minus the left pointer position
        // This works because we only move left pointer when necessary to maintain at most k distinct chars
        return characters.length - leftPointer;
    }

    // V4-1
    // IDEA: Sliding Window
    // https://walkccc.me/LeetCode/problems/340/#__tabbed_1_2
    public int lengthOfLongestSubstringKDistinct_4_1(String s, int k) {
        int ans = 0;
        int distinct = 0;
        int[] count = new int[128];

        for (int l = 0, r = 0; r < s.length(); ++r) {
            if (++count[s.charAt(r)] == 1)
                ++distinct;
            while (distinct == k + 1)
                if (--count[s.charAt(l++)] == 0)
                    --distinct;
            ans = Math.max(ans, r - l + 1);
        }

        return ans;
    }


    // V4-2
    // IDEA: ORDERED MAP
    // https://walkccc.me/LeetCode/problems/340/#__tabbed_1_2
    public int lengthOfLongestSubstringKDistinct_4_2(String s, int k) {
        if (s == null || s.length() == 0 || k == 0) {
            return 0;
        }

        int ans = 0;
        TreeMap<Integer, Character> lastSeen = new TreeMap<>(); // {last index: letter}
        Map<Character, Integer> window = new HashMap<>();       // {letter: index}

        int l = 0;
        for (int r = 0; r < s.length(); r++) {
            char inChar = s.charAt(r);

            if (window.containsKey(inChar)) {
                // remove the old position of inChar from lastSeen
                lastSeen.remove(window.get(inChar));
            }

            lastSeen.put(r, inChar);
            window.put(inChar, r);

            if (window.size() > k) {
                Map.Entry<Integer, Character> entry = lastSeen.firstEntry();
                int lastIndex = entry.getKey();
                char outChar = entry.getValue();

                lastSeen.remove(lastIndex);
                window.remove(outChar);

                l = lastIndex + 1;
            }

            ans = Math.max(ans, r - l + 1);
        }

        return ans;
    }



}
