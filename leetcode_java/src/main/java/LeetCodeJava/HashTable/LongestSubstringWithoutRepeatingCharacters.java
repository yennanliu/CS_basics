package LeetCodeJava.HashTable;

// https://leetcode.com/problems/longest-substring-without-repeating-characters/
/**
 *  3. Longest Substring Without Repeating Characters
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a string s, find the length of the longest
 * substring
 *  without repeating characters.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 * Example 2:
 *
 * Input: s = "bbbbb"
 * Output: 1
 * Explanation: The answer is "b", with the length of 1.
 * Example 3:
 *
 * Input: s = "pwwkew"
 * Output: 3
 * Explanation: The answer is "wke", with the length of 3.
 * Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
 *
 *
 * Constraints:
 *
 * 0 <= s.length <= 5 * 104
 * s consists of English letters, digits, symbols and spaces.
 *
 *
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LongestSubstringWithoutRepeatingCharacters {

    // V0
    // IDEA : HASHMAP + SLIDING WINDOW
    public int lengthOfLongestSubstring(String s) {
        // Edge case
        if (s == null || s.isEmpty()) {
            return 0;
        }

        // Map to store the last index of characters
        // (key: character, value: last index of character)
        Map<Character, Integer> map = new HashMap<>();
        int res = 0;
        int slow = 0;

        for (int fast = 0; fast < s.length(); fast++) {
            char cur = s.charAt(fast);
            if (map.containsKey(cur)) {
                // Move `slow` pointer past the last occurrence of cur
                /** NOTE !!!
                 *
                 *  instead of move slow pointer to fast idx,
                 *  we MOVE slow pointer to the `last seen same element` index + 1
                 *  (but slow idx could be`bigger` than ` map.get(cur) + 1`, so we need to get the `bigger` idx)
                 *  (e.g. Math.max(slow, map.get(cur) + 1))
                 */
                /**
                 * NOTE !!!
                 *
                 *  instead of move slow pointer to `last seen max idx`,
                 *  -> we move it to `last seen max idx` + 1 (aka map.get(k) + 1)
                 */
                slow = Math.max(slow, map.get(cur) + 1);
            }

            // Store/update the last seen index of cur
            /**
             * NOTE !!!
             *
             *  we either `add` or `update` the last seen index of cur
             */
            map.put(cur, fast);

            // Calculate max length
            res = Math.max(res, fast - slow + 1);
        }

        return res;
    }

    // V0-1
    // IDEA : HASHMAP + SLIDING WINDOW
    public int lengthOfLongestSubstring_0_1(String s) {

        /**
         *  key : element
         *  value : element count
         */
        Map<String, Integer> map = new HashMap();

        // left pointer
        int left = 0;
        // right pointer
        int right = 0;
        // final result
        int res = 0;

        /**
         * NOTE !!! sliding window (while - while)
         *
         *  https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md
         */
        while (right < s.length()) {

            String cur = String.valueOf(s.charAt(right));

            /**
             * NOTE !!!
             *        Add element to map first (do val checks, index update below)
             */
            if (map.containsKey(cur)){
                map.put(cur, map.get(cur)+1);
            }else{
                map.put(cur, 1);
            }

            /**
             *  NOTE !!! if map has element -> duplicated element
             *    -> keep remove (while loop) count from element and move left pointer to right
             *    -> on the same time, left pointer (l) is moved to left
             *    -> and map is updated (remove element count)
             */
            while (map.get(cur) > 1) {
                String l = String.valueOf(s.charAt(left));
                map.put(l, map.get(l) - 1);
                left += 1;
            }

            res = Math.max(res, right - left + 1);

            right += 1;
        }
        return res;
    }

    // V0-2
    // IDEA : SLIDING WINDOW + HASH SET
    public int lengthOfLongestSubstring_0_2(String s) {

        if (s.equals("")){
            return 0;
        }

        if (s.equals(" ")){
            return 1;
        }

        if (s.length() == 1){
            return 1;
        }

        int ans = 0;
        char[] s_array = s.toCharArray();
        for (int i = 0; i < s_array.length-1; i++){
            int j = i;
            Set<String> set = new HashSet<String>();
            while (j < s_array.length){
                String cur = String.valueOf(s_array[j]);
                if (set.contains(cur)){
                    ans = Math.max(ans, set.size());
                    break;
                }else{
                    set.add(cur);
                    ans = Math.max(ans, set.size());
                    j += 1;
                }
            }
        }

        return ans;
    }

    // V0-3
    // IDEA: HASHMAP + SLIDING WINDOW (GPT)
    public int lengthOfLongestSubstring_0_3(String s) {
        // Edge cases
        if (s == null || s.length() == 0) {
            return 0;
        }

        Map<Character, Integer> map = new HashMap<>();
        int res = 0;
        int l = 0;

        for (int r = 0; r < s.length(); r++) {
            char k = s.charAt(r);

            if (map.containsKey(k)) {
                // Move left pointer to the max of the current left and (last occurrence + 1)
                /**
                 * NOTE !!!
                 *
                 *  instead of move slow pointer to `last seen max idx`,
                 *  -> we move it to `last seen max idx` + 1 (aka map.get(k) + 1)
                 */
                l = Math.max(l, map.get(k) + 1);
            }

            map.put(k, r);  // Update the latest occurrence of the character
            res = Math.max(res, r - l + 1);
        }

        return res;
    }

    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/longest-substring-without-repeating-characters/editorial/
    public int lengthOfLongestSubstring_1(String s) {
        int n = s.length();

        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (checkRepetition(s, i, j)) {
                    res = Math.max(res, j - i + 1);
                }
            }
        }

        return res;
    }

    private boolean checkRepetition(String s, int start, int end) {
        Set<Character> chars = new HashSet<>();

        for (int i = start; i <= end; i++) {
            char c = s.charAt(i);
            if(chars.contains(c)){
                return false;
            }
            chars.add(c);
        }

        return true;
    }

    // V2
    // IDEA : SLIDING WINDOW
    // https://leetcode.com/problems/longest-substring-without-repeating-characters/editorial/
    public int lengthOfLongestSubstring_2(String s) {
        Map<Character, Integer> chars = new HashMap();

        int left = 0;
        int right = 0;
        int res = 0;

        while (right < s.length()) {
            char r = s.charAt(right);
            chars.put(r, chars.getOrDefault(r,0) + 1);

            while (chars.get(r) > 1) {
                char l = s.charAt(left);
                chars.put(l, chars.get(l) - 1);
                left++;
            }

            res = Math.max(res, right - left + 1);

            right++;
        }
        return res;
    }

}
