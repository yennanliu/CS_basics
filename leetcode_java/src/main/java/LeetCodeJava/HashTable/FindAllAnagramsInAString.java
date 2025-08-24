package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-all-anagrams-in-a-string/description/

import java.util.*;

/**
 * 438. Find All Anagrams in a String
 * Solved
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * Given two strings s and p, return an array of all the start indices of p's anagrams in s. You may return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "cbaebabacd", p = "abc"
 * Output: [0,6]
 * Explanation:
 * The substring with start index = 0 is "cba", which is an anagram of "abc".
 * The substring with start index = 6 is "bac", which is an anagram of "abc".
 * Example 2:
 *
 * Input: s = "abab", p = "ab"
 * Output: [0,1,2]
 * Explanation:
 * The substring with start index = 0 is "ab", which is an anagram of "ab".
 * The substring with start index = 1 is "ba", which is an anagram of "ab".
 * The substring with start index = 2 is "ab", which is an anagram of "ab".
 *
 *
 * Constraints:
 *
 * 1 <= s.length, p.length <= 3 * 104
 * s and p consist of lowercase English letters.
 *
 */
public class FindAllAnagramsInAString {

    // V0
    // IDEA: HASHMAP + 2 POINTERS (fixed by gpt)
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        // edge
        if (s == null || p == null || s.isEmpty() || p.isEmpty() || p.length() > s.length()) {
            return res;
        }
        if (s.equals(p)) {
            res.add(0);
            return res;
        }

        // init p map
        Map<String, Integer> p_map = new HashMap<>();
        for (String x : p.split("")) {
            p_map.put(x, p_map.getOrDefault(x, 0) + 1);
        }

        String[] s_arr = s.split("");
        Map<String, Integer> s_map = new HashMap<>();

        int l = 0;
        for (int r = 0; r < s_arr.length; r++) {
            String key = s_arr[r];
            s_map.put(key, s_map.getOrDefault(key, 0) + 1);

            /**
             *  NOTE !!!
             *
             *   need `while loop` below
             *   so we can `shrink` left pointer (window)
             *   to make the sub string size equals to `p`
             *
             *   (could be `if` logic as well here)
             *   (e.g. if (r - l + 1 > p.length()) )
             */
            // shrink window if size > p.length()
            while (r - l + 1 > p.length()) {
                String leftKey = s_arr[l];
                /**
                 *  NOTE !!!
                 *
                 *   need to update s_map
                 */
                s_map.put(leftKey, s_map.get(leftKey) - 1);
                if (s_map.get(leftKey) == 0) {
                    s_map.remove(leftKey);
                }
                l++;
            }

            /**
             *  NOTE !!!
             *
             *  if same size, compare s_map, and p_map
             */
            // if same size, compare
            if (r - l + 1 == p.length() && isEqaual(p_map, s_map)) {
                res.add(l);
            }
        }

        return res;
    }

    private boolean isEqaual(Map<String, Integer> p_map, Map<String, Integer> s_map) {
        if (p_map.size() != s_map.size()) {
            return false;
        }
        for (String k : p_map.keySet()) {
            if (!s_map.containsKey(k) || !s_map.get(k).equals(p_map.get(k))) {
                return false;
            }
        }
        return true;
    }

    // V0-1
    // IDEA: sliding window (gpt)
    public List<Integer> findAnagrams_0_1(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || p == null || s.length() < p.length()) {
            return res;
        }

        int[] pCount = new int[26];
        int[] sCount = new int[26];

        // build p frequency
        for (char c : p.toCharArray()) {
            pCount[c - 'a']++;
        }

        int window = p.length();

        // sliding window
        for (int i = 0; i < s.length(); i++) {
            // add current char
            sCount[s.charAt(i) - 'a']++;

            // remove leftmost if window too big
            if (i >= window) {
                sCount[s.charAt(i - window) - 'a']--;
            }

            // check equality
            if (Arrays.equals(pCount, sCount)) {
                res.add(i - window + 1);
            }
        }

        return res;
    }

  // V0-2
  // IDEA: HASHMAP + SLIDE WINDOW (gpt)
  /**
   *  Why `slide window` is needed trick for this problem?
   *
   *
   *  Yes 👍 the sliding window (or two-pointer) is the needed trick for that group of problems like “longest substring without repeating characters”.
   *
   * Here’s why:
   * 	•	A brute force way would check all substrings → O(n²) or worse.
   * 	•	But with a sliding window, you keep a “window” [left, right] over the string/array and expand right step by step.
   * 	•	If the constraint is violated (like duplicate chars appear, or the sum is too large), you shrink from the left until it’s valid again.
   * 	•	This way each index moves at most once → O(n) total.
   *
   * That’s the exact “trick” behind those problems. The hard part is usually:
   * 	1.	What condition makes the window valid/invalid? (duplicate chars, sum > k, etc.)
   * 	2.	When to update the answer? (on every valid window, or only when shrinking).
   *
   */
  public List<Integer> findAnagrams_0_2(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || p == null || s.isEmpty() || p.isEmpty() || p.length() > s.length()) {
            return res;
        }

        // Build p_map (pattern frequency)
        Map<String, Integer> p_map = new HashMap<>();
        for (String x : p.split("")) {
            p_map.put(x, p_map.getOrDefault(x, 0) + 1);
        }

        Map<String, Integer> s_map = new HashMap<>();
        String[] s_arr = s.split("");
        int window = p.length();

        for (int i = 0; i < s_arr.length; i++) {
            String val = s_arr[i];

            // add current char to s_map
            s_map.put(val, s_map.getOrDefault(val, 0) + 1);

            // maintain sliding window size
            if (i >= window) {
                String leftChar = s_arr[i - window];
                if (s_map.get(leftChar) == 1) {
                    s_map.remove(leftChar);
                } else {
                    s_map.put(leftChar, s_map.get(leftChar) - 1);
                }
            }

            // compare maps only when window size matches
            if (i >= window - 1 && isEqual(p_map, s_map)) {
                res.add(i - window + 1);
            }
        }

        return res;
    }

    private boolean isEqual(Map<String, Integer> p_map, Map<String, Integer> s_map) {
        if (p_map.size() != s_map.size()) {
            return false;
        }
        for (String k : p_map.keySet()) {
            if (!s_map.containsKey(k) || !p_map.get(k).equals(s_map.get(k))) {
                return false;
            }
        }
        return true;
    }

    // V1
    // https://leetcode.com/problems/find-all-anagrams-in-a-string/solutions/1500039/java-tc-osp-sc-o1-sliding-window-solutio-icz6/
    // IDEA: sliding window
    public List<Integer> findAnagrams_1(String s, String p) {
        if (s == null || p == null) {
            throw new IllegalArgumentException("Input string is null");
        }

        List<Integer> result = new ArrayList<>();
        int sLen = s.length();
        int pLen = p.length();
        if (sLen * pLen == 0 || sLen < pLen) {
            return result;
        }

        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < p.length(); i++) {
            map.put(p.charAt(i), map.getOrDefault(p.charAt(i), 0) + 1);
        }

        int toBeMatched = map.size();
        int start = 0;
        int end = 0;

        while (end < sLen) {
            char eChar = s.charAt(end);
            if (map.containsKey(eChar)) {
                int count = map.get(eChar);
                if (count == 1) {
                    toBeMatched--;
                }
                map.put(eChar, count - 1);
            }
            end++;

            if (end - start > pLen) {
                char sChar = s.charAt(start);
                if (map.containsKey(sChar)) {
                    int count = map.get(sChar);
                    if (count == 0) {
                        toBeMatched++;
                    }
                    map.put(sChar, count + 1);
                }
                start++;
            }

            if (toBeMatched == 0) {
                result.add(start);
            }
        }

        return result;
    }

    // V2

}
